/*	
	Copyright 2012 Jan Ove Saltvedt
	
	This file is part of KBot.

    KBot is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    KBot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with KBot.  If not, see <http://www.gnu.org/licenses/>.
	
*/



package com.kbotpro.scriptsystem.runnable;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.handlers.kbotscriptsystem.KBot2Script;
import com.kbotpro.scriptsystem.Methods;
import com.kbotpro.scriptsystem.interfaces.HTMLDescription;
import com.kbotpro.scriptsystem.interfaces.Looped;
import com.kbotpro.scriptsystem.interfaces.WorkerContainer;
import com.kbotpro.ui.ArgumentGetter;
import com.kbotpro.various.StaticStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.List;
import java.util.ArrayList;

/**
 * The basic class all scripts should extend.
 * Provides pointers to all parts of the bot and some shortcuts to a selected few methods.
 * Please refer to the Methods class for information about these methods.
 */
public abstract class Script extends Methods implements WorkerContainer {
    private ExecutorService executorService;
    private ThreadGroup threadGroup;
    private List<Worker> startUpWorkers = new ArrayList<Worker>();
    public List<Worker> allWorkers = new ArrayList<Worker>();
    private boolean stopped = false;

    protected Script() {
        threadGroup = new ThreadGroup(getClass().getSimpleName()+" Worker ThreadGroup");
        executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(threadGroup, r, "Worker@"+Script.this.getClass().getSimpleName());
            }
        });
    }

    /**
     * Creates a worker to the workerContainer but does not start it.
     * The created worker will start when the scripts starts, if you do not want this please use the
     * createWorker(Looped looped, boolean startAutomaticallyOnScriptStart)
     * method instead.
     * @param looped the loop that shall be ran.
     * @return The created worker which can be started using startWorker(Worker worker);
     */
    public Worker createWorker(Looped looped){
        return createWorker(looped, true);
    }

    /**
     * Creates a worker to the workerContainer but does not start it.
     * @param looped the loop that shall be ran.
     * @param startAutomaticallyOnScriptStart Whether the worker shall start when the workerContainer starts
     * @return The created worker which can be started using startWorker(Worker worker);
     */
    public Worker createWorker(Looped looped, boolean startAutomaticallyOnScriptStart){
        Worker worker = new Worker(this, looped);
        if(startAutomaticallyOnScriptStart){
            startUpWorkers.add(worker);
        }
        allWorkers.add(worker);
        return worker;
    }



    /**
     * Creates and starts a worker at once.
     * @param looped the loop that shall be ran.
     * @return The created worker
     */
    public Worker startWorker(Looped looped){
        Worker worker = createWorker(looped);
        startWorker(worker);
        return worker;
    }

    /**
     * Starts a worker if its not alreaddy alive.
     * @param worker
     */
    public void startWorker(Worker worker){
        if(worker.isAlive()){
            return; // Already running
        }
        worker.future = executorService.submit(worker.runnable);
    }

    /**
     * Starts all the workers
     */
    private void startAllWorkers(){
        for(Worker worker: startUpWorkers){
            startWorker(worker);
        }
    }

    private ServiceCallback.State state = ServiceCallback.State.DEAD;
    private ServiceCallback serviceCallback;

    private RunnableService service = new RunnableService() {


        /**
         * Is called before the service start to check if it can run.
         *
         * @return Returns a boolean indicating if the service can be started or not
         */
        public boolean sCanStart() {
            return canStart();
        }

        /**
         * Is called to start the service
         */
        public void sStart() {
            if(state == ServiceCallback.State.PAUSED){
                onResume();
                return;
            }
            if(!canStart()){
                return;
            }
            if (donatorsOnly() && !com.kbotpro.various.StaticStorage.userStorage.canUseCPUSaving()) {
                JOptionPane.showMessageDialog(StaticStorage.mainForm,
                    "The author of this script has chosen to only" +
                        " allow KBot donators to run this script.",
                    "Insufficient privileges",
                    JOptionPane.ERROR_MESSAGE);

                return;
            }
            if(Script.this instanceof HTMLDescription){
                final HTMLDescription htmlDescription = (HTMLDescription) Script.this;
                if(!(Script.this instanceof KBot2Script) || ((KBot2Script)Script.this).isKBot2HTMLDesc()){
                    final ArgumentGetter[] argumentGetter = {null};
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            argumentGetter[0] = new ArgumentGetter(htmlDescription, getBotEnv());
                        }
                    });
                    while(argumentGetter[0] == null){
                        sleep(100);
                    }
                    argumentGetter[0].waitFor();
                }
            }
            registerWorkers();
            onStart();
            startAllWorkers();
            state = ServiceCallback.State.ACTIVE;
            serviceCallback.setState(state, Script.this);
        }

        /**
         * Is called to pause the service
         */
        public void sPause() {
            onPause();
            state = ServiceCallback.State.PAUSED;
            serviceCallback.setState(state, Script.this);
        }

        /**
         * Is called to stop the service
         */
        public void sStop() {
            stopped = true;
            stop();

            for(Worker worker: allWorkers){
                if(worker.isAlive()){
                    worker.cancel();
                }
            }
            new Timer(10000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (threadGroup.activeCount() == 0) {
                        state = ServiceCallback.State.DEAD;
                        serviceCallback.setState(state, Script.this);
                        return;
                    }
                    try {
                        threadGroup.destroy();
                    } catch (ThreadDeath td) {
                        getLogger().logImportant("Force killed " + Script.this.toString());
                    }
                    state = ServiceCallback.State.DEAD;
                    serviceCallback.setState(state, Script.this);
                }
            });
        }

        /**
         * Is called before start to set the callback.
         * This is later used by the service to send information back.
         *
         * @param serviceCallback the callback to be set
         */
        public void setCallback(ServiceCallback serviceCallback) {
            Script.this.serviceCallback = serviceCallback;
        }

        /**
         * Gets the current state.
         *
         * @return state
         */
        public ServiceCallback.State getState() {
            return state;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p/>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run() {
            for(Worker worker: startUpWorkers){
                worker.start();
            }
        }
    };

    public final void notifyWorkerDone(final Worker worker){
        botEnv.executorService.submit(new Runnable() {
            public void run() {
                    sleep(10);
                boolean foundOneAlive = false;
                for(Worker worker2: allWorkers){
                    if(worker == worker2){
                        continue;
                    }
                    if(worker2.isAlive()){
                        foundOneAlive = true;
                        break;
                    }
                }
                if(!foundOneAlive){
                    stopped = true;
                    stop();
                    state = ServiceCallback.State.DEAD;
                    serviceCallback.setState(state, Script.this);
                }
            }
        });

    }



    /**
     * Gets the scripts name
     *
     * @return String containing name
     */
    public abstract String getName();

    /**
     * Is called before the workerContainer starts to check if it can run.
     *
     * @return Returns a boolean indicating if the service can be started or not
     */
    public boolean canStart(){
        return true;
    }

    public boolean donatorsOnly() {
        return false;
    }

    /**
     * Is called right before the run() gets called
     */
    public abstract void onStart();

    /**
     * This is called right before onStart() and you should create all the main workers in this method.
     */
    public abstract void registerWorkers();

    /**
     * Is called when the workerContainer is paused.
     */
    public void onPause(){ }

    /**
     * Is called when the workerContainer is paused.
     */
    public void onResume(){ }

    /**
     * Is called when an anti-random needs to deposit items to make space.
     * @return An int array containing items that are vital for the script to continue running. By default will not deposit items that stack.
     */
    public int[] doNotDeposit() {
        return new int[] {
                1271, 1265, 15259, 1267, 1273, 1275, 1269, //pickaxes
                1359, 1357, 1361, 1351, 6739, 1349, 1355, 1353, //hatchets
                301, 13431, 305, 303, 307, 309, 10129, 311, 11323, //fishing equipment
                946, 2347, 590, 10008, 10006, 8011, 8010, 8009, 8008, 8013, 8007, 995 //misc common items 
        };
    }
    
    /**
     * Is called to stop the workerContainer.
     * The workerContainer is than added to the cleanup queue and thread will be force killed if not stopped within 10 seconds.
     */
    public abstract void stop();

    /**
     * Used internally by the bot. (This is called by reflection)
     * Please ignore this...
     * @return
     */
    private final Service getService(){
        return service;
    }

    public boolean isStopped() {
        return stopped;
    }

    public BotEnvironment getBotEnv() {
        return botEnv;
    }
}

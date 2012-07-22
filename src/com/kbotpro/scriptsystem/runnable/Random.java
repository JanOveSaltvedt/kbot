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
import com.kbotpro.scriptsystem.Methods;
import com.kbotpro.scriptsystem.interfaces.Looped;
import com.kbotpro.scriptsystem.interfaces.WorkerContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Dec 18, 2009
 * Time: 2:46:08 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Random extends Methods implements WorkerContainer {
    private ExecutorService executorService;
    private boolean enabled = true;
    private List<Worker> startUpWorkers = new ArrayList<Worker>();
    private List<Worker> allWorkers = new ArrayList<Worker>();
    private ThreadGroup threadGroup;
    private boolean stopped = true;

    protected Random() {
        threadGroup = new ThreadGroup(getClass().getSimpleName()+" Worker ThreadGroup");
        executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(threadGroup, r, "Worker@"+Random.this.getClass().getSimpleName());
            }
        });
    }

    /**
     * Checks if this random is enabled currently.
     * This is set by the user, you should not override this.
     * @return
     */
    public final boolean isEnabled(){
        return enabled;
    }

    /**
     * Enables or disables the random from running.
     * @param enabled
     */
    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the name of the random 
     * @return
     */
    public abstract String getName();

    /**
     * Checks if this random should activate.
     * This method is called over again with ca 100 ms spacing.
     * Please don't do anything time consuming here as all the randoms shares the same thread for this.
     * @return
     */
    public abstract boolean activate();

    /**
     * Is called right before the workers are started.
     * You should register your workers here.
     * The workers connected to this random is dismissed before this random is started.
     * After this method has returned these workers will start
     * The random will be in a running state until all workers are dead. 
     */
    public abstract void onStart();

    /**
     * Creates a worker to the workerContainer but does not start it.
     * The created worker will start when the scripts starts, if you do not want this please use the
     * createWorker(Looped looped, boolean startAutomaticallyOnScriptStart)
     * method instead.
     * @param looped the loop that shall be ran.
     * @return The created worker which can be started using startWorker(Worker worker);
     */
    public final Worker createWorker(Looped looped){
        return createWorker(looped, true);
    }

    /**
     * Creates a worker to the workerContainer but does not start it.
     * @param looped the loop that shall be ran.
     * @param startAutomaticallyOnScriptStart Whether the worker shall start when the workerContainer starts
     * @return The created worker which can be started using startWorker(Worker worker);
     */
    public final Worker createWorker(Looped looped, boolean startAutomaticallyOnScriptStart){
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
    public final Worker startWorker(Looped looped){
        Worker worker = createWorker(looped);
        startWorker(worker);
        return worker;
    }

    /**
     * Starts a worker if its not alreaddy alive.
     * @param worker
     */
    public final void startWorker(Worker worker){
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

    public boolean isStopped() {
        return stopped;
    }

    public BotEnvironment getBotEnv() {
        return botEnv;
    }

    public final void notifyWorkerDone(final Worker worker){
        allWorkers.remove(worker);
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
                }
            }
        });
    }

    /**
     * Runs the random and waits for it to finish
     */
    public void runRandom() {
        stopped = false;
        startUpWorkers = new ArrayList<Worker>();
        allWorkers = new ArrayList<Worker>();
        onStart();
        startAllWorkers();
        waitForAllWorkersDone();
        stopped = true;
    }

    public boolean isRunning(){
        return !stopped;
    }

    private void waitForAllWorkersDone() {
        while(true){
            if(allWorkers.size() == 0){
                return;
            }
            int aliveCount = 0;
            for(Worker worker: allWorkers){
                if(worker == null){
                    continue;
                }
                if(worker.isAlive()){
                    aliveCount++;
                }
            }

            if(aliveCount == 0){
                return;
            }
            else{
                sleep(100);
            }

        }
    }

}

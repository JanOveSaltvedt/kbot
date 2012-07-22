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

import com.kbotpro.scriptsystem.interfaces.Looped;
import com.kbotpro.scriptsystem.interfaces.WorkerContainer;
import com.kbotpro.ui.ErrorBox;
import com.kbotpro.various.StaticStorage;
import org.apache.log4j.Logger;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Basic class to be used in scripting.
 * Each worker creates a Thread that works independendently from all the other workers (if any)
 */
public class Worker {

    /**
     * Can be used to call back to the workerContainer.
     * Used internally to manage the workers.
     */
    protected WorkerContainer workerContainer;
    protected Looped looped;
    public boolean loopReturned = true;
    /**
     * May only be called by the Script, therefor it is package local
     * @param workerContainer
     * @param looped
     */
    Worker(WorkerContainer workerContainer, Looped looped) {
        this.workerContainer = workerContainer;
        this.looped = looped;
    }

    Future future;

    /**
     * Package local as the Script needs it.
     */
    Runnable runnable = new Runnable() {
        public void run() {
            int returnedValue = 0;
            try{
                while(workerContainer != null && looped != null && !workerContainer.isStopped() && (returnedValue) >= 0){
                    loopReturned = false;
                    returnedValue = looped.loop();
                    loopReturned = true;
                    if (returnedValue < 0) {
                        break;
                    }
                    while(!(workerContainer instanceof Random) && workerContainer.getBotEnv().scriptManager.isScriptsPaused()){
                        Thread.sleep(500);
                    }
                    if(returnedValue == 0){
                        continue;
                    }
                    try {
                        Thread.sleep(returnedValue);
                    } catch (InterruptedException ignored) {
                    }
                }
            }catch(Exception e){
                final String[] message = new String[]{""};
                PrintStream printStream = new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        message[0] += (char)b;
                    }
                });
                e.printStackTrace(printStream);
                Logger.getRootLogger().error("Exception: ", e);
                new ErrorBox(StaticStorage.mainForm, "An error occurred in the script/random: "+ workerContainer.getName()+":\n\n"+ message[0]).setVisible(true);
            }
            workerContainer.notifyWorkerDone(Worker.this);
        }
    };

    /**
     * Checks if a worker has executed
     * @return boolean true if the worker is currently alive.
     */
    public boolean isAlive(){
        return future != null && !future.isDone();
    }

    /**
     * Attempts to cancel execution of this worker.
     * @return  false if the worker could not be cancelled, typically because it has already completed normally; true otherwise.
     */
    public boolean cancel(){
        if(future != null){
            workerContainer.notifyWorkerDone(this);
        }
        return future == null || future.cancel(true);
    }

    /**
     * Starts the worker if its not allready running
     */
    public void start(){
        workerContainer.startWorker(this);
    }

    /**
     * Creates a copy of this worker using the same loop.
     * @return
     */
    public Worker copy(){
        return workerContainer.createWorker(looped);
    }

    /**
     * Waits until the worker is done executing.
     */
    public void join(){
        if(future == null){
            return;
        }
        try {
            future.get();
        } catch (InterruptedException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

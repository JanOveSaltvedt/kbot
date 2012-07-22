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



package com.kbotpro.scriptsystem.input.jobs;

import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.bot.BotEnvironment;
import org.apache.log4j.Logger;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;

/**
 * Base class for all jobs
 */
public abstract class Job extends ModuleConnector {
    protected Runnable runnable;
    protected Future future;
    protected boolean cancelled = false;

    public Job(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Starts the job if its not allready started
     */
    public void start(){
        if(future != null && future.isDone()){
            return;
        }
        cancelled = false;
        if(this instanceof MouseJob){
            botEnv.mouse.addMouseJobInternally((MouseJob) this);
        }
        onStart();
        future = botEnv.executorService.submit(runnable);
    }

    /**
     * Checks if the job is still executing
     * @return
     */
    public boolean isAlive(){
        return future != null && !future.isDone();
    }

    /**
     * Cancels the keyboard job
     * @return
     */
    public boolean cancel(){
        cancelled = true;
        onCancelled();                                     
        return future == null || future.cancel(true);
    }

    /**
     * Waits until the TextInputJob is done executing.
     */
    public void join(){
        if(future == null){
            return;
        }
        try {
            future.get();
        } catch (InterruptedException e) {
            Logger.getRootLogger().error("Exception: ", e);
        } catch (ExecutionException e) {
            Logger.getRootLogger().error("Exception: ", e);
        } catch (CancellationException e) {
            //Logger.getRootLogger().error("Exception: ", e);
        }
    }

    protected abstract void onCancelled();
    protected abstract void onStart();
}

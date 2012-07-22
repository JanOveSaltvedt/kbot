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

import com.kbotpro.scriptsystem.Methods;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 8, 2009
 * Time: 5:21:00 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Debugger extends Methods implements Runnable {


    protected Thread thread;

    private Debugger self = this;

    private RunnableService service = new RunnableService() {
        private ServiceCallback.State state = ServiceCallback.State.DEAD;
        private ServiceCallback serviceCallback;

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
            thread = new Thread(this);
            onStart();
            thread.start();
            state = ServiceCallback.State.ACTIVE;
            serviceCallback.setState(state, self);
        }

        /**
         * Is called to pause the service
         */
        public void sPause() {
            pause();
            state = ServiceCallback.State.PAUSED;
            serviceCallback.setState(state, self);
        }

        /**
         * Is called to stop the service
         */
        public void sStop() {
            stop();
            new Timer(10000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!thread.isAlive()) {
                        state = ServiceCallback.State.DEAD;
                        serviceCallback.setState(state, self);
                        return;
                    }
                    try {
                        thread.stop();
                    } catch (ThreadDeath td) {
                        getLogger().logImportant("Force killed " + self.toString());
                    }
                    state = ServiceCallback.State.DEAD;
                    serviceCallback.setState(state, self);
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
            this.serviceCallback = serviceCallback;
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
            self.run();
            state = ServiceCallback.State.DEAD;
            serviceCallback.setState(state, self);
        }
    };

    public Debugger() {
    }

    /**
     * Gets the name shown in the debugs menu
     *
     * @return String containing name
     */
    public abstract String getName();

    /**
     * Is called before the debugger starts to check if it can run.
     *
     * @return Returns a boolean indicating if the service can be started or not
     */
    public abstract boolean canStart();

    /**
     * Is called right before the run() gets called
     */
    public abstract void onStart();


    /**
     * Is called to pause debugger.
     */
    public abstract void pause();


    /**
     * Is called to stop the debugger.
     * The debugger is than added to the cleanup queue and thread will be force killed if not deleted within 10 seconds.
     */
    public abstract void stop();


    /**
     * You should implement the main loop here.
     */
    public abstract void run();

    /**
     * Used internally by the bot. (This is called by reflection)
     * Please ignore this...
     * @return
     */
    private final Service getService(){
        return service;
    }
}

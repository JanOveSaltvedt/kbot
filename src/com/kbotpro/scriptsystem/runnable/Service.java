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

/**
 * Interface that should be implemented by all services in the bot.
 * This can for example be a workerContainer or a debugger.
 */
public interface Service {
    /**
     * Is called before the service start to check if it can run.
     * @return Returns a boolean indicating if the service can be started or not
     */
    public boolean sCanStart();

    /**
     * Is called to start the service
     */
    public void sStart();

    /**
     * Is called to pause the service
     */
    public void sPause();

    /**
     * Is called to stop the service
     */
    public void sStop();

    /**
     * Is called before start to set the callback.
     * This is later used by the service to send information back.
     * @param serviceCallback the callback to be set
     */
    public void setCallback(ServiceCallback serviceCallback);

    /**
     * Gets the current state.
     * @return state
     */
    public ServiceCallback.State getState();

}

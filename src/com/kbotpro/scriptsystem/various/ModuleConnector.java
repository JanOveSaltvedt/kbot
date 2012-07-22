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



package com.kbotpro.scriptsystem.various;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.hooks.Client;
import com.kbotpro.scriptsystem.Calculations;

import java.applet.Applet;

/**
 * Class containing often used methods in the KBot backend.
 * Containing pointers to the Bot and the client.
 */
public abstract class ModuleConnector {
    public BotEnvironment botEnv;

    protected ModuleConnector(BotEnvironment botEnv) {
        this.botEnv = botEnv;
    }

    /**
     * Empty constructor
     */
    protected ModuleConnector() {
    }

    protected Client getClient(){
        return botEnv.client;
    }

    protected ClassLoader getClientClassLoader(){
        return botEnv.clientClassLoader;
    }

    protected Calculations getCalculations(){
        return botEnv.calculations;
    }

    protected Log getLogger(){
        return botEnv.log;
    }

    protected Applet getApplet(){
        return botEnv.botPanel.botApplet;
    }

    /**
     * Gets random number between min and max (exclusive)
     * @param min
     * @param max
     * @return
     */
    public int random(int min, int max){
        return ((int) (Math.random() * (max - min))) + min;
    }

    /**
     * Makes the current thread sleep for given amount of milliseconds.
     *
     * 1 second = 1000 milliseconds
     * @param ms
     */
    public void sleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Makes the current thread sleep a random time between the two parameters.
     * @param min
     * @param max
     */
    public void sleep(int min, int max){
        sleep(random(min, max));
    }
    
}

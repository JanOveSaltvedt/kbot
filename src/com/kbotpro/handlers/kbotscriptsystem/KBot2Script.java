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

/*
 * Copyright © 2010 Jan Ove Saltvedt.
 * All rights reserved.
 */

package com.kbotpro.handlers.kbotscriptsystem;

import com.kbot2.handlers.eventSystem.eventListeners.PaintListener;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.events.ServerMessageListener;
import com.kbotpro.scriptsystem.interfaces.HTMLDescription;
import com.kbotpro.scriptsystem.interfaces.Looped;
import com.kbotpro.scriptsystem.runnable.Script;

import java.awt.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 17, 2010
 * Time: 5:11:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class KBot2Script extends Script implements PaintEventListener, ServerMessageListener, HTMLDescription{
    private com.kbot2.scriptable.Script script;

    public KBot2Script(com.kbot2.scriptable.Script script, BotEnvironment bot) {
        this.script = script;
        script.setBotEnv(new com.kbot2.bot.BotEnvironment(bot));
    }

    /**
     * Is called when the client receives a server message.
     *
     * @param message
     */
    public void onServerMessage(String message) {
        if(script instanceof com.kbot2.handlers.eventSystem.eventListeners.ServerMessageListener){
            ((com.kbot2.handlers.eventSystem.eventListeners.ServerMessageListener)script).onServerMessage(message);
        }
    }

    /**
     * Gets called when the client updates it graphics.
     * Please do not do anything extremely time consuming in here as it will make the fps go low.
     *
     * @param g Graphics to paint on
     */
    public void onRepaint(Graphics g) {
        if(script instanceof PaintListener){
            ((PaintListener)script).onRepaint(g);
        }
    }

    /**
     * Gets the scripts name
     *
     * @return String containing name
     */
    @Override
    public String getName() {
        return "KBot Lite Script:"+script.getName();
    }

    /**
     * Is called right before the run() gets called
     */
    @Override
    public void onStart() {
        script.onStart();
    }

    boolean running = true;
    /**
     * This is called right before onStart() and you should create all the main workers in this method.
     */
    @Override
    public void registerWorkers() {
        createWorker(new Looped() {
            public int loop() {
                if(!running){
                    return -1;
                }
                if(!script.active()){
                    return -1;
                }
                return script.loop();
            }
        });
    }

    /**
     * Is called to stop the workerContainer.
     * The workerContainer is than added to the cleanup queue and thread will be force killed if not stopped within 10 seconds.
     */
    @Override
    public void stop() {
        running = false;

        script.onStop();
    }

    /**
     * Gets the document the script should display.
     * This should be written in HTML and include forms to get input from the user.
     *
     * @return The HTML document
     */
    public String getDocument() {
        if(script instanceof com.kbot2.scriptable.HTMLDescription){
            return ((com.kbot2.scriptable.HTMLDescription)script).getDocument();
        }
        return null;
    }

    /**
     * Gets called to set the arguments in the script.
     *
     * @param args Map of all the arguments given my the HTML document.
     */
    public void setArguments(Map<String, String> args) {
        if(script instanceof com.kbot2.scriptable.HTMLDescription){
            ((com.kbot2.scriptable.HTMLDescription)script).setArguments(args);
        }
    }

    public boolean isKBot2HTMLDesc(){
        return script instanceof com.kbot2.scriptable.HTMLDescription;
    }
}

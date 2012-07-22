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



package com.kbotpro.debuggers;

import com.kbotpro.scriptsystem.runnable.Debugger;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.events.ServerMessageListener;
import com.kbotpro.scriptsystem.wrappers.Tile;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 21, 2009
 * Time: 8:03:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameDebugger extends Debugger implements PaintEventListener, ServerMessageListener {
    private boolean shallRun;

    /**
     * Gets the name shown in the debugs menu
     *
     * @return String containing name
     */
    public String getName() {
        return "Game Debugger";
    }

    /**
     * Is called before the debugger starts to check if it can run.
     *
     * @return Returns a boolean indicating if the service can be started or not
     */
    public boolean canStart() {
        return true;
    }

    /**
     * Is called right before the run() gets called
     */
    public void onStart() {
        shallRun = true;
    }

    /**
     * Is called to pause debugger.
     */
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Is called to stop the debugger.
     * The debugger is than added to the cleanup queue and thread will be force killed if not deleted within 10 seconds.
     */
    public void stop() {
        shallRun = false;
    }

    /**
     * You should implement the main loop here.
     */
    public void run() {
        while(shallRun){
            sleep(500);
        }
    }

    /**
     * Gets called when the client updates it graphics.
     * Please do not do anything extremely time consuming in here as it will make the fps go low.
     *
     * @param g Graphics to paint on
     */
    public void onRepaint(Graphics g) {
        if (shallRun) {
            g.setColor(Color.green);
            g.drawString("Camera angle: "+camera.getAngle(), 20, 60);
            g.drawString("Inventory item count: "+ inventory.getCount(), 20, 80);
            if(game.hasSelectedItem()){
                g.drawString("We have something selected.", 20, 100);
            }
            else{
                g.drawString("We don't have anything selected.", 20, 100);
            }
            g.drawString("Game state: "+game.getGameState(), 20, 120);
            final Tile destination = game.getDestination();
            g.drawString("Destination: "+ (destination == null?"N/A":destination), 20, 140);
            if (isLoggedIn()) {
                g.drawString("Current selected tab: "+game.getCurrentTab(), 20, 160);
            }
        }
    }

    /**
     * Is called when the client recieves a server message.
     *
     * @param message
     */
    public void onServerMessage(String message) {
        log.log("Server Message: "+message);
    }
}

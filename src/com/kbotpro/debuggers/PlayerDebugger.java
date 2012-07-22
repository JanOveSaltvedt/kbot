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
import com.kbotpro.scriptsystem.wrappers.Player;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import org.apache.log4j.Logger;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 9, 2009
 * Time: 4:02:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerDebugger extends Debugger implements PaintEventListener {
    private boolean shallRun;
    private Player[] cachedPlayers;

    /**
     * Gets the name shown in the debugs menu
     *
     * @return String containing name
     */
    public String getName() {
        return "Player Debugger";
    }

    /**
     * Is called before the debugger starts to check if it can run.
     *
     * @return Returns a boolean indicating if the service can be started or not
     */
    public boolean canStart() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
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
            try {
                if(!game.isLoggedIn()){
                    sleep(500);
                    return;
                }
                Player[] players = super.players.getPlayers();
                if(players != null){
                    cachedPlayers = players;
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    /**
     * Gets called when the client updates it graphics.
     * Please do not do anything extremely time consuming in here as it will make the fps go low.
     *
     * @param g Graphics to paint on
     */
    public void onRepaint(Graphics g) {
        if(shallRun) {
            if(cachedPlayers == null){
                g.setColor(Color.red);
                g.drawString("No players in cache!", 20, 60);
                return;
            }

            g.setColor(Color.green);
            g.drawString("Players in cache: "+ cachedPlayers.length, 20, 60);

            g.setColor(Color.BLUE);
            for(Player player: cachedPlayers){
                if(player == null){
                    continue;
                }
                else{
                    Point p =  player.getCenter();
                    if(p.x != -1 && p.y != -1){
                        g.fillOval(p.x-2, p.y-2, 4, 4);
                        g.drawString(" "+player.getName(), p.x, p.y);
                    }
                }
            }
        }
    }
}
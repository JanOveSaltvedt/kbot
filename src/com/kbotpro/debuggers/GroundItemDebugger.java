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

import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.fetch.BotSettings;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.scriptsystem.runnable.Debugger;
import com.kbotpro.scriptsystem.wrappers.GroundItem;
import com.kbotpro.scriptsystem.wrappers.Model;
import org.apache.log4j.Logger;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 12, 2009
 * Time: 9:02:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroundItemDebugger extends Debugger implements PaintEventListener{
    private boolean shallRun;
    private GroundItem[] storedGroundItems;
    private MouseTarget target;

    /**
     * Gets the name shown in the debugs menu
     * @return String containing name
     */
    public String getName() {
        return "Ground Item Debugger";
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
            try {
                if(!game.isLoggedIn()){
                    sleep(600);
                    continue;
                }
                //objects.getObjectsAt(3208, 3399);
                //objects.getObjectsAt(3209, 3399);
                storedGroundItems = groundItems.getItems(7);
                Thread.sleep(600);
                GroundItem closest = groundItems.getClosestItemNoID(7);
                if(closest != null){
                    target = closest.getTarget();
                }
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
        if (shallRun) {
            if(!game.isLoggedIn()){
                return;
            }
            if(storedGroundItems == null){
                g.setColor(Color.red);
                g.drawString("No items within reach!", 20, 60);
                return;
            }
            g.setColor(Color.BLUE);
            g.drawString("Items within reach: "+ storedGroundItems.length, 20, 60);
            for(GroundItem groundItem: storedGroundItems){
                if(groundItem == null){
                    continue;
                }
                if(botSettings.getBooleanSetting(BotSettings.SETTING_DRAW_WIREFRAMES)){
                    Model[] models = groundItem.getModels();
                    if(models.length > 0){
                        for(int i = 0; i< models.length; i++){
                            if(i == 0){
                                g.setColor(Color.blue);
                            }
                            else if(i == 1){
                                g.setColor(Color.yellow);
                            }
                            else if(i == 2){
                                g.setColor(Color.green);
                            }
                            models[i].drawWireframe(g);
                        }
                    }
                }

                Point p = groundItem.getScreenPos();
                if(p.x == -1 && p.y == -1){
                    continue;
                }

                g.setColor(Color.RED);
                g.fillOval(p.x-2, p.y-2, 4, 4);
                g.drawString(" "+groundItem.getID(), p.x, p.y);
            }

            if(target == null){
                return;
            }
            Point pTarget = target.get();
            g.setColor(Color.green);
            g.fillOval(pTarget.x-2, pTarget.y-2, 4, 4);
        }
    }
}
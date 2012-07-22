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
import com.kbotpro.scriptsystem.wrappers.Tile;
import com.kbotpro.scriptsystem.wrappers.Model;
import com.kbotpro.scriptsystem.Calculations;
import com.kbotpro.scriptsystem.fetch.BotSettings;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import org.apache.log4j.Logger;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 12, 2009
 * Time: 6:50:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyPlayerDebugger extends Debugger implements PaintEventListener{
    private boolean shallRun;
    private Player myPlayer;

    /**
     * Gets the name shown in the debugs menu
     *
     * @return String containing name
     */
    public String getName() {
        return "My Player Debugger";
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
        while (shallRun) {
            try {
                if(!game.isLoggedIn()){
                    sleep(500);
                    return;
                }
                Player myPlayer = players.getMyPlayer();
                if (myPlayer != null) {
                    this.myPlayer = myPlayer;
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
        if(!game.isLoggedIn()){
            return;
        }
        if(myPlayer == null){
            g.setColor(Color.red);
            g.drawString("No player found!", 20, 60);
            return;
        }

        g.setColor(Color.green);
        g.drawString("Current position: (global) "+myPlayer.getLocation(), 20, 60);
        g.drawString("Current position: (regional) "+myPlayer.getRegionalX()+", "+myPlayer.getRegionalY(), 20, 80);
        g.drawString("Current animation: "+myPlayer.getAnimation(), 20, 100);
        g.drawString("Current animation frame index): "+myPlayer.getCurrentAnimationFrameIndex(), 20, 120);
        g.drawString("Current orientation (degrees): "+String.format("%f", myPlayer.getOrientation()), 20, 140);
        g.drawString("Current orientation (RSUnits): "+myPlayer.getOrientationYAxis(), 20, 160);
        if(myPlayer.isMoving()){
            g.drawString("We are moving!", 20, 180);
        }
        else{
            g.drawString("We are not moving!", 20, 180);
        }
        if(myPlayer.isInCombat()){
            g.drawString("We are in combat ", 20, 200);
        }
        else{
            g.drawString("We are not in combat ", 20, 200);
        }
        g.drawString("Health bar: "+myPlayer.getHPPercent(), 20, 220);

        /*Model[] models = myPlayer.getModels();
        if(models.length > 0){
            for(int i = 0; i < models.length; i++){
                if(models[i] == null){
                    continue;
                }
                if(i == 0){
                    g.setColor(Color.orange);
                }
                else if(i == 1){
                    g.setColor(Color.yellow);
                }
                else{
                    g.setColor(Color.green);
                }
                models[i].drawWireframe(g);
            }
        }*/

        Model model = myPlayer.getModel();
        if(model != null && botSettings.getBooleanSetting(BotSettings.SETTING_DRAW_WIREFRAMES)){
            g.setColor(Color.orange);
            model.drawWireframe(g);
        }
        if(model != null){
            g.setColor(Color.green);
            model.drawSimpleBounds(g);
            Point m = mouse.getMousePos();
            if(model.isPointOver(m.x, m.y, true)){
                g.drawString("Mouse over player", 20, 240);    
            }
            else{
                g.setColor(Color.red);
                g.drawString("Mouse NOT over player", 20, 240);
            }
        }


        Point p = myPlayer.getCenter();
        if (p.x != -1 && p.y != -1) {
            g.setColor(Color.blue);
            g.fillOval(p.x - 2, p.y - 2, 4, 4);
            g.drawString(" " + myPlayer.getName(), p.x, p.y);

            if(myPlayer.isMoving()){
                // Draw path :)
                int height = myPlayer.getHeight() >> 1;
                Tile[] path = myPlayer.getWalkingArray();
                int baseX = getClient().getBaseX();
                int baseY = getClient().getBaseY();
                g.setColor(new Color(4, 199, 228, 150)); // Alpha cyan color ;)
                int[] linePathX = new int[path.length];
                int[] linePathY = new int[path.length];
                for(int i = 0; i < path.length; i++){
                    Tile tile = path[i];
                    Polygon polygon = new Polygon();
                    int regionalX = tile.getRegionalX(baseX);
                    int regionalY = tile.getRegionalY(baseY);

                    Calculations calculations1 = getCalculations();
                    Point point = calculations1.worldToScreen(regionalX, regionalY, height);
                    linePathX[i] = point.x;
                    linePathY[i] = point.y;
                    Point p2 = calculations1.worldToScreen(regionalX-64, regionalY-64, 0);
                    polygon.addPoint(p2.x, p2.y);
                    p2 = calculations1.worldToScreen(regionalX-64, regionalY+64, 0);
                    polygon.addPoint(p2.x, p2.y);
                    p2 = calculations1.worldToScreen(regionalX+64, regionalY+64, 0);
                    polygon.addPoint(p2.x, p2.y);
                    p2 = calculations1.worldToScreen(regionalX+64, regionalY-64, 0);
                    polygon.addPoint(p2.x, p2.y);
                    g.fillPolygon(polygon);
                }

                linePathX[0] = p.x;
                linePathY[0] = p.y;
                ((Graphics2D)g).setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.setColor(Color.red);
                g.drawPolyline(linePathX, linePathY, linePathX.length);
            }

        }
    }
}

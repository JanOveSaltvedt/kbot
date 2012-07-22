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

import com.kbotpro.scriptsystem.fetch.Objects;
import com.kbotpro.scriptsystem.runnable.Debugger;
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;
import com.kbotpro.scriptsystem.wrappers.Model;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.scriptsystem.fetch.BotSettings;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.wrappers.Player;
import com.kbotpro.scriptsystem.wrappers.Tile;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 12, 2009
 * Time: 9:02:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ObjectDebugger extends Debugger implements PaintEventListener{
    private boolean shallRun;
    private PhysicalObject[] physicalObjects;
    private PhysicalObject[] physicalDecorObjects;
    private MouseTarget target;

    /**
     * Gets the name shown in the debugs menu
     * @return String containing name
     */
    public String getName() {
        return "Object Debugger";
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
     * Get all visible tiles on screen. (When not in fixed, tiles under the minimap and under the tabs WILL show.
     *
     * @return returns a tile array of all the visible tiles
     * @author Andrew and Ryan`
     */
    public Tile[] getVisibleTiles() {
        Tile rsTileRight;
        Tile rsTileLeft;
        Tile rsTileUp;
        Tile rsTileDown;
        Player me = getMyPlayer();
        rsTileRight = me.getLocation();
        while (calculations.tileToScreen(new Tile(rsTileRight.getX() + 1, rsTileRight.getY())).getX() != -1) {
            rsTileRight = new Tile(rsTileRight.getX() + 1, rsTileRight.getY());
        }
        rsTileLeft = me.getLocation();
        while (calculations.tileToScreen(new Tile(rsTileLeft.getX() - 1, rsTileLeft.getY())).getX() != -1) {
            rsTileLeft = new Tile(rsTileLeft.getX() - 1, rsTileLeft.getY());
        }
        rsTileUp = me.getLocation();
        while (calculations.tileToScreen(new Tile(rsTileUp.getX(), rsTileUp.getY() + 1)).getY() != -1) {
            rsTileUp = new Tile(rsTileUp.getX(), rsTileUp.getY() + 1);
        }
        rsTileDown = me.getLocation();
        while (calculations.tileToScreen(new Tile(rsTileDown.getX(), rsTileDown.getY() - 1)).getY() != -1) {
            rsTileDown = new Tile(rsTileDown.getX(), rsTileDown.getY() - 1);
        }
        int max_X = rsTileRight.getX(), max_Y = rsTileUp.getY(), min_X = rsTileLeft.getX(), min_Y = rsTileDown.getY();
        ArrayList<Tile> list = new ArrayList<Tile>();
        for (int x = min_X; x < max_X; x++) {
            for (int y = min_Y; y < max_Y; y++) {
                Tile t = new Tile(x, y);
                if (calculations.onScreen(calculations.tileToScreen(t))) {
                    list.add(t);
                }

            }
        }
        if (list.size() > 0) {
            return list.toArray(new Tile[list.size()-1]);
        }
        return null;
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
                Tile[] visibleTiles = getVisibleTiles();
                physicalObjects = objects.getObjects(8);

                if(botSettings.getBooleanSetting(BotSettings.SETTING_INCLUDE_DECORATIVES)){
                    ArrayList<PhysicalObject> list = new ArrayList<PhysicalObject>();
                    for(Tile t : visibleTiles) {
                        PhysicalObject[] decorObjects = objects.getObjectsAtWithMask(t, Objects.MASK_DECORATIONS);
                        if(decorObjects.length > 0) {
                            list.add(decorObjects[0]);
                        }
                    }
                    if (list.size() > 0)
                        physicalDecorObjects = list.toArray(new PhysicalObject[list.size()-1]);
                }
                else{
                    physicalDecorObjects = new PhysicalObject[0];
                }

                Thread.sleep(600);
                PhysicalObject closest = objects.getClosestObjectNoID(7);
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
        try {
        if (shallRun) {
            if(!game.isLoggedIn()){
                return;
            }
            if(physicalObjects == null){
                g.setColor(Color.red);
                g.drawString("No objects within reach!", 20, 60);
                return;
            }
            if(physicalDecorObjects == null){
                g.setColor(Color.BLUE);
                g.drawString("No Decorative objects within reach!", 20, 70);
                return;
            }
            g.setColor(Color.red);
            g.drawString("Objects within reach: "+physicalObjects.length, 20, 60);
            g.setColor(Color.BLUE);
            int reach = physicalDecorObjects.length - physicalObjects.length;
            g.drawString("Decorative Objects within reach: " + reach, 20, 70);
            for(PhysicalObject physicalObject: physicalObjects){
                if(physicalObject == null){
                    continue;
                }
                g.setColor(Color.BLUE);
                if(botSettings.getBooleanSetting(BotSettings.SETTING_DRAW_WIREFRAMES)){
                    Model model = physicalObject.getModel(false);
                    if(model != null ){
                        model.drawWireframe(g);
                    }
                }

                Point p = physicalObject.getScreenPos();
                if(p.x == -1 && p.y == -1){
                    continue;
                }

                g.setColor(Color.RED);
                g.fillOval(p.x-2, p.y-2, 4, 4);
                g.drawString(" "+physicalObject.getID(), p.x, p.y);
            }
            for(PhysicalObject physicalDecorObject: physicalDecorObjects){
                if(physicalDecorObject == null){
                    continue;
                }
                g.setColor(Color.BLUE);
                if(botSettings.getBooleanSetting(BotSettings.SETTING_DRAW_WIREFRAMES)){
                    Model model = physicalDecorObject.getModel(true);
                    if(model != null ){
                        model.drawWireframe(g);
                    }
                }

                Point p = physicalDecorObject.getScreenPos();
                if(p.x == -1 && p.y == -1){
                    continue;
                }

                g.setColor(Color.BLUE);
                g.fillOval(p.x-2, p.y-2, 4, 4);
                g.drawString(" "+physicalDecorObject.getID(), p.x, p.y);
            }

            if(target == null){
                return;
            }
            Point pTarget = target.get();
            g.setColor(Color.green);
            g.fillOval(pTarget.x-2, pTarget.y-2, 4, 4);
        }
        } catch (Exception e) {}
    }
}
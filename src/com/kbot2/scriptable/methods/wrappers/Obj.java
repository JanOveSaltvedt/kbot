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

package com.kbot2.scriptable.methods.wrappers;

import com.kbot2.bot.BotEnvironment;
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;

import java.awt.*;

import static com.kbot2.scriptable.methods.Calculations.onScreen;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 4:02:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Obj extends Wrapper {
    private PhysicalObject physicalObject;

    public Obj(BotEnvironment botEnv, PhysicalObject physicalObject) {
        super(botEnv);
        this.physicalObject = physicalObject;
    }

    public int getID(){
        return physicalObject.getID();
    }

    public Point getScreenPos(){
        return physicalObject.getScreenPos();
    }

    public int getHeight(){
        return physicalObject.getHeight();
    }

    /**
     * Advanced method.
     * Basics:
     * 1 Tile contains 128*128 untits. In other words a tile is a plane with 128 coordinates on each axis.
     * This method uses the objects base tile. (South, west tile of the object, or the location if it only covers 1 tile.)
     *
     * @param xOff xOff is the amount of units to go east from the south east part of the base tile. Putting 64 here will be in the middle of the x-axis on the base tile.
     * 128 would be the start of the tile to the east of the base tile.
     * @param zOff yOff is the amount of units to go north from the south east part of the base tile. Putting 64 here will be in the middle of the y-axis on the base tile.
     * 128 would be the start of the tile to the north of the base tile.
     * @param yOff is the height. Same scale as a unit to 128 here would represent a tile if you flipped it to stand upwards.
     * @return the point on screen or -1, -1 if not on Screen
     */
    public Point getScreenPos(int xOff, int zOff, int yOff){
        Tile tile = getLocation();
        return getCalculations().worldToScreen((tile.getX() - getClient().getBaseX()) * 512+xOff, (tile.getY()
                - getClient().getBaseY()) * 512+yOff, zOff);
    }

    public Tile getLocation() {
        return new Tile(physicalObject.getLocation());
    }

    public boolean doAction(String actionContains, int xOff, int yOff, int zOff) {
        Point screenLoc = getScreenPos(xOff, yOff, zOff);
        if (!onScreen(screenLoc)) {
            botEnv.camera.setAngleTo(getLocation());
        }
        screenLoc = getScreenPos(xOff, yOff, zOff);
        if (!onScreen(screenLoc))
            return false;
        botEnv.methods.moveMouse(screenLoc, 2, 2);
        getMethods().sleep(30, 100);
        return botEnv.methods.atMenu(actionContains);
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p/>
     * The <code>toString</code> method for class <code>Object</code>
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Obj[id="+getID()+"@"+getLocation().toString()+"]";
    }


    public boolean doAction(String actionContains) {
        Point screenLoc = getScreenPos();
        if (!onScreen(screenLoc)) {
            botEnv.camera.setAngleTo(getLocation());
        }
        screenLoc = getScreenPos();
        if (!onScreen(screenLoc))
            return false;
        botEnv.methods.moveMouse(screenLoc, 2, 2);
        getMethods().sleep(30, 100);
        return botEnv.methods.atMenu(actionContains);
    }

}

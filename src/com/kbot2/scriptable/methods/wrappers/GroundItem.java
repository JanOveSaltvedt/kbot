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
import com.kbot2.scriptable.methods.interfaces.WorldObject;

import java.awt.*;

import static com.kbot2.scriptable.methods.Calculations.onScreen;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 7:05:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroundItem extends Wrapper implements WorldObject {
    private com.kbotpro.scriptsystem.wrappers.GroundItem groundItem;

    public GroundItem(BotEnvironment botEnv, com.kbotpro.scriptsystem.wrappers.GroundItem groundItem){
        super(botEnv);
        this.groundItem = groundItem;
    }

    public int getID(){
        return groundItem.getID();
    }

    public int getStackSize(){
        return groundItem.getStackSize();
    }

    public Tile getLocation() {
        return new Tile(groundItem.getLocation());
    }

    public Point getScreenPos(){
        return groundItem.getScreenPos();
    }

    public boolean doAction(String actionContains){
        Point screenLoc = getScreenPos();
        if(!onScreen(screenLoc)){
            botEnv.camera.setAngleTo(getLocation());
        }
        screenLoc = getScreenPos();
        if(!onScreen(screenLoc))
            return false;
        botEnv.methods.moveMouse(screenLoc, 2, 2);
        getMethods().sleep(30, 100);
        return botEnv.methods.atMenu(actionContains);
    }
}

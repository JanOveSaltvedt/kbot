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

package com.kbot2.scriptable.methods.data;

import com.kbot2.bot.BotEnvironment;
import com.kbot2.scriptable.methods.wrappers.GroundItem;
import com.kbot2.scriptable.methods.wrappers.Tile;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 7:05:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroundItems extends Data {
    public GroundItems(BotEnvironment botEnv) {
        super(botEnv);
    }

    public GroundItem[] getItemsAt(int x, int y){
        com.kbotpro.scriptsystem.wrappers.GroundItem[] groundItems = botEnv.proBotEnvironment.groundItems.getItemsAt(x, y);
        if(groundItems.length == 0){
            return new GroundItem[0];
        }
        GroundItem[] out = new GroundItem[groundItems.length];
        for(int i = 0; i < out.length; i++){
            out[i] = new GroundItem(botEnv, groundItems[i]);
        }
        return out;
    }

    public GroundItem[] getItemsAt(Tile tile){
        return getItemsAt(tile.getX(), tile.getY());
    }

    public GroundItem getClosestItem(int range, int... ids) {
        com.kbotpro.scriptsystem.wrappers.GroundItem groundItem = botEnv.proBotEnvironment.groundItems.getClosestItem(range, ids);
        if(groundItem == null){
            return null;
        }
        return new GroundItem(botEnv, groundItem);
    }

    public GroundItem[] getItems(int range, int... ids) {
        com.kbotpro.scriptsystem.wrappers.GroundItem[] groundItems = botEnv.proBotEnvironment.groundItems.getItems(range, ids);
        if(groundItems.length == 0){
            return new GroundItem[0];
        }
        GroundItem[] out = new GroundItem[groundItems.length];
        for(int i = 0; i < out.length; i++){
            out[i] = new GroundItem(botEnv, groundItems[i]);
        }
        return out;

    }

    public GroundItem[] getAllItems(int range) {
        com.kbotpro.scriptsystem.wrappers.GroundItem[] groundItems = botEnv.proBotEnvironment.groundItems.getItems(range);
        if(groundItems.length == 0){
            return new GroundItem[0];
        }
        GroundItem[] out = new GroundItem[groundItems.length];
        for(int i = 0; i < out.length; i++){
            out[i] = new GroundItem(botEnv, groundItems[i]);
        }
        return out;
    }
}

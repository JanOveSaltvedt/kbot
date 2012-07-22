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
import com.kbot2.scriptable.methods.wrappers.Obj;
import com.kbot2.scriptable.methods.wrappers.Tile;
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 6:44:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Objects extends Data{
    public Objects(BotEnvironment botEnv) {
        super(botEnv);
    }

    public Obj getObjectAt(int x, int y) {
        PhysicalObject[] at = botEnv.proBotEnvironment.objects.getObjectsAt(x, y);
        if(at.length == 0){
            return null;
        }
        return new Obj(botEnv, at[0]);
    }

    public Obj getObjectAt(Tile tile) {
        return getObjectAt(tile.getX(), tile.getY());
    }

    public Obj getClosestObject(int range, int... ids) {
        PhysicalObject o = botEnv.proBotEnvironment.objects.getClosestObject(range, ids);
        if(o == null){
            return null;
        }
        return new Obj(botEnv, o);
    }

    public Obj getClosestObjectNoID(int range) {
        PhysicalObject o = botEnv.proBotEnvironment.objects.getClosestObjectNoID(range);
        if(o == null){
            return null;
        }
        return new Obj(botEnv, o);
    }

    public Obj[] getObjects(int range, int... ids) {
        PhysicalObject[] at = botEnv.proBotEnvironment.objects.getObjects(range, ids);
        if(at.length == 0){
            return new Obj[0];
        }
        Obj[] out = new Obj[at.length];
        for(int i = 0; i < out.length; i++){
            out[i] = new Obj(botEnv, at[i]);
        }
        return out;
    }

    public Obj[] getObjects(int range) {
        PhysicalObject[] at = botEnv.proBotEnvironment.objects.getObjects(range);
        if(at.length == 0){
            return new Obj[0];
        }
        Obj[] out = new Obj[at.length];
        for(int i = 0; i < out.length; i++){
            out[i] = new Obj(botEnv, at[i]);
        }
        return out;
    }
}

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

package com.kbotpro.randoms;

import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.Interface;

/**
 * Created by IntelliJ IDEA.
 * User: Joshua
 * Date: Apr 6, 2010
 * Time: 8:14:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorldMapCloser extends Random {
    public final static int WORLD_MAP_INTERFACE_ID = 755;

    Interface worldMap;

    @Override
    public String getName() {
        return "World Map Closer";
    }

    @Override
    public boolean activate() {
        worldMap = interfaces.getInterface(WORLD_MAP_INTERFACE_ID);
        return worldMap != null && worldMap.getComponent(0) != null && worldMap.getComponent(0).isVisible();
    }

    @Override
    public void onStart() {
        KTimer kTimer = new KTimer(20000);
        while (!kTimer.isDone() && activate()) {
            if (worldMap.getComponent(47).doClick()) {
                sleep(2000, 3000);
                continue;
            }
            sleep(500, 600);
        }
    }
}

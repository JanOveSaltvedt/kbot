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

import com.kbotpro.scriptsystem.fetch.Objects;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;
import com.kbotpro.scriptsystem.wrappers.Tile;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alecquaquarucci
 * Date: Jan 25, 2010
 * Time: 9:38:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class LostAndFound extends Random {

    int[] levers = { 8994, 8995, 8996, 8997 };
    int[][] oddOut = { { 169121, 101473, 101474, 1, 236774, 2, 202951, 67648, 169123, 33827, 67652, 169124, 67654, 4, 101479, 135301, 67651, 33829, 135296, 6, 33824, 33831, 101477, 135298 },
            { 135172, 101443, 33793, 33889, 67778, 32, 169061, 135236, 34017, 135332, 202982, 67682, 33953, 192, 64, 67714, 169093, 101603, 168997, 101539, 67586, 236743, 128, 101411 },
            { 235751, 1024, 99427, 32801, 4096, 131204, 203974, 68674, 37921, 69698, 167077, 65602, 65602, 133252, 168101, 136324, 35873, 100451, 2048, 71746, 165029, 103523 },
            { 198722, 68707, 32768, 235718, 164897, 65536, 69764, 4228, 35939, 167011, 204007, 38053, 1057, 100418, 168068, 103589, 131072, 196608, 230433, 99361, 133186, 2114, 136357, 232547 } }; /*
                                                                                                                                                                                                    * For
                                                                                                                                                                                                    * getSettingArray()[531]
                                                                                                                                                                                                    */
    PhysicalObject lever;

    public synchronized void onStart() {
        int currentSetting = getClient().getSettingsArray()[531];
        int index = 2;
        KTimer timeout = new KTimer(600000);
        while (!botEnv.randomManager.scriptStopped && !timeout.isDone() && activate()) {
            if (game.hasSelectedItem()) {
                menu.atMenu("Cancel");
            }
            IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
            if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
                leftValuables[0].doClick();
                sleep(900, 1200);
                continue;
            }
            FIND: for (int i = 0; i < levers.length; i++) {
                for (int j = 0; j < oddOut[i].length; j++) {
                    if (oddOut[i][j] == currentSetting) {
                        index = i;
                        break FIND;
                    }
                    sleep(10);
                }
                sleep(100);
            }
            lever = getClosestObj(levers[index]);
            if (lever != null) {
                if (!lever.onScreen()) {
                    camera.setAngleTo(lever.getLocation());
                    walking.walkToMM(lever.getLocation());
                }
                if (lever.doAction("Operate")) {
                    sleep(1000, 2000);
                    while(getMyPlayer().isMoving()) sleep(100);
                    sleep(4000, 5000);
                    while(game.getGameState() == 10)
                        sleep(100);
                    interfaces.clickContinue();
                }
                lever = null;
            }
        }
        if (timeout.isDone()) {
            botEnv.scriptManager.stopAllScripts();
        }
    }

    private PhysicalObject getClosestObj(int... IDS) {

        PhysicalObject temp = null;
        double dist = 999;
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Tile t = new Tile(x + getClient().getBaseX(), y + getClient().getBaseY());
                PhysicalObject[] objs = objects.getObjectsAt(t.getX(), t.getY(), Objects.MASK_OBJECT2|Objects.MASK_BOUNDARY|Objects.MASK_OBJECT5|Objects.MASK_DECORATIONS|Objects.MASK_INTERACTIVE);
                if(objs.length == 0) return null;
                for(PhysicalObject obj : objs) {
                    for(int id : IDS) {
                        if (obj != null && obj.getID() == id && getMyPlayer().getLocation().distanceTo(obj.getLocation()) < dist) {
                            dist = getMyPlayer().getLocation().distanceTo(obj.getLocation());
                            temp = obj;
                        }
                    }
                }
            }
        }

        return temp;

    }

    public boolean activate() {
        return isLoggedIn() && getClosestObj(levers) != null;
    }

    public String getName() {
        return "Lost and Found";
    }

}

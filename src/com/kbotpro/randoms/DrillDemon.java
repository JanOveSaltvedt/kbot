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

import com.kbotpro.scriptsystem.fetch.Game;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.*;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Scott
 * Date: Jan 18, 2010
 * Time: 12:36:28 PM
 */
public class DrillDemon extends Random {

    NPC demon;

    int[] mats = { 10076, 10077, 10078, 10079 };
    int[] settings = { 2257, 794, 668, 1697, 1802, 1809, 675, 1305, 1676, 1249, 787, 2131, 738, 724, 1228, 2201, 1634, 2138, 1123, 1116, 1620, 2250, 2187, 1291 };
    String[][] orders = { { "Jog", "Situp", "Pushup", "Starjump" }, { "Situp", "Pushup", "Starjump", "Jog" }, { "Starjump", "Pushup", "Situp", "Jog" }, { "Jog", "Starjump", "Situp", "Pushup" }, { "Situp", "Jog", "Starjump", "Pushup" }, { "Jog", "Situp", "Starjump", "Pushup" },
            { "Pushup", "Starjump", "Situp", "Jog" }, { "Jog", "Pushup", "Starjump", "Situp" }, { "Starjump", "Jog", "Situp", "Pushup" }, { "Jog", "Starjump", "Pushup", "Situp" }, { "Pushup", "Situp", "Starjump", "Jog" }, { "Pushup", "Situp", "Jog", "Starjump" },
            { "Situp", "Starjump", "Pushup", "Jog" }, { "Starjump", "Situp", "Pushup", "Jog" }, { "Starjump", "Jog", "Pushup", "Situp" }, { "Jog", "Pushup", "Situp", "Starjump" }, { "Situp", "Starjump", "Jog", "Pushup" }, { "Situp", "Pushup", "Jog", "Starjump" },
            { "Pushup", "Starjump", "Jog", "Situp" }, { "Starjump", "Pushup", "Jog", "Situp" }, { "Starjump", "Situp", "Jog", "Pushup" }, { "Situp", "Jog", "Pushup", "Starjump" }, { "Pushup", "Jog", "Situp", "Starjump" }, { "Pushup", "Jog", "Starjump", "Situp" } };
    String action = null;

    public String getName() {
        return "Drill Demon";
    }

    public PhysicalObject getOtherPhysicalObject(int id) {
        for (PhysicalObject po : objects.getObjects(30)) {
            if (po.getID() == id)
                return po;
        }
        return null;
    }

    public synchronized void onStart() {
        KTimer timeout = new KTimer(600000);
        while (!botEnv.randomManager.scriptStopped && activate() && !timeout.isDone()) {
            if (game.hasSelectedItem()) {
                menu.atMenu("Cancel");
            }
            demon = npcs.getClosest(15, 2790);
            if(!activate()) break;
            IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
            if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
                leftValuables[0].doClick();
                sleep(900, 1200);
                continue;
            }
            if (game.hasSelectedItem()) {
                mouse.moveMouse(inventory.getItems()[0].getCenter());
                sleep(10, 50);
                mouse.clickMouse(false);
                sleep(10, 50);
                botEnv.menu.atMenu("Cancel");
                sleep(1000, 2000);
            }
            if(interfaces.canContinue()) {
                IComponent[] ir = interfaces.getInterfaces("Follow Da");
                if(ir.length > 0) {
                    demon.doAction("Talk");
                    sleep(3000);
                    continue;
                }
            }
            if(interfaces.canContinue()) {
                IComponent[] is = interfaces.getInterfaces("you actually did");
                interfaces.clickContinue();
                if(is.length > 0) {
                    sleep(1000);
                    while(game.getGameState() == 25) sleep(1000);
                    return;
                }
            }
            action = getAction();
            if (action == null) {
                NPC d = npcs.getClosest(30, "Damien");
                if(d != null) {
                    d.doAction("Talk");
                    while(getMyPlayer().isMoving()) sleep(100);
                }
            } else {
                camera.setCompass('n');
                camera.setAltitude(true);
                int setting = botEnv.client.getSettingsArray()[531];
                int index = 0;
                LOOP: for (int i = 0; i < settings.length; i++) {
                    if (settings[i] == setting) {
                        for (int j = 0; j < orders[i].length; j++) {
                            if (orders[i][j].equals(action)) {
                                index = j;
                                break LOOP;
                            }
                        }
                    }
                }
                log("Clicking Mat " + index);
                PhysicalObject po = getClosestObj(mats[index]);
                if (po != null) {
                    if (!po.onScreen()) {
                        camera.setAngleTo(po.getLocation());
                    }
                    po.doAction("Use");
                    action = null;
                    sleep(1000);
                    while(getMyPlayer().isMoving()) sleep(100);
                    sleep(1000);
                    while(getMyPlayer().getAnimation() != -1) sleep(1000);
                    sleep(1000, 1100);
                }
            }
            sleep(1500, 2000);
        }
        if (timeout.isDone()) {
            botEnv.scriptManager.stopAllScripts();
        }
    }

    public boolean activate() {
        return (isLoggedIn() && getClosestObj(mats) != null);
    }

    public String getAction() {
        Interface f = interfaces.getInterface(148);
        if(f != null && f.isValid()) {
            IComponent d = f.getComponent(0);
            return new String[] { "Pushup", "Jog", "Situp", "Starjump" }[d.getElementID() - 10946];
        }
        return null;
    }

    private PhysicalObject getClosestObj(int... IDS) {

        PhysicalObject temp = null;
        double dist = 999;
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Tile t = new Tile(x + getClient().getBaseX(), y + getClient().getBaseY());
                PhysicalObject[] objs = objects.getObjectsAt(t.getX(), t.getY(), objects.MASK_OBJECT2|objects.MASK_BOUNDARY|objects.MASK_OBJECT5|objects.MASK_DECORATIONS|objects.MASK_INTERACTIVE);
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
}

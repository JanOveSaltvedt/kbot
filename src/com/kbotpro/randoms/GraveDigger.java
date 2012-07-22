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
import com.kbotpro.scriptsystem.runnable.Script;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.*;
import org.apache.commons.lang.ArrayUtils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Conversion from iBot - ampZz ( CAUSE I DONT GIVE A F :D )
 */

public class GraveDigger extends Random {

    private PhysicalObject curGrave;
    private PhysicalObject curStone;

    private int id;
    private int idx;


    private boolean finished;
    private boolean tookAllCoffins;

    private final int INSTRUCTION_INTERFACE = 220;
    private final int CLOSE_CHILD_ID = 16;
    private final int DEPOSIT_BOX = 12731;
    private final int GRAVESTONE_INTERFACE_ID = 143;
    private final int[] GRAVESTONE_CONTAINED_ITEM_IDS = new int[] {7614, //Woodcutting
            7615, //Cooking
            7616, //Mining
            7617, //Farming
            7618 //Crafting
    };
    private final int COFFIN_INTERFACE_ID = 141;
    private final int[][] COFFIN_CONTAINED_ITEM_IDS = new int[][] {{7603, 7612, 7605}, //Woodcutting
            {7601, 7600, 7604}, //Cooking
            {7606, 7597, 7607}, //Mining
            {7609, 7602, 7610}, //Farming
            {7599, 7613, 7608} //Crafring
    };

    private final int[] COFFIN_IDS = new int[] {7587, 7588, 7589, 7590, 7591};

    private final int[] FILLED_GRAVE_IDS = new int[] {12721, 12722,
            12723, 12724, 12725};
    private final int[] EMPTY_GRAVE_IDS = new int[]{12726, 12727,
            12728, 12729, 12730};
    private final int[] GRAVESTONE_IDS = new int[] {12716, 12717,
            12718, 12719, 12720};
    private final int MAUSOLEUM_ID = 12731;
    private final int LEO_ID = 3508;

    private PhysicalObject mausoleum;

    /**
     * Checks if the random can run
     *
     * @return true if the run() should be called.
     */
    public boolean activate() {
        return (mausoleum = objects.getClosestObject(20, MAUSOLEUM_ID)) != null && isLoggedIn();
    }

    /**
     * Gets the index from the coffin interface
     *
     * @return The index for the arrays
     */
    private int getCoffinIndex() {
        Interface groupIface = interfaces.getInterface(COFFIN_INTERFACE_ID);
        if(groupIface == null || !groupIface.isValid())
            return -1;
        for(IComponent childIface : groupIface.getComponents())
            for(int i = 0; i < COFFIN_CONTAINED_ITEM_IDS.length; i++)
                if(ArrayUtils.contains(COFFIN_CONTAINED_ITEM_IDS[i], childIface.getElementID()))
                    return i;
        return -1;
    }

    /**
     * Gets the index from the grave interface
     *
     * @return The index for the Gravestone and Coffin containedItemIDs array
     */
    private int getGravestoneIndex() {
        Interface groupIface = interfaces.getInterface(GRAVESTONE_INTERFACE_ID);
        if(groupIface == null || !groupIface.isValid())
            return -1;
        for(IComponent childIface : groupIface.getComponents())
            for(int i = 0; i < GRAVESTONE_CONTAINED_ITEM_IDS.length; i++)
                if(GRAVESTONE_CONTAINED_ITEM_IDS[i] == childIface.getElementID())
                    return i;
        return -1;
    }


    public IComponent getIComponent(String s) {
        Interface[] is = interfaces.getInterfaces();
        for(Interface i : is) {

            for(IComponent j : i.getComponents()) {
                if(j != null && j.isValid() && j.getText().contains(s))
                    return j;
            }

        }
        return null;
    }


    /**
     * Gets called if canRun() returns true.
     */
    public synchronized void onStart() {
        finished = false;
        tookAllCoffins = false;
        id = -1;
        idx = -1;
        KTimer timeout = new KTimer(600000);
        main:	while(!botEnv.randomManager.scriptStopped && isLoggedIn() && !timeout.isDone() &&
                (mausoleum = objects.getClosestObject(20, MAUSOLEUM_ID)) != null) {
            IComponent iface;
            if(getMyPlayer().isMoving() || getMyPlayer().getAnimation() != -1) {
                sleep(50, 100);
                continue;
            }
            else if(interfaces.clickContinue()) {
                sleep(900, 1200);
                continue;
            }
            IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
            if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
                leftValuables[0].doClick();
                sleep(900, 1200);
                continue;
            } else if((iface = getIComponent("Yes, ")) != null && iface.isValid() && iface.isVisible()) {
                iface.doClick();
                sleep(500, 800);
                continue;
            } else if((iface = interfaces.getComponent(INSTRUCTION_INTERFACE, CLOSE_CHILD_ID)) != null && iface.isVisible()) {
                iface.doClick();
                sleep(500, 800);
                continue;
            }
            if (game.getCurrentTab() != Game.TAB_INVENTORY && !interfaces.interfaceExists(11)) {
                game.openTab(Game.TAB_INVENTORY);
                sleep(random(800,1200));
                continue;
            }
            if((inventory.getCount() - inventory.getCount(false, COFFIN_IDS)) > 23 || interfaces.interfaceExists(11)) {
                if (!interfaces.interfaceExists(11)) {
                    PhysicalObject o = objects.getClosestObject(30, DEPOSIT_BOX);
                    if (o != null) {
                        if (getDistanceTo(o.getLocation()) > 5 || !o.onScreen()) {
                            walking.walkToMM(o.getLocation());
                        }
                        camera.setAngle(camera.getAngleTo(o.getLocation()));
                        o.doAction("Deposit");
                        sleep(random(800,1200));
                        continue;
                    }
                }
                else {
                    IComponent[] deposit = interfaces.getComponent(11, 17).getChildren();
                    int depositedCount = 0;
                    for (IComponent i : deposit) {
                        if (depositedCount >= 5) break;
                        if (i.getElementID() != -1 && !getAllDoNoDeposit().contains(i.getElementID()) &&
                                i.getElementStackSize() == 1) {
                            if (i.doAction("Deposit-1")) {
                                depositedCount++;
                            }
                        }
                    }
                    if (depositedCount == 0) { interfaces.getComponent(11, 18).doClick(); }
                    interfaces.getComponent(11, 15).doClick();
                    sleep(1000, 2000);
                    continue;

                }
            }
            if(finished) {
                NPC leo = npcs.getClosest(20, LEO_ID);
                if(leo.onScreen()) {
                    if (game.hasSelectedItem()) {
                        menu.atMenu("Cancel");
                    }
                    leo.doAction("talk");
                }
                else
                    walking.walkToMM(leo.getLocation());
            } else if(tookAllCoffins) {
                // Dunno if this bit will work! curGrave.getUID() == objects.getObjectAt(curGrave.getLocation().getUID()))
                if(curGrave != null && curGrave.getID() == objects.getObjectsAt(curGrave.getLocation())[0].getID()) {
                    if(idx != -1) { //We know the grave's index somethingy...
                        if(id != -1) { //We know the coffin id, lets use it on the grave
                            if(curGrave.onScreen()) {
                                inventory.atItem(id, "Use");
                                sleep(50, 100);
                                if (curGrave.doAction("Use")) {
                                    waitForAnimation(827);
                                }
                            } else {
                                walking.walkToMM(curGrave.getLocation());
                            }
                        } else { //We don't know the coffin id, lets find it out
                            for(Item coffin : inventory.getItems()) {
                                if(ArrayUtils.contains(COFFIN_IDS, coffin.getID())) {
                                    inventory.atItem(coffin.getID(), "Check");
                                    sleep(1500, 3000);
                                    if(idx == getCoffinIndex()) {
                                        id = coffin.getID();
                                        while(interfaces.getComponent(COFFIN_INTERFACE_ID, 12) != null &&
                                                !clickExit(interfaces.getComponent(COFFIN_INTERFACE_ID, 12))) {
                                            sleep(800, 1200);
                                        }
                                        continue main;
                                    }
                                }
                            }
                        }
                    } else { //We don't know the grave's index somethingy, lets find it out
                        int graveIdx = ArrayUtils.indexOf(EMPTY_GRAVE_IDS, curGrave.getID());
                        if(curStone != null && curStone.getID() == objects.getClosestObject(20, GRAVESTONE_IDS[graveIdx]).getID()) {
                            if(getGravestoneIndex() != -1) {
                                idx = getGravestoneIndex();
                                while (interfaces.getComponent(GRAVESTONE_INTERFACE_ID, 3) != null &&
                                        clickExit(interfaces.getComponent(GRAVESTONE_INTERFACE_ID, 3))) {
                                    //clickExit(interfaces.getComponent(GRAVESTONE_INTERFACE_ID, 3));
                                    sleep(800, 1200);
                                }
                            } else {
                                if(curStone.onScreen()) {
                                    if (game.hasSelectedItem()) {
                                        menu.atMenu("Cancel");
                                    }
                                    //curStone.doAction("Read");
                                    mouse.moveMouse(curStone.getScreenPos());
                                    menu.atMenu("Read");
                                }
                                else
                                    walking.walkToMM(curStone.getLocation());
                            }
                        } else {
                            curStone = objects.getClosestObject(20, GRAVESTONE_IDS[graveIdx]);
                            sleep(50, 100);
                            continue main;
                        }
                    }
                } else {
                    id = -1;
                    idx = -1;
                    curGrave = objects.getClosestObject(20, EMPTY_GRAVE_IDS);
                    if(curGrave == null)
                        finished = true;
                    sleep(50, 100);
                    continue main;
                }
            } else {
                //PhysicalObject obj = objects.getClosestObject(20, FILLED_GRAVE_IDS);
                PhysicalObject obj = getClosestObj(FILLED_GRAVE_IDS);

                if(obj != null) {
                    if(obj.onScreen()) {
                        //obj.doAction("Take-coffin");
                        mouse.moveMouse(obj.getScreenPos());
                        sleep(10, 50);
                        if (botEnv.menu.atMenu("Take-coffin")) {
                            waitForAnimation(827);
                        }
                    }
                    else
                        walking.walkToMM(obj.getLocation());
                } else {
                    tookAllCoffins = true;
                }
            }
            sleep(1000, 2000);
        }
        sleep(1000,2000);
        while (game.getGameState() == 25)
            sleep(500, 1000);
        if (interfaces.canContinue()) {
            interfaces.clickContinue();
            sleep(1000, 2000);
        }
        if (timeout.isDone()) {
            botEnv.scriptManager.stopAllScripts();
        }
    }

    /**
     * Gets the randoms name. Should be somewhat descriptive.
     *
     * @return
     */
    public String getName() {
        return "GraveDigger";
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
    ArrayList<Integer> getAllDoNoDeposit() {
        ArrayList<Integer> items = new ArrayList<Integer>();
        for (Script s : botEnv.scriptManager.runningScripts) {
            for (int i : s.doNotDeposit()) {
                items.add(i);
            }
        }
        return items;
    }
    boolean clickExit(IComponent i) {
        IComponent xpCounter = interfaces.getComponent(746, 221);
        if (!game.isFixedMode() && i.getBounds().intersects(xpCounter.getBounds())) {
            Rectangle r = xpCounter.getBounds();
            r.x -= 2;
            r.height += 2;
            r = i.getBounds().intersection(r);
            Point p = getSomeFuckingPoint(i.getBounds());
            while (r.contains(p) && !i.getBounds().contains(p)) {
                p = getSomeFuckingPoint(i.getBounds());
            }
            mouse.moveMouse(p);
            return menu.atMenu("Close");
        } else {
            return i.doAction("Close");
        }

    }
    Point getSomeFuckingPoint(Rectangle r) {
        return new Point(r.x + random(0, r.width), r.y + random(0, r.height));
    }
    void waitForAnimation(int id) {
        for (int i = 0; i < random(4000, 5000); i+=50) {
            if (getMyPlayer().getAnimation() == id) break;
            sleep(50);
        }
    }
}

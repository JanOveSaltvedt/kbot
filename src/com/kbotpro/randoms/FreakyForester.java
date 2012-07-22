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
import com.kbotpro.scriptsystem.fetch.tabs.Equipment;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.runnable.Script;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.*;

import java.util.ArrayList;

/**
 * Created by alowaniank [KBot2]
 * - Not sure if works completely :S Needs to be tested
 */

public class FreakyForester extends Random {

    private int tailAmt;
    private boolean finished;

    private final int STUMP_ID = 5582;
    private final int PORTAL_ID = 8972;
    private final int FREAKY_FORESTER_ID = 2458;
    private final int DEPOSIT_BOX = 32931;
    private final int[] PHEASANTS_IDS = {2459, 2460, 2461, 2462};
    private final int[] RAW_PHEASANTS_IDS = {6177, 6178, 6179, 6180};
    private final int[] WEAPON_IDS = { 4827, 11235, 10284, 4212, 10280, 10282, 839, 859, 851, 845, 6724,
            847, 855, 15241, 9183, 13081, 9174, 837, 9177, 4734, 4938, 9181, 9185, 9179, 10156, 8880 };
    private PhysicalObject stump;
    private NPC freakyForester;

    @Override
    public boolean activate() {
        if(!isLoggedIn()) return false;

        return (stump = objects.getClosestObject(20, STUMP_ID)) != null;
    }

    @Override
    public String getName() {
        return "Freaky Forester";
    }

    @Override
    public synchronized void onStart() {
        KTimer timeout = new KTimer(600000);
        tailAmt = -1;
        finished = false;
        int equippedWeapon = -15;
        boolean checkWeapon = equipment.isItemEquipped(WEAPON_IDS);
        while(!botEnv.randomManager.scriptStopped && (stump = objects.getClosestObject(15, STUMP_ID)) != null &&
                (freakyForester = npcs.getClosest(15, FREAKY_FORESTER_ID)) != null && !timeout.isDone()) {
            sleep(1000, 2000);
            IComponent[] iface;
            if(!activate()) break;
            if(game.getCurrentTab() != Game.TAB_INVENTORY && !interfaces.interfaceExists(11)) {
                game.openTab(Game.TAB_INVENTORY);
            }
            if(getMyPlayer().getAnimation() != -1 || getMyPlayer().isMoving())
                continue;
            IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
            if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
                leftValuables[0].doClick();
                sleep(900, 1200);
                continue;
            }
            if((inventory.getCount() - inventory.getCount(false, RAW_PHEASANTS_IDS)) > 26 || interfaces.interfaceExists(11)) {
                if (!interfaces.interfaceExists(11)) {
                    PhysicalObject o = objects.getClosestObject(30, DEPOSIT_BOX);
                    if (o != null) {
                        if (getDistanceTo(o.getLocation()) > 5 || !o.onScreen()) {
                            walking.walkToMM(o.getLocation());
                            camera.setAngle(camera.getAngleTo(o.getLocation()));
                            sleep(random(600,900));
                            continue;
                        }
                        o.doAction("Deposit");
                        sleep(random(800,1200));
                        continue;
                    }
                }
                else {
                    IComponent[] deposit = interfaces.getComponent(11, 17).getChildren();
                    int depositedCount = 0;
                    for (IComponent i : deposit) {
                        if (depositedCount >= 1) break;
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
                }
            }
            if (checkWeapon) {
                equippedWeapon = equipment.getEquippedItemID(Equipment.Slots.WEAPON);
                IComponent weapon = equipment.getSlotIComponent(Equipment.Slots.WEAPON);
                checkWeapon = !weapon.doClick();
                continue;
            }
            if((iface = interfaces.getInterfaces("-tail")).length > 0) {
                tailAmt = convertTailAmt(iface);
                interfaces.clickContinue();
            } else if((iface = interfaces.getInterfaces("portal")).length > 0) {
                finished = true;
                interfaces.clickContinue();
            } else if(interfaces.clickContinue()) {
                continue;
            } else if(finished) {
                if (equippedWeapon > 0) {
                    Item[] item = inventory.getItems(equippedWeapon);
                    if (item.length > 0 && !item[0].doClick()) {
                        sleep(600,1000);
                        continue;
                    }
                }
                PhysicalObject portal;
                if((portal = objects.getClosestObject(20, PORTAL_ID)) != null) {
                    if(portal.onScreen()) {
                        portal.doAction("Enter");
                    } else {
                        walking.walkToMM(portal.getLocation());
                    }
                }
            } else {
                if(tailAmt < 0) {
                    if(freakyForester != null) {
                        if (game.hasSelectedItem()) {
                            menu.atMenu("Cancel");
                        }
                        if(freakyForester.onScreen())
                            freakyForester.doAction("Talk");
                        else
                            walking.walkToMM(freakyForester.getLocation());
                    }
                } else if(inventory.contains(RAW_PHEASANTS_IDS)) {
                    if(freakyForester != null)
                        if(freakyForester.onScreen())
                            freakyForester.doAction("Talk");
                        else
                            walking.walkToMM(freakyForester.getLocation());
                } else {
                    GroundItem rawPheasant;
                    if((rawPheasant = groundItems.getClosestItem(20, RAW_PHEASANTS_IDS)) != null) {
                        if(rawPheasant.onScreen())
                            rawPheasant.doAction("Take");
                        else
                            walking.walkToMM(rawPheasant.getLocation());
                    } else {
                        NPC pheasant;
                        if((pheasant = npcs.getClosest(20, PHEASANTS_IDS[tailAmt - 1])) != null) {
                            if(pheasant.onScreen())
                                pheasant.doAction("Attack");
                            else
                                walking.walkToMM(pheasant.getLocation());
                        }
                    }
                }
            }
        }
        if (timeout.isDone()) {
            botEnv.scriptManager.stopAllScripts();
        }
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

    /**
     * Small method to convert text amount to number
     *
     * @param a
     * @return the tail amount
     */
    private int convertTailAmt(IComponent[] a) {
        for(IComponent i : a) {
            String amt = i.getText();
            if(amt.contains("one-"))
                return 1;
            else if(amt.contains("two-"))
                return 2;
            else if(amt.contains("three-"))
                return 3;
            else if(amt.contains("four-"))
                return 4;
        }
        return -1;
    }

}

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

import com.kbotpro.scriptsystem.input.internal.mouse.EventFactory;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Interface;
import com.kbotpro.scriptsystem.wrappers.NPC;

import java.awt.*;
import java.awt.event.MouseEvent;

public class BeeKeeper extends Random {

    NPC beeKeeper;
    int modelIterator;
    private static final int BEE_KEEPER_ID = 8649;
    private static final int INTERFACE_GROUP_ID = 420;
    private static final int[] MODEL_IDS = {16036, 16025, 16022, 16034};  //Top to Bottom
    private static final int[] LEFT_INTERFACES = {12, 13, 14, 15};
    private static final int[] RIGHT_INTERFACES = {16, 17, 18, 19};
    private static final int[] MODEL_INTERFACES = {25, 22, 23, 21};

    /**
     * Checks if the random can run
     *
     * @return true if the run() should be called.
     */
    public boolean activate() {
        return isLoggedIn() && (beeKeeper = npcs.getClosest(20, BEE_KEEPER_ID)) != null &&
                objects.getClosestObject(20, 16168) != null;
    }

    /**
     * Hard to explain method because of weird things Jagex does with their randoms
     * @param modelID
     * @return
     */
    private int getIndexFromModel(int modelID) {
        IComponent iface;
        for(int i = 0; i < 4; i++) {
            iface = interfaces.getComponent(420, MODEL_INTERFACES[i]);
            if(iface != null && iface.isValid())
                if(iface.getModelID() == modelID)
                    return i;
        }
        return -1;
    }

    /**
     * Gets called if canRun() returns true.
     */
    public synchronized void onStart() {
        KTimer timeout = new KTimer(600000);
        modelIterator = 0;
        while(!botEnv.randomManager.scriptStopped && activate() && !timeout.isDone()) {
            beeKeeper = npcs.getClosest(20, BEE_KEEPER_ID);
            if(getMyPlayer().isMoving()) {
                continue;
            }
            if(interfaces.canContinue()) {
                interfaces.clickContinue();
                sleep(600, 900);
                continue;
            }
            Interface mainIface = interfaces.getInterface(INTERFACE_GROUP_ID);
            if(mainIface != null && mainIface.isValid()) {
                int leftIdx = getIndexFromModel(MODEL_IDS[modelIterator]);
                if(leftIdx != -1) {
                    IComponent leftIface, rightIface;
                    leftIface = interfaces.getComponent(420, LEFT_INTERFACES[leftIdx]);
                    rightIface = interfaces.getComponent(420, RIGHT_INTERFACES[modelIterator]);
                    Point start, end;
                    start = leftIface.getCenter();
                    end = rightIface.getCenter();
                    //mouse.moveMouse(start.x,  start.y);
                    //clickAndDrag(end);
                    //mouse.dragMouse(end);
                    mouse.clickAndDragMouse(start, end);
                } else if(modelIterator > 2) {
                    IComponent iface = interfaces.getComponent(420, 39);
                    iface.doClick();
                } else
                    modelIterator++;
            } else {
                IComponent[] iface;
                if((iface = interfaces.getInterfaces("Yeah,")).length > 0)
                    iface[0].doClick();
                else {
                    if(beeKeeper.onScreen()) {
                        if (game.hasSelectedItem()) {
                            menu.atMenu("Cancel");
                        }
                        beeKeeper.doAction("talk");
                    } else {
                        camera.setAngleTo(beeKeeper.getLocation());
                        camera.setAltitude(false);
                    }
                }
            }
            sleep(500, 800);
        }
        sleep(1000, 2000);
        while(game.getGameState() == 25)
            sleep(500, 1000);
        if(interfaces.canContinue()){
            interfaces.clickContinue();
            sleep(1000,2000);
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
        return "Bee-Keeper";
    }

}

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

package com.kbotpro.randoms;

import com.kbotpro.scriptsystem.interfaces.Looped;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.runnable.Script;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;
/**
 * Created by endoskeleton.
 */
public class Pinball extends Random {
    private static final int[] PILLARS = { 15000, 15002, 15004, 15006, 15008 };
    private static final int EXIT_CAVE = 15010;

    private int getScore() {
        if (!interfaces.componentExists(263, 1)) {
            return -1;
        }
        String s = interfaces.getComponent(263, 1).getText();
        return Integer.parseInt(s.split(": ")[1]);
    }

    private PhysicalObject getPillar() {
        return objects.getClosestObject(25, PILLARS);
    }

    private void waitForAnimation() {
        int ms = random(2000,3000);
        for (int i = 0; i < ms; i += 20) {
            sleep(20);
            if (getMyPlayer().getAnimation() != -1) {
                break;
            }
        }
    }

    public synchronized int loop() {
        if (!activate()) {
            return -1;
        }
        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }
        if (getMyPlayer().isMoving() || getMyPlayer().getAnimation() != -1) {
            return random(300,500);
        }
        if (interfaces.canContinue()) {
            interfaces.clickContinue();
            return random(500,800);
        }
        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
            return random(900, 1200);
        }
        if (getScore() > 9) {
            camera.setAngle(random(140,190));
            camera.setAltitude(false);
            PhysicalObject exit = objects.getClosestObject(25, EXIT_CAVE);
            if (exit != null) {
                exit.doAction("Exit");
                return random(300,500);
            }
        }
        PhysicalObject pillar = getPillar();
        if (pillar != null && calculations.isInGameArea(calculations.tileToScreen(pillar.getLocation()))) {
            /*mouse.moveMouse(calculations.tileToScreen(pillar.getLocation()));
            mouse.clickMouse(false);
            if (menu.isOpen()) {
                int tagCount = 0;
                for (String s : menu.getMenuItems()) {
                    if (s.contains("Tag")) {
                        tagCount++;
                    }
                }
                if (tagCount == 1 && menu.contains("Tag") && menu.atMenu("Tag")) {
                    waitForAnimation();
                    return random(300,600);
                } else {
                    mouse.moveMouseRandomly(random(100,200));
                }
            } */
            if (pillar.doAction("Tag")) {
                waitForAnimation();
                return random(300,600);
            } else {
                mouse.moveMouseRandomly(random(200,400));
            }
        }
        return 1000;
    }

    public boolean activate() {
        return isLoggedIn() && npcs.getClosest(25, 3913) != null;
    }

    public synchronized void onStart() {
        KTimer timeout = new KTimer(600000);
        while(!botEnv.randomManager.scriptStopped && activate() && !timeout.isDone()) {
            int i = loop();
            if (i <= 0) {
                break;
            }
            sleep(i);
        }
        if (timeout.isDone()) {
            botEnv.scriptManager.stopAllScripts();
        }
    }

    public String getName() {
        return "Pinball";
    }


}
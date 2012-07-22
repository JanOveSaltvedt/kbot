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

import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.interfaces.Looped;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.runnable.Script;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.*;

import java.awt.*;

public class Certer extends Random {
    private final int[] MODEL_IDS = { 2807, 8828, 8829, 8832, 8833, 8834, 8835, 8836, 8837 };
    private final String[] ITEM_NAMES = { "bowl", "battleaxe", "fish", "shield", "helmet", "ring", "shears", "sword", "spade" };
    private final String[] CERTERS = { "Miles", "Giles", "Niles" };
    private final int EXIT_PORTAL = 11368;
    private final int CERTER_INTERFACE = 184;

    private String targetName;
    private boolean finished;

    public String getName() {
        return "Certer";
    }

    @Override
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
    public boolean activate() {
        return npcs.getClosest(20, CERTERS) != null && objects.getClosestObject(20, 42354) != null;
    }

    public synchronized int loop() {
        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }
        if (!activate()) {
            return -1;
        }

        if (interfaces.componentExists(241, 4)) {
            String s = interfaces.getComponent(241, 4).getText();
            if (s.contains("Ahem")) {
                finished = false;
            }
            if (s.contains("You can go now") || s.contains("Correct.")) {
                finished = true;
            }
        }

        IComponent[] done = interfaces.getInterfaces("Thank you, you may leave now");
        if (done.length > 0) {
            finished = true;
        }

        if (interfaces.canContinue()) {
            interfaces.clickContinue();
            return random(1000,1200);
        }
        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
            return random(900, 1200);
        }
        if (getMyPlayer().isMoving() || getMyPlayer().getAnimation() != -1) {
            return random(600,800);
        }
        if (finished) {
            PhysicalObject exit = objects.getClosestObject(20, EXIT_PORTAL);
            if (exit != null) {
                if (!exit.onScreen()) {
                    walking.walkToMM(exit.getLocation());
                }
                exit.doAction("Enter");
                return random(600,900);
            }
        }
        Interface parent = interfaces.getInterface(CERTER_INTERFACE);
        if (parent == null && targetName == null || parent == null && !finished) {
            NPC npc = npcs.getClosest(15, CERTERS);
            if (npc != null) {
                if (!npc.onScreen()) {
                    walking.walkToMM(npc.getLocation());
                }
                npc.doAction("Talk-to");
            }
            return random(600,900);
        }

        if (targetName == null) {
            for (int i = 0; i < MODEL_IDS.length; i++) {
                if (MODEL_IDS[i] == parent.getComponent(8).getChildren()[3].getModelID()) {
                    targetName = ITEM_NAMES[i];
                }
            }
        }

        if (targetName != null) {
            if (parent != null) {
                for (IComponent i : parent.getComponent(8).getChildren()) {
                    if (i.getText() != null && i.getText().contains(targetName)) {
                        i.doClick();
                    }
                }
            }
        }

        return random(500,1000);
    }
}

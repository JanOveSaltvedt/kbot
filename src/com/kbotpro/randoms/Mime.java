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

import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.NPC;

/**
 * Created by endoskeleton.
 */
public class Mime extends Random {
    private final static int MIME_ID = 1056;
    private final static int MIME_INTERFACE = 372;
    private final static int DANCE_INTERFACE = 188;

    int animationID = -1;

    public synchronized int loop() {

        if (!activate()) {
            return -1;
        }

        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }

        if (interfaces.canContinue()) {
            interfaces.clickContinue();
            return random(500,900);
        }

        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
            return random(900, 1200);
        }

        if (interfaces.interfaceExists(MIME_INTERFACE) &&
                interfaces.getComponent(MIME_INTERFACE, 3).getText().contains("Watch")) {
            NPC mime = npcs.getClosest(15, MIME_ID);
            if (mime != null && mime.getAnimation() != -1 && mime.getAnimation() != 858) {
                animationID = mime.getAnimation();
            }
        }

        if (interfaces.interfaceExists(DANCE_INTERFACE)) {
            String s = "unknown";
            switch (animationID) {
                case 857:
                    s = "Think";
                    break;
                case 860:
                    s = "Cry";
                    break;
                case 861:
                    s = "Laugh";
                    break;
                case 866:
                    s = "Dance";
                    break;
                case 1128:
                    s = "Glass Wall";
                    break;
                case 1129:
                    s = "Lean";
                    break;
                case 1130:
                    s = "Rope";
                    break;
                case 1131:
                    s = "Glass Box";
                    break;
                default:
            }
            for (IComponent i : interfaces.getInterface(DANCE_INTERFACE).getComponents()) {
                if (i.getText().contains(s)) {
                    sleep(300,600);
                    i.doClick();
                }
            }
            animationID = -1;
        }
        return random(100,300);
    }

    public String getName() {
        return "Mime";
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

    public boolean activate() {
        return (isLoggedIn() && npcs.getClosest(15, MIME_ID) != null);
    }
}
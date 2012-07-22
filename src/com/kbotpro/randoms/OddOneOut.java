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
import com.kbotpro.scriptsystem.wrappers.Interface;
/**
 * Created by endoskeleton.
 */
public class OddOneOut extends Random {
    private final static int QUIZ_MASTER_ID = 2477;
    private final static int QUIZ_INTERFACE = 191;
    public final static int[] Fish = { 1715, 1414, 1753, 1383 };
    public final static int[] Jewelry = { 640, 1062, 793, 856 };
    public final static int[] Weapons = { 1510, 1218, 1331, 1996, 1073, 819, 1610 };
    public final static int[] Farming = { 908, 1280, 1032, 732 };

    enum TYPE { FISH, JEWELRY, WEAPON, FARMING, UNKNOWN }

    IComponent target = null;

    public String getName() {
        return "OddOneOut";
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
        return (isLoggedIn() && npcs.getClosest(15, QUIZ_MASTER_ID) != null);
    }

    public synchronized int loop() {

        if (!activate()){
            return -1;
        }

        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }

        if (clickContinue() && !interfaces.interfaceExists(QUIZ_INTERFACE)) {
            return random(800,1000);
        }

        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
            return random(900, 1200);
        }
        
        if (interfaces.interfaceExists(QUIZ_INTERFACE)) {
            QuizItem[] items = new QuizItem[3];
            for (int i = 0; i < 3; i++) {
                IComponent c = interfaces.getComponent(QUIZ_INTERFACE, i+6);
                if (contains(Fish, c.getModelZoom())) {
                    items[i] = new QuizItem(interfaces.getComponent(QUIZ_INTERFACE, i+3), TYPE.FISH);
                } else if (contains(Jewelry, c.getModelZoom())) {
                    items[i] = new QuizItem(interfaces.getComponent(QUIZ_INTERFACE, i+3), TYPE.JEWELRY);
                } else if (contains(Weapons, c.getModelZoom())) {
                    items[i] = new QuizItem(interfaces.getComponent(QUIZ_INTERFACE, i+3), TYPE.WEAPON);
                } else if (contains(Farming, c.getModelZoom())) {
                    items[i] = new QuizItem(interfaces.getComponent(QUIZ_INTERFACE, i+3), TYPE.FARMING);
                } else {
                    items[i] = new QuizItem(interfaces.getComponent(QUIZ_INTERFACE, i+3), TYPE.UNKNOWN);
                }
            }
            if (items[0].sameType(items[1])) {
                target = items[2].getComponent();
            } else if (items[0].sameType(items[2])) {
                target = items[1].getComponent();
            } else if (items[1].sameType(items[2])) {
                target = items[0].getComponent();
            } else {
                target = items[random(0,2)].getComponent();
            }
            if (target != null) {
                target.doClick();
                return random(800,1200);
            }
        }

        return random(300,500);
    }



    IComponent getContinueInterface() {
        for (Interface i : interfaces.getInterfaces()) {
            for (IComponent c : i.getComponents()) {
                if (c.isVisible() && c.getText().toLowerCase().contains("continue")) {
                    return c;
                }
            }
        }
        return null;
    }
    boolean contains(int[] array, int i) {
        for (int p : array) {
            if (p == i) {
                return true;
            }
        }
        return false;
    }
    boolean clickContinue() {
        IComponent i = getContinueInterface();
        return (i != null && i.doClick());

    }

    class QuizItem {
        IComponent i;
        TYPE t;

        QuizItem(IComponent ic, TYPE type) {
            this.i = ic;
            this.t = type;
        }

        TYPE getType() {
            return t;
        }

        IComponent getComponent() {
            return i;
        }

        boolean sameType(QuizItem i) {
            return (this.getType().equals(i.getType()));
        }
    }
}
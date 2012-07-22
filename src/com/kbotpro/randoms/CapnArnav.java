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
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;

/**
 * Created by endoskeleton.
 */
public class CapnArnav extends Random {
    private final static int[] ARNAV_CHEST = {42337, 42338};
    private final static int ARNAV_ID = 2308;
    private final static int EXIT_PORTAL = 11369;
    private final static int[][] INTERFACE_SOLVE_IDS = {
            {7, 14, 21},    //BOWL
            {5, 12, 19},    //RING
            {6, 13, 20},    //COIN
            {8, 15, 22}     //BAR
    };
    private final static int[][] ARROWS = {
            {2, 3},
            {9, 10},
            {16, 17}
    };

    enum STATE { OPEN_CHEST, SOLVE, TALK, EXIT }



    public String getName() {
        return "Capn Arnav";
    }

    public boolean activate() {
        return isLoggedIn() && (objects.getClosestObject(15, ARNAV_CHEST) != null && npcs.getClosest(15, ARNAV_ID) != null);
    }

    private STATE getState() {
        if (objects.getClosestObject(15, ARNAV_CHEST[1]) != null) {
            return STATE.EXIT;
        } else if (interfaces.canContinue() || interfaces.interfaceExists(228)) {
            return STATE.TALK;
        } else if (!interfaces.interfaceExists(185)) {
            return STATE.OPEN_CHEST;
        } else {
            return STATE.SOLVE;
        }
    }


    private int loop() {

        if (!activate()) {
            return -1;
        }
        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
            return random(900, 1200);
        }
        if (getMyPlayer().isMoving()) {
            return random(1000, 2000);
        }
        switch (getState()) {
            case EXIT:
                PhysicalObject portal = objects.getClosestObject(15, EXIT_PORTAL);
                if (portal != null) {
                    if (!portal.onScreen()) {
                        camera.setAngleTo(portal.getLocation());
                    }
                    if (portal.doAction("Enter")) {
                        sleep(random(1000, 1300));
                    }
                }
                break;

            case OPEN_CHEST:
                PhysicalObject chest = objects.getClosestObject(15, ARNAV_CHEST);
                if (chest != null) {
                    if (game.hasSelectedItem()) {
                        menu.atMenu("Cancel");
                    }
                    if (chest.doAction("Open")) {
                        sleep(random(1000, 1300));
                    }
                }
                break;

            case TALK:
                if (interfaces.canContinue()) {
                    interfaces.clickContinue();
                }
                IComponent[] okay = interfaces.getInterfaces("Okay, I'm ready to try");
                if (okay != null && okay.length > 0 && okay[0].isVisible()) {
                    okay[0].doClick();
                }
                return random(1400, 1700);

            case SOLVE:
                Interface solver = interfaces.getInterface(185);
                if (solver != null) {

                    String s = solver.getComponent(32).getText();
                    if (s.contains("Bowl")) {
                        index = 0;
                    } else if (s.contains("Ring")) {
                        index = 1;
                    } else if (s.contains("Coin")) {
                        index = 2;
                    } else if (s.contains("Bar")) {
                        index = 3;
                    }

                    IComponent container = solver.getComponent(23);
                    for (int i = 0; i < 3; i++) {
                        int rand = random(0,100);
                        if (rand < 50) {
                            rand = 0;
                        } else if (rand >= 50) {
                            rand = 1;
                        }
                        IComponent target = solver.getComponent(INTERFACE_SOLVE_IDS[index][i]);
                        IComponent arrow = solver.getComponent(ARROWS[i][rand]);
                        while(container.isValid() &&container.isVisible() && target.isValid() &&
                                target.isVisible() && !container.getBounds().contains(target.getCenter()) &&
                                arrow.isValid() && arrow.isVisible()) {
                            arrow.doClick();
                            sleep(random(500,700));
                        }
                    }

                    if (solved()) {
                        solver.getComponent(28).doClick();
                        return random(600,900);
                    }

                }
        }
        return random(500,800);
    }

    private boolean solved() {
        if (index == -1) {
            return false;
        } if (interfaces.interfaceExists(185)) {
            Interface solver = interfaces.getInterface(185);
            IComponent container = solver.getComponent(23);
            return (container.getBounds().contains(solver.getComponent(INTERFACE_SOLVE_IDS[index][0]).getCenter())
                    && container.getBounds().contains(solver.getComponent(INTERFACE_SOLVE_IDS[index][1]).getCenter())
                    && container.getBounds().contains(solver.getComponent(INTERFACE_SOLVE_IDS[index][2]).getCenter()));
        }
        return false;
    }
    private int index = -1;

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

}
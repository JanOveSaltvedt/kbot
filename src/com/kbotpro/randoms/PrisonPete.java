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

import com.kbotpro.scriptsystem.fetch.Game;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.runnable.Script;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by endoskeleton.
 */
public class PrisonPete extends Random {
    private final int PRISON_PETE_ID = 3118;
    private final int[] BALLOON_ANIMALS = { 3122, 3121, 3119, 3120 };
    private final int LEVER_ID = 10817;
    private final int BALLOON_INTERFACE = 273;
    private final int KEY_ID = 6966;
    private final int[] EXIT_ID = { 11178, 11177};
    private final int DEPOSIT_BOX = 32924;

    private int targetBalloon = -1;
    private boolean finished;
    private int depositItems;

    enum STATE { RETURN, POP, PULL, DEPOSIT, EXIT }

    public String getName() {
        return "Prison Pete";
    }

    public boolean activate() {
        return (isLoggedIn() && objects.getClosestObject(20, EXIT_ID) != null);
    }

    public synchronized void onStart() {
        KTimer timeout = new KTimer(12000000);
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


    private STATE getState() {
        if (finished) {
            return STATE.EXIT;
        }
        if ((inventory.getCount() - inventory.getCount(false, KEY_ID) > 27) && !inventory.contains(KEY_ID) || interfaces.interfaceExists(11)) {
            return STATE.DEPOSIT;
        }
        if (inventory.contains(KEY_ID)) {
            return STATE.RETURN;
        }
        if (targetBalloon != -1) {
            return STATE.POP;
        } else {
            return STATE.PULL;
        }
    }

    public synchronized int loop() {

        if (!activate()) {
            return -1;
        }

        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }

        if (inventory.contains(KEY_ID)) {
            targetBalloon = -1;
        }
        if (interfaces.componentExists(242, 5) && interfaces.getComponent(242, 5).getText().contains("Lucky")) {
            finished = true;
        }
        if (interfaces.componentExists(242, 4)) {
            if (interfaces.getComponent(242, 4).getText().contains("got all the keys right") ||
                    interfaces.getComponent(242, 4).getText().contains("Come on, we should leave before")) {
                finished = true;
            }
        }
        if (interfaces.canContinue()) {
            interfaces.clickContinue();
            return random(600,900);
        }
        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
            return random(900, 1200);
        }
        if (getMyPlayer().getAnimation() != -1 || getMyPlayer().isMoving()) {
            return random(300,400);
        }
        Interface parent = interfaces.getInterface(BALLOON_INTERFACE);
        if (parent != null && parent.getComponents()[0].isVisible()) {
            IComponent i = parent.getComponent(3);
            targetBalloon = (i.getModelID()-7630);
            sleep(random(800,1200));
            clickExit(parent.getComponent(4));//.doClick();
            return random(500,600);
        }
        if (game.getCurrentTab() != Game.TAB_INVENTORY && !interfaces.interfaceExists(11)) {
            game.openTab(Game.TAB_INVENTORY);
            return random(800,1200);
        }
        switch(getState()) {

            case PULL:
                PhysicalObject lever = objects.getClosestObject(30, LEVER_ID);
                if (lever != null) {
                    if (!lever.onScreen()) {
                        camera.setAngleTo(lever.getLocation());
                    }
                    if (getDistanceTo(lever.getLocation()) >= 6) {
                        walking.walkToMM(lever.getLocation());
                        return random(600,900);
                    }
                    lever.doAction("Pull");
                }
                break;

            case EXIT:
                PhysicalObject exit = objects.getClosestObject(30, EXIT_ID);
                if (exit != null) {
                    if (!exit.onScreen()) {
                        camera.setAngleTo(exit.getLocation());
                    }
                    if (getDistanceTo(exit.getLocation()) >= 6) {
                        walking.walkToMM(exit.getLocation());
                        return random(600,900);
                    }
                    exit.doAction("Open");
                }
                break;

            case POP:
                NPC balloon = npcs.getClosest(30, targetBalloon);
                if (!balloon.onScreen()) {
                    camera.setAngleTo(balloon.getLocation());
                }
                if (balloon != null) {
                    if (getDistanceTo(balloon.getLocation()) >= 7) {
                        walking.walkToMM(balloon.getLocation());
                        return random(600,900);
                    }
                    balloon.doAction("Pop");
                }
                break;

            case RETURN:
                targetBalloon = -1;
                Item[] key = inventory.getItems(KEY_ID);
                if (key != null && key.length > 0) {
                    key[0].doAction("Return");
                }
                break;

            case DEPOSIT:
                if (!interfaces.interfaceExists(11)) {
                    PhysicalObject o = objects.getClosestObject(30, DEPOSIT_BOX);
                    if (o != null) {
                        if (getDistanceTo(o.getLocation()) > 5 || !o.onScreen()) {
                            walking.walkToMM(o.getLocation());
                        }
                        camera.setAngle(camera.getAngleTo(o.getLocation()));
                        o.doAction("Deposit");
                        sleep(random(800,1200));
                        break;
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
                    break;

                }
                break;
        }
        return random(600,900);
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

}

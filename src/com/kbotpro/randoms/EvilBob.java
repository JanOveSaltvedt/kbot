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
public class EvilBob extends Random {
    private final static int SERVANT_ID = 2481;
    private final static int EVIL_BOB_ID = 2479;
    private final static int DEPOSIT_BOX = 32930;
    private final static int COOKED_FISH_ID = 6202;
    private final static int UNCOOKED_FISH_ID = 6200;
    private final static int NET_ID = 6209;
    private final static int UNCOOKING_POT = 8985;
    private final static int EXIT_PORTAL = 8987;
    private final static int FISHING_SPOT = 8986;


    enum STATE { FEED, FIND_STATUE, FISH, GET_NET, DEPOSIT, UNCOOK, EXIT, ERROR }

    Tile center = new Tile(3421, 4777);
    boolean finished = false;
    PhysicalObject fishingSpot;

    PhysicalObject[] getObjects() {
        ArrayList<PhysicalObject> objectArray = new ArrayList<PhysicalObject>();
        Tile tiles[] = { new Tile(3406, 4774), new Tile(3406, 4775), new Tile(3406, 4777), new Tile(3406, 47748),
                new Tile(3422, 4791), new Tile(3423, 4791), new Tile(3425, 4791), new Tile(3426, 4791),
                new Tile(3439, 4779), new Tile(3439, 4778), new Tile(3439, 4777), new Tile(3439, 4776),
                new Tile(3425, 4764), new Tile(3424, 4764), new Tile(3421, 4764), new Tile(3420, 4764) };
        for (Tile t : tiles) {
            for (PhysicalObject o : objects.getObjectsAt(t, new int[] {FISHING_SPOT})) {
                if (o != null) objectArray.add(o);
            }
        }
        objectArray.trimToSize();
        return objectArray.toArray(new PhysicalObject[objectArray.size()]);
    }
    PhysicalObject findFishingSpotOnScreen() {

        for (PhysicalObject element : getObjects()) {
            if (element != null && calculations.onScreen(element.getScreenPos()) &&
                    calculations.tileToScreen(element.getLocation()).getY() > (botEnv.botPanel.getSize().getHeight() / 2)) {
                return element;
            }
        }
        return null;
    }


    public String getName() {
        return "EvilBob/Scaperune Island";
    }

    public boolean activate() {
        if (!isLoggedIn()) {
            return false;
        }
        return (objects.getClosestObject(20, UNCOOKING_POT) != null) || (objects.getClosestObject(20, 8993, 8992, 8991, 8990) != null);
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
        if ((inventory.getCount() - inventory.getCount(false, COOKED_FISH_ID, UNCOOKED_FISH_ID, NET_ID)) > 26 || interfaces.interfaceExists(11)) {
            return STATE.DEPOSIT;
        }
        if (!inventory.contains(NET_ID)) {
            return STATE.GET_NET;
        }
        if (fishingSpot == null) {
            return STATE.FIND_STATUE;
        }
        if (!inventory.contains(COOKED_FISH_ID) &&
                !inventory.contains(UNCOOKED_FISH_ID)) {
            return STATE.FISH;
        }
        if (inventory.contains(COOKED_FISH_ID)) {
            return STATE.UNCOOK;
        }
        if (inventory.contains(UNCOOKED_FISH_ID)) {
            return STATE.FEED;
        }
        else {
            return STATE.ERROR;
        }
    }

    public synchronized int loop() {
        if (!activate()) {
            return -1;
        }
        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }
        if (interfaces.interfaceExists(242) && interfaces.getComponent(242,0).isVisible()) {
            for (IComponent i :interfaces.getInterface(242).getComponents()) {
                if (i.getText().toLowerCase().contains("catnap")) {
                    finished = true;
                    break;
                }
            }
        }
        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
        }
        if (getMyPlayer().isMoving() || getMyPlayer().getAnimation() != -1) {
            return random(100, 200);
        }
        if (!interfaces.interfaceExists(11) && !inventory.isOpen()) {
            game.openTab(Game.TAB_INVENTORY);
        }
        if (interfaces.componentExists(64, 4)) {

            String s = interfaces.getComponent(64, 4).getText();
            if (s != null &&  s.contains("fallen asleep")) {
                finished = true;
            }
        }
        if (interfaces.componentExists(242, 4)) {
            String s = interfaces.getComponent(242, 4).getText();
            if (s != null && s.contains("Wait! Before you do that")) {
                fishingSpot = null;
            }
        }
        if (interfaces.componentExists(94, 2)) {
            interfaces.getComponent(94, 0).doClick();
            return random(800, 900);
        }
        if (inventory.contains(6206)) {
            inventory.atItem(6206, "Destroy");
            fishingSpot = null;
            return random(800,900);
        }
        if (interfaces.componentExists(243, 4)) {
            IComponent i = interfaces.getComponent(243, 4);
            if (i.getText().contains("You're going nowhere")) {
                finished = false;
                fishingSpot = null;
            }
        }
        if (interfaces.canContinue()) {
            interfaces.clickContinue();
            return random(800,900);
        }
        switch (getState()) {

            case ERROR:
                stopAllScripts();
                return -1;

            case EXIT:
                PhysicalObject exit = objects.getClosestObject(30, EXIT_PORTAL);
                if (!exit.onScreen() ||
                        getDistanceTo(exit.getLocation()) > 6) {
                    walkTo(center);
                    camera.setAngle(camera.getAngleTo(exit.getLocation()));
                    return random(500, 600);
                }
                exit.doAction("Enter");
                waitForAnim(true);
                waitForAnim(false);
                waitForAnim(true);
                waitForAnim(false);
                break;

            case FIND_STATUE:
                NPC servant = npcs.getClosest(30, SERVANT_ID);
                if (interfaces.interfaceExists(246)) {
                    KTimer k = new KTimer(6000);
                    while (fishingSpot == null && !k.isDone()) {
                        sleep(10);
                        Interface servantInterface = interfaces.getInterface(242);
                        if (servantInterface == null || servantInterface.getComponent(4) == null ||
                                servantInterface.getComponent(6) == null) {
                            continue;
                        }
                        if (servantInterface.getComponent(4).getText().contains("L-l-l")
                                && servantInterface.getComponent(6).getText().contains("continue")) {
                            fishingSpot = findFishingSpotOnScreen();
                        }
                    }
                    sleep(30);
                }

                else if (servant != null) {
                    if (!servant.onScreen() ||
                            getDistanceTo(servant.getLocation()) > 4) {
                        walkTo(center);
                        camera.setAngle(camera.getAngleTo(servant.getLocation()));
                        break;
                    }
                    servant.doAction("Talk-to");
                    break;
                } else {
                    walkTo(center);
                    break;
                }
                break;

            case FISH:

                if (fishingSpot != null) {
                    if (getDistanceTo(fishingSpot.getLocation()) > 5) {
                        walkTo(fishingSpot.getLocation());
                        camera.setAngle(camera.getAngleTo(fishingSpot.getLocation()));
                        break;
                    }

                    PhysicalObject spot = objects.getClosestObject(15, 8986);
                    if (spot != null) {
                        if (spot.doAction("Net")) {
                            return random(1500, 2000);
                        }
                    }
                }
                break;


            case DEPOSIT:
                if (!interfaces.interfaceExists(11)) {
                    PhysicalObject o = objects.getClosestObject(30, DEPOSIT_BOX);
                    if (o != null) {
                        if (getDistanceTo(o.getLocation()) > 5 || !o.onScreen()) {
                            walkTo(o.getLocation());
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
                        if (depositedCount >= 2) break;
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

            case GET_NET:
                GroundItem net = groundItems.getClosestItem(30, 6209);
                if (!net.onScreen()) {
                    walkTo(net.getLocation());
                    camera.setAngle(camera.getAngleTo(net.getLocation()));
                    break;
                } else {
                    net.doAction("Take");
                }
                break;

            case UNCOOK:
                Item[] fish  = inventory.getItems(COOKED_FISH_ID);
                PhysicalObject pot = objects.getClosestObject(20, UNCOOKING_POT);
                if (pot != null) {
                    if (getDistanceTo(pot.getLocation()) > 6 ||
                            !pot.onScreen()) {
                        walkTo(center);
                        camera.setAngle(camera.getAngleTo(pot.getLocation()));
                        break;
                    }
                    fish[0].doClick();
                    pot.doAction("-> Uncooking pot");
                    waitForAnim(true);

                }
                break;

            case FEED:
                Item[] uncookedFish = inventory.getItems(UNCOOKED_FISH_ID);
                NPC bob = npcs.getClosest(15, EVIL_BOB_ID);
                if (getDistanceTo(bob.getLocation()) > 6) {
                    walkTo(center);
                    camera.setAngle(camera.getAngleTo(bob.getLocation()));
                    break;
                }
                camera.setAngle(random(0, 360));
                uncookedFish[0].doClick();
                mouse.moveMouse(calculations.tileToScreen(bob.getLocation()), false);
                while (!menu.isOpen()) sleep(50);
                if (menu.contains("-> Evil Bob") && menu.atMenu("-> Evil Bob")) {
                    sleep(random(1200, 1500));
                    finished = true;
                }
                /*if (bob.doAction("Use Raw fish-like thing -> Evil Bob")) {
                sleep(random(1200, 1500));
                finished = true;
                }          */
                break;
        }
        return random(700,1100);
    }
    boolean walkTo(Tile walkToTile) {
        if (distanceTo(walkToTile) > 10) {
            int x = getMyPlayer().getLocation().getX();
            int y = getMyPlayer().getLocation().getY();
            if (Math.abs(x - walkToTile.getX()) > 3) {
                if (x > walkToTile.getX()) {
                    x -= 10;
                } else {
                    x += 10;
                }
            }
            if (Math.abs(y - walkToTile.getY()) > 3) {
                if (y > walkToTile.getY()) {
                    y -= 10;
                } else {
                    y += 10;//3634
                }
            }
            return walking.walkToMM(new Tile(x, y));
        } else {
            return walking.walkToMM(walkToTile);
        }
    }

    boolean waitForAnim(boolean noAnim) {
        KTimer k = new KTimer(5000);
        while ((getMyPlayer().getAnimation() == -1) != noAnim && !k.isDone()) {
            sleep(20);
        }
        return (getMyPlayer().getAnimation() == -1) == noAnim;
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
}
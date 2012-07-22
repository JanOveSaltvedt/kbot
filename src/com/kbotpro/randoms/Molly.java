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

import com.kbotpro.scriptsystem.events.ServerMessageListener;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.*;
import com.kbotpro.various.StaticStorage;

/**
 * Created by endoskeleton.
 */
public class Molly extends Random implements ServerMessageListener {

    private int targetMolly = -1;
    private NPC originalMolly;
    private PhysicalObject controlPanel;
    private PhysicalObject door;
    private boolean caught;

    private final int DOOR_ID = 14982;
    private final int CONTROL_PANEL_ID = 14978;


    public void onServerMessage(String s) {
        if (s.contains("evil twin")) {
            caught = true;
        }
    }

    public boolean activate() {
        return isLoggedIn() && objects.getClosestObject(20, CONTROL_PANEL_ID) != null;
    }

    public String getName() {
        return "Molly";
    }

    public synchronized void onStart() {
        caught = false;
        KTimer timeout = new KTimer(600000);
        camera.setAltitude(true);
        originalMolly = npcs.getClosest(15, "Molly");
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

    public synchronized int loop() {

        if (!activate()) {
            return -1;
        }

        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }

        IComponent[] iComp = interfaces.getInterfaces("finish grabbing my twin");
        if (iComp.length > 0 && iComp[0].isVisible()) {
            caught = false;
        }

        iComp = interfaces.getInterfaces("No thanks.");
        if (iComp.length > 0 && iComp[0].isVisible()) {
            iComp[0].doClick();
            return random(1000,1500);
        }

        if (interfaces.canContinue()) {
            interfaces.clickContinue();
            return random(800, 1200);
        }

        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
            return random(900, 1200);
        }

        if (getMyPlayer().isMoving() || getMyPlayer().getAnimation() != -1) {
            return random(100, 200);
        }

        if (interfaces.componentExists(228, 2)) {
            IComponent i = interfaces.getComponent(228, 2);
            if (i.getText().contains("Yes, I know")) {
                i.doClick();
                return random(600, 900);
            }
        }
        controlPanel = objects.getClosestObject(20, CONTROL_PANEL_ID);
        door = objects.getClosestObject(20, DOOR_ID);
        if (targetMolly == -1 && originalMolly != null) {
            targetMolly = (originalMolly.getID()-40);
        }

        if (inControlRoom() && caught) {
            if (!calculations.isInGameArea(door.getScreenPos())) {
                camera.setAngleTo(door.getLocation());
            }
            door.doAction("Open");
            return random(500, 600);
        }

        if (getMyPlayer().getLocation().getX() <= door.getLocation().getX()) {
            if (!caught) {
                if (!calculations.isInGameArea(door.getScreenPos())) {
                    camera.setAngleTo(door.getLocation());
                }
                door.doAction("Open");
                return random(600, 900);
            } else {
                if (!calculations.isInGameArea(originalMolly.getScreenPos())) {
                    camera.setAngleTo(originalMolly.getLocation());
                }
                originalMolly.doAction("Talk-to");
            }
        }

        if (!caught &&  !interfaces.interfaceExists(240)) {
            if (!calculations.isInGameArea(controlPanel.getScreenPos())) {
                camera.setAngleTo(controlPanel.getLocation());
            }
            controlPanel.doAction("Use");
            return random(600, 900);
        }

        if (interfaces.interfaceExists(240) && !caught) {
            clawGame();
        }

        return random(600, 900);
    }

    boolean inControlRoom() {
        controlPanel = objects.getClosestObject(20, CONTROL_PANEL_ID);
        return getMyPlayer().getLocation().getX() > door.getLocation().getX();
    }

    void clawGame() {
        KTimer time = new KTimer(300000);
        PhysicalObject claw = objects.getClosestObject(30, 14976);
        NPC molly = npcs.getClosest(20, targetMolly);
        Interface parent = interfaces.getInterface(240);
        if (molly == null || parent == null || claw == null) {
            return;
        }
        int westEast = 0;
        IComponent westEastComponent = null;
        int northSouth = 0;
        IComponent northSouthComponent = null;
        if (molly.getLocation().getX() < claw.getLocation().getX()) {
            westEast = Math.abs(claw.getLocation().getX() - molly.getLocation().getX());
            westEastComponent = parent.getComponent(32);
        }
        if (molly.getLocation().getX() > claw.getLocation().getX()) {
            westEast = Math.abs(claw.getLocation().getX() - molly.getLocation().getX());
            westEastComponent = parent.getComponent(31);
        }
        if (molly.getLocation().getY() > claw.getLocation().getY()) {
            northSouth = Math.abs(claw.getLocation().getY() - molly.getLocation().getY());
            northSouthComponent = parent.getComponent(30);
        }
        if (molly.getLocation().getY() < claw.getLocation().getY()) {
            northSouth = Math.abs(claw.getLocation().getY() - molly.getLocation().getY());
            northSouthComponent = parent.getComponent(29);
        }
        for (int i = 0; i < westEast; i++) {
            westEastComponent.doClick();
            sleep(random(50, 100));
        }
        for (int i = 0; i < northSouth; i++) {
            northSouthComponent.doClick();
            sleep(random(50, 100));
        }
        if (molly.getLocation().equals(claw.getLocation())) {
            parent.getComponent(28).doClick();
        }
        /*while (!time.isDone() && !claw.getLocation().equals(molly.getLocation())
                && parent.getComponent(0).isVisible() && !caught) {
            Tile clawLocation = objects.getClosestObject(30, 14976).getLocation();
            Tile mollyLocation = npcs.getClosest(20, targetMolly).getLocation();
            IComponent direction;
            if (mollyLocation.getX() > clawLocation.getX()) {
                direction = parent.getComponent(31); //ok
            } else if (mollyLocation.getX() < clawLocation.getX()) {
                direction = parent.getComponent(32);
            } else if (mollyLocation.getY() < clawLocation.getY()) {
                direction = parent.getComponent(29);
            } else if (mollyLocation.getY() > clawLocation.getY()) {
                direction = parent.getComponent(30);
            } else {
                direction = parent.getComponent(28);
            }
            if (direction != null) {
                direction.doClick();
            }
            sleep(random(600,900));

        }
        sleep(random(2000, 3000)); */
    }
}
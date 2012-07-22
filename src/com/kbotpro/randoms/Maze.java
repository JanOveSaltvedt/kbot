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

import com.kbotpro.scriptsystem.Calculations;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;
import com.kbotpro.scriptsystem.wrappers.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Jacked this bitch from RSBot, if it doesnt work, blame them!
 */

public class Maze extends Random {

    public ArrayList<Door> paths = new ArrayList<Door>();

    String usePath = "None";

    public char doorDir = 'a';
    public Tile walkToTile = null;
    public int doorIndex = 0;

    public int tryCount = 0;
    public long lastTry = 0;
    public Tile lastDoor = null;
    //working as of 1/31/2010

    @Override
    public String getName() {//That's not the right way
        return "Maze Test";
    }

    @Override
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

    public void loadGreenPath() {

        paths.add(new Door(new Tile(2903, 4555), 's', 0));
        paths.add(new Door(new Tile(2890, 4566), 'e', 1));
        paths.add(new Door(new Tile(2892, 4578), 'e', 2));
        paths.add(new Door(new Tile(2894, 4567), 'e', 3));
        paths.add(new Door(new Tile(2896, 4562), 'e', 4));
        paths.add(new Door(new Tile(2905, 4561), 's', 5));
        paths.add(new Door(new Tile(2915, 4560), 'n', 6));
        paths.add(new Door(new Tile(2909, 4562), 'n', 7));
        paths.add(new Door(new Tile(2924, 4566), 'w', 8));
        paths.add(new Door(new Tile(2922, 4575), 'w', 9));
        paths.add(new Door(new Tile(2916, 4568), 'n', 10));
        paths.add(new Door(new Tile(2905, 4573), 'w', 11));
        paths.add(new Door(new Tile(2906, 4585), 'n', 12));
        paths.add(new Door(new Tile(2916, 4586), 's', 13));
        paths.add(new Door(new Tile(2920, 4582), 'w', 14));
        paths.add(new Door(new Tile(2910, 4582), 's', 15));
        paths.add(new Door(new Tile(2910, 4572), 'n', 16));
        paths.add(new Door(new Tile(2910, 4576), 'e', 17));
        //log("Loaded green path");
    }
    //working as of 1/31/2010
    public void loadBluePath() {

        paths.add(new Door(new Tile(2891, 4588), 'w', 0));
        paths.add(new Door(new Tile(2889, 4596), 'w', 1));
        paths.add(new Door(new Tile(2893, 4600), 's', 2));
        paths.add(new Door(new Tile(2901, 4598), 's', 3));
        paths.add(new Door(new Tile(2897, 4596), 's', 4));
        paths.add(new Door(new Tile(2894, 4587), 'e', 5));
        paths.add(new Door(new Tile(2896, 4582), 'e', 6));
        paths.add(new Door(new Tile(2898, 4570), 'e', 7));
        paths.add(new Door(new Tile(2900, 4567), 'e', 8));
        paths.add(new Door(new Tile(2911, 4566), 'n', 9));
        paths.add(new Door(new Tile(2906, 4585), 'n', 10));
        paths.add(new Door(new Tile(2916, 4586), 's', 11));
        paths.add(new Door(new Tile(2920, 4582), 'w', 12));
        paths.add(new Door(new Tile(2910, 4582), 's', 13));
        paths.add(new Door(new Tile(2910, 4572), 'n', 14));
        paths.add(new Door(new Tile(2910, 4576), 'e', 15));
        log("Loaded blue path");
    }
    //working as of 2/06/2010
    public void loadCyanPath() {

        paths.add(new Door(new Tile(2930, 4555), 's', 0));
        paths.add(new Door(new Tile(2912, 4553), 's', 1));
        paths.add(new Door(new Tile(2936, 4556), 'w', 2));
        paths.add(new Door(new Tile(2934, 4568), 'w', 3));
        paths.add(new Door(new Tile(2932, 4575), 'w', 4));
        paths.add(new Door(new Tile(2930, 4561), 'w', 5));
        paths.add(new Door(new Tile(2929, 4581), 'e', 6));
        paths.add(new Door(new Tile(2930, 4590), 'w', 7));
        paths.add(new Door(new Tile(2924, 4592), 's', 8));
        paths.add(new Door(new Tile(2926, 4575), 'w', 9));
        paths.add(new Door(new Tile(2924, 4583), 'w', 10));
        paths.add(new Door(new Tile(2916, 4586), 's', 11));
        paths.add(new Door(new Tile(2920, 4582), 'w', 12));
        paths.add(new Door(new Tile(2910, 4582), 's', 13));
        paths.add(new Door(new Tile(2910, 4572), 'n', 14));
        paths.add(new Door(new Tile(2910, 4576), 'e', 15));
        //log("Loaded cyan path");
    }
    //working as of 2/05/2010
    public void loadPurplePath() {

        paths.add(new Door(new Tile(2932, 4597), 'n', 0));
        paths.add(new Door(new Tile(2921, 4599), 'n', 1));
        paths.add(new Door(new Tile(2909, 4600), 's', 3));
        paths.add(new Door(new Tile(2913, 4598), 's', 4));
        paths.add(new Door(new Tile(2908, 4596), 's', 5));
        paths.add(new Door(new Tile(2919, 4594), 's', 6));
        paths.add(new Door(new Tile(2908, 4592), 's', 7));
        paths.add(new Door(new Tile(2898, 4585), 'e', 8));
        paths.add(new Door(new Tile(2903, 4588), 's', 9));
        paths.add(new Door(new Tile(2902, 4575), 'e', 10));
        paths.add(new Door(new Tile(2906, 4585), 'n', 11));
        paths.add(new Door(new Tile(2916, 4586), 's', 12));
        paths.add(new Door(new Tile(2920, 4582), 'w', 13));
        paths.add(new Door(new Tile(2910, 4582), 's', 14));
        paths.add(new Door(new Tile(2910, 4572), 'n', 15));
        paths.add(new Door(new Tile(2910, 4576), 'e', 16));

        log("Loaded purple path");
    }

    public class Door {

        public Tile doorTile;
        public char doorDir;
        public int doorID;

        public Door(final Tile doorTile, final char doorDir, int doorID) {
            this.doorTile = doorTile;
            this.doorDir = doorDir;
            this.doorID = doorID;
        }
    }

    public boolean activate() {
        if (isLoggedIn() && ((objects.getClosestObject(15, 3626) != null) || (objects.getClosestObject(15, 3628) != null))) {
            camera.setAltitude(true);
            return true;
        }
        return false;
    }

    public int loop() {

        if (!activate()) {
            return -1;
        }
        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }
        if (getMyPlayer().isMoving()) {
            return random(150,200);
        }
        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
            return random(900, 1200);
        }
        if (getMyPlayer().getLocation().equals(new Tile(2911, 4576))) {
            if (getMyPlayer().getAnimation() == -1) {
                //atTile(new Tile(2912, 4576), "Touch");
                PhysicalObject orb = objects.getClosestObject(15, 3634);
                orb.doAction("Touch");
                //mouse.moveMouse(calculations.tileToScreen(new Tile(2912, 4576)), true);
                return random(1000,1200);
            }
        }
        if (usePath.equals("None")) {
            getPath();
            return random(900,1100);
        }

        if (getMyPlayer().getLocation().equals(tileAfterDoor())) {
            doorIndex += 1;
            //log("Getting new wall");
            getNewWall();
            return random(200, 300);
        }
        if ((walkToTile != null) && (distanceTo(walkToTile) >= 3)) {
            if (distanceTo(walkToTile) > 10) {
                int x = getMyPlayer().getLocation().getX();
                int y = getMyPlayer().getLocation().getY();
                if (Math.abs(x - walkToTile.getX()) > 3) {
                    if (x > walkToTile.getX()) {
                        if (calculations.canReach(new Tile(x-10, y), false)) {
                            x -= 10;
                        }
                    } else {
                        if (calculations.canReach(new Tile(x + 10, y), false)) {
                            x += 10;
                        }
                    }
                }
                if (Math.abs(y - walkToTile.getY()) > 3) {
                    if (y > walkToTile.getY()) {
                        if (calculations.canReach(new Tile(x, y-10), false)) {
                            y -= 10;
                        }
                    } else {
                        if (calculations.canReach(new Tile(x, y+10), false)) {
                            y += 10;//3634
                        }
                    }
                }
                walking.walkToMM(new Tile(x, y));
                return random(500,600);
            }
            walking.walkToMM(new Tile(walkToTile.getX(), walkToTile.getY()));
            return random(500,600);
        }
        if ((walkToTile != null) && (distanceTo(walkToTile) <= 3)) {
            if ((doorDir != 'a') && !getMyPlayer().isMoving()) {
                if (((camera.getAngle() - turnCameraTo()) < 30) || ((camera.getAngle() - turnCameraTo()) > 30)) {
                    camera.setAngle(turnCameraTo());
                }
                if (atDoor(walkToTile, doorDir)) {
                    return (random(400,500));
                }
            }
        }
        return random(300, 350);
    }

    public void getNewWall() {
        for (Door door : paths) {
            if (door.doorID == doorIndex) {
                walkToTile = new Tile(door.doorTile.getX(), door.doorTile.getY());
                doorDir = door.doorDir;
                door.doorID = doorIndex;
                //log("Walkto: " + walkToTile.getX() + ", " + walkToTile.getY());
                //log("Door index: " + doorIndex + " | Door direction: " + doorDir);
            }
        }
    }

    public int turnCameraTo() {
        int doorD = doorDir;
        if (doorD == 'a') {
            //log("TURNCAMERATO: WALL DIRECTION IS 'A");
            return random(330, 380);
        }
        switch (doorD) {
            case 'n':
                return random(330, 380);
            case 's':
                return random(155, 190);
            case 'e':
                return random(245, 290);
            case 'w':
                return random(65, 110);
        }
        return random(330, 380);
    }

    public Tile tileAfterDoor() {
        Tile wallTile = walkToTile;
        int doorD = doorDir;
        if (doorD == 'a') {
            //log("TILEAFTERDOOR: doorD = A");
            wallTile = walkToTile;
            return new Tile(1, 1);
        }
        if (wallTile == null) {
            //log("TILEAFTERDOOR: wallTile = NULL");
            wallTile = walkToTile;
            return new Tile(1, 1);
        }
        switch (doorD) {
            case 'n':
                return new Tile(wallTile.getX(), wallTile.getY() + 1);
            case 'w':
                return new Tile(wallTile.getX() - 1, wallTile.getY());
            case 'e':
                return new Tile(wallTile.getX() + 1, wallTile.getY());
            case 's':
                return new Tile(wallTile.getX(), wallTile.getY() - 1);
        }
        return new Tile(1, 1);
    }

    public void getPath() {
        int x = getMyPlayer().getLocation().getX();
        int y = getMyPlayer().getLocation().getY();
        if ((x >= 2920) && (x <= 2940) && (y >= 4572) && (y <= 4600)) {
            loadPurplePath();
            usePath = "purple";
            walkToTile = new Tile(2932, 4597);
            doorDir = 'n';
            doorIndex = 0;
            //log("Using purple path!");
        }
        if ((x >= 2891) && (x <= 2894) && (y >= 4586) && (y <= 4599)) {
            loadBluePath();
            usePath = "blue";
            walkToTile = new Tile(2891, 4588);
            doorDir = 'w';
            doorIndex = 0;
            //log("Using blue path!");
        }
        if ((x >= 2915) && (x <= 2933) && (y >= 4555) && (y <= 4560)) {
            loadCyanPath();
            usePath = "cyan";
            walkToTile = new Tile(2930, 4555);
            doorDir = 's';
            doorIndex = 0;
            //log("Using cyan path!");
        }
        if ((x >= 2891) && (x <= 2914) && (y >= 4555) && (y <= 4561)) {
            loadGreenPath();
            usePath = "green";
            walkToTile = new Tile(2903, 4555);
            doorDir = 's';
            doorIndex = 0;
            //log("Using green path!");
        }
    }

    public boolean atDoor(final Tile location, final char direction) {
        if (location == null)
            return false;
        int x = location.getX(), y = location.getY();
        boolean fail = false;
        switch (direction) {
            case 'N':
            case 'n':
                y++;
                break;
            case 'W':
            case 'w':
                x--;
                break;
            case 'E':
            case 'e':
                x++;
                break;
            case 'S':
            case 's':
                y--;
                break;
            default:
                fail = true;
        }
        if (fail)
            throw new IllegalArgumentException();
        return atDoorTiles(location, new Tile(x, y));
    }

    public boolean atDoorTiles(final Tile a, final Tile b) {
        if (a != lastDoor) {
            lastTry = 0;
            tryCount = 0;
            lastDoor = a;
        }
        tryCount++;
        if (System.currentTimeMillis() - lastTry > random(20000, 40000)) {
            tryCount = 1;
        }
        lastTry = System.currentTimeMillis();
        if (tryCount > 4) {
            if (random(0, 10) < random(2, 4)) {
                camera.setAngle((int)camera.getAngle() + (random(0, 9) < random(6, 8) ? random(-20, 20) : random(-360, 360)));
            }
            if (random(0, 14) < random(0, 2)) {
                camera.setAngle((random(0, 100)));
            }
        }
        if (tryCount > 100) {
            //log("Problems finding wall....");
            stopAllScripts();
        }
        if (!calculations.isInGameArea(calculations.tileToScreen(a)) || !calculations.isInGameArea(calculations.tileToScreen(b)) || (distanceTo(a) > random(4, 7))) {
            if (calculations.onScreen(calculations.tileToMinimap(a))) {
                walking.walkToMM(a);
                sleep(random(750, 1250));
            } else {
                //log("Cannot find wall tiles...");
                return false;
            }
        } else {
            final ArrayList<Tile> theObjs = new ArrayList<Tile>();
            theObjs.add(a);
            theObjs.add(b);
            try {
                final Point[] thePoints = new Point[theObjs.size()];
                for (int c = 0; c < theObjs.size(); c++) {
                    thePoints[c] = calculations.tileToScreen(theObjs.get(c));
                }
                float xTotal = 0;
                float yTotal = 0;
                for (final Point thePoint : thePoints) {
                    xTotal += thePoint.getX();
                    yTotal += thePoint.getY();
                }
                final Point location = new Point((int) (xTotal / thePoints.length), (int) (yTotal / thePoints.length) - random(0, 40));
                if ((location.x == -1) || (location.y == -1))
                    return false;
                if (Math.sqrt(Math.pow((mouse.getMousePos().getX() - location.getX()), 2) + Math.pow((mouse.getMousePos().getY() - location.getY()), 2)) < random(20, 30)) {
                    final String[] commands = menu.getMenuItems();
                    for (final String command : commands) {
                        if (command.contains("Open")) {
                            if (menu.atMenu("Open")) {
                                lastTry = 0;
                                tryCount = 0;
                                return true;
                            }
                        }
                    }
                }
                mouse.moveMouse(location);
                if (menu.atMenu("Open")) {
                    lastTry = 0;
                    tryCount = 0;
                    return true;
                }
            } catch (final Exception e) {
                return false;
            }
        }
        return false;
    }

}

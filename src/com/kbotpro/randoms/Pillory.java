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
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;
import com.kbotpro.scriptsystem.wrappers.Tile;
/**
 * Created by endoskeleton.
 */
public class Pillory extends Random implements ServerMessageListener {
    private final static Tile[] PILLORY_TILES = { new Tile(2608,3105), new Tile(2606,3105), new Tile(2604,3105),
            new Tile(3226,3407), new Tile(3228,3407), new Tile(3230,3407), new Tile(2685,3489), new Tile(2683,3489),
            new Tile(2681,3489) };
    private final static int PILLORY_INTERFACE = 189;
    private final static int PILLORY_LOCK_ID = 777;
    boolean solved = false;

    public synchronized int loop() {
        if (!activate()) {
            return -1;
        }
        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }
        if (!interfaces.interfaceExists(PILLORY_INTERFACE) && !solved) {
            PhysicalObject lock = objects.getClosestObject(3, PILLORY_LOCK_ID);
            if (lock != null) {
                lock.doAction("Unlock");
                return random(1000,1200);
            }
        }
        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
            return random(900, 1200);
        }
        if (interfaces.interfaceExists(PILLORY_INTERFACE)) {
            IComponent pilloryInterface = interfaces.getComponent(PILLORY_INTERFACE, 4);
            int neededModel = 0;
            switch (pilloryInterface.getModelID()) {
                case 9753:
                    neededModel = 9749;
                    break;
                case 9754:
                    neededModel = 9750;
                    break;
                case 9755:
                    neededModel = 9751;
                    break;
                case 9756:
                    neededModel = 9752;
                    break;
            }
            for (int z = 5; z < 8; z++) {
                IComponent i = interfaces.getComponent(PILLORY_INTERFACE, z);
                if (i.getModelID() == neededModel) {
                    interfaces.getComponent(PILLORY_INTERFACE, z+3).doClick();
                    return random(1000,1200);
                }
            }
        }
        return 100;
    }

    public String getName() {
        return "Pillory";
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
        if (!isLoggedIn()) {
            return false;
        }
        for(Tile t : PILLORY_TILES) {
            if (t.equals(getMyPlayer().getLocation())) {
                return true;
            }
        }
        return false;
    }

    public void onServerMessage(String s) {
        if (s.contains("You've escaped")) {
            solved = true;
        }
    }
}
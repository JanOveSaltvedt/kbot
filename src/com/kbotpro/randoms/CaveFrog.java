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
import com.kbotpro.scriptsystem.runnable.*;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.*;

import java.awt.*;

public class CaveFrog extends Random {

    boolean talkedToKing = false;
    public int[] kingID = { 2472 };

    public synchronized int loop() {
        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }
        if (!activate()) {
            return -1;
        }
        if (game.getGameState() == 25) {
            return random(100,200);
        }

        if (getMyPlayer().isMoving()) {
            return random(100,200);
        }

        if (interfaces.canContinue()) {
            interfaces.clickContinue();
            return random(800,1000);
        }
        IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
        if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
            leftValuables[0].doClick();
            return random(900, 1200);
        }
        IComponent[] c = interfaces.getInterfaces("yes");
        if (c != null && c.length > 0 && c[0].isVisible()) {
            c[0].doClick();
            return random(800,1000);
        }
        c = interfaces.getInterfaces("please come and talk to me for a moment");
        if (c != null && c.length > 0 && c[0].isVisible()) {
            talkedToKing = false;
        }
        if (!talkedToKing) {
            NPC king = npcs.getClosest(15, kingID);
            if (king != null) {
                king.doAction("Talk-to");
                talkedToKing = true;
                return random(800, 1000);
            }
        } else {
            NPC frog = findPrinceOrPrincess();
            if (frog != null) {
                camera.setAngleTo(frog.getLocation());
                frog.doAction("Talk-to");
                return random(2000, 2500);
            }
        }

        return random(100,200);
    }

    public synchronized void onStart() {
        KTimer timeout = new KTimer(600000);
        while (!botEnv.randomManager.scriptStopped && activate() && !timeout.isDone()) {
            int i = loop();
            if (i <= 0) {
                break;
            }
            sleep(i);
            if (!activate()) {
                break;
            }
        }
        if (timeout.isDone()) {
            botEnv.scriptManager.stopAllScripts();
        }
    }

    public NPC findPrinceOrPrincess() {
        NPC[] npc = npcs.getNPCs();
        for(NPC n : npc) {
            if (!activate()) {
                break;
            }
            if (n != null && getDistanceTo(n.getLocation()) < 10) {
                if (n.getName().toLowerCase().contains("prince")) {
                    return n;
                }
                while (n.isMoving() && activate()) sleep(100);
                if (n.getHeight() == 278)
                    return n;
            }
        }
        return null;
    }


    public boolean activate() {
        if (isLoggedIn()) {
            NPC king = npcs.getClosest(30, kingID);
            if (king != null && king.getName().contains("Frog Herald") && objects.getClosestObject(20, 5917) != null) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return "Cave Frog";
    }

}
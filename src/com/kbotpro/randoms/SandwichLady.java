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

import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Interface;
import com.kbotpro.scriptsystem.wrappers.NPC;
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alecquaquarucci
 * Date: Jan 24, 2010
 * Time: 3:52:38 PM
 * To change this template use File | Settings | File Templates.
 */

// Originally created by Kosaki [KBot2]

public class SandwichLady extends Random {

    private final int OBJECT_GREEN_PORTAL_ID = 11373;
    private final int NPC_SANDWICH_LADY_ID = 8630;
    private final int INTERFACEGROUP_TALK_ID = 243;
    private final int INTERFACE_TALK_CONTINUE_ID = 7;

    private final int INTERFACEGROUP_WINDOW_ID = 297;
    private final int INTERFACE_WINDOW_TEXT_ID = 48;

    private final int[] MODEL_IDS = { 10728, 10732, 10727, 10730, 10726, 45666,	10731 };
    private final String[] MODEL_NAMES = { "chocolate", "triangle", "roll", "pie", "baguette", "doughnut", "square" };

    public String getName() {
        return "Sandwich Lady";
    }

    public boolean activate() {
        if (!isLoggedIn()) {
            return false;
        }
        NPC lady = npcs.getClosest(20, NPC_SANDWICH_LADY_ID);
        return lady != null && lady.getInteracting() != null && lady.getInteracting().equals(getMyPlayer()) ||
                lady != null && objects.getClosestObject(15, OBJECT_GREEN_PORTAL_ID) != null;
    }

    public synchronized void onStart() {
        boolean solved = false;
        KTimer timeout = new KTimer(600000);
        for(int tries = 0; tries < 10; tries++){
            if (!isLoggedIn() || timeout.isDone()) {
                break;
            }
            if (game.hasSelectedItem()) {
                menu.atMenu("Cancel");
            }
            IComponent solvedFace = interfaces.getComponent(242, 5);
            if (solvedFace != null && solvedFace.isVisible() && solvedFace.textContainsIgnoreCase("you're ready to leave")) {
                solved = true;
            }
            IComponent chatInter = interfaces.getComponent(INTERFACEGROUP_TALK_ID,INTERFACE_TALK_CONTINUE_ID);
            if(chatInter != null && chatInter.isValid()){
                chatInter.doClick();
            }

            if(solved){
                PhysicalObject portal = objects.getClosestObject(20, OBJECT_GREEN_PORTAL_ID);
                if (portal != null) {
                    if (!calculations.isInGameArea(portal.getScreenPos())) {
                        camera.setAngleTo(portal.getLocation());
                        walking.walkToMM(portal.getLocation());
                    }
                    //mouse.moveMouse(calculations.tileToScreen(portal.getLocation()), true);
                    if (portal.doAction("Enter")) {                        //Animated object clicking broke!
                        sleep(2000, 3000);
                        while(game.getGameState() == 10)
                            sleep(500, 1000);
                        if(interfaces.canContinue()){
                            interfaces.clickContinue();
                            sleep(1000,2000);
                        }
                        return;
                    }
                }
            }

            Interface interfaceGroup = interfaces.getInterface(INTERFACEGROUP_WINDOW_ID);
            if(interfaceGroup != null && interfaceGroup.isValid()){
                int index = -1;
                IComponent anInterface = interfaceGroup.getComponent(INTERFACE_WINDOW_TEXT_ID);
                for(int i = 0; i < MODEL_NAMES.length; i++){
                    if(anInterface.textContainsIgnoreCase(MODEL_NAMES[i]))
                        index = i;
                }

                if(index == -1)
                    continue;
                for(int i = 7; i < 48; i++){
                    IComponent iFace = interfaceGroup.getComponent(i);
                    if(iFace.isValid() && iFace.getModelID() == MODEL_IDS[index]){
                        iFace.doClick();
                        sleep(900, 1200);
                        if(!getClient().getValidInterfaceArray()[INTERFACEGROUP_WINDOW_ID]){
                            solved = true;
                            sleep(2000, 4500);
                            continue;
                        }
                    }
                }
            }
            else{
                NPC lady = npcs.getClosest(20, NPC_SANDWICH_LADY_ID);
                if (lady != null) {
                    if (!calculations.isInGameArea(lady.getScreenPos())) {
                        camera.setAngleTo(lady.getLocation());
                    }
                    if (getDistanceTo(lady.getLocation()) > 5) {
                        walking.walkToMM(lady.getLocation());
                    }
                    lady.doAction("Talk-to");
                    sleep(2000, 5000);
                    continue;
                }
            }
            sleep(900, 1200);
        }
        if(!solved){
            game.exitGame();
            stopAllScripts();
            return;
        }
        if (timeout.isDone()) {
            botEnv.scriptManager.stopAllScripts();
        }
    }

}

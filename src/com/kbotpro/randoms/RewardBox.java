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
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Interface;
import com.kbotpro.scriptsystem.wrappers.Item;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by endoskeleton.
 */
public class RewardBox extends Random {
    public final static int REWARD_BOX_ID = 14664;
    public final static int REWARD_BOX_INTERFACE = 202;
    public final static int[] LAMP_ID = { 2528, 11640 };
    public final static int LAMP_INTERFACE_ID = 134;

    private boolean pickLamp;

    public String getName() {
        return "Reward Box";
    }

    public boolean activate() {
        return isLoggedIn() && inventory.contains(REWARD_BOX_ID) ||
                isLoggedIn() && inventory.contains(LAMP_ID);
    }

    public synchronized void onStart() {
        KTimer timeout = new KTimer(300000);
        do {
            int i = loop();
            if (i <= 0) {
                break;
            }
            sleep(i);
        } while (!botEnv.randomManager.scriptStopped && activate() && !timeout.isDone());
        if (interfaces.canContinue()) {
            interfaces.clickContinue();
        }
        if (timeout.isDone()) {
            botEnv.scriptManager.stopAllScripts();
        }
    }


    private synchronized int loop() {
        if (bank.isOpen()) {
            bank.close();
            sleep(1000, 1500);
        }
        if (interfaces.canContinue()) {
            interfaces.clickContinue();
            sleep(1000,1500);
        }
        if (!inventory.isOpen()) {
            game.openTab(Game.TAB_INVENTORY);
        }

        if (game.hasSelectedItem()) {
            menu.atMenu("Cancel");
        }

        if (!activate()) {
            return -1;
        }

        if (inventory.contains(REWARD_BOX_ID)) {
            Interface rewardInterface = interfaces.getInterface(REWARD_BOX_INTERFACE);
            IComponent lampComponent = null;
            IComponent coinsComponent = null;
            IComponent costumeComponent = null;
            IComponent emoteComponent = null;
            ArrayList<IComponent> allChoices = new ArrayList<IComponent>();
            if (rewardInterface != null && rewardInterface.isValid()) {
                for (IComponent element : rewardInterface.getComponent(15).getChildren()) {
                    if (botEnv.botPanel.botName.useLamp && (element.getText().toLowerCase().contains("lamp") || element.getText().toLowerCase().contains("knowledge"))) {
                            lampComponent = element;
                    } else if (element.getText().toLowerCase().contains("costume")) {
                        costumeComponent = element;
                    } else if (element.getText().toLowerCase().contains("emote")) {
                        emoteComponent = element;
                    } else if (element.getText().contains("Coins")) {
                        coinsComponent = element;
                    }
                    String[] actions = element.getActions();
                    if (actions != null && actions.length > 0) {
                        allChoices.add(element);
                    }
                }
                if (lampComponent != null) {
                    chooseReward(lampComponent);
                } else if (costumeComponent != null) {
                    chooseReward(costumeComponent);
                } else if (emoteComponent != null) {
                    chooseReward(emoteComponent);
                } else if (coinsComponent != null) {
                    chooseReward(coinsComponent);
                } else {
                    inventory.drop(REWARD_BOX_ID);
                    sleep(random(1000,1500));
                }
            } else {
                Item[] box = inventory.getItems(REWARD_BOX_ID);
                if (box != null && box.length > 0) {
                    if (box[0].doAction("Open")) {
                        waitForInterface(true, REWARD_BOX_INTERFACE);
                    }
                }
            }
        } else if (inventory.contains(LAMP_ID)) {
            Interface lampInterface = interfaces.getInterface(LAMP_INTERFACE_ID);
            if (lampInterface != null && lampInterface.getComponent(0).isVisible()) {
                for (IComponent i : lampInterface.getComponents()) {
                    if (i.getElementName().contains(botEnv.botPanel.botName.lampIndex)) {
                        i.doClick();
                        lampInterface.getComponent(2).doClick();
                        waitForInterface(false, LAMP_INTERFACE_ID);
                        sleep(random(1200, 2500));
                        break;
                    }
                }
            } else {
                Item[] lamp = inventory.getItems(LAMP_ID);
                if (lamp != null && lamp.length > 0) {
                    if (lamp[0].doClick()) {
                        waitForInterface(true, LAMP_INTERFACE_ID);
                    }
                }
            }
        }
        return random(500, 600);
    }

    private void chooseReward(IComponent iComponent) {
        Interface rewardBox = interfaces.getInterface(REWARD_BOX_INTERFACE);
        if (rewardBox != null) {
            IComponent internalRewardBox = rewardBox.getComponent(15);
            Point center = iComponent.getCenter();
            if (!internalRewardBox.getBounds().contains(center)) {
                Rectangle r = rewardBox.getComponent(24).getChildren()[0].getBounds();
                if (center.y < internalRewardBox.getAbsoluteY()) {
                    mouse.moveMouse(new Point(r.x + random(0, r.width), r.y+random(0,10)), true);
                }
                if (center.y > (internalRewardBox.getAbsoluteY() + internalRewardBox.getHeight())) {
                    mouse.moveMouse(new Point(r.x + random(0, r.width), r.y + r.height - random(0,10)), true);
                }

            }
            iComponent.doClick();
            if (rewardBox.getComponent(28).getText().contains("Confirm")) {
                rewardBox.getComponent(28).doClick();
                sleep(random(3000, 3500));
            }
        }
    }

    private void waitForInterface(boolean open, int interfaceID) {
        for (int i = 0; i < 2000; i += 20) {
            if (open && interfaces.interfaceExists(interfaceID)
                    || !open && !interfaces.interfaceExists(interfaceID)) {
                break;
            }
            sleep(20);
        }
    }

}
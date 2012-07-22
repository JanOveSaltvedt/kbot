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
 * Made by endoskeleton
 */
public class BankPin extends Random {
    private final int BANK_PIN_INTERFACE = 13;

    private Interface pinInterface;
    private IComponent[] wrongEntry;
    private String bankPin;
    private byte retryCount = 2;
    private KTimer timeout;

    public boolean activate() {
        pinInterface = interfaces.getInterface(13);
        wrongEntry = interfaces.getInterfaces("That number was incorrect");
        return (pinInterface != null && pinInterface.getComponent(0).isVisible()) ||
                (wrongEntry != null && wrongEntry.length > 0 && wrongEntry[0].isVisible());
    }

    public String getName() {
        return "Bank Pin";
    }

    public synchronized void onStart() {
        bankPin = botEnv.botPanel.botName.pin;
        if (bankPin.length() != 4) {
            log.logError("No pin was entered. Stopping Scripts");
            stopAllScripts();
            return;
        }

        timeout = new KTimer(120000);
        while (!botEnv.randomManager.scriptStopped && activate() && !timeout.isDone()) {
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

    public int loop() {
        if (!activate()) {
            return -1;
        }

        if (wrongEntry != null && wrongEntry.length > 0 && wrongEntry[0].isVisible()) {
            retryCount++;
            if (retryCount <= 0) {
                log.logError("Bank pin entered wrong or too many mess ups. Ending all Scripts.");
                stopAllScripts();
                return -1;
            }
            interfaces.clickContinue();
            return random(1000,1200);
        }

        setPin();

        return random(500,1000);
    }


    private void setPin() {
        pinInterface = interfaces.getInterface(BANK_PIN_INTERFACE);
        if (pinInterface != null) {
            int index = whereToStart();

            for (int i = 11; i < 21; i++) {

                IComponent iComponent = pinInterface.getComponent(i);
                if (iComponent == null) {
                    break;
                }
                while (iComponent.getBounds().contains(mouse.getMousePos())) {
                    mouse.moveMouseRandomly(random(300,500));
                }
                if (iComponent.getText().equals(bankPin.substring(index, index+1))) {
                    if (pinInterface.getComponent(i-10).doClick()) {
                        while(index == whereToStart() && !timeout.isDone()) {
                            sleep(100);
                        }
                        mouse.moveMouseRandomly(200);
                        sleep(random(1000,1200));
                        break;
                    }
                }
            }
        }
    }

    private int whereToStart() {
        pinInterface = interfaces.getInterface(BANK_PIN_INTERFACE);
        if (pinInterface != null) {
            String list = "";
            for (int i = 21; i < 25; i++) {
                list = list + pinInterface.getComponent(i).getText();
            }
            return list.trim().indexOf("?");
        } else {
            return -1;
        }

    }
}

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

import com.kbotpro.handlers.AccountsManager;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.input.callbacks.HoldKeyCallback;
import com.kbotpro.scriptsystem.input.jobs.HoldKeyJob;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Interface;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by endoskeleton.
 */
public class AutoLogin extends Random implements PaintEventListener {
    private final int WORLD_PARENT = 910;
    private final int LOGIN_PARENT = 905;
    private final int LOBBY_PARENT = 906;

    int targetWorldNumber = -1;//used for the world select, which I can improve upon later
    String targetWorldType;

    private Font paintFont = new Font("Times New Roman", Font.BOLD, 32);
    private boolean askedForAccount = false;

    public void onRepaint(Graphics graphics) {
        if (isRunning()) {
            Graphics2D g = (Graphics2D) graphics;
            Font font = g.getFont();
            g.setFont(paintFont);
            g.setColor(Color.GREEN);
            g.drawString("Logging in!", 40, 40);
            g.setFont(font);
        }
    }

    public boolean activate() {
        final int state = game.getGameState();
        return state >= 2 && state <= 6 && !botEnv.botPanel.botName.getUsername().toLowerCase().contains("null");
    }


    public synchronized int loop(String username, String password, boolean members) {
        switch (game.getGameState()) {
            case 2: //Log in
                login(username, password, members);
                break;

            case 4: //This happens in between the login and lobby
                return random(300, 500);

            case 6: //Inside the lobby

                //Error messages, switching worlds to quickly.
                Interface lobby = interfaces.getInterface(LOBBY_PARENT);
                if (lobby.getComponent(117) != null && lobby.getComponent(117).isVisible()) {
                    if (!lobby.getComponent(117).getText().contains("YOU HAVE ONLY JUST LEFT") &&
                            lobby.getComponent(119) != null) {
                        lobby.getComponent(119).doClick();
                    }
                }

                if (lobby.getComponent(68).getText() != null && lobby.getComponent(68).getText().contains("To enjoy members")) {
                    lobby.getComponent(69).doClick();
                    return random(300, 500);
                }
                //selectWorld(false);

                IComponent clickToPlay = interfaces.getComponent(LOBBY_PARENT, 170);
                if (clickToPlay != null) {
                    clickToPlay.doClick();
                }
                break;

            case 9: //Logged in
                return -1;

            default: //Not sure if I should stop the scripts if an unknown state come sup
                break;
        }

        return 1000;
    }

    void login(String username, String password, boolean members) {
        Interface parent = interfaces.getInterface(LOGIN_PARENT);
        if (parent == null || !parent.isValid()) {
            return;
        }
        if (parent.getComponent(38).isVisible() && members) {

            if (parent.getComponent(38).doClick()) {
                while (parent.getComponent(38).isVisible()) {
                    sleep(10);
                }
            }
        }
        if (parent.getComponent(59).isVisible() && !members) {
            if (parent.getComponent(59).doClick()) {
                while (parent.getComponent(59).isVisible()) {
                    sleep(10);
                }
            }
        }
        parent = interfaces.getInterface(596);
        boolean completeDelete = false;
        if (parent.getComponent(30).isVisible()) {
            String invalid = parent.getComponent(30).getText();
            if (invalid != null && invalid.toLowerCase().contains("invalid username")) {
                parent.getComponent(36).doClick();
                completeDelete = true;
            }
        }
        IComponent userNameBox = parent.getComponent(71);
        IComponent passwordBox = parent.getComponent(91);
        String usernameString = userNameBox.getText().replace("|", "").trim();
        String passwordString = passwordBox.getText().replace("|", "").trim();
        if (!usernameString.equals(username) || completeDelete) {
            userNameBox.doClick();
            if (usernameString.length() > 0) {
                backSpace();
            }
            keyboard.writeText(username);
            return;
        }
        if (passwordString.length() != password.length() || completeDelete) {
            passwordBox.doClick();
            if (passwordString.length() > 0) {
                backSpace();
            }
            keyboard.writeText(password);
        } else {
            parent.getComponent(87).doClick();
            sleep(random(1000, 2000));
        }
    }

    void backSpace() {
        final KTimer t = new KTimer(random(800, 1000));
        HoldKeyJob bs = keyboard.createHoldKeyJob(KeyEvent.VK_BACK_SPACE, new HoldKeyCallback() {
            public void update(HoldKeyJob holdKeyJob) {
                if (t.isDone()) {
                    holdKeyJob.stopHolding();
                }
            }

            public void onFinished(HoldKeyJob holdKeyJob) {
            }
        });
        bs.start();
        bs.join();
        while (bs.isAlive()) {
            sleep(10);
        }
    }

    void selectWorld(boolean members) {

        if (interfaces.componentExists(LOBBY_PARENT, 117) && interfaces.getComponent(LOBBY_PARENT, 117).isVisible()) {
            if (!interfaces.getComponent(LOBBY_PARENT, 117).getText().contains("YOU HAVE ONLY JUST LEFT") &&
                    interfaces.getComponent(LOBBY_PARENT, 119) != null) {
                interfaces.getComponent(LOBBY_PARENT, 119).doClick();
            }
            return;
        }
        if (interfaces.getComponent(WORLD_PARENT, 0) != null &&
                !interfaces.getComponent(WORLD_PARENT, 0).isVisible() && interfaces.componentExists(LOBBY_PARENT, 183)) {
            if (interfaces.getComponent(LOBBY_PARENT, 183).doClick()) {
                sleep(random(1000, 1500));
            }
        }
        Interface parent = interfaces.getInterface(WORLD_PARENT);
        if (parent == null || !parent.isValid()) {
            return;
        }
        if (targetWorldNumber == -1) {
            IComponent[] worldActivity = parent.getComponent(71).getChildren();
            IComponent[] worldNumber = parent.getComponent(68).getChildren();
            IComponent[] worldType = parent.getComponent(73).getChildren();
            IComponent[] worldPopulation = parent.getComponent(70).getChildren();
            ArrayList<Integer> potentialWorlds = new ArrayList<Integer>();

            for (int i = 0; i < worldActivity.length; i++) {
                if (!worldActivity[i].getText().toLowerCase().contains("pvp") &&
                        !worldActivity[i].getText().toLowerCase().contains("bounty") &&
                        !worldPopulation[i].getText().contains("Offline")) {
                    if (worldType[i].getText().contains("Members") && members) {
                        potentialWorlds.add(i);
                    }
                    if (!worldType[i].getText().contains("Members") && !members) {
                        potentialWorlds.add(i);
                    }
                }
            }
            targetWorldNumber = potentialWorlds.get(random(0, potentialWorlds.size() - 1));
            targetWorldType = worldNumber[targetWorldNumber].getText();
        }
        IComponent targ = parent.getComponent(76).getChildren()[targetWorldNumber];
        if (!parent.getComponent(10).getText().contains(targetWorldType)) {
            if (parent.getComponent(61).getBounds().contains(targ.getCenter())) {
                targ.doClick();
            } else {
                IComponent bigBox = parent.getComponent(62);
                double percent = (targ.getAbsoluteY() / (bigBox.getBounds().getHeight() + bigBox.getAbsoluteY()));
                IComponent scrollBar = parent.getComponent(85).getChildren()[0];
                Point p = new Point(scrollBar.getAbsoluteX() + random(0, scrollBar.getWidth()),
                        (int) (scrollBar.getAbsoluteY() + (scrollBar.getHeight() * percent)));
                mouse.moveMouse(p, true);
            }
        } else {
            IComponent finish = interfaces.getComponent(LOBBY_PARENT, 179);
            if (finish != null) {
                finish.doClick();
            }
        }
    }

    public synchronized void onStart() {
        AccountsManager.Account account = botEnv.botPanel.botName;
        if (account == null) {
            return;
        }
        final String username = account.getUsername();
        final String password = account.getPassword();
        KTimer timeout = new KTimer(600000); //10 minutes
        while (!botEnv.randomManager.scriptStopped && activate() && !timeout.isDone()) {
            int i = loop(username, password, account.membersAccount);
            if (i <= 0) {
                break;
            }
            sleep(i);
        }
        if (timeout.isDone()) {
            botEnv.scriptManager.stopAllScripts();
        }
    }

    @Override
    public String getName() {
        return "AutoLogin";
    }
}
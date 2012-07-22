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

package com.kbotpro.utils;

import com.kbotpro.handlers.AccountsHandler;
import com.kbotpro.handlers.AccountsManager;
import com.kbotpro.interfaces.PaintCallback;
import com.kbotpro.ui.BotPanel;
import com.kbotpro.various.StaticStorage;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.applet.Applet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 10.aug.2009
 * Time: 11:42:13
 */
public class BotControl {
    public static java.util.List<BotPanel> bots = new LinkedList<BotPanel>();

    /**
     * Adds a bot to the tabbed pane and sets it in focus.
     *
     * @param account
     */
    /*public static void addBot(String name) {
        BotPanel botPanel = new BotPanel(name);
        bots.add(botPanel);

        StaticStorage.mainForm.mainTabbedPane.addTab(name, botPanel);
        StaticStorage.mainForm.mainTabbedPane.setSelectedComponent(botPanel);
        StaticStorage.mainForm.mainTabbedPane.updateUI();
    }         */

    public static void addBot(AccountsManager.Account account) {
        if (account == null) {
            BotPanel botPanel = new BotPanel(null);
            StaticStorage.mainForm.mainTabbedPane.addTab("null", botPanel);
            StaticStorage.mainForm.mainTabbedPane.setSelectedComponent(botPanel);
            StaticStorage.mainForm.mainTabbedPane.updateUI();
            return;
        }
        BotPanel botPanel = new BotPanel(account);
        bots.add(botPanel);
        StaticStorage.mainForm.mainTabbedPane.addTab(account.getUsername(), botPanel);
        StaticStorage.mainForm.mainTabbedPane.setSelectedComponent(botPanel);
        StaticStorage.mainForm.mainTabbedPane.updateUI();
        //botPanel.botEnvironment.accountsHandler.setAccount(account);
    }

    public static BotPanel setClassLoader(ClassLoader classLoader){
        if(bots.isEmpty()){
            return null;
        }

        final ClassLoader parrentClassLoader = classLoader.getClass().getClassLoader();
        for(BotPanel bot: bots){
            if(bot == null)
                continue;
            if(!bot.hasClassLoader()){
                if(bot.getLoaderClassLoader() == parrentClassLoader){
                    bot.setClassLoader(classLoader);
                    return bot;
                }
            }
        }
        return null;
    }

    public static BotPanel getAppletOwner(Applet applet){
        if(bots.isEmpty()){
            return null;
        }
        ClassLoader appletClassLoader = applet.getClass().getClassLoader();
        for(BotPanel bot: bots){
            if(bot == null)
                continue;

            if(bot.botClassLoader == appletClassLoader){
                return bot;
            }
        }
        return null;
    }

    public static PaintCallback getPaintCallback(Canvas canvas) {
        if (bots.isEmpty()) {
            return null;
        }
        ClassLoader classLoader = canvas.getClass().getClassLoader();

        for(BotPanel bot: bots){
            if(bot == null){
                continue;
            }
            if(bot.hasClassLoader()){
                if(bot.botClassLoader == classLoader){
                    return bot;
                }
            }
        }
        return null;
    }
}

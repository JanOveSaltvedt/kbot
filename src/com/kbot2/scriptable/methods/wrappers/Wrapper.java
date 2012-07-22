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

package com.kbot2.scriptable.methods.wrappers;

import com.kbot2.bot.BotEnvironment;
import com.kbot2.scriptable.methods.Calculations;
import com.kbot2.scriptable.methods.Methods;
import com.kbotpro.hooks.Client;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 3:09:14 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Wrapper {
    protected BotEnvironment botEnv;
    /*
    public Mouse mouse;
    public Keyboard keyboard;
    public Players players;
    public NPCs npcs;
    public Interfaces interfaces;
    public Objects objects;
    public Menu menu;
    public Bank bank;
    public Walking walking;
    public Settings settings;
    public Inventory inventory;
    public GameScreen gameScreen;
    public Camera camera;
    public Skills skills; */

    protected Wrapper(BotEnvironment botEnv) {
        this.botEnv = botEnv;
        /*
        mouse = botEnv.mouse;
        keyboard = botEnv.keyboard;
        players = botEnv.players;
        npcs = botEnv.npcs;
        interfaces = botEnv.interfaces;
        objects = botEnv.objects;
        menu = botEnv.menu;
        bank = botEnv.bank;
        walking = botEnv.walking;
        settings = botEnv.settings;
        inventory = botEnv.inventory;
        gameScreen = botEnv.gameScreen;
        camera = botEnv.camera;
        skills = botEnv.skills;*/
    }

    public Client getClient(){
        return botEnv.getClient();
    }

    public Methods getMethods(){
        return botEnv.methods;
    }

    public Calculations getCalculations(){
        return botEnv.calculations;
    }
}

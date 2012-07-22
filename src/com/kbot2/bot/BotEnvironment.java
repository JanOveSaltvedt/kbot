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

package com.kbot2.bot;

import com.kbot2.scriptable.methods.Calculations;
import com.kbot2.scriptable.methods.Methods;
import com.kbot2.scriptable.methods.data.*;
import com.kbot2.scriptable.methods.input.Keyboard;
import com.kbot2.scriptable.methods.input.Mouse;
import com.kbotpro.hooks.Client;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 3:06:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class BotEnvironment {
    public com.kbotpro.bot.BotEnvironment proBotEnvironment;
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
    public Methods methods;
    public Calculations calculations;
    public GroundItems groundItems;
    public Camera camera;
    private boolean CPUSaving;
    private int CPUWait;
    public double mouseSpeed = 1.0D;
    public Skills skills;


    public BotEnvironment(com.kbotpro.bot.BotEnvironment proBotEnvironment) {
        this.proBotEnvironment = proBotEnvironment;

        this.mouse = new Mouse(this);
        this.keyboard = new Keyboard(this);
        this.players = new Players(this);
        this.npcs = new NPCs(this);
        this.interfaces = new Interfaces(this);
        this.objects = new Objects(this);
        this.menu = new Menu(this);
        this.bank = new Bank(this);
        this.walking = new Walking(this);
        this.settings = new Settings(this);
        this.inventory = new Inventory(this);
        this.gameScreen = new GameScreen(this);
        this.calculations = new Calculations(this);
        this.groundItems = new GroundItems(this);
        this.camera = new Camera(this);
        this.skills = new Skills(this);



        // Must always be last
        this.methods = new Methods();
        this.methods.setBotEnv(this);

        players.setFields();
        npcs.setFields();
        interfaces.setFields();
        objects.setFields();
        menu.setFields();
        bank.setFields();
        walking.setFields();
        settings.setFields();
        inventory.setFields();
        gameScreen.setFields();
        groundItems.setFields();
        camera.setFields();
        skills.setFields();

    }

    public Client getClient() {
        return proBotEnvironment.client;
    }
}

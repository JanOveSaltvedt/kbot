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



package com.kbotpro.bot;

import com.kbotpro.handlers.AccountsHandler;
import com.kbotpro.handlers.RandomManager;
import com.kbotpro.scriptsystem.events.KPaintEventListener;
import com.kbotpro.scriptsystem.events.RandomListener;
import com.kbotpro.scriptsystem.fetch.tabs.Inventory;
import com.kbotpro.scriptsystem.runnable.Script;
import com.kbotpro.scriptsystem.various.Screenshot;
import com.kbotpro.ui.BotPanel;
import com.kbotpro.hooks.Client;
import com.kbotpro.scriptsystem.various.Log;
import com.kbotpro.scriptsystem.Calculations;
import com.kbotpro.scriptsystem.input.Keyboard;
import com.kbotpro.scriptsystem.input.Mouse;
import com.kbotpro.scriptsystem.fetch.*;
import com.kbotpro.scriptsystem.fetch.Menu;
import com.kbotpro.scriptsystem.fetch.tabs.Equipment;
import com.kbotpro.scriptsystem.fetch.Settings;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.events.ServerMessageListener;
import com.kbotpro.handlers.ScriptManager;
import com.kbotpro.handlers.DebuggerManager;
import com.kbotpro.various.ScriptClassLoader;
import com.kbotpro.various.StaticStorage;

import javax.swing.*;
import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 10.sep.2009
 * Time: 18:11:29
 */
public class BotEnvironment {
    public BotPanel botPanel;
    public Applet gameApplet;
    public Client client;
    public ClassLoader clientClassLoader;
    public Log log;
    public Calculations calculations;
    public NPCs npcs;
    public Players players;
    public Objects objects;
    public RenderData renderData = new RenderData();
    public Menu menu;
    public Interfaces interfaces;
    public Settings settings;
    public Camera camera;
    public Inventory inventory;
    public Bank bank;
    public Walking walking;
    public Skills skills;
    public Game game;
    public DebuggerManager debuggerManager;
    public Keyboard keyboard;
    public Mouse mouse;
    public BotSettings botSettings;
    public GroundItems groundItems;
    public Equipment equipment;
    public Chat chat;
    public Screenshot screenshot;
    public GrandExchange grandExchange;
    public Shop shop;

    public PaintEventListener paintEventMulticaster;
    public ScriptManager scriptManager = new ScriptManager(this);
    public RandomManager randomManager;
    public ExecutorService executorService = Executors.newCachedThreadPool();
    public ServerMessageListener serverMessageMulticaster;
    public MouseMotionListener mouseMotionMulticaster;
    public MouseListener mouseMulticaster;
	public KPaintEventListener kPaintEventMulticaster;
    public RandomListener randomMulticaster;
    public AccountsHandler accountsHandler;


	public BotEnvironment(BotPanel botPanel) {
        this.botPanel = botPanel;
        gameApplet = botPanel.botApplet;
        client = botPanel.client;
        clientClassLoader = botPanel.botClassLoader;
        log = new Log(this);
        calculations = new Calculations(this);
        npcs = new NPCs(this);
        players = new Players(this);
        objects = new Objects(this);
        menu = new Menu(this);
        interfaces = new Interfaces(this);
        settings = new Settings(this);
        camera = new Camera(this);
        inventory = new Inventory(this);
        bank = new Bank(this);
        walking = new Walking(this);
        skills = new Skills(this);
        game = new Game(this);
        keyboard = new Keyboard(this);
        mouse = new Mouse(this);
        botSettings = new BotSettings(this);
        groundItems = new GroundItems(this);
        equipment = new Equipment(this);
		chat = new Chat(this);
        screenshot = new Screenshot(this);
        grandExchange = new GrandExchange(this);
        shop = new Shop(this);
        
        debuggerManager = new DebuggerManager(this);
        debuggerManager.updateMenu();
        randomManager = new RandomManager(this);
        accountsHandler = new AccountsHandler(this);
    }

    public int appletWidth = 765;
    public int appletHeight = 503;

    public void dispatchEvent(AWTEvent e) {
        //System.out.println("Time: "+System.currentTimeMillis()+" "+e);
        if(e instanceof KeyEvent){
            KeyEvent ke = (KeyEvent) e;
            KeyListener keyListener = client.getKeyListener();
            if(keyListener == null){
                return;
            }
            int id = ke.getID();
            if(id == KeyEvent.KEY_PRESSED){
                keyListener.keyPressed(ke);
            }
            else if(id == KeyEvent.KEY_RELEASED){
                keyListener.keyReleased(ke);
            }
            else if(id == KeyEvent.KEY_TYPED){
                keyListener.keyTyped(ke);
            }
        }
        else if(e instanceof MouseEvent){
            MouseEvent mouseEvent = (MouseEvent) e;
            int id = mouseEvent.getID();
            if(id == MouseEvent.MOUSE_MOVED || id == MouseEvent.MOUSE_DRAGGED){
                MouseMotionListener mouseMotionListener = client.getMouseMotionListener();
                if(mouseMotionListener == null){
                    return;
                }
                if(id == MouseEvent.MOUSE_MOVED){
                    mouseMotionListener.mouseMoved(mouseEvent);
                }
                else {
                    mouseMotionListener.mouseDragged(mouseEvent);
                }
            }
            else{
                MouseListener mouseListener = client.getMouseListener();
                if(mouseListener == null){
                    return;
                }
                if(id == MouseEvent.MOUSE_CLICKED){
                    mouseListener.mouseClicked(mouseEvent);
                }
                else if(id == MouseEvent.MOUSE_PRESSED){
                    mouse.mousePressed = true;
                    mouseListener.mousePressed(mouseEvent);
                }
                else if(id == MouseEvent.MOUSE_RELEASED){
                    mouse.mousePressed = false;
                    mouseListener.mouseReleased(mouseEvent);
                }
                else if(id == MouseEvent.MOUSE_ENTERED){
                    mouseListener.mouseEntered(mouseEvent);
                }
                else if(id == MouseEvent.MOUSE_EXITED){
                    mouseListener.mouseExited(mouseEvent);
                }
            }

        }
    }

    public Class[] getAllClassesInScript(Script script){
        ClassLoader classLoader = script.getClass().getClassLoader();
        if(classLoader instanceof ScriptClassLoader){
            return ((ScriptClassLoader)classLoader).getAllLoadedClasses();
        }

        return new Class[0];
    }

    public JFrame getMainUI(){
        return StaticStorage.mainForm;
    }

    public void disposeBot() {
        scriptManager.disposeResources();
        debuggerManager.disposeResources();
        randomManager.disposeResources();
        executorService.shutdownNow();

        botPanel = null;
        client = null;
        clientClassLoader = null;
        mouse = null;
        keyboard = null;
        
    }
}

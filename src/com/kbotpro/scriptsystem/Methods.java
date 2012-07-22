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



package com.kbotpro.scriptsystem;

import com.kbotpro.Version;
import com.kbotpro.scriptsystem.events.KPaintEventListener;
import com.kbotpro.scriptsystem.events.RandomListener;
import com.kbotpro.scriptsystem.fetch.tabs.Equipment;
import com.kbotpro.scriptsystem.fetch.Settings;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.various.Log;
import com.kbotpro.scriptsystem.fetch.*;
import com.kbotpro.scriptsystem.fetch.Menu;
import com.kbotpro.scriptsystem.fetch.tabs.Inventory;
import com.kbotpro.scriptsystem.input.Keyboard;
import com.kbotpro.scriptsystem.input.Mouse;
import com.kbotpro.scriptsystem.wrappers.Player;
import com.kbotpro.scriptsystem.interfaces.WorldLocation;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.bot.BotEventMulticaster;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.events.ServerMessageListener;
import com.kbotpro.scriptsystem.wrappers.Tile;
import com.kbotpro.various.StaticStorage;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

/**
 * Abstract class every workerContainer should derive from to get access to methods
 */
public abstract class Methods extends ModuleConnector {
    protected Calculations calculations;
    protected NPCs npcs;
    protected Log log;
    protected Players players;
    protected Objects objects;
    protected Menu menu;
    protected Interfaces interfaces;
    protected Settings settings;
    protected Camera camera;
    protected Inventory inventory;
    protected Bank bank;
    protected Game game;
    protected Skills skills;
    protected Walking walking;
    protected Keyboard keyboard;
    protected Mouse mouse;
    protected BotSettings botSettings;
    protected GroundItems groundItems;
    protected Equipment equipment;
    protected Chat chat;
    protected Shop shop;

	/**
     * This method is reflected by the bots right after innit to set the bot environment
     * @param botEnv the botEnvironment
     */
    private void registerInternal(BotEnvironment botEnv){
        super.botEnv = botEnv;
        calculations = botEnv.calculations;
        npcs = botEnv.npcs;
        log = botEnv.log;
        players = botEnv.players;
        objects = botEnv.objects;
        menu = botEnv.menu;
        interfaces = botEnv.interfaces;
        settings = botEnv.settings;
        camera = botEnv.camera;
        inventory = botEnv.inventory;
        bank = botEnv.bank;
        game = botEnv.game;
        skills = botEnv.skills;
        walking = botEnv.walking;
        keyboard = botEnv.keyboard;
        mouse = botEnv.mouse;
        botSettings = botEnv.botSettings;
        groundItems = botEnv.groundItems;
        equipment = botEnv.equipment;
	    chat = botEnv.chat;
        shop = botEnv.shop;

        if(this instanceof PaintEventListener){
            addPaintEventListener((PaintEventListener) this);
        }
        if(this instanceof ServerMessageListener){
            addServerMessageListener((ServerMessageListener) this);
        }
        if(this instanceof MouseMotionListener){
            addMouseMotionListener((MouseMotionListener) this);
        }
        if(this instanceof MouseListener){
            addMouseListener((MouseListener) this);
        }
	    if(this instanceof KPaintEventListener){
		    botEnv.kPaintEventMulticaster = BotEventMulticaster.add(botEnv.kPaintEventMulticaster, (KPaintEventListener)this);
	    }
        if(this instanceof RandomListener){
            addRandomListener((RandomListener) this);
        }
    }

    public void addRandomListener(RandomListener listener) {
        botEnv.randomMulticaster = BotEventMulticaster.add(botEnv.randomMulticaster, listener);
    }

    public void addMouseListener(MouseListener listener) {
        botEnv.mouseMulticaster = AWTEventMulticaster.add(botEnv.mouseMulticaster, listener);
    }

    public void addMouseMotionListener(MouseMotionListener listener) {
        botEnv.mouseMotionMulticaster = AWTEventMulticaster.add(botEnv.mouseMotionMulticaster, listener);
    }

    public void addServerMessageListener(ServerMessageListener listener) {
        botEnv.serverMessageMulticaster = BotEventMulticaster.add(botEnv.serverMessageMulticaster, listener);
    }

    public void addPaintEventListener(PaintEventListener listener) {
        botEnv.paintEventMulticaster = BotEventMulticaster.add(botEnv.paintEventMulticaster, listener);
    }

    /**
     * This method is reflected internally by the bot to remove event listeners and similar.
     */
    private void deregisterInternal(){
        if(this instanceof PaintEventListener){
            removePaintEventListener((PaintEventListener) this);
        }
        if(this instanceof ServerMessageListener){
            removeServerMessageListener((ServerMessageListener) this);
        }
        if(this instanceof MouseListener){
            removeMouseListener((MouseListener) this);
        }
	    if(this instanceof MouseMotionListener){
            removeMouseMotionListener((MouseMotionListener) this);
        }
	    if(this instanceof KPaintEventListener){
		    botEnv.kPaintEventMulticaster = BotEventMulticaster.remove(botEnv.kPaintEventMulticaster, (KPaintEventListener)this);
	    }
        if(this instanceof RandomListener){
            removeRandomListener((RandomListener) this);
        }
        
        calculations = null;
        npcs = null;
        log = null;
        players = null;
        objects = null;
        menu = null;
        interfaces = null;
        settings = null;
        camera = null;
        inventory = null;
        bank = null;
        game = null;
        skills = null;
        walking = null;
        keyboard = null;
        mouse = null;
        botSettings = null;
        groundItems = null;
        equipment = null;
        shop = null;
    }

    public void removeRandomListener(RandomListener listener) {
        botEnv.randomMulticaster = BotEventMulticaster.remove(botEnv.randomMulticaster, listener);
    }

    public void removeMouseMotionListener(MouseMotionListener listener) {
        botEnv.mouseMotionMulticaster = AWTEventMulticaster.remove(botEnv.mouseMotionMulticaster, listener);
    }

    public void removeMouseListener(MouseListener listener) {
        botEnv.mouseMulticaster = AWTEventMulticaster.remove(botEnv.mouseMulticaster, listener);
    }

    public void removeServerMessageListener(ServerMessageListener listener) {
        botEnv.serverMessageMulticaster = BotEventMulticaster.remove(botEnv.serverMessageMulticaster, listener);
    }

    public void removePaintEventListener(PaintEventListener listener) {
        botEnv.paintEventMulticaster = BotEventMulticaster.remove(botEnv.paintEventMulticaster, listener);
    }

    /**
     * Gets the current player from the clients cache.
     * @return returns the player or null under rare circumstances.
     */
    public Player getMyPlayer(){
        return players.getMyPlayer();
    }

    /**
     * Calculates the distance from my player to the given worldLocation. (Which is implemented by Character, PhysicalObject and sio on)
     * @param worldLocation 
     * @return
     */
    public double getDistanceTo(WorldLocation worldLocation){
        return getMyPlayer().getLocation().distanceToPrecise(worldLocation);
    }

    /**
     * Gets the location of the player.
     * @return
     */
    public Tile getLocation(){
        return getMyPlayer().getLocation();
    }

    /**
     * Gets the current health points.
     * @return
     */
    public int getHP(){
        return game.getHealth();
    }

    /**
     * Puts a message on the log
     * @param message
     */
    public void log(String message){
        log.log(message);
    }
    /**
     * Checks if we are logged in.
     * @return
     */
    public boolean isLoggedIn(){
        return game.isLoggedIn();
    }

    /**
     * Gets random number between min and max (exclusive)
     * @param min
     * @param max
     * @return
     */
    public int random(int min, int max){
        return ((int) (Math.random() * (max - min))) + min;
    }

    /**
     * Gets random number between min and max (exclusive)
     * @param min
     * @param max
     * @return
     */
    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    /**
     * Makes the current thread sleep for given amount of milliseconds.
     *
     * 1 second = 1000 milliseconds
     * @param ms
     */
    public void sleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Makes the current thread sleep a random time between the two parameters.
     * @param min
     * @param max
     */
    public void sleep(int min, int max){
        sleep(random(min, max));
    }

    /**
     * Returns whether the user is able to interact with the game using the mouse
     * @return
     */
    public boolean isUserInputEnabled(){
        return botEnv.botPanel.isInputEnabled();
    }

    /**
     * sets whether the user is able to interact with the game using the mouse
     * @param enabled
     */
    public void setUserInputEnabled(boolean enabled){
        botEnv.botPanel.setInputEnabled(enabled);
    }

    /**
     * Enables or disables an antirandom based on the name.
     * @param enabled
     * @param name Name of the antirandom; doesn't need to be full name, not case sensitive 
     */
    public void enableRandom(boolean enabled, String name) {
       for (Random random : botEnv.randomManager.loadedRandoms) {
           if (random.getName().toLowerCase().contains(name.toLowerCase())) {
                random.setEnabled(enabled);
           }
       }
    }

    /**
     * Enabled or disables the default mouse indicator.
     * @param enabled
     */
    public void enableMouseIndicator(boolean enabled) {
        botEnv.botPanel.settingMouseIndicator.setSelected(enabled);
    }

    public String getForumUsername(){
        return StaticStorage.userStorage.getUsername();
    }

    public double distanceTo(WorldLocation worldLocation){
        return getMyPlayer().getLocation().distanceTo(worldLocation);
    }

    public void stopAllScripts() {
        botEnv.scriptManager.stopAllScripts();
    }

    /**
     * Gets the current build number for KBot.
     * 
     * @return
     */
    int getBuild() {
        return Integer.parseInt(Version.build);
    }
}

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

package com.kbot2.scriptable.methods;

import com.kbot2.bot.BotEnvironment;
import com.kbot2.scriptable.methods.data.*;
import com.kbot2.scriptable.methods.data.Menu;
import com.kbot2.scriptable.methods.input.Keyboard;
import com.kbot2.scriptable.methods.input.Mouse;
import com.kbot2.scriptable.methods.interfaces.WorldObject;
import com.kbot2.scriptable.methods.wrappers.InterfaceGroup;
import com.kbot2.scriptable.methods.wrappers.Obj;
import com.kbot2.scriptable.methods.wrappers.Player;
import com.kbot2.scriptable.methods.wrappers.Tile;
import com.kbotpro.hooks.Client;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 3:05:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Methods {
    public BotEnvironment botEnv;
	private Client client;
	public Players players;
	public NPCs npcs;
	public Interfaces interfaces;
	public Objects objects;
	public Menu menu;
	public Mouse mouse;
	public Keyboard keyboard;
	public Bank bank;
	public Walking walking;
	public Settings settings;
	public Inventory inventory;
	public GameScreen gameScreen;
	public GroundItems groundItems;
	public Camera camera;
	public Skills skills;

	/**
	 * Writes text onto the console on the bottom of the bot.
	 *
	 * @param text text to send.
	 */
	public void log(String text) {
		botEnv.proBotEnvironment.log.log(text);
	}

	/**
	 * Gets the bots client instance.
	 *
	 * @return instance of the client.
	 */
	public Client getClient() {
		if (client == null)
			client = botEnv.getClient();
		return client;
	}

	/**
	 * Shortcut for getMyPlayer().getLocation().distanceTo(Tile tile);
	 * @param wo Tile to get distance to. Can also be a Character, Object, NPC or anything else that implements WorldObject
	 * @return int: the distance in tiles from my player to tile.
	 */
	public int distanceTo(WorldObject wo){
		return players.distanceTo(wo);
	}

	/**
	 * Gets my players location.
	 * @return tile: location of my player.
	 */
	public Tile getLocation(){
		return players.getMyPlayer().getLocation();
	}

	/**
	 * Moves the mouse to the specified position on screen.
	 *
	 * @param p Point with the destination on screen.
	 */
	public void moveMouse(Point p) {
		mouse.moveMouse(p.x, p.y);
	}

	public int getIdleTime(){
		return -1; // TODO implement
	}

	/**
	 * Moves the mouse with randomness
	 *
	 * @param p	   Point with destination
	 * @param randomX maximum randomness on X axes
	 * @param randomY maximum randomness on Y axes
	 */
	public void moveMouse(Point p, int randomX, int randomY) {
		mouse.moveMouse(p, randomX, randomY);
	}

	/**
	 * Sends a text to the client in human like speed.
	 *
	 * @param txt		String: Text to send.
	 * @param pressEnter boolean: Press enter after typing?
	 */
	public void sendText(String txt, boolean pressEnter) {
		keyboard.sendKeys(txt, pressEnter);
	}

	/**
	 * Gets my player
	 *
	 * @return if null something wierd happened...
	 */
	public Player getMyPlayer() {
		return players.getMyPlayer();
	}

	/**
	 * Gets the object at a given position.
	 * That is if it is loaded in client.
	 *
	 * @param tile the object is on.
	 * @return returns null if no such object is found...
	 */
	public Obj getObjectAt(Tile tile) {
		return objects.getObjectAt(tile);
	}

	/**
	 * Gets the closest object with given ID
	 *
	 * @param range int range in tiles to search within.
	 * @param IDs   int or int array of IDs to search from.
	 * @return null if not foud within range.
	 */
	public Obj getClosestObject(int range, int... IDs) {
		return objects.getClosestObject(range, IDs);
	}

	/**
	 * Clicks the mousebutton specified in button.
	 * Left = true
	 * Right = false
	 *
	 * @param button boolean: true = left click, false = right click
	 */
	public void clickMouse(boolean button) {
		mouse.clickMouse(button);
	}

	/**
	 * Moves the mouse and clicks at the given position.
	 *
	 * @param p	  Point to move the mouse to.
	 * @param button boolean: true = left click, false = right click
	 */
	public void clickMouse(Point p, boolean button) {
		mouse.clickMouse(p, 0, 0, button);
	}

	/**
	 * Moves the mouse with randomness and clicks.
	 *
	 * @param p	   Point to move the mouse to
	 * @param randomX maximum randomness X axes
	 * @param randomY maximum randomness Y axes
	 * @param button  boolean: true = left click, false = right click
	 */
	public void clickMouse(Point p, int randomX, int randomY, boolean button) {
		mouse.clickMouse(p, randomX, randomY, button);
	}

	/**
	 * Makes the thread sleep for given time in milliseconds
	 *
	 * @param ms int: time in milliseconds to wait.
	 */
	public void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ignored) {
		}
	}

	/**
	 * Makes the thread sleep for a random time between the min and max.
	 *
	 * @param min min time to wait. Should not be negative
	 * @param max max time to wait.
	 */
	public void sleep(int min, int max) {
		sleep(random(min, max));
	}

	/**
	 * Returns a random number between min and max.
	 *
	 * @param min min random number.
	 * @param max max random number.
	 * @return int: the random number.
	 */
	public int random(int min, int max) {
		return Calculations.random(min, max);
	}

	/**
	 * The method checks if the menu item exist.
	 * If at first pos it ledt clicks, if not it open menu and clicks the option
	 *
	 * @param actionContains incasesensitive string containing action name.
	 *					   If it find 2 or more of the same, it will take the first.
	 * @return if it managed to do the action
	 */
	public boolean atMenu(String actionContains) {
		return menu.atMenu(actionContains);
	}

	/**
	 * Gets the count of items in the inventory
	 *
	 * @return int: count of items in inventory.
	 */
	public int getInventoryCount() {
		return inventory.getCount();
	}

	/**
	 * Drops the items specified in itemIDs
	 *
	 * @param itemIDs int array of item IDs to drop. Or single int of one item.
	 */
	public void drop(int... itemIDs) {
		inventory.drop(itemIDs);
	}

	/**
	 * Gets the game state. Mainly used to check if your logged in.
	 *
	 * @return int: gamestate int
	 */
	public int getGameState() {
		Client client = getClient();
		if (client == null)
			return 0;
		return client.getGameState();
	}

	/**
	 * Checks if the user is logged in.
	 *
	 * @return boolean: logged in?
	 */
	public boolean isLoggedIn() {
		Client client = getClient();
		if (client == null)
			return false;
		int gameState = client.getGameState();
		return gameState == 30 || gameState == 25;
	}

	/**
	 * Gets a interface group
	 *
	 * @param ID int with the ID of the interface group
	 * @return null if the interface don't exist.
	 */
	public InterfaceGroup getInterfaceGroup(int ID) {
		return interfaces.getInterfaceGroup(ID);

	}

	/**
	 * Holds a key down a given amount of time.
	 *
	 * @param keyCode key code is for example KeyEvent.VK_LEFT
	 * @param millis  milliseconds to hold the key.
	 */
	public void holdKey(int keyCode, int millis) {
		keyboard.holdKey(keyCode, millis);
	}

	/**
	 * Gets the mouse position
	 *
	 * @return Point with coords.
	 */
	public Point getMousePos() {
		return botEnv.proBotEnvironment.mouse.getMousePos();
	}

	/**
	 * Gets an instance of calculations.
	 * Used for making more specified methods.
	 *
	 * @return Instance of Calculations
	 */
	public Calculations getCalculations() {
		return botEnv.calculations;
	}

	/**
	 * Sets the mouse speed for the bot
	 * @param speed double: Lower value gives faster mouse movements.
	 */
	public void setMouseSpeed(double speed){
		mouse.setMouseSpeed(speed);
	}

	/**
	 * Checks and run randoms.
	 * DO NOT call this from the event dispatch thread!
	 */
	public void callRandoms() {
		botEnv.proBotEnvironment.randomManager.checkForRandoms(botEnv.proBotEnvironment);
	}

	/**
	 * Stops all scripts
	 */
	public void stopAllScripts() {
		log("Stopping all scripts.");
		botEnv.proBotEnvironment.scriptManager.stopAllScripts();
	}


	/**
	 * Kind of constructor
	 * Should not be used by scripters.
	 *
	 * @param botEnv
	 */
	public void setBotEnv(BotEnvironment botEnv) {
		this.botEnv = botEnv;
		players = botEnv.players;
		npcs = botEnv.npcs;
		interfaces = botEnv.interfaces;
		objects = botEnv.objects;
		menu = botEnv.menu;
		mouse = botEnv.mouse;
		keyboard = botEnv.keyboard;
		bank = botEnv.bank;
		walking = botEnv.walking;
		settings = botEnv.settings;
		inventory = botEnv.inventory;
		gameScreen = botEnv.gameScreen;
		groundItems = botEnv.groundItems;
		camera = botEnv.camera;
		skills = botEnv.skills;
	}

	/**
	 * Is used by the core to notify components to unregister event listeners and such.
	 * Also used to avoid references to the client after bot stop.
	 */
	public void notifyOnClose() {
		botEnv = null;
		players = null;
		npcs = null;
		interfaces = null;
		objects = null;
		menu = null;
		mouse = null;
		keyboard = null;
		bank = null;
		walking = null;
		settings = null;
		inventory = null;
		gameScreen = null;
		groundItems = null;
		camera = null;
		skills = null;
	}
}

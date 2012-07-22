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

package com.kbot2.scriptable.methods.data;

import com.kbot2.bot.BotEnvironment;
import com.kbot2.scriptable.methods.Methods;
import com.kbot2.scriptable.methods.input.Mouse;
import com.kbot2.scriptable.methods.wrappers.Interface;
import com.kbot2.scriptable.methods.wrappers.InterfaceGroup;
import com.kbot2.scriptable.methods.wrappers.Item;
import org.apache.commons.lang.ArrayUtils;

import java.awt.*;
import java.util.*;

import static com.kbot2.scriptable.methods.Calculations.random;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 20.apr.2009
 * Time: 14:45:21
 * Handles bank
 */
public class Bank extends Data{

	// Interface IDs
	public static final int BANK_INTERFACEGROUP_ID = 762;
	public static final int BANK_ITEMPANE = 81;
	public static final int BANK_SCROLLBAR = 103;
	public static int BANK_SCROLLBAR_THUMB = 1;
	public static final int BANK_SCROLLBAR_UP = 4;
	public static final int BANK_SCROLLBAR_DOWN = 5;
	public static final int BANK_BUTTON_INSERT_ITEMS_MODE = 15;
	public static int BANK_BUTTON_SEARCH = 16;
	public static final int BANK_BUTTON_NOTE = 18;
	public static final int BANK_BUTTON_DEPOSIT_CARRIED_ITEMS = 20;
	public static final int BANK_BUTTON_DEPOSIT_WORN_ITEMS = 22;
	public static final int BANK_BUTTON_DEPOSIT_BEASTS_BURDEN = 24;
	public static int BANK_BUTTON_BANK_PIN = 26;
	public static final int BANK_BUTTON_CLOSE = 30;
	public static int BANK_BUTTON_QUESTION = 31;
	public static final int BANK_TAB_ALL = 49;
	public static final int BANK_TAB_2 = 47;
	public static final int BANK_TAB_3 = 45;
	public static final int BANK_TAB_4 = 43;
	public static final int BANK_TAB_5 = 41;
	public static final int BANK_TAB_6 = 39;
	public static final int BANK_TAB_7 = 37;
	public static final int BANK_TAB_8 = 35;
	public static final int BANK_TAB_9 = 33;
	public static int BANK_SEPERATOR_TAB_2 = 51;
	public static int BANK_SEPERATOR_TAB_3 = 52;
	public static int BANK_SEPERATOR_TAB_4 = 53;
	public static int BANK_SEPERATOR_TAB_5 = 54;
	public static int BANK_SEPERATOR_TAB_6 = 55;
	public static int BANK_SEPERATOR_TAB_7 = 56;
	public static int BANK_SEPERATOR_TAB_8 = 57;
	public static int BANK_SEPERATOR_TAB_9 = 58;
	public static int BANK_TEXT_ITEMS = 105;
	public static int BANK_TEXT_ITEMS_MAX = 106;

	public Bank(BotEnvironment botEnv) {
		super(botEnv);
	}

	/**
	 * Tries to close the bank.
	 */
	public void close() {
		if (isOpen())
			getBankInterfaceGroup().getInterface(BANK_BUTTON_CLOSE).doClick();
	}

	/**
	 * Checks if the bank contains the item specified
	 *
	 * @param itemName not case sensetive
	 * @return
	 */
	public boolean contains(String itemName) {
		if (!isOpen())
			return false;
		Interface itemPane = getBankInterfaceGroup().getInterface(BANK_ITEMPANE);
		for (Interface item : itemPane.getChildren()) {
			if (item == null) {
				return false; //
			}
			if (item.getContainedItemName().equalsIgnoreCase(itemName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the bank contains the item specified.
	 *
	 * @param itemID
	 * @return
	 */
	public boolean contains(int itemID) {
		if (!isOpen())
			return false;
		Interface itemPane = getBankInterfaceGroup().getInterface(BANK_ITEMPANE);
		for (Interface item : itemPane.getChildren()) {
			if (item == null) {
				return false; //
			}
			if (item.getContainedItemID() == itemID) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Checks how much the bank contains of the specified item
	 *
	 * @param itemName not case sensetive
	 * @return itemAmount
	 */
	public int amount(String itemName) {
		if (!isOpen())
			return -1;
		Interface itemPane = getBankInterfaceGroup().getInterface(BANK_ITEMPANE);
		for (Interface item : itemPane.getChildren())
			if (item == null)
				return 0;
			else if (item.getContainedItemName().equalsIgnoreCase(itemName))
				return item.getContainedItemStackSize();
		return -1;
	}

	/*
	 * Checks how much the bank contains of the specified item
	 *
	 * @param itemID
	 * @return itemAmount
	 */
	public int amount(int itemID) {
		if (!isOpen())
			return -1;
		Interface itemPane = getBankInterfaceGroup().getInterface(BANK_ITEMPANE);
		for (Interface item : itemPane.getChildren())
			if (item == null)
				return 0;
			else if (item.getContainedItemID() == itemID)
				return item.getContainedItemStackSize();
		return -1;
	}

	/**
	 * Gets an interface representing the item.
	 *
	 * @param itemName
	 * @return null if it don't exist.
	 */
	public Interface getItem(String itemName) {
		if (!isOpen())
			return null;
		Interface itemPane = getBankInterfaceGroup().getInterface(BANK_ITEMPANE);
		for (Interface item : itemPane.getChildren()) {
			if (item == null) {
				return null;
			}
			if (item.getContainedItemName().equalsIgnoreCase(itemName)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Gets an interface representing the item.
	 *
	 * @param itemID
	 * @return null if it don't exist.
	 */
	public Interface getItem(int itemID) {
		if (!isOpen())
			return null;
		Interface itemPane = getBankInterfaceGroup().getInterface(BANK_ITEMPANE);
		for (Interface item : itemPane.getChildren()) {
			if (item == null) {
				return null;
			}
			if (item.getContainedItemID() == itemID) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Srolls using the up and down buttons.
	 *
	 * @param item
	 */
	public void scrollTo(Interface item) {
		Mouse mouse = botEnv.mouse;

		Interface scrollbar = getBankInterfaceGroup().getInterface(BANK_SCROLLBAR);
		if(scrollbar == null)
			return;
		if(scrollbar.getChildren() == null )
			return;
		if(scrollbar.getChildren().length == 0)
			return;

		Interface arrow_up = scrollbar.getChild(BANK_SCROLLBAR_UP);
		Interface arrow_down = scrollbar.getChild(BANK_SCROLLBAR_DOWN);
		boolean pressing = false;
		Interface pressingInterface = null;
		Point pressingPoint = new Point(-1, -1);

		Rectangle bounds = getBankInterfaceGroup().getInterface(BANK_ITEMPANE).getArea();
		for (int i = 0; i < 100; i++) {
			Rectangle rect = item.getArea();
			// Must move
			if (rect.y < bounds.y) {
				// Move up
				if (pressing) {
					if (pressingInterface != arrow_up) {
						mouse.releaseMouse(pressingPoint.x, pressingPoint.y, true);
						pressingInterface = null;
						pressing = false;
					}
					getMethods().sleep(40, 70);
				} else {
					pressingInterface = arrow_up;
					pressingPoint = pressingInterface.getRandomPointInside();
					mouse.moveMouse(pressingPoint.x, pressingPoint.y);
					getMethods().sleep(40, 70);
					mouse.pressMouse(pressingPoint.x, pressingPoint.y, true);
					getMethods().sleep(40, 70);
					pressing = true;
				}
			} else if (rect.y + rect.height > bounds.y + bounds.height) {
				// Move down
				if (pressing) {
					if (pressingInterface != arrow_down) {
						mouse.releaseMouse(pressingPoint.x, pressingPoint.y, true);
						pressingInterface = null;
						pressing = false;
					}
					getMethods().sleep(40, 70);
				} else {
					pressingInterface = arrow_down;
					pressingPoint = pressingInterface.getRandomPointInside();
					mouse.moveMouse(pressingPoint.x, pressingPoint.y);
					getMethods().sleep(40, 70);
					mouse.pressMouse(pressingPoint.x, pressingPoint.y, true);
					getMethods().sleep(40, 70);
					pressing = true;
				}
			} else {
				if (pressing) {
					mouse.releaseMouse(pressingPoint.x, pressingPoint.y, true);
					pressing = false;
				}
				getMethods().sleep(40, 70);
				break;
			}
		}
	}

	/**
	 * Checks if the item is clickable
	 *
	 * @param item
	 * @return
	 */
	public boolean isItemClickable(Interface item) {
		Rectangle bounds = getBankInterfaceGroup().getInterface(BANK_ITEMPANE).getArea();
		Rectangle rect = item.getArea();
		return bounds.contains(rect);
	}

	/**
	 * Checks if the bank is opened.
	 *
	 * @return
	 */
	public boolean isOpen() {
		return getBankInterfaceGroup() != null;
	}

	/**
	 * @return null if the interface dont exist!
	 */
	public InterfaceGroup getBankInterfaceGroup() {
		return botEnv.interfaces.getInterfaceGroup(BANK_INTERFACEGROUP_ID);
	}

	public static final int MENU_ALL = 0;
	public static final int MENU_ALL_BUT_ONE = 0;

	/**
	 * Withdraws an item from bank.
	 *
	 * @param itemName case insensetive
	 * @param count	exact number, or Bank.MENU_ALL or Bank.MENU_ALL_BUT_ONE
	 * @return succeeded ? true: false
	 */
	public boolean withdrawItem(String itemName, int count) {
		if (!isOpen()) {
			botEnv.proBotEnvironment.log.log("Bank is not open. Can't withdraw item: " + itemName + ".");
			return false;
		}
		if (!contains(itemName)) {
			botEnv.proBotEnvironment.log.log("Bank don't contain item: " + itemName + ".");
		}

		if (getOpenedTab() != BANK_TAB_ALL) {
			setBankTab(BANK_TAB_ALL);
		}

		Interface item = getItem(itemName);
		if (item == null) {
			return false;
		}
		scrollTo(item);
		if (!isItemClickable(item)) {
			return false;
		}

		if (count < -1) {
			throw new IllegalArgumentException("Can't withdraw less than 1");
		}

		if (count == MENU_ALL) {
			return item.doAction("Withdraw-All");
		} else if (count == MENU_ALL_BUT_ONE) {
			return item.doAction("Withdraw-All but one");
		} else {
			if (item.doAction("Withdraw-" + count)) {
				return true;
			} else {
				item.doAction("Withdraw-X");
				for (int i = 0; i < 10; i++) {
					getMethods().sleep(700, 1400);
					if (botEnv.interfaces.interfaceGroupExists(752)) {
						getMethods().sendText("" + count, true);
						return true;
					}

				}
				return false;
			}
		}

	}

	/**
	 * Withdraws an item from bank.
	 *
	 * @param itemID
	 * @param count  exact number, or Bank.WITHDRAW_ALL or Bank_WITHDRAW_ALL_BUT_ONE
	 * @return succeeded ? true: false
	 */
	public boolean withdrawItem(int itemID, int count) {
		if (!isOpen()) {
			botEnv.proBotEnvironment.log.log("Bank is not open. Can't withdraw item with ID: " + itemID + ".");
			return false;
		}
		if (!contains(itemID)) {
			botEnv.proBotEnvironment.log.log("Bank don't contain item with ID: " + itemID + ".");
		}
		if (getOpenedTab() != BANK_TAB_ALL) {
			setBankTab(BANK_TAB_ALL);
		}
		Interface item = getItem(itemID);
		if (item == null) {
			return false;
		}
		scrollTo(item);
		if (!isItemClickable(item)) {
			return false;
		}

		if (count < -1) {
			throw new IllegalArgumentException("Can't withdraw less than 1");
		}

		if (count == MENU_ALL) {
			return item.doAction("Withdraw-All");
		} else if (count == MENU_ALL_BUT_ONE) {
			return item.doAction("Withdraw-All but one");
		} else {
			if (item.doAction("Withdraw-" + count)) {
				return true;
			} else {
				item.doAction("Withdraw-X");
				for (int i = 0; i < 10; i++) {
					getMethods().sleep(700, 1400);
					if (botEnv.interfaces.interfaceGroupExists(752)) {
						getMethods().sendText("" + count, true);
						return true;
					}

				}
				return false;
			}
		}
	}

	/**
	 * Shortcut for getting methods
	 *
	 * @return
	 */
	public Methods getMethods() {
		return botEnv.methods;
	}

	/**
	 * Deposits everything in the inventory.
	 *
	 * @return
	 */
	public boolean depositInventory() {
		if (!isOpen())
			return false;
		getBankInterfaceGroup().getInterface(BANK_BUTTON_DEPOSIT_CARRIED_ITEMS).doClick();
		return true;
	}

	/**
	 * Deposits beats burden
	 *
	 * @return
	 */
	public boolean depositBeastsBurder() {
		if (!isOpen())
			return false;
		getBankInterfaceGroup().getInterface(BANK_BUTTON_DEPOSIT_BEASTS_BURDEN).doClick();
		return true;
	}

	/**
	 * Deposits everything in inventory.
	 *
	 * @return
	 */
	public boolean depositWornItems() {
		if (!isOpen())
			return false;
		getBankInterfaceGroup().getInterface(BANK_BUTTON_DEPOSIT_WORN_ITEMS).doClick();
		return true;
	}

	/**
	 * Should get what tab is opened.
	 *
	 * @return the same int as the constants BANK_TAB_X
	 */
	public int getOpenedTab() {
		if (getBankInterfaceGroup().getInterface(BANK_TAB_ALL).getTextureID() == 1419)
			return BANK_TAB_ALL;
		if (getBankInterfaceGroup().getInterface(BANK_TAB_2).getTextureID() == 1419)
			return BANK_TAB_2;
		if (getBankInterfaceGroup().getInterface(BANK_TAB_3).getTextureID() == 1419)
			return BANK_TAB_3;
		if (getBankInterfaceGroup().getInterface(BANK_TAB_4).getTextureID() == 1419)
			return BANK_TAB_4;
		if (getBankInterfaceGroup().getInterface(BANK_TAB_5).getTextureID() == 1419)
			return BANK_TAB_5;
		if (getBankInterfaceGroup().getInterface(BANK_TAB_6).getTextureID() == 1419)
			return BANK_TAB_6;
		if (getBankInterfaceGroup().getInterface(BANK_TAB_7).getTextureID() == 1419)
			return BANK_TAB_7;
		if (getBankInterfaceGroup().getInterface(BANK_TAB_8).getTextureID() == 1419)
			return BANK_TAB_8;
		if (getBankInterfaceGroup().getInterface(BANK_TAB_9).getTextureID() == 1419)
			return BANK_TAB_9;
		return -1;
	}

	/**
	 * Sets the bank tab to the desired tab.
	 *
	 * @param bankTab same as BANK_TAB_XXXX constants
	 * @return
	 */
	public boolean setBankTab(int bankTab) {
		boolean valid = false;
		if (bankTab == BANK_TAB_ALL)
			valid = true;
		else if (bankTab == BANK_TAB_2)
			valid = true;
		else if (bankTab == BANK_TAB_3)
			valid = true;
		else if (bankTab == BANK_TAB_4)
			valid = true;
		else if (bankTab == BANK_TAB_5)
			valid = true;
		else if (bankTab == BANK_TAB_6)
			valid = true;
		else if (bankTab == BANK_TAB_7)
			valid = true;
		else if (bankTab == BANK_TAB_8)
			valid = true;
		else if (bankTab == BANK_TAB_9)
			valid = true;

		if (valid) {
			getBankInterfaceGroup().getInterface(bankTab).doClick();
			return true;
		}
		return false;
	}

	/**
	 * Clicks the note button
	 *
	 * @param note if it should withdraw as note or not
	 * @return
	 */
	public boolean setWithdrawingMode(boolean note) {
		if (!isOpen())
			return false;
		if (botEnv.settings.getSetting(Settings.SETTING_BANK_TOGGLE_WITHDRAW_MODE) != (note ? 1 : 0)) {
			getBankInterfaceGroup().getInterface(BANK_BUTTON_NOTE).doClick();
			getMethods().sleep(100, 300);
		}
		return botEnv.settings.getSetting(Settings.SETTING_BANK_TOGGLE_WITHDRAW_MODE) == (note ? 1 : 0);
	}

	/**
	 * Sets rearange mode
	 *
	 * @param insert
	 * @return
	 */
	public boolean setRearangeMode(boolean insert) {
		if (!isOpen())
			return false;
		if (botEnv.settings.getSetting(Settings.SETTING_BANK_TOGGLE_WITHDRAW_MODE) != (insert ? 1 : 0)) {
			getBankInterfaceGroup().getInterface(BANK_BUTTON_INSERT_ITEMS_MODE).doClick();
			getMethods().sleep(100, 300);
		}
		return botEnv.settings.getSetting(Settings.SETTING_BANK_TOGGLE_WITHDRAW_MODE) == (insert ? 1 : 0);
	}

	/**
	 * Deposits item by itemID.
	 *
	 * @param itemID to deposit if it exists.
	 * @param count  exact number, or Bank.MENU_ALL or Bank.MENU_ALL_BUT_ONE
	 */
	public void deposit(int itemID, int count) {
		java.util.List<Integer> dropPositions = new LinkedList<Integer>();
		Item[] inventoryArray = botEnv.inventory.getItems();
		for (int iterator1 = 0; iterator1 < inventoryArray.length; iterator1++) {
			if (inventoryArray[iterator1].isValid()) {
				if (inventoryArray[iterator1].getID() == itemID)
					dropPositions.add(iterator1);
			}
		}
		if(dropPositions.size() == 0)
			return;
		int[] ascending = new int[dropPositions.size()];
		int index = 0;
		for (int pos : dropPositions) {
			ascending[index] = pos;
			index++;
		}
		Arrays.sort(ascending);
		// Random drop position
		int randomIndex = random(0, ascending.length - 1);
		int pos = ascending[randomIndex];
		if (botEnv.methods.menu.isMenuOpen()) {
			botEnv.methods.atMenu("cancel");
		}
		Point p = botEnv.inventory.getInventoryItemLoc(pos);
		botEnv.methods.moveMouse(p);
		botEnv.methods.sleep(random(20, 100));
		if (count == MENU_ALL) {
			getMethods().atMenu("Deposit-All");
		} else if (count == MENU_ALL_BUT_ONE) {
			getMethods().atMenu("Deposit-All but one");
		} else {
			if (!getMethods().atMenu("Deposit-" + count)) {
				getMethods().atMenu("Deposit-X");
				for (int i = 0; i < 21; i++) {
					if (botEnv.interfaces.interfaceGroupExists(752)) {
						getMethods().sendText("" + count, true);
					}
					getMethods().sleep(80, 130);
				}
			}
		}
		botEnv.methods.atMenu("drop");
		botEnv.methods.sleep(random(150, 300));
	}

	public void depositAllExcept(int... itemIDs){
		Item[] items = botEnv.inventory.getItems();
		java.util.List<Integer> out = new LinkedList<Integer>();
		if(items == null)
			return;
		for(Item item: items){
			if(item.getID() > 0){
				boolean found = false;
				for(int id: itemIDs){
					if(item.getID() == id){
						found = true;
					}
				}
				if(!found){
					for(int id: out){
						if(id == item.getID()){
							found = true;
						}
					}
					if(!found){
						out.add(item.getID());
					}
				}
			}
		}
		if(out.isEmpty())
			return;
		depositAll(ArrayUtils.toPrimitive(out.toArray(new Integer[1])));
	}

	public void depositAll(int... itemIDs){
		if(itemIDs == null)
			return;
		for(int id: itemIDs){
			deposit(id, MENU_ALL);
		}
	}

	public void deposit(int count, int... itemIDs){
		if(itemIDs == null)
			return;
		for(int id: itemIDs){
			deposit(id, count);
		}
	}
}


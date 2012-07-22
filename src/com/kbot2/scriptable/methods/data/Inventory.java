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
import com.kbot2.scriptable.methods.wrappers.ExtendedItem;
import com.kbot2.scriptable.methods.wrappers.Interface;
import com.kbot2.scriptable.methods.wrappers.Item;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.kbot2.scriptable.methods.Calculations.random;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 01.mai.2009
 * Time: 14:43:40
 */
public class Inventory extends Data{
	public Inventory(BotEnvironment botEnv) {
		super(botEnv);
	}

	/**
	 * Checks if the inventory contains item specified in itemIDs
	 * @param itemIDs
	 * @return
	 */
	public boolean contains(int... itemIDs){
		return botEnv.proBotEnvironment.inventory.contains(itemIDs);
	}

	/**
	 * gets the number of items in inventory
	 *
	 * @return -1 if some error occured.
	 */
	public int getCount() {
		return botEnv.proBotEnvironment.inventory.getCount();
	}

	/**
	 * gets a array representing the inventory.
	 * @return
	 */
	public Item[] getItems() {
        com.kbotpro.scriptsystem.wrappers.Item[] items = botEnv.proBotEnvironment.inventory.getItems();
        if(items.length == 0){
            return new Item[0];
        }
        Item[] out = new Item[items.length];
        for(int i = 0; i < out.length; i++){
            out[i] = new ExtendedItem(items[i]);
        }

        return out;
        
    }

	/**
	 * Drops the specified items.
	 *
	 * @param itemIDs
	 */
	public void drop(int... itemIDs) {
		botEnv.proBotEnvironment.inventory.drop(itemIDs);
	}

	/**
	 * Drops all except the specified items.
	 *
	 * @param itemIDs
	 */
	public void dropAllExcept(int... itemIDs) {
		botEnv.proBotEnvironment.inventory.dropAllExcept(itemIDs);
	}

	/**
	 * Calculates the position of a inventory item
	 *
	 * @param invIndex
	 * @return
     * @deprecated No longer work in RT5
	 */
	public Point getInventoryItemLoc(int invIndex) {
		int col = (invIndex % 4);
		int row = (invIndex / 4);
		int x = 580 + (col * 42);
		int y = 228 + (row * 36);
		return new Point(x, y);
	}

	/**
	 * Performs and action on the first item matching one of the item IDs in itemIDs
	 * @param actionContains
	 * @param itemIDs
	 * @return
	 */
	public boolean atItem(String actionContains, int... itemIDs){
		for(int ID: itemIDs){
            if(botEnv.proBotEnvironment.inventory.atItem(ID, actionContains))
                return true;
        }
        return false;
	}

	/**
	 * Counts amount of items by ID in inventory
	 *
	 * @param countStacks Count the stacks or not
	 * @param ids IDs to search for
	 * @return Amount of items with given ID in inventory
	 * @author Alowaniak
	 */
	public int getCount(boolean countStacks, int... ids) {
		Item[] items = getItems();
		int count = 0;
		for(Item item : items)
			for(int i : ids) {
				if(item.getID() == i)
					count += countStacks ? item.getStackSize() : 1;
			}
		return count;
	}

	public boolean isFull() {
		return getCount() >= 28;
	}
}
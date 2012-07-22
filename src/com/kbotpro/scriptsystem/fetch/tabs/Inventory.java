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



package com.kbotpro.scriptsystem.fetch.tabs;

import com.kbotpro.scriptsystem.fetch.Game;
import com.kbotpro.scriptsystem.fetch.Interfaces;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.Item;
import com.kbotpro.scriptsystem.wrappers.Interface;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.bot.BotEnvironment;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 21, 2009
 * Time: 8:07:11 PM
 */
public class Inventory extends ModuleConnector {
    private static final int INVENTORY_INTERFACE_ID = 149;
    private static final int INVENTORY_BANK_INTERFACE_ID = 763;
    private static final int INVENTORY_GRAND_EXCHANGE_INTERFACE_ID = 149;
    private static final int INVENTORY_TRADE_INTERFACE_ID = 336;
    private static final int TRADE_SCREEN_INTERFACE_ID = 335;

    public Inventory(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Gets all the inventory items.
     *
     * @return
     */
    public Item[] getItems() {
        Interfaces interfaces = botEnv.interfaces;
        Interface inventoryInterface = interfaces.getInterface(INVENTORY_INTERFACE_ID);
        if (botEnv.bank.isOpen())
            inventoryInterface = interfaces.getInterface(INVENTORY_BANK_INTERFACE_ID);
        else if (botEnv.grandExchange.isOpen())
            inventoryInterface = interfaces.getInterface(INVENTORY_GRAND_EXCHANGE_INTERFACE_ID);
        else if (botEnv.interfaces.interfaceExists(TRADE_SCREEN_INTERFACE_ID) && botEnv.interfaces.getComponent(TRADE_SCREEN_INTERFACE_ID, 0).isVisible()){
            inventoryInterface = interfaces.getInterface(INVENTORY_TRADE_INTERFACE_ID);
        }
        if (inventoryInterface == null) {
            return new Item[0];
        }
        IComponent inventoryPane = inventoryInterface.getComponent(0);
        IComponent[] children = inventoryPane.getChildren();
        if (children == null || children.length == 0) {
            return new Item[0];
        }
        List<Item> items = new ArrayList<Item>();
        for (IComponent aChildren : children) {
            if (aChildren == null) {
                continue;
            }
            Item item = new Item(botEnv, aChildren);
            if (item.getID() == -1) {
                continue;
            }
            items.add(item);
        }
        return items.toArray(new Item[items.size()]);
    }

    /**
     * Checks if the inventory contains any of the given items identified by their IDs.
     *
     * @param IDs the item IDs to match the item
     * @return
     */
    public boolean contains(int... IDs) {
        Item[] items = getItems();
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            for (int ID : IDs) {
                if (item.getID() == ID) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the inventory contains any of the given items identified by their names.
     *
     * @param names Must be the exact name. (but it ignores case)
     * @return
     */
    public boolean contains(String... names) {
        Item[] items = getItems();
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            for (String name : names) {
                if (item.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets all the items matching the list of IDs given.
     *
     * @param IDs Item IDs to match the items with.
     * @return
     */
    public Item[] getItems(int... IDs) {
        List<Item> out = new ArrayList<Item>();
        Item[] items = getItems();
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            for (int ID : IDs) {
                if (item.getID() == ID) {
                    out.add(item);
                    break;
                }
            }
        }
        return out.toArray(new Item[out.size()]);
    }

    /**
     * Gets all the items matching the lists of names
     *
     * @param names list of names to match the items with
     * @return
     */
    public Item[] getItems(String... names) {
        List<Item> out = new ArrayList<Item>();
        Item[] items = getItems();
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            for (String name : names) {
                if (item.getName().equalsIgnoreCase(name)) {
                    out.add(item);
                    break;
                }
            }
        }
        return out.toArray(new Item[out.size()]);
    }

    /**
     * Checks if the inventory screen is currently open.
     *
     * @return
     */
    public boolean isOpen() {
        IComponent iComponent = botEnv.interfaces.getComponent(INVENTORY_INTERFACE_ID, 0);
        if (botEnv.bank.isOpen())
            iComponent = botEnv.interfaces.getComponent(INVENTORY_BANK_INTERFACE_ID, 0);
        else if (botEnv.grandExchange.isOpen())
            iComponent = botEnv.interfaces.getComponent(INVENTORY_GRAND_EXCHANGE_INTERFACE_ID, 0);
        return iComponent != null && iComponent.isVisible();
    }

    /**
     * Counts amount of items by ID in inventory
     *
     * @param countStacks Count the stacks or not
     * @param ids         IDs to search for
     * @return Amount of items with given ID in inventory
     * @author Alowaniak
     */
    public int getCount(boolean countStacks, int... ids) {
        Item[] items = getItems();
        int count = 0;
        for (Item item : items)
            for (int i : ids) {
                if (item.getID() == i)
                    count += countStacks ? item.getStackSize() : 1;
            }
        return count;
    }

    /**
     * Gets the number of items in inventory
     *
     * @return -1 if some error occured.
     */
    public int getCount() {
        return getItems().length;
    }

    /**
     * Checks if the inventory is full
     *
     * @return
     */
    public boolean isFull() {
        return getCount() >= 28;
    }

    /**
     * Drops the specified items.
     *
     * @param itemIDs
     */
    public void drop(int... itemIDs) {
        botEnv.game.openTab(Game.TAB_INVENTORY);
        if (itemIDs == null) {
            return;
        }
        Item[] items = getItems();
        for (Item item : items) {
            if (!isOpen()) break;
            for (int itemID : itemIDs) {
                if (item.isValid()) {
                    if (item.getID() == itemID) {
                        botEnv.game.openTab(Game.TAB_INVENTORY);
                        if (botEnv.game.hasSelectedItem()) {
                            botEnv.mouse.clickMouse(true);
                            sleep(50, 100);
                        }
                        if (botEnv.menu.isOpen()) {
                            botEnv.menu.atMenu("cancel");
                        }
                        item.doAction("drop");
                        sleep(50, 100);
                    }
                }
            }
        }
    }


    /**
     * Drops all except the specified items.
     *
     * @param itemIDs
     */
    public void dropAllExcept(int... itemIDs) {
        botEnv.game.openTab(Game.TAB_INVENTORY);
        if (itemIDs == null) {
            return;
        }
        Item[] items = getItems();
        for (Item item : items) {
            if (!isOpen()) break;
            boolean found = false;
            for (int itemID : itemIDs) {
                if (item.isValid()) {
                    if (item.getID() == itemID)
                        found = true;
                }
            }
            if (!found) {
                botEnv.game.openTab(Game.TAB_INVENTORY);
                if (botEnv.game.hasSelectedItem()) {
                    botEnv.mouse.clickMouse(true);
                    sleep(50, 100);
                }
                if (botEnv.menu.isOpen()) {
                    botEnv.menu.atMenu("Cancel");
                }
                item.doAction("drop");
                sleep(50, 100);
            }
        }
    }

    /**
     * Drops all the items with the IDs vertically
     *
     * @param IDs
     * @author Ampzz
     */
    public void dropVertical(final int... IDs) {
        if (!isOpen())
            botEnv.game.openTab(Game.TAB_INVENTORY);

        if (!contains(IDs))
            return;

        Item[] invenItems = getItems();

        if (invenItems.length == 0)
            return;

        for (int i = 0; i < 4; i++) {
            for (int current = i; current < 28; current += 4) {
                if (!isOpen()) break;
                boolean dropIt = false;
                for (int ID : IDs) {
                    if (invenItems[current].getID() == ID)
                        dropIt = true;
                }
                if (dropIt) {
                    int gamble = random(0, 60);
                    if (gamble <= 1) {
                        invenItems[current].doAction("Examine");
                        current -= 4;
                    } else {
                        invenItems[current].doAction("Drop");
                        sleep(100, 200);
                    }
                }
            }
        }
    }

    /**
     * atItem() in inventory
     */
    public boolean atItem(final int id, final String action) {
        if (!isOpen())
            botEnv.game.openTab(Game.TAB_INVENTORY);

        if (!contains(id))
            return false;

        Item[] in = getItems();
        java.util.ArrayList<Item> ps = new java.util.ArrayList<Item>();
        for (Item i : in) {
            if (i.getID() == id)
                ps.add(i);
        }
        if (ps.size() == 0)
            return false;
        if (botEnv.game.hasSelectedItem())
            botEnv.mouse.clickMouse(false);
        if (botEnv.menu.isOpen())
            botEnv.menu.atMenu("Cancel");
        return ps.get(random(0, ps.size() - 1)).doAction(action);
    }

    public boolean atItem(final String name, final String action) {
        if (!isOpen())
            botEnv.game.openTab(Game.TAB_INVENTORY);

        if (!contains(name))
            return false;

        Item[] in = getItems();
        java.util.ArrayList<Item> ps = new java.util.ArrayList<Item>();
        for (Item i : in) {
            if (i.getName().toLowerCase().contains(name.toLowerCase()))
                ps.add(i);
        }
        if (ps.size() == 0)
            return false;
        if (botEnv.game.hasSelectedItem())
            botEnv.mouse.clickMouse(false);
        if (botEnv.menu.isOpen())
            botEnv.menu.atMenu("Cancel");
        return ps.get(random(0, ps.size() - 1)).doAction(action);
    }

    public int getCount(boolean stacksize, String name) {
        Item i[] = this.getItems(name);
        if (i.length < 1) {
            return 0;
        }
        return stacksize ? i[0].getStackSize() : i.length;
    }

    /**
     * @param name   name of the item
     * @param action action of the item
     * @return true if action succeeds
     * @throws InterruptedException
     */
    public boolean atItems(String name, String action) {
        int count = getCount(false, name);
        if (count == 0) {
            return false;
        }
        while (count > 0) {
            atItem(name, action);
            sleep(100, 200);
            count--;
        }
        return true;
    }

    /**
     * @param id     id of the item
     * @param action action of the item
     * @return true if sction succeeds
     * @throws InterruptedException
     */

    public boolean atItems(int id, String action) {
        int count = getCount(false, id);
        if (count == 0) {
            return false;
        }
        while (count > 0) {
            atItem(id, action);
            sleep(100, 200);
            count--;
        }
        return true;
    }

    /**
     * @param id      id of the item
     * @param action  action of the item
     * @param ammount ammount to apply action on
     * @return true if sction succeeds
     * @throws InterruptedException
     */
    public boolean atItem(int id, String action, int ammount) {
        int count = getCount(false, id);
        if (count == 0) {
            return false;
        }
        while (ammount > 0) {
            if (count == 0) {
                break;
            }
            atItem(id, action);
            sleep(100, 200);
            ammount--;
            count--;
        }
        return true;
    }

    /**
     * @param name    name of the item
     * @param action  action of the item
     * @param ammount ammount to apply action on
     * @return true if sction succeeds
     * @throws InterruptedException
     */
    public boolean atItem(String name, String action, int ammount) {
        int count = getCount(false, name);
        if (count == 0) {
            return false;
        }
        while (ammount > 0) {
            if (count == 0) {
                break;
            }
            atItem(name, action);
            sleep(100, 200);
            ammount--;
            count--;
        }
        return true;
    }

    public void open() {
        if (!isOpen()) {
            botEnv.game.openTab(Game.TAB_INVENTORY);
        }
    }
}

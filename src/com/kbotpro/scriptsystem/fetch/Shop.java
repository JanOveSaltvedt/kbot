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

package com.kbotpro.scriptsystem.fetch;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Interface;
import com.kbotpro.scriptsystem.wrappers.Item;

/**
 * Class containing the shop functions.
 * @author endoskeleton
 */
public class Shop extends ModuleConnector {
    Interface shop;

    public Shop(BotEnvironment b) {
        super(b);
    }

    private void updateShopInterface() {
        this.shop = botEnv.interfaces.getInterface(620);
    }

    /**
     * @return Closes the shop interface if open.
     */
    public boolean close() {
        updateShopInterface();
        IComponent c = shop.getComponent(18);
        if ((c == null) || (!isOpen())) {
            return false;
        }
        return c.doClick();
    }

    /**
     * @param id
     *      Item ID of the item.
     * @return Returns if the shop contains item.
     */
    public boolean contains(int id) {
        updateShopInterface();
        if (!isOpen()) {
            return false;
        }
        IComponent[] c = shop.getComponent(25).getChildren();
        if ((c != null) && (c.length > 0)) {
            for(int i = 0; i < c.length-1; i++) {
                if (c[i].getElementID() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param name
     *      Name of the item.
     * @return Returns if the shop contains item.
     */
    public boolean contains(String name) {
        updateShopInterface();
        if (!isOpen()) {
            return false;
        }
        IComponent[] c = shop.getComponent(25).getChildren();
        if ((c != null) && (c.length > 0)) {
            for(int i = 0; i < c.length-1; i++) {
                if (c[i].getElementName().contains(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param id
     *      Item ID of the item.
     * @return Returns the count of an item in shop.
     */
    public int getCount(int id) {
        updateShopInterface();
        if (!isOpen()) {
            return 0;
        }
        IComponent[] c = shop.getComponent(25).getChildren();
        int count = 0;
        if ((c != null) && (c.length > 0)) {
            for(int i = 0; i < c.length-1; i++) {
                if (c[i].getElementID() == id) {
                    count = c[i].getElementStackSize();
                }
            }
        }
        return count;
    }

    /**
     * @param name
     *      Name of the item.
     * @return Returns the count of an item in shop.
     */
    public int getCount(String name) {
        updateShopInterface();
        if (!isOpen()) {
            return 0;
        }
        IComponent[] c = shop.getComponent(25).getChildren();
        int count = 0;
        if ((c != null) && (c.length > 0)) {
            for(int i = 0; i < c.length-1; i++) {
                if (c[i].getElementName().contains(name)) {
                    count = c[i+2].getElementStackSize();
                }
            }
        }
        return count;
    }

    /**
     * @param id
     *      ID of the item.
     * @param action
     *      Action to perform.
     * @return Buys item from shop, returns true if successful.
     */
    public boolean buyItem(int id, String action) {
        updateShopInterface();
        if (!isOpen()) {
            return false;
        }
        IComponent[] c = shop.getComponent(25).getChildren();

        if ((c != null) && (c.length > 0)) {
            for(int i = 0; i < c.length-1; i++) {
                if (c[i].getElementID() == id) {
                    return c[i-2].doAction(action);
                }
            }
        }
        return false;
    }

    /**
     * @param name
     *      Name of the item.
     * @param action
     *      Action to perform.
     * @return Buys item from shop, returns true if successful.
     */
    public boolean buyItem(String name, String action) {
        updateShopInterface();
        if (!isOpen()) {
            return false;
        }
        IComponent[] c = shop.getComponent(25).getChildren();

        if ((c != null) && (c.length > 0)) {
            for(int i = 0; i < c.length-1; i++) {
                if (c[i].getElementName().contains(name)) {
                    return c[i].doAction(action);
                }
            }
        }
        return false;
    }

    /**
     * @param id
     *      ID of the item.
     * @param action
     *      Action to perform.
     * @return Sells item to shop, returns true if successful.
     */
    public boolean sellItem(int id, String action) {
        updateShopInterface();
        if (!isOpen()) {
            return false;
        }

        Item[] i = botEnv.inventory.getItems(id);
        if ((i != null) && (i.length > 1)) {
            return i[0].doAction(action);
        }
        return false;
    }

    /**
     * @param name
     *      Name of the item.
     * @param action
     *      Action to perform.
     * @return Sells item to shop, returns true if successful.
     */
    public boolean sellItem(String name, String action) {
        updateShopInterface();
        if (!isOpen()) {
            return false;
        }

        Item[] i = botEnv.inventory.getItems(name);
        if ((i != null) && (i.length > 1)) {
            return i[0].doAction(action);
        }
        return false;
    }

    /**
     *
     * @param id
     *      Item id
     * @return Returns price of the item.
     */
    public int getPrice(int id) {
        updateShopInterface();
        if (!isOpen()) {
            return -1;
        }
        IComponent[] c = shop.getComponent(25).getChildren();
        int count = 0;
        if ((c != null) && (c.length > 0)) {
            for(int i = 0; i < c.length-1; i++) {
                if (c[i].getElementID() == id) {
                    count = Integer.parseInt(c[i+2].getText());
                }
            }
        }
        return count;
    }

    /**
     *
     * @param name
     *      Name of the item.
     * @return Returns price of the item.
     */
    public int getPrice(String name) {
        if (!isOpen()) {
            return -1;
        }
        IComponent[] c = shop.getComponent(25).getChildren();
        int count = 0;
        if ((c != null) && (c.length > 0)) {
            for(int i = 0; i < c.length-1; i++) {
                if (c[i].getElementName().contains(name)) {
                    count = Integer.parseInt(c[i+4].getText());
                }
            }
        }
        return count;
    }

    /**
     * @return Checks if the shop interface is open.
     */
    public boolean isOpen() {
        return ((shop != null) && (shop.isValid()));
    }
}

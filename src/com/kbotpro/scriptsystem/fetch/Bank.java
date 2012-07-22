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

import com.kbotpro.scriptsystem.input.internal.mouse.EventFactory;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.*;
import com.kbotpro.scriptsystem.fetch.Interfaces;
import com.kbotpro.bot.BotEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

/**
 * Used to interact with the bank interface.
 *
 * @author Kosaki, 933pm, Ampzz, Ryan
 */
public class Bank extends ModuleConnector {

    // Interface IDs
    public static final int BANK_INTERFACE_ID = 762;
    public static final int BANK_ITEM_PANE_ID = 92;
    public static final int BANK_SCROLLBAR = 114;
    public static int BANK_SCROLLBAR_THUMB = 1;
    public static final int BANK_SCROLLBAR_UP = 4;
    public static final int BANK_SCROLLBAR_DOWN = 5;

    public static final int BANK_BUTTON_INSERT_ITEMS_MODE = 15;
    public static int BANK_BUTTON_SEARCH = 17;
    public static final int BANK_BUTTON_NOTE = 19;
    public static final int BANK_BUTTON_DEPOSIT_CARRIED_ITEMS = 32;
    public static final int BANK_BUTTON_DEPOSIT_WORN_ITEMS = 34;
    public static final int BANK_BUTTON_DEPOSIT_BEASTS_BURDEN = 36;
    public static int BANK_BUTTON_BANK_PIN = 38;
    public static final int BANK_BUTTON_CLOSE = 42;
    public static int BANK_BUTTON_QUESTION = 43;
    public static final int BANK_TAB_ALL = 61;
    public static final int BANK_TAB_2 = 59;
    public static final int BANK_TAB_3 = 57;
    public static final int BANK_TAB_4 = 55;
    public static final int BANK_TAB_5 = 53;
    public static final int BANK_TAB_6 = 51;
    public static final int BANK_TAB_7 = 49;
    public static final int BANK_TAB_8 = 47;
    public static final int BANK_TAB_9 = 45;
    public static int BANK_SEPERATOR_TAB_2 = 63;
    public static int BANK_SEPERATOR_TAB_3 = 64;
    public static int BANK_SEPERATOR_TAB_4 = 65;
    public static int BANK_SEPERATOR_TAB_5 = 66;
    public static int BANK_SEPERATOR_TAB_6 = 67;
    public static int BANK_SEPERATOR_TAB_7 = 68;
    public static int BANK_SEPERATOR_TAB_8 = 69;
    public static int BANK_SEPERATOR_TAB_9 = 70;
    public static int BANK_TEXT_ITEMS_FREE = 23;
    public static int BANK_TEXT_ITEMS_MAX_FREE = 34;
    public static int BANK_TEXT_ITEMS_MEMBERS = 25;
    public static int BANK_TEXT_ITEMS_MAX_MEMBERS = 26;
    private static final int WITHDRAW_ALL = 0;
    private static final int WITHDRAW_X = 1;
    private static final int WITHDRAW_ALL_BUT_ONE = 2;
    public static final int[] BANKER_IDS = { 7605, 6532, 6533, 6534, 6535, 5913, 5912, 2271, 14367, 3824, 44, 45, 2354, 2355, 499, 5488, 8948, 958, 494, 495, 6362, 5901 };
    public static final int[] BANK_BOOTH_IDS = { 11758, 11402, 34752, 35647, 2213, 25808, 2213, 26972, 27663, 4483, 14367, 19230, 29085, 12759, 6084 };
    public static final int[] BANK_CHEST_IDS = { 27663, 4483, 12308, 21301, 42192 };

    public Bank(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Opens the nearest bank if it is within range to walk to it. It does not generate a path to the bank, just simply opens the nearest.
     * @return True if the bank has been opened.
     */
    public boolean openClosestBank() {
        PhysicalObject booth = botEnv.objects.getClosestObject(20, BANK_BOOTH_IDS);
        NPC banker = botEnv.npcs.getClosest(20, BANKER_IDS);
        PhysicalObject chest = botEnv.objects.getClosestObject(20, BANK_CHEST_IDS);

        Object targetObject = null;
        String action = null;

        if (booth != null) {
            targetObject = booth;
            action = "Use-quickly";
        } else if (banker != null) {
            targetObject = banker;
            action = "Bank banker";
        } else if (chest != null) {
            targetObject = chest;
            action = "Use";
        }
        if (targetObject != null) {
            NPC targetNPC = null;
            PhysicalObject targetPhysObject = null;
            if (targetObject instanceof NPC) {
                targetNPC = (NPC)targetObject;
            } else {
                targetPhysObject = (PhysicalObject)targetObject;
            }
            if (targetNPC != null) {
                if (botEnv.players.getMyPlayer().distanceTo(targetNPC.getLocation()) > 8) {
                    if (botEnv.walking.walkToMM(targetNPC.getLocation())) {
                        sleep(300, 600);
                        while (botEnv.players.getMyPlayer().isMoving()) sleep(100);
                    } else {
                        return false;
                    }
                }
                return targetNPC.doAction(action);
            } else {
                if (botEnv.players.getMyPlayer().distanceTo(targetPhysObject.getLocation()) > 8) {
                    if (botEnv.walking.walkToMM(targetPhysObject.getLocation())) {
                        sleep(300, 600);
                        while (botEnv.players.getMyPlayer().isMoving()) sleep(100);
                    } else {
                        return false;
                    }
                }
                return targetPhysObject.doAction(action);
            }
        }
        return false;
    }

    /**
     * Gets an item array of all the bank items.
     *
     * @return Array of bank items.
     */
    public Item[] getItems() {
        Interfaces interfaces = botEnv.interfaces;
        Interface inventoryInterface = interfaces
                .getInterface(BANK_INTERFACE_ID);
        if (inventoryInterface == null) {
            return new Item[0];
        }
        IComponent inventoryPane = inventoryInterface
                .getComponent(BANK_ITEM_PANE_ID);
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
     * Checks if the bank contains any of the given items identified by their
     * IDs.
     *
     * @param IDs
     *            The item IDs to check for.
     * @return True if any of the specified items are found.
     */
    public boolean contains(int... IDs) {
        Item[] items = getItems();
        for (Item item : items) {
            if (item != null) {
                for (int ID : IDs) {
                    if (item.getID() == ID) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the bank contains any of the given items.
     *
     * @param names
     *            Item names. Must be exact (case ignored).
     * @return Whether or not the bank contains the specified item(s).
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
     * Checks if the bank is open.
     *
     * @return Whether or not the bank interface is up.
     */
    public boolean isOpen() {
        IComponent iComponent = botEnv.interfaces.getComponent(
                BANK_INTERFACE_ID, BANK_ITEM_PANE_ID);
        return iComponent != null && iComponent.isVisible();
    }

    /**
     * Checks how many of the specified item the bank contains.
     *
     * @param itemName
     *            Case-insensitive item name to count.
     *
     * @return itemAmount Stack size of item in bank.
     */
    public int getCount(String itemName) {
        if (!isOpen())
            return 0;
        for (Item item : getItems()) {
            if (item == null)
                continue;
            else if (item.getName().equalsIgnoreCase(itemName))
                return item.getStackSize();
        }
        return 0;
    }

    /**
     * Gets the Item for a specified item name.
     *
     * @param itemName
     *            Case-insensitive item name to look for.
     * @return Object of type Item with the specified name.
     */
    public Item getItem(String itemName) {
        if (!isOpen())
            return null;
        for (Item item : getItems()) {
            if (item == null) {
                continue;
            }
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gets the Item for a specified item ID.
     *
     * @param itemID
     *            Item ID to look for.
     * @return Object of type Item with the specified ID.
     */
    public Item getItem(int itemID) {
        if (!isOpen())
            return null;
        for (Item item : getItems()) {
            if (item == null) {
                continue;
            }
            if (item.getID() == itemID) {
                return item;
            }
        }
        return null;
    }

    /**
     * Checks how many of the specified item the bank contains.
     *
     * @param itemID
     *            ID of the item to count.
     *
     * @return itemAmount Stack size of item in bank.
     */
    public int getCount(int itemID) {
        if (!isOpen())
            return 0;
        for (Item item : getItems()) {
            if (item == null)
                continue;
            if (item.getID() == itemID)
                return item.getStackSize();
        }
        return 0;
    }

    /**
     * Deposits all of the specified item in the inventory.
     * @param itemName Name of item to be deposited.
     * @return Whether or not the items were deposited.
     */
    public boolean depositAll(String itemName) {
        if (!isOpen()){
            return false;
        }
        int count = 0;
        for (Item it : botEnv.inventory.getItems()) {
            if (it.getName().contains(itemName)) {
                count++;
                it.doAction("Deposit-all");
                sleep(100, 200);
            }
            if(botEnv.inventory.getCount(false, itemName) == 0){
                break;
            }
        }
        return count != 0;
    }

    /**
     * Deposits all of the specified item in the inventory.
     * @param itemID Name of item to be deposited.
     * @return Whether or not the items were deposited.
     */
    public boolean depositAll(int itemID) {
        if (!isOpen() || botEnv.inventory.getItems().length == 0){
            return false;
        }
        int count = 0;
        for (Item it : botEnv.inventory.getItems()) {
            if (it.getID() == itemID) {
                count++;
                it.doAction("Deposit-all");
                sleep(100, 200);
            }
            if(botEnv.inventory.getCount(false, itemID) == 0){
                break;
            }
        }
        return count != 0;
    }

    /**
     * Deposits all the items excluding the specified IDs.
     *
     * @param ids
     *            Item IDs not to deposit.
     * @return Whether or not the deposit was successful.
     * @author Ampzz
     */
    public boolean depositAllExcept(int... ids) {

        if (!isOpen())
            return false;

        Item[] allItems = botEnv.inventory.getItems();
        if (allItems.length == 0)
            return false;

        ArrayList<Item> possItems = new ArrayList<Item>();

        for (Item iItem : allItems) {
            boolean add = true;

            for (int id : ids) {
                if (iItem.getID() == id)
                    add = false;
            }

            if (possItems.size() > 0 && add) {
                for (Item kItem : possItems) {
                    if (kItem.getID() == iItem.getID())
                        add = false;
                }
            }

            if (add)
                possItems.add(iItem);

        }

        if (possItems.size() == 0)
            return true;

        for (Item jItem : possItems) {
            jItem.doAction("Deposit-all");
            sleep(300, 700);
        }

        return true;
    }

    /**
     * Deposits all the items in the inventory using the button.
     *
     * @return Whether or not the method succeeded.
     */
    public boolean depositAllInventory() {
        return depositByButton(Bank.BANK_BUTTON_DEPOSIT_CARRIED_ITEMS);
    }

    /**
     * Deposits all the items currently worn using the button.
     *
     * @return Whether or not the method succeeded.
     */
    public boolean depositAllEquipment() {
        return depositByButton(Bank.BANK_BUTTON_DEPOSIT_WORN_ITEMS);
    }

    /**
     * Deposits all the items currently carried by beast of burden using the button.
     *
     * @return Whether or not the method succeeded.
     */
    public boolean depositAllBeastOfBurdenItems() {
        return depositByButton(Bank.BANK_BUTTON_DEPOSIT_BEASTS_BURDEN);
    }

    /**
     *
     * @param depositType Inventory, equipment, or familiar.
     * @return
     */
    private boolean depositByButton(int depositType) {
        if (!isOpen())
            return false;

        IComponent depButton = botEnv.interfaces.getComponent(
                Bank.BANK_INTERFACE_ID, depositType);
        if (depButton != null && depButton.isVisible()) {
            if (depButton.doClick()) {
                sleep(300, 500);
                return true;
            }
        }

        return false;
    }

    /**
     * Closes the bank interface.
     */
    public void close() {
        IComponent close = botEnv.interfaces.getComponent(762,
                BANK_BUTTON_CLOSE);
        if (close != null && isOpen()) {
            close.doClick();
        }
    }

    /**
     * Withdraws all but one of the specified item.
     *
     * @param id
     *            ID of the item you want to withdraw.
     */
    public void withdrawAllButOne(int id) {
        withdraw(id, 0, WITHDRAW_ALL_BUT_ONE);
    }

    /**
     * Withdraws all of the specified item.
     *
     * @param id
     *            ID of the item you want to withdraw.
     */
    public void withdrawAll(int id) {
        withdraw(id, 0, WITHDRAW_ALL);
    }

    /**
     * Withdraws the specified item and amount.
     *
     * @param id
     *            ID of the item you want to withdraw.
     * @param amount
     *            Amount of the item you want to withdraw.
     */
    public void withdrawX(int id, int amount) {
        withdraw(id, amount, WITHDRAW_X);

    }

    /**
     * @param id
     *            ID of the item you want to withdraw.
     * @param amount
     *            Amount of the item you want to withdraw.
     * @param type
     *            Withdraw type: WITHDRAW_
     */
    private void withdraw(int id, int amount, int type) {
        boolean doAction = false;
        Item item = getItem(id);
        if (isOpen() && contains(id) && item != null) {
            doAction = scrollToItem(item);
        }
        if (doAction) {
            String action = "Withdraw-" + amount;
            if (type == WITHDRAW_ALL) {
                action = "Withdraw-All";
            } else if (type == WITHDRAW_ALL_BUT_ONE) {
                action = "Withdraw-All but one";
            }
            if (item.hasAction(action)) {
                item.doAction(action);
            } else { // withdraw X
                if (item.doAction("Withdraw-X")) {
                    for (int i = 0; i < random(2000, 3000); i+=50) {
                        IComponent c =  botEnv.interfaces.getComponent(752, 5);
                        if (c != null && c.isValid() && c.isVisible()) {
                            sleep(500, 1000);
                            botEnv.keyboard.writeText(Integer.toString(amount), true);
                            break;
                        }
                        sleep(50);
                    }
                }
            }
        }
    }

    public boolean scrollToItem(Item i) {
        Interface bankInterface = botEnv.interfaces.getInterface(Bank.BANK_INTERFACE_ID);
        IComponent itemContainer = bankInterface.getComponent(Bank.BANK_ITEM_PANE_ID);
        if (!i.container.isElementVisible()) {
            bankInterface.getComponent(62).doClick();
            sleep(500, 1000);
        }
        /*if (bankInterface.getComponent(61).getTextureID() != 1419 && (itemContainer.getBounds().getLocation().equals(i.getBounds().getLocation()) ||
                i.container.getRelativeX() == 0 && i.container.getRelativeY() == 0)) {
            bankInterface.getComponent(62).doClick();
            sleep(500, 1000);
        }   */
        if (itemContainer.getBounds().contains(i.getCenter())) {
            return true;
        }
        IComponent arrow;
        if (i.getCenter().getY() < itemContainer.getAbsoluteY()) {
            arrow = bankInterface.getComponent(114).getChildren()[4];
        } else {
            arrow = bankInterface.getComponent(114).getChildren()[5];
        }
        Point p = botEnv.mouse.getMousePos();
        if (!arrow.getBounds().contains(p)) {
            botEnv.mouse.moveMouse(arrow.getBounds());
        }
        EventFactory eventFactory = new EventFactory(botEnv);
        p = botEnv.mouse.getMousePos();
        MouseEvent mouseEvent = eventFactory.createMousePress(p.x, p.y, true);
        botEnv.dispatchEvent(mouseEvent);
        KTimer timeout = new KTimer(random(5000, 6000));
        while(!itemContainer.getBounds().contains(i.getCenter()) && !timeout.isDone()) {
            sleep(200, 500);
        }
        mouseEvent = eventFactory.createMouseRelease(p.x, p.y, true);
        botEnv.dispatchEvent(mouseEvent);
        mouseEvent = eventFactory.createMouseClicked(p.x, p.y, true);
        botEnv.dispatchEvent(mouseEvent);
        return itemContainer.getBounds().contains(i.getCenter());
    }

    /**
     * Sets withdraw mode - true for noted items, false for un-noted
     * @param which
     */
    private void withdrawNote(boolean which) {
        if(!isOpen())
            return;

        IComponent n = botEnv.interfaces.getComponent(BANK_INTERFACE_ID, BANK_BUTTON_NOTE);
        if(n == null) return;
        if(which) {
            // Set to withdraw-notes
            if(n.getTextureID() != 1433)
                n.doClick();
        } else {
            if(n.getTextureID() != 1431)
                n.doClick();
        }
    }


}
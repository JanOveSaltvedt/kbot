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

import java.util.Vector;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.fetch.Game;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.IComponent;

/**
 * @author 933pm
 */
public class Equipment extends ModuleConnector {

	public final static int SLOTS_HEAD = 8;
	public final static int SLOTS_CAPE = 11;
	public final static int SLOTS_NECK = 14;
	public final static int SLOTS_WEAPON = 17;
	public final static int SLOTS_CHEST = 20;
	public final static int SLOTS_SHIELD = 23;
	public final static int SLOTS_LEGS = 26;
	public final static int SLOTS_HANDS = 29;
	public final static int SLOTS_FEET = 32;
	public final static int SLOTS_RING = 35;
	public final static int SLOTS_AMMO = 38;
	public final static int[] SLOT_IDS = {SLOTS_HEAD, SLOTS_CAPE, SLOTS_NECK, SLOTS_WEAPON, SLOTS_CHEST, SLOTS_SHIELD, SLOTS_LEGS, SLOTS_HANDS, SLOTS_FEET, SLOTS_RING, SLOTS_AMMO};
	public final static int BUTTONS_STATS = 39;
	public final static int BUTTONS_PRICE = 42;
	public final static int BUTTONS_DEATH = 45;
	private final int interfaceID = 387;
	private BotEnvironment env = null;

	public enum Slots {
		HEAD, CAPE, NECK, WEAPON, CHEST, SHIELD, LEGS, HANDS, FEET, RING, AMMO
	}

	public Equipment(BotEnvironment botEnv) {
		super(botEnv);
        env = botEnv;
	}

	/**
	 * Opens the Equipment Stats screen and checks.
	 *
	 * @return Kilograms currently carried.
	 */
	public int getWeightCarried() {
		openEquipmentStats();
		String s = env.interfaces.getComponent(667, 32).getText().replace(" kg", "");
		return Integer.parseInt(s);
	}

	/**
	 * Opens the "Items Kept On Death" interface and checks.
	 *
	 * @return Wealth currently carried.
	 */
	public int getCarriedWealth() {
		openItemsKeptOnDeath();
		String s = env.interfaces.getComponent(102, 31).getText().replace("Carried wealth:<br>    ", "");
		return Integer.parseInt(s);
	}

	/**
	 * Opens the "Items Kept On Death" interface and checks.
	 *
	 * @return Wealth currently risked.
	 */
	public int getRiskedWealth() {
		openItemsKeptOnDeath();
		String s = env.interfaces.getComponent(102, 32).getText().replace("Risked wealth:<br>    ", "");
		return Integer.parseInt(s);
	}

	/**
	 * Opens the Equipment Stats interface.
	 */
	public void openEquipmentStats() {
		setTab();
		env.interfaces.getComponent(interfaceID, BUTTONS_STATS).doClick();
	}

	/**
	 * Opens the Price Checker interface.
	 */
	public void openPriceChecker() {
		setTab();
		env.interfaces.getComponent(interfaceID, BUTTONS_PRICE).doClick();
	}

	/**
	 * Opens the "Items Kept On Death" interface.
	 */
	public void openItemsKeptOnDeath() {
		setTab();
		env.interfaces.getComponent(interfaceID, BUTTONS_DEATH).doClick();
	}

	/**
	 * @return IComponents of all the equipment slots.
	 */
	public IComponent[] getSlotIComponents() {
		IComponent[] ica = new IComponent[11];
		int index = 0;
		for (int id : SLOT_IDS) {
			ica[index] = env.interfaces.getComponent(interfaceID, id);
			index++;
		}
		return ica;
	}

	/**
	 * @param itemNames Item(s) name to check for.
	 * @return Whether or not the specified item is currently equipped.
	 */
	public boolean isItemEquipped(String... itemNames) {
		setTab();
		for (IComponent slot : getSlotIComponents()) {
			for (String s : itemNames)
				if (slot.getElementName().equals(s)) {
					return true;
				}
		}
		return false;
	}

	/**
	 * @param itemIDs ID(s) to check for.
	 * @return Whether or not the specified item is currently equipped.
	 */
	public boolean isItemEquipped(int... itemIDs) {
		setTab();
		for (IComponent slot : getSlotIComponents()) {
			for (int i : itemIDs)
				if (slot.getElementID() == i) {
					return true;
				}
		}
		return false;
	}

	/**
	 * @return Array of all currently equipped item names.
	 */
	public String[] getSlotItemNames() {
		Vector<String> v = new Vector<String>();
		for (IComponent slot : getSlotIComponents()) {
			String s = slot.getElementName();
			if (s != null && s != "") {
				v.add(s);
			}
		}
		String[] sa = new String[v.size()];
		int index = 0;
		for (String s : v) {
			sa[index] = s;
			index++;
		}
		return sa;
	}

	/**
	 * @return Array of all currently equipped item IDs.
	 */
	public int[] getSlotItemIDs() {
		Vector<Integer> v = new Vector<Integer>();
		for (IComponent slot : getSlotIComponents()) {
			int i = slot.getElementID();
			if (i != -1) {
				v.add(i);
			}
		}
		int[] ia = new int[v.size()];
		int index = 0;
		for (int i : v) {
			ia[index] = i;
			index++;
		}
		return ia;
	}

	/**
	 * @param slot Equipment slot to check.
	 * @return Whether or not the specified slot is empty.
	 */
	public boolean isEmpty(Slots slot) {
		return getEquippedItemID(slot) == -1;
	}

	/**
	 * @param slot Slot to get the item name of.
	 * @return ID of item equipped in the specified equipment slot.
	 */
	public int getEquippedItemID(Slots slot) {
		setTab();
		return getSlotIComponent(slot).getElementID();
	}

	/**
	 * @param slot Slot to get the item name of.
	 * @return Name of item equipped in the specified equipment slot.
	 */
	public String getEquippedItemName(Slots slot) {
		setTab();
		return getSlotIComponent(slot).getElementName();
	}

	/**
	 * @return Amount of ammo currently equipped.
	 */
	@Deprecated
	public int getAmmoStackSize() {
		setTab();
		return getSlotIComponent(Slots.AMMO).getElementStackSize();
	}

	/**
	 * @param arrow Is a type of arrow, if not checks the weapon slot
	 * @return Amount of ammo currently equipped.
	 */
	public int getAmmoStackSize(boolean arrow) {
		setTab();
		return getSlotIComponent(arrow ? Slots.AMMO : Slots.WEAPON).getElementStackSize();
	}

	/**
	 * Opens the equipment tab.
	 */
	public void setTab() {
		if (env.game.getCurrentTab() != Game.TAB_EQUIPMENT) {
			env.game.openTab(Game.TAB_EQUIPMENT);
		}
	}

	/**
	 * @param s Slot to get IComponent of.
	 * @return IComponent of the specified equipment slot.
	 */
	public IComponent getSlotIComponent(Slots s) {
		switch (s) {
			case HEAD:
				return env.interfaces.getComponent(interfaceID, SLOTS_HEAD);
			case CAPE:
				return env.interfaces.getComponent(interfaceID, SLOTS_CAPE);
			case NECK:
				return env.interfaces.getComponent(interfaceID, SLOTS_NECK);
			case WEAPON:
				return env.interfaces.getComponent(interfaceID, SLOTS_WEAPON);
			case CHEST:
				return env.interfaces.getComponent(interfaceID, SLOTS_CHEST);
			case SHIELD:
				return env.interfaces.getComponent(interfaceID, SLOTS_SHIELD);
			case LEGS:
				return env.interfaces.getComponent(interfaceID, SLOTS_LEGS);
			case HANDS:
				return env.interfaces.getComponent(interfaceID, SLOTS_HANDS);
			case FEET:
				return env.interfaces.getComponent(interfaceID, SLOTS_FEET);
			case RING:
				return env.interfaces.getComponent(interfaceID, SLOTS_RING);
			case AMMO:
				return env.interfaces.getComponent(interfaceID, SLOTS_AMMO);
		}
		return null;
	}
}
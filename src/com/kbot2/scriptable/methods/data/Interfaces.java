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
import com.kbot2.scriptable.methods.wrappers.Interface;
import com.kbot2.scriptable.methods.wrappers.InterfaceGroup;
import com.kbot2.scriptable.methods.wrappers.Item;
import com.kbotpro.hooks.Client;
import com.kbotpro.scriptsystem.wrappers.IComponent;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 10.apr.2009
 * Time: 16:52:06
 */
public class Interfaces extends Data{

    public Interfaces(BotEnvironment botEnv) {
        super(botEnv);
    }

    public InterfaceGroup getInterfaceGroup(int ID) {
        com.kbotpro.scriptsystem.wrappers.Interface anInterface = botEnv.proBotEnvironment.interfaces.getInterface(ID);
        if(anInterface == null){
            return null;
        }
        return new InterfaceGroup(anInterface.getComponents(), ID, botEnv);
    }

    public Interface getInterface(int parrentID, int childID) {
        if(childID < 0){
            throw new IllegalArgumentException("childID can not be < 0");
        }
        InterfaceGroup group = getInterfaceGroup(parrentID);
        if(group == null){
            return null;
        }
        Interface[] interfaces = group.getChildren();
        if(childID >= interfaces.length){
            throw new IllegalArgumentException("childID out of range.");
        }
        return interfaces[childID];
    }

    public Client getClient() {
        return botEnv.getClient();
    }


    /**
     * Checks if an interface exists
     * @param groupID
     * @param interfaceID
     * @return
     */
    public boolean interfaceExists(int groupID, int interfaceID){
        return getInterface(groupID, interfaceID) != null;
    }

    /**
     * Checks if an interface exists.
     * @param groupID
     * @return
     */
    public boolean interfaceGroupExists(int groupID){
        return getInterfaceGroup(groupID) != null && getInterfaceGroup(groupID).isValid();
    }

    public static int EQUIPMENT_HELMET = 0;
    public static int EQUIPMENT_CAPE = 1;
    public static int EQUIPMENT_AMULET = 2;
    public static int EQUIPMENT_WEAPON = 3;
    public static int EQUIPMENT_CHEST = 4;
    public static int EQUIPMENT_SHIELD = 5;
    public static int EQUIPMENT_STORAGE = 6;
    public static int EQUIPMENT_LEGS = 7;
    public static int EQUIPMENT_GLOVE = 9;
    public static int EQUIPMENT_BOOT = 10;
    public static int EQUIPMENT_RING  = 12;

    /**
     * Gets the item at a given position.
     * positions is given in the constants begining with EQUIPMENT_.
     * @param positon f.ex Interfaces.EQUIPMENT_HELMET
     * @return an item. This might not be a valid.
     */
    public Item getEquipmentItem(int positon){
        Interface equipment = getInterface(387, 29);
        return new Item(equipment.getItemIDArray()[positon]-1, equipment.getItemStackSizeArray()[positon]);
    }

    /**
	 * A method that searches through the interfaces for
	 * specified text. If it finds the text, the interface gets
	 * added to the array.
	 * Useful for things like "Click here to continue".
	 *
	 * @author alowaniak
	 * @param text The String the child interface should contain.
	 * @return An array of interfaces.
	 */
	public Interface[] getInterfaces(String text) {
        IComponent[] interfaces = botEnv.proBotEnvironment.interfaces.getInterfaces(text);
        if(interfaces == null || interfaces.length == 0){
            return new Interface[0];
        }

        Interface[] out = new Interface[interfaces.length];
        for(int i = 0; i < out.length; i++){
            out[i] = new Interface(botEnv, interfaces[i], null, null);
        }
		return out;
	}

    /**
	 * A method that determines if you can click continue or not.
	 *
     * @author alowaniak
	 * @return true when you can click continue, false otherwise
	 */
	public boolean canContinue() {
		Rectangle rect = new Rectangle(5, 350, 510, 130);
		Interface[] psblConts = getInterfaces("Click here to continue");
		if(psblConts == null)
			return false;
		for(Interface iface : psblConts) {
			if(!iface.isValid())
				continue;
			if(rect.contains(iface.getRandomPointInside()) &&
					iface.textContainsIgnoreCase("Click here to continue"))
				return true;
		}
		return false;
    }

    /**
	 * A method that clicks the continue child interface.
	 *
     * @author alowaniak
	 * @return true when it clicked continue, false otherwise
	 */
	public boolean clickContinue() {
		Interface[] psblConts = getInterfaces("Click here to continue");
		Rectangle rect = new Rectangle(5, 350, 510, 130);
		if(psblConts == null)
			return false;
		Interface contIface = null;
		for(Interface iface : psblConts) {
			if(!iface.isValid())
				continue;
			if(rect.contains(iface.getRandomPointInside()) &&
					iface.textContainsIgnoreCase("Click here to continue")) {
				contIface = iface;
				break;
			}
		}
		if(contIface == null)
			return false;
		contIface.doClick();
		return true;
	}

    public Interface getInterface(String txt) {
        Interface[] ifaces = getInterfaces(txt);
        if(ifaces != null && ifaces.length > 0)
            return ifaces[0];
        return null;
    }

}

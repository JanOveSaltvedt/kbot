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



package com.kbotpro.scriptsystem.wrappers;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.interfaces.Targetable;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;

import java.util.regex.Pattern;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 21, 2009
 * Time: 8:19:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class Item extends Wrapper implements Targetable{
    private static Pattern pattern = Pattern.compile("<.+?>");
    public IComponent container;

    public Item(BotEnvironment botEnv, IComponent container) {
        super(botEnv);
        this.container = container;
    }

    /**
     * Gets the ID of the item
     * @return
     */
    public int getID(){
        return container.getElementID();
    }

    /**
     * Gets the stack size of the item
     * @return
     */
    public int getStackSize(){
        return container.getElementStackSize();
    }

    /**
     * Gets the formatted name.
     * This contains the runescape color system.
     *
     * @return will return a stirng like  <col=ff9040>Longbow
     * FF9040 is color which is close to orange.
     * This may also return null;
     */
    public String getFormattedName(){
        return container.getElementName();
    }

    /**
     * Gets the name of the item without Runescape formatting.
     * @return may return null if the item has no name.
     */
    public String getName(){
        String formattedName = getFormattedName();
        if(formattedName == null){
            return null;
        }
        return pattern.matcher(formattedName).replaceAll("");
    }

    /**
     * Gets the center position on screen,
     * @return
     */
    public Point getCenter() {
        return container.getCenter();
    }

    /**
     * Gets the clickable area.
     * @return
     */
    public Rectangle getBounds() {
        return container.getBounds();
    }

    /**
     * Get target
     *
     * @return
     */
    public MouseTarget getTarget() {
        return container.getTarget();
    }

    /**
     * Moves the mouse to the object and clicks at the given action
     * NOTE: Do not use this method while you have a mouse job active!
     * @param actionContains A string that the action contains. Case ignored
     * @return Boolean, true if succeeded, false if not.
     */
    public boolean doAction(final String actionContains) {
        return container.doAction(actionContains);
    }

    /**
     * Moves the mouse to the object and clicks at the given action
     * NOTE: Do not use this method while you have a mouse job active!
     * @return Boolean, true if succeeded, false if not.
     */
    public boolean doClick() {
        return container.doClick();
    }

    public boolean isValid() {
        return container.isValid();
    }

    /**
     * Gets the actions available to perform on this Iem.
     * @return Returns the actions available to perform on this item.
     */
    public String[] getActions() {
        return container.getActions();
    }

    /**
     * Checks if you can perform an action an IComponent.
     * @param action Action you're looking for, exact match only, case sensitive
     * @return Returns true if the Item has the desired action.
     */
    public boolean hasAction(String action) {
        for (String s : getActions()) {
            if (s != null && s.equals(action)) return true;
        }
        return false;
    }
}

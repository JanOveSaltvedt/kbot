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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.kbot2.scriptable.methods.Calculations.random;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 10.apr.2009
 * Time: 14:25:46
 */
public class Menu extends Data{
    private static final Pattern pattern = Pattern.compile("\\<.+?\\>");

    public Menu(BotEnvironment botEnv) {
        super(botEnv);
    }

     /**
     * @param actionContains case insensitive string that specifies the action to be performed,
     * @return succeeded
     */
    public boolean atMenu(String actionContains) {
        return botEnv.proBotEnvironment.menu.atMenu(actionContains);
    }

    public void closeMenu() {
        if (isMenuOpen()) {
            int idx = getMenuIndex("cancel");
            if (idx != -1) {
                atMenuItem(idx);
            }
        }
    }

    public boolean isMenuOpen() {
        return botEnv.proBotEnvironment.menu.isOpen();
    }

    /**
     * Searches from top...
     *
     * @param actionContains
     * @return -1 if not found
     */
    public int getMenuIndex(String actionContains) {
        return botEnv.proBotEnvironment.menu.getMenuIndex(actionContains);
    }

    public String[] getMenuItems() {
        return botEnv.proBotEnvironment.menu.getMenuItems();
    }

    public String removeFormatting(String in) {
        if(in == null)
            return "null";
        return pattern.matcher(in).replaceAll("");
    }

    public boolean atMenuItem(int i) {
        return botEnv.proBotEnvironment.menu.atMenuItem(i);
    }



    /**
     * Get the selected item name.
     * @return
     */
    public String getLastSelectedItemName(){
        return "not implemented";
    }

    public String getLastSelectedSpellName(){
        return "not implemented";
    }

    /**
     * Gets the selected action.
     * May only work when spells are selected.
     * @return
     */
    public String getLastSelectedSpellAction(){
        return "not implemented";
    }

    /**
     * Checks if a spell is selected.
     * @return
     */
    public boolean isSpellSelected(){
        return false;
    }
}

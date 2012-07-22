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

package com.kbot2.scriptable;

import com.kbot2.scriptable.methods.Methods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 3:04:01 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Script extends Methods {
    /**
     * Gets called before every loop.
     *
     * @return true to call loop() and false to stop the script.
     */
    public boolean active() {
        return true;
    }

    /**
     * Register event listener and communicate with the user to st up the script here.
     *
     * @return if the script shall start or not.
     */
    public boolean onStart() {
        return true;
    }

    /**
     * Gets called before a script stops.
     * You should remove all event listeners and such here.
     */
    public void onStop() {
    }

    /**
     * Scripts loop.
     *
     * @return time to next loop.
     */
    public abstract int loop();

    /**
     * Get the scripts name
     *
     * @return
     */
    public abstract String getName();

    /**
     * Gets the script author
     *
     * @return
     */
    public abstract String getAuthor();

    /**
     * Gets the scripts author.
     *
     * @return
     */
    public abstract String getDescription();

    /**
     * Get the scripts Catergory
     *
     * @return
     */
    public String getCatergory() {
        return "none";
    }

    /**
     * Nicer naming in ScriptSelector
     *
     * @return
     */
    public final String toString() {
        return getName();
    }

    /**
     * Search tags.
     *
     * @return
     */
    public String[] getTags() {
        return new String[0];
    }

    /**
     * Extra information shown in ScriptUI
     *
     * @return
     */
    public String[][] getInformation() {
        List<String[]> information = new ArrayList<String[]>();
        information.add(new String[]{"No more information", ""});
        return information.toArray(new String[1][1]);
    }
}

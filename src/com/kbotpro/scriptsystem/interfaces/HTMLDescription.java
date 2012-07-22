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

package com.kbotpro.scriptsystem.interfaces;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 24.mai.2009
 * Time: 14:05:32
 */
public interface HTMLDescription {
    /**
     * Gets the document the script should display.
     * This should be written in HTML and include forms to get input from the user.
     * @return The HTML document
     */
    public String getDocument();

    /**
     * Gets called to set the arguments in the script.
     * @param args Map of all the arguments given my the HTML document.
     */
    public void setArguments(Map<String, String> args);
}
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

package com.kbotpro.scriptsystem.events;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Mar 17, 2010
 * Time: 8:06:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RandomListener extends EventListener {
    /**
     * Is called when a random event activates.
     * @param name The name of the random
     * @return  boolean, true if you want to allow this random to run, false if you want to make it not run this time.
     */
    public boolean randomActivated(String name);
}

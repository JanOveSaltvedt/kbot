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

package com.kbot2.handlers.eventSystem.eventListeners;

import java.awt.*;
import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 19.mar.2009
 * Time: 17:04:48
 */
public interface PaintListener extends EventListener {
    /**
     * Gets called on each repaint.
     * This at default 50 times a second so try not to do anything resource intensive.
     * @param g instance of the graphics object to paint on
     */
    public abstract void onRepaint(Graphics g);
}
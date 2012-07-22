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



package com.kbotpro.scriptsystem.interfaces;

import java.awt.*;

/**
 * Basic interface 
 */
public interface MouseTarget {
    /**
     * Should return a point within the target, the mouse will be moving towards this point, but it is not 100% sure that it will hit this exact pixel.
     *
     * @return Point: screen position of the target
     */
    public Point get();

    /**
     * Is called to check if the mouse is over the target.
     *
     * @param posX the screenX position to compare with
     * @param posY the screenY position to compare with
     * @return true if the point is over the target, false if not.
     */
    public boolean isOver(int posX, int posY);
}

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

package com.kbotpro.scriptsystem.spiderweb;

import java.awt.*;
import com.kbotpro.scriptsystem.wrappers.Tile;

/**
 *
 * @author Nader
 */
public class QuickUtil {
 /**
  * @Param bounds the bounds of the polygon.
  * @param loc the location to be checked if it was contained in the supported bounds.
  * @return true withing bounds, else false.
  * */
public static boolean isInArea(Tile loc,Tile[] bounds){
   Polygon p = new Polygon();
    for(Tile t : bounds)
        p.addPoint((int)t.toPoint().getX(), (int)t.toPoint().getY());
        return (p.contains(loc.toPoint().x,loc.toPoint().y));
}

   /**
     * @param a start vertex of segment ab
     * @param b end vertex of segment ab
     * @return distance between the two vertices/Tiles, based off of pythagorus Theory
     */
    public static int distanceBetween(Tile a, Tile b) {
        return (int) Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }
}

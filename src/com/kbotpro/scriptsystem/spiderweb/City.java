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

import com.kbotpro.scriptsystem.wrappers.Tile;

/**
 *
 * @author Nader
 */
public class City {

    private Tile[] components;
    private Tile[] bounds;

    /**
     *
     * @param components a Tile array that the current city is made up of.
     * @param bounds a Tile array the represents bounds of the city (polygon form).
     */
    public City(Tile[] components, Tile[] bounds) {
        this.components = components;
        this.bounds = bounds;
    }

    public Tile[] getComponents() {
        return components;
    }

    public Tile[] getBounds() {
        return bounds;
    }
}

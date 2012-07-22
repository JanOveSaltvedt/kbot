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

import com.kbotpro.scriptsystem.wrappers.Tile;

/**
 * Interface that should be implemented by everything that has a world location in Runescape.
 *
 * Dictionary:
 * - Worldwide location = This is a global location identifier.
 * - Regional location = This is a regional location identifier.
 *      A regional location can not tell where something is worldwide but within a runescape region. ( Containing 104x104 tiles).
 *      1 Tile =  512x512 regional units.
 */
public interface WorldLocation {
    /**
     * Gets the Tile location.
     *
     * @param baseX baseX must be suplied to calculate this.
     * @param baseY baseY must be suplied to calculate this.
     * @return Returns a Tile containing the worldwide location
     */
    public Tile getTile(int baseX, int baseY);

    /**
     * Gets the regional X location
     * @param baseX baseX must be suplied to calculate this.
     * @return Returns an integer containing the regional y location
     */
    public int getRegionalX(int baseX);

    /**
     * Gets the regional Y location
     * @param baseY baseY must be suplied to calculate this.
     * @return Returns an integer containing the regional y location
     */
    public int getRegionalY(int baseY);

    /**
     * Same as getTile, but may not work on all implementations as baseX and baseY is not provided
     * @return
     */
    public Tile getLocation();
}

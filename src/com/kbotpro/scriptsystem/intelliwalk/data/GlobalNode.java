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

package com.kbotpro.scriptsystem.intelliwalk.data;

import com.kbotpro.scriptsystem.wrappers.Tile;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: PwnZ
 * Date: May 27, 2010
 * Time: 12:22:39 AM
 */
public class GlobalNode {
    private Tile tile;
    private ArrayList<GlobalNode> connected = new ArrayList<GlobalNode>();

    /**
     * @author PwnZ
     * @param tile node location
     */
    public GlobalNode(Tile tile) {
        this.tile = tile;
    }

    /**
     * @author PwnZ
     * @param x node location x
     * @param y node location y
     */
    public GlobalNode(final int x, final int y) {
        this.tile = new Tile(x, y);
    }

    /**
     * Gets the tile of the GlobalNode
     * @author PwnZ
     * @return tile
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Gets connected global nodes
     * @author PwnZ
     * @return connected global nodes
     */
    public GlobalNode[] getConnected() {
        return connected.toArray(new GlobalNode[connected.size()]);
    }

    /**
     * Adds a node to this GlobalNode's connected array
     * @author PwnZ
     * @param g node to be added
     * @return node added
     */
    public boolean addNodeToConnected(GlobalNode g) {
        if (connected.contains(g))
            return true;

        return connected.add(g);
    }

    /**
     * Connects this GlobalNode to GlobalNode g
     * @PwnZ
     * @param g node to be connected to
     * @return connected to GlobalNode g
     */
    public boolean connectToNode(GlobalNode g) {
        return (g.addNodeToConnected(this)
                && addNodeToConnected(g));
    }

}

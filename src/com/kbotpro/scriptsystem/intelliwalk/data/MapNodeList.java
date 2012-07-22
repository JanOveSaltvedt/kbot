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

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.Tile;
import org.apache.commons.lang.ArrayUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: PwnZ
 * Date: May 25, 2010
 * Time: 7:27:14 PM
 */
public class MapNodeList extends ModuleConnector {
    private MapNode parentNode;
    private ArrayList<MapNode> nodeList = new ArrayList<MapNode>();
    private ArrayList<Tile> invalidTiles;

    /**
     * @author PwnZ
     * @param parentNode the root node
     * @param invalid starting invalid tiles
     * @param botEnv the botEnvironment
     */
    public MapNodeList(MapNode parentNode, Tile[] invalid, BotEnvironment botEnv) {
        super(botEnv);

        if (invalid == null || invalid.length <= 0)
            invalidTiles = new ArrayList<Tile>();
        else
            invalidTiles = new ArrayList<Tile>(Arrays.asList(invalid));

        this.parentNode = parentNode;
        nodeList.add(parentNode);
        invalidTiles.add(parentNode.getTile());
    }


    /**
     * Populates nodelist using A* algorithm
     * @author PwnZ
     * @param dest destination tile
     */
    public void populateNodeList(final Tile dest) {
        while (!nodeListContains(dest)) {
            MapNode last = new MapNode(null, botEnv.players.getMyPlayer().getLocation(), botEnv);
            last = nodeList.get(nodeList.size()-1);
            Tile tile = last.calculateLeastCost(dest, invalidTiles.toArray(new Tile[invalidTiles.size()]));
            populateInvalidTiles(last.getNewInvalidTiles());
            MapNode next = new MapNode(last, tile, this.botEnv);
            nodeList.add(next);
        }
    }

    /**
     * Populates the invalid tile array
     * @author PwnZ
     * @param p invalid tile array
     */
    public void populateInvalidTiles(Tile... p) {
        Tile[] invalidArray = invalidTiles.toArray(new Tile[invalidTiles.size()]);

        if (p == null)
            return;

        for (Tile a : p) {
            if (!ArrayUtils.contains(invalidArray, a))
                invalidTiles.add(a);

        }
    }

    /**
     * Gets the Node[] object
     * @author PwnZ
     * @return node array
     */
    public MapNode[] getNodeArray() {
        return nodeList.toArray(new MapNode[nodeList.size()]);
    }

    /**
     * @author PwnZ
     * @param tile the tile
     * @return node list contains tile
     */
    public boolean nodeListContains(Tile tile) {
        MapNode[] nodes = nodeList.toArray(new MapNode[nodeList.size()]);
        for (MapNode n : nodes)
            if (n.getTile().equals(tile))
                return true;

        return false;
    }

    /**
     * Gets all InvalidTiles
     * @author PwnZ
     * @return all invalid tiles
     */
    public Tile[] getInvalidTilesForList() {
        return invalidTiles.toArray(new Tile[invalidTiles.size()]);
    }
}

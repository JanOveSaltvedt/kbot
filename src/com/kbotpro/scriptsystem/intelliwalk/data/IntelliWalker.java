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
import com.kbotpro.scriptsystem.intelliwalk.resources.GlobalNodes;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.Tile;

/**
 * Created by IntelliJ IDEA.
 * User: PwnZ
 * Date: May 27, 2010
 * Time: 12:21:07 AM
 */
public class IntelliWalker extends ModuleConnector {
    protected GlobalNodes globalNodes = new GlobalNodes();
    protected BotEnvironment botEnv;

    /**
     * @author PwnZ
     * @param botEnv the botEnvironment
     */
    public IntelliWalker(BotEnvironment botEnv) {
        super(botEnv);
        this.botEnv = botEnv;
        globalNodes.constructGlobalNodeArray();
    }

    /**
     * Generates a MapNodeList that contains the local path.
     * @author PwnZ
     * @param start starting location
     * @param finish ending location
     * @return MapNodeList
     */
    public MapNodeList generateLocalPath(final Tile start, final Tile finish) {
        MapNode startNode = new MapNode(null, start, botEnv);
        MapNodeList mapNodeList = new MapNodeList(startNode, null, botEnv);

        mapNodeList.populateNodeList(finish);
        return mapNodeList;
    }

    /**
     * Walks the local path
     * @author PwnZ
     * @param nodeList the generated nodeList
     * @param randX for each tile movement, randX amount of randomness (for the tile coordinate)
     * @param randY for each tile movement, randY amount of randomness (for the tile coordinate)
     * @param flag the amount of distance to destination before next tile movement allowed
     * @return arrived at destination
     */
    public boolean walkLocalPath(MapNodeList nodeList, final int randX, final int randY, final int flag) {
        MapNode[] path = nodeList.getNodeArray();
        Tile destination = (path[path.length - 1]).getTile();
        boolean finished = false;
        while (!(finished = botEnv.walking.walkToMM(destination))) {
            sleep(random(800, 1200));

            if (botEnv.players.getMyPlayer().isMoving()) {
                final Tile dest = botEnv.walking.getDestination();
                if(dest != null)
                if (((int) botEnv.players.getMyPlayer().distanceTo(dest)) > flag) {
                    continue;
                }
            }

            for (int i = path.length - 1; botEnv.walking.walkToMM(path[i].getTile().randomizeTile(randX, randY)); i -= 0) {
                if (i - 5 <= 0)
                    break;
            }

        }

        return finished;
    }

    public boolean walkLocalPath(MapNodeList nodeList, final int randX, final int randY) {
        return walkLocalPath(nodeList, randX, randY, 0);
    }

    public boolean walkLocalPathCont(MapNodeList nodeList, final int randX, final int randY) {
        return walkLocalPath(nodeList, randX, randY, random(4, 8));
    }

    public boolean walkLocalPathNonRand(MapNodeList nodeList, final int flag) {
        return walkLocalPath(nodeList, 0, 0, flag);
    }

    public boolean walkLocalPathRand(MapNodeList nodeList, final int randX, final int randY) {
        return walkLocalPath(nodeList, randX, randY, 0);
    }

    public boolean walkLocalPathRand(MapNodeList nodeList) {
        return walkLocalPathCont(nodeList, 3, 3);
    }

    /**
     * Gets the GlobalNodes object
     * @author PwnZ
     * @return GlobalNodes
     */
    public GlobalNodes getGlobalNodes(){
        return globalNodes;
    }
}

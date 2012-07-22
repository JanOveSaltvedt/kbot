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

package com.kbotpro.scriptsystem.intelliwalk.data;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.hooks.Client;
import com.kbotpro.hooks.MapData;
import com.kbotpro.scriptsystem.intelliwalk.resources.IWCalculations;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.Tile;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: PwnZ
 * Date: May 25, 2010
 * Time: 7:00:45 PM
 */
public class MapNode extends ModuleConnector {
    private MapNode parent;
    private Tile tile;
    private int tileData = -1;

    private Tile[] orthogonalArray = new Tile[4], diagonalArray = new Tile[4];
    private Tile[] invalidTiles = new Tile[0];


    /**
     * @param parent Parent MapNode
     * @param tile   Node tile location
     * @param botEnv botEnvironment
     * @author PwnZ
     */
    public MapNode(MapNode parent, Tile tile, BotEnvironment botEnv) {
        super(botEnv);
        this.parent = parent;
        this.tile = tile;

        /* Orthogonal */
        orthogonalArray[0] = new Tile(tile.getX(), tile.getY() + 1);
        orthogonalArray[1] = new Tile(tile.getX(), tile.getY() - 1);
        orthogonalArray[2] = new Tile(tile.getX() + 1, tile.getY());
        orthogonalArray[3] = new Tile(tile.getX() - 1, tile.getY());


        /* Diagonal */
        diagonalArray[0] = new Tile(tile.getX() + 1, tile.getY() + 1);
        diagonalArray[1] = new Tile(tile.getX() - 1, tile.getY() + 1);
        diagonalArray[2] = new Tile(tile.getX() + 1, tile.getY() - 1);
        diagonalArray[3] = new Tile(tile.getX() - 1, tile.getY() - 1);

        final Client client = botEnv.client;
        if(client == null)
            return;
        
        final MapData mapData = client.getMapDataArray()[client.getCurrentPlane()];
        if (mapData == null || mapData.getTileData().length < 1) {
            return;
        } else {
            int x = (tile.getX() - client.getBaseX()) + 1;
            int y = (tile.getY() - client.getBaseY()) + 1;

            tileData = mapData.getTileData()[x][y];
        }
    }

    @Override
    public String toString() {
        return "MapNode{" +
                "tile=" + tile +
                '}';
    }

    /**
     * @param dest               Destination
     * @param invalidTilesGlobal Invalid Tiles Global Array
     * @return The least costing tile from the surrounding 8 tiles (A* Algo)
     * @author PwnZ
     */
    protected Tile calculateLeastCost(final Tile dest, final Tile... invalidTilesGlobal) {
        Tile p = tile;
        double lowestCost = 9999.0;
        double cost;
        ArrayList<Tile> invalid = new ArrayList<Tile>();
        int i = -1;

        for (Tile o : orthogonalArray) {
            i++;
            if (ArrayUtils.contains(invalidTilesGlobal, o))
                continue;

            if (tileData != 0){
                if((IWCalculations.getTileData(o, botEnv.client) & (IWCalculations.FULL_BLOCK | IWCalculations.BLOCKED_0)) != 0){
                    invalid.add(o);
                    continue;
                }

                if (IWCalculations.calculateUnwalkable(IWCalculations.movementOrder[i], getTileData())) {
                    invalid.add(o);
                    continue;
                }
            }

            cost = IWCalculations.ORTHOGONAL_COST + (Math.abs((dest.getX() - o.getX())) + Math.abs((dest.getY() - o.getY())));
            if (cost < lowestCost) {
                invalid.add(p);
                lowestCost = cost;
                p = o;
            } else {
                invalid.add(o);
            }
        }

        for (Tile d : diagonalArray) {
            i++;

            if (ArrayUtils.contains(invalidTilesGlobal, d))
                continue;

            if (tileData != 0){
                if((IWCalculations.getTileData(d, botEnv.client) & (IWCalculations.FULL_BLOCK | IWCalculations.BLOCKED_0)) != 0){
                    invalid.add(d);
                    continue;
                }

                if (IWCalculations.calculateUnwalkable(IWCalculations.movementOrder[i], getTileData())) {
                    invalid.add(d);
                    continue;
                }
            }

            cost = IWCalculations.DIAGONAL_COST + (Math.abs((dest.getX() - d.getX())) + Math.abs((dest.getY() - d.getY())));
            if (cost < lowestCost) {
                invalid.add(p);
                lowestCost = cost;
                p = d;
            } else {
                invalid.add(d);
            }
        }

        invalidTiles = invalid.toArray(new Tile[invalidTiles.length]);
        return p;
    }

    /**
     * @return the invalid tiles array.
     * @author PwnZ
     */
    public Tile[] getNewInvalidTiles() {
        return invalidTiles;
    }

    /**
     * @return the Tile of this MapNode
     * @author PwnZ
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * @return the parent MapNode of this MapNode
     * @author PwnZ
     */
    public MapNode getParent() {
        return parent;
    }

    /**
     * Sets parent node
     *
     * @param parent the MapNode
     * @author PwnZ
     */
    public void setParent(final MapNode parent) {
        this.parent = parent;
    }

    /**
     * sets invalid tile array
     *
     * @param invalid the Tile[]
     * @author PwnZ
     */
    public void setInvalidTiles(final Tile... invalid) {
        invalidTiles = invalid;
    }

    /**
     * @return this MapNode's tile data
     * @author PwnZ
     */
    public int getTileData() {
        return tileData;
    }
}

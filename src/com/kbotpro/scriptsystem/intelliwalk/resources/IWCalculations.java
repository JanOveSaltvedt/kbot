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

package com.kbotpro.scriptsystem.intelliwalk.resources;

import com.kbotpro.hooks.Client;
import com.kbotpro.hooks.MapData;
import com.kbotpro.scriptsystem.wrappers.Tile;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: PwnZ
 * Date: May 25, 2010
 * Time: 7:03:40 PM
 */
public class IWCalculations {

    /* Hardcoded in MapNode */
    public static final Movement[] movementOrder = new Movement[] {
        Movement.NORTH,
        Movement.SOUTH,
        Movement.EAST,
        Movement.WEST,
        Movement.NORTH_EAST,
        Movement.NORTH_WEST,
        Movement.SOUTH_EAST,
        Movement.SOUTH_WEST
    };

    public static final double ORTHOGONAL_COST = 1.0;
    public static final double DIAGONAL_COST = 1.414;

    private static final int BLOCKING_OBJECT = 0x100;

    public static final int BLOCKED_0_NORTH = 0x2;
    public static final int BLOCKED_0_NORTH_EAST = 0x4;
    public static final int BLOCKED_0_EAST = 0x8;
    public static final int BLOCKED_0_SOUTH_EAST = 0x10;
    public static final int BLOCKED_0_SOUTH = 0x20;
    public static final int BLOCKED_0_SOUTH_WEST = 0x40;
    public static final int BLOCKED_0_WEST = 0x80;
    public static final int BLOCKED_0_NORTH_WEST = 0x200;
    public static final int BLOCKED_0 =
            BLOCKED_0_NORTH | BLOCKED_0_NORTH_EAST | BLOCKED_0_EAST | BLOCKED_0_SOUTH_EAST |
            BLOCKED_0_SOUTH | BLOCKED_0_SOUTH_WEST | BLOCKED_0_WEST | BLOCKED_0_NORTH_WEST;


    public static final int BLOCKED_1_NORTH = 0x400 | 0x800000;
    public static final int BLOCKED_1_NORTH_EAST = 0x800 | 0x1000000;
    public static final int BLOCKED_1_EAST = 0x1000 | 0x2000000;
    public static final int BLOCKED_1_SOUTH_EAST = 0x2000 | 0x4000000;
    public static final int BLOCKED_1_SOUTH = 0x4000 | 0x8000000;
    public static final int BLOCKED_1_SOUTH_WEST = 0x8000 | 0x10000000;
    public static final int BLOCKED_1_WEST = 0x10000 | 0x20000000;
    public static final int BLOCKED_1_NORTH_WEST = 0x40000 | 0x80000000;

    public static final int FULL_BLOCK = 0x100 | 0x40000 | 0x200000;

    public static boolean calculateUnwalkable(Movement m, int mapData){
        
        switch (m){
            case NORTH:
            if ((mapData & BLOCKED_1_NORTH) != 0){
                 System.out.println("Tile blocked north.");
                 return true;
            }
                break;
            case SOUTH:
            if ((mapData & BLOCKED_1_SOUTH) != 0){
                 System.out.println("Tile blocked south.");
                 return true;
            }

                break;
            case EAST:
            if ((mapData & BLOCKED_1_EAST) != 0){
                 System.out.println("Tile blocked east.");
                 return true;
            }

                break;
            case WEST:
            if ((mapData & BLOCKED_1_WEST) != 0){
                 System.out.println("Tile blocked west.");
                 return true;
            }

                break;
            case NORTH_EAST:
            if ((mapData & BLOCKED_1_NORTH_EAST) != 0){
                 System.out.println("Tile blocked north east.");
                 return true;
            }

                break;
            case NORTH_WEST:
            if ((mapData & BLOCKED_1_NORTH_WEST) != 0){
                 System.out.println("Tile blocked north west.");
                 return true;
            }

                break;
            case SOUTH_EAST:
            if ((mapData & BLOCKED_1_SOUTH_EAST) != 0){
                 System.out.println("Tile blocked south east.");
                 return true;
            }

                break;
            case SOUTH_WEST:
            if ((mapData & BLOCKED_1_SOUTH_WEST) != 0){
                 System.out.println("Tile blocked south west.");
                 return true;
            }
                break;
        }
        return false;
    }

    public static enum Movement {
        NORTH, SOUTH, EAST, WEST, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST
    }

    public static int getTileData(final Tile t, final Client client){
        if(client == null)
            return 0;

        final MapData mapData = client.getMapDataArray()[client.getCurrentPlane()];
        if (mapData == null || mapData.getTileData().length < 1) {
            return 0;
        } else {
            int x = (t.getX() - client.getBaseX()) + 1;
            int y = (t.getY() - client.getBaseY()) + 1;

            return mapData.getTileData()[x][y];
        }
    }
}

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

package com.kbotpro.scriptsystem.fetch;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.runnable.Script;
import com.kbotpro.scriptsystem.spiderweb.Cities;
import com.kbotpro.scriptsystem.spiderweb.City;
import com.kbotpro.scriptsystem.spiderweb.QuickUtil;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.Tile;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.awt.Point;

/**
 * @author Nader
 */
public class SpiderWeb extends ModuleConnector {

    private final int map_rad = 15;
    private final int range_limit = 7;

    public SpiderWeb(BotEnvironment botEnv) {
        super(botEnv);
    }

    private void walkToTile(Tile t) {
        Point dot = botEnv.calculations.tileToMinimap(t);

        botEnv.mouse.moveMouse(dot, true);
    }

    /**
     * continuos walking based on sleep time or range limit to find the next Tile
     *
     * @param target the targeted tile , to walk to.
     */
    public void walkTo(Tile target) {
        Tile t = null;
        Tile myloc = botEnv.players.getMyPlayer().getLocation();
        Point dot = botEnv.calculations.tileToMinimap(target);
        loop:
        while (dot.x == -1 && dot.y == -1) {
            try {
                myloc = botEnv.players.getMyPlayer().getLocation();
                if (myloc.equals(target)) {
                    break;
                }
                dot = botEnv.calculations.tileToMinimap(target);
                if (dot.getX() != -1 && dot.getY() != -1) {
                    walkToTile(target);
                }
                t = getClosestTileTo(target);
                if (t == null) {
                    break;
                }
                if (t.equals(target)) {
                    break;
                }

                int randomX = random(-1, 2);
                int randomY = random(-1, 2);
                t = new Tile(t.getX()+randomX, t.getY()+randomY);
                Point p = botEnv.calculations.tileToMinimap(t);
                if (p.x != -1 && p.y != -1) {
                    walkToTile(t);
                    sleep(3000, 4000);
                }
                if (t.equals(target)) break loop;//breaks loop when reached target.
            }
            catch (Exception e) {
                Logger.getRootLogger().error("Exception: ", e);
                continue loop;
            }
        }
    }

    /**
     * @param target targeted Tile, to walk to.
     * @return current closest tile on minimap to the target.
     */
    /* get closest tile based on few custom algorithms making use of built in methods*/
    private Tile getClosestTileTo(Tile target) {
        final Tile loc = botEnv.players.getMyPlayer().getLocation();
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        int last_dist = Integer.MAX_VALUE;
        int curr_dist = 0;
        Tile chosen = null;
        City curr_city = null;

        /* find the city corresponding to the current area*/
        l:
        for (City c : Cities.getCities()) {
            if (QuickUtil.isInArea(botEnv.players.getMyPlayer().getLocation(), c.getBounds())) {
                curr_city = c;
                break l;
            }
        }
        /* iterate through city components to find tiles that are on minimap and nearist to tartget ,
        then my location, then add them to a list, to compare the best one laster*/
        for (Tile t : curr_city.getComponents()) {
            if (QuickUtil.distanceBetween(loc, t) <= map_rad) {
                if (QuickUtil.distanceBetween(loc, target) > QuickUtil.distanceBetween(t, target)) {
                    tiles.add(t);
                }
            }
        }
        /* gets the nearist tile to the target on minimap , and returns it.*/             // Actually... A simple fix would be
        for (Tile curr : tiles) {
            if ((curr_dist = QuickUtil.distanceBetween(curr, target)) <= last_dist) {
                last_dist = curr_dist;
                chosen = curr;
            }
        }
        return chosen;

    }
}




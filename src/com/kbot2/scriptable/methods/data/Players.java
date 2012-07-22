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

package com.kbot2.scriptable.methods.data;

import com.kbot2.bot.BotEnvironment;
import com.kbot2.scriptable.methods.wrappers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 6:19:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Players extends Data {
    public Players(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Gets my player.
     *
     * @return player object
     */
    public Player getMyPlayer() {
        com.kbotpro.scriptsystem.wrappers.Player player = botEnv.proBotEnvironment.players.getMyPlayer();
        if(player == null){
            return null;
        }
        return new Player(botEnv, player);
    }

    /**
     * Gets a player by its name
     * @param name String: Name of players. Case insensetive.
     * @return player if player was found, if not found it returns null..
     */
    public Player getPlayer(String name){
        com.kbotpro.scriptsystem.wrappers.Player player = botEnv.proBotEnvironment.players.getPlayer(name);
        if(player == null){
            return null;
        }
        return new Player(botEnv, player);
    }

    /**
     * Gets an array of all Players in clients range.
     *
     * @return an array of all players within range
     */
    public Player[] getPlayers() {
        com.kbotpro.scriptsystem.wrappers.Player[] players = botEnv.proBotEnvironment.players.getPlayers();
        List<Player> out = new ArrayList<Player>();
        for (com.kbotpro.scriptsystem.wrappers.Player player : players) {
            if (player == null)
                continue;
            out.add(new Player(botEnv, player));
        }
        return out.toArray(new Player[out.size()]);
    }

    /**
     * Shortcut for getMyPlayer().getLocation().distanceTo(Tile tile);
     * @param wo Tile to get distance to. Can also be a Character, Object, NPC or anything else that implements WorldObject
     * @return int: the distance in tiles from my player to tile.
     */
    public int distanceTo(com.kbot2.scriptable.methods.interfaces.WorldObject wo){
        return getMyPlayer().getLocation().distanceTo(wo);
    }
}

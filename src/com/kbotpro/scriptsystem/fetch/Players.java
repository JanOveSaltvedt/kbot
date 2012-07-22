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

import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.NPC;
import com.kbotpro.scriptsystem.wrappers.Player;
import com.kbotpro.scriptsystem.interfaces.WorldLocation;
import com.kbotpro.bot.BotEnvironment;

import java.util.List;
import java.util.ArrayList;

/**
 * Class that handles NPC related methods.
 * Should just be innited by the BotEnvironment.
 */
public class Players extends ModuleConnector {
    public Players(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Gets all the players in the clients cache.
     * @return an array of Players, null proof.
     */
    public Player[] getPlayers() {
        com.kbotpro.hooks.Player[] players = getClient().getPlayers();
        List<Player> out = new ArrayList<Player>();
        for(com.kbotpro.hooks.Player rPlayer: players){
            if(rPlayer == null){
                continue;
            }
            out.add(new Player(botEnv, rPlayer));
        }
        return out.toArray(new Player[1]);
    }

    /**
     * Gets the current player from the clients cache.
     * @return returns the player or null under rare circumstances.
     */
    public Player getMyPlayer(){
        com.kbotpro.hooks.Player player = getClient().getMyPlayer();
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
        name = name.toLowerCase();
        for(Player player: getPlayers()){
            if(player == null){
                continue;
            }
            if(player.getName().toLowerCase().equals(name))
                return player;
        }
        return null;
    }

    /**
     * Shortcut for getMyPlayer().getLocation().distanceTo(Tile tile);
     * @param wo Tile to get distance to. Can also be a Character, Object, NPC or anything else that implements WorldObject
     * @return int: the distance in tiles from my player to tile.
     */
    public int distanceTo(WorldLocation wo){
        return (int) getMyPlayer().getLocation().distanceToPrecise(wo);
    }
}
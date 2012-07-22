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



package com.kbotpro.scriptsystem.wrappers;

import com.kbotpro.bot.BotEnvironment;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 8, 2009
 * Time: 12:57:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Player extends Character {
    private com.kbotpro.hooks.Player rPlayer;

    public Player(BotEnvironment botEnv, com.kbotpro.hooks.Player rPlayer) {
        super(botEnv, rPlayer);
        this.rPlayer = rPlayer;
    }

    /**
     * Get the display name
     * @return returns the players current display name,
     */
    public String getName(){
        return rPlayer.getDisplayName();
    }
}

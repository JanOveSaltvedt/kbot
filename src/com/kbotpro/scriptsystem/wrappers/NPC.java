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
import com.kbotpro.hooks.NPCDef;

/**
 * Wrapper for in game NPCs
 */
public class NPC extends Character{
    private com.kbotpro.hooks.NPC rNPC;

    public NPC(BotEnvironment botEnv, com.kbotpro.hooks.NPC rNPC) {
        super(botEnv, rNPC);
        this.rNPC = rNPC;
    }

    /**
     * Gets the NPC ID number.
     * Used to distinguish npcs.
     * @return The NPCs ID or -1 if some error occured.
     */
    public int getID(){
        NPCDef npcDef = rNPC.getNPCDef();
        if(npcDef == null){
            return -1;
        }
        return npcDef.getID();
    }

    /**
     * Gets the NPCs name.
     * @return returns the NPCs name, or {error} if an error occured. Can also return null if the npc has no name (or has not been given any name yet).
     */
    public String getName(){
        NPCDef npcDef = rNPC.getNPCDef();
        if(npcDef == null){
            return "{error}";
        }
        return npcDef.getName();
    }

    /**
     * Gets the menu actions this NPC has by default
      * @return an array of strings containing actions.
     */
    public String[] getActions(){
        NPCDef npcDef = rNPC.getNPCDef();
        if(npcDef == null){
            return new String[0];
        }
        return npcDef.getActions();
    }

    public Object getCollisionDetailLevel() {
        return rNPC.getNPCDef().getCollisionDetailLevel();
    }
}

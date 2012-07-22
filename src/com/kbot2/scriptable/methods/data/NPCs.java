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
import com.kbot2.scriptable.methods.wrappers.NPC;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 6:33:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class NPCs extends Data {
    public NPCs(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Gets an array of all NPCs in clients range.
     *
     * @return array of all NPCs in range
     */
    public NPC[] getNPCs() {
        com.kbotpro.scriptsystem.wrappers.NPC[] NPCs = botEnv.proBotEnvironment.npcs.getNPCs();
        List<NPC> out = new ArrayList<NPC>();
        for (com.kbotpro.scriptsystem.wrappers.NPC npc : NPCs) {
            if (npc == null)
                continue;
            out.add(new NPC(botEnv, npc));
        }
        return out.toArray(new NPC[1]);
    }

    /**
     * Gets closest NPC in given range by given IDs
     *
     * @param range Range to search in
     * @param ids   IDs to search for
     * @return If NPC is found; NPC otherwise; null
     * @author Alowaniak
     */
    public NPC getClosest(int range, int... ids) {
        com.kbotpro.scriptsystem.wrappers.NPC npc = botEnv.proBotEnvironment.npcs.getClosest(range, ids);
        if(npc == null){
            return null;
        }
        return new NPC(botEnv, npc);
    }

    /**
     * Gets closest NPC in given range by given names
     *
     * @param range Range to search in
     * @param names Names to search for
     * @return If NPC is found; NPC otherwise; null
     * @author Alowaniak
     */
    public NPC getClosest(int range, String... names) {
        com.kbotpro.scriptsystem.wrappers.NPC npc = botEnv.proBotEnvironment.npcs.getClosest(range, names);
        if(npc == null){
            return null;
        }
        return new NPC(botEnv, npc);

    }

    /**
     * Gets closest free NPC in given range by given IDs
     *
     * @param range Range to search in
     * @param ids   IDs to search for
     * @return If NPC is found; NPC otherwise; null
     * @author Alowaniak
     * @fixed/edited by Fatality
     */
    public NPC getClosestFree(int range, int... ids) {
        com.kbotpro.scriptsystem.wrappers.NPC npc = botEnv.proBotEnvironment.npcs.getClosestFree(range, ids);
        if(npc == null){
            return null;
        }
        return new NPC(botEnv, npc);
    }
}

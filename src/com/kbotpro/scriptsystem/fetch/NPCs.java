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
import com.kbotpro.hooks.NPCNode;
import com.kbotpro.hooks.Node;
import com.kbotpro.hooks.NodeCache;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.NPC;
import com.kbotpro.scriptsystem.wrappers.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles NPC related methods.
 * Should just be innited by the BotEnvironment.
 */
public class NPCs extends ModuleConnector {
    public NPCs(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Gets all the NPCs in the clients cache.
     *
     * @return an array of NPCs, null proof.
     */
    public NPC[] getNPCs() {
        /*com.kbotpro.hooks.NPC[] npcs = getClient().getNpcs();
        List<NPC> out = new ArrayList<NPC>();
        for(com.kbotpro.hooks.NPC rNPC: npcs){
            if(rNPC == null){
                continue;
            }
            out.add(new NPC(botEnv, rNPC));
        }
        return out.toArray(new NPC[1]);*/

        /*NPCNode[] npcNodes = getClient().getNPCNodes();
      List<NPC> out = new ArrayList<NPC>();
      for(NPCNode node: npcNodes){
          if(node == null){
              continue;
          }
          com.kbotpro.hooks.NPC npc = node.getNPC();
          if(npc == null){
              continue;
          }
          NPCDef npcDef = npc.getNPCDef();
          if(npcDef == null){
              continue;
          }
          if(npcDef.getID() == -1){
              continue;
          }

          out.add(new NPC(botEnv, npc));
      }
      return out.toArray(new NPC[out.size()]);*/
        NodeCache nodeCache = getClient().getNPCNodeCache();
        List<NPC> out = new ArrayList<NPC>();
        for (Node node : nodeCache.getNodes()) {
            if (node == null) {
                continue;
            }
            Node curNode = node;
            do {
                if (curNode instanceof NPCNode) {
                    NPCNode npcNode = (NPCNode) curNode;
                    com.kbotpro.hooks.NPC npc = npcNode.getNPC();
                    if (npc == null) {
                        continue;
                    }
                    out.add(new NPC(botEnv, npc));
                }
                curNode = curNode.getPrevNode();
            } while (curNode != null && curNode != node);
        }
        return out.toArray(new NPC[out.size()]);
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
        Tile myLoc = botEnv.players.getMyPlayer().getLocation();
        double closestDist = 256;
        NPC[] allNPCs = getNPCs();
        NPC closestNPC = null;
        try {
            mainLoop:
            for (NPC tempNPC : allNPCs) {
                for (int i : ids) {
                    double tempDist = tempNPC.getLocation().distanceToPrecise(myLoc);
                    if (i == tempNPC.getID() && tempDist <= range && tempDist < closestDist) {
                        closestNPC = tempNPC;
                        closestDist = tempDist;
                        continue mainLoop;
                    }
                }
            }
        } catch (Exception betterSafeThanSorry) {
            return null;
        }
        return closestNPC;
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
        Tile myLoc = botEnv.players.getMyPlayer().getLocation();
        double closestDist = 256;
        NPC[] allNPCs = getNPCs();
        NPC closestNPC = null;
        try {
            mainLoop:
            for (NPC tempNPC : allNPCs) {
                for (String s : names) {
                    double tempDist = tempNPC.getLocation().distanceToPrecise(myLoc);
                    if (tempNPC.getName().equalsIgnoreCase(s) && tempDist <= range && tempDist < closestDist) {
                        closestNPC = tempNPC;
                        closestDist = tempDist;
                        continue mainLoop;
                    }
                }
            }
        } catch (Exception betterSafeThanSorry) {
            return closestNPC;
        }
        return closestNPC;
    }

    public NPC getClosestFree(int range, int[] ids) {
        Tile myLoc = botEnv.players.getMyPlayer().getLocation();
        double closestDist = 256;
        NPC[] allNPCs = getNPCs();
        NPC closestNPC = null;
        try {
            mainLoop:
            for (NPC tempNPC : allNPCs) {
                for (int i : ids) {
                    if (tempNPC.isInCombat())
                        continue;
                    int tempDist = tempNPC.getLocation().distanceTo(myLoc);
                    if (tempNPC.getID() == i && tempDist <= range && tempDist < closestDist) {
                        closestNPC = tempNPC;
                        closestDist = tempDist;
                        continue mainLoop;
                    }
                }
            }
        } catch (Exception betterSafeThanSorry) {
            return closestNPC;
        }
        return closestNPC;
    }

    public NPC getNPCAtIndex(int index) {
        NodeCache nodeCache = getClient().getNPCNodeCache();
        final Node[] nodes = nodeCache.getNodes();
        Node node = nodes[((int) ((long) (nodes.length - 1) & (long) index))];
        if (node == null) {
            return null;
        }
        Node curNode = node;
        do {
            if (curNode.getNodeID() == index) {
                if (curNode instanceof NPCNode) {
                    NPCNode npcNode = (NPCNode) curNode;
                    com.kbotpro.hooks.NPC npc = npcNode.getNPC();
                    if (npc == null) {
                        continue;
                    }
                    return new NPC(botEnv, npc);
                }
            }
            curNode = curNode.getPrevNode();
        } while (curNode != null && curNode != node);
        return null;
    }
}

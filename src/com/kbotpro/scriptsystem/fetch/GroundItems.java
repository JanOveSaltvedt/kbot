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
import com.kbotpro.hooks.*;
import com.kbotpro.hooks.Item;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: 11.des.2009
 * Time: 21:44:40
 * To change this template use File | Settings | File Templates.
 */
public class GroundItems extends ModuleConnector {
    public GroundItems(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Gets the ground item at the given tile coords.
     *
     * @param x
     * @param y
     * @return Returns an empty array if no ground items exist
     */
    public GroundItem[] getItemsAt(int x, int y) {
        int plane = botEnv.client.getCurrentPlane();


        int uid = x | (y << 14) | (plane << 28);

        NodeCache nodeCache = botEnv.client.getGroundObjectCache();
        if (nodeCache == null) {
            return new GroundItem[0];
        }
        Node[] nodes = nodeCache.getNodes();
        if (nodes == null) {
            return new GroundItem[0];
        }
        Node node = nodes[(uid & (nodes.length - 1))];
        if (node == null) {
            return new GroundItem[0];
        }

        Node curNode = node.getPrevNode();
        do {
            if (curNode.getNodeID() == uid) {
                NodeListNode nodeListNode = (NodeListNode) curNode;
                NodeList nodeList = nodeListNode.getNodeList();
                List<GroundItem> items = new ArrayList<GroundItem>();
                Node headNode = nodeList.getHeadNode();
                Node itemNode = headNode;
                do {
                    if (itemNode != null && itemNode instanceof Item) {
                        Item item = (Item) itemNode;
                        GroundObject groundObject = getGroundObjectAt(x, y);
                        if (groundObject == null) {
                            continue;
                        }
                        items.add(new GroundItem(botEnv, item, groundObject));
                    }
                    itemNode = itemNode.getNextNode();
                } while (itemNode != headNode);
                return items.toArray(new GroundItem[items.size()]);
            }
            curNode = curNode.getPrevNode();
        } while (curNode != node);
        /*
        for(Node curNode = node.getPrevNode(); curNode != null && curNode != node; curNode = curNode.getPrevNode()){
            if(curNode.getNodeID() == uid){
                NodeListNode nodeListNode = (NodeListNode) curNode;
                NodeList nodeList = nodeListNode.getNodeList();
                List<GroundItem> items = new ArrayList<GroundItem>();
                Node headNode = nodeList.getHeadNode();
                for(Node itemNode = headNode.getNextNode(); itemNode != null && itemNode != headNode; itemNode = itemNode.getNextNode()){
                    if(itemNode instanceof Item){
                        Item item = (Item) itemNode;
                        GroundObject groundObject = getGroundObjectAt(x, y);
                        if(groundObject == null){
                            continue;
                        }
                        items.add(new GroundItem(botEnv, item, groundObject));
                    }
                }
                return items.toArray(new GroundItem[items.size()]);
            }
        }*/
        return new GroundItem[0];

    }

    /**
     * Gets the ground items at the given tile
     *
     * @param tile
     * @return Returns an empty array if no ground items exist
     */
    public GroundItem[] getItemsAt(Tile tile) {
        return getItemsAt(tile.getX(), tile.getY());
    }

    /**
     * Local coords
     *
     * @param x
     * @param y
     */
    private GroundObject getGroundObjectAt(int x, int y) {
        Client client = getClient();
        int currentPlane = client.getCurrentPlane();
        x -= botEnv.client.getBaseX();
        y -= botEnv.client.getBaseY();
        if (x < 0 || y < 0 || x > 103 || y > 103) {
            return null;
        }
        TileData[][][] datas = client.getTileDataArray();
        if (datas == null) {
            return null;
        }
        TileData tileData = datas[currentPlane][x][y];
        if (tileData == null) {
            return null;
        }
        return tileData.getGroundObject();

    }

    /**
     * Gets the closest ground item in range.
     *
     * @param range
     * @return null if no item was found
     */
    public GroundItem getClosestItemNoID(int range) {
        Tile myPos = botEnv.players.getMyPlayer().getLocation();
        int minX = myPos.getX() - range;
        int minY = myPos.getY() - range;
        int maxX = myPos.getX() + range;
        int maxY = myPos.getY() + range;
        List<GroundItem> itemList = new ArrayList<GroundItem>();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                GroundItem[] items = getItemsAt(x, y);
                for (GroundItem item : items) {
                    if (item != null) {
                        itemList.add(item);
                    }
                }
            }
        }
        if (itemList.isEmpty())
            return null;
        double closest = 9999;
        GroundItem closestItem = null;
        for (GroundItem item : itemList) {
            double distance = myPos.distanceToPrecise(item.getLocation());
            if (distance < closest) {
                closest = distance;
                closestItem = item;
            }
        }
        return closestItem;
    }

    /**
     * Gets the closest item that matches the given IDs
     *
     * @param range
     * @param ids
     * @return
     */
    public GroundItem getClosestItem(int range, int... ids) {
        Tile myPos = botEnv.players.getMyPlayer().getLocation();
        int minX = myPos.getX() - range;
        int minY = myPos.getY() - range;
        int maxX = myPos.getX() + range;
        int maxY = myPos.getY() + range;
        List<GroundItem> itemList = new ArrayList<GroundItem>();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                GroundItem[] items = getItemsAt(x, y);
                for (GroundItem item : items) {
                    if (item != null) {
                        int oID = item.getID();
                        for (int id : ids) {
                            if (id == oID) {
                                itemList.add(item);
                            }
                        }
                    }
                }
            }
        }
        if (itemList.isEmpty())
            return null;
        double closest = 9999;
        GroundItem closestItem = null;
        for (GroundItem item : itemList) {
            double distance = myPos.distanceToPrecise(item.getLocation());
            if (distance < closest) {
                closest = distance;
                closestItem = item;
            }
        }
        return closestItem;
    }

    /**
     * Get all the objects in range matching the given IDs.
     *
     * @param range
     * @param ids
     * @return
     */
    public GroundItem[] getItems(int range, int... ids) {
        List<GroundItem> out = new LinkedList<GroundItem>();
        Tile myPos = botEnv.players.getMyPlayer().getLocation();
        int minX = myPos.getX() - range;
        int minY = myPos.getY() - range;
        int maxX = myPos.getX() + range;
        int maxY = myPos.getY() + range;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                GroundItem[] items = getItemsAt(x, y);
                for (GroundItem o : items) {
                    if (o != null) {
                        int oID = o.getID();
                        for (int id : ids) {
                            if (id == oID) {
                                out.add(o);
                            }
                        }
                    }
                }
            }
        }
        return out.toArray(new GroundItem[out.size()]);
    }

    /**
     * Gets all the ground items within range.
     *
     * @param range
     * @return
     */
    public GroundItem[] getItems(int range) {
        List<GroundItem> out = new LinkedList<GroundItem>();
        Tile myPos = botEnv.players.getMyPlayer().getLocation();
        int minX = myPos.getX() - range;
        int minY = myPos.getY() - range;
        int maxX = myPos.getX() + range;
        int maxY = myPos.getY() + range;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                GroundItem[] groundItems = getItemsAt(x, y);
                for (GroundItem groundItem : groundItems) {
                    if (groundItem != null) {
                        out.add(groundItem);
                    }
                }
            }
        }
        return out.toArray(new GroundItem[out.size()]);
    }


}

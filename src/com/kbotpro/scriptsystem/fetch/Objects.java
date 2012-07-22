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
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;
import com.kbotpro.scriptsystem.wrappers.Player;
import com.kbotpro.scriptsystem.wrappers.Tile;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.hooks.Client;
import com.kbotpro.hooks.TileData;
import com.kbotpro.hooks.GameObjectNode;
import com.kbotpro.hooks.GameObject;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class to handle the fetching of in game Physical Objects like trees.
 */
public class Objects extends ModuleConnector {

    public static int MASK_OBJECT2 =  1;
    public static int MASK_BOUNDARY = 6; // 2+4
    public static int MASK_OBJECT5 = 8;
    public static int MASK_DECORATIONS = 16;
    public static int MASK_INTERACTIVE = 32;

    public Objects(BotEnvironment botEnv) {
        super(botEnv);
    }

    public PhysicalObject[] getObjectsAt(int x, int y, int mask){
        Client client = getClient();
        int currentPlane = client.getCurrentPlane();
        x -= client.getBaseX();
        y -= client.getBaseY();
        if(x < 0 || y < 0 || x > 103 || y > 103){
            return new PhysicalObject[0];
        }
        TileData[][][] datas = client.getTileDataArray();
        if(datas == null){
            return new PhysicalObject[0];   
        }
        TileData tileData = datas[currentPlane][x][y];
        if(tileData == null){
            return new PhysicalObject[0];
        }
        List<PhysicalObject> physicalObjects = new ArrayList<PhysicalObject>();
        if((mask & MASK_INTERACTIVE) != 0){
            GameObjectNode current = tileData.getGameObjectNodeHeader();
            while(current != null){
                GameObject containedGameObject = current.getContainedGameObject();
                if(containedGameObject != null){
                    if(containedGameObject instanceof com.kbotpro.hooks.PhysicalObject){
                        physicalObjects.add(new PhysicalObject(botEnv, (com.kbotpro.hooks.PhysicalObject) containedGameObject, PhysicalObject.Type.INTERACTIVE));
                    }
                }
                current = current.getNextNode();
            }
        }
        if((mask & MASK_DECORATIONS) != 0){
            com.kbotpro.hooks.PhysicalObject decoration = tileData.getDecorationObject();
            if(decoration != null){
                physicalObjects.add(new PhysicalObject(botEnv, decoration, PhysicalObject.Type.DECORATION));
            }
        }
        if((mask & MASK_OBJECT2) != 0){
            com.kbotpro.hooks.PhysicalObject object2 = tileData.getObject2();
            if(object2 != null){
                physicalObjects.add(new PhysicalObject(botEnv, object2, PhysicalObject.Type.UNKNOWN_1));
            }
        }
        if((mask & MASK_BOUNDARY) != 0){
            com.kbotpro.hooks.PhysicalObject bounding1 = tileData.getBoundingObject1();
            if(bounding1 != null){
                physicalObjects.add(new PhysicalObject(botEnv, bounding1, PhysicalObject.Type.BOUNDARY));
            }
            com.kbotpro.hooks.PhysicalObject bounding2 = tileData.getBoundingObject2();
            if(bounding2 != null){
                physicalObjects.add(new PhysicalObject(botEnv, bounding2, PhysicalObject.Type.BOUNDARY));
            }
        }
        if((mask & MASK_OBJECT5) != 0){
            com.kbotpro.hooks.PhysicalObject object5 = tileData.getObject5();
            if(object5 != null){
                physicalObjects.add(new PhysicalObject(botEnv, object5, PhysicalObject.Type.UNKNOWN_2));
            }
        }
        return physicalObjects.toArray(new PhysicalObject[1]);
    }

    public PhysicalObject[] getObjects(int range) {
        List<PhysicalObject> out = new LinkedList<PhysicalObject>();
        Tile myPos = botEnv.players.getMyPlayer().getLocation();
        int minX = myPos.getX() - range;
        int minY = myPos.getY() - range;
        int maxX = myPos.getX() + range;
        int maxY = myPos.getY() + range;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                PhysicalObject[] physicalObjects = getObjectsAt(x,  y);
                for(PhysicalObject o: physicalObjects){
                    if (o != null) {
                        out.add(o);
                    }
                }
            }
        }
        return out.toArray(new PhysicalObject[out.size()]);
    }

    public PhysicalObject[] getObjectsAt(int x, int y) {
        return getObjectsAt(x, y, MASK_BOUNDARY|MASK_INTERACTIVE);
    }


    public PhysicalObject getClosestObjectNoID(int range) {
        Tile myPos = botEnv.players.getMyPlayer().getLocation();
        int minX = myPos.getX() - range;
        int minY = myPos.getY() - range;
        int maxX = myPos.getX() + range;
        int maxY = myPos.getY() + range;
        List<PhysicalObject> objList = new ArrayList<PhysicalObject>();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                PhysicalObject[] physicalObjects = getObjectsAt(x, y);
                for(PhysicalObject o: physicalObjects){
                    if (o != null) {
                        objList.add(o);
                    }
                }
            }
        }
        if (objList.isEmpty())
            return null;
        double closest = 9999;
        PhysicalObject closestObj = null;
        for (PhysicalObject o : objList) {
            double distance = myPos.distanceToPrecise(o.getLocation());
            if (distance < closest) {
                closest = distance;
                closestObj = o;
            }
        }
        return closestObj;
    }

    public PhysicalObject[] getObjects(int range, int... ids) {
        List<PhysicalObject> out = new LinkedList<PhysicalObject>();
        Tile myPos = botEnv.players.getMyPlayer().getLocation();
        int minX = myPos.getX() - range;
        int minY = myPos.getY() - range;
        int maxX = myPos.getX() + range;
        int maxY = myPos.getY() + range;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                PhysicalObject[] physicalObjects = getObjectsAt(x, y);
                for(PhysicalObject o: physicalObjects){
                    if (o != null) {
                        int oID = o.getID();
                        for(int id: ids){
                            if(id == oID){
                                out.add(o);
                            }
                        }
                    }
                }
            }
        }
        return out.toArray(new PhysicalObject[out.size()]);
    }

    public PhysicalObject getClosestObject(int range, int... ids) {
        Tile myPos = botEnv.players.getMyPlayer().getLocation();
        int minX = myPos.getX() - range;
        int minY = myPos.getY() - range;
        int maxX = myPos.getX() + range;
        int maxY = myPos.getY() + range;
        List<PhysicalObject> objList = new ArrayList<PhysicalObject>();
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                PhysicalObject[] physicalObjects = getObjectsAt(x, y);
                for(PhysicalObject o: physicalObjects){
                    if (o != null) {
                        int oID = o.getID();
                        for(int id: ids){
                            if(id == oID){
                                objList.add(o);
                            }
                        }
                    }
                }
            }
        }
        if (objList.isEmpty())
            return null;
        double closest = 9999;
        PhysicalObject closestObj = null;
        for (PhysicalObject o : objList) {
            double distance = myPos.distanceToPrecise(o.getLocation());
            if (distance < closest) {
                closest = distance;
                closestObj = o;
            }
        }
        return closestObj;
    }

    /**
     * Gets all the objects at the given tile that matches the mask give.
     * @param tile
     * @param mask_interactive
     * @return
     */
    public PhysicalObject[] getObjectsAtWithMask(Tile tile, int mask_interactive) {
        return getObjectsAt(tile.getX(), tile.getY(), mask_interactive);
    }

    /**
     * Gets all the objects at the given tile.
     * @param tile
     * @return
     */
    public PhysicalObject[] getObjectsAt(Tile tile) {
        return getObjectsAt(tile.getX(), tile.getY());
    }

    /**
     * Gets all the objects at the given tile that matches the mask give.
     * @param tile
     * @param mask_interactive
     * @param IDs The object IDs to accept
     * @return
     */
    public PhysicalObject[] getObjectsAt(Tile tile, int mask_interactive, int... IDs) {
        return matchID(getObjectsAtWithMask(tile, mask_interactive), IDs);
    }

    /**
     * Gets all the objects at the given tile that matches the mask give.
     * @param tile
     * @param IDs The object IDs to accept
     * @return
     */
    public PhysicalObject[] getObjectsAt(Tile tile, int... IDs) {
        return matchID(getObjectsAt(tile), IDs);
    }

    /**
     * Takes an array of objects and matches the IDs and returns those hwo matches.
     * @param physicalObjects
     * @param IDs
     * @return
     */
    public PhysicalObject[] matchID(PhysicalObject[] physicalObjects, int... IDs){
        List<PhysicalObject> out = new ArrayList<PhysicalObject>();
        for(PhysicalObject physicalObject: physicalObjects){
            if(physicalObject == null){
                continue;
            }
            int pID = physicalObject.getID();
            for(int id: IDs){
                if(id == pID){
                    out.add(physicalObject);
                    break;
                }
            }
        }
        return out.toArray(new PhysicalObject[out.size()]);
    }

    /**
     * Finds the object thats closest to the player.
     * @param objects
     * @return
     */
    public PhysicalObject getClosest(PhysicalObject[] objects){
        PhysicalObject closestObj = null;
        Tile loc = botEnv.players.getMyPlayer().getLocation();
        double cloestDist = 9999;
        for(PhysicalObject object: objects){
            if(object == null){
                continue;
            }
            if(loc.distanceToPrecise(object) < cloestDist){
                closestObj = object;
            }
        }
        return closestObj;
    }

    /**
	 *
	 * @param tiles
	 *            The Zone in which you want to get the Closest PhysicalObject.
	 * @param ids
	 *            IDs of the PhysicalObjects you want to look for.
	 * @return closest PhysicalObject in zone defined by Tile[]
	 * @author SpeedWing
	 */
	public PhysicalObject getClosestObjectInZone(Tile[] tiles, int... ids) {
		try {
			PhysicalObject nearest = null;
            final Player player = botEnv.players.getMyPlayer();
            final Polygon zone = new Polygon();

			for (Tile t : tiles)
				zone.addPoint(t.getX(), t.getY());

			for (int x = zone.getBounds().x; x < zone.getBounds().x
					+ zone.getBounds().width; x++)
				for (int y = zone.getBounds().y; y < zone.getBounds().y
						+ zone.getBounds().height; y++)
					if (zone.contains(new Point(x, y)))
						if (getObjectsAt(x, y) != null) {
							PhysicalObject[] objects = getObjectsAt(x, y);
							for (int i = 0; i < objects.length; i++)
								if (objects[i] != null)
									for (int curID : ids)
										if (objects[i].getID() == curID)
											if (nearest == null
													|| player.distanceTo(objects[i]) < player.distanceTo(nearest))
												nearest = objects[i];
						}
			return nearest;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

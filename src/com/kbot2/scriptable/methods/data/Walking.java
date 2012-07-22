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
import com.kbot2.scriptable.methods.Calculations;
import com.kbot2.scriptable.methods.Methods;
import com.kbot2.scriptable.methods.wrappers.Player;
import com.kbot2.scriptable.methods.wrappers.Tile;
import org.apache.commons.lang.ArrayUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import static com.kbot2.scriptable.methods.Calculations.random;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 23.apr.2009
 * Time: 16:38:18
 * Seperates walking methods from the methods.java
 */
public class Walking extends Data{

	public final int BUTTON_RUN_INTERFACEGROUP = 750;
	public final int BUTTON_RUN_INTERFACE = 1;
	public ArrayList<WalkerNode> nodes = new ArrayList<WalkerNode>();
	private Tile current = new Tile(0, 0);
	public boolean loadNodes, loadLisks;

	/*
	 * Creates a random path to your destination.
	 *
	 * @return Returns true if you reach your destination. Does not completely work.
	 * @author Apples
	 */
	public boolean WalkPath(int x, int y) {
		int i = 0;
		while(getMethods().getMyPlayer().getLocation().distanceTo(new Tile(x, y)) > 3 && i < 10)
			try {
				walkPath(fixPath(randomizePathAdd(generateProperPath(x + random(-1, 1), y + random(-1, 1)),3, 3)));
				i++;
			} catch(Exception e) {
				return false;
			}
		return (getMethods().getMyPlayer().getLocation().distanceTo(new Tile(x, y)) <= 3);
	}

	public void mouseSpeed(int random, int random2) {
		getMethods().setMouseSpeed((random(random, random2) / 1000000));
	}

	private void load() {
		/*
		 * Nodes
		 */
		if (!loadNodes)
			try {
				final BufferedReader BR = new BufferedReader(new FileReader("settings/data.dat"));
				String s = null;
				String[] l;
				while ((s = BR.readLine()) != null) {
					l = s.split("\t");
					nodes.add(new WalkerNode(Integer.parseInt(l[0]), Integer.parseInt(l[1])));
				}
				loadNodes = true;
				BR.close();
			} catch (Exception e) {
				getMethods().log("Error loading Nodes");
				return;
			}
		/*
		 * Links
		 */
		if (!loadLisks)
			try {
				BufferedReader BR = new BufferedReader(new FileReader("settings/data2.dat"));
				String s = null;
				String[] l;
				while ((s = BR.readLine()) != null) {
					l = s.split("\t");
					WalkerNode w = nodes.get(Integer.parseInt(l[0]));
					w.neighbours.add(nodes.get(Integer.parseInt(l[1])));
					w = nodes.get(Integer.parseInt(l[1]));
					w.neighbours.add(nodes.get(Integer.parseInt(l[0])));
				}
				loadLisks = true;
				BR.close();
			} catch (Exception e) {
				getMethods().log("Error loading Links");
			}
	}

	private int distance(WalkerNode w, int X, int Y) {
		int x = w.x - X;
		int y = w.y - Y;
		return (int) Math.sqrt(x * x + y * y);
	}

	private WalkerNode[] findPath(WalkerNode startNode, WalkerNode endNode) {
		try {
			if (!loadNodes || !loadLisks)
				load();
			ArrayList<WalkerNode> l = new ArrayList<WalkerNode>();
			for (int i = 0; i < nodes.size(); i++) {
				WalkerNode thisNode = nodes.get(i);
				thisNode.distance = 999999;
				thisNode.previous = null;
				l.add(thisNode);
			}
			startNode.distance = 0;
			while (l.isEmpty() == false) {
				WalkerNode nearestNode = l.get(0);
				for (int i = 0; i < l.size(); i++) {
					WalkerNode thisNode = l.get(i);
					if (thisNode.distance <= nearestNode.distance)
						nearestNode = thisNode;

				}
				l.remove(l.indexOf(nearestNode));
				if (nearestNode == endNode)
					l.clear();
				else
					for (int i = 0; i < nearestNode.neighbours.size(); i++) {
						WalkerNode neighbourNode = nearestNode.neighbours.get(i);
						int alt = nearestNode.distance + nearestNode.distance(neighbourNode);
						if (alt < neighbourNode.distance) {
							neighbourNode.distance = alt;
							neighbourNode.previous = nearestNode;
						}
					}
			}
			ArrayList<WalkerNode> nodePath = new ArrayList<WalkerNode>();
			nodePath.add(endNode);
			WalkerNode previousNode = endNode.previous;
			while (previousNode != null) {
				nodePath.add(previousNode);
				previousNode = previousNode.previous;
			}
			if (nodePath.size() == 1)
				return null;
			WalkerNode[] nodeArray = new WalkerNode[nodePath.size()];
			for (int i = nodePath.size() - 1; i >= 0; i--)
				nodeArray[nodePath.size() - i - 1] = nodePath.get(i);
			return nodeArray;
		} catch (Exception e) {
		}
		return null;
	}

	public Tile[] generateProperPath(int targetX, int targetY) {
		if (!loadNodes || !loadLisks)
			load();
		WalkerNode startNode = null, endNode = null;
		int shortestDistance = 0;
		for (int i = 0; i < nodes.size(); i++)
			if (startNode == null || distance(nodes.get(i), getMethods().getMyPlayer().getLocation().getX(), getMethods().getMyPlayer().getLocation().getY()) < shortestDistance) {
				startNode = nodes.get(i);
				shortestDistance = distance(nodes.get(i), getMethods().getMyPlayer().getLocation().getX(), getMethods().getMyPlayer().getLocation().getY());
			}
		shortestDistance = 0;
		for (int i = 0; i < nodes.size(); i++)
			if (endNode == null || distance(nodes.get(i), targetX, targetY) < shortestDistance) {
				endNode = nodes.get(i);
				shortestDistance = distance(nodes.get(i), targetX, targetY);
			}
		WalkerNode[] nodePath = findPath(startNode, endNode);
		if (nodePath == null) {
			Tile[] start = { new Tile(getMethods().getMyPlayer().getLocation().getX(), getMethods().getMyPlayer().getLocation().getY()), new Tile(targetX, targetY) };
			return start;
		} else {
			Tile[] tilePath = new Tile[nodePath.length];
			int i = 0;
			tilePath[i] = new Tile(getMethods().getMyPlayer().getLocation().getX(), getMethods().getMyPlayer().getLocation().getY());
			i++;
			for (; i < tilePath.length - 1; i++)
				tilePath[i] = new Tile(nodePath[i - 1].x, nodePath[i - 1].y);
			tilePath[i] = new Tile(targetX, targetY);
			return tilePath;
		}
	}

	public Tile[] fixPath(Tile[] t) {
		ArrayList<Tile> newPath = new ArrayList<Tile>();
		for(int i = 0; i < t.length - 1; i++) {
			Tile[] T = fixPath(t[i].getX(), t[i].getY(), t[i + 1].getX(), t[i + 1].getY());
			for (int l = 0; l < T.length; l++)
				newPath.add(T[l]);
		}
		return newPath.toArray(new Tile[newPath.size()]);
	}

	public Tile[] fixPath(int startX, int startY, int destinationX, int destinationY) {
		double x, y;
		ArrayList<Tile> list = new ArrayList<Tile>();
		list.add(new Tile(startX, startY));
		while(Math.hypot(destinationY - startY, destinationX - startX) > 8) {
			x = destinationX - startX;
			y = destinationY - startY;
			int random = random(14, 17);
			while(Math.hypot(x, y) > random) {
				x *= .95;
				y *= .95;
			}
			startX += (int) x;
			startY += (int) y;
			list.add(new Tile(startX, startY));
		}
		list.add(new Tile(destinationX, destinationY));
		return list.toArray(new Tile[list.size()]);
	}

	public Walking(BotEnvironment botEnv) {
		super(botEnv);
	}

	/**
	 * Walks to a tile if it is on screen.
	 * NOTE: The screen might move while the mouse is moving and it wont hit the exact target.
	 * @param tile
	 * @return
	 */
	public boolean walkToMM(Tile tile){
		Point screenPos = getCalculations().tileToMinimap(tile);
		if(screenPos.x == -1 || screenPos.y == -1)
			return false;
		else {
			getMethods().clickMouse(screenPos, true);
			return true;
		}
	}

	public boolean walkPath(Tile[] path){
		if(path == null || path.length == 0)
			throw new IllegalArgumentException("path can not be null or empty");
		final Methods methods = getMethods();
		final Player myPlayer = methods.getMyPlayer();
		Tile curPos = myPlayer.getLocation();
		Tile lastPos = curPos;
        int pos = 0;
		for(pos = 0; pos < path.length; pos++){
			Tile tile = path[pos];
			if(Calculations.onScreen(getCalculations().tileToMinimap(tile)))
				break;
		}
		int tries = 0;
		while(pos < path.length){
			mouseSpeed(1824755, 2249142);
			if(tries > 35)
				return false;
			tries++;
			Tile tile = path[pos];
			while(Calculations.onScreen(getCalculations().tileToMinimap(tile))){// Get the furthest tile.
				pos++;
				if(pos >= path.length)
					break;
				tile = path[pos];
			}
			//methods.log("Furthest "+pos);
			pos--;
			tile = path[pos];
			curPos = myPlayer.getLocation();
			if(!Calculations.onScreen(getCalculations().tileToMinimap(tile))){
				//methods.log("Return false");
				return false;
			}
			if((!tile.equals(lastPos) && (curPos.distanceTo(tile) < random(5, 14))) || !myPlayer.isMoving()) {
				//methods.log("Walking to: "+tile.toString());
				walkToMM(tile);
				lastPos = tile;
			} else if(myPlayer.isMoving()){
				if(getEnergy() > random(25, 57) && random(1, 3)== 2) {
					setRunning(true);
					methods.sleep(300, 350);
				}
				//methods.log("Waiting");
				tries = 0;
				methods.sleep(100, 150);
			} else
				methods.sleep(100, 150);

			if(pos == path.length -1){
				//methods.log("Last tile in path");
				while(myPlayer.isMoving()){
					//methods.log("Waiting");
					methods.sleep(10, 50);
				}
				curPos = myPlayer.getLocation();
				if(curPos.distanceTo(path[pos]) < 3)
					//methods.log("Returning true");
					return true;
			}
		}
		//methods.log("Return false");
		return false;
	}

	/**
	 * Randomizes a path.
	 * @param path This gets modified.
	 * @param randomX maximum modification on the x-axis.
	 * @param randomY maximum modification on the y-axis.
	 */
	public static Tile[] randomizePath(Tile[] path, int randomX, int randomY){
		Tile[] out = new Tile[path.length];
		for(int i = 0; i < path.length; i++)
			out[i] = new Tile(path[i].getX()+(int)random((double) -randomX, (double)randomX), path[i].getY()+(int)random((double) -randomY, (double)randomY));
		return out;
	}

	/**
	 * Randomizes a path.
	 * @param path This gets modified.
	 * @param randomX maximum modification on the x-axis.
	 * @param randomY maximum modification on the y-axis.
	 */
	public static Tile[] randomizePathAdd(Tile[] path, int randomX, int randomY){
		Tile[] out = new Tile[path.length];
		for(int i = 0; i < path.length; i++)
			out[i] = new Tile(path[i].getX() + (int)random(1, randomX), path[i].getY() + (int)random(1, randomY));
		return out;
	}

	/**
	 * Sets running mode on and off
	 * @param run
	 * @return
	 */
	public boolean setRunning(boolean run) {
		if (botEnv.settings.getSetting(173) != (run ? 1 : 0)) {
			botEnv.interfaces.getInterface(750, 1).doClick();
			getMethods().sleep(100, 300);
		}
		return botEnv.settings.getSetting(Settings.SETTING_BANK_TOGGLE_WITHDRAW_MODE) == (run ? 1 : 0);
	}

	/**
	 * Should get the run energy
	 * @return
	 * @author ToshiXZ
	 */
	public int getEnergy() {
		try {
			return Integer.parseInt(botEnv.interfaces.getInterface(750, 5).getText());
		} catch (final NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Reverses a Tile array.
	 *
	 * @param in The Tile Array you want to reverse.
	 * @return Returns a Tile array.
	 * @author Alowaniak
	 */
	public static Tile[] reversePath(Tile[] in) {
		Tile[] out = (Tile[]) ArrayUtils.clone(in);
		ArrayUtils.reverse(out);
		return out;
	}

	public boolean atTile(Tile tile, String actionContains){
		Point p = getCalculations().tileToScreen(tile);
		if(!Calculations.onScreen(p))
			botEnv.camera.setAngleTo(tile);
		p = getCalculations().tileToScreen(tile);
		if(!Calculations.onScreen(p))
			return false;
		botEnv.methods.moveMouse(p, 2, 2);
		getMethods().sleep(30, 100);
		return botEnv.methods.atMenu(actionContains);
	}

	public boolean atTile(Tile tile, int height, String actionContains){
		Point p = getCalculations().tileToScreen(tile.getX(), tile.getY(), 0.5, 0.5, height);
		if(!Calculations.onScreen(p))
			botEnv.camera.setAngleTo(tile);
		p = getCalculations().tileToScreen(tile.getX(), tile.getY(), 0.5, 0.5, height);
		if(!Calculations.onScreen(p))
			return false;
		botEnv.methods.moveMouse(p, 2, 2);
		getMethods().sleep(30, 100);
		return botEnv.methods.atMenu(actionContains);
	}

	/**
	 * More precise click on tile.
	 * @param tile
	 * @param xOff from 0 - 1. O is west part of tile. 1 is east
	 * @param yOff from 0 -1. 0 is south part of tile. 1 is north
	 * @param height the height to click in. 512 is the same as a tile is in length
	 * @param actionContains the action to do.
	 * @return if it succeeded
	 */
	public boolean atTile(Tile tile, double xOff, double yOff, int height, String actionContains){
		Point p = getCalculations().tileToScreen(tile.getX(), tile.getY(), xOff, yOff, height);
		if(!Calculations.onScreen(p))
			botEnv.camera.setAngleTo(tile);
		p = getCalculations().tileToScreen(tile.getX(), tile.getY(), xOff, yOff, height);
		if(!Calculations.onScreen(p))
			return false;
		botEnv.methods.moveMouse(p, 2, 2);
		getMethods().sleep(30, 100);
		return botEnv.methods.atMenu(actionContains);
	}

	class WalkerNode {
		ArrayList<WalkerNode> neighbours = new ArrayList<WalkerNode>();
		WalkerNode previous;
		int x, y, distance;

		public WalkerNode(int x, int y) {
			this.x = x;
			this.y = y;
			previous = null;
		}

		public int distance(WalkerNode neighbour) {
			int X = x - neighbour.x;
			int Y = y - neighbour.y;
			return (int) Math.sqrt(X * X + Y * Y);
		}
	}
}
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

package com.kbotpro.randoms;

import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.NPC;
import com.kbotpro.scriptsystem.wrappers.Tile;

/**
 * Created by IntelliJ IDEA.
 * User: Scott
 * Date: Jan 18, 2010
 * Time: 10:53:54 AM
 * Taken from IBot
 */
public class TreeRandom extends Random {
	private int[] treeIds = new int[] { 152 };

	@Override
	public String getName() {
		return "Tree Random";
	}

	@Override
	public boolean activate() {
		if (game.isLoggedIn())
			return isInteractingWithTree();
		return false;
	}

	@Override
	public void onStart() {
		NPC npc = (NPC) getMyPlayer().getInteracting();
		if (getMyPlayer().getAnimation() == 424 || getMyPlayer().getInteracting() != null) {
			KTimer timer = new KTimer(1000);
			while (npc == null && !timer.isDone()) {
				npc = npcs.getClosest(10, treeIds);
				sleep(100);
			}
			if (npc.getLocation().distanceTo(getLocation()) > 2) {
				Tile runToTile = null;
				if (getLocation().getX() == (npc.getLocation().getX() - 1)) {
					runToTile = new Tile(getLocation().getX() - 2, getLocation().getY() + random(-5, 5));
				} else if (getLocation().getX() == (npc.getLocation().getX() + 1)) {
					runToTile = new Tile(getLocation().getX() + 2, getLocation().getY() + random(-5, 5));
				} else if (getLocation().getY() == (npc.getLocation().getY() + 1)) {
					runToTile = new Tile(getLocation().getX() + random(-5, 5), getLocation().getY() - 2);
				} else if (getLocation().getY() == (npc.getLocation().getY() - 1)) {
					runToTile = new Tile(getLocation().getX() + random(-5, 5), getLocation().getY() + 2);
				}
				if (runToTile != null) {
					walking.walkToMM(runToTile);
				}
			} else {
				log("We already got far away from tree.");
			}
		} else {
			log("Too slow.");
		}
	}

	private boolean isInteractingWithTree() {
		return getMyPlayer().getAnimation() == 424 && npcs.getClosest(10, treeIds) != null;
	}

}

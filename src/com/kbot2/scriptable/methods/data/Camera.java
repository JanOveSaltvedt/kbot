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
import com.kbot2.scriptable.methods.wrappers.Player;
import com.kbot2.scriptable.methods.wrappers.Tile;

import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 03.mai.2009
 * Time: 17:51:08
 */
public class Camera extends Data{
    public Camera(BotEnvironment botEnv) {
        super(botEnv);
    }

    public void setCompass(char direction) {
        direction = Character.toLowerCase(direction);
		switch (direction) {
		case 'n':
			setAngle(359);
			break;
		case 'e':
			setAngle(89);
			break;
		case 's':
			setAngle(179);
			break;
		case 'w':
			setAngle(269);
			break;
		}
	}

    /**
     * Sets the altitude to the bottom or top.
     * @param maxAltitude
     */
    public void setAltitude(boolean maxAltitude) {
		final char key = (char) (maxAltitude ? KeyEvent.VK_UP
				: KeyEvent.VK_DOWN);
		botEnv.keyboard.pressKey(key);
		getMethods().sleep(1000, 1500);
		botEnv.keyboard.releaseKey(key);
	}

    /**
     * Set the camera angle
     * @param degrees
     */
	public void setAngle(int degrees) {
		char left = KeyEvent.VK_LEFT;
		char right = KeyEvent.VK_RIGHT;
		char whichDir = left;
		int start = getAngle();

        start = start < 180 ? start+360 : start;
        degrees = degrees < 180 ? degrees+360 : degrees;

		if (degrees > start) {
			if (degrees - 180 < start) {
				whichDir = right;
			}
		} else if (start > degrees) {
			if (start - 180 >= degrees) {
				whichDir = right;
			}
		}
		degrees %= 360;

		botEnv.keyboard.pressKey(whichDir);
		int timeWaited = 0;
		int turnTime = 0;
		while ((getAngle() > degrees + 5 || getAngle() < degrees - 5) && turnTime < 6000) {
			getMethods().sleep(10);
			turnTime += 10;
			timeWaited += 10;
			if (timeWaited > 500) {
				int time = timeWaited - 500;
				if (time == 0) {
					botEnv.keyboard.pressKey(whichDir);
				} else if (time % 40 == 0) {
					botEnv.keyboard.pressKey(whichDir);
				}
			}
		}
		botEnv.keyboard.releaseKey(whichDir);
	}

    /***
     * Gets the camera angle
     * @return
     */
    public int getAngle(){
        double mapAngle = getClient().getCompassAngle();
		mapAngle /= 16384D;
		mapAngle *= 360D;
		return (int) mapAngle;
    }

    /**
     * Calculates the angle to a tile
     * @param tile
     * @return
     * @author Mike_
     */
    public int getAngleTo(Tile tile) {
        Player myPlayer = botEnv.players.getMyPlayer();
        int x1 = myPlayer.getLocation().getX();
		int y1 = myPlayer.getLocation().getY();
		int x = x1 - tile.getX();
		int y = y1 - tile.getY();
		double angle = Math.toDegrees(Math.atan2(x , y));
		if(x == 0 && y > 0)
			angle = 180;
		if(x < 0 && y == 0)
			angle = 90;
		if(x == 0 && y < 0)
			angle = 0;
		if(x < 0 && y == 0)
			angle = 270;
		if(x < 0 && y > 0)
			angle+=270;
		if(x > 0 && y > 0)
			angle+=90;
		if(x < 0 && y < 0)
			angle=Math.abs(angle)-180;
		if(x > 0 && y < 0)
			angle=Math.abs(angle)+270;
		if(angle<0)
			angle=360+angle;
		if(angle>=360)
			angle-=360;
		return (int)angle;
	}

    /**
     * Moves the camera towards the specified tile.
     * @param tile
     */
    public void setAngleTo(Tile tile){
        setAngle(getAngleTo(tile));
    }
}

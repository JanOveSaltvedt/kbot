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

import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.Tile;
import com.kbotpro.scriptsystem.input.jobs.HoldKeyJob;
import com.kbotpro.scriptsystem.input.callbacks.HoldKeyCallback;
import com.kbotpro.bot.BotEnvironment;

import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 21, 2009
 * Time: 6:43:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Camera extends ModuleConnector {
    public Camera(BotEnvironment botEnv) {
        super(botEnv);
    }

    /***
     * Gets the camera angle
     * @return
     */
    public double getAngle(){
        double mapAngle = getClient().getCompassAngle();
		mapAngle /= 16384D;
		mapAngle *= 360D;
		return mapAngle;
    }

    /**
     * Calculates the angle to a tile
     * @param tile
     * @return
     * @author Mike_
     */
    public int getAngleTo(Tile tile) {
        /* Tile myLoc = botEnv.players.getMyPlayer().getLocation();
     int x = myLoc.getX() - tile.getX();
     int y = myLoc.getY() - tile.getY();
     double angle = Math.toDegrees(Math.atan2(y , x));
     if (x == 0 && y > 0) {
         angle = 180;
     }
     if (x < 0 && y == 0) {
         angle = 90;
     }
     if (x == 0 && y < 0) {
         angle = 0;
     }
     if (x < 0 && y == 0) {
         angle = 270;
     }
     if (x < 0 && y > 0) {
         angle += 270;
     }
     if (x > 0 && y > 0) {
         angle += 90;
     }
     if (x < 0 && y < 0) {
         angle = Math.abs(angle) - 180;
     }
     if (x > 0 && y < 0) {
         angle = Math.abs(angle) + 270;
     }
     if (angle < 0) {
         angle = 360 + angle;
     }
     if (angle >= 360) {
         angle -= 360;
     }
     return (int) angle; */
        double dx = tile.getX() - botEnv.players.getMyPlayer().getLocation().getX();
        double dy = tile.getY() - botEnv.players.getMyPlayer().getLocation().getY();
        double angle = 0.0d;

        if (dx == 0.0) {
            if(dy == 0.0)     angle = 0.0;
            else if(dy > 0.0) angle = Math.PI / 2.0;
            else              angle = (Math.PI * 3.0) / 2.0;
        }
        else if(dy == 0.0) {
            if(dx > 0.0)      angle = 0.0;
            else              angle = Math.PI;
        }
        else {
            if(dx < 0.0)      angle = Math.atan(dy/dx) + Math.PI;
            else if(dy < 0.0) angle = Math.atan(dy/dx) + (2*Math.PI);
            else              angle = Math.atan(dy/dx);
        }
        int i= (int)((angle * 180) / Math.PI)-90;
        if (angle < 0) {
            angle += 360;
        }

        return i;

    }

    /**
     * Set the camera angle
     * @param degrees
     */
	public void setAngle(int degrees) {
		char left = KeyEvent.VK_LEFT;
		char right = KeyEvent.VK_RIGHT;
		char whichDir = left;
		double start = getAngle();

        start = start < 180 ? start + 360 : start;
        degrees = degrees < 180 ? degrees + 360 : degrees;

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

        final double start2 = getAngle();
        final double finalDegrees = degrees;
        final KTimer timeOut = new KTimer(4000);
        HoldKeyJob holdKeyJob = botEnv.keyboard.createHoldKeyJob(whichDir, new HoldKeyCallback() {
            public void update(HoldKeyJob holdKeyJob) {
                if(getAngle() > finalDegrees - 5D && getAngle() < finalDegrees + 5D || timeOut.isDone()){
                    holdKeyJob.stopHolding();
                } else if (timeOut.getTimeElapsed() > 500 && getAngle() == start2) {
                    //Failed to move the camera.
                    holdKeyJob.stopHolding();
                }
            }

            public void onFinished(HoldKeyJob holdKeyJob) {
            }
        });
        holdKeyJob.start();
        holdKeyJob.join();
	}

    /**
     * Sets the compass to the given direction
     *
     * @param direction
     * 'n' = north
     * 'e' = east
     * 'w' = west
     * 's' = south
     *
     *  */
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
     * Moves the camera towards the specified tile.
     * @param tile
     */
    public void setAngleTo(Tile tile){
        setAngle(getAngleTo(tile));
    }

    /**
     * Sets the altitude to the bottom or top.
     * @param maxAltitude
     */
    public void setAltitude(boolean maxAltitude) {
		final char key = (char) (maxAltitude ? KeyEvent.VK_UP : KeyEvent.VK_DOWN);
        final KTimer holdTime = new KTimer(random(1000,1500));
        final int startZ = botEnv.client.getCameraZ();
        HoldKeyJob holdKeyJob = botEnv.keyboard.createHoldKeyJob(key, new HoldKeyCallback() {
            long stopTime = -1;

            public void update(HoldKeyJob holdKeyJob) {
                if(holdTime.isDone() || holdTime.getTimeElapsed() > 150 && botEnv.client.getCameraZ() == startZ){
                    holdKeyJob.stopHolding();
                }
            }

            public void onFinished(HoldKeyJob holdKeyJob) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        holdKeyJob.start();
        holdKeyJob.join();
	}

    /**
     * Sets the altitude to the bottom or top.
     * @param moveUp Determines if the camera should be moved up or down
     * @param holdTime The amount of time you should hold the key
     * @author Apples
     */
    public void setAltitude(boolean moveUp, final long holdTime) {
		final char key = (char) (moveUp ? KeyEvent.VK_UP : KeyEvent.VK_DOWN);

        HoldKeyJob holdKeyJob = botEnv.keyboard.createHoldKeyJob(key, new HoldKeyCallback() {
            long stopTime = -1;

            public void update(HoldKeyJob holdKeyJob) {
                if(stopTime == -1)
                    stopTime = System.currentTimeMillis() + holdTime;
                if(System.currentTimeMillis() > stopTime)
                    holdKeyJob.stopHolding();
            }

            public void onFinished(HoldKeyJob holdKeyJob) {
            }
        });
        holdKeyJob.start();
        holdKeyJob.join();
	}
}

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

package com.kbot2.scriptable.methods.wrappers;

import com.kbot2.bot.BotEnvironment;
import org.apache.log4j.Logger;

import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 3:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Character extends Wrapper {
    private com.kbotpro.scriptsystem.wrappers.Character character;

    public Character(BotEnvironment botEnv, com.kbotpro.scriptsystem.wrappers.Character character) {
        super(botEnv);
        this.character = character;
    }

    /**
     * Gets the characters position
     *
     * @return returns a tile specifying the characters position.
     */
    public Tile getLocation() {
        return new Tile(character.getLocation());
    }

    /**
     * Gets the height of tje character
     * @return int: Negative value
     */
    public int getHeight(){
        return character.getHeight();
    }

    /**
     * Get message.
     *
     * @return
     */
    public String getMessage() {
        String message = character.getMessage();
        return message == null? "":message;
    }

    /**
     * Gets the animation.
     *
     * @return -1 if non.
     */
    public int getAnimation() {
        return character.getAnimation();
    }

    public Point getScreenPos() {
        return character.getScreenPos();
    }

    /**
     * Should click the character
     * @param actionContains
     * @return
     */
    public boolean doAction(String actionContains){
        try{
			Point screenLoc = getScreenPos();
            if(!botEnv.calculations.onScreen(screenLoc)){
                botEnv.camera.setAngleTo(getLocation());
            }
			for (int i = 0; i < 20; i++) {
				screenLoc = getScreenPos();
				if (!botEnv.calculations.onScreen(screenLoc)) {
					return false;
				}
				if (botEnv.methods.getMousePos().equals(screenLoc)) break;
				botEnv.methods.moveMouse(screenLoc);
			}
			screenLoc = getScreenPos();
			if (!botEnv.methods.getMousePos().equals(screenLoc)) return false;
			String[] items = botEnv.menu.getMenuItems();
			if (items.length <= 1) return false;
			if (items[0].toLowerCase().contains(actionContains.toLowerCase())) {
				botEnv.methods.clickMouse(screenLoc, true);
				return true;
			} else {
				botEnv.methods.clickMouse(screenLoc, false);
				return botEnv.methods.atMenu(actionContains);
			}
		} catch (Exception e) {
			Logger.getRootLogger().error("Exception: ", e);
			return false;
		}
    }

    /**
     * Gets the motion of the character.
     * @return
     */
    public int getMotion(){
        return character.getMotion();
    }

    /**
     * Checks if the character is moving.
     * @return
     */
    public boolean isMoving(){
        return getMotion() != 0;
    }

    /**
     * Gets the character the character is interacting with.
     * @return may return null
     */
    public Character getInteracting() {
        com.kbotpro.scriptsystem.wrappers.Character interacting = character.getInteracting();
        if(interacting == null){
            return null;
        }
        if(interacting instanceof com.kbotpro.scriptsystem.wrappers.Player){
            return new Player(botEnv, (com.kbotpro.scriptsystem.wrappers.Player)interacting);
        }
        else if(interacting instanceof com.kbotpro.scriptsystem.wrappers.NPC){
            return new NPC(botEnv, (com.kbotpro.scriptsystem.wrappers.NPC)interacting);
        }
        return null;
    }

    /**
     * Checks if the character is in combat.
     * @return
     */
    public boolean isInCombat() {
		return character.isInCombat();
	}

    public Tile[] getWalkingQueue(){
        com.kbotpro.scriptsystem.wrappers.Tile[] walkingArray = character.getWalkingArray();
        Tile[] out = new Tile[walkingArray.length];
        for(int i = 0; i < walkingArray.length; i++){
            out[i] = new Tile(walkingArray[i]);
        }

        return out;
    }

    /**
     * Gets the orientation in degrees.
     * @return
     */
    public int getOrientation(){
        return (int) character.getOrientation();
    }

    public String toString(){
        return "Charcter(o="+getOrientation()+",a="+getAnimation()+",m="+getMessage()+")";
    }

}

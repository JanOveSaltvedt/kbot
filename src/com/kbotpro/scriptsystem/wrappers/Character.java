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
import com.kbotpro.hooks.*;
import com.kbotpro.scriptsystem.interfaces.Targetable;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.scriptsystem.input.jobs.MouseHoverJob;
import com.kbotpro.scriptsystem.input.jobs.MouseJob;
import com.kbotpro.scriptsystem.input.callbacks.MouseMoveListener;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.various.Point3D;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Wrapper for in game character like Players and NPCs
 */
public abstract class Character extends GameObject implements Targetable {
    private com.kbotpro.hooks.Character rChar;

    protected Character(BotEnvironment botEnv, com.kbotpro.hooks.Character rChar) {
        super(botEnv, rChar);
        this.rChar = rChar;
    }

    /**
     * Be aware that this does not contain accurate data.
     * The data does not get deleted after walking is done, but overwritten when you walk again.
     *
     * @return Returns an array of the walking queue of the character.
     */
    public Tile[] getWalkingArray() {
        int[] xPoints = rChar.getWalkingX();
        int[] yPoints = rChar.getWalkingY();
        Client client = getClient();
        int baseX = client.getBaseX();
        int baseY = getClient().getBaseY();
        int maxIndex = getMotion();

        Tile[] out = new Tile[maxIndex];
        for (int i = 0; i < maxIndex; i++) {
            out[i] = new Tile(xPoints[i] + baseX, yPoints[i] + baseY);
        }
        return out;
    }

    public Point getScreenPos() {
        Model model = getModel();
        if(model != null){
            Point3D point3D = model.createSeed();
            return getCalculations().worldToScreen(getRegionalX() + point3D.x, getRegionalY()+point3D.z, -point3D.y);
        }
        int posX = getRegionalX();
        int posY = getRegionalY();
        int height = (getHeight() >> 1); // Divide by two and negate
        return getCalculations().worldToScreen(posX, posY, height);
    }

    public Point getCenter() {
        Model model = getModel();
        if(model != null){
            Point3D point3D = model.getCenter();
            return getCalculations().worldToScreen(getRegionalX() + point3D.x, getRegionalY()+point3D.z, -point3D.y);
        }
        int posX = getRegionalX();
        int posY = getRegionalY();
        int height = (getHeight() >> 1); // Divide by two and negate
        return getCalculations().worldToScreen(posX, posY, height);
    }
    /**
     * Gets the height in RS units of the character
     *
     * @return integer containing height
     */
    public int getHeight() {
        return -rChar.getHeight();
    }

    /**
     * Gets the animation ID of the character
     *
     * @return return an integer containing animation or -1 if no animation.
     */
    public int getAnimation() {
        return rChar.getAnimation();
    }

    /**
     * Gets the motion length in the client.
     * This is 0 if still and also represents how many the length of the walking queue array
     *
     * @return integer; 0 if standing still or otherwise 1-3
     */
    public int getMotion() {
        return rChar.getMotion();
    }

    /**
     * Checks if the character is currently in motion
     *
     * @return boolean true if mooving.
     */
    public boolean isMoving() {
        return getMotion() != 0;
    }

    /**
     * Gets the model of the character
     *
     * @return
     */
    public Model getModel() {
        com.kbotpro.hooks.Model model = rChar.getModel();
        if (model == null) {
            return null;
        }
        return new TransformableGameModel(botEnv, model, this);
    }

    /**
     * Gets the character models. (Normally just one)
     *
     * @return
     * @deprecated DOES NOT WORK as of #574.
     */
    public Model[] getModels() {
        List<Model> out = new ArrayList<Model>();
        com.kbotpro.hooks.Model[] models = rChar.getModels();
        for (int i = 0; i < models.length; i++) {
            if (models[i] == null) {
                continue;
            }
            out.add(new GameModel(botEnv, models[i], this));
        }

        return out.toArray(new Model[out.size()]);
    }

    /**
     * Get the orientation around the Y axis. This is runescapes unit system.
     * 0 is south.
     * 4095 is west
     * 8191 is north
     * 12287 is east
     * 16383 is max and is also south     *
     *
     * @return
     */
    public int getOrientationYAxis() {
        return rChar.getOrientation();
    }

    /**
     * Gets the orientation of the character
     * 0 is south
     * 90 is west
     * 180 is north
     * 270 is east
     *
     * @return
     */
    public double getOrientation() {
        long con = getOrientationYAxis() * 360;
        return (double) con / 0x3FFF;
    }

    /**
     * Get target
     *
     * @return
     */
    public MouseTarget getTarget() {
        if (getModel() != null) {
            return new MouseTarget() {
                public Point get() {
                    return getCenter();
                }

                public boolean isOver(int posX, int posY) {
                    return getModel().isPointOver(posX, posY /*,true*/);
                }
            };
        } else {
            return new MouseTarget() {
                public Point get() {
                    return getCenter();
                }

                public boolean isOver(int posX, int posY) {
                    Point p = getScreenPos();
                    return new Rectangle(p.x - 2, p.y - 2, 4, 4).contains(posX, posY);
                }
            };
        }
    }

    /**
     * Moves the mouse to the object and clicks at the given action
     * NOTE: Do not use this method while you have a mouse job active!
     *
     * @param actionContains A string that the action contains. Case ignored
     * @return Boolean, true if succeeded, false if not.
     */
    public boolean doAction(final String actionContains) {
        if (!onScreen()) {
            return false;
        }
        final boolean[] ret = new boolean[]{false};
        MouseHoverJob mouseHoverJob = botEnv.mouse.createMouseHoverJob(new MouseMoveListener() {
            private int count = 0;

            public void onMouseOverTarget(MouseJob mouseJob) {
                MouseHoverJob mouseHoverJob = (MouseHoverJob) mouseJob;
                count++;
                if (count > random(5, 100)) {
                    mouseHoverJob.stop();
                    ret[0] = botEnv.menu.atMenu(actionContains);
                }
            }

            public void onFinished(MouseJob mouseJob) {
            }
        }, this, new KTimer(5000));
        mouseHoverJob.start();
        mouseHoverJob.join();
        return ret[0];
    }

    public boolean doClick() {
        if (!onScreen()) {
            return false;
        }
        final boolean[] ret = new boolean[]{false};
        MouseHoverJob mouseHoverJob = botEnv.mouse.createMouseHoverJob(new MouseMoveListener() {
            private int count = 0;

            public void onMouseOverTarget(MouseJob mouseJob) {
                MouseHoverJob mouseHoverJob = (MouseHoverJob) mouseJob;
                count++;
                if (count > random(5, 100)) {
                    mouseHoverJob.doMouseClick(true);
                    ret[0] = true;
                    mouseHoverJob.stop();
                }
            }

            public void onFinished(MouseJob mouseJob) {
            }
        }, this, new KTimer(5000));
        mouseHoverJob.start();
        mouseHoverJob.join();
        return ret[0];
    }

    /**
     * Hovers over the PhysicalObject.
     * @param ms Time in milliseconds to hover.
     */
    public void hover(int ms) {
        if (!onScreen()) {
            return;
        }
        final KTimer timeout = new KTimer(ms);
        MouseHoverJob mouseHoverJob = botEnv.mouse.createMouseHoverJob(new MouseMoveListener() {
            public void onMouseOverTarget(MouseJob mouseJob) {
                MouseHoverJob mouseHoverJob = (MouseHoverJob) mouseJob;
                if (timeout.isDone()) {
                    mouseHoverJob.stop();
                }
            }

            public void onFinished(MouseJob mouseJob) {
            }
        }, this, timeout);
        mouseHoverJob.start();
        mouseHoverJob.join();
    }

    public boolean onScreen() {
        return botEnv.calculations.isInGameArea(getScreenPos());
    }

    /**
     * Gets the current frames index. Can be used for timing stuff like high alching ;)
     *
     * @return
     */
    public int getCurrentAnimationFrameIndex() {
        return rChar.getCurrentAnimationFrame();
    }

    /**
     * Gets the character the player is interacting with or null if none.
     * Cast this to Player OR NPC if you need more specific
     *
     * @return
     */
    public Character getInteracting() {
        int index = rChar.getInteractingCharacterIndex();
        if (index == -1)
            return null;

        if (index < 32768) {
            return botEnv.npcs.getNPCAtIndex(index);
        } else {
            index -= 32768;
            return new Player(botEnv, botEnv.client.getPlayers()[index]);
        }
    }

    /**
     * Checks if the character is interacting with another character!
     * DOES NOT SUPPORT interaction with objects
     *
     * @return
     */
    public boolean isInteracting() {
        return getInteracting() != null;
    }

    /**
     * Checks if the character is currently in combat.
     *
     * @return
     */
    public boolean isInCombat() {
        int loopCycleStatus = botEnv.client.getLoopCycle()-130;
        for(int loopCycle: rChar.getHitLoopCycleArray()){
            if(loopCycle > loopCycleStatus){
                return true;
            }
        }
        return false;
    }


    /**
     * This method gets the character current hp percentage by reading the length of the hp bar.
     * Therefor this only works if the character is in combat. If the character is not in combat it will retun 100% even if its low on HP.
     * @return the percentage of hp the character has left
     */
    public int getHPPercent() {
        return isInCombat() ? rChar.getHPRatio() * 100 / 255 : 100;
    }


    /**
     * Gets the message currently hovering over the character.
     * @return
     */
    public String getMessage(){
        return rChar.getMessage();
    }
}

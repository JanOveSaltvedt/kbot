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
import com.kbotpro.scriptsystem.input.callbacks.MouseMoveListener;
import com.kbotpro.scriptsystem.input.jobs.MouseHoverJob;
import com.kbotpro.scriptsystem.input.jobs.MouseJob;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.scriptsystem.interfaces.Targetable;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.various.Point3D;

import java.awt.*;

/**
 * Wrapper for in game objects. This can be anything from a tree/stone, decorations and walls.
 */
public class PhysicalObject extends Renderable implements Targetable {
    private com.kbotpro.hooks.PhysicalObject rPhysicalObject;
    private Type type;
    private Model model;

    public PhysicalObject(BotEnvironment botEnv, com.kbotpro.hooks.PhysicalObject rPhysicalObject, Type type) {
        super(botEnv, rPhysicalObject);
        this.rPhysicalObject = rPhysicalObject;
        this.type = type;
    }

    /**
     * Gets the ID number of the object.
     *
     * @return ID number of object
     */
    public int getID() {
        try {
            return rPhysicalObject.getDataContainer().getID() & 0xffff;
        } catch (java.lang.ClassCastException e) {
            return -1;
        }
    }

    public Point getScreenPos() {
        Model model = getModel();
        if (model != null) {
            Point3D point3D = model.getCenter();
            return getCalculations().worldToScreen(getRegionalX() + point3D.x, getRegionalY() + point3D.z, -point3D.y);
        }
        return getCalculations().worldToScreen(getRegionalX(), getRegionalY(), 10);
    }

    public Type getType() {
        return type;
    }

    /**
     * Gets the 3D model of the object.
     * If this object only got one form please use
     * getModel(boolean useModelCache)
     * instead to save CPU usage on construction.
     *
     * @return
     */
    public Model getModel() {
        com.kbotpro.hooks.Model rModel = rPhysicalObject.getDataContainer().getModel();
        if (rModel == null) {
            return null;
        }
        return new GameModel(botEnv, rModel, this);
    }

    /**
     * Gets the 3D model of the object.
     * This method can caches the model internally to save CPU.
     * Use this if the object only got 1 form and is not animating.
     *
     * @param useModelCache parse true if you want to use the cached model.
     * @return
     */
    public Model getModel(boolean useModelCache) {
        if (!useModelCache) {
            return getModel();
        }

        if (model == null) {
            model = getModel();
            return model;
        } else {
            return model;
        }
    }

    /**
     * Get target
     *
     * @return
     */
    public MouseTarget getTarget() {
        if (getModel() != null) {
            return new MouseTarget() {
                Point3D seed = getModel().createSeed();

                public Point get() {
                    return botEnv.calculations.worldToScreen(seed.x + getRegionalX(), seed.z + getRegionalY(), -seed.y);
                }

                public boolean isOver(int posX, int posY) {
                    return getModel().isPointOver(posX, posY);
                }
            };
        } else {
            return new MouseTarget() {
                public Point get() {
                    return getScreenPos();
                }

                public boolean isOver(int posX, int posY) {
                    Point p = get();
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

    /**
     * Hovers over the PhysicalObject.
     *
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

    public int getHeight() {
        Model model = getModel();
        if (model != null) {
            return model.getHeight();
        }
        return -10;
    }

    /**
     * Types for Physical Objects.
     */
    public enum Type {
        INTERACTIVE, // Trees, doors and similar
        DECORATION, // Ground decorations
        BOUNDARY, // Fences and walls
        UNKNOWN_1, // Not sure what it contains
        UNKNOWN_2, // Not sure what it contains
    }
}

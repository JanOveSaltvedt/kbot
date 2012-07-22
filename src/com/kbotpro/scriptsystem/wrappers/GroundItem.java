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
import com.kbotpro.hooks.GroundObject;
import com.kbotpro.hooks.Item;
import com.kbotpro.scriptsystem.input.callbacks.MouseMoveListener;
import com.kbotpro.scriptsystem.input.jobs.MouseHoverJob;
import com.kbotpro.scriptsystem.input.jobs.MouseJob;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.scriptsystem.interfaces.Targetable;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.various.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Dec 11, 2009
 * Time: 2:31:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroundItem extends GameObject implements Targetable{
    private Item rItem;
    private GroundObject rGroundObject;

    public GroundItem(BotEnvironment botEnv, com.kbotpro.hooks.Item rItem, GroundObject groundObject) {
        super(botEnv, groundObject);
        this.rItem = rItem;
        rGroundObject = groundObject;
    }

    /**
     * Gets the item ID of this object.
     * @return
     */
    public int getID(){
        return rItem.getID();
    }

    /**
     * Gets the stack size of this object.
     * @return
     */
    public int getStackSize(){
        return rItem.getStackSize();
    }

    /**
     * Gets the models for the ground items at this tile.
     * This only contains the top 3 models as it only these models that is displayed.
     *
     * @return The length of this array can vary from 1 to 3. It can also be 0 if an error occurred.
     */
    public Model[] getModels(){
        com.kbotpro.hooks.Model model1 = rGroundObject.getModel1();
        List<Model> out = new ArrayList<Model>();
        if(model1 == null){
            return new Model[0];
        }
        out.add(new GameModel(botEnv, model1, this));
        if(rGroundObject.getID2() != -1){
            com.kbotpro.hooks.Model model2 = rGroundObject.getModel2();
            if(model2 != null){
                out.add(new GameModel(botEnv, model2, this));
            }
        }
        if(rGroundObject.getID3() != -1){
            com.kbotpro.hooks.Model model3 = rGroundObject.getModel3();
            if(model3 != null){
                out.add(new GameModel(botEnv, model3, this));
            }
        }
        return out.toArray(new Model[out.size()]);
    }

    public Point getScreenPos() {
        Model[] models = getModels();
        if(models.length != 0){
            Point3D point3D = models[0].getCenter();
            return getCalculations().worldToScreen(getRegionalX() + point3D.x, getRegionalY()+point3D.z, -rGroundObject.getPosZ()-point3D.y, true);
        }
        return getCalculations().worldToScreen(getRegionalX(), getRegionalY(), -rGroundObject.getPosZ(), true);
    }

    /**
     * Get target
     *
     * @return
     */
    public MouseTarget getTarget() {
        final Model[] models = getModels();
        if (models.length != 0) {
            return new MouseTarget() {
                Point3D seed = models[0].createSeed();

                public Point get() {
                    return botEnv.calculations.worldToScreen(seed.x + getRegionalX(), seed.z + getRegionalY(), -rGroundObject.getPosZ()-seed.y, true);
                }

                public boolean isOver(int posX, int posY) {
                    if(models[0].isPointOver(posX, posY)){
                        return true;
                    }
                    if(models.length >= 2){
                        if(models[1].isPointOver(posX, posY)){
                            return true;
                        }
                    }
                    if(models.length >= 3){
                        if(models[2].isPointOver(posX, posY)){
                            return true;
                        }
                    }
                    return false;
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

}

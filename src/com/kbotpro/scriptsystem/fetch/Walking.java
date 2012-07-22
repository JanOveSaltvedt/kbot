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

import com.kbotpro.hooks.IComponent;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.fetch.Settings;
import com.kbotpro.scriptsystem.input.jobs.MouseHoverJob;
import com.kbotpro.scriptsystem.input.jobs.MouseJob;
import com.kbotpro.scriptsystem.input.callbacks.MouseMoveListener;
import com.kbotpro.scriptsystem.wrappers.Tile;
import com.kbotpro.scriptsystem.wrappers.Player;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.scriptsystem.Calculations;
import com.kbotpro.bot.BotEnvironment;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import org.apache.commons.lang.ArrayUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 22, 2009
 * Time: 9:02:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Walking extends ModuleConnector {
    public Walking(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Should get the run energy
     * @return
     * @author ToshiXZ
     */
    public int getEnergy() {
        try {
            return Integer.parseInt(botEnv.interfaces.getComponent(750, 5).getText());
        } catch (final NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Create a mouse hover job that will move the mouse and click on the given tile on the minimap
     * @param tile
     * @return
     */
    public MouseHoverJob createMouseMoveToTileMM(final Tile tile){
        return botEnv.mouse.createMouseHoverJob(new MouseMoveListener() {
            int count = 0;
            public void onMouseOverTarget(MouseJob mouseJob) {
                count++;
                if(count > random(5, 70)){
                    mouseJob.doMouseClick(true);
                    mouseJob.stop();
                }
            }

            public void onFinished(MouseJob mouseJob) {

            }
        }, new MouseTarget() {
            public Point get() {
                return botEnv.calculations.tileToMinimap(tile);
            }

            public boolean isOver(int posX, int posY) {
                Point p = get();
                return posX > p.x-1 && posX < p.x+1 && posY > p.y-1 && posY < p.y+1;
            }
        }, new KTimer(3000));
    }

    /**
     * Walk the given path if possible
     * NOTE: This is a copy of the method from KBot2 and is not yet updated to KBot PRO standards.
     * A new method will be made-
     * @param path
     * @return
     */
    public boolean walkPath(Tile[] path){
        return walkPath(path, false);
    }

    /**
     * Walk the given path if possible
     * NOTE: This is a copy of the method from KBot2 and is not yet updated to KBot PRO standards.
     * A new method will be made-
     * @param path
     * @param run will run if you have enough energy 
     * @return
     */
    public boolean walkPath(Tile[] path, boolean run){
        if(path == null || path.length == 0){
            throw new IllegalArgumentException("Path can not be null or empty");
        }
        final Player myPlayer = botEnv.players.getMyPlayer();
        Tile curPos = myPlayer.getLocation();
        Tile lastPos = curPos;
        int pos = 0;
        for(pos = 0; pos < path.length; pos++){
            Tile tile = path[pos];
            if (mapCircle().contains(getCalculations().tileToMinimap(tile)))
                break;
        }
        int tries = 0;
        while(pos < path.length){
            if(tries > 35 || botEnv.scriptManager.isScriptsPaused())
                return false;
            tries++;
            Tile tile = path[pos];
            while(mapCircle().contains(getCalculations().tileToMinimap(tile))){// Get the furthest tile.
                pos++;
                if(pos >= path.length)
                    break;
                tile = path[pos];
            }

            pos--;
            tile = path[pos];
            curPos = myPlayer.getLocation();
            if(!mapCircle().contains(getCalculations().tileToMinimap(tile))){
                return false;
            }

            if((!tile.equals(lastPos) && (curPos.distanceTo(tile) < 14)) || !myPlayer.isMoving()) {
                walkToMM(tile);
                lastPos = tile;

                for (int i = 0; i < 2000; i += 50) {
                    if (myPlayer.isMoving()) break;
                    sleep(50);
                }

            } else if(myPlayer.isMoving()) {
                if(getEnergy() > random(25, 57) && random(1, 3)== 2 && run)
                    setRunning(true);
                tries = 0;
                sleep(100,150);
            } else {
                sleep(100,150);
            }
            if(pos == path.length -1){

                while(myPlayer.isMoving()) {
                    if (botEnv.game.getDestination() != null && curPos.distanceTo(botEnv.game.getDestination()) < 8 ) {
                        break;
                    }
                    sleep(10, 50);
                }

                curPos = myPlayer.getLocation();

                if(curPos.distanceTo(path[pos]) < 3) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return Ellipse2D of the minimap.
     */
    public Ellipse2D mapCircle() {
        if (!botEnv.game.isLoggedIn()) {
            return (Ellipse2D)botEnv.calculations.getGameScreenShape();

        }
        Ellipse2D circle;
        if (botEnv.game.isFixedMode()) {
            com.kbotpro.scriptsystem.wrappers.IComponent miniMap = botEnv.interfaces.getComponent(548,60);
            circle = new Ellipse2D.Double(miniMap.getCenter().getX()-90, miniMap.getCenter().getY()-75, 152, 152);
        } else {
            com.kbotpro.scriptsystem.wrappers.IComponent miniMap = botEnv.interfaces.getComponent(746,9);
            circle = new Ellipse2D.Double(miniMap.getCenter().getX()-50, miniMap.getCenter().getY()-78, 136, 136);
        }
        return circle;
    }

    /**
     * Makes the bot click on the tile on the minimap
     * @param tile
     * @return
     */
    public boolean walkToMM(Tile tile){
        MouseHoverJob mouseHoverJob = createMouseMoveToTileMM(tile);
        Point point = mouseHoverJob.getTarget().get();
        if(point.x == -1 || point.y == -1){
            return false;
        }
        mouseHoverJob.start();
        mouseHoverJob.join();
        return true;
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
            out[i] = new Tile(path[i].getX()+(int) Calculations.random((double) -randomX, (double)randomX), path[i].getY()+(int)Calculations.random((double) -randomY, (double)randomY));
        return out;
    }

    /**
     * Sets running mode on and off
     * @param run
     * @return
     */
    public boolean setRunning(boolean run) {
        if (botEnv.settings.getSetting(173) != (run ? 1 : 0)) {
            botEnv.interfaces.getComponent(750, 1).doClick();
            sleep(100, 300);
        }
        return botEnv.settings.getSetting(/*Settings.SETTING_BANK_TOGGLE_WITHDRAW_MODE*/ 173) == (run ? 1 : 0);
    }

    /**
     * Reverses a Tile array.
     *
     * @param in The Tile Array you want to reverse.
     * @return Returns a Tile array.k
     */
    public static Tile[] reversePath(Tile[] in) {
        final int length = in.length;
        Tile[] out = new Tile[length];
        for(int i = 0; i < length; i++){
            out[length -1-i] = in[i];
        }
        return out;
    }

    /**
     * Checks if the walking destination is set
     * @return
     */
    public boolean isDestinationSet(){
        return botEnv.game.isDestinationSet();
    }

    /**
     * Gets the walking destination
     * @return destination or null if destination is not set
     */
    public Tile getDestination(){
        return botEnv.game.getDestination();
    }


}

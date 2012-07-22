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

package com.kbot2.scriptable.methods;

import com.kbot2.bot.BotEnvironment;
import com.kbot2.scriptable.methods.wrappers.Tile;
import com.kbotpro.hooks.Client;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 3:10:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Calculations {
    private final BotEnvironment botEnv;

    public Calculations(BotEnvironment botEnv) {
        this.botEnv = botEnv;
    }

    public static int random(int min, int max) {
        return ((int) (Math.random() * (max - min))) + min;
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public int tileHeight(int plane, int x, int y) {
        return botEnv.proBotEnvironment.calculations.getTileHeight(plane, x, y);
    }

    public Point worldToScreen(int X, int Y, int height) {
        return botEnv.proBotEnvironment.calculations.worldToScreen(X, Y, height);
    }

    private boolean isOnGameScreen(Point p) {
        return botEnv.proBotEnvironment.calculations.onScreen(p);
    }

    /**
     * Only works for fixed mode
     * @param x
     * @param y
     * @return
     */
    public static boolean onScreen(int x, int y){
        return x > -1 && y > -1 && x < 757 && y < 504;
    }

    /**
     * Only works for fixed mode
     * @param p
     * @return
     */
    public static boolean onScreen(Point p){
        return onScreen(p.x, p.y);
    }

    /**
     * Calculates a point minimap
     * @param x
     * @param y
     * @return new Point(-1, -1) if not on minimap.
     */
    public Point worldToMinimap(double x, double y){
        return botEnv.proBotEnvironment.calculations.worldToMinimap(x, y);
    }

    public Point tileToMinimap(Tile tile){
        return worldToMinimap(tile.getX(), tile.getY());
    }

    public static int UIDtoID(long UID){
        return (int) (UID >>> 32 & 0x7fffffff);
    }

    public Point tileToScreen(Tile t) {
        return tileToScreen(t.getX(), t.getY(), 0.5, 0.5, 0);
    }

    public Point tileToScreen(int tileX, int tileY, double dX, double dY, int height) {
        return worldToScreen((int)((tileX - getClient().getBaseX() + dX) * 512), (int)((tileY
                - getClient().getBaseY() + dY) * 512), height);
    }

    public Client getClient() {
        return botEnv.getClient();
    }
}

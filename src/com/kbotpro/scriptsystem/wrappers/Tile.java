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
import com.kbotpro.scriptsystem.Calculations;
import com.kbotpro.scriptsystem.input.callbacks.MouseMoveListener;
import com.kbotpro.scriptsystem.input.jobs.MouseHoverJob;
import com.kbotpro.scriptsystem.input.jobs.MouseJob;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.scriptsystem.interfaces.Targetable;
import com.kbotpro.scriptsystem.interfaces.WorldLocation;
import com.kbotpro.scriptsystem.various.KTimer;

import java.awt.*;

/**
 *
 */
public class Tile implements WorldLocation, Targetable {
    protected int x;
    protected int y;
    protected BotEnvironment botEnv;
    /**
     * Constructor for Tile class
     * @param x world wide x location
     * @param y world wide y location
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor using java awt point
     * @param p Contains world wide coordinates for x and y location.
     */
    public Tile(Point p){
        this.x = p.x;
        this.y = p.y;
    }

    @Override
    /**
     * @return Returns a string in the format: "Tile[X=xCord,Y=yCoord]"
     */
    public String toString() {
        return "Tile[x="+x+",Y="+y+"]";
    }


    /**
     * Gets the Tile location.
     *
     * @param baseX baseX must be suplied to calculate this.
     * @param baseY baseY must be suplied to calculate this.
     * @return Returns a Tile containing the worldwide location
     */
    public Tile getTile(int baseX, int baseY) {
        return this;
    }

    /**
     * Gets the regional X location
     *
     * @param baseX baseX must be suplied to calculate this.
     * @return Returns an integer containing the regional y location
     */
    public int getRegionalX(int baseX) {
        return ((x - baseX) << 9) + 256;
    }

    /**
     * Gets the regional Y location
     *
     * @param baseY baseY must be suplied to calculate this.
     * @return Returns an integer containing the regional y location
     */
    public int getRegionalY(int baseY) {
        return ((y - baseY) << 9) + 256;
    }

    /**
     * Same as getTile, but may not work on all implementations as baseX and baseY is not provided
     *
     * @return
     */
    public Tile getLocation() {
        return this;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double distanceToPrecise(WorldLocation wl) {
        Tile tile = wl.getLocation();
        return Math.hypot(tile.x - this.x, tile.y - this.y);
    }

    public boolean equals(Tile tile){
        return tile.x == x && tile.y == y;
    }

    public int distanceTo(WorldLocation wl) {
        return (int) distanceToPrecise(wl);
    }

    /**
     * Moves the mouse to the tile and clicks at the given action
     * NOTE: Do not use this method while you have a mouse job active!
     *
     * @param actionContains A string that the action contains. Case ignored
     * @return Boolean, true if succeeded, false if not.
     */
    @Deprecated
    public boolean doAction(final BotEnvironment botEnv, final String actionContains) {
        if (botEnv == null) {
            return false;
        }
        this.botEnv = botEnv;
        final Point point = botEnv.calculations.tileToScreen(this);
        final Tile tile = this;
        if (!botEnv.calculations.isInGameArea(point)) {
            return false;
        }
        final boolean[] ret = new boolean[]{false};
        MouseHoverJob mouseHoverJob = botEnv.mouse.createMouseHoverJob(new MouseMoveListener() {
            private int count = 0;

            public void onMouseOverTarget(MouseJob mouseJob) {
                MouseHoverJob mouseHoverJob = (MouseHoverJob) mouseJob;
                count++;
                if (count > Calculations.random(5, 100)) {
                    mouseHoverJob.stop();
                    mouseHoverJob.cancel();
                    ret[0] = botEnv.menu.atMenu(actionContains);
                }
            }

            public void onFinished(MouseJob mouseJob) {
            }

            public void update(MouseJob mouseJob) {

            }

        }, this, new KTimer(5000));
        mouseHoverJob.start();
        mouseHoverJob.join();
        return ret[0];
    }

    /**
     * Moves the mouse to the tile and left clicks
     * NOTE: Do not use this method while you have a mouse job active!
     *
     * @return Boolean, true if succeeded, false if not.
     */
    @Deprecated
    public boolean doClick(final BotEnvironment botEnv) {
        final Point point = botEnv.calculations.tileToScreen(this);
        if (!botEnv.calculations.isInGameArea(point)) {
            return false;
        }
        final boolean[] ret = new boolean[]{false};
        MouseHoverJob mouseHoverJob = botEnv.mouse.createMouseHoverJob(new MouseMoveListener() {
            private int count = 0;

            public void onMouseOverTarget(MouseJob mouseJob) {
                MouseHoverJob mouseHoverJob = (MouseHoverJob) mouseJob;
                count++;
                if (count > Calculations.random(5, 100)) {
                    mouseHoverJob.doMouseClick(true);
                    ret[0] = true;
                    mouseHoverJob.stop();
                    mouseHoverJob.cancel();
                }
            }

            public void onFinished(MouseJob mouseJob) {
            }
        }, this, new KTimer(5000));
        mouseHoverJob.start();
        mouseHoverJob.join();
        return ret[0];
    }

    public MouseTarget getTarget() {
        final Point point = botEnv.calculations.tileToScreen(this);
        final Tile tile = this;
        return new MouseTarget() {
            public Point get() {
                return point;
            }

            public boolean isOver(int posX, int posY) {
                Polygon polygon = new Polygon();
                int regionalX = tile.getRegionalX(botEnv.client.getBaseX());
                int regionalY = tile.getRegionalY(botEnv.client.getBaseY());

                Calculations calculations1 = botEnv.calculations;
                Point p2 = calculations1.worldToScreen(regionalX-64, regionalY-64, 0);
                polygon.addPoint(p2.x, p2.y);
                p2 = calculations1.worldToScreen(regionalX-64, regionalY+64, 0);
                polygon.addPoint(p2.x, p2.y);
                p2 = calculations1.worldToScreen(regionalX+64, regionalY+64, 0);
                polygon.addPoint(p2.x, p2.y);
                p2 = calculations1.worldToScreen(regionalX+64, regionalY-64, 0);
                polygon.addPoint(p2.x, p2.y);
                return polygon.contains(posX, posY);
            }
        };
    }
    /**
     * Converts this tile to an AWT point.
     * @return
     */
    public Point toPoint(){
        return new Point(x, y);
    }

    public Tile randomizeTile(int maxX, int maxY) {
        this.setX(this.x+Calculations.random(-maxX, maxX));
        this.setY(this.y+Calculations.random(-maxY, maxY));
        return this;
    }
}

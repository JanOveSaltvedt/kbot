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



package com.kbotpro.scriptsystem;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.bot.RenderData;
import com.kbotpro.hooks.Client;
import com.kbotpro.hooks.Plane;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Tile;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Sep 19, 2009
 * Time: 11:13:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class Calculations {
    public static int[] CURVE_COS;
    public static int[] CURVE_SIN;

    private BotEnvironment bot;

    public Calculations(BotEnvironment bot) {
        this.bot = bot;
    }

    /**
     * Gets random number between min and max (exclusive)
     *
     * @param min
     * @param max
     * @return
     */
    public static int random(int min, int max) {
        return ((int) (Math.random() * (max - min))) + min;
    }

    /**
     * Converts a 3D point to a 2D point on screen
     *
     * @param x      The relative to plane x coord
     * @param z      This is actually the y coord. The difference is in the conversion from 3D to 2D plane axises. This is the relative to plane y coord
     * @param height the height above ground.
     * @return
     */
    public Point worldToScreen(int x, int z, final int height) {
        return worldToScreen(x, z, height, false);
    }

    /**
     * Converts a 3D point to a 2D point on screen
     *
     * @param x      The relative to plane x coord
     * @param z      This is actually the y coord. The difference is in the conversion from 3D to 2D plane axises. This is the relative to plane y coord
     * @param height the height above ground.
     * @return
     */
    public Point worldToScreen(int x, int z, final int height, boolean includedGroundHeight) {
        Client client = bot.client;
        RenderData renderData = bot.renderData;

        if (renderData == null
                // || renderVars == null
                || x < 512
                || z < 512
                || x > 52224
                || z > 52224) {

            return new Point(-1, -1);
        }

        final int y = includedGroundHeight?-height:(getTileHeight(client.getCurrentPlane(), x, z) - height);

        Point p = null;
        int detail = 0;
        if (detail == 0) { // Low detail
            final int distanceToPoint = renderData.zOff + (renderData.x3 * x
                    + renderData.y3 * y + renderData.z3 * z >> 14);

            // Here there should be done clipping if it was rendering but not needed for worldToScreen
            if (distanceToPoint == 0) { // We don't devide by zero!
                return new Point(-1, -1);
            }

            final int calcX = (renderData.screenFactorX
                    * (renderData.xOff + (((renderData.x1 * x)
                    + (renderData.y1 * y) + (renderData.z1 * z)) >> 14))) / distanceToPoint;

            final int calcY = (renderData.screenFactorY
                    * (renderData.yOff + (renderData.x2 * x
                    + renderData.y2 * y + renderData.z2 * z >> 14))) / distanceToPoint;

            int minX = renderData.minX;
            int minY = renderData.minY;

            if (calcX >= minX && calcX <= renderData.maxX
                    && calcY >= minY && calcY <= renderData.maxY) {
                int screenx = calcX - minX;
                int screeny = calcY - minY;
                if (bot.game.isFixedMode()) {
                    // Fixed mode
                    return new Point(screenx + 4, screeny + 4);
                }
                return new Point(screenx,
                        screeny);
            }
        } else if (detail == 1) { // High detail
            p = new Point(-1, -1); // High detail is not yet supported
        }

        if (p != null)
            return p;

        return new Point(-1, -1);
    }


    public int getTileHeight(int plane, int X, int Y) {
        final Client client = bot.client;
        Plane[] planes = client.getPlaneArray();

        if (planes == null)
            return 0;
        int x1 = X >> 9;
        int y1 = Y >> 9;
        if (x1 < 0 || y1 < 0 || x1 > 103 || y1 > 103)
            return 0;
        int x2 = X & 0x1ff;
        int y2 = Y & 0x1ff;
        int zIndex = plane;
        if (zIndex <= 3 && (client.getGroundSettingsArray()[1][x1][y1] & 2) == 2)
            zIndex++;
        while(planes[zIndex] == null){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            planes = client.getPlaneArray();
        }
        final int[][] groundHeights = planes[zIndex].getTileHeights();
        if (groundHeights == null) {
            return 0;
        }

        int height1 = ((groundHeights[x1][y1] * (512 - x2)) + (groundHeights[x1 + 1][y1] * x2)) >> 9;
        int height2 = ((groundHeights[x1][(y1 + 1)] * (512 - x2)) + (groundHeights[1 + x1][(y1 + 1)] * x2)) >> 9;
        return ((height1 * (512 - y2)) + (y2 * height2)) >> 9;
    }

    public Point worldToMinimap(double x, double y) {
        x -= bot.client.getBaseX();
        y -= bot.client.getBaseY();
        final int calculatedX = (int) (x * 4 + 2)
                - bot.client.getMyPlayer().getPosX() / 128;
        final int calculatedY = (int) (y * 4 + 2)
                - bot.client.getMyPlayer().getPosY() / 128;

        try {
            final com.kbotpro.hooks.IComponent mm = bot.interfaces.getCoreMinimapIComponent();
            if (mm == null) {
                return new Point(-1, -1);
            }
            final IComponent mm2 = bot.interfaces.getComponentByUID(mm.getUID());

            final int actDistSq = calculatedX * calculatedX + calculatedY
                    * calculatedY;

            final int mmDist = 10 + Math.max(mm2.getWidth() / 2, mm2
                    .getHeight() / 2);
            if (mmDist * mmDist >= actDistSq) {
                int angle = 0x3fff & (int) bot.client.getCompassAngle();
                if (bot.client.getMinimapSetting() != 4) {
                    angle = 0x3fff & bot.client.getMinimapOffset()
                            + (int) bot.client.getCompassAngle();
                }

                int cs = Calculations.CURVE_SIN[angle];
                int cc = Calculations.CURVE_COS[angle];
                if (bot.client.getMinimapSetting() != 4) {
                    final int fact = 256 + bot.client.getMinimapScale();
                    cs = 256 * cs / fact;
                    cc = 256 * cc / fact;
                }

                final int calcCenterX = cc * calculatedX + cs * calculatedY >> 15;
                final int calcCenterY = cc * calculatedY - cs * calculatedX >> 15;

                final int screenx = calcCenterX + mm2.getAbsoluteX()
                        + mm2.getWidth() / 2;
                final int screeny = -calcCenterY + mm2.getAbsoluteY()
                        + mm2.getHeight() / 2;

                Rectangle bounds = mm2.getBounds();
                // Check if its really inside the minimap
                if (bounds.contains(screenx, screeny)) {
                    // Check if point is within the circel of the minimap instead of
                    // the
                    // rectangle!

                    if ((Math.max(calcCenterY, -calcCenterY) <= (((mm2.getWidth()) / 2.0) * .8))
                            && (Math.max(calcCenterX, -calcCenterX) <= (((mm2
                            .getHeight()) / 2) * .8))) {
                        return new Point(screenx, screeny);
                    } else {
                        return new Point(-1, -1);
                    }
                }
                else{
                    return new Point(-1, -1);
                }
            }
        } catch (final NullPointerException ignore) {
        }

        return new Point(-1, -1);// not on minimap
    }

    public Point tileToScreen(int tileX, int tileY, double dX, double dY, int height) {
        return worldToScreen((int) ((tileX - bot.client.getBaseX() + dX) * 512), (int) ((tileY
                - bot.client.getBaseY() + dY) * 512), height);
    }

    public Point tileToScreen(Tile t) {
        return tileToScreen(t.getX(), t.getY(), 0.5, 0.5, 0);
    }

    public Point tileToMinimap(Tile tile) {
        return worldToMinimap(tile.getX(), tile.getY());
    }


    static {
        CURVE_COS = new int[16384];
        CURVE_SIN = new int[16384];
        double d = 0.00038349519697141029D;
        for (int i = 0; i < 16384; i++) {
            CURVE_COS[i] = (int) (32768D * Math.cos((double) i * d));
            CURVE_SIN[i] = (int) (32768D * Math.sin((double) i * d));
        }
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public boolean onScreen(Point point) {
        return point.x > 0 && point.y > 0 && point.y < bot.appletHeight && point.x < bot.appletWidth;
    }

    /**
     * Determines if a point is inside the playing area of the game, ie: not on the minimap, tabs, chatbox, etc.
     * @param p Point on screen
     * @return Returns true if the point is inside the game area.
     */
    public boolean isInGameArea(Point p) {
        return getGameScreenShape().contains(p);
    }

    /**
     * Forms a Shape around the interfaces in the game, return the area in game where you can perform actions on the ground.
     * @return
     */
    public Shape getGameScreenShape() {
        IComponent fixed = bot.interfaces.getComponent(548, 3);
        if (bot.game.isFixedMode() && fixed != null && fixed.isVisible() && bot.game.isLoggedIn()) {
            return fixed.getBounds();
        }
        Polygon clickable = new Polygon();
        clickable.addPoint(0,0);
        IComponent m = bot.interfaces.getComponent(746,9);
        IComponent t = bot.interfaces.getComponent(746,19);
        IComponent c = bot.interfaces.getComponent(751,0);
        IComponent cb = bot.interfaces.getComponent(137,50);
        IComponent tab = bot.interfaces.getComponent(746, 82);
        if (!bot.game.isLoggedIn() || m == null || t == null || c == null || cb == null || tab == null ) {
            return new Rectangle(0, 0, bot.appletWidth, bot.appletHeight);
        }

        //Minimap
        clickable.addPoint(m.getAbsoluteX(),0);
        clickable.addPoint(m.getAbsoluteX(),m.getAbsoluteY()+m.getHeight());
        clickable.addPoint(m.getAbsoluteX()+m.getWidth(),m.getAbsoluteY()+m.getHeight());
        //Minimap



        //Tab bar
        clickable.addPoint(t.getAbsoluteX()+t.getWidth(),t.getAbsoluteY());
        if (bot.game.getCurrentTab() != 16) {
            int y = tab.getAbsoluteY();
            if (y <= m.getHeight()) {
                y = y+(m.getHeight() - y);
            }
            clickable.addPoint(tab.getAbsoluteX()+tab.getWidth(), y+tab.getHeight());
            clickable.addPoint(tab.getAbsoluteX()+tab.getWidth(), y);
            clickable.addPoint(tab.getAbsoluteX(), y);
            clickable.addPoint(tab.getAbsoluteX(), y+tab.getHeight());
        }
        clickable.addPoint(t.getAbsoluteX(),t.getAbsoluteY());
        clickable.addPoint(t.getAbsoluteX(),t.getAbsoluteY()+t.getHeight());
        //Tab bar




        //chat bar
        clickable.addPoint(c.getAbsoluteX()+c.getWidth(),c.getAbsoluteY()+c.getHeight());
        clickable.addPoint(c.getAbsoluteX()+c.getWidth(),c.getAbsoluteY());
        //chat bar

        //chatbox
        boolean chatup = false;
        IComponent[] chat = bot.interfaces.getInterface(751).getComponents();
        for (IComponent aChat : chat) {
            if (aChat.getTextureID() == 1022) {
                chatup = true;
            }
        }

        if (chatup) {
            clickable.addPoint(cb.getAbsoluteX()+cb.getWidth(),cb.getAbsoluteY()+cb.getWidth());
            clickable.addPoint(cb.getAbsoluteX()+cb.getWidth(),cb.getAbsoluteY());
            clickable.addPoint(0,cb.getAbsoluteY());
        } else {
            clickable.addPoint(0,c.getAbsoluteY());
        }
        //chatbox

        return clickable;
    }

    /**
     * Determines if a tile is reachable.
     * @param dest Destination tile
     * @param isObject True if the tile has an object on it.
     * @return  True if you're able to reach the tile.
     */
    public boolean canReach(Tile dest, boolean isObject) {
        final int startX = bot.players.getMyPlayer().getLocation().getX() - bot.client.getBaseX() + 1;
        final int startY = bot.players.getMyPlayer().getLocation().getY() - bot.client.getBaseY() + 1;
        final int destX = dest.getX() - bot.client.getBaseX() + 1;
        final int destY = dest.getY() - bot.client.getBaseY() + 1;
        final int[][] via = new int[104][104];
        final int[][] cost = new int[104][104];
        final int[] tileQueueX = new int[4000];
        final int[] tileQueueY = new int[4000];

        for (int xx = 0; xx < 104; xx++) {
            for (int yy = 0; yy < 104; yy++) {
                via[xx][yy] = 0;
                cost[xx][yy] = 99999999;
            }
        }

        int curX;
        int curY;
        via[startX][startY] = 99;
        cost[startX][startY] = 0;
        int head = 0;
        int tail = 0;
        tileQueueX[head] = startX;
        tileQueueY[head] = startY;
        head++;
        final int pathLength = tileQueueX.length;
        final int blocks[][] = bot.client.getMapDataArray()[bot.client.getCurrentPlane()].getTileData();
        while (tail != head) {
            curX = tileQueueX[tail];
            curY = tileQueueY[tail];

            if (!isObject && (curX == destX) && (curY == destY))
                return true;
            else if (isObject) {
                if (((curX == destX) && (curY == destY + 1)) || ((curX == destX) && (curY == destY - 1)) || ((curX == destX + 1) && (curY == destY)) || ((curX == destX - 1) && (curY == destY)))
                    return true;
            }
            tail = (tail + 1) % pathLength;

            // Big and ugly block of code
            final int thisCost = cost[curX][curY] + 1;
            // Can go south (by determining, whether the north side of the
            // south tile is blocked :P)
            if ((curY > 0) && (via[curX][curY - 1] == 0) && ((blocks[curX][curY - 1] & 0x1280102) == 0)) {
                tileQueueX[head] = curX;
                tileQueueY[head] = curY - 1;
                head = (head + 1) % pathLength;
                via[curX][curY - 1] = 1;
                cost[curX][curY - 1] = thisCost;
            }
            // Can go west
            if ((curX > 0) && (via[curX - 1][curY] == 0) && ((blocks[curX - 1][curY] & 0x1280108) == 0)) {
                tileQueueX[head] = curX - 1;
                tileQueueY[head] = curY;
                head = (head + 1) % pathLength;
                via[curX - 1][curY] = 2;
                cost[curX - 1][curY] = thisCost;
            }
            // Can go north
            if ((curY < 104 - 1) && (via[curX][curY + 1] == 0) && ((blocks[curX][curY + 1] & 0x1280120) == 0)) {
                tileQueueX[head] = curX;
                tileQueueY[head] = curY + 1;
                head = (head + 1) % pathLength;
                via[curX][curY + 1] = 4;
                cost[curX][curY + 1] = thisCost;
            }
            // Can go east
            if ((curX < 104 - 1) && (via[curX + 1][curY] == 0) && ((blocks[curX + 1][curY] & 0x1280180) == 0)) {
                tileQueueX[head] = curX + 1;
                tileQueueY[head] = curY;
                head = (head + 1) % pathLength;
                via[curX + 1][curY] = 8;
                cost[curX + 1][curY] = thisCost;
            }
            // Can go southwest
            if ((curX > 0) && (curY > 0) && (via[curX - 1][curY - 1] == 0) && ((blocks[curX - 1][curY - 1] & 0x128010e) == 0) && ((blocks[curX - 1][curY] & 0x1280108) == 0) && ((blocks[curX][curY - 1] & 0x1280102) == 0)) {
                tileQueueX[head] = curX - 1;
                tileQueueY[head] = curY - 1;
                head = (head + 1) % pathLength;
                via[curX - 1][curY - 1] = 3;
                cost[curX - 1][curY - 1] = thisCost;
            }
            // Can go northwest
            if ((curX > 0) && (curY < 104 - 1) && (via[curX - 1][curY + 1] == 0) && ((blocks[curX - 1][curY + 1] & 0x1280138) == 0) && ((blocks[curX - 1][curY] & 0x1280108) == 0) && ((blocks[curX][curY + 1] & 0x1280120) == 0)) {
                tileQueueX[head] = curX - 1;
                tileQueueY[head] = curY + 1;
                head = (head + 1) % pathLength;
                via[curX - 1][curY + 1] = 6;
                cost[curX - 1][curY + 1] = thisCost;
            }
            // Can go southeast
            if ((curX < 104 - 1) && (curY > 0) && (via[curX + 1][curY - 1] == 0) && ((blocks[curX + 1][curY - 1] & 0x1280183) == 0) && ((blocks[curX + 1][curY] & 0x1280180) == 0) && ((blocks[curX][curY - 1] & 0x1280102) == 0)) {
                tileQueueX[head] = curX + 1;
                tileQueueY[head] = curY - 1;
                head = (head + 1) % pathLength;
                via[curX + 1][curY - 1] = 9;
                cost[curX + 1][curY - 1] = thisCost;
            }
            // can go northeast
            if ((curX < 104 - 1) && (curY < 104 - 1) && (via[curX + 1][curY + 1] == 0) && ((blocks[curX + 1][curY + 1] & 0x12801e0) == 0) && ((blocks[curX + 1][curY] & 0x1280180) == 0) && ((blocks[curX][curY + 1] & 0x1280120) == 0)) {
                tileQueueX[head] = curX + 1;
                tileQueueY[head] = curY + 1;
                head = (head + 1) % pathLength;
                via[curX + 1][curY + 1] = 12;
                cost[curX + 1][curY + 1] = thisCost;
            }
        }
        return false;
    }

    public static boolean inArea(Tile target, Tile[] bounds) {
        Polygon poly = new Polygon();
        Point curr;
        for (Tile t : bounds) {
            curr = t.toPoint();
            poly.addPoint(curr.x, curr.y);
        }
        return poly.contains(target.toPoint());
    }

}

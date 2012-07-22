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
import com.kbotpro.bot.RenderData;
import com.kbotpro.hooks.Client;
import com.kbotpro.hooks.Plane;
import com.kbotpro.scriptsystem.Calculations;
import com.kbotpro.scriptsystem.various.Point3D;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 15, 2009
 * Time: 4:57:27 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Model extends Wrapper {
    protected Model(BotEnvironment botEnv) {
        super(botEnv);
    }

    protected int[] xPoints;

    /**
     * Negated! If you want a height of 50 the value must be -50
     */
    protected int[] yPoints;

    protected int[] zPoints;

    protected short[] indices1;
    protected short[] indices2;
    protected short[] indices3;

    protected short minX;
    /**
     * Negated
     */
    protected short minY;
    protected short minZ;

    protected short maxX;
    /**
     * Negated
     */
    protected short maxY;
    protected short maxZ;

    /**
     * Used for faster calculations
     */
    protected int[] boundsXPoints = new int[8];
    protected int[] boundsYPoints = new int[8];
    protected int[] boundsZPoints = new int[8];

    protected int[] boundsScreenX = new int[8];
    protected int[] boundsScreenY = new int[8];

    protected int[] screenPointsX;
    protected int[] screenPointsY;

    protected int baseX;
    protected int baseY;
    protected int baseZ;

    /**
     * Draws the bounding polygon the RS client uses to match models when they are animated
     * @param g
     */
    public void drawSimpleBounds(Graphics g){
        calculateBoundingBox();

        int minX = 9999999;
        int minY = 9999999;
        int maxX = -9999999;
        int maxY = -9999999;
        for(int i = 0; i < boundsScreenX.length; i++){
            int x = boundsScreenX[i];
            int y = boundsScreenY[i];

            if(x < minX){
                minX = x;
            }
            else if(x > maxX){
                maxX = x;
            }

            if(y < minY){
                minY = y;
            }
            else if(y > maxY){
                maxY = y;
            }
        }
        g.drawLine(minX, minY, minX, maxY);
        g.drawLine(minX, maxY, maxX, maxY);
        g.drawLine(maxX, maxY, maxX, minY);
        g.drawLine(maxX, minY, minX, minY);
    }

    /**
     * Draws a bounding box on the Graphics object.
     *
     * @param g
     */
    public void drawBoundingBox(Graphics g) {
        calculateBoundingBox();

        /**
         * TOP
         * (MinX, MinY, MinZ) to (MaxX, MinY, MinZ)
         */
        g.drawLine(boundsScreenX[0], boundsScreenY[0], boundsScreenX[1], boundsScreenY[1]);
        /**
         *  (MaxX, MinY, MinZ) to (MaxX, MinY, MaxZ)
         */
        g.drawLine(boundsScreenX[1], boundsScreenY[1], boundsScreenX[5], boundsScreenY[5]);

        /**
         *  (MaxX, MinY, MaxZ) to  (MinX, MinY, MaxZ)
         */
        g.drawLine(boundsScreenX[5], boundsScreenY[5], boundsScreenX[4], boundsScreenY[4]);

        /**
         *  (MinX, MinY, MaxZ) to (MinX, MinY, MinZ)
         */
        g.drawLine(boundsScreenX[4], boundsScreenY[4], boundsScreenX[0], boundsScreenY[0]);

        /**
         * Bottom
         * (MinX, MaxY, MinZ) to (MaxX, MaxY, MinZ)
         */
        g.drawLine(boundsScreenX[2], boundsScreenY[2], boundsScreenX[3], boundsScreenY[3]);

        /**
         *  (MaxX, MaxY, MinZ) to (MaxX, MaxY, MaxZ)
         */
        g.drawLine(boundsScreenX[3], boundsScreenY[3], boundsScreenX[7], boundsScreenY[7]);

        /**
         *  (MaxX, MaxY, MaxZ) to  (MinX, MaxY, MaxZ)
         */
        g.drawLine(boundsScreenX[7], boundsScreenY[7], boundsScreenX[6], boundsScreenY[6]);

        /**
         *  (MinX, MaxY, MaxZ) to (MinX, MaxY, MinZ)
         */
        g.drawLine(boundsScreenX[6], boundsScreenY[6], boundsScreenX[2], boundsScreenY[2]);

        /**
         * Side 1
         * (MinX, MaxY, MinZ) to (MinX, MinY, MinZ)
         */
        g.drawLine(boundsScreenX[2], boundsScreenY[2], boundsScreenX[0], boundsScreenY[0]);

        /**
         *  Side 2
         *  (MaxX, MaxY, MinZ) to (MaxX, MinY, MinZ)
         */
        g.drawLine(boundsScreenX[3], boundsScreenY[3], boundsScreenX[1], boundsScreenY[1]);

        /**
         *  Side 3
         *  (MaxX, MaxY, MaxZ) to  (MaxX, MinY, MaxZ)
         */
        g.drawLine(boundsScreenX[7], boundsScreenY[7], boundsScreenX[5], boundsScreenY[5]);

        /**
         *  Side 4
         *  (MinX, MaxY, MaxZ) to (MinX, MinY, MaxZ)
         */
        g.drawLine(boundsScreenX[6], boundsScreenY[6], boundsScreenX[4], boundsScreenY[4]);
    }

    public void drawWireframe(Graphics g) {
        calculateScreenPoints();
        for (int i = 0; i < indices1.length; i++) {
            int index1 = indices1[i];
            int index2 = indices2[i];
            int index3 = indices3[i];

            int point1X = screenPointsX[index1];
            int point1Y = screenPointsY[index1];
            int point2X = screenPointsX[index2];
            int point2Y = screenPointsY[index2];
            int point3X = screenPointsX[index3];
            int point3Y = screenPointsY[index3];

            g.drawLine(point1X, point1Y, point2X, point2Y);
            g.drawLine(point2X, point2Y, point3X, point3Y);
            g.drawLine(point3X, point3Y, point1X, point1Y);
        }
    }

    protected void calculateBoundingBox() {
        Client client = botEnv.client;
        RenderData renderData = botEnv.renderData;
        if (renderData == null) {
            return;
        }

        Plane[] planes = client.getPlaneArray();
        if (planes == null) {
            return;
        }

        updateBasePos();

        int curPlane = client.getCurrentPlane();

        boundsXPoints[0] = minX;
        boundsYPoints[0] = minY;
        boundsZPoints[0] = minZ;

        boundsXPoints[1] = maxX;
        boundsYPoints[1] = minY;
        boundsZPoints[1] = minZ;

        boundsXPoints[2] = minX;
        boundsYPoints[2] = maxY;
        boundsZPoints[2] = minZ;

        boundsXPoints[3] = maxX;
        boundsYPoints[3] = maxY;
        boundsZPoints[3] = minZ;

        boundsXPoints[4] = minX;
        boundsYPoints[4] = minY;
        boundsZPoints[4] = maxZ;

        boundsXPoints[5] = maxX;
        boundsYPoints[5] = minY;
        boundsZPoints[5] = maxZ;

        boundsXPoints[6] = minX;
        boundsYPoints[6] = maxY;
        boundsZPoints[6] = maxZ;

        boundsXPoints[7] = maxX;
        boundsYPoints[7] = maxY;
        boundsZPoints[7] = maxZ;

        int xOff = renderData.xOff;
        int yOff = renderData.yOff;
        int zOff = renderData.zOff;

        int x1 = renderData.x1;
        int x2 = renderData.x2;
        int x3 = renderData.x3;

        int y1 = renderData.y1;
        int y2 = renderData.y2;
        int y3 = renderData.y3;

        int z1 = renderData.z1;
        int z2 = renderData.z2;
        int z3 = renderData.z3;

        int minX = renderData.minX;
        int minY = renderData.minY;

        //int maxX = renderer.getMaxX();
        //int maxY = renderer.getMaxY();

        byte[][][] groundSettings = client.getGroundSettingsArray();
        boolean fixedMode = botEnv.client.getViewSettings().getLayout() == 1;
        for (int index = 0; index < 8; index++) {
            int xPoint = boundsXPoints[index] + baseX;
            int yPoint = -(boundsYPoints[index]);
            int zPoint = boundsZPoints[index] + baseZ;
            if(baseY == 0){
                yPoint = getTileHeight(curPlane, xPoint, zPoint, planes, groundSettings) - yPoint;
            }
            else{
                yPoint = baseY - yPoint;
            }

            final int distanceToPoint = zOff + (x3 * xPoint
                    + y3 * yPoint + z3 * zPoint >> 14);

            // Here there should be done clipping if it was rendering but not needed for worldToScreen
            if (distanceToPoint == 0) { // We don't devide by zero!
                boundsScreenX[index] = -1;
                boundsScreenY[index] = -1;
                continue;
            }

            final int calcX = renderData.screenFactorX
                    * (xOff + (x1 * xPoint
                    + y1 * yPoint + z1 * zPoint >> 14)) / distanceToPoint;

            final int calcY = renderData.screenFactorY
                    * (yOff + (x2 * xPoint
                    + y2 * yPoint + z2 * zPoint >> 14)) / distanceToPoint;

            ///if (calcX >= minX && calcX <= maxX
            //        && calcY >= minY && calcY <= maxY) {
            if(fixedMode){
                // Fixed mode
                boundsScreenX[index] = calcX - minX + 4;
                boundsScreenY[index] = calcY - minY + 4;
            }
            else{            
                boundsScreenX[index] = calcX - minX;
                boundsScreenY[index] = calcY - minY;
            }
            /*}

            else{

                boundsScreenX[index] = -1;
                boundsScreenY[index] = -1;
            }*/
        }
    }

    /**
     * Note that this is not a precise check, but is the same as the client.
     * It is not 100% accurate for performance reaons!
     * @param pointX
     * @param pointY
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @return
     */
    private final boolean isPointWithinTriangle(int pointX, int pointY, int x1, int y1, int x2, int y2, int x3, int y3){
        if(pointY < y1 && pointY < y2 && pointY < y3)
            return false;
        if(pointY > y1 && pointY > y2 && pointY > y3)
            return false;
        if(pointX < x1 && pointX < x2 && pointX < x3)
            return false;
        return pointX <= x1 || pointX <= x2 || pointX <= x3;
    }

    public boolean isPointOver(int pointX, int pointY){
        calculateScreenPoints();
        for (int i = 0; i < indices1.length; i++) {
            int index1 = indices1[i];
            int index2 = indices2[i];
            int index3 = indices3[i];

            int point1X = screenPointsX[index1];
            int point1Y = screenPointsY[index1];
            int point2X = screenPointsX[index2];
            int point2Y = screenPointsY[index2];
            int point3X = screenPointsX[index3];
            int point3Y = screenPointsY[index3];
            if(isPointWithinTriangle(pointX, pointY, point1X, point1Y, point2X, point2Y, point3X, point3Y)){
                return true;
            }
        }
        return false;
    }

    public boolean isPointOver(int pointX, int pointY, boolean justCheckBoundingBox){
        calculateBoundingBox();

        int minX = 9999999;
        int minY = 9999999;
        int maxX = -9999999;
        int maxY = -9999999;
        for(int i = 0; i < boundsScreenX.length; i++){
            int x = boundsScreenX[i];
            int y = boundsScreenY[i];

            if(x < minX){
                minX = x;
            }
            else if(x > maxX){
                maxX = x;
            }

            if(y < minY){
                minY = y;
            }
            else if(y > maxY){
                maxY = y;
            }
        }

        if(pointX < minX || pointX > maxX || pointY < minY || pointY > maxX){
            return false;
        }

        if(justCheckBoundingBox){
            return true;
        }

        calculateScreenPoints();
        for (int i = 0; i < indices1.length; i++) {
            int index1 = indices1[i];
            int index2 = indices2[i];
            int index3 = indices3[i];

            int point1X = screenPointsX[index1];
            int point1Y = screenPointsY[index1];
            int point2X = screenPointsX[index2];
            int point2Y = screenPointsY[index2];
            int point3X = screenPointsX[index3];
            int point3Y = screenPointsY[index3];
            if(isPointWithinTriangle(pointX, pointY, point1X, point1Y, point2X, point2Y, point3X, point3Y)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a random point somewhere inside the model. (The actual point is on the surface of the model)
     * @return
     */
    public Point3D createSeed(){
        applyTransforms();
        if(indices1.length == 0){
            return new Point3D(0, 0, 0);
        }
        int i = Calculations.random(0, indices1.length);
        int index1 = indices1[i];
        int index2 = indices2[i];
        int index3 = indices3[i];

        int point1X = xPoints[index1];
        int point1Y = yPoints[index1];
        int point1Z = zPoints[index1];

        int point2X = xPoints[index2]-point1X;
        int point2Y = yPoints[index2]-point1Y;
        int point2Z = zPoints[index2]-point1Z;

        int point3X = xPoints[index3]-point1X;
        int point3Y = yPoints[index3]-point1Y;
        int point3Z = zPoints[index3]-point1Z;

        double a = Math.random();
        double b = Math.random();
        if(a+b > 1){
            a = 1-a;
            b = 1-b;
        }
        double c = 1-a-b;
        return new Point3D(point1X+(int)(b*point2X+c*point3X), point1Y+(int)(b*point2Y+c*point3Y), point1Z+(int)(b*point2Z+c*point3Z));
    }

    protected void calculateScreenPoints() {
        applyTransforms();
        Client client = botEnv.client;
        RenderData renderData = botEnv.renderData;
        if (renderData == null) {
            return;
        }

        Plane[] planes = client.getPlaneArray();
        if (planes == null) {
            return;
        }

        updateBasePos();

        int curPlane = client.getCurrentPlane();
        int xOff = renderData.xOff;
        int yOff = renderData.yOff;
        int zOff = renderData.zOff;

        int x1 = renderData.x1;
        int x2 = renderData.x2;
        int x3 = renderData.x3;

        int y1 = renderData.y1;
        int y2 = renderData.y2;
        int y3 = renderData.y3;

        int z1 = renderData.z1;
        int z2 = renderData.z2;
        int z3 = renderData.z3;

        int minX = renderData.minX;
        int minY = renderData.minY;

        //int maxX = renderer.getMaxX();
        //int maxY = renderer.getMaxY();

        byte[][][] groundSettings = client.getGroundSettingsArray();
        boolean fixedMode = botEnv.client.getViewSettings().getLayout() == 1;
        int length = xPoints.length;
        if(yPoints.length < length){
            length = yPoints.length;
        }
        if(zPoints.length < length){
            length = zPoints.length;
        }
        for (int index = 0; index < length; index++) {
            int xPoint = xPoints[index] + baseX;
            int yPoint = -(yPoints[index]);
            int zPoint = zPoints[index] + baseZ;
            if(baseY == 0){
                yPoint = getTileHeight(curPlane, xPoint, zPoint, planes, groundSettings) - yPoint;
            }
            else{
                yPoint = baseY - yPoint;
            }

            final int distanceToPoint = zOff + (x3 * xPoint
                    + y3 * yPoint + z3 * zPoint >> 14);

            // Here there should be done clipping if it was rendering but not needed for worldToScreen
            if (distanceToPoint == 0) { // We don't devide by zero!
                screenPointsX[index] = -1;
                screenPointsY[index] = -1;
                continue;
            }

            final int calcX = renderData.screenFactorX
                    * (xOff + (x1 * xPoint
                    + y1 * yPoint + z1 * zPoint >> 14)) / distanceToPoint;

            final int calcY = renderData.screenFactorY
                    * (yOff + (x2 * xPoint
                    + y2 * yPoint + z2 * zPoint >> 14)) / distanceToPoint;

            //if (calcX >= minX && calcX <= maxX
            //       && calcY >= minY && calcY <= maxY) {
            if(fixedMode){
                screenPointsX[index] = calcX - minX + 4;
                screenPointsY[index] = calcY - minY + 4;
            }
            else{
                screenPointsX[index] = calcX - minX;
                screenPointsY[index] = calcY - minY;
            }
            /*}
            else{
                screenPointsX[index] = -1;
                screenPointsY[index] = -1;
            }*/
        }

    }

    /**
     * Optimized getTileHeight to minimize reflection usage in performance operations
     *
     * @param plane
     * @param X
     * @param Y
     * @param planes
     * @param groundSettings
     * @return
     */
    protected int getTileHeight(int plane, int X, int Y, Plane[] planes, byte[][][] groundSettings) {
        if (planes == null)
            return 0;
        int x1 = X >> 9;
        int y1 = Y >> 9;
        if (x1 < 0 || y1 < 0 || x1 > 103 || y1 > 103)
            return 0;
        int x2 = X & 0x1ff;
        int y2 = Y & 0x1ff;
        int zIndex = plane;
        if (zIndex <= 3 && (groundSettings[1][x1][y1] & 2) == 2)
            zIndex++;
        final int[][] groundHeights = planes[zIndex].getTileHeights();
        if (groundHeights == null) {
            return 0;
        }

        int height1 = ((groundHeights[x1][y1] * (512 - x2)) + (groundHeights[x1 + 1][y1] * x2)) >> 9;
        int height2 = ((groundHeights[x1][(y1 + 1)] * (512 - x2)) + (groundHeights[1 + x1][(y1 + 1)] * x2)) >> 9;
        return ((height1 * (512 - y2)) + (y2 * height2)) >> 9;
    }

    /**
     * Implement this method to update the base position for calculations.
     */
    protected abstract void updateBasePos();

    protected abstract void applyTransforms();

    public Point3D getCenter(){
        int deltaX = (maxX-minX) >> 1;
        int deltaY = (maxY-minY) >> 1;
        int deltaZ = (maxZ-minZ) >> 1;

        return new Point3D(minX+deltaX, minY+deltaY, minZ+deltaZ);    
    }

    public void fillWireFrame(Graphics2D g){
        calculateScreenPoints();
        for (int i = 0; i < indices1.length; i++) {
            int index1 = indices1[i];
            int index2 = indices2[i];
            int index3 = indices3[i];

            int[] xPoints = new int[3];
            int[] yPoints = new int[3];

            xPoints[0] = screenPointsX[index1];
            yPoints[0] = screenPointsY[index1];
            xPoints[1] = screenPointsX[index2];
            yPoints[1] = screenPointsY[index2];
            xPoints[2] = screenPointsX[index3];
            yPoints[2] = screenPointsY[index3];

            g.fillPolygon(xPoints, yPoints, 3);
        }

    }

    public int getHeight(){
        return -minZ;
    }

    public int[] getXVertices() {
        applyTransforms();
        return xPoints;
    }

    public int[] getYVertices() {
        applyTransforms();
        return yPoints;
    }

    public int[] getZVertices() {
        applyTransforms();
        return zPoints;
    }


    /**
     * Gets the screen points for a model
     * @return Returns a two dimensional array with the index 0 containing the x coords, and index 1 containing the y coords. (This is done for performance reasons)
     */
    public int[][] getScreenPoints(){
        calculateScreenPoints();
        return new int[][]{screenPointsX, screenPointsY};
    }
}

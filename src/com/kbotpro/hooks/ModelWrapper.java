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



package com.kbotpro.hooks;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Nov 27, 2009
 * Time: 3:17:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelWrapper implements Model {
    private short minX;
    private short minY;
    private short minZ;

    private short maxX;
    private short maxY;
    private short maxZ;

    private int[] xPoints;
    private int[] yPoints;
    private int[] zPoints;

    private short[] indices1;
    private short[] indices2;
    private short[] indices3;

    private int vertexCount;
    private int triangleCount;

    public ModelWrapper(Model model) {
        if (model == null) {
            return;
        }
        if (!(model instanceof ModelSD)) {
            return;
        }
        minX = model.getMinX();
        minY = model.getMinY();
        minZ = model.getMinZ();

        maxX = model.getMaxX();
        maxY = model.getMaxY();
        maxZ = model.getMaxZ();

        int[] intOrg = model.getXPoints();
        this.vertexCount = intOrg.length;
        int vertexCount = this.vertexCount;
        xPoints = Arrays.copyOf(intOrg, vertexCount);
        intOrg = model.getYPoints();
        yPoints = Arrays.copyOf(intOrg, vertexCount);
        intOrg = model.getZPoints();
        zPoints = Arrays.copyOf(intOrg, vertexCount);

        short[] sOrg = model.getIndices1();
        this.triangleCount = sOrg.length;
        int triangleCount = this.triangleCount;
        indices1 = Arrays.copyOf(sOrg, triangleCount);
        sOrg = model.getIndices2();
        indices2 = Arrays.copyOf(sOrg, triangleCount);
        sOrg = model.getIndices3();
        indices3 = Arrays.copyOf(sOrg, triangleCount);
    }

    public final void updateModelData(Model model) {
        if (model == null) {
            return;
        }
        if (!(model instanceof ModelSD)) {
            return;
        }
        minX = model.getMinX();
        minY = model.getMinY();
        minZ = model.getMinZ();

        maxX = model.getMaxX();
        maxY = model.getMaxY();
        maxZ = model.getMaxZ();

        int[] intOrg = model.getXPoints();
        int vertexCount = intOrg.length;
        if (vertexCount > this.vertexCount) {
            this.vertexCount = vertexCount;
            xPoints = Arrays.copyOf(intOrg, intOrg.length);
            intOrg = model.getYPoints();
            yPoints = Arrays.copyOf(intOrg, intOrg.length);
            intOrg = model.getZPoints();
            zPoints = Arrays.copyOf(intOrg, intOrg.length);
        } else {
            this.vertexCount = vertexCount;
            System.arraycopy(intOrg, 0, xPoints, 0, vertexCount);
            intOrg = model.getYPoints();
            System.arraycopy(intOrg, 0, yPoints, 0, vertexCount);
            intOrg = model.getZPoints();
            System.arraycopy(intOrg, 0, zPoints, 0, vertexCount);
        }

        short[] sOrg = model.getIndices1();
        int triangleCount = sOrg.length;
        if (triangleCount > this.triangleCount) {
            this.triangleCount = triangleCount;
            indices1 = Arrays.copyOf(sOrg, sOrg.length);
            sOrg = model.getIndices2();
            indices2 = Arrays.copyOf(sOrg, sOrg.length);
            sOrg = model.getIndices3();
            indices3 = Arrays.copyOf(sOrg, sOrg.length);
        } else {
            this.triangleCount = triangleCount;
            System.arraycopy(sOrg, 0, indices1, 0, triangleCount);
            sOrg = model.getIndices2();
            System.arraycopy(sOrg, 0, indices2, 0, triangleCount);
            sOrg = model.getIndices3();
            System.arraycopy(sOrg, 0, indices3, 0, triangleCount);
        }

    }

    public short getMinX() {
        return minX;
    }

    public short getMinY() {
        return minY;
    }

    public short getMinZ() {
        return minZ;
    }

    public short getMaxX() {
        return maxX;
    }

    public short getMaxY() {
        return maxY;
    }

    public short getMaxZ() {
        return maxZ;
    }

    public int[] getXPoints() {
        return xPoints;
    }

    public int[] getYPoints() {
        return yPoints;
    }

    public int[] getZPoints() {
        return zPoints;
    }

    public short[] getIndices1() {
        return indices1;
    }

    public short[] getIndices2() {
        return indices2;
    }

    public short[] getIndices3() {
        return indices3;
    }
}

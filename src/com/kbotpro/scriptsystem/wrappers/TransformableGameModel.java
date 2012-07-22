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

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Nov 27, 2009
 * Time: 4:53:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransformableGameModel extends Model {
    private com.kbotpro.hooks.Model rModel;
    private Renderable renderable;

    protected int[] xOrgPoints;

    /**
     * Negated! If you want a height of 50 the value must be -50
     */
    protected int[] yOrgPoints;

    protected int[] zOrgPoints;



    public TransformableGameModel(BotEnvironment botEnv, com.kbotpro.hooks.Model rModel, Renderable renderable) {
        super(botEnv);
        this.rModel = rModel;
        this.renderable = renderable;
        xOrgPoints = rModel.getXPoints();
        yOrgPoints = rModel.getYPoints();
        zOrgPoints = rModel.getZPoints();

        minX = rModel.getMinX();
        minY = rModel.getMinY();
        minZ = rModel.getMinZ();

        maxX = rModel.getMaxX();
        maxY = rModel.getMaxY();
        maxZ = rModel.getMaxZ();

        indices1 = rModel.getIndices1();
        indices2 = rModel.getIndices2();
        indices3 = rModel.getIndices3();

        screenPointsX = new int[xOrgPoints.length];
        screenPointsY = new int[xOrgPoints.length];
    }

    /**
     * Implement this method to update the base position for calculations.
     */
    protected void updateBasePos() {
        baseX = renderable.getRegionalX();
        baseZ = renderable.getRegionalY();
        baseY = renderable.getYOffset();
    }

    protected void applyTransforms() {
        int rsURotation = renderable.getOrientationYAxis();
        if(rsURotation == 0){
            xPoints = xOrgPoints;
            yPoints = yOrgPoints;
            zPoints = zOrgPoints;
            return;
        }

        int length = xOrgPoints.length;
        xPoints = new int[length];
        yPoints = new int[length];
        zPoints = new int[length];

        double radY = rsURotation * 6.283185307179586;
        radY /= 16383D;

        // Create matrix
        double[][] mat = new double[][]{
                {Math.cos(radY), 0, Math.sin(radY)},
                {0, 1, 0},
                {-Math.sin(radY), 0, Math.cos(radY)},
        };

        for(int i = 0; i < length; i++){
            int xOrg = xOrgPoints[i];
            int yOrg = yOrgPoints[i];
            int zOrg = zOrgPoints[i];

            xPoints[i] = (int)(mat[0][0]*xOrg+mat[0][1]*yOrg+mat[0][2]*zOrg);
            yPoints[i] = (int)(mat[1][0]*xOrg+mat[1][1]*yOrg+mat[1][2]*zOrg);
            zPoints[i] = (int)(mat[2][0]*xOrg+mat[2][1]*yOrg+mat[2][2]*zOrg);
        }
    }


    public Renderable getRenderable() {
        return renderable;
    }

    public void setRenderable(GameObject renderable) {
        this.renderable = renderable;
    }
}

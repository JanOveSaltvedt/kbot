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
 * Class that contains stolen in game models.
 * The constructor for this method is rather slow so please cache the object.
 * Rather reset the GameObject for similar looking objects than constructing
 * the class over again for the same type of object.
 */
public class GameModel extends Model {
    private com.kbotpro.hooks.Model rModel;
    private Renderable renderable;


    public GameModel(BotEnvironment botEnv, com.kbotpro.hooks.Model rModel, Renderable renderable) {
        super(botEnv);
        this.rModel = rModel;
        this.renderable = renderable;
        xPoints = rModel.getXPoints();
        yPoints = rModel.getYPoints();
        zPoints = rModel.getZPoints();

        minX = rModel.getMinX();
        minY = rModel.getMinY();
        minZ = rModel.getMinZ();

        maxX = rModel.getMaxX();
        maxY = rModel.getMaxY();
        maxZ = rModel.getMaxZ();

        indices1 = rModel.getIndices1();
        indices2 = rModel.getIndices2();
        indices3 = rModel.getIndices3();

        screenPointsX = new int[xPoints.length];
        screenPointsY = new int[xPoints.length];
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

    }

    public Renderable getRenderable() {
        return renderable;
    }

    public void setRenderable(GameObject renderable) {
        this.renderable = renderable;
    }
}

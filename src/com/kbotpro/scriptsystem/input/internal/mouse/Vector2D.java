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



package com.kbotpro.scriptsystem.input.internal.mouse;

import java.awt.*;

/**
 * A 2 dimensional vector class used for applying forces to a mouse move
 */
public class Vector2D {
    public double xUnits;
    public double yUnits;

    public void draw(Graphics2D g, int posX, int posY) {
        Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(2));
        g.drawLine(posX, posY, posX + (int) xUnits, posY + (int) yUnits);
        g.setStroke(stroke);
    }

    public Vector2D sum(Vector2D vector) {
        Vector2D out = new Vector2D();
        out.xUnits = xUnits + vector.xUnits;
        out.yUnits = xUnits + vector.yUnits;
        return out;
    }

    public void add(Vector2D vector) {
        xUnits += vector.xUnits;
        yUnits += vector.yUnits;
    }

    public Vector2D multiply(double factor) {
        Vector2D out = new Vector2D();
        out.xUnits = xUnits * factor;
        out.yUnits = yUnits * factor;
        return out;
    }

    public double getLength() {
        return Math.sqrt(xUnits * xUnits + yUnits * yUnits);
    }

    public double getAngle() {
        return Math.atan2(yUnits, xUnits);
    }
}

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

package com.kbot2.scriptable.methods.wrappers;

import com.kbot2.scriptable.methods.interfaces.WorldObject;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 22.mar.2009
 * Time: 20:04:34
 */
public class Tile implements WorldObject{
    private int posX;
    private int posY;
    private int plane = -999;

    public Tile(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Tile(int posX, int posY, int plane){
        this.posX = posX;
        this.posY = posY;
        this.plane = plane;
    }

    public Tile(com.kbotpro.scriptsystem.wrappers.Tile location) {
        this.posX = location.getX();
        this.posY = location.getY();
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public int getPlane() {
        return plane;
    }

    public void setPlane(int plane) {
        this.plane = plane;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int distanceTo(WorldObject wo) {
        Tile tile = wo.getLocation();
        return (int) Math.hypot(tile.posX - this.posX, tile.posY - this.posY);
    }

    public double distanceToPrecise(WorldObject wo) {
        Tile tile = wo.getLocation();
        return Math.hypot(tile.posX - this.posX, tile.posY - this.posY);
    }

    public String toString(){
        if(plane == -999){
            return "[x=" + posX + ",y="+posY+"]";
        }
        else{
            return "[x=" + posX + ",y="+posY+"plane="+plane+"]";
        }
    }

    public boolean equals(Tile tile) {
        return posX == tile.posX && posY == tile.posY && plane == tile.plane;
    }

    public Tile getLocation() {
        return this;
    }
}

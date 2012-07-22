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

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 17, 2009
 * Time: 12:16:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RenderVars {
    public float getXOff();
    public float getYOff();
    public float getZOff();

    public float getX1();
    public float getY1();
    public float getZ1();

    public float getX2();
    public float getY2();
    public float getZ2();

    public float getX3();
    public float getY3();
    public float getZ3();
}

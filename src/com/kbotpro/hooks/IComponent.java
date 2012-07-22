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
 * Time: 12:18:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IComponent {
    public int getElementID();
    public int getElementStackSize();
    public String getElementName();

    public int getX();
    public int getY();
    public int getHeight();
    public int getWidth();

    public String[] getActions();
    public String getText();
    public int getTextureID();
    public int getTextColor();
    public int getModelZoom();
    public IComponent[] getChildren();

    public int getVisibleArrayIndex();

    public int getMasterX();
    public int getMasterY();

    public int getSpecialType();
    public int getUID();

    public int getModelID();

    public int getModelType();

    public int getLoopCycleStatus();
    public boolean isElementVisible();
}

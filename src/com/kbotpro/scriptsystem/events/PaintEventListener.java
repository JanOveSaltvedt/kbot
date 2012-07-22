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



package com.kbotpro.scriptsystem.events;

//import com.kbotpro.scriptsystem.graphics.Graphics;

import com.kbotpro.scriptsystem.graphics.KGraphics;

import java.util.EventListener;
import java.awt.*;

/**
 * PaintEventListener
 */
public interface PaintEventListener extends EventListener {
    /**
     * Gets called when the client updates it graphics.
     * Please do not do anything extremely time consuming in here as it will make the fps go low.
     * @param g Graphics to paint on
     */
    public void onRepaint(Graphics g); //alright, so the
}

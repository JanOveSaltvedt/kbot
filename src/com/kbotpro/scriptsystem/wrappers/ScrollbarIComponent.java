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

package com.kbotpro.scriptsystem.wrappers;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.input.internal.mouse.EventFactory;
import com.kbotpro.scriptsystem.wrappers.IComponent;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Apples
 * Date: Jan 9, 2010
 * Time: 7:52:05 PM
 */
public class ScrollbarIComponent {
	private final BotEnvironment botEnv;

	private final int parentID;
	private final int ID;
	private final IComponent scroller;
	private final IComponent upButton;
	private final IComponent downButton;
	private final int yPos;
	private final int height;
	private final int scrollSize;

	public ScrollbarIComponent(BotEnvironment botEnv, int parentID, int ID, int scroller, int upButton, int downButton) {
		this.botEnv = botEnv;
		this.parentID = parentID;
		this.ID = ID;
		IComponent[] scrollChildren = botEnv.interfaces.getComponent(parentID, ID).getChildren();
		this.scroller = scrollChildren[scroller];
		this.upButton = scrollChildren[upButton];
		this.downButton = scrollChildren[downButton];
		this.yPos = this.upButton.getAbsoluteY();
		this.height = (this.downButton.getAbsoluteY() + 16) - yPos;
		this.scrollSize = this.height - this.scroller.getHeight();
	}

    private Point getPoint(IComponent frame) {
		int scrollToY = frame.getRelativeY() - frame.getHeight();
		int length = ((scrollSize + height) - scrollToY) - (scroller.getRelativeY() - 16);
		int randY = scrollToY - frame.getRelativeY();
		System.out.println(length + " " + scrollSize + " " + scrollToY + " " + height + " " + randY + " " + (scroller.getRelativeY() - 16));
		return new Point(botEnv.mouse.getMousePos().x + random(-7, 7), botEnv.mouse.getMousePos().y + length + random(-randY, randY));
	}

    /*
	public void scrollTo(int childID) {
		IComponent magicIcon = botEnv.interfaces.getComponent(parentID, childID);
		botEnv.mouse.moveMouse(scroller.getCenter().x + random(-4, 4), scroller.getCenter().y + random(-13, 13));
		EventFactory e = botEnv.mouse.holdMouse(true);
		botEnv.mouse.moveMouse(getPoint(magicIcon));
		botEnv.mouse.releaseMouse(true, e);
	} */

	public int random(int min, int max){
        return ((int) (Math.random() * (max - min))) + min;
    }
}

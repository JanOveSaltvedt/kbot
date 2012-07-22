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

package com.kbot2.scriptable.methods.input;

import com.kbot2.bot.BotEnvironment;
import com.kbotpro.hooks.Client;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;

import static com.kbot2.scriptable.methods.Calculations.random;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 5:07:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mouse {
    private final BotEnvironment botEnv;

    public Mouse(BotEnvironment botEnv) {
        this.botEnv = botEnv;
    }

    public void moveMouse(int x, int y, int randomX, int randomY) {
        botEnv.proBotEnvironment.mouse.moveMouse(x + random(-randomX, randomX + 1), y + random(-randomY, randomY + 1));
    }

    public void moveMouse(int x, int y) {
        moveMouse(x, y, 0, 0);
    }

    public Client getClient() {
        return botEnv.getClient();
    }

    public void setMouseSpeed(double speed) {
        //botEnv.mouseSpeed = speed;
        // TODO implement setMouseSpeed
    }

    public void pressMouse(int x, int y, boolean button) {
        if (x < 0 || y < 0 || x > 756 || y > 503)
            return;
        botEnv.proBotEnvironment.dispatchEvent(new MouseEvent((Applet) getClient(),
                MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, x, y,
                1, false, button ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3));

    }

    public void releaseMouse(int x, int y, boolean button) {
        if (x < 0 || y < 0 || x > 756 || y > 503)
            return;

        Client client = getClient();
        botEnv.proBotEnvironment.dispatchEvent(new MouseEvent((Applet) client,
                MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, x, y,
                1, false, button ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3));

        botEnv.proBotEnvironment.dispatchEvent(new MouseEvent((Applet) client,
                MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x,
                y, 1, false, button ? MouseEvent.BUTTON1
                        : MouseEvent.BUTTON3));

    }

    /**
	 * Moves the mouse with randomness and clicks.
	 *
	 * @param p
	 * @param randomX
	 * @param randomY
	 * @param button
	 */
	public void clickMouse(Point p, int randomX, int randomY, boolean button) {
		moveMouse(p, randomX, randomY);
		botEnv.methods.sleep(random(50, 150));
		clickMouse(button);
	}

	/**
	 * Moves the mouse and clicks at the given position.
	 *
	 * @param p
	 * @param button
	 */
	public void clickMouse(Point p, boolean button) {
		clickMouse(p, 0, 0, button);
	}

    /**
	 * Moves the mouse with randomness
	 *
	 * @param p
	 * @param randomX
	 * @param randomY
	 */
	public void moveMouse(Point p, int randomX, int randomY) {
		moveMouse(p.x, p.y, randomX, randomY);
	}

    private void clickMouse(int x, int y, boolean leftClick) {
		pressMouse(x, y, leftClick);
		botEnv.methods.sleep(random(0, 70));
		releaseMouse(x, y, leftClick);
	}

	public void clickMouse(boolean button) {
		Client client = getClient();
        Point point = botEnv.proBotEnvironment.game.getMousePos();
        clickMouse(point.x, point.y, button);
	}

    public void dragMouse(Point destination, int randomX, int randomY) {
		/*Client client = getClient();
		int thisX = client.getMouseX(), thisY = client.getMouseY();
		pressMouse(thisX, thisY, true);
		botEnv.methods.sleep(10, 50);
		moveMouse(destination, randomX, randomY);
		thisX = client.getMouseX();
		thisY = client.getMouseY();
		botEnv.methods.sleep(10, 50);
		clickMouse(thisX, thisY, true);*/
        // TODO implement dragging
	}

	public void dragMouse(Point destination){
		dragMouse(destination, 0, 0);
	}

    /**
	 * Gets the mouse position
	 *
	 * @return Point with coords.
	 */
	public Point getMousePos() {
		Client client = getClient();
		return botEnv.proBotEnvironment.game.getMousePos();
	}

}

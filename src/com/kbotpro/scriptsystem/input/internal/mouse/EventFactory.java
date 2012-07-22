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

import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.bot.BotEnvironment;

import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Nov 12, 2009
 * Time: 3:39:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventFactory extends ModuleConnector {
    private BotEnvironment botEnv;

    public EventFactory(BotEnvironment botEnv) {
        super(botEnv);
        this.botEnv = botEnv;
    }

    public MouseEvent createMoveMouse(int x, int y) {
        return new MouseEvent(getSource(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false, MouseEvent.NOBUTTON);
    }

    public Component getSource() {
        return (Component) botEnv.client;
    }

    public MouseEvent createMousePress(int x, int y, boolean button) {
        return new MouseEvent(getSource(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, x, y, 1, false, button ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
    }

    public MouseEvent createMouseRelease(int x, int y, boolean button) {
        return new MouseEvent(getSource(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, x, y, 1, false, button ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
    }

    public MouseEvent createMouseClicked(int x, int y, boolean button) {
        return new MouseEvent(getSource(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x, y, 1, false, button ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
    }

    public MouseEvent createMouseDragged(int x, int y, boolean button) {
        return new MouseEvent(getSource(), MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), 0, x, y, 1, false, button ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);  
    }
}

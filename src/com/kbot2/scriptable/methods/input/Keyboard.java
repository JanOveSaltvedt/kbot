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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.kbot2.scriptable.methods.Calculations.random;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 5:18:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Keyboard {
    private BotEnvironment botEnv;

    public Keyboard(BotEnvironment botEnv) {
        this.botEnv = botEnv;
    }

    private Client getClient() {
        return botEnv.getClient();
    }

    public void sendKeys(String text, boolean pressEnter) {
        botEnv.proBotEnvironment.keyboard.writeText(text, pressEnter);
    }

    public void holdKey(int keyCode, int millis) {
        KeyEvent keyEvent;
        // Press key
        keyEvent = new KeyEvent(((Applet) getClient()), KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, keyCode, (char) keyCode);
        botEnv.proBotEnvironment.dispatchEvent(keyEvent);

        if (millis > 500) {
            keyEvent = new KeyEvent(((Applet) getClient()), KeyEvent.KEY_PRESSED,
                    System.currentTimeMillis() + 500, 0, keyCode,
                    (char) keyCode);
            botEnv.proBotEnvironment.dispatchEvent(keyEvent);
            int ms2 = millis - 500;
            for (int i = 37; i < ms2; i += random(20, 40)) {
                keyEvent = new KeyEvent(((Applet) getClient()),
                        KeyEvent.KEY_PRESSED, System.currentTimeMillis() + (i)
                                + 500, 0, keyCode, (char) keyCode);
                botEnv.proBotEnvironment.dispatchEvent(keyEvent);
            }
        }
        int delay2 = millis + random(-30, 30);
        // release
        keyEvent = new KeyEvent(((Applet) getClient()), KeyEvent.KEY_RELEASED,
                System.currentTimeMillis() + delay2, 0, keyCode, (char) keyCode);
        botEnv.proBotEnvironment.dispatchEvent(keyEvent);
    }

    public void pressKey(final char ch) {
		KeyEvent ke;
		ke = new KeyEvent((Applet) getClient(), KeyEvent.KEY_PRESSED, System
				.currentTimeMillis(), 0, ch, getKeyChar(ch));
		botEnv.proBotEnvironment.dispatchEvent(ke);
	}

    public void releaseKey(final char ch) {
		KeyEvent ke;
		ke = new KeyEvent((Applet)getClient(), KeyEvent.KEY_RELEASED, System
				.currentTimeMillis(), InputEvent.ALT_DOWN_MASK, ch,
				getKeyChar(ch));
		botEnv.proBotEnvironment.dispatchEvent(ke);
	}

    private char getKeyChar(char c) {
        int i = (c);
        if (i >= 36 && i <= 40) {
            return KeyEvent.VK_UNDEFINED;
        } else {
            return c;
        }
    }
}

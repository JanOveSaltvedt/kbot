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



package com.kbotpro.scriptsystem.input.internal.keyboard;

import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.bot.BotEnvironment;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Nov 6, 2009
 * Time: 2:19:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventFactory extends ModuleConnector {
    public EventFactory(BotEnvironment botEnv) {
        super(botEnv);
    }

    public KeyEvent createKeyPressed(char ch, int code, int location, int mask){
        return new KeyEvent(getSource(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), mask, code,
                            getKeyChar(ch), location);
    }

    public KeyEvent createKeyReleased(char ch, int code, int location, int mask){
        return new KeyEvent(getSource(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), mask, code,
                            getKeyChar(ch), location);
    }

    public KeyEvent createKeyTyped(char ch, int code, int location, int mask){
        return new KeyEvent(getSource(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), mask,
                            code, ch, location);
    }

    private char getKeyChar(char c) {
        int i = (c);
        if (i >= 36 && i <= 40) {
            return KeyEvent.VK_UNDEFINED;
        } else {
            return c;
        }
    }

    public void sendKey(final char ch) {
            boolean shift = false;
            int code = ch;
            if (ch >= 'a' && ch <= 'z') {
                code -= 32;
            } else if (ch >= 'A' && ch <= 'Z') {
                shift = true;
            }
            KeyEvent ke;
            if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT
                    || code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
                ke = createKeyPressed(ch, code, KeyEvent.KEY_LOCATION_STANDARD, 0);
                dispatchEvent(ke);
                sleep(25, 220);
                ke = createKeyReleased(ch, code, KeyEvent.KEY_LOCATION_STANDARD, 0);
                dispatchEvent(ke);
            } else {
                if (!shift) {
                    ke = createKeyPressed(ch, code, KeyEvent.KEY_LOCATION_STANDARD, 0);
                    dispatchEvent(ke);
                    // Event Typed
                    ke = createKeyTyped(ch, 0, KeyEvent.KEY_LOCATION_UNKNOWN, 0);
                    dispatchEvent(ke);

                    // Event Released
                    sleep(25, 220);
                    ke = createKeyReleased(ch, code, KeyEvent.KEY_LOCATION_STANDARD, 0);
                    dispatchEvent(ke);
                } else {
                    // Event Pressed for shift key
                    ke = createKeyPressed((char) KeyEvent.VK_UNDEFINED, KeyEvent.VK_SHIFT, KeyEvent.KEY_LOCATION_LEFT,
                            InputEvent.SHIFT_DOWN_MASK);
                    dispatchEvent(ke);

                    // Event Pressed for char to send
                    ke = createKeyPressed(ch, code, KeyEvent.KEY_LOCATION_STANDARD, InputEvent.SHIFT_DOWN_MASK);
                    dispatchEvent(ke);
                    // Event Typed for char to send
                    ke = createKeyTyped(ch, 0, KeyEvent.KEY_LOCATION_UNKNOWN, InputEvent.SHIFT_DOWN_MASK);
                    dispatchEvent(ke);
                    // Event Released for char to send
                    sleep(25, 220);
                    ke = createKeyReleased(ch, code, KeyEvent.KEY_LOCATION_STANDARD, InputEvent.SHIFT_DOWN_MASK);
                    dispatchEvent(ke);

                    // Event Released for shift key
                    sleep(25, 100);
                    ke = createKeyReleased((char) KeyEvent.VK_UNDEFINED, KeyEvent.VK_SHIFT, KeyEvent.KEY_LOCATION_LEFT,
                            InputEvent.SHIFT_DOWN_MASK);
                    dispatchEvent(ke);
                }
            }
        }

    public Component getSource() {
        return (Component) botEnv.client;
    }

    public void dispatchEvent(KeyEvent ke){
        botEnv.dispatchEvent(ke);
    }

    public void sendKeyPressedEvent(char ch) {
        int code = ch;
        if (ch >= 'a' && ch <= 'z') {
            code -= 32;
        }
        KeyEvent ke = createKeyPressed(ch, code, KeyEvent.KEY_LOCATION_STANDARD, 0);
        dispatchEvent(ke);
    }

    public void sendKeyReleasedEvent(char ch) {
        int code = ch;
        if (ch >= 'a' && ch <= 'z') {
            code -= 32;
        }
        KeyEvent ke = createKeyReleased(ch, code, KeyEvent.KEY_LOCATION_STANDARD, 0);
        dispatchEvent(ke);
    }
}

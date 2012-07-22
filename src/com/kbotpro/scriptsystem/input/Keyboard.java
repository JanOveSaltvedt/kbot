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



package com.kbotpro.scriptsystem.input;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.input.jobs.TextInputJob;
import com.kbotpro.scriptsystem.input.jobs.HoldKeyJob;
import com.kbotpro.scriptsystem.input.callbacks.TextInputCallback;
import com.kbotpro.scriptsystem.input.callbacks.HoldKeyCallback;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.applet.Applet;

/**
 * Class to handle keyboard input
 */
public class Keyboard extends ModuleConnector {
    public Keyboard(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Creates a text input job, it does not start it. You can do that by doing textInputJob.start();
     * @param textToWrite The string to write
     * @param pressEnter Shall it press enter after 
     * @param textInputCallback the textInputCallback. May be null, but it obviously wont do callbacks then.
     * @return The textInputJob created.
     */
    public TextInputJob createTextInputJob(String textToWrite, boolean pressEnter, TextInputCallback textInputCallback){
        TextInputJob textInputJob = new TextInputJob(botEnv, textToWrite);
        textInputJob.setCallback(textInputCallback);
        textInputJob.setPressEnter(pressEnter);
        return textInputJob;
    }

    /**
     * Creates a text input job, it does not start it. You can do that by doing textInputJob.start();
     * @param textToWrite The string to write
     * @param pressEnter Shall it press enter after
     * @return The textInputJob created.
     */
    public TextInputJob createTextInputJob(String textToWrite, boolean pressEnter){
        return createTextInputJob(textToWrite, pressEnter, null);
    }

    /**
     * Creates a text input job, it does not start it. You can do that by doing textInputJob.start();
     * This does not press enter after writing the text
     * @param textToWrite The string to write
     * @return The textInputJob created.
     */
    public TextInputJob createTextInputJob(String textToWrite){
        return createTextInputJob(textToWrite, false, null);
    }

    /**
     * Writes the text and returns after it is done writing.
     * @param textToWrite The string to write
     * @param pressEnter Shall it press enter after
     */
    public void writeText(String textToWrite, boolean pressEnter){
        TextInputJob textInputJob = createTextInputJob(textToWrite, pressEnter);
        textInputJob.start();
        textInputJob.join();
    }

    /**
     * Writes the text and returns after it is done writing.
     * This does not press enter after returning.
     * @param textToWrite The string to write
     */
    public void writeText(String textToWrite){
        writeText(textToWrite, false);
    }

    /**
     * Creates a HoldKeyJob
     * @param key what key to hold
     * @param holdKeyCallback The callback : NOT NULL as it wont ever quit then
     * @return the HoldKeyJob created.
     */
    public HoldKeyJob createHoldKeyJob(int key, HoldKeyCallback holdKeyCallback){
        return new HoldKeyJob(botEnv, key, holdKeyCallback);
    }

}



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



package com.kbotpro.scriptsystem.input.jobs;

import com.kbotpro.scriptsystem.input.callbacks.TextInputCallback;
import com.kbotpro.bot.BotEnvironment;

import java.awt.event.KeyEvent;

/**
 * Basic keyboard job that simply writes a given text to
 */
public class TextInputJob extends KeyboardJob {
    private String text;
    private String written = "";
    private TextInputCallback callback;
    private boolean pressEnter = false;

    public TextInputJob(BotEnvironment botEnv, String text) {
        super(botEnv);
        this.text = text;
        runnable = new Runnable() {
            public void run() {
                sendKeys(TextInputJob.this.text, pressEnter);
            }
        };

    }

    private void sendKeys(String text, boolean pressEnter) {
        char[] chs = text.toCharArray();
        for (char element : chs) {
            if(cancelled){
                return;
            }
            if((byte)element == -96){ // space fix for rs client
                element = KeyEvent.VK_SPACE;
            }
            eventFactory.sendKey(element);
            written += element;
            if(callback != null){
                callback.keyTyped(element, this);
            }
            sleep(random(50, 150));
        }
        if (pressEnter) {
            eventFactory.sendKey((char) KeyEvent.VK_ENTER);
        }
        if(callback != null){
            callback.onFinished(this);
        }
    }

    /**
     * Sets the callback to use.
     * If you don't provide a callback or provide null it will not call any callbacks.
     * @param callback
     */
    public void setCallback(TextInputCallback callback) {
        this.callback = callback;
    }

    /**
     * If you set this to true the bot will press enter after typing the message.
     * @param pressEnter
     */
    public void setPressEnter(boolean pressEnter) {
        this.pressEnter = pressEnter;
    }

    /**
     * Gets the text posted so far
     * @return
     */
    public String getTypedText(){
        return written;
    }

    /**
     * Types a character at once and returns.
     * Used to add characters to text already typed
     * @param c the character to type
     */
    public void typeChar(char c){
        if(future == null || future.isDone()){
            throw new IllegalStateException("This KeyboardJob is done executing (or has not started) and can therefore not type any keys");
        }
        eventFactory.sendKey(c);
    }



    protected void onCancelled() {
        callback.onFinished(this);
    }

    protected void onStart() {
        
    }
}

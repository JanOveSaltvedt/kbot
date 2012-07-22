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

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.input.callbacks.HoldKeyCallback;

/**
 * Class to hold keys.
 */
public class HoldKeyJob extends KeyboardJob {
    private int key;
    private HoldKeyCallback callback;
    private boolean done = false;
    private boolean releasedKey = false;

    public HoldKeyJob(BotEnvironment botEnv, int key, HoldKeyCallback holdKeyCallback) {
        super(botEnv);
        this.key = key;
        this.callback = holdKeyCallback;
        runnable = new Runnable() {
            public void run() {
                long lastSent = System.currentTimeMillis();
                eventFactory.sendKeyPressedEvent((char) HoldKeyJob.this.key);
                while(!done){
                    callback.update(HoldKeyJob.this);
                    HoldKeyJob.this.sleep(10, 22);
                    if(System.currentTimeMillis() - lastSent > 38){
                        eventFactory.sendKeyPressedEvent((char) HoldKeyJob.this.key);
                    }
                }
                if(!releasedKey){
                    eventFactory.sendKeyReleasedEvent((char) HoldKeyJob.this.key);
                }
                releasedKey = true;
                callback.onFinished(HoldKeyJob.this);
            }
        };
    }

    public void stopHolding(){
        done = true;
    }

    protected void onCancelled() {
        done = true;
        callback.onFinished(this);
        if(!releasedKey){
            eventFactory.sendKeyReleasedEvent((char) HoldKeyJob.this.key);
        }
    }

    protected void onStart() {
        done = false;
        releasedKey = false;
    }
}

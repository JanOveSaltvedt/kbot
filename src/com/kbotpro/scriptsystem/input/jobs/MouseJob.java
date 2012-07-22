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

import com.kbotpro.scriptsystem.input.internal.mouse.EventFactory;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.hooks.Mouse;

import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Nov 21, 2009
 * Time: 3:11:16 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MouseJob extends Job {
    protected EventFactory eventFactory;
    protected Mouse virtualMouse;

    private int virtalMouseX;
    private int virtualMouseY;

    public MouseJob(EventFactory eventFactory, BotEnvironment botEnvironment) {
        super(botEnvironment);
        this.eventFactory = eventFactory;
        virtalMouseX = getMouse().getMouseX();
        virtualMouseY = getMouse().getMouseY();
        virtualMouse = new Mouse() {
            public int getMouseX() {
                return virtalMouseX;
            }

            public int getMouseY() {
                return virtualMouseY;
            }

            public int getClientMouseX() {
                return virtalMouseX;
            }

            public int getClientMouseY() {
                return virtualMouseY;
            }
        };
    }

    protected void setVirtualMousePos(int x, int y){
        virtalMouseX = x;
        virtualMouseY = y;
    }

    /**
     * Gets the internal bot mouse
     * @return
     */
    protected Mouse getMouse(){
        return botEnv.client.getMouse();
    }

    /**
     * Dispatchs an event to the game
     * @param mouseEvent
     */
    protected void dispatchEvent(MouseEvent mouseEvent){
        botEnv.dispatchEvent(mouseEvent);
    }

    
    public  abstract void stop();

    public abstract void doMouseClick(boolean button);

    public abstract void pause();

    public abstract boolean isPaused();

    public abstract void resume();
}

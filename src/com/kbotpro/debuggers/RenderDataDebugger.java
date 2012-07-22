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

package com.kbotpro.debuggers;

import com.kbotpro.bot.RenderData;
import com.kbotpro.hooks.RenderVars;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.runnable.Debugger;
import com.kbotpro.scriptsystem.wrappers.Player;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Feb 2, 2010
 * Time: 4:56:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class RenderDataDebugger extends Debugger implements PaintEventListener{
    private boolean shallRun;

    /**
     * Gets the name shown in the debugs menu
     *
     * @return String containing name
     */
    public String getName() {
        return "Render Data Debugger";
    }

    /**
     * Is called before the debugger starts to check if it can run.
     *
     * @return Returns a boolean indicating if the service can be started or not
     */
    @Override
    public boolean canStart() {
        return true;
    }

    /**
     * Is called right before the run() gets called
     */
    public void onStart() {
        shallRun = true;
    }

    /**
     * Is called to pause debugger.
     */
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * The debugger is than added to the cleanup queue and thread will be force killed if not deleted within 10 seconds.
     */
    public void stop() {
        shallRun = false;
    }

    /**
     * You should implement the main loop here.
     */
    public void run() {
        while(shallRun){
            sleep(500);
        }
    }

    /**
     * Gets called when the client updates it graphics.
     * Please do not do anything extremely time consuming in here as it will make the fps go low.
     *
     * @param g Graphics to paint on
     */
    public void onRepaint(Graphics g) {
        RenderData data = botEnv.renderData;
        g.drawString("x = {"+data.x1+","+data.x2+","+data.x3+"}", 20, 20);
        g.drawString("y = {"+data.y1+","+data.y2+","+data.y3+"}", 20, 40);
        g.drawString("z = {"+data.z1+","+data.z2+","+data.z3+"}", 20, 60);

        g.drawString("xOff = "+data.xOff, 20, 80);
        g.drawString("yOff = "+data.yOff, 20, 100);
        g.drawString("zOff = "+data.zOff, 20, 120);
    }
}

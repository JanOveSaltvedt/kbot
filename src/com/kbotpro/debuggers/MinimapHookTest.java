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

import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.runnable.Debugger;
import com.kbotpro.scriptsystem.wrappers.Tile;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: May 25, 2010
 * Time: 6:12:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class MinimapHookTest extends Debugger implements PaintEventListener{
    private boolean canRun;

    /**
     * Gets the name shown in the debugs menu
     *
     * @return String containing name
     */
    @Override
    public String getName() {
        return "Mini Map hook test";
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
    @Override
    public void onStart() {
        canRun = true;
    }

    /**
     * Is called to pause debugger.
     */
    @Override
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Is called to stop the debugger.
     * The debugger is than added to the cleanup queue and thread will be force killed if not deleted within 10 seconds.
     */
    @Override
    public void stop() {
        canRun = false;
    }

    /**
     * You should implement the main loop here.
     */
    @Override
    public void run() {
        while(canRun){
            sleep(1000);
        }
    }

    /**
     * Gets called when the client updates it graphics.
     * Please do not do anything extremely time consuming in here as it will make the fps go low.
     *
     * @param g Graphics to paint on
     */
    public void onRepaint(Graphics g) {
        Tile myPos = getLocation();
        Point p = calculations.tileToMinimap(new Tile(myPos.getX(), myPos.getY()+5));
        g.setColor(Color.RED);
        g.fillOval(p.x-2, p.y-2, 4, 4);
        g.setColor(new Color(0,0, 0, 128));
        ((Graphics2D)g).fill(calculations.getGameScreenShape());
        
    }
}

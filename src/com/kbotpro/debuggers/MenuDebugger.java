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



package com.kbotpro.debuggers;

import com.kbotpro.scriptsystem.runnable.Debugger;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import org.apache.log4j.Logger;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 19, 2009
 * Time: 4:01:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MenuDebugger extends Debugger implements PaintEventListener {
    boolean shallRun;
    /**
     * Gets the name shown in the debugs menu
     *
     * @return String containing name
     */
    public String getName() {
        return "Menu Debugger";
    }

    /**
     * Is called before the debugger starts to check if it can run.
     *
     * @return Returns a boolean indicating if the service can be started or not
     */
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
     * Is called to stop the debugger.
     * The debugger is than added to the cleanup queue and thread will be force killed if not deleted within 10 seconds.
     */
    public void stop() {
        shallRun = false;
    }

    /**
     * You should implement the main loop here.
     */
    public void run() {
        while (shallRun){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    /**
     * Gets called when the client updates it graphics.
     * Please do not do anything extremely time consuming in here as it will make the fps go low.
     *
     * @param g Graphics to paint on
     */
    public void onRepaint(Graphics g) {
        if(menu.isOpen()){
            g.setColor(Color.green);
            g.drawString("Menu is open!", 20, 60);
            Rectangle bounds = menu.getBounds();
            g.setColor(Color.red);
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            int count = menu.getItemCount();
            for(int i =  0; i < count; i++){
                int y = bounds.y+20+i*16;
                g.drawLine(bounds.x, y, bounds.x+bounds.width, y);
            }
        }
        else{
            g.setColor(Color.red);
            g.drawString("Menu is not open!", 20, 60);
        }

        g.setColor(Color.cyan);
        String[] items = menu.getMenuItems();
        int count = menu.getItemCount();
        for(int i = 0; i < count; i++){
            if(items[i] == null){
                continue;
            }
            g.drawString("["+i+"] = "+items[i],20, 80+(20*i));
        }
    }
}

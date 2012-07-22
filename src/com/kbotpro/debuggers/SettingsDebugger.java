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

import java.util.Arrays;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 21, 2009
 * Time: 6:09:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsDebugger extends Debugger {
    private boolean shallRun;
    private SettingsExplorer UI;
    int[] compares;

    /**
     * Gets the name shown in the debugs menu
     *
     * @return String containing name
     */
    public String getName() {
        return "Settings Debugger";
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
        UI = new SettingsExplorer();
        int[] insettings = this.settings.getSettings();
        compares = Arrays.copyOfRange(insettings, 0, insettings.length);
        UI.setCompareButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] insettings = settings.getSettings();
                compares = Arrays.copyOfRange(insettings, 0, insettings.length);
            }
        });
        updateData();
        UI.setVisible(true);
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
        UI.setVisible(false);
    }

    /**
     * You should implement the main loop here.
     */
    public void run() {
        while (shallRun){
            updateData();
            sleep(500);
        }
    }

    private void updateData(){
        String html = "<html><body>";
        String updatedHTML = "<html><body>";
        int[] inGameSettings = settings.getSettings();
        for(int i = 0; i < inGameSettings.length && i < compares.length; i++){
            if(compares[i] != inGameSettings[i]){
                html += "<font color=\"red\">["+i+"] = "+inGameSettings[i]+"</font><br>";
                updatedHTML += "["+i+"]  = "+inGameSettings[i]+"<br>";
            }
            else{
                html += "["+i+"]  = "+inGameSettings[i]+"<br>";
            }
        }
        html += "</body></html>";
        updatedHTML += "</body></html>";
        UI.dataLabel.setText(html);
        UI.updatedLabel.setText(updatedHTML);
    }
}

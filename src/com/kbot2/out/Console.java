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

package com.kbot2.out;

import com.kbotpro.various.StaticStorage;

import javax.swing.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 12.mar.2009
 * Time: 18:08:21
 */
public class Console {
    private static final List<String> consoleQueue = new LinkedList<String>();


    private static void writeLine(String text) {
        addToQueue(text + "\n");
    }

    private static void addToQueue(String text) {
        if (consoleQueue.size() < 400) {
            consoleQueue.add(text);
        }
        post();
    }

    private static void post() {
        JTextArea textArea = null;//StaticStorage.mainForm.globalLogArea;
        try{
            if (textArea != null) {
                while(!consoleQueue.isEmpty()){
                    String txt = consoleQueue.get(0);
                    textArea.append(txt);
                    consoleQueue.remove(0);
                }
                textArea.setCaretPosition(textArea.getText().length());
            }
        }catch (IndexOutOfBoundsException ignored){}
    }

    public static void writeLine(Object obj, String message) {
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        writeLine("["+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND)+"]" + (obj == null && !message.startsWith("[") ? "[ Unknown ]": obj == null ? "": ("[ "+obj.getClass().getSimpleName()) + " ] ") + message);
    }
}

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



package com.kbotpro.various.logSystem;

import com.kbotpro.utils.EscapeChars;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 10.sep.2009
 * Time: 17:48:13
 */
public class LogMessage {
    private String message;
    private String html;
    public LogType type;

    public LogMessage(String message, LogType type) {
        this.message = message;
        this.type = type;
    }

    public String toHtml(boolean includeTimeStamp){
        if(html != null){
            return html;
        }
        html = "";
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        if(type == LogType.important){
            html = "<font color=\"#FF6600\">";
        }
        else if(type == LogType.error){
            html = "<font color=\"red\"><b>";
        }
        String text = "";
        if(includeTimeStamp){
            text += "["+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND)+"]";
        }
        text += message;
        html += EscapeChars.forHTML(text);
        if(type == LogType.important){
            html += "</font>";
        }
        else if(type == LogType.error){
            html += "</b></font>";
        }
        return html;
    }

    public static enum LogType {
        irrelevant, normal, important, error
    }
}

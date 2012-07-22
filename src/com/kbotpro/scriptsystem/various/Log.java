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



package com.kbotpro.scriptsystem.various;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.various.logSystem.LogMessage;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 10.sep.2009
 * Time: 18:11:12
 */
public class Log {
    private BotEnvironment botEnvironment;

    public Log(BotEnvironment botEnvironment) {
        this.botEnvironment = botEnvironment;
    }

    public void log(String log){
        logNormal(log);
    }

    public void logError(String log){
        botEnvironment.botPanel.addLogMessage(new LogMessage(log, LogMessage.LogType.error));
    }

    public void logImportant(String log){
        botEnvironment.botPanel.addLogMessage(new LogMessage(log, LogMessage.LogType.important));
    }

    public void logNormal(String log){
        botEnvironment.botPanel.addLogMessage(new LogMessage(log, LogMessage.LogType.normal));
    }

    public void logIrrelevant(String log){
        botEnvironment.botPanel.addLogMessage(new LogMessage(log, LogMessage.LogType.irrelevant));
    }
}

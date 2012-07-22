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



package com.kbotpro.scriptsystem.interfaces;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.runnable.Worker;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Dec 18, 2009
 * Time: 3:48:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WorkerContainer {
    public boolean isStopped();

    public BotEnvironment getBotEnv();

    public String getName();

    public void notifyWorkerDone(Worker worker);

    public void startWorker(Worker worker);

    public Worker createWorker(Looped looped);
}

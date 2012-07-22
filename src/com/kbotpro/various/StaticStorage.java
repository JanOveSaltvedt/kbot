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



package com.kbotpro.various;

import com.kbotpro.ui.MainForm;
import com.kbotpro.servercom.ServerCom;
import com.kbotpro.servercom.UserStorage;
import com.kbotpro.handlers.AccountsManager;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 9, 2009
 * Time: 4:06:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class StaticStorage {

    public static MainForm mainForm;
    public static ServerCom serverCom;
    public static UserStorage userStorage = new UserStorage();
    public static AccountsManager accountsManager = new AccountsManager();
}

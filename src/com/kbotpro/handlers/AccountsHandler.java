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

package com.kbotpro.handlers;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.various.StaticStorage;

import javax.swing.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 2, 2010
 * Time: 8:49:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountsHandler extends ModuleConnector {
    public AccountsHandler(BotEnvironment botEnv) {
        super(botEnv);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private AccountsManager.Account account;

    /**
     * Gets the current selected account or null. If no account is selected.
     * @return
     */
    public AccountsManager.Account getSelectedAccount(){
        return account;
    }

    public void setAccount(AccountsManager.Account account) {
        this.account = account;
    }

    public boolean isAccountSelected(){
        return account != null;
    }

    /**
     * Shows an account selector and returns whether the user set an account or not.
     * @return
     */
    public boolean showAccountSelector(){
        List<AccountsManager.Account> accountsList = StaticStorage.accountsManager.getAccounts();
        AccountsManager.Account[] accounts = accountsList.toArray(new AccountsManager.Account[accountsList.size()]);
        AccountsManager.Account selAccount = (AccountsManager.Account) JOptionPane.showInputDialog(StaticStorage.mainForm, "Select an account:", "Account Select", JOptionPane.PLAIN_MESSAGE, null, accounts, null);
        setAccount(selAccount);
        if(selAccount == null){
            return false;

        }
        if(JOptionPane.showOptionDialog(StaticStorage.mainForm, "Is this a members account?", "Is this a members account?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "NO"}, "Yes") == JOptionPane.YES_OPTION){
            selAccount.membersAccount = true;
        }

        return selAccount != null;
    }
}

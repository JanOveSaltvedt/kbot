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



package com.kbotpro.servercom;

import com.kbotpro.utils.EscapeChars;

import java.util.HashMap;

/**
 * Used to store authentication data.
 * Parts of it will be sent on each request to the server.
 * @author Jan Ove Saltvedt (Kosaki)
 */
public class UserStorage {

    // Before auth server
    private String username;
    private String password; // Will be sent encrypted ofc

    // After first request

    private int userID;
    private int sessionID;
    private String sessionKey;

    private long lastUpdate;
    private boolean loggedIn;

    public HashMap<String, String> settings = new HashMap<String, String>();
    private int accessLevel;

    public final int MASK_PRO_ACCESS = 1;
    public final int MASK_CPU_SAVING_ACCESS = 2;

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUsername() {
        return EscapeChars.forXML(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean canRunProScripts(){
        return (accessLevel & MASK_PRO_ACCESS) == MASK_PRO_ACCESS;
    }

    public boolean canUseCPUSaving(){
        return (accessLevel & MASK_CPU_SAVING_ACCESS) == MASK_CPU_SAVING_ACCESS;
    }
}

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

package com.kbot2.scriptable.methods.wrappers;

import com.kbot2.bot.BotEnvironment;
import com.kbotpro.scriptsystem.wrappers.IComponent;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 3:31:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterfaceGroup extends Wrapper{
    private int index;
    private IComponent[] interfaces;

    public InterfaceGroup(IComponent[] interfaces, int index, BotEnvironment botEnv) {
        super(botEnv);
        this.index = index;
        this.interfaces = interfaces;
    }

    public Interface[] getChildren() {
        List<Interface> out = new LinkedList<Interface>();
        for (IComponent face : interfaces) {
            out.add(new Interface(botEnv, face, null, this));
        }
        return out.toArray(new Interface[1]);
    }

    /**
     * Hack for interface debugger
     *
     * @return
     */
    public String toString() {
        return "" + index;
    }

    public boolean isValid(){
        if(interfaces == null)
            return false;
        boolean[] validArray = botEnv.getClient().getValidInterfaceArray();
        if(validArray == null)
            return true; // As the interface group actually exists
        return index == -1 || validArray[index];
    }

    /**
     * gets an interface.
     *
     * @param interfaceID
     * @return may return null if interface does not exist.
     */
    public Interface getInterface(int interfaceID) {
        if (interfaceID < 0 || interfaceID >= interfaces.length)
            return null;
        return new Interface(botEnv, interfaces[interfaceID], null, this);
    }
}

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



package com.kbotpro.scriptsystem.wrappers;

import com.kbotpro.bot.BotEnvironment;

/**
 * Class wrapping an in game interface.
 * An in game interface does not actually exists, only its components actually exists.
 * Combined together the interface components builds up what we think of an interface in game.
 * Do NOT confuse this class with the Interface class in KBot2, the Interface class from KBot2 is now called IComponent
 */
public class Interface extends Wrapper{
    private IComponent[] children;
    private int interfaceID;

    public Interface(BotEnvironment botEnv, IComponent[] children, int interfaceID) {
        super(botEnv);
        this.children = children;
        this.interfaceID = interfaceID;
        for(IComponent iComponent: children){
            if(iComponent != null){
                iComponent.setParrentInterface(this);
            }
        }
    }

    /**
     * Gets an array of all the child components of this interface.
     * @return an array of IComponents
     */
    public IComponent[] getComponents(){
        return children;
    }

    /**
     * Gets an IComponent of the interface with the specified ID
     * @param ID integer containing the index of the IComponent
     * @return returns an IComponent or null if something weird happened.
     */
    public IComponent getComponent(int ID){
        if(ID < 0 || ID >= children.length){
            throw new IndexOutOfBoundsException("This interface does not contain a component on index: "+ID);
        }
        return children[ID];
    }

    /**
     * Gets the ID of the interface
     * @return
     */
    public int getID() {
        return interfaceID;
    }

    /**
     * Checks if the interface is valid.
     * @return
     */
    public boolean isValid(){
        boolean[] validArray = getClient().getValidInterfaceArray();
        return !(interfaceID < 0 || interfaceID >= validArray.length) && validArray[interfaceID];
    }
}

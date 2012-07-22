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



package com.kbotpro.scriptsystem.fetch;

import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.Interface;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.bot.BotEnvironment;

import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 20, 2009
 * Time: 4:07:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Interfaces extends ModuleConnector {
    public static final String[] TAB_NAMES = new String[] { "Combat Styles", "Stats", "Quest List",
            "Achievements", "Inventory", "Worn Equipment",
            "Prayer List", "Magic Spellbook", "Objectives", "Friends List",
            "Ignore List", "Clan Chat", "Options", "Emotes", "Music Player",
            "Notes", "Exit" };

    public Interfaces(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Gets the in game interface with the specified ID.
     * @param ID integer containing the ID of the interface
     * @return returns null if the interface does not exist or an Interface object.
     */
    public Interface getInterface(int ID){
        com.kbotpro.hooks.IComponent[][] interfaces = getClient().getIComponentArray();
        if(interfaces == null){
            return null;
        }
        if(interfaces[ID] == null){
            return null;
        }
        IComponent[] children = new IComponent[interfaces[ID].length];
        for(int i = 0; i < interfaces[ID].length; i++){
            if(interfaces[ID][i] == null){
                children[i] = null;
            }
            else{
                children[i] = new IComponent(botEnv, interfaces[ID][i], null);
            }
        }
        return new Interface(botEnv, children, ID);
    }

    /**
     * Gets an in game IComponent of an interface
     * @param intefaceID integer containing the interface ID
     * @param childID integer containing the child ID of the interface
     * @return null if interface was not found or an IComponent object.
     */
    public IComponent getComponent(int intefaceID, int childID){
        Interface anInterface = getInterface(intefaceID);
        if(anInterface == null){
            return null;
        }
        return anInterface.getComponent(childID);
    }

    /**
     * Checks if an interface exists.
     * NOTE this does not return whether the interface is shown on screen!
     * @param ID integer containing the interface
     * @return boolean containing if the interface exists in the games memory.
     */
    public boolean interfaceExists(int ID){
        return getInterface(ID) != null;
    }

    /**
     * Checks if an IComponent exists in the games memory.
     * NOTE this does not return whether the interface is shown on screen!
     * @param interfaceID integer containing the interfaceID
     * @param childID integer containting the child ID of the interface
     * @return true if the interface exists in game memory.
     */
    public boolean componentExists(int interfaceID, int childID){
        return getComponent(interfaceID, childID) != null;
    }

    /**
     * A method that searches through the interfaces for
     * specified text. If it finds the text, the interface gets
     * added to the array.
     * Useful for things like "Click here to continue".
     *
     * @author alowaniak, ported to KBot PRO by Kosaki
     * @param text The String the child interface should contain.
     * @return An array of interfaces.
     */
    public IComponent[] getInterfaces(String text) {
        text = text.toLowerCase();
        ArrayList<IComponent> out = new ArrayList<IComponent>();
        try {
            com.kbotpro.hooks.IComponent[][] iComponents = getClient().getIComponentArray();
            if(iComponents == null)
                return null;
            for (com.kbotpro.hooks.IComponent[] iComponent : iComponents) {
                if (iComponent == null)
                    continue;
                for (com.kbotpro.hooks.IComponent component : iComponent) {
                    if (component == null)
                        continue;
                    if (component.getText().toLowerCase().contains(text)) {
                        out.add(new IComponent(botEnv, component, null));
                    }
                }
            }
        } catch (NullPointerException ignored) {
            return null;
        }
        return out.toArray(new IComponent[out.size()]);
    }

    /**
     * A method that determines if you can click continue or not.
     *
     * @author alowaniak & ported to KBot PRO by Kosaki & fixed by Andrew
     * @return true when you can click continue, false otherwise
     */
    public boolean canContinue() {
        Interface[] is = botEnv.interfaces.getInterfaces();
        if(is != null || is.length == 0) {
            for(Interface i : is) {
                IComponent[] ics = i.getComponents();
                for(IComponent ic : ics) {
                    if(ic.getText().toLowerCase().contains("click here to continue") && ic.isVisible())
                        return true;
                    if (ic.hasChildren()) {
                        for (IComponent ic2 : ic.getChildren()) {
                            if (ic2.getText().toLowerCase().contains("click here to continue"))
                                return true;
                        }
                    }
                }
            }

        }

        return false;
    }

    /**
     * Gets all the interfaces in the games memory at this time.
     * @return
     */
    public Interface[] getInterfaces(){
        List<Interface> out = new ArrayList<Interface>();
        com.kbotpro.hooks.IComponent[][] interfaces = getClient().getIComponentArray();
        if(interfaces == null){
            return null;
        }
        for(int ID = 0; ID < interfaces.length; ID++){
            if(interfaces[ID] == null){
                continue;
            }
            IComponent[] children = new IComponent[interfaces[ID].length];
            for(int i = 0; i < interfaces[ID].length; i++){
                if(interfaces[ID][i] == null){
                    children[i] = null;
                }
                else{
                    children[i] = new IComponent(botEnv, interfaces[ID][i], null);
                }
            }
            out.add(new Interface(botEnv, children, ID));

        }
        return out.toArray(new Interface[out.size()]);
    }

    /*
     * Methods for finding the correct interface ID's
     */

    private int MAINUI_INDEX;
    private int MINIMAP_INDEX;
    private int[] GAME_TABS;

    private void resetInterfaceIDs(){
        MAINUI_INDEX = -1;
        MINIMAP_INDEX = -1;
        GAME_TABS = new int[17];
        Arrays.fill(GAME_TABS, -1);
    }

    private void checkForChanges(){
        int mainUIInterfaceIndex = botEnv.client.getMainUIInterfaceIndex();
        if(MAINUI_INDEX != mainUIInterfaceIndex){
            resetInterfaceIDs();
            MAINUI_INDEX = mainUIInterfaceIndex;
        }
    }

    public com.kbotpro.hooks.IComponent getCoreMinimapIComponent(){
        checkForChanges();

        com.kbotpro.hooks.IComponent[] ui = MAINUI_INDEX != -1 ? botEnv.client.getIComponentArray()[MAINUI_INDEX] : null;
        if(ui == null){
            return null;
        }

        if(MINIMAP_INDEX == -1){
            for(int i = 0; i < ui.length; i++)
                if(ui[i] != null && ui[i].getSpecialType() == 1338){
                    MINIMAP_INDEX = i;
                    break;
                }
        }

        if(MINIMAP_INDEX == -1){
            return null;
        }
        return ui[MINIMAP_INDEX];
    }

    public IComponent getGameTab(final int id) {
        checkForChanges();
        if (id < 0 || id >= GAME_TABS.length) {
            throw new IllegalArgumentException("Interfaces.getGameTab() invalid ID!");
        }

        final com.kbotpro.hooks.IComponent[] gui = MAINUI_INDEX != -1 ? botEnv.client.getIComponentArray()[MAINUI_INDEX]
                : null;
        if (gui == null) {
            return null;
        }

        if (GAME_TABS[id] == -1) {
            for (int i = 0; i < gui.length; i++) {
                if (gui[i] != null
                        && gui[i].getActions() != null
                        && gui[i].getActions().length > 0
                        && gui[i].getActions()[0]
                        .equals(TAB_NAMES[id])) {
                    GAME_TABS[id] = i;
                    break;
                }
            }
        }

        if (GAME_TABS[id] != -1) {
            return getComponent(MAINUI_INDEX, GAME_TABS[id]);
        }
        return null;
    }


    public IComponent getComponentByUID(int UID) {
        int interfaceID = UID >> 16;
        int componentID = UID & 0xFFFF;
        return getComponent(interfaceID,componentID);
    }

    /**
     * A method that clicks the continue child interface.
     *
     * @author alowaniak
     * @return true when it clicked continue, false otherwise
     */
    public boolean clickContinue() {
        /*IComponent[] psblConts = getInterfaces("Click here to continue");
          if(psblConts == null)
              return false;
          IComponent contIface = null;
          for(IComponent iface : psblConts) {
              if(!iface.isValid())
                  continue;
              if(iface.getBounds().contains(iface.getRandomPointInside()) &&
                      iface.getText().toLowerCase().contains("click here to continue")) {
                  contIface = iface;
                  break;
              }
          }
          if(contIface == null)
              return false;
          contIface.doClick();
          return true;   */
        Interface[] is = botEnv.interfaces.getInterfaces();
        if(is != null || is.length == 0) {

            for(Interface i : is) {
                IComponent[] ics = i.getComponents();
                for(IComponent ic : ics) {
                    if(ic.getText().toLowerCase().contains("click here to continue") && ic.isVisible()) {
                        ic.doClick();
                        return true;
                    }
                    if (ic.hasChildren()) {
                        for (IComponent ic2 : ic.getChildren()) {
                            if (ic2.getText().toLowerCase().contains("click here to continue"))
                                return true;
                        }
                    }
                }
            }

        }

        return false;
    }



}

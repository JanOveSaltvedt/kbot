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



package com.kbotpro.handlers;

import com.kbotpro.scriptsystem.runnable.*;
import com.kbotpro.scriptsystem.runnable.Service;
import com.kbotpro.scriptsystem.Methods;
import com.kbotpro.debuggers.*;
import com.kbotpro.ui.BotPanel;
import com.kbotpro.various.StaticStorage;
import com.kbotpro.bot.BotEnvironment;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 9, 2009
 * Time: 3:55:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebuggerManager implements ServiceCallback {
    public List<Container> debuggers = new ArrayList<Container>();
    private ItemListener itemListener = new ItemListener() {
        /**
         * Invoked when an item has been selected or deselected by the user.
         * The code written for this method performs the operations
         * that need to occur when an item is selected (or deselected).
         */
        public void itemStateChanged(ItemEvent e) {
            final JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
            if(e.getStateChange() != ItemEvent.SELECTED && e.getStateChange() != ItemEvent.DESELECTED){
                return;
            }
            BotPanel botPanel = StaticStorage.mainForm.getOpenedBotPanel();
            if (botPanel == null) {
                JOptionPane.showMessageDialog(null, "No bot selected.");
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                    }
                });
                return;
            }

            if (botPanel.botEnvironment == null) {
                JOptionPane.showMessageDialog(null, "This bot has not started yet.");
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                    }
                });
                return;
            }

            for (Container container : debuggers) {
                if (container.menuItem == source) {
                    if(e.getStateChange() == ItemEvent.SELECTED){
                        startDebugger(container.debugger);
                    }
                    else if(e.getStateChange() == ItemEvent.DESELECTED){
                        stopDebugger(container.debugger);
                    }
                    break;
                }
            }
        }
    };
    private BotEnvironment botEnvironment;

    public DebuggerManager(BotEnvironment botEnvironment) {
        this.botEnvironment = botEnvironment;
        loadDebuggers();
    }

    public void loadDebuggers() {
        List<Container> debuggers = new ArrayList<Container>();

        addDebugger(debuggers, new NPCDebugger());
        addDebugger(debuggers, new PlayerDebugger());
        addDebugger(debuggers, new MyPlayerDebugger());
        addDebugger(debuggers, new ObjectDebugger());
        addDebugger(debuggers, new MenuDebugger());
        addDebugger(debuggers, new InterfaceDebugger());
        addDebugger(debuggers, new SettingsDebugger());
        addDebugger(debuggers, new GameDebugger());
        addDebugger(debuggers, new InventoryDebugger());
        addDebugger(debuggers, new MouseDebugger());
        addDebugger(debuggers, new GroundItemDebugger());
        addDebugger(debuggers, new RenderDataDebugger());
        addDebugger(debuggers, new MinimapDebugger());
        addDebugger(debuggers, new MapDebugger());
        addDebugger(debuggers, new MinimapHookTest());

        JMenu debuggersMenu = StaticStorage.mainForm.debuggersMenu;
        debuggersMenu.removeAll();
        this.debuggers = debuggers;
    }

    private void addDebugger(List<Container> debuggers, Debugger debugger) {
        Container container;
        container = new Container();
        container.debugger = debugger;
        container.menuItem = new JCheckBoxMenuItem(container.debugger.getName());
        container.menuItem.addItemListener(itemListener);
        container.menuItem.setVisible(true);
        debuggers.add(container);
    }

    /**
     * Should be called when the bot's tab is selected
     */
    public void updateMenu(){
        JMenu debuggersMenu = StaticStorage.mainForm.debuggersMenu;
        for(Container container: debuggers){
            if(container.running){
                container.menuItem.setSelected(true);
            }
        }
        debuggersMenu.removeAll();
        for(Container dContainer: debuggers){
            debuggersMenu.add(dContainer.menuItem);
        }
        debuggersMenu.updateUI();
    }

    public void startDebugger(final Debugger debugger) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (debugger.canStart()) {
                    // Reflect setBotEnv()
                    try {
                        Method registeMethod = Methods.class.getDeclaredMethod("registerInternal", new Class<?>[]{BotEnvironment.class});
                        registeMethod.setAccessible(true);    // This method is private so we set it by a little reflection hack
                        registeMethod.invoke(debugger, botEnvironment);
                        Method getService = Debugger.class.getDeclaredMethod("getService");
                        getService.setAccessible(true);
                        Service service = (Service) getService.invoke(debugger);
                        service.setCallback(DebuggerManager.this);
                        service.sStart();
                        botEnvironment.log.log("Started "+debugger.getName());
                    } catch (IllegalAccessException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    } catch (InvocationTargetException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    } catch (NoSuchMethodException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        });
    }

    public void stopDebugger(final Debugger debugger) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                    try {
                        Method getService = Debugger.class.getDeclaredMethod("getService");
                        getService.setAccessible(true);
                        Service service = (Service) getService.invoke(debugger);
                        service.sStop();
                    } catch (IllegalAccessException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    } catch (InvocationTargetException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    } catch (NoSuchMethodException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
        });
    }

    /**
     * Callback to update the service state.
     *
     * @param state    state to be set
     * @param instance
     */
    public void setState(State state, Object instance) {
        if(instance instanceof Debugger){
            for(Container container: debuggers){
                if(instance == container.debugger){
                    if(state == State.ACTIVE){
                        container.menuItem.setSelected(true);
                        container.running = true;

                    }
                    else if(state == State.DEAD){
                        container.menuItem.setSelected(false);
                        try {
                            Method deregisterMethod = Methods.class.getDeclaredMethod("deregisterInternal");
                            deregisterMethod.setAccessible(true);    // This method is private so we set it by a little reflection hack
                            deregisterMethod.invoke(container.debugger);
                        } catch (NoSuchMethodException e) {
                            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                        } catch (InvocationTargetException e) {
                            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                        } catch (IllegalAccessException e) {
                            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                        }
                        container.menuItem.setSelected(false);
                        container.running = false;

                    }
                }
            }
        }
    }

    public void disableAll() {
        for(Container container: debuggers){
            if(container.running){
                stopDebugger(container.debugger);
            }
        }
    }

    public void disposeResources() {
        botEnvironment = null;
        debuggers = null;
    }

    private class Container {
        public Debugger debugger;
        public JCheckBoxMenuItem menuItem;
        public boolean running = false;
    }
}

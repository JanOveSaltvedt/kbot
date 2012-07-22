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



package com.kbotpro.debuggers;

import com.kbotpro.scriptsystem.runnable.Debugger;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Interface;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.ui.ReflectionExplorer;
import com.kbotpro.ui.WrappedObject;

import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 20, 2009
 * Time: 5:45:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterfaceDebugger extends Debugger implements PaintEventListener, TreeSelectionListener {
    private boolean shallRun;
    private InterfaceExplorer UI;
    private IComponent currentSelectedIComponent;

    /**
     * Gets the name shown in the debugs menu
     *
     * @return String containing name
     */
    public String getName() {
        return "Interface Debugger";
    }

    /**
     * Is called before the debugger starts to check if it can run.
     *
     * @return Returns a boolean indicating if the service can be started or not
     */
    public boolean canStart() {
        return true;
    }

    /**
     * Is called right before the run() gets called
     */
    public void onStart() {
        shallRun = true;
        UI = new InterfaceExplorer();

        UI.interfaceTree.addTreeSelectionListener(this);
        UI.resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTree(new IComponentFilter() {
                    public boolean includeICompontent(IComponent iComponent, InterfaceExplorer ui) {
                        return true;
                    }
                });
            }
        });
        UI.filterByTextMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final String search = JOptionPane.showInputDialog(UI, "Enter the text you want to seach for.", "Filter by text", JOptionPane.PLAIN_MESSAGE).toLowerCase();
                if(search == null){
                    JOptionPane.showMessageDialog(UI, "You got to write something into the box!");
                    return;
                }
                updateTree(new IComponentFilter() {
                    public boolean includeICompontent(IComponent iComponent, InterfaceExplorer ui) {
                        String text = iComponent.getText();
                        if (iComponent.getActions() != null && iComponent.getActions().length > 0) {
                            for (String s : iComponent.getActions()) {
                                text = text + s;
                            }
                        }
                        text = text + iComponent.getElementName();
                        return text != null && text.toLowerCase().contains(search);

                    }
                });
            }
        });
        UI.setVisible(true);
        updateTree(new IComponentFilter() {
            public boolean includeICompontent(IComponent iComponent, InterfaceExplorer ui) {
                return true;
            }
        });
        UI.reflectionExlporeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) UI.interfaceTree.getLastSelectedPathComponent();
                if (node == null){
                    return;
                }
                Object nodeObject = node.getUserObject();
                if (nodeObject instanceof IComponentNode) {
                    IComponentNode iComponentNode = (IComponentNode) nodeObject;
                    IComponent iComponent = iComponentNode.getIComponent();
                    Object o = iComponent.getClientObject();
                    ReflectionExplorer reflectionExplorer = new ReflectionExplorer(botEnv);
                    WrappedObject wrappedObject = new WrappedObject(o, botEnv, null);
                    DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(wrappedObject);
                    reflectionExplorer.tree.setModel(new DefaultTreeModel(defaultMutableTreeNode));
                    reflectionExplorer.setVisible(true);
                }
            }
        });
    }

    private void updateTree(IComponentFilter filter){
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
        Interface[] ifaces = interfaces.getInterfaces();
        java.util.List<Interface> loadedInterfaces = new LinkedList<Interface>();
        for(Interface anInterface: ifaces){
            if(anInterface == null){
                continue;
            }
            boolean shallAdd = false;
            for(IComponent child: anInterface.getComponents()){
                if(filter.includeICompontent(child, UI)){
                    shallAdd = true;
                }
            }
            if(shallAdd){
                loadedInterfaces.add(anInterface);
            }
        }
        if(loadedInterfaces.isEmpty()){
            return;
        }
        fillTree(rootNode, loadedInterfaces);
        UI.interfaceTree.setModel(new DefaultTreeModel(rootNode));
        UI.interfaceTree.updateUI();
    }

    private void fillTree(DefaultMutableTreeNode rootNode, java.util.List<Interface> loadedInterfaces){
        for(final Interface anInterface: loadedInterfaces){
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(new Object(){
                @Override
                public String toString() {
                    return "Interface["+anInterface.getID()+"]";
                }
            });
            fillInterfaceComponents(groupNode, anInterface);
            rootNode.add(groupNode);
        }
    }

    private void fillInterfaceComponents(DefaultMutableTreeNode groupNode, Interface anInterface) {
        IComponent[] components = anInterface.getComponents();
        for(int i = 0; i < components.length; i++){
            final IComponent iComponent = components[i];
            if(iComponent == null){
                continue;
            }
            final int index = i;
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new IComponentNode(){
                @Override
                public String toString() {
                    return "["+index+"]";
                }

                public IComponent getIComponent() {
                    return iComponent;  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            if(iComponent.hasChildren()){
                fillChildren(node, iComponent);
            }
            groupNode.add(node);
        }

    }

    private void fillChildren(DefaultMutableTreeNode parrentNode, IComponent parrent) {
        IComponent[] components = parrent.getChildren();
        for(int i = 0; i < components.length; i++){
            final IComponent iComponent = components[i];
            if(iComponent == null){
                continue;
            }
            final int index = i;
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new IComponentNode(){
                @Override
                public String toString() {
                    return "["+index+"]";
                }

                public IComponent getIComponent() {
                    return iComponent;  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            if(iComponent.hasChildren()){
                fillChildren(node, iComponent);
            }
            parrentNode.add(node);
        }
    }

    /**
     * Is called to pause debugger.
     */
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Is called to stop the debugger.
     * The debugger is than added to the cleanup queue and thread will be force killed if not deleted within 10 seconds.
     */
    public void stop() {
        shallRun = false;
        UI.setVisible(false);
    }

    /**
     * You should implement the main loop here.
     */
    public void run() {
        while(shallRun){
            sleep(500);
            if(!UI.isVisible()){
                return;
            }
        }
    }

    /**
     * Gets called when the client updates it graphics.
     * Please do not do anything extremely time consuming in here as it will make the fps go low.
     *
     * @param g Graphics to paint on
     */
    public void onRepaint(Graphics g) {
        if(currentSelectedIComponent != null){
            Rectangle rectangle = currentSelectedIComponent.getBounds();
            g.setColor(Color.orange);
            g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) UI.interfaceTree.getLastSelectedPathComponent();
        if (node == null){
            return;
        }
        Object nodeObject = node.getUserObject();
        if (nodeObject instanceof IComponentNode) {
            IComponent face = ((IComponentNode) nodeObject).getIComponent();
            currentSelectedIComponent = face;
            List<Object[]> data = new LinkedList<Object[]>();
            data.add(new Object[]{face.isValid()?"The interface is valid.":"The interface is not valid!"});
            data.add(new Object[]{face.isVisible()?"The interface is visible.":"The interface is not visible!"});
            data.add(new Object[]{"Screen position:"});
            Rectangle rect = face.getBounds();
            data.add(new Object[]{"    x: " + rect.getX()});
            data.add(new Object[]{"    y: " + rect.getY()});
            data.add(new Object[]{"    width: " + rect.getWidth()});
            data.add(new Object[]{"    height: " + rect.getHeight()});
            data.add(new Object[]{""});
            data.add(new Object[]{"Relative position"});
            data.add(new Object[]{"    x: " + face.getRelativeX()});
            data.add(new Object[]{"    y: " + face.getRelativeY()});
            data.add(new Object[]{""});
            data.add(new Object[]{"Text:"});
            data.add(new Object[]{"    Text: " + face.getText()});
            data.add(new Object[]{"    Text color: " + face.getTextColor()});
            data.add(new Object[]{"    Texture ID: " + face.getTextureID()});
            data.add(new Object[]{""});
            String[] actions = face.getActions();
            if (actions != null) {
                data.add(new Object[]{"Actions :"});
                for (String action : actions) {
                    data.add(new Object[]{"        " + action});
                }
            }
            data.add(new Object[]{""});
            data.add(new Object[]{"Element"});
            data.add(new Object[]{"    ID: " + face.getElementID()});
            data.add(new Object[]{"    Stack size: " + face.getElementStackSize()});
            data.add(new Object[]{"    Name: " + face.getElementName()});
            data.add(new Object[]{"    Visible: " + face.isElementVisible()});
            data.add(new Object[]{""});

            data.add(new Object[]{""});
            data.add(new Object[]{"Bot dev info: "});
            data.add(new Object[]{"Internal visible array index = "+face.getInternalVisibleArrayIndex()});
            data.add(new Object[]{""});
            data.add(new Object[]{"Model info: "});
            data.add(new Object[]{"    Model ID: " + face.getModelID()});
            data.add(new Object[]{"    Model Zoom: " + face.getModelZoom()});
            data.add(new Object[]{"    Model Type: " + face.getModelType()});
            UI.infoTable.setModel(new DefaultTableModel(
                    data.toArray(new Object[1][1]),
                    new String[]{
                            "Interface information"
                    }
            ) {
                final Class[] columnTypes = new Class[]{
                        String.class
                };
                final boolean[] columnEditable = new boolean[]{
                        false
                };

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnEditable[columnIndex];
                }
            });

        } else {
            UI.infoTable.setModel(new DefaultTableModel(
                    new Object[][]{
                            {"Select an interface."},
                    },
                    new String[]{
                            "Interface information"
                    }
            ) {
                final Class[] columnTypes = new Class[]{
                        String.class
                };
                final boolean[] columnEditable = new boolean[]{
                        false
                };

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnEditable[columnIndex];
                }
            });
        }
    }

    private abstract class IComponentNode{
        public abstract IComponent getIComponent();
    }

}

interface IComponentFilter {
    public boolean includeICompontent(IComponent iComponent, InterfaceExplorer ui);
}


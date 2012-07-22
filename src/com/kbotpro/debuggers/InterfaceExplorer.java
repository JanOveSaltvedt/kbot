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
 * Created by JFormDesigner on Tue Oct 20 21:03:30 CEST 2009
 */

package com.kbotpro.debuggers;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import org.jdesktop.layout.*;
import org.jdesktop.layout.GroupLayout;
import com.kbotpro.scriptsystem.wrappers.IComponent;

/**
 * @author Jan Ove
 */
public class InterfaceExplorer extends JFrame {
    public InterfaceExplorer() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        filterByTextMenuItem = new JMenuItem();
        menu2 = new JMenu();
        reflectionExlporeMenuItem = new JMenuItem();
        separator1 = new JPopupMenu.Separator();
        resetButton = new JButton();
        splitPane1 = new JSplitPane();
        scrollPane1 = new JScrollPane();
        interfaceTree = new JTree();
        scrollPane2 = new JScrollPane();
        infoTable = new JTable();

        //======== this ========
        setTitle("Interface Explorer");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("Filter");

                //---- filterByTextMenuItem ----
                filterByTextMenuItem.setText("Filter by text");
                menu1.add(filterByTextMenuItem);
            }
            menuBar1.add(menu1);

            //======== menu2 ========
            {
                menu2.setText("Developer Tools");

                //---- reflectionExlporeMenuItem ----
                reflectionExlporeMenuItem.setText("Reflection Explore from field");
                menu2.add(reflectionExlporeMenuItem);
            }
            menuBar1.add(menu2);
            menuBar1.add(separator1);

            //---- resetButton ----
            resetButton.setText("Reset and update");
            menuBar1.add(resetButton);
        }
        setJMenuBar(menuBar1);

        //======== splitPane1 ========
        {
            splitPane1.setContinuousLayout(true);
            splitPane1.setResizeWeight(0.5);

            //======== scrollPane1 ========
            {

                //---- interfaceTree ----
                interfaceTree.setRootVisible(false);
                scrollPane1.setViewportView(interfaceTree);
            }
            splitPane1.setLeftComponent(scrollPane1);

            //======== scrollPane2 ========
            {

                //---- infoTable ----
                infoTable.setModel(new DefaultTableModel(
                    new Object[][] {
                        {"Text", "value"},
                    },
                    new String[] {
                        "Key", "Value"
                    }
                ) {
                    Class[] columnTypes = new Class[] {
                        String.class, String.class
                    };
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return columnTypes[columnIndex];
                    }
                });
                scrollPane2.setViewportView(infoTable);
            }
            splitPane1.setRightComponent(scrollPane2);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(splitPane1, GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(splitPane1, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    public JMenuItem filterByTextMenuItem;
    private JMenu menu2;
    public JMenuItem reflectionExlporeMenuItem;
    private JPopupMenu.Separator separator1;
    public JButton resetButton;
    private JSplitPane splitPane1;
    private JScrollPane scrollPane1;
    public JTree interfaceTree;
    public JScrollPane scrollPane2;
    public JTable infoTable;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
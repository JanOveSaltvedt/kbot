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

/*
 * Created by JFormDesigner on Sun Jan 31 19:19:11 CET 2010
 */

package com.kbotpro.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.runnable.Random;
import org.jdesktop.layout.GroupLayout;

/**
 * @author Jan Ove
 */
public class RandomManager extends JFrame {
    private BotEnvironment botEnvironment;

    public RandomManager(BotEnvironment botEnvironment) {
        this.botEnvironment = botEnvironment;
        initComponents();

        Vector<JCheckBox> data = new Vector<JCheckBox>();
        for(final Random random: botEnvironment.randomManager.loadedRandoms){
            JCheckBox jCheckBox = new JCheckBox(random.getName(), null, random.isEnabled());
            jCheckBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    int state = e.getStateChange();
                    if (state == ItemEvent.SELECTED) {
                        random.setEnabled(true);
                    }
                    else{
                        random.setEnabled(false);
                    }
                }
            });
            data.add(jCheckBox);

        }

        checkBoxList.setListData(data);

        setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        checkBoxList = new CheckBoxList();

        //======== this ========
        Container contentPane = getContentPane();

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(checkBoxList);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JList checkBoxList;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public class CheckBoxList extends JList {
        protected Border noFocusBorder =
                new EmptyBorder(1, 1, 1, 1);

        public CheckBoxList() {
            setCellRenderer(new CellRenderer());

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int index = locationToIndex(e.getPoint());

                    if (index != -1) {
                        JCheckBox checkbox = (JCheckBox)
                                getModel().getElementAt(index);
                        checkbox.setSelected(
                                !checkbox.isSelected());
                        repaint();
                    }
                }
            }
            );

            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        protected class CellRenderer implements ListCellRenderer {
            public Component getListCellRendererComponent(
                    JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JCheckBox checkbox = (JCheckBox) value;
                checkbox.setBackground(isSelected ?
                        getSelectionBackground() : getBackground());
                checkbox.setForeground(isSelected ?
                        getSelectionForeground() : getForeground());
                checkbox.setEnabled(isEnabled());
                checkbox.setFont(getFont());
                checkbox.setFocusPainted(false);
                checkbox.setBorderPainted(true);
                checkbox.setBorder(isSelected ?
                        UIManager.getBorder(
                                "List.focusCellHighlightBorder") : noFocusBorder);
                return checkbox;
            }
        }
    }

}

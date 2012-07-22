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
 * Created by JFormDesigner on Fri Sep 18 18:15:05 CEST 2009
 */

package com.kbotpro.ui;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.jdesktop.layout.*;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.reflection.ReflectionEngine;
import com.kbotpro.utils.EscapeChars;

/**
 * @author Jan Ove
 */
public class ReflectionExplorer extends JFrame {
    public DefaultMutableTreeNode topNode;
    private BotEnvironment bot;

    public ReflectionExplorer(BotEnvironment bot) {
        this.bot = bot;
        topNode = new DefaultMutableTreeNode("Please select a field.");
        initComponents();
    }

    private void tree1ValueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(node == null){
            return; // Nothing is selected
        }
        Object obj = node.getUserObject();
        if(!(obj instanceof WrappedObject)){
            return; // We just handle the WrappedObjects
        }
        node.removeAllChildren();
        WrappedObject wrappedObject = (WrappedObject) obj;
        for(MutableTreeNode mutableTreeNode: wrappedObject.getAllChildren()){
            node.add(mutableTreeNode);
        }
        // show info
        fieldInfo.setText(wrappedObject.getValueHTML());
    }

    private void updateButtonActionPerformed(ActionEvent e) {
        ClassLoader classLoader = bot.clientClassLoader;
        try {
            Class klass = classLoader.loadClass(classNameTextField.getText());
            Field field = klass.getDeclaredField(fieldNameTextField.getText());
            if(!field.isAccessible()){
                field.setAccessible(true);
            }
            if(!Modifier.isStatic(field.getModifiers())){
                JOptionPane.showMessageDialog(this, "Field is not static...");
                return;
            }
            Object obj = field.get(null);
            if(obj == null){
                JOptionPane.showMessageDialog(this, "Field value is null...");
                return;
            }
            WrappedObject wrappedObject = new WrappedObject(obj, bot, field);
            DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(wrappedObject);
            tree.setModel(new DefaultTreeModel(defaultMutableTreeNode));
        } catch (ClassNotFoundException e1) {
            JOptionPane.showMessageDialog(this, "Class not found...");
        } catch (NoSuchFieldException e1) {
            JOptionPane.showMessageDialog(this, "Field not found...");
        } catch (IllegalAccessException e1) {
            JOptionPane.showMessageDialog(this, "Can't access field.");
        }
    }

    private void updateDataButoonActionPerformed(ActionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(node == null){
            return; // Nothing is selected
        }
        Object obj = node.getUserObject();
        if(!(obj instanceof WrappedObject)){
            return; // We just handle the WrappedObjects
        }
        node.removeAllChildren();
        WrappedObject wrappedObject = (WrappedObject) obj;
        for(MutableTreeNode mutableTreeNode: wrappedObject.getAllChildren()){
            node.add(mutableTreeNode);
        }

        // show info
        fieldInfo.setText(wrappedObject.getValueHTML());
    }

    private void addFieldWatchButtonActionPerformed(ActionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(node == null){
            JOptionPane.showMessageDialog(this, "No field selected.");
            return; // Nothing is selected
        }
        Object obj = node.getUserObject();
        if(!(obj instanceof WrappedObject)){
            JOptionPane.showMessageDialog(this, "No field selected.");
            return; // We just handle the WrappedObjects
        }
        final WrappedObject wrappedObject = (WrappedObject) obj;
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new FieldWatcher(wrappedObject.obj).setVisible(true);
            }
        });
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        label2 = new JLabel();
        classNameTextField = new JTextField();
        fieldNameTextField = new JTextField();
        updateButton = new JButton();
        separator1 = new JSeparator();
        scrollPane1 = new JScrollPane();
        tree = new JTree(topNode);
        separator2 = new JSeparator();
        updateDataButoon = new JButton();
        scrollPane2 = new JScrollPane();
        fieldInfo = new JLabel();
        separator3 = new JSeparator();
        addFieldWatchButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reflection Explorer");
        Container contentPane = getContentPane();

        //---- label1 ----
        label1.setText("Class:");
        label1.setLabelFor(classNameTextField);

        //---- label2 ----
        label2.setText("Field:");
        label2.setLabelFor(fieldNameTextField);

        //---- updateButton ----
        updateButton.setText("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateButtonActionPerformed(e);
            }
        });

        //======== scrollPane1 ========
        {

            //---- tree ----
            tree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    tree1ValueChanged(e);
                }
            });
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            scrollPane1.setViewportView(tree);
        }

        //---- separator2 ----
        separator2.setOrientation(SwingConstants.VERTICAL);

        //---- updateDataButoon ----
        updateDataButoon.setText("Update data");
        updateDataButoon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDataButoonActionPerformed(e);
            }
        });

        //======== scrollPane2 ========
        {

            //---- fieldInfo ----
            fieldInfo.setText("<html>\n<body>\nField info is displayed here\n</body>\n</html>");
            fieldInfo.setVerticalAlignment(SwingConstants.TOP);
            scrollPane2.setViewportView(fieldInfo);
        }

        //---- separator3 ----
        separator3.setOrientation(SwingConstants.VERTICAL);

        //---- addFieldWatchButton ----
        addFieldWatchButton.setText("Add field watch");
        addFieldWatchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addFieldWatchButtonActionPerformed(e);
            }
        });

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(contentPaneLayout.createParallelGroup()
                        .add(contentPaneLayout.createSequentialGroup()
                            .add(scrollPane1, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(separator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(contentPaneLayout.createParallelGroup()
                                .add(scrollPane2, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                                .add(updateDataButoon, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)))
                        .add(separator1, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                        .add(contentPaneLayout.createSequentialGroup()
                            .add(label1)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(classNameTextField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(label2)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(fieldNameTextField, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(updateButton)
                            .add(32, 32, 32)
                            .add(separator3, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(addFieldWatchButton)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(contentPaneLayout.createParallelGroup(GroupLayout.TRAILING)
                        .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                            .add(label1)
                            .add(classNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .add(label2)
                            .add(fieldNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .add(updateButton))
                        .add(GroupLayout.LEADING, separator3, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .add(addFieldWatchButton))
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(separator1, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(contentPaneLayout.createParallelGroup()
                        .add(separator2, GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                        .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                        .add(GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
                            .add(scrollPane2, GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(updateDataButoon)))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JLabel label2;
    private JTextField classNameTextField;
    private JTextField fieldNameTextField;
    private JButton updateButton;
    private JSeparator separator1;
    private JScrollPane scrollPane1;
    public JTree tree;
    private JSeparator separator2;
    private JButton updateDataButoon;
    private JScrollPane scrollPane2;
    private JLabel fieldInfo;
    private JSeparator separator3;
    private JButton addFieldWatchButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables



}


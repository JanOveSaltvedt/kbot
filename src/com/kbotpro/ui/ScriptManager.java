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
 * Created by JFormDesigner on Wed Nov 25 15:51:11 CET 2009
 */

package com.kbotpro.ui;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.runnable.Script;
import org.jdesktop.layout.GroupLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * @author Jan Ove
 */
public class ScriptManager extends JFrame {
    private com.kbotpro.handlers.ScriptManager scriptManager;

    public ScriptManager(BotEnvironment botEnvironment) {
        initComponents();
        scriptManager = botEnvironment.scriptManager;
        Vector<Storage> storages = new Vector<Storage>();
        for (Script script : scriptManager.runningScripts) {
            storages.add(new Storage(script));
        }
        scriptList.setListData(storages);
    }

    private void cancelButtonActionPerformed() {
        dispose();
    }

    private void stopButtonActionPerformed() {
        Storage storage = (Storage) scriptList.getSelectedValue();
        if (storage == null) {
            return;
        }
        scriptManager.stopScript(storage.script);
        dispose();
    }

    private void scriptListValueChanged() {
        Storage storage = (Storage) scriptList.getSelectedValue();
        if (storage == null) {
            stopButton.setEnabled(false);
            return;
        }
        stopButton.setEnabled(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        scriptList = new JList();
        buttonBar = new JPanel();
        stopButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Script Manager");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //======== scrollPane1 ========
                {

                    //---- scriptList ----
                    scriptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    scriptList.setModel(new AbstractListModel() {
                        String[] values = {
                                "woot",
                                "woot",
                                "woot"
                        };

                        public int getSize() {
                            return values.length;
                        }

                        public Object getElementAt(int i) {
                            return values[i];
                        }
                    });
                    scriptList.addListSelectionListener(new ListSelectionListener() {
                        public void valueChanged(ListSelectionEvent e) {
                            scriptListValueChanged();
                        }
                    });
                    scrollPane1.setViewportView(scriptList);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 85, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0};

                //---- stopButton ----
                stopButton.setText("Stop script");
                stopButton.setEnabled(false);
                stopButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        stopButtonActionPerformed();
                    }
                });
                buttonBar.add(stopButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed();
                    }
                });
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JScrollPane scrollPane1;
    private JList scriptList;
    private JPanel buttonBar;
    private JButton stopButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private class Storage {
        public Script script;

        private Storage(Script script) {
            this.script = script;
        }

        public String toString() {
            return script.getName();
        }
    }
}

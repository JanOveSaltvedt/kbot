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
 * Created by JFormDesigner on Sat Mar 06 18:51:28 CET 2010
 */

package com.kbotpro.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.kbotpro.cache.settings.MainSettings;
import org.jdesktop.layout.GroupLayout;

/**
 * @author Jan Ove
 */
public class TOS extends JFrame {
    public boolean agreedToTOS = false;
    public static int currentVersion = 2;

    public TOS() {
        initComponents();
    }

    private void agreeButtonActionPerformed(ActionEvent e) {
        agreedToTOS = true;
        MainSettings.setSetting("agreedTOS", true);
        MainSettings.setSetting("agreedTOSVersion", currentVersion);
        MainForm.openMainForm();
        dispose();
    }

    public static boolean hasAgreedTOS(int currentVersion) {
        final Object agreed = MainSettings.getSetting("agreedTOS");
        final Object version = MainSettings.getSetting("agreedTOSVersion");
        final boolean ret = !(version == null || !(version instanceof Integer)) && agreed != null && Boolean.TRUE.equals(agreed) && (Integer) version >= currentVersion;
        return ret;
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        buttonBar = new JPanel();
        agreeButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

                    //---- textArea1 ----
                    textArea1.setText("By clicking 'I Agree' you agree to everything stated below:\n\nI am an employee of Jagex LTD\n");
                    textArea1.setTabSize(4);
                    scrollPane1.setViewportView(textArea1);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .add(contentPanelLayout.createSequentialGroup()
                            .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .add(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                            .addContainerGap())
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- agreeButton ----
                agreeButton.setText("I agree");
                agreeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        agreeButtonActionPerformed(e);
                    }
                });
                buttonBar.add(agreeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
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
    private JTextArea textArea1;
    private JPanel buttonBar;
    private JButton agreeButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

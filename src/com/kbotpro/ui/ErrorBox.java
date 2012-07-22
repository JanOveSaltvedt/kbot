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
 * Created by JFormDesigner on Sun Nov 22 12:04:02 CET 2009
 */

package com.kbotpro.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.jdesktop.layout.*;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 * @author Jan Ove
 */
public class ErrorBox extends JDialog {
    public ErrorBox(Frame owner, String errorMessage) {
        super(owner);
        initComponents();
        textArea.setText(errorMessage);
    }

    public ErrorBox(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void okButtonActionPerformed(ActionEvent e) {
        dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        scrollPane1 = new JScrollPane();
        textArea = new JTextArea();
        okButton = new JButton();

        //======== this ========
        setTitle("Error!");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        Container contentPane = getContentPane();

        //---- label1 ----
        label1.setText("An error occured in the script, please report this to the script author!");

        //======== scrollPane1 ========
        {

            //---- textArea ----
            textArea.setEditable(false);
            scrollPane1.setViewportView(textArea);
        }

        //---- okButton ----
        okButton.setText("Ok");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonActionPerformed(e);
            }
        });

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(contentPaneLayout.createParallelGroup(GroupLayout.TRAILING)
                        .add(GroupLayout.LEADING, scrollPane1, GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                        .add(GroupLayout.LEADING, contentPaneLayout.createParallelGroup()
                            .add(label1)
                            .add(GroupLayout.TRAILING, okButton)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(label1)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(okButton)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JScrollPane scrollPane1;
    private JTextArea textArea;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

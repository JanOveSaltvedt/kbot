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
 * Created by JFormDesigner on Tue Nov 03 19:00:42 CET 2009
 */

package com.kbotpro.ui;

import java.awt.*;
import javax.swing.*;
import org.jdesktop.layout.*;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 * @author Jan Ove
 */
public class ProgressUI extends JDialog {
    public ProgressUI(Frame owner) {
        super(owner);
        initComponents();
        setVisible(true);
    }

    public ProgressUI(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        progressBar = new JProgressBar();
        label1 = new JLabel();

        //======== this ========
        setResizable(false);
        setTitle("Downloading script..");
        Container contentPane = getContentPane();

        //---- progressBar ----
        progressBar.setValue(50);

        //---- label1 ----
        label1.setText("Downloading script...");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(contentPaneLayout.createParallelGroup()
                        .add(progressBar, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
                        .add(label1))
                    .addContainerGap(12, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .add(GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(label1)
                    .addPreferredGap(LayoutStyle.RELATED, 9, Short.MAX_VALUE)
                    .add(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    public JProgressBar progressBar;
    private JLabel label1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

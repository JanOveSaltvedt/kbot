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
 * Created by JFormDesigner on Tue Feb 23 18:10:10 CET 2010
 */

package com.kbotpro.debuggers;

import java.awt.*;
import javax.swing.*;
import org.jdesktop.layout.*;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 * @author Jan Ove
 */
public class UI extends JFrame {
    public UI() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        myPosButton = new JButton();
        xSpinner = new JSpinner();
        label1 = new JLabel();
        label2 = new JLabel();
        ySpinner = new JSpinner();
        reflectButton = new JButton();
        createMapButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Container contentPane = getContentPane();

        //---- myPosButton ----
        myPosButton.setText("Set to current position");

        //---- label1 ----
        label1.setText("X pos:");
        label1.setIcon(null);
        label1.setLabelFor(xSpinner);

        //---- label2 ----
        label2.setText("Y pos:");

        //---- reflectButton ----
        reflectButton.setText("Reflect");

        //---- createMapButton ----
        createMapButton.setText("Create Map");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .add(contentPaneLayout.createParallelGroup()
                        .add(contentPaneLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(contentPaneLayout.createParallelGroup(GroupLayout.TRAILING, false)
                                .add(GroupLayout.LEADING, reflectButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(GroupLayout.LEADING, myPosButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(GroupLayout.LEADING, contentPaneLayout.createSequentialGroup()
                                    .add(6, 6, 6)
                                    .add(contentPaneLayout.createParallelGroup()
                                        .add(contentPaneLayout.createSequentialGroup()
                                            .add(label2)
                                            .addPreferredGap(LayoutStyle.UNRELATED)
                                            .add(ySpinner, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
                                        .add(contentPaneLayout.createSequentialGroup()
                                            .add(label1)
                                            .addPreferredGap(LayoutStyle.UNRELATED)
                                            .add(xSpinner))))))
                        .add(GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
                            .add(5, 5, 5)
                            .add(createMapButton, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(myPosButton)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                        .add(label1)
                        .add(xSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                        .add(label2)
                        .add(ySpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.UNRELATED)
                    .add(reflectButton)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(createMapButton)
                    .addContainerGap(7, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    public JButton myPosButton;
    public JSpinner xSpinner;
    private JLabel label1;
    private JLabel label2;
    public JSpinner ySpinner;
    public JButton reflectButton;
    public JButton createMapButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

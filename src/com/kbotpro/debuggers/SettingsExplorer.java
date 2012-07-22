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
 * Created by JFormDesigner on Wed Oct 21 18:02:36 CEST 2009
 */

package com.kbotpro.debuggers;

import java.awt.*;
import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 * @author Jan Ove
 */
public class SettingsExplorer extends JFrame {
    public SettingsExplorer() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        dataLabel = new JLabel();
        setCompareButton = new JButton();
        scrollPane2 = new JScrollPane();
        updatedLabel = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Settings Explorer");
        Container contentPane = getContentPane();

        //======== scrollPane1 ========
        {

            //---- dataLabel ----
            dataLabel.setText("text");
            scrollPane1.setViewportView(dataLabel);
        }

        //---- setCompareButton ----
        setCompareButton.setText("<html>\n<body>\n<center>\nUse<br>\nas<br>\ncompare<br>\nbase\n</center>\n</body>\n</html>");

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(updatedLabel);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(scrollPane1, GroupLayout.PREFERRED_SIZE, 503, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(contentPaneLayout.createParallelGroup()
                        .add(setCompareButton, GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                        .add(scrollPane2, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .add(GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(contentPaneLayout.createParallelGroup(GroupLayout.TRAILING)
                        .add(GroupLayout.LEADING, scrollPane1, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                        .add(GroupLayout.LEADING, contentPaneLayout.createSequentialGroup()
                            .add(setCompareButton, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(scrollPane2, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    public JLabel dataLabel;
    public JButton setCompareButton;
    private JScrollPane scrollPane2;
    public JLabel updatedLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

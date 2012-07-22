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
 * Created by JFormDesigner on Mon Aug 10 11:39:31 CEST 2009
 */

package com.kbotpro.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import org.jdesktop.layout.*;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import com.kbotpro.Version;

/**
 * @author Jan Ove
 */
public class AboutUI extends JDialog {
    public AboutUI(Frame owner) {
        super(owner);
        initComponents();
        versionLabel.setText(Version.majorVersion+"."+Version.minorVersion);
        build.setText(Version.build);

        String information = "" +
                "Memory roof: "+Runtime.getRuntime().totalMemory()/(1024*1024)+"MB\n" +
                "Free memory: "+Runtime.getRuntime().freeMemory()/(1024*1024)+"MB\n" +
                "";
        textArea1.setText(information);
    }

    public AboutUI(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void okButtonActionPerformed(ActionEvent e) {
        dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        versionLabel = new JLabel();
        label4 = new JLabel();
        build = new JLabel();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        label5 = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About KBot");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- label1 ----
                label1.setText("KBot:");

                //---- label2 ----
                label2.setText("Main developer: \tKosaki (Jan Ove Saltvedt)");

                //---- label3 ----
                label3.setText("Version:");

                //---- versionLabel ----
                versionLabel.setText("0.1");

                //---- label4 ----
                label4.setText("Build:");

                //---- build ----
                build.setText("0");

                //======== scrollPane1 ========
                {

                    //---- textArea1 ----
                    textArea1.setEditable(false);
                    scrollPane1.setViewportView(textArea1);
                }

                //---- label5 ----
                label5.setText("Information / Credits:");

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .add(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(contentPanelLayout.createParallelGroup()
                                .add(label1)
                                .add(label2)
                                .add(contentPanelLayout.createSequentialGroup()
                                    .add(contentPanelLayout.createParallelGroup()
                                        .add(label3)
                                        .add(label4))
                                    .addPreferredGap(LayoutStyle.RELATED)
                                    .add(contentPanelLayout.createParallelGroup()
                                        .add(build)
                                        .add(versionLabel)))
                                .add(label5)
                                .add(GroupLayout.TRAILING, scrollPane1, GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE))
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .add(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(label1)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(label2)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(contentPanelLayout.createParallelGroup(GroupLayout.BASELINE)
                                .add(label3)
                                .add(versionLabel))
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(contentPanelLayout.createParallelGroup()
                                .add(label4)
                                .add(build))
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(label5)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addContainerGap())
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
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
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel versionLabel;
    private JLabel label4;
    private JLabel build;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JLabel label5;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

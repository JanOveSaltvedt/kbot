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
 * Created by JFormDesigner on Sat Sep 19 18:00:17 CEST 2009
 */

package com.kbotpro.ui;

import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import javax.swing.*;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.kbotpro.utils.EscapeChars;

/**
 * @author Jan Ove
 */
public class FieldWatcher extends JFrame implements Runnable{
    private Object watchedObj;
    private Thread updateThread;
    private boolean active = true;
    public FieldWatcher(Object watchedObj) {
        this.watchedObj = watchedObj;
        initComponents();
        updateThread = new Thread(this);
        updateThread.start();
        this.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowClosing(WindowEvent e) {
                active = false;
            }

            public void windowClosed(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowIconified(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowDeiconified(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowActivated(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowDeactivated(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label2 = new JLabel();
        updateRateSpinner = new JSpinner();
        label3 = new JLabel();
        scrollPane1 = new JScrollPane();
        infoLabel = new JLabel();

        //======== this ========
        setTitle("Field Watch");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();

        //---- label2 ----
        label2.setText("Update rate:");
        label2.setLabelFor(updateRateSpinner);

        //---- updateRateSpinner ----
        updateRateSpinner.setModel(new SpinnerNumberModel(1.0, 0.1, null, 0.5));

        //---- label3 ----
        label3.setText("Times per second");

        //======== scrollPane1 ========
        {

            //---- infoLabel ----
            infoLabel.setText("Field info");
            infoLabel.setVerticalAlignment(SwingConstants.TOP);
            scrollPane1.setViewportView(infoLabel);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(contentPaneLayout.createParallelGroup()
                        .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                        .add(contentPaneLayout.createSequentialGroup()
                            .add(label2)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(updateRateSpinner, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(label3)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .add(GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                        .add(label2)
                        .add(updateRateSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .add(label3))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label2;
    private JSpinner updateRateSpinner;
    private JLabel label3;
    private JScrollPane scrollPane1;
    private JLabel infoLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void run() {
        while(active){
            String html = "<html><body>"+EscapeChars.forHTML(ReflectionToStringBuilder.toString(watchedObj, ToStringStyle.MULTI_LINE_STYLE))+"</body></html>";
            infoLabel.setText(html);
            try {
                Thread.sleep((int)(1000D/((Double)updateRateSpinner.getValue())));
            } catch (InterruptedException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}

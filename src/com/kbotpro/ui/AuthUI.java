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
 * Created by JFormDesigner on Sun Aug 09 19:33:27 CEST 2009
 */

package com.kbotpro.ui;

import com.kbotpro.servercom.*;
import com.kbotpro.various.StaticStorage;
import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Jan Ove
 */
public class AuthUI extends JFrame {
    public AuthUI() {
        initComponents();
        try{
            // ICON
            ArrayList<Image> images = new ArrayList<Image>();
            URL urlIconBig = MainForm.class.getResource("/images/iconacerbig.png");

            if (urlIconBig != null) {
                ImageIcon iconBig = new ImageIcon(urlIconBig);
                images.add(iconBig.getImage());
            }
            URL urlIconSmall = MainForm.class.getResource("/images/iconacersmall.png");
            if (urlIconSmall != null) {
                ImageIcon iconSmall = new ImageIcon(urlIconSmall);
                images.add(iconSmall.getImage());

            }

            if (!images.isEmpty()) {
                setIconImages(images);
            }

            // END ICON
        }catch (Throwable throwable){
            Logger.getRootLogger().error("Exception: ", throwable);
        }
        addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowClosing(WindowEvent e) {
                if(StaticStorage.userStorage == null || !StaticStorage.userStorage.isLoggedIn()){
                    System.exit(0);
                }
                else{
                    setVisible(false);
                }
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

    private void cancelButtonActionPerformed() {
        System.exit(0);
    }

    private void loginButtonActionPerformed() {
        final UserStorage userStorage = new UserStorage();
        StaticStorage.userStorage = userStorage;
        userStorage.setUsername(usernameTextField.getText());
        userStorage.setPassword(new String(passwordField.getPassword()));
        
        final ServerCom serverCom = (safeModeCheckBox.isSelected()? new HttpServerCom() : new JSPServerCom());//new SocketServerCom());
            //final ServerCom serverCom =
            StaticStorage.serverCom = serverCom;
        final Object[] response = serverCom.login();
        if(response[0].equals(Boolean.TRUE)){
            infoLabel.setForeground(Color.GREEN);
            infoLabel.setText((String) response[1]);
            // First we check if the TOS has been read and agreed
            if(!TOS.hasAgreedTOS(TOS.currentVersion)){
                TOS tos = new TOS();
                tos.setVisible(true);
            }
            else{
                MainForm.openMainForm();
            }
            dispose();
        }
        else{
            infoLabel.setForeground(Color.RED);
            infoLabel.setText((String) response[1]);
        }

    }

    private void usernameTextFieldKeyTyped() {
        if(usernameTextField.getText().length() > 0){
            usernameTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.green));
        }
        else{
            usernameTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
        }
    }

    private void passwordFieldKeyTyped() {
        if(passwordField.getPassword().length > 0){
            passwordField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.green));
        }
        else{
            passwordField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
        }
    }

    private void usernameTextFieldKeyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            passwordField.requestFocus();
        }
    }

    private void passwordFieldKeyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            loginButton.doClick();
        }
    }

    private void usernameTextFieldKeyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            passwordField.requestFocus();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        infoLabel = new JLabel();
        label1 = new JLabel();
        usernameTextField = new JTextField();
        label2 = new JLabel();
        passwordField = new JPasswordField();
        buttonBar = new JPanel();
        safeModeCheckBox = new JCheckBox();
        loginButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Login");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- infoLabel ----
                infoLabel.setText("Please login with your KBot forums account!");
                infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

                //---- label1 ----
                label1.setText("Username:");

                //---- usernameTextField ----
                usernameTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        usernameTextFieldKeyReleased(e);
                    }
                    @Override
                    public void keyTyped(KeyEvent e) {
                        usernameTextFieldKeyTyped();
                    }
                });
                usernameTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));

                //---- label2 ----
                label2.setText("Password:");

                //---- passwordField ----
                passwordField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        passwordFieldKeyReleased(e);
                    }
                    @Override
                    public void keyTyped(KeyEvent e) {
                        passwordFieldKeyTyped();
                    }
                });
                passwordField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .add(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(contentPanelLayout.createParallelGroup()
                                .add(infoLabel, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                                .add(contentPanelLayout.createSequentialGroup()
                                    .add(contentPanelLayout.createParallelGroup()
                                        .add(label1)
                                        .add(label2))
                                    .addPreferredGap(LayoutStyle.RELATED)
                                    .add(contentPanelLayout.createParallelGroup(GroupLayout.TRAILING)
                                        .add(passwordField, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                        .add(GroupLayout.LEADING, usernameTextField, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))))
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .add(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(infoLabel)
                            .add(18, 18, 18)
                            .add(contentPanelLayout.createParallelGroup(GroupLayout.BASELINE)
                                .add(label1)
                                .add(usernameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.UNRELATED)
                            .add(contentPanelLayout.createParallelGroup(GroupLayout.BASELINE)
                                .add(label2)
                                .add(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- safeModeCheckBox ----
                safeModeCheckBox.setText("Use backup server");
                buttonBar.add(safeModeCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- loginButton ----
                loginButton.setText("Login");
                loginButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        loginButtonActionPerformed();
                    }
                });
                buttonBar.add(loginButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
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
    private JLabel infoLabel;
    private JLabel label1;
    private JTextField usernameTextField;
    private JLabel label2;
    private JPasswordField passwordField;
    private JPanel buttonBar;
    private JCheckBox safeModeCheckBox;
    private JButton loginButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

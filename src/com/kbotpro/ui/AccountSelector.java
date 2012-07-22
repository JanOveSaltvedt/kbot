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
 * Created by JFormDesigner on Fri Jan 01 18:49:16 CET 2010
 */

package com.kbotpro.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.PlainDocument;

import com.kbotpro.handlers.AccountsManager;
import com.kbotpro.utils.BotControl;
import com.kbotpro.various.StaticStorage;
import org.jdesktop.layout.*;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 * @author Jan Ove
 */
public class AccountSelector extends JFrame {
    private char[] maskChars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    private AccountsManager.Account[] accountList;
    private BotPanel botPanel;

    public AccountSelector(Object[] accounts) {
        accountList = new AccountsManager.Account[accounts.length];
        for (int i = 0; i < accountList.length; i ++) {
            accountList[i] = (AccountsManager.Account)accounts[i];
        }
        initComponents();
        setVisible(true);
    }

    public AccountSelector(Object[] accounts, BotPanel botPanel) {
        accountList = new AccountsManager.Account[accounts.length];
        for (int i = 0; i < accountList.length; i ++) {
            accountList[i] = (AccountsManager.Account)accounts[i];
        }
        this.botPanel = botPanel;
        initComponents();
        setVisible(true);
    }

    private void accountComboActionPerformed() {
    }

    private void pinCheckBoxStateChanged() {
        pinField.setEditable(pinCheckBox.isSelected());
    }

    private void lampCheckBoxStateChanged() {
        lampCombo.setEnabled(lampCheckBox.isSelected());
    }

    private void okButtonActionPerformed() {
        AccountsManager.Account account = (AccountsManager.Account)accountCombo.getSelectedItem();
        if (pinCheckBox.isSelected()) {
            try {
                Integer.parseInt(pinField.getText());
            } catch(NumberFormatException e) {
                return;
            }
            if (pinField.getText().length() != 4) {
                return;
            }

        }
        account.pin = pinField.getText();
        account.membersAccount = memberCheckBox.isSelected();
        account.useLamp = lampCheckBox.isSelected();
        account.lampIndex = lampCombo.getSelectedItem().toString();
        if (botPanel != null) {
            botPanel.botName = account;
            StaticStorage.mainForm.mainTabbedPane.setTitleAt(StaticStorage.mainForm.mainTabbedPane.getSelectedIndex(), account.getUsername());
        } else {
            BotControl.addBot(account);
        }
        /*StaticStorage.mainForm.botMenu.addSeparator();
        StaticStorage.mainForm.botMenu.add("Change Account for current tab").addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });*/
        this.setVisible(false);
        this.dispose();
    }

    private void cancelButtonActionPerformed() {
        if (botPanel != null) {
            botPanel.botName = StaticStorage.accountsManager.constructAccount("null", "null");
            StaticStorage.mainForm.mainTabbedPane.setTitleAt(StaticStorage.mainForm.mainTabbedPane.getSelectedIndex(), botPanel.botName.getUsername());
        } else {
            BotControl.addBot(StaticStorage.accountsManager.constructAccount("null", "null"));
        }
        this.setVisible(false);
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - josh hurr
        accountCombo = new JComboBox();
        memberCheckBox = new JCheckBox();
        pinCheckBox = new JCheckBox();
        pinField = new JTextField();
        lampCheckBox = new JCheckBox();
        lampCombo = new JComboBox();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Account Selector");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();

        //---- accountCombo ----
        accountCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accountComboActionPerformed();
            }
        });
        accountCombo.setModel(new DefaultComboBoxModel(                 accountList         ));

        //---- memberCheckBox ----
        memberCheckBox.setText("Member");

        //---- pinCheckBox ----
        pinCheckBox.setText("Use Pin");
        pinCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                pinCheckBoxStateChanged();
            }
        });

        //---- pinField ----
        pinField.setEditable(false);

        //---- lampCheckBox ----
        lampCheckBox.setText("Lamp");
        lampCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                lampCheckBoxStateChanged();
            }
        });

        //---- lampCombo ----
        lampCombo.setModel(new DefaultComboBoxModel(new String[] {
                "Attack",
                "Strength",
                "Ranged",
                "Magic",
                "Defence",
                "Crafting",
                "Constitution",
                "Prayer",
                "Agility",
                "Herblore",
                "Thieving",
                "Fishing",
                "Runecraft",
                "Slayer",
                "Farming",
                "Mining",
                "Smithing",
                "Hunter",
                "Cooking",
                "Firemaking",
                "Woodcutting",
                "Fletching",
                "Construction",
                "Summoning",
                "Dungeoneering"
        }));
        lampCombo.setEnabled(false);
        pinField.setDocument(new MaskedDocument(true, maskChars));

        //---- okButton ----
        okButton.setText("Ok");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonActionPerformed();
            }
        });

        //---- cancelButton ----
        cancelButton.setText("No Account");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed();
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
                                        .add(memberCheckBox, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())
                                .add(contentPaneLayout.createSequentialGroup()
                                .add(contentPaneLayout.createParallelGroup()
                                        .add(contentPaneLayout.createSequentialGroup()
                                                .add(lampCheckBox)
                                                .add(18, 18, 18)
                                                .add(lampCombo, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
                                        .add(contentPaneLayout.createSequentialGroup()
                                                .add(pinCheckBox)
                                                .addPreferredGap(LayoutStyle.UNRELATED)
                                                .add(pinField, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                                        .add(accountCombo, 0, 181, Short.MAX_VALUE))
                                .add(328, 328, 328))))
                        .add(GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(okButton, GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.UNRELATED)
                        .add(cancelButton)
                        .add(327, 327, 327))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .add(contentPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(accountCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.UNRELATED)
                        .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                                .add(pinCheckBox)
                                .add(pinField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                                .add(lampCheckBox)
                                .add(lampCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(memberCheckBox)
                        .addPreferredGap(LayoutStyle.UNRELATED)
                        .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                                .add(okButton, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .add(cancelButton, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        setSize(210, 195);
        setLocationRelativeTo(StaticStorage.mainForm);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - josh hurr
    private JComboBox accountCombo;
    private JCheckBox memberCheckBox;
    private JCheckBox pinCheckBox;
    private JTextField pinField;
    private JCheckBox lampCheckBox;
    private JComboBox lampCombo;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    class MaskedDocument extends PlainDocument {

        char[] maskChars;

        boolean accept;


        public MaskedDocument(boolean accept, char[] maskChars) {

            super();

            this.maskChars = maskChars;

            this.accept = accept;

        }
    }
}

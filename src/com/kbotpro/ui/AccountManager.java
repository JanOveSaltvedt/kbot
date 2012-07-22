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
 * Created by JFormDesigner on Sun Nov 29 13:37:36 CET 2009
 */

package com.kbotpro.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import com.kbotpro.handlers.AccountsManager;
import com.kbotpro.various.StaticStorage;

/**
 * @author Jan Ove
 */
public class AccountManager extends JFrame {
    private Vector<AccountWrapper> accountWrappers = new Vector<AccountWrapper>();

    public AccountManager() {
        initComponents();
        refreshList();
    }

    private void cancelButtonActionPerformed() {
        dispose();
    }

    private void refreshList() {
        Vector<AccountWrapper> accountWrappers = new Vector<AccountWrapper>();
        List<AccountsManager.Account> list = StaticStorage.accountsManager.getAccounts();
        for (AccountsManager.Account account : list) {
            AccountWrapper wrapper = new AccountWrapper();
            wrapper.account = account;
            accountWrappers.add(wrapper);
        }
        this.accountWrappers = accountWrappers;
        accountsList.setListData(this.accountWrappers);
    }

    private void updateList() {
        accountsList.setListData(accountWrappers);
    }

    private void accountsListValueChanged(ListSelectionEvent e) {
        Object o = accountsList.getSelectedValue();
        if (o == null || !(o instanceof AccountWrapper)) {
            usernameTextField.setText("");
            passwordField.setText("");
            usernameTextField.setEditable(false);
            passwordField.setEditable(false);
            applyButton.setEnabled(false);
            return;
        }
        AccountWrapper accountWrapper = (AccountWrapper) o;
        usernameTextField.setText(accountWrapper.account.getUsername());
        passwordField.setText(accountWrapper.account.getPassword());
        usernameTextField.setEditable(true);
        passwordField.setEditable(true);
        applyButton.setEnabled(true);

    }

    private void createNewButtonActionPerformed() {
        AccountsManager.Account account = StaticStorage.accountsManager.constructAccount("Account X", "password");
        AccountWrapper accountWrapper = new AccountWrapper();
        accountWrapper.account = account;
        accountWrapper.changed = true;
        accountWrappers.add(accountWrapper);
        updateList();
        accountsList.setSelectedValue(accountWrapper, true);
    }

    private void applyButtonActionPerformed() {
        Object o = accountsList.getSelectedValue();
        if (o == null || !(o instanceof AccountWrapper)) {
            usernameTextField.setEditable(false);
            passwordField.setEditable(false);
            applyButton.setEnabled(false);
            return;
        }
        AccountWrapper accountWrapper = (AccountWrapper) o;
        String password = new String(passwordField.getPassword());
        String username = usernameTextField.getText();
        if (!username.equals(accountWrapper.account.getUsername()) || !password.equals(accountWrapper.account.getPassword())) {
            accountWrapper.changed = true;
            if (accountWrapper.account.ID >= 0) {
                StaticStorage.accountsManager.deleteAccount(accountWrapper.account);
            }
            accountWrapper.account = StaticStorage.accountsManager.constructAccount(username, password);
            StaticStorage.accountsManager.createAccount(accountWrapper.account);
            StaticStorage.accountsManager.updateAccounts();
            refreshList();
        }
    }

    private void deleteButtonActionPerformed() {
        Object o = accountsList.getSelectedValue();
        if (o == null || !(o instanceof AccountWrapper)) {
            usernameTextField.setEditable(false);
            passwordField.setEditable(false);
            applyButton.setEnabled(false);
            return;
        }
        AccountWrapper accountWrapper = (AccountWrapper) o;
        if (accountWrapper.account.ID >= 0) {
            StaticStorage.accountsManager.deleteAccount(accountWrapper.account);
        }
        StaticStorage.accountsManager.updateAccounts();
        refreshList();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        accountsList = new JList();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        scrollPane2 = new JScrollPane();
        table1 = new JTable();
        usernameTextField = new JTextField();
        passwordField = new JPasswordField();
        createNewButton = new JButton();
        applyButton = new JButton();
        cancelButton = new JButton();
        deleteButton = new JButton();

        //======== this ========
        setTitle("Account Manager");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();

        //======== scrollPane1 ========
        {

            //---- accountsList ----
            accountsList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    accountsListValueChanged(e);
                }
            });
            scrollPane1.setViewportView(accountsList);
        }

        //---- label1 ----
        label1.setText("Accounts");

        //---- label2 ----
        label2.setText("Username:");

        //---- label3 ----
        label3.setText("Password");

        //---- label4 ----
        label4.setText("Settings:");

        //======== scrollPane2 ========
        {

            //---- table1 ----
            table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table1.setModel(new DefaultTableModel(
                new Object[][] {
                    {"PIN", "0000"},
                    {null, null},
                },
                new String[] {
                    "Name", "Value"
                }
            ) {
                boolean[] columnEditable = new boolean[] {
                    false, false
                };
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnEditable[columnIndex];
                }
            });
            table1.setEnabled(false);
            scrollPane2.setViewportView(table1);
        }

        //---- usernameTextField ----
        usernameTextField.setEditable(false);

        //---- passwordField ----
        passwordField.setEditable(false);

        //---- createNewButton ----
        createNewButton.setText("Create new");
        createNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewButtonActionPerformed();
            }
        });

        //---- applyButton ----
        applyButton.setText("Apply");
        applyButton.setEnabled(false);
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyButtonActionPerformed();
            }
        });

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed();
            }
        });

        //---- deleteButton ----
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteButtonActionPerformed();
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
                            .add(scrollPane1, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.UNRELATED)
                            .add(contentPaneLayout.createParallelGroup()
                                .add(scrollPane2, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                                .add(label4)
                                .add(contentPaneLayout.createSequentialGroup()
                                    .add(label3)
                                    .addPreferredGap(LayoutStyle.UNRELATED)
                                    .add(passwordField, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                                .add(contentPaneLayout.createSequentialGroup()
                                    .add(label2)
                                    .addPreferredGap(LayoutStyle.RELATED)
                                    .add(usernameTextField, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                                .add(contentPaneLayout.createSequentialGroup()
                                    .add(createNewButton)
                                    .addPreferredGap(LayoutStyle.UNRELATED)
                                    .add(deleteButton)
                                    .addPreferredGap(LayoutStyle.RELATED, 14, Short.MAX_VALUE)
                                    .add(applyButton)
                                    .addPreferredGap(LayoutStyle.RELATED)
                                    .add(cancelButton))))
                        .add(label1))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .add(GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(label1)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(contentPaneLayout.createParallelGroup()
                        .add(GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
                            .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                                .add(label2)
                                .add(usernameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                                .add(label3)
                                .add(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.UNRELATED)
                            .add(label4)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(scrollPane2, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                                .add(createNewButton)
                                .add(cancelButton)
                                .add(applyButton)
                                .add(deleteButton)))
                        .add(scrollPane1, GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JList accountsList;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JScrollPane scrollPane2;
    private JTable table1;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton createNewButton;
    private JButton applyButton;
    private JButton cancelButton;
    private JButton deleteButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private class AccountWrapper {
        public AccountsManager.Account account;
        public boolean changed = false;

        /**
         * Returns a string representation of the object. In general, the
         * <code>toString</code> method returns a string that
         * "textually represents" this object. The result should
         * be a concise but informative representation that is easy for a
         * person to read.
         * It is recommended that all subclasses override this method.
         * <p/>
         * The <code>toString</code> method for class <code>Object</code>
         * returns a string consisting of the name of the class of which the
         * object is an instance, the at-sign character `<code>@</code>', and
         * the unsigned hexadecimal representation of the hash code of the
         * object. In other words, this method returns a string equal to the
         * value of:
         * <blockquote>
         * <pre>
         * getClass().getName() + '@' + Integer.toHexString(hashCode())
         * </pre></blockquote>
         *
         * @return a string representation of the object.
         */
        @Override
        public String toString() {
            return account.getUsername();
        }
    }
}

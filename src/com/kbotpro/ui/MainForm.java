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
 * Created by JFormDesigner on Thu Jul 30 15:13:49 CEST 2009
 */

package com.kbotpro.ui;


import com.kbotpro.handlers.AccountsManager;
import com.kbotpro.utils.BotControl;
import com.kbotpro.various.StaticStorage;
import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.AllPermission;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Jan Ove
 */
public class MainForm extends JFrame {
    public MainForm() {
        StaticStorage.mainForm = this;
        initComponents();

        try {
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
        } catch (Throwable throwable) {
            Logger.getRootLogger().error("Exception: ", throwable);
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
        addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {

            }

            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(StaticStorage.mainForm, "Are you sure you would like to quit?",
                        "Are you sure you would like to quit?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }

            public void windowClosed(WindowEvent e) {

            }

            public void windowIconified(WindowEvent e) {

            }

            public void windowDeiconified(WindowEvent e) {

            }

            public void windowActivated(WindowEvent e) {
                final BotPanel bot = getOpenedBotPanel();
                if (bot != null) {
                    bot.onBotTabSelected();
                }
            }

            public void windowDeactivated(WindowEvent e) {

            }
        });
       // globalLogArea.append("KBot PRO by Kosaki has started.\n");
        //globalLogArea.append("Copyright © 2009 Jan Ove Saltvedt.\n");
       // globalLogArea.append("Welcome " + StaticStorage.userStorage.getUsername() + "!\n");
        new Thread(new Runnable() {
            public void run() {
                StaticStorage.serverCom.getSettings();
                updateSettingCheckboxes();
            }
        }, "Startup thread").start();

        // remove the switch tab listener from the tabbedpane
        //mainTabbedPane.setFocusable(false);
        //setFocusable(false);
    }


    private void aboutMenuItemActionPerformed() {
        final MainForm mainForm = this;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AboutUI(mainForm).setVisible(true);
            }
        });
    }

    private void newBotMenuItemActionPerformed() {
        final Thread t = new Thread(new Runnable() {
            public void run() {
                //String ans = JOptionPane.showInputDialog(null, "Bot name?");
                java.util.List<AccountsManager.Account> accountsList = StaticStorage.accountsManager.getAccounts();
                /*AccountsManager.Account account = (AccountsManager.Account)JOptionPane.showInputDialog(null, "Select an account to use", "Select an account to use", JOptionPane.QUESTION_MESSAGE, null, accountsList.toArray(), accountsList.toArray()[0]);
                if (account == null) {
                    return;
                }
                if(JOptionPane.showOptionDialog(StaticStorage.mainForm, "Is this a members account?", "Is this a members account?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "NO"}, "Yes") == JOptionPane.YES_OPTION){
                    account.membersAccount = true;
                }
                String ans = account.getUsername();
                if (ans == null) {
                    return;
                }
                */
                new AccountSelector(accountsList.toArray());
                //try {
                //    wait(50);
                //} catch(InterruptedException ignored) { }
                //BotControl.addBot(account);
            }
        }, "Temp thread");
        t.start();

    }

    public BotPanel getOpenedBotPanel() {
        Object obj = mainTabbedPane.getSelectedComponent();
        if (obj == null) {
            return null;
        }
        if (obj instanceof BotPanel) {
            return (BotPanel) obj;
        } else {
            return null;
        }
    }

    private void reflectionExplorerMenuItemActionPerformed(ActionEvent e) {
        final BotPanel bot = getOpenedBotPanel();
        if (bot == null) {
            JOptionPane.showMessageDialog(this, "Please select a bot before opening a reflection explorer.");
            return;
        }
        if (bot.botEnvironment == null) {
            JOptionPane.showMessageDialog(this, "This bot has not started yet.");
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ReflectionExplorer(bot.botEnvironment).setVisible(true);
            }
        });
    }

    private void settingsRevertButtonActionPerformed(ActionEvent e) {
        updateSettingCheckboxes();
    }

    private void settingsApplyButtonActionPerformed(ActionEvent e) {
        updateSettings();
        new Thread(new Runnable() {
            public void run() {
                HashMap<String, String> settings = StaticStorage.userStorage.settings;
                boolean enableDebugs = (settings.get("enableDebugs") != null && settings.get("enableDebugs").equals("true"));
                if (enableDebugs != debugsCheckBox.isSelected()) {
                    if (!StaticStorage.serverCom.setSettingServerSide("enableDebugs", "" + debugsCheckBox.isSelected())) {
                        JOptionPane.showMessageDialog(null, "Failed to upload settings to server.");
                    }
                    settings.put("enableDebugs", "" + debugsCheckBox.isSelected());
                }
                boolean devMode = (settings.get("devMode") != null && settings.get("devMode").equals("true"));
                if (devMode != devModeCheckBox.isSelected()) {
                    if (!StaticStorage.serverCom.setSettingServerSide("devMode", "" + devModeCheckBox.isSelected())) {
                        JOptionPane.showMessageDialog(null, "Failed to upload settings to server.");
                    }
                    settings.put("devMode", "" + devModeCheckBox.isSelected());
                }
            }
        }, "ServerCom update settings").start();
    }

    private void startScriptMenuItemActionPerformed(ActionEvent e) {
        final BotPanel bot = getOpenedBotPanel();
        if (bot == null) {
            JOptionPane.showMessageDialog(this, "Please select a bot before starting a script");
            return;
        }
        if (bot.botEnvironment == null) {
            JOptionPane.showMessageDialog(this, "This bot has not started yet.");
            return;
        }
        bot.botEnvironment.randomManager.scriptStopped = false;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ScriptSelector(bot);
            }
        });
    }

    private void mainTabbedPaneStateChanged(ChangeEvent e) {
        final BotPanel bot = getOpenedBotPanel();
        if (bot == null) {
            toggleInput.setEnabled(false);
            pauseScriptButton.setEnabled(false);
            debuggersMenu.setEnabled(false);
            screenShotButton.setEnabled(false);
            randomManagerMenuItem.setEnabled(false);
            startScriptMenuItem.setEnabled(false);
            stopScriptMenuITem.setEnabled(false);
            closeBotMenuItem.setEnabled(false);
            reflectionExplorerMenuItem.setEnabled(false);
            if (botMenu.getMenuComponentCount() > 2) {
                botMenu.remove(2);
                botMenu.remove(2);
            }
            return;
        }
        if (botMenu.getMenuComponentCount() < 3) {
            botMenu.addSeparator();
            botMenu.add("Change account for tab").addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new AccountSelector(StaticStorage.accountsManager.getAccounts().toArray(), bot);
                }
            })  ;
        }
        toggleInput.setEnabled(true);
        pauseScriptButton.setEnabled(true);
        debuggersMenu.setEnabled(true);
        screenShotButton.setEnabled(true);
        randomManagerMenuItem.setEnabled(true);
        startScriptMenuItem.setEnabled(true);
        stopScriptMenuITem.setEnabled(true);
        closeBotMenuItem.setEnabled(true);
        reflectionExplorerMenuItem.setEnabled(true);
        bot.onBotTabSelected();
    }

    private void toggleInputItemStateChanged(ItemEvent e) {
        final BotPanel bot = getOpenedBotPanel();
        if (bot == null) {
            return;
        }
        bot.toggleInputItemStateChanged(e);
    }

    private void stopScriptMenuITemActionPerformed() {
        final BotPanel bot = getOpenedBotPanel();
        if (bot == null) {
            JOptionPane.showMessageDialog(this, "Please select a bot before opening the script manager.");
            return;
        }
        if (bot.botEnvironment == null) {
            JOptionPane.showMessageDialog(this, "This bot has not started yet.");
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (bot.botEnvironment.scriptManager.runningScripts.size() == 1) {
                    bot.botEnvironment.scriptManager.stopAllScripts();
                    bot.botEnvironment.randomManager.scriptStopped = true;
                    return;
                }
                new ScriptManager(bot.botEnvironment).setVisible(true);
            }
        });

    }

    private String defaultPath;

    private void startScriptLocalActionPerformed() {
        final BotPanel bot = getOpenedBotPanel();
        if (bot == null) {
            JOptionPane.showMessageDialog(this, "Please select a bot before starting a script.");
            return;
        }
        if (bot.botEnvironment == null) {
            JOptionPane.showMessageDialog(this, "This bot has not started yet.");
            return;
        }
        bot.botEnvironment.randomManager.scriptStopped = false;
        JFileChooser fc = null;
        if (defaultPath == null) {
            fc = new JFileChooser();
        } else {
            fc = new JFileChooser(defaultPath);
        }

        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jar");
            }

            @Override
            public String getDescription() {
                return ".jar (KBot Script files)";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);
        int ret = fc.showOpenDialog(this);

        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            defaultPath = file.getParent();
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fileInputStream.read(data);
                final ArrayList<Permission> permissions = new ArrayList<Permission>();
                permissions.add(new AllPermission());
                bot.botEnvironment.scriptManager.startScript(data, false, permissions);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "An error occured.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "An error occured.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ret == JFileChooser.ERROR) {
            JOptionPane.showMessageDialog(this, "An error occured.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void vmOutMenuItemActionPerformed() {
        new Console().setVisible(true);
    }

    protected void removeTab(BotPanel botPanel) {
        if (botPanel == null) {
            return;
        }
        mainTabbedPane.remove(botPanel);

    }

    private void closeBotMenuItemActionPerformed() {
        new Thread(new Runnable() {
            public void run() {
                final BotPanel bot = getOpenedBotPanel();
                if (bot == null) {
                    JOptionPane.showMessageDialog(MainForm.this, "Please select a bot before trying to close it. (duh)");
                    return;
                }
                if (bot.botEnvironment == null) {
                    JOptionPane.showMessageDialog(MainForm.this, "This bot has not started yet.");
                    return;
                }
                bot.destroy();
                BotControl.bots.remove(bot);
                removeTab(bot);
            }
        }).start();

    }

    public void pauseScriptButtonActionPerformed() {
        final BotPanel bot = getOpenedBotPanel();
        if (bot == null) {
            JOptionPane.showMessageDialog(this, "Please select a bot with a running script first");
            return;
        }
        if (bot.botEnvironment == null) {
            JOptionPane.showMessageDialog(this, "This bot has not started yet.");
            return;
        }
        bot.onPauseScriptButtonPressed();
    }

    private void accountManagerMenuItemActionPerformed() {
        new AccountManager().setVisible(true);
    }

    private void screenShotButtonActionPerformed(ActionEvent e) {
        final BotPanel bot = getOpenedBotPanel();
        if (bot == null) {
            JOptionPane.showMessageDialog(this, "Please select a bot.");
            return;
        }
        if (bot.botEnvironment == null) {
            JOptionPane.showMessageDialog(this, "This bot has not started yet.");
            return;
        }
        bot.botEnvironment.screenshot.takeScreenshot();
    }

    private void randomManagerMenuItemActionPerformed(ActionEvent e) {
        final BotPanel bot = getOpenedBotPanel();
        if (bot == null) {
            JOptionPane.showMessageDialog(this, "Please select a bot.");
            return;
        }
        if (bot.botEnvironment == null) {
            JOptionPane.showMessageDialog(this, "This bot has not started yet.");
            return;
        }
        new RandomManager(bot.botEnvironment);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        botMenu = new JMenu();
        newBotMenuItem = new JMenuItem();
        closeBotMenuItem = new JMenuItem();
        menu2 = new JMenu();
        startScriptMenuItem = new JMenuItem();
        stopScriptMenuITem = new JMenuItem();
        menu1 = new JMenu();
        accountManagerMenuItem = new JMenuItem();
        randomManagerMenuItem = new JMenuItem();
        devToolsMenu = new JMenu();
        startScriptLocal = new JMenuItem();
        vmOutMenuItem = new JMenuItem();
        reflectionExplorerMenuItem = new JMenuItem();
        debuggersMenu = new JMenu();
        helpMenu = new JMenu();
        aboutMenuItem = new JMenuItem();
        separator1 = new JPopupMenu.Separator();
        screenShotButton = new JButton();
        pauseScriptButton = new JButton();
        toggleInput = new JToggleButton();
        mainTabbedPane = new JTabbedPane();
        panel1 = new JPanel();
        tabbedPane2 = new JTabbedPane();
        scrollPane1 = new JScrollPane();
        botOverview = new BotOverview();
        panel2 = new JPanel();
        debugsCheckBox = new JCheckBox();
        devModeCheckBox = new JCheckBox();
        settingsApplyButton = new JButton();
        settingsRevertButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("KBot PRO");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== botMenu ========
            {
                botMenu.setText("Bot");

                //---- newBotMenuItem ----
                newBotMenuItem.setText("New bot");
                newBotMenuItem.setIcon(new ImageIcon(MainForm.class.getResource("/com/jtattoo/plaf/acryl/icons/TreeClosedButton.gif")));
                newBotMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        newBotMenuItemActionPerformed();
                    }
                });
                botMenu.add(newBotMenuItem);

                //---- closeBotMenuItem ----
                closeBotMenuItem.setText("Close bot");
                closeBotMenuItem.setIcon(new ImageIcon(MainForm.class.getResource("/com/jtattoo/plaf/acryl/icons/TreeOpenButton.gif")));
                closeBotMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        closeBotMenuItemActionPerformed();
                    }
                });
                botMenu.add(closeBotMenuItem);
            }
            menuBar1.add(botMenu);

            //======== menu2 ========
            {
                menu2.setText("Scripts");

                //---- startScriptMenuItem ----
                startScriptMenuItem.setText("Start script");
                startScriptMenuItem.setIcon(new ImageIcon(MainForm.class.getResource("/images/NewScriptIcon.gif")));
                startScriptMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        startScriptMenuItemActionPerformed(e);
                    }
                });
                menu2.add(startScriptMenuItem);

                //---- stopScriptMenuITem ----
                stopScriptMenuITem.setText("Stop script");
                stopScriptMenuITem.setIcon(new ImageIcon(MainForm.class.getResource("/images/StopScriptIcon.gif")));
                stopScriptMenuITem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        stopScriptMenuITemActionPerformed();
                    }
                });
                menu2.add(stopScriptMenuITem);
            }
            menuBar1.add(menu2);

            //======== menu1 ========
            {
                menu1.setText("Tools");

                //---- accountManagerMenuItem ----
                accountManagerMenuItem.setText("Account Manager");
                //accountManagerMenuItem.setSelectedIcon(null);
                accountManagerMenuItem.setIcon(new ImageIcon(MainForm.class.getResource("/images/AccountManagerIcon.gif")));
                accountManagerMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        accountManagerMenuItemActionPerformed();
                    }
                });
                menu1.add(accountManagerMenuItem);

                //---- randomManagerMenuItem ----
                randomManagerMenuItem.setText("Random Manager");
                randomManagerMenuItem.setIcon(new ImageIcon(MainForm.class.getResource("/images/RandomManagerIcon.gif")));
                randomManagerMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        randomManagerMenuItemActionPerformed(e);
                    }
                });
                menu1.add(randomManagerMenuItem);

                //======== devToolsMenu ========
                {
                    devToolsMenu.setText("Developer tools");
                    devToolsMenu.setIcon(new ImageIcon(MainForm.class.getResource("/images/DeveloperToolsIcon.gif")));
                    //---- startScriptLocal ----
                    startScriptLocal.setText("Start script local");
                    startScriptLocal.setIcon(new ImageIcon(MainForm.class.getResource("/images/NewScriptIcon.gif")));
                    startScriptLocal.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            startScriptLocalActionPerformed();
                        }
                    });
                    devToolsMenu.add(startScriptLocal);

                    //---- vmOutMenuItem ----
                    vmOutMenuItem.setText("Display VM output");
                    vmOutMenuItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            vmOutMenuItemActionPerformed();
                        }
                    });
                    devToolsMenu.add(vmOutMenuItem);

                    //---- reflectionExplorerMenuItem ----
                    reflectionExplorerMenuItem.setText("Reflection Explorer");
                    reflectionExplorerMenuItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            reflectionExplorerMenuItemActionPerformed(e);
                        }
                    });
                    devToolsMenu.add(reflectionExplorerMenuItem);
                }
                menu1.add(devToolsMenu);
            }
            menuBar1.add(menu1);

            //======== debuggersMenu ========
            {
                debuggersMenu.setText("Debuggers");
                debuggersMenu.setVisible(false);
            }
            menuBar1.add(debuggersMenu);

            //======== helpMenu ========
            {
                helpMenu.setText("Help");

                //---- aboutMenuItem ----
                aboutMenuItem.setText("About");
                aboutMenuItem.setIcon(new ImageIcon(MainForm.class.getResource("/images/AboutMenuIcon.gif")));
                aboutMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        aboutMenuItemActionPerformed();
                    }
                });
                helpMenu.add(aboutMenuItem);
            }
            menuBar1.add(helpMenu);
            //separator1.setVisible(false);
            menuBar1.add(separator1);

            //---- screenShotButton ----
            screenShotButton.setText("Screenshot");
            screenShotButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    screenShotButtonActionPerformed(e);
                }
            });
            screenShotButton.setFocusable(false);
            menuBar1.add(screenShotButton);

            //---- pauseScriptButton ----
            pauseScriptButton.setText("Pause");
            pauseScriptButton.setEnabled(false);
            pauseScriptButton.setIcon(new ImageIcon(MainForm.class.getResource("/images/PauseScriptIcon.gif")));
            pauseScriptButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pauseScriptButtonActionPerformed();
                }
            });
            pauseScriptButton.setFocusable(false);
            menuBar1.add(pauseScriptButton);

            //---- toggleInput ----
            toggleInput.setText("Input [Enabled]");
            toggleInput.setEnabled(false);
            toggleInput.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    toggleInputItemStateChanged(e);
                }
            });
            toggleInput.setFocusable(false);
            menuBar1.add(toggleInput);
        }
        setJMenuBar(menuBar1);

        //======== mainTabbedPane ========
        {
            mainTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            mainTabbedPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    mainTabbedPaneStateChanged(e);
                }
            });
            mainTabbedPane.setFocusable(false);

            //======== panel1 ========
            {
                panel1.setLayout(new BorderLayout());

                //======== tabbedPane2 ========
                {
                    tabbedPane2.setTabPlacement(SwingConstants.LEFT);

                    //======== scrollPane1 ========
                    {

                        //---- globalLogArea ----
                        //globalLogArea.setEditable(false);
                        //globalLogArea.setBackground(Color.white);
                        scrollPane1.setViewportView(botOverview);
                    }
                    tabbedPane2.addTab("Overview", scrollPane1);


                    //======== panel2 ========
                    {

                        //---- debugsCheckBox ----
                        debugsCheckBox.setText("Enable debugs");

                        //---- devModeCheckBox ----
                        devModeCheckBox.setText("Developer mode");

                        //---- settingsApplyButton ----
                        settingsApplyButton.setText("Apply");
                        settingsApplyButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                settingsApplyButtonActionPerformed(e);
                            }
                        });

                        //---- settingsRevertButton ----
                        settingsRevertButton.setText("Revert");
                        settingsRevertButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                settingsRevertButtonActionPerformed(e);
                            }
                        });

                        GroupLayout panel2Layout = new GroupLayout(panel2);
                        panel2.setLayout(panel2Layout);
                        panel2Layout.setHorizontalGroup(
                                panel2Layout.createParallelGroup()
                                        .add(panel2Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(panel2Layout.createParallelGroup()
                                                        .add(debugsCheckBox)
                                                        .add(devModeCheckBox))
                                                .addContainerGap(602, Short.MAX_VALUE))
                                        .add(GroupLayout.TRAILING, panel2Layout.createSequentialGroup()
                                        .addContainerGap(598, Short.MAX_VALUE)
                                        .add(settingsApplyButton)
                                        .addPreferredGap(LayoutStyle.RELATED)
                                        .add(settingsRevertButton)
                                        .addContainerGap())
                        );
                        panel2Layout.setVerticalGroup(
                                panel2Layout.createParallelGroup()
                                        .add(panel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .add(debugsCheckBox)
                                        .addPreferredGap(LayoutStyle.RELATED)
                                        .add(devModeCheckBox)
                                        .addPreferredGap(LayoutStyle.RELATED, 417, Short.MAX_VALUE)
                                        .add(panel2Layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(settingsApplyButton)
                                        .add(settingsRevertButton)))
                        );
                    }
                    tabbedPane2.addTab("Setting", panel2);

                }
                panel1.add(tabbedPane2, BorderLayout.CENTER);
            }
            mainTabbedPane.addTab("Control", panel1);

        }
        contentPane.add(mainTabbedPane, BorderLayout.CENTER);
        setSize(795, 625);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - josh hurr
    private JMenuBar menuBar1;
    public JMenu botMenu;
    private JMenuItem newBotMenuItem;
    private JMenuItem closeBotMenuItem;
    private JMenu menu2;
    private JMenuItem startScriptMenuItem;
    private JMenuItem stopScriptMenuITem;
    private JMenu menu1;
    private JMenuItem accountManagerMenuItem;
    private JMenuItem randomManagerMenuItem;
    private JMenu devToolsMenu;
    private JMenuItem startScriptLocal;
    private JMenuItem vmOutMenuItem;
    private JMenuItem reflectionExplorerMenuItem;
    public JMenu debuggersMenu;
    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;
    private JPopupMenu.Separator separator1;
    private JButton screenShotButton;
    public JButton pauseScriptButton;
    public JToggleButton toggleInput;
    public JTabbedPane mainTabbedPane;
    private JPanel panel1;
    private JTabbedPane tabbedPane2;
    private JScrollPane scrollPane1;
    public BotOverview botOverview;
    private JPanel panel2;
    private JCheckBox debugsCheckBox;
    private JCheckBox devModeCheckBox;
    private JButton settingsApplyButton;
    private JButton settingsRevertButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void updateSettingCheckboxes() {
        HashMap<String, String> settings = StaticStorage.userStorage.settings;
        debugsCheckBox.setSelected((settings.get("enableDebugs") != null && settings.get("enableDebugs").equals("true")));
        devModeCheckBox.setSelected((settings.get("devMode") != null && settings.get("devMode").equals("true")));
        updateSettings();
    }

    private void updateSettings() {
        if (debugsCheckBox.isSelected()) {
            debuggersMenu.setVisible(true);
        } else {
            debuggersMenu.setVisible(false);
        }

        if (devModeCheckBox.isSelected()) {
            devToolsMenu.setVisible(true);
        } else {
            devToolsMenu.setVisible(false);
        }
    }

    static void openMainForm() {
        StaticStorage.mainForm = new MainForm();
        StaticStorage.mainForm.setVisible(true);
    }
}

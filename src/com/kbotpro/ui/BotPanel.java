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
 * Created by JFormDesigner on Fri Jul 31 16:04:59 CEST 2009
 */

package com.kbotpro.ui;


import com.kbotpro.Main;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.bot.RenderData;
import com.kbotpro.bot.injector.ClassData;
import com.kbotpro.bot.injector.Injector;
import com.kbotpro.handlers.AccountsManager;
import com.kbotpro.hooks.Client;
import com.kbotpro.hooks.RenderVars;
import com.kbotpro.hooks.Renderer;
import com.kbotpro.interfaces.ClassLoaderCallback;
import com.kbotpro.interfaces.ClientCallback;
import com.kbotpro.interfaces.PaintCallback;
import com.kbotpro.scriptsystem.graphics.KGraphics;
import com.kbotpro.utils.Constant;
import com.kbotpro.utils.RSLoaderClassLoader;
import com.kbotpro.utils.UsefulMethods;
import com.kbotpro.utils.VirtualBrowser;
import com.kbotpro.various.StaticStorage;
import com.kbotpro.various.logSystem.LogMessage;
import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Jan Ove
 */
public class BotPanel extends JPanel implements PaintCallback, ClassLoaderCallback {
    Logger logger = Logger.getRootLogger();

    public AccountsManager.Account botName;
    public ClassLoader loaderClassLoader;
    public ClassLoader botClassLoader;
    public Applet botApplet;
    public Client client;
    public BotEnvironment botEnvironment;
    public boolean customPaint = false;
    public String hooksXML;
    public int randomWaitTime = 1000;

    private boolean loading = true;

    private String loadingText = "Loading KBot...";
    private int loadingPercentage = 5;

    private ArrayList<LogMessage> logMessages = new ArrayList<LogMessage>();
    private boolean rsVersionSupported = true;
    private Document document;
    private boolean renderGround = true;
    private boolean renderObjects = true;

    public Client getClient(){
        return client;
    }

    public BotPanel(AccountsManager.Account botName) {
        this.botName = botName;
        initComponents();
        tabbedPane.setFocusable(false);
        //setFocusable(false);
        //botAppletPanel.requestFocus();
        botAppletPanel.requestFocus();
        if(!StaticStorage.userStorage.canUseCPUSaving()){
            disableGroundCheckbox.setEnabled(false);
            disableObjectRendering.setEnabled(false);
            randomSlider.setEnabled(false);
        }
    }

    public void addLogMessage(LogMessage message){
        if(logMessages.size() > 200){
            logMessages.remove(0); // Remove one
        }
        logMessages.add(message);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                updateLogPane();
            }
        });
    }

    private void updateLogPane(){
        try{
            boolean includeTimeStamp = includeTimeStampLogCheckBox.isSelected();
            boolean showErrors = errorLogShowCheckBox.isSelected();
            boolean showImportant = importantLogShowCheckBox.isSelected();
            boolean showNormal = normalLogShowCheckBox.isSelected();
            boolean showIrrelevant = irrelevantLogShowCheckBox.isSelected();
            String html = "<html>\n" +
                    "  <head>\n" +
                    "  </head>\n" +
                    "  <body>";

            if(!logMessages.isEmpty()){
                List<LogMessage> logMessages = (List<LogMessage>)this.logMessages.clone();
                for(LogMessage logMessage: logMessages){
                    if(!showErrors && logMessage.type == LogMessage.LogType.error){
                        continue;
                    }
                    else if(!showImportant && logMessage.type == LogMessage.LogType.important){
                        continue;
                    }
                    else if(!showNormal && logMessage.type == LogMessage.LogType.normal){
                        continue;
                    }
                    else if(!showIrrelevant && logMessage.type == LogMessage.LogType.irrelevant){
                        continue;
                    }
                    html += logMessage.toHtml(includeTimeStamp)+"<br>";
                }
            }
            html += "  </body>\n" +
                    "</html>";
            logTextPane.setText(html);
        }catch(Exception e){
            // Work around for weird random jdk bug
            Logger.getRootLogger().error("Exception: ", e);
        }
    }

    private void logShowCheckBoxActionPerformed() {
        updateLogPane();
    }

    public void destroy() {

        if(botEnvironment != null){
            botEnvironment.scriptManager.stopAllScripts();
            botEnvironment.debuggerManager.disableAll();
            botEnvironment.disposeBot();
        }
        client = null;

        if(botApplet != null){
            botApplet.destroy();
        }
        botClassLoader = null;
        loaderClassLoader = null;
    }

    private void disableGroundCheckboxItemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            renderGround = false;
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
            renderGround = true;
        }
    }

    private void disableObjectRenderingItemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            renderObjects = false;
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
            renderObjects = true;
        }
    }

    private void slider1StateChanged() {
        label2.setText("Mouse Speed: " + slider1.getValue());
        this.botEnvironment.mouse.setDefaultSpeed(slider1.getValue() + Math.random());
    }

    private void randomSliderStateChanged() {
        randomWaitTime = randomSlider.getValue();
        label4.setText("Random Check sleep timer: " + randomSlider.getValue() + " ms");
    }

    private void displayDecorativesActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        tabbedPane = new JTabbedPane();
        botAppletPanel = new BotAppletPanel();
        panel1 = new JPanel();
        panel3 = new JPanel();
        settingMouseIndicator = new JCheckBox();
        settingDisplayWireframes = new JCheckBox();
        displayDecoratives = new JCheckBox();
        panel4 = new JPanel();
        disableGroundCheckbox = new JCheckBox();
        disableObjectRendering = new JCheckBox();
        label4 = new JLabel();
        randomSlider = new JSlider();
        panel5 = new JPanel();
        slider1 = new JSlider();
        label2 = new JLabel();
        panel2 = new JPanel();
        label1 = new JLabel();
        irrelevantLogShowCheckBox = new JCheckBox();
        normalLogShowCheckBox = new JCheckBox();
        importantLogShowCheckBox = new JCheckBox();
        errorLogShowCheckBox = new JCheckBox();
        scrollPane1 = new JScrollPane();
        logTextPane = new JEditorPane();
        includeTimeStampLogCheckBox = new JCheckBox();
        separator1 = new JSeparator();

        //======== this ========
        setBorder(null);
        setLayout(new BorderLayout());

        //======== tabbedPane ========
        {
            tabbedPane.setFocusable(false);

            //======== botAppletPanel ========
            {
                botAppletPanel.setLayout(null);

                { // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < botAppletPanel.getComponentCount(); i++) {
                        Rectangle bounds = botAppletPanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = botAppletPanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    botAppletPanel.setMinimumSize(preferredSize);
                    botAppletPanel.setPreferredSize(preferredSize);
                }
            }
            tabbedPane.addTab("Bot", botAppletPanel);


            //======== panel1 ========
            {

                //======== panel3 ========
                {
                    panel3.setBorder(new TitledBorder("Paint"));

                    //---- settingMouseIndicator ----
                    settingMouseIndicator.setText("Mouse indicator");
                    settingMouseIndicator.setSelected(true);
                    settingMouseIndicator.setToolTipText("If this is selected a mouse indicator will be drawn.");

                    //---- settingDisplayWireframes ----
                    settingDisplayWireframes.setText("Display wireframes (debug)");
                    settingDisplayWireframes.setToolTipText("If this is selected model wireframes will be displayed in debugs.");

                    //---- displayDecoratives ----
                    displayDecoratives.setText("Display decorative obejcts (debug)");
                    displayDecoratives.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            displayDecorativesActionPerformed(e);
                        }
                    });

                    GroupLayout panel3Layout = new GroupLayout(panel3);
                    panel3.setLayout(panel3Layout);
                    panel3Layout.setHorizontalGroup(
                        panel3Layout.createParallelGroup()
                            .add(panel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(panel3Layout.createParallelGroup()
                                    .add(settingMouseIndicator)
                                    .add(settingDisplayWireframes)
                                    .add(displayDecoratives))
                                .addContainerGap(95, Short.MAX_VALUE))
                    );
                    panel3Layout.setVerticalGroup(
                        panel3Layout.createParallelGroup()
                            .add(panel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(settingMouseIndicator)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(settingDisplayWireframes)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(displayDecoratives)
                                .addContainerGap(360, Short.MAX_VALUE))
                    );
                }

                //======== panel4 ========
                {
                    panel4.setBorder(new TitledBorder("CPU Saving (Donators only)"));

                    //---- disableGroundCheckbox ----
                    disableGroundCheckbox.setText("Disable ground rendering");
                    disableGroundCheckbox.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                            disableGroundCheckboxItemStateChanged(e);
                        }
                    });

                    //---- disableObjectRendering ----
                    disableObjectRendering.setText("Disable game object rendering");
                    disableObjectRendering.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                            disableObjectRenderingItemStateChanged(e);
                        }
                    });

                    //---- label4 ----
                    label4.setText("Random Check sleep timer: 100 ms");

                    //---- randomSlider ----
                    randomSlider.setMaximum(2000);
                    randomSlider.setMinimum(100);
                    randomSlider.setValue(100);
                    randomSlider.setMajorTickSpacing(500);
                    randomSlider.setMinorTickSpacing(100);
                    randomSlider.addChangeListener(new ChangeListener() {
                        public void stateChanged(ChangeEvent e) {
                            randomSliderStateChanged();
                        }
                    });

                    GroupLayout panel4Layout = new GroupLayout(panel4);
                    panel4.setLayout(panel4Layout);
                    panel4Layout.setHorizontalGroup(
                        panel4Layout.createParallelGroup()
                            .add(panel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(panel4Layout.createParallelGroup(GroupLayout.TRAILING, false)
                                    .add(GroupLayout.LEADING, randomSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(GroupLayout.LEADING, disableGroundCheckbox)
                                    .add(GroupLayout.LEADING, disableObjectRendering, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(GroupLayout.LEADING, label4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(25, Short.MAX_VALUE))
                    );
                    panel4Layout.setVerticalGroup(
                        panel4Layout.createParallelGroup()
                            .add(panel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(disableGroundCheckbox)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(disableObjectRendering)
                                .add(18, 18, 18)
                                .add(label4)
                                .addPreferredGap(LayoutStyle.UNRELATED)
                                .add(randomSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(324, Short.MAX_VALUE))
                    );
                }

                //======== panel5 ========
                {
                    panel5.setBorder(new TitledBorder("Default Script Settings"));

                    //---- slider1 ----
                    slider1.setMaximum(3);
                    slider1.setMinimum(1);
                    slider1.setValue(1);
                    slider1.setBorder(null);
                    slider1.addChangeListener(new ChangeListener() {
                        public void stateChanged(ChangeEvent e) {
                            slider1StateChanged();
                        }
                    });

                    //---- label2 ----
                    label2.setText("Mouse Speed: 1");

                    GroupLayout panel5Layout = new GroupLayout(panel5);
                    panel5.setLayout(panel5Layout);
                    panel5Layout.setHorizontalGroup(
                        panel5Layout.createParallelGroup()
                            .add(panel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(panel5Layout.createParallelGroup()
                                    .add(panel5Layout.createSequentialGroup()
                                        .add(slider1, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                                        .addContainerGap())
                                    .add(panel5Layout.createSequentialGroup()
                                        .add(label2, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                        .add(34, 34, 34))))
                    );
                    panel5Layout.setVerticalGroup(
                        panel5Layout.createParallelGroup()
                            .add(panel5Layout.createSequentialGroup()
                                .add(4, 4, 4)
                                .add(label2)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(slider1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(396, Short.MAX_VALUE))
                    );
                }

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .add(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .add(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(panel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(18, 18, 18))
                );
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .add(GroupLayout.TRAILING, panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .add(panel1Layout.createParallelGroup(GroupLayout.TRAILING)
                                .add(panel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(GroupLayout.LEADING, panel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(GroupLayout.LEADING, panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap())
                );
            }
            tabbedPane.addTab("Settings", panel1);


            //======== panel2 ========
            {

                //---- label1 ----
                label1.setText("Show:");

                //---- irrelevantLogShowCheckBox ----
                irrelevantLogShowCheckBox.setText("Irrelevant");
                irrelevantLogShowCheckBox.setSelected(true);
                irrelevantLogShowCheckBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        logShowCheckBoxActionPerformed();
                    }
                });

                //---- normalLogShowCheckBox ----
                normalLogShowCheckBox.setText("Normal");
                normalLogShowCheckBox.setSelected(true);
                normalLogShowCheckBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        logShowCheckBoxActionPerformed();
                    }
                });

                //---- importantLogShowCheckBox ----
                importantLogShowCheckBox.setText("Important");
                importantLogShowCheckBox.setSelected(true);
                importantLogShowCheckBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        logShowCheckBoxActionPerformed();
                    }
                });

                //---- errorLogShowCheckBox ----
                errorLogShowCheckBox.setText("Error");
                errorLogShowCheckBox.setSelected(true);
                errorLogShowCheckBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        logShowCheckBoxActionPerformed();
                    }
                });

                //======== scrollPane1 ========
                {

                    //---- logTextPane ----
                    logTextPane.setContentType("text/html");
                    logTextPane.setEditable(false);
                    scrollPane1.setViewportView(logTextPane);
                }

                //---- includeTimeStampLogCheckBox ----
                includeTimeStampLogCheckBox.setText("Time Stamp");
                includeTimeStampLogCheckBox.setSelected(true);

                //---- separator1 ----
                separator1.setOrientation(SwingConstants.VERTICAL);

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .add(panel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .add(panel2Layout.createParallelGroup()
                                .add(GroupLayout.TRAILING, scrollPane1, GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
                                .add(panel2Layout.createSequentialGroup()
                                    .add(label1)
                                    .addPreferredGap(LayoutStyle.RELATED, 349, Short.MAX_VALUE)
                                    .add(includeTimeStampLogCheckBox)
                                    .addPreferredGap(LayoutStyle.RELATED)
                                    .add(separator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.RELATED)
                                    .add(errorLogShowCheckBox)
                                    .addPreferredGap(LayoutStyle.RELATED)
                                    .add(importantLogShowCheckBox)
                                    .addPreferredGap(LayoutStyle.RELATED)
                                    .add(normalLogShowCheckBox)
                                    .addPreferredGap(LayoutStyle.UNRELATED)
                                    .add(irrelevantLogShowCheckBox)))
                            .addContainerGap())
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .add(panel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .add(panel2Layout.createParallelGroup()
                                .add(label1)
                                .add(includeTimeStampLogCheckBox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(GroupLayout.TRAILING, irrelevantLogShowCheckBox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(GroupLayout.TRAILING, normalLogShowCheckBox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(GroupLayout.TRAILING, importantLogShowCheckBox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(GroupLayout.TRAILING, errorLogShowCheckBox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(separator1, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                            .add(18, 18, 18)
                            .add(scrollPane1, GroupLayout.PREFERRED_SIZE, 433, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                );
            }
            tabbedPane.addTab("Log", panel2);

        }
        add(tabbedPane, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public boolean hasClassLoader() {
        return botClassLoader != null;
    }

    public ClassLoader getLoaderClassLoader(){
        return loaderClassLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
       botClassLoader = classLoader;

       botApplet.start();
       botEnvironment = new BotEnvironment(this);
       botEnvironment.log.logImportant("KBot PRO by Kosaki started.\n" +
               "Copyright © 2009 Jan Ove Saltvedt.");
       loadingText = "Injecting hooks...";
       loadingPercentage += 10;
       botAppletPanel.repaint();
       new Thread(new Runnable() {
           public void run() {
               //Utils.compileHooks(hooksXML);
               updateUI();
               botEnvironment.log.logNormal("Runescape started.");
               customPaint = true;
           }
       }).start();
        botAppletPanel.addFocusListener(((BotAppletPanel)botAppletPanel).focusListener);
        botAppletPanel.addMouseListener(((BotAppletPanel)botAppletPanel).mouseListener);
        botAppletPanel.addMouseMotionListener(((BotAppletPanel)botAppletPanel).mouseMotionListener);
        botAppletPanel.addMouseWheelListener(((BotAppletPanel)botAppletPanel).mouseWheelListener);
        botAppletPanel.addKeyListener(((BotAppletPanel)botAppletPanel).keyListener);
	    botAppletPanel.setFocusTraversalKeys(0, new HashSet<AWTKeyStroke>());
    }

    public Graphics getClientGraphics() {
        if(loading){
            loading = false;
        }
        final BotAppletPanel botApplet = (BotAppletPanel) botAppletPanel;
        final Graphics graphics = botApplet.clientBuffer.getGraphics();
        // Copy client image to back buffer
        Graphics g = botApplet.backBuffer.getGraphics();
        g.drawImage(botApplet.clientBuffer,0,0, null);

        // Make screen repaint
        botApplet.repaint();
        return graphics;
    }

    public ClassData injectClass(String name, byte[] bytes, int off, int len, ProtectionDomain protectionDomain) {
        ClassData classData = new ClassData();
        classData.name = name;
        classData.classData = bytes;
        classData.offset = off;
        classData.length = len;
        classData.protectionDomain = protectionDomain;
        if(!rsVersionSupported){
            return classData;
        }
        try{

            Document doc = document;
            return Injector.inject(classData, doc);
        }catch(IllegalStateException e){
            rsVersionSupported = false;
        }
        catch (Exception e){
            // This is a gateway to the client so everything should be catched so it does not get sent to the client
            Logger.getRootLogger().error("Exception: ", e);
        }
        return classData;
    }

    public void setClient(Applet applet) {
        if(applet instanceof Client){
            client = (Client) applet;
            botEnvironment.client = client;
            client.setCallback(new ClientCallback(){
                public void serverMessage(String message) {
                    try{
                        if(botEnvironment.serverMessageMulticaster != null){
                            botEnvironment.serverMessageMulticaster.onServerMessage(message);
                        }
                    }catch(Throwable e){
                        Logger.getRootLogger().error("Error thrown towards client: ", e);
                    }
                }

                public void updateRenderData() {
                    RenderData data = botEnvironment.renderData;
                    RenderVars renderVars = botEnvironment.client.getGameRenderVars();
                    data.xOff = (int) renderVars.getXOff();
                    data.yOff = (int) renderVars.getYOff();
                    data.zOff = (int) renderVars.getZOff();

                    data.x1 = (int) renderVars.getX1();
                    data.x2 = (int) renderVars.getX2();
                    data.x3 = (int) renderVars.getX3();

                    data.y1 = (int) renderVars.getY1();
                    data.y2 = (int) renderVars.getY2();
                    data.y3 = (int) renderVars.getY3();

                    data.z1 = (int) renderVars.getZ1();
                    data.z2 = (int) renderVars.getZ2();
                    data.z3 = (int) renderVars.getZ3();

                    Renderer renderer = botEnvironment.client.getGameRenderer();

                    data.screenFactorX = renderer.getScreenFactorX();
                    data.screenFactorY = renderer.getScreenFactorY();

                    data.minX = renderer.getMinX();
                    data.minY = renderer.getMinY();

                    data.maxX = renderer.getMaxX();
                    data.maxY = renderer.getMaxY();
                }

                public void updateMenuData() {

                }

                public boolean disableGroundRender() {
                    return !renderGround;
                }

                public boolean disableObjectsRender() {
                    return !renderObjects;
                }
            });
        }
    }

    public boolean isInputEnabled(){
        BotAppletPanel botAppletPanel = (BotAppletPanel)this.botAppletPanel;
        return botAppletPanel.inputEnabled;

    }
    public void setInputEnabled(boolean enable){
        BotAppletPanel botAppletPanel = (BotAppletPanel)this.botAppletPanel;
        botAppletPanel.inputEnabled = enable;

        if(StaticStorage.mainForm.getOpenedBotPanel() == this){
            if(botAppletPanel.inputEnabled){
                StaticStorage.mainForm.toggleInput.setText("Input [Enabled]");
                StaticStorage.mainForm.toggleInput.setSelected(false);
            }
            else{
                StaticStorage.mainForm.toggleInput.setText("Input [Disabled]");
                StaticStorage.mainForm.toggleInput.setSelected(true);
            }
        }
    }

    public void onBotTabSelected() {
        if(botEnvironment != null){
            botEnvironment.debuggerManager.updateMenu();
        }
        BotAppletPanel botAppletPanel = (BotAppletPanel)this.botAppletPanel;

        botAppletPanel.requestFocus();
        botAppletPanel.requestFocusInWindow();
        if(botAppletPanel.inputEnabled){
            StaticStorage.mainForm.toggleInput.setText("Input [Enabled]");
            StaticStorage.mainForm.toggleInput.setSelected(false);
        } else {
            StaticStorage.mainForm.toggleInput.setText("Input [Disabled]");
            StaticStorage.mainForm.toggleInput.setSelected(true);
        }
        StaticStorage.mainForm.pauseScriptButton.setText(botEnvironment != null && botEnvironment.scriptManager.isScriptsPaused() ? "Resume" : "Pause");
        StaticStorage.mainForm.pauseScriptButton.setIcon(new ImageIcon(MainForm.class.getResource(
                botEnvironment != null && botEnvironment.scriptManager.isScriptsPaused() ? "/images/NewScriptIcon.gif" : "/images/PauseScriptIcon.gif")));
    }

    public void toggleInputItemStateChanged(ItemEvent e) {
        JToggleButton jToggleButton = (JToggleButton)e.getSource();
        if(!jToggleButton.isSelected()){ // Reversed as the change happens after the event are sent
            ((BotAppletPanel)botAppletPanel).inputEnabled = true;
            jToggleButton.setText("Input [Enabled]");
        }
        else{
            ((BotAppletPanel)botAppletPanel).inputEnabled = false;
            jToggleButton.setText("Input [Disabled]");
        }
    }

    public void onPauseScriptButtonPressed() {
        boolean paused = !botEnvironment.scriptManager.isScriptsPaused();
        botEnvironment.scriptManager.setPauseScripts(paused);
    }

    public class BotAppletPanel extends JPanel implements Runnable {
        public boolean inputEnabled = true;
        public boolean hasFocus = false;

        public BufferedImage clientBuffer;
        public BufferedImage backBuffer;

        private Font helvetica = new Font("Helvetica", Font.BOLD, 13);
        private FontMetrics helveticaFontMetrics = getFontMetrics(helvetica);

        private int width = 765;
        private int height = 503;
        BufferedImage logo = null;

        public MouseListener mouseListener = new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if(botEnvironment != null && botEnvironment.mouseMulticaster != null){
                    botEnvironment.mouseMulticaster.mouseClicked(e);
                }
                dispatchBotEvent(e);
            }

            public void mousePressed(MouseEvent e) {
                if(botEnvironment != null && botEnvironment.mouseMulticaster != null){
                    botEnvironment.mouseMulticaster.mousePressed(e);
                    botEnvironment.mouse.mousePressed = true;
                }
                dispatchBotEvent(e);
            }

            public void mouseReleased(MouseEvent e) {
                if(botEnvironment != null && botEnvironment.mouseMulticaster != null){
                    botEnvironment.mouseMulticaster.mouseReleased(e);
                    botEnvironment.mouse.mousePressed = false;
                }
                dispatchBotEvent(e);
            }

            public void mouseEntered(MouseEvent e) {
                if(botEnvironment != null && botEnvironment.mouseMulticaster != null){
                    botEnvironment.mouseMulticaster.mouseEntered(e);
                }
                dispatchBotEvent(e);
            }

            public void mouseExited(MouseEvent e) {
                if(botEnvironment != null && botEnvironment.mouseMulticaster != null){
                    botEnvironment.mouseMulticaster.mouseExited(e);
                }
                dispatchBotEvent(e);
            }
        };

        public MouseMotionListener mouseMotionListener = new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                if(botEnvironment != null && botEnvironment.mouseMotionMulticaster != null){
                    botEnvironment.mouseMotionMulticaster.mouseDragged(e);
                }
                dispatchBotEvent(e);
            }

            public void mouseMoved(MouseEvent e) {
                if(botEnvironment != null && botEnvironment.mouseMotionMulticaster != null){
                    botEnvironment.mouseMotionMulticaster.mouseMoved(e);
                }
                dispatchBotEvent(e);
            }
        };

        private void dispatchBotEvent(AWTEvent e){
            if(botApplet == null){
                return;
            }
            if(botApplet.getComponentCount() < 1){
                return;
            }
            Component component1 = botApplet.getComponent(0);
            if(component1 != null && (inputEnabled || e instanceof KeyEvent || e instanceof ComponentListener)){
                if (e instanceof MouseEvent) {
                    if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                        mousePressed = true;
                    } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                        mousePressed = false;
                    }
                }
                //System.out.println("Time: "+System.currentTimeMillis()+" "+e);
                component1.dispatchEvent(e);
            }
        }

        public KeyListener keyListener = new KeyListener() {
            public void keyTyped(KeyEvent e) {
                dispatchBotEvent(e);
            }

            public void keyPressed(KeyEvent e) {
                dispatchBotEvent(e);
            }

            public void keyReleased(KeyEvent e) {
                dispatchBotEvent(e);
            }
        };

        public MouseWheelListener mouseWheelListener = new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                dispatchBotEvent(e);
            }
        };

        public FocusListener focusListener = new FocusListener() {
            public void focusGained(FocusEvent e) {
                if(!botApplet.hasFocus()){
                    botApplet.requestFocus();
                }
            }

            public void focusLost(FocusEvent e) {
            }
        };



        public ComponentListener componentListener = new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                updateSize(e);
                if(botApplet != null)
                    dispatchBotEvent(e);

            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
            }
        };

        private void updateSize(ComponentEvent e){
            width = getWidth();
            height = getHeight();
            if (width < 0 || height < 0) {
                return;
            }
            clientBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
            backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
            if(botApplet != null)
                botApplet.resize(width, height);
            if(botEnvironment != null){
                botEnvironment.appletWidth = width;
                botEnvironment.appletHeight = height;
            }
            //System.out.println("Resize: Width: "+width+" Height: "+height);
        }

        /**
         * Returns whether this Component can be focused.
         *
         * @return <code>true</code> if this Component is focusable;
         *         <code>false</code> otherwise.
         * @see #setFocusable
         * @since 1.4
         */
        @Override
        public boolean isFocusable() {
            return true;
        }

        /**
         * Creates a new <code>JPanel</code> with a double buffer
         * and a flow layout.
         */
        private BotAppletPanel() {
            setFocusable(true);
            clientBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
            backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
            addComponentListener(componentListener);
            new Thread(this, botName + " loader").start();
            Set<AWTKeyStroke> set = new HashSet<AWTKeyStroke>();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(0, set);
            try {
                logo = ImageIO.read(BotPanel.class.getResource("/images/KBotLogo.png"));
            } catch(IOException e) {
                Logger.getRootLogger().error("Exception: ", e);
            }
        }

        /**
         * Calls the UI delegate's paint method, if the UI delegate
         * is non-<code>null</code>.  We pass the delegate a copy of the
         * <code>Graphics</code> object to protect the rest of the
         * paint code from irrevocable changes
         * (for example, <code>Graphics.translate</code>).
         * <p/>
         * If you override this in a subclass you should not make permanent
         * changes to the passed in <code>Graphics</code>. For example, you
         * should not alter the clip <code>Rectangle</code> or modify the
         * transform. If you need to do these operations you may find it
         * easier to create a new <code>Graphics</code> from the passed in
         * <code>Graphics</code> and manipulate it. Further, if you do not
         * invoker super's implementation you must honor the opaque property,
         * that is
         * if this component is opaque, you must completely fill in the background
         * in a non-opaque color. If you do not honor the opaque property you
         * will likely see visual artifacts.
         * <p/>
         * The passed in <code>Graphics</code> object might
         * have a transform other than the identify transform
         * installed on it.  In this case, you might get
         * unexpected results if you cumulatively apply
         * another transform.
         *
         * @param gr the <code>Graphics</code> object to protect
         * @see #paint
         * @see javax.swing.plaf.ComponentUI
         */
        @Override
        protected void paintComponent(Graphics gr) {

            Graphics2D g = (Graphics2D)gr.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (loading) {
                g.setColor(Color.black);
                Rectangle clipBounds = g.getClipBounds();
                g.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
                if (logo != null) {
                    g.drawImage(logo, 0, 0, null);
                }
                g.setColor(Color.white);
                g.drawRect(230, 239, 300, 37);

                //g.setColor(new Color(80, 80, 80));
                g.fillRect(232, 241, (int) (((double) loadingPercentage / 100D) * 296), 34); //34

                //g.setColor(new Color(67, 67, 67));
                //g.fillRect(232, 258, (int) (((double) loadingPercentage / 100D) * 296), 17);

                g.setFont(helvetica);
                g.setColor(new Color(140, 17, 17));
                //g.drawString("KBot PRO is loading - please wait...", 382 - helveticaFontMetrics.stringWidth("KBot PRO is loading - please wait...") / 2, 225);
                g.drawString(loadingText, (304 - helveticaFontMetrics.stringWidth(loadingText)) / 2 + 230, 264);
                return;
            }
            g.drawImage(backBuffer, 0, 0, null);
            try{
                if(customPaint){
                    if(botEnvironment.paintEventMulticaster != null){
                        botEnvironment.paintEventMulticaster.onRepaint(gr);
                    }
	                if(botEnvironment.kPaintEventMulticaster != null){
					botEnvironment.kPaintEventMulticaster.onRepaint(new KGraphics((Graphics2D) gr));
                    }
                    if(settingMouseIndicator.isSelected() && botEnvironment != null && botEnvironment.game != null){
                        Point m = botEnvironment.game.getMousePos();
                        if (botEnvironment.mouse.mousePressed || mousePressed) {
                            g.setColor(Color.red);
                            g.drawLine(m.x-6, m.y, m.x+6, m.y);
                            g.drawLine(m.x, m.y-6, m.x, m.y+6);
                        } else {
                            g.setColor(Color.white);
                            g.drawLine(m.x-6, m.y-6, m.x+6, m.y+6);
                            g.drawLine(m.x+6, m.y-6, m.x-6, m.y+6);
                        }
                    }
                }
            }catch (Exception e){
                Logger.getRootLogger().error("Exception: ", e);
            }

            //super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
        }
        public boolean mousePressed = false;
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p/>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run() {
            final int NAVIGATE_STEPS = 4;
            loadingText = "Navigating site [0/" + NAVIGATE_STEPS + "]";
            repaint();
            VirtualBrowser virtualBrowser = new VirtualBrowser();
            try {

                URL currentURL = new URL("http://runescape.com");
                URL lastURL = null;
                logger.debug("Downloading: "+currentURL);
                virtualBrowser.get(currentURL, lastURL, null);
                lastURL = currentURL;
                loadingText = "Navigating site [1/" + NAVIGATE_STEPS + "]";
                loadingPercentage += 10;
                repaint();
                UsefulMethods.sleep(100, 1000);
                currentURL = new URL("http://runescape.com/game.ws?m=" + UsefulMethods.random(1) + "&j=1");
                logger.debug("Downloading: "+currentURL);
                String html = virtualBrowser.get(currentURL, lastURL, null);
                logger.debug("Downloaded:\n"+html+"\n\n");
                lastURL = currentURL;
                loadingText = "Navigating site [2/" + NAVIGATE_STEPS + "]";
                loadingPercentage += 10;
                repaint();
                Pattern pattern = Pattern.compile("src=\"(http://world[0-9]+\\.runescape\\.com/)\"", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

                Matcher matcher = pattern.matcher(html);
                if (!matcher.find()) {
                    loadingText = "ERROR: Could not get world link!";
                    repaint();
                    return;
                }
                currentURL = new URL(matcher.group(1));
                logger.debug("Downloading: "+currentURL);
                UsefulMethods.sleep(100, 1000);

                pattern = Pattern.compile("(http://world[0-9]+\\.runescape\\.com)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                matcher = pattern.matcher(matcher.group(1));
                if (!matcher.find()) {
                    loadingText = "ERROR: Could not get base URL!";
                    repaint();
                    return;
                }
                String base = matcher.group(1);
                logger.debug("Base = "+base);
                logger.debug("Downloading: "+currentURL);
                html = virtualBrowser.get(currentURL, lastURL, null);
                logger.debug("Downloaded:\n"+html+"\n\n");
                lastURL = currentURL;
                loadingText = "Navigating site [3/" + NAVIGATE_STEPS + "]";
                loadingPercentage += 10;
                repaint();
                UsefulMethods.sleep(10, 200);

                pattern = Pattern.compile("src=\"(plugin\\.js\\?param=[a-zA-Z0-9,]+)&key=\"", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                matcher = pattern.matcher(html);
                if (!matcher.find()) {
                    loadingText = "ERROR: Could not get paramater link!";
                    System.err.println("HTML\n"+html);
                    repaint();
                    return;
                }

                currentURL = new URL(base + "/" + matcher.group(1));
                logger.debug("Downloading: "+currentURL);
                String xml = virtualBrowser.get(currentURL, lastURL, null);
                logger.debug("Downloaded:\n"+xml+"\n\n");
                loadingText = "Navigating site [4/" + NAVIGATE_STEPS + "]";
                loadingPercentage += 10;
                repaint();
                UsefulMethods.sleep(10, 200);

                pattern = Pattern.compile("document\\.write\\('archive=(loader[0-9\\-]+\\.jar) code=loader\\.class", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                matcher = pattern.matcher(xml);
                if (!matcher.find()) {
                    loadingText = "ERROR: Could not get loader-xxxxxx.jar link!";
                    repaint();
                    return;
                }

                loadingText = "Downloading hook list...";
                loadingPercentage += 5;
                repaint();

                String hooksURL = "http://" + Constant.SERVER_HOST + "/kbotpro/botcom/getHooks.php?ver=latest" + (Main.devMode ? "&dev=true" : "");
                hooksXML = virtualBrowser.get(new URL(hooksURL), null, null);
                try {
                    document = new SAXBuilder().build(new StringReader(hooksXML));
                } catch (JDOMException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                }

                currentURL = new URL(base + "/" + matcher.group(1));
                //currentURL = new URL(base+"/loader.jar");
                loadingText = "Downloading loader...";
                loadingPercentage += 5;
                repaint();

                File file = File.createTempFile(matcher.group(1).replace(".jar", ""), ".jar");
                byte[] bytes = virtualBrowser.getRaw(currentURL, lastURL, null);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
                fileOutputStream.close();
                file.deleteOnExit();


                loaderClassLoader = new RSLoaderClassLoader(file);
                final Class<?> loaderClass = loaderClassLoader.loadClass("loader");
                botApplet = (Applet) loaderClass.newInstance();
                botApplet.setSize(width, height);

                file.deleteOnExit();

                pattern = Pattern.compile("world([0-9]+)\\.runescape.com");
                matcher = pattern.matcher(base);
                matcher.find();

                String paramHTML = virtualBrowser.get(new URL(base+"/plugin.js?param=o0,a0,m0,s0"), lastURL, null);
                Stub stub = new Stub(botApplet, matcher.group(1), paramHTML);

                botApplet.setStub(stub);
                botApplet.init();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }

        private class Stub implements AppletStub, AppletContext, Enumeration<Applet> {
            private final HashMap<String, String> parameters = new HashMap<String, String>();
            private final HashMap<String, InputStream> appletStreams = new HashMap<String, InputStream>();
            private int nextElementCalled = 0;
            public String worldId;

            private Applet theApplet;
            private boolean ALLOW_SHOW_DOCUMENT;

            public Stub(Applet applet, String worldId, String plugin) {
                ALLOW_SHOW_DOCUMENT = true;
                this.worldId = worldId;
                this.theApplet = applet;

                try {
                    Pattern regex = Pattern.compile("<param name=\"?([a-zA-Z0-9_ \\-\\.\\.=]+)\"?\\s+value=\"?(([a-zA-Z0-9_ \\-\\.=\\?&:/]+))\"?>", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                    Matcher regexMatcher = regex.matcher(plugin);
                    while (regexMatcher.find()){
                        if (!parameters.containsKey(regexMatcher.group(1))){
                            if(regexMatcher.group(2) != null){
                                parameters.put(regexMatcher.group(1), regexMatcher.group(2));

                            }
                            else{
                                parameters.put(regexMatcher.group(1), "");
                            }
                        }
                    }
                    parameters.put("haveie6", "0");
                } catch (PatternSyntaxException ex) {
                    ex.printStackTrace();
                }
                return;
            }


            public void appletResize(int width, int height) {
                // Do not resize..
            }

            public final URL getCodeBase() {
                try {
                    return new URL("http://world" + worldId + ".runescape.com");
                } catch (Exception e) {
                    return null;
                }
            }

            public boolean isActive() {
                return false;
            }

            public final URL getDocumentBase() {
                try {
                    return new URL("http://world" + worldId + ".runescape.com/m0");
                } catch (Exception e) {
                    return null;
                }
            }

            public final String getParameter(String name) {
                return parameters.get(name);
            }

            public AppletContext getAppletContext() {
                return this;  //To change body of implemented methods use File | Settings | File Templates.
            }


            public AppletStub getStub() {
                return this;
            }

            /**
             * Creates an audio clip.
             *
             * @param url an absolute URL giving the location of the audio clip.
             * @return the audio clip at the specified URL.
             */
            public AudioClip getAudioClip(URL url) {
                return new AudioClipSub(url);
            }

            public Image getImage(URL url) {
                return Toolkit.getDefaultToolkit().createImage(url);
            }

            public Applet getApplet(String name) {
                return null;
            }

            public Enumeration<Applet> getApplets() {
                return this;
            }

            public void showDocument(URL url) {
                if(url.toString().endsWith("error_game_crash.ws")){
                    try{
                        if(botEnvironment.client.getLoadingString().contains("Loaded client variable data")){
                            JOptionPane.showMessageDialog(StaticStorage.mainForm, "KBot can only run RS with graphics settings set to Safe Mode.\n" +
                                    "To do this you have to start the game normally from the official Runescape site and set graphics mode manually to Safe Mode.\n" +
                                    "You can find the setting button for this on the bottom right of the game screen. (Before logging into the game)", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (ALLOW_SHOW_DOCUMENT && Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(url.toURI());
                    } catch (IOException e) {
                        Logger.getRootLogger().error("Exception: ", e);
                    } catch (URISyntaxException e) {
                        Logger.getRootLogger().error("Exception: ", e);
                    }
                }
            }

            public void showDocument(URL url, String target) {
                showDocument(url);
            }

            public void showStatus(String status) {
            }

            public void setStream(String key, InputStream stream) throws IOException {
                appletStreams.put(key, stream);
            }

            public InputStream getStream(String key) {
                return appletStreams.get(key);
            }

            public Iterator<String> getStreamKeys() {
                return appletStreams.keySet().iterator();
            }

            public void close() {
                theApplet = null;
            }

            public boolean hasMoreElements() {
                return (nextElementCalled == 0);
            }

            public Applet nextElement() throws NoSuchElementException {
                nextElementCalled++;
                if (nextElementCalled != 1)
                    throw new NoSuchElementException();
                return null;
            }
        }

        class AudioClipSub implements java.applet.AudioClip {

            public static final short STATE_STOPPED = 0;

            public static final short STATE_PLAYING = 1;

            public static final short STATE_LOOPING = 2;

            private final URL sourceURL;

            private short audioClipState;

            public AudioClipSub(URL sourceURL) {
                this.sourceURL = sourceURL;
                audioClipState = STATE_STOPPED;
            }

            public short getAudioClipState() {
                return audioClipState;
            }

            public URL getURL() {
                return sourceURL;
            }

            public boolean equals(Object obj) {
                if (obj == null)
                    return false;
                if (obj == this)
                    return true;
                if (!(obj instanceof AudioClip))
                    return false;
                AudioClipSub ac = (AudioClipSub) obj;
                return ac.getAudioClipState() == audioClipState
                        && ac.getURL().equals(sourceURL);
            }

            public void play() {
                audioClipState = STATE_PLAYING;
            }

            public void loop() {
                audioClipState = STATE_LOOPING;
            }

            public void stop() {
                audioClipState = STATE_STOPPED;
            }

        }

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTabbedPane tabbedPane;
    public JPanel botAppletPanel;
    private JPanel panel1;
    private JPanel panel3;
    public JCheckBox settingMouseIndicator;
    public JCheckBox settingDisplayWireframes;
    public JCheckBox displayDecoratives;
    private JPanel panel4;
    private JCheckBox disableGroundCheckbox;
    private JCheckBox disableObjectRendering;
    private JLabel label4;
    private JSlider randomSlider;
    private JPanel panel5;
    private JSlider slider1;
    private JLabel label2;
    private JPanel panel2;
    private JLabel label1;
    private JCheckBox irrelevantLogShowCheckBox;
    private JCheckBox normalLogShowCheckBox;
    private JCheckBox importantLogShowCheckBox;
    private JCheckBox errorLogShowCheckBox;
    private JScrollPane scrollPane1;
    private JEditorPane logTextPane;
    private JCheckBox includeTimeStampLogCheckBox;
    private JSeparator separator1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

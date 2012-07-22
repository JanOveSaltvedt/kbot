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



package com.kbotpro;

import com.kbotpro.ui.AuthUI;
import com.kbotpro.utils.LoggingOutputStream;
import com.kbotpro.various.CustomSecurityManager;
import org.apache.log4j.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 10.aug.2009
 * Time: 12:57:46
 */
public class Main {
    public static final boolean devMode = false;  // TODO REMEMBER TO SET BEFORE RELEASE

    public static void main(String[] args){
        setLogger();
        if(args.length == 1 && args[0].equals("debug")){
            addDebugAppender();
        }    
        Logger.getRootLogger().warn("KBot started.");  // Not really error, but want this to show in error log.
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground", "true");        
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        System.setSecurityManager(new CustomSecurityManager());
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                try {
                   // UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel"); 
                    UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

                    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                } catch (InstantiationException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                } catch (UnsupportedLookAndFeelException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                }

                new AuthUI().setVisible(true);
            }
        });

    }

    private static void addDebugAppender() {
        final Logger logger = Logger.getRootLogger();
        logger.setLevel(Level.DEBUG);
        final RollingFileAppender appender;
        try {
            appender = new RollingFileAppender(new TTCCLayout(), "kbot.debug.log.txt");
            appender.setThreshold(Level.DEBUG);
            logger.addAppender(appender);
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static PrintStream oldErr;

    private static void setLogger() {
        BasicConfigurator.configure();
        final Logger logger = Logger.getRootLogger();
        logger.setLevel(Level.INFO);
        final RollingFileAppender appender;
        try {
            appender = new RollingFileAppender(new TTCCLayout(), "kbot.error.log.txt");
            appender.setThreshold(Level.WARN);
            logger.addAppender(appender);
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }

        oldErr = System.err;
        System.setErr(new PrintStream(new LoggingOutputStream(logger,Level.ERROR),true));

    }
}

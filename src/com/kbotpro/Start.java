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


import org.apache.log4j.*;

import javax.swing.*;
import java.io.*;
import java.net.URLDecoder;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 09.aug.2009
 * Time: 15:45:01
 */
public class Start {

    public static void main(String[] args) throws IOException {
        setLogger();
        String path = Start.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, "UTF-8");
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("windows")) {
            if (path.toCharArray()[0] == '/') {
                path = path.substring(1);
            }
        }

        String memory = "-Xmx512M";
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        try {
            Method method = operatingSystemMXBean.getClass().getDeclaredMethod("getFreePhysicalMemorySize");
            if (Modifier.isPublic(method.getModifiers())) {
                long freeMem = (Long) method.invoke(operatingSystemMXBean);
                if (freeMem < 96 * 1024 * 1024) {
                    JOptionPane.showMessageDialog(null, "There is not enough free memory for runescape to start!", "Notice", JOptionPane.ERROR_MESSAGE);
                    System.exit(-15);
                } else if (freeMem < 128 * 1024 * 1024) {
                    JOptionPane.showMessageDialog(null, "This computer has very little free memory and KBot might lag a bit.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    memory = "-Xmx96M";
                } else if (freeMem < 256 * 1024 * 1024) {
                    memory = "-Xmx128M";
                } else if (freeMem < 512 * 1024 * 1024) {
                    memory = "-Xmx256M";
                } else if (freeMem < 768 * 1024 * 1024) {
                    memory = "-Xmx512M";
                } else if (freeMem < 1024 * 1024 * 1024) {
                    memory = "-Xmx768M";
                }
            }
        } catch (NoSuchMethodException e) {
            Logger.getRootLogger().warn("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            Logger.getRootLogger().warn("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().warn("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }

        String cmdargs = " ";
        if(args.length == 1 && args[0].equals("debug")){
            cmdargs += "debug";
        }

        path = path.replaceAll("\\\\", "/");
        System.out.println("Path: " + path);

        System.out.println("Starting with: " + memory);
        if (!OS.contains("windows")) {
            Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "java " + memory + " -Dsun.java2d.noddraw=true -Xbootclasspath/p:\"" + path + "\" com.kbotpro.Main"+cmdargs});
        } else {
            Runtime.getRuntime().exec("java " + memory + " -Dsun.java2d.noddraw=true -Xbootclasspath/p:\"" + path + "\" com.kbotpro.Main"+cmdargs);
        }
        //JOptionPane.showMessageDialog(null, path);
    }

    private static void setLogger() {
        BasicConfigurator.configure();
        final Logger logger = Logger.getRootLogger();
        logger.setLevel(Level.INFO);
        final RollingFileAppender appender;
        try {
            appender = new RollingFileAppender(new TTCCLayout(), "kbot.error.log.txt");
            appender.setThreshold(Level.ERROR);
            logger.addAppender(appender);
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}

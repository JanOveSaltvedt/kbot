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

package com.kbotpro.servercom;

import com.kbotpro.Version;
import com.kbotpro.handlers.AccountsManager;
import com.kbotpro.servercom.jsp.KBrowser;
import com.kbotpro.utils.ProgressCallback;
import com.kbotpro.utils.VirtualBrowser;
import com.kbotpro.various.StaticStorage;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Feb 15, 2010
 * Time: 6:31:49 PM
 * To change this template use File | Settings | File Templates.
 */
public final class JSPServerCom extends ServerCom implements ActionListener {
    public static final String mainURI = "http://tom.kbot.info/kserver/";
    public static KBrowser kbrowser = new KBrowser();

    public JSPServerCom() {
    }


    /**
     * Sends login data to server for verification.
     *
     * @return returns a xml string containing response.
     */
    public Object[] login() {
        final UserStorage userStorage = StaticStorage.userStorage;
        String xml = null;
        try {
            String postParams = "username=" + URLEncoder.encode(userStorage.getUsername(), "UTF-8");
            postParams += "&passwd=" + URLEncoder.encode(MD5(userStorage.getPassword()), "UTF-8");
            postParams += "&build=" + Version.build;

            xml = kbrowser.post(new URL(mainURI + "botcom/auth.jsp"), postParams, null);
        } catch (UnsupportedEncodingException e) {
            xml = "<login><succeeded>false</succeeded><message>Could not URL encode data.</message></login>";
        } catch (NoSuchAlgorithmException e) {
            xml = "<login><succeeded>false</succeeded><message>This computer does not support MD5 hashing.</message></login>";
        } catch (MalformedURLException e) {
            xml = "<login><succeeded>false</succeeded><message>An error occured! </message></login>";
        }
        try {
            Document document = new SAXBuilder().build(new StringReader(xml));
            Element root = document.getRootElement();
            String succeded = root.getChildText("succeeded");
            if (!succeded.equals("true")) {
                StaticStorage.userStorage = null;
                return new Object[]{Boolean.FALSE, root.getChildText("message")};
            } else {
                userStorage.setUserID(Integer.parseInt(root.getChildText("userID")));
                userStorage.setAccessLevel(Integer.parseInt(root.getChildText("access")));
                startServerPinger();
                return new Object[]{Boolean.TRUE, root.getChildText("message")};
            }
        } catch (JDOMException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            return new Object[]{Boolean.FALSE, "Error in server response."};
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            return new Object[]{Boolean.FALSE, "Error in server response."};
        }

    }

    private String getSettingsInternal() {
        try {
            return kbrowser.get(new URL(mainURI + "botcom/getsettings.jsp"), null, null);
        } catch (MalformedURLException e) {
            return "<login><succeeded>false</succeeded><message>An error occured! </message></login>";
        }
    }

    public String getAccountsInternal() {
        try {
            return kbrowser.get(new URL(mainURI + "botcom/getaccounts.jsp"), null, null);
        } catch (MalformedURLException e) {
            return "<login><succeeded>false</succeeded><message>An error occured! </message></login>";
        }
    }

    public boolean setSettingServerSide(String name, String value) {
        try {
            String postParams = "settingName=" + URLEncoder.encode(name, "UTF-8");
            postParams += "&settingValue=" + URLEncoder.encode(value, "UTF-8");
            String xml = kbrowser.post(new URL(mainURI + "botcom/setSetting.jsp"), postParams, null);
            Document document = new SAXBuilder().build(new StringReader(xml));
            Element root = document.getRootElement();

            return root.getChildText("succeeded").equals("true");
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (MalformedURLException e) {
            return false;
        } catch (JDOMException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public void getSettings() {
        String xml = getSettingsInternal();
        HashMap<String, String> settings = new HashMap<String, String>();
        try {
            Document document = new SAXBuilder().build(new StringReader(xml));
            Element root = document.getRootElement();
            if (root.getChildText("succeeded").equals("true")) {
                for (Element setting : (List<Element>) root.getChildren("setting")) {
                    settings.put(setting.getChildText("name"), setting.getChildText("value"));
                }
            }
        } catch (JDOMException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
        StaticStorage.userStorage.settings = settings;
    }


    private boolean outdatedOpen = false;


    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    @Override
    public void startServerPinger() {
        new Thread(new Runnable() {
            public void run() {
                Timer timer = new Timer(300000, JSPServerCom.this);
                timer.setInitialDelay(0);
                timer.start();
            }
        }).start();
    }

    public static String SHA1(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static String MD5(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] sha1hash;
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public String getScriptList() {
        VirtualBrowser virtualBrowser = new VirtualBrowser();
        try {
            return virtualBrowser.get(new URL(mainURI + "botcom/getscriptlist.jsp"), null, null);
        } catch (MalformedURLException e) {
            return "<scripts></scripts>";
        }
    }

    public byte[] downloadScript(int id, ProgressCallback progressCallback) {
        final UserStorage userStorage = StaticStorage.userStorage;
        try {
            String postParams = "scriptID=" + id;
            return kbrowser.postRaw(new URL(mainURI + "botcom/getscript.jsp"), postParams, progressCallback);
        } catch (MalformedURLException e) {
            return new byte[0];
        }
    }

    public boolean addAccount(AccountsManager.Account account) {
        try {
            String postParams = "enusername=" + URLEncoder.encode(account.encryptedUsername, "UTF-8");
            postParams += "&usernamehash=" + URLEncoder.encode(account.usernameHash, "UTF-8");
            postParams += "&enpasswd=" + URLEncoder.encode(account.encryptedPassword, "UTF-8");
            postParams += "&passwdhash=" + URLEncoder.encode(account.passwordHash, "UTF-8");
            String xml = kbrowser.post(new URL(mainURI + "botcom/addaccount.jsp"), postParams, null);
            Document document = new SAXBuilder().build(new StringReader(xml));
            Element root = document.getRootElement();

            return root.getChildText("succeeded").equals("true");
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (MalformedURLException e) {
            return false;
        } catch (JDOMException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean deleteAccount(AccountsManager.Account account) {
        final UserStorage userStorage = StaticStorage.userStorage;
        try {
            String postParams = "accountID=" + account.ID;
            String xml = kbrowser.post(new URL(mainURI + "botcom/deleteaccount.jsp"), postParams, null);
            Document document = new SAXBuilder().build(new StringReader(xml));
            Element root = document.getRootElement();

            return root.getChildText("succeeded").equals("true");
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (MalformedURLException e) {
            return false;
        } catch (JDOMException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        try{
            String xml;
            String postParams = "build=" + Version.build;

            try {
                xml = kbrowser.post(new URL(mainURI + "botcom/ping.jsp"), postParams, null);
            } catch (MalformedURLException e1) {
                xml = "<pong>\n" +
                        "    <succeeded>false</succeeded><message>error</message>\n" +
                        "    <tasks></tasks>\n" +
                        "</pong>";
            }
            Document document = null;
            try {
                document = new SAXBuilder().build(new StringReader(xml));
            } catch (JDOMException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            Element root = document.getRootElement();
            Element tasks = root.getChild("tasks");
            final List<Element> children = tasks.getChildren("task");
            for(Element task: children){
                if(task == null){
                    continue;
                }
                String type = task.getAttributeValue("type");
                if(type == null){
                    continue;
                }
                if(type.equals("displaymsg")){
                    final String message = task.getChildText("msg");
                    final int msgtype = Integer.parseInt(task.getChildText("msgtype"));
                    new Thread(new Runnable() {
                        public void run() {
                            JOptionPane.showMessageDialog(StaticStorage.mainForm, message, "Server message", msgtype);
                        }
                    }).start();
                }
                if(type.equals("kill")){
                    System.exit(0);
                }
            }
        }catch (Throwable throwable){
            Logger.getLogger(JSPServerCom.class).error("Exception in server pinger!: ", throwable);
        }
    }
}
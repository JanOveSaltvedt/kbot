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
import com.kbotpro.utils.Constant;
import com.kbotpro.utils.ProgressCallback;
import com.kbotpro.utils.VirtualBrowser;
import com.kbotpro.various.StaticStorage;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.swing.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Feb 15, 2010
 * Time: 6:31:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpServerCom extends ServerCom implements Runnable{
    public boolean runServerPinger = true;
    private Thread serverPinger;

    public HttpServerCom(){
        serverPinger = new Thread(this, "ServerCom");
    }

    public void startServerPinger(){
        runServerPinger = true;
        serverPinger.start();
    }

    /**
     * Sends login data to server for verification.
     * @return returns a xml string containing response.
     */
    public Object[] login(){
        final UserStorage userStorage = StaticStorage.userStorage;
        String xml = null;
        try {
            String postParams = "username="+ URLEncoder.encode(userStorage.getUsername(), "UTF-8");
            postParams += "&md5="+URLEncoder.encode(MD5(userStorage.getPassword()), "UTF-8");
            VirtualBrowser virtualBrowser = new VirtualBrowser();
            xml =  virtualBrowser.post(new URL("http://"+ Constant.SERVER_HOST+"/kbotpro/botcom/auth.php"), postParams, null);
        } catch (UnsupportedEncodingException e) {
            xml = "<login><succeeded>false</succeeded><message>Could not URL encode data.</message></login>";
        } catch (NoSuchAlgorithmException e) {
            xml = "<login><succeeded>false</succeeded><message>This computer does not support MD5 hashing.</message></login>";
        } catch (MalformedURLException e) {
            xml = "<login><succeeded>false</succeeded><message>An error occured! </message></login>";
        }
        try {
            Document document = new SAXBuilder().build(new StringReader(xml));
            org.jdom.Element root = document.getRootElement();
            String succeded = root.getChildText("succeeded");
            if(!succeded.equals("true")){
                StaticStorage.userStorage = null;
                return new Object[]{Boolean.FALSE, root.getChildText("message")};
            }
            else{
                userStorage.setUserID(Integer.parseInt(root.getChildText("userID")));
                userStorage.setSessionID(Integer.parseInt(root.getChildText("sessionID")));
                userStorage.setSessionKey(root.getChildText("sessionKey"));
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

    private String getSettingsInternal(){
        final UserStorage userStorage = StaticStorage.userStorage;
        try {
            String postParams = "userID="+userStorage.getUserID();
            postParams += "&sessionID="+userStorage.getSessionID();
            postParams += "&sessionKey="+URLEncoder.encode(userStorage.getSessionKey(), "UTF-8");
            VirtualBrowser virtualBrowser = new VirtualBrowser();
            return virtualBrowser.post(new URL("http://"+ Constant.SERVER_HOST+"/kbotpro/botcom/getSettings.php"), postParams, null);
        } catch (UnsupportedEncodingException e) {
            return "<login><succeeded>false</succeeded><message>Could not URL encode data.</message></login>";
        } catch (MalformedURLException e) {
            return "<login><succeeded>false</succeeded><message>An error occured! </message></login>";
        }
    }

    public String getAccountsInternal(){
        final UserStorage userStorage = StaticStorage.userStorage;
        try {
            String postParams = "userID="+userStorage.getUserID();
            postParams += "&sessionID="+userStorage.getSessionID();
            postParams += "&sessionKey="+URLEncoder.encode(userStorage.getSessionKey(), "UTF-8");
            VirtualBrowser virtualBrowser = new VirtualBrowser();
            return virtualBrowser.post(new URL("http://"+ Constant.SERVER_HOST+"/kbotpro/botcom/getAccounts.php"), postParams, null);
        } catch (UnsupportedEncodingException e) {
            return "<login><succeeded>false</succeeded><message>Could not URL encode data.</message></login>";
        } catch (MalformedURLException e) {
            return "<login><succeeded>false</succeeded><message>An error occured! </message></login>";
        }
    }

    public boolean setSettingServerSide(String name, String value){
        final UserStorage userStorage = StaticStorage.userStorage;
        try {
            String postParams = "userID="+userStorage.getUserID();
            postParams += "&sessionID="+userStorage.getSessionID();
            postParams += "&sessionKey="+URLEncoder.encode(userStorage.getSessionKey(), "UTF-8");
            postParams += "&settingName="+URLEncoder.encode(name, "UTF-8");
            postParams += "&settingValue="+URLEncoder.encode(value, "UTF-8");
            VirtualBrowser virtualBrowser = new VirtualBrowser();
            String xml = virtualBrowser.post(new URL("http://" + Constant.SERVER_HOST + "/kbotpro/botcom/setSetting.php"), postParams, null);
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

    public void getSettings(){
        String xml = getSettingsInternal();
        HashMap<String,  String> settings = new HashMap<String, String>();
        try {
            Document document = new SAXBuilder().build(new StringReader(xml));
            Element root = document.getRootElement();
            if(root.getChildText("succeeded").equals("true")){
                for(Element setting: (List<Element>)root.getChildren("setting")){
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
        while(StaticStorage.userStorage != null && runServerPinger){
            final UserStorage userStorage = StaticStorage.userStorage;
            VirtualBrowser virtualBrowser = new VirtualBrowser();
            try {
                String postParams = "userID="+ userStorage.getUserID();
                postParams += "&sessionID="+ userStorage.getSessionID();
                postParams += "&sessionKey="+URLEncoder.encode(userStorage.getSessionKey(), "UTF-8");
                String response = virtualBrowser.post(new URL("http://"+ Constant.SERVER_HOST+"/kbotpro/botcom/pinger.php"), postParams, null);
                try {
                    Document document = new SAXBuilder().build(new StringReader(response));
                    Element root = document.getRootElement();
                    if(!root.getChildText("succeeded").equals("true")){
                        JOptionPane.showMessageDialog(null, root.getChildText("message"), "Error in server comunication.", JOptionPane.ERROR_MESSAGE);
                    }
                    Element tasks = root.getChild("tasks");
                    if(tasks != null){
                        for(Element task: (List<Element>)tasks.getChildren("task")){
                            String action = task.getChildText("action");
                            if(action.equalsIgnoreCase("killbot")){
                                System.exit(-10);
                            } else if(action.equalsIgnoreCase("checkBuild")){
                                int build = Integer.valueOf(task.getChildText("build"));
                                if(Integer.valueOf(Version.build) < build){
                                    if(!outdatedOpen){
                                        new Thread(new Runnable() {
                                            public void run() {
                                                JOptionPane.showMessageDialog(null, "This version of KBot is considered outdated. \nPlease update your bot.", "Bot Outdated", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }).start();
                                    }

                                }
                            }
                        }
                    }

                } catch (JDOMException e) {
                    if(!response.contains("<succeeded>true</succeeded>")){
                        Pattern pattern = Pattern.compile("<message>([\\w\\s]+)</message>");
                        Matcher matcher = pattern.matcher(response);
                        if(matcher.find()){
                            String message = matcher.group(1);
                            JOptionPane.showMessageDialog(null, message, "Error in server comunication.", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Could not connect with master server.", "Error in server comunication.", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                } catch (IOException e) {
                    if(!response.contains("<succeeded>true</succeeded>")){
                        Pattern pattern = Pattern.compile("<message>([\\w\\s]+)</message>");
                        Matcher matcher = pattern.matcher(response);
                        if(matcher.find()){
                            String message = matcher.group(1);
                            JOptionPane.showMessageDialog(null, message, "Error in server comunication.", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Could not connect with master server.", "Error in server comunication.", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

            } catch (UnsupportedEncodingException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (MalformedURLException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }

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
        	} while(two_halfs++ < 1);
        }
        return buf.toString();
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
            return virtualBrowser.get(new URL("http://"+ Constant.SERVER_HOST+"/kbotpro/botcom/scriptList.php"), null, null);
        } catch (MalformedURLException e) {
            return "<scripts></scripts>";
        }
    }

    public byte[] downloadScript(int id, ProgressCallback progressCallback) {
        final UserStorage userStorage = StaticStorage.userStorage;
        try {
            String postParams = "userID="+userStorage.getUserID();
            postParams += "&sessionID="+userStorage.getSessionID();
            postParams += "&sessionKey="+URLEncoder.encode(userStorage.getSessionKey(), "UTF-8");
            postParams += "&scriptID="+id;
            VirtualBrowser virtualBrowser = new VirtualBrowser();
            return virtualBrowser.postRaw(new URL("http://"+ Constant.SERVER_HOST+"/kbotpro/botcom/downloadscript.php"), postParams, progressCallback);
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        } catch (MalformedURLException e) {
            return new byte[0];
        }
    }

    public boolean addAccount(AccountsManager.Account account) {
        final UserStorage userStorage = StaticStorage.userStorage;
        try {
            String postParams = "userID="+userStorage.getUserID();
            postParams += "&sessionID="+userStorage.getSessionID();
            postParams += "&sessionKey="+URLEncoder.encode(userStorage.getSessionKey(), "UTF-8");
            postParams += "&enusername="+URLEncoder.encode(account.encryptedUsername, "UTF-8");
            postParams += "&usernamehash="+URLEncoder.encode(account.usernameHash, "UTF-8");
            postParams += "&enpasswd="+URLEncoder.encode(account.encryptedPassword, "UTF-8");
            postParams += "&passwdhash="+URLEncoder.encode(account.passwordHash, "UTF-8");
            VirtualBrowser virtualBrowser = new VirtualBrowser();
            String xml = virtualBrowser.post(new URL("http://" + Constant.SERVER_HOST + "/kbotpro/botcom/addAccount.php"), postParams, null);
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
            String postParams = "userID="+userStorage.getUserID();
            postParams += "&sessionID="+userStorage.getSessionID();
            postParams += "&sessionKey="+URLEncoder.encode(userStorage.getSessionKey(), "UTF-8");
            postParams += "&accountID="+account.ID;
            VirtualBrowser virtualBrowser = new VirtualBrowser();
            String xml = virtualBrowser.post(new URL("http://" + Constant.SERVER_HOST + "/kbotpro/botcom/deleteAccount.php"), postParams, null);
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
}

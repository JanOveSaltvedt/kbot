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

package com.kbotpro.handlers;

import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

import javax.crypto.spec.DESKeySpec;
import javax.crypto.*;
import java.util.List;
import java.util.ArrayList;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.StringReader;

import com.kbotpro.servercom.ServerCom;
import com.kbotpro.various.StaticStorage;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Nov 29, 2009
 * Time: 11:22:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class AccountsManager {
    private List<Account> accounts;

    public List<Account> getAccounts(){
        if(accounts != null){
            return accounts;
        }
        updateAccounts();
        return accounts;

    }

    public void createAccount(Account account){
        StaticStorage.serverCom.addAccount(account);
    }

    public void deleteAccount(Account account){
        if(account.ID < 0){
            return;
        }
        StaticStorage.serverCom.deleteAccount(account);
    }

    public void updateAccounts() {
        List<Account> accounts = new ArrayList<Account>();
        String xml = StaticStorage.serverCom.getAccountsInternal();
        try {
            Document document = new SAXBuilder().build(new StringReader(xml));
            Element root = document.getRootElement();
            
            if(root.getChildText("succeeded").equals("true")){
                for(Element accountNode: (List<Element>)root.getChildren("account")){
                    int ID = Integer.parseInt(accountNode.getAttribute("id").getValue());
                    String encryptedUsername = accountNode.getChildText("enusername");
                    String encryptedPassword = accountNode.getChildText("enpasswd");
                    String usernameHash = accountNode.getChildText("usernamehash");
                    String passwordHash = accountNode.getChildText("passwdhash");
                    final Account account = new Account(ID, encryptedUsername, encryptedPassword, usernameHash, passwordHash);
                    if(account.toString() != null){
                        accounts.add(account);
                    }
                    else{
                        StaticStorage.serverCom.deleteAccount(account);
                        Logger.getRootLogger().warn("Deleting account because we can no longer decrypt it. Probably because user changed password on forums.");
                    }
                }
            }
        } catch (JDOMException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
        this.accounts = accounts;
    }

    public Account constructAccount(String username, String password) {
        return new Account(username, password, StaticStorage.userStorage.getPassword());
    }

    public class Account {
        public int ID = -1;
        public String encryptedUsername;
        public String encryptedPassword;
        public String usernameHash;
        public String passwordHash;
        public boolean membersAccount = false;
        public String pin = "";
        public boolean useLamp = false;
        public String lampIndex = "Runecraft";
        
        public Account(int ID, String encryptedUsername, String encryptedPassword, String usernameHash, String passwordHash) {
            this.ID = ID;
            this.encryptedUsername = encryptedUsername;
            this.encryptedPassword = encryptedPassword;
            this.usernameHash = usernameHash;
            this.passwordHash = passwordHash;
        }

        public Account(String username, String password, String passPhrase) {
            try {
                
                DESKeySpec keySpec = new DESKeySpec(generateKey(passPhrase));
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey key = keyFactory.generateSecret(keySpec);
                sun.misc.BASE64Encoder base64encoder = new BASE64Encoder();

                byte[] cleartext = username.getBytes("UTF8");
                Cipher cipher = Cipher.getInstance("DES");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                encryptedUsername = base64encoder.encode(cipher.doFinal(cleartext));

                cleartext = password.getBytes("UTF8");
                encryptedPassword = base64encoder.encode(cipher.doFinal(cleartext));

                usernameHash = ServerCom.MD5(username);
                passwordHash = ServerCom.MD5(password);
            } catch (InvalidKeyException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnsupportedEncodingException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvalidKeySpecException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (NoSuchAlgorithmException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalBlockSizeException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (NoSuchPaddingException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (BadPaddingException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        private String username;
        public String getUsername(){
            if(username != null){
                return username;
            }
            String decrypted = decode(StaticStorage.userStorage.getPassword(), encryptedUsername);
            if(verify(decrypted, usernameHash)){
                username = decrypted;
                return decrypted;
            }
            else{
                return null;
            }
        }

        private String password;
        public String getPassword(){
            if(password != null){
                return password;
            }
            String decrypted = decode(StaticStorage.userStorage.getPassword(), encryptedPassword);
            if(verify(decrypted, passwordHash)){
                password = decrypted;
                return decrypted;
            }
            else{
                return null;
            }
        }

        private byte[] generateKey(String passPhrase){
            if(passPhrase.length() < 8){
                int add = 8-passPhrase.length();
                while(add != 0){
                    passPhrase += 'g';
                    add--;
                }
            }

            try {
                return passPhrase.getBytes("UTF8");
            } catch (UnsupportedEncodingException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                return null;
            }

        }

        private String decode(String passPhrase, String encryptedString){
            try {
                DESKeySpec keySpec = new DESKeySpec(generateKey(passPhrase));
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey key = keyFactory.generateSecret(keySpec);
                sun.misc.BASE64Decoder base64decoder = new BASE64Decoder();

                byte[] encrypedPwdBytes = base64decoder.decodeBuffer(encryptedString);

                Cipher cipher = Cipher.getInstance("DES");// cipher is not thread safe
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));
                return new String(plainTextPwdBytes, "UTF8");
            } catch (InvalidKeyException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnsupportedEncodingException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (NoSuchAlgorithmException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvalidKeySpecException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalBlockSizeException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (NoSuchPaddingException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (BadPaddingException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }
            return "<invalid>";
        }

        private boolean verify(String decodedString, String verifyHash){
            try {
                String hash = ServerCom.MD5(decodedString);
                return hash.equalsIgnoreCase(verifyHash);
            } catch (NoSuchAlgorithmException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnsupportedEncodingException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }
            return false;
        }

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
            return getUsername();
        }


    }
}

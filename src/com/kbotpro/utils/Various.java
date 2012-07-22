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

package com.kbotpro.utils;

import com.kbotpro.Main;
import com.kbotpro.Start;

import java.io.*;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Feb 16, 2010
 * Time: 8:03:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Various {
    public static byte[] getBotMD5() {
        try {
            final String rawPath = Various.class.getResource("").getPath();
            String path = rawPath.substring(6, rawPath.lastIndexOf(".jar")+4);
            path = URLDecoder.decode(path, "UTF-8");
            String OS = System.getProperty("os.name").toLowerCase();
            if (OS.contains("windows")) {
                if (path.toCharArray()[0] == '/') {
                    path = path.substring(1);
                }
            }
            path = path.replaceAll("\\\\", "/");

            MessageDigest digest = MessageDigest.getInstance("MD5");
            File f = new File(path);
            InputStream is = new FileInputStream(f);
            byte[] buffer = new byte[8192];
            int read = 0;
            try {
                while ((read = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, read);
                }
                return digest.digest();
            }
            catch (IOException e) {
                throw new RuntimeException("Unable to process file for MD5", e);
            }
            finally {
                try {
                    is.close();
                }
                catch (IOException e) {
                    throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
                }
            }
        } catch (FileNotFoundException e) {
            final byte[] bytes = new byte[16];
            Arrays.fill(bytes, (byte) 0);
            return bytes;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not suppored.", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Cannot decode URL", e);
        }

    }
}

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

package com.kbotpro.servercom.jsp;

import com.kbotpro.utils.ProgressCallback;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 31.jul.2009
 * Time: 16:08:05
 */
public class KBrowser {
    public String session = null;

    public byte[] getRaw(URL url, URL previousPage, ProgressCallback callback) {
        HttpURLConnection connection = createConnection(url, "GET");
        if (previousPage != null) {
            connection.addRequestProperty("Referer", previousPage.toString());
        }
        try {
            return download(connection, callback);
        } catch (IOException e) {
            return null;
        }
    }

    public String get(URL url, URL previousPage, ProgressCallback callback) {
        return new String(getRaw(url, previousPage, callback));
    }

    private byte[] download(HttpURLConnection connection, ProgressCallback callback) throws IOException {
        if(connection.getHeaderField("Set-Cookie") != null){
            Pattern pattern = Pattern.compile("JSESSIONID=([0-9A-Za-z]+);");
            Matcher matcher = pattern.matcher(connection.getHeaderField("Set-Cookie"));
            if(matcher.find()){
                session = matcher.group(1);  
            }
        }
        int len = connection.getContentLength();
        InputStream is = new BufferedInputStream(connection.getInputStream());

        String compression = connection.getHeaderField("Content-Encoding");
        if (compression != null && compression.toLowerCase().contains("gzip")) {
            is = new GZIPInputStream(is);
        }

        ByteArrayOutputStream bAOut = new ByteArrayOutputStream();
        int c = 0;
        int off = 0;
        try{
            while ((c = is.read()) != -1) {
                bAOut.write(c);
                off++;
                if(callback != null){
                    callback.update(off, len);
                }
            }
        }catch (IOException e){
            Logger.getRootLogger().error("Exception: ", e);
        }
        if(callback != null){
            callback.onComplete();
        }
        return bAOut.toByteArray();
    }

    private HttpURLConnection createConnection(URL url, String requestMethod) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("User-Agent", "KBrowser 1.0");
            if(session != null){
                connection.setRequestProperty("Cookie", "JSESSIONID="+session);
            }
            return connection;
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public byte[] postRaw(URL url, String urlParameters, ProgressCallback callback) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            connection = createConnection(url, "POST");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            return download(connection, callback);

        } catch (Exception e) {

            Logger.getRootLogger().error("Exception: ", e);
            return null;

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public String post(URL url, String urlParameters, ProgressCallback callback) {
        return new String(postRaw(url, urlParameters, callback));
    }
}
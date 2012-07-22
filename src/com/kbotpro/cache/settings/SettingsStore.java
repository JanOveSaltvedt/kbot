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

package com.kbotpro.cache.settings;

import com.kbotpro.cache.CacheManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Mar 6, 2010
 * Time: 5:30:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsStore {
    public static final String MAIN_SETTINGS = "bot.settings";
    private String filename;

    public SettingsStore(String filename) {
        this.filename = filename;
    }

    /**
     * Gets the document or creates an empty document with the content <settings></settings>
     * @return
     * @throws IOException
     */
    public Document getDocument() throws IOException{
        final File directory = new File(CacheManager.getSettingsDir());
        if(!directory.exists()){
            if(!directory.mkdirs()){
                throw new IOException("Could not create directories to settings");
            }
        }
        File file = new File(CacheManager.getSettingsDir()+filename);
        if(!file.exists()){
            return new Document(new Element("settings"));
        }
        try {
            return new SAXBuilder().build(file);
        } catch (JDOMException e) {
            throw new IOException("Invalid format on settings file");
        }
    }

    public void save(Document document) throws IOException {
        final File directory = new File(CacheManager.getSettingsDir());
        if(!directory.exists()){
            if(!directory.mkdirs()){
                throw new IOException("Could not create directories to settings");
            }
        }
        File file = new File(CacheManager.getSettingsDir()+filename);
        if(!file.exists()){
            if(!file.createNewFile()){
                throw new IOException("Could not create settings file");
            }
        }
        new XMLOutputter().output(document, new FileOutputStream(file));
    }
}

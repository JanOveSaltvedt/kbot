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

import com.kbotpro.cache.xml.XMLParser;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Mar 6, 2010
 * Time: 7:00:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainSettings {
    public static SettingsStore settingsStore = new SettingsStore(SettingsStore.MAIN_SETTINGS);
    private static XMLParser xmlParser = new XMLParser();


    public static Object getSetting(String name){
        try {
            final Document document = settingsStore.getDocument();
            Element element = getSettingElement(document, name);
            if(element == null){
                return null;
            }

            return xmlParser.getValue(element);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static void setSetting(String name, Object value){
        try {
            final Document document = settingsStore.getDocument();
            Element element = getSettingElement(document, name);
            final Element ourElement = xmlParser.createElement(name, value);
            if(element == null){
                document.getRootElement().addContent(ourElement);
            }
            else{
                document.getRootElement().removeContent(element);
                document.getRootElement().addContent(ourElement);
            }
            settingsStore.save(document);
            return;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return;
    }

    private static Element getSettingElement(Document document, String name) {
        Element root = document.getRootElement();
        for(Content node: (List<Content>)root.getChildren("setting")){
            if(node instanceof Element){
                Element element = (Element) node;
                if(name.equals(element.getAttributeValue("name"))){
                    return element;
                }

            }

        }
        return null;
    }
}

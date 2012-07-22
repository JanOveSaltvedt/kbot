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



package com.kbotpro.handlers;

import com.kbotpro.various.ScriptMetaData;
import com.kbotpro.various.StaticStorage;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Nov 2, 2009
 * Time: 8:27:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptMetaDataManager {
    public static List<ScriptMetaData> loadedScriptMetaData = new ArrayList<ScriptMetaData>();
    public static long lastUpdated;

    public static void loadScriptMetaData(){
        if(System.currentTimeMillis()-lastUpdated > 3600) { // One hour
            String xml = StaticStorage.serverCom.getScriptList();
            try {
                Document doc = new SAXBuilder().build(new StringReader(xml));
                List<ScriptMetaData> loadedScriptMetaData = new ArrayList<ScriptMetaData>();
                Element root = doc.getRootElement();
                for(Element script: (List<Element>)root.getChildren("script")){
                    if(script == null){
                        continue;
                    }
                    List<Permission> permissionExceptions = new ArrayList<Permission>();
                    final Element policyNode = script.getChild("spolicy");
                    if(policyNode != null){
                        for(Element permission: (List<Element>)policyNode.getChildren("permission")){
                            String className = permission.getAttributeValue("classname");
                            try {
                                Class klass = ScriptMetaDataManager.class.forName(className);
                                final Constructor constructor = klass.getConstructor(new Class<?>[]{String.class, String.class});
                                final Permission perm = (Permission) constructor.newInstance(permission.getAttributeValue("name"), permission.getAttributeValue("actions"));
                                permissionExceptions.add(perm);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (InstantiationException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }

                        }
                    }
                    ScriptMetaData scriptMetaData = new ScriptMetaData(Integer.parseInt(script.getAttributeValue("ID")),
                            script.getAttributeValue("name"),
                            script.getAttributeValue("author"), Integer.parseInt(script.getAttributeValue("downloads")),
                            StringEscapeUtils.unescapeXml(script.getChildText("description")),
                            script.getAttributeValue("category"), script.getAttributeValue("type"),
                            script.getAttributeValue("version"),
                            Integer.parseInt(script.getAttributeValue("rev")),
                            Integer.parseInt(script.getAttributeValue("modifier")), permissionExceptions);
                    loadedScriptMetaData.add(scriptMetaData);
                }
                ScriptMetaDataManager.loadedScriptMetaData = loadedScriptMetaData;
            } catch (JDOMException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

}

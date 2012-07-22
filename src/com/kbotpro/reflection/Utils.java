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



package com.kbotpro.reflection;

import java.io.StringReader;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 10.aug.2009
 * Time: 18:01:45
 */
public class Utils {
    /*public static InstanceHook[] embedObjects(Class<? extends InstanceHook> klass, Class<? extends InstanceHook[]> arrayKlass, Object[] objects, ClassLoader classLoader){
        InstanceHook[] out = new InstanceHook[objects.length];
        for(int i = 0; i < objects.length; i++){
            if(objects[i] == null){
                out[i] = null;
            }
            else{
                try {
                    final Constructor<? extends InstanceHook> constructor = klass.getConstructor(ClassLoader.class, Object.class);
                    out[i] = constructor.newInstance(classLoader, objects[i]);
                } catch (NoSuchMethodException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    out[i] = null;
                } catch (InvocationTargetException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    out[i] = null;
                } catch (IllegalAccessException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    out[i] = null;
                } catch (InstantiationException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    out[i] = null;
                }
            }
        }
        return Arrays.copyOf(out, out.length, arrayKlass);
    } */

    public static void compileHooks(String xml) {
        HashMap<String, String> classNames = new HashMap<String, String>();
        HashMap<String, String> fieldNames = new HashMap<String, String>();
        HashMap<String, String> classNicks = new HashMap<String, String>();
        try {
            Document doc = new SAXBuilder().build(new StringReader(xml));
            final Element root = doc.getRootElement();
            Element fields = root.getChild("fields");
            for(Element fieldNode: (List<Element>)fields.getChildren("field")){
                String fieldGroup = fieldNode.getChildText("fieldGroup");
                String fieldNick = fieldNode.getChildText("fieldNick");
                classNames.put(fieldGroup+"::"+fieldNick, fieldNode.getChildText("className"));
                fieldNames.put(fieldGroup+"::"+fieldNick, fieldNode.getChildText("fieldName"));
            }
            Element classes = root.getChild("classNicks");
            if(classes != null){
                for(Element classNode: (List<Element>)classes.getChildren("class")){
                    String classNick = classNode.getChildText("classNick");
                    String className = classNode.getChildText("className");
                    classNicks.put(classNick, className);
                }
            }
            ReflectionEngine.classNames = classNames;
            ReflectionEngine.fieldNames = fieldNames;
            ReflectionEngine.classNicks = classNicks;
        }  catch (JDOMException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

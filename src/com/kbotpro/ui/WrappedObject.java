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

package com.kbotpro.ui;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.utils.EscapeChars;
import org.apache.log4j.Logger;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 15, 2009
 * Time: 7:00:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class WrappedObject{
        Object obj;
        private BotEnvironment bot;
        private Field container;
        public String arrayString = "";

        public WrappedObject(Object obj, BotEnvironment bot, Field container) {
            this.obj = obj;
            this.bot = bot;
            this.container = container;
        }

        public Object getObj() {
            return obj;
        }

        @Override
        public String toString() {
            if(container == null){
                return "<unknown>:("+getObjClass().getSimpleName()+")"+arrayString+" = "+(obj==null?"null":obj.toString());
            }
            return container.getName()+":("+getObjClass().getSimpleName()+")"+arrayString+" = "+(obj==null?"null":obj.toString());
        }

        public String getValueHTML(){
            String ret = "<html>\n" +
                    "<body>\n";
            Class aClass = getObjClass();
            if(aClass != null){
                ret += EscapeChars.forHTML("Class: "+ aClass.getSimpleName()+"\n");
            }
            else{
                ret += EscapeChars.forHTML("Class: unknown");
            }
            ret += EscapeChars.forHTML("Value: ");
            if(obj == null){
                ret += "null\n";
            }
            else if(getObjClass().isArray()){
                if(obj instanceof int[]){
                    ret += Arrays.toString((int[]) obj);
                }
                else if(obj instanceof short[]){
                    ret += Arrays.toString((short[]) obj);
                }
                else if(obj instanceof byte[]){
                    ret += Arrays.toString((byte[]) obj);
                }
                else if(obj instanceof long[]){
                    ret += Arrays.toString((long[]) obj);
                }
                else if(obj instanceof float[]){
                    ret += Arrays.toString((float[]) obj);
                }
                else if(obj instanceof boolean[]){
                    ret += Arrays.toString((boolean []) obj);
                }
            }else{
                ret += EscapeChars.forHTML(obj.toString()+"\n");
            }
            ret +=  "</body>\n" +
                    "</html>";
            return ret;
        }

        public Class getObjClass(){
            if(obj == null){
                if(container == null){
                    return null;
                }
                return container.getType();
            }
            return obj.getClass();
        }

        public Field[] getFields(){
            if(obj == null){
                return new Field[0];
            }
            Class<? extends Object> aClass = obj.getClass();
            return getFields(aClass);
        }

        public Field[] getFields(Class aClass){
            if(obj == null){
                return new Field[0];
            }
            Field[] out = aClass.getDeclaredFields();
            if(aClass.equals(Object.class)){
                return out;
            }
            Vector<Field> fields = new Vector<Field>();
            fields.addAll(Arrays.asList(out));
            fields.addAll(Arrays.asList(getFields(aClass.getSuperclass())));
            return fields.toArray(new Field[fields.size()]);
        }

        public MutableTreeNode[] getAllChildren(){
            Field[] fields = getFields();
            if(fields.length == 0){
                if(obj != null && obj instanceof Object[]){
                    Object[] array = (Object[]) obj;
                    DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[array.length];
                    for(int i = 0; i < array.length; i++){
                        WrappedObject userObject = new WrappedObject(array[i], bot, container);
                        userObject.arrayString += arrayString+"["+i+"]";
                        nodes[i] = new DefaultMutableTreeNode(userObject);
                    }
                    return nodes;
                }
                return new MutableTreeNode[0]; // return an empty array
            }
            List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
            for (Field field : fields) {
                if(field == null){
                    continue;
                }
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                WrappedObject child = getChild(field);
                if(child == null){
                    continue;
                }
                nodes.add(new DefaultMutableTreeNode(child));
            }
            return nodes.toArray(new MutableTreeNode[nodes.size()]);
        }


        public WrappedObject getChild(Field field){
            try {
                if(!field.isAccessible()){
                    field.setAccessible(true);
                }
                return new WrappedObject(field.get(obj),bot, field);
            } catch (IllegalAccessException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                return null;
            }
        };
    }

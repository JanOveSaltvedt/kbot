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

package com.kbotpro.cache.xml;

import org.apache.commons.codec.binary.Base64;
import org.jdom.Element;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Mar 6, 2010
 * Time: 5:13:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class XMLParser {
    public String getName(Element element){
        return element.getAttributeValue("name");
    }

    public Object getValue(Element element){
        final String type = element.getAttributeValue("type");
        if(type == null){
            return null;
        }
        if(element.getText() == null){
            return null;
        }
        if(type.equals("int")){
            return Integer.parseInt(element.getText());
        }
        if(type.equals("long")){
            return Long.parseLong(element.getText());
        }
        if(type.equals("double")){
            return Double.parseDouble(element.getText());
        }
        if(type.equals("float")){
            return Float.parseFloat(element.getText());
        }
        if(type.equals("boolean")){
            return Boolean.parseBoolean(element.getText());
        }
        if(type.equals("base64")){
            return Base64.decodeBase64(element.getText());
        }
        if(type.equals("string")){
            return element.getText();
        }
        if(type.equals("null")){
            return null;
        }

        return null;
    }

    public Element createElement(String name, Object value){
        Element element = new Element("setting");
        element.setAttribute("name", name);
        if(value instanceof Number && !(value instanceof Long || value instanceof Double || value instanceof Float)){
            element.setAttribute("type", "int");
            element.setText(value.toString());
            return element;
        }
        if(value instanceof Long){
            element.setAttribute("type", "long");
            element.setText(value.toString());
            return element;
        }
        if(value instanceof Double){
            element.setAttribute("type", "double");
            element.setText(value.toString());
            return element;
        }
        if(value instanceof Float){
            element.setAttribute("type", "float");
            element.setText(value.toString());
            return element;
        }
        if(value instanceof Boolean){
            element.setAttribute("type", "boolean");
            element.setText(value.toString());
            return element;
        }
        if(value instanceof byte[]){
            element.setAttribute("type", "base64");
            element.setText(Base64.encodeBase64String((byte[]) value));
            return element;
        }
        if(value instanceof String){
            element.setAttribute("type", "string");
            element.setText(value.toString());
            return element;
        }
        if(value == null){
            element.setAttribute("type", "null");
            return element;
        }
        else{
            throw new IllegalArgumentException(""+value.getClass()+" not supported");
        }


    }
}

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

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 10.aug.2009
 * Time: 14:35:05
 */
public class ReflectionEngine {
    public static HashMap<String, String> classNames = new HashMap<String, String>();
    public static HashMap<String, String> fieldNames = new HashMap<String, String>();
    public static HashMap<String, String> classNicks = new HashMap<String, String>();

    public static Field getField(Class klass, String fieldName){
        try {
            Field field = klass.getField(fieldName);
            if(!field.isAccessible()){
                field.setAccessible(true);
            }
            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No such field: "+fieldName+" in class: "+klass.getSimpleName());
        }
    }

    /**
     * Gets a field within a class.
     * @param instance An instance of an object of class klass or null if the field is static.
     * @param klass A Class of the instance aka the parrent class of the field
     * @param fieldName The name of the field.
     * @return
     */
    public static int getIntField(Object instance, Class klass, String fieldName){
        try {
            return getField(klass, fieldName).getInt(instance);
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            return -1;
        }
    }

    /**
     * Gets a field within a class.
     * @param instance An instance of an object of class klass or null if the field is static.
     * @param klass A Class of the instance aka the parrent class of the field
     * @param fieldName The name of the field.
     * @return
     */
    public static byte getByteField(Object instance, Class klass, String fieldName){
        try {
            return getField(klass, fieldName).getByte(instance);
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            return -1;
        }
    }

    /**
     * Gets a field within a class.
     * @param instance An instance of an object of class klass or null if the field is static.
     * @param klass A Class of the instance aka the parrent class of the field
     * @param fieldName The name of the field.
     * @return
     */
    public static long getLongField(Object instance, Class klass, String fieldName){
        try {
            return getField(klass, fieldName).getLong(instance);
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            return -1L;
        }
    }

    /**
     * Gets a field within a class.
     * @param instance An instance of an object of class klass or null if the field is static.
     * @param klass A Class of the instance aka the parrent class of the field
     * @param fieldName The name of the field.
     * @return
     */
    public static double getDoubleField(Object instance, Class klass, String fieldName){
        try {
            return getField(klass, fieldName).getDouble(instance);
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            return -1D;
        }
    }

    /**
     * Gets a field within a class.
     * @param instance An instance of an object of class klass or null if the field is static.
     * @param klass A Class of the instance aka the parrent class of the field
     * @param fieldName The name of the field.
     * @return
     */
    public static float getFloatField(Object instance, Class klass, String fieldName){
        try {
            return getField(klass, fieldName).getFloat(instance);
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            return -1F;
        }
    }

    /**
     * Gets a field within a class.
     * @param instance An instance of an object of class klass or null if the field is static.
     * @param klass A Class of the instance aka the parrent class of the field
     * @param fieldName The name of the field.
     * @return
     */
    public static short getShortField(Object instance, Class klass, String fieldName){
        try {
            return getField(klass, fieldName).getShort(instance);
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            return -1;
        }
    }

    /**
     * Gets a field within a class.
     * @param instance An instance of an object of class klass or null if the field is static.
     * @param klass A Class of the instance aka the parrent class of the field
     * @param fieldName The name of the field.
     * @return
     */
    public static char getCharField(Object instance, Class klass, String fieldName){
        try {
            return getField(klass, fieldName).getChar(instance);
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            return (char) -1;
        }
    }

    /**
     * Gets a field within a class.
     * @param instance An instance of an object of class klass or null if the field is static.
     * @param klass A Class of the instance aka the parrent class of the field
     * @param fieldName The name of the field.
     * @return
     */
    public static Object getObjectField(Object instance, Class klass, String fieldName){
        try {
            return getField(klass, fieldName).get(instance);
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }
}

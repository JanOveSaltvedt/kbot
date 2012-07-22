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

package com.kbotpro.various;

import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 29, 2010
 * Time: 2:29:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptAnalyzer {
    List<ClassGen> classes = new ArrayList<ClassGen>();

    public ScriptAnalyzer(JarInputStream jarFile){
        try{
            JarEntry entry = null;
            while ((entry = jarFile.getNextJarEntry()) != null) {
                if (entry.getName().endsWith(".class")) {

                    byte[] data;
                    final int length = (int) entry.getSize();
                    if(length == -1){
                        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int offset = 0;
                        int ret;
                        do{
                            ret = jarFile.read(buffer, 0, 1024);
                            offset += ret;
                            if(ret > 0){
                                byteArrayOutputStream.write(buffer, 0, ret);
                            }
                        }while(ret > 0);
                        data = byteArrayOutputStream.toByteArray();
                    }
                    else{
                        int offset = 0;
                        data = new byte[length];
                        int ret;
                        do{
                            ret = jarFile.read(data, offset, length-offset);
                            offset += ret;
                        }while(ret > 0);
                    }

                    ClassGen cG = new ClassGen(new ClassParser(new ByteArrayInputStream(data), entry.getName().replaceAll(".class", "")).parse());
                    classes.add(cG);
                }
            }
        }catch(IOException e){
            Logger.getRootLogger().error("Exception: ", e);
        }
    }

    public boolean usesKBotPROPackages(){
        for(ClassGen cG: classes){
            ConstantPoolGen cpg = cG.getConstantPool();
            ConstantPool pool = cpg.getConstantPool();
            for(Constant constant: pool.getConstantPool()){
                if(constant instanceof ConstantClass){
                    ConstantClass constantClass = (ConstantClass) constant;
                    String s = constantClass.getBytes(pool);
                    if(s.contains("com.kbotpro")){
                        return true;
                    }
                    else if(s.contains("com/kbotpro")){
                        return true;
                    }
                    else if(s.contains("com\\kbotpro")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean usesRuntime(){
        for(ClassGen cG: classes){
            ConstantPoolGen cpg = cG.getConstantPool();
            ConstantPool pool = cpg.getConstantPool();
            for(Constant constant: pool.getConstantPool()){
                if(constant instanceof ConstantClass){
                    ConstantClass constantClass = (ConstantClass) constant;
                    String s = constantClass.getBytes(pool);
                    if(s.contains("java.lang.Runtime")){
                        return true;
                    }
                    else if(s.contains("java/lang/Runtime")){
                        return true;
                    }
                    else if(s.contains("java\\lang\\Runtime")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

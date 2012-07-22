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



package com.kbotpro.various;

import com.kbotpro.scriptsystem.runnable.Script;
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import org.apache.log4j.Logger;

import javax.sound.sampled.AudioPermission;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.net.SocketPermission;
import java.security.*;
import java.security.cert.Certificate;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Nov 3, 2009
 * Time: 7:42:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptClassLoader extends ClassLoader {
    private ZipInputStream file;
    private List<Permission> permissionExceptions;
    private HashMap<String, Class> classes = new HashMap<String, Class>();
    private HashMap<String, Object[]> classData = new HashMap<String, Object[]>();
    private Class mainClass;
    private ProtectionDomain protectionDomain;

    /**
     * Creates a new class loader using the <tt>ClassLoader</tt> returned by
     * the method {@link #getSystemClassLoader()
     * <tt>getSystemClassLoader()</tt>} as the parent class loader.
     * <p/>
     * <p> If there is a security manager, its {@link
     * SecurityManager#checkCreateClassLoader()
     * <tt>checkCreateClassLoader</tt>} method is invoked.  This may result in
     * a security exception.  </p>
     *
     * @param file             JarFile
     * @param loadedFromServer
     * @param permissionExceptions
     * @throws SecurityException If a security manager exists and its
     *                           <tt>checkCreateClassLoader</tt> method doesn't allow creation
     *                           of a new class loader.
     */
    public ScriptClassLoader(JarInputStream file, boolean loadedFromServer, List<Permission> permissionExceptions) {
        this.file = file;
        this.permissionExceptions = permissionExceptions;
        try {
            JarEntry entry = null;
            while ((entry = file.getNextJarEntry()) != null) {
                if (entry.getName().endsWith(".class")) {
                    String name = entry.getName();
                    byte[] data;
                    final int length = (int) entry.getSize();
                    if (length == -1) {
                        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int offset = 0;
                        int ret;
                        do {
                            ret = file.read(buffer, 0, 1024);
                            offset += ret;
                            if (ret > 0) {
                                byteArrayOutputStream.write(buffer, 0, ret);
                            }
                        } while (ret > 0);
                        data = byteArrayOutputStream.toByteArray();
                    } else {
                        int offset = 0;
                        data = new byte[length];
                        int ret;
                        do {
                            ret = file.read(data, offset, length - offset);
                            offset += ret;
                        } while (ret > 0);
                    }

                    final JavaClass javaClass = new ClassParser(new ByteArrayInputStream(data), entry.getName().replaceAll(".class", "")).parse();
                    String shortName = name.replaceAll("\\.class", "");
                    byte[] bytes = javaClass.getBytes();
                    String validName = shortName.replaceAll("/", ".");
                    validName = validName.replaceAll("\\\\", ".");
                    classData.put(validName, new Object[]{validName, bytes});
                }
            }
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);
        }

        for (Object[] data : this.classData.values()) {
            String name = (String) data[0];
            if (classes.containsKey(name)) {
                continue;
            }
            byte[] bytes = (byte[]) data[1];
            Class<?> klass = ourDefineClass(name, bytes, 0, bytes.length);
            if (Script.class.isAssignableFrom(klass)) {
                mainClass = klass;
            }
            if (com.kbot2.scriptable.Script.class.isAssignableFrom(klass)) {
                mainClass = klass;
            }
            classes.put(name, klass);
        }
    }

    public Class loadMainClass() {
        return mainClass;
    }


    /**
     * Finds the class with the specified <a href="#name">binary name</a>.
     * This method should be overridden by class loader implementations that
     * follow the delegation model for loading classes, and will be invoked by
     * the {@link #loadClass <tt>loadClass</tt>} method after checking the
     * parent class loader for the requested class.  The default implementation
     * throws a <tt>ClassNotFoundException</tt>.  </p>
     *
     * @param name The <a href="#name">binary name</a> of the class
     * @return The resulting <tt>Class</tt> object
     * @throws ClassNotFoundException If the class could not be found
     * @since 1.2
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (classes.containsKey(name)) {
            return classes.get(name);
        } else if (classData.containsKey(name)) {
            Object[] data = classData.get(name);
            byte[] bytes = (byte[]) data[1];
            Class<?> aClass = ourDefineClass(name, bytes, 0, bytes.length);
            if (Script.class.isAssignableFrom(aClass)) {
                mainClass = aClass;
            }
            if (com.kbot2.scriptable.Script.class.isAssignableFrom(aClass)) {
                mainClass = aClass;
            }
            classes.put(name, aClass);
            return aClass;
        }
        return super.findClass(name);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private Class<?> ourDefineClass(String name, byte[] b, int off, int len) {
        ProtectionDomain domain = getProtectionDomain();
        return defineClass(name, b, off, len, domain);
    }

    public Class[] getAllLoadedClasses() {
        return classes.values().toArray(new Class[classes.size()]);
    }

    private ProtectionDomain getProtectionDomain() {
        if (protectionDomain == null) {
            CodeSource codeSource = new CodeSource(null, (Certificate[]) null);
            PermissionCollection permissionCollection = createPermissions();/*new OurPermissionCollection();*//*new Permissions();
            permissionCollection.add(new AllPermission());
            permissionCollection.add(new ScriptPermission());
            permissionCollection.setReadOnly(); */
            protectionDomain = new ProtectionDomain(codeSource, permissionCollection);
        }
        return protectionDomain;
    }

    private PermissionCollection createPermissions() {
        PermissionCollection p = new Permissions();
        p.add(new PropertyPermission("*", "read"));
        p.add(new FilePermission("-", "read,write,delete"));
        p.add(new SocketPermission("services.runescape.com:80", "connect,resolve"));
        p.add(new SocketPermission("*.runescape.com:80", "connect,resolve"));
        p.add(new SocketPermission("runescape.com:80", "connect,resolve"));
        p.add(new SocketPermission("pastie.org:80", "connect,resolve"));
        p.add(new RuntimePermission("getClassLoader"));
        p.add(new RuntimePermission("stopThread"));
        p.add(new RuntimePermission("modifyThreadGroup"));
        p.add(new RuntimePermission("modifyThread"));
        p.add(new RuntimePermission("getClassLoader"));
        p.add(new RuntimePermission("getStackTrace"));
        p.add(new AudioPermission("play"));
        p.add(new AWTPermission("accessClipboard"));
        p.add(new AWTPermission("accessEventQueue"));
        p.add(new AWTPermission("setWindowAlwaysOnTop"));
        p.add(new AWTPermission("showWindowWithoutWarningBanner"));

        for(Permission permission: permissionExceptions){
            p.add(permission);
        }
        
        return p;
    }
}

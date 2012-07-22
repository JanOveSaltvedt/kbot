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



package com.kbotpro.utils;

import com.kbotpro.bot.injector.ClassData;
import com.kbotpro.ui.BotPanel;
import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.generic.*;
import com.sun.org.apache.bcel.internal.util.InstructionFinder;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 8, 2009
 * Time: 11:22:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class RSLoaderClassLoader extends ClassLoader {
    public final HashMap<String, byte[]> classes = new HashMap<String, byte[]>();
    private ProtectionDomain domain;
    private File jarFile;

    CodeSource codeSource;

    public RSLoaderClassLoader(File jarFile) {
        super();
        this.jarFile = jarFile;
        try {
            codeSource = new CodeSource(new URL("http://runescape.com/"), (CodeSigner[]) null);
            domain = new ProtectionDomain(codeSource, createPermissions());


            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    JavaClass javaClass = new ClassParser(new DataInputStream(jar.getInputStream(entry)), name).parse();
                    ClassGen classGen = new ClassGen(javaClass);
                    boolean somethingChanged = false;
                    if (classGen.getConstantPool().lookupString("random.dat") != -1) {
                        // UID HACK
                        int random = (int) (Math.random() * Integer.MAX_VALUE);
                        ConstantPoolGen cpg = classGen.getConstantPool();
                        ((ConstantString) cpg.getConstant(cpg.lookupString("random.dat"))).setStringIndex(
                                cpg.addUtf8("random-" + random + ".dat"));
                        somethingChanged = true;
                    }
                    if (classGen.getSuperclassName().equals("java.lang.ClassLoader")) {
                        transformClassLoader(classGen);
                        somethingChanged = true;
                    }
                    if (somethingChanged) {
                        javaClass = classGen.getJavaClass();
                    }
                    classes.put(name.replaceAll("\\.class", ""), javaClass.getBytes());
                }
            }
        } catch (Exception e) {
            Logger.getRootLogger().error("Exception: ", e);
        }
    }

    private void transformClassLoader(ClassGen classGen) {
        String classDataClassName = ClassData.class.getCanonicalName();
        String classDataFieldName = null;
        for (Field field : ClassData.class.getDeclaredFields()) {
            if (field.getType().equals(byte[].class)) {
                classDataFieldName = field.getName();
            }
        }

        String botPanelMethodName = null;
        for (java.lang.reflect.Method m : BotPanel.class.getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) || m.getParameterTypes().length != 5 || !m.getParameterTypes()[4].equals(ProtectionDomain.class)) {
                continue;
            }
            botPanelMethodName = m.getName();
        }


        ConstantPoolGen cpg = classGen.getConstantPool();
        classGen.addField(new com.sun.org.apache.bcel.internal.classfile.Field(0, cpg.addUtf8("ourBotPanel"), cpg.addUtf8(Type.getType(BotPanel.class).getSignature()), null, cpg.getConstantPool()));
        for (Method method : classGen.getMethods()) {
            if (!method.getName().equals("loadClass")) {
                continue;
            }
            MethodGen methodGen = new MethodGen(method, classGen.getClassName(), cpg);

            InstructionFactory instructionFactory = new InstructionFactory(classGen);
            InstructionList instructionList = methodGen.getInstructionList();
            InstructionFinder instructionFinder = new InstructionFinder(instructionList);
            for (Iterator<InstructionHandle[]> iterator = instructionFinder.search("aload aload aload iconst_0 aload ARRAYLENGTH aload getfield InvokeInstruction"); iterator.hasNext();) {
                InstructionHandle[] ih = iterator.next();

                InvokeInstruction invokeInstruction = (InvokeInstruction) ih[ih.length - 1].getInstruction();
                if (!invokeInstruction.getMethodName(cpg).equals("defineClass")) {
                    continue;
                }

                InstructionList newList = new InstructionList();
                newList.append(InstructionConstants.ALOAD_0);
                newList.append(instructionFactory.createFieldAccess(classGen.getClassName(), "ourBotPanel", Type.getType(BotPanel.class), Constants.GETFIELD));
                for (int i = 1; i < ih.length - 1; i++) {
                    newList.append(ih[i].getInstruction());
                }
                newList.append(instructionFactory.createInvoke(BotPanel.class.getCanonicalName(), botPanelMethodName, Type.getType(ClassData.class), Type.getArgumentTypes("(Ljava/lang/String;[BIILjava/security/ProtectionDomain;)L"+ClassData.class.getCanonicalName()+";"), Constants.INVOKEVIRTUAL));
                newList.append(instructionFactory.createFieldAccess(classDataClassName, classDataFieldName, Type.getType(byte[].class), Constants.GETFIELD));
                ALOAD aload = (ALOAD) ih[2].getInstruction();
                newList.append(new ASTORE(aload.getIndex()));

                final InstructionHandle firstHandle = instructionList.insert(ih[0], newList);
                if(ih[0].hasTargeters()){
                    for(InstructionTargeter targeter: ih[0].getTargeters()){
                        targeter.updateTarget(ih[0], firstHandle);
                    }
                }
                System.out.println("Injected class def change.");
            }

            methodGen.setMaxStack();
            methodGen.setMaxLocals();
            classGen.replaceMethod(method, methodGen.getMethod());
        }

        for (Method method : classGen.getMethods()) {
            if (!method.getName().equals("<init>")) {
                continue;
            }
            MethodGen methodGen = new MethodGen(method, classGen.getClassName(), cpg);

            InstructionFactory instructionFactory = new InstructionFactory(classGen);
            InstructionList instructionList = methodGen.getInstructionList();
            InstructionList newList = new InstructionList();
            String setClassLoaderMethodName = null;
            for (java.lang.reflect.Method m : BotControl.class.getMethods()) {
                if (!Modifier.isStatic(m.getModifiers()) || m.getParameterTypes().length != 1 || !m.getParameterTypes()[0].equals(ClassLoader.class)) {
                    continue;
                }
                setClassLoaderMethodName = m.getName();

            }
            final Type botPanelType = Type.getType(BotPanel.class);
            newList.append(InstructionConstants.ALOAD_0);  // putfield obgject link
            newList.append(InstructionConstants.ALOAD_0); // ARG
            newList.append(instructionFactory.createInvoke(BotControl.class.getCanonicalName(), setClassLoaderMethodName, botPanelType, new Type[]{Type.getType(ClassLoader.class)}, Constants.INVOKESTATIC));
            newList.append(instructionFactory.createFieldAccess(classGen.getClassName(), "ourBotPanel", botPanelType, Constants.PUTFIELD));
            newList.append(instructionFactory.createPrintln("Injected classloader"));

            InstructionFinder instructionFinder = new InstructionFinder(instructionList);
            for (Iterator<InstructionHandle[]> iterator = instructionFinder.search("invokespecial"); iterator.hasNext();) {
                InstructionHandle[] ih = iterator.next();
                InvokeInstruction invokeInstruction = (InvokeInstruction) ih[0].getInstruction();
                if (invokeInstruction.getMethodName(cpg).equals("<init>") && invokeInstruction.getClassName(cpg).contains("ClassLoader")) {
                    instructionList.append(ih[0], newList);
                    System.out.println("Injected botPanel back link");
                }
            }


            methodGen.setMaxStack();
            methodGen.setMaxLocals();
            classGen.replaceMethod(method, methodGen.getMethod());
        }
    }

    private Permissions createPermissions() {
        final Permissions ps = new Permissions();
        /*ps.add(new AWTPermission("accessEventQueue"));
        ps.add(new AWTPermission("createRobot"));
        ps.add(new AWTPermission("fullScreenExclusive"));
        ps.add(new PropertyPermission("user.home", "read"));
        ps.add(new PropertyPermission("java.vendor", "read"));
        ps.add(new PropertyPermission("java.version", "read"));
        ps.add(new PropertyPermission("os.name", "read"));
        ps.add(new PropertyPermission("os.arch", "read"));
        ps.add(new PropertyPermission("os.version", "read"));
        ps.add(new SocketPermission("*", "connect,resolve"));
        String uDir = System.getProperty("user.home");
        if (uDir != null) {
            uDir += "/";
        } else {
            uDir = "~/";
        }  */
        /*final String[] dirs = {"c:/rscache/", "/rscache/", "c:/windows/", "c:/winnt/",
                "c:/", uDir, "/tmp/", "."};
        final String[] rsDirs = {".jagex_cache_32", ".file_store_32"};
        for (String dir : dirs) {
            final File f = new File(dir);
            ps.add(new FilePermission(dir, "read"));
            if (!f.exists()) {
                continue;
            }
            dir = f.getPath();
            for (final String rsDir : rsDirs) {
                ps.add(new FilePermission(dir + File.separator + rsDir + File.separator + "-", "read"));
                ps.add(new FilePermission(dir + File.separator + rsDir + File.separator + "-", "write"));
            }
        }          */
        /*ps.add(new FilePermission("<<ALL FILES>>", "read,write,execute"));
        ps.add(new RuntimePermission("loadLibrary.*"));
        */
        ps.add(new AllPermission());
        ps.setReadOnly();

        return ps;
    }


    public final Class<?> findClass(String name) throws ClassNotFoundException {
        if (classes.containsKey(name)) {
            byte buffer[] = classes.remove(name);
            return defineClass(name, buffer, 0, buffer.length, domain);
        }
        return super.loadClass(name);
    }

    /**
     * Returns all of the <tt>Packages</tt> defined by this class loader and
     * its ancestors.  </p>
     *
     * @return The array of <tt>Package</tt> objects defined by this
     *         <tt>ClassLoader</tt>
     * @since 1.2
     */
    @Override
    protected Package[] getPackages() {
        System.out.println("Requested packages.");
        return super.getPackages();
    }
}

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



package com.kbotpro.bot.injector;

import com.kbotpro.hooks.Model;
import com.kbotpro.hooks.ModelWrapper;
import com.kbotpro.interfaces.ClientCallback;
import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.generic.*;
import com.sun.org.apache.bcel.internal.util.InstructionFinder;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 17, 2009
 * Time: 1:24:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Injector {

    public static ClassData inject(ClassData inputData, Document doc) {
        try {
            ClassGen cG = new ClassGen(new ClassParser(new ByteArrayInputStream(inputData.classData), inputData.name).parse());

            Element root = doc.getRootElement();
            Element classesElement = root.getChild("classes");
            if (cG.getClassName().equals("client")) {
                if (!isVersion(cG, Integer.parseInt(root.getChildText("RSVersion")))) {
                    JOptionPane.showMessageDialog(null, "This version of Runescape is not yet supported\nPlease wait patiently for an update.", "Outdated!", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalStateException("Not supported yet.");
                }
            }
            for (Element classNode : (List<Element>) classesElement.getChildren("class")) {
                if (classNode.getChildText("className").equals(cG.getClassName())) {
                    for (Element taskElement : (List<Element>) classNode.getChildren("task")) {
                        String type = taskElement.getChildText("type");
                        if (type.equals("injectInterface")) {
                            Element data = taskElement.getChild("data");
                            String interfaceClassName = data.getChildText("interfaceClassName");
                            cG.getInterfaceNames();
                            cG.addInterface(interfaceClassName);
                        } else if (type.equals("getter")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String fieldClassName = data.getChildText("fieldClassName");
                            String fieldName = data.getChildText("fieldName");
                            String fieldSignature = data.getChildText("fieldSignature");
                            int fieldAccessFlags = Integer.parseInt(data.getChildText("fieldAccessFlags"));
                            String returnSignature = data.getChildText("returnSignature");
                            injectSimpleGetter(cG, methodName, fieldClassName, fieldName, fieldSignature, fieldAccessFlags, returnSignature);
                        } else if (type.equals("setter")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String fieldName = data.getChildText("fieldName");
                            String fieldSignature = data.getChildText("fieldSignature");
                            String argumentsSignature = data.getChildText("argumentsSignature");
                            int fieldAccessFlags = Integer.parseInt(data.getChildText("fieldAccessFlags"));
                            int methodAccessFlags = Integer.parseInt(data.getChildText("methodAccessFlags"));
                            injectSimpleSetter(cG, methodName, argumentsSignature, fieldName, fieldSignature, fieldAccessFlags, methodAccessFlags);
                        } else if (type.equals("injectField")) {
                            Element data = taskElement.getChild("data");
                            String fieldName = data.getChildText("fieldName");
                            String fieldSignature = data.getChildText("fieldSignature");
                            int fieldAccessFlags = Integer.parseInt(data.getChildText("fieldAccessFlags"));
                            injectField(cG, fieldName, fieldSignature, fieldAccessFlags);
                        } else if (type.equals("serverMessageCallback")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String methodSignature = data.getChildText("methodSignature");
                            int injectionPos = Integer.parseInt(data.getChildText("injectionPos"));
                            int aloadIndex = Integer.parseInt(data.getChildText("aloadIndex"));
                            makeServerMessageCallback(cG, methodName, methodSignature, aloadIndex, injectionPos);
                        } else if (type.equals("updateRenderDataCallback")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String methodSignature = data.getChildText("methodSignature");
                            makeUpdateRenderDataCallback(cG, methodName, methodSignature);
                        } else if (type.equals("2LevelGetter")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String field1ClassName = data.getChildText("field1ClassName");
                            String field1Name = data.getChildText("field1Name");
                            String field1Signature = data.getChildText("field1Signature");
                            int field1AccessFlags = Integer.parseInt(data.getChildText("field1AccessFlags"));

                            String field2ClassName = data.getChildText("field2ClassName");
                            String field2Name = data.getChildText("field2Name");
                            String field2Signature = data.getChildText("field2Signature");
                            int field2AccessFlags = Integer.parseInt(data.getChildText("field2AccessFlags"));
                            String returnSignature = data.getChildText("returnSignature");
                            createLevel2Getter(cG, methodName, field1ClassName, field1Name, field1Signature, field1AccessFlags, field2ClassName, field2Name, field2Signature, field2AccessFlags, returnSignature);
                        } else if (type.equals("returnSelf")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String returnSignature = data.getChildText("returnSignature");
                            injectReturnSelf(cG, methodName, returnSignature);
                        } else if (type.equals("IComponentMastersHook")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String methodSignature = data.getChildText("methodSignature");
                            String iCompClassName = data.getChildText("IComponentClassName");
                            int injectionPos = Integer.parseInt(data.getChildText("injectionPos"));
                            int aloadIndex = Integer.parseInt(data.getChildText("aloadIndex"));
                            int iloadXIndex = Integer.parseInt(data.getChildText("iloadXIndex"));
                            int iloadYIndex = Integer.parseInt(data.getChildText("iloadYIndex"));
                            injectMasterXHook(cG, methodName, methodSignature, aloadIndex, iloadXIndex, iloadYIndex, iCompClassName, injectionPos);
                        } else if (type.equals("returnInteger")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            int returnValue = Integer.parseInt(data.getChildText("returnValue"));
                            injectReturnInteger(cG, methodName, returnValue);
                        } else if (type.equals("returnNull")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String returnSignature = data.getChildText("returnSignature");
                            injectReturnNull(cG, methodName, returnSignature);
                        } else if (type.equals("CharacterModelDumper")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String methodSignature = data.getChildText("methodSignature");
                            int injectionPos = Integer.parseInt(data.getChildText("injectionPos"));
                            String modelClassName = data.getChildText("modelClassName");
                            int aloadIndex = Integer.parseInt(data.getChildText("aloadIndex"));
                            characterModelDumper(cG, methodName, methodSignature, injectionPos, modelClassName, aloadIndex);
                        } else if (type.equals("ModelDumper")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String methodSignature = data.getChildText("methodSignature");
                            int injectionPos = Integer.parseInt(data.getChildText("injectionPos"));
                            String modelClassName = data.getChildText("modelClassName");
                            int aloadIndex = Integer.parseInt(data.getChildText("aloadIndex"));
                            String fieldName = data.getChildText("fieldName");
                            modelDumper(cG, methodName, methodSignature, injectionPos, modelClassName, aloadIndex, fieldName);
                        } else if (type.equals("IComponentVisibleHook")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String methodSignature = data.getChildText("methodSignature");
                            String iComponentClassNAme = data.getChildText("IComponentClassName");
                            int aloadIndex = Integer.parseInt(data.getChildText("aloadIndex"));
                            String fieldName = data.getChildText("fieldFieldName");
                            String fieldClassName = data.getChildText("fieldClassName");
                            String loopFieldName = data.getChildText("loopFieldName");
                            String loopFieldClassName = data.getChildText("loopClassName");
                            injectIComponentVisibleHook(cG, methodName, methodSignature, iComponentClassNAme, aloadIndex, fieldClassName, fieldName, loopFieldClassName, loopFieldName);
                        } else if (type.equals("CondVoidDisabler")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String methodSignature = data.getChildText("methodSignature");
                            String condMethodName = data.getChildText("condMethodName");
                            conditionalDisableVoidMethod(cG, methodName, methodSignature, condMethodName);
                        } else if (type.equals("AnimatedModelDumper")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String methodSignature = data.getChildText("methodSignature");
                            animatedModelDumper(cG, methodName, methodSignature);
                        } else if (type.equals("CharacterModelDumperOnce")) {
                            Element data = taskElement.getChild("data");
                            String methodName = data.getChildText("methodName");
                            String methodSignature = data.getChildText("methodSignature");
                            int injectionPos = Integer.parseInt(data.getChildText("injectionPos"));
                            String modelClassName = data.getChildText("modelClassName");
                            int aloadIndex = Integer.parseInt(data.getChildText("aloadIndex"));
                            characterModelDumperOnce(cG, methodName, methodSignature, injectionPos, modelClassName, aloadIndex);
                        }

                    }
                    break;
                }
            }
            inputData.classData = cG.getJavaClass().getBytes();
            inputData.offset = 0;
            inputData.length = inputData.classData.length;
        } catch (IOException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
        return inputData;
    }

    private static void characterModelDumperOnce(ClassGen cG, String methodName, String methodSignature, int injectionPos, String modelClassName, int aloadIndex) {
        final ConstantPoolGen cPool = cG.getConstantPool();

        Method method = null;
        for (Method m : cG.getMethods()) {
            if (m.getName().equals(methodName) && m.getSignature().equals(methodSignature)) {
                method = m;
            }
        }


        MethodGen methodGen = new MethodGen(method, cG.getClassName(), cPool);
        InstructionList mInstructionList = methodGen.getInstructionList();

        InstructionFactory iFac = new InstructionFactory(cG, cPool);
        ObjectType modelWrapperType = (ObjectType) Type.getType(ModelWrapper.class);

        InstructionList createNewIList = new InstructionList();
        final InstructionHandle firstHandle = createNewIList.append(InstructionConstants.ALOAD_0);
        createNewIList.append(iFac.createNew(modelWrapperType));
        createNewIList.append(new DUP());
        createNewIList.append(new ALOAD(aloadIndex));
        Type[] args = new Type[]{Type.getType(Model.class)};
        createNewIList.append(iFac.createInvoke(modelWrapperType.getClassName(), "<init>", Type.VOID, args, Constants.INVOKESPECIAL));
        final InstructionHandle putfieldHandle = createNewIList.append(iFac.createPutField(cG.getClassName(), "model", modelWrapperType));

        InstructionList flowIList = new InstructionList();
        /*if(!cG.getClassName().equals("cd")){
            flowIList.append(iFac.createPrintln("Dumping model: "+cG.getClassName()));
        }*/
        flowIList.append(InstructionConstants.ALOAD_0);
        flowIList.append(iFac.createGetField(cG.getClassName(), "model", modelWrapperType));

        final InstructionHandle createNewIListStartHandle = flowIList.append(createNewIList);



        int index = 0;
        final int[] positions = mInstructionList.getInstructionPositions();
        for (; index < positions.length; index++) {
            if (positions[index] == injectionPos) {
                break;
            }
        }

        mInstructionList.append(mInstructionList.getInstructionHandles()[index], flowIList);
        mInstructionList.insert(createNewIListStartHandle,new IFNONNULL(putfieldHandle.getNext()));
        methodGen.setMaxStack();
        methodGen.setMaxLocals();
        methodGen.update();
        cG.replaceMethod(method, methodGen.getMethod());
    }

    private static void animatedModelDumper(ClassGen cG, String methodName, String methodSignature) {
        final ConstantPoolGen cPool = cG.getConstantPool();

        Method method = null;
        for (Method m : cG.getMethods()) {
            if (m.getName().equals(methodName) && m.getSignature().equals(methodSignature)) {
                method = m;
            }
        }


        MethodGen methodGen = new MethodGen(method, cG.getClassName(), cPool);
        InstructionList mInstructionList = methodGen.getInstructionList();

        InstructionFactory iFac = new InstructionFactory(cG, cPool);
        ObjectType modelWrapperType = (ObjectType) Type.getType(ModelWrapper.class);

        InstructionList createNewIList = new InstructionList();
        int aloadIndex = methodGen.getMaxLocals();
        final InstructionHandle firstHandle = createNewIList.append(InstructionConstants.ALOAD_0);
        createNewIList.append(iFac.createNew(modelWrapperType));
        createNewIList.append(new DUP());
        createNewIList.append(new ALOAD(aloadIndex));
        Type[] args = new Type[]{Type.getType(Model.class)};
        createNewIList.append(iFac.createInvoke(modelWrapperType.getClassName(), "<init>", Type.VOID, args, Constants.INVOKESPECIAL));
        final InstructionHandle putfieldHandle = createNewIList.append(iFac.createPutField(cG.getClassName(), "model", modelWrapperType));

        InstructionList updateOldIList = new InstructionList();
        updateOldIList.append(InstructionConstants.ALOAD_0);
        updateOldIList.append(iFac.createGetField(cG.getClassName(), "model", modelWrapperType));
        updateOldIList.append(new ALOAD(aloadIndex));
        updateOldIList.append(iFac.createInvoke(modelWrapperType.getClassName(), "updateModelData", Type.VOID, args, Constants.INVOKEVIRTUAL));


        InstructionList flowIList = new InstructionList();
        flowIList.append(new ASTORE(aloadIndex));
        //flowIList.append(iFac.createPrintln("Invoked getModel in class "+cG.getClassName()));
        flowIList.append(InstructionConstants.ALOAD_0);
        flowIList.append(iFac.createGetField(cG.getClassName(), "model", modelWrapperType));
        flowIList.append(new IFNULL(firstHandle));
        flowIList.append(updateOldIList);
        final InstructionHandle createNewIListStartHandle = flowIList.append(createNewIList);
        flowIList.append(new ALOAD(aloadIndex));


        for(InstructionHandle handle: mInstructionList.getInstructionHandles()){
            if(handle.getInstruction() instanceof ARETURN){
                mInstructionList.insert(handle, flowIList);
                mInstructionList.insert(createNewIListStartHandle, new GOTO(putfieldHandle.getNext()));
                continue;
            }
        }


        methodGen.setMaxStack();
        methodGen.setMaxLocals();
        methodGen.update();
        cG.replaceMethod(method, methodGen.getMethod());
    }

    private static void conditionalDisableVoidMethod(ClassGen cG, String methodName, String methodSignature, String condMethodName) {
        ConstantPoolGen cpg = cG.getConstantPool();
        for (Method method : cG.getMethods()) {
            if (method.getName().equals(methodName)
                    && method.getSignature().equals(methodSignature)) {
                MethodGen methodGen = new MethodGen(method, cG.getClassName(), cpg);
                InstructionList mInstructionList = methodGen.getInstructionList();

                InstructionFactory instructionFactory = new InstructionFactory(cG);
                InstructionList iList = new InstructionList();
                iList.append(instructionFactory.createGetStatic("client", "callback", Type.getType(ClientCallback.class)));
                iList.append(instructionFactory.createInvoke("com/kbotpro/interfaces/ClientCallback", condMethodName, Type.BOOLEAN, new Type[]{}, Constants.INVOKEINTERFACE));
                iList.append(new IFEQ(mInstructionList.getStart()));
                iList.append(new RETURN());
                mInstructionList.insert(iList);

                methodGen.setMaxStack();
                methodGen.setMaxLocals();
                methodGen.update();
                cG.replaceMethod(method, methodGen.getMethod());
                cG.setConstantPool(cpg);
            }
        }

    }

    private static void injectIComponentVisibleHook(ClassGen cG, String methodName, String methodSignature, String iComponentClassName, int aloadIndex, String fieldClassName, String fieldName, String loopFieldClassName, String loopFieldName) {
        final ConstantPoolGen cpg = cG.getConstantPool();

        Method method = null;
        for (Method m : cG.getMethods()) {
            if (m.getName().equals(methodName) && m.getSignature().equals(methodSignature)) {
                method = m;
            }
        }

        InstructionFactory instructionFactory = new InstructionFactory(cG);

        MethodGen methodGen = new MethodGen(method, cG.getClassName(), cpg);
        InstructionList mInstructionList = methodGen.getInstructionList();
        InstructionFinder instructionFinder = new InstructionFinder(mInstructionList);
        for (Iterator<InstructionHandle[]> iterator = instructionFinder.search("getstatic iload baload ((ifeq)|(ifne))"); iterator.hasNext();) {
            InstructionHandle[] ih = iterator.next();
            FieldInstruction fieldInstruction = (FieldInstruction) ih[0].getInstruction();
            if (fieldInstruction.getClassName(cpg).equals(fieldClassName) && fieldInstruction.getFieldName(cpg).equals(fieldName)) {
                InstructionHandle ifInsHandle = ih[ih.length - 1];
                IfInstruction ifInstruction = (IfInstruction) ifInsHandle.getInstruction();
                /*
                 Inject the
                 icomponent.visible = true;
                  */
                InstructionList iList1 = new InstructionList();
                iList1.append(new ALOAD(aloadIndex));
                //iList1.append(instructionFactory.createConstant(true)); // TRUE
                iList1.append(instructionFactory.createFieldAccess(loopFieldClassName, loopFieldName, Type.INT, Constants.GETSTATIC));
                iList1.append(instructionFactory.createFieldAccess(iComponentClassName, "visibleLoopCycleStatus", Type.INT, Constants.PUTFIELD));
                //iList1.append(instructionFactory.createPrintln("Called!"));
                InstructionHandle target = ifInstruction.getTarget();
                if (ifInstruction instanceof IFEQ) {
                    mInstructionList.append(ifInsHandle, iList1);
                } else {
                    mInstructionList.append(target, iList1);
                }


            }
        }
        /*for (Iterator<InstructionHandle[]> iterator = instructionFinder.search("aaload astore"); iterator.hasNext();) {
            InstructionHandle[] ih = iterator.next();
            ASTORE astore = (ASTORE) ih[1].getInstruction();
            if (astore.getIndex() != aloadIndex) {
                continue;
            }
            InstructionList iList2 = new InstructionList();
            iList2.append(new ALOAD(aloadIndex));
            iList2.append(instructionFactory.createConstant(false)); // FALSE
            iList2.append(instructionFactory.createFieldAccess(iComponentClassName, "visible", Type.BOOLEAN, Constants.PUTFIELD));
            mInstructionList.append(ih[1], iList2);

        }*/
        methodGen.setMaxStack();
        methodGen.setMaxLocals();
        methodGen.update();
        cG.replaceMethod(method, methodGen.getMethod());
    }

    private static void modelDumper(ClassGen cG, String methodName, String methodSignature, int injectionPos, String modelClassName, int aloadIndex, String fieldName) {
        final ConstantPoolGen cPool = cG.getConstantPool();

        Method method = null;
        for (Method m : cG.getMethods()) {
            if (m.getName().equals(methodName) && m.getSignature().equals(methodSignature)) {
                method = m;
            }
        }


        MethodGen methodGen = new MethodGen(method, cG.getClassName(), cPool);
        InstructionList mInstructionList = methodGen.getInstructionList();
        InstructionList tempInstructionList = new InstructionList();
        InstructionFactory iFac = new InstructionFactory(cG, cPool);

        tempInstructionList.append(InstructionConstants.ALOAD_0);
        ObjectType modelType = (ObjectType) Type.getType("L" + modelClassName + ";");
        tempInstructionList.append(new ALOAD(aloadIndex));
        tempInstructionList.append(iFac.createPutField(cG.getClassName(), fieldName, modelType));

        int index = 0;
        final int[] positions = mInstructionList.getInstructionPositions();
        for (; index < positions.length; index++) {
            if (positions[index] == injectionPos) {
                break;
            }
        }
        mInstructionList.append(mInstructionList.getInstructionHandles()[index], tempInstructionList);
        methodGen.setMaxStack();
        methodGen.setMaxLocals();
        methodGen.update();
        cG.replaceMethod(method, methodGen.getMethod());
    }

    private static void characterModelDumper(ClassGen cG, String methodName, String methodSignature, int injectionPos, String modelClassName, int aloadIndex) {
        final ConstantPoolGen cPool = cG.getConstantPool();

        Method method = null;
        for (Method m : cG.getMethods()) {
            if (m.getName().equals(methodName) && m.getSignature().equals(methodSignature)) {
                method = m;
            }
        }


        MethodGen methodGen = new MethodGen(method, cG.getClassName(), cPool);
        InstructionList mInstructionList = methodGen.getInstructionList();

        InstructionFactory iFac = new InstructionFactory(cG, cPool);
        ObjectType modelWrapperType = (ObjectType) Type.getType(ModelWrapper.class);

        InstructionList createNewIList = new InstructionList();
        final InstructionHandle firstHandle = createNewIList.append(InstructionConstants.ALOAD_0);
        createNewIList.append(iFac.createNew(modelWrapperType));
        createNewIList.append(new DUP());
        createNewIList.append(new ALOAD(aloadIndex));
        Type[] args = new Type[]{Type.getType(Model.class)};
        createNewIList.append(iFac.createInvoke(modelWrapperType.getClassName(), "<init>", Type.VOID, args, Constants.INVOKESPECIAL));
        final InstructionHandle putfieldHandle = createNewIList.append(iFac.createPutField(cG.getClassName(), "model", modelWrapperType));

        InstructionList updateOldIList = new InstructionList();
        updateOldIList.append(InstructionConstants.ALOAD_0);
        updateOldIList.append(iFac.createGetField(cG.getClassName(), "model", modelWrapperType));
        updateOldIList.append(new ALOAD(aloadIndex));
        updateOldIList.append(iFac.createInvoke(modelWrapperType.getClassName(), "updateModelData", Type.VOID, args, Constants.INVOKEVIRTUAL));


        InstructionList flowIList = new InstructionList();
        /*if(!cG.getClassName().equals("cd")){
            flowIList.append(iFac.createPrintln("Dumping model: "+cG.getClassName()));
        } */
        flowIList.append(InstructionConstants.ALOAD_0);
        flowIList.append(iFac.createGetField(cG.getClassName(), "model", modelWrapperType));
        flowIList.append(new IFNULL(firstHandle));
        flowIList.append(updateOldIList);
        final InstructionHandle createNewIListStartHandle = flowIList.append(createNewIList);


        int index = 0;
        final int[] positions = mInstructionList.getInstructionPositions();
        for (; index < positions.length; index++) {
            if (positions[index] == injectionPos) {
                break;
            }
        }

        mInstructionList.append(mInstructionList.getInstructionHandles()[index], flowIList);
        mInstructionList.insert(createNewIListStartHandle, new GOTO(putfieldHandle.getNext()));
        methodGen.setMaxStack();
        methodGen.setMaxLocals();
        methodGen.update();
        cG.replaceMethod(method, methodGen.getMethod());
    }

    private static void injectReturnNull(ClassGen cG, String methodName, String returnSignature) {
        ConstantPoolGen cp = cG.getConstantPool();
        InstructionList iList = new InstructionList();
        MethodGen method = new MethodGen(Constants.ACC_PUBLIC,
                Type.getType(returnSignature), Type.NO_ARGS, new String[]{}, methodName,
                cG.getClassName(), iList, cp);
        InstructionFactory instructionFactory = new InstructionFactory(cG, cp);
        iList.append(new ACONST_NULL());
        iList.append(new ARETURN());
        method.setMaxStack();
        method.setMaxLocals();
        cG.addMethod(method.getMethod());
    }

    private static void injectReturnInteger(ClassGen cG, String methodName, int returnValue) {
        ConstantPoolGen cp = cG.getConstantPool();
        InstructionList iList = new InstructionList();
        MethodGen method = new MethodGen(Constants.ACC_PUBLIC,
                Type.INT, Type.NO_ARGS, new String[]{}, methodName,
                cG.getClassName(), iList, cp);
        InstructionFactory instructionFactory = new InstructionFactory(cG, cp);
        iList.append(new SIPUSH((short) returnValue));
        iList.append(new IRETURN());
        method.setMaxStack();
        method.setMaxLocals();
        cG.addMethod(method.getMethod());
    }

    private static boolean isVersion(ClassGen cG, int version) {
        for (Method m : cG.getMethods()) {
            if (!m.getName().equals("main")) {
                continue;
            }
            InstructionList iList = new InstructionList(m.getCode().getCode());
            InstructionFinder instructionFinder = new InstructionFinder(iList);
            InstructionFinder.CodeConstraint codeConstraint = new InstructionFinder.CodeConstraint() {
                public boolean checkCode(InstructionHandle[] match) {
                    int value = ((SIPUSH) match[0].getInstruction()).getValue().intValue();
                    return value >= 550 && value <= 700;
                }
            };
            for (Iterator<InstructionHandle[]> iterator = instructionFinder.search("sipush", codeConstraint); iterator.hasNext();) {
                InstructionHandle[] ih = iterator.next();
                int value = ((SIPUSH) ih[0].getInstruction()).getValue().intValue();
                return value == version;
            }
        }
        return false;
    }

    private static void injectReturnSelf(ClassGen cG, String methodName, String returnSignature) {

        ConstantPoolGen cp = cG.getConstantPool();
        InstructionList iList = new InstructionList();
        MethodGen method = new MethodGen(Constants.ACC_PUBLIC,
                Type.getType(returnSignature), Type.NO_ARGS, new String[]{}, methodName,
                cG.getClassName(), iList, cp);
        InstructionFactory instructionFactory = new InstructionFactory(cG, cp);
        iList.append(new ALOAD(0));
        iList.append(instructionFactory.createCheckCast((ReferenceType) Type.getType(returnSignature)));
        iList.append(new ARETURN());
        method.setMaxStack();
        method.setMaxLocals();
        cG.addMethod(method.getMethod());
    }

    private static void createLevel2Getter(ClassGen cG, String methodName, String field1ClassName, String field1Name, String field1Signature, int field1AccessFlags, String field2ClassName, String field2Name, String field2Signature, int field2AccessFlags, String returnSignature) {
        boolean isField1Static = (field1AccessFlags & Constants.ACC_STATIC) != 0;
        boolean isField2Static = (field2AccessFlags & Constants.ACC_STATIC) != 0;

        ConstantPoolGen cp = cG.getConstantPool();
        InstructionList iList = new InstructionList();
        MethodGen method = new MethodGen(Constants.ACC_PUBLIC,
                Type.getType(returnSignature), Type.NO_ARGS, new String[]{}, methodName,
                cG.getClassName(), iList, cp);
        InstructionFactory iFact = new InstructionFactory(cG, cp);
        Instruction pushSelf = new ALOAD(0);
        Instruction getFirstField;
        if (isField1Static)
            getFirstField = iFact.createFieldAccess(field1ClassName, field1Name, Type.getType(field1Signature), Constants.GETSTATIC);
        else
            getFirstField = iFact.createFieldAccess(field1ClassName, field1Name, Type.getType(field1Signature), Constants.GETFIELD);

        Instruction getSecondField;
        if (isField2Static)
            getSecondField = iFact.createFieldAccess(field2ClassName, field2Name, Type.getType(field2Signature), Constants.GETSTATIC);
        else
            getSecondField = iFact.createFieldAccess(field2ClassName, field2Name, Type.getType(field2Signature), Constants.GETFIELD);
        Instruction returner = InstructionFactory.createReturn(Type.getType(returnSignature));
        if (!isField1Static) {
            iList.append(pushSelf);
        }
        iList.append(getFirstField);
        iList.append(getSecondField);
        if (field2Signature.equals("I") && returnSignature.equals("F")) {
            iList.append(new I2F());
        } else if (field2Signature.equals("I") && returnSignature.equals("S")) {
            iList.append(new I2S());
        }
        iList.append(returner);
        method.setMaxStack();
        method.setMaxLocals();

        cG.addMethod(method.getMethod());

    }

    private static void makeUpdateRenderDataCallback(ClassGen cG, String methodName, String methodSignature) {
        ConstantPoolGen cpg = cG.getConstantPool();
        for (Method method : cG.getMethods()) {
            if (method.getName().equals(methodName)
                    && method.getSignature().equals(methodSignature)) {
                MethodGen methodGen = new MethodGen(method, cG.getClassName(), cpg);
                InstructionList mInstructionList = methodGen.getInstructionList();

                InstructionFinder instructionFinder = new InstructionFinder(mInstructionList);
                for (Iterator<InstructionHandle[]> iterator = instructionFinder.search("return"); iterator.hasNext();) {
                    InstructionHandle[] ih = iterator.next();
                    InstructionList newList = new InstructionList();
                    InstructionFactory instructionFactory = new InstructionFactory(cG, cpg);
                    newList.append(instructionFactory.createGetStatic("client", "callback", Type.getType(ClientCallback.class)));
                    newList.append(instructionFactory.createInvoke("com/kbotpro/interfaces/ClientCallback", "updateRenderData", Type.VOID, new Type[]{}, Constants.INVOKEINTERFACE));
                    InstructionHandle injectHandle = mInstructionList.insert(ih[0], newList);
                    if (ih[0].hasTargeters()) {
                        for (InstructionTargeter targeter : ih[0].getTargeters()) {
                            targeter.updateTarget(ih[0], injectHandle);
                        }
                    }
                    mInstructionList.setPositions();
                }
                methodGen.setMaxStack();
                methodGen.setMaxLocals();
                methodGen.update();
                cG.replaceMethod(method, methodGen.getMethod());
                cG.setConstantPool(cpg);
            }
        }
    }

    private static void injectField(ClassGen cG, String fieldName, String fieldSignature, int fieldAccessFlags) {
        ConstantPoolGen cpg = cG.getConstantPool();
        cG.addField(new Field(fieldAccessFlags, cpg.addUtf8(fieldName), cpg.addUtf8(fieldSignature), null, cpg.getConstantPool()));
    }

    private static void makeServerMessageCallback(ClassGen cG, String methodName, String methodSignature, int aloadIndex, int injectionPos) {
        ConstantPoolGen cpg = cG.getConstantPool();
        for (Method method : cG.getMethods()) {
            if (method.getName().equals(methodName)
                    && method.getSignature().equals(methodSignature)) {
                MethodGen methodGen = new MethodGen(method, cG.getClassName(), cpg);
                InstructionList mInstructionList = methodGen.getInstructionList();

                InstructionList newList = new InstructionList();
                InstructionFactory instructionFactory = new InstructionFactory(cG, cpg);
                newList.append(instructionFactory.createGetStatic("client", "callback", Type.getType(ClientCallback.class)));
                newList.append(new ALOAD(aloadIndex));
                newList.append(instructionFactory.createInvoke("com/kbotpro/interfaces/ClientCallback", "serverMessage", Type.VOID, new Type[]{Type.STRING}, Constants.INVOKEINTERFACE));
                int index = 0;

                final int[] positions = mInstructionList.getInstructionPositions();
                for (; index < positions.length; index++) {
                    if (positions[index] == injectionPos) {
                        break;
                    }
                }
                mInstructionList.insert(mInstructionList.getInstructionHandles()[index].getNext(), newList);
                methodGen.setMaxStack();
                methodGen.setMaxLocals();
                methodGen.update();
                cG.replaceMethod(method, methodGen.getMethod());
                cG.setConstantPool(cpg);
            }
        }
    }

    private static void injectSimpleSetter(ClassGen cG, String methodName, String argumentsSignature, String fieldName, String fieldSignature, int fieldAccessFlags, int methodAccessFlags) {
        boolean staticField = (fieldAccessFlags & Constants.ACC_STATIC) != 0;
        boolean staticMethod = (methodAccessFlags & Constants.ACC_STATIC) != 0;

        ConstantPoolGen cp = cG.getConstantPool();
        InstructionList iList = new InstructionList();
        MethodGen method = new MethodGen(methodAccessFlags,
                Type.VOID, Type.getArgumentTypes("(" + argumentsSignature + ")V"), null, methodName,
                cG.getClassName(), iList, cp);

        InstructionFactory iFact = new InstructionFactory(cG, cp);
        Instruction thisPush = new ALOAD(0);
        Instruction set;
        if (staticField)
            set = iFact.createFieldAccess(cG.getClassName(), fieldName, Type.getType(fieldSignature), Constants.PUTSTATIC);
        else
            set = iFact.createFieldAccess(cG.getClassName(), fieldName, Type.getType(fieldSignature), Constants.PUTFIELD);
        Instruction returner = InstructionFactory.createReturn(Type.VOID);
        if (!staticField) {
            iList.append(thisPush);
        }
        int localIndex = staticMethod ? 0 : 1;
        if (Type.getType(argumentsSignature) instanceof ObjectType) {

            iList.append(new ALOAD(localIndex));
        } else {
            iList.append(new ILOAD(localIndex));
        }
        iList.append(set);
        iList.append(returner);
        method.setMaxStack();
        method.setMaxLocals();

        cG.addMethod(method.getMethod());
    }

    private static void injectSimpleGetter(ClassGen cG, String methodName, String fieldClassName, String fieldName, String fieldSignature, int fieldAccessFlags, String returnSignature) {
        boolean isStatic = (fieldAccessFlags & Constants.ACC_STATIC) != 0;

        ConstantPoolGen cp = cG.getConstantPool();
        InstructionList iList = new InstructionList();
        MethodGen method = new MethodGen(Constants.ACC_PUBLIC,
                Type.getType(returnSignature), Type.NO_ARGS, new String[]{}, methodName,
                cG.getClassName(), iList, cp);
        InstructionFactory iFact = new InstructionFactory(cG, cp);
        Instruction pushThis = new ALOAD(0);
        Instruction get;
        if (isStatic)
            get = iFact.createFieldAccess(fieldClassName, fieldName, Type.getType(fieldSignature), Constants.GETSTATIC);
        else
            get = iFact.createFieldAccess(fieldClassName, fieldName, Type.getType(fieldSignature), Constants.GETFIELD);
        Instruction returner = InstructionFactory.createReturn(Type.getType(returnSignature));
        if (!isStatic) {
            iList.append(pushThis);
        }
        iList.append(get);
        if (fieldSignature.equals("I") && returnSignature.equals("F")) {
            iList.append(new I2F());
        }
        iList.append(returner);
        method.setMaxStack();
        method.setMaxLocals();

        cG.addMethod(method.getMethod());
    }

    private static void injectMasterXHook(ClassGen cG, String methodName, String methodSignature, int aloadIndex, int iloadXIndex, int iloadYIndex, String iCompClassName, int injectionPos) {
        final ConstantPoolGen cPool = cG.getConstantPool();

        Method method = null;
        for (Method m : cG.getMethods()) {
            if (m.getName().equals(methodName) && m.getSignature().equals(methodSignature)) {
                method = m;
            }
        }


        MethodGen methodGen = new MethodGen(method, cG.getClassName(), cPool);
        InstructionList mInstructionList = methodGen.getInstructionList();
        InstructionList tempInstructionList = new InstructionList();
        InstructionFactory iFac = new InstructionFactory(cG, cPool);

        tempInstructionList.append(new ILOAD(iloadXIndex));
        tempInstructionList.append(iFac.createPutField(iCompClassName, "masterX", Type.INT));
        tempInstructionList.append(new ALOAD(aloadIndex));

        tempInstructionList.append(new ILOAD(iloadYIndex));
        tempInstructionList.append(iFac.createPutField(iCompClassName, "masterY", Type.INT));
        tempInstructionList.append(new ALOAD(aloadIndex));

        int index = 0;
        final int[] positions = mInstructionList.getInstructionPositions();
        for (; index < positions.length; index++) {
            if (positions[index] == injectionPos) {
                break;
            }
        }
        mInstructionList.append(mInstructionList.getInstructionHandles()[index], tempInstructionList);
        methodGen.setMaxStack();
        methodGen.setMaxLocals();
        methodGen.update();
        cG.replaceMethod(method, methodGen.getMethod());
    }
}

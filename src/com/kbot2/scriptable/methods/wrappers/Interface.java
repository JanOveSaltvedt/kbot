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

package com.kbot2.scriptable.methods.wrappers;

import com.kbot2.bot.BotEnvironment;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.awt.*;
import java.util.*;

import static com.kbot2.scriptable.methods.Calculations.random;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 3:29:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Interface extends Wrapper {
    private IComponent inter;
    private Interface parrentInterface;
    private InterfaceGroup parrentInterfaceGroup;
    private int index;

    public Interface(BotEnvironment botEnv, IComponent iComponent, Interface parrentInterface, InterfaceGroup parrentInterfaceGroup) {
        super(botEnv);
        this.inter = iComponent;
        this.parrentInterface = parrentInterface;
        this.parrentInterfaceGroup = parrentInterfaceGroup;
    }

    /**
     * Gets a child interface.
     *
     * @param childID
     * @return may return null if interface does not exist.
     */
    public Interface getChild(int childID) {
        IComponent[] children = inter.getChildren();
        if(children == null)
            return null;
        if (childID < 0 || childID >= children.length)
            return null;
        return new Interface(botEnv, children[childID], this, parrentInterfaceGroup);
    }

    public int[] getItemIDArray() {
        Item[] items = getItems();
        int[] out = new int[items.length];
        for(int i = 0; i < items.length; i++){
            out[i] = items[i].getID();
        }

        return out;
    }

    public int[] getItemStackSizeArray() {
        Item[] items = getItems();
        int[] out = new int[items.length];
        for(int i = 0; i < items.length; i++){
            out[i] = items[i].getStackSize();
        }

        return out;
    }

    public Item[] getItems(){
        com.kbotpro.scriptsystem.wrappers.Item[] items = botEnv.proBotEnvironment.inventory.getItems();
        Item[] out = new Item[items.length];
        for(int i = 0; i < items.length; i++){
            out[i] = new ExtendedItem(items[i]);
        }
        return out;
    }

    public boolean hasParrent() {
        return parrentInterface != null;
    }

    public Interface getParrent() {
        return parrentInterface;
    }

    public int getX() {
        return inter.getRelativeX();
    }

    public int getY() {
        return inter.getRelativeY();
    }

    public int getMasterX() {
        return inter.getAbsoluteX()-inter.getRelativeX();
    }

    public int getMasterY() {
        return inter.getAbsoluteY()-inter.getRelativeY();
    }

    public int getAbsoluteX() {
        return inter.getAbsoluteX();
    }

    public int getAbsoluteY() {
        return inter.getAbsoluteY();
    }

    public int getHeight() {
        return inter.getHeight();
    }

    public int getWidth() {
        return inter.getWidth();
    }

    public Rectangle getArea() {
        return new Rectangle(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
    }

    public Point getCenter() {
        Rectangle area = getArea();
        return new Point((int) area.getCenterX(), (int) area.getCenterY());
    }

/**
     * Hack for interface debugger.
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Hack for interface debugger.
     *
     * @return
     */
    public String toString() {
        return "" + index;
    }

    public Interface[] getChildren() {
        if(inter != null) {
            java.util.List<Interface> out = new LinkedList<Interface>();
            IComponent[] interfaces = inter.getChildren();
            if (interfaces == null)
                return null;
            for (IComponent child : interfaces) {
                if (child == null)
                    continue;
                out.add(new Interface(botEnv, child, this, parrentInterfaceGroup));
            }
            return out.toArray(new Interface[1]);
        }
        return null;
    }

    public int getContainedItemID() {
        return inter.getElementID();
    }

    public int getContainedItemStackSize() {
        return inter.getElementStackSize();
    }

    public String getContainedItemName() {
        return inter.getElementName();
    }

    public String[] getActions() {
        return inter.getActions();
    }

    public String getText() {
        return inter.getText();
    }

    public int getTextColor() {
        return inter.getTextColor();
    }

    public int getTextureID() {
        return inter.getTextureID();
    }

    public int getHorizontalScrollBarSize() {
        return -1;
    }

    public int getHorizontalScrollBarThumbPosition() {
        return -1;
    }

    public int getHorizontalScrollBarThumbSize() {
        return -1;
    }

    public int getVerticalScrollBarSize() {
        return -1;
    }

    public int getVerticalScrollBarThumbPosition() {
        return -1;
    }

    public int getVerticalScrollBarThumbSize() {
        return -1;
    }

    public boolean textContains(String text) {
        String s = getText();
        return s != null && s.contains(text);
    }

    public boolean textContainsIgnoreCase(String text) {
        String s = getText();
        return s != null && s.toLowerCase().contains(text.toLowerCase());
    }

    public Point getRandomPointInside(){
        Rectangle rect = getArea();
        int randomX = random(rect.x, rect.x + rect.width);
        int randomY = random(rect.y, rect.y + rect.height);
        return new Point(randomX, randomY);
    }

    /**
     * Gets the model id of the interface.
     * @return -1 if null.
     */
    public int getModelID(){
		return inter.getModelID();
    }

    /**
     * Gets the model type of the interface
     * @return -1 if null
     */
    public int getModelType(){
        return inter.getModelType();
    }

    public void doClick() {

        Rectangle rect = getArea();
        final Point mousePos = getMethods().getMousePos();
        if(rect.contains(mousePos)){
            int random = random(0, 5);
            if(random < 4){
                botEnv.mouse.clickMouse(true);
                return;
            }
            else if(random == 4){
                Point ranPoint = new Point(mousePos.x+random(-5, 5), mousePos.y+random(-5, 5));
                if(rect.contains(ranPoint)){
                    botEnv.methods.clickMouse(ranPoint, true);
                    return;
                }
            }
        }
        int randomX = random(rect.x, rect.x + rect.width);
        int randomY = random(rect.y, rect.y + rect.height);
        botEnv.methods.clickMouse(new Point(randomX, randomY), true);
    }

    public boolean doAction(String actionContains) {
        Rectangle rect = getArea();
        int randomX = random(rect.x, rect.x + rect.width);
        int randomY = random(rect.y, rect.y + rect.height);
        botEnv.methods.moveMouse(new Point(randomX, randomY));
        return botEnv.methods.atMenu(actionContains);
    }

    public boolean isValid(){
        return inter.isValid();
    }

    public String getReflectedFieldsString(){
        return ReflectionToStringBuilder.reflectionToString(inter, ToStringStyle.MULTI_LINE_STYLE, true);
    }

    public Point getScreenPos() {
        return new Point(getAbsoluteX(), getAbsoluteY());
    }


}

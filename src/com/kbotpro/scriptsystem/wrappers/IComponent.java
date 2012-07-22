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



package com.kbotpro.scriptsystem.wrappers;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.interfaces.Targetable;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.scriptsystem.input.jobs.MouseHoverJob;
import com.kbotpro.scriptsystem.input.jobs.MouseJob;
import com.kbotpro.scriptsystem.input.callbacks.MouseMoveListener;
import com.kbotpro.scriptsystem.various.KTimer;

import java.awt.*;

/**
 * IComponent is very much the same as a Component in awt in Java.
 * It can be a window or just a part of a window.
 *
 * Usually many IComponenents is grouped together to create a Runescape interface.
 */
public class IComponent extends Wrapper implements Targetable{
    private com.kbotpro.hooks.IComponent rIComponent;
    private IComponent parrent;
    private Interface parrentInterface;

    /**
     * Constructor for IComponent
     * @param botEnv botEnvironment to connect to the rest of the bot
     * @param rIComponent Reflection wrapper for IComponent
     * @param parrent the parrent IComponent or null if no parrent.
     */
    public IComponent(BotEnvironment botEnv, com.kbotpro.hooks.IComponent rIComponent, IComponent parrent) {
        super(botEnv);
        this.rIComponent = rIComponent;
        this.parrent = parrent;
    }

    /**
     * Gets the ID of the contained element.
     * This can for example be the ID of a bank item or the object ID of a spinning object.
     * @return Returns the ID of the contained element.
     */
    public int getElementID(){
        return rIComponent.getElementID();
    }

    /**
     * This gets the contained elements stack size.
     * This can for example be the stack size of the a bank item.
     * @return Return an integer containing the stack size of the contained element.
     */
    public int getElementStackSize(){
        return rIComponent.getElementStackSize();
    }

    /**
     * This gets the contained elements name.
     * For example the name of an item in the bank
     * @return Returns a String containing the name or null if this IComponent does not have a contained element name
     */
    public String getElementName(){
        return rIComponent.getElementName();
    }

    /**
     * Checks if the element is visible, for example a bank item is visible on your current tab.
     * @return Return true if is visible.
     */
    public boolean isElementVisible() {
        return !rIComponent.isElementVisible();
    }

    /**
     * Gets the upper left screen position of the interface.
     * @return Return Point containing screen position.
     */
    public Point getUpperLeftScreenPos(){
        int x = rIComponent.getX();
        int y = rIComponent.getY();

        int indexArray = rIComponent.getVisibleArrayIndex();
        if(indexArray != -1){
            Rectangle[] bounds = getClient().getInterfaceBounds();
            if(bounds != null && bounds[indexArray] != null){
                return new Point(x+bounds[indexArray].x, y+bounds[indexArray].y);
            }
        }
        if(parrent != null){
            Point point = parrent.getUpperLeftScreenPos();
            point.x += x;
            point.y += y;
            return point;
        }
        return new Point(x, y);
    }

    /**
     * Gets the screen boundings of the interface.
     * @return Returns a Rectangle containing screen position and bounds.
     */
    public Rectangle getBounds(){
        int x = rIComponent.getX();
        int y = rIComponent.getY();

        x += rIComponent.getMasterX();
        y += rIComponent.getMasterY();
        /*
        Bad solution
        int indexArray = rIComponent.getVisibleArrayIndex();
        if(indexArray != -1){
            Rectangle[] bounds = getClient().getInterfaceBounds();
            if(bounds != null && bounds[indexArray] != null){
                return new Rectangle(x+bounds[indexArray].x, y+bounds[indexArray].y, getWidth(), getHeight());
            }
        }
        if(parrent != null){
            Point point = parrent.getUpperLeftScreenPos();
            return new Rectangle(point.x+x, point.y+y, getWidth(), getHeight());
        }*/
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    /**
     * Basically the same as bounds but a bit smaller so it does not include the edges.
     * @return
     */
    public Rectangle getClickableArea(){
        Rectangle bounds = getBounds();
        return new Rectangle(bounds.x+3, bounds.y+3, bounds.width-6, bounds.height-6);
    }

    /**
     * Gets the width of the IComponent.
     * @return Returns an integer with the width of the IComponent
     */
    public int getWidth(){
        return rIComponent.getWidth();
    }

    /**
     * Gets the height of the IComponent
     * @return Returns an integer with the height of the IComponent
     */
    public int getHeight(){
        return rIComponent.getHeight();
    }

    /**
     * Returns the screen posituon of the center of the IComponent.
     * @return Returns a Point containing the center of the IComponent
     */
    public Point getScreenPos(){
        Rectangle bounds = getBounds();
        return new Point((int)bounds.getCenterX(), (int)bounds.getCenterY());
    }

    /**
     * Gets the menu actions for the IComponent
     * @return Returns a String array or null if no actions.
     */
    public String[] getActions(){
        return rIComponent.getActions();
    }

    /**
     * Gets the text of the IComponent
     * @return Returns a String or null if no text.
     */
    public String getText(){
        return rIComponent.getText();
    }

    /**
     * Gets the texture ID of the IComponent
     * @return int containtaining the texture ID.
     */
    public int getTextureID(){
        return rIComponent.getTextureID();
    }

    /**
     * Gets the text color of the IComponent
     * @return int.
     */
    public int getTextColor(){
        return rIComponent.getTextColor();
    }

    /**
     * Gets the children of the IComponent
     * @return Return an array of IComponents or an empty array if no children
     */
    public IComponent[] getChildren(){
        com.kbotpro.hooks.IComponent[] rChildren = rIComponent.getChildren();
        if(rChildren == null){
            return new IComponent[0];
        }

        IComponent[] out = new IComponent[rChildren.length];
        for(int i = 0; i < rChildren.length; i++){
            if(rChildren[i] == null){
                continue;
            }
            out[i] = new IComponent(botEnv, rChildren[i], this);
            out[i].parrentInterface = parrentInterface;
        }
        return out;
    }

    /**
     * Checks if the IComponents has child components.
     * @return
     */
    public boolean hasChildren(){
        com.kbotpro.hooks.IComponent[] rChildren = rIComponent.getChildren();
        return rChildren != null && rChildren.length != 0;
    }

    /**
     * Gets the parrent interface for the IComponent
     * @return
     */
    public Interface getParrentInterface() {
        return parrentInterface;
    }

    /**
     * Used internally by the bot to update the parrent interface
     * @param parrentInterface
     */
    void setParrentInterface(Interface parrentInterface) {
        this.parrentInterface = parrentInterface;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p/>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     * <code>x</code>, <code>x.equals(x)</code> should return
     * <code>true</code>.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     * <code>x</code> and <code>y</code>, <code>x.equals(y)</code>
     * should return <code>true</code> if and only if
     * <code>y.equals(x)</code> returns <code>true</code>.
     * <li>It is <i>transitive</i>: for any non-null reference values
     * <code>x</code>, <code>y</code>, and <code>z</code>, if
     * <code>x.equals(y)</code> returns <code>true</code> and
     * <code>y.equals(z)</code> returns <code>true</code>, then
     * <code>x.equals(z)</code> should return <code>true</code>.
     * <li>It is <i>consistent</i>: for any non-null reference values
     * <code>x</code> and <code>y</code>, multiple invocations of
     * <tt>x.equals(y)</tt> consistently return <code>true</code>
     * or consistently return <code>false</code>, provided no
     * information used in <code>equals</code> comparisons on the
     * objects is modified.
     * <li>For any non-null reference value <code>x</code>,
     * <code>x.equals(null)</code> should return <code>false</code>.
     * </ul>
     * <p/>
     * The <tt>equals</tt> method for class <code>Object</code> implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values <code>x</code> and
     * <code>y</code>, this method returns <code>true</code> if and only
     * if <code>x</code> and <code>y</code> refer to the same object
     * (<code>x == y</code> has the value <code>true</code>).
     * <p/>
     * Note that it is generally necessary to override the <tt>hashCode</tt>
     * method whenever this method is overridden, so as to maintain the
     * general contract for the <tt>hashCode</tt> method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     * @see #hashCode()
     * @see java.util.Hashtable
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof IComponent))
            return super.equals(obj);
        IComponent iComponent = (IComponent) obj;
        return iComponent.rIComponent == rIComponent;
    }

    /**
     * Checks if the interface is valid.
     * @return
     */
    public boolean isValid(){
        return parrentInterface!= null && parrentInterface.isValid();
    }

    /**
     * Checks if the interface is visible.
     * NOTE: This method is not working perfectly at the moment. It broke in #569 and returns true if the interface has been viewed once.
     * @return
     */
    public boolean isVisible(){
        /* int index = rIComponent.getVisibleArrayIndex();
      if(index != -1){
          return true;//getClient().getVisibleIComponents()[index]; // Somethign weird happened to this field after the 569 update
      }
      else if(parrent != null){
          return parrent.isValid();
      }  */
        return botEnv.client.getLoopCycle() < rIComponent.getLoopCycleStatus()+20;
    }

    /**
     * Gets its position relative to the parrents position
     * @return
     */
    public int getRelativeX() {
        return rIComponent.getX();
    }

    /**
     * Gets its position relative to the parrents position
     * @return
     */
    public int getRelativeY() {
        return rIComponent.getY();
    }

    /**
     * Gets the model ID
     * @return
     */
    public int getModelID(){
        return rIComponent.getModelID();
    }

    /**
     * Gets the model type
     * @return
     */
    public int getModelType(){
        return rIComponent.getModelType();
    }

    /**
     * Gets the model zoom
     * @return
     */
    public int getModelZoom(){
        return rIComponent.getModelZoom();
    }

    /**
     * Gets the center of the screen area of this object
     * @return
     */
    public Point getCenter() {
        Rectangle bounds = getBounds();
        return new Point((int)bounds.getCenterX(), (int)bounds.getCenterY());
    }

    /**
     * Internal method used for debugging. This should not be used by scripters
     * @return
     */
    public int getInternalVisibleArrayIndex() {
        return rIComponent.getVisibleArrayIndex();
    }


    /**
     * Get target
     *
     * @return
     */
    public MouseTarget getTarget() {
        return new MouseTarget() {
            Rectangle bounds = getClickableArea();
            Point p = new Point(random(bounds.x+3, bounds.x+bounds.width-6), random(bounds.y+3, bounds.y+bounds.height-6));
            public Point get() {
                return p;
            }

            public boolean isOver(int posX, int posY) {
                return bounds.contains(posX, posY);
            }
        };
    }

    /**
     * Moves the mouse to the object and clicks at the given action
     * NOTE: Do not use this method while you have a mouse job active!
     * @param actionContains A string that the action contains. Case ignored
     * @return Boolean, true if succeeded, false if not.
     */
    public boolean doAction(final String actionContains) {
        if (getBounds().contains(botEnv.mouse.getMousePos())) {
            return botEnv.menu.atMenu(actionContains);
        }
        final boolean[] ret = new boolean[]{false};
        MouseHoverJob mouseHoverJob = botEnv.mouse.createMouseHoverJob(new MouseMoveListener() {
            private int count = 0;

            public void onMouseOverTarget(MouseJob mouseJob) {
                MouseHoverJob mouseHoverJob = (MouseHoverJob) mouseJob;
                count++;
                if (count > random(5, 100)) {
                    mouseHoverJob.stop();
                    ret[0] = botEnv.menu.atMenu(actionContains);
                }
            }

            public void onFinished(MouseJob mouseJob) {
            }
        }, this, new KTimer(5000));
        mouseHoverJob.start();
        mouseHoverJob.join();
        return ret[0];
    }

    public boolean doClick() {
        if (getBounds().contains(botEnv.mouse.getMousePos())) {
            botEnv.mouse.clickMouse(true);
            return true;
        }
        final boolean[] ret = new boolean[]{false};
        MouseHoverJob mouseHoverJob = botEnv.mouse.createMouseHoverJob(new MouseMoveListener() {
            private int count = 0;

            public void onMouseOverTarget(MouseJob mouseJob) {
                MouseHoverJob mouseHoverJob = (MouseHoverJob) mouseJob;
                count++;
                if (count > random(5, 100)) {
                    mouseHoverJob.doMouseClick(true);
                    ret[0] = true;
                    mouseHoverJob.stop();
                }
            }

            public void onFinished(MouseJob mouseJob) {
            }
        }, this, new KTimer(5000));
        mouseHoverJob.start();
        mouseHoverJob.join();
        return ret[0];
    }

    /**
     * Gets the x axis screen pos
     * @return
     */
    public int getAbsoluteX() {
        return rIComponent.getX()+rIComponent.getMasterX();
    }

    /**
     * Gets the y axis screen pos
     * @return
     */
    public int getAbsoluteY() {
        return rIComponent.getY()+rIComponent.getMasterY();
    }

    public Object getClientObject() {
        return rIComponent;
    }

    /**
     * Returns a random point inside of the component
     * @return
     */
    public java.awt.Point getRandomPointInside() {
        return new Point(
                random(getAbsoluteX(), getAbsoluteX()+(int)getBounds().getX()),
                random(getAbsoluteY(), getAbsoluteY()+(int)getBounds().getY())
        );
    }

    /**
     * textContainsIgnoreCase(String)
     * @return boolean
     */
    public boolean textContainsIgnoreCase(String s) {
        String p = this.getText();
        return (p.toLowerCase().contains(s.toLowerCase()));
    }
}

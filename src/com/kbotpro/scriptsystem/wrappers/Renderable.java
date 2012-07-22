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
import com.kbotpro.hooks.Client;
import com.kbotpro.scriptsystem.interfaces.WorldLocation;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 18, 2009
 * Time: 4:22:05 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Renderable extends Wrapper implements WorldLocation {
    private com.kbotpro.hooks.Renderable rRenderable;

    protected Renderable(BotEnvironment botEnv, com.kbotpro.hooks.Renderable rRenderable) {
        super(botEnv);
        this.rRenderable = rRenderable;
    }

    /**
     * Gets the Tile location.
     *
     * @param baseX baseX must be suplied to calculate this.
     * @param baseY baseY must be suplied to calculate this.
     * @return Returns a Tile containing the worldwide location
     */
    public Tile getTile(int baseX, int baseY) {
        return new Tile((rRenderable.getPosX()>>9)+baseX, (rRenderable.getPosY()>>9)+baseY);
    }

    /**
     * Gets the regional X location
     *
     * @param baseX baseX must be suplied to calculate this.
     * @return Returns an integer containing the regional y location
     */
    public int getRegionalX(int baseX) {
        return rRenderable.getPosX();
    }

    /**
     * Gets the regional Y location
     *
     * @param baseY baseY must be suplied to calculate this.
     * @return Returns an integer containing the regional y location
     */
    public int getRegionalY(int baseY) {
        return rRenderable.getPosY();
    }

    /**
     * Gets the regional X location
     *
     * @return Returns an integer containing the regional y location
     */
    public int getRegionalX() {
        return rRenderable.getPosX();
    }

    /**
     * Gets the regional Y location
     *
     * @return Returns an integer containing the regional y location
     */
    public int getRegionalY() {
        return rRenderable.getPosY();
    }

    /**
     * Gets the current location of the object
     * @return
     */
    public Tile getLocation(){
        Client client = getClient();
        return getTile(client.getBaseX(), client.getBaseY());
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
        if(obj == null || !(obj instanceof Renderable))
            return super.equals(obj);
        Renderable Renderable = (Renderable) obj;
        return Renderable.rRenderable == rRenderable;
    }


    /**
     * Get the orientation around the Y axis. This is Runescape's unit system.
     * 0 is south.
     * 4095 is west
     * 8191 is north
     * 12287 is east
     * 16383 is max and is also south     *
     *
     * @return
     */
    public int getOrientationYAxis() {
        return 0;
    }

    public double distanceTo(WorldLocation worldLocation){
        return getLocation().distanceTo(worldLocation);
    }

    public int getYOffset(){
        return 0;
    }
}

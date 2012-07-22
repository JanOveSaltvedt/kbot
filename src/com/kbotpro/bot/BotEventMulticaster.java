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

package com.kbotpro.bot;

import com.kbotpro.scriptsystem.events.KPaintEventListener;
import com.kbotpro.scriptsystem.events.PaintEventListener;
import com.kbotpro.scriptsystem.events.RandomListener;
import com.kbotpro.scriptsystem.events.ServerMessageListener;
import com.kbotpro.scriptsystem.graphics.KGraphics;

import java.awt.*;
import java.util.EventListener;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * More or less a copy of AWTEventMulticaster with some extra event types
 */
public class BotEventMulticaster implements PaintEventListener, KPaintEventListener, ServerMessageListener, RandomListener {

    protected final EventListener a, b;


    /**
     * Creates an event multicaster instance which chains listener-a
     * with listener-b. Input parameters <code>a</code> and <code>b</code>
     * should not be <code>null</code>, though implementations may vary in
     * choosing whether or not to throw <code>NullPointerException</code>
     * in that case.
     * @param a listener-a
     * @param b listener-b
     */
    protected BotEventMulticaster(EventListener a, EventListener b) {
	this.a = a; this.b = b;
    }

    /**
     * Removes a listener from this multicaster.
     * <p>
     * The returned multicaster contains all the listeners in this
     * multicaster with the exception of all occurrences of {@code oldl}.
     * If the resulting multicaster contains only one regular listener
     * the regular listener may be returned.  If the resulting multicaster
     * is empty, then {@code null} may be returned instead.
     * <p>
     * No exception is thrown if {@code oldl} is {@code null}.
     *
     * @param oldl the listener to be removed
     * @return resulting listener
     */
    protected EventListener remove(EventListener oldl) {
	if (oldl == a)  return b;
	if (oldl == b)  return a;
	EventListener a2 = removeInternal(a, oldl);
	EventListener b2 = removeInternal(b, oldl);
	if (a2 == a && b2 == b) {
	    return this;	// it's not here
	}
	return addInternal(a2, b2);
    }


    /**
     * Returns the resulting multicast listener from adding listener-a
     * and listener-b together.
     * If listener-a is null, it returns listener-b;
     * If listener-b is null, it returns listener-a
     * If neither are null, then it creates and returns
     * a new BotEventMulticaster instance which chains a with b.
     * @param a event listener-a
     * @param b event listener-b
     */
    protected static EventListener addInternal(EventListener a, EventListener b) {
	if (a == null)  return b;
	if (b == null)  return a;
	return new BotEventMulticaster(a, b);
    }

    /**
     * Returns the resulting multicast listener after removing the
     * old listener from listener-l.
     * If listener-l equals the old listener OR listener-l is null,
     * returns null.
     * Else if listener-l is an instance of BotEventMulticaster,
     * then it removes the old listener from it.
     * Else, returns listener l.
     * @param l the listener being removed from
     * @param oldl the listener being removed
     */
    protected static EventListener removeInternal(EventListener l, EventListener oldl) {
	if (l == oldl || l == null) {
	    return null;
	} else if (l instanceof BotEventMulticaster) {
	    return ((BotEventMulticaster)l).remove(oldl);
	} else {
	    return l;		// it's not here
	}
    }


    /* Serialization support.
     */

    protected void saveInternal(ObjectOutputStream s, String k) throws IOException {
        if (a instanceof BotEventMulticaster) {
	    ((BotEventMulticaster)a).saveInternal(s, k);
        }
        else if (a instanceof Serializable) {
            s.writeObject(k);
            s.writeObject(a);
        }

        if (b instanceof BotEventMulticaster) {
	    ((BotEventMulticaster)b).saveInternal(s, k);
        }
        else if (b instanceof Serializable) {
            s.writeObject(k);
            s.writeObject(b);
        }
    }

    protected static void save(ObjectOutputStream s, String k, EventListener l) throws IOException {
      if (l == null) {
          return;
      }
      else if (l instanceof BotEventMulticaster) {
          ((BotEventMulticaster)l).saveInternal(s, k);
      }
      else if (l instanceof Serializable) {
           s.writeObject(k);
           s.writeObject(l);
      }
    }

    /*
     * Recursive method which returns a count of the number of listeners in
     * EventListener, handling the (common) case of l actually being an
     * BotEventMulticaster.  Additionally, only listeners of type listenerType
     * are counted.  Method modified to fix bug 4513402.  -bchristi
     */
    private static int getListenerCount(EventListener l, Class listenerType) {
        if (l instanceof BotEventMulticaster) {
            BotEventMulticaster mc = (BotEventMulticaster)l;
            return getListenerCount(mc.a, listenerType) +
             getListenerCount(mc.b, listenerType);
        }
        else {
            // Only count listeners of correct type
            return listenerType.isInstance(l) ? 1 : 0;
        }
    }

    /*
     * Recusive method which populates EventListener array a with EventListeners
     * from l.  l is usually an BotEventMulticaster.  Bug 4513402 revealed that
     * if l differed in type from the element type of a, an ArrayStoreException
     * would occur.  Now l is only inserted into a if it's of the appropriate
     * type.  -bchristi
     */
    private static int populateListenerArray(EventListener[] a, EventListener l, int index) {
        if (l instanceof BotEventMulticaster) {
            BotEventMulticaster mc = (BotEventMulticaster)l;
            int lhs = populateListenerArray(a, mc.a, index);
            return populateListenerArray(a, mc.b, lhs);
        }
        else if (a.getClass().getComponentType().isInstance(l)) {
            a[index] = l;
            return index + 1;
        }
        // Skip nulls, instances of wrong class
        else {
            return index;
        }
    }

    /**
     * Returns an array of all the objects chained as
     * <code><em>Foo</em>Listener</code>s by the specified
     * <code>java.util.EventListener</code>.
     * <code><em>Foo</em>Listener</code>s are chained by the
     * <code>BotEventMulticaster</code> using the
     * <code>add<em>Foo</em>Listener</code> method.
     * If a <code>null</code> listener is specified, this method returns an
     * empty array. If the specified listener is not an instance of
     * <code>BotEventMulticaster</code>, this method returns an array which
     * contains only the specified listener. If no such listeners are chanined,
     * this method returns an empty array.
     *
     * @param l the specified <code>java.util.EventListener</code>
     * @param listenerType the type of listeners requested; this parameter
     *          should specify an interface that descends from
     *          <code>java.util.EventListener</code>
     * @return an array of all objects chained as
     *          <code><em>Foo</em>Listener</code>s by the specified multicast
     *          listener, or an empty array if no such listeners have been
     *          chained by the specified multicast listener
     * @exception NullPointerException if the specified
     *             {@code listenertype} parameter is {@code null}
     * @exception ClassCastException if <code>listenerType</code>
     *          doesn't specify a class or interface that implements
     *          <code>java.util.EventListener</code>
     *
     * @since 1.4
     */
    public static <T extends EventListener> T[]
        getListeners(EventListener l, Class<T> listenerType)
    {
        if (listenerType == null) {
            throw new NullPointerException ("Listener type should not be null");
        }

        int n = getListenerCount(l, listenerType);
        T[] result = (T[]) Array.newInstance(listenerType, n);
        populateListenerArray(result, l, 0);
        return result;
    }

    public void onRepaint(Graphics g) {
        ((PaintEventListener)a).onRepaint(g);
        ((PaintEventListener)b).onRepaint(g);
    }


    public static PaintEventListener add(PaintEventListener a, PaintEventListener b) {
        return (PaintEventListener)addInternal(a, b);
    }

    public static PaintEventListener remove(PaintEventListener l, PaintEventListener oldl) {
	    return (PaintEventListener) removeInternal(l, oldl);
    }

	public static KPaintEventListener add(KPaintEventListener a, KPaintEventListener b) {
        return (KPaintEventListener)addInternal(a, b);
    }

    public static KPaintEventListener remove(KPaintEventListener l, KPaintEventListener oldl) {
	    return (KPaintEventListener) removeInternal(l, oldl);
    }

    public static ServerMessageListener add(ServerMessageListener a, ServerMessageListener b) {
        return (ServerMessageListener)addInternal(a, b);
    }

    public static ServerMessageListener remove(ServerMessageListener l, ServerMessageListener oldl) {
	    return (ServerMessageListener) removeInternal(l, oldl);
    }

	public void onRepaint(KGraphics g) {
		((KPaintEventListener)a).onRepaint(g);
        ((KPaintEventListener)b).onRepaint(g);
	}

    /**
     * Is called when the client recieves a server message.
     *
     * @param message
     */
    public void onServerMessage(String message) {
        ((ServerMessageListener)a).onServerMessage(message);
        ((ServerMessageListener)b).onServerMessage(message);
    }

    public static RandomListener remove(RandomListener l, RandomListener oldl) {
        return (RandomListener) removeInternal(l, oldl);
    }

    public static RandomListener add(RandomListener a, RandomListener b) {
        return (RandomListener)addInternal(a, b);
    }

    /**
     * Is called when a random event activates.
     *
     * @param name The name of the random
     * @return boolean, true if you want to allow this random to run, false if you want to make it not run this time.
     */
    public boolean randomActivated(String name) {
        return ((RandomListener) a).randomActivated(name) && ((RandomListener) b).randomActivated(name);
    }
}

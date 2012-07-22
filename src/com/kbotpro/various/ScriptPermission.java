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

import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.net.SocketPermission;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Apr 7, 2010
 * Time: 5:21:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptPermission extends Permission {
    List<SocketPermission> allowedSockets = new ArrayList<SocketPermission>();
    FilePermission filePermission = new FilePermission("-", "read,write,delete");
    public ScriptPermission() {
        super("<all permissions>");
        allowedSockets.add(new SocketPermission("services.runescape.com:80", "connect,resolve"));
        allowedSockets.add(new SocketPermission("pastie.org:80", "connect,resolve"));
    }

    /**
     * Checks if the specified permission's actions are "implied by"
     * this object's actions.
     * <p/>
     * This must be implemented by subclasses of Permission, as they are the
     * only ones that can impose semantics on a Permission object.
     * <p/>
     * <p>The <code>implies</code> method is used by the AccessController to determine
     * whether or not a requested permission is implied by another permission that
     * is known to be valid in the current execution context.
     *
     * @param permission the permission to check against.
     * @return true if the specified permission is implied by this object,
     *         false if not.
     */
    @Override
    public boolean implies(Permission permission) {
        if(permission instanceof SocketPermission){
            for(SocketPermission socketPermission: allowedSockets){
                if(socketPermission.implies(permission)){
                    return true;
                }
            }
            return false;
        }
        if(permission instanceof FilePermission){
            if(permission.getActions().contains("execute")){
                return false;
            }
            if(!filePermission.implies(permission)){
                return false;
            }

        }
        if(permission instanceof RuntimePermission){
            if(permission.getName().equals("createClassLoader")){
                return false;
            }
            if(permission.getName().equals("setContextClassLoader")){
                return false;
            }
            if(permission.getName().equals("setSecurityManager")){
                return false;
            }
            if(permission.getName().equals("exitVM")){
                return false;
            }
            if(permission.getName().equals("shutdownHooks")){
                return false;
            }
            if(permission.getName().startsWith("loadLibrary")){
                return false;
            }
            if(permission.getName().equals("queuePrintJob")){
                return true;
            }
        }
        if(permission instanceof ReflectPermission){
            return false;
        }
        return true;
    }

    /**
     * Checks two Permission objects for equality.
     * <p/>
     * Do not use the <code>equals</code> method for making access control
     * decisions; use the <code>implies</code> method.
     *
     * @param obj the object we are testing for equality with this object.
     * @return true if both Permission objects are equivalent.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ScriptPermission;
    }

    /**
     * Returns the hash code value for this Permission object.
     * <p/>
     * The required <code>hashCode</code> behavior for Permission Objects is
     * the following: <p>
     * <ul>
     * <li>Whenever it is invoked on the same Permission object more than
     * once during an execution of a Java application, the
     * <code>hashCode</code> method
     * must consistently return the same integer. This integer need not
     * remain consistent from one execution of an application to another
     * execution of the same application. <p>
     * <li>If two Permission objects are equal according to the
     * <code>equals</code>
     * method, then calling the <code>hashCode</code> method on each of the
     * two Permission objects must produce the same integer result.
     * </ul>
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return 1;
    }

    /**
     * Returns the actions as a String. This is abstract
     * so subclasses can defer creating a String representation until
     * one is needed. Subclasses should always return actions in what they
     * consider to be their
     * canonical form. For example, two FilePermission objects created via
     * the following:
     * <p/>
     * <pre>
     *   perm1 = new FilePermission(p1,"read,write");
     *   perm2 = new FilePermission(p2,"write,read");
     * </pre>
     * <p/>
     * both return
     * "read,write" when the <code>getActions</code> method is invoked.
     *
     * @return the actions of this Permission.
     */
    @Override
    public String getActions() {
        return "<all actions>";
    }

    /**
     * Implements the guard interface for a permission. The
     * <code>SecurityManager.checkPermission</code> method is called,
     * passing this permission object as the permission to check.
     * Returns silently if access is granted. Otherwise, throws
     * a SecurityException.
     *
     * @param object the object being guarded (currently ignored).
     * @throws SecurityException if a security manager exists and its
     *                           <code>checkPermission</code> method doesn't allow access.
     * @see java.security.Guard
     * @see java.security.GuardedObject
     * @see SecurityManager#checkPermission
     */
    @Override
    public void checkGuard(Object object) throws SecurityException {
        super.checkGuard(object);    //To change body of overridden methods use File | Settings | File Templates.
    }
}

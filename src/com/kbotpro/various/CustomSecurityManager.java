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


import com.kbotpro.handlers.ScriptManager;

import java.lang.reflect.ReflectPermission;
import java.security.AccessControlContext;
import java.security.Permission;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 10.aug.2009
 * Time: 14:47:30
 */
public class CustomSecurityManager extends SecurityManager {

    /**
     * Throws a <code>SecurityException</code> if the
     * specified security context is denied access to the resource
     * specified by the given permission.
     * The context must be a security
     * context returned by a previous call to
     * <code>getSecurityContext</code> and the access control
     * decision is based upon the configured security policy for
     * that security context.
     * <p/>
     * If <code>context</code> is an instance of
     * <code>AccessControlContext</code> then the
     * <code>AccessControlContext.checkPermission</code> method is
     * invoked with the specified permission.
     * <p/>
     * If <code>context</code> is not an instance of
     * <code>AccessControlContext</code> then a
     * <code>SecurityException</code> is thrown.
     *
     * @param perm    the specified permission
     * @param context a system-dependent security context.
     * @throws SecurityException    if the specified security context
     *                              is not an instance of <code>AccessControlContext</code>
     *                              (e.g., is <code>null</code>), or is denied access to the
     *                              resource specified by the given permission.
     * @throws NullPointerException if the permission argument is
     *                              <code>null</code>.
     * @see SecurityManager#getSecurityContext()
     * @see java.security.AccessControlContext#checkPermission(java.security.Permission)
     * @since 1.2
     */
    @Override
    public void checkPermission(Permission perm, Object context) {
        if(perm instanceof ReflectPermission){
            final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            for(StackTraceElement element: stackTraceElements){
                if(element.getClassName().equals(ScriptManager.class.getName())){
                    return;
                }
            }
        }
        if (context instanceof AccessControlContext) {
            super.checkPermission(perm, context);
        } else {
            checkPermission(perm);
        }
    }

    /**
     * Throws a <code>SecurityException</code> if the requested
     * access, specified by the given permission, is not permitted based
     * on the security policy currently in effect.
     * <p/>
     * This method calls <code>AccessController.checkPermission</code>
     * with the given permission.
     *
     * @param perm the requested permission.
     * @throws SecurityException    if access is not permitted based on
     *                              the current security policy.
     * @throws NullPointerException if the permission argument is
     *                              <code>null</code>.
     * @since 1.2
     */
    @Override
    public void checkPermission(Permission perm) {
        if(perm instanceof ReflectPermission){
            final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            for(StackTraceElement element: stackTraceElements){
                if(element.getClassName().equals(ScriptManager.class.getName())){
                    return;
                }
            }
        }
        if(perm instanceof ScriptPermission){
            throw new SecurityException("Script tried to do something thats not allowed by KBot. Please report to staff so we can resolve this.");
        }
        super.checkPermission(perm);
    }
}

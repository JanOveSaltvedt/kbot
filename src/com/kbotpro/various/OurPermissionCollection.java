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

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Apr 7, 2010
 * Time: 7:50:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OurPermissionCollection extends PermissionCollection {
    final ScriptPermission scriptPermission;

    public OurPermissionCollection() {
        scriptPermission = new ScriptPermission();
    }

    /**
     * Adds a permission object to the current collection of permission objects.
     *
     * @param permission the Permission object to add.
     * @throws SecurityException -  if this PermissionCollection object
     *                           has been marked readonly
     */
    @Override
    public void add(Permission permission) {

    }

    /**
     * Checks to see if the specified permission is implied by
     * the collection of Permission objects held in this PermissionCollection.
     *
     * @param permission the Permission object to compare.
     * @return true if "permission" is implied by the  permissions in
     *         the collection, false if not.
     */
    @Override
    public boolean implies(Permission permission) {
        return scriptPermission.implies(permission);
    }

    /**
     * Returns an enumeration of all the Permission objects in the collection.
     *
     * @return an enumeration of all the Permissions.
     */
    @Override
    public Enumeration<Permission> elements() {
        return new Enumeration<Permission>() {
            public boolean hasMore = true;
            public boolean hasMoreElements() {
                return hasMore;
            }

            public Permission nextElement() {
                hasMore = false;
                return scriptPermission;
            }
        };
    }
}

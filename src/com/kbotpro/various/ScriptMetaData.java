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

import java.security.AllPermission;
import java.security.Permission;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Nov 2, 2009
 * Time: 8:26:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptMetaData {
    public ScriptMetaData(int ID, String name, String author, int downloads, String description, String category, String type, String version, int revision, int modifier, List<Permission> permissionExceptions) {
        this.name = name;
        this.author = author;
        this.downloads = downloads;
        this.description = description;
        this.category = category;
        this.type = type;
        this.version = version;
        this.revision = revision;
        this.ID = ID;
        this.modifier = modifier;
        this.permissionExceptions = permissionExceptions;
        if(isTrusted()){
            permissionExceptions.add(new AllPermission());
        }
    }

    public List<Permission> permissionExceptions;
    public int ID;
    public String name;
    public String author;
    public int downloads;
    public String description;
    public String category;
    public String type;
    public int revision;
    public String version;
    public int modifier;

    public static final int MODIFIER_TRUSTED = 1;

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p/>
     * The <code>toString</code> method for class <code>Object</code>
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return name;
    }

    public boolean isTrusted(){
        return (modifier & MODIFIER_TRUSTED) != 0;
    }

    public boolean isProScript() {
        return type.equalsIgnoreCase("pro");
    }
}

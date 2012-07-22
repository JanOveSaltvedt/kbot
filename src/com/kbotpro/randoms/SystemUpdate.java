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

package com.kbotpro.randoms;

import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.wrappers.IComponent;

/**
 * Created by IntelliJ IDEA.
 * User: Scott
 * Date: Jan 18, 2010
 * Time: 10:54:18 AM
 */
public class SystemUpdate extends Random {
	@Override
	public String getName() {
		return "System Update";
	}

	@Override
	public boolean activate() {
		IComponent t = interfaces.getComponent(754, 5);
        return (t != null && t.getText() != null && t.getText().contains("System update in:") && t.isVisible());
	}

	@Override
	public void onStart() {
        stopAllScripts();
	}
}

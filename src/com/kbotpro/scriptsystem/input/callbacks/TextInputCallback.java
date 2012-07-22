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



package com.kbotpro.scriptsystem.input.callbacks;

import com.kbotpro.scriptsystem.input.jobs.TextInputJob;

/**
 * Basic interface for a TextInputJob callback.
 * Contains methods that is after a key is typed.
 */
public interface TextInputCallback {
    /**
     * Is called when a new character is typed.
     * @param c The character typed
     * @param job The job this callback belongs to. For easier access without using final variables
     */
    public void keyTyped(char c, TextInputJob job);

    /**
     * Is called when the keyboard job is done executing.
     * @param job The job this callback belongs to. For easier access without using final variables
     */
    public void onFinished(TextInputJob job);
}

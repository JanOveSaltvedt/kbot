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



package com.kbotpro.scriptsystem.interfaces;

/**
 * Simple interface to be used for creating a Worker.
 */
public interface Looped {
    /**
     * This is the method you will put the main part of your workerContainer in.
     * This is basically a looping method.
     * It will be called over and over again with a pause defined by its return value.
     * Returning -1 will stop the worker completly, return 0 will call the method again at once.
     * Any number over 0 will make it wait x milliseconds (given by the return value) and recall the method
     * @return -1 to stop, 0 to call again imidiatly, > 0 to make it wait x milliseconds before calling again.
     */
    public abstract int loop();
}

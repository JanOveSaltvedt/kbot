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



package com.kbotpro.utils;

import org.apache.log4j.Logger;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 31.jul.2009
 * Time: 18:00:43
 */
public class UsefulMethods {
    public static int random(int max){
        return new Random().nextInt(max);
    }

    public static int random(int min, int max){
        return random(max - min) + min;
    }

    public static void sleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void sleep(int min, int max){
        sleep(random(min, max));
    }
}

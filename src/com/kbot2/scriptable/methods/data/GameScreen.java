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

package com.kbot2.scriptable.methods.data;

import com.kbot2.bot.BotEnvironment;
import com.kbot2.scriptable.methods.wrappers.Interface;
import com.kbot2.scriptable.methods.wrappers.InterfaceGroup;
import com.kbotpro.scriptsystem.wrappers.IComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove / Kosaki
 * Date: 01.mai.2009
 * Time: 18:31:00
 */
public class GameScreen extends Data{
	public GameScreen(BotEnvironment botEnv) {
		super(botEnv);
	}

	public static final int TAB_ATTACK = 0;
	public static final int TAB_STATS = 1;
	public static final int TAB_QUESTS = 2;
	public static final int TAB_DIARIES = 3;
	public static final int TAB_INVENTORY = 4;
	public static final int TAB_EQUIPMENT = 5;
	public static final int TAB_PRAYER = 6;
	public static final int TAB_MAGIC = 7;
	public static final int TAB_SUMMONING = 8;
	public static final int TAB_FRIENDS = 9;
	public static final int TAB_IGNORE = 10;
	public static final int TAB_CLAN = 11;
	public static final int TAB_OPTIONS = 12;
	public static final int TAB_EMOTES = 13;
	public static final int TAB_MUSIC = 14;
	public static final int TAB_NOTES = 15;
	public static final int TAB_LOGOUT = 16;

	/**
	 * Opens the tab if not already opened.
	 * @param tab use constants like GameScreen.TAB_ATTACK
	 */
	public void openTab(int tab){
		if (tab == getCurrentTab())
			return;
		Interface[] tabs = getTabButtons();
		int id = tab;
		if (id >= 8 && id != TAB_LOGOUT)
			id -= 8;
		else if(id != TAB_LOGOUT)
			id += 8;
		tabs[id].doClick();
	}

	/**
	 * Gets the current tab.
	 * ETC: GameScreen.TAB_ATTACK
	 * @return
	 */
	public int getCurrentTab() {
		Interface[] tabs = getTabButtons();

		for (int i = 0; i < tabs.length; i++)
			if (tabs[i].getTextureID() != -1)
				return (i < 8) ? (i + 8) : (i - 8);

		return TAB_LOGOUT;
	}

	public Interface[] getTabButtons(){
        IComponent[] buttons = botEnv.proBotEnvironment.game.getTabButtons();
        Interface[] out = new Interface[buttons.length];
        for(int i = 0; i < out.length; i++){
            out[i] = new Interface(botEnv, buttons[i], null, null);
        }
        return out;
    }

	public void logout(){
		botEnv.proBotEnvironment.game.logout();
	}

	/**
	 * Proxy method for setRunning as it is on gamescreen.
	 * @param run
	 * @return
	 */
	public boolean setRunning(boolean run){
		return botEnv.proBotEnvironment.walking.setRunning(true);
	}

	public static final int BUTTON_PUBLIC = 0;
	public static final int BUTTON_PRIVATE = 1;
	public static final int BUTTON_CLAN = 2;
	public static final int BUTTON_TRADE = 3;
	public static final int BUTTON_ASSIST = 4;

	//public static final int MODE_VIEW = 0;
	public static final int MODE_FRIENDS = 1;
	public static final int MODE_OFF = 2;
	public static final int MODE_ON = 3;
	public static final int MODE_HIDE = 4;

	/**
	 * Sets a mode to a gamescreen button
	 * @param button button to click on GameScreen.BUTTON_XXXXXX
	 * @param mode mode to select GameScreen.MODE_XXXXXX
	 * @return
	 */
	public boolean setMode(int button, int mode){
		int textID = -1;
		int clickableID = -1;
		String text = null;

		switch(button) {
			case BUTTON_PUBLIC:
				clickableID = 7;
				textID = 10;
				break;
			case BUTTON_PRIVATE:
				clickableID = 11;
				textID = 14;
				break;
			case  BUTTON_CLAN:
				clickableID = 15;
				textID = 18;
				break;
			case BUTTON_TRADE:
				clickableID = 19;
				textID = 22;
				break;
			case BUTTON_ASSIST:
				clickableID = 23;
				clickableID = 26;
				break;
			default:
				return false;
		}

		switch(mode) {
			case MODE_ON:
				text = "On";
				break;
			case MODE_OFF:
				text = "Off";
				break;
			case MODE_FRIENDS:
				text = "Friends";
				break;
			case MODE_HIDE:
				text = "Hide";
				break;
			default:
				return false;
		}

		return botEnv.interfaces.getInterface(751, textID).textContainsIgnoreCase(text) || botEnv.interfaces.getInterface(751, clickableID).doAction(text);
	}

	public int getPrayerPoints(){
		return botEnv.proBotEnvironment.game.getPrayerPoints();
	}

	public int getRunningEnergy(){
		return botEnv.proBotEnvironment.walking.getEnergy();
	}

	public int getHealth(){
		return botEnv.proBotEnvironment.game.getHealth();
	}

	public void takeScreenshot(String fileName, boolean includePaint){
		botEnv.proBotEnvironment.screenshot.takeScreenshot(fileName);
	}

	public void takeWScreenshot(String fileName, boolean includePaint){
		botEnv.proBotEnvironment.screenshot.takeScreenshot(fileName);
	}

	/**
	 * Checks if an item is selected.
	 * @return
	 */
	public boolean hasSelectedItem(){
		return botEnv.proBotEnvironment.game.hasSelectedItem();
	}
}

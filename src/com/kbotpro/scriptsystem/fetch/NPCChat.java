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

package com.kbotpro.scriptsystem.fetch;


import java.util.Vector;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Interface;

/**
 * Used to interact with multi-option NPC chat interfaces.
 * 
 * @author _933pm
 * 
 */
public class NPCChat extends ModuleConnector {

	public NPCChat(BotEnvironment botEnv) {
		super(botEnv);
	}

	/**
	 * Interface ID for NPC Chat with 2 options.
	 */
	public static int TWO_OPTION_INTERFACE = 228;
	/**
	 * Interface ID for NPC Chat with 3 options.
	 */
	public static int THREE_OPTION_INTERFACE = 230;
	/**
	 * Interface ID for NPC Chat with 4 options.
	 */
	public static int FOUR_OPTION_INTERFACE = 232;
	/**
	 * Array of all three NPC Chat interface IDs.
	 */
	private static int[] ALL_CHAT_INTERFACES = { TWO_OPTION_INTERFACE,
			THREE_OPTION_INTERFACE, FOUR_OPTION_INTERFACE };

	/**
	 * Returns whether or not any of the NPC Chat interfaces are up.
	 * 
	 * @return Whether or not any of the NPC Chat interfaces are up.
	 */
	public boolean isChatUp() {
		for (int id : ALL_CHAT_INTERFACES) {
			if (botEnv.interfaces.getInterface(id) != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the number of options in the current NPC Chat.
	 * 
	 * @return The number of options in the current NPC Chat.
	 */
	public int getOptionCount() {
		if (isChatUp()) {
			if (botEnv.interfaces.getInterface(TWO_OPTION_INTERFACE) != null) {
				return 2;
			} else if (botEnv.interfaces.getInterface(THREE_OPTION_INTERFACE) != null) {
				return 3;
			} else if (botEnv.interfaces.getInterface(FOUR_OPTION_INTERFACE) != null) {
				return 4;
			}
		}
		return -1;
	}

	/**
	 * Returns Whether or not any of the current NPC Chat options contain the
	 * specified text.
	 * 
	 * @param option
	 *            The text of the option to search for.
	 * @return Whether or not any of the current NPC Chat options contain the
	 *         specified text.
	 */
	public boolean containsOption(String option) {
		if (getOptions() != null) {
			for (String s : getOptions()) {
				if (s.contains(option)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns whether or not any of the current NPC Chat options equal the
	 * specified text.
	 * 
	 * @param option
	 *            The text of the option to search for.
	 * @return Whether or not any of the current NPC Chat options equal the
	 *         specified text.
	 */
	public boolean containsOptionExact(String option) {
		if (getOptions() != null) {
			for (String s : getOptions()) {
				if (s.equals(option)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Clicks an option that contains the specified text.
	 * 
	 * @param option
	 *            The text of the option to click.
	 * @return Whether or not the specified option was clicked.
	 */
	public boolean selectOption(String option) {
		if (getNPCChatInterface() != null) {
			for (IComponent inter : getNPCChatInterface().getComponents()) {
				if (inter.getText().contains(option)) {
					inter.doClick();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Clicks an option that equals the specified text.
	 * 
	 * @param option
	 *            The text of the option to click.
	 * @return Whether or not the specified option was clicked.
	 */
	public boolean selectOptionExact(String option) {
		if (getNPCChatInterface() != null) {
			for (IComponent inter : getNPCChatInterface().getComponents()) {
				if (inter.getText().equals(option)) {
					inter.doClick();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns an array of the options' text.
	 * 
	 * @return An array of the options' text.
	 */
	public String[] getOptions() {
		if (getNPCChatInterface() != null) {
			Vector<String> tempVector = new Vector<String>();
			for (IComponent inter : getNPCChatInterface().getComponents()) {
				if (!inter.getText().equals("") && inter.getTextColor() == 0) {
					tempVector.add(inter.getText());
				}
			}
			String[] options = new String[tempVector.size()];
			for (int i = 0; i < tempVector.size(); i++) {
				options[i] = tempVector.get(i);
			}
			return options;
		}
		return null;
	}

	/**
	 * Returns the question or choice you are selecting an option for.
	 * 
	 * @return The question or choice you are selecting an option for.
	 */
	public String getQuestion() {
		if (getNPCChatInterface() != null) {
			return getNPCChatInterface().getComponent(1).getText();
		}
		return null;
	}

	/**
	 * Returns the interface of the current NPC Chat.
	 * 
	 * @return The interface of the current NPC Chat.
	 */
	public Interface getNPCChatInterface() {
		switch (getOptionCount()) {
			case -1:
				return null;
			case 2:
				return botEnv.interfaces.getInterface(TWO_OPTION_INTERFACE);
			case 3:
				return botEnv.interfaces.getInterface(THREE_OPTION_INTERFACE);
			case 4:
				return botEnv.interfaces.getInterface(FOUR_OPTION_INTERFACE);
		}
		return null;
	}

	/**
	 * Returns a string representation of the NPC Chat. Example: 2-option NPC
	 * Chat is up. Question: Where would you like to go? Option: Varrock Option:
	 * Ardougne
	 */
	public String toString() {
		if (getNPCChatInterface() != null) {
			StringBuilder b = new StringBuilder();
			b.append(getOptionCount() + "-option NPC Chat ");
			b.append("is " + ((isChatUp()) ? "up." : "not up."));
			b.append(" Question: " + getQuestion());
			for (String option : getOptions()) {
				b.append(" Option: " + option);
			}
			return b.toString();
		}
		return null;
	}

}
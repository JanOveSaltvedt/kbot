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

package com.kbotpro.scriptsystem.fetch;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Interface;
import com.kbotpro.scriptsystem.wrappers.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Apples
 * Date: Jan 2, 2010
 * Time: 11:33:02 PM
 */
public class Chat extends ModuleConnector {

	private final static int OPTIONS_INTERFACE_ID = 751;
	public final static int CHAT_OPTIONS_ALL = 2;
	public final static int CHAT_OPTIONS_GAME = 5;
	public final static int CHAT_OPTIONS_PUBLIC = 9;
	public final static int CHAT_OPTIONS_PRIVATE = 13;
	public final static int CHAT_OPTIONS_CLAN = 17;
	public final static int CHAT_OPTIONS_TRADE = 21;
	public final static int CHAT_OPTIONS_ASSIST = 25;
	private final static int CHAT_OPTIONS_ABUSE = 28;
	private final static int CHAT_INTERFACE_ID = 137;
	public final static int CHAT_OPTIONS_QUICKCHAT = 54;

	private static Message[] chatMessages = new Message[100];

	public Chat(BotEnvironment botEnv) {
		super(botEnv);
	}

	/**
	 *
	 * @param index message index from 0 to 99
	 * @return If it is cached the message else null
	 */
	public Message getMessage(int index) {
		return index > -1 && index < 100 ? chatMessages[index] : null;
	}

	/**
	 * Clears all the cached messages
	 */
	public void clearMessages() {
		for (int index = 0; index < 100; index++)
			chatMessages[index] = null;
	}

	/**
	 * Caches all the current available messages
	 */
	public void updateMessages() {
		for (int index = 0; index < 100; index++) {
			IComponent i = botEnv.interfaces.getComponent(CHAT_INTERFACE_ID, index + 59);
			chatMessages[index] = i.getText().length() > 0 ? new Message(index, i) : null;
		}
	}

	/**
	 * Gets the first message found in the string(s)
	 *
	 * @param text The string(s) you are searching for
	 * @return The IComponent of the first found message
	 */
	public Message getMessage(String... text) {
		for (int index = 0; index < 100; index++) {
			if (getMessage(index) == null)
				break;
			if (getMessage(index).contains(true, text))
				return getMessage(index);
		}
		return null;
	}

	/**
	 * Gets all the messages in the chatbox
	 *
	 * @return All the messages valid in the chatbox
	 */
	public String[] getMessages() {
		List<String> chatStrings = new ArrayList<String>();
		for (int index = 0; index < 100; index++) {
			if (getMessage(index) == null)
				break;
			chatStrings.add(getMessage(index).getText());
		}
		return chatStrings.toArray(new String[1]);
	}

	/**
	 * Gets the messages at the index(s)
	 *
	 * @param indexs The index(s) that you want to get. Valid: 0-99
	 * @return The IComponents of the parameter indexs
	 */
	public Message[] getMessages(int... indexs) {
		Message[] chatMessages = new Message[indexs.length];
		for (int index = 0; index < chatMessages.length; index++)
			chatMessages[index] = getMessage(indexs[index]);
		return chatMessages;
	}

	/**
	 * Gets all the IComponents that contains the sting(s)
	 *
	 * @param text The text you are searching for
	 * @return The IComponents of the parameter text
	 */
	public Message[] getMessages(String... text) {
		List<Message> chatMessages = new ArrayList<Message>();
		for (int index = 0; index < 100; index++) {
			if (getMessage(index) == null)
				break;
			if (getMessage(index).contains(true, text))
				chatMessages.add(getMessage(index));
		}
		return chatMessages.toArray(new Message[1]);
	}

	/**
	 * Checks if the chat message contains the text
	 *
	 * @param includeNames Include names in your search
	 * @param text         The text you are searching for
	 * @return true if it contains any of the strings
	 */
	public boolean messagesContain(boolean includeNames, String... text) {
		for (int index = 0; index < 100; index++)
			if (getMessage(index).contains(includeNames, text))
				return true;
		return false;
	}

	/**
	 * Counts how many messages are currently displayed in the chat
	 *
	 * @return Amount of messages in the chatbox
	 */
	public int getMessageCount() {
		int count;
		for (count = 0; count < 100; count++)
			if (getMessage(count) == null)
				break;
		return count;
	}

	/**
	 * Uses the quickChat by typing in the selected letters from the parameter navLetters
	 *
	 * @param navLetters The letters to navigate where your going. Can also include the text you want to search after getting to the destination.
	 * @param pressEnter Should press enter after searching. Used if your searching for something.
	 */
	public void useQuickChat(String navLetters, boolean pressEnter) {
		botEnv.interfaces.getInterface(CHAT_INTERFACE_ID).getComponent(CHAT_OPTIONS_QUICKCHAT).doClick();
		sleep(321, 413);
		for (char c : navLetters.toCharArray()) {
			botEnv.keyboard.writeText(c + "", c == navLetters.charAt(navLetters.length() - 1) && pressEnter);
			sleep(522, 889);
		}
	}

	/**
	 * Clicks the All options button
	 */
	public void setChatAll() {
		botEnv.interfaces.getInterface(OPTIONS_INTERFACE_ID).getComponent(CHAT_OPTIONS_ALL).doClick();
	}

	/**
	 * Does the action to the specific options button
	 *
	 * @param optionsButton Which options button you want to check. Use Chat.CHAT_OPTIONS_**** for the correct options button
	 * @param setting       Changes the setting on the options button. If setting equals "view" it will change to the selected options button
	 */
	public void setChatTab(int optionsButton, String setting) {
		if (!getState(optionsButton).toLowerCase().equals(setting))
			botEnv.interfaces.getInterface(OPTIONS_INTERFACE_ID).getComponent(optionsButton).doAction(setting);
	}

    /**
     * Checks to see if chat window is open
     * @return true/false   Based whether the chat window is open (Resizable mode only)
     */
    public boolean isChatOpen() {
        return botEnv.interfaces.getInterface(CHAT_INTERFACE_ID).getComponent(CHAT_OPTIONS_QUICKCHAT).isVisible();
    }

	/**
	 * Opens the report abuse screen
	 */
	public void openReportAbuse() {
		botEnv.interfaces.getInterface(OPTIONS_INTERFACE_ID).getComponent(CHAT_OPTIONS_ABUSE).doClick();
	}

	/**
	 * Gets the state the option button is in in string form.
	 *
	 * @param optionsButton Which options button you want to check. Use Chat.CHAT_OPTIONS_**** for the correct options button
	 * @return String form of the current state
	 */
	public String getState(int optionsButton) {
		String s = botEnv.interfaces.getInterface(OPTIONS_INTERFACE_ID).getComponent(optionsButton + 2).getText();
		return optionsButton != CHAT_OPTIONS_ALL ? s.substring(s.lastIndexOf(">") + 1) : "View";
	}

	/**
	 * Contains the message information
	 */
	public class Message {

		private int index;
		private IComponent component;
		private String text;
		private String string;
		private String playerName;
		private String textColor;
		private int rights;

		public Message(int index, IComponent component) {
			this.index = index;
			this.component = component;
			this.string = component.getText();
			if (string.contains("<") && string.contains(":")) {
				String[] messageSplit = string.split(":");
				{
					int offset = messageSplit[0].lastIndexOf("<");
					if (offset > 0) {
						int rights = Integer.parseInt(messageSplit[0].substring(offset + 5, offset + 5));
						messageSplit[0].replace("<img=" + rights + '>', "");
						this.rights = rights > 3 ? 0 : rights;
					} else
						this.rights = 0;
					this.playerName = messageSplit[0];
				}
				{
					int l = messageSplit[1].lastIndexOf(">");
					int i = messageSplit[1].lastIndexOf("<");
					this.textColor = "0x" + messageSplit[1].substring(i + 5, l);
					this.text = messageSplit[1].substring(l + 1);
				}
			} else
				this.text = string;
		}

		/**
		 *
		 * @return The messages index from 0-99
		 */
		public int getIndex() {
			return index;
		}

		/**
		 *
		 * @return The messages main component
		 */
		public IComponent getComponent() {
			return component;
		}

		/**
		 *
		 * @return The message
		 */
		public String getText() {
			return text;
		}

		/**
		 *
		 * @return The player that said the message
		 */
		public String getPlayerName() {
			return playerName;
		}

		/**
		 *
		 * @return The rights of the player that said the message. 0 = Reg, 1 = Mod, 2 = Admin, 3 = QuickChat
		 */
		public int getPlayerRights() {
			return rights;
		}


		/**
		 *
		 * @return The textColor of the message
		 */
		public String getTextColor() {
			return textColor;
		}

		/**
		 * Checks if the chat message contains the text
		 *
		 * @param includeNames Include names in your search
		 * @param text         The text you are searching for
		 * @return true if it contains any of the strings
		 */
		public boolean contains(boolean includeNames, String... text) {
			for (String s : text)
				if (toString().toLowerCase().contains(includeNames ? s : getText()))
					return true;
			return false;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Message) {
				Message other = ((Message) o);
				return other.toString().equals(toString()) || other.getText().equals(getText());
			}
			if (o instanceof String) {
				String other = ((String) o);
				return other.equals(toString()) || other.equals(getText());
			}
			return false;
		}

		/**
		 * Gets the original message of the IComponent
		 */
		@Override
		public String toString() {
			return string;
		}
	}
}

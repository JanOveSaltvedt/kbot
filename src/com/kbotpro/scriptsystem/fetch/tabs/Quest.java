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

package com.kbotpro.scriptsystem.fetch.tabs;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.fetch.Game;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.IComponent;

/**
 * Used to interact with the Quest Tab interface.
 * 
 * @author _933pm
 * 
 */
public class Quest extends ModuleConnector {

	public Quest(BotEnvironment botEnv) {
		super(botEnv);
	}

	public enum Progress {
		NOT_STARTED, IN_PROGRESS, FINISHED
	};

	/**
	 * Main interface ID of Quest Tab.
	 */
	public static final int INTERFACE_ID = 190;
	/**
	 * Component ID of the Filter button.
	 */
	public static final int BUTTON_FILTER = 10;
	/**
	 * Component ID of the Reverse button.
	 */
	public static final int BUTTON_REVERSE = 12;
	/**
	 * ID of the Quest Points Component.
	 */
	public static final int QUEST_POINTS_ID = 2;
	/**
	 * ID of the Quests Component.
	 */
	public static final int QUESTS_ID = 18;

	/**
	 * Check if the specified button is selected.
	 * 
	 * @param buttonID
	 *            Component ID of the button to check selection for.
	 * @return Whether or not the specified button is selected.
	 */
	public boolean isButtonSelected(int buttonID) {
		setTab();
		IComponent button = botEnv.interfaces.getComponent(INTERFACE_ID,
				buttonID);
		if (button.getTextureID() == 699) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the selection state of the specified button.
	 * 
	 * @param buttonID
	 *            Component ID of the button to select/deselect.
	 * @param select
	 *            Whether the button should be selected or deselected.
	 */
	public void setButtonSelected(int buttonID, boolean select) {
		setTab();
		if (select != isButtonSelected(buttonID)) {
			IComponent button = botEnv.interfaces.getComponent(INTERFACE_ID,
					buttonID);
			button.doClick();
		}
	}

	/**
	 * Gets the player's current number of quest points.
	 * 
	 * @return Player's current number of quest points.
	 */
	public int getQuestPoints() {
		setTab();
		String s = botEnv.interfaces
				.getComponent(INTERFACE_ID, QUEST_POINTS_ID).getText().replace(
						"Quest Points: ", "");
		return Integer.parseInt(s.split(" / ")[0]);
	}

	/**
	 * Gets the maximum number of quest points possible.
	 * 
	 * @return Maximum number of quest points possible.
	 */
	public int getMaxQuestPoints() {
		setTab();
		String s = botEnv.interfaces
				.getComponent(INTERFACE_ID, QUEST_POINTS_ID).getText().replace(
						"Quest Points: ", "");
		return Integer.parseInt(s.split(" / ")[1]);
	}

	/**
	 * Checks if the specified quest is filtered out or not.
	 * 
	 * @param q
	 *            Quest to check.
	 * @return Whether the Quest is filtered out or not.
	 */
	public boolean isHidden(com.kbotpro.scriptsystem.enums.Quest q) {
		if (getQuestIComponent(q) != null) {
			return getQuestIComponent(q).getTextColor() == 2236962;
		}
		return false;
	}

	/**
	 * Checks if a Quest is finished. Only works if not hidden.
	 * 
	 * @param q
	 *            Quest to check completion status of.
	 * @return Whether the specified quest is completed or not.
	 */
	public boolean isQuestDone(com.kbotpro.scriptsystem.enums.Quest q) {
		if (!isHidden(q)) {
			return getQuestProgress(q).equals(Progress.FINISHED);
		}
		return false;
	}

	/**
	 * Returns a Quest.Progress object to reflect completion status of quest.
	 * Only works if not hidden.
	 * 
	 * @param q
	 *            Quest to check completion status of.
	 * @return Correct Quest.Progress object.
	 */
	public Progress getQuestProgress(com.kbotpro.scriptsystem.enums.Quest q) {
		if (!isHidden(q)) {
			switch (getQuestIComponent(q).getTextColor()) {
				case 16711680:
					return Progress.NOT_STARTED;
				case 16776960:
					return Progress.IN_PROGRESS;
				case 65280:
					return Progress.FINISHED;
			}
		}
		return null;
	}

	/**
	 * Returns the IComponent of the specified Quest.
	 * 
	 * @param q
	 *            Quest to retrieve IComponent of.
	 * @return IComponent of specified Quest.
	 */
	public IComponent getQuestIComponent(com.kbotpro.scriptsystem.enums.Quest q) {
		setTab();
		for (IComponent i : getQuestIComponents()) {
			if (i.getText().equals(q.getQuestName())) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Returns an array of all the individual quest IComponents.
	 * 
	 * @return Array of all individual quest IComponents.
	 */
	public IComponent[] getQuestIComponents() {
		setTab();
		return botEnv.interfaces.getComponent(INTERFACE_ID, QUESTS_ID)
				.getChildren();
	}

	/**
	 * Selects the Quests tab if not already selected.
	 */
	public void setTab() {
		if (botEnv.game.getCurrentTab() != Game.TAB_QUESTS) {
			botEnv.game.openTab(Game.TAB_QUESTS);
		}
	}
}

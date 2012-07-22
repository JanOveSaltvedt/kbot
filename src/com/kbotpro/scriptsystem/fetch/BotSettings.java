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

import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.bot.BotEnvironment;

/**
 * Fast access for bot specific settings
 */
public class BotSettings extends ModuleConnector {

    public static final int SETTING_DRAW_MOUSE_INDICATOR = 1;
    public static final int SETTING_DRAW_WIREFRAMES = 2;
    public static final int SETTING_INCLUDE_DECORATIVES = 4;
    public BotSettings(BotEnvironment botEnv) {
        super(botEnv);
    }

    public boolean getBooleanSetting(int setting){
        switch(setting){
            case SETTING_DRAW_MOUSE_INDICATOR: return botEnv.botPanel.settingMouseIndicator.isSelected();
            case SETTING_DRAW_WIREFRAMES: return botEnv.botPanel.settingDisplayWireframes.isSelected();
            case SETTING_INCLUDE_DECORATIVES: return botEnv.botPanel.displayDecoratives.isSelected();
            default: return false;
        }
    }
}

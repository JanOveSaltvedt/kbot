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

/**
 * Skills handles all skill related method in KBot
 * @author Kosaki
 */
public class Skills extends Data{
    public Skills(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Skills
     */
    public static final int SKILL_ATTACK = 0;
	public static final int SKILL_DEFENSE = 1;
	public static final int SKILL_STRENGTH = 2;
	public static final int SKILL_HITPOINTS = 3;
	public static final int SKILL_RANGE = 4;
	public static final int SKILL_PRAYER = 5;
	public static final int SKILL_MAGIC = 6;
	public static final int SKILL_COOKING = 7;
	public static final int SKILL_WOODCUTTING = 8;
	public static final int SKILL_FLETCHING = 9;
	public static final int SKILL_FISHING = 10;
	public static final int SKILL_FIREMAKING = 11;
	public static final int SKILL_CRAFTING = 12;
	public static final int SKILL_SMITHING = 13;
	public static final int SKILL_MINING = 14;
	public static final int SKILL_HERBLORE = 15;
	public static final int SKILL_AGILITY = 16;
	public static final int SKILL_THIEVING = 17;
	public static final int SKILL_SLAYER = 18;
	public static final int SKILL_FARMING = 19;
	public static final int SKILL_RUNECRAFTING = 20;
	public static final int SKILL_HUNTER = 21;
	public static final int SKILL_CONSTRUCTION = 22;
	public static final int SKILL_SUMMONING = 23;

    /**
     * Gets the current experience in a skill.
     * Example usage:
     * <code>skills.getExperience(Skills.SKILL_SLAYER);<code>
     * @param skill skill is a constant starting with SKILL_
     * @return integer: The experience in the skill or -1 if invalid skill or an error occured
     */
    public int getExperience(int skill){
        int[] skills = getClient().getExperiences();
        if(skills == null || skills.length == 0 || skill < 0 || skill >= skills.length){
            return -1;
        }
        return skills[skill];
    }

    /**
     * Gets the current level in the given skill.
     * Example usage:
     * <code>skills.getLevel(Skills.SKILL_SLAYER);<code>
     * @param skill skill is constant starting with SKILL_
     * @return integer: The level in the skill or -1 if invalid skill or an error occured
     */
    public int getLevel(int skill){
        int[] skills = getClient().getLevels();
        if(skills == null || skills.length == 0 || skill < 0 || skill >= skills.length){
            return -1;
        }
        return skills[skill];
    }

    /**
     * Gets the amount of experience needed until next level.
     * Example usage:
     * <code>skills.getExperienceToNextLevel(Skills.SKILL_SLAYER);</code>
     * @param skill skill is constant starting with SKILL_
     * @return integer: The amount of experience until next level or 0 if level 99.
     * Returns -1 if invalid skill or an error occured.
     */
    public int getExperienceToNextLevel(int skill){
        int level = getLevel(skill);
        if(level == -1){
            return -1;
        }
        if(level == 99)
            return 0;

        int experience = getExperience(skill);
        int needed = expTable[level+1];
        return needed - experience;
    }

    /**
     * Gets the amount of experience until level specified in level in the skill in skill.
     * @param skill skill is constant and starts with SKILL_
     * @param level level is a valid level between 1 and 99.
     * @return integer: the amount of experience to the level or -1 if wrong arguments or an error occured.
     */
    public int getExperienceToLevel(int skill, int level){
        if(level < 0 || level > 99)
            return -1;
        int experience = getExperience(skill);
        if(experience == -1)
            return -1;
        return expTable[level]-experience;
    }

    /**
     * Gets the percentage until next level
     * @param skill skill is constant and starts with SKILL_
     * @return integer: 0-100% or -1 if wrong argument or an error occured.
     * @Fixed by Fatality
     */
		public int getPercentageToNextLevel(int skill){
		int currLvl = getLevel(skill);
		if (currLvl == 99)
			return 0; // Duh.
		int expTot = expTable[currLvl+1] - expTable[currLvl];
		if (expTot == 0)
			return 0; // Duh
		int completedXP = getExperience(skill) - expTable[currLvl];
		return (100 * completedXP / expTot);
	}

    /**
     * Table of levels. Index is the level and the value is the experience required.
     */
    private static final int[] expTable = {
            0 , 0, 83, 174, 276, 388, 512, 650, 801,
            969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973,
            4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031,
            13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408,
            33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
            83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636,
            184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599,
            407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
            899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200,
            1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
            3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253,
            7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431
    };
}

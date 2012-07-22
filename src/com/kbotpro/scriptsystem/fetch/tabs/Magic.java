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
import com.kbotpro.scriptsystem.fetch.Skills;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.*;
import com.kbotpro.scriptsystem.wrappers.Character;

/**
 * Created by IntelliJ IDEA.
 * User: Scott
 * Date: Jan 18, 2010
 * Time: 3:02:50 PM
 */
public class Magic extends ModuleConnector {

	public Regular REGULAR;
	public Ancients ANCIENTS;
	public Lunar LUNAR;
	public ItemNode FIRE_RUNE = new ItemNode(554, true);
	public ItemNode WATER_RUNE = new ItemNode(555, true);
	public ItemNode AIR_RUNE = new ItemNode(556, true);
	public ItemNode EARTH_RUNE = new ItemNode(557, true);
	public ItemNode DUST_RUNE = new ItemNode(4696, true);
	public ItemNode LAVA_RUNE = new ItemNode(4699, true);
	public ItemNode MUD_RUNE = new ItemNode(4695, true);
	public ItemNode SMOKE_RUNE = new ItemNode(4697, true);
	public ItemNode STEAM_RUNE = new ItemNode(4694, true);
	public ItemNode BODY_RUNE = new ItemNode(559, true);
	public ItemNode COSMIC_RUNE = new ItemNode(564, true);
	public ItemNode NATURE_RUNE = new ItemNode(561, true);
	public ItemNode LAW_RUNE = new ItemNode(563, true);
	public ItemNode MIND_RUNE = new ItemNode(558, true);
	public ItemNode CHAOS_RUNE = new ItemNode(562, true);
	public ItemNode ASTRAL_RUNE = new ItemNode(9075, true);
	public ItemNode DEATH_RUNE = new ItemNode(560, true);
	public ItemNode BLOOD_RUNE = new ItemNode(565, true);
	public ItemNode SOUL_RUNE = new ItemNode(566, true);
	public ItemNode BANANA = new ItemNode(1963, true);
	public ItemNode SARADOMIN_STAFF = new ItemNode(2415, false);
	public ItemNode GUTHIX_STAFF = new ItemNode(2416, false);
	public ItemNode ZAMORAK_STAFF = new ItemNode(2417, false);
	public ItemNode IBAN_STAFF = new ItemNode(1409, false);
	public ItemNode SLAYER_STAFF = new ItemNode(4170, false);

	public Magic(BotEnvironment botEnv) {
		super(botEnv);
		REGULAR = new Regular();
		ANCIENTS = new Ancients();
		LUNAR = new Lunar();
	}

	public class Regular {
		public final int INTERFACE_ID = 192;
		public final Options DEFENSIVE_CASTING_TOGGLE = new Options(INTERFACE_ID, 2);
		public final Options HIDE_COMBAT_SPELLS_FILTER = new Options(INTERFACE_ID, 7);
		public final Options HIDE_TELEPORT_SPELLS_FILTER = new Options(INTERFACE_ID, 9);
		public final Options MISC_SPELLS_FILTER = new Options(INTERFACE_ID, 11);
		public final Options HIDE_SKILL_SPELLS_FILTER = new Options(INTERFACE_ID, 13);
		public final Options LEVEL_ORDER_SORT = new Options(INTERFACE_ID, 15);
		public final Options COMBAT_FIRST_SORT = new Options(INTERFACE_ID, 16);
		public final Options TELEPORT_FIRST_SORT = new Options(INTERFACE_ID, 17);

		public final Spell HOME_TELEPORT = new Spell(INTERFACE_ID, 24, 0, null);
		public final Spell WIND_STRIKE = new Spell(INTERFACE_ID, 25, 1, new SpellReq[]{new SpellReq(AIR_RUNE, 1), new SpellReq(MIND_RUNE, 1)});
		public final Spell CONFUSE = new Spell(INTERFACE_ID, 26, 3, new SpellReq[]{new SpellReq(WATER_RUNE, 3), new SpellReq(EARTH_RUNE, 2), new SpellReq(BODY_RUNE, 1)});
		public final Spell ENCHANT_CROSSBOW = new Spell(INTERFACE_ID, 27, 4, null);
		public final Spell WATER_STRIKE = new Spell(INTERFACE_ID, 28, 5, new SpellReq[]{new SpellReq(AIR_RUNE, 1), new SpellReq(WATER_RUNE, 1), new SpellReq(MIND_RUNE, 1)});
		public final Spell LVL1_ENCHANT = new Spell(INTERFACE_ID, 29, 7, new SpellReq[]{new SpellReq(COSMIC_RUNE, 1), new SpellReq(WATER_RUNE, 1)});
		public final Spell EARTH_STRIKE = new Spell(INTERFACE_ID, 30, 9, new SpellReq[]{new SpellReq(AIR_RUNE, 1), new SpellReq(EARTH_RUNE, 2), new SpellReq(MIND_RUNE, 1)});
		public final Spell MOBILISING_ARMIES_TELEPORT = new Spell(INTERFACE_ID, 31, 10, new SpellReq[]{new SpellReq(AIR_RUNE, 1), new SpellReq(WATER_RUNE, 2), new SpellReq(LAW_RUNE, 1)});
		public final Spell WEAKEN = new Spell(INTERFACE_ID, 32, 11, new SpellReq[]{new SpellReq(EARTH_RUNE, 2), new SpellReq(WATER_RUNE, 3), new SpellReq(BODY_RUNE, 1)});
		public final Spell FIRE_STRIKE = new Spell(INTERFACE_ID, 33, 13, new SpellReq[]{new SpellReq(FIRE_RUNE, 3), new SpellReq(AIR_RUNE, 2), new SpellReq(MIND_RUNE, 1)});
		public final Spell BONES_TO_BANANAS = new Spell(INTERFACE_ID, 34, 15, new SpellReq[]{new SpellReq(EARTH_RUNE, 2), new SpellReq(WATER_RUNE, 2), new SpellReq(NATURE_RUNE, 1)});
		public final Spell WIND_BOLT = new Spell(INTERFACE_ID, 35, 17, new SpellReq[]{new SpellReq(AIR_RUNE, 2), new SpellReq(CHAOS_RUNE, 1)});
		public final Spell CURSE = new Spell(INTERFACE_ID, 36, 19, new SpellReq[]{new SpellReq(WATER_RUNE, 2), new SpellReq(EARTH_RUNE, 3), new SpellReq(BODY_RUNE, 1)});
		public final Spell BIND = new Spell(INTERFACE_ID, 37, 20, new SpellReq[]{new SpellReq(NATURE_RUNE, 2), new SpellReq(EARTH_RUNE, 3), new SpellReq(WATER_RUNE, 3)});
		public final Spell LOW_LEVEL_ALCHEMY = new Spell(INTERFACE_ID, 38, 21, new SpellReq[]{new SpellReq(NATURE_RUNE, 1), new SpellReq(FIRE_RUNE, 3)});
		public final Spell WATER_BOLT = new Spell(INTERFACE_ID, 39, 23, new SpellReq[]{new SpellReq(AIR_RUNE, 2), new SpellReq(CHAOS_RUNE, 1), new SpellReq(WATER_RUNE, 2)});
		public final Spell VARROCK_TELEPORT = new Spell(INTERFACE_ID, 40, 25, new SpellReq[]{new SpellReq(AIR_RUNE, 3), new SpellReq(FIRE_RUNE, 1), new SpellReq(LAW_RUNE, 1)});
		public final Spell LVL2_ENCHANT = new Spell(INTERFACE_ID, 41, 27, new SpellReq[]{new SpellReq(AIR_RUNE, 3), new SpellReq(COSMIC_RUNE, 1)});
		public final Spell EARTH_BOLT = new Spell(INTERFACE_ID, 42, 29, new SpellReq[]{new SpellReq(AIR_RUNE, 2), new SpellReq(EARTH_RUNE, 3), new SpellReq(CHAOS_RUNE, 1)});
		public final Spell LUMBRIDGE_TELEPORT = new Spell(INTERFACE_ID, 43, 31, new SpellReq[]{new SpellReq(AIR_RUNE, 3), new SpellReq(EARTH_RUNE, 11), new SpellReq(LAW_RUNE, 1)});
		public final Spell TELEKINETIC_GRAB = new Spell(INTERFACE_ID, 44, 33, new SpellReq[]{new SpellReq(AIR_RUNE, 1), new SpellReq(LAW_RUNE, 1)});
		public final Spell FIRE_BOLT = new Spell(INTERFACE_ID, 45, 35, new SpellReq[]{new SpellReq(AIR_RUNE, 3), new SpellReq(FIRE_RUNE, 4), new SpellReq(CHAOS_RUNE, 1)});
		public final Spell FALADOR_TELEPORT = new Spell(INTERFACE_ID, 46, 37, new SpellReq[]{new SpellReq(AIR_RUNE, 3), new SpellReq(WATER_RUNE, 1), new SpellReq(LAW_RUNE, 1)});
		public final Spell CRUMBLE_UNDEAD = new Spell(INTERFACE_ID, 47, 39, new SpellReq[]{new SpellReq(AIR_RUNE, 2), new SpellReq(EARTH_RUNE, 2), new SpellReq(CHAOS_RUNE, 1)});
		public final Spell TELEPORT_TO_HOUSE = new Spell(INTERFACE_ID, 48, 40, new SpellReq[]{new SpellReq(AIR_RUNE, 1), new SpellReq(EARTH_RUNE, 1), new SpellReq(LAW_RUNE, 1)});
		public final Spell WIND_BLAST = new Spell(INTERFACE_ID, 49, 41, new SpellReq[]{new SpellReq(AIR_RUNE, 3), new SpellReq(DEATH_RUNE, 1)});
		public final Spell SUPERHEAT_ITEM = new Spell(INTERFACE_ID, 50, 43, new SpellReq[]{new SpellReq(FIRE_RUNE, 4), new SpellReq(NATURE_RUNE, 1)});
		public final Spell CAMELOT_TELEPORT = new Spell(INTERFACE_ID, 51, 45, new SpellReq[]{new SpellReq(AIR_RUNE, 5), new SpellReq(LAW_RUNE, 1)});
		public final Spell WATER_BLAST = new Spell(INTERFACE_ID, 52, 47, new SpellReq[]{new SpellReq(AIR_RUNE, 3), new SpellReq(WATER_RUNE, 3), new SpellReq(DEATH_RUNE, 1)});
		public final Spell LVL3_ENCHANT = new Spell(INTERFACE_ID, 53, 49, new SpellReq[]{new SpellReq(FIRE_RUNE, 5), new SpellReq(COSMIC_RUNE, 1)});
		public final Spell IBAN_BLAST = new Spell(INTERFACE_ID, 54, 50, new SpellReq[]{new SpellReq(FIRE_RUNE, 5), new SpellReq(DEATH_RUNE, 1), new SpellReq(IBAN_STAFF, 1)});
		public final Spell SNARE = new Spell(INTERFACE_ID, 55, 50, new SpellReq[]{new SpellReq(NATURE_RUNE, 3), new SpellReq(EARTH_RUNE, 4), new SpellReq(WATER_RUNE, 4)});
		public final Spell MAGIC_DART = new Spell(INTERFACE_ID, 56, 50, new SpellReq[]{new SpellReq(DEATH_RUNE, 1), new SpellReq(MIND_RUNE, 4), new SpellReq(SLAYER_STAFF, 1)});
		public final Spell ARDOUGNE_TELEPORT = new Spell(INTERFACE_ID, 57, 51, new SpellReq[]{new SpellReq(WATER_RUNE, 2), new SpellReq(LAW_RUNE, 2)});
		public final Spell EARTH_BLAST = new Spell(INTERFACE_ID, 58, 53, new SpellReq[]{new SpellReq(AIR_RUNE, 3), new SpellReq(EARTH_RUNE, 4), new SpellReq(DEATH_RUNE, 1)});
		public final Spell HIGH_LEVEL_ALCHEMY = new Spell(INTERFACE_ID, 59, 55, new SpellReq[]{new SpellReq(FIRE_RUNE, 5), new SpellReq(NATURE_RUNE, 1)});
		public final Spell CHARGE_WATER_ORB = new Spell(INTERFACE_ID, 60, 56, new SpellReq[]{new SpellReq(WATER_RUNE, 30), new SpellReq(COSMIC_RUNE, 3)});
		public final Spell LVL4_ENCHANT = new Spell(INTERFACE_ID, 61, 57, new SpellReq[]{new SpellReq(COSMIC_RUNE, 1), new SpellReq(EARTH_RUNE, 10)});
		public final Spell WATCHTOWER_TELEPORT = new Spell(INTERFACE_ID, 62, 58, new SpellReq[]{new SpellReq(LAW_RUNE, 2), new SpellReq(EARTH_RUNE, 2)});
		public final Spell FIRE_BLAST = new Spell(INTERFACE_ID, 63, 59, new SpellReq[]{new SpellReq(AIR_RUNE, 4), new SpellReq(FIRE_RUNE, 5), new SpellReq(DEATH_RUNE, 1)});
		public final Spell CHARGE_EARTH_ORB = new Spell(INTERFACE_ID, 64, 60, new SpellReq[]{new SpellReq(COSMIC_RUNE, 3), new SpellReq(EARTH_RUNE, 30)});
		public final Spell BONES_TO_PEACHS = new Spell(INTERFACE_ID, 65, 60, new SpellReq[]{new SpellReq(EARTH_RUNE, 4), new SpellReq(WATER_RUNE, 4), new SpellReq(NATURE_RUNE, 2)});
		public final Spell SARADOMIN_STRIKE = new Spell(INTERFACE_ID, 66, 60, new SpellReq[]{new SpellReq(AIR_RUNE, 4), new SpellReq(BLOOD_RUNE, 2), new SpellReq(FIRE_RUNE, 2), new SpellReq(SARADOMIN_STAFF, 1)});
		public final Spell CLAWS_OF_GUTHIX = new Spell(INTERFACE_ID, 67, 60, new SpellReq[]{new SpellReq(AIR_RUNE, 4), new SpellReq(BLOOD_RUNE, 2), new SpellReq(FIRE_RUNE, 1), new SpellReq(GUTHIX_STAFF, 1)});
		public final Spell FLAMES_OF_ZAMORAK = new Spell(INTERFACE_ID, 68, 60, new SpellReq[]{new SpellReq(AIR_RUNE, 1), new SpellReq(BLOOD_RUNE, 2), new SpellReq(FIRE_RUNE, 4), new SpellReq(ZAMORAK_STAFF, 1)});
		public final Spell TROLLHEIM_TELEPORT = new Spell(INTERFACE_ID, 69, 61, new SpellReq[]{new SpellReq(LAW_RUNE, 2), new SpellReq(FIRE_RUNE, 2)});
		public final Spell WIND_WAVE = new Spell(INTERFACE_ID, 70, 62, new SpellReq[]{new SpellReq(AIR_RUNE, 5), new SpellReq(BLOOD_RUNE, 1)});
		public final Spell CHARGE_FIRE_ORB = new Spell(INTERFACE_ID, 71, 63, new SpellReq[]{new SpellReq(COSMIC_RUNE, 3), new SpellReq(FIRE_RUNE, 30)});
		public final Spell TELEPORT_TO_APE_ATOLL = new Spell(INTERFACE_ID, 72, 64, new SpellReq[]{new SpellReq(LAW_RUNE, 2), new SpellReq(WATER_RUNE, 2), new SpellReq(FIRE_RUNE, 2), new SpellReq(BANANA, 1)});
		public final Spell WATER_WAVE = new Spell(INTERFACE_ID, 73, 65, new SpellReq[]{new SpellReq(AIR_RUNE, 5), new SpellReq(BLOOD_RUNE, 1), new SpellReq(WATER_RUNE, 7)});
		public final Spell CHARGE_AIR_ORB = new Spell(INTERFACE_ID, 74, 66, new SpellReq[]{new SpellReq(AIR_RUNE, 30), new SpellReq(COSMIC_RUNE, 3)});
		public final Spell VULNERABILITY = new Spell(INTERFACE_ID, 75, 66, new SpellReq[]{new SpellReq(EARTH_RUNE, 5), new SpellReq(WATER_RUNE, 5), new SpellReq(SOUL_RUNE, 1)});
		public final Spell LVL5_ENCHANT = new Spell(INTERFACE_ID, 76, 68, new SpellReq[]{new SpellReq(EARTH_RUNE, 15), new SpellReq(WATER_RUNE, 15), new SpellReq(COSMIC_RUNE, 1)});
		public final Spell EARTH_WAVE = new Spell(INTERFACE_ID, 77, 70, new SpellReq[]{new SpellReq(AIR_RUNE, 5), new SpellReq(BLOOD_RUNE, 1), new SpellReq(EARTH_RUNE, 7)});
		public final Spell ENFEEBLE = new Spell(INTERFACE_ID, 78, 73, new SpellReq[]{new SpellReq(EARTH_RUNE, 8), new SpellReq(WATER_RUNE, 8), new SpellReq(SOUL_RUNE, 1)});
		public final Spell TELEOTHER_LUMBRIDGE = new Spell(INTERFACE_ID, 79, 74, new SpellReq[]{new SpellReq(SOUL_RUNE, 1), new SpellReq(LAW_RUNE, 1), new SpellReq(EARTH_RUNE, 1)});
		public final Spell FIRE_WAVE = new Spell(INTERFACE_ID, 80, 75, new SpellReq[]{new SpellReq(AIR_RUNE, 5), new SpellReq(BLOOD_RUNE, 1), new SpellReq(FIRE_RUNE, 7)});
		public final Spell ENTANGLE = new Spell(INTERFACE_ID, 81, 79, new SpellReq[]{new SpellReq(EARTH_RUNE, 5), new SpellReq(WATER_RUNE, 5), new SpellReq(NATURE_RUNE, 4)});
		public final Spell STUN = new Spell(INTERFACE_ID, 82, 80, new SpellReq[]{new SpellReq(EARTH_RUNE, 12), new SpellReq(WATER_RUNE, 12), new SpellReq(SOUL_RUNE, 1)});
		public final Spell CHARGE = new Spell(INTERFACE_ID, 83, 80, new SpellReq[]{new SpellReq(AIR_RUNE, 3), new SpellReq(BLOOD_RUNE, 3), new SpellReq(FIRE_RUNE, 3)});
		public final Spell WIND_SURGE = new Spell(INTERFACE_ID, 84, 81, new SpellReq[]{new SpellReq(AIR_RUNE, 7), new SpellReq(BLOOD_RUNE, 1), new SpellReq(AIR_RUNE, 10)});
		public final Spell TELEOTHER_FALADOR = new Spell(INTERFACE_ID, 85, 82, new SpellReq[]{new SpellReq(SOUL_RUNE, 1), new SpellReq(LAW_RUNE, 1), new SpellReq(WATER_RUNE, 1)});
		public final Spell TELEPORT_BLOCK = new Spell(INTERFACE_ID, 86, 85, new SpellReq[]{new SpellReq(DEATH_RUNE, 1), new SpellReq(LAW_RUNE, 1), new SpellReq(DEATH_RUNE, 1)});
		public final Spell WATER_SURGE = new Spell(INTERFACE_ID, 87, 85, new SpellReq[]{new SpellReq(AIR_RUNE, 7), new SpellReq(BLOOD_RUNE, 1), new SpellReq(WATER_RUNE, 10), new SpellReq(DEATH_RUNE, 1)});
		public final Spell LVL6_ENCHANT = new Spell(INTERFACE_ID, 88, 87, new SpellReq[]{new SpellReq(COSMIC_RUNE, 1), new SpellReq(EARTH_RUNE, 20), new SpellReq(FIRE_RUNE, 20)});
		public final Spell EARTH_SURGE = new Spell(INTERFACE_ID, 89, 90, new SpellReq[]{new SpellReq(AIR_RUNE, 7), new SpellReq(BLOOD_RUNE, 1), new SpellReq(DEATH_RUNE, 1)});
		public final Spell TELEOTHER_CAMELOT = new Spell(INTERFACE_ID, 90, 90, new SpellReq[]{new SpellReq(SOUL_RUNE, 2), new SpellReq(LAW_RUNE, 1)});
		public final Spell FIRE_SURGE = new Spell(INTERFACE_ID, 91, 95, new SpellReq[]{new SpellReq(AIR_RUNE, 7), new SpellReq(BLOOD_RUNE, 1), new SpellReq(FIRE_RUNE, 10), new SpellReq(DEATH_RUNE, 1)});
		private ScrollbarIComponent scrollBar;

		public ScrollbarIComponent getScrollBar() {
			return scrollBar != null ? scrollBar : (scrollBar = new ScrollbarIComponent(botEnv, INTERFACE_ID, 94, 1, 4, 5));
		}
	}

	public class Ancients {
	}

	public class Lunar {
		public final int INTERFACE_ID = 430;
		public final Options DEFENSIVE_CASTING_TOGGLE = new Options(INTERFACE_ID, 20);
		public final Options HIDE_COMBAT_SPELLS_FILTER = new Options(INTERFACE_ID, 5);
		public final Options HIDE_TELEPORT_SPELLS_FILTER = new Options(INTERFACE_ID, 7);
		public final Options MISC_SPELLS_FILTER = new Options(INTERFACE_ID, 8);
		public final Options LEVEL_ORDER_SORT = new Options(INTERFACE_ID, 11);
		public final Options COMBAT_FIRST_SORT = new Options(INTERFACE_ID, 12);
		public final Options TELEPORT_FIRST_SORT = new Options(INTERFACE_ID, 13);
		public final Spell CURE_OTHER = new Spell(INTERFACE_ID, 23, 68, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 1), new SpellReq(LAW_RUNE, 1), new SpellReq(EARTH_RUNE, 10)});
		public final Spell FERTILE_SOIL = new Spell(INTERFACE_ID, 24, 83, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(NATURE_RUNE, 2), new SpellReq(EARTH_RUNE, 15)});
		public final Spell CURE_GROUP = new Spell(INTERFACE_ID, 25, 74, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(LAW_RUNE, 2), new SpellReq(COSMIC_RUNE, 2)});
		public final Spell NPC_CONTACT = new Spell(INTERFACE_ID, 26, 67, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 1), new SpellReq(COSMIC_RUNE, 1), new SpellReq(AIR_RUNE, 2)});
		public final Spell ENERGY_TRANSFER = new Spell(INTERFACE_ID, 27, 91, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(LAW_RUNE, 2), new SpellReq(NATURE_RUNE, 1)});
		public final Spell MONSTER_EXAMINE = new Spell(INTERFACE_ID, 28, 66, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 1), new SpellReq(COSMIC_RUNE, 1), new SpellReq(MIND_RUNE, 1)});
		public final Spell HUMIDIFY = new Spell(INTERFACE_ID, 29, 68, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 1), new SpellReq(WATER_RUNE, 3), new SpellReq(FIRE_RUNE, 1)});
		public final Spell HUNTER_KIT = new Spell(INTERFACE_ID, 30, 71, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(EARTH_RUNE, 2)});
		public final Spell STAT_SPY = new Spell(INTERFACE_ID, 31, 75, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(COSMIC_RUNE, 2), new SpellReq(BODY_RUNE, 5)});
		public final Spell DREAM = new Spell(INTERFACE_ID, 32, 79, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(COSMIC_RUNE, 1), new SpellReq(BODY_RUNE, 5)});
		public final Spell PLANK_MAKE = new Spell(INTERFACE_ID, 33, 86, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(NATURE_RUNE, 1), new SpellReq(EARTH_RUNE, 15)});
		public final Spell SPELLBOOK_SWAP = new Spell(INTERFACE_ID, 34, 96, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(LAW_RUNE, 1), new SpellReq(COSMIC_RUNE, 2)});
		public final Spell MAGIC_IMBUE = new Spell(INTERFACE_ID, 35, 82, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(FIRE_RUNE, 7), new SpellReq(WATER_RUNE, 7)});
		public final Spell VENGEANCE = new Spell(INTERFACE_ID, 36, 94, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 4), new SpellReq(DEATH_RUNE, 2), new SpellReq(EARTH_RUNE, 10)});
		public final Spell BAKE_PIE = new Spell(INTERFACE_ID, 37, 65, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 1), new SpellReq(FIRE_RUNE, 5), new SpellReq(WATER_RUNE, 4)});
		public final Spell LUNAR_HOME_TELEPORT = new Spell(INTERFACE_ID, 38, 0, null);
		public final Spell FISHING_GUILD_TELEPORT = new Spell(INTERFACE_ID, 39, 85, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(LAW_RUNE, 3), new SpellReq(WATER_RUNE, 10)});
		public final Spell KHAZARD_TELEPORT = new Spell(INTERFACE_ID, 40, 78, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(LAW_RUNE, 2), new SpellReq(WATER_RUNE, 4)});
		public final Spell VENGEANCE_OTHER = new Spell(INTERFACE_ID, 41, 93, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(DEATH_RUNE, 2), new SpellReq(EARTH_RUNE, 10)});
		public final Spell MOONCLAN_TELEPORT = new Spell(INTERFACE_ID, 42, 69, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(LAW_RUNE, 1), new SpellReq(EARTH_RUNE, 2)});
		public final Spell CATHERBY_TELEPORT = new Spell(INTERFACE_ID, 43, 87, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(LAW_RUNE, 3), new SpellReq(WATER_RUNE, 10)});
		public final Spell STRING_JEWELLERY = new Spell(INTERFACE_ID, 44, 80, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(EARTH_RUNE, 10), new SpellReq(WATER_RUNE, 5)});
		public final Spell CURE_ME = new Spell(INTERFACE_ID, 45, 71, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(LAW_RUNE, 1), new SpellReq(COSMIC_RUNE, 2)});
		public final Spell WATERBIRTH_TELEPORT = new Spell(INTERFACE_ID, 46, 72, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(LAW_RUNE, 1), new SpellReq(WATER_RUNE, 1)});
		public final Spell SUPERGLASS_MAKE = new Spell(INTERFACE_ID, 47, 77, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(FIRE_RUNE, 6), new SpellReq(AIR_RUNE, 10)});
		public final Spell BOOST_POTION_SHARE = new Spell(INTERFACE_ID, 48, 84, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(EARTH_RUNE, 12), new SpellReq(WATER_RUNE, 10)});
		public final Spell STAT_RESTORE_POT_SHARE = new Spell(INTERFACE_ID, 49, 81, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(EARTH_RUNE, 10), new SpellReq(WATER_RUNE, 10)});
		public final Spell ICE_PLATEAU_TELEPORT = new Spell(INTERFACE_ID, 50, 89, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(LAW_RUNE, 3), new SpellReq(WATER_RUNE, 8)});
		public final Spell HEAL_OTHER = new Spell(INTERFACE_ID, 51, 92, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(BLOOD_RUNE, 1), new SpellReq(LAW_RUNE, 3)});
		public final Spell HEAL_GROUP = new Spell(INTERFACE_ID, 52, 95, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 4), new SpellReq(BLOOD_RUNE, 3), new SpellReq(LAW_RUNE, 6)});
		public final Spell OURANIA_TELEPORT = new Spell(INTERFACE_ID, 53, 71, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(LAW_RUNE, 1), new SpellReq(EARTH_RUNE, 6)});
		public final Spell CURE_PLANT = new Spell(INTERFACE_ID, 54, 66, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 1), new SpellReq(EARTH_RUNE, 8)});
		public final Spell TELE_GROUP_MOONCLAN = new Spell(INTERFACE_ID, 55, 70, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(LAW_RUNE, 1), new SpellReq(EARTH_RUNE, 4)});
		public final Spell TELE_GROUP_WATERBIRTH = new Spell(INTERFACE_ID, 56, 73, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(LAW_RUNE, 1), new SpellReq(WATER_RUNE, 5)});
		public final Spell TELE_GROUP_BARBARIAN = new Spell(INTERFACE_ID, 57, 76, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(LAW_RUNE, 2), new SpellReq(FIRE_RUNE, 6)});
		public final Spell TELE_GROUP_KHAZARD = new Spell(INTERFACE_ID, 58, 79, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 2), new SpellReq(LAW_RUNE, 2), new SpellReq(WATER_RUNE, 8)});
		public final Spell TELE_GROUP_FISHING_GUILD = new Spell(INTERFACE_ID, 59, 86, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(LAW_RUNE, 3), new SpellReq(WATER_RUNE, 14)});
		public final Spell TELE_GROUP_CATHERBY = new Spell(INTERFACE_ID, 60, 88, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(LAW_RUNE, 3), new SpellReq(WATER_RUNE, 15)});
		public final Spell TELE_GROUP_ICE_PLATEAU = new Spell(INTERFACE_ID, 61, 90, new SpellReq[]{new SpellReq(ASTRAL_RUNE, 3), new SpellReq(LAW_RUNE, 3), new SpellReq(WATER_RUNE, 16)});
	}

	public class Spell {
		private int parentID;
		private int ID;
		private int level;
		private SpellReq[] requiredValues;

		public Spell(int parentID, int ID, int level, SpellReq[] requiredValues) {
			this.parentID = parentID;
			this.ID = ID;
			this.level = level;
			this.requiredValues = requiredValues;
		}

		public IComponent getComponent() {
			return botEnv.interfaces.getComponent(parentID, ID);
		}

		/**
		 * Attempts to click the spell.
		 *
		 * @param character The player or NPC you want to cast the spell on
		 * @return Whether or not the method succeeded.
		 */
		public boolean castOn(Character character) {
				botEnv.game.openTab(Game.TAB_MAGIC);
			if (canCast() && getComponent().isVisible()) {
				//if (parentID == 192 && !botEnv.interfaces.getComponent(192, container).getBounds().contains(getComponent().getBounds()))
				//	regular.getScrollBar().scrollTo(getComponent());
				return getComponent().doClick() && character.doClick();
			}
			return false;
		}

		/**
		 * Attempts to click the spell.
		 *
		 * @param item The Item you want to cast the spell on
		 * @return Whether or not the method succeeded.
		 */
		public boolean castOn(Item item) {
			botEnv.game.openTab(Game.TAB_MAGIC);
			if (canCast() && getComponent().isVisible()) {
				//if (parentID == 192 && !botEnv.interfaces.getComponent(192, container).getBounds().contains(getComponent().getBounds()))
				//	regular.getScrollBar().scrollTo(getComponent());
				return getComponent().doClick() && item.doClick();
			}
			return false;
		}

		/**
		 * Checks if the player has the necessary items to cast the spell.
		 *
		 * @return Whether or not the player has the necessary items to cast the spell.
		 */
		public boolean hasItems() {
			for (SpellReq r : requiredValues) {
				if (r.item.isEquipment && (botEnv.inventory.getCount(true, r.item.ID) >= r.amount) || (r.item == FIRE_RUNE && (botEnv.equipment.isItemEquipped(1387, 1393, 1401, 3053, 3054, 11736, 11738) || botEnv.inventory.getCount(true, LAVA_RUNE.ID, STEAM_RUNE.ID, SMOKE_RUNE.ID) >= r.amount)) || (r.item == WATER_RUNE && (botEnv.equipment.isItemEquipped(1383, 1395, 1403, 6562, 6563, 11736, 11738) || botEnv.inventory.getCount(true, MUD_RUNE.ID, STEAM_RUNE.ID) >= r.amount)) || (r.item == AIR_RUNE && (botEnv.equipment.isItemEquipped(1381, 1397, 1405) || botEnv.inventory.getCount(true, DUST_RUNE.ID, SMOKE_RUNE.ID) >= r.amount)) || (r.item == EARTH_RUNE && (botEnv.equipment.isItemEquipped(1385, 1399, 1407, 3053, 3054, 6562, 6563) || botEnv.inventory.getCount(true, LAVA_RUNE.ID, DUST_RUNE.ID, MUD_RUNE.ID) >= r.amount))) {
					continue;
				}
				if (!r.item.isEquipment && botEnv.equipment.isItemEquipped(r.item.ID)) {
					continue;
				}
				return false;
			}
			return true;
		}

		/**
		 * Checks all requirements to cast a spell.
		 *
		 * @return Whether or not the player can cast the specified spell now.
		 */
		public boolean canCast() {
			return (botEnv.skills.getLevel(Skills.SKILL_MAGIC) >= level && hasItems());
		}
	}

	public class Options {
		private int parentID;
		private int ID;

		private Options(int parentID, int ID) {
			this.parentID = parentID;
			this.ID = ID;
		}

		public void toggle(boolean select) {
			if (select != isToggled()) {
				botEnv.game.openTab(Game.TAB_MAGIC);
				botEnv.interfaces.getComponent(parentID, ID).doClick();
			}
		}

		public boolean isToggled() {
			return botEnv.interfaces.getComponent(parentID, ID).getTextureID() == 1703;
		}
	}

	public class SpellReq {
		private ItemNode item;
		private int amount;

		private SpellReq(ItemNode item, int amount) {
			this.item = item;
			this.amount = amount;
		}
	}

	public class ItemNode {
		public int ID;
		public boolean isEquipment;

		private ItemNode(int ID, boolean isEquipment) {
			this.ID = ID;
			this.isEquipment = isEquipment;
		}
	}
}
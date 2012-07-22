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

package com.kbotpro.scriptsystem.enums;

/**
 * All quests currently available in game with attached data.
 * 
 * @author _933pm
 * 
 */
public enum Quest {
	CooksAssistant("Cook's Assistant", false), DemonSlayer("Demon Slayer",
			false), DoricsQuest("Doric's Quest", false), DragonSlayer(
			"Dragon Slayer", false), ErnesttheChicken("Ernest the Chicken",
			false), GoblinDiplomacy("Goblin Diplomacy", false), ImpCatcher(
			"Imp Catcher", false), TheKnightsSword("The Knight's Sword", false), PiratesTreasure(
			"Pirate's Treasure", false), PrinceAliRescue("Prince Ali Rescue",
			false), TheRestlessGhost("The Restless Ghost", false), RomeoJuliet(
			"Romeo & Juliet", false), RuneMysteries(
			"SpellRequirement Mysteries", false), SheepShearer("Sheep Shearer",
			false), ShieldofArrav("Shield of Arrav", false), VampireSlayer(
			"Vampire Slayer", false), WitchsPotion("Witch's Potion", false), AnimalMagnetism(
			"Animal Magnetism", true), BetweenaRock("Between a Rock...", true), BigChompyBirdHunting(
			"Big Chompy Bird Hunting", true), Biohazard("Biohazard", true), CabinFever(
			"Cabin Fever", true), ClockTower("Clock Tower", true), Contact(
			"Contact!", true), ZogreFleshEaters("Zogre Flesh Eaters", true), CreatureofFenkenstrain(
			"Creature of Fenkenstrain", true), DarknessofHallowvale(
			"Darkness of Hallowvale", true), DeathtotheDorgeshuun(
			"Death to the Dorgeshuun", true), DeathPlateau("Death Plateau",
			true), DesertTreasure("Desert Treasure", true), DeviousMinds(
			"Devious Minds", true), TheDigSite("The Dig Site", true), DruidicRitual(
			"Druidic Ritual", true), DwarfCannon("Dwarf Cannon", true), EadgarsRuse(
			"Eadgar's Ruse", true), EaglesPeak("Eagles' Peak", true), ElementalWorkshopI(
			"Elemental Workshop I", true), ElementalWorkshopII(
			"Elemental Workshop II", true), EnakhrasLament("Enakhra's Lament",
			true), EnlightenedJourney("Enlightened Journey", true), TheEyesofGlouphrie(
			"The Eyes of Glouphrie", true), FairytaleIGrowingPains(
			"Fairytale I - Growing Pains", true), FairytaleIICureaQueen(
			"Fairytale II - Cure a Queen", true), FamilyCrest("Family Crest",
			true), TheFeud("The Feud", true), FightArena("Fight Arena", true), FishingContest(
			"Fishing Contest", true), ForgettableTale("Forgettable Tale...",
			true), TheFremennikTrials("The Fremennik Trials", true), WaterfallQuest(
			"Waterfall Quest", true), GardenofTranquillity(
			"Garden of Tranquillity", true), GertrudesCat("Gertrude's Cat",
			true), GhostsAhoy("Ghosts Ahoy", true), TheGiantDwarf(
			"The Giant Dwarf", true), TheGolem("The Golem", true), TheGrandTree(
			"The Grand Tree", true), TheHandintheSand("The Hand in the Sand",
			true), HauntedMine("Haunted Mine", true), HazeelCult("Hazeel Cult",
			true), HeroesQuest("Heroes' Quest", true), HolyGrail("Holy Grail",
			true), HorrorfromtheDeep("Horror from the Deep", true), IcthlarinsLittleHelper(
			"Icthlarin's Little Helper", true), InAidoftheMyreque(
			"In Aid of the Myreque", true), InSearchoftheMyreque(
			"In Search of the Myreque", true), JunglePotion("Jungle Potion",
			true), LegendsQuest("Legends' Quest", true), LostCity("Lost City",
			true), TheLostTribe("The Lost Tribe", true), LunarDiplomacy(
			"Lunar Diplomacy", true), MakingHistory("Making History", true), MerlinsCrystal(
			"Merlin's Crystal", true), MonkeyMadness("Monkey Madness", true), MonksFriend(
			"Monk's Friend", true), MountainDaughter("Mountain Daughter", true), MourningsEndsPartI(
			"Mourning's Ends Part I", true), MourningsEndsPartII(
			"Mourning's Ends Part II", true), MurderMystery("Murder Mystery",
			true), MyArmsBigAdventure("My Arm's Big Adventure", true), NatureSpirit(
			"Nature Spirit", true), ObservatoryQuest("Observatory Quest", true), OneSmallFavour(
			"One Small Favour", true), PlagueCity("Plague City", true), PriestinPeril(
			"Priest in Peril", true), RagandBoneMan("Rag and Bone Man", true), Ratcatchers(
			"Ratcatchers", true), RecipeforDisaster("Recipe for Disaster", true), RecruitmentDrive(
			"Recruitment Drive", true), Regicide("Regicide", true), RovingElves(
			"Roving Elves", true), RoyalTrouble("Royal Trouble", true), RumDeal(
			"Rum Deal", true), ScorpionCatcher("Scorpion Catcher", true), SeaSlug(
			"Sea Slug", true), TheSlugMenace("The Slug Menace", true), ShadesofMortton(
			"Shades of Mort'ton", true), ShadowoftheStorm(
			"Shadow of the Storm", true), SheepHerder("Sheep Herder", true), ShiloVillage(
			"Shilo Village", true), ASoulsBane("A Soul's Bane", true), SpiritsoftheElid(
			"Spirits of the Elid", true), SwanSong("Swan Song", true), TaiBwoWannaiTrio(
			"Tai Bwo Wannai Trio", true), ATailofTwoCats("A Tail of Two Cats",
			true), TearsofGuthix("Tears of Guthix", true), TempleofIkov(
			"Temple of Ikov", true), ThroneofMiscellania(
			"Throne of Miscellania", true), TheTouristTrap("The Tourist Trap",
			true), WitchsHouse("Witch's House", true), TreeGnomeVillage(
			"Tree Gnome Village", true), TribalTotem("Tribal Totem", true), TrollRomance(
			"Troll Romance", true), TrollStronghold("Troll Stronghold", true), UndergroundPass(
			"Underground Pass", true), Wanted("Wanted!", true), Watchtower(
			"Watchtower", true), ColdWar("Cold War", true), TheFremennikIsles(
			"The Fremennik Isles", true), TowerofLife("Tower of Life", true), TheGreatBrainRobbery(
			"The Great Brain Robbery", true), WhatLiesBelow("What Lies Below",
			true), OlafsQuest("Olaf's Quest", true), AnotherSliceofHAM(
			"Another Slice of H.A.M.", true), DreamMentor("Dream Mentor", true), GrimTales(
			"Grim Tales", true), KingsRansom("King's Ransom", true), ThePathofGlouphrie(
			"The Path of Glouphrie", true), BacktomyRoots("Back to my Roots",
			true), LandoftheGoblins("Land of the Goblins", true), DealingwithScabaras(
			"Dealing with Scabaras", true), WolfWhistle("Wolf Whistle", true), AsaFirstResort(
			"As a First Resort", true), CatapultConstruction(
			"Catapult Construction", true), KennithsConcerns(
			"Kennith's Concerns", true), LegacyofSeergaze("Legacy of Seergaze",
			true), PerilsofIceMountain("Perils of Ice Mountain", true), TokTzKetDill(
			"TokTz-Ket-Dill", true), SmokingKills("Smoking Kills", true), RockingOut(
			"Rocking Out", true), SpiritofSummer("Spirit of Summer", true), MeetingHistory(
			"Meeting History", true), AllFiredUp("All Fired Up", true), SummersEnd(
			"Summer's End", true), DefenderofVarrock("Defender of Varrock",
			true), WhileGuthixSleeps("While Guthix Sleeps", true), InPyreNeed(
			"In Pyre Need", true), UnstableFoundations("Unstable Foundations",
			true), MythsoftheWhiteLands("Myths of the White Lands", true), GloriousMemories(
			"Glorious Memories", true), TheTaleoftheMuspah(
			"The Tale of the Muspah", true), HuntforRedRaktuber(
			"Hunt for Red Raktuber", true), TheChosenCommander(
			"The Chosen Commander", true), SweptAway("Swept Away", true), FurnSeek(
			"Fur 'n' Seek", true), MissingMyMummy("Missing My Mummy", true), TheCurseofArrav(
			"The Curse of Arrav", true), BlackKnightsFortress(
			"Black Knights' Fortress", false), ForgivenessofaChaosDwarf(
			"Forgiveness of a Chaos Dwarf", true), WithintheLight(
			"Within the Light", true), TheTempleatSenntisten(
			"The Temple at Senntisten", true), BloodRunsDeep("Blood Runs Deep",
			true), NomadsRequiem("Nomad's Requiem", true);

	private String questName = "";
	private boolean members = true;

	Quest(String name, boolean mem) {
		this.questName = name;
		this.members = mem;
	}

	/**
	 * The name of the quest.
	 * 
	 * @return The name of the quest.
	 */
	public String getQuestName() {
		return questName;
	}

	/**
	 * Whether or not the quest is members-only.
	 * 
	 * @return Whether or not the quest is members-only.
	 */
	public boolean isMembers() {
		return members;
	}
}
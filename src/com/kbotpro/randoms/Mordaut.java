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
 * Copyright � 2010 Jan Ove Saltvedt.
 * All rights reserved.
 */

package com.kbotpro.randoms;

import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Interface;
import com.kbotpro.scriptsystem.wrappers.NPC;
import com.kbotpro.scriptsystem.wrappers.PhysicalObject;

import java.awt.*;

public class Mordaut extends Random {


    private Interface nextObjectInterface;
    private Interface relatedCardsInterface;
    private PhysicalObject door = null;
    private final NextObjectQuestion nextObjectQuestion = new NextObjectQuestion();

    private final int[] Ranged = { 11539, 11540, 11541, 11614, 11615, 11633 };

    private final int[] Cooking = { 11526, 11529, 11545, 11549, 11550, 11555, 11560,
            11563, 11564, 11607, 11608, 11616, 11620, 11621, 11622, 11623,
            11628,

            11629, 11634, 11639, 11641, 11649, 11624 };

    private final int[] Fishing = { 11527, 11574, 11578, 11580, 11599, 11600, 11601,
            11602, 11603,

            11604, 11605, 11606, 11625 };

    private final int[] Combat = { 11528, 11531, 11536, 11537, 11579, 11591, 11592,
            11593, 11597, 11627, 11631, 11635, 11636, 11638, 11642, 11648 }; //11617(bones)

    private final int[] Farming = { 11530, 11532, 11547, 11548, 11554, 11556, 11571,
            11581, 11586, 11645, 11634, 11549, 11607, 11550};

    private final int[] Magic = { 11533, 11534, 11538, 11562, 11567, 11582 };

    private final int[] Firemaking = { 11535, 11551, 11552, 11559, 11646 };

    private final int[] Hats = { 11540, 11557, 11558, 11560, 11570, 11619, 11626,
            11630, 11632, 11637, 11654 };

    private final int[] Drinks = { 11542, 11543, 11544, 11644, 11647 };

    private final int[] Woodcutting = { 11573, 11595 };

    private final int[] Boots = { 11561, 11618, 11650, 11651 };

    private final int[] Crafting = { 11546, 11565, 11566, 11568, 11569, 11572,
            11575, 11576, 11577, 11581, 11583, 11584, 11585, 11598, 11643, 11652,
            11653, 11610, 11553 };

    private final int[] Mining = { 11587, 11588, 11594, 11596, 11598, 11609, 11648,  11612};

    private final int[] Smithing = { 11611, 11612, 11613, 11553 };

    private final int[][] items = { Ranged, Cooking, Fishing, Combat, Farming, Magic,
            Firemaking, Hats, Drinks, Woodcutting, Boots, Crafting, Mining,
            Smithing };

    public final SimilarObjectQuestion[] simObjects = {
            new SimilarObjectQuestion("landlubber", Hats),
            new SimilarObjectQuestion("I never leave the house without some sort of jewellery.", Crafting),
            new SimilarObjectQuestion("There is no better feeling than", Crafting),
            new SimilarObjectQuestion("start a fire", Firemaking),
            new SimilarObjectQuestion("I'm feeling dehydrated", Drinks),
            new SimilarObjectQuestion("All this work is making me thirsty.", Drinks),
            new SimilarObjectQuestion("quenched my thirst", Drinks),
            new SimilarObjectQuestion("light my fire", Firemaking),
            new SimilarObjectQuestion("fishy", Fishing),
            new SimilarObjectQuestion("fishing for answers", Fishing),
            new SimilarObjectQuestion("fish out of water", Drinks),
            new SimilarObjectQuestion("I'm feeling dehydrated", Drinks),
            new SimilarObjectQuestion("strange headgear", Hats),
            new SimilarObjectQuestion("tip my hat", Hats),
            new SimilarObjectQuestion("thinking cap", Hats),
            new SimilarObjectQuestion("wizardry here", Magic),
            new SimilarObjectQuestion("rather mystical", Magic),
            new SimilarObjectQuestion("abracada", Magic),
            new SimilarObjectQuestion("hide one's face", Hats),
            new SimilarObjectQuestion("shall unmask", Hats),
            new SimilarObjectQuestion("hand-to-hand", Combat),
            new SimilarObjectQuestion("melee weapon", Combat),
            new SimilarObjectQuestion("me hearties", Hats),
            new SimilarObjectQuestion("mighty pirate", Hats),
            new SimilarObjectQuestion("mighty archer", Ranged),
            new SimilarObjectQuestion("as an arrow", Ranged),
            new SimilarObjectQuestion("shiny things", Crafting),

            // Default
            new SimilarObjectQuestion("range", Ranged),
            new SimilarObjectQuestion("arrow", Ranged),
            new SimilarObjectQuestion("drink", Drinks),
            new SimilarObjectQuestion("logs", Firemaking),
            new SimilarObjectQuestion("light", Firemaking),
            new SimilarObjectQuestion("headgear", Hats),
            new SimilarObjectQuestion("hat", Hats),
            new SimilarObjectQuestion("cap", Hats),
            new SimilarObjectQuestion("mine", Mining),
            new SimilarObjectQuestion("mining", Mining),
            new SimilarObjectQuestion("ore", Mining),
            new SimilarObjectQuestion("fish", Fishing),
            new SimilarObjectQuestion("fishing", Fishing),
            new SimilarObjectQuestion("thinking cap", Hats),
            new SimilarObjectQuestion("cooking", Cooking),
            new SimilarObjectQuestion("cook", Cooking),
            new SimilarObjectQuestion("bake", Cooking),
            new SimilarObjectQuestion("farm", Farming),
            new SimilarObjectQuestion("farming", Farming),
            new SimilarObjectQuestion("cast", Magic),
            new SimilarObjectQuestion("magic", Magic),
            new SimilarObjectQuestion("craft", Crafting),
            new SimilarObjectQuestion("boot", Boots),
            new SimilarObjectQuestion("chop", Woodcutting),
            new SimilarObjectQuestion("cut", Woodcutting),
            new SimilarObjectQuestion("tree", Woodcutting),

    };

    /* From rsbot
     private int Key = 11589;
     private int Book = 11590;
     private int Bones = 11617; // Combat?
     private int Feather = 11624; // Cooking?
     private int Hook = 11626; // Added to hats for pirate stuff.
     private int Cape = 11627; // Added to combat (legends cape)
     // Missing 11640
     private int Talisman = 11643; // Added to crafting

     private int Candle = 11646; // WTF? (Firemaking)

     private int Vial = 11653; // Crafting?
     */

    /**
     * Checks if the random can run
     *
     * @return true if the run() should be called.
     */
    public boolean activate() {
        //door = null;
        //NPC mordaut = npcs.getClosest(20,"Mr. Mordaut");
        return objects.getClosestObject(20, 2188, 2189, 2192, 2193) != null && isLoggedIn();
    }

    /**
     * Gets called if canRun() returns true.
     */
    public synchronized void onStart() {
        KTimer timeout = new KTimer(600000);
        while(!botEnv.randomManager.scriptStopped && activate() && !timeout.isDone()){
            int time = loop();
            if(time < 0)
                return;
            sleep(time);
        }
        if (timeout.isDone()) {
            botEnv.scriptManager.stopAllScripts();
        }
    }

    private synchronized int loop(){
        try{
            NPC mordaut = npcs.getClosest(20, 6117);
            if(mordaut == null || !isLoggedIn())
                return -1;
            if (game.hasSelectedItem()) {
                menu.atMenu("Cancel");
            }
            if (getMyPlayer().isMoving() || getMyPlayer().getAnimation() != -1) {
                return random(800, 1200);
            }
            IComponent[] leftValuables = interfaces.getInterfaces("No - I haven't left any valuables");
            if (leftValuables.length > 0 && leftValuables[0].isVisible()) {
                leftValuables[0].doClick();
                return random(900, 1200);
            }
            if(door != null){
                if (!calculations.isInGameArea(door.getScreenPos())) {
                    walking.walkToMM(door.getLocation());
                    camera.setAngleTo(door.getLocation());
                    camera.setAltitude(false);
                    return random(500,600);
                }
                if(door.doAction("Open")){
                    return random(3000, 5000);
                }
            }
            IComponent[] inter = interfaces.getInterfaces("door");
            if(inter.length > 0){
                if(inter[0].textContainsIgnoreCase("red")){
                    door = objects.getClosestObject(20, 2188);
                }
                else if(inter[0].textContainsIgnoreCase("blue")){
                    door = objects.getClosestObject(20, 2189);
                }
                else if(inter[0].textContainsIgnoreCase("purple")){
                    door = objects.getClosestObject(20, 2192);
                }
                else if(inter[0].textContainsIgnoreCase("green")){
                    door = objects.getClosestObject(20, 2193);
                }
            }

            nextObjectInterface = interfaces.getInterface(103);
            relatedCardsInterface = interfaces.getInterface(559);
            if(nextObjectInterface != null && nextObjectInterface.isValid()){
                log.log("Question type: Next object");
                if(nextObjectQuestion.getObjects()){
                    if(nextObjectQuestion.clickAnswer()){
                        return  random(800, 1200);
                    }
                    else{
                        nextObjectQuestion.guess();
                        return random(800, 1200);
                    }
                }
            } else if(relatedCardsInterface != null && relatedCardsInterface.isValid()){
                log.log("Question type: Similar objects");
                for(SimilarObjectQuestion obj: simObjects){
                    if(obj.activateCondition()){
                        if(obj.clickObjects()){
                            obj.accept();
                        }
                    }
                }
                return random(800, 1200);
            } else if(interfaces.canContinue()){
                interfaces.clickContinue();
                return random(800, 3500);
            } else {
                if (!calculations.isInGameArea(mordaut.getScreenPos())) {
                    walking.walkToMM(mordaut.getLocation());
                    camera.setAngleTo(mordaut.getLocation());
                } else if (mordaut.doAction("Talk-to")) {
                    KTimer k = new KTimer(10000);
                    while (!interfaces.canContinue() && !k.isDone()) {
                        sleep(50);
                    }
                }

            }

            return random(800, 3500);
        }catch (NullPointerException e){
            return random(800, 3500);
        }
    }

    /**
     * Gets the randoms name. Should be somewhat descriptive.
     *
     * @return
     */
    public String getName() {
        return "Mordaut / School House";
    }

    private class NextObjectQuestion {
        private int one = -1, two = -1, three = -1;

        public NextObjectQuestion() {
        }

        public boolean arrayContains(final int[] arr, final int i) {
            boolean returnt = false;
            for (final int num : arr) {
                if (num == i) {
                    returnt = true;
                }
            }

            return returnt;
        }

        public boolean clickAnswer() {
            int[] Answers;
            if ((Answers = returnAnswer()) == null) {
                return false;
            }

            for (int i = 10; i <= 13; i++) {
                if (arrayContains(Answers, nextObjectInterface.getComponent(i)
                        .getElementID())) {
                    nextObjectInterface.getComponent(i).doClick();
                    return true;
                }
            }

            return false;
        }

        public boolean getObjects() {
            one = -1;
            two = -1;
            three = -1;
            one = nextObjectInterface.getComponent(6).getElementID();
            two = nextObjectInterface.getComponent(7).getElementID();
            three = nextObjectInterface.getComponent(8).getElementID();

            return one != -1 && two != -1 && three != -1;
        }

        public void guess() {
            final int[] objects = new int[4];
            objects[0] = nextObjectInterface.getComponent(10).getElementID();
            objects[1] = nextObjectInterface.getComponent(11).getElementID();
            objects[2] = nextObjectInterface.getComponent(12).getElementID();
            objects[3] = nextObjectInterface.getComponent(13).getElementID();

            int lowest = 120;
            int click = 10;
            final int compare = returnAnswer()[0];
            if (compare <= 10) {
                nextObjectInterface.getComponent(random(10, 13)).doClick();
                return;
            }

            for (int i = 0; i < objects.length; i++) {
                if (Math.abs(objects[i] - compare) <= lowest) {
                    lowest = Math.abs(objects[i] - compare);
                }
                click = 10 + i;
            }

            nextObjectInterface.getComponent(click).doClick();
        }

        public int[] returnAnswer() {
            final int[] count = new int[items.length];
            for (int i = 0; i < count.length; i++) {
                count[i] = 0;
            }

            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items[i].length; j++) {
                    if (items[i][j] == one) {
                        count[i]++;
                    }
                    if (items[i][j] == two) {
                        count[i]++;
                    }
                    if (items[i][j] == three) {
                        count[i]++;
                    }

                    if (count[i] >= 2) {
                        log("Answer Type Found!");
                        return items[i];
                    }
                }
            }

            return null;
        }
    }


    private class SimilarObjectQuestion {
        private final String question;
        private final int[] Answers;

        public SimilarObjectQuestion(final String q, final int[] Answers) {
            question = q.toLowerCase();
            this.Answers = Answers;
        }

        private boolean accept() {
            relatedCardsInterface.getComponent(26).doClick();
            return true;
        }

        private boolean activateCondition() {
            if (!relatedCardsInterface.isValid()) {
                return false;
            }

            if (relatedCardsInterface.getComponent(25).textContainsIgnoreCase(question)) {
                log.log("Question keyword: " + question);
                return true;
            }

            return false;
        }

        private boolean clickObjects() {
            int count = 0;
            for (int i = 42; i <= 56; i++) {
                for (final int answer : Answers) {
                    IComponent anInterface = relatedCardsInterface.getComponent(i);
                    if (anInterface != null && anInterface.getElementID() == answer) {
                        anInterface.doClick();
                        sleep(400, 600);
                        count++;
                        if (count >= 3) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

}

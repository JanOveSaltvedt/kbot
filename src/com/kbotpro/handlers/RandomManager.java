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



package com.kbotpro.handlers;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.randoms.*;
import com.kbotpro.scriptsystem.Methods;
import com.kbotpro.scriptsystem.runnable.Random;
import com.kbotpro.scriptsystem.runnable.Script;
import com.kbotpro.scriptsystem.runnable.Worker;
import com.kbotpro.scriptsystem.various.KTimer;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Dec 18, 2009
 * Time: 2:44:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomManager {
    private BotEnvironment bot;
    public List<Random> loadedRandoms;
    Thread conditionalThread;
    boolean stopped = false;
    public boolean scriptStopped = false;
    public RandomManager(final BotEnvironment bot) {
        this.bot = bot;
        loadRandoms();
        conditionalThread = new Thread(new Runnable() {
            public void run() {
                while(!stopped){
                    if(RandomManager.this.bot == null || RandomManager.this.bot.scriptManager.getRunningScriptCount() == 0 || scriptStopped){
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                        }
                        continue;
                    }
                    checkForRandoms(bot);
                    try {
                        Thread.sleep(bot.botPanel.randomWaitTime);
                    } catch (InterruptedException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }, "Random checking thread");
        conditionalThread.start();

    }

    public synchronized void checkForRandoms(BotEnvironment bot) {
        for(Random random: loadedRandoms){
            try{
                if(stopped || scriptStopped){
                    return;
                }
                if(random.isEnabled()){
                    if(random.activate()){
                        if(bot.randomMulticaster != null){
                            if(!bot.randomMulticaster.randomActivated(random.getName())){
                                continue;
                            }
                        }
                        bot.scriptManager.setPauseScripts(true);
                        try {
                            bot.log.logImportant("Random "+  random.getName() + " found. Waiting for script to return.");
                            bot.mouse.stopAllJobs();
                            KTimer forceStop = new KTimer(30000);
                            boolean killed = false;
                            for (Script script : bot.scriptManager.runningScripts) {
                                for (Worker worker : script.allWorkers) {
                                   while (!worker.loopReturned) { //waits for worker's loop to return, cancels if it takes over 30 seconds.
                                        Thread.sleep(50);
                                        if (forceStop.isDone()) {
                                            bot.scriptManager.stopAllScripts();
                                            killed = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (forceStop.isDone()) {
                                bot.log.logError("Force killed script after 30 seconds. Contact the script writer to let him know of a problem.");
                            }
                            bot.log.logImportant("Starting random: "+random.getName());
                            random.runRandom();
                            if (killed) {
                                bot.game.exitGame();
                            }
                        } catch (Exception e){
                            Logger.getRootLogger().error("Exception: ", e);
                        }
                        bot.log.logImportant("Random: "+random.getName()+" exited.");

                        bot.scriptManager.setPauseScripts(false);
                        checkForRandoms(bot); // check again
                    }
                }
                Thread.sleep(150);
            }catch (Exception e){
                Logger.getRootLogger().error("Exception: ", e);
            }
        }
    }

    private void loadRandoms() {
        List<Random> loadedScripts = new ArrayList<Random>();

        loadedScripts.add(initRandom(new AutoLogin()));
        loadedScripts.add(initRandom(new BankPin()));
        loadedScripts.add(initRandom(new BeeKeeper()));
        loadedScripts.add(initRandom(new CapnArnav()));
        loadedScripts.add(initRandom(new CaveFrog()));
        loadedScripts.add(initRandom(new Certer()));
        loadedScripts.add(initRandom(new DrillDemon()));
        loadedScripts.add(initRandom(new EvilBob()));
        loadedScripts.add(initRandom(new FreakyForester()));
        loadedScripts.add(initRandom(new GraveDigger()));
        loadedScripts.add(initRandom(new LostAndFound()));
        loadedScripts.add(initRandom(new Maze()));
        loadedScripts.add(initRandom(new Mime()));
        loadedScripts.add(initRandom(new Molly()));
        loadedScripts.add(initRandom(new Mordaut()));
        loadedScripts.add(initRandom(new OddOneOut()));
        loadedScripts.add(initRandom(new Pillory()));
        loadedScripts.add(initRandom(new Pinball()));
        loadedScripts.add(initRandom(new PrisonPete()));
        loadedScripts.add(initRandom(new RewardBox()));
        loadedScripts.add(initRandom(new SandwichLady()));
        loadedScripts.add(initRandom(new SystemUpdate()));
        loadedScripts.add(initRandom(new WorldMapCloser()));
        this.loadedRandoms = loadedScripts;
    }

    private Random initRandom(Random random) {
        Method registeMethod = null;
        try {
            registeMethod = Methods.class.getDeclaredMethod("registerInternal", new Class<?>[]{BotEnvironment.class});
        } catch (NoSuchMethodException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
        registeMethod.setAccessible(true);    // This method is private so we set it by a little reflection hack
        try {
            registeMethod.invoke(random, bot);
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
        return random;
    }

    public void disposeResources() {
        stopped = true;
        loadedRandoms = null;
    }
}

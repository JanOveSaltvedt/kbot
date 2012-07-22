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

import com.kbot2.scriptable.methods.Calculations;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.hooks.Mouse;
import com.kbotpro.scriptsystem.wrappers.Tile;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Oct 22, 2009
 * Time: 9:03:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Game extends ModuleConnector {
    public Game(BotEnvironment botEnv) {
        super(botEnv);
    }

    public static final String[] TAB_NAMES = new String[]{"Combat Styles", "Stats", "Quest List",
            "Achievements", "Inventory", "Worn Equipment",
            "Prayer List", "Magic Spellbook", "Objectives", "Friends List",
            "Ignore List", "Clan Chat", "Options", "Emotes", "Music Player",
            "Notes", "Log Out"};

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
     * Gets the players curent prayer points
     *
     * @return
     */
    public int getPrayerPoints() {
        try {
            return Integer.parseInt(botEnv.interfaces.getComponent(749, 4).getText());
        } catch (final NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Gets the current running energy
     *
     * @return
     */
    public int getRunningEnergy() {
        try {
            return Integer.parseInt(botEnv.interfaces.getComponent(750, 5).getText());
        } catch (final NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Gets the players life points
     *
     * @return
     */
    public int getLifePoints() {
        try {
            return Integer.parseInt(botEnv.interfaces.getComponent(748, 8).getText());
        } catch (final NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Gets the players current health (life points / 10)
     *
     * @return
     */
    public int getHealth() {
        return (getLifePoints() / 10);
    }

    /**
     * Gets the current game state. Indicates if we are logged in and/or if the game is loading
     *
     * @return
     */
    public int getGameState() {
        return getClient().getGameState();
    }

    /**
     * Checks if the user is logged in. This may also return false when the game is loading new maps.
     *
     * @return
     */
    public boolean isLoggedIn() {
        int gameState = getGameState();
        return gameState >= 9;
    }

    /**
     * Gets the current mouse position
     *
     * @return
     */
    public Point getMousePos() {
        if (getClient() == null) {
            return new Point(-1, -1);
        }
        Mouse mouse = getClient().getMouse();
        if (mouse == null) {
            return new Point(-1, -1);
        }
        return new Point(mouse.getMouseX(), mouse.getMouseY());
    }

    /**
     * Gets an array of the games IComponents for the in game tabs.
     *
     * @return
     */
    public IComponent[] getTabButtons() {
        IComponent[] out = new IComponent[17];
        for (int i = 0; i < TAB_NAMES.length; i++) {
            out[i] = botEnv.interfaces.getGameTab(i);
        }
        return out;
    }

    /**
     * Gets the current tab.
     * ETC: GameScreen.TAB_ATTACK
     *
     * @return
     */
    public int getCurrentTab() {
        for (int i = 0; i < TAB_NAMES.length; i++) {
            if (i == TAB_LOGOUT) {
                return TAB_LOGOUT;
            }

            IComponent iComponent = botEnv.interfaces.getGameTab(i);
            if (iComponent.getTextureID() != -1) {
                return i;
            }
        }

        return TAB_LOGOUT;
    }

    /**
     * Opens the tab if not already opened.
     *
     * @param tab use constants like GameScreen.TAB_ATTACK
     */
    public void openTab(int tab) {
        if (tab == getCurrentTab())
            return;
        IComponent i = botEnv.interfaces.getGameTab(tab);
        if (i == null || !i.isElementVisible()) {
            return;
        }
        i.doClick();
    }

    /**
     * Closes the current opened tab
     *
     * @return whether the tab is closed or not.
     * @deprecated
     */
    public boolean closeCurrenTab() {
        return closeCurrentTab();
    }

    /**
     * Closes the current opened tab
     *
     * @return whether the tab is closed or not.
     */
    public boolean closeCurrentTab() {
        return getCurrentTab() <= -1 || closeTab(getCurrentTab());
    }
    
    /**
     * Closes the given tab.
     *
     * @param id
     * @return
     * @author Ampzz
     */
    public boolean closeTab(int id) {

        if (id < 0 || id > 16)
            return false;
        IComponent currTab = botEnv.interfaces.getGameTab(id);
        return (currTab != null && currTab.doClick());

    }

    /**
     * Checks if you are in lobby
     *
     * @return boolean
     */
    public boolean inLobby() {
        IComponent inLobby = botEnv.interfaces.getComponent(906, 0);
        return inLobby != null && inLobby.isVisible();
    }

    /**
     * Exits to lobby
     */
    public void exitGame() {
        openTab(TAB_LOGOUT);
        sleep(300, 1000);
        IComponent exitToLobby = botEnv.interfaces.getComponent(182, 7);
        if (exitToLobby != null && exitToLobby.isValid() && exitToLobby.isVisible())
            exitToLobby.doClick();
    }

    /**
     * Exits to login window
     */
    public void exitToLogin() {
        openTab(TAB_LOGOUT);
        sleep(300, 1000);
        IComponent exitToLobby = botEnv.interfaces.getComponent(182, 15);
        if (exitToLobby != null && exitToLobby.isValid() && exitToLobby.isVisible())
            exitToLobby.doClick();
    }

    /**
     * Complete logout
     */
    public void exitLobby() {
        IComponent exitLobby = botEnv.interfaces.getComponent(906, 187);
        if (exitLobby != null && exitLobby.isValid() && exitLobby.isVisible())
            exitLobby.doClick();
    }



    /**
     * Makes the user log out
     *
     * @deprecated
     */
    public void logout() {
        exitGame();
        sleep(500, 2000);
        exitLobby();
    }


    /**
     * Checks if an item is selected
     * This does not determine if its a spell or an inventory item thats selected
     *
     * @return
     */
    public boolean hasSelectedItem() {
        return botEnv.client.isScreenMenuItemSelected();
    }

    /*
      * Click the compass to face north
      *
     public void clickCompass() {
         Dimension d = botEnv.client.getCanvas().getSize();
         botEnv.mouse.moveMouse((int)d.getWidth() - 209 - random(0, 26), 11 + random(0, 26));
         botEnv.mouse.clickMouse(true);
     }
      */

    /**
     * Takes a screenshot with the time as filename
     */
    public void takeScreenshot() {
        botEnv.screenshot.takeScreenshot();
    }

    /**
     * Takes a screenshot with the desired file name.
     *
     * @param filename
     */
    public void takeScreenshot(String filename) {
        botEnv.screenshot.takeScreenshot(filename);
    }

    /**
     * Gets the layout of the game.
     * This checks if we are currently in fixed mode.
     *
     * @return
     */
    public boolean isFixedMode() {
        return botEnv.client.getViewSettings().getLayout() == 1;
    }

    /**
     * Checks if the game is loading.
     * This also returns true when not logged in or on welcome screen.
     *
     * @return
     */
    public boolean isLoading() {
        return getGameState() <= 25;
    }

    /**
     * Checks if the walking destination is set
     *
     * @return
     */
    public boolean isDestinationSet() {
        return botEnv.client.isDestSet() && botEnv.client.getDestX() != -1;
    }

    /**
     * Gets the walking destination
     *
     * @return destination or null if destination is not set
     */
    public Tile getDestination() {
        if (!isDestinationSet()) {
            return null;
        }
        return new Tile(botEnv.client.getDestX()+botEnv.client.getBaseX(), botEnv.client.getDestY()+botEnv.client.getBaseY());
    }

    public boolean atTile(Tile tile, String actionContains) {
        Point p = getCalculations().tileToScreen(tile);
        if (!Calculations.onScreen(p))
            botEnv.camera.setAngleTo(tile);
        p = getCalculations().tileToScreen(tile);
        if (!Calculations.onScreen(p))
            return false;
        botEnv.mouse.moveMouse(p);
        sleep(30, 100);
        return botEnv.menu.atMenu(actionContains);
    }

    public int getCurrentFloor(){
        return botEnv.client.getCurrentPlane();
    }
}

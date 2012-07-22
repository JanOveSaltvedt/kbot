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

import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.input.jobs.MouseHoverJob;
import com.kbotpro.scriptsystem.input.jobs.MouseJob;
import com.kbotpro.scriptsystem.input.callbacks.MouseMoveListener;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.hooks.MenuElementNode;
import com.kbotpro.hooks.NodeList;
import com.kbotpro.hooks.Node;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class that takes care of the interaction with the menu system of Runescape.
 */
public class Menu extends ModuleConnector {
    private static final Pattern pattern = Pattern.compile("\\<.+?\\>");

    public Menu(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Gets the menu's top left position on screen.
     * @return Point
     */
    public Point getPosition(){
        int menuX = getClient().getMenuX();
        int menuY = getClient().getMenuY();
        return new Point(menuX, menuY);
    }

    /**
     * Gets the on screen boundings of the menu.
     * @return Rectangle
     */
    public Rectangle getBounds(){
        Point pos = getPosition();
        // Client::menuHeight is not precise!
        int height = (getItemCount())*16+22;
        return new Rectangle(pos.x, pos.y, getClient().getMenuWidth(), height);
    }

    /**
     * Gets the current menu actions.
     * @return String[] array contains formatted menu strings.
     */
    public String[] getMenuItemsFormatted(){
        List<String> actions = new ArrayList<String>();
        NodeList menuNodeList = getClient().getMenuNodeList();
        if(menuNodeList == null){
            return new String[0];
        }
        Node headNode = menuNodeList.getHeadNode();
        if(headNode == null || headNode.getNextNode() == null){
            return new String[0];
        }
        Node currentNode = headNode.getNextNode();
        while(currentNode != null && currentNode != headNode){
            if(currentNode instanceof MenuElementNode){
                MenuElementNode menuElementNode = (MenuElementNode) currentNode;
                actions.add(menuElementNode.getAction()+" "+menuElementNode.getOption());
            }
            currentNode = currentNode.getNextNode();
        }
        if(actions.size() == 0){
            return new String[0];
        }
        else{
            return actions.toArray(new String[1]);
        }
    }

    /**
     * Gets the current menu actions.
     * @return String[] array of menu items.
     */
    public String[] getMenuItems(){
        String[] in = getMenuItemsFormatted();
        String[] out = new String[in.length];
        for(int i = 0; i < in.length; i++){
            out[i] = removeFormatting(in[i]);
        }
        return out;
    }

    /**
     * Checks if the menu is currently opened
     * @return
     */
    public boolean isOpen(){
        return getClient().isMenuOpen();
    }

    /**
     * Gets the current menu item count.
     * @return
     */
    public int getItemCount(){
        return getClient().getMenuOptionsCount();
    }

    /**
     * Removes the runescape string formatting from the parsed string
     * @param in
     * @return
     */
    public String removeFormatting(String in) {
        if(in == null)
            return "null";
        return pattern.matcher(in).replaceAll("");
    }

    /**
     * Does a click on the menu item with the given index.
     *
     * @param index the index of the menu item
     * @return false if it dis not succeed. (Normally if the menu is not open)
     */
    public boolean atMenuItem(int index){
        if(!isOpen()){
            return false;
        }

        Rectangle menuBounds = getBounds();
        final Rectangle boxBounds = new Rectangle(menuBounds.x+4, menuBounds.y+20+index*16, menuBounds.width-8, 15);
        final int targetX = random(boxBounds.x+3, boxBounds.x+boxBounds.width-6);
        final int targetY = random(boxBounds.y+3, boxBounds.y+boxBounds.height-6);

        MouseTarget target = new MouseTarget() {
            public Point get() {
                return new Point(targetX, targetY);
            }

            public boolean isOver(int posX, int posY) {
                return boxBounds.contains(posX, posY);
            }
        };


        final boolean[] succeeded = new boolean[]{false};
        MouseHoverJob mouseHoverJob = botEnv.mouse.createMouseHoverJob(new MouseMoveListener() {
            int count = 0;
            public void onMouseOverTarget(MouseJob mouseJob) {
                count++;
                if(count > random(6, 50)){
                    MouseHoverJob job = (MouseHoverJob) mouseJob;
                    job.doMouseClick(true);

                        succeeded[0] = true;
                        job.stop();

                }
            }
            
            public void onFinished(MouseJob mouseJob) {
            }
        }, target, new KTimer(3000));
        mouseHoverJob.start();
        mouseHoverJob.join();
        sleep(3);

        return succeeded[0];
    }


     /**
     * @param actionContains case insensitive string that specifies the action to be performed,
     * @return succeeded
     */
     public boolean atMenu(String actionContains){
        int idx = getMenuIndex(actionContains);
        for(int iterator = 0; iterator < 16; iterator++){
            if(idx != -1){
                break;
            }
            idx = getMenuIndex(actionContains);
            sleep(random(10, 30));
        }
        if(idx == -1){
            while(isOpen()){
                    botEnv.mouse.moveMouseRandomly(750);
					try { Thread.sleep(random(100,500));
					}
					catch (InterruptedException ie) {
					}
                if(idx != -1)
                    atMenuItem(idx);
            }
            return false; // Could not find item
        }
        if(!isOpen()){
            if(idx == 0){
                // First menu item is the same as top text so a left click will do.
                botEnv.mouse.clickMouse(true);
                return true;
            }
            else{
                // Open menu
                botEnv.mouse.clickMouse(false);
                sleep(40, 70);
            }
        }
        for(int i = 0; i < 20; i++){
            if(isOpen())
                break;
            sleep(random(10, 20));
        }
        if(!isOpen())
            return false; // Could not open menu
        idx = getMenuIndex(actionContains);
         return idx != -1 && atMenuItem(idx);
     }

    /**
     * Searches from top...
     *
     * @param actionContains
     * @return -1 if not found
     */
    public int getMenuIndex(String actionContains) {
        try {
            actionContains = actionContains.toLowerCase();
            String[] actions = getMenuItems();
            for (int i = 0; i < actions.length; i++) {
                if (actions[i].toLowerCase().contains(actionContains)) {
                    return i;
                }
            }
            return -1;
        } catch (NullPointerException ignored) {
            return -1;
        }
    }

    /**
	 * @param actionContains - Actions name to check the menu for.
	 * @return - Whether the menu contains the specified String.
	 */
	public boolean contains(String actionContains) {
		for (String s : getMenuItems()) {
			if (s.toLowerCase().contains(actionContains.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

}

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



package com.kbotpro.scriptsystem.input;

import com.kbotpro.scriptsystem.input.internal.mouse.EventFactory;
import com.kbotpro.scriptsystem.input.jobs.MouseHoverJob;
import com.kbotpro.scriptsystem.input.jobs.MouseJob;
import com.kbotpro.scriptsystem.input.callbacks.MouseMoveListener;
import com.kbotpro.scriptsystem.various.KTimer;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.scriptsystem.interfaces.Targetable;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.ui.BotPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Class to supply mouse jobs and quick similar
 */
public class Mouse extends ModuleConnector {
    public List<MouseJob> mouseJobs = Collections.synchronizedList(new ArrayList<MouseJob>());
    private Double defaultSpeed = 1.0D;

    public Mouse(BotEnvironment botEnv) {
        super(botEnv);
    }

    /**
     * Creates a MouseHoverJob with the given callback and target
     * @param callback The callback
     * @param target The target
     * @return The created mouse job, you have to start it manually
     */
    public MouseHoverJob createMouseHoverJob(MouseMoveListener callback, MouseTarget target){
        MouseHoverJob job = new MouseHoverJob(new EventFactory(botEnv), botEnv, callback, target);
        job.setSpeedMultiplier(defaultSpeed);
        return job;
    }

    /**
     * Creates a MouseHoverJob with the given callback and target 
     * @param callback The callback
     * @param targetable The target
     * @return The created mouse job, you have to start it manually
     */
    public MouseHoverJob createMouseHoverJob(MouseMoveListener callback, Targetable targetable){
        return createMouseHoverJob(callback, targetable.getTarget());
    }

    /**
     * Creates a MouseHoverJob with the given callback and target
     * @param callback The callback
     * @param target The target
     * @param runTime KTimer containing how long the job should run
     * @return The created mouse job, you have to start it manually
     */
    public MouseHoverJob createMouseHoverJob(MouseMoveListener callback, MouseTarget target, KTimer runTime){
        MouseHoverJob job = new MouseHoverJob(new EventFactory(botEnv), botEnv, callback, target);
        job.setTimeLimit(runTime);
        job.setSpeedMultiplier(defaultSpeed);
        return job;
    }

    /**
     * Creates a MouseHoverJob with the given callback and target
     * @param callback The callback
     * @param targetable The target
     * @param runTime KTimer containing how long the job should run
     * @return The created mouse job, you have to start it manually
     */
    public MouseHoverJob createMouseHoverJob(MouseMoveListener callback, Targetable targetable, KTimer runTime){
        return createMouseHoverJob(callback, targetable.getTarget(), runTime);
    }

    /**
     * Moves the mouse to the screen position and clicks the mouse if told so.
     * @param x the x screen position
     * @param y the y screen position
     * @param click Boolean, if this is true the bot will click after reaching the target.
     * @param button true =  left mouse button, false = right mouse button
     */
    public synchronized void moveMouse(final int x, final int y, final boolean click, final boolean button) {
        MouseTarget mouseTarget = new MouseTarget() {
            public Point get() {
                return new Point(x, y);
            }

            public boolean isOver(int posX, int posY) {
                return posX > x-1 && posX < x+1 && posY > y-1 && posY < y+1;
            }
        };

        MouseHoverJob mouseHoverJob = createMouseHoverJob(new MouseMoveListener() {
            int count = 0;
            public void onMouseOverTarget(MouseJob mouseJob) {
                if(!click){
                    mouseJob.stop();
                    return;
                }
                else{
                    count++;
                    if(count > random(3, 16)){
                        mouseJob.stop();
                        mouseJob.doMouseClick(button);
                        return;
                    }
                }
            }

            public void onFinished(MouseJob mouseJob) {
            }
        }, mouseTarget);
        mouseHoverJob.start();
        mouseHoverJob.join();
    }

    /**
     * Moves the mouse to the screen position given.
     * @param x the x screen position
     * @param y the y screen position
     */
    public void moveMouse(final int x, final int y) {
        moveMouse(x, y, false, false);
    }

    /**
     * Moves the mouse to the screen position and clicks the mouse btton given.
     * @param p The screen point to move to
     * @param button true =  left mouse button, false = right mouse button
     */
    public void moveMouse(Point p, final boolean button) {
        moveMouse(p.x, p.y, true, button);
    }

    /**
     * Moves the mouse to the screen position and clicks the mouse btton given.
     * @param p The screen point to move to
     */
    public void moveMouse(Point p) {
        moveMouse(p.x, p.y);
    }

    /**
     * Moves the mouse to a point inside a rectangle
     * @param r The rectangle to move the mouse to
     */
    public void moveMouse(Rectangle r) {
        moveMouse(r.x + random(0, r.width), r.y + random(0, r.height));
    }
    /**
     * VERY Human like method - great for anti bans!
     * This will move the mouse around the screen at a random distance between
     * 1 and maxDistance, but will sometimes move it more than one, like a human
     * would, resulting in cool effects like cursor circling and more.
     *
     * @param maxDistance
     * @return true if it is going to call on itself again, false otherwise
     *         (returns false to you every time)
     */
    public boolean moveMouseRandomly(int maxDistance) {
        if (maxDistance == 0) {
            return false;
        }
        maxDistance = random(1, maxDistance);
        Point p = new Point(getRandomMouseX(maxDistance), getRandomMouseY(maxDistance));
        if (p.x < 1 || p.y < 1) {
            p.x = p.y = 1;
        }
        moveMouse(p.x, p.y);
        return random(0, 2) != 0 && moveMouseRandomly(maxDistance / 2);
    }

    /**
     * Gives a X position on the screen within the maxDistance.
     *
     * @param maxDistance the maximum distance the cursor will move on the X axis
     * @return A random int that represents a X coordinate for the
     *         moveMouseRandomly method.
     */
    private int getRandomMouseX(int maxDistance) {
        Point p = getMousePos();
        if (random(0, 2) == 0) {
            return p.x - random(0, p.x < maxDistance ? p.x : maxDistance);
        } else {
            return p.x + random(1, (762 - p.x < maxDistance) ? 762 - p.x : maxDistance);
        }
    }

    /**
     * Gives a Y position on the screen within the maxDistance.
     *
     * @param maxDistance the maximum distance the cursor will move on the Y axis
     * @return A random int that represents a Y coordinate for the
     *         moveMouseRandomly method.
     */
    private int getRandomMouseY(int maxDistance) {
        Point p = getMousePos();
        if (random(0, 2) == 0) {
            return p.y - random(0, p.y < maxDistance ? p.y : maxDistance);
        } else {
            return p.y + random(1, (500 - p.y < maxDistance) ? 500 - p.y : maxDistance);
        }
    }

    /**
     * Gets the mouse position
     *
     * @return Point with coords.
     */
    public Point getMousePos() {
        return botEnv.game.getMousePos();
    }

    /**
     * Makes the mouse click the given button.
     * @param button
     */
    public void clickMouse(boolean button) {
        EventFactory eventFactory = new EventFactory(botEnv);
        Point point = getMousePos();
        MouseEvent mouseEvent = eventFactory.createMousePress(point.x, point.y, button);
        dispatchEvent(mouseEvent);
        sleep(50, 100);
        mouseEvent = eventFactory.createMouseRelease(point.x, point.y, button);
        dispatchEvent(mouseEvent);
        mouseEvent = eventFactory.createMouseClicked(point.x, point.y, button);
        dispatchEvent(mouseEvent);
    }

    /**
     * Moves the mouse to the start point, presses the mouse, moves to the destination and releases.
     * @param startPoint
     * @param destPoint
     */
    public void clickAndDragMouse(Point startPoint, final Point destPoint) {
        EventFactory eventFactory = new EventFactory(botEnv);
        MouseTarget mouseTarget = new MouseTarget() {
            public Point get() {
                return new Point(destPoint.x, destPoint.y);
            }

            public boolean isOver(int posX, int posY) {
                return posX > destPoint.x-1 && posX < destPoint.x+1 && posY > destPoint.y-1 && posY < destPoint.y+1;
            }
        };

        if (!getMousePos().equals(startPoint)) {
            moveMouse(startPoint);
        }

        MouseEvent mouseEvent = eventFactory.createMousePress(startPoint.x, startPoint.y, true);
        botEnv.dispatchEvent(mouseEvent);
        sleep(50, 80);

        MouseHoverJob mouseHoverJob = createMouseHoverJob(new MouseMoveListener() {
            int count = 0;
            public void onMouseOverTarget(MouseJob mouseJob) {
                count++;
                if(count > random(3, 16)){
                    mouseJob.stop();
                    return;
                }
            }

            public void onFinished(MouseJob mouseJob) {
            }
        }, mouseTarget);
        mouseHoverJob.setDrag(true);
        mouseHoverJob.start();
        mouseHoverJob.join();

        sleep(50, 100);
        mouseEvent = eventFactory.createMouseRelease(destPoint.x, destPoint.y, true);
        botEnv.dispatchEvent(mouseEvent);

    }

    /**
     * Only returns if the mouse is being pressed by the bot, not by user input. Use isMousePressed() instead.
     */
    public boolean mousePressed = false;


    /**
     * @return Returns true if the mouse is being held down by either the bot or the user.
     */
    public boolean isMousePressed() {
        return mousePressed || ((BotPanel.BotAppletPanel)botEnv.botPanel.botAppletPanel).mousePressed;
    }

    private void dispatchEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
            mousePressed = true;
        } else if (mouseEvent.getID() == MouseEvent.MOUSE_RELEASED) {
            mousePressed = false;
        }
        botEnv.dispatchEvent(mouseEvent);
    }

    public synchronized void pauseAllJobs(){
        for(MouseJob mouseJob: mouseJobs){
            if(mouseJob.isAlive()){
                mouseJob.pause();
            }
        }
    }

    public synchronized  void resumeAllJobs(){
        for(MouseJob mouseJob: mouseJobs){
            if(mouseJob.isPaused()){
                mouseJob.resume();
            }
        }
    }

    public synchronized void stopAllJobs() {
        for(MouseJob mouseJob: mouseJobs){
            if (mouseJob.isAlive()) {
                mouseJob.cancel();
            }
        }
    }

    public boolean isMouseActive() {
        for(MouseJob mouseJob: mouseJobs){
            if(mouseJob.isAlive() && !mouseJob.isPaused()){
                return true;
            }
        }
        return false;
    }

    /**
     * Internal method, don't use.
     * @param mouseJob
     */
    public void removeMouseJobInternally(MouseJob mouseJob) {
        mouseJobs.remove(mouseJob);
    }

    /**
     * Internal method, don't use.
     * @param mouseJob
     */
    public void addMouseJobInternally(MouseJob mouseJob) {
        mouseJobs.add(mouseJob);
    }

    /**
     * Gets the default mouse speed.
     * 1 is normal.
     * 2 is double acceleration
     * @return
     */
    public Double getDefaultSpeed() {
        return defaultSpeed;
    }

    /**
     * Sets the default mouse speed.
     * 1 is normal.
     * 2 is double acceleration
     * @param defaultSpeed
     */
    public void setDefaultSpeed(Double defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }
}

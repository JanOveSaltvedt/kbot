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



package com.kbotpro.scriptsystem.input.jobs;

import com.kbotpro.scriptsystem.input.internal.mouse.EventFactory;
import com.kbotpro.scriptsystem.input.internal.mouse.Vector2D;
import com.kbotpro.scriptsystem.input.internal.mouse.MouseForceModifier;
import com.kbotpro.scriptsystem.input.callbacks.MouseMoveListener;
import com.kbotpro.scriptsystem.interfaces.MouseTarget;
import com.kbotpro.scriptsystem.Calculations;
import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.hooks.Mouse;
import com.kbotpro.scriptsystem.various.KTimer;

import java.util.List;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * This mouse job moves the mouse to target and starts hovering.
 */
public class MouseHoverJob extends MouseJob {
    protected Vector2D velocity = new Vector2D();
    protected Vector2D lastAcceleration = new Vector2D();
    protected MouseMoveListener callback;
    protected MouseTarget target;
    protected boolean finished = false;
    protected boolean paused = false;
    protected boolean drag = false;
    protected List<MouseForceModifier> forceModifiers = new ArrayList<MouseForceModifier>();
    private double speedMultiplier = 1.0D;
    private KTimer timeLimit = null;
    
    public MouseHoverJob(EventFactory eventFactory, BotEnvironment botEnvironment, final MouseMoveListener callback, final MouseTarget target) {
        super(eventFactory, botEnvironment);
        runnable = new Runnable() {
            public void run() {
                while(!finished){
                    if (timeLimit != null && timeLimit.isDone()) {
                        break;
                    }
                    if(paused){
                        sleep(8, 10);
                        continue;
                    }
                    Mouse mouse = getMouse();
                    Point point = target.get();
                    if(point.x < 0 || point.y < 0){
                        finished = true;
                        break;
                    }
                    if(target.isOver(mouse.getMouseX(), mouse.getMouseY())){
                        callback.onMouseOverTarget(MouseHoverJob.this);
                    }
                    if(finished){
                        break;
                    }
                    double deltaTime = Calculations.random(8D, 10D)/1000D;
                    applyForces(deltaTime);
                    moveMouse(deltaTime);
                    int millis = (int) (deltaTime * 1000);
                    sleep(millis);
                }
                callback.onFinished(MouseHoverJob.this);
                botEnv.mouse.removeMouseJobInternally(MouseHoverJob.this);
            }
        };
        this.callback = callback;
        this.target = target;
        forceModifiers.add(new MouseForceModifier() {
            // TARGET GRAVITY
            public Vector2D applyForce(double deltaTime, MouseJob mouseJob) {
                Vector2D force = new Vector2D();

                Vector2D toTarget = new Vector2D();
                Point pTarget = MouseHoverJob.this.target.get();
                Mouse mouse = virtualMouse;
                toTarget.xUnits = pTarget.x-mouse.getMouseX();
                toTarget.yUnits = pTarget.y-mouse.getMouseY();
                if(toTarget.xUnits == 0 && toTarget.yUnits == 0){
                    return null;
                }

                double alpha = toTarget.getAngle();
                double acc = Calculations.random(1500, 2000)*speedMultiplier;
                force.xUnits = Math.cos(alpha)*acc;
                force.yUnits = Math.sin(alpha)*acc;

                return force;
            }
        });

        forceModifiers.add(new MouseForceModifier() {
            // "friction"
            public Vector2D applyForce(double deltaTime, MouseJob mouseJob) {
                return velocity.multiply(-1);
            }
        });

        forceModifiers.add(new MouseForceModifier() {

            private int offset = Calculations.random(300, 500);
            private double offsetAngle = -1;
            // Offset
            public Vector2D applyForce(double deltaTime, MouseJob mouseJob) {
                if(offsetAngle == -1){
                    offsetAngle = Calculations.random(-Math.PI, Math.PI);
                }
                Vector2D toTarget = new Vector2D();
                Point pTarget = MouseHoverJob.this.target.get();
                Mouse mouse = virtualMouse;
                toTarget.xUnits = pTarget.x-mouse.getMouseX();
                toTarget.yUnits = pTarget.y-mouse.getMouseY();
                if(offset > 0 && toTarget.getLength() > Calculations.random(25, 55)){
                    Vector2D force = new Vector2D();
                    force.xUnits = Math.cos(offsetAngle)*offset;
                    force.yUnits = Math.sin(offsetAngle)*offset;
                    offset -= Calculations.random(0, 6);
                    return force;
                }
                return null;
            }
        });

        forceModifiers.add(new MouseForceModifier() {
            // correction when close
            public Vector2D applyForce(double deltaTime, MouseJob mouseJob) {
                Vector2D toTarget = new Vector2D();
                Point pTarget = MouseHoverJob.this.target.get();
                Mouse mouse = virtualMouse;
                toTarget.xUnits = pTarget.x-mouse.getMouseX();
                toTarget.yUnits = pTarget.y-mouse.getMouseY();
                double length = toTarget.getLength();
                if(length < Calculations.random(75, 125)){
                    Vector2D force = new Vector2D();

                    double speed = velocity.getLength();
                    double rh = speed*speed;
                    double s = toTarget.xUnits * toTarget.xUnits + toTarget.yUnits * toTarget.yUnits;
                    if(s == 0){
                        return null;
                    }
                    double f = rh/ s;
                    f = Math.sqrt(f);
                    Vector2D adjustedToTarget = toTarget.multiply(f);

                    force.xUnits = (adjustedToTarget.xUnits-velocity.xUnits)/(deltaTime);
                    force.yUnits = (adjustedToTarget.yUnits-velocity.yUnits)/(deltaTime);

                    double v = 4D / length;
                    if(v < 1D){
                        force = force.multiply(v);
                    }
                    if(length < 10){
                        force = force.multiply(0.5D);
                    }
                    return force;
                }
                return null;
            }
        });

        forceModifiers.add(new MouseForceModifier() {
            // correction when close
            public Vector2D applyForce(double deltaTime, MouseJob mouseJob) {
                Point pTarget = MouseHoverJob.this.target.get();
                Mouse mouse = virtualMouse;
                int mouseX = mouse.getMouseX();
                int mouseY = mouse.getMouseY();
                //if(mouseX > pTarget.x-2 && mouseX < pTarget.x+2 && mouseY > pTarget.y-2 && mouseY < pTarget.y+2){
                if(mouseX == pTarget.x && mouseY == pTarget.y){
                    velocity.xUnits = 0;
                    velocity.yUnits = 0;
                }
                return null;
            }
        });
    }

    protected void applyForces(double deltaTime){
        Vector2D force = new Vector2D();
        for(MouseForceModifier modifier: forceModifiers){
            Vector2D f = modifier.applyForce(deltaTime, this);
            if(f == null){
                continue;
            }
            force.add(f);
        }

        if(Double.isNaN(force.xUnits) || Double.isNaN(force.yUnits)){
            return;
        }
        lastAcceleration = force;
        velocity.add(force.multiply(deltaTime));
    }

    protected void moveMouse(double deltaTime){
        Mouse mouse = getMouse();
        int mouseX = mouse.getMouseX();
        int mouseY = mouse.getMouseY();

        Vector2D deltaPosition = velocity.multiply(deltaTime);
        int newX = mouseX + (int) deltaPosition.xUnits;
        int newY = mouseY + (int) deltaPosition.yUnits;
        if(newX == virtualMouse.getMouseX() && newY == virtualMouse.getMouseY()){
            return;
        }
        setVirtualMousePos(newX, newY); 

        if(!paused && !finished){

            MouseEvent mouseEvent;
            if (drag) {
                mouseEvent = eventFactory.createMouseDragged(newX, newY, true);
            } else {
                mouseEvent = eventFactory.createMoveMouse(newX, newY);
            }
            dispatchEvent(mouseEvent);
        }
    }

    /**
     * Makes the bot click at the current mouse position
     * @param button
     */
    public void doMouseClick(boolean button){
        boolean wasPaused = paused;
        if(wasPaused){
            paused = true;
        }
        int mouseX = virtualMouse.getMouseX();
        int mouseY = virtualMouse.getMouseY();
        MouseEvent mouseEvent = eventFactory.createMousePress(mouseX, mouseY, button);
        dispatchEvent(mouseEvent);
        sleep(50, 100);
        mouseEvent = eventFactory.createMouseRelease(virtualMouse.getMouseX(), mouseY, button);
        dispatchEvent(mouseEvent);   
        mouseEvent = eventFactory.createMouseClicked(virtualMouse.getMouseX(), mouseY, button);
        dispatchEvent(mouseEvent);
        if(wasPaused){
            paused = false;
        }    
    }

    protected void onCancelled() {
        finished = true;
        callback.onFinished(this);
        super.botEnv.mouse.resumeAllJobs();
    }

    protected void onStart() {
        super.botEnv.mouse.pauseAllJobs();
        finished = false;
        paused = false;
    }

    public synchronized void pause(){
        paused = true;
    }

    public synchronized void resume(){
        paused = false;
    }

    public void stop() {
        finished = true;
        botEnv.mouse.removeMouseJobInternally(this);
    }

    public synchronized MouseTarget getTarget() {
        return target;
    }

    public synchronized boolean isPaused(){
        return paused;
    }

    public synchronized void setDrag(boolean drag) {
        this.drag = drag;
    }

    public synchronized void setTimeLimit(KTimer timeLimit) {
        this.timeLimit = timeLimit;
    }
    
    /**
     * 1.0 is normal speed
     * @return
     */
    public synchronized double getSpeedMultiplier() {
        return speedMultiplier;
    }

    /**
     * 1.0 is normal speed
     * @param speedMultiplier
     */
    public synchronized void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }
}

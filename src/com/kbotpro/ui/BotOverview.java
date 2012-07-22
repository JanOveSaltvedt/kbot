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

package com.kbotpro.ui;

import com.kbotpro.utils.BotControl;
import com.kbotpro.various.StaticStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;


public class BotOverview extends JPanel implements MouseMotionListener, MouseListener {
    Point mousePos = new Point(-1, -1);
    ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    ArrayList<Integer> ints = new ArrayList<Integer>();
    JPopupMenu menu;
    BotPanel currentSelection;

    public BotOverview() {
        super();
        menu = createMenu();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        };
        Timer t = new Timer();
        t.schedule(tt, 0, 400);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setBackground(Color.white);
    }

    JPopupMenu createMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem item;

        item = new JMenuItem("Close");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        if (currentSelection == null || currentSelection.botEnvironment == null) {
                            return;
                        }
                        currentSelection.destroy();
                        BotControl.bots.remove(currentSelection);
                        StaticStorage.mainForm.removeTab(currentSelection);
                    }
                }).start();
            }
        });
        menu.add(item);

        item = new JMenuItem("Stop Scripts");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        if (currentSelection == null || currentSelection.botEnvironment == null) {
                            return;
                        }
                        currentSelection.botEnvironment.scriptManager.stopAllScripts();
                    }
                }).start();
            }
        });
        menu.add(item);
        return menu;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tabCount = StaticStorage.mainForm.mainTabbedPane.getTabCount();
        if (tabCount > 1) {
            rectangles.clear();
            ints.clear();
            int totalWidth = 0;
            int totalHeight = 0;
            Rectangle mouseRect = null;
            for (int i = 0; i < tabCount; i++) {
                Component obj = StaticStorage.mainForm.mainTabbedPane.getComponentAt(i);
                if (obj instanceof BotPanel) {
                    BotPanel botPanel = (BotPanel)obj;
                    BufferedImage backBuffer = ((BotPanel.BotAppletPanel)botPanel.botAppletPanel).backBuffer;
                    int x = totalWidth;
                    int y = totalHeight;

                    int width = 229;
                    int height = 150;

                    totalWidth += width;
                    if (totalWidth > this.getBounds().width) {
                        y = totalHeight += height;
                        totalWidth = width;
                        x = 0;
                    }
                    g.drawImage(backBuffer, x, y, width, height, null);
                    Rectangle r = new Rectangle(x, y, width, height);
                    rectangles.add(r);
                    ints.add(i);
                    if (r.contains(mousePos)) {
                        mouseRect = r;
                    }
                }
            }
            if (mouseRect != null) {
                g.setColor(Color.green);
                Stroke s = ((Graphics2D)g).getStroke();
                ((Graphics2D)g).setStroke(new BasicStroke(4, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
                g.drawRect(mouseRect.x+2, mouseRect.y+2, mouseRect.width-4, mouseRect.height-4);
                ((Graphics2D)g).setStroke(s);
            }
        } else {
            g.drawString("This page will tile scaled down previews of all your currently opened bot sessions.", 2, 10);
            g.drawString("You can left click on the preview to switch to that tab.", 2, 22);
            g.drawString("You can right click to choose from the menu options.", 2, 34);
        }
    }

    public void mouseDragged(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {
        mousePos = e.getPoint();
        for (Rectangle r : rectangles) {
            if (r.contains(mousePos)) repaint();
        }
    }

    public void mouseClicked(MouseEvent e) { }

    public void processMouseEvent( MouseEvent event ) {
        int i = 0;
        for (Rectangle r : rectangles) {
            i++;
            if (r.contains(mousePos)) {
                if (event.isPopupTrigger()) {
                    currentSelection = (BotPanel)StaticStorage.mainForm.mainTabbedPane.getComponentAt(i);
                    menu.show(event.getComponent(), event.getX(), event.getY());
                } else if (event.getID() == MouseEvent.MOUSE_CLICKED && event.getButton() == MouseEvent.BUTTON1) {
                    StaticStorage.mainForm.mainTabbedPane.setSelectedIndex(i);
                }
            }

        }

        super.processMouseEvent( event );
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {
        mousePos = new Point(-1, -1);
    }
}

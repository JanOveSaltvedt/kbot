/*
 * Copyright 2005 MH-Software-Entwicklung. All rights reserved.
 * Use is subject to license terms.
 */
package com.jtattoo.plaf;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;

/**
 * @author Michael Hagen
 */
public class BaseSplitPaneDivider extends BasicSplitPaneDivider {

    public BaseSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
    }

    public Border getBorder() {
        return null;
    }

    public void paint(Graphics g) {
        int width = getSize().width;
        int height = getSize().height;
        int dx = 0;
        int dy = 0;
        if ((width % 2) == 0) {
            dx = 1;
        }
        if ((height % 2) == 0) {
            dy = 1;
        }
        Color color = AbstractLookAndFeel.getBackgroundColor();
        Color cHi = ColorHelper.brighter(color, 25);
        Color cLo = ColorHelper.darker(color, 5);
        Color colors[] = ColorHelper.createColorArr(cHi, cLo, 10);

        if (UIManager.getLookAndFeel() instanceof AbstractLookAndFeel) {
            AbstractLookAndFeel lf = (AbstractLookAndFeel) UIManager.getLookAndFeel();
            if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
                JTattooUtilities.fillVerGradient(g, colors, 0, 0, width, height);
                Icon horBumps = lf.getIconFactory().getSplitterHorBumpIcon();
                if ((horBumps != null) && (width > horBumps.getIconWidth())) {
                    Graphics2D g2D = (Graphics2D) g;
                    Composite composite = g2D.getComposite();
                    AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
                    g2D.setComposite(alpha);

                    int x = (width - horBumps.getIconWidth()) / 2 + dx;
                    int y = (height - horBumps.getIconHeight()) / 2;

                    horBumps.paintIcon(this, g, x, y);

                    g2D.setComposite(composite);
                }
            } else {
                JTattooUtilities.fillHorGradient(g, colors, 0, 0, width, height);
                Icon verBumps = lf.getIconFactory().getSplitterVerBumpIcon();
                if ((verBumps != null) && (height > verBumps.getIconHeight())) {
                    Graphics2D g2D = (Graphics2D) g;
                    Composite composite = g2D.getComposite();
                    AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
                    g2D.setComposite(alpha);

                    int x = (width - verBumps.getIconWidth()) / 2;
                    int y = (height - verBumps.getIconHeight()) / 2 + dy;

                    verBumps.paintIcon(this, g, x, y);

                    g2D.setComposite(composite);
                }
            }
        }
        paintComponents(g);
    }

    protected JButton createLeftOneTouchButton() {
        JButton b = new JButton() {

            public void paint(Graphics g) {
                Color color = getBackground();
                int w = getSize().width;
                int h = getSize().height;
                if (getModel().isPressed() && getModel().isArmed()) {
                    g.setColor(ColorHelper.darker(color, 40));
                    g.fillRect(0, 0, w, h);
                } else if (getModel().isRollover() && getModel().isArmed()) {
                    g.setColor(ColorHelper.brighter(color, 40));
                    g.fillRect(0, 0, w, h);
                }
                Icon icon = null;
                if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
                    icon = BaseIcons.getSplitterLeftArrowIcon();
                } else {
                    icon = BaseIcons.getSplitterUpArrowIcon();
                }
                icon.paintIcon(this, g, 0, 0);
                if (getModel().isRollover() && getModel().isArmed()) {
                    if (getModel().isPressed()) {
                        JTattooUtilities.draw3DBorder(g, ColorHelper.darker(color, 60), ColorHelper.brighter(color, 80), 0, 0, w, h);
                    } else {
                        JTattooUtilities.draw3DBorder(g, ColorHelper.brighter(color, 80), ColorHelper.darker(color, 60), 0, 0, w, h);
                    }
                }
            }

            public boolean isFocusTraversable() {
                return false;
            }
        };
        b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setRolloverEnabled(true);
        return b;
    }

    protected JButton createRightOneTouchButton() {
        JButton b = new JButton() {

            public void paint(Graphics g) {
                Color color = getBackground();
                int w = getSize().width;
                int h = getSize().height;
                if (getModel().isPressed() && getModel().isArmed()) {
                    g.setColor(ColorHelper.darker(color, 40));
                    g.fillRect(0, 0, w, h);
                } else if (getModel().isRollover() && getModel().isArmed()) {
                    g.setColor(ColorHelper.brighter(color, 40));
                    g.fillRect(0, 0, w, h);
                }
                Icon icon = null;
                if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
                    icon = BaseIcons.getSplitterRightArrowIcon();
                } else {
                    icon = BaseIcons.getSplitterDownArrowIcon();
                }
                icon.paintIcon(this, g, 0, 0);
                if (getModel().isRollover() && getModel().isArmed()) {
                    if (getModel().isPressed()) {
                        JTattooUtilities.draw3DBorder(g, ColorHelper.darker(color, 60), ColorHelper.brighter(color, 80), 0, 0, w, h);
                    } else {
                        JTattooUtilities.draw3DBorder(g, ColorHelper.brighter(color, 80), ColorHelper.darker(color, 60), 0, 0, w, h);
                    }
                }
            }

            public boolean isFocusTraversable() {
                return false;
            }
        };
        b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setRolloverEnabled(true);
        return b;
    }
}

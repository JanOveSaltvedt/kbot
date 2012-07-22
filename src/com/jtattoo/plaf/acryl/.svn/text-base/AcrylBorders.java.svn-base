/*
 * Copyright 2005 MH-Software-Entwicklung. All rights reserved.
 * Use is subject to license terms.
 */
package com.jtattoo.plaf.acryl;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;

import com.jtattoo.plaf.*;

/**
 * @author Michael Hagen
 */
public class AcrylBorders extends BaseBorders {

    private static Border textFieldBorder = null;
    private static Border spinnerBorder = null;
    private static Border comboBoxBorder = null;
    private static Border buttonBorder = null;
    private static Border toggleButtonBorder = null;
    private static Border rolloverToolButtonBorder = null;
    private static Border internalFrameBorder = null;

    //------------------------------------------------------------------------------------
    // Lazy access methods
    //------------------------------------------------------------------------------------
    public static Border getTextBorder() {
        if (textFieldBorder == null) {
            textFieldBorder = new TextFieldBorder();
        }
        return textFieldBorder;
    }

    public static Border getSpinnerBorder() {
        if (spinnerBorder == null) {
            spinnerBorder = new SpinnerBorder();
        }
        return spinnerBorder;
    }

    public static Border getTextFieldBorder() {
        return getTextBorder();
    }

    public static Border getComboBoxBorder() {
        if (comboBoxBorder == null) {
            comboBoxBorder = new ComboBoxBorder();
        }
        return comboBoxBorder;
    }

    public static Border getButtonBorder() {
        if (buttonBorder == null) {
            buttonBorder = new ButtonBorder();
        }
        return buttonBorder;
    }

    public static Border getToggleButtonBorder() {
        if (toggleButtonBorder == null) {
            toggleButtonBorder = new ToggleButtonBorder();
        }
        return toggleButtonBorder;
    }

    public static Border getRolloverToolButtonBorder() {
        if (rolloverToolButtonBorder == null) {
            rolloverToolButtonBorder = new RolloverToolButtonBorder();
        }
        return rolloverToolButtonBorder;
    }

    public static Border getInternalFrameBorder() {
        if (internalFrameBorder == null) {
            internalFrameBorder = new InternalFrameBorder();
        }
        return internalFrameBorder;
    }

    //------------------------------------------------------------------------------------
    // Implementation of border classes
    //------------------------------------------------------------------------------------
    public static class TextFieldBorder extends AbstractBorder implements UIResource {

        private static final Insets insets = new Insets(2, 2, 2, 2);

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color borderColor = ColorHelper.brighter(AbstractLookAndFeel.getTheme().getFrameColor(), 50);
            g.setColor(borderColor);
            g.drawRect(x, y, width - 1, height - 1);
        }

        public Insets getBorderInsets(Component c) {
            return insets;
        }
    } // class TextFieldBorder

    public static class SpinnerBorder extends AbstractBorder implements UIResource {

        private static final Insets insets = new Insets(1, 1, 1, 1);

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color borderColor = ColorHelper.brighter(AbstractLookAndFeel.getTheme().getFrameColor(), 50);
            g.setColor(borderColor);
            g.drawRect(x, y, width - 1, height - 1);
        }

        public Insets getBorderInsets(Component c) {
            return insets;
        }
    } // class SpinnerBorder

    public static class ComboBoxBorder extends AbstractBorder implements UIResource {

        private static final Insets insets = new Insets(1, 1, 1, 1);

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color borderColor = ColorHelper.brighter(AbstractLookAndFeel.getTheme().getFrameColor(), 50);
            g.setColor(borderColor);
            g.drawRect(x, y, width - 1, height - 1);
        }

        public Insets getBorderInsets(Component c) {
            return insets;
        }
    } // class ComboBoxBorder

    public static class ButtonBorder implements Border, UIResource {

        private static final Insets borderInsets = new Insets(3, 6, 3, 6);

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            Graphics2D g2D = (Graphics2D) g;
            Color frameColor = AbstractLookAndFeel.getTheme().getFrameColor();
            if (!JTattooUtilities.isFrameActive(button)) {
                frameColor = ColorHelper.brighter(frameColor, 60);
            }
            if (model.isRollover() && !model.isPressed() && !model.isArmed()) {
                frameColor = AbstractLookAndFeel.getTheme().getRolloverColorDark();
            }
            Object savedRederingHint = g2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2D.setColor(Color.white);
            g2D.drawRoundRect(x, y, w - 1, h - 1, 6, 6);

            g2D.setColor(frameColor);
            g2D.drawRoundRect(x, y, w - 2, h - 2, 6, 6);
            
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, savedRederingHint);
        }

        public Insets getBorderInsets(Component c) {
            return borderInsets;
        }

        public boolean isBorderOpaque() {
            return true;
        }
    } // class ButtonBorder

    public static class ToggleButtonBorder implements Border, UIResource {

        private static final Insets insets = new Insets(3, 6, 3, 6);

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            if (model.isEnabled()) {
                Color frameHiColor1 = Color.white;
                Color frameHiColor2 = ColorHelper.brighter(AcrylLookAndFeel.getFrameColor(), 80);
                Color frameLoColor1 = ColorHelper.brighter(AcrylLookAndFeel.getFrameColor(), 40);
                Color frameLoColor2 = ColorHelper.brighter(AcrylLookAndFeel.getFrameColor(), 20);
                if ((model.isPressed() && model.isArmed()) || model.isSelected()) {
                    JTattooUtilities.draw3DBorder(g, frameLoColor1, frameHiColor1, x + 1, y + 1, w - 3, h - 3);
                    JTattooUtilities.draw3DBorder(g, frameLoColor2, frameLoColor1, x + 1, y + 1, w - 2, h - 2);
                } else {
                    JTattooUtilities.draw3DBorder(g, frameHiColor2, frameHiColor1, x, y, w, h);
                    JTattooUtilities.draw3DBorder(g, ColorHelper.brighter(AcrylLookAndFeel.getFrameColor(), 20), AcrylLookAndFeel.getFrameColor(), x + 1, y + 1, w - 2, h - 2);
                }
            } else {
                JTattooUtilities.drawBorder(g, Color.lightGray, x, y, w, h);
            }
        }

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public boolean isBorderOpaque() {
            return true;
        }
    } // class ToggleButtonBorder

    public static class RolloverToolButtonBorder implements Border, UIResource {

        private static final Insets insets = new Insets(2, 2, 2, 2);

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            if (model.isEnabled()) {
                if ((model.isPressed() && model.isArmed()) || model.isSelected()) {
                    Color frameColor = ColorHelper.darker(AbstractLookAndFeel.getToolbarBackgroundColor(), 30);
                    g.setColor(frameColor);
                    g.drawRect(x, y, w - 1, h - 1);
                    Graphics2D g2D = (Graphics2D) g;
                    Composite composite = g2D.getComposite();
                    AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
                    g2D.setComposite(alpha);
                    g.setColor(Color.black);
                    g.fillRect(x + 1, y + 1, w - 2, h - 2);
                    g2D.setComposite(composite);
                } else if (model.isRollover()) {
                    Color frameColor = AbstractLookAndFeel.getToolbarBackgroundColor();
                    Color frameHiColor = ColorHelper.darker(frameColor, 5);
                    Color frameLoColor = ColorHelper.darker(frameColor, 30);
                    JTattooUtilities.draw3DBorder(g, frameHiColor, frameLoColor, x, y, w, h);
                    frameHiColor = Color.white;
                    frameLoColor = ColorHelper.brighter(frameLoColor, 60);
                    JTattooUtilities.draw3DBorder(g, frameHiColor, frameLoColor, x + 1, y + 1, w - 2, h - 2);

                    Graphics2D g2D = (Graphics2D) g;
                    Composite composite = g2D.getComposite();
                    AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
                    g2D.setComposite(alpha);
                    g.setColor(Color.white);
                    g.fillRect(x + 2, y + 2, w - 4, h - 4);
                    g2D.setComposite(composite);
                } else if (model.isSelected()) {
                    Color frameColor = AbstractLookAndFeel.getToolbarBackgroundColor();
                    Color frameHiColor = Color.white;
                    Color frameLoColor = ColorHelper.darker(frameColor, 30);
                    JTattooUtilities.draw3DBorder(g, frameLoColor, frameHiColor, x, y, w, h);
                }
            }
        }

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public boolean isBorderOpaque() {
            return true;
        }
    } // class RolloverToolButtonBorder

    public static class InternalFrameBorder extends BaseInternalFrameBorder {

        public InternalFrameBorder() {
            insets.top = 3;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            boolean active = isActive(c);
            int th = getTitleHeight(c);
            Color titleColor = AcrylLookAndFeel.getWindowInactiveTitleColorLight();
            Color borderColor = AcrylLookAndFeel.getWindowInactiveTitleColorLight();
            Color frameColor = ColorHelper.darker(AcrylLookAndFeel.getWindowInactiveBorderColor(), 10);
            if (active) {
                titleColor = AcrylLookAndFeel.getWindowTitleColorLight();
                borderColor = AcrylLookAndFeel.getWindowTitleColorLight();
                frameColor = ColorHelper.darker(AcrylLookAndFeel.getWindowBorderColor(), 10);
            }
            g.setColor(titleColor);
            g.fillRect(x, y + 1, w, insets.top - 1);
            g.setColor(borderColor);
            g.fillRect(x + 1, y + h - dw, w - 2, dw - 1);

            if (active) {
                JTattooUtilities.fillHorGradient(g, AcrylLookAndFeel.getTheme().getWindowTitleColors(), 1, insets.top, dw, th + 1);
                JTattooUtilities.fillHorGradient(g, AcrylLookAndFeel.getTheme().getWindowTitleColors(), w - dw, insets.top, dw, th + 1);
            } else {
                JTattooUtilities.fillHorGradient(g, AcrylLookAndFeel.getTheme().getWindowInactiveTitleColors(), 1, insets.top, dw - 1, th + 1);
                JTattooUtilities.fillHorGradient(g, AcrylLookAndFeel.getTheme().getWindowInactiveTitleColors(), w - dw, insets.top, dw - 1, th + 1);
            }
            g.setColor(borderColor);
            g.fillRect(1, insets.top + th + 1, dw - 1, h - th - dw);
            g.fillRect(w - dw, insets.top + th + 1, dw - 1, h - th - dw);

            g.setColor(frameColor);
            g.drawRect(x, y, w - 1, h - 1);
            g.drawLine(x + dw - 1, y + insets.top + th, x + dw - 1, y + h - dw);
            g.drawLine(x + w - dw, y + insets.top + th, x + w - dw, y + h - dw);
            g.drawLine(x + dw - 1, y + h - dw, x + w - dw, y + h - dw);
        }
    } // class InternalFrameBorder
} // class AcrylBorders


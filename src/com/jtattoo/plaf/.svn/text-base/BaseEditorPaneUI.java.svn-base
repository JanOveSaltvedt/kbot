/*
 * Copyright 2005 MH-Software-Entwicklung. All rights reserved.
 * Use is subject to license terms.
 */
package com.jtattoo.plaf;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.JTextComponent;

/**
 * @author Michael Hagen
 */
public class BaseEditorPaneUI extends BasicEditorPaneUI {

    public static ComponentUI createUI(JComponent c) {
        return new BaseEditorPaneUI();
    }

    public void installDefaults() {
        super.installDefaults();
        updateBackground();
    }

    private void updateBackground() {
        JTextComponent c = getComponent();
        if (c.getBackground() instanceof UIResource) {
            if (!c.isEnabled() || !c.isEditable()) {
                c.setBackground(AbstractLookAndFeel.getDisabledBackgroundColor());
            } else {
                c.setBackground(AbstractLookAndFeel.getInputBackgroundColor());
            }
        }
    }
}

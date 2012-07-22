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

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.interfaces.HTMLDescription;
import com.kbotpro.scriptsystem.runnable.Script;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jan Ove
 */
public class ArgumentGetter extends JFrame {
	private final HTMLDescription script;
	private final BotEnvironment botEnvironment;
    private boolean argumentsSet = false;

    public ArgumentGetter(HTMLDescription script, BotEnvironment botEnvironment) {
		this.script = script;
		this.botEnvironment = botEnvironment;
		initComponents();
		if(script instanceof Script){
			setTitle(((Script)script).getName());
		}
		editorPane.setContentType("text/plain");
		editorPane.setContentType("text/html");
		editorPane.setText(script.getDocument());
		editorPane.updateUI();
		editorPane.setEditable(false);
		try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(new URL("http://i28.tinypic.com/feo0zq.jpg")));
		} catch (Exception e) {
		}
		setVisible(true);
	}

    public void waitFor(){
        while(!argumentsSet){
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

	/**
	 * Internal method to get the arguments out of the form tags in the document.
	 * @return
	 */
	private Map<String, String> getArguments() {
		final HTMLDocument htmlDoc = (HTMLDocument) editorPane.getDocument();
		final Map<String, String> args = new HashMap<String, String>();
		for (final Element elem : htmlDoc.getRootElements()) {
			getArguments(args, elem);
		}
		return args;
	}

	/**
	 * Internal method to get the arguments out of the form tags in the document.
	 * @param args
	 * @param elem
	 */
	private void getArguments(final Map<String, String> args, final Element elem) {
		final int len = elem.getElementCount();
		if (elem.getName().equalsIgnoreCase("input")
				|| elem.getName().equalsIgnoreCase("select")) {
			final AttributeSet as = elem.getAttributes();
			final Object model = as.getAttribute(StyleConstants.ModelAttribute);
			final String name = as.getAttribute(HTML.Attribute.NAME).toString();
			if (model instanceof PlainDocument) {
				final PlainDocument pd = (PlainDocument) model;
				String value = null;
				try {
					value = pd.getText(0, pd.getLength());
				} catch (final BadLocationException e) {
					Logger.getRootLogger().error("Exception: ", e);
				}
				args.put(name, value);
			} else if (model instanceof JToggleButton.ToggleButtonModel) {
				final JToggleButton.ToggleButtonModel buttonModel = (JToggleButton.ToggleButtonModel) model;
				if (!args.containsKey(name)) {
					args.put(name, null);
				}
				if (buttonModel.isSelected()) {
					args.put(name, as.getAttribute(HTML.Attribute.VALUE)
							.toString());
				}
			} else if (model instanceof DefaultComboBoxModel) {
				args.put(name, ((DefaultComboBoxModel) model).getSelectedItem()
						.toString());
			} else {
				throw new Error("Unknown model [" + model.getClass().getName()
						+ "]");
			}
		}
		for (int i = 0; i < len; i++) {
			final Element e = elem.getElement(i);
			getArguments(args, e);
		}
	}

	private void canceklButtonActionPerformed() {
		dispose();
	}

	private void startButtonActionPerformed() {
		Map<String,  String> map = getArguments();
		script.setArguments(map);
		argumentsSet = true;
		dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		startButton = new JButton();
		scrollPane1 = new JScrollPane();
		editorPane = new JEditorPane();
		canceklButton = new JButton();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();

		//---- startButton ----
		startButton.setText("Start script");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startButtonActionPerformed();
			}
		});

		//======== scrollPane1 ========
		{

			//---- editorPane ----
			editorPane.setContentType("text/html");
			scrollPane1.setViewportView(editorPane);
		}

		//---- canceklButton ----
		canceklButton.setText("Cancel");
		canceklButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceklButtonActionPerformed();
			}
		});

		org.jdesktop.layout.GroupLayout contentPaneLayout = new org.jdesktop.layout.GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.add(scrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
				.add(org.jdesktop.layout.GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
					.addContainerGap(514, Short.MAX_VALUE)
					.add(startButton)
					.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
					.add(canceklButton)
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.add(org.jdesktop.layout.GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
					.add(scrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
					.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
					.add(contentPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
						.add(canceklButton)
						.add(startButton))
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JButton startButton;
	private JScrollPane scrollPane1;
	private JEditorPane editorPane;
	private JButton canceklButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}


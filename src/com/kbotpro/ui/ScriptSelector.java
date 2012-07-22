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

/*
 * Created by JFormDesigner on Mon Nov 02 19:58:47 CET 2009
 */

package com.kbotpro.ui;

import com.kbotpro.handlers.ScriptMetaDataManager;
import com.kbotpro.various.ScriptMetaData;
import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Ove
 */
public class ScriptSelector extends JFrame implements ListSelectionListener {
    private BotPanel botPanel;

    public ScriptSelector(BotPanel botPanel) {
        this.botPanel = botPanel;
        initComponents();

        completeUpdateList();
        scriptTable.getSelectionModel().addListSelectionListener(this);

    }

    @Override
    public void toFront() {
        int state = super.getExtendedState();
        state &= ~JFrame.ICONIFIED;
        super.setExtendedState(state);
        super.setAlwaysOnTop(true);
        super.toFront();
        super.requestFocus();
        super.setAlwaysOnTop(false);
    }


    private void completeUpdateList() {
        final boolean[] loadedScripts = new boolean[]{false};
        new Thread(new Runnable() {
            public void run() {
                ScriptMetaDataManager.loadScriptMetaData();
                loadedScripts[0] = true;
            }
        }).start();
        setEnabled(false);
        loadingDialog.setVisible(true);
        toFront();
        requestFocus();
        new Thread(new Runnable() {
            public void run() {
                while (!loadedScripts[0]) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                loadingDialog.setVisible(false);
                setVisible(true);
                setEnabled(true);
                toFront();

                updateList();

            }
        }).start();


    }

    private void updateList() {
        //Vector<String> categories = new Vector<String>();

        List<ScriptMetaData> scriptMetaDataList = ScriptMetaDataManager.loadedScriptMetaData;
        List<Object[]> data = new ArrayList<Object[]>();
        for (ScriptMetaData scriptMetaData : scriptMetaDataList) {
            if (filterCategoryCheckBoc.isSelected()) {

                if (filterCatergoyCombo.getSelectedItem() != null && !filterCatergoyCombo.getSelectedItem().equals(scriptMetaData.category)) {
                    continue;
                }
            }
            if(!showUnverifiedScriptsCheckBox.isSelected() && !scriptMetaData.isTrusted()){
                continue;
            }
            if(!searchTextBox.getText().equals("")){
                boolean found = false;
                if(scriptMetaData.name.toLowerCase().contains(searchTextBox.getText().toLowerCase())){
                    found = true;
                }
                else if(scriptMetaData.author.toLowerCase().contains(searchTextBox.getText().toLowerCase())){
                    found = true;
                }
                else if(scriptMetaData.description.toLowerCase().contains(searchTextBox.getText().toLowerCase())){
                    found = true;
                }

                if(!found){
                    continue;
                }
            }
            if (!((filterRegularCheckBox.isSelected() && scriptMetaData.type.equalsIgnoreCase("regular"))
                    || (filterProCheckBox.isSelected() && scriptMetaData.type.equalsIgnoreCase("pro")))) {
                continue;
            }
            Object columns[] = new Object[1];

            //columns[0] = scriptMetaData.type.equalsIgnoreCase("pro") ? "P" : "R";
            columns[0] = scriptMetaData;
            if (sortComboBox.getSelectedItem().equals("Name")) {
                int pos = 0;
                for (Object[] node : data) {
                    ScriptMetaData scriptMetaData2 = (ScriptMetaData) node[0];
                    if (scriptMetaData.name.compareToIgnoreCase(scriptMetaData2.name) <= 0) {
                        break;
                    }
                    pos++;
                }
                data.add(pos, columns);
            } else if (sortComboBox.getSelectedItem().equals("Author")) {
                int pos = 0;
                for (Object[] node : data) {
                    ScriptMetaData scriptMetaData2 = (ScriptMetaData) node[0];
                    if (scriptMetaData.author.compareToIgnoreCase(scriptMetaData2.author) <= 0) {
                        break;
                    }
                    pos++;
                }
                data.add(pos, columns);
            } else if (sortComboBox.getSelectedItem().equals("Downloads")) {
                int pos = 0;
                for (Object[] node : data) {
                    ScriptMetaData scriptMetaData2 = (ScriptMetaData) node[0];
                    if (scriptMetaData.downloads >= scriptMetaData2.downloads) {
                        break;
                    }
                    pos++;
                }
                data.add(pos, columns);
            } else {
                data.add(columns);
            }
           /* boolean foundCategory = false;
            for (String category : categories) {
                if (category.equals(scriptMetaData.category)) {
                    foundCategory = true;
                }
            }
            if (!foundCategory) {
                categories.add(scriptMetaData.category);
            }    */

        }
        scriptTable.setModel(new DefaultTableModel(
                data.toArray(new Object[data.size()][1]),
                new String[]{
                        /*"T", */"Name"
                }
        ) {
            Class[] columnTypes = new Class[]{
                    /*String.class, */Object.class
            };
            boolean[] columnEditable = new boolean[]{
                    /*false, */false

            };

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnEditable[columnIndex];
            }
        });
        {
            TableColumnModel cm = scriptTable.getColumnModel();
        }
        //filterCatergoyCombo.setModel(new DefaultComboBoxModel(categories));
    }

    private void filterProCheckBoxItemStateChanged(ItemEvent e) {
        updateList();
    }

    private void filterRegularCheckBoxItemStateChanged(ItemEvent e) {
        updateList();
    }

    private void filterCategoryCheckBocItemStateChanged(ItemEvent e) {
        updateList();
    }

    private void filterCatergoyComboActionPerformed(ActionEvent e) {
        updateList();
    }

    private void sortComboBoxActionPerformed(ActionEvent e) {
        updateList();
    }

    private void startButtonActionPerformed(ActionEvent e) {
        int row = scriptTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a script first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

       final ScriptMetaData scriptMetaData = (ScriptMetaData) scriptTable.getModel().getValueAt(row, 0);
        //ScriptMetaData scriptMetaData = (ScriptMetaData) scriptTable.getModel().getValueAt(row, 0);
       new Thread(new Runnable() {
           public void run() {
                botPanel.botEnvironment.scriptManager.startScript(scriptMetaData);
                dispose();
           }

       }, "Script download").start();
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        dispose();
    }

    private void searchTextBoxKeyTyped(KeyEvent e) {
        updateList();
    }

    private void descriptionTextPanelHyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType().toString().equals("ACTIVATED")){
            URL url = e.getURL();
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (URISyntaxException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void showUnverifiedScriptsCheckBoxItemStateChanged(ItemEvent e) {
        updateList();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        filterCategoryCheckBoc = new JCheckBox();
        filterCatergoyCombo = new JComboBox();
        filterRegularCheckBox = new JCheckBox();
        filterProCheckBox = new JCheckBox();
        separator1 = new JSeparator();
        label3 = new JLabel();
        searchTextBox = new JTextField();
        showUnverifiedScriptsCheckBox = new JCheckBox();
        splitPane1 = new JSplitPane();
        scrollPane1 = new JScrollPane();
        scriptTable = new JTable();
        panel3 = new JPanel();
        scrollPane3 = new JScrollPane();
        infoLabel = new JLabel();
        scrollPane2 = new JScrollPane();
        descriptionTextPanel = new JTextPane();
        panel2 = new JPanel();
        label1 = new JLabel();
        sortComboBox = new JComboBox();
        cancelButton = new JButton();
        startButton = new JButton();
        loadingDialog = new Dialog(this);
        label2 = new JLabel();
        progressBar1 = new JProgressBar();

        //======== this ========
        setTitle("Script Selector");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        
        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("Filter"));

            //---- filterCategoryCheckBoc ----
            filterCategoryCheckBoc.setText("Category");
            filterCategoryCheckBoc.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    filterCategoryCheckBocItemStateChanged(e);
                }
            });

            //---- filterCatergoyCombo ----
            filterCatergoyCombo.setModel(new DefaultComboBoxModel(new String[] {
                    "Combat",
                    "Cooking",
                    "Crafting",
                    "Firemaking",
                    "Fishing",
                    "Fletching",
                    "Hunting",
                    "Magic",
                    "Mining",
                    "Money Making",
                    "Other",
                    "Runecrafting",
                    "Smithing",
                    "Thieving",
                    "Unknown",
                    "Woodcutting",

            }));
            filterCatergoyCombo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    filterCatergoyComboActionPerformed(e);
                }
            });

            //---- filterRegularCheckBox ----
            filterRegularCheckBox.setText("Regular Scripts");
            filterRegularCheckBox.setSelected(true);
            filterRegularCheckBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    filterRegularCheckBoxItemStateChanged(e);
                }
            });

            //---- filterProCheckBox ----
            filterProCheckBox.setText("PRO Scripts");
            filterProCheckBox.setSelected(true);
            filterProCheckBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    filterProCheckBoxItemStateChanged(e);
                }
            });

            //---- separator1 ----
            separator1.setOrientation(SwingConstants.VERTICAL);

            //---- label3 ----
            label3.setText("Search:");
            label3.setLabelFor(searchTextBox);

            //---- searchTextBox ----
            searchTextBox.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    searchTextBoxKeyTyped(e);
                }
            });

            //---- showUnverifiedScriptsCheckBox ----
            showUnverifiedScriptsCheckBox.setText("Show unverified scripts");
            showUnverifiedScriptsCheckBox.setSelected(true);
            showUnverifiedScriptsCheckBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    showUnverifiedScriptsCheckBoxItemStateChanged(e);
                }
            });

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .add(panel1Layout.createSequentialGroup()
                        .add(panel1Layout.createParallelGroup()
                            .add(panel1Layout.createSequentialGroup()
                                .add(filterCategoryCheckBoc)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(filterCatergoyCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .add(panel1Layout.createSequentialGroup()
                                .add(filterRegularCheckBox)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(filterProCheckBox))
                            .add(showUnverifiedScriptsCheckBox))
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(separator1, GroupLayout.PREFERRED_SIZE, 12, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(panel1Layout.createParallelGroup()
                            .add(searchTextBox, GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                            .add(label3))
                        .addContainerGap())
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .add(panel1Layout.createSequentialGroup()
                        .add(panel1Layout.createParallelGroup()
                            .add(panel1Layout.createSequentialGroup()
                                .add(panel1Layout.createParallelGroup(GroupLayout.BASELINE)
                                    .add(filterCategoryCheckBoc)
                                    .add(filterCatergoyCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(panel1Layout.createParallelGroup(GroupLayout.BASELINE)
                                    .add(filterRegularCheckBox)
                                    .add(filterProCheckBox))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(showUnverifiedScriptsCheckBox))
                            .add(separator1, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .add(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(label3)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(searchTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
            );
        }

        //======== splitPane1 ========
        {
            splitPane1.setResizeWeight(0.25);

            //======== scrollPane1 ========
            {

                //---- scriptTable ----
                scriptTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                scriptTable.setModel(new DefaultTableModel(
                    new Object[][] {
                        {"test"},
                        {null},
                    },
                    new String[] {
                        "Name"
                    }
                ) {
                    Class[] columnTypes = new Class[] {
                        Object.class
                    };
                    boolean[] columnEditable = new boolean[] {
                        false
                    };
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return columnTypes[columnIndex];
                    }
                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return columnEditable[columnIndex];
                    }
                });
                scriptTable.setShowVerticalLines(false);
                scriptTable.setPreferredScrollableViewportSize(new Dimension(200, 400));
                scrollPane1.setViewportView(scriptTable);
            }
            splitPane1.setLeftComponent(scrollPane1);

            //======== panel3 ========
            {
                panel3.setBorder(null);

                //======== scrollPane3 ========
                {

                    //---- infoLabel ----
                    infoLabel.setText("Please select a script");
                    scrollPane3.setViewportView(infoLabel);
                }

                //======== scrollPane2 ========
                {

                    //---- descriptionTextPanel ----
                    descriptionTextPanel.setContentType("text/html");
                    descriptionTextPanel.setText("<html>\r   <head>\r \r   </head>\r   <body>\r Please select a script  </body>\r </html>\r ");
                    descriptionTextPanel.setEditable(false);
                    descriptionTextPanel.addHyperlinkListener(new HyperlinkListener() {
                        public void hyperlinkUpdate(HyperlinkEvent e) {
                            descriptionTextPanelHyperlinkUpdate(e);
                        }
                    });
                    scrollPane2.setViewportView(descriptionTextPanel);
                }

                GroupLayout panel3Layout = new GroupLayout(panel3);
                panel3.setLayout(panel3Layout);
                panel3Layout.setHorizontalGroup(
                    panel3Layout.createParallelGroup()
                        .add(GroupLayout.TRAILING, panel3Layout.createSequentialGroup()
                            .addContainerGap()
                            .add(panel3Layout.createParallelGroup(GroupLayout.TRAILING)
                                .add(GroupLayout.LEADING, scrollPane2, GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                                .add(GroupLayout.LEADING, scrollPane3, GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE))
                            .addContainerGap())
                );
                panel3Layout.setVerticalGroup(
                    panel3Layout.createParallelGroup()
                        .add(panel3Layout.createSequentialGroup()
                            .addContainerGap()
                            .add(scrollPane3, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(scrollPane2, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                            .addContainerGap())
                );
            }
            splitPane1.setRightComponent(panel3);
        }

        //======== panel2 ========
        {
            panel2.setBorder(new TitledBorder("Sort"));

            //---- label1 ----
            label1.setText("Sort by");

            //---- sortComboBox ----
            sortComboBox.setModel(new DefaultComboBoxModel(new String[] {
                "Name",
                "Author",
                "Downloads"
            }));
            sortComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sortComboBoxActionPerformed(e);
                }
            });

            GroupLayout panel2Layout = new GroupLayout(panel2);
            panel2.setLayout(panel2Layout);
            panel2Layout.setHorizontalGroup(
                panel2Layout.createParallelGroup()
                    .add(panel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(label1)
                        .addPreferredGap(LayoutStyle.UNRELATED)
                        .add(sortComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(25, Short.MAX_VALUE))
            );
            panel2Layout.setVerticalGroup(
                panel2Layout.createParallelGroup()
                    .add(panel2Layout.createSequentialGroup()
                        .add(19, 19, 19)
                        .add(panel2Layout.createParallelGroup(GroupLayout.BASELINE)
                            .add(label1)
                            .add(sortComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(32, Short.MAX_VALUE))
            );
        }

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed(e);
            }
        });

        //---- startButton ----
        startButton.setText("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButtonActionPerformed(e);
            }
        });

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .add(GroupLayout.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(contentPaneLayout.createParallelGroup(GroupLayout.TRAILING)
                        .add(GroupLayout.LEADING, splitPane1, GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                        .add(contentPaneLayout.createSequentialGroup()
                            .add(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.RELATED)
                            .add(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .add(contentPaneLayout.createSequentialGroup()
                            .add(startButton)
                            .addPreferredGap(LayoutStyle.UNRELATED)
                            .add(cancelButton)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .add(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(contentPaneLayout.createParallelGroup(GroupLayout.LEADING, false)
                        .add(panel1, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .add(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(splitPane1, GroupLayout.PREFERRED_SIZE, 416, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(contentPaneLayout.createParallelGroup(GroupLayout.BASELINE)
                        .add(cancelButton)
                        .add(startButton))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());

        //======== loadingDialog ========
        {
            loadingDialog.setTitle("Loading...");
            loadingDialog.setAlwaysOnTop(true);
            loadingDialog.setResizable(false);

            //---- label2 ----
            label2.setText("Loading script list...");

            //---- progressBar1 ----
            progressBar1.setIndeterminate(true);

            GroupLayout loadingDialogLayout = new GroupLayout(loadingDialog);
            loadingDialog.setLayout(loadingDialogLayout);
            loadingDialogLayout.setHorizontalGroup(
                loadingDialogLayout.createParallelGroup()
                    .add(loadingDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(loadingDialogLayout.createParallelGroup()
                            .add(label2)
                            .add(progressBar1, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
                        .addContainerGap())
            );
            loadingDialogLayout.setVerticalGroup(
                loadingDialogLayout.createParallelGroup()
                    .add(loadingDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(label2)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(progressBar1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            loadingDialog.pack();
            loadingDialog.setLocationRelativeTo(loadingDialog.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - josh hurr
    private JPanel panel1;
    private JCheckBox filterCategoryCheckBoc;
    private JComboBox filterCatergoyCombo;
    private JCheckBox filterRegularCheckBox;
    private JCheckBox filterProCheckBox;
    private JSeparator separator1;
    private JLabel label3;
    private JTextField searchTextBox;
    private JCheckBox showUnverifiedScriptsCheckBox;
    private JSplitPane splitPane1;
    private JScrollPane scrollPane1;
    private JTable scriptTable;
    private JPanel panel3;
    private JScrollPane scrollPane3;
    private JLabel infoLabel;
    private JScrollPane scrollPane2;
    private JTextPane descriptionTextPanel;
    private JPanel panel2;
    private JLabel label1;
    private JComboBox sortComboBox;
    private JButton cancelButton;
    private JButton startButton;
    private Dialog loadingDialog;
    private JLabel label2;
    private JProgressBar progressBar1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged(ListSelectionEvent e) {
        int row = scriptTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        ScriptMetaData scriptMetaData = (ScriptMetaData) scriptTable.getModel().getValueAt(row, 0);
        descriptionTextPanel.setText(scriptMetaData.description);
        descriptionTextPanel.updateUI();
        infoLabel.setText("<html><body>" +
                "Author: "+scriptMetaData.author +
                "<br>Type: "+scriptMetaData.type +
                "<br>Version: "+scriptMetaData.version +
                "<br>Verified: "+scriptMetaData.isTrusted() +
                "</body></html>");
    }
}

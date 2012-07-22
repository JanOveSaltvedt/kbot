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



package com.kbotpro.handlers;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.handlers.kbotscriptsystem.KBot2Script;
import com.kbotpro.scriptsystem.Methods;
import com.kbotpro.scriptsystem.runnable.Script;
import com.kbotpro.scriptsystem.runnable.Service;
import com.kbotpro.scriptsystem.runnable.ServiceCallback;
import com.kbotpro.servercom.ServerCom;
import com.kbotpro.ui.ErrorBox;
import com.kbotpro.ui.MainForm;
import com.kbotpro.ui.ProgressUI;
import com.kbotpro.utils.ProgressCallback;
import com.kbotpro.various.ScriptAnalyzer;
import com.kbotpro.various.ScriptClassLoader;
import com.kbotpro.various.ScriptMetaData;
import com.kbotpro.various.StaticStorage;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Nov 3, 2009
 * Time: 6:57:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptManager implements ServiceCallback {
    private BotEnvironment bot;
    public List<Script> runningScripts = Collections.synchronizedList(new ArrayList<Script>());
    private boolean askForAccount = true;

    public ScriptManager(BotEnvironment bot) {
        this.bot = bot;
    }

    public void startScript(final ScriptMetaData scriptMetaData) {
        if (!scriptMetaData.isTrusted()) {
            if (JOptionPane.showConfirmDialog(null, "This script is not verified.\nThe script may contain malicious code.\nDo you still want to run the script?", "Unverified script!", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
        }
        if (scriptMetaData.isProScript() && !StaticStorage.userStorage.canRunProScripts()) {
            JOptionPane.showMessageDialog(null, "You don't have privileges to run KBot PRO scripts", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ProgressCallback progressCallback = new ProgressCallback() {
            int counter = 0;
            ProgressUI ui = new ProgressUI(StaticStorage.mainForm);

            public void update(int pos, int length) {
                counter++;
                if (counter > 150) {
                    counter = 0;
                    if (ui.isVisible()) {
                        ui.setVisible(true);
                        if (length == -1) {
                            ui.progressBar.setIndeterminate(true);
                        } else {
                            ui.progressBar.setValue((pos * 100) / length);
                        }
                    }
                }
            }

            public void onComplete() {
                ui.dispose();
            }
        };

        ServerCom serverCom = StaticStorage.serverCom;
        byte[] data = serverCom.downloadScript(scriptMetaData.ID, progressCallback);
        //---
        /*if (askForAccount && !bot.accountsHandler.isAccountSelected()) {
            if (!bot.accountsHandler.showAccountSelector()) {
                JOptionPane.showMessageDialog(null, "Can not use accounts, auto login disabled.");
            }
            askForAccount = false;
        }         */
        //--

        JarInputStream zipInputStream = null;

        startScript(data, true, scriptMetaData.permissionExceptions);


    }

    public void startScript(final byte[] data, final boolean loadedFromServer, final List<Permission> permissionExceptions) {

        new Thread(new Runnable() {

            public void run() {
                try {
                    JarInputStream jarInputStream = new JarInputStream(new ByteArrayInputStream(data));
                    ScriptAnalyzer scriptAnalyzer = new ScriptAnalyzer(jarInputStream);
                    if (scriptAnalyzer.usesKBotPROPackages() && !StaticStorage.userStorage.canRunProScripts()) {
                        JOptionPane.showMessageDialog(null, "You don't have privileges to run KBot PRO scripts", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    /*if(scriptAnalyzer.usesRuntime()){
                        JOptionPane.showMessageDialog(null, "This script uses methods that may be used for malicious purposes.\n KBot will not run this script.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }*/

                    jarInputStream = new JarInputStream(new ByteArrayInputStream(data));
                    try {
                        ScriptClassLoader classLoader = new ScriptClassLoader(jarInputStream, loadedFromServer, permissionExceptions);
                        Class klass = classLoader.loadMainClass();
                        if (klass == null) {
                            return;
                        }
                        Object scriptObject = klass.newInstance();
                        Script script;
                        if (scriptObject instanceof com.kbot2.scriptable.Script) {
                            script = new KBot2Script((com.kbot2.scriptable.Script) scriptObject, bot);
                        } else {
                            script = (Script) scriptObject;
                        }
                        Method registeMethod = Methods.class.getDeclaredMethod("registerInternal", new Class<?>[]{BotEnvironment.class});
                        registeMethod.setAccessible(true);    // This method is private so we set it by a little reflection hack
                        registeMethod.invoke(script, bot);
                        Method getService = Script.class.getDeclaredMethod("getService");
                        getService.setAccessible(true);
                        Service service = (Service) getService.invoke(script);
                        service.setCallback(ScriptManager.this);
                        service.sStart();
                        jarInputStream.close();
                        return;
                    } catch (IllegalAccessException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                        outputError(e);
                    } catch (InstantiationException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                        outputError(e);
                    } catch (InvocationTargetException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                        outputError(e);
                    } catch (NoSuchMethodException e) {
                        Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                        outputError(e);
                    } catch (NoClassDefFoundError e) {
                        Logger.getRootLogger().error("Exception: ", e);
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        return;
                    }
                    try {
                        jarInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } catch (IOException e) {
                    Logger.getRootLogger().error("Exception: ", e);
                }
            }
        }).start();

    }

    public void outputError(Throwable throwable) {
        final String[] message = new String[]{""};
        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                message[0] += (char) b;
            }
        });
        throwable.printStackTrace(printStream);
        new ErrorBox(StaticStorage.mainForm, "An error occured:\n\n" + message[0]).setVisible(true);
    }

    /**
     * Callback to update the service state.
     *
     * @param state    state to be set
     * @param instance
     */
    public synchronized void setState(State state, Object instance) {
        if (state == State.ACTIVE) {
            Script script = (Script) instance;
            bot.log.log("Started script " + script.getName());
            runningScripts.add(script);
        } else if (state == State.DEAD) {
            Script script = (Script) instance;
            if (runningScripts.contains(script)) {
                bot.log.log("Stopped script " + script.getName());
                try {
                    Method deregisterMethod = Methods.class.getDeclaredMethod("deregisterInternal");
                    deregisterMethod.setAccessible(true);    // This method is private so we set it by a little reflection hack
                    deregisterMethod.invoke(script);
                } catch (NoSuchMethodException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                } catch (InvocationTargetException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
                }
                runningScripts.remove(script);
            }
        }
    }

    public void stopScript(Script script) {
        Method getService = null;
        try {
            getService = Script.class.getDeclaredMethod("getService");
            getService.setAccessible(true);
            Service service = (Service) getService.invoke(script);
            service.sStop();
        } catch (NoSuchMethodException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            Logger.getRootLogger().error("Exception: ", e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void stopAllScripts() {
        bot.randomManager.scriptStopped = true;
        for (Script script : runningScripts) {
            script.getBotEnv().mouse.stopAllJobs();
            stopScript(script);
        }
    }

    private boolean pauseScripts = false;

    public boolean isScriptsPaused() {
        return pauseScripts;
    }

    public void setPauseScripts(boolean pauseScripts) {
        this.pauseScripts = pauseScripts;
        if (StaticStorage.mainForm.getOpenedBotPanel().equals(bot.botPanel)) {
            if(pauseScripts){
                StaticStorage.mainForm.pauseScriptButton.setIcon(new ImageIcon(MainForm.class.getResource("/images/NewScriptIcon.gif")));
                StaticStorage.mainForm.pauseScriptButton.setText("Resume");
            }
            else{
                StaticStorage.mainForm.pauseScriptButton.setIcon(new ImageIcon(MainForm.class.getResource("/images/PauseScriptIcon.gif")));
                StaticStorage.mainForm.pauseScriptButton.setText("Pause");
            }
        }
        for (Script script : runningScripts) {
            if (pauseScripts) {
                script.onPause();
            } else {
                script.onResume();
            }
        }
    }

    public int getRunningScriptCount() {
        return runningScripts.size();
    }

    public void disposeResources() {
        bot = null;
        runningScripts = null;
    }
}

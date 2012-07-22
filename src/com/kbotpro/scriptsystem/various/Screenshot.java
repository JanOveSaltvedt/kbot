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

package com.kbotpro.scriptsystem.various;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.graphics.KGraphics;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Model;
import com.kbotpro.scriptsystem.wrappers.Player;
import com.kbotpro.ui.BotPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: Scott
 * Date: Jan 9, 2010
 * Time: 3:19:46 PM
 */
public class Screenshot extends ModuleConnector {
	public boolean blockUsername = true;
	public boolean blockInventory = true;
	public boolean blockPlayer = true;
	public boolean blockOrbs = true;
	public boolean blockChat = false;
	private String extention = "png";

	public Screenshot(BotEnvironment botEnv) {
        super(botEnv);
	}

	public void takeScreenshot() {
		Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
		takeScreenshot(calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.SECOND));
	}

	public void takeScreenshot(String fileName) {
		try {
			BufferedImage image = new BufferedImage(botEnv.client.getCanvas().getWidth(), botEnv.client.getCanvas().getHeight(), BufferedImage.TYPE_INT_RGB);
			KGraphics g = new KGraphics((Graphics2D) image.getGraphics());
			g.drawImage(((BotPanel.BotAppletPanel)botEnv.botPanel.botAppletPanel).backBuffer, 0, 0, null);
			File dir = new File("screenshots");
			if (!dir.exists())
				dir.mkdir();
			File file = new File("screenshots\\" + fileName + '.' + extention);
			if (file.exists())
				if (JOptionPane.showConfirmDialog(null, file.getAbsolutePath() + " already exists.\nDo you want to replace it?", "Screenshot already exists", 2) != 0)
				    return;
			else
				file.createNewFile();
			if (blockUsername || blockChat || blockInventory || blockPlayer || blockOrbs) {
				g.setColor(Color.black);
				if (blockChat) {
                    IComponent iComponent = botEnv.interfaces.getComponent(137, 58);
                    if(iComponent != null)
                        g.fill(iComponent.getBounds());
                }
				if (blockUsername) {
                    IComponent iComponent = botEnv.interfaces.getComponent(137, 54);
                    if(iComponent != null)
                        g.fill(iComponent.getBounds());
                }
				if (blockInventory)
					if (botEnv.inventory.isOpen()) {
                        IComponent iComponent = botEnv.interfaces.getComponent(149, 0);
                        if(iComponent != null)
                            g.fill(iComponent.getBounds());
                    }
				if (blockPlayer) {
                    Player player = botEnv.players.getMyPlayer();
                    if(player != null){
                        Model model = player.getModel();
                        if(model != null){
                            g.fillModel(model);
                        }
                    }
                }
				if (blockOrbs) {
					Dimension fixed = null;
					if (botEnv.client.getViewSettings().getLayout() != 1)
					    fixed = botEnv.client.getCanvas().getSize();

					g.fill(new Rectangle(fixed == null ? 721 : (int)fixed.getWidth() - 225, fixed == null ? 28 : 53, 22, 14));
					g.fill(new Rectangle(fixed == null ? 737 : (int)fixed.getWidth() - 238, fixed == null ? 67 : 86, 22, 14));
					g.fill(new Rectangle(fixed == null ? 737 : (int)fixed.getWidth() - 217, fixed == null ? 106 : 119, 22, 14));
				}
				g.dispose();
			}
			ImageIO.write(image, extention, file);
            botEnv.log.log("Wrote screenshot: "+fileName+"."+extention+" to disk.");
		} catch (IOException e) {
			botEnv.log.log("Can not write screenshot to disk.");
		}
	}

	private Rectangle getBounds(IComponent frame) {
		return new Rectangle(frame.getAbsoluteX(), frame.getAbsoluteY(), frame.getAbsoluteX() + frame.getWidth(), frame.getAbsoluteY() + frame.getHeight());
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}
}

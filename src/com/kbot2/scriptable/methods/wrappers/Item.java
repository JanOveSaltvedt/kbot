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

package com.kbot2.scriptable.methods.wrappers;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Ove Saltvedt
 * Date: Jan 15, 2010
 * Time: 3:37:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Item {
    private final int ID;
	private final int stackSize;

	public Item(int id, int stackSize) {
		this.stackSize = stackSize;
		ID = id;
	}

	public int getID() {
		return ID;
	}

	public int getStackSize() {
		return stackSize;
	}

	public boolean isValid(){
		return ID != -1 && stackSize != 0;
	}

	public int getMarketPrice(int id) {
		String pageSource = "";
		int begin = 0;
		int end = 0;
		try {
			URL url = new URL("http://itemdb-rs.runescape.com/viewitem.ws?obj="
					+ this.getID());
			URLConnection urlConnection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String inputLine = "";
			while ((inputLine = in.readLine()) != null)
				pageSource += inputLine;
			in.close();
			begin = pageSource.indexOf("<b>Market price:</b> ")
					+ ("<b>Market price:</b> ").length();
			end = pageSource.indexOf("</span><span><b>Maximum price:</b>");
		} catch (Exception e) {
			System.out
					.println("Error obtaining price of http://itemdb-rs.runescape.com/viewitem.ws?obj="
							+ this.getID());
		}
		String withcomma = pageSource.substring(begin, end);
		String withoutcomma = withcomma.replace(",", "");
		String withoutperiod = withoutcomma.replace(".", "");
		String withoutK = withoutperiod.replace("k", "00");
		String withoutspace = withoutK.replace(" ", "");
		String withoutM = withoutspace.replace("m", "00000");
		int MARKET_PRICE = Integer.parseInt(withoutM);
		return MARKET_PRICE;
	}

}

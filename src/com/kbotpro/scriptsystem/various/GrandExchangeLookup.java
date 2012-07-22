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

package com.kbotpro.scriptsystem.various;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class used to look up prices of items on the runescape website.
 *
 * @author endoskeleton
 */

public class GrandExchangeLookup {
    private int minPrice = 0;
    private int marketPrice = 0;
    private int maxPrice = 0;
    private double changeNinety = 0;
    private double changeThirty = 0;
    private int id = 0;
    private String description;
    private String name;

    /**
     * Constructor used to look up the prices of an item on the runescape website.
     * @param itemID ID of the item you wish to lookup.
     */
    public GrandExchangeLookup(int itemID) {
        this.id = itemID;
        try {

            URL url = new URL("http://services.runescape.com/m=itemdb_rs/viewitem.ws?obj="+ itemID);

            BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));
            String feed;
            boolean nextLine = false;                //get all the info instead of connecting multiple times
            while((feed = read.readLine()) != null) {
                if (feed.contains("<b>Market price:</b>")) {
                    this.marketPrice = reformatPrice(feed);
                } else if (feed.contains("<b>Minimum price:</b>")) {
                    this.minPrice = reformatPrice(feed);
                } else if (feed.contains("<b>Maximum price:</b>")) {
                    this.maxPrice = reformatPrice(feed);
                } else if (feed.contains("30 Days:")) {
                    this.changeThirty = reformatChange(feed);
                } else if (feed.contains("90 Days:")) {
                    this.changeNinety = reformatChange(feed);
                    break;
                } else if (feed.contains("<img id=\"item_image\" src=\"http://services.runescape.com/m=")) {
                    this.name = feed.split("alt=\"")[1].split("\">")[0].trim();
                    nextLine = true;
                } else if (nextLine) {
                    this.description = feed;
                    nextLine = false;
                } else if (feed.contains("Item not found")) {
                    this.marketPrice = -1;
                    this.changeNinety = -1;
                    this.changeThirty = -1;
                    this.description = "Item not found";
                    this.name = "Item not found";
                    this.maxPrice = -1;
                    this.minPrice = -1;
                    break;
                }
            }
            read.close();
        } catch(MalformedURLException ignore) {
        } catch(IOException ignore) {
        }

    }

    private int reformatPrice(String s) {
        try {
            int multiplier = 1;

            s = s.replace("<b>Market price:</b>", "");
            s = s.replace("<b>Minimum price:</b>", "");
            s = s.replace("<b>Maximum price:</b>", "");
            s = s.replace(",", "");
            s = s.replace(".", "");

            if (s.contains("k")) {
                multiplier = 100;
            } else if (s.contains("m")) {                     
                multiplier = 100000;
            }

            s = s.replace("k", "");
            s = s.replace("m", "");
            s = s.trim();

            return (Integer.parseInt(s) * multiplier);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double reformatChange(String s) {
        try {
            return Double.parseDouble(s.split("\">")[1].split("</")[0].replace("+", "").replace("%", "").replace("\"", "").replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public int getMinimumPrice() {
        return this.minPrice;
    }

    public int getMaximumPrice() {
        return this.maxPrice;
    }

    public int getMarketPrice() {
        return this.marketPrice;
    }

    public double getThirtyDayChange() {
        return this.changeThirty;
    }

    public double getNinetyDayChange() {
        return this.changeNinety;
    }

    public int getID() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }

}

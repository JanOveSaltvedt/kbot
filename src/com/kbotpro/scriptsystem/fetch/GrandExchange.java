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

package com.kbotpro.scriptsystem.fetch;

import com.kbotpro.bot.BotEnvironment;
import com.kbotpro.scriptsystem.various.ModuleConnector;
import com.kbotpro.scriptsystem.wrappers.IComponent;
import com.kbotpro.scriptsystem.wrappers.Interface;
import com.kbotpro.scriptsystem.wrappers.Item;

/**
 * Created by IntelliJ IDEA.
 * User: Apples
 * Date: Jan 11, 2010
 * Time: 3:09:45 AM
 */
public class GrandExchange extends ModuleConnector {

	public static final int GRAND_EXCHANGE_INTERFACE_ID = 105;
	private static int[][] buySellOfferData = new int[][]{
			{ 30, 31 },
			{ 46, 47 },
			{ 62, 63 },
			{ 81, 82 },
			{ 100, 101 },
			{ 119, 120 },
			{ 30, 32 },
			{ 18, 34, 50, 69, 88, 107 }};
	private BuySellOffer[] buySellOffers = new BuySellOffer[6];

	private Interface GEInterface;
	private BuySellInternalFrame buySellInternalFrame;

	public GrandExchange(BotEnvironment botEnv) {
	    super(botEnv);
	}

	public BuySellOffer[] getBuySellOffers() {
		return buySellOffers;
	}

    /**
     * @return Checks if the shop interface is open.
     */
    public boolean isOpen() {
        return (GEInterface != null && (GEInterface.isValid()));
    }

	private class BuySellOffer {
		private final IComponent component;

		private BuySellOffer(int index) {
			component = botEnv.interfaces.getComponent(GRAND_EXCHANGE_INTERFACE_ID, buySellOfferData[7][index]);
		}

		public String getOfferType() {
			return component.getChildren()[10].getText();
		}

		public int getProgress() {
			try {
			    return (byte)((component.getChildren()[13].getWidth() / 124) * 100);
			} catch (Exception e) {
				return -1;
			}
		}

		public boolean isCancled() {
			try {
			    return component.getChildren()[13].getTextColor() == 9043984;
			} catch (Exception e) {
				return false;
			}
		}

		public Item getItem() {
			try {
			    return new Item(botEnv, component.getChildren()[17]);
			} catch (Exception e) {
				return null;
			}
		}

		public String getItemName() {
			try {
			    return component.getChildren()[18].getText();
			} catch (Exception e) {
				return null;
			}
		}

		public int getPrice() {
			try {
			    return Integer.parseInt(component.getChildren()[19].getText().replace(" gp", ""));
			} catch (Exception e) {
				return -1;
			}
		}

		public void open() {
			component.doClick();
		}

		public void cancel(boolean collectItemsAfter) {
			IComponent[] children = component.getChildren();
			if (children[13].getTextColor() == 9043984 || !children[10].getText().equals("Sell"))
				return;
			open();
			buySellInternalFrame.abortOffer();
			if (collectItemsAfter)
				buySellInternalFrame.collectItems(); 
		}
	}

	private class BuySellInternalFrame {
		private static final int START_ID = 126;
		private static final int BACK_BUTTON = 127;
		private static final int OFFER_TYPE = 133;
		private static final int ITEM_DISPLAY = 138;
		private static final int ITEM_MONEY_AMOUNT = 140;
		private static final int ITEM_NAME = 141;
		private static final int ITEM_DESCRIPTION = 142;
		private static final int MIN_MAX_PRICE = 145;
		private static final int QUANTITY = 150;
		private static final int PRICE_PER_ITEM = 155;
		private static final int MINUS_QUANTITY_BUTTON = 157;
		private static final int PLUS_QUANTITY_BUTTON = 159;
		private static final int QUANTITY_1 = 162;
		private static final int QUANTITY_10 = 164;
		private static final int QUANTITY_100 = 166;
		private static final int QUANTITY_1000 = 168;
		private static final int ENTER_QUANTITY = 170;
		private static final int MINUS_PRICE_PER_ITEM = 171;
		private static final int PLUS_PRICE_PER_ITEM = 173;
		private static final int SET_MIN_PRICE = 177;
		private static final int SET_MARKET_PRICE = 180;
		private static final int SET_MAX_PRICE = 183;
		private static final int CUSTOM_PRICE_PER_ITEM = 185;
		private static final int TOTAL_PRICE = 189;
		private static final int CONFIRM_ORDER = 190;  // Buy only
		private static final int SOLD_TOTAL_PRICE = 201;
		private static final int PROGRESS_BAR = 202;
		private static final int CANCEL_OFFER = 203;
		private static final int ITEM_COLLECT = 209;
		private static final int ITEM_COLLECT_2 = 211;

		private void abortOffer() {
			while(botEnv.interfaces.getComponent(GRAND_EXCHANGE_INTERFACE_ID, CANCEL_OFFER).doClick());
		}

		private void collectItems() {
			IComponent itemCollect = botEnv.interfaces.getComponent(GRAND_EXCHANGE_INTERFACE_ID, ITEM_COLLECT);
			if (itemCollect.getActions()[0].contains("Collect"))
				//if (botEnv.inventory.getCount() > ItemDef.isStackable())
				//	itemCollect.doAction("Collect-notes");
				//else
					itemCollect.doClick();
			itemCollect = botEnv.interfaces.getComponent(GRAND_EXCHANGE_INTERFACE_ID, ITEM_COLLECT_2);
			if (itemCollect.getActions()[0].contains("Collect"))
				//if (botEnv.inventory.getCount() > ItemDef.isStackable())
				//	itemCollect.doAction("Collect-notes");
				//else
					itemCollect.doClick();
		}
	}

	private class ItemInformation {
		private static final int INTERFACE_ID = 449;
		private static final int CLOSE = 1;
		private static final int ITEM_EXAMINE_AND_SLOT = 8;
		private static final int ITEM_SPRITE = 13;
		private static final int YOU_HAVE_AMOUNT = 25;

		private Interface GEItemInformationInterface;

		public boolean isOpen() {
			if (GEItemInformationInterface == null)
				GEItemInformationInterface = botEnv.interfaces.getInterface(INTERFACE_ID);
		    return (GEItemInformationInterface.isValid());
		}
	}
}

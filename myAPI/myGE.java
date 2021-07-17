package scripts.myAPI;

import java.awt.Point;
import java.awt.Rectangle;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.GrandExchange;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Login;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSGEOffer;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;

public class myGE {

	public static RSNPC[] GEClerk = NPCs.findNearest("Grand Exchange Clerk");	
	public static RSModel thisModel = new RSModel(GEClerk[0], GEClerk[0].getPosition());
	
	public static void openGE() {	
		
		if (GrandExchange.getWindowState()==null) {
			while (GrandExchange.getWindowState()==null) {	
					if (!GEClerk[0].isClickable()) {
						GEClerk[0].adjustCameraTo();
					}				
					DynamicClicking.clickRSNPC(GEClerk[0], "Exchange Grand Exchange Clerk");
					Antiban.get().generateAndSleep(1000);
				}
			}
		}
	
	public static void closeGE() {
		if (GrandExchange.getWindowState() != null) {
			GrandExchange.close();
		}
	}
	
	public static int countOffers() {
		
		RSGEOffer[] geItems = GrandExchange.getOffers();
		int emptyOffers = 0;
		for (int j = 0; j < 8; j++) {
			if (geItems[j].getStatus() == RSGEOffer.STATUS.valueOf("EMPTY")) {
				emptyOffers++;
			}
		}
		return 8 - emptyOffers;
	}
	
	public static boolean buyGE(String string, int buyNum, int buyLimit) {
		
		if (buyNum > buyLimit && buyLimit != -1) {
			buyNum = buyLimit;
		}	
		
		General.println("did this");
		
		return GrandExchange.offer(string, -1, buyNum, false);	
	}
	
	public static boolean buyGE(RSItem buyItem, int buyNum, int buyLimit) {
		
		if (buyNum > buyLimit && buyLimit != -1) {
			buyNum = buyLimit;
		}
		
		return GrandExchange.offer(buyItem.name, -1, buyNum, false);		
	}
	
	public static boolean checkIfCollect() {
		
		RSGEOffer[] geItems = GrandExchange.getOffers();
		
		for (int i = 0; i < geItems.length; i++) {
			
			if (geItems[i].getStatus() == RSGEOffer.STATUS.valueOf("COMPLETED")) {
				return true;
			}
		}
		return false;
	}	
	
	public static int calculateBuy(int Coins, int Price, int buyLimit) {
		if ((Coins/Price) > buyLimit) { 
			
			return buyLimit;
		} else {
			return (Coins/Price);				
		}	
	}
	
	public static boolean goToNullOfferWindow(int nullIndex) {
		
		boolean newOfferWindow = false;
		if (GrandExchange.getWindowState()!=GrandExchange.WINDOW_STATE.SELECTION_WINDOW) {
			GrandExchange.goToSelectionWindow(true);
		}		
		General.sleep(100);
		if (GrandExchange.getWindowState()==GrandExchange.WINDOW_STATE.SELECTION_WINDOW) {
			final RSInterface buyItem_interface = Interfaces.get(465, 7+nullIndex, 26);				
			Rectangle thisRectangle = buyItem_interface.getAbsoluteBounds();			
			int thisNumX;
			int thisNumY;
			thisNumX = General.randomSD(-16, 16, 4);
			thisNumY = General.randomSD(-16, 16, 4);		
			Point thisPoint = new Point(thisRectangle.x+(buyItem_interface.getWidth()/2)+thisNumX, thisRectangle.y+(buyItem_interface.getHeight()/2)+thisNumY);
			Mouse.move(thisPoint);
			General.sleep(100,300);
			Mouse.click(1);
			for (int i=0;i<5;i++) {
				General.sleep(250,350);
				if (GrandExchange.getWindowState()==GrandExchange.WINDOW_STATE.NEW_OFFER_WINDOW) {
					newOfferWindow=true;
					break;
				}
			}		
		}
	
		return newOfferWindow;		
	}
	
	public static boolean buyItem(String rsItemString, int price, int quantity) {		
		
		boolean ifBought = false;
		RSGEOffer[] theseOffers = GrandExchange.getOffers();
		int nullIndex = -1;
		for (RSGEOffer thisOffer: theseOffers) {
			if (thisOffer.getStatus()==RSGEOffer.STATUS.EMPTY) {
				nullIndex = thisOffer.getIndex();
				break;
			}
		}
		General.sleep(200,300);
		ifBought = goToNullOfferWindow(nullIndex);
		General.println("ifBought step 1 = "+ifBought);
		General.sleep(200,300);
		ifBought = GrandExchange.setItem(true, rsItemString);
		General.println("ifBought step 2 = "+ifBought);
		if (price!=-1) {
			General.sleep(200,300);
			ifBought = GrandExchange.setPrice(true, price);
			General.println("ifBought step 2.5 = "+ifBought);
		}
		General.sleep(200,300);
		ifBought = GrandExchange.setQuantity(true, quantity);
		General.println("ifBought step 3 = "+ifBought);
		General.sleep(200,300);
		ifBought = GrandExchange.confirmOffer(true);
		General.println("ifBought step 4 = "+ifBought);
		
		return ifBought;	
	}	
}

package scripts.HighAlch;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GrandExchange;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSGEOffer;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;

import scripts.myAPI.Antiban;
import scripts.myAPI.myFiler;
import scripts.myAPI.myGE;

public class highAlchHelper {
	
	//file names: Buy Limits, Collected Times
	
	public static void collectItems(String username) {	
		
		long thisTime;
		int thisIndex;
		String previousCollected = "";		
		General.println("Going to collectItems()");	
		
		RSGEOffer[] Offers = GrandExchange.getOffers();		
		for (RSGEOffer thisOffer: Offers) {
			General.println("this should collect all items, "+thisOffer.getItemName());
			if (thisOffer.getItemName() != null && thisOffer.getStatus() == RSGEOffer.STATUS.COMPLETED &&  thisOffer.getItemName().contains("Rune ")) {				
				myGE.openGE();
				General.println("Going to collect "+thisOffer.getItemName());	
				thisTime = Timing.currentTimeMillis();
				thisIndex = thisOffer.getIndex();
				RSGEOffer[] OffersCheck = GrandExchange.getOffers();
				while (OffersCheck[thisIndex].getItemName() != null) {
					General.println("does this loop for a while? 1");
					thisOffer.click();
					Antiban.get().generateAndSleep(300);	
					if (GrandExchange.getCurrentOffer() != null 
							&& GrandExchange.getCurrentOffer().getItemName() != OffersCheck[thisIndex].getItemName()) {
						GrandExchange.goToSelectionWindow();
						Antiban.get().generateAndSleep(50);
					}
											
					GrandExchange.collectItems(GrandExchange.COLLECT_METHOD.NOTES, GrandExchange.getCollectItems(thisOffer.getItemName(), "Coins"));
					Antiban.get().generateAndSleep(50);
					General.println("tried collecting "+thisOffer.getTransferredAmount()+" "+thisOffer.getItemName());
					OffersCheck = GrandExchange.getOffers();
					if (OffersCheck[thisIndex].getItemName() == null) {
						previousCollected = thisOffer.getItemName()+"." + thisOffer.getTransferredAmount()+"."+thisTime+",";
						myFiler.add("Collected Times", previousCollected, username);
						General.println("myFiler added "+previousCollected);
					}							
				}							
			}
			if (thisOffer.getItemName() != null && thisOffer.getStatus() == RSGEOffer.STATUS.CANCELLED &&  thisOffer.getItemName().contains("Rune ")) {				
				myGE.openGE();
				General.println("Going to collect cancelled "+thisOffer.getItemName());	
				thisTime = Timing.currentTimeMillis();				
				Antiban.get().generateAndSleep(300);
				int transferredAmount = thisOffer.getTransferredAmount();				
				int quantity = thisOffer.getQuantity();
				String itemName = thisOffer.getItemName();
				String buyLimitsString = myFiler.readTxt("Buy Limits", highAlch.username);
				String[] buyLimitsList = buyLimitsString.split(",");
				List<String> buyLimitsArray = new ArrayList<String>();		
				List<String> nameOrder = new ArrayList<String>();	
				for (String thisString: buyLimitsList) {
					nameOrder.add(thisString.split("\\.")[0]);
					buyLimitsArray.add(thisString.split("\\.")[1]);			
				}							
				int j = nameOrder.indexOf(itemName);
				buyLimitsArray.set(j, String.valueOf(Integer.valueOf(buyLimitsArray.get(j)) + (quantity - transferredAmount)));
				buyLimitsString = "";
				for (int i=0;i<nameOrder.size();i++) {
					buyLimitsString += nameOrder.get(i)+"."+buyLimitsArray.get(i)+",";
				}
				thisIndex = thisOffer.getIndex();
				RSGEOffer[] OffersCheck = GrandExchange.getOffers();				
				while (OffersCheck[thisIndex].getItemName() != null) {
					General.println("does this loop for a while? 2");
					thisOffer.click();
					Antiban.get().generateAndSleep(200);
					if (GrandExchange.getCurrentOffer() != null &&
							GrandExchange.getCurrentOffer().getItemName() != OffersCheck[thisIndex].getItemName()) {
						GrandExchange.goToSelectionWindow();
						Antiban.get().generateAndSleep(100);
					}					
					myFiler.write("Buy Limits", buyLimitsString, highAlch.username);					
					GrandExchange.collectItems(GrandExchange.COLLECT_METHOD.NOTES, GrandExchange.getCollectItems(thisOffer.getItemName(), "Coins"));
					Antiban.get().generateAndSleep(50);
					General.println("tried collected cancelled "+thisOffer.getTransferredAmount()+" "+thisOffer.getItemName());
					OffersCheck = GrandExchange.getOffers();
					if (transferredAmount > 0 && OffersCheck[thisIndex].getItemName()==null) {
							previousCollected = thisOffer.getItemName()+"." + thisOffer.getTransferredAmount()+"."+thisTime+",";
							myFiler.add("Collected Times", previousCollected, username);
							General.println("myFiler added "+previousCollected);
					}						
				}							
			}
		}		
	}
	
	public static String checkBuyLimits(String username) {
		
		String returnString = "Don't buy";
		String buyLimitsString = myFiler.readTxt("Buy Limits", username);
		String[] buyLimitsList = buyLimitsString.split(",");
		for (String thisString: buyLimitsList) {
			if (Integer.valueOf(thisString.split("\\.")[1]) > 0) {
				returnString = thisString.split("\\.")[0];
				break;
			}
		}		
		return returnString;
	}
	
	public static void updateBuyLimits(String username) {
		
		String previousCollected = myFiler.readTxt("Collected Times", username);		
		if (previousCollected != null && previousCollected.length() > 0) {			
			long currentTime = Timing.currentTimeMillis();
			String buyLimitsString = myFiler.readTxt("Buy Limits", username);
			String[] buyLimitsList = buyLimitsString.split(",");
			List<String> buyLimitsArray = new ArrayList<String>();		
			List<String> nameOrder = new ArrayList<String>();	
			String[] buyLimitsDotList = null;
			for (String thisString: buyLimitsList) {
				buyLimitsDotList = thisString.split("\\.");
				nameOrder.add(buyLimitsDotList[0]);
				buyLimitsArray.add(buyLimitsDotList[1]);			
			}
			String[] previousCollectedList = previousCollected.split(",");
			previousCollected = "";
			String[] previousCollectedDotList = null;
			for (String thisString: previousCollectedList) { 
				previousCollectedDotList = thisString.split("\\.");				
				int j = nameOrder.indexOf(previousCollectedDotList[0]);				
				if (Long.valueOf(previousCollectedDotList[2]) + 14400000 <= currentTime) {
					buyLimitsArray.set(j, String.valueOf(Integer.valueOf(buyLimitsArray.get(j))+Integer.valueOf(previousCollectedDotList[1])));
				}
				if (Long.valueOf(previousCollectedDotList[2]) + 14400000 > currentTime) {
					previousCollected += previousCollectedDotList[0]+"."+previousCollectedDotList[1]+"."+previousCollectedDotList[2]+",";
				}	
			}
			buyLimitsString = "";
			for (int i=0;i<buyLimitsArray.size();i++) {
				buyLimitsString += nameOrder.get(i)+"."+buyLimitsArray.get(i)+",";				
			}		
			myFiler.write("Buy Limits", buyLimitsString, username);
			myFiler.write("Collected Times", previousCollected, username);
		}		
	}
	
	public static void correctBuyLimits(String username) {

		String buyLimitsString = myFiler.readTxt("Buy Limits", username);
		String[] buyLimitsList = buyLimitsString.split(",");
		buyLimitsString = "";
		for (String thisString: buyLimitsList) {
			if (Integer.valueOf(thisString.split("\\.")[1]) > 70) {
				buyLimitsString += thisString.split("\\.")[0]+"."+"70"+",";
			} else {
				buyLimitsString += thisString.split("\\.")[0]+"."+thisString.split("\\.")[1]+",";
			}			
		}
		myFiler.write("Buy Limits", buyLimitsString, username);		
	}
	
	public static void buyItem(String username, String lowestOrBest, int gp) {
		
		String buyLimitsString = myFiler.readTxt("Buy Limits", username);
		String[] buyLimitsList = buyLimitsString.split(",");
		List<String> buyLimitsArray = new ArrayList<String>();
		List<String> nameOrder = new ArrayList<String>();
		for (String thisString: buyLimitsList) {
			nameOrder.add(thisString.split("\\.")[0]);
			buyLimitsArray.add(thisString.split("\\.")[1]);			
		}		
		ArrayList<Integer> prices = new ArrayList<Integer>();		
		prices.add(38300);
		prices.add(37700);
		prices.add(37700);
		prices.add(37700);
		prices.add(31950);
		prices.add(29350);
		prices.add(24400);
		prices.add(20500);
		prices.add(18500);
		prices.add(14800);		
		prices.add(11100);		
		ArrayList<Integer> lowestprices = new ArrayList<Integer>();			
		lowestprices.add(11100);
		lowestprices.add(14800);
		lowestprices.add(18500);
		lowestprices.add(20500);
		lowestprices.add(24400);
		lowestprices.add(29350);
		lowestprices.add(31950);
		lowestprices.add(37700);		
		lowestprices.add(37700);
		lowestprices.add(37700);
		lowestprices.add(38300);		
		if (lowestOrBest == "buyLowest") {			
			int buyNum;
			int itemIndex = -1;	
			General.println("bug check 1");
			for (int j=0;j<lowestprices.size();j++) {				
				General.println("bug check j "+j);
				if (prices.indexOf(lowestprices.get(j)) !=-1) {
					General.println("bug check prices.indexOf(lowestprices.get(j)) "+prices.indexOf(lowestprices.get(j)));
					if (Integer.valueOf(buyLimitsArray.get(prices.indexOf(lowestprices.get(j))) ) > 0) {
						itemIndex = prices.indexOf(lowestprices.get(j));
						break;
					}
				}
			}
			General.println("bug check 2");
			if (itemIndex != -1) {				
				buyNum = myGE.calculateBuy(gp, prices.get(itemIndex), Integer.valueOf(buyLimitsArray.get(itemIndex)));
				General.println("buyLowest is "+nameOrder.get(itemIndex)+" "+prices.get(itemIndex)+" "+buyNum);
				if (myGE.buyItem(nameOrder.get(itemIndex), -1, buyNum)) {
					buyLimitsArray.set(itemIndex, String.valueOf(Integer.valueOf(buyLimitsArray.get(itemIndex)) - buyNum));
					Antiban.get().generateAndSleep(250);
				}										
			} else {
				General.sleep(200);
				General.println("All items have been bought, lowest");
			}
			buyLimitsString = "";
			for (int i=0;i<buyLimitsArray.size();i++) {
				buyLimitsString += nameOrder.get(i)+"."+buyLimitsArray.get(i)+",";				
			}		
			myFiler.write("Buy Limits", buyLimitsString, username);		
		} else if (lowestOrBest == "buyBest") {
			int buyNum = 0;				
			int itemIndex = -1;			
			for (int t = 0; t < prices.size(); t++) {				
				if (Integer.valueOf(buyLimitsArray.get(t)) > 0) {					
					General.println("Integer.valueOf(buyLimitsArray.get(t)) = " +Integer.valueOf(buyLimitsArray.get(t)));						
					buyNum = myGE.calculateBuy(gp, prices.get(t), Integer.valueOf(buyLimitsArray.get(t)));								
					if (buyNum > 0) {
						itemIndex = t;
						break;
					}
				}									
			}
			if (itemIndex != -1) {		
				if (myGE.buyItem(nameOrder.get(itemIndex), -1, buyNum)) {			
					buyLimitsArray.set(itemIndex, String.valueOf(Integer.valueOf(buyLimitsArray.get(itemIndex)) - buyNum));		
					Antiban.get().generateAndSleep(250);
				}
			} else {
				General.sleep(200);
				General.println("All items have been bought, best");
			}			
			buyLimitsString = "";
			for (int i=0;i<buyLimitsArray.size();i++) {
				buyLimitsString += nameOrder.get(i)+"."+buyLimitsArray.get(i)+",";				
			}		
			myFiler.write("Buy Limits", buyLimitsString, username);			
		} else {
			General.println("buyItem else happened");
		}
	}	
		
	public static String calculateNextStep() {		
		String thisStep = null;	
		double randomNum =  Math.random();		
		if (randomNum <= .35) {
			General.println("randomNum is "+randomNum+", buyBest");
			thisStep = "buyBest";
			return thisStep;
		} else {
			General.println("randomNum is "+randomNum+", buyLowest");
			thisStep = "buyLowest";
			return thisStep;
		}
	}
	
	public static void clickInvIcon() {
		
		final RSInterface Inventory_interface = Interfaces.get(548, 59);				
		Rectangle thisRectangle = Inventory_interface.getAbsoluteBounds();	
		int thisNumX;
		int thisNumY;
		thisNumX = General.randomSD(-16, 16, 4);
		thisNumY = General.randomSD(-16, 16, 4);		
		Point thisPoint = new Point(thisRectangle.x+(Inventory_interface.getWidth()/2)+thisNumX, thisRectangle.y+(Inventory_interface.getHeight()/2)+thisNumY);
		Mouse.move(thisPoint);
		General.sleep(100,300);
		Mouse.click(1);
		for (int i=0;i<5;i++) {
			General.sleep(250,350);
			if (Magic.isSpellSelected()==false) {
				break;
			}
			Mouse.click(1);
		}		
	}
	
	public static void dragOver(RSItem runeItem, int modifierx, int modifiery) {
		
		General.println("dragOver item is "+runeItem.getDefinition().getName());
		
		while (Magic.isSpellSelected()) {
			General.println("need to exit spell cast");
			clickInvIcon();
			while (GameTab.getOpen()!=GameTab.TABS.INVENTORY) {
				General.println("need to open inv");
				General.sleep(150,200);
				Inventory.open();
			}
		}
		
		myGE.closeGE();
		
		runeItem = Inventory.find(runeItem.getDefinition().getName())[0];
		
		final RSInterface highAlch_interface = Interfaces.get(218, 39);				
		Rectangle thisRectangle = highAlch_interface.getAbsoluteBounds();				
		Point thisPoint = new Point(thisRectangle.x, thisRectangle.y);
		
		runeItem.hover();
		General.sleep(300,400);
		Mouse.sendPress(Mouse.getPos(), 1);
		General.sleep(100,150);
		Mouse.move(thisPoint);
		General.sleep(300,500);
		Mouse.sendRelease(thisPoint, 1);
		General.sleep(300,500);
	}
	
	public static boolean checkOver(RSItem runeItem,int modifierx, int modifiery) {
		
		General.println("checkOver item is "+runeItem.getDefinition().getName());
		
		boolean spotOver = false;		
		final RSInterface highAlch_interface = Interfaces.get(218, 39);				
		Rectangle thisRectangle = highAlch_interface.getAbsoluteBounds();				
		Point thisPoint = new Point(thisRectangle.x+modifierx, thisRectangle.y+modifiery);		
		Mouse.move(thisPoint);			
		for (int i=0;i<2;i++) {			
			General.sleep(50);
			if (Game.getUptext().contains(runeItem.getDefinition().getName())) {
				//General.println("contains "+runeItem.getDefinition().getName());
				spotOver = true;
			}
		}
		return spotOver;
	}
	
	public static void clickThisSpot(int modifierx,int modifiery) {
		
		final RSInterface highAlch_interface = Interfaces.get(218, 39);				
		Rectangle thisRectangle = highAlch_interface.getAbsoluteBounds();				
		Point thisPoint = new Point( (thisRectangle.x+modifierx), (thisRectangle.y+modifiery) );
		General.println((thisRectangle.x+modifierx) + " " + (thisRectangle.y+modifiery) );
		Mouse.click(thisPoint, 1);
		
	}
	
	public static String highAlch(RSItem runeItem,int rsItemInt,int itemCount) {
		
		String returnString = itemCount + " " + runeItem.name; 
		int ID = runeItem.getID();
		String name = runeItem.getDefinition().getName();		
		
		while (Magic.isSpellSelected()) {
			clickInvIcon();
		}
		
		if (GameTab.getOpen() != GameTab.TABS.MAGIC) {
			Keyboard.pressFunctionKey(6);
			General.sleep(200,300);
		}	
		
		int MagicEXP = 0;
		int MagicEXPcount = 0;
		int randomX = General.randomSD(3,8,5,1);
		int randomY = General.randomSD(3,8,5,1);
		
		while (itemCount > 0 && Inventory.getCount("Nature rune") >0 ) {	
			myGE.closeGE();
			boolean useThisSpot = false;
			
			General.sleep(1200,1300);
			if (GameTab.getOpen() != GameTab.TABS.MAGIC && !Magic.isSpellSelected()) {
				GameTab.open(GameTab.TABS.MAGIC);
				Magic.selectSpell("High Level Alchemy");
				General.sleep(400);
			}
			
			if (Math.random() < .93) {
				useThisSpot = true;		
			} else {
				General.println("==============useThisSpot = false ============");
			}
			if (itemCount%10==0) {
				General.println("itemCount%10");
				if (GameTab.getOpen() == GameTab.TABS.MAGIC && !Magic.isSpellSelected()) {
					if (useThisSpot) {
						clickThisSpot(randomX,randomY);
					} else {
						Magic.selectSpell("High Level Alchemy");
					}
					
				}
				General.sleep(500,600);
				if (Magic.isSpellSelected() && GameTab.getOpen() == GameTab.TABS.INVENTORY) {
					if (!checkOver(Inventory.find(name)[0],randomX,randomY)) {
						General.println("had to dragOver");
						while (Magic.isSpellSelected()) {
							General.println("==========1 exited spell cast during this had to dragOver==========");
							General.sleep(250);
							clickInvIcon();
						}
						randomX = General.randomSD(3,8,5,1);
						randomY = General.randomSD(3,8,5,1);
						dragOver(runeItem,randomX,randomY);
						runeItem = Inventory.find(name)[0]; //update position in inv
					}	else if (Magic.isSpellSelected() && GameTab.getOpen()== GameTab.TABS.MAGIC) {
						General.println("========exited spell cast during itemCount%10===========");
						while (Magic.isSpellSelected() && GameTab.getOpen()== GameTab.TABS.MAGIC) {
							clickInvIcon();
						}
					} else {
						General.println("didn't dragOver");
						General.println("========%10 alching========");
						General.sleep(500,600);
						clickThisSpot(randomX,randomY);
					}
				}
			}			
			MagicEXP = Skills.getXP(Skills.SKILLS.MAGIC);
			if (GameTab.getOpen() == GameTab.TABS.MAGIC && !Magic.isSpellSelected()) {
				if (useThisSpot) {
					General.println("selecting spell");
					clickThisSpot(randomX,randomY);
				} else {
					Magic.selectSpell("High Level Alchemy");
				}
				
			}
			if (Magic.isSpellSelected() && GameTab.getOpen() == GameTab.TABS.INVENTORY) {
				if (useThisSpot) {
					if (!checkOver(Inventory.find(name)[0],randomX,randomY)) {
						General.sleep(500);
						while (Magic.isSpellSelected()) {
							General.println("2 exited spell cast during this had to dragOver");
							clickInvIcon();
						}
						General.println("had to dragOver");		
						randomX = General.randomSD(3,8,5,1);
						randomY = General.randomSD(3,8,5,1);
						dragOver(runeItem, randomX, randomY);
						runeItem = Inventory.find(name)[0];
					}
					General.println("high alching");
					General.sleep(500,600);
					clickThisSpot(randomX,randomY);
					//General.println("mouse click spot, mouse spot is "+Mouse.getPos());
					//Mouse.click(1);
				} else {
					runeItem.click();
					General.println("regular item click");
				}
				MagicEXPcount++;
			} else if (Magic.isSpellSelected() && GameTab.getOpen()== GameTab.TABS.MAGIC) {
				General.println("exited spell cast during while itemCount > 0");
				while (Magic.isSpellSelected() && GameTab.getOpen()== GameTab.TABS.MAGIC) {
					clickInvIcon();
					General.sleep(200);
				}
				
			}
			General.sleep(1000);				
			if (MagicEXPcount > 5 && MagicEXP == Skills.getXP(Skills.SKILLS.MAGIC) ) {
				General.println("MagicEXP is "+MagicEXP);
				General.println("Skills.getXP(Skills.SKILLS.MAGIC) is "+Skills.getXP(Skills.SKILLS.MAGIC));
				General.println("MagicEXP did not increase through 10 MagicEXPcounts, break loop");
				break;
			} else if (MagicEXP < Skills.getXP(Skills.SKILLS.MAGIC)) {
				General.println("MagicEXP is "+MagicEXP);
				General.println("Skills.getXP(Skills.SKILLS.MAGIC) is "+Skills.getXP(Skills.SKILLS.MAGIC));
				General.println("Successfully cast High Alch, MagicEXP is "+MagicEXP);
				MagicEXPcount = 0;
			}
			
			MagicEXP = Skills.getXP(Skills.SKILLS.MAGIC);
			
			runeItem = Inventory.find(name)[0];			
			itemCount = Inventory.getCount(rsItemInt);
		}		
		return returnString;
	}
	
	public static String prepareInventory() {
		
		String returnString = "prepareInventory failed";
		boolean withdrawStaff = false;		
		if (!Equipment.isEquipped("Staff of fire","Fire battlestaff")) {			
			RSItem weaponSlot = Equipment.getItem(Equipment.SLOTS.WEAPON);						
			while (weaponSlot != null) {				
				Equipment.remove(Equipment.SLOTS.WEAPON);				
				General.sleep(300,500);
				weaponSlot = Equipment.getItem(Equipment.SLOTS.WEAPON);	
			}			
			if (Inventory.find("Staff of fire","Fire battlestaff").length > 0) {				
				while (Banking.isBankScreenOpen()) {
					General.sleep(200);
					General.println(Banking.close());
				}
				while (!Equipment.isEquipped("Staff of fire","Fire battlestaff")) {					
					Inventory.open();
					General.sleep(300,800);
					Inventory.find("Staff of fire","Fire battlestaff")[0].click();
					General.sleep(300,800);					
				}
			} else {
				withdrawStaff = true;
			}			
		}		
		General.println("Gonna withdraw items now");		
		RSItem[] checkItems = Inventory.find("Nature rune","Coins");		
		if (checkItems.length==0 || withdrawStaff) {
			while  (!Banking.isBankLoaded()) {
				General.println("open bank while loop");
				General.sleep(200);
				Banking.openBank();
			}
			if (Inventory.getAll().length > 20) {
				Banking.depositAllExcept("Nature rune","Coins","Staff of fire","Fire battlestaff");
			}		
			RSItem[] bankItems = Banking.find("Nature rune","Coins","Staff of fire","Fire battlestaff");
			if (bankItems.length != 0) {			
				for (RSItem thisItem: bankItems) {					
					General.sleep(600);
					if (thisItem.getDefinition().getName().equals("Nature rune") || thisItem.getDefinition().getName().equals("Coins")) {
						General.println(Banking.withdrawItem(thisItem, -1));
						if (Math.random() < .85) {
							General.sleep(200,400);
						} else {
							General.sleep(400,3000);
						}
					} else if (thisItem.getDefinition().getName().equals("Staff of fire") || thisItem.getDefinition().getName().equals("Fire battlestaff")) {
						if (Inventory.find("Staff of fire","Fire battlestaff").length > 0) {						
							continue;
						}
						General.println(Banking.withdrawItem(thisItem, 1));
						if (Math.random() < .85) {
							General.sleep(200,400);
						} else {
							General.sleep(400,3000);
						}
					} else {
						General.println("Shouldn't happen");
					}				
				}
			}
		}		
		if (Inventory.getCount("Fire battlestaff","Staff of fire") != 0) {
			while (Banking.isBankLoaded()) {
				General.sleep(200);
				General.println(Banking.close());
			}
			Inventory.open();
			General.sleep(300,800);
			Inventory.find("Staff of fire","Fire battlestaff")[0].click();
			General.sleep(300,800);	
		}
		
		General.sleep(1500);
		checkItems = Inventory.find("Nature rune","Coins");		
		if (Equipment.isEquipped("Staff of fire","Fire battlestaff") && checkItems.length==2) {
			returnString = "Inventory was prepared";
		} else {
			highAlch.stopScript = true;
		}
		
		return returnString;
	}
}

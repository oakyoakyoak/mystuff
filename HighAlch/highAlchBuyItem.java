package scripts.HighAlch;

import java.util.Random;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.GrandExchange;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Magic;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSGEOffer;

import scripts.myAPI.Antiban;
import scripts.myAPI.Node;
import scripts.myAPI.myFiler;
import scripts.myAPI.myGE;

public class highAlchBuyItem extends Node {
	
	@Override
	public boolean checkTask() {		
		if (highAlch.stageOfAlch=="buy nature runes" || highAlch.stageOfAlch=="buy rune items" 
				&& !highAlch.stopScript)
				return true;
		else {
			return false;	
		}
	}
	@Override
	public void doTask() {
		
		while (Magic.isSpellSelected()) {					
			Mouse.click(1);
			General.sleep(500);
		}	
		String nextStep = "buyLowest";		
		highAlch.natureRuneNum = Inventory.getCount(561);		
		highAlch.totalCoins = Inventory.getCount("Coins");		
		General.println("step is "+highAlch.stageOfAlch);		
		myGE.openGE();
		RSGEOffer[] Offers = GrandExchange.getOffers();		
		for (RSGEOffer thisOffer: Offers) {
			if (thisOffer.getStatus()!=RSGEOffer.STATUS.EMPTY && thisOffer.getItemID() == 561) {				
				highAlch.natureRuneBought = true;
			}						
		}
		if (highAlch.stageOfAlch=="buy nature runes" && highAlch.natureRuneBought==false) {
			if (myGE.buyItem("Nature rune", -1, 300+( 100*General.random(0, 4) ))) {
				highAlch.natureRuneBought = true;
				Antiban.get().generateAndSleep(200);
			}
		} else if (highAlch.stageOfAlch=="buy rune items" && myGE.countOffers() < 8) {
				nextStep = highAlchHelper.calculateNextStep();
				highAlchHelper.buyItem(highAlch.username, nextStep, highAlch.totalCoins);
				highAlch.totalCoins = Inventory.getCount("Coins");
		} else {
			General.println("this shouldn't be printed in highAlchBuyItem");
		}		
		Antiban.get().idleTimedActions();	
	}
	@Override
	public String status() {		
		return "finished buying items";	
	}
}

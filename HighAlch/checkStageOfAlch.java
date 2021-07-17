package scripts.HighAlch;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.GrandExchange;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSGEOffer;
import org.tribot.api2007.types.RSItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import scripts.myAPI.Antiban;
import scripts.myAPI.Node;
import scripts.myAPI.myFiler;
import scripts.myAPI.myGE;

public class checkStageOfAlch extends Node {

	@Override
	public boolean checkTask() throws InterruptedException {
		
		if (!highAlch.stopScript) {
			return true;
		}			
		else {
			return false;
		}			
	}

	@Override
	public void doTask() throws InterruptedException {
		
		while (Magic.isSpellSelected()) {					
			Mouse.click(1);
			General.println("does this loop for a while?");
			Antiban.get().generateAndSleep(200);
		}
		
		highAlchHelper.updateBuyLimits(highAlch.username);
		highAlchHelper.correctBuyLimits(highAlch.username);				
		Inventory.open();
		highAlch.totalCoins = Inventory.getCount("Coins");
		int natureRunes = 0;		
		natureRunes = Inventory.getCount(561);
		int runeItemCount = 0;		
		runeItemCount = Inventory.getCount("Rune platebody", "Rune platelegs", "Rune plateskirt",
				"Rune 2h sword", "Rune kiteshield", "Rune chainbody", "Rune battleaxe",
				"Rune full helm", "Rune longsword", "Rune scimitar", "Rune med helm");		
		if (runeItemCount > 0 && natureRunes > runeItemCount && highAlch.stageOfAlch=="default" ) {
			highAlch.stageOfAlch="high alch"; //high alch
		}		
		RSGEOffer[] Offers = GrandExchange.getOffers();		
		for (RSGEOffer thisOffer: Offers) {
			if (thisOffer.getItemName() != null && thisOffer.getItemID() == 561) {				
				highAlch.natureRuneBought = true;
			}						
		}		
		if (highAlch.stageOfAlch=="default" 
				&& natureRunes < 400 
				&& highAlch.totalCoins > 58000 
				&& myGE.countOffers() < 8 
				&& highAlch.natureRuneBought == false) {
			highAlch.stageOfAlch="buy nature runes"; //buy
		}		
		if (highAlch.stageOfAlch=="default" 
				&& highAlch.totalCoins > 50000 
				&& myGE.countOffers() < 8
				&& highAlchHelper.checkBuyLimits(Player.getRSPlayer().getName())!="Don't buy") {
			highAlch.stageOfAlch="buy rune items"; //buy
		}				
		if (highAlch.stageOfAlch=="default") {			
			highAlch.stageOfAlch="collect"; //collect
			if (myGE.countOffers() == 0) {
				highAlch.stopScript = true;
			}
		}
	}

	@Override
	public String status() throws InterruptedException {		
		return "Set highAlch.stageOfAlch to "+highAlch.stageOfAlch;
	}
	
}

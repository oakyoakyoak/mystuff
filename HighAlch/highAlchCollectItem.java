package scripts.HighAlch;


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
import scripts.myAPI.myGE;

public class highAlchCollectItem extends Node {
	
	public boolean checkTask() {		
		if (highAlch.stageOfAlch=="collect" && !highAlch.stopScript) 
			return true;
		else {
			return false;
		}
	}
	@Override
	public void doTask() {
		
		while (Magic.isSpellSelected()) {					
			Mouse.click(1);
			General.println("does this loop for a while? magic is spell selected 3");
			Antiban.get().generateAndSleep(200);
		}		
		int runeItemCount = 0;		
		int sleepTimeCount = 5;
		int sleptTimeCount = 0;		
		while (sleptTimeCount < sleepTimeCount) {			
			myGE.openGE();				
			RSGEOffer[] Offers = GrandExchange.getOffers();		
			for (RSGEOffer thisOffer: Offers) {						
				if (thisOffer.getStatus() == RSGEOffer.STATUS.COMPLETED && thisOffer.getItemID() == 561) {							
					thisOffer.click();
					General.println("Collecting nature runes was "+
					GrandExchange.collectItems
					(GrandExchange.COLLECT_METHOD.ITEMS, 
							GrandExchange.getCollectItems(thisOffer.getItemName(), "Coins")));
					Offers = GrandExchange.getOffers();
				}
			}				
			highAlchHelper.collectItems(Player.getRSPlayer().getName());
			General.println("does this loop for a while? 0");
			Antiban.get().generateAndSleep(300);
			runeItemCount = Inventory.getCount("Rune platebody", "Rune platelegs", "Rune plateskirt",
						"Rune 2h sword", "Rune kiteshield", "Rune chainbody", "Rune battleaxe",
						"Rune full helm", "Rune longsword", "Rune scimitar", "Rune med helm");				
			if (runeItemCount > 0 || highAlchHelper.checkBuyLimits(highAlch.username) != "Don't buy") {
				General.println("breaking sleepCount");
				break;
			}						
			Antiban.get().idleTimedActions();
			General.sleep(15000, 30000);
			if (Login.getLoginState() == Login.STATE.INGAME && Math.random() < .15 && sleptTimeCount == 4) {
				Login.logout();
				General.sleep(250000, 1000000);
			}	
			if (Login.getLoginState() != Login.STATE.INGAME) {				
				Login.login(highAlch.loginuser,highAlch.password);
			}
			Antiban.get().idleTimedActions();			
			sleptTimeCount++;
			General.println("===========================Script has slept: "+sleptTimeCount+"===========================");
		} //while close
		
		Antiban.get().idleTimedActions();
	}			

	@Override
	public String status() {		
		return "collecting";
	}
}
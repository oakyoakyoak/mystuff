package scripts.HighAlch;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.types.RSItem;

import scripts.myAPI.Antiban;
import scripts.myAPI.Node;
import scripts.myAPI.myGE;

public class highAlchItem extends Node {
	
	@Override
	public boolean checkTask() {		
		if (highAlch.stageOfAlch =="high alch" && !highAlch.stopScript) {
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
			Antiban.get().generateAndSleep(200);
		}
		
		General.println("highAlchNodeCycle is "+highAlch.highAlchNodeCycle);		
		if (highAlch.highAlchNodeCycle > 6) {
				highAlch.highAlchNodeCycle -= 5;
		}
		General.println("highAlchNodeCycle-5 is "+highAlch.highAlchNodeCycle);
		
		myGE.closeGE();				
		RSItem[] runeItems = Inventory.find("Rune platebody", "Rune platelegs", "Rune plateskirt",
				"Rune 2h sword", "Rune kiteshield", "Rune chainbody", "Rune battleaxe",
				"Rune full helm", "Rune longsword", "Rune scimitar", "Rune med helm");
		RSItem runeItem;
		String rsItemString;
		int rsItemInt;
		while (runeItems != null && runeItems.length > 0 && Inventory.getCount("Nature rune") > 0) {						
			runeItem = runeItems[0];
			rsItemInt = runeItem.getID();
			rsItemString = runeItem.getDefinition().getName();				
			int itemCount = Inventory.getCount(rsItemInt);										
			rsItemString = runeItem.getDefinition().getName();
			int runeItemCount = 0;
			runeItemCount = Inventory.getCount("Rune platebody", "Rune platelegs", "Rune plateskirt",
					"Rune 2h sword", "Rune kiteshield", "Rune chainbody", "Rune battleaxe",
					"Rune full helm", "Rune longsword", "Rune scimitar", "Rune med helm");			
			while (!Game.getUptext().contains(rsItemString) && runeItemCount > 0) {					
				General.println("this should happen check, putting item over high alch");
				highAlchHelper.dragOver(runeItem,General.randomSD(3,8,5,1),General.randomSD(3,8,5,1));
				runeItemCount = Inventory.getCount("Rune platebody", "Rune platelegs", "Rune plateskirt",
						"Rune 2h sword", "Rune kiteshield", "Rune chainbody", "Rune battleaxe",
						"Rune full helm", "Rune longsword", "Rune scimitar", "Rune med helm");
				Antiban.get().generateAndSleep(200);				
			}
			
			runeItem = Inventory.find(rsItemString)[0];			
			General.println(highAlchHelper.highAlch(runeItem, rsItemInt, runeItemCount));					
			
			runeItems = Inventory.find("Rune platebody", "Rune platelegs", "Rune plateskirt",
					"Rune 2h sword", "Rune kiteshield", "Rune chainbody", "Rune battleaxe",
					"Rune full helm", "Rune longsword", "Rune scimitar", "Rune med helm");					
		}		
		Antiban.get().idleTimedActions();		
	}	

	@Override
	public String status() {		
		return "High alched all rune armor in inventory.";
	}

}

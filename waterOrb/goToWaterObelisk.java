package scripts.waterOrb;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.myAPI.Node;
import scripts.myAPI.abc;

public class goToWaterObelisk extends Node {

	@Override
	public boolean checkTask() throws InterruptedException {		
		if ( Objects.find(10, 2151).length != 0) 
			return false;		
		
		if (Inventory.getCount("Falador teleport") > 0 
				|| Inventory.getCount("Cosmic rune") > 0 				
				|| Inventory.getCount("Unpowered orb") > 0) {
			General.println("this was true go to");
			return true;
		}		
		return false;
	}

	@Override
	public void doTask() throws InterruptedException {		
		RSObject[] thisLadder;
		
		abc.performAllTimedActions();				
		DaxWalker.walkTo(new RSTile (2835, 9817, 0)); //ladder
				
		if (!Options.isRunEnabled()) {
			Options.setRunEnabled(true);
		}
		
		DaxWalker.walkTo(new RSTile (2842, 9823, 0)); // shortcut			
		General.sleep(2500);				
		thisLadder = Objects.find(10, 17385);
			
		while (thisLadder.length == 0) {			
			General.sleep(2000);
			thisLadder = Objects.find(10, 17385);
		}		
			
		DaxWalker.walkTo(new RSTile (2844, 9823, 0));			
		General.sleep(3000, 5000);		
		thisLadder[0].adjustCameraTo();
		DynamicClicking.clickRSObject(thisLadder[0], "Climb-up Ladder");
		General.sleep(3000);		
	}

	@Override
	public String status() throws InterruptedException {		
		return "went to water obelisk";
	}

}
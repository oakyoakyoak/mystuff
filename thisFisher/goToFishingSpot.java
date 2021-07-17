package scripts.thisFisher;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.myAPI.Node;

public class goToFishingSpot extends Node {	
	
	RSNPC[] fishingSpots;
	boolean isFullInv;
	
	@Override
	public boolean checkTask() throws InterruptedException {				
		fishingSpots = NPCs.find(1525);		
		isFullInv = Inventory.isFull();		
		if (Banking.isInBank())
			return true;		
		
		if (fishingSpots.length == 0 && !isFullInv && Player.getAnimation() != 621) {
			General.println("goToFishingSpot true");
			return true;
		}		
		General.println("goToFishingSpot false");
		return false;
	}

	@Override
	public void doTask() throws InterruptedException {
		fishingSpots = NPCs.find(1525);		
		double z = Math.random();
		do {			
			fishingSpots = NPCs.find(1525);
			General.println(z);
			if (z < .5) 
				DaxWalker.walkTo(new RSTile(3088, 3230, 0));
			if (z <= .8 && z >= .5)
				DaxWalker.walkTo(new RSTile(3086, 3230, 0));
			if (z > .8 && z <= 1) 
				DaxWalker.walkTo(new RSTile(3086, 3231, 0));			
		} while (fishingSpots.length == 0);
		General.sleep(1000, 2000);
		General.println("At Fishing Spot");
	}

	@Override
	public String status() throws InterruptedException {
		return "~~~Going to Fishing Spot~~~";
	}
}


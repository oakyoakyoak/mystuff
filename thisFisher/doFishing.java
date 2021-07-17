package scripts.thisFisher;

import java.util.function.BooleanSupplier;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSNPC;

import scripts.myAPI.Node;
import scripts.myAPI.abc;

public class doFishing extends Node {
	RSNPC[] theseSpots;
	boolean isFullInv;
	boolean isValid;	
	
	@Override
	public boolean checkTask() throws InterruptedException {		
		theseSpots = NPCs.find(1525);
		isFullInv = Inventory.isFull();
		
		if (theseSpots.length != 0 && Player.getAnimation() != 621 || !isFullInv) {
			return true;
		}
		return false;
	}

	@Override
	public void doTask() throws InterruptedException {
		
		General.println("continued doTask");
		
		fishingSynchronized.theseSpots = theseSpots;
		fishingSynchronized.hoverOrClick();
		
		BooleanSupplier getAnimation = () -> {
				General.sleep(1000, 1200);
				General.println("BooleanSupplier");
				return (Player.getAnimation() == 621);
		};
		while (!Timing.waitCondition(getAnimation, 5000)) {
			General.println("Wasn't fishing");
			General.sleep(400, 500);
			fishingSynchronized.theseSpots = theseSpots;
			fishingSynchronized.hoverOrClick();
		}		
		
		General.println("after Timing");
		do {
			ThisFisher.fishing = true;
			General.println("doFishing do{} after afterTiming");
			isFullInv = Inventory.isFull();	
			General.sleep(2500, 5000);
			
			General.println("isFullInv:"+isFullInv);
		} while (Timing.waitCondition(getAnimation, 2500) && !isFullInv); 
		ThisFisher.fishing = false;
		General.println("end of doFishing");
	}

	@Override
	public String status() throws InterruptedException {		
		return "~~~Starting to Fish~~~";
	}

}

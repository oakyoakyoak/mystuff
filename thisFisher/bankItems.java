package scripts.thisFisher;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSTile;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.myAPI.Node;

public class bankItems extends Node {

	@Override
	public boolean checkTask() throws InterruptedException {		
		if (Inventory.isFull()) {
			General.println("bankItems true");
			return true;
		}
		
		General.println("bankItems false");
		return false;		
	}

	@Override
	public void doTask() throws InterruptedException {		
		do {
			DaxWalker.walkTo(new RSTile(3092, 3243, 0));
			General.sleep(1000, 2000);
		} while (!Banking.isInBank());
		
		General.sleep(1000, 2000);		
		
		do {
			do {
				Banking.openBank();		
				General.sleep(2000, 2500);
			} while (!Banking.isBankScreenOpen());
			Banking.depositAllExcept(303);
			General.sleep(1000, 2000);
		} while (Inventory.isFull());		
		General.println("Finished Banking Items");		
	}

	@Override
	public String status() throws InterruptedException {		
		return "~~~Going Banking Items~~~";
	}
}

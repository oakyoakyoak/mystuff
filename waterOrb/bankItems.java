package scripts.waterOrb;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSItem;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;
import scripts.myAPI.Node;

public class bankItems extends Node {

	@Override
	public boolean checkTask() throws InterruptedException {		
		if (Objects.find(10, 2151).length != 0 && Inventory.getCount("Unpowered orb") != 0) 
			return false;		
		
		if (Inventory.getCount("Unpowered orb") == 0
				|| Inventory.getCount("Falador teleport") == 0 
				|| Inventory.getCount("Cosmic rune") == 0)
			return true;
				
		return false;
	}

	@Override
	public void doTask() throws InterruptedException {		
		int invCount;
		int foodNum;
		RSItem[] foodInInv;
		RSItem[] potion;	
		
		while (!Banking.isInBank()) {				
				DaxWalker.walkToBank(RunescapeBank.FALADOR_WEST);
				General.sleep(2000, 3000);
		}
		
		Banking.openBank();		
		General.sleep(1000, 1500);			
		invCount = Inventory.getAll().length;			
		
		if (invCount > 2) {
			while (invCount != 2) {
				Banking.depositAllExcept(8009, 564);
				Banking.withdraw(0, 8009);
				Banking.withdraw(0, 564);
				General.sleep(1000);
				invCount = Inventory.getAll().length;
			}
		}
		
		Banking.withdraw(1, Banking.find(185, 183, 181, 2448)[0].getID());
		
		if (Combat.getHP() < Combat.getMaxHP()) {			
			if (Combat.getHP() > Combat.getMaxHP() - 22) {
				foodNum = 1;
			} else {
			foodNum = (Combat.getMaxHP() - Combat.getHP())/22;
			}
			Banking.withdraw(foodNum, 3144);
			General.sleep(1000);
			Banking.close();
			foodInInv = Inventory.find(3144);
			while (foodInInv.length > 0) {
				foodInInv[0].click();
				General.sleep(2300, 2500);
				foodInInv = Inventory.find(3144);
			}			
		}
		
		if (Banking.isBankScreenOpen()) 
			Banking.close();
		
		General.sleep(1000, 1500);
		potion = Inventory.find(185, 183, 181, 2448);
		potion[0].click();
		General.sleep(2500, 2700);		
		Banking.openBank();
		General.sleep(1500, 2000);
		Banking.depositAllExcept(8009, 564);
		Banking.withdraw(26, 567);			
		
		if (Math.random() < .51) 
				Banking.close();		
	}

	@Override
	public String status() throws InterruptedException {		
		return "banked Items";
	}	
}
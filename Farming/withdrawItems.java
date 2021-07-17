package scripts.Farming;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;
import scripts.myAPI.Antiban;
import scripts.myAPI.Node;

public class withdrawItems extends Node {
	@Override
	public boolean checkTask() {		
		//ectophial
		herbRun.withdrawList.add(4251);
		//spade
		herbRun.withdrawList.add(952);
		//seed dibber		
		herbRun.withdrawList.add(5343);	
		//rake
		herbRun.withdrawList.add(5341);
		//ardougne cloak
		herbRun.withdrawList.add(13121);
		//falador teleport
		herbRun.withdrawList.add(8009);		
		//ardougne teleport
		herbRun.withdrawList.add(8011);
		//camelot teleport
		herbRun.withdrawList.add(8010);		
		//herb seeds
		herbRun.seedAndCompostList.add(herbRun.herbSeedInt);	
		//ultracompost
		herbRun.seedAndCompostList.add(21483);	
		
		if (herbRun.grownHerbLocation != "no herbs to check" && herbRun.needToWithdraw && herbRun.stageOfRun!="don't collect") {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void doTask() throws InterruptedException {
		
		General.println("started withdraw");
		
		while (Game.getItemSelectionState()==1) {
			General.sleep(300,500);
			Mouse.click(1);
		}
		
		if (!Banking.isInBank()) {			
			DaxWalker.walkToBank();
			Antiban.get().generateAndSleep(General.random(2000, 12000));
		}				
		while (!Banking.isBankScreenOpen()) {
			Banking.openBank();
			Antiban.get().generateAndSleep(600);
		}				
		for (int i:herbRun.withdrawList) {
			if (Inventory.getCount(i)==0) {
				General.println(Banking.withdraw(1, i));
			}			
			General.sleep(300);
		}
		for (int i:herbRun.seedAndCompostList) {
			int count = herbRunHelper.countPatches();
			General.println("did this seedandcompst");
			if (Inventory.getCount(i)!=count) {
				General.println("this boolean is "+ (Inventory.getCount(i)!=count));
				General.println(Banking.withdraw(count, i));
			}	
		
			General.sleep(300);
		}
		herbRun.needToWithdraw = false;
		Antiban.get().idleTimedActions();
		Antiban.get().generateAndSleep(250);
	}
	

	@Override
	public String status() throws InterruptedException {
		return "withdrew Items";
	}

}

package scripts.Farming;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;

import scripts.myAPI.Node;

public class checkStageOfRun extends Node {

	@Override
	public boolean checkTask() {		
			return true;			
	}

	@Override
	public void doTask() {
		
		while (Game.getItemSelectionState()==1) {
			General.sleep(300,500);
			Mouse.click(1);
		}		
		if (herbRunHelper.countPatches() > 0) {
			herbRunHelper.setHerbLocation();
			Inventory.open();
			General.sleep(350,500);
			if (Inventory.getCount(herbRun.herbSeed)== 0 && Inventory.getCount("Ultracompost")==0 && herbRunHelper.countPatches() == 5) {
				herbRun.needToWithdraw = true;				
			}
		} else if (herbRunHelper.countPatches()==0) {
			General.println("No patches to check");
			herbRun.stageOfRun="don't collect";
		}						
	}

	@Override
	public String status() {		
		return "checkStageOfRun completed, the next location is "+herbRun.grownHerbLocation;
	}
}

package scripts.Farming;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;

import scripts.myAPI.Node;

public class clearPatch extends Node {
	
	@Override
	public boolean checkTask() {
		
		RSObject[] thisPatchList = Objects.find(10, "Herbs", "Herb patch", "Dead herbs");
		
		if (herbRun.grownHerbLocation != "no herbs to check" 
				&& thisPatchList.length>0
				&& Player.getPosition().distanceTo(patchLocations.returnSpecificTile(herbRun.grownHerbLocation)) < 10) {
			return true;
		} else {
			General.println("clear patch false");
			return false;
		}
	}

	@Override
	public void doTask() {
		
		while (Game.getItemSelectionState()==1) {
			General.sleep(300,500);
			Mouse.click(1);
		}

		Inventory.open();
		RSObject[] thisPatchList = Objects.find(10, "Herbs", "Herb patch", "Dead herbs");
		RSObject thisPatch = thisPatchList[0];
		String thisPatchName = thisPatch.getDefinition().getName();
		String thisPatchAction = thisPatch.getDefinition().getActions()[0];		
		if (!thisPatch.isClickable()) {
			thisPatch.adjustCameraTo();			
		}	
		while (!thisPatchAction.equals("Inspect")) {				
			switch(thisPatchAction) {
			case "Pick":
				General.println("patch was pick");
				if (Inventory.isFull()) {
					herbRunHelper.clearGrimyHerb();						
				}
				General.println(herbRunHelper.pickHerbs());
				if (Inventory.isFull()) {
					herbRunHelper.clearGrimyHerb();						
				}
				thisPatch = Objects.find(10, "Herbs", "Herb patch", "Dead herbs")[0];
				thisPatchAction = thisPatch.getDefinition().getActions()[0];
				if (thisPatchAction.equals("Pick")) {								
					if (!thisPatch.isClickable()) {
						thisPatch.adjustCameraTo();	
					}						
					General.println(herbRunHelper.pickHerbs());
					}								
				break;
			case "Clear":
				General.println("patch was clear");
				General.println(herbRunHelper.clearDeadHerbs());
				break;
			case "Rake":
				General.println("patch was rake");
				thisPatch = Objects.find(10, "Herbs", "Herb patch", "Dead herbs")[0];
				thisPatchAction = thisPatch.getDefinition().getActions()[0];
				if (!thisPatch.isClickable()) {
					thisPatch.adjustCameraTo();	
				}
				General.println(herbRunHelper.rakePatch());
				break;	
			}
			thisPatch = Objects.find(10, "Herbs", "Herb patch", "Dead herbs")[0];				
			thisPatchName = thisPatch.getDefinition().getName();
			thisPatchAction = thisPatch.getDefinition().getActions()[0];
		}				
		while (thisPatchName.equals("Herb patch") && thisPatchAction.equals("Inspect")) {
			if (!thisPatch.isClickable()) {
				thisPatch.adjustCameraTo();	
			}
			General.println("going to try to plant");
			General.println(herbRunHelper.plantPatch());
			General.sleep(1500);
			General.println("going to try to compost");
			while ( Inventory.getCount("Ultracompost") != 0 && Inventory.getCount("Ultracompost") >= herbRunHelper.countPatches()) { 
				General.println("while tried to compost");
				General.println(herbRunHelper.ultraCompostPatch());
			}		
			if (Inventory.getCount(herbRun.grimyHerbInt) > General.random(5, 7)) {
				herbRunHelper.clearGrimyHerb();
			}
			thisPatch = Objects.find(10, "Herbs", "Herb patch", "Dead herbs")[0];				
			thisPatchName = thisPatch.getDefinition().getName();
			thisPatchAction = thisPatch.getDefinition().getActions()[0];
		}
		thisPatch = Objects.find(10, "Herbs", "Herb patch", "Dead herbs")[0];				
		thisPatchName = thisPatch.getDefinition().getName();
		thisPatchAction = thisPatch.getDefinition().getActions()[0];
		if (thisPatchName.equals("Herbs") && thisPatchAction.equals("Inspect")) {
			if (Inventory.getCount("Ultracompost") > 0 &&  Inventory.getCount("Ultracompost") >= herbRunHelper.countPatches()) {
				General.println(herbRunHelper.ultraCompostPatch());
				General.sleep(500);				
			}
			herbRunHelper.updateLocation();
		}
	}
	
	@Override
	public String status() {
		
		return "Collected herbs at location " + herbRun.grownHerbLocation + " | herbRun.stageofRun is = " +herbRun.stageOfRun;
	}
}

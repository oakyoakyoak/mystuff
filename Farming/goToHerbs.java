package scripts.Farming;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSTile;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.myAPI.Antiban;
import scripts.myAPI.Node;

public class goToHerbs extends Node {
	
	@Override
	public boolean checkTask() throws InterruptedException {		
		if (herbRun.grownHerbLocation != "no herbs to check" 
				&& !herbRun.needToWithdraw 
				&& Player.getPosition().distanceTo(patchLocations.returnSpecificTile(herbRun.grownHerbLocation)) > 10) {
			General.println("goToHerbs true and last boolean is "+ (Player.getPosition().distanceTo(patchLocations.returnSpecificTile(herbRun.grownHerbLocation)) > 10));			
			return true;
		} else {
			General.println("go to herbs false");
			return false;
		}		
	}

	@Override
	public void doTask() throws InterruptedException {				
		
		General.println("started go to herbs");
		
		while (Game.getItemSelectionState()==1) {
			General.sleep(300,500);
			Mouse.click(1);
		}
		
		if (!Options.isRunEnabled()) {
			Options.setRunEnabled(true);
		}		
		String location = herbRun.grownHerbLocation;				
		RSTile thisTile = patchLocations.returnSpecificTile(location);				
		Antiban.get().idleTimedActions();
		DaxWalker.walkTo(thisTile);	
		Antiban.get().idleTimedActions();
		/*
			e
			thisPatch = Objects.findNearest(10, 8153);		
			f
			thisPatch = Objects.findNearest(10, 8150);	
			gk
			thisPatch = Objects.findNearest(10, 27115);
			a
			thisPatch = Objects.findNearest(10, 8152);		
			c
			thisPatch = Objects.findNearest(10, 8151);
		*/	
		while (Objects.find(10, "Herbs", "Herb Patch", "Dead herbs").length==0) {			
			Antiban.get().idleTimedActions();
			if (Math.random() < .15) {
				General.println("camera adjust was else");
				Camera.setCameraAngle(Camera.getOptimalAngleForPositionable(Player.getPosition())-General.randomSD(0,50,15,6));
				Antiban.get().rotateCamera();
				
			} else {
				General.println("camera adjust was else");
			}
			DaxWalker.walkTo(thisTile);
			General.println("Waiting to find herb patch");
			Antiban.get().idleTimedActions();
			Antiban.get().generateAndSleep(250);		
		}		
		if (Objects.find(10, "Herbs", "Herb Patch", "Dead herbs")[0].isClickable())	{			
			herbRun.stageOfRun = "collect";
		}		
	}

	@Override
	public String status() throws InterruptedException {			
		return "Went to herbs at location " + herbRun.grownHerbLocation;
	}
}

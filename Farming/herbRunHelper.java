package scripts.Farming;

import java.util.function.BooleanSupplier;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;

import scripts.myAPI.Antiban;
import scripts.myAPI.myFiler;

public class herbRunHelper {

	public static void clearGrimyHerb() {		
		
		RSNPC[] toolLep = NPCs.find("Tool Leprechaun");	
		General.println("clearing "+herbRun.grimyHerb);		
		Inventory.open();
		General.sleep(200);		
		while (Inventory.getCount(herbRun.grimyHerb) != 0) {					
			Inventory.find(herbRun.grimyHerbInt)[0].click();
			Antiban.get().hoverAndMenu(toolLep[0]);
			General.sleep(1250*Inventory.getCount(herbRun.grimyHerb));
			Antiban.get().generateAndSleep(250);
		}					 				
		while (Inventory.getCount(herbRun.cleanHerbInt) != 0) { // Clean toadflax unnoted	
		toolLep = NPCs.find("Tool Leprechaun");	
			if (!toolLep[0].isClickable()) {
				Camera.setCameraAngle(Camera.getOptimalAngleForPositionable(toolLep[0].getPosition()));
				toolLep[0].adjustCameraTo();
			}
			if (Game.getItemSelectionState()==0 && Inventory.find(herbRun.cleanHerbInt)[0].click()) { 
				General.sleep(350,500);
				General.println("this");
				General.println(DynamicClicking.clickRSNPC(toolLep[0], "Tool Leprechaun"));				
			} else if ((Game.getItemSelectionState()==1 && Game.getSelectedItemName().equals(herbRun.cleanHerb))) {
				General.println(DynamicClicking.clickRSNPC(toolLep[0], "Tool Leprechaun"));	
			}
			General.sleep(5000,6000);
			Antiban.get().idleTimedActions();
		}		
	}
	
	public static String pickHerbs() {
		
		String returnString = "Didn't pick herbs";
		BooleanSupplier pickingHerbsAnimation = () -> {
			General.sleep(100);
			General.println("pickHerbs BooleanSupplier");
			return (-1!=Player.getAnimation());
		};	
		do {
			General.println("Picking herbs");
			if (!Timing.waitCondition(pickingHerbsAnimation, General.random(2000,2200)))  {
				if (Inventory.isFull()) {
					clearGrimyHerb();
					if (!Objects.find(10, "Herbs")[0].isClickable()) {
						Objects.find(10, "Herbs")[0].adjustCameraTo();
						General.sleep(1000,1300);
					}
				}
				DynamicClicking.clickRSObject(Objects.find(10, "Herbs")[0], "Pick Herbs");
			}			
		} while (Objects.find(10, "Herbs").length > 0 && Objects.find(10, "Herbs")[0].getDefinition().getActions()[0].equals("Pick"));
		if (Objects.find(10, "Herb patch").length > 0 && Objects.find(10, "Herb patch")[0].getDefinition().getActions()[0].equals("Inspect")) {
			returnString="Picked patch";	
		}
		Antiban.get().idleTimedActions();
		return returnString;
	}
	
	public static String clearDeadHerbs() {		
		
		String returnString = "Didn't clear dead herbs";
		BooleanSupplier clearingDeadHerbsAnimation = () -> {
			General.sleep(100);
			General.println("clearDeadHerbs BooleanSupplier");
			return (-1!=Player.getAnimation());
		};		
		do {
			General.println("Clearing dead herbs");
			if (!Timing.waitCondition(clearingDeadHerbsAnimation, General.random(2000, 3000))) {
				DynamicClicking.clickRSObject(Objects.find(10, "Dead herbs")[0], "Clear Dead herbs");
			}
			
			General.sleep(1500, 2000);
		} while (Objects.find(10, "Dead herbs").length > 0);
		if (Objects.find(10, "Herb patch").length > 0 && Objects.find(10, "Herb patch")[0].getDefinition().getActions()[0].equals("Inspect")) {
			returnString="Cleared dead herbs";	
		}
		Antiban.get().idleTimedActions();
		return returnString;
	}
	
	public static String rakePatch() {		
		
		String returnString = "Didn't rake";
		BooleanSupplier rakingPatchAnimation = () -> {
			General.sleep(1000, 1200);
			General.println("rakePatch BooleanSupplier");
			return (-1!=Player.getAnimation());
		};		
		do {
			General.println("Raking herb patch");
			if (!Timing.waitCondition(rakingPatchAnimation, General.random(1000,1300))) {
				DynamicClicking.clickRSObject(Objects.find(10, "Herb patch")[0], "Rake Herb patch");			
				General.sleep(1500, 2000);
			}
			
		} while (Objects.find(10, "Herb patch").length > 0 && Objects.find(10, "Herb patch")[0].getDefinition().getActions()[0].equals("Rake"));
		if (Objects.find(10, "Herb patch").length > 0 && Objects.find(10, "Herb patch")[0].getDefinition().getActions()[0].equals("Inspect")) {
			returnString="Raked patch";	
		}
		Antiban.get().idleTimedActions();
	return returnString;
	}
	
	public static String plantPatch() {
		General.println("started plantPatch");
		
		while (Game.getItemSelectionState()==1) {
			Mouse.click(1);
			General.sleep(100);
		}		
		String returnString="Didn't plant";
		BooleanSupplier plantingPatchAnimation = () -> {
			General.sleep(100);
			General.println("plantPatch BooleanSupplier");
			return (-1!=Player.getAnimation());
		};		 
		while (
				(Objects.find(10, "Herb patch").length > 0 
				&& Objects.find(10, "Herb patch")[0].getDefinition().getActions()[0].equals("Inspect"))
				&& Inventory.getCount(herbRun.herbSeed) >= countPatches()) {
			General.println("planting patch");			
				if (!Timing.waitCondition(plantingPatchAnimation, General.random(1000,1500))) {
					General.println("wasn't planting");				
					if (Game.getItemSelectionState()==0 && Objects.find(10, "Herb patch").length>0) {
						General.println("going to click "+ herbRun.herbSeed);
						if (Inventory.find(herbRun.herbSeed)[0].click("Use "+herbRun.herbSeed)) {
							General.println("found seeds and clicked");
							General.sleep(1500,1700);
							DynamicClicking.clickRSObject(Objects.find(10, "Herb patch")[0], "Herb patch");
							General.sleep(500, 700);					
						}
					} else if (Game.getItemSelectionState()==1 && Game.getSelectedItemName().equals(herbRun.herbSeed) && Objects.find(10, "Herb patch").length>0) {
						General.println("else if click patch happened");
						if (Objects.find(10, "Herb patch").length > 0) {
							DynamicClicking.clickRSObject(Objects.find(10, "Herb patch")[0], "Herb patch");
						}						
						General.sleep(500, 1000);	
					}					
				}				
			}
		General.println("loop was done");
		if (Objects.find(10, "Herbs").length > 0 && Objects.find(10, "Herbs")[0].getDefinition().getActions()[0].equals("Inspect")) {
			returnString="Planted patch";	
		}	
		Antiban.get().idleTimedActions();			
	return returnString;		
	}
	
	public static String ultraCompostPatch() {
		
		General.println("started ultraCompost");
		
		while (Game.getItemSelectionState()==1) {
			Mouse.click(1);
			General.sleep(500);
		}
		
		BooleanSupplier ultraCompostAnimation = () -> {
			General.sleep(100);
			General.println("ultraCompost BooleanSupplier");
			return (-1!=Player.getAnimation());
		};	
		
		String returnString="Didn't compost";
		
		int tryCount = 0;
		General.sleep(2000,3000);
		while (				
				(Objects.find(10, "Herbs")[0].getDefinition().getActions()[0]!="Inspect" 
				&& Objects.find(10, "Herbs")[0].getDefinition().getName().equals("Herbs") ) && tryCount < 3 
				&&  Inventory.getCount("Ultracompost") >= herbRunHelper.countPatches()
				) 		
		{				
			if (!Timing.waitCondition(ultraCompostAnimation, General.random(1500, 2000))) {				
				if (Game.getItemSelectionState()==0) {
					if (Inventory.find("Ultracompost")[0].click("Use Ultracompost")) {
						DynamicClicking.clickRSObject(Objects.find(10, "Herbs", "Herb patch")[0], "Herbs");
						General.sleep(500, 1000);				
					}
				} else if (Game.getItemSelectionState()==1 && Game.getSelectedItemName().equals("Ultracompost") ) {
					DynamicClicking.clickRSObject(Objects.find(10, "Herbs", "Herb patch")[0], "Herbs");
					General.sleep(500, 1000);	
				}					
			}				
			tryCount++;
			if (tryCount==3) {
				General.println("tryCount is 3, this patch is probably ultracomposted");
			}
		}
		Antiban.get().idleTimedActions();	
		return returnString;
	}
	
	public static String updateLocation() {
		
		String returnString="Didn't update location";		
		if (Objects.find(10, "Herbs", "Herb patch")[0].getDefinition().getName().equals("Herbs") && 
				Objects.find(10, "Herbs", "Herb patch")[0].getDefinition().getActions()[0].equals("Inspect")) {			
			General.println("Going to save a new planted file");
			String lastTimings = myFiler.readTxt("Last Time Planted", herbRun.username);
			String[] lastTimingsList = lastTimings.split(",");	
			String replaceString = String.valueOf(Timing.currentTimeMillis());
			String writeString = "";
			switch(herbRun.grownHerbLocation) {			
				case "ECTOPHIAL":
					writeString += replaceString+","+lastTimingsList[1]+","+lastTimingsList[2]+","+lastTimingsList[3]+","+lastTimingsList[4]+",";
					break;
				case "CABBAGEPATCH":
					writeString += lastTimingsList[0]+","+replaceString+","+lastTimingsList[2]+","+lastTimingsList[3]+","+lastTimingsList[4]+",";
					break;
				case "GREATKOUREND":
					writeString += lastTimingsList[0]+","+lastTimingsList[1]+","+replaceString+","+lastTimingsList[3]+","+lastTimingsList[4]+",";
					break;
				case "ARDOUGNE":
					writeString += lastTimingsList[0]+","+lastTimingsList[1]+","+lastTimingsList[2]+","+replaceString+","+lastTimingsList[4]+",";
					break;
				case "CAMELOT":
					writeString += lastTimingsList[0]+","+lastTimingsList[1]+","+lastTimingsList[2]+","+lastTimingsList[3]+","+replaceString+",";
					break;
				}
			if (countPatches() > Inventory.getCount("Ultracompost")) {
				myFiler.write("Last Time Planted", writeString, herbRun.username);
			}
			returnString = "Updated this location";
			}	
		return returnString;
	}

	public static int countPatches() {
		
		String lastTimings = null;
		String[] lastTimingsList = null;
		long currentTime = Timing.currentTimeMillis();
		lastTimings = myFiler.readTxt("Last Time Planted", herbRun.username);
		if (lastTimings.equals("")) {
			lastTimings = "1,1,1,1,1,";
		}
		lastTimingsList = lastTimings.split(",");
		int countPatches = 0;
		if (lastTimingsList.length > 0) {
			for (int i = 0; i < lastTimingsList.length; i++) {
				if ((Long.valueOf(lastTimingsList[i]) + 4800000) < currentTime) {
					countPatches++;
				}
			}						
		}
		General.println("countPatches = "+countPatches);
		return countPatches;		
	}
	
	public static void setHerbLocation() {
		
		String lastTimings = null;
		String[] lastTimingsList = null;
		long currentTime = Timing.currentTimeMillis();
		lastTimings = myFiler.readTxt("Last Time Planted", herbRun.username);
		lastTimingsList = lastTimings.split(",");		
		if (lastTimingsList.length > 0) {
			for (int i = 0; i < lastTimingsList.length; i++) {
				herbRun.grownHerbLocation="no herbs to check";
				if ((Long.valueOf(lastTimingsList[i]) + 4800000) < currentTime) {
					switch(i) {
						case 0:
							herbRun.grownHerbLocation="ECTOPHIAL";
							break;						
						case 1:
							herbRun.grownHerbLocation="CABBAGEPATCH";
							break;							
						case 2:
							herbRun.grownHerbLocation="GREATKOUREND";
							break;							
						case 3:
							herbRun.grownHerbLocation="ARDOUGNE";
							break;							
						case 4:
							herbRun.grownHerbLocation="CAMELOT";
							break;				
					}				
					break;
				}				
			}			
		}
	}
	
	public static String prepareInventory() {
	
		String returnString = "Didn't have magic secateurs equipped";
		
		boolean withdrawSecateurs = false;		
		if (!Equipment.isEquipped("Magic secateurs")) {			
			RSItem weaponSlot = Equipment.getItem(Equipment.SLOTS.WEAPON);						
			while (weaponSlot != null) {				
				Equipment.remove(Equipment.SLOTS.WEAPON);				
				General.sleep(300,500);
				weaponSlot = Equipment.getItem(Equipment.SLOTS.WEAPON);	
			}			
			if (Inventory.getCount("Magic secateurs")==1) {				
				while (Banking.isBankLoaded()) {
					General.sleep(200);
					General.println(Banking.close());
				}
				while (!Equipment.isEquipped("Magic secateurs") && Inventory.getCount("Magic secateurs")==1) {					
					Inventory.open();
					General.sleep(300,800);
					Inventory.find("Magic secateurs")[0].click();
					General.sleep(300,800);					
				}
			} else {
				withdrawSecateurs = true;
			}			
		}		
		General.println("Gonna withdraw magic secateurs now");		
		if (withdrawSecateurs) {
			while  (!Banking.isBankLoaded()) {
				General.println("open bank while loop");
				General.sleep(200);
				Banking.openBank();
			}
			if (Inventory.getAll().length > 20) {
				Banking.depositAllExcept("Magic secateurs");
			}		
			RSItem[] bankItems = Banking.find("Magic secateurs");
			if (bankItems.length == 1) {								
				General.println(Banking.withdraw(1, "Magic secateurs"));
				General.sleep(1200,1500);
			}
		}
		General.println("Gonna equip magic secateurs now");	
		if (Inventory.getCount("Magic secateurs")==1 && Banking.isBankLoaded()) {			
			Clicking.click("Wield Magic secateurs", Inventory.find( "Magic secateurs")[0]);
			General.sleep(300,800);	
		}			
		if (Inventory.getCount("Magic secateurs")==1 && !Banking.isBankLoaded()) {
			while (Banking.isBankLoaded()) {
				General.sleep(200);
				General.println(Banking.close());
			} 				
			Inventory.open();
			General.sleep(300,800);
			Inventory.find("Magic secateurs")[0].click();
			General.sleep(300,800);	
		}		
		if (Equipment.isEquipped("Magic secateurs")) {
			returnString = "Magic secateurs were equipped";
		}		
		
		return returnString;
	}
	
	public static String depositUseless() {
		
		String returnString = "";
		
		
		
		
		return returnString;
	}
	
	//You plant a toadflax seed in the herb patch.
	//You treat the herb patch with ultracompost.
	//This herb patch has already been treated with ultracompost.
	
}

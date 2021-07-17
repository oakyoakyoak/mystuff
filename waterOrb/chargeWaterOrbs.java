package scripts.waterOrb;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSObject;

import scripts.myAPI.Node;
import scripts.myAPI.abc;

public class chargeWaterOrbs extends Node {
	
	@Override
	public boolean checkTask() throws InterruptedException {		
		if (Objects.find(10, 2151).length != 0 && Inventory.getCount(571) != 26)		
			return true;			
		return false;
	}

	@Override
	public void doTask() throws InterruptedException {			
		RSObject[] waterObelisk;
		boolean charging;
		int y;
		
		General.sleep(2000);
		waterOrb.abc.sleep(1500);		
		waterObelisk = Objects.find(10, 2151);		
		Inventory.open();
		waterObelisk[0].adjustCameraTo();		
		General.sleep(1500);					
		Magic.selectSpell("Charge Water Orb");
		waterOrb.abc.sleep(100);
			
		if (Magic.isSpellSelected()) {				
			DynamicClicking.clickRSObject(waterObelisk[0], "Obelisk of Water");
			//General.sleep(1000);
			if (Player.isMoving()) {
				while (Player.isMoving()) {					
					General.sleep(2500);
				}
			}
		}
		
		General.sleep(2500);			
		final RSInterface charge_interface = Interfaces.get(270, 14);
		charge_interface.click();
		charging = true;
		General.sleep(1500);
		Inventory.open();
		General.sleep(3000);
		
		while (charging) {
			y = 0;
			for (int i = 0; i < 5; i++) {
				if (Player.getAnimation() == 726) {
					charging = true;
					y++;
				}					
				General.sleep(1000);
			}
			if (y == 0)
				charging = false;
		}				
		abc.performAllTimedActions();		
	}

	@Override
	public String status() throws InterruptedException {		
		return "charged Water orbs";
	}
}
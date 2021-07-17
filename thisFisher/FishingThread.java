package scripts.thisFisher;

import java.util.ArrayList;
import java.util.Collections;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;

import scripts.ScriptTesting.myTest;
import scripts.myAPI.Node;

public class FishingThread extends Thread {
	
	public FishingThread() {}

	static long startTime;
	static long fishingTime;
	public static RSNPC[] theseFishingSpots;
	public static ArrayList<Node> nodes = new ArrayList<Node>();
	boolean invOpen;
	boolean performABC;
	static RSItem[] findNet;
		
	public void run() {		
		General.println("Started Fishing Thread");
		startTime = Timing.currentTimeMillis();
		General.println("startTime is " + startTime);
		try {
			defaultFishing();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void defaultFishing() throws InterruptedException {
		
		General.println("defaultFishing started");
		
		//Login.login("scaper227@gmail.com","classy123");
		Login.login("yellowman633@gmail.com","classy123");
		
		findNet = Inventory.find(303);
		
		do{
			Inventory.open();	
			General.sleep(1000, 2000);
		}			
		while (findNet.length == 0); 
		
		General.sleep(1200, 1500);
		
		Collections.addAll(nodes, new bankItems(), new goToFishingSpot(), new doFishing());
				
		myTest.configureDaxWalker();	
			
		fishingTime = Timing.currentTimeMillis() - startTime;
			
		while (Login.getLoginState() != Login.STATE.LOGINSCREEN) {
			for (final Node node: nodes) {	
				try {
					if (node.checkTask()) {										
						General.println("fishingTime is " + fishingTime);						
						node.doTask();														                        
						General.println(node.status());  
					   }
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ArrayIndexOutOfBoundsException z) {
					General.println("Null list");
					z.printStackTrace();
					continue;
				}
				
			}
			General.sleep(500, 600);
			General.println("went through a loop");
		}
	}
}

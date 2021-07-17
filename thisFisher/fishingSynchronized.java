package scripts.thisFisher;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSNPC;

import scripts.myAPI.abc;

public class fishingSynchronized extends Thread {
	
	static boolean hover;
	static RSNPC[] theseSpots; 
		
	public synchronized static void hoverOrClick() {
		General.println("hoverOrClick started");
		if (ThisFisher.abcOrRegular) {
			General.sleep(1000, 2000);
			ThisFisher.thisabc.performAllTimedActions();
			ThisFisher.abcOrRegular = false;
		} else {		
			hover = abc.getShouldHover();
			do {
				General.println("did find theseSpots");
				theseSpots = NPCs.find(1525);
				General.println("theseSpots length is " + theseSpots.length);
				General.sleep(1000, 1500);
			} while (theseSpots == null);
		
			if (hover) {
				General.println("if hover");
				if (theseSpots.length != 0)
					General.println("theseObjects is empty");
				if (theseSpots.length == 1) {
					Clicking.hover(theseSpots[0]);
					General.sleep(1000,2000);
					DynamicClicking.clickRSNPC(theseSpots[0], "Small Net Fishing spot");	
					General.println("hover and clicked");
				}
			} else {
				General.sleep(1000, 2000);
				DynamicClicking.clickRSNPC(theseSpots[0], "Small Net Fishing spot");
				General.println("clicked");
			}
		}
	}		
}
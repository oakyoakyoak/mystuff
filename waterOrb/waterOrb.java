package scripts.waterOrb;

import java.util.ArrayList;
import java.util.Collections;

import org.tribot.api.General;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Login;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import scripts.ScriptTesting.myTest;
import scripts.myAPI.Node;

@ScriptManifest(name = "waterOrber", category = "Magic", authors = {"oak"})
public class waterOrb extends Script {
	
	static ABCUtil abc;
	public static ArrayList<Node> nodes = new ArrayList<Node>();	
	public static int waterOrbTaskCycle = 0;
	
	@Override
	public void run() {		
		if (Login.getLoginState() == Login.STATE.LOGINSCREEN) {				
			Login.login("korosun524@yahoo.com", "classy123");
		}	
			
		while (Login.getLoginState() == Login.STATE.INGAME) {			
			try {					
				defaultWaterOrb();
				if (waterOrbTaskCycle == 40)
					break;
			} catch (InterruptedException e) {					
				e.printStackTrace();
				General.println("defaultRuneAlch crashed");
			} catch (NullPointerException e) {
				continue;
			}				
		}			
	} // run() bracket
	
	public static void defaultWaterOrb() throws InterruptedException {		
		abc = new ABCUtil();		
		Collections.addAll(nodes , new bankItems(), new goToWaterObelisk(), new chargeWaterOrbs());	   
		myTest.configureDaxWalker();  
		
		while (Login.getLoginState() != Login.STATE.LOGINSCREEN) {
			for (final Node node: nodes) {	
				if (node.checkTask()) {										
					General.println("waterOrbTaskCycle is " + waterOrbTaskCycle);						
					node.doTask();														                        
					General.println(node.status());  
                }
			}
			waterOrbTaskCycle++;
			General.println("waterOrbTaskCycle is " + waterOrbTaskCycle);
			if (waterOrbTaskCycle == 40)
                break;			
		}
	}	
} //public class bracket


package scripts.thisFisher;

import org.tribot.api.General;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import scripts.myAPI.abc;

@ScriptManifest(name = "myFisher", category = "First", authors = {"oak"})
public class ThisFisher extends Script {	
	
	ABCThread abcThread = new ABCThread();
	FishingThread generalThread = new FishingThread();
	
	@Override
	public void run() {
		while (true) {
			generalThread.start();
			General.sleep(1500, 2000);
			generalThread.run();
			General.sleep(3000, 5000);	
			abcThread.start();
			General.sleep(1000, 1500);
			abcThread.run();			
			General.sleep(1000, 1500);
		}
	}	
}
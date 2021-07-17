package scripts.ScriptTesting;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.function.BooleanSupplier;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Camera;
import org.tribot.api2007.GrandExchange;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSGEOffer;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MessageListening07;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.dax_api.api_lib.models.RunescapeBank;
import scripts.myAPI.Antiban;
import scripts.myAPI.myGE;

@ScriptManifest(name = "myTest", category = "First", authors = {"oak"})
public class myTest extends Script implements Ending, MessageListening07 { 	

		
	public static void configureDaxWalker() {
		//WebWalkerServerApi.getInstance().setDaxCredentialsProvider(new DaxCredentialsProvider()
		DaxWalker.setCredentials(new DaxCredentialsProvider()
		{
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_J4SzZPy4GL9MRK", "8d139eb2-dc9c-4075-9139-cf133b14f653");
            }
        });
    }	
	
	public void run()  {
		
		configureDaxWalker();		
		/*
		
		General.println("================================================STARTED TEST======================================");

		myGE.openGE();
		
		RSGEOffer[] theseOffers = GrandExchange.getOffers();
		int nullIndex = -1;
		for (RSGEOffer thisOffer: theseOffers) {
			if (thisOffer.getItemName()==null) {
				nullIndex = thisOffer.getIndex();
				General.println(nullIndex);
			}
		}
		theseOffers[1].click();
		for (int i=0;i<1000;i++) {
			thisNum = General.randomSD(-14, 14, 4);
			General.println("first is "+thisNum);
			if (thisNum>=7||thisNum<=-7) {
				break;
			}		
			General.sleep(50);
		}
		*/
		
		General.println(Inventory.getCount("Magic secateurs"));

		
		
		
		
		
		General.println("======================================END OF TEST==================================================");
	}
	// run()  bracket		
	
	@Override
	public void onEnd() {
		/*
		
		
		String clientString = "";
		long currentTime = Timing.currentTimeMillis();
		List<String> clientLog = General.getClientLog();		
		for (String thisString: clientLog) {
			clientString += thisString;
		}		
		myFiler.write("Client Logs\\Client Log Test "+currentTime,clientString, Player.getRSPlayer().getName());
		
		Antiban.get().generateAndSleep(400);
		*/	
		
		//Mouse.leaveGame();
	}
	
} //main bracket
	
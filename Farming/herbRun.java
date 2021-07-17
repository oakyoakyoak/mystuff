package scripts.Farming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Starting;

import scripts.ScriptTesting.myTest;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;
import scripts.myAPI.Antiban;
import scripts.myAPI.Node;
import scripts.myAPI.myFiler;

@ScriptManifest(name = "herbRun", category = "Farming", authors = {"oak"})
public class herbRun extends Script implements Starting, Ending{
	
	public static String username;
	public static boolean needToWithdraw;	
	public static String stageOfRun = "default";
	public static String grownHerbLocation = "no location";	
	public static ArrayList<Node> nodes = new ArrayList<Node>();
	public static List<Integer> withdrawList = new ArrayList<Integer>();	
	public static List<Integer> seedAndCompostList = new ArrayList<Integer>();
	public static String grimyHerb = "Grimy toadflax";
	public static String cleanHerb = "Clean toadflax";
	public static String herbSeed = "Toadflax seed";
	
	public static int grimyHerbInt = 3049;
	public static int cleanHerbInt = 2998;
	public static int herbSeedInt = 5296; 
	
	
	
	// 0 - e, 1 - f, 2 - gk, 3 - a, 4 - c	
	public static void doHerbRun() throws InterruptedException {	
		
		myTest.configureDaxWalker();
		
		if (!Equipment.isEquipped("Magic secateurs")) {
			General.println("Magic secateurs are not equipped");
			if (!Banking.isInBank()) {
				General.println("Going to bank");
				DaxWalker.walkToBank();				
			}			
			herbRunHelper.prepareInventory();			
		}		
		Inventory.open();	
		needToWithdraw = false;
		//3, 4 -> withdraw
		//0, 0 -> don't withdraw
		if (Inventory.getCount(herbRun.herbSeed) < 1 && herbRunHelper.countPatches() > 1) {
			needToWithdraw = true;
		}		
		Collections.addAll(nodes, new checkStageOfRun(), new withdrawItems(), new goToHerbs(), new clearPatch());
		for (final Node node: nodes) {			
			if (node.checkTask()) {																							
					node.doTask();
				General.println("current node.status() is " + node.status());													
			}			
		}
		
		General.println("=============="+stageOfRun);
		
		if (stageOfRun=="don't collect") {				
			if (!Options.isRunEnabled()) {
				Options.setRunEnabled(true);
			}				
			DaxWalker.walkToBank(RunescapeBank.GRAND_EXCHANGE);
			General.sleep(10000, 15000);
			General.println("cleaning "+herbRun.grimyHerb);			
			Inventory.open();
			General.sleep(200);		
			while (Inventory.getCount(herbRun.grimyHerb) != 0) {					
				Inventory.find(herbRun.grimyHerbInt)[0].click();
				General.sleep(1250*Inventory.getCount(herbRun.grimyHerb));
				Antiban.get().generateAndSleep(250);
			}	
			if (Banking.isInBank() && !Banking.isBankScreenOpen()) {
				Banking.openBank();
				Antiban.get().generateAndSleep(2000);
			}
			Banking.depositAll();		
		}			
	}
			
	private boolean theStart() {
		General.println("started run()");
		return true;
	}
	
	public void run() {		
		if (theStart()) {		
			while (Login.getLoginState() == Login.STATE.INGAME)	{			
				General.sleep(1000,1500);
				try {
					doHerbRun();
				} catch (InterruptedException e) {					
					General.println("doHerbRun crashed");
					e.printStackTrace();
				}
				
				 /*
				catch (ArrayIndexOutOfBoundsException e) {
					General.println("ArrayOutOfBounds");
					e.printStackTrace();			
				}
				*/			
				if (stageOfRun=="don't collect") 
					break;
			}
		}
	} // run bracket

	@Override
	public void onEnd() {
		Mouse.leaveGame();
	}

	@Override
	public void onStart() {
		General.println(Player.getRSPlayer().getName());
		username = Player.getRSPlayer().getName();
		String loginString = myFiler.readTxt("Login Info", username);
		String loginusername = loginString.split(",")[0];
		String password = loginString.split(",")[1];			
		General.println(Player.getRSPlayer().getName()+" "+password);
		
		if (Login.getLoginState()!=Login.STATE.INGAME) {
			General.sleep(500);
			General.println("Login is "+Login.login(loginusername,password));
			Antiban.get().generateAndSleep(General.random(5000, 7000));
		}		
	}
} // last bracket

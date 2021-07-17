package scripts.HighAlch;

import scripts.ScriptTesting.myTest;
import scripts.myAPI.Antiban;
import scripts.myAPI.Node;
import scripts.myAPI.itemPrices;
import scripts.myAPI.myFiler;
import scripts.myAPI.myGE;

import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Starting;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.types.RSItem;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.util.abc.ABCUtil;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;

@ScriptManifest(name = "myHighAlcher", category = "Magic", authors = {"oak"})
public class highAlch extends Script implements Starting, Ending, MessageListening07 {
	
	public static boolean stopScript = false;
	public static boolean natureRuneBought = false;	
	public static int totalCoins;
	static int highAlchNodeCycle = 0;
	public static String username = "";
	public static String loginuser = "";
	public static String password = "";	
	public static int randomSpot = General.randomSD(5, 9, 1);	
	public static String stageOfAlch = "default";	
	static int natureRuneNum;
	static int itemBuyNum = 0;
	
	public static ArrayList<Node> nodes = new ArrayList<Node>();
	
	public static void defaultRuneAlching() throws InterruptedException  {

		
	
		if (Login.getLoginState()!=Login.STATE.INGAME && username!="") {
			General.sleep(2000,3000);
			General.println("Login is "+Login.login(loginuser,password));
			Antiban.get().generateAndSleep(General.random(500, 1000));
		}

		//int mouseSpeed = (int) Math.random();
		Mouse.setSpeed(110);				
		myTest.configureDaxWalker();		
		while (NPCs.findNearest("Grand Exchange Clerk").length==0) {
			General.println("Walking to GE");
			DaxWalker.walkToBank(RunescapeBank.GRAND_EXCHANGE);			
		}
		General.println(highAlchHelper.prepareInventory());
		Inventory.open();       
		totalCoins = Inventory.getCount("Coins");
        natureRuneNum = Inventory.getCount(561);
        General.sleep(500,1500);  
        General.sleep(1000,2000);
        Collections.addAll(nodes, new checkStageOfAlch(), new highAlchCollectItem(), new highAlchItem(), new highAlchBuyItem());              	
		while (Login.getLoginState() != Login.STATE.LOGINSCREEN && highAlchNodeCycle <= 30) {
			General.sleep(250);
			for (final Node node: nodes) {
				General.println(!highAlch.stopScript);
				if (node.checkTask()) {
					General.println(highAlch.stageOfAlch+" is highAlch.stageOfAlch");
					node.doTask();					                        
					General.println(node.status());                                   
                }                    
            }
			highAlchNodeCycle++;
			General.println("highAlchNodeCycle is " + highAlchNodeCycle);			
			if (stopScript) {
				General.println("stopScript");
				break;
			}
			highAlch.stageOfAlch="default";
		}
		General.println("highAlchNodeCycle is " + highAlchNodeCycle);
		General.println("highAlchNodeCycle shouldn't get to be more than 30");        
	}     	
	
	public void run() {
		General.println("stopScript is "+stopScript);
		if (!stopScript) {			
			while (highAlchNodeCycle <= 30 && !stopScript) {
				try {
					defaultRuneAlching();
				} catch (InterruptedException e) {
					General.println("javaError:InterruptedException");
					e.printStackTrace();
					continue;
				} catch (ArrayIndexOutOfBoundsException e) {
					General.println("javaError:ArrayOutOfBounds");
					e.printStackTrace();
					continue;
				} 
				/*
				catch (NullPointerException e) {
					General.println("javaError:NullPointerException");
					e.printStackTrace();
				}
				*/
			}
			//Login.logout();									
		}
	}

	@Override
	public void serverMessageReceived(String message) {
		
		MessageListening07.super.serverMessageReceived(message);
		
		if (message=="hello") {
			General.println("hi");
		}
		
	}
	
	@Override
	public void onStart() {	
		
		General.println("Script started");
		username = Player.getRSPlayer().getName();
		String loginString = myFiler.readTxt("Login Info", username);
		String loginusername = loginString.split(",")[0];
		String password = loginString.split(",")[1];			
		General.println(Player.getRSPlayer().getName()+" "+password);
		
	}
	
	@Override
	public void onEnd() {
		
		List<String> clientLog = General.getClientLog();		
		String clientLogString = "";	
		for (String thisString: clientLog) {
			clientLogString += thisString;
		}		
		myFiler.write("Client Logs\\High Alcher Client Log "+String.valueOf(Timing.currentTimeMillis()), clientLogString, username);
		Mouse.leaveGame();
		General.sleep(500);		
	}	
}

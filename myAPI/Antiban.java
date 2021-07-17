package scripts.myAPI;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Clickable;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCProperties;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;

import scripts.thisFisher.ThisFisher;

public  class Antiban {
	   /*
	     Build static single instance object
	    */
	    private Antiban() {}
	    private static Antiban antiban = new Antiban();
	    public static Antiban get() {
	        return antiban;
	    }
	
	    /*
	     Instantiate abcutil class
	    */
	    private ABCUtil abcInstance = new ABCUtil();
	
	    /*
	    Declare bools for Action Conditions
	    */
	    private boolean shouldHover = false, shouldOpenMenu = false;
	
	    /*
	     ***TIMED ACTIONS*** (25 pts)
	     *
	     * Checks timers based on our profile, performs timed action and prints action if bool returns true
	     *
	     * Our player is idling when we call this method
	    */
	    public void idleTimedActions() {
	        
	    	if (abcInstance.shouldCheckTabs()) {
	    		General.println("==================");
	            General.println("Checking tabs.");
	            General.println("==================");
	            abcInstance.checkTabs();
	        }
	        if (abcInstance.shouldCheckXP()) {
	        	General.println("==================");
	            System.out.println("Checking xp.");
	            General.println("==================");;
	            abcInstance.checkXP();
	        }
	        if (abcInstance.shouldExamineEntity()) {
	        	General.println("==================");
	        	General.println("Examining entity.");
	            General.println("==================");
	            abcInstance.examineEntity();
	        }
	        if (abcInstance.shouldMoveMouse()) {
	        	General.println("==================");
	        	General.println("Moving mouse.");
	            General.println("==================");
	            abcInstance.moveMouse();
	        }
	        if (abcInstance.shouldPickupMouse()) {
	        	General.println("==================");
	        	General.println("Picking up mouse.");
	            General.println("==================");
	            abcInstance.pickupMouse();
	        }
	        if (abcInstance.shouldRightClick()) {
	        	General.println("==================");
	        	General.println("Right clicking.");
	            General.println("==================");
	            abcInstance.rightClick();
	        }
	        if (abcInstance.shouldRotateCamera()) {
	        	General.println("==================");
	        	General.println("Rotating camera.");
	            General.println("==================");
	            abcInstance.rotateCamera();
	        }
	        if (abcInstance.shouldLeaveGame()) {
	        	General.println("==================");
	        	General.println("Leaving game.");
	            General.println("==================");
	            abcInstance.leaveGame();
	        }
	    }
	    /*
	     ***Preferences*** (10 pts)
	     *
	     * Preferred ways of doing things based on our profile.
	     *
	     * Preferences already handled by default in this script:
	     *      * Open Bank Preference handled by default
	     *      * Tab Switching Preference handled by default
	     *      * Walking Preference handled by Dax Walker
	     */
	    public RSObject getNextTarget(Positionable[] targets) {
	        return (RSObject) abcInstance.selectNextTarget(targets); //selects a target based on our profile
	    }
	    /*
	     ***Action Conditions*** (26 pts)
	     *
	     * Generates when something should be done based on our profile.
	     *
	     * Action Conditions already handled by this script:
	     *      * HP to eat at not applicable
	     *      * Energy to activate run at handled by Dax Walker
	     *      * Moving to anticipated in not applicable to mining
	     *      * Resource switching upon high competition is not applicable since we're only mining one type of rock
	     */
	    public void setBools() { //Called before starting an action
	        this.shouldHover = abcInstance.shouldHover();
	        this.shouldOpenMenu = abcInstance.shouldOpenMenu();
	    }
	
	    public void hoverAndMenu(RSObject target) { //Called while performing an action
	        if (Mouse.isInBounds() && this.shouldHover) {
	            Clicking.hover(target);
	            General.println("Hovering over target");
	            if (this.shouldOpenMenu) {
	                if (!ChooseOption.isOpen()) {
	                    DynamicClicking.clickRSObject(target, 3);
	                    General.println("Opening menu");
	                }
	            }
	        } else {
	        	General.println("Didn't hover");
	        }
	    }
	    
	    public void hoverAndMenu(RSNPC target) { //Called while performing an action
	        if (Mouse.isInBounds() && this.shouldHover) {
	            Clicking.hover(target);
	            General.println("Hovering over target");
	            if (this.shouldOpenMenu) {
	                if (!ChooseOption.isOpen()) {
	                    DynamicClicking.clickRSNPC(target, 3);
	                    General.println("Opening menu");
	                }
	            }
	        } else {
	        	General.println("Didn't hover");
	        }
	    }
	    /*
	     ***Reaction Times*** (40 pts)
	     *
	     * Generates reaction times using abc instead of using random sleeps
	     */
	    private int generateReactionTime(int waitingTime /*waiting time : amount of time spent mining the rock*/) {
	        boolean menuOpen = abcInstance.shouldOpenMenu() && abcInstance.shouldHover();
	        boolean hovering = abcInstance.shouldHover();
	        long menuOpenOption = menuOpen ? ABCUtil.OPTION_MENU_OPEN : 0;
	        long hoverOption = hovering ? ABCUtil.OPTION_HOVERING : 0;
	
	        return abcInstance.generateReactionTime(abcInstance.generateBitFlags(waitingTime, menuOpenOption, hoverOption)); //return reaction time in ms
	    } //Called right after finishing mining a rock
	    public void generateSupportingTrackerInfo(int estimatedTime) { //Called right after clicking a rock
	        abcInstance.generateTrackers(estimatedTime);
	    }
	    public void generateAndSleep(int waitingTime) { //sleeps for the generated amount of time. Called after reaction time is generated
	        try {
	            int time = generateReactionTime(waitingTime);
	            General.println("generated Time is" + time);
	            General.println("Sleeping for: " + ((double) (time / 1000) ) + " seconds.");
	            abcInstance.sleep(time);
	        } catch (InterruptedException e) {
	        	General.println("The background thread interrupted the abc sleep.");
	        }
	    }
	    
	    public void examineEntity() {
	    	abcInstance.examineEntity();
	    }
	    
	    public void rotateCamera() {
	    	abcInstance.rotateCamera();;
	    }
	    
	    
	}	


package scripts.Farming;

import org.tribot.api.General;
import org.tribot.api2007.types.RSTile;

public enum patchLocations {	
	ECTOPHIAL(3604, 3530, 0),
	CABBAGEPATCH(3057, 3311, 0),
	GREATKOUREND(1739, 3552, 0),
	ARDOUGNE(2671, 3376, 0),
	CAMELOT(2814, 3465, 0),
	;   	
	RSTile position;    
	patchLocations(int x, int y, int z){
        position = new RSTile(x,y,z);
    }   
	public RSTile getPosition(){
        return position;
    }	
	public static RSTile returnSpecificTile(String location) {		
		RSTile thisTile = null;		
		switch (location) {		
		case "ECTOPHIAL":
			thisTile = ECTOPHIAL.getPosition();
			break;		
		case "CABBAGEPATCH":
			thisTile = CABBAGEPATCH.getPosition();
			break;		
		case "GREATKOUREND":
			thisTile = GREATKOUREND.getPosition();
			break;		
		case "ARDOUGNE":
			thisTile = ARDOUGNE.getPosition();
			break;		
		case "CAMELOT":
			thisTile = CAMELOT.getPosition();
			break;		
		}		
		if (thisTile == null) {			
			General.println("thisTile was null");			
		}		
		return thisTile;
	}
}

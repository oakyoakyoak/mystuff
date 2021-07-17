package scripts.myAPI;

import org.tribot.api.General;
import org.tribot.api2007.types.RSItem;

public enum itemPrices {
	
	//public static ArrayList<RSItem[]> arrayOfItemArray = new ArrayList<RSItem[]>();
	//public static ArrayList<RSItem> arrayOfItem = new ArrayList<RSItem>();	
	RUNELONGSWORD(2, 1303, 1),
	RUNEBATTLEAXE(4, 1373, 1),
	RUNECHAINBODY(5, 1113, 1),	
	RUNESCIMITAR(1, 1333, 1),	
	RUNEMEDHELM(0, 1147, 1),
	RUNEFULLHELM(3, 1163, 1),	
	RUNEPLATEBODY(10, 1127, 1),
	RUNEKITESHIELD(6, 1201, 1),
	RUNEPLATELEGS(7, 1079, 1),	
	RUNEPLATESKIRT(9, 1093, 1),	
	RUNE2HSWORD(8, 1319, 1),		
	;	
	
	RSItem Item;
	
	itemPrices(int x, int y, int z){
        Item = new RSItem(x,y,z, RSItem.TYPE.INVENTORY);
    }
	
	public RSItem getItem(){
        return Item;
    } 
	
	public static RSItem returnSpecificItem(int index) {		
		RSItem returnItem = null;		
		switch (index) {		
		case 0:
			returnItem = RUNELONGSWORD.getItem();
			break;		
		case 1:
			returnItem = RUNEBATTLEAXE.getItem();
			break;		
		case 2:
			returnItem = RUNECHAINBODY.getItem();
			break;		
		case 3:
			returnItem = RUNESCIMITAR.getItem();
			break;			
		case 4:
			returnItem = RUNEMEDHELM.getItem();
			break;		 
		case 5:
			returnItem = RUNEFULLHELM.getItem();
			break;			
		case 6:
			returnItem = RUNEPLATEBODY.getItem();
			break;			
		case 7:			
			returnItem = RUNEKITESHIELD.getItem();
			break;			
		case 8:
			returnItem = RUNEPLATELEGS.getItem();
			break;
		case 9:
			returnItem = RUNEPLATESKIRT.getItem();
			break;			
		case 10:
			returnItem = RUNE2HSWORD.getItem();
			break;		
		}		
		if (returnItem == null) 			
			General.println("thisTile was null");
		return returnItem;
	}	
	 
}
	

package scripts.myAPI;

import java.io.FileReader;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

import org.tribot.api.General;

public class myFiler {
	
	public static String readTxt(String fileName, String username) {
		
        int ch;  
        FileReader fr=null;
        String thisString = ""; 
        try { 
            fr = new FileReader("C:\\Users\\Andrew's PC\\AppData\\Roaming\\.tribot\\Saved Data\\"+username+"\\"+fileName+ ".txt");
            while ((ch=fr.read())!=-1) {
            	thisString = thisString + (char)ch;
            }
            fr.close();
        } catch (IOException fe) { 
            General.println("File not found"); 
        }       
        return thisString;		
	}	
	
	public static void write(String fileName, String fileString, String username) {		  
		  FileWriter fw;
		try {
			fw = new FileWriter("C:\\Users\\Andrew's PC\\AppData\\Roaming\\.tribot\\Saved Data\\"+username+"\\"+fileName+".txt");
			for (int i = 0; i < fileString.length(); i++) 
	            fw.write(fileString.charAt(i)); 
	        fw.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	        
	}
	
	public static void add(String fileName, String fileString, String username) {		  
		 
		int ch;  
        FileReader fr=null;
        String thisString = ""; 
        try { 
            fr = new FileReader("C:\\Users\\Andrew's PC\\AppData\\Roaming\\.tribot\\Saved Data\\"+username+"\\"+fileName+ ".txt");
            while ((ch=fr.read())!=-1) {
            	thisString = thisString + (char)ch;
            }
            fr.close();
        } catch (IOException fe) { 
            General.println("File not found"); 
        }        
        fileString = thisString + fileString;        
		FileWriter fw;
		try {
			fw = new FileWriter("C:\\Users\\Andrew's PC\\AppData\\Roaming\\.tribot\\Saved Data\\"+username+"\\"+fileName+".txt");
			for (int i = 0; i < fileString.length(); i++) 
	            fw.write(fileString.charAt(i)); 
	        fw.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	        
	}	
}
	
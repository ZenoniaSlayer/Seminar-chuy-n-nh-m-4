package WSN;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

import Editor.DataImport;
import Editor.Verify;
public class Main 
{
	static int index = 1;
	public static void main(String[] args) throws Exception 
	{	
		String filename = "";
		String filePath = "kwsn/";
		
		while(true)
		{
			 filename = "Cluster" + index + ".kwsn";	
			 File file = new File(filePath+filename);
			 if (file.exists() == true) 
			 {
				try 
				{
				//filename = "Cluster" + index + ".kwsn";						
				String pnmlFile = "temp/temp.pnml";
				String txtFile = "temp/temp.txt";
				String min_txtFile = "temp/temp_mini.txt";
				DataImport readKwsn = new DataImport();
				readKwsn.Import(filePath+filename);
				System.out.println("\nImport  "+ filename +"  successful\n");		
				System.out.println("Verifying  " + filename + " ..........");	
				Instant before = Instant.now();
				Verify verify = new Verify();
				verify.getVeriInfo(pnmlFile, txtFile, min_txtFile);	
				Instant after = Instant.now();								
		    	int delta = (int) Duration.between(before, after).toMillis();
		    	String s = String.format("%6.3f", delta / 1000.0);
		    	//String s = Long.toString(delta);
		    	//s = new StringBuilder(s).insert(s.length()-3, ".").toString();
		    	if(s.length() == 4)
		    	  {
		    		  s = "0" + s; 
		    	  }
		    	System.out.println("\n\t-------------------------");
		    	System.out.println(" \t|     Time: " + s + " s     |");
		    	System.out.print(" \t-------------------------\n");
		    	System.out.print("____________________________________________\n\n");
				index = index + 1;							
			}
				catch(Exception e)
				{
					System.out.println("Error " + e);
					
				}
			}
			else
			{							
				break;
			}
			
		}
			System.out.println("\n---------------     THE END"+"     ---------------");
	}
}

/* Verify only one file kwsn
String filePath = "verify.kwsn";
String pnmlFile = "temp/temp.pnml";
String txtFile = "temp/temp.txt";
String min_txtFile = "temp/temp_mini.txt";
DataImport readKwsn = new DataImport();
readKwsn.Import(filePath);
System.out.println("\nImport successful\n");		
System.out.println("Verifying.....");	    
Verify verify = new Verify();
verify.getVeriInfo(pnmlFile, txtFile, min_txtFile);	*/
	 
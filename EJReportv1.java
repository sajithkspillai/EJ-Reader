/****************************************************************************************************************************
*			Program to parse Electronic Journal and count number of times ATM user card was inserted.						*
*			Author: Sajith.K.S													Version:1									*
*			Date: 28-Sep-2018																								*
*																															*	
*****************************************************************************************************************************/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.stream.Stream;
import java.util.Scanner;
import java.io.FileWriter;
import java.nio.file.OpenOption;
import java.io.BufferedWriter;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.lang.Math;	


 



public class EJReportv1 {
	
	static int value=0;
	static int count =0;	//static int for counter 
	static String [][] printdata = new String [10000][10];     														// static array for data transfer and print
    private static final long MEGABYTE = 1024L * 1024L;																	//for memory test	

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }
		
	public static void main(String[] args) throws IOException
	{
		
		long startTime = System.currentTimeMillis();                                                                        //For checking run time

        long total = 0;
        for (int i = 0; i < 10000000; i++) {
            total += i;
        }
		
		Scanner InputLocation = new Scanner(System.in);
		System.out.println("");
		System.out.println("Please enter the folder path for EJ files:");
		System.out.println("");
		String Inlocation=InputLocation.nextLine();																	//Input for code.
		System.out.println("");
		System.out.println("EJ,ATM ID,Success,Attempt");														//Report header
		//String Inlocation="E:\\Projects\\ATM Switch\\Testing\\NPCI EJ Standardisation\\Check";
		Date date = new Date() ;
		SimpleDateFormat dateFormat = 	new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");									//for date bassed report
		File reportfile 			=   new File(dateFormat.format(date) + ".csv");
		String Outputreport 		=	reportfile.getAbsolutePath().substring(reportfile.getAbsolutePath().lastIndexOf("\\")+1); //convert file to string with only filename
		Outputreport				=   "CardReport"+Outputreport;
		Path path					=   FileSystems.getDefault().getPath("C:\\Users\\Public\\Downloads",Outputreport);     //set path
		File folder 				= 	new File(Inlocation);
						
        EJReportv1 listFiles = new EJReportv1();
        listFiles.listAllFiles(folder,path);
		
		
		long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(elapsedTime);
			
	
		
		
		// Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);
        System.out.println("Used memory is megabytes: "
                + bytesToMegabytes(memory));

		
		
	
	
	
	}


	
        public void listAllFiles(File folder,Path textoutput){    //get filename and path
		 File[] fileNames = folder.listFiles();
         for(File file : fileNames)
			{
			  if(file.isFile())
				{	
				count++;
				}												// if directory call the same method again
              if(file.isDirectory())
				{
                 listAllFiles(file,textoutput);
				}
				else
					{
					try{
						ManageContent(file,textoutput,count);     //sent filename, path and count of file
						} 
						catch (IOException e) 
						{
						System.out.println("Error generated while parsing the EJ record for data");		// catch block
						e.printStackTrace();
						}
        
					}
			}
		}
     
		public void ManageContent(File file,Path textoutput, int filecount) throws IOException{						//method ManageContent
         //System.out.println("read file " + file.getCanonicalPath() );


		 	
         try(BufferedReader reader  = new BufferedReader(new FileReader(file))){									//Try read file
				String line;
				String seclast="";
				String lastword="";
				String Forcorrection="";
				String checkword1="";
				String checkword2="";
				String checkword3="";
				String checkword4="";
				String checkword5="";																				//for Vortex
				String checkword6="";
				String checkword7="";
				String checkword8="";
				String checkword9="";
				String checkword10="";
				int wordchar=0;
				int possibleid=0;
				int yeschar=0;
				int nochar=0 ;
				String word1="";
				int CardA=0;
				int CardB=0;
				int CardC=0;
				int CardD=0;
				int Power=0;
				String vortexCard ="";
				int tempCard=0;
				int j=0;
				int k=0;
				Boolean transtime=false;
				Boolean lasttrans=false;
				String ATMarray [] = new String [10000];	
				
				
				while((line = reader.readLine()) != null){ 																//While file is not empty open
            	if(!(line.equals(""))){ 																				//if check each buffer line is not null open																							 
                String[] LineList = line.split("\\n+");																	// \\n+ is the line delimiter in java
				for(String EJline: LineList){																			//For present each line of file open									
				//System.out.println(EJline);	
				String[] wordList = line.split("\\s+");
				for (String wrd: wordList){ 																			// For wrd remove unwanted character
					                String []correction1 = {"ae4","as","a","e3","e4","e5","e6","e7","e8","e9","s","000p","040q","020t","05p","00p","0r","h162","w3","e1"}; 
									String []correction2 = {"[\\+,\\%,\\?,\\[,\\],\\(,\\),\\,\\]"};
																														// string array to remove characters not needed in EJ. 
									String Correctionword = wrd; 														//assign word to Correctionword
					//System.out.println(wrd);
									for ( String correctword1: correction1){ 												//take one string each from correction
									Forcorrection = Correctionword.replaceAll(correctword1," "); 						//remove unwanted character of correction with space
					//System.out.println(Forcorrection);
									for (String correctword2: correction2){
									Correctionword = Forcorrection.replaceAll(correctword2,"");}		
					//System.out.println(Correctionword);}
									}	
																														//assign filtered string back to Correctionword, continue till all character are removed																
								
					//System.out.println(Correctionword);
									String [] wordarray = Correctionword.split("\\s+");									//split the Correctionword using \\s+
					
												for (String word: wordarray){ 											//For wordarry-word open
												wordchar=0;
												j=0;
												k=0;
												if(!(word.equals(""))){												// if word is not null open	
					//System.out.println("the final split words "+word);												//checking card insertion count
					//System.out.println("lastword :"+lastword);
					//System.out.println("seclast :"+seclast);
					  																								
												checkword2=  "CARD";
												checkword1 = "INSERTED";                           						//for NCR and Diebold
												if(word.equals(checkword1)){
												if(lastword.equals(checkword2)){
												CardB++;}
												}
				
												checkword3="NO:";														//for Vortex
												checkword4="CARD";
												if(word.length()>3)
												vortexCard = word.substring(0,3);									  	//check only from 0 to 2 of string
					//System.out.println("the truncated "+vortexCard);
												if(vortexCard.equals(checkword3)){
												if(lastword.equals(checkword4)){
												CardA++;}
												}
												
												String [] NCRdispCode = {"E1*2","E1*3","E1*4","E1*5"};
												if(word.length()>11){
												checkword5=word.substring(8,12);
												for(String NCRdisperrcode: NCRdispCode){
												if(NCRdisperrcode.equals(checkword5)){
					//System.out.println(NCRdisperrcode);
					//System.out.println(checkword5);	
												CardC++;}
												}
												}
												String [] VrtxdispCode = {"E*2","E*3","E*4","E*5"};
												if(word.length()>11){
												checkword6=word.substring(0,3);
					//System.out.println(checkword6);
												for(String Vrtxdisperrcode: VrtxdispCode){
												if(Vrtxdisperrcode.equals(checkword6)){
												CardC++;}
												}
												}
												String [] DbddispCode = {"000DI01:38:00:00","000DI01:3D:37:26","000DI01:3D:37:38","000DI01:3D:37:39","001D901:38:00:00","001D901:3A:38:31","001D901:3B:33:33","001D901:3D:37:38","001D901:3D:37:39","000DR01:38:00:00","000DR01:3A:37:41","000DR01:3A:39:44"};
												if(word.length()>15){								
												checkword7 =new String(word);
					//System.out.println("Checkword5 "+checkword7+" Word "+word+" length "+word.length());						
												for(String Dbddisperrcode: DbddispCode){
					//System.out.println("Dbddisperrcode "+Dbddisperrcode);								
												if(Dbddisperrcode.equals(checkword7)){
												CardC++;}
												}
												}
					//System.out.println("CardC :"+CardC);		
						
												checkword8=":ADMINISTRATION";											//for Administrative transaction exception
												if(word.equals(checkword8)){
												CardD++;}
						
												checkword10="POWER";														//for Vortex
												checkword9="UP0";
					//System.out.println(checkword6);
												if(word.equals(checkword9)){
												if(lastword.equals(checkword10)){
												Power++;}
												}
						
												checkword9="POWER-UP/RESET";
												checkword10="FAIL-BATTERY";
												if(word.equals(checkword10)||word.equals(checkword9)){
												Power++;}
												else	
												lastword=word;
												
		
												
					//System.out.println("Inner loop Vortex CardA "+CardA);
					//System.out.println("Inner loop Others CardB "+CardB);
					//System.out.println("Inner loop Others CardC "+CardC);
					//System.out.println("Inner loop Others CardD "+CardD);
					//System.out.println("Inner loop Others Power "+Power);														//finish checking card insertion
												

												SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");					//Sets date format
												if (word.trim().length() != dateFormat.toPattern().length())
												transtime = false;	
												else{					          												//if check length matches open
												j++;
					//System.out.println(word.trim().length());
					//System.out.println(dateFormat.toPattern().length());							
					//System.out.println("Logic j "+j);							
												dateFormat.setLenient(false);													//avoid parsing error for date format
												try {																			//	try open
												dateFormat.parse(word.trim());													//parse the word
					//System.out.println(dateFormat.parse(word.trim()));							
												k++;
					//System.out.println("Logic k "+k);							
												}																				// try close
												catch (ParseException pe) {														// catch parse open	
												transtime = false;}																// catch parse close
												if(k>0){
												transtime = true;}	
												}																			//if check length matches closed																							
												
					//System.out.println("Logic lasttranstime "+lasttrans);
						
												int year = Calendar.getInstance().get(Calendar.YEAR);
												String Year = String.valueOf(year);
					
					//System.out.println("year :"+Year);	
					
												wordchar += word.length();  													// check length of word
					//System.out.println( "Word length is wordchar "+wordchar);
													
													if(seclast.equals(Year))
													if(wordchar==8&&lasttrans){													// if wordchar length is 8 and last word time open
													yeschar =0; 																// increased on first char being digit
													nochar=1; 																	// decreased on first char not being digit. Exit option
													while(yeschar<8 && nochar==1){												//while for checking digits of ATM ID open
													int c;
													c = Character.getNumericValue(word.charAt(yeschar)); 						//convert char to number, word.chartAt() gets the charector frpm word
					//System.out.println(" Char to Integer value "+c);
													if(c>=0 && c<=9){ 															// if for comparison open
													yeschar++;
					//System.out.println(" Counter to check char of word "+yeschar);
													}											//if close	
													else{																		//else open
													nochar--;}																	//else close
													if(yeschar==7){ 															//if open
													ATMarray[possibleid]=word;
					//System.out.println("Before array word "+word);
					//System.out.println("Before array word "+lastword);
					//System.out.println("Before array ATMarray[possibleid] "+ATMarray[possibleid]);
					//System.out.println("Before array possibleid "+possibleid);
													possibleid++; 																// give count of repetition of ATM ID
														
													}																			//if close									
												}																				// while closed
											}																					// if for wordchar length closed			
											if(wordchar!=8)
											if(!lasttrans)
											seclast = lastword;		

											lasttrans=transtime;
											j=0;k=0;
											
					//System.out.println("After array possible id "+possibleid);
											

										}																						// if word is not null closed	
									} 																							//For wordarray-word closed		 
								}																								// For wrd remove unwanted character
							} 																									//For present each line of file close
						}																										//if check each buffer line is not null open	
					}																											//While file is not empty open 					
				
				/*				int counter = possibleid;
								int arrcount = possibleid;	
								while(arrcount>=1)
								{	
								if((ATMarray[possibleid-1]).equals(ATMarray[arrcount-1])){
								arrcount--;
								}
								else{
								arrcount--;
								counter--;
								}
								}
								possibleid=counter;	
								*/
					//System.out.println("End possibleid "+possibleid);
								if(possibleid!=0){
								word1=ATMarray[possibleid-1];}
								else{
								word1="";}
					//System.out.println("After array word1 "+word1);
					//System.out.println("After array ATMarray "+ATMarray[possibleid]);
														
						
						if(CardA!=0)
						{
						tempCard = CardA;
						}
						else
						{	
						if(CardB!=0)
						{	
						tempCard=CardB;
						}
						}
						String filename=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1);                                   //convert full filename with path to file name
						if((word1.equals(""))){
						System.out.println(filename+",,,");
						try{
						Printreport(filename,textoutput,possibleid,tempCard,word1,filecount,Power,CardD,CardC);}															//call method to print file using absolute path
						catch(IOException er){
						System.out.println("Error while passing value for printing");
						er.printStackTrace();	
						}
						//System.out.println("Called Printreport");					
						}
						else{	
						System.out.println(filename+","+word1+ "," +possibleid+","+tempCard);
						try{
						Printreport(filename,textoutput,possibleid,tempCard,word1,filecount,Power,CardD,CardC);}
						catch(IOException er){
						System.out.println("Error while passing value for printing");
						er.printStackTrace();	
						}
						//System.out.println("Called Printreport");						
						}						
							
				}																																		//Try close file
		}																																				//Method ManageContent close
							
		         
		
		
		public void Printreport(String Name, Path report, int hit, int card, String id, int row, int restart, int admin, int disperr) throws IOException{              
		int incrt=row;
		value++;
		int incr = value;
		//System.out.println("Reached Printreport");	
		printdata[0][0] = "EJ";
		printdata[0][1] = "ATM";
//		System.out.println("printdata[0][0] "+printdata[0][0]);
		printdata[0][2] = "Hit";
//		System.out.println("printdata[0][1] "+printdata[0][1]);		
		printdata[0][3] = "CardIns";
		printdata[0][4] = "Var";
		printdata[0][5] = "%";
		printdata[0][6] = "Sev";
		printdata[0][7] = "Pwr";
		printdata[0][8] = "LeadCap";
		printdata[0][9] = "DispErr";
		int diff = (card-hit);
		float perc =0;
		if(diff!=0&&card!=0)
		{	
		perc = (float)diff/(float)card;
		perc = perc*100;
		perc = Math.round(perc);
		}
		
//		System.out.println("printdata[0][2] "+printdata[0][2]);		
			
//		System.out.println("incr: "+incr);
		if(incr!=0)
		{
		printdata[incr][0]=Name;	
//		System.out.println("Entered first if loop"+incr);	
		printdata[incr][1]=id;
//		System.out.println(printdata[incr][0]);
		printdata[incr][2]=String.valueOf(hit);
//		System.out.println(printdata[incr][1]);
		printdata[incr][3]=String.valueOf(card);
//		System.out.println(printdata[incr][2]);
		printdata[incr][4]=String.valueOf(diff);
		printdata[incr][5]=String.valueOf(perc);
		if(perc>=50)
		printdata[incr][6]="High";
		if(perc>=30&&perc<50)
		printdata[incr][6]="Medium";
		if(perc<30)
		printdata[incr][6]="Low";	
		printdata[incr][7]=String.valueOf(restart);
		printdata[incr][8]=String.valueOf(admin);
		printdata[incr][9]=String.valueOf(disperr);
		
		}
		

		BufferedWriter output = Files.newBufferedWriter(report);                                                        //file created
//		System.out.println("Before while incrt: "+incrt);
		int printline=0;
		while( incr==incrt&&printline<=incr){
			//System.out.println("Count in loop "+incr);
			output.write(printdata[printline][0]);
			//System.out.println(printdata[printline][0]);
			output.write(",");
			//System.out.println(printdata[printline][1]);
			output.write(printdata[printline][1]);
			//System.out.println(printdata[printline][2]);
			output.write(",");		
			output.write(printdata[printline][2]);
			output.write(",");
			output.write(printdata[printline][3]);
			output.write(",");
			output.write(printdata[printline][4]);
			output.write(",");
			output.write(printdata[printline][5]);
			output.write(",");
			output.write(printdata[printline][6]);
			output.write(",");
			output.write(printdata[printline][7]);
			output.write(",");
			output.write(printdata[printline][8]);
			output.write(",");
			output.write(printdata[printline][9]);	
			output.newLine();
			printline++;	
			}
			output.close();
			
			
		}																																		//close print report
	
	
		
}
						
						
						
			
	
     
    
     

package proj2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

//use any import that is from java.*


public class WikiCrawler {
	
	//needs to be named this, will be changed by instructor to grade
	private static final String BASE_URL = "https://en.wikipedia.org";
	
	private String seed;
	private int numPages;
	private ArrayList<String> keywords;
	private String outFile;
	
	//constructor to setup crawler
	public WikiCrawler(String seedURL, int max, ArrayList<String> topics, String fileName) {
		seed = seedURL;
		numPages = max;
		keywords = topics;
		outFile = fileName;
	}
	public void crawl() throws InterruptedException, IOException {
		//count number of pages with topics
		int relevantPages = 0;
				
		//counter to keep track of page requests
		int count = 0;
		
		//set up output file writer
		PrintWriter writer = new PrintWriter(outFile,"UTF-8");
		
		writer.println(numPages);
		
		//Queue<String> queue = new Queue<String>();
		//
		
		
		//Queue<SimpleEntry<String, String>> queue_visit = new Queue<SimpleEntry<String,String>>();
		//try linked list to see if easier debug? Also need to store key and value
		//using linked list implementation, holding SimpleEntry of string (from abstract map)
		Queue<SimpleEntry<String,String>> queueVisit = new LinkedList<SimpleEntry<String,String>>();
		//add initial with seed value.
		queueVisit.add(new SimpleEntry<String,String>("",seed));
		
		//set up list of visited pages as hashmap for fast search 0(1)
		//2. Initialize a Queue Q and a list visited.
		HashSet<String> visited = new HashSet<String>();
		
		
		
		//why is this part not working??
		//oops it was a semicolon not a bracket -- sorry
		while(!queueVisit.isEmpty() && (relevantPages < numPages)){
			
			// get next edge
			SimpleEntry<String,String> edge = queueVisit.poll();
			String currentPage = edge.getValue();
			
			//create new ArrayList of topics so that they can be removed when found
			ArrayList<String> coveredKeys = new ArrayList<String>();
			//add all topics
			for(int k = 0; k < keywords.size(); k++){
				coveredKeys.add(keywords.get(k));
			}
			
			//check if it contains in hashmap
			if(!visited.contains(currentPage)){
				boolean actualContent = false;
				//holder
				HashSet<String> temp = new HashSet<String>();
				
				//sometimes this takes a long time?? not sure if this is cause?
				Scanner myScanner = new Scanner((new URL(BASE_URL+currentPage)).openStream());
			
				//keep track of links on the current page, same format as above (with SimpleEntry)
				LinkedList<SimpleEntry<String,String>> lists = new LinkedList<SimpleEntry<String,String>>();
				
				//add to hashmap of visited
				visited.add(currentPage);
				
				while(myScanner.hasNextLine()){
					String currentLine = myScanner.nextLine();
					
					//For this PA, you may assume the following: The “actual text content” of the
					//page starts immediately after the first occurrence of the html tag <p>.
					if(currentLine.contains("<p>")) actualContent = true;
					
					//check if topic has been covered
					//save for search time
					if(!coveredKeys.isEmpty()){
						//check remaining keys
						for(int i=0; i<coveredKeys.size(); i++){
							//if contains remove
							if(currentLine.contains(coveredKeys.get(i))){
								coveredKeys.remove(i);
							}
						}
					}
					
					//check if current contains any of the topics
//					//Must handle if topics is empty!!!
////					if(!?.isEmpty()){
////						for(SimpleEntry<String, String> i: links){
////							if(currentLine.contains(i.getKey())){
////								links.remove(i);
////							}
////						}
////					}
//					
//					//
					//href and /wiki/ are where the header starts
					while(currentLine.contains("href=\"/wiki/") && actualContent){
						
						String nextPage = ""; 
						//ignore href=/ when getting value from wiki
						
						//int startOfLink = currentLine.indexOf("href=\"/wiki/");
						int index = currentLine.indexOf("href=\"/wiki/")+6;
						
						//if(index >= 6) {
						//}
						
						//crawl over the next part until \ is hit
						
						//end of section if true
						while(currentLine.charAt(index)!='\"'){
							nextPage += currentLine.charAt(index);
							index++;
						}
						//according to pdf do not allow things with : or # as these are images or other irrelevant info.
						if(!nextPage.contains(":")&&!nextPage.contains("#")) { //duplicates???
							if (!temp.contains(nextPage)){
								lists.add(new SimpleEntry<String,String>(edge.getValue(),nextPage));
								temp.add(nextPage);
							}
						}
						//update current line for next use
						currentLine = currentLine.replace(nextPage,"");
					}
				}
				
				
				// only add if everything has been hit
				if(coveredKeys.isEmpty()){
					
					//populate main queue to then write
					queueVisit.addAll(lists);
					//increment
					relevantPages++;
					
					if(!edge.getKey().equals("")){
						
						//writer.println("test" + " " + edge.getValue());
						writer.println(edge.getKey() + " " + edge.getValue());
					}
				}
				myScanner.close();
				count++;
				
				//limit page requests
//				//Wait for at least 3 seconds after
//				//every 25 requests. If you do not adhere to this policy you will receive ZERO credit.
//				//Please use Thread.sleep() for waiting.
////				if(count == 25){
////					count = 0;
////					Thread.sleep(3000);
////				}	
//				//must use modulo since its every 25 requests.
				if(count % 25==0){
					Thread.sleep(3000);
				}
			}
		}
		//write values to output file from the lists created previously
		while(!queueVisit.isEmpty()){
			//pop from queue
			SimpleEntry<String,String> edge = queueVisit.poll();
			
			if(visited.contains(edge.getKey()) && visited.contains(edge.getValue())){
				// output in form: /wiki/Iowa_State_University /wiki/Ames,_Iowa
				writer.println(edge.getKey() + " " + edge.getValue());
			}
		}
		//memory leak possible without
		writer.close();
	}
}
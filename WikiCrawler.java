

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class WikiCrawler{
	static final String BASE_URL = "https://en.wikipedia.org/";
	
	private String seed;
	private int numPages;
	private ArrayList<String> keywords;
	private String outFile;

	public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, String fileName){
		this.seed = seedUrl;
		this.numPages = max;
		this.keywords = topics;
		this.outFile = fileName;

	}

	public void crawl() throws MalformedURLException, IOException, InterruptedException{
		//set up output file writer
		PrintWriter writer = new PrintWriter(outFile, "UTF-8");
		writer.println(numPages);
		
		//set up queue
		Queue<SimpleEntry<String, String>> toVisit = new LinkedList<SimpleEntry<String,String>>();
		toVisit.add(new SimpleEntry<String,String>("",seed));
		
		//set up list of visited pages
		HashSet<String> visited = new HashSet<String>();
		
		int relevantPages = 0;	//count number of pages with topics
		int counter = 0;		//counter to keep track of page requests
		
		while(!toVisit.isEmpty() && (relevantPages <= numPages));
			//one edge
			SimpleEntry<String,String> edge = toVisit.poll();
			String curPage = edge.getValue();
			
			//keep track of keywords on this page
			ArrayList<String> pageTopics = new ArrayList<String>();
			pageTopics.addAll(keywords);
			
			if(!visited.contains(curPage)){
				//set up scanner for current wiki page
				Scanner scanner = new Scanner((new URL(BASE_URL+curPage)).openStream());
				
				boolean actualContent = false;
				
				//keep track of links on the current page
				LinkedList<SimpleEntry<String,String>> links = new LinkedList<SimpleEntry<String,String>>();
				
				visited.add(curPage);
				
				while(scanner.hasNextLine()){
					String curLine = scanner.nextLine();
					
					//found actual content
					if(curLine.contains("<p>")){
						actualContent = true;
					}
					
					//find keywords
					if(actualContent && !pageTopics.isEmpty()){
						for(int i=0; i < pageTopics.size(); i++){
							if(curLine.contains(pageTopics.get(i))){
								pageTopics.remove(i);
							}
						}
					}
					
					//find links
					if(curLine.contains("href=\"/wiki/")){
						int index = curLine.indexOf("href=\"/wiki/") + 6;
						String nextPage = "";
						while(curLine.charAt(index)!='\"'){
							nextPage += curLine.charAt(index);
							index++;
						}
						if(!nextPage.contains(":") && !nextPage.contains("#")){
							links.add(new SimpleEntry<String,String>(edge.getValue(),nextPage));
						}
					}
					
					if(curLine.contains("<\\p>")) actualContent = false;		
					
				}
				
				boolean relevantPage = pageTopics.isEmpty();
				
				//if current page has all keywords
				if(relevantPage){
					relevantPages++;
					
					//add to queue
					toVisit.addAll(links);
					
					//write edge to output file
					if(!edge.getKey().equals("")){
						writer.println(edge.getKey() + " " + edge.getValue());
					}
				}
				
				scanner.close();
				counter++;
				
				//limit page requests
				if(counter == 25){
					counter = 0;
					Thread.sleep(3000);
				}	
			}
	}
}
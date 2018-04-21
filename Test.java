
import java.util.ArrayList;

public class Test{
	
	public static void main(String[] args) {
        NetworkInfluence ni = new NetworkInfluence("src/inftest.txt");
        
        ArrayList<String> s4 = ni.shortestPath("D","E");
        System.out.println(s4);
		
//        ArrayList<String> topics = new ArrayList<String>();
////		topics.add("Iowa State");
////		topics.add("Cyclones");
//		WikiCrawler w = new WikiCrawler("/wiki/Computer_Science", 100, topics, "WikiCS.txt");
//		//WikiCrawler w = new WikiCrawler("/wiki/Iowa_State_University", 100, topics, "WikiISU.txt");
//		w.crawl();
//		int i = 0;
//		i++;
//		System.out.println(i);
		 
		
		
    }
	
	
}
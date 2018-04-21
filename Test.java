
import java.util.ArrayList;

public class Test{
	
	public static void main(String[] args) {
        NetworkInfluence ni = new NetworkInfluence("src/WikiCS.txt");
        
        ArrayList<String> infDegree = new ArrayList<String>();
        ArrayList<String> infModular = new ArrayList<String>();
        ArrayList<String> infSubModular = new ArrayList<String>();
        
        infDegree = ni.mostInfluentialDegree(10);
        infModular = ni.mostInfluentialModular(10);
        infSubModular = ni.mostInfluentialSubModular(10);
        
        System.out.println("10 most influential degree greedy nodes");
        System.out.println(infDegree);
        System.out.println("10 most influential modular greedy nodes");
        System.out.println(infModular);
        System.out.println("10 most influential submodular greedy nodes");
        System.out.println(infSubModular);
        
        
        
        //ArrayList<String> s4 = ni.shortestPath("D","E");
       //System.out.println(s4);
		
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
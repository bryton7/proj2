package proj2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class tester {

	public static void main(String[] args) throws IOException, InterruptedException {
		ArrayList<String> topics = new ArrayList<String>();
//		topics.add("Iowa State");
//		topics.add("Cyclones");
		WikiCrawler w = new WikiCrawler("/wiki/Computer_Science", 100, topics, "WikiCS.txt");
		//WikiCrawler w = new WikiCrawler("/wiki/Iowa_State_University", 100, topics, "WikiISU.txt");
		w.crawl();
		int i = 0;
		i++;
		System.out.println(i);
	}

}

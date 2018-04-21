import java.util.ArrayList;

public class Test{
	
	public static void main(String[] args) {
        NetworkInfluence ni = new NetworkInfluence("src/inftest.txt");
        
        ArrayList<String> s4 = ni.shortestPath("D","E");
        System.out.println(s4);
		
		
		 
		
		
    }
	
	
}
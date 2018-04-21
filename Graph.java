import java.util.HashMap;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph{
		
		public LinkedList<String> adjList[];
		public HashMap<String, Integer> vertices;
		public String graphFile;
		public int numVertices;
		
		public Graph(String graphData) throws FileNotFoundException{
			this.graphFile = graphData;
			this.vertices = new HashMap<String, Integer>();
			this.populateList();
		}
		
		public void populateList() throws FileNotFoundException{
			Scanner scanner = new Scanner(new File(graphFile));
			
			String curLine = scanner.nextLine();
			this.numVertices = Integer.parseInt(curLine);
			
			adjList = new LinkedList[numVertices];
			for(int i = 0; i < numVertices; i++){
				adjList[i] = new LinkedList<String>();
			}
			
			int cntr = 0;
			while(scanner.hasNextLine()){
				curLine = scanner.nextLine();
				Scanner lineScanner = new Scanner(curLine);
				
				String vFrom = lineScanner.next();
				String vTo = lineScanner.next();
				
				if(!vertices.containsKey(vFrom)){
					vertices.put(vFrom, cntr);
					adjList[cntr].add(vFrom);
					cntr++;
				}
				if(!vertices.containsKey(vTo)){
					vertices.put(vTo, cntr);
					adjList[cntr].add(vTo);
					cntr++;
				}
				adjList[vertices.get(vFrom)].add(vTo);
				lineScanner.close();
			}
			scanner.close();
		}
		
		public int getOutDegree(String v){
			return adjList[vertices.get(v)].size() - 1;
		}
		
		
	}
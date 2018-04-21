

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import java.util.Collections;

public class NetworkInfluence{
	private String graphFile;
	private Graph graph;
	
	public NetworkInfluence(String graphData){
		this.graphFile = graphData;
		try {
			this.graph = new Graph(graphFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int outDegree(String v){
		return graph.getOutDegree(v);
	}
	
	public ArrayList<String> shortestPath(String u, String v){
		ArrayList<String> path = new ArrayList<>();
		HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
		
		if(!graph.vertices.containsKey(u) || !graph.vertices.containsKey(v)){
			return path;
		}
		
		if((u == v)){
			path.add(u);
			path.add(u);
			return path;
		}
		
		Queue<String> queue = new LinkedList<String>();
		Stack<String> stack = new Stack<String>();
		
		queue.add(u);
		stack.add(u);
		visited.put(u, true);
		
		while(!queue.isEmpty()){
			String curNode = queue.poll();
			
			for(String to : graph.adjList[graph.vertices.get(curNode)]){
				if(!visited.containsKey(to) && !curNode.equals(to)){
					queue.add(to);
					visited.put(to, true);
					stack.add(to);
					if(curNode.equals(v)){
						break;
					}
				}
			}
		}
		
		String node;
		String curSrc = v;
		path.add(v);
		
		while(!stack.isEmpty()){
			node = stack.pop();
			if(graph.adjList[graph.vertices.get(node)].contains(curSrc) && !path.contains(node)){	//might be backwards
				path.add(node);
				curSrc = node;
				if(node.equals(u)){
					break;
				}
			}
		}
		
		Collections.reverse(path);
		
		if(path.size() == 1){
			path.clear();
		}
		
		//System.out.println(path);
		
		return path;
	}
	
	public int distance(String u, String v){
		if(u.equals(v)){
			return 0;
		}
		ArrayList<String> path = shortestPath(u,v);
		return path.size() - 1;
	}
	
	public int distance(ArrayList<String> s, String v){
		int minDist = Integer.MAX_VALUE;
		int curDist;
		for(int i = 0 ; i < s.size(); i++){
			curDist = distance(s.get(i), v);
			if((curDist < minDist) && (curDist != -1)){
				minDist = curDist;
			}
		}
		
		if(minDist == Integer.MAX_VALUE){
			return -1;
		}
			
		return minDist;
	}
	
	public float influence(String u){
		float influence = 0;
		HashSet<String> visited = new HashSet<String>();
		
		
		
		
		return influence;
	}
	
	
}
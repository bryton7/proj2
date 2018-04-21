import java.io.FileNotFoundException;
import java.util.AbstractMap.SimpleEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.sun.org.apache.xpath.internal.axes.NodeSequence;

import java.util.ListIterator;

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
		Queue<SimpleEntry<String,Integer>> queue = new LinkedList<SimpleEntry<String,Integer>>();
		
		queue.add(new SimpleEntry<String,Integer>(u,0));
		
		while(!queue.isEmpty()){
			SimpleEntry<String,Integer> node = queue.poll();
			if(!visited.contains(node.getKey())){
				influence += 1 / Math.pow(2,node.getValue());
				visited.add(node.getKey());
				for(String v : graph.adjList[graph.vertices.get(node.getKey())]){
					queue.add(new SimpleEntry<String,Integer>(v,node.getValue()+1));
				}
			}	
		}
		
		return influence;
	}
	
	public float influence(ArrayList<String> s){
		float influence = 0;
		
		HashSet<String> visited = new HashSet<String>();
		Queue<SimpleEntry<String,Integer>> queue = new LinkedList<SimpleEntry<String,Integer>>();
		
		for(int i = 0; i < s.size(); i++){
			queue.add(new SimpleEntry<String,Integer>(s.get(i), 0));
		}
		
		while(!queue.isEmpty()){
			SimpleEntry<String,Integer> node = queue.poll();
			if(!visited.contains(node.getKey())){
				influence += 1 / Math.pow(2,node.getValue());
				visited.add(node.getKey());
				for(String v : graph.adjList[graph.vertices.get(node.getKey())]){
					queue.add(new SimpleEntry<String,Integer>(v,node.getValue()+1));
				}
			}	
		}
		
		return influence;
	}
	
	public ArrayList<String> mostInfluentialDegree(int k){
		ArrayList<String> mostInfluential = new ArrayList<String>();
		ArrayList<String> nodes = new ArrayList<String>();
		
		for(int i = 0; i < graph.adjList.length; i++){
			if(!nodes.contains(graph.adjList[i].get(0))){
				nodes.add(graph.adjList[i].get(0));
			}
		}
		
		ArrayList<SimpleEntry<String, Integer>> kInfluential = new ArrayList<SimpleEntry<String, Integer>>();
		
		for(int i = 0; i < graph.numVertices; i++){
			int curDegree = outDegree(nodes.get(i));
			
			if(i < k){
				kInfluential.add(new SimpleEntry<String, Integer>(nodes.get(i), curDegree));
			}
			else{
				int minDegree = kInfluential.get(0).getValue();
				int minIndex = 0;
				
				for(int j = 1; j < k; j++){
					if(kInfluential.get(j).getValue() < minDegree){
						minDegree = kInfluential.get(j).getValue();
						minIndex = j;
					}
				}
				if(curDegree > minDegree){
					kInfluential.set(minIndex, new SimpleEntry<String, Integer>(nodes.get(i), curDegree));
				}	
			}
		}
		
		for(SimpleEntry<String, Integer> v : kInfluential){
			mostInfluential.add(v.getKey());
			//System.out.println(v.getKey());
		}
		
		return mostInfluential;
		
	}
	
	
}
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

/**
 * 
 * @author brytonhayes johningwersen
 *
 */
public class NetworkInfluence{
	private String graphFile;	//path to file representing the graph
	private Graph graph;		//graph object for the given file
	
	/*
	 * Basic constructor
	 */
	public NetworkInfluence(String graphData){
		this.graphFile = graphData;
		try {
			this.graph = new Graph(graphFile);
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find file: " + graphFile);
		}
	}
	
	/*
	 * return out degree for v
	 * see getOutDegree() method in Graph class
	 */
	public int outDegree(String v){
		return graph.getOutDegree(v);
	}
	
	/*
	 * use BFS to find shortest path from u to v
	 */
	public ArrayList<String> shortestPath(String u, String v){
		ArrayList<String> path = new ArrayList<>();
		HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
		
		//empty path if node doesnt exist
		if(!graph.vertices.containsKey(u) || !graph.vertices.containsKey(v)){
			return path;
		}
		
		//if same node, path is u to u
		if((u == v)){
			path.add(u);
			path.add(u);
			return path;
		}
		
		//structures to store nodes
		Queue<String> queue = new LinkedList<String>();
		Stack<String> stack = new Stack<String>();
		queue.add(u);
		stack.add(u);
		
		visited.put(u, true);
		
		//do BFS
		while(!queue.isEmpty()){
			String curNode = queue.poll();
			//add all vertices with an edge from current node
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
		//create path from the stack
		path.add(v);
		while(!stack.isEmpty()){
			node = stack.pop();
			//endge from node to source
			if(graph.adjList[graph.vertices.get(node)].contains(curSrc) && !path.contains(node)){
				path.add(node);
				curSrc = node;
				if(node.equals(u)){
					break;
				}
			}
		}
		
		//path u to v
		Collections.reverse(path);
		
		//no path if only 1 node
		if(path.size() == 1){
			path.clear();
		}
		
		//System.out.println(path);
		
		return path;
	}
	
	/*
	 * return distance from u to v
	 * -1 if no path
	 */
	public int distance(String u, String v){
		//0 if same node
		if(u.equals(v)){
			return 0;
		}
		//ArrayList<String> path = shortestPath(u,v);
		return shortestPath(u,v).size() - 1;
	}
	
	/*
	 * return min distance from nodes in s to v
	 * -1 if doesn't exist
	 */
	public int distance(ArrayList<String> s, String v){
		int minDist = Integer.MAX_VALUE;
		int curDist;
		//find minum distance
		for(int i = 0 ; i < s.size(); i++){
			curDist = distance(s.get(i), v);
			if((curDist < minDist) && (curDist != -1)){
				minDist = curDist;
			}
		}
		//doesn't exist
		if(minDist == Integer.MAX_VALUE){
			return -1;
		}
			
		return minDist;
	}
	
	/*
	 * return influence of node u
	 */
	public float influence(String u){
		float influence = 0;
		
		//visited nodes
		HashSet<String> visited = new HashSet<String>();
		
		//set up queue for nodes to visit (name and distance)
		Queue<SimpleEntry<String,Integer>> queue = new LinkedList<SimpleEntry<String,Integer>>();
		queue.add(new SimpleEntry<String,Integer>(u,0));
		
		//while there are nodes to visit
		while(!queue.isEmpty()){
			SimpleEntry<String,Integer> node = queue.poll();
			//if not visited
			if(!visited.contains(node.getKey())){
				influence += 1 / Math.pow(2,node.getValue());	//doesnt work with distance method?
				visited.add(node.getKey());
				//for every vertex for which there is an edge from current vertex
				for(String v : graph.adjList[graph.vertices.get(node.getKey())]){
					//add to the queue
					queue.add(new SimpleEntry<String,Integer>(v,node.getValue()+1));
				}
			}	
		}
		
		return influence;
	}
	
	/*
	 * return influence of a set of nodes
	 */
	public float influence(ArrayList<String> s){
		float influence = 0;
		
		HashSet<String> visited = new HashSet<String>();
		
		//set up queue for nodes to visit (name and distance)
		Queue<SimpleEntry<String,Integer>> queue = new LinkedList<SimpleEntry<String,Integer>>();
		//add every vertex in s
		for(int i = 0; i < s.size(); i++){
			queue.add(new SimpleEntry<String,Integer>(s.get(i), 0));
		}
		
		while(!queue.isEmpty()){
			SimpleEntry<String,Integer> node = queue.poll();
			if(!visited.contains(node.getKey())){
				influence += 1 / Math.pow(2,node.getValue());	//doesn't work with distance method?
				visited.add(node.getKey());
				//add all vertices with an edge from current vertex to the queue
				for(String v : graph.adjList[graph.vertices.get(node.getKey())]){
					queue.add(new SimpleEntry<String,Integer>(v,node.getValue()+1));
				}
			}	
		}
		
		return influence;
	}
	
	/*
	 * return k most degree greedy influential nodes
	 */
	public ArrayList<String> mostInfluentialDegree(int k){
		//most influential nodes
		ArrayList<String> mostInfluential = new ArrayList<String>();
		//all nodes in the graph
		ArrayList<String> nodes = new ArrayList<String>();
		
		//add all nodes
		for(int i = 0; i < graph.adjList.length; i++){
			if(!nodes.contains(graph.adjList[i].get(0))){
				nodes.add(graph.adjList[i].get(0));
			}
		}
		
		//name and out degree of most influential nodes
		ArrayList<SimpleEntry<String, Integer>> kInfluential = new ArrayList<SimpleEntry<String, Integer>>();
		
		//for every node in the graph
		for(int i = 0; i < graph.numVertices; i++){
			int curDegree = outDegree(nodes.get(i));
			//add first k nodes
			if(i < k){
				kInfluential.add(new SimpleEntry<String, Integer>(nodes.get(i), curDegree));
			}
			else{
				int minDegree = kInfluential.get(0).getValue();
				int minIndex = 0;
				//find node with minimum out degree
				for(int j = 1; j < k; j++){
					if(kInfluential.get(j).getValue() < minDegree){
						minDegree = kInfluential.get(j).getValue();
						minIndex = j;
					}
				}
				//replace if smaller than the current nodes out degree
				if(curDegree > minDegree){
					kInfluential.set(minIndex, new SimpleEntry<String, Integer>(nodes.get(i), curDegree));
				}	
			}
		}
		
		//add nodes to the array list
		for(SimpleEntry<String, Integer> v : kInfluential){
			mostInfluential.add(v.getKey());
			//System.out.println(v.getKey());
		}
		
		return mostInfluential;
		
	}
	
	/*
	 * return k most modular greedy influential nodes
	 */
	public ArrayList<String> mostInfluentialModular(int k){
		//most influential nodes
		ArrayList<String> mostInfluential = new ArrayList<String>();
		//all nodes in the graph
		ArrayList<String> nodes = new ArrayList<String>();
		
		//add all nodes
		for(int i = 0; i < graph.adjList.length; i++){
			if(!nodes.contains(graph.adjList[i].get(0))){
				nodes.add(graph.adjList[i].get(0));
			}
		}
		
		//name and influence of most influential nodes
		ArrayList<SimpleEntry<String, Float>> kInfluential = new ArrayList<SimpleEntry<String, Float>>();
		
		//for every node in the graph
		for(int i = 0; i < graph.numVertices; i++){
			float curInfluence = influence(nodes.get(i));
			//add first k nodes
			if(i < k){
				kInfluential.add(new SimpleEntry<String, Float>(nodes.get(i), curInfluence));
			}
			else{
				float minInfluence = kInfluential.get(0).getValue();
				int minIndex = 0;
				//find node with minimum influence
				for(int j = 1; j < k; j++){
					if(kInfluential.get(j).getValue() < minInfluence){
						minInfluence = kInfluential.get(j).getValue();
						minIndex = j;
					}
				}
				//replace if smaller than the current nodes influence
				if(curInfluence > minInfluence){
					kInfluential.set(minIndex, new SimpleEntry<String, Float>(nodes.get(i), curInfluence));
				}	
			}
		}
		
		//add nodes to the array list
		for(SimpleEntry<String, Float> v : kInfluential){
			mostInfluential.add(v.getKey());
			//System.out.println(v.getKey());
		}
		
		return mostInfluential;
		
	}
	
	
	
}
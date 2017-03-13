package MainFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import Graph.Edge;
import Graph.GraphNode;

public class Main {
	private Stack<GraphNode> pathStack = new Stack<>();
	private List<Stack<GraphNode>> listOfPathStack = new ArrayList<>();
	
	private List<GraphNode> path = new ArrayList<>();
	private List<List<GraphNode>> paths = new ArrayList<>();
	
	private int numOfPaths = 0;
	
	private List<GraphNode> towns; // Directed Graph

	
	public Main() {
		BuildGraph();
		testOutputs();
	}
	
	/**
	 * This is the main method that goes through the 10 tests
	 */
	public void testOutputs() {
		ArrayList<String> route1 = new ArrayList<String>() {{
		    add("A");
		    add("B");
		    add("C");
		}};
		
		int d1 = findDistance(route1);
		printRoute(d1);
				
		ArrayList<String> route2 = new ArrayList<String>() {{
		    add("A");
		    add("D");
		}};
		
		int d2 = findDistance(route2);
		printRoute(d2);
		
		ArrayList<String> route3 = new ArrayList<String>() {{
		    add("A");
		    add("D");
		    add("C");
		}};
		
		int d3 = findDistance(route3);
		printRoute(d3);
		
		ArrayList<String> route4 = new ArrayList<String>() {{
		    add("A");
		    add("E");
		    add("B");
		    add("C");
		    add("D");
		}};
		
		int d4 = findDistance(route4);
		printRoute(d4);
		
		ArrayList<String> route5 = new ArrayList<String>() {{
		    add("A");
		    add("E");
		    add("D");
		}};
		
		int d5 = findDistance(route5);
		printRoute(d5);
		
		//The number of trips starting at C and ending at C with a maximum of 3 stops
		GraphNode C = findNode("C");
		pathStack.push(C);
		findPaths(C, C);
		printPaths(3);
		
		// The number of trips starting at A and ending at C with exactly 4 stops.
		GraphNode A = findNode("A");
		pathStack.push(A);
		findPathsWithNDepth(4, A, C, path);
		printPathsWithNDepth("A");
		
		// Shortest route (distance) from A to C.
		System.out.println(shortestPath(A, C)); 
		
		// Shortest route (distance) from B to B.
		GraphNode B = findNode("B");
		System.out.println(shortestPath(B, B));

		BuildGraph(); // clean up the graph (i.e. reset to original)
				
		// find all paths between C and C that have a distance < 30
		splitStartEnd(C); // split the node into two separate nodes
		GraphNode node = findNode("start");
		GraphNode end = findNode("end");
		findPathLessThanNDistance(30, node, end, path);
		printPathsWithNDepth("C");
	

	}

	/**
	 * This method recursively finds all paths that have a distance < a given amount starting and ending
	 * at two given nodes.
	 * 
	 * @param weightSoFar - distance of the path being traversed so far
	 * @param node - the starting node
	 * @param end - the goal/target node
	 * @param path - the list of nodes in the path currently being traversed
	 */
	public void findPathLessThanNDistance(int weightSoFar, GraphNode node, GraphNode end, List<GraphNode> path) {
		if (weightSoFar > 0 && node.getName().equals(end.getName())) { // we found a path, record path, and look for more
			List<GraphNode> temp = new ArrayList<>();
			temp.addAll(path);
			paths.add(temp);
		}
		else if (weightSoFar <= 0) { // we've reached the max depth and found no path, stop search
			return;
		}
		
		for (Edge e : findNode(node.getName()).getRoutes()) {
			path.add(node); // add the current node to the path
			findPathLessThanNDistance(weightSoFar - e.getDistance(), e.getToNode(), end, path); // recursively check each path
			path.remove(path.size() - 1); // remove the last element to clean up the path
		}
	}
	
	/**
	 * This method finds the shortest path (using Dijkstra's algorithm) to all nodes in a graph starting at a 
	 * given start node. 
	 * 
	 * @param start - the starting node
	 * @return 
	 */
	public int shortestPath(GraphNode start, GraphNode end) {
		PriorityQueue<GraphNode> pQueue = new PriorityQueue<>(10, (a,b) -> a.getDist() - b.getDist());
		
		if (start == end) {
			splitStartEnd(start); // split the node into two separate nodes
			start = findNode("start");
			end = findNode("end");
		}
		
		start.setDist(0);
		pQueue.offer(start);
		
		// while queue is not empty
		while (!pQueue.isEmpty()) {
			GraphNode currentNode = pQueue.poll();
			
			// if the current node is the target, we've found the shortest path - break loop
			if (currentNode.getName().equals(end.getName())) {
				return currentNode.getDist();
			}
			
			List<Edge> edges = currentNode.getRoutes();
			
			for (Edge e : edges) {
				GraphNode nextNode = e.getToNode();				
				int updatedDist = e.getDistance() + currentNode.getDist();
				
				if (updatedDist < nextNode.getDist()) {
					nextNode.setDist(updatedDist);
					pQueue.offer(nextNode);
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * This method splits a given node into two separate ones.
	 * Use this method for when we want to find the shortest path
	 * starting and ending at the same node.
	 * 
	 * @param node - the node we want to split
	 */
	public void splitStartEnd(GraphNode node) {	
		// create two new nodes
		GraphNode start = new GraphNode("start");
		GraphNode end = new GraphNode("end");
				
		List<Edge> tempEdges = new ArrayList<>();
		
		for (GraphNode n : towns) {
			for (Edge e : n.getRoutes()) {
				// for any edge from our node to another, add it to the start node
				if (e.getFromNode().getName().equals(node.getName())) {
					start.addRoute(new Edge(start, e.getToNode(), e.getDistance()));
				}
				
				// for any edge to our node from another, add it to the end node
				if (e.getToNode().getName().equals(node.getName())) {
					Edge newEdge = new Edge(n, end, e.getDistance());
					tempEdges.add(newEdge);
				}
			}
		}
		
		// iterate over the edges that need to be removed (we need this to avoid concurrent modification errors)
		for (Edge e : tempEdges) {
			GraphNode n = findNode(e.getFromNode().getName());
			n.addRoute(e);
		}
		
		// iterate over the edges of the node being split and add the outgoing edges to end (ensures the new graph looks like the original)
		for (Edge e : node.getRoutes()) {
			end.addRoute(new Edge(end, e.getToNode(), e.getDistance()));
		}
		
		towns.remove(findNode(node.getName())); // remove the node we just split
		
		List<Edge> tempRemoveEdge = new ArrayList<>();
		for (GraphNode n : towns) {
			for (Edge e : n.getRoutes()) {
				if (e.getToNode().getName().equals(node.getName())) {
					tempRemoveEdge.add(e);
				}
			}
			for (Edge e2 : tempRemoveEdge) {
				n.getRoutes().remove(e2);
			}
			tempRemoveEdge.clear();
		}
		
		// add the two new nodes to the list of towns/graph
		towns.add(start);
		towns.add(end);
		
		
	}
	
	/**
	 * This method recursively looks for paths with a specified length between
	 * two given nodes.
	 * 
	 * @param depth - the length of the path
	 * @param node - current node being traversed
	 * @param end - target node
	 * @param path - the path so far
	 * @return 
	 */
	public void findPathsWithNDepth(int depth, GraphNode node, GraphNode end, List<GraphNode> path) {
		if (depth == 0 && node.getName().equals(end.getName())) { // we found a path, stop search and return the path
			// create temp and add the path to temp (so we don't lose the path when it gets cleaned) and store it in the list of paths
			List<GraphNode> temp = new ArrayList<>();
			temp.addAll(path);
			paths.add(temp);
			return;
		}
		else if (depth == 0) { // we've reached the max depth and found no path, stop search
			return;
		}
		
		for (Edge e : node.getRoutes()) {
			path.add(node); // add the current node to the path
			findPathsWithNDepth(depth - 1, e.getToNode(), end, path); // recursively check each path
			path.remove(path.size() - 1); // remove the last element to clean up the path
		}
	}


	/**
	 * This method attempts to find the distance of a given route.
	 * If no route is found, print "NO SUCH ROUTE"
	 * 
	 * @param route - The route in question 
	 */
	public int findDistance(ArrayList<String> route) {
		String startTown = route.get(0); // store the name of the starting town so we can retrieve it from the towns list
		
		// find the startingNode and set it to the currentNode
		GraphNode currentNode = findNode(startTown);

	    int distanceSoFar = 0;
	    if (currentNode != null) {
	    	// iterate over the nodes in the route starting from 1 (because 0 is the start node)
	    	for (int i = 1; i < route.size(); i ++) {
	    	    boolean found = false; 
			    for (Edge e : currentNode.getRoutes()) {
			    	if (e.getToNode().getName().equals(route.get(i))) { // if the edge points to the next node in the route, store distance and move on to next town
			    		distanceSoFar += e.getDistance();
			    		currentNode = e.getToNode(); // set the currentNode to the nextNode so we can move on in the route
			    		found = true;
			    		break;
			    	}
			    }
			    if (!found) { // if we can't reach the next town from the current node, print no such route and exit
			    	return 0;
			    }
	    	}
	    }
	    else {
	    	return 0;
	    }
	    
	    return distanceSoFar;
	}
	
	/**
	 * Recursive method which goes through each node using brute force to find all paths
	 * that start and end at two given nodes
	 * 
	 */
	public void findPaths(GraphNode node, GraphNode end) {
		for (Edge e : node.getRoutes()) {
			if (e.getToNode().getName().equals(end.getName())) {
				Stack<GraphNode> temp = new Stack<>();
				for (GraphNode node1 : pathStack) {
					temp.add(node1);
				}
				temp.add(e.getToNode());
				listOfPathStack.add(temp);
				//findPaths(e.getToNode(), end);
			}
			else if (!pathStack.contains(e.getToNode())) {
				pathStack.push(e.getToNode());
				findPaths(e.getToNode(), end);
				pathStack.pop();
			}
		}
	}	
	
	
	/**
	 *  Helper method for finding the node in the graph by name.
	 *  
	 * @param nodeName - the name of the node
	 * @return the node from the list of towns
	 */
	public GraphNode findNode(String nodeName) {
	    for (GraphNode n : towns) {
	        if (n.getName().equals(nodeName)) {
	        	return n;
	        }
	    }
		return null;
	}
	
	/**
	 * Helper method for printing the route distance
	 * 
	 * @param distance - the distance/weight of the route
	 */
	public void printRoute(int distance) {
		if (distance == 0) {
			System.out.println("NO SUCH ROUTE");
		}
		else {
			System.out.println(distance);
		}
	}
	
	/**
	 * Helper method for printing the routes starting and ending at two given nodes
	 * 
	 * @param maxTrips - the number of maximum stops in each path
	 */
	public void printPaths(int maxTrips) {
		System.out.print("Paths: ");
		for (Stack<GraphNode> s : listOfPathStack) {
			if (s.size() == maxTrips) {
				continue;
			}
			
			System.out.print("(");

			for (GraphNode o : s) {
				System.out.print(o.getName() + "");
			}
			System.out.print("), ");

			numOfPaths++;
		}
		
		System.out.println(numOfPaths +" path/s total");
		
		// empty the path stack and paths list
		pathStack.clear();
		listOfPathStack.clear();
	}
	
	/**
	 * Helper method for pretty printing limited paths. (i.e. paths with a specified 
	 * weight/distance)
	 * 
	 * @param node - This parameter is only used when the node is split into "start" and "end" nodes.
	 */
	private void printPathsWithNDepth(String node) {
		System.out.print("Paths: ");
		
		for (List<GraphNode> p : paths) {
			System.out.print("(");
			
			for (GraphNode n : p) {
				if (n.getName().equals("start") || n.getName().equals("end")) {
					System.out.print(node);
				}
				else {
					System.out.print(n.getName());
				}
			}
			System.out.print("C");

			System.out.print("), ");
		}
		
		System.out.println(paths.size() +" paths total");
		
		paths.clear();
	}
	
	
	/**
	 * This method builds the directed graph used for testing
	 */
	public void BuildGraph() {
		towns = new ArrayList<GraphNode>();
		GraphNode a = new GraphNode("A");
		GraphNode b = new GraphNode("B");
		GraphNode c = new GraphNode("C");
		GraphNode d = new GraphNode("D");
		GraphNode e = new GraphNode("E");
		
		towns.add(a);
		towns.add(b);
		towns.add(c);
		towns.add(d);
		towns.add(e);
		
		// AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7
		Edge aEdge1 = new Edge(a, b, 5);
		Edge aEdge2 = new Edge(a, d, 5);
		Edge aEdge3 = new Edge(a, e, 7);
		a.addRoute(aEdge1);
		a.addRoute(aEdge2);
		a.addRoute(aEdge3);
		
		Edge bEdge1 = new Edge(b, c, 4);
		b.addRoute(bEdge1);
		
		Edge cEdge1 = new Edge(c, d, 8);
		Edge cEdge2 = new Edge(c, e, 2);
		c.addRoute(cEdge1);
		c.addRoute(cEdge2);

		Edge dEdge1 = new Edge(d, c, 8);
		Edge dEdge2 = new Edge(d, e, 6);
		d.addRoute(dEdge1);
		d.addRoute(dEdge2);
		
		Edge eEdge1 = new Edge(e, b, 3);
		e.addRoute(eEdge1);
		
	}
		
	public static void main(String[] args)  {
		new Main();
	}

	public Stack<GraphNode> getPathStack() {
		return pathStack;
	}

	public List<Stack<GraphNode>> getListOfPathStack() {
		return listOfPathStack;
	}

	public List<GraphNode> getPath() {
		return path;
	}

	public List<List<GraphNode>> getPaths() {
		return paths;
	}

	public List<GraphNode> getTowns() {
		return towns;
	}
	
	public int getNumOfPaths() {
		return this.numOfPaths;
	}
	
	public void setNumOfPaths(int i) {
		this.numOfPaths = i;
	}
	
	public void setTowns(List<GraphNode> t) {
		this.towns = t;
	}
}


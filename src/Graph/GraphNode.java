package Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * A GraphNode represents a city. Each city contains a list of outgoing
 * edges which represent the routes to different cities.
 *
 */
public class GraphNode {

	// list of outgoing edges to store  relationships with other towns
	private List<Edge> routes = new ArrayList<Edge>();
	
	private String name;
	public boolean visited = false;
	
	private int dist = Integer.MAX_VALUE;
	
	public GraphNode(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	public void addRoute(Edge e) {
		routes.add(e);
	}
	
	public List<Edge> getRoutes() {
		return this.routes;
	}
	
	public int getDist() {
		return this.dist;
	}
	
	public void setDist(int d) {
		this.dist = d;
	}
}

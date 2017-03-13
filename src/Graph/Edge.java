package Graph;

/**
 * An edge represents a route between two nodes (cities). The weighting of the
 * edge indicates the distance between the two cities (toNode and fromNode)
 *
 */
public class Edge {

	// the distance of this route
	public int distance;

	// the two nodes at each end of this edge
	public GraphNode fromNode;
	public GraphNode toNode;

	public Edge(GraphNode from, GraphNode to, int dist) {
		this.fromNode = from;
		this.toNode = to;
		this.distance = dist;
	}

	public GraphNode getFromNode() {
		return fromNode;
	}

	public GraphNode getToNode() {
		return toNode;
	}
	
	public int getDistance() {
		return this.distance;
	}

}

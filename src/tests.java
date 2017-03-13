import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Graph.Edge;
import Graph.GraphNode;
import MainFrame.Main;


public class tests {

	Main mf;
	
	public void setup() {
		this.mf = new Main();
		mf.BuildGraph();
	}
	
	/**
	 * 
	 */
	@Test
	public void testRoutes() {
		setup();
		
		ArrayList<String> route1 = new ArrayList<String>() {{
		    add("A");
		    add("B");
		    add("C");
		}};
		
		int d1 = mf.findDistance(route1);
		mf.printRoute(d1);
		assertTrue(d1 == 9); // ensure the distance of this path is 9
			
		ArrayList<String> route2 = new ArrayList<String>() {{
		    add("A");
		    add("D");
		}};
		
		int d2 = mf.findDistance(route2);
		mf.printRoute(d2);
		assertTrue(d2 == 5); // ensure the distance of this path is 5
		
		ArrayList<String> route3 = new ArrayList<String>() {{
		    add("A");
		    add("D");
		    add("C");
		}};
		
		int d3 = mf.findDistance(route3);
		mf.printRoute(d3);
		assertTrue(d3 == 13); // ensure the distance of this path is 13
		
		ArrayList<String> route4 = new ArrayList<String>() {{
		    add("A");
		    add("E");
		    add("B");
		    add("C");
		    add("D");
		}};
		
		int d4 = mf.findDistance(route4);
		mf.printRoute(d4);
		assertTrue(d4 == 22); // ensure the distance of this path is 22
		
		ArrayList<String> route5 = new ArrayList<String>() {{
		    add("A");
		    add("E");
		    add("D");
		}};
		
		int d5 = mf.findDistance(route5);
		mf.printRoute(d5);
		assertTrue(d5 == 0); // ensure the distance of this path is 0 - i.e "NO SUCH ROUTE"
	}
	
	@Test
	public void testPathsBetweenNodes() {
		setup();
		mf.setNumOfPaths(0); // reset number of paths
		mf.getPathStack().clear(); // reset path stack
		
		//The number of trips starting at C and ending at C with a maximum of 3 stops
		GraphNode C = mf.findNode("C");
		mf.getPathStack().push(C);
		mf.findPaths(C, C);
		mf.printPaths(3);
		assertTrue(mf.getNumOfPaths() == 2);
		
		// The number of trips starting at A and ending at C with exactly 4 stops.
		GraphNode A = mf.findNode("A");
		mf.getPathStack().push(A);
		mf.findPathsWithNDepth(4, A, C, mf.getPath());
		assertTrue(mf.getPaths().size() == 3);
	}
	
	@Test
	public void testShortestPaths() {
		setup();
		GraphNode A = mf.findNode("A");
		GraphNode B = mf.findNode("B");
		GraphNode C = mf.findNode("C");

		// Shortest route (distance) from A to C.
		assertTrue(mf.shortestPath(A, C) == 9); 
		
		// Shortest route (distance) from B to B.
		assertTrue(mf.shortestPath(B, B) == 9);
	}
	
	@Test 
	public void testPathsLessThan30() {
		setup();
		GraphNode C = mf.findNode("C");

		// find all paths between C and C that have a distance < 30
		mf.splitStartEnd(C); // split the node into two separate nodes
		GraphNode node = mf.findNode("start");
		GraphNode end = mf.findNode("end");
		mf.findPathLessThanNDistance(30, node, end, mf.getPath());
		assertTrue(mf.getPaths().size() == 7);
	}
	
	
	/**
	 * 
	 */
	@Test
	public void testRoutes2() {
		BuildGraph2();
		
		ArrayList<String> route1 = new ArrayList<String>() {{
		    add("A");
		    add("B");
		    add("C");
		}};
		
		int d1 = mf.findDistance(route1);
		mf.printRoute(d1);
		assertTrue(d1 == 5); // ensure the distance of this path is 5
						
		ArrayList<String> route2 = new ArrayList<String>() {{
		    add("A");
		    add("D");
		}};
		
		int d2 = mf.findDistance(route2);
		mf.printRoute(d2);
		assertTrue(d2 == 4); // ensure the distance of this path is 4
		
		ArrayList<String> route3 = new ArrayList<String>() {{
		    add("A");
		    add("D");
		    add("C");
		}};
		
		int d3 = mf.findDistance(route3);
		mf.printRoute(d3);
		assertTrue(d3 == 0); // ensure the distance of this path is 0 - i.e "NO SUCH ROUTE"
		
		ArrayList<String> route4 = new ArrayList<String>() {{
		    add("A");
		    add("E");
		    add("B");
		    add("C");
		    add("D");
		}};
		
		int d4 = mf.findDistance(route4);
		mf.printRoute(d4);
		assertTrue(d4 == 0); // ensure the distance of this path is 0 - i.e "NO SUCH ROUTE"
		
		ArrayList<String> route5 = new ArrayList<String>() {{
		    add("A");
		    add("E");
		    add("D");
		}};
		
		int d5 = mf.findDistance(route5);
		mf.printRoute(d5);
		assertTrue(d5 == 0); // ensure the distance of this path is 0 - i.e "NO SUCH ROUTE"
	}
	

	@Test
	public void testPathsBetweenNodes2() {
		BuildGraph2();
		mf.setNumOfPaths(0); // reset number of paths
		mf.getPathStack().clear(); // reset path stack
		
		//The number of trips starting at A and ending at C with a maximum of 2 stops
		GraphNode C = mf.findNode("C");
		mf.getPathStack().push(C);
		mf.findPaths(C, C);
		mf.printPaths(2);
		assertTrue(mf.getNumOfPaths() == 1); // only one path exists with maximum of 2 stops - CBC
		
		// The number of trips starting at A and ending at C with exactly 4 stops.
		GraphNode A = mf.findNode("A");
		mf.getPathStack().push(A);
		mf.findPathsWithNDepth(4, A, C, mf.getPath());
		assertTrue(mf.getPaths().size() == 1); // only one path exists with exactly 4 stops - ABCBC
	}
	
	
	@Test
	public void testShortestPaths2() {
		BuildGraph2();
		GraphNode A = mf.findNode("A");
		GraphNode B = mf.findNode("B");
		GraphNode C = mf.findNode("C");

		// Shortest route (distance) from A to C.
		assertTrue(mf.shortestPath(A, B) == 2); 
		
		// Shortest route (distance) from B to B.
		assertTrue(mf.shortestPath(C, C) == 4);
	}
	
	@Test 
	public void testPathsLessThan30V2() {
		BuildGraph2();
		GraphNode C = mf.findNode("C");

		// find all paths between C and C that have a distance < 10
		mf.splitStartEnd(C); // split the node into two separate nodes
		GraphNode node = mf.findNode("start");
		GraphNode end = mf.findNode("end");
		mf.findPathLessThanNDistance(10, node, end, mf.getPath());
		assertTrue(mf.getPaths().size() == 2); // only 2 paths with distance less than 10 exist - CBC (distance 4), CBCBC (Distance 8)
	}
	
	
	
	/**
	 * Another graph for testing methods
	 */
	public void BuildGraph2() {
		setup();
		
		List<GraphNode> towns = new ArrayList<GraphNode>();
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

		// AB2, AD4, BC3, BE2, CB1, ED2
		Edge aEdge1 = new Edge(a, b, 2);
		Edge aEdge2 = new Edge(a, d, 4);
		a.addRoute(aEdge1);
		a.addRoute(aEdge2);

		Edge bEdge1 = new Edge(b, c, 3);
		Edge bEdge2 = new Edge(b, e, 2);
		b.addRoute(bEdge1);

		Edge cEdge1 = new Edge(c, b, 1);
		c.addRoute(cEdge1);

		Edge eEdge1 = new Edge(e, d, 2);
		e.addRoute(eEdge1);
		
		mf.setTowns(towns); // set the towns in mainframe to this new graph
	}
	
}



import java.util.*;

/**
 * GraphLibrary.java 
 * 
 * @author Timothy Pierson (creating scaffold)
 * @author Jeremy Hadfields
 * @date May 13, 2019
 * @category Dartmouth CS10 19S, Problem Set 4 — Kevin Bacon Game
 */
public class GraphLibrary {
	/**
	 * Takes a random walk from a vertex, up to a given number of steps
	 * So a 0-step path only includes start, while a 1-step path includes start and one of its out-neighbors,
	 * and a 2-step path includes start, an out-neighbor, and one of the out-neighbor's out-neighbors
	 * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
	 * @param g		graph to walk on
	 * @param start	initial vertex (assumed to be in graph)
	 * @param steps	max number of steps
	 * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list;
	 * 			    null if start isn't in graph
	 */
	public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, int steps) {
		if (!g.hasVertex(start)) return null; // if g does not have a start vertex, can't walk
		
		List<V> returnPath = new ArrayList<V>(); // the final path of vertices to be returned
		returnPath.add(start); // path always contains the first element
		
		// walk randomly through the graph up to the specified number of steps
		int j = 0; // iterators
		while (j < steps - 1) {
			List<V> neighborVertices = new ArrayList<V>(); // to hold the neighboring items 
			
			if(g.outDegree(returnPath.get(returnPath.size() - 1)) != 0) {
				// go through neighbors and add each to the path
				for(V vertex : g.outNeighbors(returnPath.get(returnPath.size() - 1))) 
					neighborVertices.add(vertex);
				
				// choose random neighbor index
				int randIndex = (int) Math.random() * neighborVertices.size();
				returnPath.add(neighborVertices.get(randIndex));
			}
			j++;
		}
		
		return returnPath; // return the path thru the graph
	}
	
	
	/**
	 * Orders vertices in decreasing order by their in-degree
	 * @param g		graph
	 * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByInDegree(Graph<V,E> g, int low, int high) {
		System.out.println("Entered!");
		List<V> resultOrder = new ArrayList<V>();
		
		int maxDegree = 0; // the current largest in-degree
		
		if (low > high) System.err.println("The lower limit is greater than the upper limit!");
		
		for (Iterator<V> iter = g.vertices().iterator(); iter.hasNext();) {
			V currVertex = iter.next();
			boolean inLimits = g.inDegree(currVertex) >= low && g.inDegree(currVertex) <= high;
			// if the vertex is between low and high, add it to the list
			if (inLimits) resultOrder.add(currVertex);
			
		}
		
		// sorts the list in place by each element's in-degree
		resultOrder.sort((v1, v2) -> g.inDegree(v2) - g.inDegree(v1));
		
		return resultOrder;
	}
	
	/**
	 * Perform a breadth-first search from the current center of the graph universe
	 * 
	 * @param g — the graph 
	 * @param source — the center of the universe / starting node
	 * @return — a graph that traces visited nodes of the breadth first search
	 */
	public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
		Graph <V, E> searchTrace = new AdjacencyMapGraph<V, E>(); // keeps track of all the nodes visited
		Queue<V> q = new LinkedList<V>(); // the items-to-visit
		Set<V> visited = new HashSet<V>(); // the items already visited
		
		// ensure that source is in the queue, the graph, and the set
		q.add(source); visited.add(source); searchTrace.insertVertex(source);
		
		while (!q.isEmpty()) { // while there are still nodes to visit
			V currNode = q.remove();
			for (V nextNode : g.outNeighbors(currNode)) {
				// check if node been visited already 
				if(!visited.contains(nextNode)) {
					// mark it as visited
					visited.add(nextNode);
					// if not, it should be visited so add it to the queue 
					q.add(nextNode);
					// add the vertex to the trace of visited nodes
					searchTrace.insertVertex(nextNode);
					// insert an edge 
					searchTrace.insertDirected(nextNode, currNode, g.getLabel(nextNode, nextNode));
				}
			}
		}
		
		return searchTrace;
	}
	
	/**
	 * Make a path from the given vertex back to Ye Grand Old Center of the Universe
	 * 
	 * @param tree — a shortest path tree
	 * @param v — a single vertex
	 * @return — a list that represents the path from the vertex to the origin of the galaxy
	 */
	public static <V,E> List<V> getPath(Graph<V,E> tree, V current) {
		List<V> pathBackHome = new ArrayList<V>(); 
		pathBackHome.add(current); // we must start at the beginning
		
		if (!tree.hasVertex(current)) System.out.println("Current node does not exist! Vertex unreachable via BFS.");
		else {
			while (tree.outDegree(current) != 0) { // if there are still available branches in the tree
				pathBackHome.add(current); // add the node to the path back home
				// update current to be the next available item in the tree
				current = tree.outNeighbors(current).iterator().next();
			}
		}
		
		return pathBackHome; // ah if only, if only
	}
	
	/**
	 * Find all the lonely abandoned vertices that BFS missed 
	 * 
	 * @param graph
	 * @param subgraph — shortest path tree
	 * @return — a set of vertices that are in the graph but not the subgraph (not reached by BFS)
	 */
	public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
		Set<V> missing = new HashSet<V>(); 
		
		// loop through the graph's iterator, find all the items in the subgraph but not the graph
		for (V v : graph.vertices()) { 
			if (!subgraph.hasVertex(v)) missing.add(v);
		}
		
		return missing;
	}; 
	
	/**
	 * Find the average distance-from-root in a shortest path tree. 
	 * 
	 * @param tree 
	 * @param root
	 * @return
	 */
	public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
		double totalDistance = 0; // the total distance 
		
		// go through the tree and add the size of each shortest path to the total distance. 
		for (V leaf: tree.vertices()) { // go through the tree's vertex iterator. 
			List<V> shortestPath = GraphLibrary.getPath(tree, leaf);
			totalDistance += (shortestPath.size() - 1); 
		}
		
		// avg separation = total distance / number of vertices in the tree
		double avgSep = totalDistance / (tree.numVertices() - 1); 
		return avgSep; 
	};
}

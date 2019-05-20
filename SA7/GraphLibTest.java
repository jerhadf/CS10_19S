
/**
 * GraphLibTest.java 
 * 
 * @author Jeremy Hadfield
 * @date May 13, 2019
 * @category Dartmouth CS10 19S, Short Assignment 7
 */
public class GraphLibTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AdjacencyMapGraph<String,String> testGraph = new AdjacencyMapGraph<String,String>();
		
		// initialize graph with sample nodes 
		testGraph.insertVertex("First");
		testGraph.insertVertex("Second");
		testGraph.insertVertex("Third");
		testGraph.insertVertex("Fourth");
		testGraph.insertVertex("Fifth");
		testGraph.insertDirected("First", "Second", "edgeDirected");
		testGraph.insertDirected("First", "Third", "edgeDirected");
		testGraph.insertDirected("First", "Fourth", "edgeDirected");
		testGraph.insertDirected("First", "Fifth", "edgeDirected");
		testGraph.insertDirected("Second", "First", "edgeDirected");
		testGraph.insertDirected("Second", "Third", "edgeDirected");
		testGraph.insertDirected("Third", "First", "edgeDirected");
		testGraph.insertDirected("Third", "Second", "edgeDirected");
		testGraph.insertDirected("Third", "Fourth", "edgeDirected");
		testGraph.insertDirected("Fifth", "Second", "edgeDirected");
		testGraph.insertDirected("Fifth", "Third", "edgeDirected");
		
		// use GraphLib to walk through graph
		GraphLib.randomWalk(testGraph, "First", 4);
		
		// print vertices sorted by in degree
		System.out.println("\nVertices Sorted by In Degree:");
		System.out.println(GraphLib.verticesByInDegree(testGraph));
		
		// use GraphLib to order by decreasing in-degree
		GraphLib.verticesByInDegree(testGraph);
	}

}

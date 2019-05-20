import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * BaconGame.java — runs the Kevin Bacon game
 * 
 * COMMANDS FOR THE GAME: 
 * 
 * c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation
 * d <low> <high>: list actors sorted by degree, with degree between low and high
 * i: list actors with infinite separation from the current center
 * p <name>: find path from <name> to current center of the universe
 * s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high
 * u <name>: make <name> the center of the universe
 * q: quit game
 * 
 * @author Jeremy Hadfield
 * @date May 13, 2019
 * @category Dartmouth CS10 19S, Problem Set 4
 */
public class BaconGame {
	
	// save the filepath names as string instance variables 
	protected static final String actorsPath = "inputs/actors.txt";
	protected static final String moviesPath = "inputs/movies.txt";
	protected static final String movieActorsPath = "inputs/movie-actors.txt";
	protected static final String actorsTestFile = "inputs/actorsTest.txt";
	protected static final String moviesTestFile = "inputs/moviesTest.txt";
	protected static final String movieActorsTestFile = "inputs/movie-actorsTest.txt";
	
	// initialize data structures for storing actors, movies, IDs, and the graph
	private Map<String, String> actorMap;
	private Map<String, String> movieMap;
	private Map<String, Set<String>> movieActorMap;
	private Graph<String, Set<String>> universeCenterGraph;
	private Graph<String, Set<String>> masterGraph;
	private String universeCenter;
	
	
	/**
	 * Constructor for BaconGame, initializes all the maps and graphs and other instance variables. 
	 * 
	 * @param actorMap — map of actor ID --> actor name
	 * @param movieMap — map of movie ID --> movie name 
	 * @param idMap — map of movie ID --> list of actorIDs (contains all actors in movie)
	 * @param universeCenterGraph — 
	 * @param universeCenter — the current center of the universe
	 */
	public BaconGame() {
		readInFile fileReader = new readInFile(actorsPath, moviesPath, movieActorsPath);
		
		// create all of the maps using the fileReader class
		this.actorMap = fileReader.getActorMap();
		this.movieMap = fileReader.getMovieMap();
		this.movieActorMap = fileReader.getMovieActorMap();
	
		// initialize the graphs
		this.masterGraph = new AdjacencyMapGraph<String, Set<String>>();
		this.universeCenterGraph = new AdjacencyMapGraph<String, Set<String>>();
		
		// create the original graph to store relationships between actors and movies
		makeMasterGraph();
		
		// initialize the center of the universe
		this.universeCenter = "Kevin Bacon";
		setUniverseCenter(universeCenter);
	}

	// GETTERS
	public Map<String, String> getActorMap() {
		return actorMap;
	}

	public Map<String, String> getMovieMap() {
		return movieMap;
	}

	public Map<String, Set<String>> getMovieActorMap() {
		return movieActorMap;
	}

	public Graph<String, Set<String>> getMasterGraph() {
		return masterGraph;
	}

	public Graph<String, Set<String>> getUniverseCenterGraph() {
		return universeCenterGraph;
	}
	
	public String getUniverseCenter() {
		return universeCenter;
	}

	// SETTERS
	public void setActorMap(Map<String, String> actorMap) {
		this.actorMap = actorMap;
	}

	public void setMovieMap(Map<String, String> movieMap) {
		this.movieMap = movieMap;
	}

	public void setMovieActorMap(Map<String, Set<String>> movieActorMap) {
		this.movieActorMap = movieActorMap;
	}

	public void setMasterGraph(Graph<String, Set<String>> masterGraph) {
		this.masterGraph = masterGraph;
	}

	public void setUniverseCenter(String universeCenter) {
		this.universeCenter = universeCenter; // update the center of the universe
		
		// create a new graph based on the new center of universe and the old graph
		universeCenterGraph = GraphLibrary.bfs(masterGraph, universeCenter);
		
		// find the number of actors connected to the center of the universe in the new graph
		int numActors = universeCenterGraph.numVertices() - GraphLibrary.missingVertices(universeCenterGraph, universeCenterGraph).size();
		
		// find the average separation of the updated graph
		double avgSeparation = GraphLibrary.averageSeparation(universeCenterGraph, universeCenter);
		
		// print out the new center of the universe
		System.out.println("CENTER OF UNIVERSE UPDATED TO: " + universeCenter + "\n" +
				"The new graph has an average seperation of: " +  + avgSeparation + "\n" + 
				"and the center of the universe is connected to " + numActors + "/" + actorMap.size() + 
				" of all the actors in the map.\n");
	}
	
	// HELPER METHODS
	/**
	 * Initializes the master graph. Works by adding a vertex for each actor,
	 * then adding edges between actors that shared movies together.

	 * @throws NoSuchFieldException if one of the movieIDs is null
	 */
	private void makeMasterGraph() {
		// add all actor IDs to the actorMap as vertices
		Set<String> actorIDSet = actorMap.keySet();
		Set<String> movieIDSet = movieMap.keySet();
		
		// insert all of the actor IDs into the master graph as vertices
		for (String id : actorIDSet) masterGraph.insertVertex(actorMap.get(id));
			
		// loop through all the movie IDs
		Iterator<String> movieItr = movieIDSet.iterator(); 
		
		while(movieItr.hasNext()){
			// get the set of actors in the next movie
			String movieName = movieMap.get(movieItr.next());
			Set<String> currMovieActors = movieActorMap.get(movieName); 
			if (currMovieActors == null) continue;
			
			for (String actorName : currMovieActors) {
				// loop over each costar in that movie
				for (String extraActor : currMovieActors) {
					if (actorName != extraActor) { // if the current actor is not the same as the costar
						if (!masterGraph.hasEdge(actorName, extraActor)) // add an edge if there isn't one already
							masterGraph.insertUndirected(actorName, extraActor, new HashSet<String>());
		
						// find the label between the actor and the costar, and add the movie to the label set
						masterGraph.getLabel(actorName, extraActor).add(movieName);
					}
				}
			}
		}
	}
	
	/**
	 * Helper method to return the actor's # of steps from center of universe
	 * @param actor — name of actor
	 * @return the integer degree of the actor
	 */
	private int actorDegree(String actorName) {
		List<String> path = GraphLibrary.getPath(universeCenterGraph, actorName);
		int pathLength = path.size() - 1;
		return pathLength;
	}
	
	// EVENT HANDLERS (respond to user input)
	

	/**
	 * list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation
	 * @param input
	 */
	public void handleC(String[] input) {
		// create a map to store actor : average separation of actor
		Map<String, Double> avgSepMap = new HashMap<String, Double>();
		
		// loop over all actors, making each center of universe, and adding their average separation to the map
		for (String actName : universeCenterGraph.vertices())
			avgSepMap.put(actName, GraphLibrary.averageSeparation(GraphLibrary.bfs(masterGraph, actName), actName)); 
		
		// go through all the actors and sort them by their separation
		List<String> sortedActList = sortBySep(avgSepMap);
		
		// num = the number of top centers to sort
		int limit = Integer.parseInt(input[1]);
		int indx = Math.abs(limit); // used as iterator
		
		// use a list to store the centers of the universe list
		List<String> centerList = new ArrayList<String>();
		
		// sort the list of actors from top to bottom
		while (indx > 0) {
			if (limit > 0) centerList.add(sortedActList.remove(sortedActList.size()-1));
			else centerList.add(sortedActList.remove(0));
			indx--;
		}
		
		System.out.println("Centers of universe up to "+limit+" in ascending order by average separation:"+ centerList);
		
	}
	
	/**
	 * If 'd' is pressed, list actors sorted by degree, with degree between low and high
	 * @param input — must follow format d <# low> <# high>
	 */
	public void handleD(String[] input) {
		int lowLimit = Integer.parseInt(input[1]);
		int highLimit = Integer.parseInt(input[2]);
		System.out.println("List of actors sorted by degree, between "+lowLimit+" and "+highLimit+": ");
		System.out.println(GraphLibrary.verticesByInDegree(universeCenterGraph, lowLimit, highLimit));
	}
	
	/**
	 * If 'i' is pressed, print the list of actors that have infinite separation from the center of universe
	 * @param input — a single character 'i'
	 */
	public void handleI(String[] input) {
		//TODO check if this is right - is it really universeCenterGraph? 
		System.out.println("Set of actors completely separated from universe center "+universeCenter+": ");
		System.out.println(GraphLibrary.missingVertices(masterGraph, universeCenterGraph));
	}

	/**
	 * If 'p' is pressed, find the shortest path from the center of the universe to the actor 
	 * @param input - a single char 'p' plus the actor name 
	 */
	public void handleP(String[] input) {
		String actorName = findActorName(input);
		
		// create a set of all missing actors, with infinite separation
		Set<String> missing = GraphLibrary.missingVertices(masterGraph, universeCenterGraph);
		
		// check if there are any connections between actor and universe center
		boolean isMissing = missing.contains(actorName); 
		if (isMissing) System.err.println("There are no shared movies between " + actorName + " and " + universeCenter + ".");
		
		if (masterGraph.hasVertex(actorName)) {
			// store the shortest path from the actor to the center of the universe
			List<String> path = GraphLibrary.getPath(universeCenterGraph, actorName);
			
			// print the actor's Bacon (or other) number
			int actorNum = path.size() - 1;
			System.out.println(actorName+ "'s number is " + actorNum);
			
			// loop through the graph, printing the connections between each actor in the path
			String currentActor = universeCenter;
			for (int i = 1; i < path.size(); i++) { 
				String nextActor = path.get(i);
				String sharedMovies = masterGraph.getLabel(currentActor, nextActor).toString();
				System.out.println(currentActor + " and " + nextActor + " were costars in " + sharedMovies);
				currentActor = nextActor; 
			}	
		} else {
			System.err.println("Actor name not found in the graph! Try a different name.");
		}
	}
	
	/**
	 * Helper method to save an actor name as a String using a String[] array input
	 */
	public String findActorName(String[] input) {
		String actorName = input[1]; // used to store actor name
		if (!(actorName instanceof String)) System.out.println("Must enter actor name as String!");
		// loop through input to create actorName, since actor name can be more than one word
		for (int j = 2; j < input.length; j++) actorName += " " + input[j];
		return actorName;
	}
	
	/**
	 * If 's' is pressed, list actors sorted by non-infinite separation from the current center between low and high
	 * @param input — 's' <low> <high>
	 */
	public void handleS(String[] input) {
		List<String> connectedActors = new ArrayList<String>(); // actors with connection to center
		int lowLimit = Integer.parseInt(input[1]);
		int highLimit = Integer.parseInt(input[2]);
		
		for (String currActor : universeCenterGraph.vertices()) {
			int actorPathLength = GraphLibrary.getPath(universeCenterGraph, currActor).size() - 1;
			// add the actor to the set if the path from actor to universe center is within the limits
			if (actorPathLength >= lowLimit && actorPathLength <= highLimit) connectedActors.add(currActor);
		}
		// sort by path length from shortest to longest
		connectedActors.sort((String actor1, String actor2) -> pathLengthDifference(actor1, actor2));

		System.out.println("List of actors separated by between "+lowLimit+" to "+highLimit+" "
				+ "steps from the universe center: "+connectedActors);
		System.out.println("There are "+connectedActors.size() + " actors seperated from universe center by "
				+lowLimit+" to "+highLimit+" steps.");
	}
	
	/**
	 * Helper method for handleS(), returns the difference in path length between two actors
	 */
	public int pathLengthDifference(String act1, String act2) {
		int pathLength1 = GraphLibrary.getPath(universeCenterGraph, act1).size(); 
		int pathLength2 = GraphLibrary.getPath(universeCenterGraph, act2).size(); 
		return pathLength1 - pathLength2;
	}
	
	/**
	 * If 'u' is pressed, make <name> the center of the universe
	 * @param input - an actor name
	 */
	public void handleU(String[] input) {
		if (input.length > 1) {
			String actorName = findActorName(input);
			setUniverseCenter(actorName);
		} else {
			System.out.println("Must enter an actor name!");
		}
	}
	
	
	/**
	 * Helper method to sort a map of actors by the actor's average separations
	 */
	public List<String> sortBySep(Map<String, Double> avgSeparationMap) {
		ArrayList<String> sortedActors = new ArrayList<String>(); // list of actors sorted by their separation
		
		// go through all the actors in the map, find their separation, and sort by separation
		for (String key : avgSeparationMap.keySet()) {
			int actIndex = sortedActors.size();
			while (actIndex > 0 && avgSeparationMap.get(sortedActors.get(actIndex - 1)) >= avgSeparationMap.get(key)) {
				actIndex--;
			}
			sortedActors.add(actIndex,key);
		}
		return sortedActors;
	}
	
	// GAME RUNNER METHODS
	
	public void runGame() {
		Scanner in = new Scanner(System.in);
		
		// start the game and handle any user input
		if (masterGraph.hasVertex(universeCenter)) System.out.println("****NOW PLAYING: THE "+universeCenter+" GAME****");
		while (true) {
			System.out.println("\nENTER A COMMAND BELOW:");
			String strInput = in.nextLine();
			String[] inputTerms = strInput.split(" ");
			
			// handle each possible character
			switch(inputTerms[0]) {
				case "c": 
					if (inputTerms.length > 1) handleC(inputTerms);
					else System.out.println("Must enter number <#>!");
					break;
				case "d": 
					handleD(inputTerms);
					break;
				case "i":
					if (universeCenter=="" | !masterGraph.hasVertex(universeCenter)) 
						System.err.println("Must select a valid center of universe first.");
					else handleI(inputTerms);
					break;
				case "p":
					if (universeCenter=="" | !masterGraph.hasVertex(universeCenter)) 
						System.err.println("Must select a valid center of universe first.");
					else handleP(inputTerms);
					break;
				case "s":
					if (inputTerms.length > 1) handleS(inputTerms);
					else System.out.println("Must enter Integer <low> and Integer <high>!");
					break;
				case "u": 
					handleU(inputTerms);
					break;
				case "q": 
					System.out.println("GAME ENDED! Thanks for playing :)");
					System.exit(0);
					in.close();
				default:
					System.out.println("That input is not a valid command.");
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BaconGame gameRunner = new BaconGame();
		System.out.println("GAME COMMANDS:\n"
				+ "* c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation\r\n" + 
				" * d <low> <high>: list actors sorted by degree, with degree between low and high\r\n" + 
				" * i: list actors with infinite separation from the current center\r\n" + 
				" * p <name>: find path from <name> to current center of the universe\r\n" + 
				" * s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high\r\n" + 
				" * u <name>: make <name> the center of the universe\r\n" + 
				" * q: quit game\n");
		gameRunner.runGame();
	}

}

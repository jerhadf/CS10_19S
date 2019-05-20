import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * readInFile.java 
 * Handles all of the file reading and map creation.
 * 
 * @author Jeremy Hadfield
 * @date May 15, 2019
 * @category Dartmouth CS10 19S, Problem Set 4
 */
public class readInFile {
	public BufferedReader inputFile; // used to read in the main file
	
	// store all of the maps as instance variables
	private Map<String, String> actorMap;
	private Map<String, String> movieMap;
	private Map<String, Set<String>> movieActorMap;

	/**
	 * Read in the file and save the output to the fileMap instance variable
	 * @param filename — the path to the file (usually in inputs/ folder)
	 */
	public readInFile(String actorPath, String moviePath, String movieActorPath) {
		// initialize each map type
		actorMap = new HashMap<String, String>();
		movieMap = new HashMap<String, String>();
		movieActorMap = new HashMap<String, Set<String>>();
		
		// generate maps using given file paths
		makeFileMap(actorPath, "actor");
		makeFileMap(moviePath, "movie");
		makeFileMap(movieActorPath, "movie-actor");
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
	
	// HELPER METHODS
	/**
	 * Helper method that opens a file and throws error if it cannot be found
	 * @param filename
	 */
	private BufferedReader openFile(String filename) {
		// read in the file
		try {
			inputFile = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find file!");
			e.printStackTrace();
		} finally {
			return inputFile;
		}
	}
	
	/**
	 * Helper method that converts a file into a map.
	 * Can handle actor files, movie files, or movie-actor files.
	 * @param filename
	 * @param filetype — the type of file. Can be "actor", "movie", or "movie-actor"
	 */
	private void makeFileMap(String filename, String filetype) {
		BufferedReader inputFile = openFile(filename);
		
		// process the file into the map of Strings to Strings
		try {
			// read each line in the file and add the data to the map
			String line = inputFile.readLine(); 
			
			while(line != null) {
				// split by pipe character
				String[] splitLine = line.split("\\|");
				// add the line to the respective map
				if (filetype.equals("actor")) actorMap.put(splitLine[0], splitLine[1]);
				else if (filetype.equals("movie")) movieMap.put(splitLine[0], splitLine[1]);
				// add the line to the movie-actors map
				else {
					String movie = movieMap.get(splitLine[0]);
					String actor = actorMap.get(splitLine[1]);
					// if the movie has not yet been added to the map, add it. 
					if (!movieActorMap.containsKey(movie)) movieActorMap.put(movie, new HashSet<String>());
					// add the actor to the movie in the map of movies to actors
					movieActorMap.get(movie).add(actor);
				}
				
				// move on to the next line
				line = inputFile.readLine();
			}
			
			inputFile.close();
		} catch (IOException e) {
			System.err.print("Input/output error in reading file:" + e.getMessage());
		} 
	}
}

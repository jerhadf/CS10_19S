import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MarkovModel.java 
 * 
 * @author Jeremy Hadfield '21 (partner)
 * @author Adam McQuilkin '22 (partner)
 * @date May 16, 2019
 * @category Dartmouth CS10 19S, PS-5
 */
public class MarkovModel {
	
	// A Markov Model (simulating a graph using a hash map).
	// keys: all the possible parts of speech tags
	// values: all the *next* POS tags that occur and their probabilities
	private Map<String, HashMap<String, Double>> transitionMap;			// [Part of Speech -> [New Part of Speech -> Probability]]
	private Map<String, HashMap<String, Double>> observationMap;		// [Part of Speech -> [Contains Word -> Probability]]
	
	private Map<String, Integer> wordFreqMap;							// how many times each word has been encountered
	private Map<String, String> textToLabel;							// Each word to its type of speech
	
	// Number of words processed (for probability)
	private int numWords;
	
	// Penalty for an unseen word
	private double unseenPenalty = -100;
	
	// End of sentence indicator (period)
	Set<String> endOfSentence = new HashSet<String>();
	
	// Graph starting signifier
	String startString = "#";
	
	// The locations of input files
	private String textFilepath; 
	private String labelFilepath;
	
	// The set of characters to ignore
	private Set<Character> charsToIgnore = new HashSet<Character>();
	
	/**
	 * Initialize model
	 */
	public MarkovModel(String textLocation, String labelLocation) {
		// Initialize maps
		transitionMap = new HashMap<String, HashMap<String, Double>>();
		wordFreqMap = new HashMap<String, Integer>();
		textToLabel = new HashMap<String, String>();
		observationMap = new HashMap<String, HashMap<String, Double>>();
		
		// Initialize counted words
		numWords = 0;
		
		// Save file location
		this.textFilepath = textLocation;
		this.labelFilepath = labelLocation;
		
		// Add characters to ignore in files, like punctuation (ex. '\n' or ']')
		final char[] ignoredChars = {'\n', '\t', ' ', ',', ':', '-', '(', ')', '[', ']', '*', '/', '\\'};
		for (char c : ignoredChars) charsToIgnore.add(c);
		
		final String[] finalPunct = {".", "!", "?"};
		for (String s : finalPunct) endOfSentence.add(s);
	}
	
	/**
	 * Return the number of words the algorithm has processed
	 */
	public int getNumWords() {
		return numWords;
	}
	
	/**
	 * Return the map of observations ([Part of Speech -> [Word -> Probability]])
	 */
	public Map<String, HashMap<String, Double>> getemissionMap() {
		return observationMap;
	}
	
	/**
	 * Return the HMM Map ([Part of Speech -> [Part of Speech -> Probability]])
	 */
	public Map<String, HashMap<String, Double>> getTransitionMap() {
		return transitionMap;
	}
	
	/**
	 * Convert a given file into a list of lines (in the form of a list of strings)
	 * @param filePath
	 * @return
	 */
	public ArrayList<ArrayList<String>> parseFile(String filePath){
		// the list of all lines in the file, where each line is a list of words
		ArrayList<ArrayList<String>> fileList = new  ArrayList<ArrayList<String>>();
		
		int lineIndex = 0;	// Which line is the program on?
		
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
			
			int r = fileReader.read(); 		// returns -1 if get to end of file
			String currReadString = "";
			
			while (r != -1) {
				
				// Add each char to the current string
				currReadString += (char) r;
				
				// If sentence has ended, convert sentence to 
				if (r == '\n') {
					fileList.add(lineIndex, parseLine(currReadString));
					currReadString = "";
					lineIndex++;
				}
				
				// Read another char
				r = fileReader.read();
			}
			
			// If the program ends without reading a line
			if (currReadString.length() != 0) {
				fileList.add(lineIndex, parseLine(currReadString));
			}	
			
			fileReader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileList;
	}
	
	/**
	 * Helper method for parseFile
	 * Convert a given string into a list of words or tags
	 * @return
	 */
	private ArrayList<String> parseLine(String lineString) {
		
		ArrayList<String> lineWords = new ArrayList<String>();
		String currWord = "";
		
		// Process line char by char
		for (int i = 0; i < lineString.length(); i++){
		    char c = lineString.charAt(i);
		    
		    // Remove all unwanted characters
		    if (charsToIgnore.contains(c) || endOfSentence.contains(Character.toString(c))) {
		    	if (currWord.length() > 0) {
			    	lineWords.add(currWord.toLowerCase());
			    	currWord = "";
		    	}
		    	continue;
		    }
		    
		    currWord += c;
		}
		System.out.println(lineWords);
		return lineWords;
	}
	
	
	/**
	 * Generate maps
	 * @param filepath
	 */
	public void generateMaps() {
		
		try {
			// Readers for reading text and label data from file
			BufferedReader textReader = new BufferedReader(new FileReader(textFilepath));
			BufferedReader labelReader = new BufferedReader(new FileReader(labelFilepath));
			
			// Initialize the holder variables for read() function
			// (reading in char by char)
			int currTextInt = textReader.read(); 		// returns -1 if get to end of file
			int currLabelInt = labelReader.read();		// returns -1 if get to end of file
			
			// Previous values of the system (for transitions)
			String prevTextString = startString;	// Signifies starting point in graph
			String prevLabelString = startString;
			
			boolean sentenceEnded = false;
			
			// Parse the text file (main processing loop)
			while (currTextInt != -1 && currLabelInt != -1) { 	// as long as we have not reached end of file (nextLine = -1)
				
				// Current word or label reading
				String currTextString = "";
				String currLabelString = "";
				
				// Load the current word into string
				while (currTextInt != -1) {
					if (charsToIgnore.contains((char) currTextInt)) {
						break;					
					}
					currTextString += (char) currTextInt;
					currTextInt = textReader.read();
				}
				
				// Load the current label into string
				while (currLabelInt != -1) {
					if (charsToIgnore.contains((char) currLabelInt)) {	//  || currLabelInt == endOfSentence
						break;					
					}
					
					currLabelString += (char) currLabelInt;
					currLabelInt = labelReader.read();
				}
				
				// Check if the sentence has ended
				for (String s : endOfSentence) {
					if (currTextString.equals(s) && currLabelString.equals(s)) {
						sentenceEnded = true;
					}
				}
				
				// Ignore all ' ' or '\n' characters for text
				while (charsToIgnore.contains((char) currTextInt)) {
					currTextInt = textReader.read();
				}
				
				// Ignore all ' ' or '\n' characters for label
				while (charsToIgnore.contains((char) currLabelInt)) {
					currLabelInt = labelReader.read();
				}
				
				//---------------------------------------------------//
				// Code here for running on words and keys from current loop
				
				// Standardize formatting for words and parts of speech
				currTextString.toLowerCase();
				currLabelString.toUpperCase();
				
				// Add word to word -> part of speech map
				textToLabel.put(currTextString, currLabelString);
				
				//---------------------//
				// Add word to part of speech -> words map if not already present
				
				// If the part of speech (label) hasn't been seen before, initialize map
				if (!observationMap.containsKey(currLabelString)) {
					observationMap.put(currLabelString, new HashMap<String, Double>());
				}
				
				// If word has been seen before, add 1.0 to frequency (double)
				if (observationMap.get(currLabelString).containsKey(currTextString)) {
					observationMap.get(currLabelString).put(currTextString, observationMap.get(currLabelString).get(currTextString) + 1.0);
				}
				
				// If word hasn't been seen for a given part of speech, load it as been seen once
				else {
					observationMap.get(currLabelString).put(currTextString, 1.0);	// Load previous words and frequencies
				}
				
				//---------------------//
				// Add word to frequency map
				
				// If word is already in frequency map
				if (wordFreqMap.get(currTextString) != null) {
					// increment frequency of the word by 1
					wordFreqMap.put(currTextString, wordFreqMap.get(currTextString) + 1);
				}
				
				// If not, initialize the frequency to 1
				else {
					wordFreqMap.put(currTextString, 1);
				}
				
				//---------------------//
				// Reset the sentence if necessary
				
				if (sentenceEnded) {
					prevTextString = startString;
					prevLabelString = startString;
					sentenceEnded = false;
				}
				
				else {
					//---------------------//
					// Load current transition into Hidden Markov Model
					
					// If the node hasn't been seen before, initialize it in the transition map
					if (!transitionMap.containsKey(prevLabelString)) {
						transitionMap.put(prevLabelString, new HashMap<String, Double>());
					}
					
					// If the transition hasn't been seen before, initialize it
					if (!transitionMap.get(prevLabelString).containsKey(currLabelString)) {
						transitionMap.get(prevLabelString).put(currLabelString, 0.0);
					}
					
					// Update the frequency of the transition 
					transitionMap.get(prevLabelString).put(currLabelString, transitionMap.get(prevLabelString).get(currLabelString) + 1.0);
					
					//---------------------//
					// Adjust previous strings
					prevTextString = currTextString;
					prevLabelString = currLabelString;
				}
				
				//---------------------//
				// Add one to total words iterated
				numWords++;
			}
			
			textReader.close();
			labelReader.close();
			
		} catch (Exception e) {
			System.err.println("Error while loading system maps... — " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Converts all frequencies to the value of log base e of the probabilities
	 */
	public void setToLog() {
		convertTransLog();
		convertObsLog();
	}
	
	/**
	 * Helper method for setToLog()
	 * Converts transitionMap frequencies to probabilities, and then sets them to log base e of the probability. 
	 */
	public void convertTransLog() {
		// Set transitionMap probabilities to log base e
		for (String tag : transitionMap.keySet()) {	// Loop through all values in "first-level" of map
			Map<String, Double> tagMap = transitionMap.get(tag);
			
			double tagSum = 0; // the total frequency of this tag
			for (String t1 : tagMap.keySet()) tagSum += tagMap.get(t1); // add the tag's frequency to the sum
			
			for (String t2 : tagMap.keySet()) {
				if (transitionMap.containsKey(tag) && tagMap.containsKey(t2)) {
					double tagProbability = tagMap.get(t2) / tagSum;
					tagMap.put(t2, Math.log(tagProbability));
				}
			}
		}
	}
	
	/**
	 * Helper method for setToLog()
	 * Converts observationMap frequencies to probabilities, and then sets them to log base e of the probability. 
	 */
	public void convertObsLog() {
		for (String tag : observationMap.keySet()) {	// Loop through all values in "first-level" of map
			Map<String, Double> wordMap = observationMap.get(tag);
			
			double wordSum = 0; // the total frequency of this word
			for (String word : wordMap.keySet()) wordSum += wordMap.get(word); // add the word's frequency to the sum
			
			for (String word : wordMap.keySet()) {
				if (observationMap.containsKey(tag) && wordMap.containsKey(word)) {
					double wordProbability = wordMap.get(word) / wordSum;
					observationMap.get(tag).put(word, Math.log(wordProbability));
				}
			}
		}
	}
	
	/**
	 * Applies the vitterbi algorithm
	 * 
	 * @param wordList — a list of words parsed in from the file
	 * @return — a list of tags
	 */
	public List<Map<String, String>> vitterbiAlgorithm(List<String> wordList) {
		// backtrack list holds all the options that led to the final conclusion
		List<Map<String, String>> backTrace = new ArrayList<Map<String, String>>();
		
		// the toal number of observations
		int observationNum = wordList.size();
		
		// Initialize data holders for algorithm
		List<String> currStates = new ArrayList<String>();
		currStates.add(startString); // hashtag represents the starting point
		
		HashMap<String, Double> currScores = new HashMap<String, Double>();
		currScores.put(startString, 0.0);
		
		// loop through the observations 
		for (int i = 0; i < observationNum; i++) {
			List<String> nextStates = new ArrayList<String>(); // keeps track of next possible states
			HashMap<String, Double> nextScores = new HashMap<String, Double>(); // keeps track of next possible scores
			
			for (String currState : currStates) {
				
				for (String nextState : transitionMap.get(currState).keySet()) {
					
					nextStates.add(nextState); // add each possible state to the next state map
					
					// get the current score
					Double currScore = currScores.get(currState); 
					// get the transition score between the current state and the next one
					Double tranScore = transitionMap.get(currState).get(nextState); 
					// observation and next scores are empty initially
					Double obsvScore; // the observation score
					Double nextScore; // the next score, calculated below
					
					// !TODO is this right? should we be using nextState here?
					if (transitionMap.get(currState).containsKey(wordList.get(i))) { 
						obsvScore = transitionMap.get(nextState).get(wordList.get(i));
					} else {
						obsvScore = unseenPenalty; // apply the penalty for not finding the item
					}
					
					// calculate the next store
					nextScore = currScore + tranScore + obsvScore;
					
					// check the map of next scores for this state; if it isn't found or it has a better score, add it to the map
					if (!nextScores.containsKey(nextState) || nextScore > nextScores.get(nextState)) {
						nextScores.put(nextState, nextScore); 
						HashMap<String, String> blankMap = new HashMap<String,String>();
						
						// if we haven't initialized the back-trace map yet, insert the blank map
						if (backTrace.size() <= i) backTrace.add(i, blankMap);
						
						// add the next and current states to the backtrack map
						backTrace.get(i).put(nextState, currState);

					}
				}
			}
			
			currStates = nextStates;
			currScores = nextScores;
		}
		
		return backTrace;
	}
	
	/**
	 * Write a method to train a model (observation and transition probabilities) 
	 * on corresponding lines (sentence and tags) from a pair of training files.
	 */
	public void trainModel() {
		
	}
	
	/**
	 * Write a console-based test method to give the tags from an input line.
	 */
	public void consoleTestModel() {
		
	}
	
	/**
	 * Write a file-based test method to evaluate the performance on a pair of test files 
	 * (corresponding lines with sentences and tags).
	 */
	public void fileTestModel() {
		
	}

}

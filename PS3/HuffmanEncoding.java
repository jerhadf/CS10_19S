import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * Class for performing the Huffman encoding process, holds state and provides methods to compress/decompress
 * 
 * @author Jeremy Hadfield
 * @date May 6, 2019
 * @category Dartmouth CS10 19S, Problem Set 3
 */
public class HuffmanEncoding {
	// files
	protected String pathToFile; // the path to the uncompressed file
	protected String compressedFileName; // Name of the compressed file
	protected String decompressedFileName; // Name of the decompressed file
	
	// queues 
	protected PriorityQueue<BinaryTree<TreeLeaf>> PQ;
	protected PriorityQueue<BinaryTree<TreeLeaf>> treeQ; // priority queue full of trees - a priority forest :)
	protected BinaryTree<TreeLeaf> resultTree; // the final tree that will contain all values
	
	// maps
	protected Map<Character, Integer> freqTable; // holds the key-value pairs of Characters and Integers
	protected Map<Character, String> codeMap;    // Map to store code sequences
	private String pathName;
	
	/**
	 * Constructor for the Encoding class
	 * @param pathName
	 */
	public HuffmanEncoding(String pathName) {
		freqTable = new HashMap<Character, Integer>(); // create a frequency table
		codeMap = new HashMap<Character, String>(); // create a code map
		this.pathName = pathName; // use an input file with the pathName
	}
	
	/**
	 * Helper function
	 * Iterate through the file character by character and update each frequency value. 
	 * @return none, saves output to freqTable (a map of characters to their frequencies)
	 */
	public void fillFreqTable() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(pathName));
		try {
			int c;
			while ((c = input.read()) != -1) { // when input.read() == -1, file reading is complete
				char curr_char = (char) c;
				if (!freqTable.containsKey(curr_char)) 
					freqTable.put(curr_char, 1); // if freqTable doesn't contain char, add it
				else { // increment each character freq one by one
					int frequency = freqTable.get(curr_char); frequency += 1;
					// Replace current with incremented value
					freqTable.put(curr_char, frequency);
				}
			}
		}
		finally { // no error handling - just let the error be thrown.
			input.close();
		}
	}
	
	/**
	 * Helper function
	 * Make single-node trees for each char. Add each tree to priority queue, and use PQ to make final tree.
	 * @return none, saves a BinaryTree made from coalescing all the single-node trees for each char
	 */
	public void generateTree() {
		TreeComparator compare = new TreeComparator(); // initialize the comparator
		
		// create a binary tree with a tree comparator
		PriorityQueue<BinaryTree<TreeLeaf>> treeQ = 
				new PriorityQueue<BinaryTree<TreeLeaf>>(freqTable.size() + 1, compare);
		Set<Character> charList = freqTable.keySet(); // returns a Set based on all the keys of the map
		
		for (char character: charList) { // iterate thru all the characters and add each item to the priority queue
			TreeLeaf countNewChars = 
					new TreeLeaf(character, freqTable.get(character)); // freqTable.get() == frequency of char
			BinaryTree<TreeLeaf> charCountTree = new BinaryTree<TreeLeaf>(countNewChars);
			// Add tree to priority queue
			treeQ.add(charCountTree);
		}
		
		// handle the edge cases
		if (treeQ.size() == 0) {
			resultTree = new BinaryTree<TreeLeaf>(new TreeLeaf('0', 0));
			return; // creates a tree w/ one element and saves it to resultTree
		} 
		if (treeQ.size() == 1) {
			BinaryTree<TreeLeaf> child = treeQ.element();
			resultTree = new BinaryTree<TreeLeaf>(null, child, new BinaryTree<TreeLeaf>(new TreeLeaf('0', 0)));
			return; // creates a tree w/ one element and a single child, saves it to resultTree
		}
		
		while (treeQ.size() > 1) {
			// remove the two lowest-frequency trees
			BinaryTree<TreeLeaf> lowTreeOne = treeQ.remove();
			BinaryTree<TreeLeaf> lowTreeTwo = treeQ.remove();
			
			// sum the frequencies of the two trees
			TreeLeaf totalFreq = new TreeLeaf(lowTreeOne.getData().getFreq() 
					+ lowTreeTwo.getData().getFreq()); 
			
			// attach the child trees to a parent (or foster parent)
			BinaryTree<TreeLeaf> parentTree = new BinaryTree<TreeLeaf>(totalFreq, lowTreeOne, lowTreeTwo);
			
			// Add the new tree back into the queue
			treeQ.add(parentTree);
		}
		// Single tree is the last element standing in the queue
		resultTree = treeQ.element();
	}
	
	
	/**
	 * Helper function, recursive
	 * Code = path (in binary 1s and 0s) from root of tree to that character.
	 * Traverse goes through the tree once to create a map pairing characters with their codes. 
	 * @param bits — initially empty
	 */
	public void traverse(BinaryTree<TreeLeaf> tree, String bits) {
		// if we're going left, add a 0, and go again
		if (tree.hasLeft()) traverse(tree.getLeft(), bits + "0"); 
		
		// if we're going right, add a 1, and go again
		if (tree.hasRight()) traverse(tree.getRight(), bits + "1");
		
		// we're at a leaf node 
		// so put the bit series into a map with the character as the key
		else codeMap.put(tree.data.getCharacter(), bits);
	}
	
	/**
	 * Helper function 
	 * Get code map in a single traverse using traverse() helper method.
	 */
	public void getCodeMap() {
		// Creates blank bitSequence and then fills it using the resultTree.
		String bitSequence = new String();
		traverse(resultTree, bitSequence);
	}
	
	/**
	 * Use the code map to compress a given text file
	 * @param none; input is the path to a file created in HuffmanEncoding constructor
	 * @return none; saves bit output to a compressed file 
	 */
	public void compress() throws IOException {
		System.out.println(" ... Starting compression! ...");
		// change file name to compressed version and create bit readers & writers
		compressedFileName = pathName.replace(".txt", "_compressed.txt");
		BufferedBitWriter compressedBits = new BufferedBitWriter(compressedFileName); // bit sequence to write 
		BufferedReader input = new BufferedReader(new FileReader(pathName)); // read the next character
		
		try {
			int nextChar;
			while ((nextChar = input.read()) != -1) {	
				char currChar = (char) nextChar; // char being read @now
				String charCode = codeMap.get(currChar); // get char's code
				for (char bit: charCode.toCharArray()) { // write code to a file
					Boolean bitVal = (bit == '0');
					// write 0 to file if bit is 0, else write 1
					if (bitVal) compressedBits.writeBit(bitVal);
					else compressedBits.writeBit(bitVal);
				}
			}
		}
		finally {
			compressedBits.close(); input.close();
		}
	}
	
	/**
	 * Use the code map to decopress file
	 * @param none; input is the path to a file created in HuffmanEncoding constructor
	 * @return none; saves text output to an uncompressed file
	 */
	public void decompress() throws IOException {
		System.out.println(" ... Starting decompression! ...");
		// change file name to decompressed version and create bit readers & writers
		decompressedFileName = pathName.replace(".txt", "_decompressed.txt"); 
		FileWriter output = new FileWriter(decompressedFileName); // bit sequence to write 
		BufferedReader input = new BufferedReader(new FileReader(pathName));
		
		// BitReader to read bits from the compressed file
		BufferedBitReader bitInput = new BufferedBitReader(compressedFileName);
		
		try {
			boolean currBit = false; // value of the bit being read 
			BinaryTree<TreeLeaf> tree = resultTree; // copy result tree into new blank trees
			// Read compressed file bit by bit
			while (currBit == bitInput.readBit()) {
				// go through path 
				if (currBit && tree.hasLeft()) tree = tree.getLeft(); // go left if bit 0
				else if (currBit && tree.hasRight()) tree = tree.getRight(); // go right if bit 1
				if (!tree.hasRight() && !tree.hasLeft()) { // no child trees
					// write the character at that location to a file
					char character = tree.getData().getCharacter();
//					output.write(character);
					tree = resultTree; // all clear, go back to root of tree
				}
			} 
		}
		finally { // close all files
			bitInput.close(); output.close(); input.close();
		}
	}
	
	
	
	/**
	 * Tests all the Huffman Encoding functions
	 */
	public static void main(String[] args) {
		String file = "inputs/Alice.txt";
		try {
			HuffmanEncoding huffmanTest = new HuffmanEncoding(file);
			huffmanTest.fillFreqTable();
			huffmanTest.generateTree();
			huffmanTest.getCodeMap();
			huffmanTest.compress();
			huffmanTest.decompress(); // wish I could decompress too :/ :) 
		}
		catch (IOException e) {
			System.err.println("Some kind of file error! (No such directory or file is corrupt");
		}
	}
} 
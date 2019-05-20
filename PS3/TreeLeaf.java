
/**
 * Data type for the binary trees
 * 
 * @author Jeremy Hadfield
 * @date May 8, 2019
 * @category Dartmouth CS10 19S, Problem Set 3
 */
public class TreeLeaf {
	protected Character character;  // use the non-primitive wrapper to store character 
	protected Integer freq;     // frequency: number of times item appears in the text, using non-primitive wrapper
	
	/**
	 * TreeLeaf constructor with only one argument
	 * @param freq
	 */
	public TreeLeaf(int freq) {
		this.character = null; 
		this.freq = freq;
	}
	
	/**
	 * Full constructor for a TreeLeaf element
	 * @param character
	 * @param freq
	 */
	public TreeLeaf(char character, int freq) {
		this.character = character; 
		this.freq = freq; 
	}


	/* 
	 * Method to return a string representation of this object
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (character != null) {
			return character.toString() + ": " + freq.toString();
		}
		else {
			return "*: " + freq.toString();
		}
	}

	/**
	 * All below are getters and setters
	 * @return the character
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * @param character the character to set
	 */
	public void setCharacter(Character character) {
		this.character = character;
	}

	/**
	 * @return the freq
	 */
	public int getFreq() {
		return freq;
	}

	/**
	 * @param freq the freq to set
	 */
	public void setFreq(int freq) {
		this.freq = freq;
	}
	
}

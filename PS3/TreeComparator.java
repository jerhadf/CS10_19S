import java.util.*;

/**
 * TreeComparator.java — class for comparing two trees based on the character counts stored 
 * in their TreeLeaf objects. 
 * 
 * @author Jeremy Hadfield
 * @date May 8, 2019
 * @category Dartmouth CS10 19S, Problem Set 3
 * @return -1 if frequency in first tree higher, 1 if frequency in second tree higher, and return 0 if equal
 */

public class TreeComparator implements Comparator<BinaryTree<TreeLeaf>> {
	public int compare(BinaryTree<TreeLeaf> tree1, BinaryTree<TreeLeaf> tree2) {
		
		// if the char in tree1 is *less* frequent than char in tree 2, return -1
		if (tree1.data.getFreq() < tree2.data.getFreq()) {
			return -1;
		}
		// if the char in tree1 is *more* frequent than char in tree 2, return -1
		else if (tree1.data.getFreq() > tree2.data.getFreq()) {
			return 1;
		}
		// the two trees have equal frequencies for the char
		else {
			return 0;
		}
	}
}

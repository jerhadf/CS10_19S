package cs10;

import java.util.Arrays;

/**
 * A simple class with a few methods that solve array-related problems commonly asked in coding interviews
 * 
 * @author Jeremy Hadfield
 * @category Dartmouth CS10 19S, Short Assignment 0  
 * 
 */

public class SA0 {
	
	private int[] nums;

	/**
	 * Constructor
	 * @param nums - the array to be used for all the methods in this class
	 */
	public SA0(int[] nums) {
		super();
		this.setNums(nums);
	}
	
	/**
	 * @return the nums
	 */
	public int[] getNums() {
		return nums;
	}

	/**
	 * @param the array to set nums to
	 */
	public void setNums(int[] nums) {
		this.nums = nums;
	}

	
	/**
	 * Implements recursive quick sort in place. Could use Arrays.sort() instead. 
	 * To sort the entire array, set low = 0 and high = arr.length - 1
	 * 
	 * @return the sorted array
	 * @timeComplexity O(Nlog(N))
	 */
	private void quickSort(int low, int high) {
		if (low < high) { 
            int pi = partition(nums, low, high); // partition index
            quickSort(low, pi-1); // recursively sort the elements before partition index
            quickSort(pi+1, high); // recursively sort the elements after partition index
        } 
	}
	
    /**
     * Helper function for quickSort. Sets a pivot element, then places all numbers greater than pivot to the right
     * and all numbers less than pivot to the left. 
     * @param arr - array of integers to partition
     * @param low - 
     * @param high - 
     * @return 
     */
    private int partition(int[] arr, int low, int high) { 
        int pivot = arr[high]; 
        int i = (low-1); // index of smaller element 
        for (int j=low; j<=high-1; j++) 
        { 
            // If current element is smaller than or 
            // equal to pivot 
            if (arr[j] <= pivot) 
            { 
                i++; 
  
                // swap arr[i] and arr[j] 
                int temp = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = temp; 
            } 
        } 
  
        // swap arr[i+1] and arr[high] (or pivot) 
        int temp = arr[i+1]; 
        arr[i+1] = arr[high]; 
        arr[high] = temp; 
  
        return i+1; 
    } 
	
	
	/**
	 * Given an integer array, find the sum of the minimum numbers in every pair (i, j) in the array where i starts 
	 * at the beginning of the array and j starts at the end. Uses two-pointer technique.
	 * 
	 * @return the sum of the minimum items in corresponding pairs in the array. This is equal to the sum of 
	 * all the elements left of the midpoint if the array was sorted in ascending order.
	 * @timeComplexity O((3/2)N) 
	 */
	private int arrayPairSum() {
        int[] pairs = new int[nums.length / 2]; 
        for (int i = 0, j = nums.length - 1; i < j; i++, j--) { 
            int min = nums[i] < nums[j] ? nums[i] : nums[j]; 
            pairs[i] = min; 
        }
        int pairs_sum = 0;
        for (int x : pairs) pairs_sum += x;
        return pairs_sum;
    }
    
    /**
     * Given a non-empty array, return true if there is a place to split the array so that the sum of the numbers 
     * on one side is equal to the sum of the numbers on the other side.
     * 
     * @return boolean of whether there is a balanced place to split the array
     * @timeComplexity O(2N) 
     */
    private boolean canBalance() {
	  if (nums.length == 1) return false; 
	  int rightsum = 0; 
	  int leftsum = 0; 
	  for (int i : nums) rightsum += i; 
	  for (int j = 0; j < nums.length; j++) {
	    if (rightsum == leftsum) return true; 
	    leftsum += nums[j]; 
	    rightsum -= nums[j]; 
	  }
	  return false;
    }
    
    
    /**
     * Linear search for element k
     * 
     * @param k - the number to search for in the array
     * @return the index of the number k, OR -1 if number is not found
     * @timeComplexity O(N)
     */
    private int linearSearch(int k) {
    	for (int i = 0; i < nums.length; i++) {
    		if (nums[i] == k) return i; 
    	}
    	return -1; // number not found
    }
    
    /**
     * An iterative implementation of binary search. Returns the index of the specified element in the array. 
     * Assumes that the array is already sorted in ascending order (must run quickSort first)
     * 
     * Because it has to sort the array first, this is less efficient than linear search unless we are going to 
     * conduct multiple searches on the array (the time cost is amortized over multiple searches). 
     * 
     * @param k - the number to search for in the array
     * @return the index of the number k, OR -1 if the number is not found
     * @timeComplexity O(log(N)) - plus quickSort time complexity O(Nlog(N)) if needed
     */
    private int binarySearch(int k) {
    	int l = 0; 
    	int r = nums.length; 
    	while (l < r) {
    		int mid = (l + r) / 2; // Java integer division is equivalent to Math.floor()
    		if (nums[mid] == k) return mid; // search complete
    		if (nums[mid] < k) l = mid + 1; // search left half of array
    		else r = mid - 1; // search right half of array
 
    	}
    	return - 1; // if l > r at any point, the search is unsuccessful. this also handles empty arrays. 
    }
    
    /**
     * Print the answers to all of the SA0 class methods for a sample array
     */
	
	public static void main(String[] args) {
		int[] nums = {3, 6, 5, 1, 1, 4, 3, 8, 2, 13}; 
		SA0 example = new SA0(nums); 
		System.out.println("Running some example tests on the SA0 class using this array: " + Arrays.toString(nums));
		int arrayPairAnswer = example.arrayPairSum(); 
		System.out.println("arrayPairSum answer: " + arrayPairAnswer);
		boolean canBalanceAnswer = example.canBalance(); 
		System.out.println("canBalance answer: " + canBalanceAnswer);
		int linearSearchAnswer = example.linearSearch(25); 
		System.out.println("linearSearch for index of 25: " + linearSearchAnswer);
		example.quickSort(0, nums.length - 1); // sorts in place
		System.out.println("Sorted array in place using quickSort: " + Arrays.toString(nums));
		int binarySearchAnswer = example.binarySearch(8); 
		System.out.println("Binary search for index of 8 in sorted array: " + binarySearchAnswer);
	}

}

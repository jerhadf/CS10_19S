package cs10;

import java.util.Arrays;

/**
 * Random tests for various Java stuff
 * @author Jeremy Hadfield
 *
 */
public class RandomTests {
	
	public static int[] twoSum(int[] nums, int target) {
        int i = 0; 
        int j = nums.length - 1; 
        int[] sorted = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sorted); // quickSort, O(log(n))
        while (i < j) {
            int sum = sorted[i] + sorted[j];
            if (sum == target) { 
            	int indx1 = search(sorted[i], nums, nums.length); 
            	int indx2 = search(sorted[j], nums, indx1);
            	return new int[] {indx1, indx2}; 
        	}
            if (sum < target) i++; 
            if (sum > target) j--;
        }
        System.out.println("Target sum " + target + " not possible in list!");
        return new int[] {-1, -1}; 
    }
    
    // returns index of given int in a sorted array, O(n)
    private static int search(int num, int[] arr, int found_already) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num && i != found_already) return i; 
        } 
        return 0;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] list = new int[] {3, 4, 8, 9, 3, 3, 6, 12};
		System.out.println(Arrays.toString(twoSum(list, 15)));
	}

}

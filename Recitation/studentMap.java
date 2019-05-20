import java.util.ArrayList;
import java.util.HashMap;

/**
 * studentMap.java 
 * 
 * @author Jeremy Hadfield
 * @date Apr 25, 2019
 * @category Dartmouth CS10 19S, Recitation Week 5
 */
public class studentMap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// generate a hashmap of type <names, courselist>
		HashMap<String, ArrayList<String>> students = new HashMap(); 
		// add my name and an empty courselist to hashmap
		String myName = "Jeremy Hadfield"; 
		ArrayList<String> myCourses = new ArrayList<String>();
		students.put(myName, myCourses);
		// add CS10 to my courselist
		students.get(myName).add("COSC 10");
		// print the map as a string
		System.out.println(students.toString());
		
	}

}

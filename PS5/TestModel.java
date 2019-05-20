/**
 * TestModel.java 
 * 
 * @author Jeremy Hadfield
 * @date May 16, 2019
 * @category Dartmouth CS10 19S, Assignment Name
 */

public class TestModel {

	/**
	 *  Train and test with the two example cases provided in texts.zip: "simple", just some made up sentences and tags, 
	 *  and "brown", the full corpus of sentences and tags. The sample solution got 32 tags right and 5 wrong for simple, 
	 *  and 35109 right vs. 1285 wrong for brown, with an unseen-word penalty of -100. (Note: these are using natural log; 
	 *  if you use log10 there might be some differences.) In a short report, provide some example new sentences that are 
	 *  tagged as expected and some that aren't, discussing why. 
	 *  Also discuss your overall testing performance, 
	 *  and how it depends on the unseen-word penalty (and any other parameters you use).
	 * @param args
	 */
	public static void main(String[] args) {
		MarkovModel m = new MarkovModel("PS5/texts/simple-train-sentences.txt", "PS5/texts/simple-train-tags.txt");
//		MarkovModel m = new MarkovModel("PS-5/texts/brown-train-sentences.txt", "PS-5/texts/brown-train-tags.txt");
//		
//		m.generateMaps();
//		System.out.println(m.getTransitionMap());
//		System.out.println(m.getlabelToText());
//		System.out.println();
//		
//		m.setToLog();
//		System.out.println(m.getTransitionMap());
//		System.out.println(m.getlabelToText());
//		System.out.println();
//		
//		m.vitterbi("PS-5/texts/simple-test-sentences.txt");
//		
//		System.out.println(m.parseFile("PS-5/texts/simple-test-sentences.txt"));
//		System.out.println(m.parseFile("PS-5/texts/simple-test-tags.txt"));
//		System.out.println(m.parseLine("the dog saw trains in the night ."));
<<<<<<< HEAD
		m.consoleTestModel();
=======
//		m.consoleTestModel();
		
		ArrayList<String> testArr = new ArrayList<String>();	// DET N V N P DET N
//		testArr.add("the");
//		testArr.add("dog");
//		testArr.add("saw");
//		testArr.add("trains");
//		testArr.add("in");
//		testArr.add("the");
//		testArr.add("night");
		testArr.add("det");
		testArr.add("n");
		testArr.add("v");
		testArr.add("n");
		testArr.add("p");
		testArr.add("det");
		testArr.add("n");
		System.out.println(m.fileTestModel("PS-5/texts/simple-test-sentences.txt", "PS-5/texts/simple-test-tags.txt", testArr));
>>>>>>> 333ae9b7cd284a79f055b50f5cfd1a52f07b1152
	}
}

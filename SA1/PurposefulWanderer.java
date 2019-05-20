/**
 * Purposeful wanderer that moves a specified distance before changing direction
 * 
 * @author Jeremy Hadfield
 * @date Mar 6, 2019
 * @category Dartmouth CS10 19S, Short Assignment 1
 */

public class PurposefulWanderer extends Blob {
	protected int wanderDist;    // the number of steps to take before changing velocity
	protected int numSteps;      // number of steps taken so far
	
	/**
	 * Constructor for x and y. Randomly generates a wander distance.
	 * @see Blob. 
	 * @param x 
	 * @param y
	 */
	public PurposefulWanderer(double x, double y) {
		super(x, y);
		this.wanderDist = (int) (10  + ((Math.random() * 10))); // generates random num between 10 & 20
	} 
	
	/**
	 * Constructor for x, y, and wander distance. 
	 * @param x 
	 * @param y 
	 * @param w
	 */
	public PurposefulWanderer(double x, double y, int wanderDist) {
		super(x, y);
		this.wanderDist = wanderDist; 
	} 
	
	@Override 
	public void step() {
		x += dx; 
		y += dy; 
		// if we have stepped the wander distance, reset the velocity
		if (numSteps % wanderDist == 0) {
			// sets new random values for velocity between -2r and 2r
			dx = 2 * r * (Math.random() - .5); 
			dy = 2 * r * (Math.random() - .5);
		}
		numSteps += 1;
	}
	
}

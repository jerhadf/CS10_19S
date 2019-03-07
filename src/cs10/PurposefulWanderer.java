package cs10;

/**
 * Purposeful wanderer that moves a specified distance before changing direction
 * 
 * @author Jeremy Hadfield
 * @date Mar 6, 2019
 * @category Dartmouth CS10 19S, Short Assignment 1
 */
public class PurposefulWanderer extends Blob {
	protected int wander_dist; // the number of steps to take before changing velocity
	protected int num_steps; // number of steps taken so far
	
	/**
	 * Constructor for x and y. Randomly generates a wander distance.
	 * @see Blob. 
	 */
	public PurposefulWanderer(double x, double y) {
		super(x, y);
		this.wander_dist = (int) (10  + ((Math.random() * 10))); // generates random num between 10 & 20
	} 
	
	/**
	 * Constructor for x, y, and wander distance. 
	 */
	public PurposefulWanderer(double x, double y, int wander_dist) {
		super(x, y);
		this.wander_dist = wander_dist; 
	} 
	
	@Override 
	public void step() {
		x += dx; 
		y += dy; 
		// if we have stepped the wander distance, reset the velocity
		if (num_steps % wander_dist == 0) {
			// sets new random values for velocity between -2 and 2
			dx = 4 * (Math.random() - .5); 
			dy = 4 * (Math.random() - .5);
		}
		num_steps += 1;
	}
	
	
}

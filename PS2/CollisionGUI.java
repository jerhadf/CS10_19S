import java.awt.*;

import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Using a quadtree for collision detection
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, updated for blobs
 * @author CBK, Fall 2016, using generic PointQuadtree
 */
public class CollisionGUI extends DrawingGUI {
	private static final int width=800, height=600;		// size of the universe

	private List<Blob> blobs;						// all the blobs
	private List<Blob> colliders;					// the blobs who collided at this step
	private char blobType = 'b';						// what type of blob to create
	private char collisionHandler = 'c';				// when there's a collision, 'c'olor them, or 'd'estroy them
	private int delay = 100;							// timer control

	public CollisionGUI() {
		super("super-collider", width, height);

		blobs = new ArrayList<Blob>();

		// Timer drives the animation.
		startTimer();
	}

	/**
	 * Adds an blob of the current blobType at the location
	 */
	private void add(int x, int y) {
		if (blobType=='b') {
			blobs.add(new Bouncer(x,y,width,height));
		}
		else if (blobType=='w') {
			blobs.add(new Wanderer(x,y));
		}
		else {
			System.err.println("Unknown blob type "+blobType);
		}
	}

	/**
	 * DrawingGUI method, here creating a new blob
	 */
	public void handleMousePress(int x, int y) {
		add(x,y);
		repaint();
	}

	/**
	 * DrawingGUI method
	 */
	public void handleKeyPress(char k) {
		if (k == 'f') { // faster
			if (delay>1) delay /= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 's') { // slower
			delay *= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 'r') { // add some new blobs at random positions
			for (int i=0; i<10; i++) {
				add((int)(width*Math.random()), (int)(height*Math.random()));
				repaint();
			}			
		}
		else if (k == 'c' || k == 'd') { // control how collisions are handled
			collisionHandler = k;
			System.out.println("collision:"+k);
		}
		else if (k == '1') { // execute test 
			test1(); 
		}
		else { // set the type for new blobs
			blobType = k;			
		}
	}

	/**
	 * DrawingGUI method, here drawing all the blobs and then re-drawing the colliders in red
	 * 
	 * @g : the built-in Java graphics object
	 */
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE
		// Ask all the blobs to draw themselves.
		for(Blob blob : blobs) { 
			System.out.println("drawing in black");
			g.setColor(Color.black); // set the graphics color to black
			// draw the blob at point (x, y) as an oval with radius set to R
			g.fillOval((int)blob.getX(), (int)blob.getY(), (int)blob.getR(), (int)blob.getR());
		}
		
		// Ask the colliders to draw themselves in red.
		if(collisionHandler == 'c') { 
			g.setColor(Color.red);
			if(colliders != null) { 
				System.out.println("drawing in red"); 
				for(Blob collider : colliders) { 
					g.fillOval((int)collider.getX(), (int)collider.getY(), (int)collider.getR(), (int)collider.getR());
				}
			} 
		}
	}

	/**
	 * Sets colliders to include all blobs in contact with another blob
	 */
	private void findColliders() {
		// TODO: YOUR CODE HERE
		// Create the tree
		colliders = new ArrayList<Blob>(); // initializes empty list
		
		Blob first_blob = blobs.get(0); // gets the first blob
		
		// creates a tree with the first blob at the center
		PointQuadtree<Blob> blobsTree = new PointQuadtree<Blob>(first_blob, 0, 0, width, height);
		
		// inserts every blob into the tree
		for(Blob blob : blobs) { 
			blobsTree.insert(blob);
		}
		
		// For each blob, see if anybody else collided with it
		for (Blob blob : blobs) { 	
			// check if there are any blobs within a surrounding circle of radius * 2
			if(blobsTree.findInCircle(blob.getX(), blob.getY(), (int) blob.getR() * 2).size() > 1) { 
				colliders.add(blob); 
				System.out.println(blob + " collided, number of collided blobs: " + colliders.size()); 
			}
		}
	}

	/**
	 * DrawingGUI method, here moving all the blobs and checking for collisions
	 */
	public void handleTimer() {
		// Ask all the blobs to move themselves.
		for (Blob blob : blobs) {
			blob.step();
		}
		// Check for collisions
		if (blobs.size() > 0) {
			findColliders();
			if (collisionHandler=='d') {
				blobs.removeAll(colliders);
				colliders = null;
			}
		}
		// Now update the drawing
		repaint();
	}
	
	/**
	 * tests the GUI to make sure objects are colliding as expected 
	 * 
	 */
	private void test1() {
		Blob testBlob = new Bouncer(320,420,width,height);
		blobs.add(testBlob); 
		Blob testBlob2 = new Bouncer(40,80,width,height);
		blobs.add(testBlob2);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CollisionGUI();
			}
		});
	}
}

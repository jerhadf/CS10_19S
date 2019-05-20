import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * Load an image and display it
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author Jeremy Hadfield, Dartmouth CS 10, Spring 2018, updated for SA-3 
 */

public class DeepFryerGUI extends DrawingGUI {
	private DeepFryer mod;		// handles the image processing
	// keeps track of whether deep fry brush is up (true) or down (false)
	private boolean brushDeepFry = false; 
	
	
	/**
	 * Creates the GUI for the image processor, with the window scaled to the to-process image's size
	 */
	public DeepFryerGUI(DeepFryer mod) {
		super("Image processing", mod.getImage().getWidth(), mod.getImage().getHeight());
		this.mod = mod;
	}

	/**
	 * DrawingGUI method, here showing the current image
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(mod.getImage(), 0, 0, null);
		
	}

	/**
	 * DrawingGUI method, here dispatching on image processing operations
	 */
	@Override
	public void handleKeyPress(char key) {
		System.out.println("Handling key '"+key+"'");
		if (key=='s') { // save a snapshot
			saveImage(mod.getImage(), "pictures/snapshot.png", "png");
		} else if (key == 'd') { // sets brush to down (deepfry) 
			this.brushDeepFry = true; 
		} else if (key == 'u') { // sets brush to up (all modifiers)
			this.brushDeepFry = false; 
		} else if (key == 'e') { 
			mod.deepfryImage(); 
		} else if (key=='s') { // save a snapshot
			saveImage(mod.getImage(), "pictures/snapshot.png", "png");
		} else {
			System.out.println("Unknown operation");
		}

		repaint(); // Re-draw, since image has changed
	}
	
	@Override
	public void handleMouseMotion(int x, int y) {
		if (brushDeepFry) { 
			mod.deepfry(x, y, 30);
			repaint(); 
		} else { 
			System.out.print("Unknown effect");
		}
	}

	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Load the image to process
				BufferedImage picture = loadImage("pictures/baker.jpg");
				// Create a new processor, and a GUI to handle it
				new DeepFryerGUI(new DeepFryer(picture));
			}
		});
	}
}


import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * Load an image and display it
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 * @author Jeremy Hadfield, Dartmouth CS 10, Spring 2019, updated for SA-3 and DeepFryer
 */
public class MemeProcessingGUI extends DrawingGUI {
	private MemeProcessor proc;		    // handles the image processing
	private boolean brushDeepFry = false; 	// keeps track of if deep fry brush is on/off
	private boolean brushEmojify = false; 	// keeps track of if emojify brush is on/off
	// list of all emoji images available
	private ArrayList<String> emojiList = new ArrayList<String>();
	// keeps track of which index in emojiList is being used for the current emoji
	private int currentEmoji = 0; 			

	/**
	 * Creates the GUI for the image processor, with the window scaled to the to-process image's size
	 */
	public MemeProcessingGUI(MemeProcessor proc) {
		super("Image processing", proc.getImage().getWidth(), proc.getImage().getHeight());
		this.proc = proc;
		// adds the existing emojis to the list
		emojiList.add("cryinglaughing.png");
		emojiList.add("cool-doge.png");
		emojiList.add("smiley.png");
		emojiList.add("cowboi.png");
		emojiList.add("crying_boi.png");
		emojiList.add("dogeicon.png");
		emojiList.add("otherface.png");
		emojiList.add("thinkface.png");
		emojiList.add("sunglass.png");
		emojiList.add("sadbirthday");
	}

	/**
	 * DrawingGUI method, here showing the current image
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(proc.getImage(), 0, 0, null);
	}

	/**
	 * DrawingGUI method, here dispatching on image processing operations
	 */
	@Override
	public void handleKeyPress(char op) {
		System.out.println("Handling op '"+op+"'");
		if (op=='s') { // save a snapshot
			saveImage(proc.getImage(), "pictures/snapshot.png", "png");
		} else if (op == 'd') { // toggles deepfry brush: stop/start deepfrying
			this.brushDeepFry = !this.brushDeepFry; 
			System.out.println("DEEPFRY BRUSH TOGGLED: " + this.brushDeepFry);
		} else if (op == 't') { // deepfries the entire image
			proc.deepfryImage(); 
		} else if (op == 'e') { // toggles emoji brush: stop/start emojifying
			this.brushEmojify = !this.brushEmojify; 
			System.out.println("EMOJI BRUSH TOGGLED: " + this.brushEmojify);
			String currEmoji = emojiList.get(currentEmoji);
			System.out.println("CURRENT EMOJI: " + currEmoji);
		} else if (op == 'n') { // switches to a new emoji image
			if(currentEmoji >= emojiList.size() - 1) {
				currentEmoji = 0;
			} else {
				currentEmoji += 1;
			}
			String newEmoji = emojiList.get(currentEmoji);
			System.out.println("NEW EMOJI: " + newEmoji);
			// grabs the emoji file from the pictures directory
			BufferedImage currentEmojiFile = loadImage("pictures/emojis/" + newEmoji);
			// sets the current emoji to the image file
			proc.setEmoji(currentEmojiFile);
		} 
		else {
			System.out.println("Unknown operation");
		}

		repaint(); // Re-draw, since image has changed
	}
	
	@Override
	public void handleMouseMotion(int x, int y) {
		if (brushDeepFry) { 
			proc.deepfry(x, y, 30);
			repaint(); 
		} else if (brushEmojify) { 
			proc.emojify(x, y);
			repaint(); 
		} else { 
			System.out.print("Unknown effect. ");
		}
	}

	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Load the image to process
				BufferedImage baker = loadImage("pictures/baker.jpg");
				// Create a new processor, and a GUI to handle it
				new MemeProcessingGUI(new MemeProcessor(baker));
			}
		});
	}
}

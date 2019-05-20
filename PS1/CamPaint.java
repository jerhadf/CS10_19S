import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam-based drawing 
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Spring 2015 (based on a different webcam app from previous terms)
 * 
 * @author Jeremy Hadfield
 * @date Apr 11, 2019
 * @category Dartmouth CS10 19S, Problem Set 1, Cam Paint component 
 */
public class CamPaint extends Webcam {
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder finder;			// handles the finding
	private Color targetColor;          	// color of regions of interest (set by mouse press)
	private Color paintColor = Color.red;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece

	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
		finder = new RegionFinder();
		clearPainting();
	}

	/**
	 * Resets the painting to a blank image
	 */
	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * DrawingGUI method, here drawing one of live webcam, recolored image, or painting, 
	 * depending on display variable ('w', 'r', or 'p')
	 */
	@Override
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE 
		// change the way the screen is drawn based on screen presses
		
		// live webcam is just the streaming video with no processing
		// recolored image is from region finder
		// painting is the tracks left by the paintbrush over time
		
		if (displayMode == 'p') { // changes display mode to painting
			g.drawImage(image, 0, 0, null); // last argument is null b/c we don't need an ImageObserver? 
		} else if (displayMode == 'r') { // changes display mode to recolored image
			g.drawImage(finder.getRecoloredImage(), 0, 0, null);
		} else { // displayMode is p, changes display mode to webcam
			g.drawImage(painting, 0, 0, null);
		}
		
	}

	/**
	 * Webcam method, here finding regions and updating the painting.
	 */
	@Override
	public void processImage() {
		// TODO: YOUR CODE HERE
		if (targetColor != null) {  // if the mouse has been pressed to set targetColor
			
			 finder.setImage(image); 			 // sets up the image, 
			 finder.findRegions(targetColor);    // finds regions matching the target color 
			 finder.recolorImage();              // recolor image based on matching regions
				
			 ArrayList<Point> largest = finder.largestRegion();	// find the largest region 
			 
			 // loop through each point in the largest region and reset its color
			 for(Point p: largest) { 
				 painting.setRGB((int) p.getX(), (int) p.getY(), targetColor.getRGB()); // change pixel's color to target color
			 }
		}
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		// TODO: YOUR CODE HERE 
		
		// just grab the color of the pixel at location where the mouse was clicked 
		Color mouseColor = new Color(image.getRGB(x, y));
		
		int r = mouseColor.getRed();
		int g = mouseColor.getGreen();
		int b = mouseColor.getBlue();
		
		targetColor = new Color(r, g, b);
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 */
	@Override
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
		}
		else if (k == 'c') { // clear
			clearPainting();
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}

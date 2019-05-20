import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint

 * @author Jeremy Hadfield
 * @date Apr 11, 2019
 * @category Dartmouth CS10 19S, Problem Set 1, Region Finder
 */
public class RegionFinder {
	private static final int maxColorDiff = 30;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 120; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points
	
	/**
	 * Default constructor, sets image to null
	 */
	public RegionFinder() {
		this.image = null;
	}
	
	
	/**
	 * Constructor with one argument, setting an image
	 * @param image
	 */
	public RegionFinder(BufferedImage image) {
		this.image = image;		
	}


	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * @return the recoloredImage
	 */
	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * @param recoloredImage the recoloredImage to set
	 */
	public void setRecoloredImage(BufferedImage recoloredImage) {
		this.recoloredImage = recoloredImage;
	}

	/**
	 * @return the regions
	 */
	public ArrayList<ArrayList<Point>> getRegions() {
		return regions;
	}

	/**
	 * @param regions the regions to set
	 */
	public void setRegions(ArrayList<ArrayList<Point>> regions) {
		this.regions = regions;
	}

	/**
	 * @return the maxcolordiff
	 */
	public static int getMaxcolordiff() {
		return maxColorDiff;
	}

	/**
	 * @return the minregion
	 */
	public static int getMinregion() {
		return minRegion;
	}

	/**
	 * Helper method that generates a black picture for colors to be pasted into
	 * @return an all-black BufferedImage
	 */
	private BufferedImage createBlankImage() {
		BufferedImage blankImage = new BufferedImage(
				image.getWidth(), 
				image.getHeight(), 
				BufferedImage.TYPE_INT_ARGB); // ensure that the image uses RGB colors
		
		// loop through the entire image, making every pixel black
		for(int y = 0; y < blankImage.getHeight(); y++) {
			for (int x = 0; x < blankImage.getWidth(); x++) {
				blankImage.setRGB(x, y, 0); // 0 is black in RGB - sets pixel to black
			}
		}
		
		return blankImage;
	}
	
	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 * @param targetColor — the color that defines the region, set by the mouse click
	 */
	public void findRegions(Color targetColor) {
		// TODO: YOUR CODE HERE (implement the flood-fill/region-finding algorithm) 
		
		// create a blank image to keep track of visited pixels
		BufferedImage visitedImage = createBlankImage();
		
		// list to contain all regions - defined as instance variable, instantiated here
		regions = new ArrayList<>(); 
		
		// loop through all the pixels 
		for(int j = 0; j < image.getHeight(); j++) { // j = y values 
			for(int i = 0; i < image.getWidth(); i++) { // i = x values
		
				// create a color object to keep track of current pixel color
				Color pixelColor = new Color(image.getRGB(i, j)); 
				
				if(visitedImage.getRGB(i, j) == 0 && // if pixel hasn't been visited (black on the visitedImage)
					colorMatch(pixelColor, targetColor)) {// if pixel matches target color
					
					ArrayList<Point> currRegion = new ArrayList<Point>(); // keeps track of the current region points
					ArrayList<Point> pointsToVisit = new ArrayList<Point>(); // keeps track of unvisited points (the queue)
					
					// since this point hasn't been visited and matches target color, we should visit this point 
					pointsToVisit.add(new Point(i,j)); 
					
					// ensures that there are still pointsToVisit, and while there are, keeps running flood-fill
					while(!pointsToVisit.isEmpty()) {
						
						Point currPoint = pointsToVisit.get(0);	// get the first point in the to-visit list
						pointsToVisit.remove(currPoint); // remove the point from the to-visit list since we're boutta visit it
						
						// if the pixel is not black on the visited Image (hasn't been visited yet)
						if(visitedImage.getRGB((int)currPoint.getX(), (int)currPoint.getY()) == 0) {		
							
							// set the current pixel to white (256^3 as TYPE_INT_ARGB)
							visitedImage.setRGB((int)currPoint.getX(), (int)currPoint.getY(), (int) Math.pow(256, 3));
							
							currRegion.add(currPoint); // add the new point to the region currently being built
		
							// loop through the points and get X and Y, ensuring that the point stays within the screen
							for(int y = Math.max(0, (int) currPoint.getY() - 1); 
									y <= Math.min(currPoint.getY() + 1, image.getHeight() - 1); y++) { 
								
								// loop through the points neighbors 
								for(int x = Math.max(0, (int) currPoint.getX() - 1); 
										x <= Math.min(currPoint.getX() + 1, image.getWidth() - 1); x++) {
									
									Point newPoint = new Point(x,y);
									
									// get the new point's color
									Color n = new Color(image.getRGB((int)newPoint.getX(), (int)newPoint.getY()));
									
									// if the color matches the target, add the color to pointsToVisit
									if(colorMatch(n,targetColor)) pointsToVisit.add(newPoint);
										
								}
							}
						}
					}
					if(currRegion.size() > minRegion) { // if built region is large enough, add to list of regions
						regions.add(currRegion);
					}
				}
			}
		}
	}

	/**
	 * Tests whether the two colors are "similar enough" 
	 * (your definition, subject to the maxColorDiff threshold, which you can vary).
	 * 
	 * Uses the Euclidean formula to determine if colors match
	 * @see https://en.wikipedia.org/wiki/Color_difference
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		// TODO: YOUR CODE HERE
		// use the Euclidean formula: 
		// sqrt((r2 - r1)^2 + (g2 - g1)^2 + (b2 - b1)^2)
		double colorDiff = Math.sqrt((
				Math.pow(c2.getRed()-c1.getRed(), 2) + 
				Math.pow(c2.getBlue()-c1.getBlue(), 2) +
				Math.pow(c2.getGreen()-c1.getGreen(), 2) 
		));
		
		if (colorDiff <= maxColorDiff) {
			return true; // less than the threshold: similar enough to be called a match
		} else {
			return false;
		}
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		// TODO: YOUR CODE HERE
		ArrayList<Point> largestRegion;
		// get the first item in the region — if we don't find larger region, return this.
		largestRegion = regions.get(0); 
		
		// loop through the entire region
		for(ArrayList<Point> region: regions) {
			// check if the curr region is greater than the largest
			if(region.size() > largestRegion.size()) {
				// if it is, set the largest region to the curr region
				largestRegion = region; 
			}
		}
		
		return largestRegion;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are. 
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), 
				image.copyData(null), 
				image.getColorModel().isAlphaPremultiplied(), null);
		
		// TODO: YOUR CODE HERE (Now recolor the regions in it)
		
		// loop through all the regions
		for (ArrayList<Point> region : regions) {
			
			// set all the color values to a random number between 0 and 255
			int red = (int)(Math.random()*256);
			int green = (int)(Math.random()*256);
			int blue = (int)(Math.random()*256);
			
			// create a color object from the color values
			int color = new Color(red,green,blue).getRGB();
			
			// loop through all the points in the region
			for (Point point: region) { 
				// set the RGB values of the point to the randomly generated color
				recoloredImage.setRGB((int) point.getX(), (int) point.getY(), color);
			}
		}
	}
}

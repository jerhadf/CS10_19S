import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * A class demonstrating the deep-fryer, an image processor/filter that adds 
 * noise, brightness, and saturation, making the area appear 'deep fryed.' 
 * You can use this process to manually deep fry your own images, by hand! Enjoy. 
 * It also can add emojis! Makes the image a bit more spicy. 
 * 
 * See http://knowyourmeme.com/memes/deep-fried-memes
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 * @author Jeremy Hadfield, Dartmouth CS 10, Spring 2019, added deep fryer for SA-3
 */
public class MemeProcessor extends DrawingGUI {
	private BufferedImage image;		// the current image being processed
	private BufferedImage emoji; 		// the current emoji applied in the emojifier
	

	/**
	 * Default constructor without a specified emoji image; uses a default emoji
	 * @param image
	 */
	public MemeProcessor(BufferedImage image) {
		this.image = image;
		BufferedImage emojiFile = loadImage("pictures/emojis/smiley.png");
		this.emoji = emojiFile;
	}

	/**
	 * Constructor with a specified emoji image
	 * @param image
	 * @param emoji
	 */
	public MemeProcessor(BufferedImage image, BufferedImage emoji) {
		this.image = image;
		this.emoji = emoji;
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
	 * @return the emoji
	 */
	public BufferedImage getEmoji() {
		return emoji;
	}

	/**
	 * @param emoji the emoji to set
	 */
	public void setEmoji(BufferedImage emoji) {
		this.emoji = emoji;
	}
	
	/**
	 * Helper function to keep a number within certain bounds
	 * Returns the value (if it's between min or max), or returns min/max (if it's outside that range).
	 * @param val
	 * @param min
	 * @param max
	 * @return constrained value
	 */
	public static double constrain(double val, double min, double max) {
		if (val < min) {
			return min;
		}
		else if (val > max) {
			return max;
		}
		return val;
	}
	
	/**
	 * Helper function to return an image that's a copy of the current one
	 */
	private BufferedImage createCopyResult() {
		return new BufferedImage(image.getColorModel(), 
				image.copyData(null), 
				image.getColorModel().isAlphaPremultiplied(), null);
	}
	
	/** 
	 * Helper function to add noise to the area. 
	 * @param x          x location of pixel 
	 * @param y          y location of pixel
	 * @param scale      scale of the noise (higher numbers = more noise) 
	 * @param image      the image to modify
	 */
	public void noise(int x, int y, double scale, BufferedImage image) { 
		// add noise to color by multiplying RGB values by randomly generated numbers
		Color color = new Color(image.getRGB(x, y));
		int red = (int)(constrain(color.getRed() + scale * (2*Math.random() - 1.5), 0, 255));
		int green = (int)(constrain(color.getGreen() + scale * (2*Math.random() - 1.5), 0, 255));
		int blue = (int)(constrain(color.getBlue() + scale * (2*Math.random() - 1.5), 0, 255));
		
		// set color to new noisy color
		Color newColor = new Color(red, green, blue);
		image.setRGB(x, y, newColor.getRGB());
	} 
	
	 /**
	  * Helper function to print the contents of buffered image 2 on buffered image 1
	  * with the given opacity. 
	  * 
	  * @author user tim on StackOverflow: https://codereview.stackexchange.com/questions/58067/drawing-a-bufferedimage-into-another
	  * @author Jeremy Hadfield, modified to fit SA3 code for emojifier functionality
	  */
	 private void addImage(BufferedImage buff1, BufferedImage buff2,
	         float opaque, int x, int y) {
	     Graphics2D g2d = buff1.createGraphics();
	     g2d.setComposite(
	             AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opaque));
	     g2d.drawImage(buff2, x, y, null);
	     g2d.dispose();
	 }
	
	/** 
	 * Helper function to add the emoji in the area
	 * @param x          x location of pixel 
	 * @param y          y location of pixel
	 * @param image      the image to modify
	 */
	public void addEmoji(int x, int y, BufferedImage image, BufferedImage emoji) { 
		// pastes the emoji image onto the main image at the specified location
		final float OPACITY = 1;				 // sets the opacity of the emoji
		addImage(image, emoji, OPACITY, x, y);
	} 
	
	/** 
	 * Helper function to add brightness to the area. 
	 * @param x          x location of pixel 
	 * @param y          y location of pixel
	 * @param scale      scale of the brightening (higher numbers = more brightness) 
	 * @param image      the image to modify
	 */
	public void brightness(int x, int y, double scale, BufferedImage image) { 
		// increase brightness of color
		Color color = new Color(image.getRGB(x, y));
		int red = (int)(constrain(color.getRed() * scale, 0, 255));
		int green = (int)(constrain(color.getGreen() * scale, 0, 255));
		int blue = (int)(constrain(color.getBlue() * scale, 0, 255));
		
		// set color to new brighter color
		Color newColor = new Color(red, green, blue);
		image.setRGB(x, y, newColor.getRGB());
	} 
	
	/** 
	 * Helper function to add saturation to the area. 
	 * @param x          x location of pixel 
	 * @param y          y location of pixel
	 * @param scale      scale of the saturation (higher numbers = more saturation) 
	 * @param image      the image to modify
	 */
	public void saturation(int x, int y, double scale, BufferedImage image) { 
		// get the color components
		Color color = new Color(image.getRGB(x, y));
		int red = color.getRed(); 
		int green = color.getGreen();
		int blue = color.getBlue(); 
		
		// convert to HSB format 
		float[] hsb = Color.RGBtoHSB(red, green, blue, null);
		float hue = hsb[0];
		float saturation = hsb[1];
		float brightness = hsb[2];
		
		// change the saturation
		saturation *= scale; 
		
		// convert back to RGB format 
		int rgb = Color.HSBtoRGB(hue, saturation, brightness);
		
		// set image color 
		image.setRGB(x,  y,  rgb);
	} 
	
	/** 
	 * Makes the area 'deep-fried,' adding noise, contrast, and saturation
	 * 
	 * @param cx 	   center x of square 
	 * @param cy 	   center y of square
	 * @param r 	   radius of square
	 */
	public void deepfry(int cx, int cy, int r) { 
		BufferedImage result = createCopyResult(); 
		for (int y = Math.max(0,  cy-r); y < Math.min(image.getHeight(), cy+r); y++) { 
			for (int x = Math.max(0, cx-r); x < Math.min(image.getWidth(), cx+r); x++) { 
				// add noise 
				noise(x, y, 2, result); // adds noise, with scale constant = 2
				
				// add brightness 
				brightness(x, y, 1.05, result); // increases brightness by factor of 1.05
				
				// add saturation
				saturation(x, y, 1.05, result); // increases saturation by factor of 1.1
				
			}
		}
		image = result; 
	} 
	
	/** 
	 * Makes the entire image 'deep fried,' adding noise, contrast, and saturation to the whole thing
	 * 
	 * @param cx 	    center x of square 
	 * @param cy 	    center y of square
	 * @param r 	    radius of square
	 */
	public void deepfryImage() { 
		BufferedImage result = createCopyResult(); 
		for (int y = 0; y < image.getHeight(); y++) { 
			for (int x = 0; x < image.getWidth(); x++) { 
				// add noise 
				noise(x, y, 2, result); // adds noise, with scale constant = 2
				
				// add brightness 
				brightness(x, y, 1.02, result); // increases brightness by factor of 1.05
				
				// add saturation
				saturation(x, y, 2, result); // increases saturation by factor of 3
				
			}
		}
		image = result; 
	} 
	
	
	/** 
	 * Makes the area emojified, adding an emoji image in the area
	 * 
	 * @param cx 	   center x of emojification area
	 * @param cy 	   center y of emojification area
	 */
	public void emojify(int cx, int cy) { 
		BufferedImage result = createCopyResult(); 
		// generate a random number so that the emojis aren't so tightly packed 
		double frequency = 0.3; // (e.g. if this is .7 only 70% of emojis will be drawn) 
		// draws the emoji onto the screen within the mouse's area
		if (Math.random() > (1 - frequency)) { 
			addEmoji(cx, cy, image, emoji);
		}
	} 
}

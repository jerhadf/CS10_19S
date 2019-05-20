import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * A class demonstrating the deep-fryer, an image processor/filter that adds 
 * noise, brightness, and saturation, making the area appear 'deep fryed.' 
 * You can use this process to manually deep fry your own images, by hand! Enjoy. 
 * See http://knowyourmeme.com/memes/deep-fried-memes
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012, bare bones framework
 * @author Jeremy Hadfield, Dartmouth CS 10, Spring 2018, added deep fryer for SA-3
 */
public class DeepFryer {
	private BufferedImage img; // the image currently being processed 
	
	/**
	 * @param the original image
	 */
	public DeepFryer(BufferedImage img) {
		this.img = img;
	}
	
	public BufferedImage getImage() {
		return img;
	}

	public void setImage(BufferedImage img) {
		this.img = img;
	}
	
	/**
	 * Returns a value that is one of val (if it's between min or max) or min or max (if it's outside that range).
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
	 * Returns an image that's a copy of the current one
	 */
	private BufferedImage createCopyResult() {
		return new BufferedImage(img.getColorModel(), img.copyData(null), img.getColorModel().isAlphaPremultiplied(), null);
	}
	
	/** 
	 * adds noise to the area
	 */
	public void noise(int x, int y, double scale, BufferedImage img) { 
		// add noise to color
		Color color = new Color(img.getRGB(x, y));
		int red = (int)(constrain(color.getRed() + scale * (2*Math.random() - 1), 0, 255));
		int green = (int)(constrain(color.getGreen() + scale * (2*Math.random() - 1), 0, 255));
		int blue = (int)(constrain(color.getBlue() + scale * (2*Math.random() - 1), 0, 255));
		
		// set color to new noisy color
		Color newColor = new Color(red, green, blue);
		img.setRGB(x, y, newColor.getRGB());
	} 
	
	/** 
	 * adds brightness to the area
	 */
	public void brightness(int x, int y, double scale, BufferedImage img) { 
		// increase brightness of color
		Color color = new Color(img.getRGB(x, y));
		int red = (int)(constrain(color.getRed() * scale, 0, 255));
		int green = (int)(constrain(color.getGreen() * scale, 0, 255));
		int blue = (int)(constrain(color.getBlue() * scale, 0, 255));
		
		// set color to new brighter color
		Color newColor = new Color(red, green, blue);
		img.setRGB(x, y, newColor.getRGB());
	} 
	
	/** 
	 * adds saturation to the area
	 */
	public void saturation(int x, int y, double scale, BufferedImage img) { 
		// get the color components
		Color color = new Color(img.getRGB(x, y));
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
		img.setRGB(x,  y,  rgb);
	} 
	
	/** 
	 * Makes the area 'deep-fried,' adding noise, contrast, and saturation
	 * 
	 * @param cx 	center x of square 
	 * @param cy 	center y of square
	 * @param r 	radius of square
	 */
	public void deepfry(int cx, int cy, int r) { 
		BufferedImage result = createCopyResult(); 
		for (int y = Math.max(0,  cy-r); y < Math.min(img.getHeight(), cy+r); y++) { 
			for (int x = Math.max(0, cx-r); x < Math.min(img.getWidth(), cx+r); x++) { 
				// add noise 
				noise(x, y, 2, result); // adds noise, with scale constant = 2
				
				// add brightness 
				brightness(x, y, 1.05, result); // increases brightness by factor of 1.05
				
				// add saturation
				saturation(x, y, 1.05, result); // increases saturation by factor of 1.1
				
			}
		}
		img = result; 
	} 
	
	/** 
	 * Makes the entire image 'deep fried,' adding noise, contrast, and saturation
	 * 
	 * @param cx 	center x of square 
	 * @param cy 	center y of square
	 * @param r 	radius of square
	 */
	
	public void deepfryImage() { 
		BufferedImage result = createCopyResult(); 
		for (int y = 0; y < img.getHeight(); y++) { 
			for (int x = 0; x < img.getWidth(); x++) { 
				// add noise 
				noise(x, y, 2, result); // adds noise, with scale constant = 2
				
				// add brightness 
				brightness(x, y, 1.02, result); // increases brightness by factor of 1.05
				
				// add saturation
				saturation(x, y, 2, result); // increases saturation by factor of 3
				
			}
		}
		img = result; 
	} 
	
}

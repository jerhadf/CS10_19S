import javax.swing.SwingUtilities;

/**
 * DeepFryVideo.java 
 * 
 * @author Jeremy Hadfield
 * @date Apr 8, 2019
 * @category Dartmouth CS10 19S, fun with deep frying videos
 */
public class DeepFryVideo extends Webcam {
	private DeepFryer fryer;  // uses all the deep fry code
	
	/**
	 * Use the DeepFryer image processing class to process the video (deep frying it)
	 */
	@Override
	public void processImage() {
		fryer = new DeepFryer(image);
		fryer.deepfryImage();
		image = fryer.getImage();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new DeepFryVideo();
			}
		});
	}

}

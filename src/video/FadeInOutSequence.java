package video;

import java.awt.image.BufferedImage;

/**
 * A BufferedImage sequence of a given BufferedImage fading in and out.
 * It consists of two arrays: an array of BufferedImages of the input image at varying brightnesses,
 * and an array of ints that describes the order of frames.
 * It also adds an overlay (for this purpose, the Adult Swim logo).
 */
public class FadeInOutSequence {

	private BufferedImage overlay;
	private BufferedImage[] frames;
	private int[] frameOrder;
	
	// I decided to just draw the Adult Swim overlay on each frame in this class (because its brightness stays constant).
	// These variables are used to make sure that the logo can be drawn correctly no matter the resolution.
	final double overlayXRatio = 1092.0 / 1280;
	final double overlayYRatio = 648.0 / 720;
	final double overlayWidthRatio = 108.0 / 1280;
	final double overlayHeightRatio = 24.0 / 720;
	int overlayX, overlayY, overlayWidth, overlayHeight;
	
	/**
	 * Creates a FadeInOutSequence object.
	 * @param slide - The slide to be faded in and out.
	 * @param fps - The intended frames per second.
	 * @param fadeDuration - The intended duration, in seconds, during which the slide fades in and out.
	 * @param holdDuration - The intended duration, in seconds, during which the slide stays at default brightness.
	 * @pparam overlay - A BUfferedImage of the Adult Swim logo.
	 */
	public FadeInOutSequence(BufferedImage slide, int fps, double fadeDuration, double holdDuration, BufferedImage overlay)
	{
		// The amount of frames needed for the transitions and holds.
		int fadeLength = (int) Math.round(fps * fadeDuration);
		int holdLength = (int) Math.round(fps * holdDuration);
		
		// Overlay stuff; now that the resolution is known, we can calculate the dimensions of the logo.
		this.overlay = overlay;
		overlayX = (int) Math.round(overlayXRatio * slide.getWidth());
		overlayY = (int) Math.round(overlayYRatio * slide.getHeight());
		overlayWidth = (int) Math.round(overlayWidthRatio * slide.getWidth());
		overlayHeight = (int) Math.round(overlayHeightRatio * slide.getHeight());
		
		frames = createFrames(slide, fadeLength);
		frameOrder = new int[fadeLength * 2 + holdLength]; // Declare length of whole sequence
		
		// Initialize for loop incrementer
		int i = 0;
		
		// Frames fading in
		for (i = 0; i < fadeLength; i++)
		{
			frameOrder[i] = i;
		}
		// Frames at default brightness
		for (; i < fadeLength + holdLength; i++)
		{
			frameOrder[i] = fadeLength;
		}
		// Frames fading out
		for (; i < frameOrder.length; i++)
		{
			frameOrder[i] = frameOrder.length - i;
		}
	}
	
	/**
	 * Creates an array of BufferedImages of the given BufferedImage at varying brightness levels; brightness slowly increases.
	 * @param slide - The BufferedImage to be faded.
	 * @param fadeLength - 
	 * @return
	 */
	private BufferedImage[] createFrames(BufferedImage slide, int fadeLength)
	{
		double brightnessIncrement = 1.0 / fadeLength; // The amount by which to increment the brightness each frame
		BufferedImage[] output = new BufferedImage[fadeLength + 1];
		
		// Add the slides, slowly incrementing the brightness level
		for (int i = 0; i < fadeLength; i++)
		{
			output[i] = brightnessAdjustedImage(slide, brightnessIncrement * i);
		}
		
		output[fadeLength] = slide; // Add the final slide, which has default brightness
		output[fadeLength].getGraphics().drawImage(overlay, overlayX, overlayY, overlayWidth, overlayHeight, null); // ...and add the overlay
		
		return output;
	}
	
	private BufferedImage brightnessAdjustedImage(BufferedImage bi, double brightness)
	{
		BufferedImage output = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR); // TYPE-3BYTE_BGR is the only type I've seen to be compatible with humble-video.
		output.getGraphics().drawImage(bi, 0, 0, null);
		
		int[] currentPixel = {0, 0, 0};
		
		// Go pixel-by-pixel...
		for (int i = 0; i < bi.getHeight(); i++)
		{
			for (int j = 0; j < bi.getWidth(); j++)
			{
				bi.getRaster().getPixel(j, i, currentPixel);
				
				// ... multiplying each RGB value by the brightness modifier
				currentPixel[0] = (int) Math.max(0, Math.min(255, Math.round(brightness * currentPixel[0])));
				currentPixel[1] = (int) Math.max(0, Math.min(255, Math.round(brightness * currentPixel[1])));
				currentPixel[2] = (int) Math.max(0, Math.min(255, Math.round(brightness * currentPixel[2])));
				
				output.getRaster().setPixel(j, i, currentPixel);
			}
		}
		
		output.getGraphics().drawImage(overlay, overlayX, overlayY, overlayWidth, overlayHeight, null); // ...and add the overlay.
		
		return output;
	}
	
	public BufferedImage[] getFrames()
	{
		return frames;
	}
	
	public int[] getFrameOrder()
	{
		return frameOrder;
	}
	
	public int getFrameWidth()
	{
		return frames[0].getWidth();
	}
	
	public int getFrameHeight()
	{
		return frames[0].getHeight();
	}
}

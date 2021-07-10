package video;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Contains a method which builds the slide to be faded in and out by {@link FadeInOutSequence}.
 *
 */
public class SlideBuilder {
	
	// Some variables to allow for scaling between resolutions
	final static double userPictureLengthRatio = 246.0 / 360;
	final static double userPictureTopPaddingRatio = 32.0 / 360;
	final static double textYRatio = 320.0 / 360;
	
	/**
	 * Builds the slide intended to be faded in and out by {@link FadeInOutSequence}.
	 * @param name - The name of the shoutoutee.
	 * @param userPicture - The picture of the shoutoutee.
	 * @param width - The width of the slide.
	 * @param height - the height of the slide.
	 * @param pathToFont - the system path to the font to be used.
	 * @return A BufferedImage of the slide.
	 */
	public static BufferedImage buildSlide(String name, BufferedImage userPicture, int width, int height, String pathToFont)
	{
		BufferedImage slide = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		loadFont(pathToFont);
		
		Graphics g = slide.getGraphics();
		
		// Actually calculating the screen position and width of the userPicture once it is drawn
		int userPictureSideLength = (int) Math.round(height * userPictureLengthRatio);
		int paddingAboveUserPicture = (int) Math.round(height * userPictureTopPaddingRatio);
		
		if (userPicture != null)
		{
			g.drawImage(userPicture, width / 2 - userPictureSideLength / 2, paddingAboveUserPicture, userPictureSideLength, userPictureSideLength, null);
		}
		else // Just in case...
		{
			g.setColor(Color.RED);
			g.setFont(new Font("Impact", Font.PLAIN, height / 10));
			g.drawString("IMAGE IS NULL", 100, 100);
		}
		
		// Setting up font; scales with image height.
		g.setColor(new Color(230, 235, 204));
		g.setFont(new Font("Helvetica", Font.PLAIN, height / 10));
		
		// Some metrics to keep the font in the correct position
		int nameXCoord, nameWidth;
		FontMetrics fm = g.getFontMetrics();
		nameWidth = fm.stringWidth(name);
		nameXCoord = slide.getWidth() / 2 - nameWidth / 2; // Centre the font
		int textY = (int) Math.round(textYRatio * height);
		
		g.drawString(name, nameXCoord, textY);
		
		g.dispose(); // Not bothered to look up what this does but I'm supposed to do it.
		
		return slide;
	}
	
	/**
	 * Loads and registers a .ttf font.
	 * @param path - The path of the .ttf font.
	 */
	private static void loadFont(String path)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try
		{
			Font f = Font.createFont(Font.TRUETYPE_FONT, new File(path));
			ge.registerFont(f);
		}
		catch (Exception e) { }
	}
}

package video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.zakgof.velvetvideo.IMuxer;
import com.zakgof.velvetvideo.IVelvetVideoLib;
import com.zakgof.velvetvideo.IVideoEncoderStream;
import com.zakgof.velvetvideo.impl.VelvetVideoLib;

/**
 * Creates the fade-in-out sequence as a video.
 * @author Marco
 * Hard-coded this to be MP4 because who's gonna use something else?
 */
public class FioVideoBuilder {
	
	/**
	 * Creates an MP4 video out of a FadeInOutSequence.
	 * @param pathName - The directory in which the output video will go.
	 * @param filename - The path of the output video.
	 * @param imgSequence - The FadeInOutSequence to be converted into a video.
	 * @param fps - the frames-per-second of the video.
	 */
	public static void buildVideo (String pathName, String filename, FadeInOutSequence imgSequence, int fps)
	{
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.OFF);
		
		File output = new File(pathName + filename);
		if (!output.exists()) // If the output file does not exist...
		{
			try
			{
				Files.createDirectories(Paths.get("./" + pathName.replace("/", ""))); // Create directory
				output.createNewFile(); // Create new file
			}
			catch (IOException e)
			{
				System.out.println("I couldn't make a new file. I probably don't have write access. Create a blank file in the \"processing\" subdirectory of where the jar is, called \"fioVideo.mp4\".");
			}
		}
		IVelvetVideoLib lib = VelvetVideoLib.getInstance();
		try (IMuxer muxer = lib.muxer("mp4").videoEncoder(lib.videoEncoder("libx264").framerate(1, fps)).build(output))
		{
			IVideoEncoderStream encoder = muxer.videoEncoder(0);
			
			for (int i = 0; i < imgSequence.getFrameOrder().length; i++)
			{
				BufferedImage img = imgSequence.getFrames()[imgSequence.getFrameOrder()[i]];
				encoder.encode(img);
			}
		}
	}
}

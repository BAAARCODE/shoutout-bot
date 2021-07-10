package video;

import java.io.IOException;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

/**
 * Contains a method which adds a specified audio track to a video using FFmpeg.
 * Probably the only class here that's useful in other contexts.
 */
public class AudioAdder {

	/**
	 * Adds audio to a video. Quite simple. Will cut the length to whichever media is shorter in length.
	 * @param ffmpegFolder - the folder which contains the ffmpeg EXE files, with a slash at the end.
	 * @param videoURL - The video. path
	 * @param audioURL - The audio path.
	 * @param outputURL - The output path.
	 * @throws IOException
	 */
	public static void addAudioToVideo(String ffmpegFolder, String videoURL, String audioURL, String outputURL) throws IOException
	{
		FFmpeg ffmpeg = new FFmpeg(ffmpegFolder + "ffmpeg.exe");
		FFprobe ffprobe = new FFprobe(ffmpegFolder + "ffprobe.exe");
		
		FFmpegBuilder builder = new FFmpegBuilder()
				.addInput(videoURL)
				.addInput(audioURL)
				.addOutput(outputURL)
				.addExtraArgs("-map","0:v","-map","1:a","-c:v","copy","-shortest")
				.done();
		
		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
		executor.createJob(builder).run();
	}
}

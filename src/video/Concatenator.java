package video;

import java.io.IOException;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

/**
 * Contains a method which concatenates three videos using FFMpeg. Does NOT concatenate audio.
 */
public class Concatenator {

	/**
	 * @param ffmpegFolder - Path to the folder in which the ffmpeg and ffprobe .exe files are.
	 * @param video1Url - The first video to be concatenated.
	 * @param video2Url - The second video to be concatenated.
	 * @param video3Url - The third video to be concatenated.
	 * @param outputUrl - The output path.
	 * @throws IOException
	 */
	public static void concatenate3Videos(String ffmpegFolder, String video1Url, String video2Url, String video3Url, String outputUrl, int width, int height) throws IOException
	{
		FFmpeg ffmpeg = new FFmpeg(ffmpegFolder + "ffmpeg.exe");
		FFprobe ffprobe = new FFprobe(ffmpegFolder + "ffprobe.exe");
		
		FFmpegBuilder builder = new FFmpegBuilder()
				.addInput(video1Url)
				.addInput(video2Url)
				.addInput(video3Url)
				.addOutput(outputUrl)
				.setVideoWidth(width)
				.setVideoHeight(height)
				.addExtraArgs(
						"-filter_complex",
						"\"[0:v] [1:v] [2:v] concat=n=3:v=1 [v]\"",
						"-map",
						"\"[v]\"") // Don't know what any of this means sorry
				.done();
		
		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
		executor.createJob(builder).run();
	}
}

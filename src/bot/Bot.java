package bot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import video.AudioAdder;
import video.Concatenator;
import video.FadeInOutSequence;
import video.FioVideoBuilder;
import video.SlideBuilder;

public class Bot extends ListenerAdapter {

	static String pathToFFmpeg = "";
	static String pathToFont = "res/font.ttf"; // Default font location
	static String processingDirectory = "processing/"; // 
	
	public static void main(String[] args) throws LoginException
	{
		BasicConfigurator.configure(); 
		Logger.getRootLogger().setLevel(Level.OFF); 
		
		pathToFFmpeg = "";
		if (args.length < 1) // Missing API key
		{
			System.out.println("I need an API key. Goodbye.");
			System.exit(1);
		}
		else if (args.length < 2) // Missing path to FFmpeg
		{
			System.out.println("I need the path to your FFmpeg installation. Goodbye.");
			System.exit(1);
		}
		else if (args.length > 2) // Path to font exists
		{
			pathToFont = args[2];
		}
		
		pathToFFmpeg = args[1];
		
		System.out.println("Logging in...");
		
		JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES)
		.addEventListeners(new Bot())
		.build();
		
		System.out.println("Login complete!");
	}
	
	public void onMessageReceived(MessageReceivedEvent event)
	{
		Message msg = event.getMessage();
		if (messageIsShoutingOut(msg))
		{
			long time = System.nanoTime();
			//msg.addReaction("\u1f441\ufe0f").queue();
			System.out.println(msg.getMember().getUser().getName() + " is giving a quick shoutout to " + msg.getMentionedMembers().get(0).getEffectiveName() + "!");
			System.out.print("Rendering video...");
			makeVideo(msg.getMentionedMembers().get(0));
			msg.reply(new File("processing/shoutout.mp4")).mentionRepliedUser(false).queue();
			System.out.println("Video sent! (" + (System.nanoTime() - time)/1000000 + "ms)");
		}
	}
	
	private boolean messageIsShoutingOut(Message msg)
	{
		if (	msg.getMentions(MentionType.USER).size() == 1
			&&	msg.getContentRaw().toLowerCase().contains("let")
			&&	msg.getContentRaw().toLowerCase().contains("give a quick shoutout to"))
		{
			return true;
		}
		return false;
	}
	
	private void makeVideo(Member m)
	{
		try {
			BufferedImage pic = getUserAvatar(m.getUser());
			BufferedImage overlay = ImageIO.read(new File("res/adultswim.png"));
			
			System.out.print(".");
			BufferedImage slide = SlideBuilder.buildSlide(m.getEffectiveName().toUpperCase(), pic, 1280, 720, pathToFont);
			System.out.print(".");
			FadeInOutSequence imgSequence = new FadeInOutSequence(slide, 30, 11.0 / 15, 107.0 / 30, overlay);
			System.out.print(".");
			FioVideoBuilder.buildVideo(processingDirectory, "fioVideo.mp4", imgSequence, 30);
			System.out.print(".");
			Concatenator.concatenate3Videos(pathToFFmpeg, "res/beforeShoutout.mp4", processingDirectory + "fioVideo.mp4", "res/afterShoutout.mp4", processingDirectory + "concatenatedVideo.mp4", 1280, 720);
			System.out.println(".");
			AudioAdder.addAudioToVideo(pathToFFmpeg, processingDirectory + "concatenatedVideo.mp4", "res/cheering.mp3", processingDirectory + "shoutout.mp4");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("whoops");
		}
	}
	
	public static BufferedImage getUserAvatar(User user) throws IOException {
		  URLConnection connection = new URL(user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl()).openConnection();
		  connection.setRequestProperty("User-Agent", "bot shoutout-bot");
		  BufferedImage profileImg;
		  try {
		    profileImg = ImageIO.read(connection.getInputStream());
		  } catch (Exception ignored) {
		    profileImg = ImageIO.read(new File("res/error.png"));
		  }
		  return profileImg;
		}

}

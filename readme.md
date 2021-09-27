### *NOTE: I have removed all potentially copyright-infringing content from this repository. This bot will thus no longer be distributed on GitHub with all the files it needs to function.*

# Shoutout Bot
A JDA Discord bot that allows users to type "Let's give a quick shoutout to @User" and get a video resembling the Christina Applegate shoutout from the Eric Andre Show, Season 3, Episode 2.

## Running the Bot
### Prerequisites
* Java 8+
* An extracted [FFmpeg build](https://ffmpeg.org/download.html)
* A [Discord bot account](https://discordpy.readthedocs.io/en/stable/discord.html)
### Configuration and Launch (Windows)
1. Extract the archive.
2. Edit the run.bat file included in the archive as follows:
```
java -jar shoutout.jar BOT_TOKEN PATH_TO_FFMPEG
```
[Click here](https://discordpy.readthedocs.io/en/stable/discord.html) for instructions on how to get your BOT_TOKEN.
The PATH_TO_FFMPEG should be the directory in which the ffprobe and ffmpeg files are. It should end with a forward-slash. For example: `H:/Documents/ffmpeg/bin/`

3. Run the run.bat file to get the bot running. You did it! If you want to shut the bot down, kill it by closing the command prompt.
4. (optional) If you don't like the command prompt being there, you can replace `java` with `javaw` in the run.bat. Just make sure to kill it with your task manager!

## Dependencies
I used Maven to manage (most of) these dependencies.
* [JDA](https://github.com/DV8FromTheWorld/JDA) (version 4.3.0_277)
* [FFmpeg Java](https://github.com/bramp/ffmpeg-cli-wrapper) (version 0.6.2)
* [Velvet Video](https://github.com/zakgof/velvet-video) (version 0.5.2)
  * NOTE: velvet-video-natives had to be downloaded and added to the classpath as a JAR to be properly exported to a runnable JAR.
* [SLF4J API](http://www.slf4j.org/) (version 2.0.0-alpha0)
* [SLF4J LOG4J](https://logging.apache.org/log4j/2.x/) (version 2.0.0-alpha0)

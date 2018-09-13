package ca.carleton.gcrc.tetherScript;




import java.text.ParseException;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;




public class Command {

	public static void main(String[] args) {
		
		Option videofile = Option.builder("v").required(false).longOpt("video_input").desc("The video file for tethering.").hasArg().build();
		Option transcriptfile = Option.builder("t").required(true).longOpt("transcript_input").desc("The transcript file for the video.").hasArg().build();
		
		Option outputdir = Option.builder("o").required(false).longOpt("output_path").desc("(Optional) The output directory for [target].srt, the default place is the current folder.").hasArg().build();
		Option credential = Option.builder("c").required(false).longOpt("credential").desc("Provide credential file for google api.").hasArg().build();
		Option language = Option.builder("l").required(false).longOpt("language").desc("Indicate the language used in the video [en|fr|de|ja|es|it|zhcn]").hasArg().build();
		Option videoId = Option.builder("i").required(false).longOpt("videoId").desc("The videoId of the video on youtube"
				+ "you can find out the videoId through: url: https://youtu.be/{VIDEOID} or url: https://www.youtube.com/watch?v={VIDEOID}").hasArg().build();
		//Option recursiveTethering = Option.builder("r").required(false).longOpt("recursive").desc("If provided, recursive tethering all the video and transcript(same-name) in provided folder")
			//	.hasArg().build();
		
		Options options = new Options();
		options.addOption(videofile);
		options.addOption(transcriptfile);
		options.addOption(outputdir);
		options.addOption(credential);
		options.addOption(language);
		options.addOption(videoId);
		//options.addOption(recursiveTethering);
		String header = "Transcript tethering tool. \n\n";
		String footer = "Example: -- \n"
				+ "java -jar transcript_tether-x.y.z.jar -v 'test.mp4' -t 'test.txt' -c 'client_secret.json' \n"
				+ "java -jar transcript_tether-x.y.z.jar -i 'youtubeID' -t 'test.txt' \n\n";
		
		
		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new DefaultParser();

		// create the Options
		try {
			
			CommandLine line = parser.parse(options, args);
			
			if(line.hasOption('v') && line.getOptionValue("video_input")!= null &&
					line.hasOption('t') && line.getOptionValue("transcript_input")!= null) {
				System.out.format("--> The video file is located at <%s>\n", line.getOptionValue("video_input"));
				System.out.format("--> The transcript file is located at <%s>\n", line.getOptionValue("transcript_input"));   
				ApiExample.execute(line.getOptionValue("video_input")
						,line.getOptionValue("transcript_input")
						,line.getOptionValue("output_path",System.getProperty("user.dir"))
						,line.getOptionValue("credential")
						,line.getOptionValue("language","en"),false);
			} else if(line.hasOption('i') && line.getOptionValue("videoId")!= null &&
					line.hasOption('t') && line.getOptionValue("transcript_input")!= null) {
				System.out.format("--> The existing youtube video id is <%s>\n", line.getOptionValue("videoId"));
				System.out.format("--> The transcript file is located at <%s>\n", line.getOptionValue("transcript_input"));   
				ApiExample.execute(null
						,line.getOptionValue("transcript_input")
						,line.getOptionValue("output_path",System.getProperty("user.dir"))
						,line.getOptionValue("credential")
						,line.getOptionValue("language","en"),true, line.getOptionValue("videoId"));
			} else {
				System.err.println("No video source provided -- Please provide a video source videoFile or videoId");
				System.exit(1);
			}
			/*
			if(line.hasOption('o') && line.getOptionValue("output_path")!= null)
				System.out.format("--> The output folder is at <%s>\n", line.getOptionValue("output_path"));
			if(line.hasOption('c') && line.getOptionValue("credential")!= null)
				System.out.format("--> The credential file is located at <%s>\n", line.getOptionValue("credential"));   
			*/
			
			
		} catch (org.apache.commons.cli.ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("Parsing failed. Reason: " + e.getMessage());
			formatter.printHelp("java -jar transcript_tether.jar",header,  options, footer, true);
		}
		
		
	
		
		
		
		}

	
	
}

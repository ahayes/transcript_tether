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
		
		Option videofile = Option.builder("v").required(true).longOpt("video_file").desc("The video file for tethering.").hasArg().build();
		Option transcriptfile = Option.builder("t").required(true).longOpt("transcript_file").desc("The transcript file for the video.").hasArg().build();
		Option outputdir = Option.builder("o").required(false).longOpt("output_path").desc("The output directory for [target].srt, the default place is the current folder.").hasArg().build();
		Option credential = Option.builder("c").required(false).longOpt("credential").desc("Provide credential file for google api.").hasArg().build();
		
		Options options = new Options();
		options.addOption(videofile);
		options.addOption(transcriptfile);
		options.addOption(outputdir);
		options.addOption(credential);
		String header = "Transcript tethering tool. \n\n";
		String footer = "\n ";
		
		HelpFormatter formatter = new HelpFormatter();
		
		
		
		
		CommandLineParser parser = new DefaultParser();

		// create the Options
		try {
			
			CommandLine line = parser.parse(options, args);
			
			if(line.hasOption('v') && line.getOptionValue("video_file")!= null &&
					line.hasOption('t') && line.getOptionValue("transcript_file")!= null) {
				System.out.format("|--> The video file is located at <%s>\n", line.getOptionValue("video_file"));
				System.out.format("|--> The transcript file is located at <%s>\n", line.getOptionValue("transcript_file"));   
			}
			
			if(line.hasOption('o') && line.getOptionValue("output_path")!= null)
				System.out.format("|--> The output folder is at <%s>\n", line.getOptionValue("output_path"));
			if(line.hasOption('c') && line.getOptionValue("credential")!= null)
				System.out.format("|--> The credential file is located at <%s>\n", line.getOptionValue("credential"));   
			ApiExample.execute(line.getOptionValue("video_file"),line.getOptionValue("transcript_file"),line.getOptionValue("output_path",System.getProperty("user.dir")),line.getOptionValue("credential"));
			
		} catch (org.apache.commons.cli.ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("Parsing failed. Reason: " + e.getMessage());
			formatter.printHelp("teh",header,  options, footer, true);
		}
		
		
	
		
		
		
		}

	
	
}
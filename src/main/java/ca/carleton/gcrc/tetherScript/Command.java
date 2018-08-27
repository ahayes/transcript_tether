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
	    
		Option videofile = Option.builder("v").required(true).longOpt("video").desc("the video file for tethering.").hasArg().build();
		Option transcriptfile = Option.builder("t").required(true).longOpt("transcript").desc("the transcript file for the video.").hasArg().build();
		Option outputdir = Option.builder("o").required(false).longOpt("output").desc("the output directory for [target].srt, the default place is the current folder.").hasArg().build();
		Option credential = Option.builder("c").required(false).longOpt("credential").desc("use given file for credential.").hasArg().build();
		
		Options options = new Options();
		options.addOption(videofile);
		options.addOption(transcriptfile);
		options.addOption(outputdir);
		options.addOption(credential);
		String header = "Transcript tethering tool. \n";
		String footer = "\n ";
		
		HelpFormatter formatter = new HelpFormatter();
		
		
		
		
		CommandLineParser parser = new DefaultParser();

		// create the Options
		try {
			
			CommandLine line = parser.parse(options, args);
			
			if(line.hasOption("video") && line.getOptionValue("video")!= null &&
					line.hasOption("transcript") && line.getOptionValue("transcript")!= null) {
		    	System.out.format("|--> The video file is located at <%s>\n", line.getOptionValue("video"));
		    	System.out.format("|--> The transcript file is located at <%s>\n", line.getOptionValue("transcript"));   
			}
			
			if(line.hasOption("output") && line.getOptionValue("output")!= null)
		    	System.out.format("|--> The output folder is at <%s>\n", line.getOptionValue("output"));
		    if(line.hasOption("credential") && line.getOptionValue("credential")!= null)
		    	System.out.format("|--> The credential file is located at <%s>\n", line.getOptionValue("credential"));   
			ApiExample.execute(line.getOptionValue("video"),line.getOptionValue("transcript"),line.getOptionValue("output",System.getProperty("user.dir")),line.getOptionValue("credential"));
			
		} catch (org.apache.commons.cli.ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("Parsing failed. Reason: " + e.getMessage());
			formatter.printHelp("teh",header,  options, footer, true);
		}
		
		
	
		
	    
	    
	  }

	
	
}

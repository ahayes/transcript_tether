


import com.google.devtools.common.options.OptionsParser;
import java.util.Collections;

public class Command {

	public static void main(String[] args) {
	    OptionsParser parser = OptionsParser.newOptionsParser(ServiceOptions.class);
	    parser.parseAndExitUponError(args);
	    ServiceOptions options = parser.getOptions(ServiceOptions.class);
	    if (options.videoPath.isEmpty() || options.transcriptPath.isEmpty() ) {
	      printUsage(parser);
	      return;
	    }
	    
	    System.out.format("The video:transcript files are at %s--->%s...\n", options.videoPath, options.transcriptPath);
	    
	    System.out.format("\\--> The output folder is at <%s>\n", options.outputPath);
	    
	    ApiExample.execute(options.videoPath, options.transcriptPath, options.outputPath,options.credential);
	  }

	  private static void printUsage(OptionsParser parser) {
	    System.out.println("Usage: java -jar command.jar OPTIONS");
	    System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(),
	                                              OptionsParser.HelpVerbosity.LONG));
	  }
	
}

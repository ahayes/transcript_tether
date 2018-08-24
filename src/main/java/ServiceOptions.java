
import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

import java.util.List;

public class ServiceOptions extends OptionsBase {

	
	@Option(
			name = "help",
			abbrev = 'h',
			help = "Prints usage info.",
			defaultValue = "true"
			)
	public boolean help;
	
	@Option(
			name = "video",
			abbrev = 'v',
			help = "The path of input video file",
			defaultValue = ""
			)
	public String videoPath;
	
	@Option(
			name = "transcript",
			abbrev = 't',
			help = "The path of transcript file [with or without the timecode]",
			defaultValue = ""
			)
	public String transcriptPath;
	
	@Option(
			name = "output",
			abbrev = 'o',
			help = "The path to output the syncronized transcript file",
			defaultValue = "./"
			)
	public String outputPath;
	
	@Option(
			name = "credential",
			abbrev = 'c',
			help = "The path to the credential file. (If you don't have one, please refer to "
					+ "Url-[\"https://console.cloud.google.com/apis/dashboard\"])",
			defaultValue = ""
			)
	public String credential;
	
}

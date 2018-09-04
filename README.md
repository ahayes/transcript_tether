[![Build Status](https://travis-ci.com/qrafzv/transcript_tether.svg?branch=beta)](https://travis-ci.com/qrafzv/transcript_tether)

# transcript_tether
Cross-platform java library and command-line utility to generate a subtitle file given a video and transcript

# Release
-------

To create a standalone executable java program:

> gradle fatJar

The executable is located inside {PROJECT_FOLDER}/build/lib/

# Usage
------

> java -jar teh-x.y.z.jar<br>
> -c,    --credential <arg>    Provide credential file for google api.<br>
> -l,--language <arg>          (Optional) Indicate the language used in the video [en|fr].<br>
> -o,--output_path <arg>       (Optional) The output directory for [target].srt, the default place is the current folder.<br>
>-t,--transcript_file <arg>   The transcript file for the video.<br>
>-v,--video_file <arg>        The video file for tethering.


## Example

> java -jar teh-x.y.z.jar -v 'test.mp4' -t 'test.txt' -c 'HOME/.client_secret.json'
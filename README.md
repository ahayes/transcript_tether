[![Build Status](https://travis-ci.com/qrafzv/transcript_tether.svg?branch=beta)](https://travis-ci.com/qrafzv/transcript_tether)

# transcript_tether
Cross-platform java library and command-line utility to generate a subtitle file given a video and transcript

# Release
-------

To create a standalone executable java program:

> gradle exec

The executable is located inside {PROJECT_FOLDER}/build/libs/

# Running_prerequisition

[Java SE Runtime Environment 8 [Downloads](http://www.oracle.com/technetwork/java/javase/jre8-downloads-2133155.html)]<br>
[Setting JAVA_HOME environment variables](https://www.mkyong.com/java/how-to-set-java_home-on-windows-10/)

# Usage
------

> java -jar transcript_tether-x.y.z.jar<br>
> -c,    --credential <arg>    Provide credential file for google api.<br>
> -l,--language <arg>          (Optional) Indicate the language used in the video [en|fr].<br>
> -o,--output_path <arg>       (Optional) The output directory for [target].srt, the default place is the current folder.<br>
>-t,--transcript_file <arg>   The transcript file for the video.<br>
>-v,--video_file <arg>        The video file for tethering.


## Example

> java -jar transcript_tether-x.y.z.jar -v 'test.mp4' -t 'test.txt' -c 'HOME/.client_secret.json'
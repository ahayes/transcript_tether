[![Build Status](https://travis-ci.com/qrafzv/transcript_tether.svg?branch=master)](https://travis-ci.com/qrafzv/transcript_tether)

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

> java -jar transcript_tether-x.y.z.jar -v 'test.mp4' -t 'test.txt' -c 'HOME/.client_secret.json'<br>
> java -jar transcript_tether-x.y.z.jar -i 'youtubevideoid' -t 'test.vtt'

## FAQ

1. First, to install and deploy the software, you need to enable the youtube Data API v3 "https://console.developers.google.com/apis/library/youtube.googleapis.com". 

2. Second, at the first execution, the program will prompt up for you to create the OAUTH2.0 json file. Just follow the process, download the secret.json file and rerun the program with "-c {secret.json}". The json file will be cached to the user.home folder, so that you don't need to enter the secret info at next time.

3. The youtube uploading service has a 20 min length limitation for uploaded video. To remove this limitation, perform the verification on youtube account at "https://www.youtube.com/my_videos_upload_verify"

4. The video should be uploaded to the same youtube account, the youtube account authorizes the API.

5. The youtube channel needs to be created, since the program upload the video to users' personal channel (Privated).


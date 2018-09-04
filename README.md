# transcript_tether
Cross-platform java library and command-line utility to generate a subtitle file given a video and transcript

# Release
-------

To create a new executable java program:

> gradle fatJar

The executable is located inside {PROJECT_FOLDER}/build/lib/

# Usage
------

> java -jar teh-x.y.z.jar --Options

## Example

> java -jar teh-x.y.z.jar -v 'test.mp4' -t 'test.txt' -c 'HOME/.client_secret.json'
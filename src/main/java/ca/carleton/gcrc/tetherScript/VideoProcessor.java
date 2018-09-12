package ca.carleton.gcrc.tetherScript;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.YouTube;

public class VideoProcessor {
	
    /**
     * Define a global instance of a YouTube object, which will be used to make
     * YouTube Data API requests.
     */
    private static YouTube youtube;
    private static String VideoId;

	



	public VideoProcessor(YouTube y2b) throws NullPointerException {
		
		if(y2b != null) {
			youtube = y2b;
		
		}
		else {
			throw new NullPointerException();
			
		}
		
		
	}
	
	
	
public void execute() throws GoogleJsonResponseException, IOException {
		
		
	
        String mime_type = "video/*";
        String media_filename = ApiExample.VIDEOFILE; //"Massively multi-player.mp4";
        File media_file = new File(media_filename);
        String media_file_name_onyoutube = media_file.getName();
        //final long bytes = media_file.length();
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("part", "snippet,status");


        Video video = new Video();
        VideoSnippet snippet = new VideoSnippet();
        snippet.set("categoryId", "22");
        snippet.set("description", "Description of uploaded video.");
        snippet.set("title", media_file_name_onyoutube);
        VideoStatus status = new VideoStatus();
        status.set("privacyStatus", "private");

        video.setSnippet(snippet);
        video.setStatus(status);

        InputStreamContent mediaContent = new InputStreamContent(mime_type,new FileInputStream(media_file));
        mediaContent.setLength(media_file.length());
        YouTube.Videos.Insert videosInsertRequest = youtube.videos().insert(parameters.get("part").toString(), video, mediaContent);
        MediaHttpUploader uploader = videosInsertRequest.getMediaHttpUploader();


        uploader.setDirectUploadEnabled(false);

        MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
            public void progressChanged(MediaHttpUploader uploader) throws IOException {
            	String anim  = "----------------->";
            	                
                switch (uploader.getUploadState()) {
                    case INITIATION_STARTED:
                        System.out.println("Video --Initiation Started");
                        break;
                    case INITIATION_COMPLETE:
                       // System.out.println("Video --Initiation Completed");
                        break;
                    case MEDIA_IN_PROGRESS:
                        //System.out.println("Video --Upload in progress");
                    	String percentage =  NumberFormat.getPercentInstance(Locale.US).format(uploader.getProgress());
                        
                        System.out.format("\rVideo --Upload Percentage: %3s  |%-18s|" , percentage,  anim.substring( anim.length()-(int)(uploader.getProgress()*anim.length()), anim.length()) );
                        break;
                    case MEDIA_COMPLETE:
                    	percentage =  NumberFormat.getPercentInstance(Locale.US).format(uploader.getProgress());
                    	System.out.format("\rVideo --Upload Percentage: %3s  |%-18s|" , percentage,  anim.substring( anim.length()-(int)(uploader.getProgress()*anim.length()), anim.length()) );
                        System.out.println(" Video --Upload Completed!");
                        break;
                    case NOT_STARTED: 
                        System.out.println("Video --Upload Not Started!");
                        break;
                }
            }
        };
        uploader.setProgressListener(progressListener);
        Video response = videosInsertRequest.execute();
        VideoId = response.getId();


        //System.out.println("UPLOADINFO_---- ");
	//}catch (GoogleJsonResponseException e) {
      //  e.printStackTrace();
       // System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
  
		
		
	}
	

	public  String getVideoId() {
		return VideoId;
	}



	public static void setVideoId(String videoId) {
		VideoId = videoId;
	}
}

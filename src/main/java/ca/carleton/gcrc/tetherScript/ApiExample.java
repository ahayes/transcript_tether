package ca.carleton.gcrc.tetherScript;
// Sample Java code for user authorization

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class ApiExample {

    /** Application name. */
    public static final String APPLICATION_NAME = "API Sample";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
    System.getProperty("user.home"), ".credentials/java-youtube-api");

    /** Global instance of the {@link FileDataStoreFactory}. */
    //private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;
    
    protected static String VIDEOFILE = null;
    protected static String TRANSCRIPTFILE = null;
    protected static String OUTPUTFILE = null;
    protected static String CREDENTIALFILE = null;
    protected static final String CREDENTIALFILE_INTERNAL =  System.getProperty("user.home") + "/.secret/client_secret_other.json";
    

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
   
    	

    static {
        try {
        
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
           // DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
   /* public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = ApiExample.class.getResourceAsStream("/client_secret_other.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader( in ));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(DATA_STORE_FACTORY)
            .setAccessType("offline")
            .build();
        Credential credential =
        		new AuthorizationCodeInstalledApp(
        flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
            "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }
*/
    /**
     * Build and return an authorized API client service, such as a YouTube
     * Data API client service.
     * @return an authorized API client service
     * @throws IOException
     */
    public static YouTube getYouTubeService() {
    	Credential credential =null ;
    	try {
         credential = Auth.authorize(CREDENTIALFILE, "videos");

    	}catch(IOException e)
    	{
    		e.printStackTrace();
    		
    	}
        return new YouTube.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }
    
   

    protected static void execute(String videoFile, String transcriptFile, String outputPath, String credential)  {
    	File savedCredential = new File(CREDENTIALFILE_INTERNAL);
    	if(credential == null) {
    		
    		if(savedCredential.length() == 0) {
    			System.out.println("Before the first time uploading, you must provide your client_secret file.");
    		    System.exit(1);
    		} else {
    			CREDENTIALFILE = CREDENTIALFILE_INTERNAL;
    		}
    		
    	} else {
    		CREDENTIALFILE = credential;
    		
    	}
       // YouTube youtube = getYouTubeService();
    	
    	VIDEOFILE = videoFile;
    	TRANSCRIPTFILE = transcriptFile;
    	
    	
    	String outputFileDir = outputPath;
		if(outputPath.charAt(outputPath.length()-1) != '/')
			outputFileDir += '/';
		String inputNameWithoutExt =  VIDEOFILE.substring(VIDEOFILE.lastIndexOf('/')+1,VIDEOFILE.lastIndexOf('.'));
		OUTPUTFILE = outputFileDir + inputNameWithoutExt + ".srt";
		
    	
    	
    	
    	
        try {
            YouTube youtube = getYouTubeService();
/*            String mime_type = "video/*";
            String media_filename = "Massively multi-player.mp4";
            File media_file = new File(ApiExample.class.getResource(media_filename).toURI());
            //final long bytes = media_file.length();
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("part", "snippet,status");


            Video video = new Video();
            VideoSnippet snippet = new VideoSnippet();
            snippet.set("categoryId", "22");
            snippet.set("description", "Description of uploaded video.");
            snippet.set("title", "Test video upload");
            VideoStatus status = new VideoStatus();
            status.set("privacyStatus", "private");

            video.setSnippet(snippet);
            video.setStatus(status);

            InputStreamContent mediaContent = new InputStreamContent(mime_type,
            ApiExample.class.getResourceAsStream(media_filename));
            mediaContent.setLength(media_file.length());
            YouTube.Videos.Insert videosInsertRequest = youtube.videos().insert(parameters.get("part").toString(), video, mediaContent);
            MediaHttpUploader uploader = videosInsertRequest.getMediaHttpUploader();


            uploader.setDirectUploadEnabled(false);

            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            System.out.println("Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            System.out.println("Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            System.out.println("Upload in progress");
                            System.out.println("Upload percentage: " +   uploader.getProgress() );
                            break;
                        case MEDIA_COMPLETE:
                            System.out.println("Upload Completed!");
                            break;
                        case NOT_STARTED: 
                            System.out.println("Upload Not Started!");
                            break;
                    }
                }
            };
            uploader.setProgressListener(progressListener);
            Video response = videosInsertRequest.execute();
            String VideoId = response.getId();*/
            VideoProcessor vidp = new VideoProcessor(youtube);
            vidp.execute();
            System.out.println("VIDEOID is: "+ vidp.getVideoId());
            CaptionProcessor capp = new CaptionProcessor(youtube);
            
            Caption uploadedCaptionResponse = capp.uploadCaption(vidp.getVideoId(), "en","plain_transcript_n2",new File(TRANSCRIPTFILE) );
            String captionId = uploadedCaptionResponse.getId();
            System.out.println("Waiting for sync...");
            ProgressBarRotating pb1 = new ProgressBarRotating();
            pb1.start();
            
            	while(pb1.showProgress == true){
                // do some activities
            		
            		Caption caption = capp.listCaption(vidp.getVideoId(), captionId);
            		if(caption != null) {
            			Thread.sleep(60000);
            			String captionSyncStatus =  caption.getSnippet().getStatus();    
            			if (captionSyncStatus.equals("syncing") )
            				continue;
            			else if(captionSyncStatus.equals("serving")){
            				pb1.showProgress = false;
            				break;
            			}
            		}
                 //System.out.println(response);
            				
              
             
            
            	}
            System.out.println("\n We start to download the syncronized caption");

            	 
            //captionId = "rxIHgFycrpqyzrUThEWut_ZbOMLC84iHroMGBZFbeadcSpjZbQVdlFXCOupU0rUh";
            // CaptionProcessor capp = new CaptionProcessor(youtube);
            capp.downloadCaption(captionId);
            System.out.println("Download the transcipt successfully");
            			 
        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}


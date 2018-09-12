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
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.YouTube;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;


public class ApiExample {

	
	private static final String CREDENTIAL_NOTIFY = 
			"The credential file can be created and retrieved at: \n" 
			+
			"\"https://console.cloud.google.com/apis/credentials\" "
			+"Follow the google instruction, enter your info and \"Create project\""
			+
			"\n \"Create Credentials\" ==> \"OAuth client ID\" ==> \"Other\" ==> \"Create\""
			+
			"\nDownload the credential file to your local folder. "
			+ 
			"\nExecute this program again with option: '-c {path_to_credential_file}'. \n";
    /** Application name. */
    public static final String APPLICATION_NAME = "API Sample";

   
    /** Global instance of the {@link FileDataStoreFactory}. */
    //private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;
    
    protected static String VIDEOFILE = null;
    protected static String EXISTINGVIDEOID = null;
    
    protected static String VIDEOID = null;
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
     * Build and return an authorized API client service, such as a YouTube
     * Data API client service.
     * @return an authorized API client service
     * @throws IOException
     */
    public static YouTube getYouTubeService() {
    	Credential credential =null ;
    	try {
         credential = Auth.authorize(CREDENTIALFILE, "tether");

    	}catch(IOException e)
    	{
    		e.printStackTrace();
    		
    	}
        return new YouTube.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }
    
   

    protected static void execute(String videoFile, 
    		String transcriptFile, String outputPath, 
    		String credential,
    		String lang, boolean hasUploadedVideoID, String...videoId)  {
   
    	File savedCredential = new File(CREDENTIALFILE_INTERNAL);
    	if(credential == null) {
    		
    		if(savedCredential.length() == 0) {
    			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    			System.out.println("\nYou need a \"google credential\" to execute this program, please provide your credential file (.json).");
    			System.out.println(CREDENTIAL_NOTIFY);
    			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    			ApiExample.openInBrowser("https://console.cloud.google.com/apis/credentials");
    			System.exit(1);
    		} else {
    			CREDENTIALFILE = CREDENTIALFILE_INTERNAL;
    		}
    		
    	} else {
    		CREDENTIALFILE = credential;
    		
    	}
       // YouTube youtube = getYouTubeService();
    	if(hasUploadedVideoID) {
    		if(videoId.length >0 && videoId[0] != null)
    			EXISTINGVIDEOID = videoId[0];
    		else {
    			System.err.println("The existing videoId is not here");
    			System.exit(1);
    		}
    	}
    		
    	
    	TRANSCRIPTFILE = transcriptFile;
    	
    	
    	String outputFileDir = outputPath;
		if(outputPath.charAt(outputPath.length()-1) != '/')
			outputFileDir += '/';
		if(!hasUploadedVideoID) {
			VIDEOFILE = videoFile;
			String tmpfile = new File(VIDEOFILE).getName();
		
			String inputNameWithoutExt = tmpfile.lastIndexOf('.')>0?  tmpfile.substring(0,tmpfile.lastIndexOf('.')) : tmpfile ;
			OUTPUTFILE = outputFileDir + inputNameWithoutExt + ".srt";
		} else {
				 
				OUTPUTFILE = outputFileDir + EXISTINGVIDEOID + ".srt";

		}
        try {
            YouTube youtube = getYouTubeService();
           
            if(!hasUploadedVideoID) {
            	VideoProcessor vidp = new VideoProcessor(youtube);
            	vidp.execute();
            	VIDEOID = vidp.getVideoId();
            	System.out.println("VideoId is: "+ VIDEOID);
            } else {
            	VIDEOID = EXISTINGVIDEOID;
            	System.out.println("The existing VideoId is: "+ VIDEOID);
            	
            }
            CaptionProcessor capp = new CaptionProcessor(youtube);
            LANGUAGE curLanguage = LANGUAGE.valueOf(lang.toUpperCase());
            Caption uploadedCaptionResponse =null;
            String transcript_file_name_onyoutube = new File(TRANSCRIPTFILE).getName();
            switch(curLanguage) {
            case EN:
            	uploadedCaptionResponse = capp.uploadCaption(VIDEOID, "en",transcript_file_name_onyoutube,new File(TRANSCRIPTFILE) );
            	break;
            case FR:
            	uploadedCaptionResponse = capp.uploadCaption(VIDEOID, "fr",transcript_file_name_onyoutube,new File(TRANSCRIPTFILE) );
            	break;
            case ES:
            	uploadedCaptionResponse = capp.uploadCaption(VIDEOID, "es",transcript_file_name_onyoutube,new File(TRANSCRIPTFILE) );
            	break;
            case IT:
            	uploadedCaptionResponse = capp.uploadCaption(VIDEOID, "it",transcript_file_name_onyoutube,new File(TRANSCRIPTFILE) );
            	break;
            case ZHCN:
            	uploadedCaptionResponse = capp.uploadCaption(VIDEOID, "zh-CN",transcript_file_name_onyoutube,new File(TRANSCRIPTFILE) );
        		break;
            case DE:
            	uploadedCaptionResponse = capp.uploadCaption(VIDEOID, "de",transcript_file_name_onyoutube,new File(TRANSCRIPTFILE) );
        		break;
            case JA:
            	uploadedCaptionResponse = capp.uploadCaption(VIDEOID, "ja",transcript_file_name_onyoutube,new File(TRANSCRIPTFILE) );
        		break;
            default:
            	uploadedCaptionResponse = capp.uploadCaption(VIDEOID, "en",transcript_file_name_onyoutube,new File(TRANSCRIPTFILE) );
            	
            
            }
            
            String captionId = uploadedCaptionResponse.getId();
            System.out.println("The google synchronization engine is running, this may take a while");
            System.out.println("Please be patient, this process can take up to 2X{video_length}...");
            System.out.println("Waiting for sync...");
            ProgressBarRotating pb1 = new ProgressBarRotating();
            pb1.start();
            
            	while(pb1.showProgress == true){
                // do some activities
            		
            		Caption caption = capp.listCaption(VIDEOID, captionId);
            		if(caption != null) {
            			Thread.sleep(60000);
            			String captionSyncStatus =  caption.getSnippet().getStatus();    
            			if (captionSyncStatus.equals("syncing") )
            				continue;
            			else if(captionSyncStatus.equals("serving")){
            				pb1.showProgress = false;
            				break;
            			}
            		} else {
            			
            			System.err.println("The caption doesn't exist");
            			System.exit(1);
            			
            		}
         
            				
              
             
            
            	}
            System.out.println("\n We start to download the syncronized caption");

            	 
            //captionId = "rxIHgFycrpqyzrUThEWut_ZbOMLC84iHroMGBZFbeadcSpjZbQVdlFXCOupU0rUh";
            // CaptionProcessor capp = new CaptionProcessor(youtube);
            capp.downloadCaption(captionId);
            System.out.println("Download the timecoded transcript successfully");
            //https://www.youtube.com/timedtext_editor?v=5WRBROTmW4I&lang=en&name=
            
            
            String finalTuneUrl = "https://www.youtube.com/timedtext_editor?v="+VIDEOID+"&lang=en&name="
                    + transcript_file_name_onyoutube +"&kind=&contributor_id=0&bl=vmp&action_view_track=1&sow=yes&ui=se";
            System.out.println("The transcript can be fine-tuned at:"+ "\""+ finalTuneUrl + "\"");
           
            openInBrowser(finalTuneUrl);
        } catch (GoogleJsonResponseException e) {
            //e.printStackTrace();
            System.err.println("HTTP ERROR CODE: "+e.getStatusCode());
            switch (e.getStatusCode()) {
            	case 401:
            		System.err.println("[INFO] If you have a brand new youtube account, you need to create a channel, so that this program can upload content.");
        			openInBrowser("https://www.youtube.com/create_channel");
        			System.err.println("[INFO] Also, please verify your youtube account at \"https://www.youtube.com/verify\" ");
        			break;
            	default: 
            		if(e.getDetails().getMessage().endsWith("parameter could not be found.")) {
            			System.err.println("[INFO] The video doesnot exist, two potential causes: 1. length limitation 2. duplicated video");
            		} else if(e.getDetails().getMessage().startsWith("Access Not Configured. YouTube Data API"))
            		{
            			String enableAPIURL = (String) e.getDetails().getErrors().get(0).get("extendedHelp");
            			
            			System.err.println("[INFO] The youtube data API needs to be enabled at this website: " + enableAPIURL);
            			openInBrowser(enableAPIURL);
            		} else if (Integer.valueOf(e.getStatusCode()).compareTo(401)==0){
            	
            			
            		}
            }
        } catch (Throwable t) {
            //t.printStackTrace();
        }
    }
    private static void openInBrowser(String url) {
    	
    	try {
    		URI uri = new URL(url).toURI();
    		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(uri);
            } else {
                String os = System.getProperty("os.name").toLowerCase();
                Runtime rt = Runtime.getRuntime();
                if(os.indexOf("win") >= 0)
                	rt.exec("start " + url);
                else if(os.indexOf("mac") >= 0)
                	rt.exec("open " + url);
                else if(os.indexOf("nix") >=0 || os.indexOf("nux") >=0)
                	rt.exec("xdg-open " + url);
            }
 
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    private enum LANGUAGE  {
    		EN,
    		FR,
    		DE,
    		JA,
    		ZHCN,
    		IT,
    		ES
    		
    		
    };
}


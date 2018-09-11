package ca.carleton.gcrc.tetherScript;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Shared class used by every sample. Contains methods for authorizing a user and caching credentials.
 */
public class Auth {

    /**
     * Define a global instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define a global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * This is the directory that will be used under the user's home directory where OAuth tokens will be stored.
     */
    private static final String CREDENTIALS_DIRECTORY = ".youtubeapi3credentials";
    

    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");
    /**
     * Authorizes the installed application to access user's protected data.
     *
     * @param scopes              list of scopes needed to run youtube upload.
     * @param credentialDatastore name of the credential datastore to cache OAuth tokens
     */
    public static Credential authorize( String client_secret_json, String credentialDatastore) throws IOException {

        // Load client secrets.
    	if(!isSameCredentialJson(client_secret_json, ApiExample.CREDENTIALFILE_INTERNAL)) {
     
    		overrideOldInternalCred(ApiExample.CREDENTIALFILE_INTERNAL, client_secret_json);
    		
    	}
    	
    	Reader clientSecretReader = new InputStreamReader(new FileInputStream(ApiExample.CREDENTIALFILE_INTERNAL ));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);

        // Checks that the defaults have been replaced (Default = "Enter X here").
       

        // This creates the credential datastore at ~/.credentials/${credentialDatastore}
        FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
        DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDatastore);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setCredentialDataStore(datastore)
                .build();

        // Build the local server and bind it to port 8080
       // LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();

        // Authorize.
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
    
    
    private static boolean isSameCredentialJson(String l_json, String r_json) throws IOException {
    	try {
    		File lfile = new File(l_json);
    		File rfile = new File(r_json);
    		if( !rfile.exists())
    			return false;
    		if(!lfile.exists())
    			throw new IOException();
    		Reader l_clientSecretReader = new InputStreamReader(new FileInputStream(l_json ));
    		GoogleClientSecrets l_clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, l_clientSecretReader);
    		Reader r_clientSecretReader = new InputStreamReader(new FileInputStream(r_json ));
    		GoogleClientSecrets r_clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, r_clientSecretReader);
        
    		return l_clientSecrets.getDetails().getClientId().equals(r_clientSecrets.getDetails().getClientId());
    	} catch(Exception e) {
    		e.printStackTrace();	
    		System.exit(1);
    		return false;
    	}
        
    }
    private static void overrideOldInternalCred(String dst, String srt) throws IOException{
    	   InputStream is = null;
    	    OutputStream os = null;
    	    try {
    	        is = new FileInputStream(srt);
    	        File folder = new File(System.getProperty("user.home") + "/.secret" );
    	        if( !folder.exists())
    	        	folder.mkdir();
    	        os = new FileOutputStream(dst,false);
    	        byte[] buffer = new byte[1024];
    	        int length;
    	        while ((length = is.read(buffer)) > 0) {
    	            os.write(buffer, 0, length);
    	        }
    	    }catch(IOException e) {
    	    	e.printStackTrace();
    	    }
    	    finally {
    	        is.close();
    	        os.close();
    	    }
    }
}

	

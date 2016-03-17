package com.mycompany.myapp.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.java6.auth.oauth2.GooglePromptReceiver;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.mycompany.myapp.domain.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by c4r0n0s on 06.03.16.
 */
@Service
public class GoogleDriveService {

    private final Logger log = LoggerFactory.getLogger(GoogleDriveService.class);

    private static final String CLIENT_ID = "862957035176-7l03qga94bl75t1i07jnfn0kl9dtibg5.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "5095SSHUcBIfG3Wxn31hjWWl";
    private static final String REDIRECT_URI = "http://127.0.0.1:8080/";
    private static final String APPLICATION_NAME = "OnlineMusic";
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        "src/main/resources/", ".credentials/drive.json");
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    private static Credential credential = null;
    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = new JacksonFactory();
    public static String FOLDER_ID = "0ByyuEB-HD5xibFVtQ1NjOTZlZG8";

    @Autowired
    private MusicService musicService;

    static {
        try {
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Credential authorize() throws IOException {

        InputStream in = GoogleDriveService.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_FILE);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        credential = new AuthorizationCodeInstalledApp(
            flow, new GooglePromptReceiver()).authorize("user");

        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public Drive getDriveService() throws IOException {
        if (credential == null) {
            credential = authorize();
        }

        return new Drive.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }

    public void getFilesFromFolder(String folderId) throws IOException {
        Drive driveService = getDriveService();

        // Print the names and IDs for up to 10 files.
        Drive.Children.List request = driveService.children().list(folderId);
        do {
            ChildList children = request.execute();
            for (ChildReference child : children.getItems()) {
                System.out.println("File Id: " + child.getId());
            }
            request.setPageToken(children.getNextPageToken());
        } while (request.getPageToken() != null &&
            request.getPageToken().length() > 0);

    }

    public File uploadFile(String title, String mimeType, InputStream stream) {
        File body = new File();
        body.setTitle(title);
        body.setMimeType(mimeType);

        if (FOLDER_ID != null && FOLDER_ID.length() > 0) {
            body.setParents(
                Arrays.asList(new ParentReference().setId(FOLDER_ID)));
        }

        InputStreamContent mediaContent = new InputStreamContent(mimeType, new BufferedInputStream(stream));
        try {
            Drive driveService = getDriveService();

            return driveService.files().insert(body, mediaContent).execute();
        } catch (IOException e) {
            log.error("un error en drive service: "+ e);
        }

        return null;
    }

}

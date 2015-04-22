package Models;
import Classes.MetaMusicSong;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.CurrentUserRequest;
import com.wrapper.spotify.methods.GetMySavedTracksRequest;
import com.wrapper.spotify.methods.authentication.RefreshAccessTokenRequest;
import com.wrapper.spotify.models.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by matt.rowe on 4/15/2015.
 * Come code is copied from / modified from src: https://github.com/thelinmichael/spotify-web-api-java
 */
public class SpotifyAPIModel {

    private static String clientID;
    private static String clientSecret;
    private static String redirectURL = "http://google.com";
    private static Api api;
    private static String refreshToken;
    private static String accessToken;
    private static String successOrFailureForAuthentication;
    private static String userID;


    public static void getKeyandSecret() {
        try {
            FileReader reader = new FileReader("../javaFinalProjectKeysAndSecrets/spotifyKeys.txt");
            BufferedReader bufReader = new BufferedReader(reader);
            String line = bufReader.readLine();
            String[] lineArray = line.split(",");
            clientID = lineArray[0];
            line = bufReader.readLine();
            lineArray = line.split(",");
            clientSecret = lineArray[0];
            reader.close();
        } catch  (Exception e) {
            System.out.println("Problem reading files");
        }
    }

    public static void buildAPI() {
        api = Api.builder()
                .clientId(clientID)
                .clientSecret(clientSecret)
                .redirectURI(redirectURL)
                .build();
    }

    public static String getAuthenticationURL() {
        /* Set the necessary scopes that the application will need from the user */
        //https://developer.spotify.com/web-api/using-scopes/
        final List<String> scopes = Arrays.asList("user-read-private", "playlist-read-private", "user-follow-read", "user-library-read");

        /* Set a state. This is used to prevent cross site request forgeries. */
        final String state = "metaMusic";

        String authorizeURL = api.createAuthorizeURL(scopes, state);
        return authorizeURL;
    }

    public static boolean getAuthenticationTokenFromURL(String URL) {

        /* Application details necessary to get an access token */
        //example URL:  https://www.google.com/?code=AQCVmqZGVahbN4OyTm0znRgSTu3aq6V0yLABaqhX8joTnlknFUpLY2AKjsOUoUnMHLeDUZyjkH6-VYPa9_NEDtlvh4eSsxHxOGAFYvgkHo0BGZJdkV1ZryNDFqYk-b7kQD7fNujT1o-yYN3Uf6jzyj5qfLxJH5JtfUb_3yFfa-Cq6T-jSzX4r_tdSYOi1JTNM9FEVAzZI8i4WUPIy5XZkhb2y1uLWC16vRCQepOPvncLpEBFan1FNrjvHHxdzkwhoYOBumHjx48abIwjI92zhj1PXwdwWw&state=metaMusic&gws_rd=ssl
        String code = URL;
        //eliminate the "http://google.com"
        code = code.substring(code.indexOf('=') + 1);
        code = code.substring(0, code.indexOf('&'));
        successOrFailureForAuthentication = "";

        /* Make a token request. Asynchronous requests are made with the .getAsync method and synchronous requests
         * are made with the .get method. This holds for all type of requests. */
        SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = api.authorizationCodeGrant(code).build().getAsync();

        /* Add callbacks to handle success and failure */
        Futures.addCallback(authorizationCodeCredentialsFuture, new FutureCallback<AuthorizationCodeCredentials>() {
            @Override
            public void onSuccess(AuthorizationCodeCredentials authorizationCodeCredentials) {
                 /* The tokens were retrieved successfully! */
                System.out.println("Successfully retrieved an access token! " + authorizationCodeCredentials.getAccessToken());
                System.out.println("The access token expires in " + authorizationCodeCredentials.getExpiresIn() + " seconds");
                System.out.println("Luckily, I can refresh it using this refresh token! " + authorizationCodeCredentials.getRefreshToken());


                /* Set the access token and refresh token so that they are used whenever needed */
                refreshToken = authorizationCodeCredentials.getRefreshToken();
                accessToken = authorizationCodeCredentials.getAccessToken();
                api.setAccessToken(authorizationCodeCredentials.getAccessToken());
                api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
                successOrFailureForAuthentication = "success";

            }

            @Override
            public void onFailure(Throwable throwable) {
            /* Let's say that the client id is invalid, or the code has been used more than once,
             * the request will fail. Why it fails is written in the throwable's message. */

                //throwable.printStackTrace();
                successOrFailureForAuthentication = "failure";
             }
        });

        if (successOrFailureForAuthentication.equals("success")) {
            return true;
        }
        return false;

    }

    public static void getAuthenticationTokenFromRefreshToken() {

        /* Application details necessary to get an access token */
        //example URL:  https://www.google.com/?code=AQCVmqZGVahbN4OyTm0znRgSTu3aq6V0yLABaqhX8joTnlknFUpLY2AKjsOUoUnMHLeDUZyjkH6-VYPa9_NEDtlvh4eSsxHxOGAFYvgkHo0BGZJdkV1ZryNDFqYk-b7kQD7fNujT1o-yYN3Uf6jzyj5qfLxJH5JtfUb_3yFfa-Cq6T-jSzX4r_tdSYOi1JTNM9FEVAzZI8i4WUPIy5XZkhb2y1uLWC16vRCQepOPvncLpEBFan1FNrjvHHxdzkwhoYOBumHjx48abIwjI92zhj1PXwdwWw&state=metaMusic&gws_rd=ssl
        String code = refreshToken;


        /* Make a token request. Asynchronous requests are made with the .getAsync method and synchronous requests
         * are made with the .get method. This holds for all type of requests. */
        RefreshAccessTokenRequest.Builder builder = api.refreshAccessToken();
        builder.refreshToken(refreshToken);
        RefreshAccessTokenRequest requester = builder.build();
        try {
            RefreshAccessTokenCredentials refreshAccessTokenCredentials =  requester.get();
            refreshAccessTokenCredentials.getAccessToken();
            refreshAccessTokenCredentials.getExpiresIn();
            api.setAccessToken(refreshAccessTokenCredentials.getAccessToken());
            accessToken = refreshAccessTokenCredentials.getAccessToken();
            System.out.println(accessToken);
            System.out.println("Refresh worked!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getUserName() {
        CurrentUserRequest request = api.getMe().build();

        try {
            User user = request.get();
            userID = user.getId();
            return userID;
        } catch (Exception e) {
            System.out.println("Something went wrong!" + e.getMessage());
        }
        return "Something Went Wrong";
    }

    public static LinkedList<MetaMusicSong> getUsersSavedTracks() {

        LinkedList<MetaMusicSong> metaMusicSongList = new LinkedList<MetaMusicSong>();

        GetMySavedTracksRequest request = api.getMySavedTracks()
                //.limit(5)
                //.offset(0)
                .build();

        try {
            Page<LibraryTrack> libraryTracks = request.get();
            List<LibraryTrack> libraryTracksList = libraryTracks.getItems();
            for (LibraryTrack libTrack : libraryTracksList) {

                String trackID;                                     //declare all variables of info from track to get
                String strAlbumName;
                LinkedList<String> strListArtists;
                String trackName;

                Track track = libTrack.getTrack();                  //get the track ID
                trackID = track.getId();
                SimpleAlbum album = track.getAlbum();                //get the Album name
                strAlbumName = album.getName();
                List <SimpleArtist> artistList =  track.getArtists();                //get all artists' names
                strListArtists = new LinkedList<String>();
                for (SimpleArtist simpleArtist : artistList) {
                    strListArtists.add(simpleArtist.getName());
                }
                trackName = track.getName();                        //get track name
                MetaMusicSong metaMusicSong = new MetaMusicSong(trackID, strAlbumName, strListArtists, trackName);
                metaMusicSongList.add(metaMusicSong);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong!" + e.getMessage());
        }
        return metaMusicSongList;
    }

}





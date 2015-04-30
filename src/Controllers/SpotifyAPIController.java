package Controllers;

import Classes.MetaMusicSong;
import Models.DatabaseModel;
import Models.SpotifyAPIModel;

import javax.xml.crypto.Data;
import java.awt.image.DataBuffer;
import java.util.LinkedList;

/**
 * Created by MatthewRowe on 4/17/15.
 * Handle's view requests for Spotify Data (SpotifyAPIModel)
 */
public class SpotifyAPIController {



    public static String getSpotifyUserName() {
        return new String(SpotifyAPIModel.getUserName());
    }

    public static LinkedList<MetaMusicSong> getUsersSavedTracks() {
        //put users saved tracks in the database
        DatabaseModel.initializeDatabase();
        DatabaseModel.createTableAuthorized();
        try {
            DatabaseModel.receiveMetaMusicSongObjectLinkedListIntoDatabase(SpotifyAPIModel.getUsersSavedTracks());
        } catch (Exception e) {
            System.out.println("Couldn't get Spotify Saved Tracks and insert them into the database");
        }
        //get those tracks from the database
        return (LinkedList<MetaMusicSong>) DatabaseModel.selectAllMetaMusicSongObjectsFromDatabase(true).clone();
    }

    public static LinkedList<MetaMusicSong> getGlobalSavedTracks() {
        DatabaseModel.initializeDatabase();
        DatabaseModel.createTableGlobal();
        return (LinkedList<MetaMusicSong>) DatabaseModel.selectAllMetaMusicSongObjectsFromDatabase(false).clone();
    }


}
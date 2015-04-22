package Controllers;

import Classes.MetaMusicSong;
import Models.DatabaseModel;
import Models.SpotifyAPIModel;
import java.util.LinkedList;

/**
 * Created by MatthewRowe on 4/17/15.
 */
public class SpotifyAPIController {



    public static String getSpotifyUserName() {
        return SpotifyAPIModel.getUserName();
    }

    public static LinkedList<MetaMusicSong> getUsersSavedTracks() {
        //put users saved tracks in the database
        DatabaseModel.initializeDatabase();
        try {
            DatabaseModel.insertMetaMusicSongObjectLinkedListIntoDatabaseTableMetaMusicSong(SpotifyAPIModel.getUsersSavedTracks());
        } catch (Exception e) {
            System.out.println("Couldn't get Spotify Saved Tracks and insert them into the database");
        }
        //get those tracks from the database
        return DatabaseModel.selectAllMetaMusicSongObjectsFromDatabase();
    }
}
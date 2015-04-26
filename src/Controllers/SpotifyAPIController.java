package Controllers;

import Classes.MetaMusicSong;
import Models.DatabaseModel;
import Models.SpotifyAPIModel;

import javax.xml.crypto.Data;
import java.awt.image.DataBuffer;
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
        DatabaseModel.createTableAuthorized();
        try {
            DatabaseModel.receiveMetaMusicSongObjectLinkedListIntoDatabase(SpotifyAPIModel.getUsersSavedTracks());
        } catch (Exception e) {
            System.out.println("Couldn't get Spotify Saved Tracks and insert them into the database");
        }
        //get those tracks from the database
        return DatabaseModel.selectAllMetaMusicSongObjectsFromDatabase(true);
    }

    public static LinkedList<MetaMusicSong> getGlobalSavedTracks() {
        DatabaseModel.initializeDatabase();
        DatabaseModel.createTableGlobal();
        return DatabaseModel.selectAllMetaMusicSongObjectsFromDatabase(false);
    }


}
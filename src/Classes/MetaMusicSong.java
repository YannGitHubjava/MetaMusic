package Classes;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by MatthewRowe on 4/21/15.
 */
public class MetaMusicSong  implements Serializable{

    private String trackID;                                     //declare all variables of info from track to get
    private String strAlbumName;
    private LinkedList<String> strListArtists;
    private String trackName;

    public MetaMusicSong(String trackID, String strAlbumName, LinkedList<String> strListArtists, String trackName) {
        this.trackID = trackID;
        this.strAlbumName = strAlbumName;
        this.strListArtists = strListArtists;
        this.trackName = trackName;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getTrackID() {
        return trackID;
    }

    public String getStrAlbumName() {
        return strAlbumName;
    }

    public LinkedList<String> getStrListArtists() {
        return strListArtists;
    }

    @Override
    public String toString() {
        String artists = "";
        for (String artist : strListArtists) {
            artists += artist + " ";
        }
        return trackName + " " + strAlbumName + " " + artists;
    }
}

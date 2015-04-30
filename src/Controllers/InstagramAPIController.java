package Controllers;

import Models.InstagramAPIModel;

import java.util.LinkedList;

/**
 * Created by MatthewRowe on 4/26/15.
 * In charge of handling view requests to the InstagramAPIModel
 */
public class InstagramAPIController {

    public static LinkedList<String> getNurlsForArtist (int n, String artistName) {
        LinkedList<String> stringLinkedList = InstagramAPIModel.getNimageURLsFromArtist(n, artistName);
        return (LinkedList<String>) stringLinkedList.clone();   //need to implement this in all other methods...
    }
}

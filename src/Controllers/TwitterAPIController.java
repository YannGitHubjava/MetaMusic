package Controllers;

import Models.TwitterAPIModel;
import twitter4j.Status;
import twitter4j.User;

import java.util.LinkedList;

/**
 * Created by MatthewRowe on 4/21/15.
 */
public class TwitterAPIController {

    public static void loginToTwitter() {
        TwitterAPIModel.getKeysAndSecrets();
        TwitterAPIModel.loginToTwitter();
    }


    public static LinkedList<String> getArtistsTweetsAsStrings(String artistName) {
        LinkedList<Status> llstatus = TwitterAPIModel.getNmostRecentTweetsFromArtistName(10, artistName);
        LinkedList<String> llstring = new LinkedList<String>();
        for (Status status : llstatus) {
            String str = status.getCreatedAt() + ": " + status.getText() + " RT: " + status.getRetweetCount() + " FAV: " + status.getFavoriteCount();
            llstring.add(str);
        }
        return llstring;
    }

    public static String getArtistTwitterHandle(String artistName) {
        User u = TwitterAPIModel.findAnArtistsTwitterUserAccount(artistName);
        return  u.getScreenName();
    }

}

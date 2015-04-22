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



    public static LinkedList<String> getArtistTwitterHandleAndTweets(String artistName) {
        return TwitterAPIModel.getTwitterHandleAndTweets(artistName);
    }

}

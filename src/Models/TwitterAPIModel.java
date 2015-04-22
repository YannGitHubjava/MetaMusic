package Models;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by MatthewRowe on 4/21/15.
 */
//http://stackoverflow.com/questions/27882162/i-cant-seem-to-get-the-first-twitter4j-code-example-simple-post-on-twitter-to
public class TwitterAPIModel {


    private static Twitter twitter;
    private static String twitterAPIConsumerKey;
    private static String twitterAPIConsumerSecret;
    private static String twitterAPITokenKey;
    private static String twitterAPITokenSecret;


    public static void getKeysAndSecrets() {
        try {
            FileReader reader = new FileReader("../javaFinalProjectKeysAndSecrets/twitterKeys.txt");
            BufferedReader bufReader = new BufferedReader(reader);
            String line = bufReader.readLine();
            String[] lineArray = line.split(",");
            twitterAPIConsumerKey = lineArray[0];
            line = bufReader.readLine();
            lineArray = line.split(",");
            twitterAPIConsumerSecret = lineArray[0];
            line = bufReader.readLine();
            lineArray = line.split(",");
            twitterAPITokenKey = lineArray[0];
            line = bufReader.readLine();
            lineArray = line.split(",");
            twitterAPITokenSecret = lineArray[0];
            reader.close();
        } catch  (Exception e) {
            System.out.println("Problem reading files");
        }

    }

    public static void loginToTwitter () {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitterAPIConsumerKey)
                .setOAuthConsumerSecret(twitterAPIConsumerSecret)
                .setOAuthAccessToken(twitterAPITokenKey)
                .setOAuthAccessTokenSecret(twitterAPITokenSecret);

        twitter = new TwitterFactory(cb.build()).getInstance();
    }

    public static LinkedList<Status> getNmostRecentTweetsFromArtistName(int numTweets, String artistName ) {
        return getNTweetsFromSpecifiedUser(numTweets, findAnArtistsTwitterUserAccount(artistName));
    }


    //Need to figure out how to be sure I'm finding the official twitter for a given user... e.g. katyperry
    //... do a query... for a user and use :verified tag...?
    public static User findAnArtistsTwitterUserAccount(String artistName) {
        //http://stackoverflow.com/questions/15319971/how-to-use-twitter4j-to-show-tweets
        User u = null;
        try {
            artistName = artistName.replace(" ", "");
            String[] user = new String[1];
            user[0] = artistName;
            ResponseList<User> rl =  twitter.lookupUsers(user);
            u = rl.get(0);
            System.out.println(u.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }

    private static LinkedList<Status> getNTweetsFromSpecifiedUser(int n, User u) {
        LinkedList<Status> tweetsForUser = new LinkedList<Status>();
        try {
            //First param of Paging() is the page number, second is the number per page (this is capped around 200 I think.
            //http://stackoverflow.com/questions/2943161/get-tweets-of-a-public-twitter-profile
            Paging paging = new Paging(1, n);
            List<Status> statuses = twitter.getUserTimeline(u.getScreenName(),paging);
            for (Status s : statuses) {
                tweetsForUser.add(s);
            }

        } catch (Exception e) {
            System.out.println("error");
        }

        return tweetsForUser;

    }

}

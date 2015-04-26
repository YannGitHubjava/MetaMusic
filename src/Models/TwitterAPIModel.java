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

/*    public static LinkedList<Status> getNmostRecentTweetsFromArtistName(int numTweets, String artistName ) {
        return getNTweetsFromSpecifiedUser(numTweets, findAnArtistsTwitterUserAccount(artistName));
    }*/





    /*
    LinkedList<String> handleAndTweets = new LinkedList<String>();  //linkedList to pass
        User u = TwitterAPIModel.findAnArtistsTwitterUserAccount(artistName);  //user found
        LinkedList<Status> tweets = TwitterAPIModel.getNmostRecentTweetsFromArtistName(10, artistName);   //tweets list

        } else {
            handleAndTweets.add("user not found");
        }
     */

    //http://twitter4j.org/javadoc/twitter4j/api/UsersResources.html#searchUsers%28java.lang.String,%20int%29

    public static User findAnArtistsTwitterUserAccount(String artistName) {
        //http://stackoverflow.com/questions/15319971/how-to-use-twitter4j-to-show-tweets
        User u = null;
        try {

            /*
            //try to just do a search on the User first
            String artistNameNoSpaces = artistName.replace(" ", "");
            String[] user = new String[1];
            user[0] = artistNameNoSpaces;
            ResponseList<User> rl =  twitter.lookupUsers(user);
            if (rl.get(0).isVerified()) {
                u = rl.get(0);
            } else */{
                //that didn't work... so try this now:
                //Query query = new Query(artistName);
                ResponseList<User> responseListUser = twitter.searchUsers(artistName, 1);
                for (User user1 : responseListUser) {
                    if (user1.isVerified()) {
                        u = user1;
                        break;
                    }
                }
            }
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

    //http://stackoverflow.com/questions/15502357/search-people-in-twitter-in-java-twitter4j
    //http://twitter4j.org/en/code-examples.html
    public static LinkedList<String> getTwitterHandleAndTweets(String artistName) {
        LinkedList<String> handleAndTweets = new LinkedList<String>();  //strings to pass to the controller and then to the view
        User u = findAnArtistsTwitterUserAccount(artistName);
        if (u != null) {
            handleAndTweets.add("Twitter Handle: " + u.getScreenName() + "\n"); //add handle to list to pass
            LinkedList<Status> tweets = getNTweetsFromSpecifiedUser(10, u);
            for (Status s : tweets) {
                String strToAdd = s.getCreatedAt() + " <br> " + s.getText() + " <br> RT[" + s.getRetweetCount() + "] + Fav[" + s.getFavoriteCount() + "] \n";
                handleAndTweets.add(strToAdd);
            }

        } else {
            handleAndTweets.add("Twitter Handle not found");
        }

        return handleAndTweets;
    }
}

package Controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import Models.SpotifyAPIModel;

/**
 * Created by MatthewRowe on 4/16/15.
 * In charge of keeping track of when to refresh the authentication token for Spotify's api
 */
public class ClockController extends TimerTask {

    Date now;


    public ClockController() {
    }

    @Override
    public void run() {
        //the access token is good for 3600 seconds (or one hour)... so refresh every half hour should be fine.
        //http://stackoverflow.com/questions/8150155/java-gethours-getminutes-and-getseconds
        now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        if (minutes % 25 == 0) {
            if (seconds % 30 == 0){
                SpotifyAPIModel.getAuthenticationTokenFromRefreshToken();
            }
        }
    }
}

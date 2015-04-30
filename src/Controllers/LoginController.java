package Controllers;

import Models.SpotifyAPIModel;
import Views.LoginView;
import Views.MetaMusicView;
import Views.MetaMusicView;

import java.util.Timer;



/**
 * Created by matt.rowe on 4/15/2015.
 * Controller that handle's the login view's request for authentication into the SpofityAPI, or lack of authentication if user isn't using it
 */
public class LoginController {

    private static LoginView loginView;
    private static ClockController clockController;

    public static void main(String[] args) {
        setup();

        //get authentication token
        //spotifyModel.getAuthenticationTokenFromURL("https://www.google.com/?code=AQApk53oDSt7Ie5pxnbQIWUNm76Qj1ksNDP9-fWt9gFpEC612GieC3lhbhMRddKMQliwdfuQdzr40yL0j6wmmsI0kTr4-Asy9mlbzZWVlPO8wFY5WaznX3LUNB15p4Ymh8Tdwtf4RHz_QznsL2x2uEZq9oAVdyrvd62Btcs0CaDUqkc8S1biIZ_MoI8c3pmMrslkumRawyn1pZdXgP95ExIJEdSXEpRiqcIVsQPAtdJGhgwhigjl0Tyc5ftLzUaKozHoX3if-6NfosJkyy9N2j-qlYHj8w&state=metaMusic&gws_rd=ssl");
    }

    public static void setup() {
        loginView = new LoginView();

    }

    public static void setupClockController() {
        //setup the timer that will be used to send a refresh token and get a new access token if needed (every 30 minutes on x:25 and x:50)
        clockController = new ClockController();
        Timer timer = new Timer();
        //timer ticks every minute
        timer.schedule(clockController, 0, 100);
    }


    public static String getAuthenticationURL() {
        //because the SpotifyAPIModel is static, need to set the keys before building the API
        SpotifyAPIModel.getKeyandSecret();
        SpotifyAPIModel.buildAPI();
        return new String (SpotifyAPIModel.getAuthenticationURL());
    }

    public static boolean authenticateTokenFromURL (String authenticatedURL) {
        return SpotifyAPIModel.getAuthenticationTokenFromURL(authenticatedURL);
    }

    public static void destroyLoginFrameAndLoadMetaMusicFrameUSER() {
        loginView.dispose();
        new MetaMusicView(true);
        setupClockController();
    }

    public static void destroyLoginFrameAndLoadMetaMusicFrameUNAUTH() {
        loginView.dispose();
        new MetaMusicView(false);
    }





}

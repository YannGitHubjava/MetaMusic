package Views;

import Classes.MetaMusicSong;
import Controllers.SpotifyAPIController;
import Controllers.TwitterAPIController;
import Models.DatabaseModel;
import twitter4j.Twitter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Created by MatthewRowe on 4/16/15.
 */
public class MetaMusicView extends  JFrame{

    private JPanel rootPanel;
    private JLabel lblSpotifyUserName;
    private JComboBox cboSavedTracks;
    private JButton btnExit;
    private JButton btnExecute;
    private JList lstTweets;
    private JScrollPane paneScroll;
    LinkedList<MetaMusicSong> metaMusicSongList;

    public MetaMusicView(){
        super("MetaMusic");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        lblSpotifyUserName.setText(SpotifyAPIController.getSpotifyUserName());
        pack();

        fillComboBoxWithUsersSavedSongList();
        pack();


        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseModel.disconnectFromDatabase();
                System.exit(0);
            }
        });

        btnExecute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TwitterAPIController.loginToTwitter();
                MetaMusicSong mms1 = (MetaMusicSong) cboSavedTracks.getSelectedItem();                //get MetaMusicSong object that's selected
                String artistName = mms1.getStrListArtists().get(0);                 //currently only going to show the first artist... add more functionality later
                String artistTwitterHandle = TwitterAPIController.getArtistTwitterHandle(artistName);
                LinkedList<String> tweets = TwitterAPIController.getArtistsTweetsAsStrings(artistName);
                //http://www.seasite.niu.edu/cs580java/JList_Basics.htm
                String[] toDisplay = new String[tweets.size() + 1];
                toDisplay[0] = artistTwitterHandle;
                for (int ix = 1; ix < tweets.size() + 1; ix++) {    //+1 for the artistTwitterHandle
                    toDisplay[ix] = tweets.get(ix - 1);
                }
                lstTweets.setCellRenderer(new MyCellRenderer(350));
                lstTweets.setListData(toDisplay);
                pack();
            }
        });
    }


    private void fillComboBoxWithUsersSavedSongList() {
        metaMusicSongList = SpotifyAPIController.getUsersSavedTracks();
        for (MetaMusicSong mmSong: metaMusicSongList) {
            cboSavedTracks.addItem(mmSong);
        }

    }
}

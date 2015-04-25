package Views;

import Classes.MetaMusicSong;
import Controllers.SpotifyAPIController;
import Controllers.TwitterAPIController;
import Models.DatabaseModel;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Created by MatthewRowe on 4/16/15.
 */
public class MetaMusicView extends  JFrame{

    protected JPanel rootPanel;
    protected JLabel lblSpotifyUserName;
    protected JComboBox cboSavedTracks;
    protected JButton btnExit;
    protected JButton btnExecute;
    protected JScrollPane scrollPaneTwitter;
    protected JTextPane txtpaneTwitter;
    LinkedList<MetaMusicSong> metaMusicSongList;


    public MetaMusicView(){
        super("MetaMusic");
        this.setSize(new Dimension(1500, 500));
        //txtpaneTwitter.setSize(400, 300);
        txtpaneTwitter.setPreferredSize(new Dimension(400, 300));
        //scrollPaneTwitter.setSize(400, 300);
        scrollPaneTwitter.setPreferredSize(new Dimension(400, 300));
        setContentPane(rootPanel);
        //pack();
        lblSpotifyUserName.setSize(200, 50);
        cboSavedTracks.setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        lblSpotifyUserName.setText(SpotifyAPIController.getSpotifyUserName());
        //pack();

        fillComboBoxWithUsersSavedSongList();
        //pack();


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
                LinkedList<String> stringsToDisplay = TwitterAPIController.getArtistTwitterHandleAndTweets(artistName);
                String strToDisplay = "";
                for (String s : stringsToDisplay) {
                    strToDisplay += s + "\n";
                }

                Document doc = new DefaultStyledDocument();
                AttributeSet as = new SimpleAttributeSet();
                try {
                    doc.insertString(0, strToDisplay, as);
                } catch (Exception ex) {

                }
                txtpaneTwitter.setDocument(doc);
                //pack();


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

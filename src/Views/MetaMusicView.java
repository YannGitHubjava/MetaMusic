package Views;

import Classes.MetaMusicSong;
import Controllers.InstagramAPIController;
import Controllers.SpotifyAPIController;
import Controllers.TwitterAPIController;
import Models.DatabaseModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;

/**
 * Created by MatthewRowe on 4/16/15.
 * View for MetaMusic frame
 */
public class MetaMusicView extends  JFrame{

    protected JPanel rootPanel;
    protected JLabel lblSpotifyUserName;
    protected JComboBox cboSavedTracks;
    protected JButton btnExit;
    protected JButton btnExecute;
    private JScrollPane scrollPane;
    private JPanel innerInstagramPanel;
    private JScrollPane scrollpaneTwitter;
    private JEditorPane textpaneTwitter;
    LinkedList<MetaMusicSong> metaMusicSongList;


    public MetaMusicView(boolean isUserAuthenticated){
        super("MetaMusic");
        //txtpaneTwitter.setSize(400, 300);
        //textpaneTwitter.setPreferredSize(new Dimension(400, 300));
        //textpaneTwitter.setMaximumSize(new Dimension(400, 300));
        //scrollPaneTwitter.setSize(400, 300);
        //scrollpaneTwitter.setPreferredSize(new Dimension(400, 300));
        //scrollpaneTwitter.setMaximumSize(new Dimension(400, 300));
        //scrollPane.setSize(new Dimension(400, 300));
        //scrollpaneTwitter.setSize(new Dimension(400, 300));
        setContentPane(rootPanel);
        //pack();
        //lblSpotifyUserName.setSize(200, 50);
        //cboSavedTracks.setSize(400, 300);

        //set sizes
        this.setSize(new Dimension(1500, 500));
        cboSavedTracks.setPreferredSize(new Dimension(200, 50));
        scrollpaneTwitter.setPreferredSize(new Dimension(600, 300));
        //textpaneTwitter.setPreferredSize((new Dimension(600, 300)));
        scrollpaneTwitter.setPreferredSize(new Dimension(600, 300));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        if (isUserAuthenticated) {
            lblSpotifyUserName.setText(SpotifyAPIController.getSpotifyUserName());
            fillComboBoxWithUsersSavedSongList();
        }  else {
            lblSpotifyUserName.setText("MetaMusic User");
            fillComboBoxWithGlobalSavedSongList();
        }

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

                //TWITTER
                TwitterAPIController.loginToTwitter();
                MetaMusicSong mms1 = (MetaMusicSong) cboSavedTracks.getSelectedItem();                //get MetaMusicSong object that's selected
                String artistName = mms1.getStrListArtists().get(0);                 //currently only going to show the first artist... add more functionality later
                LinkedList<String> stringsToDisplay = TwitterAPIController.getArtistTwitterHandleAndTweets(artistName);
                String strToDisplay = "";
                for (String s : stringsToDisplay) {
                    String stringToAdd = addHyperlinks(s);
                    strToDisplay += "<p>" + stringToAdd + "</p>";
                }
                strToDisplay += "<a href=\\\"http://www.google.com/finance?q=NYSE:C\\\">C</a>, <a href=\\\"http://www.google.com/finance?q=NASDAQ:MSFT\\\">MSFT</a>";

//                Document doc = new DefaultStyledDocument();
//                AttributeSet as = new SimpleAttributeSet();
//                try {
//                    doc.insertString(0, strToDisplay, as);
//                } catch (Exception ex) {
//
//                }
                //textpaneTwitter.setDocument(doc);
                //http://stackoverflow.com/questions/3693543/hyperlink-in-jeditorpane
                textpaneTwitter.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
                textpaneTwitter.setEditable(false);
                textpaneTwitter.addHyperlinkListener(new HyperlinkListener() {
                    public void hyperlinkUpdate(HyperlinkEvent e) {
                        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            // Do something with e.getURL() here
                            HyperlinkEvent.EventType et = e.getEventType();
                            try {
                                if(Desktop.isDesktopSupported()) {
                                    URI uri = new URI(e.getURL().toString());
                                    Desktop.getDesktop().browse(uri);
                                }
                            } catch (Exception ex) {
                                System.out.println("loading url from twitter click didn't work");
                            }

                        }
                    }
                });
                textpaneTwitter.setText(strToDisplay);

                //INSTAGRAM

                innerInstagramPanel.removeAll();
                LinkedList<String> instagramImageURLs = InstagramAPIController.getNurlsForArtist(10, artistName);
                scrollPane.setPreferredSize(new Dimension(640, 600));
                scrollPane.setMinimumSize(new Dimension(640, 600));
                innerInstagramPanel.setLayout(new GridLayout(instagramImageURLs.size(), 1));
                for (String imageURL : instagramImageURLs) {
                    try {
                        URL url = new URL(imageURL);
                        BufferedImage image = ImageIO.read(url);
                        ImageIcon icon = new ImageIcon(image);
                        JLabel lblLabel = new JLabel();
                        lblLabel.setIcon(icon);
                        lblLabel.setMinimumSize(new Dimension(640, 600));
                        //dimensions 640 x 600
                        lblLabel.setText("");
                        innerInstagramPanel.add(lblLabel);
                        scrollPane.revalidate();
                        pack();

                    } catch (Exception ex) {
                        System.out.println("algo paso instagram image loading in the meta music view");
                    }

                }


            }
        });

    }

                    private void fillComboBoxWithGlobalSavedSongList() {
                        metaMusicSongList = SpotifyAPIController.getGlobalSavedTracks();
                        for (MetaMusicSong mmSong: metaMusicSongList) {
                            cboSavedTracks.addItem(mmSong);
                        }
                    }


                    private void fillComboBoxWithUsersSavedSongList() {
                        metaMusicSongList = SpotifyAPIController.getUsersSavedTracks();
                        for (MetaMusicSong mmSong: metaMusicSongList) {
                            cboSavedTracks.addItem(mmSong);
                        }
                    }

                    private String addHyperlinks(String s) {
                        String[] list = s.split(" ");
                        String newURLString = "";
                        int indexOfChange = 0;
                        boolean didReplace = false;
                        String changedString = "";
                        for (int ix = 0; ix < list.length; ix++) {
                            if (list[ix].startsWith("http://") || list[ix].startsWith("https://")) {
                                indexOfChange = ix;
                                String url = list[ix];
                                //strToDisplay += "<a href=\\\"http://www.google.com/finance?q=NYSE:C\\\">C</a>, <a href=\\\"http://www.google.com/finance?q=NASDAQ:MSFT\\\">MSFT</a>";
                                //"<a href=\"http://www.google.com/finance?q=NYSE:C\">C</a>
                                newURLString = "<a href=\"" + url + "\">" + url + "</a>";
                                didReplace = true;
                            }
                        }

                        if (didReplace) {
                            for (int ix = 0; ix < list.length; ix++) {
                                if (ix == indexOfChange) {
                                    changedString += newURLString + " ";
                                } else {
                                    changedString += list[ix] + " ";
                                }
                            }
                            return changedString;
                        }

                        return s;

                    }
}

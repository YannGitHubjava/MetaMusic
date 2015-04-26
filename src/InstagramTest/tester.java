package InstagramTest;

import Models.InstagramAPIModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Created by MatthewRowe on 4/26/15.
 */

//http://stackoverflow.com/questions/18873315/javafx-webview-loading-page-in-background

public class tester extends JFrame{

    private JPanel rootPanel;
    private JLabel lblLabel;
    private JScrollPane paneScrollPane;
    private JPanel innerPanel;
    private JLabel lblLabel2;

    public tester() {

        super("MetaMusic");
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();

        try {
            paneScrollPane.setPreferredSize(new Dimension(680, 600));
            paneScrollPane.setMinimumSize(new Dimension(680,600));
            //scrollPaneTwitter.setSize(400, 300);
            //innerPanel.setPreferredSize(new Dimension(640, 600));
            innerPanel.setLayout(new GridLayout(2, 1));


            String path = InstagramAPIModel.getLinkURL();
            URL url = new URL(path);
            BufferedImage image = ImageIO.read(url);
            ImageIcon icon = new ImageIcon(image);
            lblLabel = new JLabel();
            lblLabel.setIcon(icon);
            lblLabel.setMinimumSize(new Dimension(640, 600));
            //dimensions 640 x 600
            lblLabel.setText("");
            innerPanel.add(lblLabel);

            String path2 = InstagramAPIModel.getSecondLinkURL();
            URL url2 = new URL(path2);
            BufferedImage image2 = ImageIO.read(url2);
            ImageIcon icon2 = new ImageIcon(image2);
            lblLabel2 = new JLabel();
            lblLabel2.setIcon(icon2);
            //dimensions 640 x 600
            lblLabel2.setText("");
            innerPanel.add(lblLabel2);

            pack();
            paneScrollPane.revalidate();


        } catch (Exception e ) {
            System.out.println("error");
        }

    }



}

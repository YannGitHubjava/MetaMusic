package trials;

import javax.swing.*;
import java.awt.*;

/**
 * Created by MatthewRowe on 4/30/15.
 */
public class testView extends JFrame {

    private JPanel rootPanel;

    private void instantiateMainPanel() {
        rootPanel = new JPanel();
    }

    public testView () {
        super("MetaMusic");
        instantiateMainPanel();
        setContentPane(rootPanel);
        this.setSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        twitterScrollPane t = new twitterScrollPane("<p>ROAR</p>", 200, 200);
        this.add(t);
        t.revalidate();
    }

    public static void main(String[] args) {
        new testView();
    }
}

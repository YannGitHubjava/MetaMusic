package trials;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.net.URI;

/**
 * Created by MatthewRowe on 4/30/15.
 *
 * could make an Interface that has methods like, "addComponent" and "customizeScrollPane"
 * ... would every one of these classes extend JScrollPane or just be a generic Object that returns a JScrollPane...?
 * ... which makes more sense?
 */
public class twitterScrollPane extends JScrollPane{

    JEditorPane editorPane;

    public twitterScrollPane (String strToDisplay, int width, int height) {
        addEditorPane(width, height);
        editorPane.setText(strToDisplay);
        this.setSize(new Dimension(width, height));
    }

    private void addEditorPane (int width, int height) {
        editorPane = new JEditorPane();
        editorPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    // Do something with e.getURL() here
                    HyperlinkEvent.EventType et = e.getEventType();
                    try {
                        if (Desktop.isDesktopSupported()) {
                            URI uri = new URI(e.getURL().toString());
                            Desktop.getDesktop().browse(uri);
                        }
                    } catch (Exception ex) {
                        System.out.println("loading url from twitter click didn't work");
                    }

                }
            }
        });
        editorPane.setSize(new Dimension(width, height));
        this.add(editorPane);
    }

}

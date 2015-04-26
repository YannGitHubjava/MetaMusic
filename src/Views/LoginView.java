package Views;

import Controllers.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

/**
 * Created by MatthewRowe on 4/16/15.
 */
public class LoginView extends JFrame {
    private JPanel rootPanel;
    private JButton btnAuthenticate;
    private JTextField txtAuthenticatedLink;
    private JButton btnLogin;
    private JButton btnLoginWithoutAuthenticating;

    public LoginView () {
        super("Login");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);


        //http://stackoverflow.com/questions/527719/how-to-add-hyperlink-in-jlabel
        btnAuthenticate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        URI uri = new URI(LoginController.getAuthenticationURL());
                        Desktop.getDesktop().browse(uri);
                    } catch (Exception ex) {
                        System.out.println("Launch didn't work");
                    }
                } else { /* TODO: error handling */ }
            }
        });


        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = LoginController.authenticateTokenFromURL(txtAuthenticatedLink.getText());
                if (success) {
                    LoginController.destroyLoginFrameAndLoadMetaMusicFrameUSER();
                } else {
                    System.out.println("loginfailed");
                }
            }
        });

        btnLoginWithoutAuthenticating.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginController.destroyLoginFrameAndLoadMetaMusicFrameUNAUTH();
            }
        });
    }


}

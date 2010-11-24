package com.fedroot.demo.dacs.swing;

import fedroot.dacs.client.DacsAuthenticateRequest;
import fedroot.dacs.entities.Credential;
import fedroot.dacs.entities.Credentials;
import fedroot.dacs.entities.CredentialsLoader;
import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.entities.Role;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsCookieName;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Get DACS login details
 */
public class DacsLoginFrame extends JFrame {

    private static final Logger logger = Logger.getLogger(DacsLoginFrame.class.getName());
    private String dacsCookieName;
    private DacsClientContext dacsClientContext;
    private final Federation federation;
    private JComboBox cmbJurisdictions;
    private List<String> jurisdictionNames;
    private TextField tfUsername;
    private TextField tfStatus;
    private JPasswordField pwfPassword;
    /** The OK button */
    final private JButton ok;
    /** The cancel button */
    final private JButton can;

    /**
     * Construct a DacsLoginFrame
     * 
     * @param federation the Federation in which DACS authentication will be done
     * @param dacsClientContext the UserContext under which authentication will be done
     */
    public DacsLoginFrame(final Federation federation, DacsClientContext user) {
        super();
        setTitle("DACS Login to Federation: " + federation.getFederationName());
        this.federation = federation;
        this.dacsClientContext = user;
        jurisdictionNames = new ArrayList<String>();
        for (Jurisdiction jurisdiction : federation.getAuthenticatingJurisdictions()) {
            this.jurisdictionNames.add(jurisdiction.getJName());
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Set up the content pane.
        Container contentPane = this.getContentPane();

        JPanel inputsPane = new JPanel();
        inputsPane.setLayout(new BoxLayout(inputsPane, BoxLayout.PAGE_AXIS));

        // Jurisdictions ComboBox
        cmbJurisdictions = new JComboBox(this.jurisdictionNames.toArray());
        cmbJurisdictions.setAlignmentX(LEFT_ALIGNMENT);

        cmbJurisdictions.setToolTipText("Select a Jurisdiction");
        cmbJurisdictions.setEditable(false);
        cmbJurisdictions.setSelectedIndex(0);

        // Username
        JPanel userPane = new JPanel();
        userPane.setLayout(new BoxLayout(userPane, BoxLayout.LINE_AXIS));
        userPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        userPane.add(Box.createHorizontalGlue());
        userPane.setAlignmentX(LEFT_ALIGNMENT);

        tfUsername = addTextField("Username: ", 20, userPane);
        tfUsername.requestFocus();

        // Password
        JPanel passwordPane = new JPanel();
        passwordPane.setLayout(new BoxLayout(userPane, BoxLayout.LINE_AXIS));
        passwordPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        passwordPane.add(Box.createHorizontalGlue());
        passwordPane.setAlignmentX(LEFT_ALIGNMENT);


        pwfPassword = addPasswordField("Password: ", 10, passwordPane);

        // Controls
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.setAlignmentX(CENTER_ALIGNMENT);

        ok = new JButton("OK");
        can = new JButton("Cancel");
        buttonPane.add(ok);
        buttonPane.add(can);

        // add inputs
        inputsPane.add(cmbJurisdictions);
        inputsPane.add(userPane);
        inputsPane.add(passwordPane);
        inputsPane.add(buttonPane);


        // Status line
        JPanel statusPane = new JPanel(new FlowLayout());
        statusPane.setAlignmentX(LEFT_ALIGNMENT);
        
        tfStatus = addTextField("Status", 80, statusPane);

        ok.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        String jurisdictionName = jurisdictionNames.get(cmbJurisdictions.getSelectedIndex());
                        tfStatus.setText("authenticating in " + jurisdictionName);
                        if (authenticate(federation.getJurisdictionByName(jurisdictionName))) {
                            tfStatus.setText("successfully authenticated in " + jurisdictionName);
                        } else {
                            tfStatus.setText("authentication failed");
                        }
                        ;
                        pwfPassword.setText("");
                    }
                });

        can.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        dispose();
                    }
                });

        contentPane.add(inputsPane, BorderLayout.CENTER);
        contentPane.add(statusPane, BorderLayout.SOUTH);

        pack();
    }

    // handle details of adding a TextField to a container
    private TextField addTextField(String text, int width, Container container) {
        JPanel panInput = new JPanel(new FlowLayout());
        Label label = new Label(text);
        label.setAlignment(Label.LEFT);
        TextField textfield = new TextField(width);
        textfield.setEditable(true);
        panInput.add(label);
        panInput.add(textfield);
        // panInput.setVisible(true);
        container.add(panInput);
        return textfield;
    }

    // handle details of adding a JPasswordField to a container
    private JPasswordField addPasswordField(String text, int width, Container container) {
        JPanel panInput = new JPanel(new FlowLayout());
        Label label = new Label(text);
        label.setAlignment(Label.LEFT);
        JPasswordField pwfield = new JPasswordField(width);
        pwfield.setEditable(true);
        panInput.add(label);
        panInput.add(pwfield);
        panInput.setVisible(true);
        container.add(panInput);
        return pwfield;
    }

    /*
     * extract username/password text fields, do DACS authentication in jurisdiction
     * @return true on success, else false
     * @param jurisdiction DACS jurisdiction in which to authenticate username
     * @deprecated fixme
     */
    /**
     * authentication dacsClientContext in a jurisdiction
     * @param jurisdiction the Jurisdiction in which to authentication
     * @return 
     */
    protected boolean authenticate(Jurisdiction jurisdiction) {
//        tfUsername.selectAll();
//        String username = tfUsername.getSelectedText();
        String username = tfUsername.getText();
        username = (username != null ? username.trim() : username);
        pwfPassword.selectAll();
        String password = pwfPassword.getSelectedText();
        password = (password != null ? password.trim() : password);
        try {
            CredentialsLoader credentialsLoader = new CredentialsLoader(jurisdiction, username, password, dacsClientContext);
            Credentials credentials = credentialsLoader.getCredentials();
            if (credentials.hasCredentials()) {
                logger.log(Level.FINE, "User {0} login succeeded.", username);
                // save DACS cookie name so we can nuke the cookie for fast logout
                dacsCookieName = DacsCookieName.getName(federation.getFederationName(), jurisdiction.getJName(), username);
                return true;
            } else { // shouldn't get here - a DacsException is thrown when dacs_authenticate fails
                logger.log(Level.FINE, "User {0} login failed.", username);
                return false;
            }
        } catch (DacsException ex) {
            logger.log(Level.FINE, "User login exception: {0}.", ex.getMessage());
            return false;
        }

    }
}

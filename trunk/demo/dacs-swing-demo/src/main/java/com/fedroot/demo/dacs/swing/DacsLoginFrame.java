package com.fedroot.demo.dacs.swing;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.DacsUserAccount;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import com.fedroot.dacs.UserContext;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import org.apache.commons.httpclient.HttpStatus;

/**
 * Get DACS login details
 */
public class DacsLoginFrame extends JFrame {
    protected UserContext user;
    protected final Federation federation;
    private JComboBox cmbJurisdictions;
    private List<String> jurisdiction_names;
    protected TextField tfUsername, tfStatus;
    protected JPasswordField pwfPassword;
    /** The OK button */
    final protected JButton ok;
    /** The cancel button */
    final protected JButton can;
    
    /**
     * Construct a DacsLoginFrame
     * 
     * @param federation the Federation in which DACS authentication will be done
     * @param user the UserContext under which authentication will be done
     */
    public DacsLoginFrame(final Federation federation, UserContext user) {
        super();
        setTitle("DACS Login to Federation: " + federation.getName());
        this.federation = federation;
        this.user = user;
        this.jurisdiction_names = federation.getAuthenticatingJurisdictionNames();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //Set up the content pane.
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
                
        // Jurisdictions combobox
        cmbJurisdictions = new JComboBox(this.jurisdiction_names.toArray());
        cmbJurisdictions.setToolTipText("Select a Jurisdiction");
        cmbJurisdictions.setEditable(false);
        cmbJurisdictions.setSelectedIndex(0);
        
        JPanel textpane = new JPanel(new FlowLayout());
        textpane.add(cmbJurisdictions);
        tfUsername = addTextField("Username", 20, textpane);
        tfUsername.requestFocus();
        pwfPassword = addPasswordField("Password", 10, textpane);

        
        JPanel buttonpane = new JPanel(new FlowLayout());
        
        ok = new JButton("OK");
        can = new JButton("Cancel");
        buttonpane.add(ok);
        buttonpane.add(can);
        
        JPanel statuspane = new JPanel(new FlowLayout());
        tfStatus = addTextField("Status",  80, statuspane);
        
        ok.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String jur_name = jurisdiction_names.get(cmbJurisdictions.getSelectedIndex());
                tfStatus.setText("authenticating in " + jur_name);
                if (authenticate(federation.getJurisdictionByName(jur_name))) {
                    tfStatus.setText("successfully authenticated in " + jur_name);
                } else {
                    tfStatus.setText("authentication failed");
                };
                pwfPassword.setText("");
            }
        });
        
        can.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        
        JPanel inputpane = new JPanel(new BorderLayout());
        inputpane.add(textpane, BorderLayout.CENTER);
        inputpane.add(buttonpane, BorderLayout.SOUTH);
        
        pane.add(inputpane, BorderLayout.CENTER);
        pane.add(statuspane, BorderLayout.SOUTH);
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
        pwfield .setEditable(true);
        panInput.add(label);
        panInput.add(pwfield );
        panInput.setVisible(true);
        container.add(panInput);
        return pwfield ;
    }
    
    /*
     * extract username/password text fields, do DACS authentication in jurisdictionand on
     * @return true on success, else false
     * @param jurisdiction DACS jurisdiction in which to authenticate username
     * @deprecated fixme
     */
    /**
     * authentication user in a jurisdiction
     * @param jurisdiction the Jurisdiction in which to authentication
     * @return 
     */
    protected boolean authenticate(Jurisdiction jurisdiction) {
        tfUsername.selectAll();
        String username = tfUsername.getSelectedText();
        pwfPassword.selectAll();
        String password = pwfPassword.getSelectedText();
        DacsUserAccount account = new DacsUserAccount(this.federation, jurisdiction, username);
        try {
            return this.user.authenticate(account, password);     
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

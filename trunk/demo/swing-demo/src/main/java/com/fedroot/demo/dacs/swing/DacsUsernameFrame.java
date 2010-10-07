package com.fedroot.demo.dacs.swing;

import fedroot.dacs.http.DacsClientContext;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

/**
 * Get DACS signout details
 */
public class DacsUsernameFrame extends JFrame {

    private DacsClientContext dacsClientContext;
    private JComboBox cmbDacsUsernames;
    private List<String> dacs_usernames;
    protected TextField tfStatus;
    /** The OK button */
    final protected JButton remove;
    /** The cancel button */
    final protected JButton can;
    
    /**
     * Construct a DacsLoginFrame
     * 
     * @param dacsCientContext the UserContext under which DACS authentication will be done
     */
    public DacsUsernameFrame(DacsClientContext dacsCientContext) {
        super();
        this.dacsClientContext = dacsCientContext;
//        setTitle("DACS Credentials: " + dacsCientContext.getName());
//        dacs_usernames = dacsCientContext.getDacsUsernames();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //Set up the content pane.
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());

        JPanel textpane = new JPanel(new FlowLayout());
        
        if (dacs_usernames.size() > 0) {
            // DACS Usernames combobox
            cmbDacsUsernames = new JComboBox(dacs_usernames.toArray());
            cmbDacsUsernames.setToolTipText("DACS Username Credentials");
            cmbDacsUsernames.setEditable(false);
            cmbDacsUsernames.setSelectedIndex(0);
            
            textpane.add(cmbDacsUsernames);
        }

        JPanel buttonpane = new JPanel(new FlowLayout());
        
        remove = new JButton("Remove");
        can = new JButton("Cancel");
        buttonpane.add(remove);
        buttonpane.add(can);
        
        JPanel statuspane = new JPanel(new FlowLayout());
        tfStatus = addTextField("Status",  80, statuspane);
        
        remove.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (dacs_usernames.size() > 0) {
                    String dacs_username = dacs_usernames.get(cmbDacsUsernames.getSelectedIndex());
                    signout(dacs_username);
                    tfStatus.setText("signed out " + dacs_username);
                }
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
    
    /**
     * signout a DACS username
     * @param dacs_username the DACS username to signout
     */
    protected void signout(String dacs_username) {
//        this.dacsClientContext.signout(dacs_username);
        dacs_usernames.remove(dacs_username);
        cmbDacsUsernames.removeItem(dacs_username);
    }
}

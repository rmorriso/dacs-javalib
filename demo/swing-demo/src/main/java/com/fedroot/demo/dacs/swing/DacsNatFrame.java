package com.fedroot.demo.dacs.swing;

import fedroot.dacs.http.DacsClientContext;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.*;

/**
 * Show current DACS NATs and allow them to be cleared
 */
public class DacsNatFrame extends JFrame {
    protected DacsClientContext dacscontext;
    protected DacsClientContext dacsClientContext;
    private JTextArea textarea;
    private JComboBox cmbDacsNats;
    private List<String> dacs_natnames;
    protected TextField tfStatus;
    /** The OK button */
    final protected JButton remove;
    /** The cancel button */
    final protected JButton can;
    
    /**
     * Construct a DacsLoginFrame
     *
     * @param dacsClientContext the UserContext under which DACS authentication will be done
     */
    public DacsNatFrame(final DacsClientContext dacsClientContext) {
        super();
        this.dacsClientContext = dacsClientContext;
        setTitle("DACS Notice Acknowledgement Tokens");
//        dacs_natnames = dacsClientContext.getDacsNatNames();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //Set up the content pane.
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        
        textarea = new JTextArea();
        textarea.setEditable(false);
        textarea.setCaretPosition(0);
        
        JPanel selectpane = new JPanel(new FlowLayout());
        
        if (dacs_natnames.size() > 0) {
            // DACS Usernames combobox
            cmbDacsNats = new JComboBox(dacs_natnames.toArray());
            cmbDacsNats.setToolTipText("Signout Credentials");
            cmbDacsNats.setEditable(false);
            cmbDacsNats.setSelectedIndex(0);
//            textarea.setText(dacsClientContext.getNoticeUris(cmbDacsNats.getSelectedItem().toString()));
            cmbDacsNats.addItemListener(
                    new ItemListener() {
                public void itemStateChanged(ItemEvent ae) {
                    if (ae.getStateChange() == ItemEvent.SELECTED) {
                        int i = cmbDacsNats.getSelectedIndex();
                        if (i >= 0) {
//                         textarea.setText(dacsClientContext.getNoticeUris(cmbDacsNats.getSelectedItem().toString()));
                        }
                    }
                }
            });
            selectpane.add(cmbDacsNats);
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
                if (dacs_natnames.size() > 0) {
                    String dacs_natname = dacs_natnames.get(cmbDacsNats.getSelectedIndex());
                    remove(dacs_natname);
                    tfStatus.setText("removed " + dacs_natname);
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
        inputpane.add(selectpane, BorderLayout.NORTH);
        inputpane.add(textarea, BorderLayout.CENTER);
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
     * remove a DACS Notice Acknowledgment Token
     * @param dacs_username the DACS username to signout
     * @return the number of DACS NATs remaining
     */
    protected void remove(String dacs_natname) {
//        this.dacsClientContext.removeNat(dacs_natname);
        this.dacs_natnames.remove(dacs_natname);
        this.cmbDacsNats.removeItem(dacs_natname);
        if (dacs_natnames.size() > 0) {
            cmbDacsNats.setSelectedIndex(0);
        } else {
            this.textarea.setText("");
        }
    }   
}

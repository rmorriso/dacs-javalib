/*
 * A password dialog box.
 * Copyright (C) 2001, 2002 Stephen Ostermiller
 * http://ostermiller.org/contact.pl?regarding=Java+Utilities
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See COPYING.TXT for details.
 */

package com.fedroot.dacs.swingutil;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.DacsUserAccount;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * A modal dialog that asks the user for a DACS jurisdiction, username and password.
 * Adapted from com.Ostermiller.util.PasswordDialog.java.
 * @author Stephen Ostermiller http://ostermiller.org/utils/PasswordDialog.html
 */
public class DacsLoginDialog extends JDialog {

    protected Federation federation;
    protected DacsContext dacscontext;
    /**
     * Locale specific strings displayed to the user.
     *
    */
    protected ResourceBundle labels;

    /**
     * Set the locale used for getting localized
     * strings.
     *
     * @param locale Locale used to for i18n.
     *
     */
    public void setLocale(Locale locale){
        labels = ResourceBundle.getBundle("com.fedroot.dacs.swingutil.DacsLoginDialog",  locale);
    }

    /**
     * Jurisdictions pick list
     *
     */
    protected JComboBox cmbJurisdictions;
    
    /**
     * Where the name is typed.
     *
     */
    protected JTextField name;
    /**
     * Where the password is typed.
     *
     */
    protected JPasswordField pass;
    /**
     * The OK button.
     *
     */
    protected JButton okButton;
    /**
     * The cancel button.
     *
     */
    protected JButton cancelButton;
    /**
     * The label for the field in which the name is typed.
     *
     */
    protected JLabel nameLabel;
    /**
     * The label for the field in which the password is typed.
     *
     */
    protected JLabel passLabel;

    /**
     * set DACS Jurisdictions for federation in the Combo Box
     * @param federation the DACS federation for which jurisdictions should be set
     *
     */
    public void setJurisdictions(Federation federation){
        for (String name : federation.getAuthenticatingJurisdictionNames()) {
            this.cmbJurisdictions.addItem(name);
        }
        this.cmbJurisdictions.setToolTipText("Select a Jurisdiction");
        this.cmbJurisdictions.setEditable(false);
        this.cmbJurisdictions.setSelectedIndex(0);   
    }
    

    /**
     * Set the name that appears as the default
     * An empty string will be used if this in not specified
     * before the dialog is displayed.
     *
     * @param name default name to be displayed.
     *
     */
    public void setName(String name){
        this.name.setText(name);
    }

    /**
     * Set the password that appears as the default
     * An empty string will be used if this is not specified
     * before the dialog is displayed.
     *
     * @param pass default password to be displayed.
     */
    public void setPass(String pass){
        this.pass.setText(pass);
    }

    /**
     * Set the label on the OK button.
     * The default is a localized string.
     *
     * @param ok label for the ok button.
     *
     */
    public void setOKText(String ok){
        this.okButton.setText(ok);
        pack();
    }

    /**
     * Set the label on the cancel button.
     * The default is a localized string.
     *
     * @param cancel label for the cancel button.
     *
     */
    public void setCancelText(String cancel){
        this.cancelButton.setText(cancel);
        pack();
    }

    /**
     * Set the label for the field in which the name is entered.
     * The default is a localized string.
     *
     * @param name label for the name field.
     */
    public void setNameLabel(String name){
        this.nameLabel.setText(name);
        pack();
    }

    /**
     * Set the label for the field in which the password is entered.
     * The default is a localized string.
     *
     * @param pass label for the password field.
     */
    public void setPassLabel(String pass){
        this.passLabel.setText(pass);
        pack();
    }

    /**
     * Get the name that was entered into the dialog before
     * the dialog was closed.
     *
     * @return the name from the name field.
     */
    public String getName(){
        return name.getText();
    }

    /**
     * Get the password that was entered into the dialog before
     * the dialog was closed.
     *
     * @return the password from the password field.
     */
    public String getPass(){
        return new String(pass.getPassword());
    }

    /**
     * Finds out if user used the OK button or an equivalent action
     * to close the dialog.
     * Pressing enter in the password field may be the same as
     * 'OK' but closing the dialog and pressing the cancel button
     * are not.
     *
     * @return true if the the user hit OK, false if the user canceled.
     */
    public boolean okPressed(){
        
        return pressed_OK;
    }

    /**
     * update this variable when the user makes an action
     */
    private boolean pressed_OK = false;

    /**
     * Create this dialog with the given parent and title.
     *
     * @param parent window from which this dialog is launched
     * @param title the title for the dialog box window
     */
    public DacsLoginDialog(Frame parent, String title, Federation federation, DacsContext dacscontext) {

        super(parent, title, true);

        setLocale(Locale.getDefault());

        if (title==null){
            setTitle(labels.getString("dialog.title"));
        }
        if (parent != null){
            setLocationRelativeTo(parent);
        }
        
        this.federation = federation;
        this.dacscontext = dacscontext;
        setJurisdictions(federation);
        pack();
        // super calls dialogInit, so we don't need to do it again.
    }

    /**
     * Create this dialog with the given parent and the default title.
     *
     * @param parent window from which this dialog is launched
     */
    public DacsLoginDialog(Frame parent, Federation federation, DacsContext dacscontext) {
        this(parent, null, federation, dacscontext);
    }

    /**
     * Create this dialog with the default title.
     */
    public DacsLoginDialog(Federation federation, DacsContext dacscontext) {
        this(null, null, federation, dacscontext);
    }

    /**
     * Called by constructors to initialize the dialog.
     */
    protected void dialogInit(){

        if (labels == null){
            setLocale(Locale.getDefault());
        }
        this.cmbJurisdictions = new JComboBox();
     
        name = new JTextField("", 12);
        pass = new JPasswordField("", 12);
        okButton = new JButton(labels.getString("dialog.ok"));
        cancelButton = new JButton(labels.getString("dialog.cancel"));
        nameLabel = new JLabel(labels.getString("dialog.name") + " ");
        passLabel = new JLabel(labels.getString("dialog.pass") + " ");

        super.dialogInit();

        KeyListener keyListener = (new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE ||
                        (e.getSource() == cancelButton
                        && e.getKeyCode() == KeyEvent.VK_ENTER)){
                    pressed_OK = false;
                    DacsLoginDialog.this.setVisible(false);
                }
                if (e.getSource() == okButton &&
                        e.getKeyCode() == KeyEvent.VK_ENTER){
                      pressed_OK = true;
                      DacsLoginDialog.this.setVisible(false);
                }
            }
        });
        addKeyListener(keyListener);

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Object source = e.getSource();
                if (source == name){
                    // the user pressed enter in the name field.
                    name.transferFocus();
                } else {
                    // other actions close the dialog.
                    pressed_OK = (source == pass || source == okButton);
                    DacsLoginDialog.this.setVisible(false);
                }
            }
        };

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets.top = 5;
        c.insets.bottom = 5;
        JPanel pane = new JPanel(gridbag);
        pane.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
        JLabel label;

        c.anchor = GridBagConstraints.EAST;
        
        gridbag.setConstraints(cmbJurisdictions, c);
        pane.add(cmbJurisdictions);
        
        c.gridy = 1;
        c.insets.left = 2; c.insets.right = 2;
        gridbag.setConstraints(nameLabel, c);
        pane.add(nameLabel);

        gridbag.setConstraints(name, c);
        name.addActionListener(actionListener);
        name.addKeyListener(keyListener);
        pane.add(name);
        
        c.gridy = 1;
        gridbag.setConstraints(passLabel, c);
        pane.add(passLabel);

        gridbag.setConstraints(pass, c);
        pass.addActionListener(actionListener);
        pass.addKeyListener(keyListener);
        pane.add(pass);

        c.gridy = 3;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.CENTER;
        JPanel panel = new JPanel();
        okButton.addActionListener(actionListener);
        okButton.addKeyListener(keyListener);
        panel.add(okButton);
        cancelButton.addActionListener(actionListener);
        cancelButton.addKeyListener(keyListener);
        panel.add(cancelButton);
        gridbag.setConstraints(panel, c);
        pane.add(panel);

        getContentPane().add(pane);

        pack();
    }

    /**
     * Shows the dialog and returns true if login was successful
     *
     * @return true if login successful, false if the user canceled.
     */
    public boolean showDialog(){
        setVisible(true);
        if (okPressed()) {
           Jurisdiction jurisdiction = federation.getJurisdictionByName(cmbJurisdictions.getSelectedItem().toString());
           DacsUserAccount account = 
              new DacsUserAccount(federation, jurisdiction, getName());
           try {
              if (account.authenticate(dacscontext, getPass())) {
                  return true;
              } else {
                  return showDialog();
              }
           } catch (Exception ex) {
               // should updated DacsLoginDialog to report exception message
               ex.printStackTrace();
               return showDialog();
           }
        } else {
            return false;
        }
    }

}
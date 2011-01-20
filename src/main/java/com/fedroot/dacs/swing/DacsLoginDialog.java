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
package com.fedroot.dacs.swing;

import fedroot.dacs.entities.Jurisdiction;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

    protected SessionManager sessionManager;
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
    @Override
    public void setLocale(Locale locale) {
        labels = ResourceBundle.getBundle("DacsLoginDialog", locale);
    }
    /**
     * Jurisdictions pick list
     *
     */
    protected JComboBox cmbJurisdictions;
    /**
     * Where the username is typed.
     *
     */
    protected JTextField username;
    /**
     * Where the password is typed.
     *
     */
    protected JPasswordField password;
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
     * The label for the field in which the username is typed.
     *
     */
    protected JLabel usernameLabel;
    /**
     * The label for the field in which the password is typed.
     *
     */
    protected JLabel passwordLabel;

    /**
     * set DACS Jurisdictions for federation in the Combo Box
     * @param federation the DACS federation for which jurisdictions should be set
     *
     */
    public void setJurisdictions(SessionManager sessionController) {
        for (Jurisdiction jurisdiction : sessionController.getAuthenticatingJurisdictions()) {
            this.cmbJurisdictions.addItem(jurisdiction);
        }
        this.cmbJurisdictions.setToolTipText("Select a Jurisdiction");
        this.cmbJurisdictions.setEditable(false);
        this.cmbJurisdictions.setSelectedIndex(0);
    }

    /**
     * Set the username that appears as the default
     * An empty string will be used if this in not specified
     * before the dialog is displayed.
     *
     * @param username default username to be displayed.
     *
     */
    public void setUsername(String username) {
        this.username.setText(username);
    }

    /**
     * Set the password that appears as the default
     * An empty string will be used if this is not specified
     * before the dialog is displayed.
     *
     * @param password default password to be displayed.
     */
    public void setPassword(String password) {
        this.password.setText(password);
    }

    /**
     * Set the label on the OK button.
     * The default is a localized string.
     *
     * @param ok label for the ok button.
     *
     */
    public void setOKText(String ok) {
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
    public void setCancelText(String cancel) {
        this.cancelButton.setText(cancel);
        pack();
    }

    /**
     * Set the label for the field in which the username is entered.
     * The default is a localized string.
     *
     * @param username label for the username field.
     */
    public void setUsernameLabel(String name) {
        this.usernameLabel.setText(name);
        pack();
    }

    /**
     * Set the label for the field in which the password is entered.
     * The default is a localized string.
     *
     * @param password label for the password field.
     */
    public void setPasswordLabel(String pass) {
        this.passwordLabel.setText(pass);
        pack();
    }

    /**
     * Get the username that was entered into the dialog before
     * the dialog was closed.
     *
     * @return the username from the username field.
     */
    public String getUsername() {
        return username.getText();
    }

    /**
     * Get the password that was entered into the dialog before
     * the dialog was closed.
     *
     * @return the password from the password field.
     */
    public String getPassword() {
        return new String(password.getPassword());
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
    public boolean okPressed() {

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
    public DacsLoginDialog(Frame parent, String title, SessionManager sessionController) {

        super(parent, title, true);

        this.sessionManager = sessionController;

        setLocale(Locale.getDefault());

        if (title == null) {
            setTitle(labels.getString("dialog.title"));
        }
        if (parent != null) {
            setLocationRelativeTo(parent);
        }

        setJurisdictions(sessionController);
        pack();
        // super calls dialogInit, so we don't need to do it again.
    }

    /**
     * Called by constructors to initialize the dialog.
     */
    @Override
    protected void dialogInit() {

        if (labels == null) {
            setLocale(Locale.getDefault());
        }
        this.cmbJurisdictions = new JComboBox();

        username = new JTextField("", 12);
        password = new JPasswordField("", 12);
        okButton = new JButton(labels.getString("dialog.ok"));
        cancelButton = new JButton(labels.getString("dialog.cancel"));
        usernameLabel = new JLabel(labels.getString("dialog.name") + " ");
        passwordLabel = new JLabel(labels.getString("dialog.pass") + " ");

        super.dialogInit();

        KeyListener keyListener = (new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE
                        || (e.getSource() == cancelButton
                        && e.getKeyCode() == KeyEvent.VK_ENTER)) {
                    pressed_OK = false;
                    DacsLoginDialog.this.setVisible(false);
                }
                if (e.getSource() == okButton
                        && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pressed_OK = true;
                    DacsLoginDialog.this.setVisible(false);
                }
            }
        });
        addKeyListener(keyListener);

        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == username) {
                    // the user pressed enter in the username field.
                    username.transferFocus();
                } else {
                    // other actions close the dialog.
                    pressed_OK = (source == password || source == okButton);
                    DacsLoginDialog.this.setVisible(false);
                }
            }
        };


        JPanel jurisdictionPanel = new JPanel(new FlowLayout());
        jurisdictionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
        jurisdictionPanel.add(cmbJurisdictions);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets.top = 5;
        c.insets.bottom = 5;

        JPanel gridPanel = new JPanel(gridbag);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

        c.anchor = GridBagConstraints.EAST;
        gridbag.setConstraints(usernameLabel, c);
        gridPanel.add(usernameLabel);

        gridbag.setConstraints(username, c);
        username.addActionListener(actionListener);
        username.addKeyListener(keyListener);
        gridPanel.add(username);

        c.gridy = 1;
        gridbag.setConstraints(passwordLabel, c);
        gridPanel.add(passwordLabel);

        gridbag.setConstraints(password, c);
        password.addActionListener(actionListener);
        password.addKeyListener(keyListener);
        gridPanel.add(password);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        okButton.addActionListener(actionListener);
        okButton.addKeyListener(keyListener);
        buttonPanel.add(okButton);
        cancelButton.addActionListener(actionListener);
        cancelButton.addKeyListener(keyListener);
        buttonPanel.add(cancelButton);
       
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(jurisdictionPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        Container container = getContentPane();
        container.setSize(new Dimension(300,180));
        container.add(mainPanel);
        
        setResizable(false);
        pack();
    }

    /**
     * Shows the dialog and returns true if login was successful.
     *
     * @return true if login successful, false if the user canceled.
     */
    public boolean showDialog() {
        setVisible(true);
        if (okPressed()) {
            Jurisdiction jurisdiction = (Jurisdiction) cmbJurisdictions.getSelectedItem();
            sessionManager.signon(jurisdiction, getUsername(), getPassword());
            return true;
        } else {
            return false;
        }
    }
}

/*
 * A DACS Notice Presentation dialog box.
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
import com.fedroot.dacs.DacsNotices;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import org.apache.commons.httpclient.Header;

/**
 * A modal dialog that asks the user for a DACS jurisdiction, username and password.
 * Adapted from com.Ostermiller.util.PasswordDialog.java.
 * More information about this class is available from <a target="_top" href=
 * "http://ostermiller.org/utils/PasswordDialog.html">ostermiller.org</a>.
 * @author Stephen Ostermiller http://ostermiller.org/contact.pl?regarding=Java+Utilities
 */
public class DacsNoticePresentationDialog extends JDialog {

    protected Header contentType;
    private JEditorPane htmlpane;

    /**
     * Locale specific strings displayed to the user.
     *
     * @since ostermillerutils 1.00.00
     */
    protected ResourceBundle labels;

    /**
     * Set the locale used for getting localized
     * strings.
     *
     * @param locale Locale used to for i18n.
     */
    public void setLocale(Locale locale){
        labels = ResourceBundle.getBundle("com.fedroot.dacs.swingutil.DacsNoticePresentationDialog",  locale);
    }

    
    /**
     * The ACCEPT button.
     */
    protected JButton acceptButton;

    /**
     * The DECLINE button.
     */
    protected JButton declineButton;


    /**
     * Set the label on the ACCEPT button.
     * The default is a localized string.
     */
    public void setAcceptText(String accept){
        this.acceptButton.setText(accept);
        pack();
    }
    
    /**
     * Set the label on the DECLINE button.
     * The default is a localized string.
     *
     * @param decline label for the decline button.
     */
    public void setDeclineText(String decline){
        this.declineButton.setText(decline);
        pack();
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
    public boolean acceptPressed(){
        
        return pressed_ACCEPT;
    }

    /**
     * update this variable when the user makes an action
     */
    private boolean pressed_ACCEPT = false;

    /**
     * Create this dialog with the given parent and title.
     *
     * @param parent window from which this dialog is launched
     * @param title the title for the dialog box window
     */
    public DacsNoticePresentationDialog(Frame parent, String title, DacsContext dacscontext, DacsNotices notices) {

        super(parent, title, true);

        setLocale(Locale.getDefault());

        if (title == null){
            setTitle(labels.getString("dialog.title"));
        }
        if (parent != null){
            setLocationRelativeTo(parent);
        }
        
        setDocumentContent("text/html", notices.getHtml());
        validate();
        // super calls dialogInit, so we don't need to do it again.
    }

    /**
     * Create this dialog with the given parent and the default title.
     *
     * @param parent window from which this dialog is launched
     */
    public DacsNoticePresentationDialog(Frame parent, DacsContext dacscontext, DacsNotices notices) {
        this(parent, null, dacscontext, notices);
    }

    /**
     * Create this dialog with the default title.
     */
    public DacsNoticePresentationDialog(DacsContext dacscontext, DacsNotices notices) {
        this(null, null, dacscontext, notices);
    }

    /**
     * Called by constructors to initialize the dialog.
     */
    protected void dialogInit(){

        if (labels == null){
            setLocale(Locale.getDefault());
        }

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Object source = e.getSource();
                if (source == acceptButton){
                    pressed_ACCEPT = true;
                    DacsNoticePresentationDialog.this.setVisible(false);
                } else if (source == declineButton) {
                    pressed_ACCEPT = false;
                    DacsNoticePresentationDialog.this.setVisible(false);
                } else {
                    pressed_ACCEPT = false;
                    DacsNoticePresentationDialog.this.setVisible(false);                    
                }
            }
        };

        acceptButton = new JButton(labels.getString("dialog.accept"));
        declineButton = new JButton(labels.getString("dialog.decline"));
        
        super.dialogInit();
        
        JPanel inputpane = new JPanel(new FlowLayout());        

        inputpane.add(acceptButton);
        inputpane.add(declineButton);
        
        acceptButton.addActionListener(actionListener);
        inputpane.add(acceptButton);
        declineButton.addActionListener(actionListener);
        inputpane.add(declineButton);
              
        htmlpane = new JEditorPane();
        // htmlpane.setContentType("image/png");
        htmlpane.setEditable(false);
        // initialize htmlpane with something
        setDocumentContent("text/html", "<html><body><img src=\"http://fedroot.com/images/rick-and-frog.jpg\" width=400 height=300></body></html>");
        // not set when called from super() setDocumentContent(this.content);
        JScrollPane presentation = new JScrollPane(htmlpane);
        

        // call super.dialogInit() to set the content pane
        // NO! super already calls dialogInit() :  
        // super.dialogInit() ;
        
        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.add(presentation, BorderLayout.CENTER);
        pane.add(inputpane, BorderLayout.SOUTH);
        
        Container container = getContentPane();
        container.add(pane);
        container.setSize(600,400);
        pack();

    }

    /**
     * Shows the dialog and returns true if the user pressed ok.
     *
     * @return true if the the user hit OK, false if the user canceled.
     */
    public boolean showDialog(){
        setVisible(true);
        return acceptPressed();
    }

    /**
     * Sets the HTML content to be displayed.
     *
     * @param content an HTML document string
     */
    private void setDocumentContent(String contenttype, String content) {
        HTMLDocument doc = new HTMLDocument();
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        // doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        
        try {
            htmlpane.setContentType(contenttype);
            htmlpane.read(new ByteArrayInputStream(content.getBytes()), doc);
            htmlpane.setDocument(doc);
            htmlpane.setCaretPosition(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
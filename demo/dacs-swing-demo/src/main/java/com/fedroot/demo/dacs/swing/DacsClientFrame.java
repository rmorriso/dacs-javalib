/*
 * DacsClientFrame.java
 *
 * Created on December 6, 2005, 8:46 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package com.fedroot.demo.dacs.swing;

import fedroot.dacs.entities.Federation;
import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsGetRequest;
import fedroot.dacs.http.DacsStatus;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * main JFrame from which DACS example operations are launched
 * @author rmorriso
 */
public class DacsClientFrame extends JFrame {

    private final Federation federation;
    private DacsClientContext dacsClientContext;
    protected Header contentType;
    private JComboBox cmbActions, cmbJurisdictions;
    private TextField tfUrl;
    private JTextArea taTextResponse;
    private JTextArea taCookieText;
    private JTextArea taOtherText;
    private JEditorPane htmlPane;
    private JCheckBox jcbDacsCheckonly, jcbEnableEventHandling;
    /** Log object for this class. */
    private static final Log LOG = LogFactory.getLog(DacsClientFrame.class);
    private static final String[] actions = {
        "List DEMO Jurisdictions",
        "Requires Authentication",
        "Requires Notice Acknowledgement",
        "Requires Secure Notice Acknowledgment",
        "Requires Authenticated Notice Acknowledgment",
        "Signout from demo.fedroot.com",
        "Print environment"
    };
    private static final String[] actionUrls = {
        "https://fedroot.com/dacs/dacs_list_jurisdictions",
        "https://demo.fedroot.com/test/dacs-wrapped/auth-required.html",
        "https://demo.fedroot.com/test/dacs-wrapped/notice-required.html",
        "https://demo.fedroot.com/testalt/dacs-wrapped/secure-notice-required.html",
        "https://demo.fedroot.com/test/dacs-wrapped/auth-and-notice-required.html",
        "https://fedroot.com/metalogic/dacs/dacs_signout",
        "https://demo.fedroot.com/test/dacs/prenv"
    };

    /**
     * 
     * @param dacsClientContext
     * @param feduri 
     * @throws java.lang.Exception 
     */
    public DacsClientFrame(DacsClientContext dacsClientContext, Federation federation)
            throws Exception {
        LOG.info("Federation " + federation.getFederationName());
        this.federation = federation;
        this.dacsClientContext = dacsClientContext;
//        this.dacsClientContext.setDacs902EventHandler(federation, new Event902Handler(this));
//        this.dacsClientContext.setDacs905EventHandler(federation, new Event905Handler(this));

        JPanel panAction = new JPanel(new BorderLayout());
        JPanel panInput_0 = new JPanel(new FlowLayout());
        JPanel panInput_1 = new JPanel(new FlowLayout());
        JPanel panInput_2 = new JPanel(new FlowLayout());

        /** Enable/Disable DACS Check_only mode */
        jcbDacsCheckonly = new JCheckBox("Enable DACS Check Only", false);
        jcbDacsCheckonly.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        if (jcbDacsCheckonly.isSelected()) {
                            jcbEnableEventHandling.setSelected(false);
                            jcbEnableEventHandling.setEnabled(false);
                        } else {
                            jcbEnableEventHandling.setEnabled(true);
                        }
                    }
                });

        /** Enable/Disable Event Handling */
        jcbEnableEventHandling = new JCheckBox("Enable Event Handling", false);

        final JButton btnGOTO = new JButton("Goto URL");
        btnGOTO.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        try {
                            followUrl(new URI(tfUrl.getText()));
                        } catch (URISyntaxException ex) {
                            // TODO implement popup for error messages
                        }
                    }
                });

        final JButton btnGO = new JButton("GO");
        btnGO.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        try {
                            followUrl(new URI(actionUrls[cmbActions.getSelectedIndex()]));
                        } catch (URISyntaxException ex) {
                            // TODO implement popup for error messages
                        }
                    }
                });

        final JButton btnUSERNAMES = new JButton("Usernames");
        btnUSERNAMES.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        createAndShowDacsUsernameFrame();
                    }
                });

        final JButton btnLOGIN = new JButton("Login");
        btnLOGIN.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        createAndShowLoginFrame();
                    }
                });

        final JButton btnNAT = new JButton("NATs");
        btnNAT.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        createAndShowDacsNatFrame();
                    }
                });


        Container container = this.getContentPane();

        cmbActions = new JComboBox(actions);
        cmbActions.setToolTipText("Select an Action");
        cmbActions.setEditable(true);
        cmbActions.setSelectedIndex(0);

        JLabel lblAction = new JLabel("Action:");

        tfUrl = new TextField(70);
        tfUrl.setEditable(true);

        panInput_0.add(tfUrl);
        panInput_0.add(btnGOTO);

        panInput_1.add(lblAction);
        panInput_1.add(cmbActions);
        panInput_1.add(btnGO);
        panInput_1.add(btnLOGIN);
        panInput_1.add(btnUSERNAMES);
        panInput_1.add(btnNAT);

        panAction.add(panInput_0, BorderLayout.NORTH);
        panAction.add(panInput_1, BorderLayout.SOUTH);

        panInput_2.add(jcbDacsCheckonly);
        panInput_2.add(jcbEnableEventHandling);

        JSplitPane splitInputPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                panAction, panInput_2);

        splitInputPane.setOneTouchExpandable(false);

        taTextResponse = new JTextArea();
        taTextResponse.setEditable(false);
        taTextResponse.setCaretPosition(0);

        htmlPane = new JEditorPane();
        // htmlPane.setContentType("image/png");
        htmlPane.setEditable(false);

        JSplitPane splitResponsePane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(taTextResponse),
                new JScrollPane(htmlPane));
        splitResponsePane.setOneTouchExpandable(false);
        splitResponsePane.setResizeWeight(0.35);


        container.setLayout(new BorderLayout());
        container.add(splitInputPane, BorderLayout.NORTH);
        container.add(splitResponsePane, BorderLayout.CENTER);
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
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

        try {
            htmlPane.setContentType(contenttype);
            htmlPane.read(new ByteArrayInputStream(content.getBytes()), doc);
            htmlPane.setDocument(doc);
            htmlPane.setCaretPosition(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        taTextResponse.setText(content);
        taTextResponse.setCaretPosition(0);
        taTextResponse.requestFocus();
    }

    /**
     * execute DacsGetRequest for a URL, handling DACS response status
     * @param uri
     */
    private synchronized void followUrl(final URI uri) {
        final StringBuffer sb = new StringBuffer();
        final BufferedInputStream responsestream;
        DacsGetRequest dacsget = new DacsGetRequest(uri);
        // not used: uncomment for HTTP basic auth processing
        // dacsget.setDoAuthentication(true);
        // needed when event handling is not used
        // TODO: do this in dacsContext now??? dacsget.setFollowRedirects(true);
        try {
            HttpResponse dacsstatus;
//            if (jcbDacsCheckonly.isSelected()) {
//                dacsstatus = dacsClientContext.executeCheckOnlyMethod(dacsget);
//            } else if (jcbEnableEventHandling.isSelected()) {
//                dacsstatus = dacsClientContext.executeCheckFailMethod(dacsget);
//            } else {
                dacsstatus = dacsClientContext.executeGetRequest(dacsget);
//            }

//            switch (dacsstatus.getStatusLine().getStatusCode()) {
//                case DacsStatus.SC_DACS_ACCESS_GRANTED: //check mode
//                case DacsStatus.SC_OK: // normal mode
//                    String contenttype = dacsget.getHttpGet().getFirstHeader("Content-Type").getValue();
//                    loadPage(contenttype, dacsget.getInputStream(dacsClientContext.)
//                    break;
//                case DacsStatus.SC_DACS_ACCESS_DENIED: // check mode
//                    LOG.info(DacsStatus.getStatusText(dacsstatus.));
//                    contenttype = dacsget.getResponseHeader("Content-Type").getValue();
//                    loadPage(contenttype, dacsget.getResponseBodyAsStream());
//                    break;
//                case DacsStatus.SC_DACS_ACCESS_ERROR: // check mode
//                    LOG.info(DacsStatus.getStatusText(dacsstatus));
//                    break;
//                default:
//                    LOG.info("DacsGetRequest returned status: " + DacsStatus.getStatusText(dacsstatus));
//                    break;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads contents of the input stream in a separate thread.
     * @param is input stream to be rendered as HTML
     */
    private void loadPage(final String contenttype, final InputStream is) {
        // create a new thread to load the URL from
        final StringBuffer sb = new StringBuffer();
        new Thread() {

            public void run() {

                try {
                    // chain the InputStream to a Reader
                    Reader r = new InputStreamReader(new BufferedInputStream(is));
                    int c;
                    while ((c = r.read()) != -1) {
                        sb.append((char) c);
                    }
                    is.close();
                    if (is != null) {
                        // set the HTML on the UI thread
                        SwingUtilities.invokeLater(
                                new Runnable() {

                                    public void run() {
                                        setDocumentContent(contenttype, sb.toString());
                                    }
                                });
                    }
                } catch (IOException ex) {
                    DacsClientFrame.LOG.warn(ex.getMessage());
                    //  ex.printStackTrace();
                } finally {
                    setDocumentContent(contenttype, sb.toString());
                }
            }
        }.start();
    }

    /**
     * Loads the page at the given URL from a separate thread.
     * @param url
     */
    private void dumpHeaders(DacsGetRequest dacsget) {
        // dump headers
        for (Header hdr : dacsget.getHttpGet().getAllHeaders()) {
            System.out.println("Header: " + hdr.getName() + " = " + hdr.getValue());
        }
        contentType = dacsget.getHttpGet().getFirstHeader("Content-Type");
        System.out.println("Content-Type: " + contentType);

    }

    /**
     * Create and display the DACS Login Frame.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowLoginFrame() {
        // use the tiddly window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        DacsLoginFrame loginFrame = new DacsLoginFrame(this.federation, this.dacsClientContext);
        loginFrame.setSize(750, 200);
        loginFrame.setVisible(true);
    }

    /**
     * Create and display the DACS Username Frame.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowDacsUsernameFrame() {
        // use the tiddly window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        DacsUsernameFrame signoutFrame = new DacsUsernameFrame(this.dacsClientContext);
        signoutFrame.setSize(750, 200);
        signoutFrame.setVisible(true);
    }

    /**
     * Create and display the DACS Username Frame.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowDacsNatFrame() {
        // use the tiddly window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        DacsNatFrame natFrame = new DacsNatFrame(this.dacsClientContext);
        natFrame.setSize(750, 200);
        natFrame.setVisible(true);
    }
}

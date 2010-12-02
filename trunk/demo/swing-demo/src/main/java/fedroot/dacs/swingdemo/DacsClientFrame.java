/*
 * DacsClientFrame.java
 *
 * Created on December 6, 2005, 8:46 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.swingdemo;

import fedroot.dacs.entities.Federation;
import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsGetRequest;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private JComboBox actionsComboBox;
    private TextField urlTextField;
    private JTextArea responseTextArea;
    private JEditorPane htmlPane;
    private JCheckBox checkOnlyCheckBox;
    private JCheckBox enableEventHandlingCheckBox;

    private static final Logger logger = Logger.getLogger(DacsSwingDemo.class.getName());

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
        logger.log(Level.INFO, "Federation {0}", federation.getFederationName());
        
        this.federation = federation;
        this.dacsClientContext = dacsClientContext;
//        this.dacsClientContext.setDacs902EventHandler(federation, new Event902Handler(this));
//        this.dacsClientContext.setDacs905EventHandler(federation, new Event905Handler(this));

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel gotoUrlPanel = new JPanel(new FlowLayout());
        JPanel actionPanel = new JPanel(new FlowLayout());
        JPanel modifiersPanel = new JPanel(new FlowLayout());

        /** Enable/Disable DACS Check_only mode */
        checkOnlyCheckBox = new JCheckBox("Enable DACS Check Only", false);
        checkOnlyCheckBox.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        if (checkOnlyCheckBox.isSelected()) {
                            enableEventHandlingCheckBox.setSelected(false);
                            enableEventHandlingCheckBox.setEnabled(false);
                        } else {
                            enableEventHandlingCheckBox.setEnabled(true);
                        }
                    }
                });

        /** Enable/Disable Event Handling */
        enableEventHandlingCheckBox = new JCheckBox("Enable Event Handling", false);

        final JButton btnGOTO = new JButton("Goto URL");
        btnGOTO.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        try {
                            followUrl(new URI(urlTextField.getText().trim()));
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
                            followUrl(new URI(actionUrls[actionsComboBox.getSelectedIndex()]));
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

        actionsComboBox = new JComboBox(actions);
        actionsComboBox.setToolTipText("Select an Action");
        actionsComboBox.setEditable(true);
        actionsComboBox.setSelectedIndex(0);

        JLabel actionLabel = new JLabel("Action:");

        urlTextField = new TextField(70);
        urlTextField.setEditable(true);

        gotoUrlPanel.add(urlTextField);
        gotoUrlPanel.add(btnGOTO);

        actionPanel.add(actionLabel);
        actionPanel.add(actionsComboBox);
        actionPanel.add(btnGO);
        actionPanel.add(btnLOGIN);
        actionPanel.add(btnUSERNAMES);
        actionPanel.add(btnNAT);

        mainPanel.add(gotoUrlPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        modifiersPanel.add(checkOnlyCheckBox);
        modifiersPanel.add(enableEventHandlingCheckBox);

        JSplitPane splitInputPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                mainPanel, modifiersPanel);

        splitInputPane.setOneTouchExpandable(false);

        responseTextArea = new JTextArea();
        responseTextArea.setEditable(false);
        responseTextArea.setCaretPosition(0);

        htmlPane = new JEditorPane();
        // htmlPane.setContentType("image/png");
        htmlPane.setEditable(false);

        JSplitPane splitResponsePane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(responseTextArea),
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

        responseTextArea.setText(content);
        responseTextArea.setCaretPosition(0);
        responseTextArea.requestFocus();
    }

    /**
     * execute DacsGetRequest for a URL, handling DACS response status
     * @param uri
     */
    private synchronized void followUrl(URI uri) {
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
                    DacsClientFrame.logger.severe(ex.getMessage());
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

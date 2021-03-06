/*
 * DacsClientFrame.java
 *
 * Created on December 6, 2005, 8:46 AM
 *
 * Copyright (c) 2010 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.swingdemo;

import com.fedroot.dacs.swing.DacsLoginDialog;
import com.fedroot.dacs.swing.SessionManager;
import fedroot.dacs.client.DacsCheckRequest;
import fedroot.dacs.events.DacsEventNotifier;
import fedroot.dacs.events.DacsEventNotifier.Status;
import fedroot.dacs.exceptions.DacsException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import org.apache.http.Header;

/**
 * main JFrame from which example DACS-wrapped HTTP requests are launched
 * @author rmorriso
 */
public class DacsSwingDemo {

    protected JFrame mainFrame;
    protected Header contentType;
    private JComboBox actionsComboBox;
    private TextField urlTextField;
    private JTextArea responseTextArea;
    private JEditorPane htmlPane;
    private JButton btnGO;
    private JButton btnLOGIN;
    private JButton btnLOGOUT;
    private SessionManager sessionManager;
    private static final Logger logger = Logger.getLogger(DacsSwingDemo.class.getName());
    private static final String DACS_BASE_URI = "https://fedroot.com/test/dacs";
    private DacsLoginDialog loginDialog;
    // TODO - use a ComboBoxModel instead
    private static final String[] actions = {
        "No Authentication Required",
        "Requires Authentication",
        "Requires DEV Group Membership",
        "Requires DACS Admin Privilege",
        "No Rule Applies"
    };
    private static final String[] testURLs = {
        "https://fedroot.com/test/dacs-wrapped/noauth-required.html",
        "https://fedroot.com/test/dacs-wrapped/auth-required.html",
        "https://fedroot.com/test/dacs-wrapped/dev-required.html",
        "https://fedroot.com/test/dacs/dacs_prenv",
        "https://fedroot.com/test/dacs-wrapped/no-rule.html"
    };

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        SessionManager sessionManager = new SessionManager(DACS_BASE_URI);

        DacsSwingDemo dacsSwingDemo = new DacsSwingDemo("DACS JavaLib Example Thick Client", sessionManager);
        dacsSwingDemo.start();
    }

    /**
     * 
     * @param title the title of the top level window
     * @param sessionManager a DACS aware session manager for login, logout, making HTTP requests, saving session state
     * @throws java.lang.Exception 
     */
    public DacsSwingDemo(String title, final SessionManager sessionManager) {

        logger.log(Level.FINEST, "Launching MainFrame ");

        mainFrame = new JFrame(title);
        Dimension minSize = new Dimension(600, 400);
        mainFrame.setMinimumSize(minSize);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.sessionManager = sessionManager;

        loginDialog = new DacsLoginDialog(mainFrame, "Login", sessionManager);

        DacsEventNotifier.Listener dacsEventListener = new DacsEventNotifier.Listener() {

            @Override
            public void status(Status status) {
                // TODO replace this with updates to the status line text at the bottom of the frame
                switch (status) {
                    case signon:
                        btnLOGIN.setEnabled(false);
                        btnLOGOUT.setEnabled(true);
                        break;
                    case signout:
                        btnLOGIN.setEnabled(true);
                        btnLOGOUT.setEnabled(false);
                        break;
                }
            }

            @Override
            public void notify(DacsException ex, DacsCheckRequest checkRequest) {
                switch (ex.getStatusCode()) {
                    case 900:
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "900 Error", JOptionPane.WARNING_MESSAGE);
                        break;
                    case 901:
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "901 Error", JOptionPane.WARNING_MESSAGE);
                        break;
                    case 902:
//                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "902 Error", JOptionPane.WARNING_MESSAGE );
                        if (loginDialog.showDialog()) {
                            // login successful - try the request again
                            loadPage("text/html", testURLs[actionsComboBox.getSelectedIndex()]);
                        }
                        break;
                    case 903:
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "903 Error", JOptionPane.WARNING_MESSAGE);
                        break;
                    case 904:
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "904 Error", JOptionPane.WARNING_MESSAGE);
                        break;
                }

            }
        };

        sessionManager.addDacsEventListener(dacsEventListener);

        init(mainFrame);

    }

    private void init(JFrame mainFrame) {

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel gotoUrlPanel = new JPanel(new FlowLayout());
        JPanel actionPanel = new JPanel(new FlowLayout());
        JPanel modifiersPanel = new JPanel(new FlowLayout());


        btnGO = new JButton("GO");
        btnGO.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        loadPage("text/html", testURLs[actionsComboBox.getSelectedIndex()]);
                    }
                });

        btnLOGIN = new JButton("Login");
        btnLOGIN.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        loginDialog.showDialog();
                    }
                });


        btnLOGOUT = new JButton("Logout");
        btnLOGOUT.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        sessionManager.signout();
                    }
                });
        //initially user is not signed in - btnLOGOUT will be enabled upon successful login
        btnLOGOUT.setEnabled(false);



        actionsComboBox = new JComboBox(actions);
        actionsComboBox.setToolTipText("Select an Action");
        actionsComboBox.setEditable(true);
        actionsComboBox.setSelectedIndex(0);

        JLabel actionLabel = new JLabel("Action:");

        urlTextField = new TextField(70);
        urlTextField.setEditable(true);

        actionPanel.add(actionLabel);
        actionPanel.add(actionsComboBox);
        actionPanel.add(btnGO);
        actionPanel.add(btnLOGIN);
        actionPanel.add(btnLOGOUT);

        mainPanel.add(gotoUrlPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);


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

        Container container = mainFrame.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(splitInputPane, BorderLayout.NORTH);
        container.add(splitResponsePane, BorderLayout.CENTER);

        mainFrame.pack();
    }

    public void start() {
        mainFrame.setVisible(true);
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
        } catch (BadLocationException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

        try {
            htmlPane.setContentType(contenttype);
            htmlPane.read(new ByteArrayInputStream(content.getBytes()), doc);
            htmlPane.setDocument(doc);
            htmlPane.setCaretPosition(0);
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }

        responseTextArea.setText(content);
        responseTextArea.setCaretPosition(0);
        responseTextArea.requestFocus();
    }

    /**
     * Loads contents of the input stream in a separate thread.
     * @param is input stream to be rendered as HTML
     */
    private void loadPage(final String contenttype, final String url) {
        // create a new thread to load the URL from
        final StringBuffer stringBuffer = new StringBuffer();
        new Thread() {

            @Override
            public void run() {
                {
                    InputStream inputStream = null;
                    try {
                        inputStream = sessionManager.getInputStream(url);
                        Reader reader = new InputStreamReader(new BufferedInputStream(inputStream));
                        int character;
                        while ((character = reader.read()) != -1) {
                            stringBuffer.append((char) character);
                        }
                        inputStream.close();
                        if (inputStream != null) {
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    setDocumentContent(contenttype, stringBuffer.toString());
                                }
                            });
                        }
                    } catch (DacsException ex) { // note: we EXPECT DacsExceptions
                        logger.log(Level.FINEST, ex.getMessage());
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, ex.getMessage());
                    } finally {
                        setDocumentContent(contenttype, stringBuffer.toString());
                        try {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(DacsSwingDemo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }.start();
    }
}

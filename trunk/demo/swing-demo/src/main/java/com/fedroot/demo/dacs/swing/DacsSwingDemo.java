
/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */
package com.fedroot.demo.dacs.swing;

import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsGetRequest;
import java.util.logging.Level;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;
import org.jdesktop.application.Task.InputBlocker;
import org.jdesktop.application.TaskMonitor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import org.apache.http.HttpResponse;
import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.SingleFrameApplication;

/**
 * A demo of the {@code @Action} <i>block</i> options for background
 * task.  It's an example of three of the {@code Action.Block} types:
 * <pre>
 * &#064;Action(block = Task.BlockingScope.ACTION)  
 * public Task blockAction() { ... }
 * 
 * &#064;Action(block = Task.BlockingScope.COMPONENT) 
 * public Task blockComponent() { ... }
 * 
 * &#064;Action(block = Task.BlockingScope.WINDOW) 
 * public Task blockWindow() { ... }
 * 
 * &#064;Action(block = Task.BlockingScope.APPLICATION)
 * public Task blockApplication() { ... }
 * </pre>
 * The first {@code BlockingScope.ACTION} {@code @Action} disables the
 * corresponding {@code Action} while {@code blockAction} method runs.
 * When you press the blockAction button or toolbar-button or menu
 * item you'll observe that all of the components are disabled.  The
 * {@code BlockingScope.COMPONENT} version only disables the component
 * that triggered the action.  The {@code Block.WINDOW} method
 * uses a custom {@link Task.InputBlocker inputBlocker} to 
 * temporarily block input to the by making the window's
 * glass pane visible.  And the {@code Task.BlockingScope.APPLICATION} 
 * version pops up a modal dialog for the action's duration.
 * The blocking dialog's title/message/icon are defined by resources
 * from the ResourceBundle named {@code BlockingExample1}:
 * <pre>
 * BlockingDialog.title = Blocking Application 
 * BlockingDialog.message = Please wait patiently ...
 * Action.BlockingDialog.icon = wait.png
 * </pre>
 * 
 * <p>
 * All of the actions in this example just sleep for about 2 seconds,
 * while periodically updating their Task's message/progress properties.
 * 
 * <p>
 * This class loads resources from the ResourceBundle called
 * {@code BlockingExample1}.  It depends on the example {@code StatusBar} class.
 * 
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 * @see ApplicationContext
 * @see Application
 * @see Action
 * @see Task
 * @see TaskMonitor
 * @see StatusBar
 */
public class DacsSwingDemo extends SingleFrameApplication {

    private static final Logger logger = Logger.getLogger(DacsSwingDemo.class.getName());

    private static DacsClientContext dacsClientContext = new DacsClientContext();
    private static String feduri = "https://fedroot.com/dacs";
    


    private JFrame mainFrame = null;
    private TextField federationURLTextField = null;
    private TextField urlTextField = null;
    private JCheckBox checkOnlyCheckBox = null;
    private JCheckBox enableEventHandlingCheckBox = null;
    private StatusBar statusBar = null;
    private BusyIndicator busyIndicator = null;

   private Federation currentFederation;
    private boolean checkOnly = false;
    private boolean enableEventHandling = false;

    @Override
    protected void startup() {
        try {
            FederationLoader federationLoader = new FederationLoader(feduri, dacsClientContext);
            currentFederation = federationLoader.getFederation();
            mainFrame = getMainFrame();
            statusBar = new StatusBar(this, getContext().getTaskMonitor());
            busyIndicator = new BusyIndicator();
            mainFrame.setJMenuBar(createMenuBar());
            mainFrame.add(createControlPanel(), BorderLayout.NORTH);
            mainFrame.add(createActionsPanel(), BorderLayout.CENTER);
            mainFrame.add(statusBar, BorderLayout.SOUTH);
            mainFrame.setGlassPane(busyIndicator);
            show(mainFrame);
        } catch (DacsException ex) {
            Logger.getLogger(DacsSwingDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DacsSwingDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ActionMap actionMap() {
        return getContext().getActionMap();
    }

    private JMenu createMenu(String menuName, String[] actionNames) {
        JMenu menu = new JMenu();
        menu.setName(menuName);
        for (String actionName : actionNames) {
            JMenuItem menuItem = new JMenuItem();
            menuItem.setAction(actionMap().get(actionName));
            menu.add(menuItem);
        }
        return menu;
    }

    private JMenuBar createMenuBar() {
        String[] menuActionNames = {
            "openAction",
            "helpAction",
            "aboutAction",
            "quit"
        };
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenu("mainMenu", menuActionNames));
        return menuBar;
    }

    // TODO pass in the global text field, etc
    private JPanel createControlPanel() {
        JPanel settingsPanel = new JPanel(new BorderLayout());
        // TODO ad the infoPanel as a listener to the loadFederation action

        JPanel federationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel federationUriLabel = new JLabel("Federation URI:");
        federationPanel.add(federationUriLabel);

        federationURLTextField = new TextField(40);
        federationURLTextField.setEditable(true);

        JButton loadFederationButton = new JButton(actionMap().get("loadFederation"));

        federationPanel.add(federationURLTextField);
        federationPanel.add(loadFederationButton);

        JPanel jurisdictionsPanel = new JPanel(new FlowLayout());
        JLabel jurisdictionsLabel = new JLabel("Jurisdictions");
        jurisdictionsPanel.add(jurisdictionsLabel);

        JPanel modifiersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        checkOnlyCheckBox = new JCheckBox(actionMap().get("toggleCheckOnly"));
        enableEventHandlingCheckBox = new JCheckBox(actionMap().get("toggleEnableEventHandling"));
        modifiersPanel.add(checkOnlyCheckBox);
        modifiersPanel.add(enableEventHandlingCheckBox);
        
        settingsPanel.add(federationPanel,BorderLayout.NORTH);
        settingsPanel.add(jurisdictionsPanel, BorderLayout.CENTER);
        settingsPanel.add(modifiersPanel, BorderLayout.SOUTH);

        return settingsPanel;
    }

    private JPanel createGotoPanel() {

        JPanel gotoUrlPanel = new JPanel(new FlowLayout());

        JLabel urlLabel = new JLabel("URL:");
        gotoUrlPanel.add(urlLabel);

        urlTextField = new TextField(70);
        urlTextField.setEditable(true);
        gotoUrlPanel.add(urlTextField);

        JButton gotoButton = new JButton(actionMap().get("followUrl"));

//        JButton gotoButton = new JButton();
//        gotoButton.addActionListener(
//                new ActionListener() {
//
//                    @Override
//                    public void actionPerformed(ActionEvent ae) {
//                        try {
//                            URL url = new URL(urlTextField.getText().trim());
//                            followUrl(url.toURI(), false, false);
//                        } catch (Exception ex) {
//                            showErrorDialog("Invalid or empty URL", ex);
//                        }
//                    }
//                });
//        gotoButton.setRequestFocusEnabled(false);
//        gotoButton.setAction(actionMap().get("gotoURL"));
//        gotoButton.setVerticalTextPosition(JButton.BOTTOM);
//        gotoButton.setHorizontalTextPosition(JButton.CENTER);
//        gotoButton.setName("GOTO");

        gotoUrlPanel.add(gotoButton);

        return gotoUrlPanel;
    }

    private JComponent createActionsPanel() {
        JButton clientCredentialsButton = new JButton(actionMap().get("clientCredentials"));
        JButton componentButton = new JButton(actionMap().get("blockComponent"));
        JButton applicationButton = new JButton(actionMap().get("blockApplication"));
        JButton windowButton = new JButton(actionMap().get("blockWindow"));


        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 50));
        panel1.add(clientCredentialsButton);
        panel1.add(componentButton);
        panel1.add(applicationButton);
        panel1.add(windowButton);

        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.add(createGotoPanel(), BorderLayout.NORTH);
        panel2.add(new JSeparator(), BorderLayout.CENTER);
        panel2.add(panel1, BorderLayout.SOUTH);
        panel2.setBorder(new EmptyBorder(0, 2, 0, 2)); // top, left, bottom, right
        return panel2;
    }

    /* Progress is interdeterminate for the first 150ms, then
     * run for another 7500ms, marking progress every 150ms.
     */
    private class DoNothingTask extends Task<Void, Void> {

        DoNothingTask() {
            super(DacsSwingDemo.this);
            setUserCanCancel(true);
        }

        @Override
        protected Void doInBackground() throws InterruptedException {
            for (int i = 0; i < 50; i++) {
                setMessage("Working... [" + i + "]");
                Thread.sleep(150L);
                setProgress(i, 0, 49);
            }
            Thread.sleep(150L);
            return null;
        }

        @Override
        protected void succeeded(Void ignored) {
            setMessage("Done");
        }

        @Override
        protected void cancelled() {
            setMessage("Canceled");
        }
    }

    /* Progress is interdeterminate for the first 150ms, then
     * run for another 7500ms, marking progress every 150ms.
     */
    private class FollowUrlTask extends Task<Void, Void> {

        FollowUrlTask() {
            super(DacsSwingDemo.this);
            setUserCanCancel(true);
        }

        @Override
        protected Void doInBackground() throws InterruptedException {
            try {
                URL url = new URL(urlTextField.getText().trim());
                followUrl(url.toURI(), false, false);
            } catch (Exception ex) {
                showErrorDialog("Invalid or empty URL", ex);
            }
//            for (int i = 0; i < 50; i++) {
//                setMessage("Working... [" + i + "]");
//                Thread.sleep(150L);
//                setProgress(i, 0, 49);
//            }
//            Thread.sleep(150L);
            return null;
        }
                @Override
        protected void succeeded(Void ignored) {
            setMessage("Done");
        }

        @Override
        protected void cancelled() {
            setMessage("Canceled");
        }
    }

    /* Progress is interdeterminate for the first 150ms, then
     * run for another 7500ms, marking progress every 150ms.
     */
    private class LoadFederationTask extends Task<Void, Void> {

        LoadFederationTask() {
            super(DacsSwingDemo.this);
            setUserCanCancel(true);
        }

        @Override
        protected Void doInBackground() throws InterruptedException {
            String federationURI = federationURLTextField.getText().trim();
            try {
                URL url = new URL(federationURI);

                FederationLoader federationLoader = new FederationLoader(federationURI, dacsClientContext);
                currentFederation = federationLoader.getFederation();
                // TODO make main frame an event listener for current federation changed events
                getMainFrame().setTitle("Browsing Federation " + currentFederation.getFederationName());
            } catch (DacsException ex) {
                showErrorDialog("Failed to load federation at URL " + federationURI, ex);
                Logger.getLogger(DacsSwingDemo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                showErrorDialog("Invalid or empty URL", ex);
            }
//            for (int i = 0; i < 50; i++) {
//                setMessage("Working... [" + i + "]");
//                Thread.sleep(150L);
//                setProgress(i, 0, 49);
//            }
//            Thread.sleep(150L);
            return null;
        }


        @Override
        protected void succeeded(Void ignored) {
            setMessage("Loaded federation " + currentFederation.getFederationName());
        }

        @Override
        protected void cancelled() {
            setMessage("Canceled");
        }
    }

    @Action(block = BlockingScope.ACTION)
    public Task openAction() {
        return new DoNothingTask();
    }

    @Action(block = BlockingScope.ACTION)
    public Task helpAction() {
        return new DoNothingTask();
    }

    @Action(block = BlockingScope.ACTION)
    public Task aboutAction() {
        return new DoNothingTask();
    }

    @Action(block = BlockingScope.COMPONENT)
    public Task followUrl() {
        Task task = new FollowUrlTask();
        return task;
    }

   @Action(block = BlockingScope.COMPONENT)
    public Task loadFederation() {
        Task task = new LoadFederationTask();
        return task;
    }

    @Action(block = BlockingScope.APPLICATION)
    public Task blockApplication() {
        Task task = new DoNothingTask();
        task.setInputBlocker(new BusyIndicatorInputBlocker(task));
        return task;
    }

    @Action(selectedProperty = "checkOnly") public void toggleCheckOnly() {
    }

    @Action(selectedProperty = "enableEventHandling") public void toggleEnableEventHandling() {
    }

    public boolean isCheckOnly() {
        return checkOnly;
    }

    public void setCheckOnly(boolean selected) {
        boolean oldValue = this.checkOnly;
        this.checkOnly = selected;
        firePropertyChange("checkOnly", oldValue, this.checkOnly);
        ApplicationAction cba = (ApplicationAction)checkOnlyCheckBox.getAction();
//        String msg =
//            String.format("%s.setSelected(%s)\n", getClass().getName(), this.selected) +
//            String.format("checkBox.getAction().isSelected() %s\n", cba.isSelected()) +
//            String.format("checkBox.isSelected() %s\n", checkBox.isSelected()) +
//            String.format("radioButton.isSelected() %s\n", radioButton.isSelected());
//        textArea.append(msg + "\n");
    }

    public boolean isEnableEventHandling() {
        return enableEventHandling;
    }

    public void setEnableEventHandling(boolean selected) {
        boolean oldValue = this.checkOnly;
        this.enableEventHandling = selected;
        firePropertyChange("enableEventHandling", oldValue, this.enableEventHandling);
        ApplicationAction cba = (ApplicationAction)enableEventHandlingCheckBox.getAction();
//        String msg =
//            String.format("%s.setSelected(%s)\n", getClass().getName(), this.selected) +
//            String.format("checkBox.getAction().isSelected() %s\n", cba.isSelected()) +
//            String.format("checkBox.isSelected() %s\n", checkBox.isSelected()) +
//            String.format("radioButton.isSelected() %s\n", radioButton.isSelected());
//        textArea.append(msg + "\n");
    }

    /**
     * execute DacsGetRequest for a URL, handling DACS response status
     * @param uri
     */
    // TODO move followURL to fedutils
    private synchronized void followUrl(URI uri, boolean checkOnly, boolean enableEventHandling) {
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

    private void showErrorDialog(String message, Exception e) {
        String title = "Error";
        int type = JOptionPane.ERROR_MESSAGE;
        message = "Error: " + message;
        JOptionPane.showMessageDialog(getMainFrame(), message, title, type);
    }

    public static void main(String[] args) {
        Application.launch(DacsSwingDemo.class, args);
    }

    /* This component is intended to be used as a GlassPane.  It's
     * start method makes this component visible, consumes mouse
     * and keyboard input, and displays a spinning activity indicator 
     * animation.  The stop method makes the component not visible.
     * The code for rendering the animation was lifted from 
     * org.jdesktop.swingx.painter.BusyPainter.  I've made some
     * simplifications to keep the example small.
     */
    private static class BusyIndicator extends JComponent implements ActionListener {

        private int frame = -1;  // animation frame index
        private final int nBars = 8;
        private final float barWidth = 6;
        private final float outerRadius = 28;
        private final float innerRadius = 12;
        private final int trailLength = 4;
        private final float barGray = 200f;  // shade of gray, 0-255
        private final Timer timer = new Timer(65, this); // 65ms = animation rate

        BusyIndicator() {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            MouseInputListener blockMouseEvents = new MouseInputAdapter() {
            };
            addMouseMotionListener(blockMouseEvents);
            addMouseListener(blockMouseEvents);
            InputVerifier retainFocusWhileVisible = new InputVerifier() {

                public boolean verify(JComponent c) {
                    return !c.isVisible();
                }
            };
            setInputVerifier(retainFocusWhileVisible);
        }

        public void actionPerformed(ActionEvent ignored) {
            frame += 1;
            repaint();
        }

        void start() {
            setVisible(true);
            requestFocusInWindow();
            timer.start();
        }

        void stop() {
            setVisible(false);
            timer.stop();
        }

        @Override
        protected void paintComponent(Graphics g) {
            RoundRectangle2D bar = new RoundRectangle2D.Float(
                    innerRadius, -barWidth / 2, outerRadius, barWidth, barWidth, barWidth);
            // x,         y,          width,       height,   arc width,arc height
            double angle = Math.PI * 2.0 / (double) nBars; // between bars
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(getWidth() / 2, getHeight() / 2);
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i = 0; i < nBars; i++) {
                // compute bar i's color based on the frame index
                Color barColor = new Color((int) barGray, (int) barGray, (int) barGray);
                if (frame != -1) {
                    for (int t = 0; t < trailLength; t++) {
                        if (i == ((frame - t + nBars) % nBars)) {
                            float tlf = (float) trailLength;
                            float pct = 1.0f - ((tlf - t) / tlf);
                            int gray = (int) ((barGray - (pct * barGray)) + 0.5f);
                            barColor = new Color(gray, gray, gray);
                        }
                    }
                }
                // draw the bar
                g2d.setColor(barColor);
                g2d.fill(bar);
                g2d.rotate(angle);
            }
        }
    }

    private class BusyIndicatorInputBlocker extends InputBlocker {

        BusyIndicatorInputBlocker(Task task) {
            super(task, Task.BlockingScope.WINDOW, busyIndicator);
        }

        @Override
        protected void block() {
            busyIndicator.start();
        }

        @Override
        protected void unblock() {
            busyIndicator.stop();
        }
    }
}

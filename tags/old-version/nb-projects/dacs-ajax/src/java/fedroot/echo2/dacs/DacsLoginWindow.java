/*
 * DacsLoginWindow.java
 *
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2.dacs;

import fedroot.echo2.DacsAjaxApp;
import fedroot.echo2.MessageDialog;
import fedroot.echo2.Styles;
import fedroot.echo2.demo.Messages;
import java.util.List;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Grid;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.ListBox;
import nextapp.echo2.app.PasswordField;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SelectField;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

/**
 *
 * @author rmorriso
 */
public class DacsLoginWindow extends WindowPane {
    private static final Extent PX_300 = new Extent(300, Extent.PX);
    private final SelectField jurisdictionField = new SelectField(DacsAjaxApp.getApp().jurisdictionList());
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Grid layoutGrid = new Grid();
    private final Row controlRow = new Row();
    private final Button button = new Button(Messages.getString("LoginScreen.Login"), Styles.ICON_24_YES);
    
    private final SplitPane vsplitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(32));

    /**
     * Creates a new instance of DacsLoginWindow
     * Note: this relies on stylesheet configuration
     */
    public DacsLoginWindow() {
        super();
        setStyleName("Default");
        setInsets(new Insets(10, 5));
        setTitle(Messages.getString("LoginScreen.LoginWindowTitle"));
        setStyleName("LoginScreen.LoginWindow");
        setDefaultCloseOperation(WindowPane.DISPOSE_ON_CLOSE);
        add(vsplitPane);
        
        controlRow.setStyleName("ControlPane");
        vsplitPane.add(controlRow);
        
        button.setStyleName("ControlPane.Button");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processLogin();
            }
        });
        controlRow.add(button);
        
        layoutGrid.setStyleName("LoginScreen.LayoutGrid");
        vsplitPane.add(layoutGrid);
        
        // jurisdictionField list label
        Label label = new Label(Messages.getString("LoginScreen.PromptJurisdiction"));
        label.setStyleName("LoginScreen.Prompt");
        layoutGrid.add(label);
        
        // jurisdictionField SelectField
        jurisdictionField.setWidth(PX_300);
        jurisdictionField.setStyleName("Default");
        layoutGrid.add(jurisdictionField);
        
        // usernameField label
        label = new Label(Messages.getString("LoginScreen.PromptUsername"));
        label.setStyleName("LoginScreen.Prompt");
        layoutGrid.add(label);
        
        // usernameField textfield
        usernameField.setWidth(PX_300);
        usernameField.setStyleName("Default");
        usernameField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DacsAjaxApp.getActive().setFocusedComponent(passwordField);
            }
        });
        layoutGrid.add(usernameField);
        
        // passwordField label
        label = new Label(Messages.getString("LoginScreen.PromptPassword"));
        label.setStyleName("LoginScreen.Prompt");
        layoutGrid.add(label);
        
        // passwordField field
        passwordField.setWidth(PX_300);
        passwordField.setStyleName("Default");
        passwordField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processLogin();
            }
        });
        layoutGrid.add(passwordField);
        
        DacsAjaxApp.getActive().setFocusedComponent(usernameField);
        
        // block other actions until login is complete
        setModal(true);
    }
    
    /**
     * Processes a user log-in request.
     */
    private void processLogin() {
        String username = usernameField.getText().trim();
        usernameField.setText(username);
        String password = passwordField.getText();
        // validate input - require jurisdiction & username
        if (jurisdictionField.getSelectedItem() == null | username == null | username.length() == 0) {
            MessageDialog messageDialog = new MessageDialog(Messages.getString("LoginScreen.InvalidData.Title"),
                    Messages.getString("LoginScreen.InvalidData.Message"), MessageDialog.TYPE_ERROR, MessageDialog.CONTROLS_OK);
            getApplicationInstance().getDefaultWindow().getContent().add(messageDialog);
        } else {
            String jurisdiction = jurisdictionField.getSelectedItem().toString();
            // try login
            try {
                boolean login = DacsAjaxApp.getApp().login(jurisdiction, username, password);
                if (!login) {
                    MessageDialog messageDialog = new MessageDialog(Messages.getString("LoginScreen.InvalidLogin.Title"),
                            Messages.getString("LoginScreen.InvalidLogin.Message"), MessageDialog.TYPE_ERROR, MessageDialog.CONTROLS_OK);
                    getApplicationInstance().getDefaultWindow().getContent().add(messageDialog);
                } else {
                    getApplicationInstance().getDefaultWindow().getContent().remove(this);
                }
            } catch (Exception e) {
                MessageDialog messageDialog = new MessageDialog(Messages.getString("LoginScreen.InvalidLogin.Title"),
                        Messages.getString("LoginScreen.InvalidLogin.Message"), MessageDialog.TYPE_ERROR, MessageDialog.CONTROLS_OK);
                getApplicationInstance().getDefaultWindow().getContent().add(messageDialog);
            }
        }
    }
}

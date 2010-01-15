/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2.dacs;

import nextapp.echo2.app.Alignment;
import nextapp.echo2.app.Border;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Grid;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.MutableStyle;
import nextapp.echo2.app.Style;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.layout.GridLayoutData;
import nextapp.echo2.app.layout.SplitPaneLayoutData;
import nextapp.echo2.webcontainer.ContainerContext;
import nextapp.echo2.webrender.ClientConfiguration;
import nextapp.echo2.webrender.WebRenderServlet;

/**
 * Interactive test for client configuration settings.
 */
public class ConfigurationColumn extends Column {

    private static final Style PROMPT_STYLE;
    static {
        MutableStyle style = new MutableStyle();
        GridLayoutData layoutData = new GridLayoutData();
        layoutData.setAlignment(new Alignment(Alignment.RIGHT, Alignment.TOP));
        style.setProperty(PROPERTY_LAYOUT_DATA, layoutData);
        PROMPT_STYLE = style;
    }

    private TextField dacs_baseuri, serverErrorMessageText, sessionExpirationUriText, sessionExpirationMessageText;
    
    /**
     * Default constructor. 
     */
    public ConfigurationColumn() {
        super();
        SplitPaneLayoutData splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setInsets(new Insets(10));
        setLayoutData(splitPaneLayoutData);
        setCellSpacing(new Extent(20));
        
        Grid paramgrid = new Grid(3);
        paramgrid.setBorder(new Border(2, Color.BLUE, Border.STYLE_GROOVE));
        paramgrid.setInsets(new Insets(10, 5));
        add(paramgrid);
        
        Grid datagrid = new Grid(2);
//        datagrid.setBorder(new Border(2, Color.BLUE, Border.STYLE_GROOVE));
        datagrid.setInsets(new Insets(10, 5));
        add(datagrid);
        
        Label label;
        
        label = new Label("Federation DACS Uri:");
        label.setStyle(PROMPT_STYLE);
        paramgrid.add(label);
        
        dacs_baseuri = new TextField();
        dacs_baseuri.setStyleName("Default");
        dacs_baseuri.setToolTipText("enter DACS base uri for Federation");
        paramgrid.add(dacs_baseuri);
        
        Button updateButton = new Button("Update");
        updateButton.setStyleName("Default");
        updateButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                updateConfiguration();
            }
        });
        paramgrid.add(updateButton);
        
        label = new Label("Federation Name:");
        label.setStyle(PROMPT_STYLE);
        datagrid.add(label);
        
        serverErrorMessageText = new TextField();
        serverErrorMessageText.setStyleName("Default");
        serverErrorMessageText.setEnabled(false);
        datagrid.add(serverErrorMessageText);
        
        
        label = new Label("Federation Domain:");
        label.setStyle(PROMPT_STYLE);
        datagrid.add(label);
        
        sessionExpirationUriText = new TextField();
        sessionExpirationUriText.setStyleName("Default");
        sessionExpirationUriText.setEnabled(false);
        datagrid.add(sessionExpirationUriText);
                        
    }
    
    /**
     * Performs <code>ClientConfigurationUpdate</code>.
     */
    private void updateConfiguration() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        if (dacs_baseuri.getText().trim().length() > 0) {
            clientConfiguration.setProperty(ClientConfiguration.PROPERTY_SERVER_ERROR_URI, dacs_baseuri.getText());
        }
        if (serverErrorMessageText.getText().trim().length() > 0) {
            clientConfiguration.setProperty(ClientConfiguration.PROPERTY_SERVER_ERROR_MESSAGE, serverErrorMessageText.getText());
        }
        if (sessionExpirationUriText.getText().trim().length() > 0) {
            clientConfiguration.setProperty(ClientConfiguration.PROPERTY_SESSION_EXPIRATION_URI, 
                    sessionExpirationUriText.getText());
        }
        if (sessionExpirationMessageText.getText().trim().length() > 0) {
            clientConfiguration.setProperty(ClientConfiguration.PROPERTY_SESSION_EXPIRATION_MESSAGE, 
                    sessionExpirationMessageText.getText());
        }
        
        ContainerContext containerContext 
                = (ContainerContext) getApplicationInstance().getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
        containerContext.setClientConfiguration(clientConfiguration);
    }
}

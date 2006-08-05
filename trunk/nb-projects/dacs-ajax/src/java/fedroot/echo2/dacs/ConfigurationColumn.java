/* 
 * This file is part of the Echo Web Application Framework (hereinafter "Echo").
 * Copyright (C) 2002-2005 NextApp, Inc.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
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

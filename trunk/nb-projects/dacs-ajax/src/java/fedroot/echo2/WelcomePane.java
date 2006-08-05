/* 
 * This file is part of the Echo2 Extras Project.
 * Copyright (C) 2005-2006 NextApp, Inc.
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

package fedroot.echo2;

import nextapp.echo2.app.Alignment;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Font;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.layout.ColumnLayoutData;
import nextapp.echo2.app.layout.SplitPaneLayoutData;

/**
 * <code>ContentPane</code> which displays a welcome/instruction message to 
 * users when they initially visit the application.
 */
public class WelcomePane extends ContentPane {
    
    /**
     * Default constructor.
     */
    public WelcomePane() {
        super();
        setStyleName("WelcomePane");
        
        Label label;

        Column column = new Column();
        column.setStyleName("WelcomePane.Column");
        add(column);
        
        label = new Label(Styles.FEDROOT_LOGO);
        column.add(label);
        
        label = new Label(Styles.ECHO2_IMAGE);
        column.add(label);
        
        label = new Label(Styles.LABEL1_IMAGE);
        column.add(label);
        
        WindowPane loginWindow = new WindowPane();
        loginWindow.setTitle("Welcome to the DACS Ajax Demonstration Application");
        loginWindow.setStyleName("WelcomePane");
        loginWindow.setClosable(false);
        add(loginWindow);
        
        SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(32));
        loginWindow.add(splitPane);
        
        Row controlRow = new Row();
        controlRow.setStyleName("ControlPane");
        splitPane.add(controlRow);
        
        Button button = new Button("Continue", Styles.ICON_24_YES);
        button.setStyleName("ControlPane.Button");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DacsAjaxApp.getApp().displayTestPane();
            }
        });
        controlRow.add(button);
        
        Column infoColumn = new Column();
        infoColumn.setInsets(new Insets(20, 5));
        infoColumn.setCellSpacing(new Extent(10));
        
        splitPane.add(infoColumn);
        
        label = new Label("About this application:");
        label.setFont(new Font(null, Font.BOLD, null));
        infoColumn.add(label);
        
        label = new Label("This application demonstrates DACS use in an Ajax setting. " +
                          "Ajax functionality is provided by the Echo2 Java rendering engine by NextApp.");
        label.setFont(new Font(null, Font.ITALIC, null));
        infoColumn.add(label);
        
        Row infoRow = new Row();
        infoRow.setCellSpacing(new Extent(8));
        
        label = new Label("Further information on DACS: ");
        label.setFont(new Font(null, Font.BOLD, null));
        infoRow.add(label);
        label = new Label("http://dacs.dss.ca");
        infoRow.add(label);
        
        infoColumn.add(infoRow);
        
        infoRow = new Row();
        infoRow.setCellSpacing(new Extent(8));
        
        label = new Label("Further information on Echo2:");
        label.setFont(new Font(null, Font.BOLD, null));
        infoRow.add(label);
        label = new Label("http://nextapp.com/platform/echo2");
        infoRow.add(label);
        
        infoColumn.add(infoRow);

        // insert spacing
        label = new Label();
        infoColumn.add(label);
        label = new Label();
        infoColumn.add(label);
        
        label = new Label("This application has been tested only under Mozilla Firefox 1.5+ " +
                          "and IE 6.0+ ... Earlier versions and other browsers have known issues.");
        label.setFont(new Font(null, Font.BOLD, null));
        infoColumn.add(label);
    }
}

/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2;

import nextapp.echo2.app.Button;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Font;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.layout.SplitPaneLayoutData;

/**
 * A <code>WindowPane</code> which contains an event console.
 */
public class ConsoleWindowPane extends WindowPane {
    
    private Column column;
    private ContentPane logPane;
    
    public ConsoleWindowPane() {
        super();
        setTitle("Console");
        setStyleName("Default");
        
        SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(32));
        add(splitPane);
        
        Row controlRow = new Row();
        controlRow.setStyleName("ControlPane");
        splitPane.add(controlRow);
        
        Button button = new Button("Clear", Styles.ICON_24_NO);
        button.setStyleName("ControlPane.Button");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                column.removeAll();
            }
        });
        controlRow.add(button);
        
        SplitPaneLayoutData splitPaneLayoutData;
        logPane = new ContentPane();
        logPane.setFont(new Font(Font.MONOSPACE, Font.PLAIN, new Extent(10)));
        logPane.setForeground(Color.GREEN);
        splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setBackground(Color.BLACK);
        logPane.setLayoutData(splitPaneLayoutData);
        splitPane.add(logPane);
        
        column = new Column();
        column.setInsets(new Insets(5));
        logPane.add(column);
    }
    
    public void writeMessage(String message) {
        column.add(new Label(message));
        logPane.setVerticalScroll(new Extent(-1));
    }
}

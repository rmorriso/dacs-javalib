/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2.dacs;

import com.fedroot.dacs.Credentials;
import fedroot.echo2.DacsAjaxApp;
import java.util.List;
import nextapp.echo2.app.Border;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.TextArea;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.layout.SplitPaneLayoutData;
import nextapp.echo2.app.text.StringDocument;

public class CredentialsPane extends SplitPane {
    
    public CredentialsPane() {
        super(SplitPane.ORIENTATION_VERTICAL, new Extent(250, Extent.PX));
        setStyleName("DefaultResizable");

        SplitPaneLayoutData splitPaneLayoutData;

        final Column testColumn = new Column();
        testColumn.setCellSpacing(new Extent(15));
        splitPaneLayoutData = new SplitPaneLayoutData();
        splitPaneLayoutData.setInsets(new Insets(15));
        testColumn.setLayoutData(splitPaneLayoutData);
        add(testColumn);
        
        Label testLabel = new Label("User Credentials");
        testColumn.add(testLabel);
        
        ButtonColumn controlsColumn = new ButtonColumn();
        controlsColumn.setStyleName("TestControlsColumn");
        add(controlsColumn);

        final TextArea textArea = new TextArea(new StringDocument(), null, 40, 8);
        textArea.setBorder(new Border(1, Color.BLUE, Border.STYLE_SOLID));
        testColumn.add(textArea);
        
        controlsColumn.addButton("Show Credentials", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread.sleep(3);
                } catch (InterruptedException ex) { }
                textArea.getDocument().setText(getCredentials());
            }
        });
        
        controlsColumn.addButton("De-select All", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread.sleep(3);
                } catch (InterruptedException ex) { }
                textArea.getDocument().setText("This will de-select all credentials.");
            }
        });
    }
    
    private String getCredentials() {
        StringBuffer strbuf = new StringBuffer("");
        List<Credentials> creds = DacsAjaxApp.getApp().getCredentials();
        for (Credentials cred : creds) {
            strbuf.append(cred.getFederation()
            + ":" + cred.getJurisdiction()
            + ":" + cred.getName());
        }
        return strbuf.toString();
    }
}

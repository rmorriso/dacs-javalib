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

import com.fedroot.dacs.Credentials;
import fedroot.echo2.DacsAjaxApp;
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
                StringBuffer strbuf = new StringBuffer("");
                for (Credentials cred : DacsAjaxApp.USERCONTEXT.getDacsCredentials()) {
                    strbuf.append(cred.toString());
                }
                textArea.getDocument().setText(strbuf.toString());
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
}

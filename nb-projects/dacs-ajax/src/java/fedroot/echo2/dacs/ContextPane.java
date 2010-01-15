/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2.dacs;

import fedroot.echo2.DacsAjaxApp;
import fedroot.echo2.StyleUtil;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
// import nextapp.echo2.testapp.interactive.StyleUtil;

/**
 * Interactive test module for <code>ConfigurationPane</code>s.
 */
public class ContextPane extends ContentPane {

    public ContextPane() {
        super();
        setStyleName("DefaultResizable");
        
        final ContentPane rootContentPane = DacsAjaxApp.getApp().getDefaultWindow().getContent();
        final Label contentLabel = new Label(StyleUtil.QUASI_LATIN_TEXT_1 + StyleUtil.QUASI_LATIN_TEXT_1);
        
        ContainerColumn cont = new ContainerColumn();
        cont.setStyleName("TestControlsColumn");
        add(cont);
        
    }
}

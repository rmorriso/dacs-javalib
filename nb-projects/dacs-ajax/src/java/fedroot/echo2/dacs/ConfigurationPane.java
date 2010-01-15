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

/**
 * Interactive test module for <code>ConfigurationPane</code>s.
 */
public class ConfigurationPane extends SplitPane {

    public ConfigurationPane() {
        super(SplitPane.ORIENTATION_VERTICAL, new Extent(250, Extent.PX));
        setStyleName("DefaultResizable");
        
        final ContentPane rootContentPane = DacsAjaxApp.getApp().getDefaultWindow().getContent();
        final Label contentLabel = new Label(StyleUtil.QUASI_LATIN_TEXT_1 + StyleUtil.QUASI_LATIN_TEXT_1);
        
        ConfigurationColumn confcol = new ConfigurationColumn();
        confcol.setStyleName("TestControlsColumn");
        add(confcol);
        
        
        final ContentPane testContentPane = new ContentPane();
        add(testContentPane);

    }
}

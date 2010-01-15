/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2.demo;

import nextapp.echo2.app.Color;
import nextapp.echo2.app.FillImageBorder;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.extras.app.BorderPane;
import fedroot.echo2.TabSplitPane;
import fedroot.echo2.StyleUtil;
import fedroot.echo2.Styles;
import fedroot.echo2.TestControlPane;

/**
 * Interactive test module for <code>BorderPane</code>s.
 */
public class BorderPaneTest extends TabSplitPane {

    public BorderPaneTest() {
        super("BorderPane", Styles.ICON_16_BORDER_PANE);
        final BorderPane borderPane = new BorderPane();
        borderPane.setStyleName("Shadow");
        add(borderPane);
        setTestComponent(this, borderPane);
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Small Label", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borderPane.removeAll();
                borderPane.add(new Label("Child Component"));
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Large Label", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borderPane.removeAll();
                borderPane.add(new Label(StyleUtil.QUASI_LATIN_TEXT_1));
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "BorderPaneTest", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borderPane.removeAll();
                borderPane.add(new BorderPaneTest());
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Clear Content", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borderPane.removeAll();
            }
        });
        
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "foreground");
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "background");
        
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Border = Null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borderPane.setBorder(null);
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Border = 20px/10px Red", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borderPane.setBorder(new FillImageBorder(Color.RED, new Insets(20), new Insets(10)));
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Style = Null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borderPane.setStyleName(null);
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Style = Shadow", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borderPane.setStyleName("Shadow");
            }
        });

        addStandardIntegrationTests();
    }
}

/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2.demo;

import nextapp.echo2.app.Color;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.extras.app.ColorSelect;
import fedroot.echo2.TabSplitPane;
import fedroot.echo2.DacsAjaxApp;
import fedroot.echo2.StyleUtil;
import fedroot.echo2.Styles;
import fedroot.echo2.TestControlPane;

/**
 * Interactive test module for <code>ColorSelect</code>s.
 */
public class ColorSelectTest extends TabSplitPane {
    
    private static final Extent[] EXTENT_VALUES = new Extent[]{null, new Extent(16), new Extent(32), new Extent(64), 
            new Extent(128), new Extent(256), new Extent(512)};

    public ColorSelectTest() {
        super("ColorSelect", Styles.ICON_16_COLOR_SELECT);
        
        final ColorSelect colorSelect = new ColorSelect();
        add(colorSelect);
        setTestComponent(this, colorSelect);
        
        // Properties

        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Query Color", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = colorSelect.getColor();
                DacsAjaxApp.getApp().consoleWrite("Color: " + color == null ? "null" : color.toString());
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Color", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorSelect.setColor(StyleUtil.randomColor());
            }
        });
        
        addExtentPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "hueWidth", EXTENT_VALUES);
        addExtentPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "saturationHeight", EXTENT_VALUES);
        addExtentPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "valueWidth", EXTENT_VALUES);
        addBooleanPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "displayValue");
        
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Add ColorSelect WindowPane", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WindowPane windowPane = new WindowPane("Color Select Test", new Extent(250), new Extent(270));
                windowPane.setPositionX(new Extent((int) (Math.random() * 500)));
                windowPane.setPositionY(new Extent((int) (Math.random() * 300) + 140));
                windowPane.setStyleName("Default");
                windowPane.setInsets(new Insets(10, 5));
                windowPane.add(new ColorSelect(StyleUtil.randomColor()));
                DacsAjaxApp.getApp().getDefaultWindow().getContent().add(windowPane);
            }
        });

        // Integration
        
        addStandardIntegrationTests();
    }
}

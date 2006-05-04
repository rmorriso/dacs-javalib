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

package fedroot.echo2.demo;

import nextapp.echo2.app.Color;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.extras.app.ColorSelect;
import fedroot.echo2.AbstractTest;
import fedroot.echo2.DacsAjaxApp;
import fedroot.echo2.StyleUtil;
import fedroot.echo2.Styles;
import fedroot.echo2.TestControlPane;

/**
 * Interactive test module for <code>ColorSelect</code>s.
 */
public class ColorSelectTest extends AbstractTest {
    
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

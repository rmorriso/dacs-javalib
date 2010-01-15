/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2;

import nextapp.echo2.app.Border;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Grid;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.layout.GridLayoutData;

/**
 * A layout component based on a <code>Grid</code> which provides a two column
 * layout, with labels in each left-hand cell describing test content in the
 * right-hand cells. 
 */
public class TestGrid extends Grid {
    
    private static final Color HEADER_CELL_BACKGROUND = new Color(0x9f9fcf);
    private static final Color DESCRIPTOR_CELL_BACKGROUND = new Color(0xffffcf);
    private static final Border BORDER = new Border(2, new Color(0xcfcfff), Border.STYLE_GROOVE);
    
    /**
     * Creates a new <code>TestGrid</code>.
     */
    public TestGrid() {
        super(2);
        setInsets(new Insets(10, 5));
        setBorder(BORDER);
    }
    
    /**
     * Adds a header row to the <code>TestGrid</code>.
     * 
     * @param text the header text
     */
    public void addHeaderRow(String text) {
        Label label = new Label(text);
        GridLayoutData layoutData = new GridLayoutData();
        layoutData.setBackground(HEADER_CELL_BACKGROUND);
        layoutData.setColumnSpan(2);
        label.setLayoutData(layoutData);
        add(label);
    }
    
    /**
     * Adds a test item row cell to the <code>TestGrid</code>
     * 
     * @param descriptor a description of the item
     * @param testComponent the item <code>Component</code>
     */
    public void addTestRow(String descriptor, Component testComponent) {
        Label label = new Label(descriptor);
        GridLayoutData layoutData = new GridLayoutData();
        layoutData.setBackground(DESCRIPTOR_CELL_BACKGROUND);
        label.setLayoutData(layoutData);
        add(label);
        add(testComponent);
    }

}

/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2.demo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.extras.app.CalendarSelect;
import fedroot.echo2.TabSplitPane;
import fedroot.echo2.DacsAjaxApp;
import fedroot.echo2.StyleUtil;
import fedroot.echo2.Styles;
import fedroot.echo2.TestControlPane;

/**
 * Interactive test module for <code>CalendarSelect</code>s.
 */
public class CalendarSelectTest extends TabSplitPane {

    public CalendarSelectTest() {
        super("CalendarSelect", Styles.ICON_16_CALENDAR_SELECT);
        setStyleName("DefaultResizable");
        
        final CalendarSelect calendarSelect = new CalendarSelect();
        setTestComponent(this, calendarSelect);
        add(calendarSelect);
        
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, CalendarSelect.PROPERTY_FOREGROUND);
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, CalendarSelect.PROPERTY_BACKGROUND);
        addFillImagePropertyTests(TestControlPane.CATEGORY_PROPERTIES, CalendarSelect.PROPERTY_BACKGROUND_IMAGE, 
                StyleUtil.TEST_FILL_IMAGES);
        addFontPropertyTests(TestControlPane.CATEGORY_PROPERTIES, CalendarSelect.PROPERTY_FOREGROUND);
        addBorderPropertyTests(TestControlPane.CATEGORY_PROPERTIES, CalendarSelect.PROPERTY_BORDER);

        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Query Date", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date date = calendarSelect.getDate();
                DacsAjaxApp.getApp().consoleWrite(date == null ? "No Date" : date.toString());
            }
        });
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Date", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Calendar calendar = new GregorianCalendar();
                calendar.add(Calendar.DAY_OF_MONTH, ((int) (Math.random() * 10000)) - 5000);
                calendarSelect.setDate(calendar.getTime());
            }
        });
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Add CalendarSelect WindowPane", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WindowPane windowPane = new WindowPane("Calendar Select Test", new Extent(240), new Extent(225));
                windowPane.setPositionX(new Extent((int) (Math.random() * 500)));
                windowPane.setPositionY(new Extent((int) (Math.random() * 300) + 140));
                windowPane.setStyleName("Default");
                windowPane.setInsets(new Insets(10, 5));
                CalendarSelect calendarSelect = new CalendarSelect();
                calendarSelect.setBackgroundImage(Styles.FILL_IMAGE_LIGHT_BLUE_LINE);
                windowPane.add(calendarSelect);
                DacsAjaxApp.getApp().getDefaultWindow().getContent().add(windowPane);
            }
        });
        
        addStandardIntegrationTests();
    }
}

/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2;

import nextapp.echo2.app.FillImage;
import nextapp.echo2.app.ImageReference;
import nextapp.echo2.app.ResourceImageReference;
import nextapp.echo2.app.StyleSheet;
import nextapp.echo2.app.componentxml.ComponentXmlException;
import nextapp.echo2.app.componentxml.StyleSheetLoader;

/**
 * 
 */
public class Styles {
    
    public static final String FEDROOT_IMAGE_PATH = "/fedroot/echo2/resource/image/";
    public static final String FEDROOT_STYLE_PATH = "/fedroot/echo2/resource/style/";
    public static final String IMAGE_PATH = "/echo2-extras/resource/image/";
    public static final String STYLE_PATH = "/echo2-extras/resource/style/"; 
    public static final ImageReference ICON_16_ACCORDION_PANE 
            = new ResourceImageReference(IMAGE_PATH + "AccordionPaneIcon16.gif"); 
    public static final ImageReference ICON_16_BORDER_PANE 
            = new ResourceImageReference(IMAGE_PATH + "BorderPaneIcon16.gif"); 
    public static final ImageReference ICON_16_CALENDAR_SELECT 
            = new ResourceImageReference(IMAGE_PATH + "CalendarSelectIcon16.gif"); 
    public static final ImageReference ICON_16_COLOR_SELECT
            = new ResourceImageReference(IMAGE_PATH + "ColorSelectIcon16.gif"); 
    public static final ImageReference ICON_16_MENU_BAR_PANE 
            = new ResourceImageReference(IMAGE_PATH + "MenuBarPaneIcon16.gif"); 
    public static final ImageReference ICON_16_TAB_PANE 
            = new ResourceImageReference(IMAGE_PATH + "TabPaneIcon16.gif"); 

    public static final FillImage FILL_IMAGE_SHADOW_BACKGROUND_DARK_BLUE = new FillImage(
            new ResourceImageReference(IMAGE_PATH + "ShadowBackgroundDarkBlue.png"));
    public static final FillImage FILL_IMAGE_SHADOW_BACKGROUND_LIGHT_BLUE = new FillImage(
            new ResourceImageReference(IMAGE_PATH + "ShadowBackgroundLightBlue.png"));
    public static final FillImage FILL_IMAGE_PEWTER_LINE = new FillImage(
            new ResourceImageReference(IMAGE_PATH + "PewterLineBackground.png"));
    public static final FillImage FILL_IMAGE_SILVER_LINE = new FillImage(
            new ResourceImageReference(IMAGE_PATH + "SilverLineBackground.png"));
    public static final FillImage FILL_IMAGE_LIGHT_BLUE_LINE = new FillImage(
            new ResourceImageReference(IMAGE_PATH + "LightBlueLineBackground.png"));

    public static final FillImage FILL_IMAGE_EXTRAS_BACKGROUND = new FillImage(
            new ResourceImageReference(IMAGE_PATH + "ExtrasBackground.jpg"));
    
    public static final ImageReference ICON_24_NO = new ResourceImageReference(IMAGE_PATH + "Icon24No.gif"); 
    public static final ImageReference ICON_24_YES = new ResourceImageReference(IMAGE_PATH + "Icon24Yes.gif");
    public static final ImageReference ECHO2_IMAGE = new ResourceImageReference(IMAGE_PATH + "Echo2.png");
    public static final ImageReference INTERACTIVE_TEST_APPLICATION_IMAGE 
            = new ResourceImageReference(IMAGE_PATH + "InteractiveTestApplication.png");
    public static final ImageReference NEXTAPP_LOGO = new ResourceImageReference(IMAGE_PATH + "NextAppLogo.png");
    
    public static final ImageReference ICON_LOGO =  new ResourceImageReference(IMAGE_PATH + "Logo.png");

    public static final StyleSheet DEFAULT_STYLE_SHEET;
    static {
        try {
            DEFAULT_STYLE_SHEET = StyleSheetLoader.load(STYLE_PATH + "Default.stylesheet", 
                    Thread.currentThread().getContextClassLoader());
        } catch (ComponentXmlException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /*
     * Fedroot Styles
     */
    public static final ImageReference FEDROOT_LOGO = new ResourceImageReference(FEDROOT_IMAGE_PATH + "fedroot.gif");
    public static final ImageReference LABEL1_IMAGE  
            = new ResourceImageReference(FEDROOT_IMAGE_PATH + "demo-label.png");
    public static final ImageReference ICON_LOGIN = new ResourceImageReference(FEDROOT_IMAGE_PATH + "login.png"); 
    public static final StyleSheet FEDROOT_STYLE_SHEET;
    static {
        try {
            FEDROOT_STYLE_SHEET = StyleSheetLoader.load(FEDROOT_STYLE_PATH + "fedroot.stylesheet", 
                    Thread.currentThread().getContextClassLoader());
        } catch (ComponentXmlException ex) {
            throw new RuntimeException(ex);
        }
    }
}

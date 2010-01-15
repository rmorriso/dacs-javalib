/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2.demo;

import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.FillImage;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.extras.app.MenuBarPane;
import nextapp.echo2.extras.app.menu.DefaultMenuStateModel;
import nextapp.echo2.extras.app.menu.DefaultOptionModel;
import nextapp.echo2.extras.app.menu.DefaultMenuModel;
import nextapp.echo2.extras.app.menu.DefaultRadioOptionModel;
import nextapp.echo2.extras.app.menu.DefaultToggleOptionModel;
import nextapp.echo2.extras.app.menu.MenuModel;
import nextapp.echo2.extras.app.menu.MenuStateModel;
import nextapp.echo2.extras.app.menu.SeparatorModel;
import fedroot.echo2.TabSplitPane;
import fedroot.echo2.DacsAjaxApp;
import fedroot.echo2.StyleUtil;
import fedroot.echo2.Styles;
import fedroot.echo2.TestControlPane;

/**
 * Interactive test module for <code>MenuBarPane</code>s.
 */
public class MenuBarPaneTest extends TabSplitPane {
    
    private static final Extent DEFAULT_MENU_HEIGHT = new Extent(26);
    private static final FillImage[] TEST_FILL_IMAGES = new FillImage[] { null, 
            Styles.FILL_IMAGE_SHADOW_BACKGROUND_DARK_BLUE, Styles.FILL_IMAGE_SHADOW_BACKGROUND_LIGHT_BLUE,
            Styles.FILL_IMAGE_PEWTER_LINE, Styles.FILL_IMAGE_LIGHT_BLUE_LINE,
            Styles.FILL_IMAGE_SILVER_LINE};

    public MenuBarPaneTest() {
        super("MenuBarPane", Styles.ICON_16_MENU_BAR_PANE);
        
        SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL, DEFAULT_MENU_HEIGHT);
        splitPane.setStyleName("DefaultResizable");
        add(splitPane);
        
        final MenuBarPane menu = new MenuBarPane(createMenuModel());
        MenuStateModel stateModel = new DefaultMenuStateModel();
        stateModel.setSelected("abc", true);
        stateModel.setEnabled("disabled1", false);
        stateModel.setEnabled("disabled2", false);
        menu.setStateModel(stateModel);
        menu.addActionListener(new ActionListener(){
        
            public void actionPerformed(ActionEvent e) {
                DacsAjaxApp.getApp().consoleWrite("Menu action: menu=" + e.getSource() + ", command=" + e.getActionCommand());
            }
        });
        splitPane.add(menu);
        
        setTestComponent(splitPane, menu);

        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "foreground");
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "background");
        addBorderPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "border");
        addFillImagePropertyTests(TestControlPane.CATEGORY_PROPERTIES, "backgroundImage", TEST_FILL_IMAGES);
        addFontPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "font");
        
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "menuForeground");
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "menuBackground");
        addFillImagePropertyTests(TestControlPane.CATEGORY_PROPERTIES, "menuBackgroundImage", TEST_FILL_IMAGES);
        addBorderPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "menuBorder");
        
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "selectionForeground");
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "selectionBackground");
        addFillImagePropertyTests(TestControlPane.CATEGORY_PROPERTIES, "selectionBackgroundImage", TEST_FILL_IMAGES);
        
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Add Test WindowPane", new ActionListener(){
        
            public void actionPerformed(ActionEvent e) {
                ContentPane rootContent = getApplicationInstance().getDefaultWindow().getContent();
                WindowPane windowPane = new WindowPane();
                windowPane.setTitle("Menu Test Window");
                windowPane.setStyleName("Default");
                SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM, DEFAULT_MENU_HEIGHT);
                windowPane.add(splitPane);
                MenuBarPane menu = new MenuBarPane(createMenuModel());
                menu.addActionListener(new ActionListener() {
                
                    public void actionPerformed(ActionEvent e) {
                        DacsAjaxApp.getApp().consoleWrite("Menu action: menu=" + e.getSource() 
                                + ", command=" + e.getActionCommand());
                    }
                });
                splitPane.add(menu);
                splitPane.add(new Label(StyleUtil.QUASI_LATIN_TEXT_1));
                rootContent.add(windowPane);
            }
        });
        
        addStandardIntegrationTests();
    }
    
    private MenuModel createMenuModel() {
        DefaultMenuModel menuModel = new DefaultMenuModel();
        
        DefaultMenuModel fileMenuModel = new DefaultMenuModel(null, "File");
        fileMenuModel.addItem(new DefaultOptionModel("new", "New", null));
        fileMenuModel.addItem(new DefaultOptionModel("open", "Open", null));
        DefaultMenuModel openRecentMenuModel = new DefaultMenuModel(null, "Open Recent");
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-1", "Hotel.pdf", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-2", "Alpha.txt", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-3", "q4-earnings.txt", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-4", "Bravo.odt", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-5", "Golf.pdf", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-6", "Alpha.txt", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-7", "q3-earnings.txt", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-8", "Charlie.odt", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-9", "XYZ.pdf", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-10", "Delta.txt", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-11", "q1-earnings.txt", null));
        openRecentMenuModel.addItem(new DefaultOptionModel("open-recent-12", "Foxtrot.odt", null));
        fileMenuModel.addItem(openRecentMenuModel);
        fileMenuModel.addItem(new SeparatorModel());
        fileMenuModel.addItem(new DefaultOptionModel("save", "Save", null));
        fileMenuModel.addItem(new DefaultOptionModel("save-as", "Save as...", null));
        menuModel.addItem(fileMenuModel);
        
        DefaultMenuModel optionsMenuModel = new DefaultMenuModel(null, "Options");
        optionsMenuModel.addItem(new DefaultOptionModel("load-preferences", "Load Preferences...", null));
        optionsMenuModel.addItem(new DefaultOptionModel("save-preferences", "Save Preferences...", null));
        optionsMenuModel.addItem(new SeparatorModel());
        optionsMenuModel.addItem(new DefaultToggleOptionModel("abc", "Enable ABC"));
        optionsMenuModel.addItem(new DefaultToggleOptionModel("def", "Enable DEF"));
        optionsMenuModel.addItem(new DefaultToggleOptionModel("ghi", "Enable GHI"));
        optionsMenuModel.addItem(new SeparatorModel());
        optionsMenuModel.addItem(new DefaultOptionModel("disabled1", "Disabled Option", null));
        optionsMenuModel.addItem(new DefaultToggleOptionModel("disabled2", "Disabled Toggle"));
        optionsMenuModel.addItem(new DefaultToggleOptionModel("def", "Enable DEF"));
        optionsMenuModel.addItem(new SeparatorModel());
        optionsMenuModel.addItem(new DefaultRadioOptionModel("foo1", "foomode", "Foo Mode 1"));
        optionsMenuModel.addItem(new DefaultRadioOptionModel("foo2", "foomode", "Foo Mode 2"));
        optionsMenuModel.addItem(new DefaultRadioOptionModel("foo3", "foomode", "Foo Mode 3"));
        optionsMenuModel.addItem(new DefaultRadioOptionModel("foo4", "foomode", "Foo Mode 4"));
        optionsMenuModel.addItem(new SeparatorModel());
        optionsMenuModel.addItem(new DefaultRadioOptionModel("bar1", "barmode", "Bar Mode 1"));
        optionsMenuModel.addItem(new DefaultRadioOptionModel("bar2", "barmode", "Bar Mode 2"));
        menuModel.addItem(optionsMenuModel);
        return menuModel;
    }
}

/*
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2;

import fedroot.echo2.dacs.DacsLoginWindow;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.FillImage;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.extras.app.MenuBarPane;
import nextapp.echo2.extras.app.TabPane;
import nextapp.echo2.extras.app.menu.AbstractMenuStateModel;
import nextapp.echo2.extras.app.menu.DefaultMenuModel;
import nextapp.echo2.extras.app.menu.DefaultOptionModel;
import nextapp.echo2.extras.app.menu.DefaultRadioOptionModel;
import nextapp.echo2.extras.app.menu.MenuStateModel;
import nextapp.echo2.extras.app.menu.SeparatorModel;

/**
 * Main InteractiveTest <code>ContentPane</code> which displays a menu
 * of available tests.
 */
public class TestPane extends ContentPane {
    
    private ActionListener commandActionListener = new ActionListener() {
        
        /**
         * @see nextapp.echo2.app.event.ActionListener#actionPerformed(nextapp.echo2.app.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == null) {
                DacsAjaxApp.getApp().displayWelcomePane();
            } else if (e.getActionCommand().equals("DacsLogin")) {
                DacsLoginWindow loginWindow = new DacsLoginWindow();
                DacsAjaxApp.getApp().getDefaultWindow().getContent().add(loginWindow);
            } else if (e.getActionCommand().equals("OpenConsole")) {
                DacsAjaxApp.getApp().consoleWrite(null);
            } else if (e.getActionCommand().equals("Reset")) {
                DacsAjaxApp.getApp().displayTestPane();
            } else if (e.getActionCommand().equals("Exit")) {
                DacsAjaxApp.getApp().displayWelcomePane();
            }
        }
    };
    
    private SplitPane menuVerticalPane;
    
    public TestPane() {
        super();
        
        setBackgroundImage(Styles.FILL_IMAGE_LIGHT_BLUE_LINE);
        
        DefaultMenuModel menuBarMenu = new DefaultMenuModel();
        
        DefaultMenuModel fileMenu = new DefaultMenuModel(null, "File");
        fileMenu.addItem(new DefaultOptionModel("DacsLogin", "DACS Login", null));
        fileMenu.addItem(new SeparatorModel());
        fileMenu.addItem(new DefaultOptionModel("Reset", "Reset", null));
        fileMenu.addItem(new DefaultOptionModel("Exit", "Exit", null));
        menuBarMenu.addItem(fileMenu);
        
        DefaultMenuModel editMenu = new DefaultMenuModel(null, "Edit");
        editMenu.addItem(new DefaultOptionModel("Cut", "Cut", null));
        editMenu.addItem(new DefaultOptionModel("Copy", "Copy", null));
        editMenu.addItem(new DefaultOptionModel("Paste", "Paste", null));
        editMenu.addItem(new DefaultOptionModel("Find", "Find", null));
        menuBarMenu.addItem(editMenu);
        
        DefaultMenuModel backgroundsMenu = new DefaultMenuModel("Backgrounds", "Backgrounds");
        backgroundsMenu.addItem(new DefaultRadioOptionModel("BackgroundDefault", "Backgrounds", "Default"));
        backgroundsMenu.addItem(new DefaultRadioOptionModel("BackgroundPewter", "Backgrounds", "Pewter"));
        backgroundsMenu.addItem(new DefaultRadioOptionModel("BackgroundSilver", "Backgrounds", "Silver"));
        backgroundsMenu.addItem(new DefaultRadioOptionModel("BackgroundBlue", "Backgrounds", "Blue"));
        
        DefaultMenuModel toolsMenu = new DefaultMenuModel(null, "Tools");
        toolsMenu.addItem(new DefaultOptionModel("OpenConsole", "Open Console", null));
        toolsMenu.addItem(new DefaultOptionModel("editprefs", "Edit Preferences", null));
        menuBarMenu.addItem(toolsMenu);
        
        DefaultMenuModel helpMenu = new DefaultMenuModel(null, "Help");
        helpMenu.addItem(new DefaultOptionModel("about", "About DacsAjax", null));
        helpMenu.addItem(new DefaultOptionModel("manual", "FedAdmin User Manual", null));
        menuBarMenu.addItem(helpMenu);
        
        SplitPane titleVerticalPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL);
        titleVerticalPane.setStyleName("TestPane");
        add(titleVerticalPane);
        
        Label titleLabel = new Label("DACS Ajax Demonstration Application");
        titleLabel.setStyleName("TitleLabel");
        titleVerticalPane.add(titleLabel);
        
        menuVerticalPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL, new Extent(26));
        titleVerticalPane.add(menuVerticalPane);
        
        MenuStateModel stateModel = new AbstractMenuStateModel() {
            
            boolean showBackground = true;
            FillImage background = Styles.FILL_IMAGE_EXTRAS_BACKGROUND;
            
            public void setSelected(String id, boolean selected) {
                if ("ShowBackground".equals(id)) {
                    showBackground = selected;
                    setBackgroundImage(showBackground ? background : null);
                } else if (selected && "BackgroundDefault".equals(id)) {
                    background = Styles.FILL_IMAGE_EXTRAS_BACKGROUND;
                    if (showBackground) {
                        setBackgroundImage(background);
                    }
                } else if (selected && "BackgroundPewter".equals(id)) {
                    background = Styles.FILL_IMAGE_PEWTER_LINE;
                    if (showBackground) {
                        setBackgroundImage(background);
                    }
                } else if (selected && "BackgroundSilver".equals(id)) {
                    background = Styles.FILL_IMAGE_SILVER_LINE;
                    if (showBackground) {
                        setBackgroundImage(background);
                    }
                } else if (selected && "BackgroundBlue".equals(id)) {
                    background = Styles.FILL_IMAGE_LIGHT_BLUE_LINE;
                    if (showBackground) {
                        setBackgroundImage(background);
                    }
                }
                fireStateChanged();
            }
            
            public void setEnabled(String id, boolean enabled) { }
            
            public boolean isSelected(String id) {
                if ("ShowBackground".equals(id)) {
                    return showBackground;
                } else if ("BackgroundDefault".equals(id)) {
                    return Styles.FILL_IMAGE_EXTRAS_BACKGROUND.equals(background);
                } else if ("BackgroundPewter".equals(id)) {
                    return Styles.FILL_IMAGE_PEWTER_LINE.equals(background);
                } else if ("BackgroundSilver".equals(id)) {
                    return Styles.FILL_IMAGE_SILVER_LINE.equals(background);
                } else if ("BackgroundBlue".equals(id)) {
                    return Styles.FILL_IMAGE_LIGHT_BLUE_LINE.equals(background);
                } else {
                    return false;
                }
            }
            
            public boolean isEnabled(String id) {
                if ("Backgrounds".equals(id)) {
                    return showBackground;
                } else {
                    return true;
                }
            }
        };
        
        MenuBarPane menu = new MenuBarPane(menuBarMenu, stateModel);
        menu.setStyleName("Default");
        menu.addActionListener(commandActionListener);
        menuVerticalPane.add(menu);
        
        Component content = (Component) new TabSplitPane("Federations", Styles.ICON_16_TAB_PANE);
        
        menuVerticalPane.add(content);
        
    }
}

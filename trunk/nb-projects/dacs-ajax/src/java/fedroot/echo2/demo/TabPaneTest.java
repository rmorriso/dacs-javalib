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
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.layout.SplitPaneLayoutData;
import nextapp.echo2.extras.app.CalendarSelect;
import nextapp.echo2.extras.app.TabPane;
import nextapp.echo2.extras.app.layout.TabPaneLayoutData;
import fedroot.echo2.AbstractTest;
import fedroot.echo2.ButtonColumn;
import fedroot.echo2.StyleUtil;
import fedroot.echo2.Styles;
import fedroot.echo2.TestControlPane;
import nextapp.echo2.extras.webcontainer.TabPanePeer;

/**
 * Interactive test module for <code>TabPane</code>s.
 */
public class TabPaneTest extends AbstractTest {
        
    private int tabNumber;

    public TabPaneTest() {
        
        super("TabPane", Styles.ICON_16_TAB_PANE);
        
        final TabPane tabPane = new TabPane();
        add(tabPane);
        setTestComponent(this, tabPane);
        
        // Add/Remove Tabs

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Label (No LayoutData)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabPane.add(new Label("Generic Label"));
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Label", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Label label = new Label("Tab Pane Child " + tabNumber);
                label.setBackground(StyleUtil.randomBrightColor());
                TabPaneLayoutData layoutData = new TabPaneLayoutData();
                layoutData.setTitle("Label #" + tabNumber);
                label.setLayoutData(layoutData);
                tabPane.add(label);
                ++tabNumber;
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Label and Select", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Label label = new Label("Tab Pane Child " + tabNumber);
                label.setBackground(StyleUtil.randomBrightColor());
                TabPaneLayoutData layoutData = new TabPaneLayoutData();
                layoutData.setTitle("Label #" + tabNumber);
                label.setLayoutData(layoutData);
                tabPane.add(label);
                ++tabNumber;
                tabPane.setActiveTabIndex(tabPane.visibleIndexOf(label));
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Label", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Label label = createTestTab();
                tabPane.add(label);
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add-Remove-Add Label", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Label label = createTestTab();
                tabPane.add(label);
                tabPane.remove(label);
                tabPane.add(label);
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Label (index 0)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Label label = new Label("Tab Pane Child " + tabNumber);
                label.setBackground(StyleUtil.randomBrightColor());
                TabPaneLayoutData layoutData = new TabPaneLayoutData();
                layoutData.setTitle("Inserted Label #" + tabNumber);
                label.setLayoutData(layoutData);
                tabPane.add(label, 0);
                ++tabNumber;
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Label (index 2)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tabPane.getComponentCount() < 2) {
                    // Do nothing
                    return;
                }
                Label label = createTestTab();
                tabPane.add(label, 2);
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Three Labels (Index 0)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 3; ++i) {
                    Label label = createTestTab();
                    tabPane.add(label, i);
                }
            }
        });
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Three Labels (Index 3)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int startIndex = tabPane.getComponentCount() < 3 ? tabPane.getComponentCount() : 3; 
                for (int i = 0; i < 3; ++i) {
                    Label label = createTestTab();
                    tabPane.add(label, i + startIndex);
                }
            }
        });
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Three Labels (Append)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 3; ++i) {
                    Label label = createTestTab();
                    tabPane.add(label);
                }
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add 1-6 labels randomly", 
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count = 1 + ((int) (Math.random() * 5));
                for (int i = 0; i < count; ++i) {
                    addLabelRandomly(tabPane);
                }
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Remove 1-6 labels randomly", 
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count = 1 + ((int) (Math.random() * 5));
                for (int i = 0; i < count; ++i) {
                    removeLabelRandomly(tabPane);
                }
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add or Remove 1-6x randomly", 
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count = 1 + ((int) (Math.random() * 5));
                for (int i = 0; i < count; ++i) {
                    boolean add = Math.random() < 0.5;
                    if (add) {
                        addLabelRandomly(tabPane);
                    } else {
                        removeLabelRandomly(tabPane);
                    }
                }
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add CalendarSelect", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CalendarSelect calendarSelect = new CalendarSelect();
                TabPaneLayoutData layoutData = new TabPaneLayoutData();
                layoutData.setTitle("Calendar #" + tabNumber++);
                calendarSelect.setLayoutData(layoutData);
                tabPane.add(calendarSelect);
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add TabPaneTest", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TabPaneTest tabPaneTest = new TabPaneTest();
                TabPaneLayoutData layoutData = new TabPaneLayoutData();
                layoutData.setTitle("TPT #" + tabNumber++);
                tabPaneTest.setLayoutData(layoutData);
                tabPane.add(tabPaneTest);
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add ContentPane", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final ContentPane contentPane = new ContentPane();
                ButtonColumn buttonColumn = new ButtonColumn();
                buttonColumn.addButton("Add WindowPane", new ActionListener(){
                
                    public void actionPerformed(ActionEvent e) {
                        WindowPane windowPane = new WindowPane();
                        windowPane.setStyleName("Default");
                        windowPane.setTitle("Test Window");
                        windowPane.add(new Label(StyleUtil.QUASI_LATIN_TEXT_1));
                        contentPane.add(windowPane);
                    }
                });
                buttonColumn.addButton("Add TabPaneTest WindowPane", new ActionListener(){
                
                    public void actionPerformed(ActionEvent e) {
                        WindowPane windowPane = new WindowPane();
                        windowPane.setStyleName("Default");
                        windowPane.setTitle("Test Window");
                        windowPane.add(new TabPaneTest());
                        contentPane.add(windowPane);
                    }
                });
                contentPane.add(buttonColumn);
                TabPaneLayoutData layoutData = new TabPaneLayoutData();
                layoutData.setTitle("ContentPane #" + tabNumber++);
                contentPane.setLayoutData(layoutData);
                tabPane.add(contentPane);
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add SplitPane", new ActionListener(){
        
            public void actionPerformed(ActionEvent arg0) {
                SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL_LEFT_RIGHT);
                splitPane.setResizable(true);
                TabPaneLayoutData layoutData = new TabPaneLayoutData();
                layoutData.setTitle("SplitPane #" + tabNumber++);
                splitPane.setLayoutData(layoutData);
                tabPane.add(splitPane);
                
                SplitPaneLayoutData splitPaneLayoutData;
                
                Label left = new Label("Left");
                splitPaneLayoutData = new SplitPaneLayoutData();
                splitPaneLayoutData.setBackground(new Color(0xafafff));
                left.setLayoutData(splitPaneLayoutData);
                splitPane.add(left);
                
                Label right = new Label("Right");
                splitPaneLayoutData = new SplitPaneLayoutData();
                splitPaneLayoutData.setBackground(new Color(0xafffaf));
                right.setLayoutData(splitPaneLayoutData);
                splitPane.add(right);
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Remove Last Tab", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tabPane.getComponentCount() > 0) {
                    tabPane.remove(tabPane.getComponentCount() - 1);
                }
            }
        });

        // General Properties

        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Tab Position = Top", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabPane.setTabPosition(TabPane.TAB_POSITION_TOP);
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Tab Position = Bottom", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabPane.setTabPosition(TabPane.TAB_POSITION_BOTTOM);
            }
        });
        
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_FOREGROUND);
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_BACKGROUND);
        addFontPropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_FONT);
        
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Border Type = None", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabPane.setBorderType(TabPane.BORDER_TYPE_NONE);
            }
        });
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Border Type = Adjacent", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabPane.setBorderType(TabPane.BORDER_TYPE_ADJACENT_TO_TABS);
            }
        });
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Border Type = Parallel", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabPane.setBorderType(TabPane.BORDER_TYPE_PARALLEL_TO_TABS);
            }
        });
        testControlsPane.addButton(TestControlPane.CATEGORY_PROPERTIES, "Set Border Type = Surround", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabPane.setBorderType(TabPane.BORDER_TYPE_SURROUND);
            }
        });
        
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_TAB_ACTIVE_BACKGROUND);
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_TAB_ACTIVE_FOREGROUND);
        addBorderPropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_TAB_ACTIVE_BORDER);
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_TAB_INACTIVE_BACKGROUND);
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_TAB_INACTIVE_FOREGROUND);
        addBorderPropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_TAB_INACTIVE_BORDER);

        addFillImagePropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_TAB_ACTIVE_BACKGROUND_IMAGE, 
                StyleUtil.TEST_FILL_IMAGES);
        addFillImagePropertyTests(TestControlPane.CATEGORY_PROPERTIES, TabPane.PROPERTY_TAB_INACTIVE_BACKGROUND_IMAGE, 
                StyleUtil.TEST_FILL_IMAGES);
        
        addInsetsPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "insets");
        addInsetsPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "defaultContentInsets");
        addExtentPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "tabInset", 
                new Extent[]{null, new Extent(0), new Extent(1), new Extent(2), new Extent(5), new Extent(10), new Extent(20)});
        addExtentPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "tabSpacing", 
                new Extent[]{null, new Extent(0), new Extent(1), new Extent(2), new Extent(5), new Extent(10), new Extent(20)});
        
        // Selection Properties

        for (int i = 0; i < 10; ++i) {
            final int tabIndex = i;
            testControlsPane.addButton(TestControlPane.CATEGORY_SELECTION, "Select TabIndex " + i, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tabPane.setActiveTabIndex(tabIndex);
                }
            });
        }
        
        addStandardIntegrationTests();

        // Lazy Rendering Properties
        
        testControlsPane.addButton("Lazy Render Tests", "LazyRenderEnabled = false", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabPane.setProperty(TabPanePeer.PROPERTY_LAZY_RENDER_ENABLED, Boolean.FALSE);
            }
        });
        
        testControlsPane.addButton("Lazy Render Tests", "LazyRenderEnabled = true", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabPane.setProperty(TabPanePeer.PROPERTY_LAZY_RENDER_ENABLED, Boolean.TRUE);
            }
        });
        
        testControlsPane.addButton("Lazy Render Tests", "LazyRenderEnabled = default (true)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabPane.setProperty(TabPanePeer.PROPERTY_LAZY_RENDER_ENABLED, null);
            }
        });
    }
    
    private void addLabelRandomly(TabPane tabPane) {
        Label label = createTestTab();
        int position = ((int) (Math.random() * (tabPane.getComponentCount() + 1)));
        tabPane.add(label, position);
        ++tabNumber;
    }
    
    private Label createTestTab() {
        Label label = new Label("Tab Pane Child " + tabNumber);
        label.setBackground(StyleUtil.randomBrightColor());
        TabPaneLayoutData layoutData = new TabPaneLayoutData();
        layoutData.setTitle("Label #" + tabNumber);
        label.setLayoutData(layoutData);
        ++tabNumber;
        return label;
    }

    private void removeLabelRandomly(TabPane tabPane) {
        if (tabPane.getComponentCount() == 0) {
            return;
        }
        int position = ((int) (Math.random() * (tabPane.getComponentCount())));
        tabPane.remove(position);
    }
}

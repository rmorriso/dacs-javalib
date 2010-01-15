/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2.demo;

import nextapp.echo2.app.Color;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.layout.SplitPaneLayoutData;
import nextapp.echo2.extras.app.CalendarSelect;
import nextapp.echo2.extras.app.AccordionPane;
import nextapp.echo2.extras.app.layout.AccordionPaneLayoutData;
import fedroot.echo2.TabSplitPane;
import fedroot.echo2.ButtonColumn;
import fedroot.echo2.StyleUtil;
import fedroot.echo2.Styles;
import fedroot.echo2.TestControlPane;
import nextapp.echo2.extras.webcontainer.AccordionPanePeer;

/**
 * Interactive test module for <code>AccordionPane</code>s.
 */
public class AccordionPaneTest extends TabSplitPane {
        
    private int tabNumber;

    public AccordionPaneTest() {
        super("AccordionPane", Styles.ICON_16_ACCORDION_PANE);
        
        final AccordionPane accordionPane = new AccordionPane();
        add(accordionPane);
        setTestComponent(this, accordionPane);
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Label (No LayoutData)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accordionPane.add(new Label("Generic Label"));
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Label", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Label label = new Label("Accordion Pane Child " + tabNumber);
                label.setBackground(StyleUtil.randomBrightColor());
                AccordionPaneLayoutData layoutData = new AccordionPaneLayoutData();
                layoutData.setTitle("Label #" + tabNumber);
                label.setLayoutData(layoutData);
                accordionPane.add(label);
                ++tabNumber;
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add-Remove-Add Label", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Label label = new Label("Accordion Pane Child " + tabNumber);
                label.setBackground(StyleUtil.randomBrightColor());
                AccordionPaneLayoutData layoutData = new AccordionPaneLayoutData();
                layoutData.setTitle("Label #" + tabNumber);
                label.setLayoutData(layoutData);
                accordionPane.add(label);
                accordionPane.remove(label);
                accordionPane.add(label);
                ++tabNumber;
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add and Select Label", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Label label = new Label("Accordion Pane Child " + tabNumber);
                label.setBackground(StyleUtil.randomBrightColor());
                AccordionPaneLayoutData layoutData = new AccordionPaneLayoutData();
                layoutData.setTitle("Label #" + tabNumber);
                label.setLayoutData(layoutData);
                accordionPane.add(label);
                ++tabNumber;
                accordionPane.setActiveTabIndex(accordionPane.visibleIndexOf(label));
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Label (index 0)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Label label = new Label("Accordion Pane Child " + tabNumber);
                label.setBackground(StyleUtil.randomBrightColor());
                AccordionPaneLayoutData layoutData = new AccordionPaneLayoutData();
                layoutData.setTitle("Inserted Label #" + tabNumber);
                label.setLayoutData(layoutData);
                accordionPane.add(label, 0);
                ++tabNumber;
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Label (index 2)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (accordionPane.getComponentCount() < 2) {
                    // Do nothing
                    return;
                }
                Label label = new Label("Accordion Pane Child " + tabNumber);
                label.setBackground(StyleUtil.randomBrightColor());
                AccordionPaneLayoutData layoutData = new AccordionPaneLayoutData();
                layoutData.setTitle("Inserted Label #" + tabNumber);
                label.setLayoutData(layoutData);
                accordionPane.add(label, 2);
                ++tabNumber;
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Three Labels (Index 0)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 3; ++i) {
                    Label label = createTestTab();
                    accordionPane.add(label, i);
                }
            }
        });
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Three Labels (Index 3)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int startIndex = accordionPane.getComponentCount() < 3 ? accordionPane.getComponentCount() : 3; 
                for (int i = 0; i < 3; ++i) {
                    Label label = createTestTab();
                    accordionPane.add(label, i + startIndex);
                }
            }
        });
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add Three Labels (Append)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 3; ++i) {
                    Label label = createTestTab();
                    accordionPane.add(label);
                }
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add 1-6 labels randomly", 
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count = 1 + ((int) (Math.random() * 5));
                for (int i = 0; i < count; ++i) {
                    addLabelRandomly(accordionPane);
                }
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Remove 1-6 labels randomly", 
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count = 1 + ((int) (Math.random() * 5));
                for (int i = 0; i < count; ++i) {
                    removeLabelRandomly(accordionPane);
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
                        addLabelRandomly(accordionPane);
                    } else {
                        removeLabelRandomly(accordionPane);
                    }
                }
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add CalendarSelect", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CalendarSelect calendarSelect = new CalendarSelect();
                AccordionPaneLayoutData layoutData = new AccordionPaneLayoutData();
                layoutData.setTitle("Calendar #" + tabNumber++);
                calendarSelect.setLayoutData(layoutData);
                accordionPane.add(calendarSelect);
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add AccordionPaneTest", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AccordionPaneTest accordionPaneTest = new AccordionPaneTest();
                AccordionPaneLayoutData layoutData = new AccordionPaneLayoutData();
                layoutData.setTitle("APT #" + tabNumber++);
                accordionPaneTest.setLayoutData(layoutData);
                accordionPane.add(accordionPaneTest);
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
                buttonColumn.addButton("Add AccordionPaneTest WindowPane", new ActionListener(){
                
                    public void actionPerformed(ActionEvent e) {
                        WindowPane windowPane = new WindowPane();
                        windowPane.setStyleName("Default");
                        windowPane.setTitle("Test Window");
                        windowPane.add(new AccordionPaneTest());
                        contentPane.add(windowPane);
                    }
                });
                contentPane.add(buttonColumn);
                AccordionPaneLayoutData layoutData = new AccordionPaneLayoutData();
                layoutData.setTitle("ContentPane #" + tabNumber++);
                contentPane.setLayoutData(layoutData);
                accordionPane.add(contentPane);
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Add SplitPane", new ActionListener(){
        
            public void actionPerformed(ActionEvent arg0) {
                SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_HORIZONTAL_LEFT_RIGHT);
                splitPane.setResizable(true);
                AccordionPaneLayoutData layoutData = new AccordionPaneLayoutData();
                layoutData.setTitle("SplitPane #" + tabNumber++);
                splitPane.setLayoutData(layoutData);
                accordionPane.add(splitPane);
                
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

        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Remove-Add Index 0", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (accordionPane.getComponentCount() < 0) {
                    return;
                }
                Component component = accordionPane.getComponent(0);
                accordionPane.remove(component);
                accordionPane.add(component, 0);
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Remove-Add Index 2", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (accordionPane.getComponentCount() < 2) {
                    return;
                }
                Component component = accordionPane.getComponent(2);
                accordionPane.remove(component);
                accordionPane.add(component, 2);
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_CONTENT, "Remove Last Tab", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (accordionPane.getComponentCount() > 0) {
                    accordionPane.remove(accordionPane.getComponentCount() - 1);
                }
            }
        });
 
        // General Properties
        
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "foreground");
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "background");
        addInsetsPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "defaultContentInsets");

        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "tabForeground");
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "tabBackground");
        addBorderPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "tabBorder");
        
        addBooleanPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "tabRolloverEnabled");
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "tabRolloverForeground");
        addColorPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "tabRolloverBackground");
        addBorderPropertyTests(TestControlPane.CATEGORY_PROPERTIES, "tabRolloverBorder");
        
        // Selection Properties

        for (int i = 0; i < 10; ++i) {
            final int tabIndex = i;
            testControlsPane.addButton(TestControlPane.CATEGORY_SELECTION, "Select TabIndex " + i, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    accordionPane.setActiveTabIndex(tabIndex);
                }
            });
        }

        // Integration Tests
        
        addStandardIntegrationTests();

        // Lazy Rendering Properties
        
        testControlsPane.addButton("Lazy Render Tests", "LazyRenderEnabled = false", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accordionPane.setProperty(AccordionPanePeer.PROPERTY_LAZY_RENDER_ENABLED, Boolean.FALSE);
            }
        });
        
        testControlsPane.addButton("Lazy Render Tests", "LazyRenderEnabled = true", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accordionPane.setProperty(AccordionPanePeer.PROPERTY_LAZY_RENDER_ENABLED, Boolean.TRUE);
            }
        });
        
        testControlsPane.addButton("Lazy Render Tests", "LazyRenderEnabled = default (false)", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accordionPane.setProperty(AccordionPanePeer.PROPERTY_LAZY_RENDER_ENABLED, null);
            }
        });
    }

    private void addLabelRandomly(AccordionPane accordionPane) {
        Label label = createTestTab();
        int position = ((int) (Math.random() * (accordionPane.getComponentCount() + 1)));
        accordionPane.add(label, position);
        ++tabNumber;
    }
    
    private Label createTestTab() {
        Label label = new Label("Tab Pane Child " + tabNumber);
        label.setBackground(StyleUtil.randomBrightColor());
        AccordionPaneLayoutData layoutData = new AccordionPaneLayoutData();
        layoutData.setTitle("Label #" + tabNumber);
        label.setLayoutData(layoutData);
        ++tabNumber;
        return label;
    }

    private void removeLabelRandomly(AccordionPane accordionPane) {
        if (accordionPane.getComponentCount() == 0) {
            return;
        }
        int position = ((int) (Math.random() * (accordionPane.getComponentCount())));
        accordionPane.remove(position);
    }
}

/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2;

import fedroot.echo2.dacs.ConfigurationPane;
import fedroot.echo2.dacs.ContextPane;
import fedroot.echo2.dacs.CredentialsPane;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nextapp.echo2.app.Border;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.FillImage;
import nextapp.echo2.app.Font;
import nextapp.echo2.app.ImageReference;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.extras.app.TabPane;
import nextapp.echo2.extras.app.layout.TabPaneLayoutData;

public class TabSplitPane extends SplitPane {

    private Component testComponentParent;
    private Component testComponent;
    protected TestControlPane testControlsPane;
    
    public TabSplitPane(String testName) {
        this(testName, null);
    }
    
    public TabSplitPane(String testName, ImageReference icon) {
        super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(300, Extent.PX));
        setStyleName("DefaultResizable");
        
        testControlsPane = new TestControlPane(testName, icon);
        add(testControlsPane);
        
        final TabPane tabPane = new TabPane();
        add(tabPane);
        
        setTestComponent(this, tabPane);
        
        Label label = new Label("Overview");
        label.setBackground(StyleUtil.randomBrightColor());
        TabPaneLayoutData layoutData = new TabPaneLayoutData();
        layoutData.setTitle("Overview");
        label.setLayoutData(layoutData);
        tabPane.add(label, 0);
        
        label = new Label("Jurisdictions");
        label.setBackground(StyleUtil.randomBrightColor());
        layoutData = new TabPaneLayoutData();
        layoutData.setTitle("Jurisdictions");
        label.setLayoutData(layoutData);
        tabPane.add(label, 1);
        
        layoutData = new TabPaneLayoutData();
        layoutData.setTitle("Credentials");
        CredentialsPane t = new CredentialsPane();
        t.setLayoutData(layoutData);
        tabPane.add(t, 2);
        
        layoutData = new TabPaneLayoutData();
        layoutData.setTitle("Configuration");
        ConfigurationPane cont = new ConfigurationPane();
        cont.setLayoutData(layoutData);
        tabPane.add(cont, 3);
        
        layoutData = new TabPaneLayoutData();
        layoutData.setTitle("Container Context");
        ContextPane cp = new ContextPane();
        cp.setLayoutData(layoutData);
        tabPane.add(cp);
    }
    
    protected void setTestComponent(Component testComponentParent, Component testComponent) {
        this.testComponentParent = testComponentParent;
        this.testComponent = testComponent;
    }
    
    protected void addBooleanPropertyTests(String category, final String propertyName) {
        testControlsPane.addButton(category, propertyName + ": true", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, boolean.class, Boolean.TRUE);
            }
        });
        testControlsPane.addButton(category, propertyName + ": false", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, boolean.class, Boolean.FALSE);
            }
        });
    }
    
    protected void addBorderPropertyTests(String category, final String propertyName) {
        testControlsPane.addButton(category, propertyName + ": Randomize", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Border.class, StyleUtil.randomBorder());
            }
        });
        testControlsPane.addButton(category, propertyName + ": Set Color", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Border border = (Border) getTestComponentProperty(propertyName);
                if (border == null) {
                    border = new Border(new Extent(1), Color.BLUE, Border.STYLE_SOLID);
                }
                setTestComponentProperty(propertyName, Border.class, 
                        new Border(border.getSize(), StyleUtil.randomColor(), border.getStyle()));
            }
        });
        testControlsPane.addButton(category, propertyName + ": Set Size", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Border.class, 
                        StyleUtil.nextBorderSize((Border) getTestComponentProperty(propertyName)));
            }
        });
        testControlsPane.addButton(category, propertyName + ": Set Style", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Border.class, 
                        StyleUtil.nextBorderStyle((Border) getTestComponentProperty(propertyName)));
            }
        });
        testControlsPane.addButton(category, propertyName + ": null", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Border.class, null);
            }
        });
    }
    
    protected void addFillImagePropertyTests(String category, final String propertyName, final FillImage[] fillImageValues) {
        for (int i = 0; i < fillImageValues.length; ++i) {
            final int index = i;
            String name = fillImageValues[i] == null ? "null" : Integer.toString(i);
            testControlsPane.addButton(category, propertyName + ": " + name, new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    setTestComponentProperty(propertyName, FillImage.class, fillImageValues[index]);
                }
            });
        }
    }
    
    protected void addExtentPropertyTests(String category, final String propertyName, final Extent[] extentValues) {
        for (int i = 0; i < extentValues.length; ++i) {
            String extentString = extentValues[i] == null ? "null" : extentValues[i].toString();
            final int index = i;
            testControlsPane.addButton(category, propertyName + ": " + extentString, new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    setTestComponentProperty(propertyName, Extent.class, extentValues[index]);
                }
            });
        }
    }
    
    protected void addColorPropertyTests(String category, final String propertyName) {
        testControlsPane.addButton(category, propertyName + ": Randomize", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Color.class, StyleUtil.randomColor());
            }
        });
        testControlsPane.addButton(category, propertyName + ": null", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Color.class, null);
            }
        });
    }
    
    protected void addFontPropertyTests(String category, final String propertyName) {
        testControlsPane.addButton(category, propertyName + ": Randomize", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Font.class, StyleUtil.randomFont());
            }
        });
        testControlsPane.addButton(category, propertyName + ": null", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Font.class, null);
            }
        });
    }
    
    protected void addInsetsPropertyTests(String category, final String propertyName) {
        
        testControlsPane.addButton(category, propertyName + ": 0px", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Insets.class, new Insets(0));
            }
        });
        testControlsPane.addButton(category, propertyName + ": 1px", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Insets.class, new Insets(1));
            }
        });
        testControlsPane.addButton(category, propertyName + ": 2px", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Insets.class, new Insets(2));
            }
        });
        testControlsPane.addButton(category, propertyName + ": 5px", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Insets.class, new Insets(5));
            }
        });
        testControlsPane.addButton(category, propertyName + ": 10/20/30/40px", 
                new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Insets.class, new Insets(10, 20, 30, 40));
            }
        });
        testControlsPane.addButton(category, propertyName + ": null", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setTestComponentProperty(propertyName, Insets.class, null);
            }
        });
    }
    
    protected void addStandardIntegrationTests() {

        testControlsPane.addButton(TestControlPane.CATEGORY_INTEGRATION, "Add Component", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testComponentParent.indexOf(testComponent) == -1) {
                    testComponentParent.add(testComponent);
                }
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_INTEGRATION, "Remove Component", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testComponentParent.remove(testComponent);
            }
        });
        
        testControlsPane.addButton(TestControlPane.CATEGORY_INTEGRATION, "Enable Component", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testComponent.setEnabled(true);
            }
        });

        testControlsPane.addButton(TestControlPane.CATEGORY_INTEGRATION, "Disable Component", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testComponent.setEnabled(false);
            }
        });
    }
    
    private Object getTestComponentProperty(String propertyName) {
        try {
            String methodName = "get" + Character.toUpperCase(propertyName.charAt(0)) +  propertyName.substring(1);
            Method setPropertyMethod = testComponent.getClass().getMethod(methodName, new Class[]{});
            return setPropertyMethod.invoke(testComponent, new Object[]{});
        } catch (NoSuchMethodException ex) {
            DacsAjaxApp.getApp().consoleWrite(ex.toString());
        } catch (IllegalArgumentException ex) {
            DacsAjaxApp.getApp().consoleWrite(ex.toString());
        } catch (IllegalAccessException ex) {
            DacsAjaxApp.getApp().consoleWrite(ex.toString());
        } catch (InvocationTargetException ex) {
            DacsAjaxApp.getApp().consoleWrite(ex.toString());
        }
        return null;
    }
    
    private void setTestComponentProperty(String propertyName, Class propertyClass, Object newValue) {
        try {
            String methodName = "set" + Character.toUpperCase(propertyName.charAt(0)) +  propertyName.substring(1);
            Method setPropertyMethod = testComponent.getClass().getMethod(methodName, new Class[]{propertyClass});
            setPropertyMethod.invoke(testComponent, new Object[]{newValue});
        } catch (NoSuchMethodException ex) {
            DacsAjaxApp.getApp().consoleWrite(ex.toString());
        } catch (IllegalArgumentException ex) {
            DacsAjaxApp.getApp().consoleWrite(ex.toString());
        } catch (IllegalAccessException ex) {
            DacsAjaxApp.getApp().consoleWrite(ex.toString());
        } catch (InvocationTargetException ex) {
            DacsAjaxApp.getApp().consoleWrite(ex.toString());
        }
    }
}

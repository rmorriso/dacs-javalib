/*
 * DacsLoginDialogTest.java
 * JUnit based test
 *
 * Created on December 12, 2005, 9:35 AM
 */

package com.fedroot.dacs.swingutil;

import com.fedroot.dacs.Federation;
import com.fedroot.dacs.UserContext;
import com.fedroot.dacs.test.TestConstants;
import junit.framework.*;
import java.util.Locale;

/**
 *
 * @author rmorriso
 */
public class DacsLoginDialogTest extends TestCase {
    Federation testfed;
    UserContext testuser;
    
    public DacsLoginDialogTest(String testName) {
        super(testName);
        testuser = UserContext.getInstance("DacsLoginDialogTest");
        try {
            testfed = Federation.getInstance(testuser, TestConstants.testbaseuri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DacsLoginDialogTest.class);
        
        return suite;
    }

    /**
     * Test of setLocale method, of class com.fedroot.dacs.swing.DacsLoginDialog.
     */
    public void testSetLocale() {
        System.out.println("setLocale");
        
        Locale locale = Locale.US;
        DacsLoginDialog instance = new DacsLoginDialog(testfed,testuser);
        
        instance.setLocale(locale);
        
        assertTrue(true);
    }

    /**
     * Test of main method, of class com.fedroot.dacs.swing.DacsLoginDialog.
     */
    public void testMain() {
        System.out.println("main");
        
     /**
     * A simple example to show how this might be used.
     * If there are arguments passed to this program, the first
     * is treated as the default name, the second as the default password
     *
     * @param args command line arguments: name and password (optional)
     *
     * @since ostermillerutils 1.00.00
     */
        DacsLoginDialog p = new DacsLoginDialog(testfed, testuser);
        if(p.showDialog()){
            System.out.println("Name: " + p.getName());
            System.out.println("Pass: " + p.getPass());
        } else {
            System.out.println("User selected cancel");
        }
        p.dispose();
        p = null;
    }
    
}

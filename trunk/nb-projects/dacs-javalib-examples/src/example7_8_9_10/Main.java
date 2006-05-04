/*
 * Example7_8_9_10.java
 *
 * Created on December 2, 2005, 8:28 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package example7_8_9_10;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.UserContext;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * main class for examples 7, 8, 9, 10 in DJL system documentation;
 * this is a simple Swing application to demonstrate various DACS operations
 * @author Rick Morrison
 */
public class Main {
    
    private static UserContext usercontext = UserContext.getInstance("DacsClient Demo");

    private static String feduri = "https://demo.fedroot.com/fedadmin/dacs";
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        try {
            Federation federation = Federation.getInstance(usercontext, feduri);
            DacsClientFrame f = new DacsClientFrame(usercontext, federation);
            f.setTitle("DACS JavaLib Example Thick Client");
            f.setSize(900, 500);
            f.addWindowListener(
                    new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            }
            );
            f.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
 * Example7_8_9_10.java
 *
 * Created on December 2, 2005, 8:28 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.swingdemo;

import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.http.DacsClientContext;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * main class for examples 7, 8, 9, 10 in DJL system documentation;
 * this is a simple Swing application to demonstrate various DACS operations
 * @author Rick Morrison
 */
public class Main {
    
    private static final String DACS_BASE_URI = "https://fedroot.com/test/dacs";
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        try {
            SessionController sessionController = new SessionController(DACS_BASE_URI);
            
            JFrame frame = new JFrame();
            frame.setTitle("DACS JavaLib Example Thick Client");
            frame.setSize(500,200);
            
            DacsLoginDialog loginDialog = new DacsLoginDialog(frame);
//            f.setSize(900, 500);
            frame.addWindowListener(
                    new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            }
            );
            frame.setVisible(true);
            loginDialog.showDialog();

//            DacsLoginFrame f = new DacsLoginFrame(federation, dacsClientContext);
//            f.setTitle("DACS JavaLib Example Thick Client");
////            f.setSize(900, 500);
//            f.addWindowListener(
//                    new WindowAdapter() {
//                public void windowClosing(WindowEvent e) {
//                    System.exit(0);
//                }
//            }
//            );
//            f.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
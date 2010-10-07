/*
 * Example7_8_9_10.java
 *
 * Created on December 2, 2005, 8:28 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.demo.dacs.swing;

import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.http.DacsClientContext;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * main class for examples 7, 8, 9, 10 in DJL system documentation;
 * this is a simple Swing application to demonstrate various DACS operations
 * @author Rick Morrison
 */
public class Main {
    
    private static DacsClientContext dacsClientContext = new DacsClientContext();

    private static String feduri = "https://fedroot.com/fedadmin/dacs";
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        try {
            FederationLoader federationLoader = new FederationLoader(feduri, dacsClientContext);
            Federation federation = federationLoader.getFederation();
            DacsClientFrame f = new DacsClientFrame(dacsClientContext, federation);
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

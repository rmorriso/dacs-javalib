/*
 * Event902Handler.java
 *
 * Created on December 13, 2005, 3:46 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.demo.dacs.swing;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.dacs.swingutil.DacsLoginDialog;
import com.fedroot.dacs.util.DacsAccess902Event;
import com.fedroot.dacs.util.Dacs902EventHandler;
import java.awt.Frame;

/**
 *
 * @author rmorriso
 */
public class Event902Handler implements Dacs902EventHandler{
    Frame parent;
    /** Creates a new instance of Event902Handler */
    public Event902Handler(Frame parent) {
        this.parent = parent;
    }
    
    public int handleEvent(DacsContext dacscontext, DacsGetMethod dacsget, DacsAccess902Event event) {
        try {
            Federation federation = Federation.getInstance(dacscontext, event.getJurUri());
            DacsLoginDialog dialog = new DacsLoginDialog(this.parent, "DACS Login", federation, dacscontext);
            if(dialog.showDialog()){ // user login was successful
                System.out.println("Name: " + dialog.getName());
                System.out.println("Pass: " + dialog.getPass());
                dialog.dispose();
                // return the result of executing dacsget again in the modified context
                return dacscontext.executeCheckFailMethod(dacsget);
            } else {
                System.out.println("User selected cancel");
                dialog.dispose();
                return DacsStatus.SC_DACS_ACCESS_DENIED;
            }
        } catch (Exception e) {
            return DacsStatus.SC_DACS_ACCESS_DENIED;
        }
    }
}

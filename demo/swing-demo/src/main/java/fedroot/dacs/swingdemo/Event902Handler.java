/*
 * Event902Handler.java
 *
 * Created on December 13, 2005, 3:46 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.swingdemo;


import com.fedroot.dacs.swing.DacsLoginDialog;
import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.events.Dacs902EventHandler;
import fedroot.dacs.events.DacsAccess902Event;
import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsGetRequest;
import fedroot.dacs.http.DacsStatus;
import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmorriso
 */
public class Event902Handler implements Dacs902EventHandler{
    private Frame parent;

    private static final Logger logger = Logger.getLogger("com.fedroot");

    // TODO get this from user preferences file
    private static String DACS_BASE_URI = "https://fedroot.com/dacs";
    /** 
     * Creates a new instance of Event902Handler
     */
    public Event902Handler(Frame parent) {
        this.parent = parent;
    }
    
    public int handleEvent(DacsClientContext dacscontext, DacsGetRequest dacsget, DacsAccess902Event event) {
        try {
            DacsClientContext dacsClientContext = new DacsClientContext();
            FederationLoader federationLoader = new FederationLoader(DACS_BASE_URI, dacsClientContext);
                logger.log(Level.FINE, "loading federation from {0}", DACS_BASE_URI);
                Federation federation = federationLoader.getFederation();
                logger.log(Level.FINE, "loaded federation {0}", federation.getFederationName());

            DacsLoginDialog dialog = new DacsLoginDialog(this.parent, "DACS Login", federation, dacscontext);
            if(dialog.showDialog()){ // user login was successful
                logger.log(Level.FINE, "Username: {0}", dialog.getName());
                logger.log(Level.FINE, "Password: {0}", dialog.getPass());
                dialog.dispose();
                // return the result of executing dacsget again in the modified context
                return 1; //dacscontext.executeCheckFailMethod(dacsget);
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

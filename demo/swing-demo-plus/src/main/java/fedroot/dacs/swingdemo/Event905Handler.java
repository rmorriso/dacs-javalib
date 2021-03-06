/*
 * Event905Handler.java
 *
 * Created on December 13, 2005, 3:46 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.swingdemo;

import fedroot.dacs.events.Dacs905EventHandler;
import fedroot.dacs.events.DacsAccess905Event;
import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsGetRequest;
import java.awt.Frame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rmorriso
 */
public class Event905Handler implements Dacs905EventHandler{
    Frame parent;
    
    private static final Log LOG = LogFactory.getLog(Event905Handler.class);
    
    /** Creates a new instance of Event905Handler */
    public Event905Handler(Frame parent) {
        this.parent = parent;
    }
    
    public int handleEvent(DacsClientContext dacscontext, DacsGetRequest dacsget, DacsAccess905Event event) {
        return -1; //TODO fix me!
//        try {
//            // execute DacsNoticePresentationService to get notices back for acceptance
//            // then execute DacsNoticePresentationService to send user's acceptance and get NAT
//            DacsNotices notices = new DacsNotices(dacscontext, event);
//            DacsNoticePresentationDialog dialog = new DacsNoticePresentationDialog(this.parent, "DACS Notices Must Be Acknowledged", dacscontext, notices);
//            if(dialog.showDialog()){ // user clicked ACCEPT
//                // send user accept response via DacsNoticeAckService
//                if (notices.acceptNotices(dacscontext)) {
//                    // dialog.dispose();
//                    dialog.setVisible(false);
//                    // return the result of executing dacsget again in the modified context
//                    return dacscontext.executeCheckFailMethod(dacsget);
//                } else {
//                    // TODO: these messages should be reported in GUI
//                    LOG.info("Notice acknowledgement failed");
//                    return DacsStatus.SC_DACS_ACCESS_DENIED;
//                }
//            } else {
//                LOG.info("User declined or closed dialog without accepting notices.");
//                // dialog.dispose();
//                dialog.setVisible(false);
//                return DacsStatus.SC_DACS_ACCESS_DENIED;
//            }
//        } catch (Exception e) {
//            return DacsStatus.SC_DACS_ACCESS_DENIED;
//        }
    }
}

/*
 * Event905Handler.java
 *
 * Created on December 13, 2005, 3:46 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package example7_8_9_10;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.DacsNotices;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.dacs.swingutil.DacsNoticePresentationDialog;
import com.fedroot.dacs.util.Dacs905EventHandler;
import com.fedroot.dacs.util.DacsAccess905Event;
import java.awt.Frame;

/**
 *
 * @author rmorriso
 */
public class Event905Handler implements Dacs905EventHandler{
    Frame parent;
    /** Creates a new instance of Event902Handler */
    public Event905Handler(Frame parent) {
        this.parent = parent;
    }
    
    public int handleEvent(DacsContext dacscontext, DacsGetMethod dacsget, DacsAccess905Event event) {
        try {
            // execute DacsNoticePresentationService to get notices back for acceptance
            // then execute DacsNoticePresentationService to send user's acceptance and get NAT
            DacsNotices notices = new DacsNotices(dacscontext, event);
            DacsNoticePresentationDialog dialog = new DacsNoticePresentationDialog(this.parent, "DACS Notices Must Be Acknowledged", dacscontext, notices);
            if(dialog.showDialog()){ // user clicked ACCEPT                
                // send user accept response via DacsNoticeAckService
                if (notices.acceptNotices(dacscontext)) {
                    dialog.dispose();
                    // return the result of executing dacsget again in the modified context
                    return dacscontext.executeCheckFailMethod(dacsget);
                } else {
                    // TODO: these messages should be reported in GUI
                    System.out.println("Notice acknowledgement failed");
                    return DacsStatus.SC_DACS_ACCESS_DENIED;
                }
            } else {
                System.out.println("User declined or closed dialog without accepting notices.");
                dialog.dispose();
                return DacsStatus.SC_DACS_ACCESS_DENIED;
            }
        } catch (Exception e) {
            return DacsStatus.SC_DACS_ACCESS_DENIED;
        }
    }
}

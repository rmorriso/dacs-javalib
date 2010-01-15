/*
 * Test902Handler.java
 *
 * Created on September 22, 2005, 12:56 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.web;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.dacs.test.TestConstants;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rmorriso
 * @version
 */
public class Test902Handler extends DacsServlet {
    
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            // load Federation and its jurisdictions
            //   Federation testfed = Federation.getInstance(dacscontext, TestConstants.testbaseuri);
            Federation testfed = Federation.getInstance(usercontext, TestConstants.testbaseuri);
            Jurisdiction testjur = testfed.getJurisdictionByName("TEST");
            // signout of any credentials in federation TEST
            // note: this does not clear credentials set in user's browser 
            // - only those set directly in user context
            usercontext.signout(testfed);
            // try to access a resource that requires authentication
            // we should get thrown to the Event902Handler
            DacsGetMethod dacsget = new DacsGetMethod(TestConstants.event902uri);
            int dacsstatus = usercontext.executeCheckFailMethod(dacsget);
            if (dacsstatus == DacsStatus.SC_DACS_ACCESS_GRANTED) {
                buf.append("<p><em>" + dacsget.getResponseBodyAsString() + "</em></p>");
            } else {
                buf.append("<p><em>authentication failed - access denied</em></p>");
            }
            dacsget.releaseConnection();
        } catch (DacsException e) {
            out.println("DacsException: " + e.getMessage());
        } catch (Exception e) {
            out.println("Exception: " + e.getMessage());
            e.printStackTrace(out);
        }
    }
}

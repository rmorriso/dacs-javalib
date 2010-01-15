/*
 * DacsConf.java
 *
 * Created on September 22, 2005, 12:56 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.web;
import com.fedroot.dacs.DacsUserAccount;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import com.fedroot.dacs.JurisdictionConfiguration;
import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.services.DACS;
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
public class DacsConf extends DacsServlet {
    
    
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
            // dacs_conf requires authentication as a a DACS ADMIN_IDENTITY
            // authenticate a DacsUserAccount under this UserContext
            if (usercontext.getCookieByName(DACS.makeCookieName(testfed.getName(),testjur.getName(),TestConstants.testadminusername)) != null) {
                buf.append("<p>" + TestConstants.testadminusername + " has already been authenticated </p>");
            } else {
                buf.append("<p>authenticating ADMIN_IDENTITY " + TestConstants.testadminusername + "</p>");
                // get a test account in testfed:testjur
                DacsUserAccount testaccount = new DacsUserAccount(testfed, testjur, TestConstants.testadminusername, "yes");
                // authenticate testaccount under testuser
                usercontext.authenticate(testaccount, TestConstants.testadminpassword);
            }
        
            buf.append("<h2>TEST Jurisdiction Configuration:</h2>");
            JurisdictionConfiguration config = testjur.getConfiguration(usercontext);
            buf.append(config.toString());
            
        } catch (DacsException e) {
            out.println("DacsException: " + e.getMessage());
        } catch (Exception e) {
            out.println("Exception: " + e.getMessage());
            e.printStackTrace(out);
        }
    }
}

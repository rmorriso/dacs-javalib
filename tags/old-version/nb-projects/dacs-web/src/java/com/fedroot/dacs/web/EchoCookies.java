/*
 * EchoCookies.java
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
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.services.DACS;
import com.fedroot.dacs.test.TestConstants;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author rmorriso
 * @version
 */
public class EchoCookies extends DacsServlet {
    
    
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
            // try to access a resource that requires authentication
            // we should get thrown to the Event902Handler
            DacsGetMethod dacsget = new DacsGetMethod(TestConstants.event902uri);
            usercontext.executeCheckFailMethod(dacsget);
            dacsget.releaseConnection();
            // authenticate a DacsUserAccount under this UserContext
            if (usercontext.getCookieByName(DACS.makeCookieName(testfed.getName(),testjur.getName(),TestConstants.testusername_1)) != null) {
                buf.append(TestConstants.testusername_1 + " has already been authenticated");
            } else {
                buf.append("authenticating " + TestConstants.testusername_1);
                // get a test account in testfed:testjur
                DacsUserAccount testaccount = new DacsUserAccount(testfed, testjur, TestConstants.testusername_1, "yes");
                // authenticate testaccount under testuser
                usercontext.authenticate(testaccount, TestConstants.testpassword_1);
            }
            // the following requires that usercontext be authenticated as a DACS ADMIN_IDENTITY
            buf.append("EchoCookies Jurisdiction Configuration: ");
            out.println(buf);
            JurisdictionConfiguration config = testjur.getConfiguration(usercontext);
            config.dumpConfiguration(out);
            
        } catch (DacsException e) {
            out.println("DacsException: " + e.getMessage());
        } catch (Exception e) {
            out.println("Exception: " + e.getMessage());
            e.printStackTrace(out);
        }
        // usercontext should hold any DACS cookies attached to this request
        // plus those explitly obtained, as through the authentication above
        buf.append("<h2>DACS Credential Cookies in UserContext</h2>");
        for (org.apache.commons.httpclient.Cookie cookie : usercontext.getDacsCookies()) {
            buf.append("<p>Credential: " + cookie + "</p>");
        }
        buf.append("<h2>DACS NAT Cookies in UserContext</h2>");
        for (org.apache.commons.httpclient.Cookie cookie : usercontext.getDacsNatCookies()) {
            buf.append("<p>NAT: " + cookie + "</p>");
        }
    }
}

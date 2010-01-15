/*
 * EditCookies.java
 *
 * Created on April 19, 2006, 12:56 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.web;
import com.fedroot.dacs.services.DACS;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rmorriso
 * @version
 */
public class EditCookies extends DacsServlet {
    
    static Log log = LogFactory.getLog(EditCookies.class);
    static String base;
     
    public void init(ServletConfig servletConfig)
    throws ServletException {
        super.init(servletConfig);
        base  = "https://demo.fedroot.com";
        
    }
         
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String url =  base + request.getRequestURI();
        String name = request.getParameter("signout");
        if (name != null & ! "".equals(name)) { // signout request - delete the cookie
            usercontext.signout(name, response);
            response.sendRedirect(url);
        } else { // just emit the DACS cookies in the request
            // buf.append("<h2>DACS Credential Cookies in UserContext</h2>");
            for (Cookie cookie : usercontext.getDacsCookies()) {
                String username = DACS.getDacsUsername(cookie);
                buf.append("<p>" + username + " -- ");
                buf.append("<a href=\"" + url +
                           "?signout=" + username + "\">Signout</a></p>");
            }
        }
    }
        
}

/*
 * DacsAuthenticate.java
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
import com.fedroot.dacs.UserContext;
import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.services.DACS;
import com.fedroot.dacs.services.DACS.CommonArgs;
import com.fedroot.dacs.test.TestConstants;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *
 * @author rmorriso
 * @version
 */
public class DacsAuthenticate extends HttpServlet {
    private UserContext usercontext;
    // should get the following from a properties file
    private boolean issecure = true;
    private String domain;
    private String path;
    private String dacs_error_url;
    
    public void init() throws ServletException {
        domain = getServletContext().getInitParameter("domain");
        path = getServletContext().getInitParameter("path");
        if (domain == null) {
            throw new ServletException("domain not specified in servlet init");
        }
        if (path == null) {
            throw new ServletException("path not specified in servlet init");
        }
                
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Dacs Authenticate</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet DacsAuthenticate at " + request.getContextPath() + "</h1>");
        // Get the usercontext's session, or if none, create one
        HttpSession session = request.getSession(true);
        usercontext = (UserContext)session.getAttribute("user");
        if (usercontext == null) {
            out.println("new session");
            // kludge to work around session cookies in a proxied deployment
            // (creates 2 JSESSIONID cookies in the client)
            Cookie cookie = new Cookie("JSESSIONID", session.getId());
            cookie.setPath(path);
            response.addCookie(cookie);
            usercontext = UserContext.getInstance(session.getId());
//            usercontext.setDacs902EventHandler(federation, event902handler);
//            usercontext.setDacs905EventHandler(federation, event905handler);
            session.setAttribute("user", usercontext);
        } else {
            out.println("existing session");
        }
        try {
            // set any DACS cookies found in request in usercontext's dacscontext cookie store
            usercontext.setDacsCookies(request, domain, issecure);
            
            out.println("jsessionid: " + session.getId().toString());
            out.println("user: " + usercontext.getName());
        } catch (Exception e) {
            out.println("Exception: " + e.getMessage());
            e.printStackTrace(out);
        }
        try {
//            String querystring = request.getQueryString();
//            if (querystring != null) {
//                String[] kvps = querystring.split("&");
//                for (String kvp : kvps) {
//                    String pair[] = kvp.split("=");
//                    if (pair[0] == DACS.InternalArgs.DACS_ERROR_URL.toString()) {
//                        this.dacs_error_url = pair[1];
//                    }
//                }
//            }
            this.dacs_error_url = request.getParameter("DACS_ERROR_URL");
            // redirect to value of DACS_ERROR_URL
            response.sendRedirect(this.dacs_error_url);
            // load Federation and its jurisdictions
            //   Federation testfed = Federation.getInstance(dacscontext, TestConstants.testbaseuri);
            Federation testfed = Federation.getInstance(usercontext, TestConstants.testbaseuri);
            Jurisdiction testjur = testfed.getJurisdictionByName("TEST");
            // authenticate a DacsUserAccount under this UserContext
            if (usercontext.getCookieByName(DACS.makeCookieName(testfed.getName(),testjur.getName(),TestConstants.testusername_1)) != null) {
                out.println(TestConstants.testusername_1 + " has already been authenticated");
            } else {
                out.println("authenticating " + TestConstants.testusername_1);
                // get a test account in testfed:testjur
                DacsUserAccount testaccount = new DacsUserAccount(testfed, testjur, TestConstants.testusername_1, "yes");
                // authenticate testaccount under testuser
                usercontext.authenticate(testaccount, TestConstants.testpassword_1);
            }
            // the following requires that usercontext be authenticated as a DACS ADMIN_IDENTITY
            out.println("EchoCookies Jurisdiction Configuration: ");
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
        usercontext.dumpDacsCookies(out);
        usercontext.dumpDacsNats(out);
        
        out.println("</body>");
        out.println("</html>");
        
        out.close();
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}

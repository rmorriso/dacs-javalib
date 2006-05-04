/*
 * DacsServlet.java
 *
 * Created on September 22, 2005, 12:56 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.web;
import com.fedroot.dacs.UserContext; 
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author rmorriso
 * @version
 */
public abstract class DacsServlet extends HttpServlet {
    protected Logger logger;
    protected HttpSession session;
    protected UserContext usercontext;
    // should get the following from a properties file
    protected boolean issecure = true;
    protected String domain;
    protected String path;
    protected PrintWriter out;
    protected StringBuffer buf = new StringBuffer();
    
    public void init() throws ServletException {
        logger = Logger.getLogger("com.fedroot");
        domain = getServletContext().getInitParameter("domain");
        path = getServletContext().getInitParameter("proxypath");
        if (domain == null) {
            throw new ServletException("domain not specified in servlet init");
        }
        if (path == null) {
            throw new ServletException("proxypath not specified in servlet init");
        }
        
    }
    
    /** 
     * set session from request, setting session cookie in response if required
     * @param request servlet request
     * @param response servlet response
     */
    private void setSession(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        session = request.getSession(true);
        usercontext = (UserContext)session.getAttribute("user");
        if (usercontext == null) {
            // kludge to work around session cookies in a proxied deployment
            // (creates 2 JSESSIONID cookies in the client)
            Cookie cookie = new Cookie("JSESSIONID", session.getId());
            cookie.setPath(path);
            response.addCookie(cookie);
            // get unique UserContext named by session id
            usercontext = UserContext.getInstance(session.getId());
            logger.debug("new session: " + usercontext);
            session.setAttribute("user", usercontext);
        }
    }
    
    protected void emitBuffer() {
        out.println(buf);
        buf.setLength(0);
        buf.trimToSize();
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException ;
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        out = response.getWriter();
        buf.setLength(0);
        buf.trimToSize();
        // set the usercontext's session, or if none, create one
        setSession(request, response);
        try {
            // set any DACS cookies found in request in usercontext's dacscontext cookie store
            usercontext.setDacsCookies(request, domain, issecure);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
        }
        // call concrete method defined in subclass to do the work
        processRequest(request, response);
        out.close();
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

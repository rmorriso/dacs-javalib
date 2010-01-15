/*
 * EchoCookies.java
 *
 * Created on September 22, 2005, 12:56 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.web;
import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.UserContext;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rmorriso
 * @version
 */
public class DacsCheck extends HttpServlet {
    private DacsContext dacscontext;
    private UserContext user;
    // should get the following from a properties file
    private boolean issecure = true;
    private String domain;
    private String path;
    
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
        out.println("<title>Servlet DacsCheck</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet DacsCheck at " + request.getContextPath() + "</h1>");
        // String username = System.getenv("REMOTE_USER");
        String username = ":bob";
        String resource = "/named-user";
            
        boolean expResult = true;
        try {
            Process child = Runtime.getRuntime().exec("ls /etc");
//            int exitVal = child.waitFor();
//            out.println("return code " + exitVal);
             boolean result = com.fedroot.dacs.DacsCheck.test(username, resource);
             out.println("access to " + resource + ": " + result);
        } catch (Exception e) {
            e.printStackTrace(out);
        }
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

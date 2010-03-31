/*
 * RoleServlet.java
 * 
 * Created on October 18, 2007
 *
 * Copyright 2006-2010 Cisco Systems, Inc. All rights reserved. No portion of this
 * software may be reproduced in any form, or by any means, without prior written
 * permission from Cisco Systems, Inc.
 */
package fedroot.dacs.extras;

import fedroot.database.DatabaseConnection;
import fedroot.database.DbUtils;
import fedroot.ldaplib.LdapUser;
import fedroot.ldaplib.LdapUtil;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;


/**
 * a web service to retrieve a Cisco user's roles information
 * the DACS distributed access control framework is configured to call this
 * web services in signon.cisco.com:/data/dacs/federations/cisco.com/cdo.conf:
 * 
 * <Roles id="getroles">
 *    EXPR 'http("http://signon.cisco.com/local_pnxe_roles/roles", get, username, ${Auth::DACS_USERNAME})'
 * </Roles>
 *
 * @author ricmorri
 */
public class RolesServlet extends HttpServlet {

    private static final String USERNAME = "username";
    private static final String DATASOURCE = "jdbc/app_common";
    private static final String APP_USER = "app_user";

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setStatus(HttpStatus.SC_OK);
        PrintWriter out = response.getWriter();

        // good input - carry on

        if (true) { // set this false to suppress printing out System.out
            PrintStream ps = new PrintStream(new PipedOutputStream());
            System.setOut(ps);
        }


        try {
            String username = getArgString(request, USERNAME, null);
            DatabaseConnection databaseConnection = new DatabaseConnection(DbUtils.getConnection(DATASOURCE));
            DBWorker dbWorker = new DBWorker(databaseConnection, APP_USER);

            try {
                // validate dacsUsername
                User user = dbWorker.getUser(username);
                if (user == null) {
                    throw new Exception("email ID not found for user " + username);
                }
                // get roles from PNXE database
                String roles = user.getRolesString();
                // add cisco_manager role, if applicable
                LdapUser ldapUser = LdapUtil.getUser(username);
                if (isManager(ldapUser)) {
                    roles = (roles == null ? "" : roles) + ",cisco_manager";
                }
                out.print(roles);
            } catch (Exception ex) {
                out.println();
                Logger.getLogger(RolesServlet.class.getName()).log(Level.WARNING, ex.getLocalizedMessage());
            } finally {
                dbWorker.closeConnection();
            }
        } catch (SQLException ex) {
            // TODO derived locale from user request
            Logger.getLogger(RolesServlet.class.getName()).log(Level.WARNING, ex.getLocalizedMessage());
        }
    }

    /**
     *
     * @param ldapUser the Active Directory (AD) user wrapper for a given username (email ID)
     * @return true if ldpaUser is memberOf "allmgrs" in AD
     */
    private boolean isManager(LdapUser ldapUser) {
        return ldapUser != null && ldapUser.getMembership() != null && ldapUser.getMembership().contains("allmgrs");
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

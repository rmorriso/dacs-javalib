/*
 * HealthCheckServlet.java
 *
 * Created on June 19, 2007, 3:03 PM
 */
package cisco.demos;

import cisco.servlets.util.HttpUtil;
import com.fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.utils.DacsUtil;
import java.io.*;

import java.util.ResourceBundle;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.httpclient.HttpStatus;

/**
 *
 * @author ricmorri
 * @version
 */
public class DacsDemoServlet extends HttpServlet {

    private static final String ARG_REQUEST = "request";
    private static final String SESSION_USER = "username";
    // DACS User Authentication
    private static final String DACS_AUTH_JURISDICTION;
    private static final String DACS_BASE_URI;


    static {
        // Static initializer to retrieve information from configuration properties file.
        ResourceBundle config = ResourceBundle.getBundle("cisco.demos.Configuration");
        DACS_BASE_URI = config.getString("dacs_base_uri");
        DACS_AUTH_JURISDICTION = config.getString("dacs_auth_jurisdiction");
    }

    private enum RequestType {
        info, logout
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String requestString = null;
        RequestType requestType = RequestType.info;
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute(SESSION_USER);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.SC_OK);

        // bail out with error message on invalid requests
        try {
            if (request.getQueryString() != null) {
                requestString = HttpUtil.getArgString(request, ARG_REQUEST, null);
                if (requestString != null) {
                    requestType = RequestType.valueOf(requestString.trim());
                } else {
                    throw new IllegalArgumentException("request must be one of " + "info or logout");
                }
            }
            // we have good input - carry on

            switch (requestType) {
                case info:
                    if (username == null) {
                        username = getUsername(request);
                        session.setAttribute(SESSION_USER, username);
                        out.println("initialized session user from DACS credential: " + username);
                    } else {
                        out.println("username found in session: " + username);
                    }
                    break;
                case logout:
                    dacsLogout(session, response);
                    break;
            }
        } catch (DacsException ex) {
            out.println("DACS exception: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            out.println("invalid request: " + requestString);
        } finally {
            out.close();
        }
    }

    private String getUsername(HttpServletRequest request) throws DacsException {
        return DacsUtil.resolveUsername(DACS_BASE_URI, DACS_AUTH_JURISDICTION, request);
    }

    private void dacsLogout(HttpSession session, HttpServletResponse response) throws DacsException, IOException {
        session.removeAttribute(SESSION_USER);
        response.sendRedirect(DACS_BASE_URI + "/dacs_signout");
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
        return "NSSTG Custom Health Checks of Tomcat Platform";
    }
    // </editor-fold>
}


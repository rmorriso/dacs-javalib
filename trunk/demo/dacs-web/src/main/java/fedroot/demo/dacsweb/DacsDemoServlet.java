/*
 * DacsDemoServlet.java
 *
 * Created on June 19, 2007, 3:03 PM
 */
package fedroot.demo.dacsweb;

import fedroot.dacs.exceptions.DacsException;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.http.HttpStatus;


/**
 *
 * @author ricmorri
 * @version
 */
public class DacsDemoServlet extends HttpServlet {

    private static final String ARG_REQUEST = "request";
    private static String SESSION_USERNAME = "session_username";

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
//        String username = request.getParameter("username");
        String username = (String) session.getAttribute(SESSION_USERNAME);

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.SC_OK);

        // bail out with error message on invalid requests
        try {
            if (request.getQueryString() != null) {
                requestString = request.getParameter(ARG_REQUEST);
                if (requestString != null) {
                    requestType = RequestType.valueOf(requestString.trim());
                } else {
                    throw new IllegalArgumentException("request must be one of " + "info or logout");
                }
            }
            // we have good input - carry on

            switch (requestType) {
                case info:
                    if (username != null) {
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

    private void dacsLogout(HttpSession session, HttpServletResponse response) throws DacsException, IOException {
        session.removeAttribute(SESSION_USERNAME);
//        response.sendRedirect(DACS_BASE_URI + "/dacs_signout");
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


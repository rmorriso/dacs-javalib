/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fedroot.demo.dacsweb;

import fedroot.servlet.ParameterValidator;
import fedroot.servlet.ServiceContext;
import fedroot.servlet.WebService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rmorriso
 */
@WebServlet(name = "DemoService", urlPatterns = {"/user"})
public class DemoWebService extends WebService {

    private static final String PARAM_REQUEST = "request";
    private static String SESSION_USERNAME = "session_username";

    // TODO do value checking for parameters in ParameterValidation (data type and enumerated value sets)
    private enum RequestType {

        info, logout
    }

    @Override
    protected void initWebService() {
        setContentType(ContentType.PLAIN);
        ParameterValidator parameterValidator = new ParameterValidator(ParameterValidator.ValidationType.ALL);
        parameterValidator.addParameter(PARAM_REQUEST);
        addParameterValidator(parameterValidator);
    }

    @Override
    protected void printResponse(PrintWriter out, ServiceContext serviceContext) throws Exception {
        RequestType requestType = RequestType.info;
        String requestString = serviceContext.getParameters().getString(PARAM_REQUEST);
        String username = (String) serviceContext.getSessionAttribute(SESSION_USERNAME);
        if (requestString != null) {
            requestType = RequestType.valueOf(requestString.trim());
            switch (requestType) {
                case info:
                    if (username != null) {
                        out.println("username found in session: " + username);
                    }
                    break;
                case logout:
                    serviceContext.removeSessionAttribute(SESSION_USERNAME);
                    out.println("removed username " + username + " from session");
                    break;
            }
        } else {
            throw new IllegalArgumentException("request must be one of " + "info or logout");
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

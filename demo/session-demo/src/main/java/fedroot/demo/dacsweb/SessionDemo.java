/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fedroot.demo.dacsweb;

import fedroot.dacs.entities.Credential;
import fedroot.dacs.servlet.SessionManager;
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
 * an example stateful DACS-wrapped Web service living behind a DacsFilter
 * the DacsFilter extracts the DACS credentials from the HttpRequest and sets
 * a SessionManager in the session
 * @author rmorriso
 */
@WebServlet(name = "SessionDemo", urlPatterns = {"/user"})
public class SessionDemo extends WebService {

    private static final String PARAM_REQUEST = "request";
    private static String SESSION_MANAGER = "session_manager";

    // TODO do value checking for parameters in ParameterValidation (data type and enumerated value sets)
    private enum RequestType {

        federation_details, info, reset
    }

    @Override
    protected void initWebService() {
        setContentType(ContentType.PLAIN);
        ParameterValidator parameterValidator = new ParameterValidator(ParameterValidator.ValidationType.ALL);
        parameterValidator.addParameter(PARAM_REQUEST);
        addParameterValidator(parameterValidator);
    }

    @Override
    protected void printResponse(HttpServletResponse response, ServiceContext serviceContext) throws Exception {
        PrintWriter out = response.getWriter();
        HttpServletRequest request = serviceContext.getRequest();
        RequestType requestType = RequestType.info;
        String requestString = serviceContext.getParameters().getString(PARAM_REQUEST);
        SessionManager sessionManager = (SessionManager) serviceContext.getSessionAttribute(SESSION_MANAGER);

        if (sessionManager != null) {
            if (requestString != null) {
                requestType = RequestType.valueOf(requestString.trim());
                switch (requestType) {
                    case federation_details:

                        break;
                    case info:
                        out.println("Info from SessionManager found in session:");
                        Credential credential = sessionManager.getSelectedCredential();
                        if (credential != null) {
                            out.println(" Federation: " + credential.getFederationName());
                            out.println(" Jurisdiction: " + credential.getJurisdictionName());
                            out.println(" Username: " + credential.getName());
                        } else {
                            out.println("No effective DACS credential is set in SessionManager.");
                        }
                        break;
                    case reset:
                        if (sessionManager != null) {
                            serviceContext.removeSessionAttribute(SESSION_MANAGER);
                            out.println("removed SessionManager " + sessionManager + " from session");
                        } else {
                            out.println("No SessionManager was found in session.");
                        }
                        break;
                }
            } else {
                throw new IllegalArgumentException("request must be one of " + "info or logout");
            }
        } else {
            out.println("No SessionManager found in session");
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

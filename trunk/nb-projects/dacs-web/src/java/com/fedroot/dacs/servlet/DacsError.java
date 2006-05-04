/*
 * DacsError.java
 *
 * Created on January 3, 2006, 9:32 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.servlet;

import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.jsptaglib.Constants;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author rmorriso
 */

public class DacsError {
    private int status;
    private Exception exception;
    private static final String SHOW_STACK_TRACE = "showStackTrace";
    
    /** 
     * Creates a new instance of DacsError 
     */
    public DacsError(HttpServletRequest request) {
        String status = request.getParameter("DACS_ERROR_CODE");
        if (status != null) {
            try {
                this.status = Integer.parseInt(status);
            } catch (Exception e) {
                this.status = 200; // RNM -1;
            }
        } else {
            this.status = 200; // RNM -1;
        }
        this.exception = (Exception) request.getAttribute(Constants.ERROR);
    }
    
    
    /**
     * Using the error number taken from the javax.servlet.error.status_code,    */
    /* match it against keys found in the text_en text_fr file to display message*/
    
    public String getErrorText(){
        return DacsStatus.getStatusText(this.status);
    }
    
    /**
     *  Using the error number taken from the javax.servlet.error.status_code
     * match it agianst keys found in the text_en text_fr file to display message
     */
    public String getErrorType() {
        return "DACS Event: " + this.status;
    }
    
    /**
     * Used for testing purposes only
     */
    public String dacsCode(HttpServletRequest request){
        String test = request.getParameter("DACS_ERROR_CODE");
        return test;
    }
    
    /**
     * This method saves the "javax.servlet.error.status_code" within the session and
     * each time the page is diplayed (including after language choice) we check if a
     * new "javax.servlet.error.status_code" is present, if not, we do not change the
     * saved session variable holding the original "javax.servlet.error.status_code"
     *
     * This method is needed because of the language button. When an error is thrown,
     * the "javax.servlet.error.status_code" variable is initialized with the error code
     * If the user clicks the language button, an error is not thrown
     * when re-displaying the page, resulting in destroying the
     * variable held by the "javax.servlet.error.status_code".
     */
    public void setInSession(HttpSession session) throws Exception{
        /* Set the error in the session*/
        session.setAttribute("myCode", this);
    }
    
    public Exception getError() {
        return this.exception;
    }
    
    public void printErrorMessage(JspWriter out, Properties text)
    throws Exception {
        
        out.write("<p>" + this.exception.getMessage() + "</p>");
        
        if (this.exception.getStackTrace() != null) {
            
            out.write(
                    "<form method=\"post\" action=\"" + Constants.ERROR_JSP + "\">" +
                    "<input type=\"submit\" class=\"button\" name=\"" + SHOW_STACK_TRACE +
                    "\" value=\"" + text.getProperty("more.error") + "\"/>" +
                    "</form>");
        } else {
            out.write("<p>No details provided ...</p>");
        }
    }
    
}

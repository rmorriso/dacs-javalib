/*
 * DacsGetMethod.java
 *
 * Created on September 16, 2005, 10:50 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.http;

import com.fedroot.dacs.services.DACS;
import fedroot.util.httpclient.NameValueSet;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * DACS-aware subclass of HttpClient GetMethod
 * @author rmorriso
 */
public class DacsGetMethod extends GetMethod {
    NameValueSet nvs;
    
    /**
     * constructor for DacsGetMethod, building NameValueSet from uri
     * Query String if applicable;
     * sets custom DACS_HANDLER header in request - used
     * in DACS server configuration directives to select the appropriate
     * form of event handling (redirect to HTML or XML reply)
     * @param uri uri for which HTTP Get is to be executed
     */
    public DacsGetMethod(String uri) {
        super(uri);
        this.nvs = new NameValueSet<String>();
        if (uri.contains("?")) {
            String querystring = uri.substring(uri.indexOf('?') + 1) + "&";
            String kvp[] = querystring.split("&");
            for (String pair : kvp) {
                String x[] = pair.split("=");
                this.setNVP(x[0],x[1]);
            }
        }
        this.addRequestHeader("DACS_HANDLER", "xml");
    }
    
    /**
     * constructor for DacsGetMethod;
     * sets custom DACS_HANDLER header in request - used
     * in DACS server configuration directives to select the appropriate
     * form of event handling (redirect to HTML or XML reply)
     * @param uri uri for GET request
     * @param nvs set of name-value pairs used to construct Query_String for DacsGetMethod
     * @throws java.lang.Exception TODO
     */
    public DacsGetMethod(String uri, NameValueSet nvs) {
        super(uri);
        this.nvs = nvs;
        this.setQueryString(nvs.getNVPArray());
        this.addRequestHeader("DACS_HANDLER", "xml");
    }
    
    /**
     * get DACS status line resulting from execution DacsGetMethod
     * @return 
     */
    public String getDacsStatusLine() {
        Header dacsheader = super.getResponseHeader("DACS-Status-Line");
        if (dacsheader != null) {
            return dacsheader.toExternalForm();
        } else {
            return "DACS-1.4.9 798 Access granted";
        }
    }
    
    /**
     * get DACS status code resulting from execution of DacsGetMethod
     * TODO: fixme should return enumerated type rather than int
     * @return 
     */
    public int getDacsStatusCode() {
        Header dacsheader = super.getResponseHeader("DACS-Status-Line");
        if (dacsheader == null) { // occurs for check_fail success
            int httpstatus = super.getStatusCode();
            if (httpstatus == HttpStatus.SC_OK) {
                return DacsStatus.SC_DACS_ACCESS_GRANTED;
            } else {
                return DacsStatus.SC_DACS_ACCESS_ERROR;
            }
        } else {
            // extract code from custom header, for example:
            // "DACS-Status-Line" ":" DACS-Version SP ACS-Status-Code SP Reason-Phrase CRLF
            return Integer.decode(dacsheader.toExternalForm().split(" ")[2]);
        }
    }
  
    /**
     * set a name-value pair in the NameValueSet associated with request
     * @param name name to be set
     * @param value value to set
     */
    public void setNVP(String name, String value) {
        nvs.setValue(name, value);
        // update QueryString from NameValueSet
        this.setQueryString(nvs.getNVPArray());
    }
    
    /**
     * modify DacsGetMethod QUERY_STRING so that DACS causes request to
     * be ignored, returning instead the access control decision
     * it would render if the request were actually invoked
     * 
     * 
     * @param check_type the type of check_type (either check_only or check_fail);
     * check_only always causes DACS to return an access control 
     * decision, while check_fail allow the request to
     * execute if access would be permitted
     * @param format_type format_type of the XML reply (HTML, XML, XMLSCHEMA)
     * @throws java.lang.Exception TODO
     */
    public void setDacsAcs(DACS.AcsCheck check_type, DACS.ReplyFormat format_type) {
        nvs.setValue(
           DACS.CommonArgs.DACS_ACS,
           DACS.makeDacsAcsValue(check_type, format_type));
        this.setQueryString(nvs.getNVPArray());
    }
    
    /**
     * unset DacsAcs checking
     */
    public void unsetDacsAcs() {
        try {
            nvs.remove(DACS.CommonArgs.DACS_ACS);
            this.setQueryString(nvs.getNVPArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * return string representation of DacsGetMethod
     * @return 
     */
    public String toString() {
        String uristring;
        try {
            uristring = this.getURI().toString();
        } catch (URIException ex) {
            uristring = "#error parsing uri#";
        }
       return uristring;
    }
}

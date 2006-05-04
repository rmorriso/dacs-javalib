/*
 * DacsHeadMethod.java
 *
 * Created on October 23, 2005, 10:50 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.http;

import com.fedroot.dacs.services.DACS;
import com.fedroot.util.httpclient.NameValueSet;
import org.apache.commons.httpclient.methods.HeadMethod;

/**
 * DACS-aware subclass of HttpClient PHeadMethod
 * @author rmorriso
 */
public class DacsHeadMethod extends HeadMethod {
    NameValueSet nvs;
    
    /**
     * constructor for DacsPostMethod;
     * sets custom DACS_HANDLER header in request - used
     * in DACS server configuration directives to select the appropriate
     * form of event handling (redirect to HTML or XML reply)
     * @param uri uri for which HTTP Get is to be executed
     */
    public DacsHeadMethod(String uri) {
        super(uri);
        this.nvs = new NameValueSet<String>();
    }
    
    /**
     * constructor for DacsPostMethod;
     * sets custom DACS_HANDLER header in request - used
     * in DACS server configuration directives to select the appropriate
     * form of event handling (redirect to HTML or XML reply)
     * @param uri uri for GET request
     * @param nvs set of name-value pairs used to construct Query_String
     * @throws java.lang.Exception TODO
     */
    public DacsHeadMethod(String uri, NameValueSet nvs) 
    throws Exception {
        super(uri);
        this.nvs = nvs;
        this.setQueryString(nvs.getNVPArray());
    }
  
    /**
     * set a name-value pair in the NameValueSet associated with request
     * @param name name to be set
     * @param value value to set
     * @throws java.lang.Exception TODO
     */
    public void setNVP(String name, String value)
    throws Exception {
        nvs.setValue(name, value);
        // update QueryString
        this.setQueryString(nvs.getNVPArray());
    }
    /**
     * modify DacsGetMethod so that DACS cause request to
     * be ignored, returning instead the access control decision
     * it would render if the request were actually invoked
     * @param check the type of check (either check_only or check_fail);
     * check_only always causes DACS to return an access control 
     * decision, while check_fail allow the request to
     * execute if access would be permitted
     * @param format format of the XML reply (HTML, XML, XMLSCHEMA)
     * @throws java.lang.Exception TODO
     */ 
    public void setDacsAcs(DACS.AcsCheck check, DACS.ReplyFormat format) 
    throws Exception {
        nvs.setValue(
           DACS.CommonArgs.DACS_ACS,
           DACS.makeDacsAcsValue(check, format));
        this.setQueryString(nvs.getNVPArray());
    }
 
    /**
     * unset DacsAcs checking
     * @throws java.lang.Exception TODO
     */
    public void unsetDacsAcs() 
    throws Exception {
        nvs.remove(DACS.CommonArgs.DACS_ACS);
        this.setQueryString(nvs.getNVPArray());
    }
}

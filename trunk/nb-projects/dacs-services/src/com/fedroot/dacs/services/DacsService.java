/*
 * DacsAuthenticateService.java
 *
 * Created on August 30, 2005, 12:12 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.services;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsPostMethod;
import fedroot.util.httpclient.NameValueSet;
import com.fedroot.dacs.services.DACS.ServiceName;
import org.apache.commons.httpclient.NameValuePair;


/**
 * All DACS services extend this abstract class
 * @author rmorriso
 */
public abstract class DacsService {
    public ServiceName name;
    protected String serviceuri;
    protected NameValueSet nvs;

    /**
     * constructor for DacsService
     * @param serviceuri uri for DACS service
     * @param args query string arguments for DACS service
     */
    public DacsService(String serviceuri, String... args) {
        this.serviceuri = serviceuri;
    }
    
    /**
     * get DacsGetMethod for this DacsService
     * @return DacsGetMethod with which to invoke service
     * @throws java.lang.Exception TODO
     */
    public DacsGetMethod getDacsGetMethod() {
        DacsGetMethod dacsget = new DacsGetMethod(this.serviceuri, this.nvs);
        return dacsget;
    }
    
    /**
     * get DacsPostMethod for this DacsService
     * @return DacsGetMethod with which to invoke service
     * @throws java.lang.Exception TODO
     */
    public DacsPostMethod getDacsPostMethod() {
        DacsPostMethod dacspost = new DacsPostMethod(this.serviceuri, this.nvs);
        return dacspost;
    }
    
    /**
     * getter for DacsService.uri
     * @return the uri to be used for invoking the service
     */
    public String getServiceUri() {
        return serviceuri;
    }
    
    /**
     * getter for DacsService.nvs
     * @return the NVS containing name-value pairs for the service
     */
    public NameValueSet getNVS() {
        return this.nvs;
    }
    
    /**
     * getter for DacsService to NVPArray
     * @return the NVPArray (constructed from NVS)
     * @throws java.lang.Exception TODO
     */
    public NameValuePair[] getNVPArray()
    throws Exception {
        return nvs.getNVPArray();
    }
}

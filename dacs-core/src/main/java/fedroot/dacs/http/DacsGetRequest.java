/*
 * DacsGetRequest.java
 * Created on Jan 15, 2010 8:02:49 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.http;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */

import fedroot.dacs.client.DacsWebServiceRequest;
import fedroot.dacs.exceptions.DacsException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;




/**
 * This example demonstrates the use of a local HTTP context populated with
 * custom attributes.
 */
public class DacsGetRequest {

    private HttpGet httpGet;

    public DacsGetRequest(DacsWebServiceRequest dacsWebServiceRequest) {
        this.httpGet = new HttpGet(dacsWebServiceRequest.getURI());
    }

    public DacsGetRequest(URI uri) {
        this.httpGet = new HttpGet(uri);
    }

    public HttpGet getHttpGet() {
        return this.httpGet;
    }

    ResponseHandler<InputStream> responseHandler = new ResponseHandler<InputStream>() {
        @Override
        public InputStream handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                entity = new BufferedHttpEntity(entity);
                return entity.getContent();
            } else {
                return null;
            }
        }
    };

    ResponseHandler<String> handler = new ResponseHandler<String>() {
        @Override
        public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            } else {
                return null;
            }
        }
    };


    public InputStream getInputStream(HttpClient httpClient, HttpContext httpContext) throws DacsException {
        try {
            return httpClient.execute(httpGet, responseHandler, httpContext);
        } catch (IOException ex) {
            Logger.getLogger(DacsGetRequest.class.getName()).log(Level.SEVERE, null, ex);
            throw new DacsException("DACS HTTP Get Request failed: " + ex.getMessage());
        } finally {
           // TODO need to close connection, or use multithreaded connection manager or SOMETHING!
        }
    }

    public String getString(HttpClient httpClient, HttpContext httpContext) throws DacsException {
        try {
            return httpClient.execute(httpGet, handler, httpContext);
        } catch (IOException ex) {
            Logger.getLogger(DacsGetRequest.class.getName()).log(Level.SEVERE, null, ex);
            throw new DacsException("DACS HTTP Get Request failed: " + ex.getMessage());
        } finally {
           // TODO need to close connection, or use multithreaded connection manager or SOMETHING!
        }
    }

}


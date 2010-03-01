/*
 * DacsClientContext.java
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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.protocol.HttpContext;


/**
 * This example demonstrates the use of a local HTTP context populated with
 * custom attributes.
 */
public class DacsPostRequest {

    private URI baseUri;
    private HttpPost httpPost;

    public DacsPostRequest(URI baseUri) {
        this.baseUri = baseUri;
    }

    public DacsPostRequest(DacsWebServiceRequest dacsWebServiceRequest) {
        this.httpPost = new HttpPost(dacsWebServiceRequest.getURI());
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(dacsWebServiceRequest.getNameValuePairs(), "us-ascii");
            // urlEncodedFormEntity = new UrlEncodedFormEntity(dacsWebServiceRequest.getNameValuePairs(), "UTF-8");
            this.httpPost.setEntity(urlEncodedFormEntity);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DacsPostRequest.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Invalid DacsWebServiceRequest parameters " + ex.getMessage());
        }

    }

    public HttpPost getHttpPost() {
        return this.httpPost;
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

    public InputStream getInputStream(HttpClient httpClient, HttpContext httpContext) throws DacsException {
        try {
            return httpClient.execute(httpPost, responseHandler, httpContext);
        } catch (IOException ex) {
            Logger.getLogger(DacsGetRequest.class.getName()).log(Level.SEVERE, null, ex);
            throw new DacsException("DACS HTTP Post Request failed: " + ex.getMessage());
        } finally {
           // TODO need to close connection, or use multithreaded connection manager or SOMETHING!
        }
    }

}


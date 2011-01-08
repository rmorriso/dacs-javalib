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
import fedroot.dacs.exceptions.DacsException;
import fedroot.servlet.NameFilePair;
import fedroot.servlet.WebServiceRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
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

    public DacsPostRequest(WebServiceRequest webServiceRequest) {
        if (webServiceRequest.getEnclosureType() == null || "application/x-www-form-urlencoded".equals(webServiceRequest.getEnclosureType())) {
            this.httpPost = urlEncodedPost(webServiceRequest);
        } else {
            this.httpPost = multipartPost(webServiceRequest);
        }
    }

    private HttpPost urlEncodedPost(WebServiceRequest webServiceRequest) {
        HttpPost urlEncodedPost = new HttpPost(webServiceRequest.getBaseURI());
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(webServiceRequest.getNameValuePairs());
            // urlEncodedFormEntity = new UrlEncodedFormEntity(dacsWebServiceRequest.getNameValuePairs(), "UTF-8");
            urlEncodedPost.setEntity(urlEncodedFormEntity);
            return urlEncodedPost;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DacsPostRequest.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Invalid DacsWebServiceRequest parameters " + ex.getMessage());
        }
    }

    /**
     * this is a "hybrid" multipart/form-data with parameters passed in the query string
     * and only file(s) included in the multipart entity
     * @param webServiceRequest
     * @return
     */
    private HttpPost multipartPost(WebServiceRequest webServiceRequest) {
        HttpPost multipartPost = new HttpPost(webServiceRequest.getURI());
        MultipartEntity multipartEntity = new MultipartEntity();

        for (NameFilePair nameFilePair : webServiceRequest.getNameFilePairs()) {
            multipartEntity.addPart(nameFilePair.getName(), nameFilePair.getFileBody());
        }
        multipartPost.setEntity(multipartEntity);
        return multipartPost;
    }

    /**
     * the following, which includes parameters in the multipart entity is not grokked
     * by DACS multipart parsing
     * @param webServiceRequest
     * @return
     */
    private HttpPost multipartBrokenPost(WebServiceRequest webServiceRequest) {
        try {
            HttpPost multipartPost = new HttpPost(webServiceRequest.getBaseURI());
//            multipartPost.setHeader("Content-Type", webServiceRequest.getEnclosureType());
            multipartPost.setHeader("Content-Transfer-Encoding", "7bit");
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "----HttpClientBoundarynSbUMwsZpJVNlFYK", Charset.forName("US-ASCII"));

            for (NameValuePair nameValuePair : webServiceRequest.getNameValuePairs()) {
                multipartEntity.addPart(nameValuePair.getName(), new StringBody(new String(nameValuePair.getValue().getBytes(), Charset.forName("US-ASCII")), Charset.forName("US-ASCII")));
            }
            for (NameFilePair nameFilePair : webServiceRequest.getNameFilePairs()) {
                multipartEntity.addPart(nameFilePair.getName(), nameFilePair.getFileBody());
            }
            multipartPost.setEntity(multipartEntity);
            return multipartPost;
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

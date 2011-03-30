/*
 * DacsResponse.java
 * Created on Jan 05, 2011 8:02:49 PM.
 * Copyright (c) 2011 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.http;

import fedroot.dacs.exceptions.DacsException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BufferedHttpEntity;

/**
 * a DACS wrapper class for HttpResponse
 * @author rmorriso
 */
public class DacsResponse {

    private HttpResponse httpResponse;
    private InputStream inputStream;

    private static final Logger logger = Logger.getLogger(DacsResponse.class.getName());

    public DacsResponse(HttpResponse httpResponse) throws DacsException {
        this.httpResponse = httpResponse;
        if (httpResponse.getEntity() != null) {
            try { // we use a BufferedHttpEntity so we can reset the input stream  after a DacsCheckRequest
                this.inputStream = new BufferedHttpEntity(httpResponse.getEntity()).getContent();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
                throw new DacsException(ex.getLocalizedMessage());
            }
        }
    }

    /**
     * @return the httpResponse
     */
    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    /**
     * @param httpResponse the httpResponse to set
     */
    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**
     * @return the inputStream
     */
    public InputStream getInputStream() throws DacsException {
        return inputStream;
    }

    /**
     * @param inputStream the inputStream to set
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

}

/*
 * WebServiceEntityLoader.java
 * Created on Jan 16, 2010 5:00:08 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.entities;

import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsResponse;
import fedroot.servlet.HttpRequestType;
import fedroot.servlet.WebServiceRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
abstract public class WebServiceEntityLoader {

    private static final Logger logger = Logger.getLogger(WebServiceEntityLoader.class.getName());

    private WebServiceRequest webServiceRequest;
    private HttpRequestType httpRequestType;

    /**
     * subclasses are expected to initialize the appropriate DacsWebServiceRequest
     */
    public WebServiceEntityLoader(WebServiceRequest webServiceRequest) {
        // default request type is GET
        this(webServiceRequest, HttpRequestType.GET);
    }

    public WebServiceEntityLoader(WebServiceRequest webServiceRequest, HttpRequestType httpRequestType) {
        this.webServiceRequest = webServiceRequest;
        this.httpRequestType = httpRequestType;
    }

    /**
     * unmarshall the XML entity in the document returned from WebServiceRequest
     * note this is intolerant to ill-formed XML in the response document
     * @param dacsClientContext the DacsClientContext to execute the WebServiceRequest
     * @return the XML entity
     * @throws DacsException
     */
    protected Object load(DacsClientContext dacsClientContext, String contextPath) throws DacsException {
        Object entity = null;
        DacsResponse dacsResponse = dacsClientContext.getDacsResponse(webServiceRequest);
        InputStream inputStream = dacsResponse.getInputStream();
        try {
            JAXBContext jc = JAXBContext.newInstance(contextPath);
            Unmarshaller um = jc.createUnmarshaller();
            entity = um.unmarshal(inputStream);
        } catch (JAXBException ex) {
            throw new DacsException("error unmarshalling XML from input stream: " + ex.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(CredentialsLoader.class.getName()).log(Level.SEVERE, ex.getMessage());
            }
        }
        return entity;
    }

}

/*
 * WebServiceEntityLoader.java
 * Created on Jan 16, 2010 5:00:08 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.entities;

import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
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
        this.webServiceRequest = webServiceRequest;
        // default request type is GET
        this.httpRequestType = HttpRequestType.GET;
    }

    public WebServiceEntityLoader(WebServiceRequest webServiceRequest, HttpRequestType httpRequestType) {
        this.webServiceRequest = webServiceRequest;
        // default request type is GET
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
        InputStream inputStream = getXmlStream(dacsClientContext);
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




    /**
     * Returns the XML stream required to instantiate this entity.
     * @param dacsClientContext the dacsClientContext to issue the Web service request
     * @return the XML document returned in the Web service request
     */
    protected InputStream getXmlStream(DacsClientContext dacsClientContext) throws DacsException {
        if (httpRequestType == HttpRequestType.GET) {
            return dacsClientContext.executeGetRequest(webServiceRequest);
        } else {
            return dacsClientContext.executePostRequest(webServiceRequest);
        }
    }
}

/*
 * DacsCheckLoader.java
 * Created on Jan 16, 2010 5:48:01 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.entities;

import com.fedroot.dacs.AccessDenied;
import com.fedroot.dacs.DacsAcs;
import fedroot.dacs.client.DacsCheckRequest;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
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
public class DacsCheckLoader {

    private static final Logger logger = Logger.getLogger(DacsCheckLoader.class.getName());

    private DacsCheckRequest webServiceRequest;

    /**
     * subclasses are expected to initialize the appropriate DacsWebServiceRequest
     */

    public DacsCheckLoader(DacsCheckRequest webServiceRequest) {
        this.webServiceRequest = webServiceRequest;
    }

    /**
     * unmarshall the XML entity in the document returned from WebServiceRequest
     * note this is intolerant to ill-formed XML in the response document
     * @param dacsClientContext the DacsClientContext to execute the WebServiceRequest
     * @return the XML entity
     * @throws DacsException
     */
    public InputStream getInputStream(DacsClientContext dacsClientContext) throws DacsException, IOException {
        InputStream inputStream = getXmlStream(dacsClientContext);
        try {
            JAXBContext jc = JAXBContext.newInstance(DacsAcs.class);
            Unmarshaller um = jc.createUnmarshaller();
            DacsAcs dacsAcs = (DacsAcs) um.unmarshal(inputStream);
            AccessDenied accessDenied = dacsAcs.getAccessDenied();
            if (accessDenied.getEvent900() != null) {
                throw new DacsException(900, accessDenied.getEvent900().getMessage());
            } else if (accessDenied.getEvent901() != null) {
                throw new DacsException(901, accessDenied.getEvent901().getMessage());
            } else if (accessDenied.getEvent902() != null) {
                throw new DacsException(902, accessDenied.getEvent902().getMessage());
            } else if (accessDenied.getEvent903() != null) {
                throw new DacsException(903, accessDenied.getEvent903().getMessage());
            } else if (accessDenied.getEvent904() != null) {
                throw new DacsException(904, accessDenied.getEvent904().getMessage());
            } else {

            }
            try {
                if (dacsAcs != null && inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
            }
        } catch (JAXBException ex) {
            inputStream.reset();
            return inputStream;
        }
        return inputStream;
    }

    /**
     * Returns the XML stream required to instantiate this entity.
     * @param dacsClientContext the dacsClientContext to issue the Web service request
     * @return the XML document returned in the Web service request
     */
    private InputStream getXmlStream(DacsClientContext dacsClientContext) throws DacsException {
        switch (webServiceRequest.getHttpRequestType()) {
            case GET:
                return dacsClientContext.executeGetRequest(webServiceRequest);
            case POST:
                return dacsClientContext.executePostRequest(webServiceRequest);
            case DELETE:
            case HEAD:
            case PUT:
            case RACE:
            default:
                return null;
        }
    }

}

/*
 * WebServiceEntityLoader.java
 * Created on Jan 16, 2010 5:00:08 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.entities;

import fedroot.dacs.client.DacsWebServiceRequest;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
abstract public class WebServiceEntityLoader {

    private DacsWebServiceRequest dacsWebServiceRequest;

    /**
     * subclasses are expected to initialize the appropriate DacsWebServiceRequest
     */
    public WebServiceEntityLoader(DacsWebServiceRequest dacsWebServiceRequest) {
        this.dacsWebServiceRequest = dacsWebServiceRequest;
    }
    

    /**
     * unmarshall the XML entity in the document returned from DacsWebServiceRequest
     * @param dacsClientContext the DacsClientContext to execute the DacsWebServiceRequest
     * @return the XML entity
     * @throws DacsException
     */
    protected Object load(DacsClientContext dacsClientContext) throws DacsException {
        Object entity = null;
        InputStream inputStream = getXmlStream(dacsClientContext);
        try {
            JAXBContext jc = JAXBContext.newInstance("com.fedroot.dacs");
            Unmarshaller um = jc.createUnmarshaller();
            entity = um.unmarshal(inputStream);
        } catch (Exception ex) {
            throw new DacsException("error unmarshalling XML from input stream: " + ex.getMessage());
        } finally {
            try {
                if (inputStream != null) inputStream.close();
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
    private InputStream getXmlStream(DacsClientContext dacsClientContext)  throws DacsException {
            return dacsClientContext.executeGetRequest(dacsWebServiceRequest);
    }

}

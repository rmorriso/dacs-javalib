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
import fedroot.dacs.http.DacsResponse;
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

    private DacsCheckRequest dacsCheckRequest;

    /**
     * subclasses are expected to initialize the appropriate DacsWebServiceRequest
     */

    public DacsCheckLoader(DacsCheckRequest dacsCheckRequest) {
        this.dacsCheckRequest = dacsCheckRequest;
    }

    /**
     * unmarshall the XML entity in the document returned from WebServiceRequest
     * note this is intolerant to ill-formed XML in the response document
     * @param dacsClientContext the DacsClientContext to execute the WebServiceRequest
     * @return the DacsResponse
     * @throws DacsException
     */

    public DacsResponse getDacsResponse(DacsClientContext dacsClientContext) throws DacsException, IOException {
        DacsResponse dacsResponse = dacsClientContext.getDacsResponse(dacsCheckRequest);
        InputStream inputStream = dacsResponse.getInputStream();
        try { // if access is denied by DACS response contains and DACS ACS document
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
            } finally {
                return null;
            }
        } catch (JAXBException ex) { // failed to parse DACS ACS document - access was allowed
            inputStream.reset();
            return dacsResponse;
        }
    }

}

/*
 * CredentialsLoader.java
 * Created on Jan 16, 2010 5:48:01 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.entities;

import com.fedroot.dacs.DacsCurrentCredentials;
import fedroot.dacs.client.DacsCurrentCredentialsRequest;
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
public class CredentialsLoader extends AbstractEntityLoader {

    private Jurisdiction jurisdiction;
    private Credentials credentials;

    public CredentialsLoader(DacsClientContext dacsClientContext, Jurisdiction jurisdiction) {
        super(dacsClientContext);
        this.jurisdiction = jurisdiction;
    }

    /**
     * Returns the DACS current credentials
     *
     * @return the federation
     */
    public Credentials getCredentials() {
        return this.credentials;
    }

    @Override
    protected InputStream getXmlStream(DacsClientContext dacsClientContext) throws DacsException, IOException {
            DacsCurrentCredentialsRequest dacsCurrentCredentialsRequest = new DacsCurrentCredentialsRequest(jurisdiction);
            return dacsClientContext.executeGetRequest(dacsCurrentCredentialsRequest);
    }

    @Override
    protected void loadEntityFromStream(InputStream inputStream) throws DacsException {
        try {
            JAXBContext jc = JAXBContext.newInstance("com.fedroot.dacs");
            Unmarshaller um = jc.createUnmarshaller();
            DacsCurrentCredentials dacsCurrentCredentials = (DacsCurrentCredentials) um.unmarshal(inputStream);
            credentials = new Credentials(dacsCurrentCredentials.getFederationName(), dacsCurrentCredentials.getFederationDomain());
            for(DacsCurrentCredentials.Credentials credential : dacsCurrentCredentials.getCredentials()) {
                credentials.addCredential(new Credential(credential.getFederation(), credential.getJurisdiction(), credential.getName(), credential.getRoles(), credential.getAuthStyle(), credential.getCookieName()));
            }
        } catch (JAXBException ex) {
            Logger.getLogger(CredentialsLoader.class.getName()).log(Level.SEVERE, ex.getMessage());
        } catch (IllegalStateException ex) {
            Logger.getLogger(CredentialsLoader.class.getName()).log(Level.SEVERE, ex.getMessage());
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(CredentialsLoader.class.getName()).log(Level.SEVERE, ex.getMessage());
            }
        }
    }

}

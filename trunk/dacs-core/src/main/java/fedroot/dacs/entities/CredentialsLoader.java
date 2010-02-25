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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
public class CredentialsLoader extends AbstractEntityLoader {

    private Jurisdiction jurisdiction;
    private Set<Credential> credentials;

    public CredentialsLoader(DacsClientContext dacsClientContext, Jurisdiction jurisdiction) {
        super(dacsClientContext);
        this.jurisdiction = jurisdiction;
    }

    /**
     * Returns the DACS current credentials
     *
     * @return the federation
     */
    public Set<Credential> getCredentials() {
        return this.credentials;
    }

    @Override
    protected HttpEntity getXmlForEntity(DacsClientContext dacsClientContext) throws DacsException {
        HttpEntity httpEntity  = null;
        try {
            DacsCurrentCredentialsRequest dacsCurrentCredentialsRequest = new DacsCurrentCredentialsRequest(jurisdiction);
            HttpResponse httpResponse = dacsClientContext.executeGetRequest(dacsCurrentCredentialsRequest);
            httpEntity = httpResponse.getEntity();
        } catch (IllegalStateException ex) {
            Logger.getLogger(CredentialsLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return httpEntity;
        }
    }

    @Override
    protected void loadEntityFromXml(HttpEntity httpEntity) throws DacsException {
        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();
            JAXBContext jc = JAXBContext.newInstance("com.fedroot.dacs");
            Unmarshaller um = jc.createUnmarshaller();
            DacsCurrentCredentials dacsCurrentCredentials = (DacsCurrentCredentials) um.unmarshal(inputStream);
            for(DacsCurrentCredentials.Credentials credential : dacsCurrentCredentials.getCredentials()) {
                credentials.add(new Credential(credential.getFederation(), credential.getJurisdiction(), credential.getName(), credential.getRoles(), credential.getAuthStyle(), credential.getCookieName()));
            }
        } catch (JAXBException ex) {
            Logger.getLogger(CredentialsLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CredentialsLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(CredentialsLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(CredentialsLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    private Credential valueOf(Credentials dacsCredential) {
//
//    }

}

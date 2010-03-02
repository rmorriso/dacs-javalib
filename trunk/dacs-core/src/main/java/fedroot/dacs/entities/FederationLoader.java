/*
 * FederationLoader.java
 * Created on Jan 16, 2010 5:48:01 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.entities;

import com.fedroot.dacs.DacsJurisdiction;
import com.fedroot.dacs.DacsListJurisdictions;
import fedroot.dacs.client.DacsListJurisdictionsRequest;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
public class FederationLoader extends AbstractEntityLoader {

    private String dacsUrl;
    private Federation federation;
    private Map<String,Jurisdiction> jurisdictions;

    public FederationLoader(DacsClientContext dacsClientContext, String dacsUrl) {
        super(dacsClientContext);
        this.dacsUrl = dacsUrl;
        this.jurisdictions = new HashMap<String,Jurisdiction>();
    }

    /**
     * Returns the loaded federation
     *
     * @return the federation
     */
    public Federation getFederation() {
        return this.federation;
    }

    @Override
    protected InputStream getXmlStream(DacsClientContext dacsClientContext) throws DacsException {
            DacsListJurisdictionsRequest dacsListJurisdictionsRequest = new DacsListJurisdictionsRequest(dacsUrl);
            return dacsClientContext.executeGetRequest(dacsListJurisdictionsRequest);
    }

    @Override
    protected void loadEntityFromStream(InputStream inputStream) throws DacsException {
        try {
            JAXBContext jc = JAXBContext.newInstance("com.fedroot.dacs");
            Unmarshaller um = jc.createUnmarshaller();
            DacsListJurisdictions dacsListJurisdictions = (DacsListJurisdictions) um.unmarshal(inputStream);
            federation = new Federation(dacsListJurisdictions.getFederation(), dacsListJurisdictions.getDomain(), dacsListJurisdictions.getFedId(), dacsListJurisdictions.getFedPublicKey());
            for (DacsJurisdiction dacsJurisdiction : dacsListJurisdictions.getJurisdiction()) {
                federation.addJurisdiction(valueOf(dacsJurisdiction));
            }
        } catch (JAXBException ex) {
            Logger.getLogger(FederationLoader.class.getName()).log(Level.SEVERE, ex.getMessage());
        } catch (IllegalStateException ex) {
            Logger.getLogger(FederationLoader.class.getName()).log(Level.SEVERE, ex.getMessage());
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(FederationLoader.class.getName()).log(Level.SEVERE, ex.getMessage());
            }
        }
    }


    private Jurisdiction valueOf(DacsJurisdiction dacsJurisdiction) {
        boolean authenticates = (dacsJurisdiction.getAuthenticates().equals("yes") ? true : false);
        boolean prompts = (dacsJurisdiction.getPrompts().equals("yes") ? true : false);
        Jurisdiction jurisdiction =
            new Jurisdiction(dacsJurisdiction.getJname(),
                             dacsJurisdiction.getName(),
                             dacsJurisdiction.getAltName(),
                             dacsJurisdiction.getDacsUrl(),
                             authenticates, prompts,
                             dacsJurisdiction.getAuxiliary());
        return jurisdiction;
    }

}

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

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
public class FederationLoader extends WebServiceEntityLoader {

    private DacsListJurisdictionsRequest dacsListJurisdictionsRequest;
    private Federation federation;

    public FederationLoader(String dacsUrl, DacsClientContext dacsClientContext) throws DacsException {
        super(new DacsListJurisdictionsRequest(dacsUrl));
        DacsListJurisdictions dacsListJurisdictions = (DacsListJurisdictions) load(dacsClientContext, "com.fedroot.dacs");
        federation = new Federation(dacsListJurisdictions.getFederation(), dacsListJurisdictions.getDomain(), dacsListJurisdictions.getFedId(), dacsListJurisdictions.getFedPublicKey());
        for (DacsJurisdiction dacsJurisdiction : dacsListJurisdictions.getJurisdiction()) {
            federation.addJurisdiction(valueOf(dacsJurisdiction));
        }
    }

    /**
     * Returns the loaded federation
     *
     * @return the federation
     */
    public Federation getFederation() {
        return this.federation;
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

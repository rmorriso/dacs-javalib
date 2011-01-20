/*
 * Federation.java
 *
 * Created on September 1, 2005, 9:00 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.entities;

import java.util.ArrayList;
import java.util.List;


/**
 * facade class for DACS federation;
 * encapsulates list of member jurisdictionList,
 * @author rmorriso
 */
public class Federation {
//    // feddb implements a set of unique federation objects
//    static HashMap<String,Federation> feddb = new HashMap<String,Federation>();
    String federationName;
    String federationDomain;
    String federationId;
    private String publicKey;
    List<Jurisdiction> jurisdictionList;
    
    /**
     * a private constructor prevents instantiation except through
     * getInstance() method; construct Federation from DacsListJurisdiction
     * service
     */
    public Federation(String federation, String federationDomain, String federationId, String publicKey) {
        this.federationName = federation;
        this.federationDomain = federationDomain;
        this.federationId = federationId;
        this.publicKey = publicKey;
        this.jurisdictionList = new ArrayList<Jurisdiction>();
    }


    
    /**
     * get federationName of this federation
     * @return federation federationName
     */
    public String getFederationName() {
        return federationName;
    }

    public void setFederationName(String federationName) {
        this.federationName = federationName;
    }
    
    /**
     * get federationDomain of this federation
     * @return fedadminuri (string)
     */
    public String getFederationDomain() {
        return federationDomain;
    }

    public void setFederationDomain(String federationDomain) {
        this.federationDomain = federationDomain;
    }

    /**
     * @return the publicKey
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * get list of Jurisdiction objects for this Federation
     * @return list of all jurisdictionList in this federation
     */
    public List<Jurisdiction> getJurisdictions() {
        return jurisdictionList;
    }

    public void addJurisdiction(Jurisdiction jurisdiction) {
        jurisdictionList.add(jurisdiction);
        jurisdiction.setFederation(this);
    }

    /**
     * get list of Jurisdiction objects which provide authentication services
     * @return list of jurisdictionList that authenticate
     */
    public List<Jurisdiction> getAuthenticatingJurisdictions() {
        List<Jurisdiction>authenticatingJurisdictions = new ArrayList<Jurisdiction>();
        for (Jurisdiction jurisdiction : jurisdictionList) {
            if (jurisdiction.isAuthenticates()) {
                authenticatingJurisdictions.add(jurisdiction);
            }
        }
        return authenticatingJurisdictions;
    }


    /**
     * get Jurisdiction from list by jurisdiction federationName
     * @param federationName jurisdiction federationName
     * @return Jurisdiction identified by @param federationName
     */
    public Jurisdiction getJurisdictionByName(String jname) {
        for (Jurisdiction jurisdiction : jurisdictionList) {
            if (jurisdiction.getJName().equalsIgnoreCase(jname)) {
                return jurisdiction;
            }
        }
        return (Jurisdiction) null;
    }

    /**
     * equality operator on Federations
     * @param fed federation to test for equality
     * @return true if this federation is the same object as @param fed
     */
    public boolean equals(Federation fed) {
        return this.federationDomain.equalsIgnoreCase(fed.federationDomain);
    }

}

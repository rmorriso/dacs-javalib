/*
 * Federation.java
 *
 * Created on September 1, 2005, 9:00 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs;

import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.services.DacsListJurisdictionsService;
import com.fedroot.dacs.xmlbeans.DacsJurisdiction;
import com.fedroot.dacs.xmlbeans.DacsListJurisdictionsDocument;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

/**
 * facade class for DACS federation;
 * encapsulates list of member jurisdictions,
 * @author rmorriso
 */
public class Federation {
    // feddb implements a set of unique federation objects
    static HashMap<String,Federation> feddb = new HashMap<String,Federation>();
    String name;
    String domain;
    String fed_id;
    String public_key;
    String fedadmin_uri;
    List<Jurisdiction> jurisdictions;
    
    /**
     * a private constructor prevents instantiation except through
     * getInstance() method; construct Federation from DacsListJurisdiction
     * service
     */
    private Federation(DacsContext dacscontext, DacsListJurisdictionsDocument dacsListJurisdictionsDocument)
    throws Exception {
        this.name = dacsListJurisdictionsDocument.getDacsListJurisdictions().getFederation();
        this.domain = dacsListJurisdictionsDocument.getDacsListJurisdictions().getDomain();
        this.public_key = dacsListJurisdictionsDocument.getDacsListJurisdictions().getFedPublicKey();
        // Add the jurisdictions from dacsListJurisdiction to the Jurisdictions list
        this.jurisdictions = new ArrayList<Jurisdiction>();
        DacsJurisdiction[] jurisdictionList = dacsListJurisdictionsDocument.getDacsListJurisdictions().getJurisdictionArray();
        for(DacsJurisdiction dacsJur :jurisdictionList){
            // Jurisdiction jur = new Jurisdiction(dacsJur.getDacsUrl().getStringValue(), dacsJur.getName().getStringValue(), fedadmin);
            Jurisdiction jur = new Jurisdiction(dacsJur);
            jur.setFederation(this);
            this.jurisdictions.add(jur);
            // TODO: fixme
            if (jur.getName().equals("FEDADMIN")) {
                fedadmin_uri = jur.getDacsUri();
            }
        }
    }
    
    /**
     * static "instance" method enforces uniqueness of Federation instances
     * synchronized to ensure Thread safety
     * @param dacscontext get a unique instance of Federation, populating jurisdiction list
     * @param dacsbaseuri use dacsbaseuri to "bootstrap" the creation of a Federation object (dacs_list_jurisdictions)
     * @throws java.lang.Exception TODO
     * @return unigue Federation object
     */
    public static synchronized Federation getInstance(DacsContext dacscontext, String dacsbaseuri)
    throws Exception, IOException, XmlException {
        Federation federation = null;
        // if dacsbaseuri is present in feddb, then the Federation
        // has already been created - return it; otherwise create a new
        // Federation, insert it in the list and return it
        DacsListJurisdictionsService dacsservice =
                new DacsListJurisdictionsService(dacsbaseuri);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        
        int httpstatus = dacscontext.executeMethod(dacsget);
        if (httpstatus == HttpStatus.SC_OK) {
//            XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
            DacsListJurisdictionsDocument dacsListJurisdictionsDocument = DacsListJurisdictionsDocument.Factory.parse(dacsget.getResponseBodyAsStream());
            dacsget.releaseConnection();
            
            // Check that it is an instance of the DacsListJurisdictionsDocument
//            if(expectedXmlObject instanceof DacsListJurisdictionsDocument){
//                DacsListJurisdictionsDocument dacsListJurisdictionsDocument =
//                        (DacsListJurisdictionsDocument)expectedXmlObject;
                String domain = dacsListJurisdictionsDocument.getDacsListJurisdictions().getDomain();
                federation = feddb.get(domain);
                if (federation == null) {
                    federation = new Federation(dacscontext, dacsListJurisdictionsDocument);
                    feddb.put(federation.getDomain(), federation);
                }
//            } else {
//                throw new Exception("Federation listJurisdictions: unknown XML reply");
//            }
        } else {
            throw new Exception("Federation listJurisdictions: returned HTTP status " + httpstatus);
        }
        return federation;
    }
    
    /**
     * get list of Jurisdiction objects for this Federation
     * @return list of all jurisdictions in this federation
     */
    public List<Jurisdiction> getJurisdictions() {
        return jurisdictions;
    }
    
    /**
     * get list of Jurisdiction names in this Federation
     * @return list of jurisdictions names
     */
    public List<String> getJurisdictionNames() {
        List<String>jur_names = new ArrayList<String>();
        for (Jurisdiction jur : jurisdictions) {
            jur_names.add(jur.getName());
        }
        return jur_names;
    }
    
    /**
     * get list of Jurisdiction objects which provide authentication services
     * @return list of jurisdictions that authenticate
     */
    public List<Jurisdiction> getAuthenticatingJurisdictions() {
        List<Jurisdiction>authjurs = new ArrayList<Jurisdiction>();
        for (Jurisdiction jur : jurisdictions) {
            if (jur.isAuthenticates()) {
                authjurs.add(jur);
            }
        }
        return authjurs;
    }
    
    /**
     * get list of Jurisdiction names in this Federation
     * @return list of jurisdictions names
     */
    public List<String> getAuthenticatingJurisdictionNames() {
        List<String>jur_names = new ArrayList<String>();
        for (Jurisdiction jur : jurisdictions) {
            if (jur.isAuthenticates()) {
                jur_names.add(jur.getName());
            }
        }
        return jur_names;
    }
    
    /**
     * get Jurisdiction from list by jurisdiction name
     * @param name jurisdiction name
     * @return Jurisdiction identified by @param name
     */
    public Jurisdiction getJurisdictionByName(String name) {
        for (Jurisdiction jur : jurisdictions) {
            if (jur.name.equalsIgnoreCase(name)) {
                return jur;
            }
        }
        return (Jurisdiction) null;
    }
    
    
    
    /**
     * get name of this federation
     * @return federation name
     */
    public String getName() {
        return name;
    }
    
    /**
     * get domain of this federation
     * @return fedadminuri (string)
     */
    public String getDomain() {
        return domain;
    }
    
    /**
     * get URL for the FedAdmin application jurisdiction within this federation
     * @return fedadminuri (string)
     */
    public String getFedadminUri() {
        return fedadmin_uri;
    }
    
    /**
     * equality operator on Federations
     * @param fed federation to test for equality
     * @return true if this federation is the same object as @param fed
     */
    public boolean equals(Federation fed) {
        return this.domain.equalsIgnoreCase(fed.domain);
    }
}

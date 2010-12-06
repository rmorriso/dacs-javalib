/*
 * Jurisdiction.java
 *
 * Created on August 23, 2005, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.entities;

import java.util.ArrayList;
import java.util.List;


/**
 * data type for a DACS jurisdiction entity
 * @author rmorriso
 */
public class Jurisdiction {
    private Federation federation;
    private String jname;
    private String name;
    private String altName; // as defined in dacs_list_jurisdictions
    private String dacsUrl;
    private boolean authenticates;
    private boolean prompts;
    private String auxilliary;
    private List<Acl> acls;
    private List<Revocation> revocations;
    
    /**
     * create new Jurisdiction from DacsJurisdiction XmlBean
     * @param dacsjur the DacsJurisdiction XmlBean unmarshalled from the DACS dacs_list_jurisdictions service
     */
    public Jurisdiction(String jname, String name, String altName, String dacsUrl, boolean authenticates, boolean prompts, String auxilliary) {
        this.jname = jname;
        this.name = name;
        this.altName = altName;
        this.dacsUrl = dacsUrl;
        this.authenticates = authenticates;
        this.prompts = prompts;
        this.auxilliary = auxilliary;
        acls = new ArrayList<Acl>();
        revocations = new ArrayList<Revocation>();
    }
    
    /**
     * getter for Jurisdiction.federation
     * @return the federation for this jurisdiction
     */
    public Federation getFederation() {
        return federation;
    }
    
    /**
     * setter for Jurisdiction.federation
     * Note: this method should not exist. Jurisdiction should only
     * be set at the time of instantiation. It should not be possible to change a
     * jurisdiction's federation
     * @param federation federation to set
     */
    public void setFederation(Federation federation) {
        this.federation = federation;
    }
    
    /**
     * getter for Jurisdiction.name
     * @return jurisdiction name
     */
    public String getJName() {
        return jname;
    }
        
    /**
     * @param jname the name to set
     */
    public void setJName(String jname) {
        this.jname = jname;
    }

    /**
     * getter for Jurisdiction.dacsurl
     * @return uri for the base of the DACS install under this jurisdiction
     */
    public String getDacsUri() {
        return getDacsUrl();
    }

    public void setDacsUri(String dacsUri) {
        this.setDacsUrl(dacsUri);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the dacsUrl
     */
    public String getDacsUrl() {
        return dacsUrl;
    }

    /**
     * @param dacsUrl the dacsUrl to set
     */
    public void setDacsUrl(String dacsUrl) {
        this.dacsUrl = dacsUrl;
    }

    public String getAltName() {
        return altName;
    }
    
    public void setAltName(String altname) {
        this.altName = altname;
    }
    
    public boolean isAuthenticates() {
        return authenticates;
    }
    
    public void setAuthenticates(boolean authenticates) {
        this.authenticates = authenticates;
    }

    public boolean isPrompts() {
        return prompts;
    }

    public void setPrompts(boolean prompts) {
        this.prompts = prompts;
    }


    public String getAuxilliary() {
        return auxilliary;
    }

    /**
     * @param auxilliary the auxilliary to set
     */
    public void setAuxilliary(String auxilliary) {
        this.auxilliary = auxilliary;
    }

    /**
     * getter for acls instance variable
     * acls is initially empty and is only populated by an explicit call
     * to loadAcls
     * @return list of cached Acl objects previously populated from dacs_admin XML reply
     */
    public List<Acl> getAcls() {
        return this.acls;
    }

    /**
     * @param acls the acls to set
     */
    public void setAcls(List<Acl> acls) {
        this.acls = acls;
    }


    /**
     * getter for Jurisdiction.revocations
     * revocations is initially empty and is only populated by an explicit call
     * to loadRevocations()
     * @return list of Revocation objects constructed from DacsAdminService/revocations
     */

    public List<Revocation> getRevocations() {
        return this.revocations;
    }

    /**
     * wrapper for dacs_admin/revocations service
     * TODO: this simply updates the cached list - needs
     * to push revocations through to DACS via call to DacsAdminService
     * @param revocations set revocations in this jurisdiction
     */
    public void setRevocations(List<Revocation> revocations) {
        this.setRevocations(revocations);
    }

    public boolean isSecure() {
        return getDacsUrl().toLowerCase().startsWith("https");
    }
    
    /**
     * standard toString() operator
     * @return string representation of this jurisdiction
     */
    @Override
    public String toString() {
        return getName();
    }

}

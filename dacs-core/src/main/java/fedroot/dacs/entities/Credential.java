/*
 * Credential.java
 *
 * Created on February 25, 2010, 8:43 AM
 *
 * Copyright (c) 2005-2011 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * data type wrapper for a DACS credential
 * @author rmorriso
 */
public class Credential {
    private String federationName;
    private String jurisdictionName;
    private String name;
    private String authStyle;
    private String cookieName;
    private List<Role> roles;
    
    /**
     * create new Credential from Credentials XmlBean components
     * @param federationName the DACS Federation name (e.g., NFIS)
     * @param jurisdictionName the DACS jurisdiction name (eg, NRCan)
     * @param name the DACS credential name (eg, rmorriso)
     * @param rolesString the DACS roles (eg, black, beautiful)
     * @param authStyle the DACS auth style used to obtain the credential
     * @param cookieName the DACS cookie name holding the credential (e.g., DACS:NFIS::NRCan:rmorriso)
     */
    public Credential(String federationName, String jurisdictionName, String name, String rolesString, String authStyle, String cookieName) {
        this.federationName = federationName;
        this.jurisdictionName = jurisdictionName;
        this.name = name;
        this.authStyle = authStyle;
        this.cookieName = cookieName;
        this.roles = parseRolesString(rolesString);
    }

    /*
     * parse a comma-separated list of roles into a list of Role objects
     * @return the list of Roles
     */
    private List<Role> parseRolesString(String rolesString) {
        List roleList = new ArrayList<Role>();
        Pattern p = Pattern.compile("[\\w]+");
        Matcher m = p.matcher(rolesString);
        while(m.find()) {
            roleList.add(new Role(m.group()));
        }
        return (roleList.isEmpty() ? null : roleList);
    }
    /**
     * standard toString() operator
     * @return string representation of this credential
     */
    @Override
    public String toString() {
        return cookieName;
    }

    /**
     * @return the federationName
     */
    public String getFederationName() {
        return federationName;
    }

    /**
     * @return the jurisdictionName
     */
    public String getJurisdictionName() {
        return jurisdictionName;
    }

    /**
     * get the credential name (typically the username) associated with the
     * credential, such as rmorriso, blow@gmail.com, etc.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the authStyle
     */
    public String getAuthStyle() {
        return authStyle;
    }

    /**
     * get the cookie name associated with this credential; this is useful
     * if we need to remove the cookie from a DacsContext
     * @return the cookieName
     */
    public String getCookieName() {
        return cookieName;
    }

    /**
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }

}

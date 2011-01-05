/*
 * Credential.java
 *
 * Created on February 25, 2010, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * data type for a DACS jurisdiction entity
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
     * @param dacsjur the DacsJurisdiction XmlBean unmarshalled from the DACS dacs_current_credentials service
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
     * @return string representation of this jurisdiction
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

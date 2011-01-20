/*
 * DacsCookieName.java
 * Created on Jan 15, 2010 8:24:49 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.http;

import fedroot.dacs.exceptions.DacsRuntimeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ricmorri
 */
public class DacsCookieName {

    private String cookieName;
    private String federationPart;
    private String jurisdictionPart;
    private String usernamePart;
    private boolean selectCookieName;

    /** 
     * a DACS cookie name is of the form DACS:Federation::Jurisdiction:Username
     */
    private DacsCookieName(String federationName, String jurisdictionName, String username) throws DacsRuntimeException {
        federationPart = federationName;
        jurisdictionPart = jurisdictionName;
        usernamePart = username;
        cookieName = "DACS:" + federationPart + "::" + jurisdictionPart + ":" + usernamePart;
        selectCookieName = false;
    }

    /**
     * a DACS cookie name is of the form DACS:Federation:::SELECTED
     */
    private DacsCookieName(String federationName) throws DacsRuntimeException {
        federationPart = federationName;
        cookieName = "DACS:" + federationPart + ":::SELECTED";
        selectCookieName = true;
    }


    /**
     * parse cookieName into component parts
     */
    public static DacsCookieName valueOf(String cookieName) {
//        Pattern name = Pattern.compile("DACS:([\\w]+)::([\\w]+):([\\w]+)");
        Pattern name = Pattern.compile("DACS:([\\w]+)::([\\w]+):([\\w\\-]*[\\.]{0,1}[\\w\\-]*[@[A-Za-z0-9-]+\\.+[A-Za-z]{2,4}]*)", Pattern.CASE_INSENSITIVE);
        Matcher m = name.matcher(cookieName);
        if (m.matches()) {
            String federationPart = m.group(1);
            String jurisdictionPart = m.group(2);
            String usernamePart = m.group(3);
            return new DacsCookieName(federationPart, jurisdictionPart, usernamePart);
        } else {
            name = Pattern.compile("DACS:([\\w]+):::SELECTED");
            m = name.matcher(cookieName);
            if (m.matches()) {
                String federationPart = m.group(1);
                return new DacsCookieName(federationPart);
            }
        }
        return null;
    }

    public static String getName(String federationName, String jurisdictionName, String username) {
        return "DACS:" + federationName + "::" + jurisdictionName + ":" + username;
    }
    
    public String getCookieName() {
        return cookieName;
    }
    
    public String getFederationPart() {
        return federationPart;
    }

    public String getJurisdictionPart() {
        return jurisdictionPart;
    }

    public String getUsernamePart() {
        return usernamePart;
    }

    public boolean isSelectCookieName() {
        return selectCookieName;
    }
}

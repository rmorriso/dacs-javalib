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
     * a DACS cookie name is of the form DACS:Federation-Name:::SELECTED
     */
    private DacsCookieName(String federationName) throws DacsRuntimeException {
        federationPart = federationName;
        cookieName = "DACS:" + federationPart + ":::SELECTED";
        selectCookieName = true;
    }

    /*
     * TOTO parse cookieName into component parts
     */
//    public static DacsCookieName valueOf(String cookieName) {
//        if (cookieName.startsWith("DACS:")) {
//            Pattern word = Pattern.compile("[\\w]+");
//            try {
//                String rest = cookieName.substring(cookieName.indexOf(":") + 1);
//                // look for DACS Select Credentials cookie name of the form
//                // DACS:Federation-Name:::SELECTED
//                if (rest.endsWith(":::SELECTED")) {
//                    String federationPart = rest.substring(0, rest.indexOf(":::SELECTED"));
//                    Matcher m = word.matcher(federationPart);
//                    if (m.matches()) {
//                        return new DacsCookieName(federationPart);
//                    }
//                } else {
//                // look for DACS Credentials cookie name of the form
//                // DACS:Federation-Name::Jurisdiction-Name:Username
//                    String federationPart = rest.substring(0, rest.indexOf("::"));
//                    Matcher m = word.matcher(federationPart);
//                    if (m.matches()) {
//                        rest = rest.substring(rest.indexOf("::") + 2);
//                        String jurisdictionPart = rest.substring(0, rest.indexOf(":"));
//                        m = word.matcher(jurisdictionPart);
//                        if (m.matches()) {
//                            rest = rest.substring(rest.indexOf(":") + 1);
//                            String usernamePart = rest;
//                            m = word.matcher(usernamePart);
//                            if (m.matches()) {
//                                return new DacsCookieName(federationPart, jurisdictionPart, usernamePart);
//                            }
//                        }
//                    }
//                }
//            } catch (IndexOutOfBoundsException ex) {
//                return null;
//            }
//        }
//        return null;
//    }

    /**
     * TOTO parse cookieName into component parts
     */
    public static DacsCookieName valueOf(String cookieName) {
        Pattern name = Pattern.compile("DACS:([\\w]+)::([\\w]+):([\\w]+)");
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

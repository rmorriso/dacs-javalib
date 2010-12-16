/*
 * DACS.java
 *
 * Created on August 22, 2005, 10:35 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs;

import fedroot.dacs.exceptions.DacsRuntimeException;
import fedroot.servlet.ParameterValidator;
import fedroot.servlet.ParameterValidator.ValidationType;
import org.apache.http.cookie.Cookie;

/**
 * container for DACS static types
 * @author rmorriso
 */

public class DACS {
    
/**
 * Enumeration of valid service names
 */
    public enum ServiceName {
        dacs_acs,
        dacs_admin,
        dacs_auth_agent,
        dacs_authenticate,
        dacs_auth_transfer,
        dacs_conf,
        dacs_current_credentials,
        dacs_error,
        dacs_group,
        dacs_list_jurisdictions,
        dacs_notices,
        dacs_passwd,
        dacs_signout,
        dacs_version
    }
    
/**
 * Enumeration of common DACS arguments
 */
   public enum CommonArgs {
       DACS_ACS,
       FORMAT
   }
   
/**
 * Enumeration of internal DACS arguments
 */
   public enum InternalArgs {
       DACS_ERROR_URL,
       DACS_JURISDICTION
   }
   
/**
 * Enumeration of valid check options to DACS_ACS argument (-check_only, -check_fail)
 */
    public enum AcsCheck {
        check_only,
        check_fail
    }
    
/**
 * Enumeration of valid values for FORMAT argument to DACS services
 */
    public enum ReplyFormat {
        HTML,
        PLAIN,
        TEXT,
        PHP,
        XML,
        XMLDTD,
        XMLSIMPLE,
        XMLSCHEMA,
        FILE
    }
    
    /**
     * construct a syntractically correct query string parameter
     * for DACS_ACS
     * @param check one of check_only or check_fail
     * @param format one of HTML, XML, XMLSCHEMA
     * @return query string parameter for use in RHS of 
     * name value pair
     */
    public static String makeDacsAcsValue(AcsCheck check, ReplyFormat format) {
        // example: "-check_only -format XMLSCHEMA"
        return "-" + check + " -format " + format;
    }
    
    /**
     * construct a syntactically correct DACS cookie name
     * @param federation DACS federation name
     * @param jurisdiction DACS jurisdiction name
     * @param username DACS username
     * @return DACS cookie name of form DACS::FEDNAME:JURNAME:USERNAME
     */
    public static String makeCookieName(String federation, String jurisdiction, String username) {
        // example: "DACS:FEDROOT::METALOGIC:smith"
        return "DACS:" + federation + "::" + jurisdiction + ":" + username ;
    }
    
    /**
     *
     * @param dacscookie
     * @return
     * @throws DacsRuntimeException
     */
    public static String getDacsUsername(Cookie dacscookie) throws DacsRuntimeException {
        String name = dacscookie.getName();
        if (name.startsWith("DACS:")) {
            name = name.substring(5);
            return name;
        } else {
            throw new DacsRuntimeException("invalid DACS cookie: " + name);
        }
    }

    public static ParameterValidator getDacsCheckParameterValidator() {
        ParameterValidator dacsCheckParameterValidator = new ParameterValidator(ValidationType.ANY);
        dacsCheckParameterValidator.addParameter(CommonArgs.DACS_ACS.toString());
        return dacsCheckParameterValidator;
    }
}

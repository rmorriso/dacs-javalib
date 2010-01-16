/*
 * DacsUtil.java
 *
 * Created on December 21, 2006, 11:23 AM
 *
 * Copyright 2006 Cisco Systems, Inc. All rights reserved. No portion of this
 * software may be reproduced in any form, or by any means, without prior written
 * permission from Cisco Systems, Inc.
 */

package fedroot.dacs.utils;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.http.DacsCookie;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsPostMethod;
import com.fedroot.dacs.services.DacsAuthenticateService;
import com.fedroot.dacs.services.DacsCurrentCredentialsService;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author ricmorri
 */
public class DacsUtil {
    
    private static Logger logger = Logger.getLogger(DacsUtil.class.getName());
    
    /**
     * extracts <code>username</code> from DACS credential cookie in HTTP request
     */
    public static String resolveUsername(String dacsURI, String authJurisdiction, HttpServletRequest request) throws DacsException {
        String userName = null;
        DacsContext appContext = new DacsContext();
        Federation federation;
        Jurisdiction fedHome;
        try {
            federation = Federation.getInstance(appContext, dacsURI);
            fedHome = federation.getJurisdictionByName(authJurisdiction);
            if (request != null) {
                javax.servlet.http.Cookie[] jcookies = request.getCookies();
                if (jcookies != null) {
                    for (javax.servlet.http.Cookie jcookie : jcookies) {
                        if (DacsCookie.isDacsCookie(jcookie)) {
                            userName = fedHome.resolveUsername(jcookie);
                            if (userName != null) { // found username
                                logger.debug("resolved username " + userName);
                                logger.debug("adding Java Cookie to userContext: " + jcookie.getName() + "=" + jcookie.getValue());
                                return userName;
                            }
                        }
                    }
                }
            }
        } catch (DacsException ex) {
            ex.printStackTrace();
            throw new DacsException("failed to instantiate DACS Federation from " + dacsURI);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DacsException(ex.toString());
        }
        return userName;
    }
    
    public static String resolveUsername(String dacsURI, String authJurisdiction, Cookie[] jcookies) {
        String userName = null;
        DacsContext appContext = new DacsContext();
        Federation federation;
        Jurisdiction fedHome;
        try {
            federation = Federation.getInstance(appContext, dacsURI);
            fedHome = federation.getJurisdictionByName(authJurisdiction);
            if (jcookies != null) {
                for (javax.servlet.http.Cookie jcookie : jcookies) {
                    if (DacsCookie.isDacsCookie(jcookie)) {
                        try {
                            userName = fedHome.resolveUsername(jcookie);
                            if (userName != null) { // found username
                                logger.debug("resolved username " + userName);
                                logger.debug("adding Java Cookie to userContext: "
                                        + jcookie.getName() + "=" + jcookie.getValue());
                                return userName;
                            }
                        } catch (DacsException ex) {
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return userName;
    }
    
    public static String processRequest(String dacsURI, HttpServletRequest request) throws DacsException {
        DacsContext appContext = new DacsContext();
        Federation federation;
        try {
            federation = Federation.getInstance(appContext, dacsURI);
            if (request != null) {
                javax.servlet.http.Cookie[] jcookies = request.getCookies();
                if (jcookies != null) {
                    for (javax.servlet.http.Cookie jcookie : jcookies) {
                        if (DacsCookie.isDacsCookie(jcookie)) {
                            logger.debug("adding Java Cookie to context: " + jcookie.getName() + "=" + jcookie.getValue());
                            appContext.addDacsCookie(federation, jcookie);
                            DacsCurrentCredentialsService dacsCurrentCredentialsService = new DacsCurrentCredentialsService(dacsURI);
                            DacsGetMethod dacsGetMethod = dacsCurrentCredentialsService.getDacsGetMethod();
                            int status = appContext.executeMethod(dacsGetMethod);
                            logger.debug("DacsGetMethod http://gotprod.cisco.com/dacs/dacs_current_credentials returned status: " + String.valueOf(status));
                            return dacsGetMethod.getResponseBodyAsString();
                        }
                    }
                }
            }
            return "service failure";
        } catch (DacsException ex) {
            ex.printStackTrace();
            throw new DacsException("failed to instantiate DACS Federation from " + dacsURI);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DacsException(ex.toString());
        }
    }
    
    public static String processRequest(String requestURL, String dacsURI, String authJurisdiction, String username, String password) throws DacsException {
        DacsContext appContext = new DacsContext();
        try {
            DacsAuthenticateService dacsAuthenticateService = new DacsAuthenticateService(dacsURI, authJurisdiction, username, password);
            DacsPostMethod dacsPostMethod = dacsAuthenticateService.getDacsPostMethod();
            int status = appContext.executeMethod(dacsPostMethod);
            DacsGetMethod dacsGetMethod = new DacsGetMethod(requestURL);
            status = appContext.executeMethod(dacsGetMethod);
            logger.debug("DacsGetMethod http://gotprod.cisco.com/dacs/dacs_current_credentials returned status: " + String.valueOf(status));
            return dacsGetMethod.getResponseBodyAsString();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DacsException("xxx " + dacsURI);
        } 
    }
}

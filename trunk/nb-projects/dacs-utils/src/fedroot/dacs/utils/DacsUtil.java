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

import com.fedroot.dacs.Credentials;
import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.http.DacsCookie;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author ricmorri
 */
public class DacsUtil {
    
    private static Logger logger = Logger.getLogger(DacsUtil.class);
    
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
            List <Credentials> credentials = new ArrayList<Credentials>();
            if (request != null) {
                javax.servlet.http.Cookie[] jcookies = request.getCookies();
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
            }
        } catch (Exception ex) {
            throw new DacsException("failed to instantiate DACS Federation from " + dacsURI);
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
}

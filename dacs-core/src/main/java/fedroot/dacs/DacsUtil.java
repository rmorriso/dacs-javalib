/*
 * DacsUtil.java
 * Created on Jan 15, 2010 8:24:49 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs;

import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.exceptions.DacsRuntimeException;
import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ricmorri
 */
public class DacsUtil {

    private static Logger logger = Logger.getLogger(DacsUtil.class.getName());

    /**
     * extracts <code>username</code> from DACS credential cookie in HTTP request
     * looks for cookies with a valid DACS cookie name in @param request, then
     * creates and attaches a corresponding HttpClient cookie in a DacsClientContext.
     * The resulting DacsClientContext is used to execute a DacsCurrentCredentials
     * web service request. In some DACS deployments it is permissible for a user
     * session to carry mutliple DACS credentials, but in those cases one such
     * credential must be the <i>selected</i> credential. A DacsException is thrown if this
     * is not the case.
     * @return the username associated with the any DACS cookies found in @param request
     * or null if none is found
     */
    public static String resolveUser(Jurisdiction jurisdiction, HttpServletRequest request) {
        String username = null;
        if (request != null) {
            DacsCookie selectCookie = null;
            List<DacsCookie> dacsCookies = new ArrayList<DacsCookie>();
            javax.servlet.http.Cookie[] jcookies = request.getCookies();
            if (jcookies != null) {
                for (javax.servlet.http.Cookie jcookie : jcookies) {
                    if (DacsCookie.isDacsCookie(jcookie)) {
                        dacsCookies.add(new DacsCookie(jcookie));
//                        userName = fedHome.resolveUsername(jcookie);
//                        if (userName != null) { // found username
//                            logger.debug("resolved username " + userName);
//                            logger.debug("adding Java Cookie to userContext: " + jcookie.getName() + "=" + jcookie.getValue());
//                            return userName;
//                        }
                    }
                    // if there are multiple DACS cookies, exactly one must be
                    // indicated as the SELECTED DACS cookie
                    if (DacsCookie.isDacsSelectCookie(jcookie)) {
                        if (selectCookie != null) {
                            throw new DacsRuntimeException("Multiple DACS SELECT cookies found in request.");
                        }
                        selectCookie = new DacsCookie(jcookie);
                    }
                }
            }
            // not all DACS deployments permit multiple DACS credentials, but if 
            // multiple credentials are found, one of them must be "selected"
            // as implemented by the dacs_select_credentials Web service
            if (dacsCookies.size() > 1 && selectCookie == null) {
                throw new DacsRuntimeException("Multiple DACS credentials in request without accompanying DACS:SELECT cookie.");
            }
            if (dacsCookies.size() == 1) {
                DacsClientContext dacsClientContext = new DacsClientContext();
                dacsClientContext.addCookie(dacsCookies.get(1));
                try {
                    // URI dacsUri = URI uri = URIUtils.resolve();
                    URI dacsUri = new URI(jurisdiction.getDacsUri()); //+ "/dacs_current_credentials");
                    
                    dacsClientContext.executeGetRequest(dacsUri);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(DacsUtil.class.getName()).log(Level.SEVERE, null, ex);
                    throw new DacsRuntimeException("Invalid DACS URI found: " + jurisdiction.getDacsUri());
                } catch (DacsException ex) {
                        Logger.getLogger(DacsUtil.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            if (selectCookie != null) {
                DacsCookie selectedCookie = null;
                for (DacsCookie dacsCookie : dacsCookies) {
//                    if (dacsCookie.equals(selectCookie))
                }
            }

        }
        return username;
    }

}

/*
 * DacsUtil.java
 * Created on Jan 15, 2010 8:24:49 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs;

import fedroot.dacs.entities.Credential;
import fedroot.dacs.entities.Credentials;
import fedroot.dacs.entities.CredentialsLoader;
import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.exceptions.DacsRuntimeException;
import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsCookie;
import fedroot.dacs.http.DacsCookieName;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

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
     * @return the username associated with the effective credential in the
     * DACS cookies found in @param request or null if none is found
     */
    public static Credential resolveUser(Jurisdiction jurisdiction, HttpServletRequest request) throws DacsException {
        List<DacsCookie> dacsCookies = getDacsCookies(jurisdiction.getFederation(), getCookieHeaders(request));
        if (dacsCookies != null) {
            DacsClientContext dacsClientContext = new DacsClientContext();
            dacsClientContext.addDacsCookies(dacsCookies);
            CredentialsLoader credentialsLoader = new CredentialsLoader(jurisdiction, dacsClientContext);
            Credentials credentials = credentialsLoader.getCredentials();
            return (credentials != null ? credentials.getEffectiveCredential() : null);
        }
        return null;
    }

    public static String resolveUsername(Jurisdiction jurisdiction, HttpServletRequest request) throws DacsException {
        Credential effectiveCredential = resolveUser(jurisdiction, request);
        return (effectiveCredential != null ? effectiveCredential.getName() : null);
    }

    public static List<DacsCookie> getDacsCookies(String domain, HttpServletRequest request) {
        javax.servlet.http.Cookie[] jcookies = request.getCookies();
        if (jcookies == null) {
            return null;
        } else {
            DacsCookie dacsCookie = null;
            DacsCookie selectCookie = null;
            List<DacsCookie> dacsCookies = new ArrayList<DacsCookie>();
            for (javax.servlet.http.Cookie jcookie : jcookies) {
                if (DacsCookie.isDacsCookie(jcookie)) {
                    dacsCookie = new DacsCookie(domain, jcookie);
                    dacsCookies.add(dacsCookie);
                    // if there are multiple DACS cookies, exactly one must be
                    // indicated as the SELECTED DACS cookie
                }
                if (DacsCookie.isDacsSelectCookie(jcookie)) {
                    if (selectCookie != null) {
                        throw new DacsRuntimeException("Multiple DACS SELECT cookies found in request.");
                    }
                    selectCookie = new DacsCookie(domain, jcookie);
                    dacsCookies.add(selectCookie);
                }
            }
            return dacsCookies;
        }
    }

    public static List<DacsCookie> getDacsCookies(Federation federation, Enumeration cookieHeaders) {
        String federationName = federation.getFederationName();
        String federationDomain = federation.getFederationDomain();
        List<DacsCookie> dacsCookies = new ArrayList<DacsCookie>();
        Pattern word = Pattern.compile("DACS:[\\w:]+=[.]+[\b]");
        while (cookieHeaders.hasMoreElements()) {
            String cookieHeader = (String) cookieHeaders.nextElement();
            Matcher m = word.matcher(cookieHeader);
            while (m.find()) {
                String cookieString = m.group();
                logger.log(Level.INFO, cookieString);
                String cookieName = cookieString.substring(0, cookieString.indexOf('='));
                String cookieValue = cookieString.substring(cookieString.indexOf('=') + 1);
                DacsCookieName dacsCookieName = DacsCookieName.valueOf(cookieName);
                if (dacsCookieName != null && federationName.equals(dacsCookieName.getFederationPart())) {
                    Jurisdiction jurisdiction = federation.getJurisdictionByName(dacsCookieName.getJurisdictionPart());
                    dacsCookies.add(new DacsCookie(federationDomain, cookieName, cookieValue, jurisdiction.isSecure()));
                }
            }
        }
        return dacsCookies;
    }

    public static List<Cookie> getCookies(String cookieHeader) {
        List<Cookie> cookies = new ArrayList<Cookie>();
        Pattern name = Pattern.compile("(DACS:[\\w]+::[\\w]+:[\\w]+)=([-\\w]+)"); //.*");
        Matcher m = name.matcher(cookieHeader);
        while (m.find()) {
            String cookieName = m.group(1);
            String cookieValue = m.group(2);
            cookies.add(new BasicClientCookie(cookieName, cookieValue));
        }
        return cookies;
    }

    public static Enumeration getCookieHeaders(HttpServletRequest request) {
        return request.getHeaders("cookie");
    }
}

/*
 * DacsCookie.java
 * Created on Jan 15, 2010 8:24:49 PM.
 * Copyright (c) 2010-2011 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.http;

import fedroot.dacs.exceptions.DacsRuntimeException;
import java.util.Date;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 *
 * @author ricmorri
 */
public class DacsCookie extends BasicClientCookie {

    /** 
     * Creates a new instance of DacsCookie from a javax.servlet.http.net.Cookie
     */
    public DacsCookie(String domain, javax.servlet.http.Cookie cookie) throws DacsRuntimeException {
//        super(federationDomain, jcookie.getName(),jcookie.getValue(),"/", jcookie.getMaxAge(),jcookie.getSecure());
        super(cookie.getName(), cookie.getValue());

        if (!isDacsCookie(cookie)) {
            throw new DacsRuntimeException("invalid DACS cookie: " + cookie.getName());
        }

        // the domain of a DACS federation never refers to a single host
        // if there is no leading dot we add one to the domain,
        // so a cookie with domain "foo.com" becomes a DACS
        // cookie with domain ".foo.com" causing user agents to send the cookie
        // to hosts foo.com, bar.foo.com, baz.foo.com etc
        
        setVersion(1);
        if (domain.startsWith(".")) {
            setDomain(domain);
        } else {
            setDomain("." + domain);
        }
        setPath("/");

        if (cookie.getMaxAge() == -1) {
        } else {
            Date expires = new Date();
            expires.setTime(expires.getTime() + cookie.getMaxAge());
            setExpiryDate(expires);
        }

        setSecure(cookie.getSecure());
    }

    public DacsCookie(String domain, String name, String value, boolean secure) throws DacsRuntimeException {
        super(name, value);

        if (!isDacsCookieName(name)) {
            throw new DacsRuntimeException("invalid DACS cookie: " + name);
        }

        setVersion(0);
        if (domain.startsWith(".")) {
            setDomain(domain);
        } else {
            setDomain("." + domain);
        }
        setPath("/");

        // TODO: set expire date from DACS configuration
        Date expires = new Date();
        expires.setTime(expires.getTime() + 3600);
        setExpiryDate(expires);

        setSecure(secure);
    }

    public static boolean isDacsCookie(org.apache.http.cookie.Cookie cookie) {
        return isDacsCookieName(cookie.getName());
    }

    public static boolean isDacsCookie(javax.servlet.http.Cookie cookie) {
        return isDacsCookieName(cookie.getName());
    }

    public static boolean isDacsSelectCookie(org.apache.http.cookie.Cookie cookie) {
        return isDacsSelectCookieName(cookie.getName());
    }

    public static boolean isDacsSelectCookie(javax.servlet.http.Cookie cookie) {
        return isDacsSelectCookieName(cookie.getName());
    }

    private static boolean isDacsCookieName(String cookieName) {
        return (cookieName.startsWith("DACS:"));
    }

    private static boolean isDacsSelectCookieName(String cookieName) {
        return (cookieName.startsWith("DACS:") && cookieName.endsWith(":SELECT"));
    }

}

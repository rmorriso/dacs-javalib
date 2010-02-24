/*
 * DacsClientContext.java
 * Created on Jan 15, 2010 8:02:49 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.http;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */

import fedroot.dacs.exceptions.DacsException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;


/**
 * This example demonstrates the use of a local HTTP context populated with
 * custom attributes.
 */
public class DacsClientContext {

    private String federationDomain;
    private HttpClient httpClient;
    private CookieStore cookieStore;
    private HttpContext localContext;

    public DacsClientContext() {
        httpClient = new DefaultHttpClient();
        cookieStore = new BasicCookieStore();
        localContext = new BasicHttpContext();
        // Bind custom cookie store to the local context
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    public HttpResponse executeGetRequest(URI uri) throws DacsException {
        HttpGet httpGet = new HttpGet(uri);
        try {
            return httpClient.execute(httpGet, localContext);
        } catch (IOException ex) {
            Logger.getLogger(DacsClientContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new DacsException("DACS HTTP Get Request failed: " + ex.getMessage());
        }
    }

    public List<Cookie> getAllCookies() {
        return cookieStore.getCookies();
    }

    public List<Cookie> getDacsCookies(String federationDomain) {
        List<Cookie> dacsCookies = new ArrayList<Cookie>();
        for (Cookie cookie : cookieStore.getCookies()) {
            if (DacsCookie.isDacsCookie(cookie) && cookie.getDomain().equals(federationDomain)) {
                dacsCookies.add(cookie);
            }
        }
        return dacsCookies;
    }

    public void addCookie(Cookie cookie) {
        cookieStore.addCookie(cookie);
    }

    /**
     * When HttpClient instance is no longer needed,
     * shut down the connection manager to ensure
     * immediate deallocation of all system resources
     */
    public void closeConnection() {
        httpClient.getConnectionManager().shutdown();
    }
    

}


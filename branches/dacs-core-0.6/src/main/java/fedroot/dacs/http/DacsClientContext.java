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
import fedroot.servlet.WebServiceRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;

/**
 * This example demonstrates the use of a local HTTP context populated with
 * custom attributes.
 */
public class DacsClientContext {

    private HttpClient httpClient;
    private HttpContext httpContext;
    private CookieStore cookieStore;

    /**
     * constructor for DacsContext;
     * initializes HttpClient that will be used for this DacsContext,
     * sets legacy BROWSER_COMPATIBILITY mode to emulate typical browser behaviour.
     * In particular this is relied upon to ensure that cookies are sent when
     * hostname = cookie domainname.
     */
    public DacsClientContext() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(
                new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        ClientConnectionManager cm = new ThreadSafeClientConnManager(schemeRegistry);

        httpClient = new DefaultHttpClient(cm);
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        cookieStore = new BasicCookieStore();
        httpContext = new BasicHttpContext();
        // Bind custom cookie store to the local context
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    public InputStream executeGetRequest(WebServiceRequest webServiceRequest) throws DacsException {
        DacsGetRequest dacsGetRequest = new DacsGetRequest(webServiceRequest);
        return dacsGetRequest.getInputStream(httpClient, httpContext);
    }

    public HttpResponse executeGetRequest(URI uri) throws DacsException {
        DacsGetRequest dacsGetRequest = new DacsGetRequest(uri);
        HttpGet httpGet = dacsGetRequest.getHttpGet();
        try {
            return httpClient.execute(httpGet, httpContext);
        } catch (IOException ex) {
            Logger.getLogger(DacsClientContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new DacsException("DACS HTTP Get Request failed: " + ex.getMessage());
        } finally {
            // TODO need to close connection, or use multithreaded connection manager or SOMETHING!
        }
    }

    public HttpResponse executeGetRequest(DacsGetRequest dacsGetRequest) throws IOException {
        return httpClient.execute(dacsGetRequest.getHttpGet(), httpContext);
    }

    public HttpResponse executeGetRequest(HttpGet httpGet) throws DacsException {
        try {
            return httpClient.execute(httpGet, httpContext);
        } catch (IOException ex) {
            Logger.getLogger(DacsClientContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new DacsException("DACS HTTP Get Request failed: " + ex.getMessage());
        } finally {
            // TODO need to close connection, or use multithreaded connection manager or SOMETHING!
        }
    }
//    public HttpResponse executePostRequest(WebServiceRequest webServiceRequest) throws IOException {
//        DacsPostRequest dacsPostRequest = new DacsPostRequest(webServiceRequest);
//        HttpResponse response = httpClient.execute(dacsPostRequest.getHttpPost(), httpContext);
//        HttpEntity entity = response.getEntity();
//        if (entity != null) entity.consumeContent();
//        return response;
//    }

    public InputStream executePostRequest(WebServiceRequest webServiceRequest) throws DacsException {
        DacsPostRequest dacsPostRequest = new DacsPostRequest(webServiceRequest);
        return dacsPostRequest.getInputStream(httpClient, httpContext);
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

    /**
     * get cookie from CookieStore by name
     * @param name name of cookie to retrieve
     * @return cookie matching @param name, else null
     */
    private DacsCookie getCookieByName(String cookieName) {
        for (Cookie cookie : cookieStore.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return (DacsCookie) cookie;
            }
        }
        return null;
    }

    /**
     * remove cookie from CookieStore by name
     * @param name name of cookie to remove
     */
    public void removeCookieByName(String cookieName) {
        for (Cookie cookie : cookieStore.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                BasicClientCookie removeCookie = (BasicClientCookie) cookie;
                removeCookie.setExpiryDate(new Date(0));
            }
        }
        cookieStore.clearExpired(new Date());
    }

    public void addCookie(DacsCookie cookie) {
        DacsCookie foundCookie = getCookieByName(cookie.getName());
        if (foundCookie != null) {
            // do nothing if identical cookie is present in DacsContext
            if (foundCookie.getValue().equals(cookie.getValue())) {
                return;
            }
            // cookie with same name but different value present in DacsContext
            //    -- nuke it and add the new one --
            // this is relevant when the browser has obtained credentials
            // outside of FedAdmin app; we assume the browser holds the
            // definitive version of state
            foundCookie.setExpiryDate(new Date(0));
            cookieStore.clearExpired(new Date());
        }
        cookieStore.addCookie(cookie);
    }

    public void addDacsCookies(List<DacsCookie> cookies) {
        for (DacsCookie cookie : cookies) {
            cookieStore.addCookie(cookie);
        }
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

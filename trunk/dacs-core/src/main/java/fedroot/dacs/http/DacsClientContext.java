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

import fedroot.dacs.client.DacsWebServiceRequest;
import fedroot.dacs.exceptions.DacsException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHost;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;


/**
 * This example demonstrates the use of a local HTTP context populated with
 * custom attributes.
 */
public class DacsClientContext {

    private HttpClient httpClient;
    private CookieStore cookieStore;
    private HttpContext localContext;

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
                new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(
                new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager cm = new ThreadSafeClientConnManager(schemeRegistry);

        httpClient = new DefaultHttpClient(cm);
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        cookieStore = new BasicCookieStore();
        localContext = new BasicHttpContext();
        // Bind custom cookie store to the local context
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    public InputStream executeGetRequest(DacsWebServiceRequest dacsWebServiceRequest) throws DacsException {
            DacsGetRequest dacsGetRequest = new DacsGetRequest(dacsWebServiceRequest);
            return dacsGetRequest.getInputStream(httpClient, localContext);
    }
    
    public HttpResponse executeGetRequest(URI uri) throws DacsException {
        DacsGetRequest dacsGetRequest = new DacsGetRequest(uri);
        HttpGet httpGet = dacsGetRequest.getHttpGet();
        try {
            return httpClient.execute(httpGet, localContext);
        } catch (IOException ex) {
            Logger.getLogger(DacsClientContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new DacsException("DACS HTTP Get Request failed: " + ex.getMessage());
        } finally {
           // TODO need to close connection, or use multithreaded connection manager or SOMETHING!
        }
    }

    public HttpResponse executeGetRequest(DacsGetRequest dacsGetRequest) throws IOException {
        return httpClient.execute(dacsGetRequest.getHttpGet(), localContext);
    }

    public HttpResponse executePostRequest(DacsWebServiceRequest dacsWebServiceRequest) throws IOException {
        DacsPostRequest dacsPostRequest = new DacsPostRequest(dacsWebServiceRequest);
        return httpClient.execute(dacsPostRequest.getHttpPost(), localContext);
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


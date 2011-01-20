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
import com.fedroot.dacs.AccessDenied;
import com.fedroot.dacs.DacsAcs;
import fedroot.dacs.client.DacsCheckRequest;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
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

    private static final Logger logger = Logger.getLogger(DacsClientContext.class.getName());

    /**
     * constructor for DacsClientContext;
     * initializes HttpClient that will be used for this DacsContext,
     * sets legacy BROWSER_COMPATIBILITY mode to emulate typical browser behaviour.
     * In particular this is relied upon to ensure that cookies are sent when
     * hostname = cookie domainname.
     */
    public DacsClientContext() {
        this(new BasicHttpParams());
    }

    public DacsClientContext(HttpParams httpParams) {

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(
                new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        ClientConnectionManager cm = new ThreadSafeClientConnManager(schemeRegistry);
        if (httpParams.getParameter(ClientPNames.COOKIE_POLICY) == null) {
            httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        }
        httpClient = new DefaultHttpClient(cm, httpParams);

        cookieStore = new BasicCookieStore();
        httpContext = new BasicHttpContext();
        // Bind custom cookie store to the local context
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    public DacsResponse executeGetRequest(WebServiceRequest webServiceRequest) throws DacsException {
        return executeGetRequest(webServiceRequest.getURI());
    }

    public DacsResponse executeGetRequest(URI uri) throws DacsException {
        DacsGetRequest dacsGetRequest = new DacsGetRequest(uri);
        return execute(dacsGetRequest, httpClient, httpContext);
    }

    public DacsResponse executeGetRequest(DacsGetRequest dacsGetRequest) throws DacsException {
        return execute(dacsGetRequest, httpClient, httpContext);
    }

    public DacsResponse executePostRequest(WebServiceRequest webServiceRequest) throws DacsException {
        DacsPostRequest dacsPostRequest = new DacsPostRequest(webServiceRequest);
        return execute(dacsPostRequest, httpClient, httpContext);
    }

    public DacsResponse execute(DacsGetRequest dacsGetRequest, HttpClient httpClient, HttpContext httpContext) throws DacsException {
        try {
            return new DacsResponse(httpClient.execute(dacsGetRequest.getHttpGet(), httpContext));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new DacsException(ex.getLocalizedMessage());
        }
    }

    public DacsResponse execute(DacsPostRequest dacsPostRequest, HttpClient httpClient, HttpContext httpContext) throws DacsException {
        try {
            return new DacsResponse(httpClient.execute(dacsPostRequest.getHttpPost(), httpContext));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new DacsException(ex.getLocalizedMessage());
        }
    }

    public DacsResponse getDacsResponse(WebServiceRequest webServiceRequest) throws DacsException {
        switch (webServiceRequest.getHttpRequestType()) {
            case GET:
                return executeGetRequest(webServiceRequest);
            case POST:
                return executePostRequest(webServiceRequest);
            case DELETE:
            case HEAD:
            case PUT:
            case RACE:
            default:
                return null;
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

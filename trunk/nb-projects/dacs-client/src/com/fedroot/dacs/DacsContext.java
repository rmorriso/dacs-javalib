/*
 * DacsContext.java
 *
 * Created on August 26, 2005, 10:40 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs;
import com.fedroot.dacs.http.DacsDeleteMethod;
import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsHeadMethod;
import com.fedroot.dacs.http.DacsPostMethod;
import com.fedroot.dacs.http.DacsPutMethod;
import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.dacs.services.DACS;
import com.fedroot.dacs.util.DacsAccess902Event;
import com.fedroot.dacs.util.Dacs902EventHandler;
import com.fedroot.dacs.util.DacsAccess905Event;
import com.fedroot.dacs.util.Dacs905EventHandler;
import com.fedroot.dacs.util.Dacs9xxEventHandler;
import com.fedroot.dacs.util.DacsAccess9xxEvent;
import com.fedroot.dacs.util.Event902DefaultHandler;
import com.fedroot.dacs.util.Event905DefaultHandler;
import com.fedroot.dacs.util.Event9xxDefaultHandler;
import com.fedroot.dacs.xmlbeans.AccessDeniedDocument;
import com.fedroot.dacs.xmlbeans.DacsAcsDocument;
import com.fedroot.dacs.xmlbeans.Event900Document;
import com.fedroot.dacs.xmlbeans.Event901Document;
import com.fedroot.dacs.xmlbeans.Event902Document;
import com.fedroot.dacs.xmlbeans.Event903Document;
import com.fedroot.dacs.xmlbeans.Event904Document;
import com.fedroot.dacs.xmlbeans.Event905Document;
import com.fedroot.dacs.xmlbeans.Event998Document;
import com.fedroot.net.DomainTree;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpClientParams;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;


/**
 * repository for client state arising from a history of interactions with
 * DACS authentication, notice acknowledgement; uses HttpClient for HTTP request
 * and response processing ...
 */
public class DacsContext {
    private HttpClient httpclient;
    private HttpClientParams clientparams;
    private Dacs902EventHandler event902handler;
    private Dacs902EventHandler event902defaulthandler;
    private Dacs905EventHandler event905handler;
    private Dacs905EventHandler event905defaulthandler;
    private Dacs9xxEventHandler event9xxdefaulthandler;
    private Map<String, Dacs902EventHandler> event902handlers;
    private Map <String, Dacs905EventHandler> event905handlers;
    private DomainTree domaintree;
    
    /** Log object for this class. */
    private static final Log LOG = LogFactory.getLog(DacsContext.class);
    
    static {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Java version: " + System.getProperty("java.version"));
            LOG.debug("Java vendor: " + System.getProperty("java.vendor"));
            LOG.debug("Java class path: " + System.getProperty("java.class.path"));
            LOG.debug("Operating system name: " + System.getProperty("os.name"));
            LOG.debug("Operating system architecture: " + System.getProperty("os.arch"));
            LOG.debug("Operating system version: " + System.getProperty("os.version"));
        }
    }
    
    /**
     * constructor for DacsContext;
     * initializes HttpClient that will be used for this DacsContext,
     * sets legacy BROWSER_COMPATIBILITY mode to emulate typical browser behaviour.
     * In particular this is relied upon to ensure that cookies are sent when
     * hostname = cookie domainname.
     */
    public DacsContext() {
        // use multi-threaded connection manager to allow concurrent execution of
        // requests; note that HTTP RFC 2616 directs that a user agent
        // "SHOULD NOT maintain more than 2 connections with any server or proxy"
        this.httpclient = new HttpClient(new MultiThreadedHttpConnectionManager());
        this.httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        this.clientparams = httpclient.getParams();
        // select old style, browser-compatible cookie
        this.clientparams.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // set default event handlers
        this.event902defaulthandler  = new Event902DefaultHandler();
        this.event905defaulthandler = new Event905DefaultHandler();
        this.event9xxdefaulthandler = new Event9xxDefaultHandler();
        this.domaintree = new DomainTree();
    }
    
    /**
     * execute GET method in the current DacsContext context as
     * instantiated in httpclient (parameter settings, cookies, etc)
     * @param dacsget DacsGetMethod to be executed
     * @return HTTP Status Code returned by dacsget
     * @throws java.io.IOException TODO
     * @throws org.apache.commons.httpclient.HttpException TODO
     */
    public int executeMethod(DacsGetMethod dacsget)
    throws IOException, HttpException {
        return this.httpclient.executeMethod(dacsget);
    }
       
    /**
     * check DacsGetmethod (DACS_ACS=-check_only) in the current DacsContext context as
     * instantiated in httpclient (parameter settings, cookies, etc);
     * allow reply format to
     * @param dacsget DacsGetMethod to be executed
     * @param reply_type select format of reply (eg, HTML, XML, XMLSCHEMA ...)
     * @return DacsStatus code returned by dacsget
     * @throws com.fedroot.dacs.DacsException TODO
     * @throws org.apache.commons.httpclient.HttpException TODO
     */
    public int executeCheckOnlyMethod(DacsGetMethod dacsget, DACS.ReplyFormat reply_type)
    throws DacsException {
        dacsget.setDacsAcs(DACS.AcsCheck.check_only, reply_type);
        try {
            int httpstatus = httpclient.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) { // check mode succeeded
                return dacsget.getDacsStatusCode();
            } else { // check mode failed
                throw new DacsException("executeMethod in check mode failed with HTTP status: " + httpstatus);
            }
        } catch (IOException ex) {
            throw new DacsException("IOException thrown executing dacsget: " + dacsget.toString());
        } 
    }
    
    /**
     * check DacsGetmethod (DACS_ACS=-check_only) in the current DacsContext context as
     * instantiated in httpclient (parameter settings, cookies, etc);
     * set reply format to default to XMLSCHEMA.
     * @return DacsStatus code returned by dacsget
     * @throws com.fedroot.dacs.DacsException TODO
     */
    public int executeCheckOnlyMethod(DacsGetMethod dacsget)
    throws DacsException {
        return executeCheckOnlyMethod(dacsget, DACS.ReplyFormat.XMLSCHEMA);
    }
    
    /**
     * check DacsGetMethod (DACS_ACS=-check_fail) in the current DacsContext context as
     * instantiated in httpclient (parameter settings, cookies, etc)
     * @param dacsget DacsGetMethod to be executed
     * @return HTTP Status Code returned by dacsget
     * @throws java.io.IOException TODO
     * @throws org.apache.commons.httpclient.HttpException TODO
     */
    public int executeCheckFailMethod(DacsGetMethod dacsget, DACS.ReplyFormat reply_type)
    throws DacsException {
        dacsget.setDacsAcs(DACS.AcsCheck.check_fail, reply_type);
        try {
            int httpstatus = httpclient.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) { // check mode succeeded
                int dacsstatus = dacsget.getDacsStatusCode();
                if (dacsstatus == DacsStatus.SC_DACS_ACCESS_DENIED) {
                    // parse XML reply, calling event handlers if appropriate
                    return handleDacsAccessDenied(dacsget);
                } else {
                    return dacsstatus;
                }
            } else { // check mode failed
                throw new DacsException("executeMethod in check mode failed with HTTP status: " + httpstatus);
            }
        } catch (IOException ex) {
            throw new DacsException("IOException thrown executing dacsget: " + dacsget.toString());
        } 
    }
    
    /**
     * execute DacsGetMethod (DACS_ACS=-check_fail) in the current DacsContext context as
     * instantiated in httpclient (parameter settings, cookies, etc);
     * set reply format to default to XMLSCHEMA.
     * @return DacsStatus code returned by dacsget
     * @throws com.fedroot.dacs.DacsException TODO
     */
    public int executeCheckFailMethod(DacsGetMethod dacsget)
    throws DacsException {
        return executeCheckFailMethod(dacsget, DACS.ReplyFormat.XMLSCHEMA);
    }
    /**
     * execute POST method in the current DacsContext context as
     * instantiated in httpclient (parameter settings, cookies, etc)
     * @param dacsget DacsGetMethod to be executed
     * @return HTTP Status Code returned by dacsget
     * @throws java.io.IOException TODO
     * @throws org.apache.commons.httpclient.HttpException TODO
     */
    public int executeMethod(DacsPostMethod dacspost)
    throws IOException, HttpException {
        return httpclient.executeMethod(dacspost);
    }
    
    /**
     * execute DELETE method in the current DacsContext context as
     * instantiated in httpclient (parameter settings, cookies, etc)
     * @param dacsget DacsGetMethod to be executed
     * @return HTTP Status Code returned by dacsget
     * @throws java.io.IOException TODO
     * @throws org.apache.commons.httpclient.HttpException TODO
     */
    public int executeMethod(DacsDeleteMethod dacsdelete)
    throws IOException, HttpException {
        return httpclient.executeMethod(dacsdelete);
    }
    
    /**
     * execute PUT method in the current DacsContext context as
     * instantiated in httpclient (parameter settings, cookies, etc)
     * @param dacsget DacsGetMethod to be executed
     * @return HTTP Status Code returned by dacsget
     * @throws java.io.IOException TODO
     * @throws org.apache.commons.httpclient.HttpException TODO
     */
    public int executeMethod(DacsPutMethod dacsput)
    throws IOException, HttpException {
        return httpclient.executeMethod(dacsput);
    }
    
    public void setHttpParameter(String param, Object value) {
        this.clientparams.setParameter(param, value);
    }
    
    /**
     * execute HEAD method in the current DacsContext context as
     * instantiated in httpclient (parameter settings, cookies, etc)
     * @param dacsget DacsGetMethod to be executed
     * @return HTTP Status Code returned by dacsget
     * @throws java.io.IOException TODO
     * @throws org.apache.commons.httpclient.HttpException TODO
     */
    public int executeMethod(DacsHeadMethod dacshead)
    throws IOException, HttpException {
        return httpclient.executeMethod(dacshead);
    }
    /**
     * set the Dacs902EventHandler for a federation
     * @param federation
     * @param handler
     */
    public synchronized void setDacs902EventHandler(Federation federation, Dacs902EventHandler handler) {
        this.domaintree.insert(federation.getDomain(), "Dacs902EventHandler", handler);
    }
    
    /**
     * set the Dacs902EventHandler to be used in the absence of a federation-specific handler
     * @param handler
     */
    public synchronized void setDacs902EventHandler(Dacs902EventHandler handler) {
        this.event902defaulthandler = handler;
    }
    
    /**
     * unset the Dacs902EventHandler
     * @param federation
     */
    public synchronized void unsetDacs902EventHandler(Federation federation) {
        this.domaintree.insert(federation.getDomain(), "Dacs902EventHandler", null);
    }
    
    /**
     * set the Dacs905EventHandler for a federation
     * @param federation
     * @param handler
     */
    public synchronized void setDacs905EventHandler(Federation federation, Dacs905EventHandler handler) {
        this.domaintree.insert(federation.getDomain(), "Dacs905EventHandler", handler);
    }
    
    /**
     * set the Dacs905EventHandler to be used in the absence of a federation-specific handler
     * @param handler
     */
    public synchronized void setDacs905EventHandler(Dacs905EventHandler handler) {
        this.event905defaulthandler = handler;
    }
    /**
     * unset the Dacs905Handler
     * @param federation
     */
    public synchronized void unsetDacs905EventHandler(Federation federation) {
        this.domaintree.insert(federation.getDomain(), "Dacs905EventHandler", null);
    }
    
    private Dacs902EventHandler getDacs902EventHandler(DacsGetMethod dacsget) {
        try {
            String uri = dacsget.getURI().getHost();
            Dacs902EventHandler eventhandler = (Dacs902EventHandler) this.domaintree.search(uri, "Dacs902EventHandler");
            if (eventhandler != null) {
                return eventhandler;
            } else {
                LOG.info("using event902defaulthandler");
                return this.event902defaulthandler;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    private Dacs905EventHandler getDacs905EventHandler(DacsGetMethod dacsget) {
        try {
            String uri = dacsget.getURI().getHost();
            Dacs905EventHandler eventhandler = (Dacs905EventHandler) this.domaintree.search(uri, "Dacs905EventHandler");
            if (eventhandler != null) {
                return eventhandler;
            } else {
                LOG.info("using event905defaulthandler");
                return this.event905defaulthandler;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    private Dacs9xxEventHandler getDacs9xxEventHandler(DacsGetMethod dacsget) {
        try {
            String uri = dacsget.getURI().getHost();
            Dacs9xxEventHandler eventhandler = (Dacs9xxEventHandler) this.domaintree.search(uri, "Dacs9xxEventHandler");
            if (eventhandler != null) {
                return eventhandler;
            } else {
                return this.event9xxdefaulthandler;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    private int handleDacsAccessDenied(DacsGetMethod dacsget)
    throws DacsException {
        XmlObject expectedXmlObject;
        // parse XML and call event handlers if appropriate
        try {
            expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
        } catch (IOException ex) {
            throw new DacsException("IOException thrown processing dacsget:" + dacsget.toString());
        } catch (XmlException ex) {
            throw new DacsException("XmlException thrown parsing response from dacsget:" + dacsget.toString());
        }
        // Check that it is an instance of the DacsAcsDocument
        LOG.info("document type: " + expectedXmlObject.getClass().getName());
        if(expectedXmlObject instanceof DacsAcsDocument) {
            DacsAcsDocument doc = (DacsAcsDocument)expectedXmlObject;
            if (doc.validate()) { // valid DacsAcsDocument from check_only or check_fail
                DacsAcsDocument.DacsAcs dacsacs = doc.getDacsAcs();
                if (dacsacs.isSetAccessDenied()) {
                    AccessDeniedDocument.AccessDenied denied = dacsacs.getAccessDenied();
                    if (denied.isSetEvent900()) {
                        Event900Document.Event900 event = denied.getEvent900();
                        Dacs9xxEventHandler event9xxhandler = this.getDacs9xxEventHandler(dacsget);
                        return event9xxhandler.handleEvent(this, dacsget, new DacsAccess9xxEvent(dacsacs.getFedDomain(), dacsacs.getJurDacsUrl(), event));
                    } else if (denied.isSetEvent901()) {
                        Event901Document.Event901 event = denied.getEvent901();
                        Dacs9xxEventHandler event9xxhandler = this.getDacs9xxEventHandler(dacsget);
                        return event9xxhandler.handleEvent(this, dacsget, new DacsAccess9xxEvent(dacsacs.getFedDomain(), dacsacs.getJurDacsUrl(), event));
                    } else if (denied.isSetEvent902()) {
                        Event902Document.Event902 event = denied.getEvent902();
                        Dacs902EventHandler event902handler = this.getDacs902EventHandler(dacsget);
                        return event902handler.handleEvent(this, dacsget, new DacsAccess902Event(dacsacs.getFedDomain(), dacsacs.getJurDacsUrl(), event));
                    } else if (denied.isSetEvent903()) {
                        Event903Document.Event903 event = denied.getEvent903();
                        Dacs9xxEventHandler event9xxhandler = this.getDacs9xxEventHandler(dacsget);
                        return event9xxhandler.handleEvent(this, dacsget, new DacsAccess9xxEvent(dacsacs.getFedDomain(), dacsacs.getJurDacsUrl(), event));
                    } else if (denied.isSetEvent904()) {
                        Event904Document.Event904 event = denied.getEvent904();
                        Dacs9xxEventHandler event9xxhandler = this.getDacs9xxEventHandler(dacsget);
                        return event9xxhandler.handleEvent(this, dacsget, new DacsAccess9xxEvent(dacsacs.getFedDomain(), dacsacs.getJurDacsUrl(), event));
                    } else if (denied.isSetEvent905()) {
                        Event905Document.Event905 event = denied.getEvent905();
                        Dacs905EventHandler event905handler = this.getDacs905EventHandler(dacsget);
                        if (event905handler != null) {
                            return event905handler.handleEvent(this, dacsget, new DacsAccess905Event(dacsacs.getFedDomain(), dacsacs.getJurDacsUrl(), event));
                        } else {
                            return DacsStatus.SC_DACS_ACCESS_DENIED;
                        }
                    } else if (denied.isSetEvent998()) {
                        Event998Document.Event998 event = denied.getEvent998();
                        Dacs9xxEventHandler event9xxhandler = this.getDacs9xxEventHandler(dacsget);
                        return event9xxhandler.handleEvent(this, dacsget, new DacsAccess9xxEvent(dacsacs.getFedDomain(), dacsacs.getJurDacsUrl(), event));
                    } else {
                        throw new DacsException("access denied did not set event");
                    }
                } else {
                    throw new DacsException("access denied not set");
                }
            } else {
                throw new DacsException("invalid DacsAcsDocument");
            }
        } else {
            throw new DacsException("invalid DacsAcsDocument");
        }
    }
    
    
    /**
     * signout (invalidate) DACS username;
     * Note: does NOT call the DacsSignoutService - this simply
     * removes the associated cookie from this user's DacsContext
     * @param dacs_username DACS username to signout
     * @return cookie that was signed out (set to expired)
     */
    public Cookie signout(String dacs_username) {
        Cookie expired = null;
        Cookie[] cookies = this.httpclient.getState().getCookies();
        // create a date in the past
        Date date = new Date();
        date.setTime(0);
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("DACS:" + dacs_username)) {
                // expire the matching cookie
                cookie.setExpiryDate(date);
                expired = cookie;
            }
        }
        httpclient.getState().purgeExpiredCookies();
        return expired;
    }
    
    /**
     * signout (invalidate) DACS username in DacsContext and HttpServletResponse;
     * Note: does NOT call the DacsSignoutService - this simply
     * removes the associated cookie from this user's DacsContext and sets
     * expired cookie in HttpServletResponse
     * @param dacs_username DACS username to signout
     * @param response HttpServletResponse to be updated
     */
    public void signout(String dacs_username, HttpServletResponse response) {
        Cookie cookie = signout(dacs_username);
        if (cookie != null) { // may be null if user already signed out or expired
            // invalidate in the response to browser
            javax.servlet.http.Cookie jcookie = new javax.servlet.http.Cookie(cookie.getName(),cookie.getValue());
            jcookie.setDomain(cookie.getDomain());
            jcookie.setPath(cookie.getPath());
            // expire the cookie
            jcookie.setMaxAge(0);
            response.addCookie(jcookie);
        }
    }

    /**
     * signout credentials in federation from user context;
     * @param fed delete cookies for this DACS federation;
     */

    public void signoutFederation(Federation fed) {
        removeMatchingCookie("DACS:" + fed.getName());
    }
    
    /**
     * signout credentials in jurisdiction from user context;
     * @param jur delete cookies for this DACS jurisdiction;
     */
    public void signoutJurisdiction(Jurisdiction jur) {
        removeMatchingCookie("DACS:" + jur.getFederation().getName() + "::" + jur.getName());
    }

    /**
     * clear cookies
     */
    public void clearCookies() {
        httpclient.getState().clearCookies();
    }
    
    /**
     * remove cookie;
     * Note: this simply removes the associated cookie from this user's DacsContext
     * @param cookiename name of cookie to remove
     */
    public void removeCookie(String cookiename) {
        Cookie[] cookies = httpclient.getState().getCookies();
        // create a date in the past
        Date date = new Date();
        date.setTime(0);
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookiename)) {
                // expire the matching cookie
                cookie.setExpiryDate(date);
                if (cookie.isExpired()) {
                    System.out.println(cookie.getName() + " is expired");
                }
            }
        }
        httpclient.getState().purgeExpiredCookies();
    }
    
    /**
     * remove cookie that start with prefix string;
     * Note: simply removes the associated cookie from this user's DacsContext
     * user signout(String, ServletResponse) to invalidate cookies in user agent
     * as well
     * @param prefix remove cookies starting with this prefix string
     */
    public void removeMatchingCookie(String prefix) {
        Date date = new Date();
        date.setTime(0);
        for (Cookie cookie : this.getDacsCookies()) {
            if (cookie.getName().startsWith(prefix)) {
                System.out.println(cookie.getName());
                // expire the matching cookie
                cookie.setExpiryDate(date);
            }
        }
        httpclient.getState().purgeExpiredCookies();
    }
        
    /**
     * remove (invalidate) NAT;
     * Note: this simply removes the associated cookie from this user's DacsContext
     * @param dacs_nat the NAT to remove
     */
    public void removeNat(String dacs_nat) {
        this.removeCookie("NAT-DACS:" + dacs_nat);
    }
    
    /**
     * get cookie in DacsContext by name
     * @param name name of cookie to retrieve
     * @return cookie matching @param name, else null
     */
    public Cookie getCookieByName(String name) {
        Cookie[] cookies = httpclient.getState().getCookies();
        for(Cookie cookie:cookies) {
            if(cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return (Cookie) null;
    }

        

        
    /**
     * externalize and dump DACS credential cookies present
     * in dacscontext to PrintStream @param out
     * @param out output stream to write to
     */
    public void dumpDacsCookies(PrintStream out) {
        // Display the cookies
        out.println("Present cookies: ");
        for (Cookie cookie : this.getDacsCookies()) {
            out.println(cookie.getName() +
                    "(version = " + cookie.getVersion() +
                    ", domain = " + cookie.getDomain() +
                    ", path = " + cookie.getPath() +
//                    ", expiry = " + cookie.getExpiryDate() != null ? cookie.getExpiryDate().toString() : "null" +
                    ")");
            out.println("External Form: " + cookie.toExternalForm());
        }
    }
    
    /**
     * externalize and dump DACS credential cookies present
     * in dacscontext to PrintWriter @param out
     * @param out PrintWriter to write to (used in servlets)
     */
    public void dumpDacsCookies(PrintWriter out) {
        // Display the cookies
        out.println("Present cookies: ");
        for (Cookie cookie : this.getDacsCookies()) {
            out.println(cookie.getName() +
                    "(version = " + cookie.getVersion() +
                    ", domain = " + cookie.getDomain() +
                    ", path = " + cookie.getPath() +
//                    ", expiry = " + cookie.getExpiryDate() != null ? cookie.getExpiryDate().toString() : "null" +
                    ")");
            out.println("External Form: " + cookie.toExternalForm());
        }
    }
    
    /**
     * externalize DACS NATs present in dacscontext and dump to PrintStream @para out
     * @param out PrintStream to write to
     */
    public void dumpDacsNats(PrintStream out) {
        // Display the cookies
        out.println("Present NAT cookies: ");
        for (Cookie cookie : this.getDacsNatCookies()) {
            out.println(cookie.getName() + "=" + cookie.getValue());
            String decodedString = new String(Base64.decodeBase64(cookie.getValue().getBytes()));
            out.println("Decoded Value: " + decodedString);
        }
    }
    
    /**
     * externalize NATs present in dacscontext and dump to PrintWriter @param out
     * @param out PrintWriter to write to
     */
    public void dumpDacsNats(PrintWriter out) {
        // Display the cookies
        out.println("Present NAT cookies: ");
        for (Cookie cookie : this.getDacsNatCookies()) {
            byte[] encodedarray = cookie.getValue().getBytes();
            out.println(cookie.getName() + "=" + cookie.getValue());
            String decodedString = new String(Base64.decodeBase64(encodedarray));
            out.println("Base64: " + Base64.isArrayByteBase64(encodedarray));
            out.println("Decoded Value: " + decodedString);
        }
    }
    
    /**
     * get list of cookies set in httpclient
     * @return list of DacsCookies
     */
    public List<Cookie> getCookies() {
        // Get all the DACS credential cookies
        Cookie[] cookies = httpclient.getState().getCookies();
        List<Cookie> dacscookies = new ArrayList<Cookie>();
        for (Cookie cookie : cookies) {
            if (cookie.getName().startsWith("DACS:") || cookie.getName().startsWith("NAT-")) {
                dacscookies.add(cookie);
            }
        }
        return dacscookies;
    }
    
    /**
     * get DACS cookies present in dacscontext (httpclient state)
     * @return list of DACS credential Cookies
     */
    public List<Cookie> getDacsCookies() {
        // Get all the DACS credential cookies
        Cookie[] cookies = httpclient.getState().getCookies();
        List<Cookie> dacscookies = new ArrayList<Cookie>();
        for (Cookie cookie : cookies) {
            if (cookie.getName().startsWith("DACS:")) {
                dacscookies.add(cookie);
            }
        }
        return dacscookies;
    }
    
    /**
     * get names of DACS cookies present in dacscontext (httpclient state)
     * @return list of DACS credential cookie names
     */
    public List<String> getDacsUsernames() {
        // Get all the DACS usernames from DACS credential cookies
        List<String> cookienames = new ArrayList<String>();
        for (Cookie cookie : httpclient.getState().getCookies()) {
            String cookiename = cookie.getName();
            if (cookiename.startsWith("DACS:")) {
                cookienames.add(cookiename.substring(cookiename.indexOf(':') + 1));
            }
        }
        return cookienames;
    }
    
    /**
     * filtered getter (Federation) for DACS usernames in dacscontext;
     * @param federation federation to filter on
     * @return list of DACS usernames in federation
     */
    public List<String> getDacsUsernames(Federation federation) {
        List<String> cookienames = new ArrayList<String>();
        for (Cookie cookie : httpclient.getState().getCookies()) {
            String cookiename = cookie.getName();
            if (cookiename.startsWith("DACS:" + federation.getName())) {
                cookienames.add(cookiename.substring(cookiename.indexOf(':') + 1));
            }
        }
        return cookienames;
    }
    
    /**
     * filtered getter (Jurisdiction) for DACS usernames in dacscontext;
     * @param jurisdiction jurisdiction to filter on
     * @return list of DACS usernames in jurisdiction
     */
    public List<String> getDacsUsernames(Jurisdiction jurisdiction) {
        // Get all the DACS NAT cookies
        List<String> cookienames = new ArrayList<String>();
        for (Cookie cookie : httpclient.getState().getCookies()) {
            String cookiename = cookie.getName();
            if (cookiename.startsWith("DACS:" + jurisdiction.getFederation().getName() + ":" + jurisdiction.getName())) {
                cookienames.add(cookiename.substring(cookiename.indexOf(':') + 1));
            }
        }
        return cookienames;
    }
    
    /**
     * get DACS NAT cookies present in dacscontext (httpclient state)
     * @return list of DACS NAT Cookies present
     */
    public List<Cookie> getDacsNatCookies() {
        // Get all the DACS NAT cookies
        Cookie[] cookies = httpclient.getState().getCookies();
        List<Cookie> natcookies = new ArrayList<Cookie>();
        for (Cookie cookie : cookies) {
            if (cookie.getName().startsWith("NAT-")) {
                natcookies.add(cookie);
            }
        }
        return natcookies;
    }
    
    /**
     * get DACS NAT cookies present in dacscontext (httpclient state)
     * @return list of DACS NAT Cookies present
     */
    public List<NAT> getDacsNats() {
        // Get all the DACS NAT cookies
        Cookie[] cookies = httpclient.getState().getCookies();
        List<NAT> nats = new ArrayList<NAT>();
        for (Cookie cookie : cookies) {
            if (cookie.getName().startsWith("NAT-")) {
                nats.add(new NAT(cookie));
            }
        }
        return nats;
    }
    
    /**
     * get DACS Notice Acknowledgment Token (NAT)
     * @param dacs_natname
     * @return DJL NAT object derived from DACS NAT cookie
     */
    public NAT getDacsNatByName(String dacs_natname) {
        Cookie[] cookies = httpclient.getState().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("NAT-DACS:" + dacs_natname)) {
                return new NAT(cookie);
            }
        }
        return null;
    }
    
    /**
     * get the notice uris associated with a named DACS NAT
     * @param dacs_natname
     * @return notice_uris as a String
     */
    public String getNoticeUris(String dacs_natname) {
        Cookie[] cookies = httpclient.getState().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("NAT-DACS:" + dacs_natname)) {
                NAT nat = new NAT(cookie);
                return nat.getNoticeUris();
            }
        }
        return null;
    }
    
    /**
     * get names of DACS Notice Acknowledgement Token (NAT) cookies present in dacscontext (httpclient state)
     * @return list of DACS NAT cookie names
     */
    public List<String> getDacsNatNames() {
        // Get all the DACS NAT cookies
        List<String> cookienames = new ArrayList<String>();
        for (Cookie cookie : httpclient.getState().getCookies()) {
            String cookiename = cookie.getName();
            if (cookiename.startsWith("NAT-")) {
                cookienames.add(cookiename.substring(cookiename.indexOf(':') + 1));
            }
        }
        return cookienames;
    }
    
    /**
     * add DACS or NAT cookie to httpclient state
     * @param domain federation domain
     * @param name cookie name
     * @param value cookie value
     * @param path cookie path (DACS default is /)
     * @param maxage how long the cookie should remain valid
     * @param secure a secure cookie must only be sent over HTTPS
     */
    public void addDacsCookie(String domain, String name, String value, String path, int maxage, boolean secure) {
        // look for matching cookie in DacsContext
        Cookie thiscookie = getCookieByName(name);
        if (thiscookie != null) {
            // do nothing if identical cookie is present in DacsContext
            if (thiscookie.getValue().equals(value)) {
                return;
            }
            // cookie with same name but different value present in DacsContext
            //    -- nuke it and add the new one -- 
            // this is relevant when the browser has obtained credentials 
            // outside of FedAdmin app; we assume the browser holds the 
            // definitive version of state
            if (thiscookie.getDomain().equals(domain)) {
                thiscookie.setExpiryDate(new Date(0));
                httpclient.getState().purgeExpiredCookies();
            }
        }
        Cookie cookie = new Cookie(domain, name, value, path, maxage, secure);
        httpclient.getState().addCookie(cookie);
    }
    
    /**
     * set DACS cookies received in request in dacscontext; note that cookies set
     * in HttpServletRequest are of type javax.servlet.http.Cookie which we convert
     * to cookies of type org.apache.commons.httpclient.Cookie by addDacsCookie()
     *
     * @param request request from client potentially with accompanying DACS cookies
     * @param domain domain value to assign to cookie (cookie sent in request do not
     * contain domain or secure values)
     * @param issecure secure value to assign to cookie
     *
     */
    public void setDacsCookies(HttpServletRequest request, String domain, boolean issecure) {
        javax.servlet.http.Cookie[] jcookies = request.getCookies();
        if (jcookies != null) {
            for (javax.servlet.http.Cookie jcookie : jcookies) {
                String cookiename = jcookie.getName();
                String path = jcookie.getPath();
                if (cookiename.startsWith("DACS:") || cookiename.startsWith("NAT-")) {
                    if (domain.startsWith(".")) {
                        addDacsCookie(domain, cookiename, jcookie.getValue(),"/",jcookie.getMaxAge(),issecure);
                    } else {
                        addDacsCookie("." + domain, cookiename, jcookie.getValue(),"/",jcookie.getMaxAge(),issecure);
                    }
                }
            }
        }
    }
    
    /**
     * set DACS cookies in dacscontext in HttpServletResponse;
     * note that cookies set in HttpServletResponse are of type 
     * javax.servlet.http.Cookie which we create by converting dacscontext
     * cookies of type org.apache.commons.httpclient.Cookie by addDacsCookie()
     *
     * @param request request from client potentially with accompanying DACS cookies
     * @param domain domain value to assign to cookie (cookie sent in request do not
     * contain domain or secure values)
     * @param issecure secure value to assign to cookie
     *
     */
    public void setDacsCookies(HttpServletResponse response) {
        Cookie[] cookies = httpclient.getState().getCookies();
        for (Cookie cookie : cookies) {
            if (! cookie.isExpired() & cookie.getName().startsWith("DACS:")) {
                javax.servlet.http.Cookie jcookie = new javax.servlet.http.Cookie(cookie.getName(),cookie.getValue());
                jcookie.setDomain(cookie.getDomain());
                jcookie.setPath(cookie.getPath());
                jcookie.setSecure(cookie.getSecure());
                response.addCookie(jcookie);
            }
        }
    }
    
    
}
/*
 * DacsClientContextTest.java
 * Created on Jan 15, 2010 8:24:49 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.http;

import java.net.URI;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.cookie.Cookie;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
public class DacsClientContextTest extends TestCase {

    DacsClientContext dacsClientContext;

    public DacsClientContextTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dacsClientContext = new DacsClientContext();
    }

    public void testExecuteGetRequest() throws Exception {
        URI uri = URIUtils.createURI("https", "fedroot.com", -1, "/dacs/dacs_current_credentials", "FORMAT=XML", null);
        DacsGetRequest dacsGetRequest = new DacsGetRequest(uri);
        DacsResponse dacsResponse = dacsClientContext.executeGetRequest(dacsGetRequest);
        HttpEntity httpEntity = dacsResponse.getHttpResponse().getEntity();
        assertNotNull(httpEntity);
        assertEquals(200, dacsResponse.getHttpResponse().getStatusLine().getStatusCode());
        assertEquals("Content-Type: text/xml", httpEntity.getContentType().toString());
    }

    public void testGetAllCookies() throws Exception {
        URI uri = URIUtils.createURI("http", "www.google.com", -1, "/search", "q=httpclient&btnG=Google+Search&aq=f&oq=", null);
        DacsGetRequest dacsGetRequest = new DacsGetRequest(uri);
        DacsResponse dacsResponse = dacsClientContext.executeGetRequest(dacsGetRequest);
        List<Cookie> cookies = dacsClientContext.getAllCookies();
        assertEquals(2, cookies.size());
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
            assertFalse(cookie.isSecure());
            assertFalse(cookie.isExpired(new Date()));
            assertFalse(DacsCookie.isDacsCookie(cookie));
        }

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        dacsClientContext.closeConnection();
    }

}

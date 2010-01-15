package com.fedroot.dacs.services;
/*
 * DacsServicesTest.java
 *
 * Created on August 26, 2005, 10:40 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

import com.fedroot.dacs.http.DacsGetMethod;
import junit.framework.*;
import com.fedroot.dacs.test.TestConstants;
import com.fedroot.dacs.xmlbeans.*;
import com.fedroot.dacs.xmlbeans.DacsAuthAgentDocument.DacsAuthAgent;
import org.apache.xmlbeans.XmlObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpClientParams;

/**
 * tests for dacs-services;
 * uses TestConstants
 */

public class DacsServicesTest extends TestCase {
    // HttpClient et al are declared static to allow tests to inherit state
    // established by preceding tests
    // note that we do not use DacsContext, since it belongs to the dacs-client
    // library which itself relies on this (dacs-services) library
    private static HttpClient httpclient;
    private static HttpClientParams clientparams;
    private String hmac;
    private String time;
    
    /**
     * constructor for DacsServicesTest
     */
    public DacsServicesTest(String testName) {
        super(testName);
        httpclient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        clientparams = httpclient.getParams();
        // select old style, browser-compatible cookie
        clientparams.setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
    }
    
    public void testDacsAcsXml_902()
    throws Exception {
        System.out.println("testDacsAcsXml (902)");
        try {
            DacsGetMethod dacsget = new DacsGetMethod(TestConstants.event902uri);
            dacsget.setDacsAcs(DACS.AcsCheck.check_only, DACS.ReplyFormat.XMLSCHEMA);
            int httpstatus = httpclient.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) {
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                // Check that it is an instance of the DacsAcsDocument
                System.out.println(expectedXmlObject.getClass().getName());
                if(expectedXmlObject instanceof DacsAcsDocument) {
                    DacsAcsDocument doc =
                            (DacsAcsDocument)expectedXmlObject;
                    assertTrue("invalid DacsAcsDocument", doc.validate());
                    DacsAcsDocument.DacsAcs dacsacs = doc.getDacsAcs();
                    assertTrue("wrong DACS return status", dacsacs.isSetAccessDenied());
                    assertTrue("wrong access_denied event", dacsacs.getAccessDenied().isSetEvent902());
                    
                } else {
                    fail("incorrect XML document type");
                }
            } else {
                fail("httpstatus != 200");
            }
        } catch (Exception e) {
            fail("unexpected exception thrown: " + e.getMessage());
        }
    }
    
    // this test must be run before testDacsConfXml which depends
    // on authentication as an ADMIN_IDENTITY
    public void testDacsAuthenticateXml()
    throws Exception {
        System.out.println("testDacsAuthenticateXml (failure mode 1)");
        try {
            DacsAuthenticateService dacsservice =
                    new DacsAuthenticateService(TestConstants.mlbaseuri, TestConstants.mljurisdiction, TestConstants.mlusername_1, TestConstants.badpassword_1);
            DacsGetMethod httpget = dacsservice.getDacsGetMethod();
            int httpstatus = httpclient.executeMethod(httpget);
            if (httpstatus == HttpStatus.SC_OK) {
                XmlObject expectedXmlObject = XmlObject.Factory.parse(httpget.getResponseBodyAsStream());
                // Check that it is an instance of the DacsListJurisdictionsDocument
                System.out.println(expectedXmlObject.getClass().getName());
                if(expectedXmlObject instanceof DacsAuthReplyDocument) {
                    DacsAuthReplyDocument doc =
                            (DacsAuthReplyDocument)expectedXmlObject;
                    DacsAuthReplyDocument.DacsAuthReply authreply = doc.getDacsAuthReply();
                    assertFalse(authreply.isSetDacsCurrentCredentials());
                    assertTrue(authreply.isSetCommonStatus());
                    assertTrue("invalid common_error element", authreply.getCommonStatus().validate());
                    assertEquals(999, authreply.getCommonStatus().getCode());
                    assertEquals("800 Authentication failed, invalid authenticating information (Authentication failed)", authreply.getCommonStatus().getMessage());
                    
                } else {
                    fail("incorrect XML document type");
                }
            } else {
                fail("httpstatus != 200");
            }
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
        System.out.println("testDacsAuthenticateXml (failure mode 2)");
        try {
            DacsAuthenticateService dacsservice =
                    new DacsAuthenticateService(TestConstants.mlbaseuri, TestConstants.mljurisdiction, TestConstants.badusername_1, TestConstants.badpassword_1);
            DacsGetMethod httpget = dacsservice.getDacsGetMethod();
            int httpstatus = httpclient.executeMethod(httpget);
            if (httpstatus == HttpStatus.SC_OK) {
                XmlObject expectedXmlObject = XmlObject.Factory.parse(httpget.getResponseBodyAsStream());
                // Check that it is an instance of the DacsAuthReplyDocument
                System.out.println(expectedXmlObject.getClass().getName());
                if(expectedXmlObject instanceof DacsAuthReplyDocument) {
                    DacsAuthReplyDocument doc =
                            (DacsAuthReplyDocument)expectedXmlObject;
                    DacsAuthReplyDocument.DacsAuthReply authreply = doc.getDacsAuthReply();
                    assertFalse(authreply.isSetDacsCurrentCredentials());
                    assertTrue(authreply.isSetCommonStatus());
                    assertTrue("invalid CommonError", authreply.getCommonStatus().validate());
                    assertEquals(999, authreply.getCommonStatus().getCode());
                    assertEquals("800 Authentication failed, invalid authenticating information (Authentication failed)", authreply.getCommonStatus().getMessage());
                    
                } else {
                    fail("incorrect XML document type");
                }
            } else {
                fail("httpstatus != 200");
            }
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
        System.out.println("testDacsAuthenticateXml (success mode)");
        try {
            DacsAuthenticateService dacsservice =
                    new DacsAuthenticateService(TestConstants.testbaseuri, TestConstants.testjurisdiction, TestConstants.testadminusername, TestConstants.testadminpassword);
            DacsGetMethod httpget = dacsservice.getDacsGetMethod();
            int httpstatus = httpclient.executeMethod(httpget);
            if (httpstatus == HttpStatus.SC_OK) {
                XmlObject expectedXmlObject = XmlObject.Factory.parse(httpget.getResponseBodyAsStream());
                // Check that it is an instance of the DacsAuthReplyDocument
                System.out.println(expectedXmlObject.getClass().getName());
                if(expectedXmlObject instanceof DacsAuthReplyDocument) {
                    DacsAuthReplyDocument doc =
                            (DacsAuthReplyDocument)expectedXmlObject;
                    DacsAuthReplyDocument.DacsAuthReply authreply = doc.getDacsAuthReply();
                    System.out.println(authreply.toString());
                    assertTrue(authreply.isSetDacsCurrentCredentials());
                    assertFalse(authreply.isSetCommonStatus());
                    assertTrue("invalid DacsCurrentCredentials", authreply.getDacsCurrentCredentials().validate());
                    assertEquals("login failed", TestConstants.testadminusername, authreply.getDacsCurrentCredentials().getCredentialsArray(0).getName());
                } else {
                    fail("incorrect XML document type");
                }
            } else {
                fail("httpstatus != 200");
            }
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
    }
    
    public void testDacsAcsXml_905()
    throws Exception {
        System.out.println("testDacsAcsXml (905 secure)");
        try {
            DacsGetMethod dacsget = new DacsGetMethod(TestConstants.event905secureuri);
            dacsget.setDacsAcs(DACS.AcsCheck.check_only, DACS.ReplyFormat.XMLSCHEMA);
            int httpstatus = httpclient.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) {
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                // Check that it is an instance of the DacsAcsDocument
                System.out.println(expectedXmlObject.getClass().getName());
                if(expectedXmlObject instanceof DacsAcsDocument) {
                    DacsAcsDocument doc =
                            (DacsAcsDocument)expectedXmlObject;
                    System.out.println(doc.getDacsAcs().toString());
                    assertTrue("invalid DacsAcsDocument", doc.validate());
                    DacsAcsDocument.DacsAcs dacsacs = doc.getDacsAcs();
                    assertTrue("wrong DACS return status", dacsacs.isSetAccessDenied());
                    assertTrue("wrong access_denied event", dacsacs.getAccessDenied().isSetEvent905());
                    assertTrue("hmac not set", dacsacs.getAccessDenied().getEvent905().isSetHmac());
                    // set hmac and time for later use in dacs_notices ack test
                    hmac = dacsacs.getAccessDenied().getEvent905().getHmac();
                    time = dacsacs.getAccessDenied().getEvent905().getTime().toString();
                } else {
                    fail("incorrect XML document type");
                }
            } else {
                fail("httpstatus != 200");
            }
        } catch (Exception e) {
            fail("unexpected exception thrown: " + e.getMessage());
        }
    }
    
    public void testDacsListJurisdictionsXml()
    throws Exception {
        System.out.println("testDacsListJurisdictionsXml");
        try {
            DacsListJurisdictionsService dacsservice =
                    new DacsListJurisdictionsService(TestConstants.mlbaseuri);
            DacsGetMethod httpget = dacsservice.getDacsGetMethod();
            int httpstatus = httpclient.executeMethod(httpget);
            if (httpstatus == HttpStatus.SC_OK) {
                XmlObject expectedXmlObject = XmlObject.Factory.parse(httpget.getResponseBodyAsStream());
                // Check that it is an instance of the DacsListJurisdictionsDocument
                System.out.println(expectedXmlObject.getClass().getName());
                if(expectedXmlObject instanceof DacsListJurisdictionsDocument) {
                    DacsListJurisdictionsDocument doc =
                            (DacsListJurisdictionsDocument)expectedXmlObject;
                    assertTrue("invalid DacsListJurisdictionsDocument", doc.validate());
                } else {
                    fail("incorrect XML document type");
                }
            } else {
                fail("httpstatus != HttpStatus.SC_OK");
            }
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
    }
    
    public void testDacsConfXml()
    throws Exception {
        System.out.println("testDacsConfXml");
        try {
            DacsConfService dacsservice =
                    new DacsConfService(TestConstants.testbaseuri);
            DacsGetMethod httpget = dacsservice.getDacsGetMethod();
            int httpstatus = httpclient.executeMethod(httpget);
            if (httpstatus == HttpStatus.SC_OK) {
                System.out.println(httpget.getResponseBodyAsString());
                XmlObject expectedXmlObject = XmlObject.Factory.parse(httpget.getResponseBodyAsStream());
                // Check that it is an instance of the DacsListJurisdictionsDocument
                System.out.println(expectedXmlObject.getClass().getName());
                if(expectedXmlObject instanceof DacsConfReplyDocument) {
                    DacsConfReplyDocument doc =
                            (DacsConfReplyDocument)expectedXmlObject;
                    assertTrue("invalid DacsConfReplyDocument", doc.validate());
                } else {
                    fail("incorrect XML document type");
                }
            } else {
                fail("httpstatus != HttpStatus.SC_OK");
            }
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
    }
    
    public void testDacsAuthAgentXml()
    throws Exception {
        System.out.println("testAuthAgentXml");
        try {
            DacsAuthAgentService dacsservice =
                    new DacsAuthAgentService(TestConstants.testbaseuri, TestConstants.testjurisdiction, TestConstants.badusername_1);
            DacsGetMethod httpget = dacsservice.getDacsGetMethod();
            int httpstatus = httpclient.executeMethod(httpget);
            if (httpstatus == HttpStatus.SC_OK) {
                System.out.println(httpget.getResponseBodyAsString());
                XmlObject expectedXmlObject = XmlObject.Factory.parse(httpget.getResponseBodyAsStream());
                // Check that it is an instance of the DacsListJurisdictionsDocument
                System.out.println(expectedXmlObject.getClass().getName());
               if(expectedXmlObject instanceof DacsAuthAgentDocument) {
                    DacsAuthAgentDocument doc =
                            (DacsAuthAgentDocument)expectedXmlObject;
                    assertTrue("invalid DacsAuthAgent", doc.getDacsAuthAgent().validate());
                } else {
                    fail("incorrect XML document type");
                }
            } else {
                fail("httpstatus != HttpStatus.SC_OK");
            }
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
    }
}
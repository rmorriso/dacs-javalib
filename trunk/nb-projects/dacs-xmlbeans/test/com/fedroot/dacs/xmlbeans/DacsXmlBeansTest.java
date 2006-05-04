package com.fedroot.dacs.xmlbeans;
/*
 * DacsXmlBeansTest.java
 *
 * Created on August 26, 2005, 10:40 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

import junit.framework.*;
import com.fedroot.dacs.xmlbeans.*;
import org.apache.xmlbeans.XmlObject;


/**
 * tests for dacs-services;
 * uses TestConstants
 */

public class DacsXmlBeansTest extends TestCase {
    // HttpClient et al are declared static to allow tests to inherit state
    // established by preceding tests
    // note that we do not use DacsContext, since it belongs to the dacs-client
    // library which itself relies on this (dacs-services) library
    private String hmac;
    private String time;
    
    /**
     * constructor for DacsXmlBeansTest
     */
    public DacsXmlBeansTest(String testName) {
        super(testName);
    }
    
    public void testDacsAcsXml_902()
    throws Exception {
        System.out.println("testDacsAcsXml (902)");
        try {
            // TODO - change getResponseBody() to read from file instead
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
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
        System.out.println("testDacsAuthenticateXml (failure mode 2)");
        try {
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
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
        System.out.println("testDacsAuthenticateXml (success mode)");
        try {
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
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
    }
    
    public void testDacsAcsXml_905()
    throws Exception {
        System.out.println("testDacsAcsXml (905 secure)");
        try {
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
        } catch (Exception e) {
            fail("unexpected exception thrown: " + e.getMessage());
        }
    }
    
    public void testDacsListJurisdictionsXml()
    throws Exception {
        System.out.println("testDacsListJurisdictionsXml");
        try {
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
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
    }
    
    public void testDacsConfXml()
    throws Exception {
        System.out.println("testDacsConfXml");
        try {
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
        } catch (Exception e) {
            fail("unexpected exception thrown");
        }
    }
}
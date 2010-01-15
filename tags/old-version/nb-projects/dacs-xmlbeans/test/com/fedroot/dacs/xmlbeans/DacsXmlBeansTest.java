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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.xmlbeans.XmlObject;


/**
 * tests for dacs-xmlbeans;
 * uses TestConstants
 */

public class DacsXmlBeansTest extends TestCase {
    // HttpClient et al are declared static to allow tests to inherit state
    // established by preceding tests
    // note that we do not use DacsContext, since it belongs to the dacs-client
    // library which itself relies on this (dacs-services) library
    private String hmac;
    private String time;
    private HttpClient httpclient;
    private GetMethod httpget;
    
    /**
     * constructor for DacsXmlBeansTest
     */
    public DacsXmlBeansTest(String testName) {
        super(testName);
        httpclient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        httpget = new GetMethod("https://fedroot.com/dacs/dacs_list_jurisdictions?FORMAT=XMLSCHEMA");
    }
    
    public void testListJurisdictionsXml()
    throws Exception {
        System.out.println("testListJurisdictionsXml");
        try {
            // TODO - change getResponseBody() to read from file instead
            httpclient.executeMethod(httpget);
            // DacsListJurisdictionsDocument expectedXmlObject = DacsListJurisdictionsDocument.Factory.parse(httpget.getResponseBodyAsStream());
            XmlObject expectedXmlObject = XmlObject.Factory.parse(httpget.getResponseBodyAsStream());
            // Check that it is an instance of the DacsAcsDocument
            System.out.println(expectedXmlObject.getClass().getName());
            System.out.println(expectedXmlObject.toString());
            if(expectedXmlObject instanceof DacsListJurisdictionsDocument) {
                DacsListJurisdictionsDocument doc =
                        (DacsListJurisdictionsDocument)expectedXmlObject;
                assertTrue("invalid DacsListJurisdictionsDocument", doc.validate());
                DacsListJurisdictionsDocument.DacsListJurisdictions jurs = doc.getDacsListJurisdictions();
                assertEquals(14, jurs.getJurisdictionList().size());
            } else {
                fail("incorrect XML document type");
            }
        } catch (Exception e) {
            fail("unexpected exception thrown: " + e.getMessage());
        }
    }
    
    
}
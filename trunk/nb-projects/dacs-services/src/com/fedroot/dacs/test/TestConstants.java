/*
 * TestConstants.java
 *
 * Created on September 22, 2005, 9:04 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.test;

/**
 * static constants used in JUnit tests
 * @author rmorriso
 */
public class TestConstants {
    // total number of jurisdictions in DEMO federation
    public static final int numDEMOjurisdictions = 9;
    public static final int numTESTjurisdictions = numDEMOjurisdictions;
    // total number of authenticating jurisdictions in DEMO federation
    public static final int numauthDEMOjurisdictions = 4;
    public static final int numauthTESTjurisdictions = numauthDEMOjurisdictions;
    public static final String fedadminuri = "https://demo.fedroot.com/fedadmin/dacs";
    public static final String fedname = "DEMO";
    // OpenID Jurisdiction
    public static final String openiduri = "https://demo.fedroot.com/openid/dacs";
    // CubeWerx jurisdiction
    public static final String cwbaseuri = "https://cubewerx.demo.fedroot.com/cgi-bin/dacs";
    public static final String cwjurisdiction = "CUBEWERX";
    public static final String cwevent905secureuri = "https://cubewerx.demo.fedroot.com/demo/cubeserv/cubeserv.cgi?SERVICE=WMS&VERSION=1.3.1&REQUEST=GetMap&CRS=EPSG%3A4326&BBOX=44.59825510781435,-131.042791827497,53.40174489218565,-116.957208172503&WIDTH=560&HEIGHT=350&LAYERS=ETOPO2%3AFoundation,GTOPO30%3AFoundation,COASTL_1M%3AFoundation&STYLES=,,&FORMAT=image%2Fpng%3B+PhotometricInterpretation%3DPaletteColor&BGCOLOR=0xFFFFFF&TRANSPARENT=FALSE&EXCEPTIONS=INIMAGE&QUALITY=MEDIUM";
    public static final String cwnotice_uris = "http://demo.fedroot.com/notices/geobase-license-agreement.html http://demo.fedroot.com/notices/usgs-disclaimer.html";
    public static final String cwsecureresource_uris = "https://cubewerx.demo.fedroot.com/demo/cubeserv/cubeserv.cgi";
    // Metalogic jurisdiction
    public static final String mlbaseuri = "https://demo.fedroot.com/metalogic/dacs";
    public static final String mljurisdiction = "METALOGIC";
    public static final String testprenvuri = "https://demo.fedroot.com/test/dacs/prenv";
    // test user/password in METALOGIC jurisdiction
    public static final String mlusername_1 = "smith";
    public static final String mlpassword_1 = "foozle";
    public static final String mlusername_2 = "jones";

    
    // test bad user/password in ANY jurisdiction
    public static final String badusername_1 = "smythe";
    public static final String badpassword_1 = "boozle";    
    // DSS jurisdiction
    public static final String dssbaseuri = "https://dss.demo.fedroot.com/cgi-bin/dacs";
    public static final String dssjurisdiction = "DSS";
    // test user/password in DSS jurisdiction
    public static final String dssusername_1 = "jones";
    public static final String dsspassword_1 = "foozle";
    public static final String testbaseuri = "https://demo.fedroot.com/test/dacs";
    public static final String testaltbaseuri = "https://demo.fedroot.com/testalt/dacs";
    public static final String dacsnoticesuri = "https://demo.fedroot.com/fedadmin/dacs/dacs_notices";
    // the number of local users in TEST jurisdiction
    public static final int numTESTusers = 7;
    // the number of ACL file in TEST jurisdiction
    public static final int numTESTacls = 10;
    // the number of revoked users in TEST jurisdiction
    public static final int numTESTrevocations = 1;

    public static final String testjurisdiction = "TEST";
    // test user/password in TEST jurisdiction
    public static final String testusername_1 = "white"; 
    public static final String testusername_2 = "gray";
    public static final String testpassword_1 = "foozle";
    public static final String testpassword_2 = "boozle";
    // test user is also ADMIN_IDENTITY in TEST jurisdiction
    public static final String testadminusername = "black";
    public static final String testadminpassword = "foozle";
    // URI to generate an access denied 902 event (authentication required)
    public static final String event902uri = "https://demo.fedroot.com/test/dacs-wrapped/auth-required.html";
    // URI to generate an access denied 905 (insecure) event (notices to acknowledge)
    public static final String event905uri = "https://demo.fedroot.com/test/dacs-wrapped/notice-required.html";
    // URI to generate an access denied 905 (secure) event (notices to acknowledge)
    public static final String event905secureuri = "https://demo.fedroot.com/testalt/dacs-wrapped/secure-notice-required.html";
    // URI requiring both authentication and notice acknowledgement
    public static final String event902905uri = "https://demo.fedroot.com/test/dacs-wrapped/auth-and-notice-required.html";
    public static final String notice_uris = "http://demo.fedroot.com/test/notices/arjis-disclaimer.html http://demo.fedroot.com/test/notices/usgs-disclaimer.html";
    public static final String resource_uris = "https://demo.fedroot.com/test/dacs-wrapped/notice-required.html";
    public static final String secureresource_uris = "https://demo.fedroot.com/testalt/dacs-wrapped/secure-notice-required.html";
}

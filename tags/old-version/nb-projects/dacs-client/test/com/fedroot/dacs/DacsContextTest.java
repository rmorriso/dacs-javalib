/*
 * DacsContextTest.java
 * JUnit based test
 *
 * Created on August 23, 2005, 3:49 PM
 */

package com.fedroot.dacs;

import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.http.DacsGetMethod;
import java.util.ArrayList;
import java.util.List;
import junit.framework.*;
import com.fedroot.dacs.test.TestConstants;
import com.fedroot.dacs.xmlbeans.AccessDeniedDocument;
import com.fedroot.dacs.xmlbeans.DacsAcsDocument;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author rmorriso
 */
public class DacsContextTest extends TestCase {
    DacsContext dacscontext;
    Federation testfed;
    Jurisdiction testjur, dssjur;
    DacsUserAccount testuser;
    List<Jurisdiction> jurisdictions = new ArrayList<Jurisdiction>();
    
    public DacsContextTest(String testName) 
    throws Exception {
        super(testName);
        dacscontext = new DacsContext();
        testfed = Federation.getInstance(dacscontext, TestConstants.testbaseuri);
        testjur = testfed.getJurisdictionByName("TEST");
        dssjur = testfed.getJurisdictionByName("DSS");
        testuser = new DacsUserAccount(testfed, testjur, TestConstants.testusername_1, "yes");
    }
    
    protected void setUp() throws Exception {
        jurisdictions.clear();
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        System.out.println("DacsContextTest");
        TestSuite suite = new TestSuite(DacsContextTest.class);
        return suite;
    }
    
    public void testCheckOnly()
    throws Exception {
        System.out.println("testCheckOnly (event902)");
        try {
            // URI TestConstants.event902905uri requires authentication and notice acknowledgment
            // 1) check_only returns event902; then authenticate
            // 2) check_only returns event905
            DacsGetMethod dacsget = new DacsGetMethod(TestConstants.event902905uri);
//            int httpstatus = dacscontext.executeMethod(dacsget, DACS.AcsCheck.check_only, DACS.ReplyFormat.XMLSCHEMA);
            int dacsstatus = dacscontext.executeCheckOnlyMethod(dacsget);
//            if (httpstatus == HttpStatus.SC_OK) {
                try {
                    XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                    System.out.println(expectedXmlObject.getClass().getName());
                    
                    // Check that it is a valid instance of the DacsAcsDocument
                    
                    if(expectedXmlObject instanceof DacsAcsDocument) {
                        DacsAcsDocument doc =
                                (DacsAcsDocument)expectedXmlObject;
                        assertTrue("invalid DacsAcsDocument", doc.validate());
                        DacsAcsDocument.DacsAcs acs = doc.getDacsAcs();
                        if (acs.isSetAccessDenied()) {
                            AccessDeniedDocument.AccessDenied status = acs.getAccessDenied();
                            assertTrue("Event902 is not set", status.isSetEvent902());
                        } else {
                            fail("Acs status not access_denied");
                        }
                    } else {
                        fail("incorrect XML document type");                       
                    }
                }  catch (Exception e) {
                    System.out.println("XML exception: " + e.getMessage());
                    fail("Exception thrown during XML parsing");
                }
//            } else {
//                fail("httpstatus != 200");
//            }

        System.out.println("testCheckOnly (event905)");
        testuser.authenticate(dacscontext, TestConstants.testpassword_1);
        dacsstatus = dacscontext.executeMethod(dacsget);
//        if (httpstatus == HttpStatus.SC_OK) {
            try {
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                System.out.println(expectedXmlObject.getClass().getName());
                
                // Check that it is a valid instance of the DacsAcsDocument
                
                if(expectedXmlObject instanceof DacsAcsDocument) {
                    DacsAcsDocument doc =
                            (DacsAcsDocument)expectedXmlObject;
                    assertTrue("invalid DacsAcsDocument", doc.validate());
                    DacsAcsDocument.DacsAcs acs = doc.getDacsAcs();
                    if (acs.isSetAccessDenied()) {
                        AccessDeniedDocument.AccessDenied status = acs.getAccessDenied();
                        assertTrue("Event905 is not set", status.isSetEvent905());
                    } else {
                        fail("Acs status not access_denied");
                    }
                } else {
                    fail("incorrect XML document type");
                }
            } catch (Exception e) {
                System.out.println("XML exception: " + e.getMessage());
                fail("Exception thrown during XML parsing");
            }
//        } else {
//            fail("httpstatus != 200");
//        }
    } catch (DacsException ex) {
        fail("executeMethod threw exception in check mode");
    }
}
    /**
     * Test of getFederation method, of class com.fedroot.dacs.DacsContext.
     */
    public void testGetFederation() {
        System.out.println("testGetFederation");
        Federation fed;
        try {
            fed = Federation.getInstance(dacscontext, TestConstants.fedadminuri);
            assertEquals(TestConstants.fedname, fed.name);
            assertEquals(TestConstants.numDEMOjurisdictions, fed.getJurisdictions().size());
            assertEquals(TestConstants.numauthDEMOjurisdictions,fed.getAuthenticatingJurisdictions().size());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }
}

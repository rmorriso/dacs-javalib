/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fedroot.dacs.client;

import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.http.DacsClientContext;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author ricmorri
 */
public class DacsCurrentCredentialsRequestTest extends TestCase {
    
    FederationLoader federationLoader;
    Jurisdiction signon;
    
    public DacsCurrentCredentialsRequestTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DacsClientContext dacsClientContext = new DacsClientContext();
        federationLoader = new FederationLoader(dacsClientContext, "http://signon.cisco.com/dacs");
        federationLoader.load();
        Federation federation = federationLoader.getFederation();
        signon = federation.getJurisdictionByName("SIGNON");
        DacsAuthenticateRequest dacsAuthenticateRequest = new DacsAuthenticateRequest(signon, "jcarcill", "foozle");
        dacsClientContext.executePostRequest(dacsAuthenticateRequest);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDacsCurrentCredentialsRequest() {
        try {
            // get a dacs_current_credentials request for the SIGNON jurisdiction
            DacsCurrentCredentialsRequest dacsCurrentCredentialsRequest = new DacsCurrentCredentialsRequest(signon);
            URI signonCurrentCredentialsURI = dacsCurrentCredentialsRequest.getServiceURI();
            assertEquals(0, signonCurrentCredentialsURI.compareTo(new URI("http://signon.cisco.com/dacs/dacs_current_credentials")));
//            assertEquals("FORMAT=XMLSCHEMA", dacsCurrentCredentialsURI.getQuery());
        } catch (Exception ex) {
            Logger.getLogger(DacsCurrentCredentialsRequestTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fedroot.dacs;

import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.http.DacsClientContext;
import junit.framework.TestCase;

/**
 *
 * @author ricmorri
 */
public class DacsTest extends TestCase {
    
    private FederationLoader federationLoader;
    private Federation federation;
    private Jurisdiction jurisdiction;

    public DacsTest(String testName) throws Exception {
        super(testName);
        federationLoader = new FederationLoader(new DacsClientContext(), "http://signon.cisco.com/dacs");
        federationLoader.load();
        federation = federationLoader.getFederation();
        jurisdiction = federation.getJurisdictionByName("SIGNON");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public Jurisdiction getJurisdiction() {
        return this.jurisdiction;
    }
}

/*
 * DacsCookieNameTest.java
 * Created on Mar 6, 2010 8:11:06 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.http;

import junit.framework.TestCase;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
public class DacsCookieNameTest extends TestCase {

    private String credentialsName = "DACS:FEDROOT::METALOGIC:ricky";
    private String selectName = "DACS:FEDROOT:::SELECTED";
    private String badName1 = "DACS:FEDROOT:ricky";
    private String badName2 = "DACS::FEDROOT::tricky:ricky";
    private String badName3 = "::FEDROOT:ricky";
    private String badName4 = "DACS:FED-ROOT::METALOGIC:ricky";


    public DacsCookieNameTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testValueOf() {
        assertNull(DacsCookieName.valueOf(badName1));
        assertNull(DacsCookieName.valueOf(badName2));
        assertNull(DacsCookieName.valueOf(badName3));
        assertNull(DacsCookieName.valueOf(badName4));
    }

    public void testGetFederationPart() {
        DacsCookieName dacsCookieName = DacsCookieName.valueOf(credentialsName);
        assertEquals("FEDROOT", dacsCookieName.getFederationPart());
        assertFalse(dacsCookieName.isSelectCookieName());
        DacsCookieName selectCookieName = DacsCookieName.valueOf(selectName);
        assertEquals("FEDROOT", selectCookieName.getFederationPart());
        assertNull(selectCookieName.getJurisdictionPart());
        assertNull(selectCookieName.getUsernamePart());
        assertTrue(selectCookieName.isSelectCookieName());
    }

    public void testGetJurisdictionPart() {
        DacsCookieName dacsCookieName = DacsCookieName.valueOf(credentialsName);
        assertEquals("METALOGIC", dacsCookieName.getJurisdictionPart());

    }

    public void testGetUsernamePart() {
        DacsCookieName dacsCookieName = DacsCookieName.valueOf(credentialsName);
        assertEquals("ricky", dacsCookieName.getUsernamePart());
    }

}

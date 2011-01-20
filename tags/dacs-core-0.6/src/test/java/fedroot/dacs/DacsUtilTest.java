/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fedroot.dacs;

import fedroot.dacs.http.DacsCookie;
import java.util.List;
import junit.framework.TestCase;
import org.apache.http.cookie.Cookie;

/**
 *
 * @author rmorriso
 */
public class DacsUtilTest extends TestCase {

    private String header1 = "JSESSIONID=fec51a10f955a26756e4d15d6eb2; DACS:FEDROOT::METALOGIC:rmorriso=mIIKA5vrtLJgMxTG3UsrS4FBbQopZk1gxG4lDmuOUCL2w53n3wYoX2vdPBiF1K1xKOaEsyw3arq0PBnrxWNDLW0IP_O1jVJGAO14gWUNgkIFaEpdGLY3vOnXdZmCjYDTKLqAdzTJDm8GSHBjzr-XZvfokf_yrOnkYpFrxpZQgACEgMnPamqO4BUMeZcbWqo1_4TjxmzM5gWLKu1y0KwltG8QVLqfc4cCWfnakQuIT9VNDRnoyi79lh-RhIMugJGRJcICwlTEi5nlusrucooAk7_PP0kCEh2FMGJb03GR3Cj-yf6Ayh87KZpOuSNYPCyrmxW030bVLbsVHIlBdeMyvpmz5xJkqu-jfAuINlgqmKdY1p6jYkxsijI2s2lTJetIpkZnocnbSvU_RQSMezhLQWeVQu02clhDusMvjv3bMWfwNU7CiDhowXq8cSly5mLH6GhKH7iyP0";
    private String header2 = "DACS:FEDROOT::METALOGIC:rmorriso=mIIKA5vrtLJgMxTG3UsrS4FBbQopZk1gxG4lDmuOUCL2w53n3wYoX2vdPBiF1K1xKOaEsyw3arq0PBnrxWNDLW0IP_O1jVJGAO14gWUNgkIFaEpdGLY3vOnXdZmCjYDTKLqAdzTJDm8GSHBjzr-XZvfokf_yrOnkYpFrxpZQgACEgMnPamqO4BUMeZcbWqo1_4TjxmzM5gWLKu1y0KwltG8QVLqfc4cCWfnakQuIT9VNDRnoyi79lh-RhIMugJGRJcICwlTEi5nlusrucooAk7_PP0kCEh2FMGJb03GR3Cj-yf6Ayh87KZpOuSNYPCyrmxW030bVLbsVHIlBdeMyvpmz5xJkqu-jfAuINlgqmKdY1p6jYkxsijI2s2lTJetIpkZnocnbSvU_RQSMezhLQWeVQu02clhDusMvjv3bMWfwNU7CiDhowXq8cSly5mLH6GhKH7iyP0; JSESSIONID=fec51a10f955a26756e4d15d6eb2";
    private String header3 = "JSESSIONID=fec51a10f955a26756e4d15d6eb2; DACS:FEDROOT::Public:roderick.morrison@shaw.ca=mIIKA5vrtLJgMxTG3UsrS4FBbQopZk1gxG4lDmuOUCL2w53n3wYoX2vdPBiF1K1xKOaEsyw3arq0PBnrxWNDLW0IP_O1jVJGAO14gWUNgkIFaEpdGLY3vOnXdZmCjYDTKLqAdzTJDm8GSHBjzr-XZvfokf_yrOnkYpFrxpZQgACEgMnPamqO4BUMeZcbWqo1_4TjxmzM5gWLKu1y0KwltG8QVLqfc4cCWfnakQuIT9VNDRnoyi79lh-RhIMugJGRJcICwlTEi5nlusrucooAk7_PP0kCEh2FMGJb03GR3Cj-yf6Ayh87KZpOuSNYPCyrmxW030bVLbsVHIlBdeMyvpmz5xJkqu-jfAuINlgqmKdY1p6jYkxsijI2s2lTJetIpkZnocnbSvU_RQSMezhLQWeVQu02clhDusMvjv3bMWfwNU7CiDhowXq8cSly5mLH6GhKH7iyP0; DACS:FEDROOT::DSS:brachman=mIIKA5vrtLJgMxTG3UsrS4FBbQopZk1gxG4lDmuOUCL2w53n3wYoX2vdPBiF1K1xKOaEsyw3arq0PBnrxWNDLW0IP_O1jVJGAO14gWUNgkIFaEpdGLY3vOnXdZmCjYDTKLqAdzTJDm8GSHBjzr-XZvfokf_yrOnkYpFrxpZQgACEgMnPamqO4BUMeZcbWqo1_4TjxmzM5gWLKu1y0KwltG8QVLqfc4cCWfnakQuIT9VNDRnoyi79lh-RhIMugJGRJcICwlTEi5nlusrucooAk7_PP0kCEh2FMGJb03GR3Cj-yf6Ayh87KZpOuSNYPCyrmxW030bVLbsVHIlBdeMyvpmz5xJkqu-jfAuINlgqmKdY1p6jYkxsijI2s2lTJetIpkZnocnbSvU_RQSMezhLQWeVQu02clhDusMvjv3bMWfwNU7CiDhowXq8cSly5mLH6GhKH7iyP0; JSESSIONID=fec51a10f955a26756e4d15d6eb2";

    public DacsUtilTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of resolveUser method, of class DacsUtil.
     */
    public void testGetCookies1() throws Exception {
        List<Cookie> cookies = DacsUtil.getCookies(header1);
        assertEquals(1, cookies.size());
        for (Cookie cookie : cookies) {
            assertTrue(DacsCookie.isDacsCookie(cookie));
        }
    }

    public void testGetCookies2() throws Exception {
        List<Cookie> cookies = DacsUtil.getCookies(header2);
        assertEquals(1, cookies.size());
        for (Cookie cookie : cookies) {
            assertTrue(DacsCookie.isDacsCookie(cookie));
        }
    }

    public void testGetCookies3() throws Exception {
        List<Cookie> cookies = DacsUtil.getCookies(header3);
        assertEquals(2, cookies.size());
        for (Cookie cookie : cookies) {
            assertTrue(DacsCookie.isDacsCookie(cookie));
        }
    }
}

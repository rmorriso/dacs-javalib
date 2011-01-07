/*
 * ExampleRunner.java
 *
 * Created on February 25, 2010, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.examples;

import fedroot.dacs.DacsUtil;
import fedroot.dacs.client.DacsAuthenticateRequest;
import fedroot.dacs.entities.Credential;
import fedroot.dacs.entities.Credentials;
import fedroot.dacs.entities.CredentialsLoader;
import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsCookieName;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;

/**
 *
 * @author ricmorri
 */
public class ExampleRunner {

        private final static Logger logger = Logger.getLogger(ExampleRunner.class.getName());
        private static FederationLoader federationLoader;
        private static Federation federation;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DacsClientContext dacsClientContext = new DacsClientContext();
            federationLoader = new FederationLoader("https://fedroot.com/dacs", dacsClientContext);
            federation = federationLoader.getFederation();

//            failedAuthenticationExample(dacsClientContext);
            authenticationExample(dacsClientContext);
            credentialsExample(dacsClientContext);
//            federationExample();
//            getCookiesWithEmail();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private static void federationExample(DacsClientContext dacsClientContext) throws Exception {
        Jurisdiction jurisdiction = federation.getJurisdictionByName("TEST");
    }

    private static void authenticationExample(DacsClientContext dacsClientContext) {
        try {
            Jurisdiction test = federation.getJurisdictionByName("TEST");
            // authenticate as test user "black"
            String username = "black";
            String password = "foozle";
            DacsAuthenticateRequest dacsAuthenticateRequest = new DacsAuthenticateRequest(test, username, password);
            dacsClientContext.executePostRequest(dacsAuthenticateRequest);
            //        System.out.println("HTTP response: " + response.getStatusLine());
            System.out.println("Cookies present in dacsClientContext after authentication:");
            for (Cookie cookie : dacsClientContext.getDacsCookies(".fedroot.com")) {
                //        for (Cookie cookie : dacsClientContext.getAllCookies()) {
                System.out.println(cookie.getName());
            }
            // now remove a cookie by name
            dacsClientContext.removeCookieByName(DacsCookieName.getName(federation.getFederationName(), test.getJName(), username));
            System.out.println("Cookies present in dacsClientContext after removing DACS cookie:");
            for (Cookie cookie : dacsClientContext.getDacsCookies("fedroot.com")) {
                System.out.println(cookie.getName());
            }
        } catch (DacsException ex) {
            logger.log(Level.SEVERE, "Authentication example failed: {0}", ex.getLocalizedMessage());
        }
    }

    private static void failedAuthenticationExample(DacsClientContext dacsClientContext) {
        try {
            Jurisdiction test = federation.getJurisdictionByName("TEST");
            // authenticate as test user "black"
            String username = "black";
            String badPassword = "foozl";
            String goodPassword = "foozle";
            DacsAuthenticateRequest dacsAuthenticateRequest = new DacsAuthenticateRequest(test, username, badPassword);
            dacsClientContext.executePostRequest(dacsAuthenticateRequest);
            System.out.println("Cookies present in dacsClientContext after failed authentication:");
            for (Cookie cookie : dacsClientContext.getDacsCookies(".fedroot.com")) {
                System.out.println(cookie.getName());
            }
            dacsAuthenticateRequest = new DacsAuthenticateRequest(test, username, goodPassword);
            dacsClientContext.executePostRequest(dacsAuthenticateRequest);
            System.out.println("Cookies present in dacsClientContext after successful authentication:");
            for (Cookie cookie : dacsClientContext.getDacsCookies(".fedroot.com")) {
                System.out.println(cookie.getName());
            }
        } catch (DacsException ex) {
            logger.log(Level.SEVERE, "Failed Authentication example failed: {0}", ex.getLocalizedMessage());
        }
    }

    private static void credentialsExample(DacsClientContext dacsClientContext) throws Exception {
        Jurisdiction test = federation.getJurisdictionByName("TEST");
        CredentialsLoader credentialsLoader = new CredentialsLoader(test, dacsClientContext);
        Credentials credentials = credentialsLoader.getCredentials();
        for (Credential credential : credentials.getCredentials()) {
            System.out.println(credential.getName() + credential.getRoles());
        }
    }

    private static void getCookiesExample() throws Exception {
        String header1 = "JSESSIONID=fec51a10f955a26756e4d15d6eb2; DACS:FEDROOT::METALOGIC:rmorriso=mIIKA5vrtLJgMxTG3UsrS4FBbQopZk1gxG4lDmuOUCL2w53n3wYoX2vdPBiF1K1xKOaEsyw3arq0PBnrxWNDLW0IP_O1jVJGAO14gWUNgkIFaEpdGLY3vOnXdZmCjYDTKLqAdzTJDm8GSHBjzr-XZvfokf_yrOnkYpFrxpZQgACEgMnPamqO4BUMeZcbWqo1_4TjxmzM5gWLKu1y0KwltG8QVLqfc4cCWfnakQuIT9VNDRnoyi79lh-RhIMugJGRJcICwlTEi5nlusrucooAk7_PP0kCEh2FMGJb03GR3Cj-yf6Ayh87KZpOuSNYPCyrmxW030bVLbsVHIlBdeMyvpmz5xJkqu-jfAuINlgqmKdY1p6jYkxsijI2s2lTJetIpkZnocnbSvU_RQSMezhLQWeVQu02clhDusMvjv3bMWfwNU7CiDhowXq8cSly5mLH6GhKH7iyP0";
        String header2 = "DACS:FEDROOT::METALOGIC:rmorriso=mIIKA5vrtLJgMxTG3UsrS4FBbQopZk1gxG4lDmuOUCL2w53n3wYoX2vdPBiF1K1xKOaEsyw3arq0PBnrxWNDLW0IP_O1jVJGAO14gWUNgkIFaEpdGLY3vOnXdZmCjYDTKLqAdzTJDm8GSHBjzr-XZvfokf_yrOnkYpFrxpZQgACEgMnPamqO4BUMeZcbWqo1_4TjxmzM5gWLKu1y0KwltG8QVLqfc4cCWfnakQuIT9VNDRnoyi79lh-RhIMugJGRJcICwlTEi5nlusrucooAk7_PP0kCEh2FMGJb03GR3Cj-yf6Ayh87KZpOuSNYPCyrmxW030bVLbsVHIlBdeMyvpmz5xJkqu-jfAuINlgqmKdY1p6jYkxsijI2s2lTJetIpkZnocnbSvU_RQSMezhLQWeVQu02clhDusMvjv3bMWfwNU7CiDhowXq8cSly5mLH6GhKH7iyP0; JSESSIONID=fec51a10f955a26756e4d15d6eb2";
        String header3 = "JSESSIONID=fec51a10f955a26756e4d15d6eb2; DACS:FEDROOT::METALOGIC:rmorriso@fedroot.com=mIIKA5vrtLJgMxTG3UsrS4FBbQopZk1gxG4lDmuOUCL2w53n3wYoX2vdPBiF1K1xKOaEsyw3arq0PBnrxWNDLW0IP_O1jVJGAO14gWUNgkIFaEpdGLY3vOnXdZmCjYDTKLqAdzTJDm8GSHBjzr-XZvfokf_yrOnkYpFrxpZQgACEgMnPamqO4BUMeZcbWqo1_4TjxmzM5gWLKu1y0KwltG8QVLqfc4cCWfnakQuIT9VNDRnoyi79lh-RhIMugJGRJcICwlTEi5nlusrucooAk7_PP0kCEh2FMGJb03GR3Cj-yf6Ayh87KZpOuSNYPCyrmxW030bVLbsVHIlBdeMyvpmz5xJkqu-jfAuINlgqmKdY1p6jYkxsijI2s2lTJetIpkZnocnbSvU_RQSMezhLQWeVQu02clhDusMvjv3bMWfwNU7CiDhowXq8cSly5mLH6GhKH7iyP0; DACS:FEDROOT::DSS:brachman=mIIKA5vrtLJgMxTG3UsrS4FBbQopZk1gxG4lDmuOUCL2w53n3wYoX2vdPBiF1K1xKOaEsyw3arq0PBnrxWNDLW0IP_O1jVJGAO14gWUNgkIFaEpdGLY3vOnXdZmCjYDTKLqAdzTJDm8GSHBjzr-XZvfokf_yrOnkYpFrxpZQgACEgMnPamqO4BUMeZcbWqo1_4TjxmzM5gWLKu1y0KwltG8QVLqfc4cCWfnakQuIT9VNDRnoyi79lh-RhIMugJGRJcICwlTEi5nlusrucooAk7_PP0kCEh2FMGJb03GR3Cj-yf6Ayh87KZpOuSNYPCyrmxW030bVLbsVHIlBdeMyvpmz5xJkqu-jfAuINlgqmKdY1p6jYkxsijI2s2lTJetIpkZnocnbSvU_RQSMezhLQWeVQu02clhDusMvjv3bMWfwNU7CiDhowXq8cSly5mLH6GhKH7iyP0; JSESSIONID=fec51a10f955a26756e4d15d6eb2";


        List<Cookie> cookies = DacsUtil.getCookies(header3);
        Cookie cookie2 = cookies.get(1);
        for (Cookie cookie : cookies) {
            System.out.println(cookie);
        }
    }

    private static void getCookiesWithEmail() {
//        Pattern name = Pattern.compile("(DACS:[:\\w]+)=([-\\w]+)");
//        Matcher m = name.matcher(cookieHeader);
//        while (m.find()) {
//            String cookieName = m.group(1);
//            String cookieValue = m.group(2);
//        }

//        String test = "DACS:NFIS::CA:roderick.oneil-morrison@fedroot.com";
        String test = "JSESSIONID=fec51a10f955a26756e4d15d6eb2; DACS:FEDROOT::METALOGIC:rmorriso@fedroot.com=mIIKA5vrtLJgMxTG3UsrS4FBbQopZk1gxG4lDmuOUCL2w53n3wYoX2vdPBiF1K1xKOaEsyw3arq0PBnrxWNDLW0IP_O1jVJGAO14gWUNgkIFaEpdGLY3vOnXdZmCjYDTKLqAdzTJDm8GSHBjzr-XZvfokf_yrOnkYpFrxpZQgACEgMnPamqO4BUMeZcbWqo1_4TjxmzM5gWLKu1y0KwltG8QVLqfc4cCWfnakQuIT9VNDRnoyi79lh-RhIMugJGRJcICwlTEi5nlusrucooAk7_PP0kCEh2FMGJb03GR3Cj-yf6Ayh87KZpOuSNYPCyrmxW030bVLbsVHIlBdeMyvpmz5xJkqu-jfAuINlgqmKdY1p6jYkxsijI2s2lTJetIpkZnocnbSvU_RQSMezhLQWeVQu02clhDusMvjv3bMWfwNU7CiDhowXq8cSly5mLH6GhKH7iyP0; DACS:FEDROOT::DSS:brachman=mIIKA5vrtLJgMxTG3UsrS4FBbQopZk1gxG4lDmuOUCL2w53n3wYoX2vdPBiF1K1xKOaEsyw3arq0PBnrxWNDLW0IP_O1jVJGAO14gWUNgkIFaEpdGLY3vOnXdZmCjYDTKLqAdzTJDm8GSHBjzr-XZvfokf_yrOnkYpFrxpZQgACEgMnPamqO4BUMeZcbWqo1_4TjxmzM5gWLKu1y0KwltG8QVLqfc4cCWfnakQuIT9VNDRnoyi79lh-RhIMugJGRJcICwlTEi5nlusrucooAk7_PP0kCEh2FMGJb03GR3Cj-yf6Ayh87KZpOuSNYPCyrmxW030bVLbsVHIlBdeMyvpmz5xJkqu-jfAuINlgqmKdY1p6jYkxsijI2s2lTJetIpkZnocnbSvU_RQSMezhLQWeVQu02clhDusMvjv3bMWfwNU7CiDhowXq8cSly5mLH6GhKH7iyP0; JSESSIONID=fec51a10f955a26756e4d15d6eb2";
        Pattern email = Pattern.compile("(DACS:[:\\w]+[\\w][\\w\\-]*[\\.]{0,1}[\\w\\-]*[@[A-Za-z0-9-]+\\.+[A-Za-z]{2,4}]*)=([-\\w]+)", Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = email.matcher(test);
        while (emailMatcher.find()) {
            String group1 = emailMatcher.group(1);
            String group2 = emailMatcher.group(2);
        }
    }
}

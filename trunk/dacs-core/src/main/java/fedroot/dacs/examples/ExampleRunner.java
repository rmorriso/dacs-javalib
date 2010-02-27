/*
 * ExampleRunner.java
 *
 * Created on February 25, 2010, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.examples;

import fedroot.dacs.client.DacsAuthenticateRequest;
import fedroot.dacs.entities.Credential;
import fedroot.dacs.entities.Credentials;
import fedroot.dacs.entities.CredentialsLoader;
import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.http.DacsClientContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

/**
 *
 * @author ricmorri
 */
public class ExampleRunner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            credentialsExample();
//            federationExample();
        } catch (Exception ex) {
            Logger.getLogger(ExampleRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void federationExample() throws Exception {
        FederationLoader federationLoader = new FederationLoader(new DacsClientContext(), "https://fedroot.com/dacs");
        federationLoader.load();
        Federation federation = federationLoader.getFederation();
        Jurisdiction jurisdiction = federation.getJurisdictionByName("TEST");
    }

    private static void credentialsExample() throws Exception {
        DacsClientContext dacsClientContext = new DacsClientContext();
        FederationLoader federationLoader = new FederationLoader(new DacsClientContext(), "https://fedroot.com/dacs");
        federationLoader.load();
        Federation federation = federationLoader.getFederation();
        Jurisdiction test = federation.getJurisdictionByName("TEST");
        CredentialsLoader credentialsLoader = new CredentialsLoader(dacsClientContext, test);
        // authenticate as test user
        DacsAuthenticateRequest dacsAuthenticateRequest = new DacsAuthenticateRequest(test, "black", "foozle");
        HttpResponse response = dacsClientContext.executePostRequest(dacsAuthenticateRequest);
        HttpEntity entity = response.getEntity();
        entity.getContent();
        entity.consumeContent();
        credentialsLoader.load();
        Credentials credentials = credentialsLoader.getCredentials();
        for (Credential credential : credentials.getCredentials()) {
            System.out.println(credential.getName() + credential.getRoles());
        }
    }
}

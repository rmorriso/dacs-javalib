/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fedroot.dacs.swing;

import fedroot.dacs.client.DacsAuthenticateRequest;
import fedroot.dacs.client.DacsCheckRequest;
import fedroot.dacs.entities.Credential;
import fedroot.dacs.entities.Credentials;
import fedroot.dacs.entities.CredentialsLoader;
import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.entities.DacsCheckLoader;
import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.entities.Role;
import fedroot.dacs.events.DacsEventNotifier;
import fedroot.dacs.events.DacsEventNotifier.Status;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmorriso
 */
public class SessionManager {

    private static final Logger logger = Logger.getLogger(SessionManager.class.getName());
    private DacsEventNotifier dacsEventNotifier;
    private DacsClientContext dacsClientContext;
    private Federation federation;
    private Jurisdiction jurisdiction;
    private String username;
    private List<Role> roles;
    private String cookieName;

    public SessionManager(String dacsBaseUri) {
        dacsEventNotifier = new DacsEventNotifier();

        try {
            dacsClientContext = new DacsClientContext();
            FederationLoader federationLoader = new FederationLoader(dacsBaseUri, dacsClientContext);
            logger.log(Level.INFO, "loading federation from {0}", dacsBaseUri);
            federation = federationLoader.getFederation();
            logger.log(Level.INFO, "loaded federation {0}", federation.getFederationName());

            // TODO derive log level from Application runtime configuration
            logger.setLevel(Level.INFO);
        } catch (DacsException ex) {
            Logger.getLogger(SessionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return add object
     */
    public void addDacsEventListener(Object o) {
        dacsEventNotifier.addEventListener((DacsEventNotifier.Listener) o);

    }

    public List<Jurisdiction> getAuthenticatingJurisdictions() {
        return federation.getAuthenticatingJurisdictions();
    }

    public void signon(Jurisdiction jurisdiction, String username, String password) {
        try {
            if (this.jurisdiction != null) {
                logger.log(Level.INFO,"Multiple conccurent signon is not supported. Forcing signout.");
                signout();
            }
            // authenticate in given jurisdiction as username
            DacsAuthenticateRequest dacsAuthenticateRequest = new DacsAuthenticateRequest(jurisdiction, username, password);
            dacsClientContext.executePostRequest(dacsAuthenticateRequest);
            CredentialsLoader credentialsLoader = new CredentialsLoader(jurisdiction, dacsClientContext);
            Credentials credentials = credentialsLoader.getCredentials();
            this.jurisdiction = jurisdiction;
            for (Credential credential : credentials.getCredentials()) {
                this.setUsername(credential.getName());
                this.setRoles(credential.getRoles());
                this.cookieName = credential.getCookieName();
            }
            dacsEventNotifier.status(Status.signon, "user signin: " + username + (getRoles() == null ? "" : " with roles " + getRoles()));
        } catch (DacsException ex) {
//            dacsEventNotifier.notify(ex);
            logger.log(Level.INFO, ex.getMessage());
        }
    }

    public void signout() {
        dacsClientContext.removeCookieByName(cookieName);
        dacsEventNotifier.status(Status.signout, "signout: " + getUsername());
        reset(); // wipe current values
        logger.log(Level.FINE, "user signout: {0}", getUsername());
    }

    public synchronized InputStream getInputStream(DacsCheckRequest dacsCheckRequest) throws IOException, DacsException {
        try {
            DacsCheckLoader htmlLoader = new DacsCheckLoader(dacsCheckRequest);
            return htmlLoader.getInputStream(dacsClientContext);
        } catch (DacsException ex) {
            dacsEventNotifier.notify(ex, dacsCheckRequest);
            throw ex;
        }
    }

    /**
     * @return the jurisdiction
     */
    public Jurisdiction getJurisdiction() {
        return jurisdiction;
    }

    /**
     * @param jurisdiction the jurisdiction to set
     */
    public void setJurisdiction(Jurisdiction jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    private void reset() {
        this.jurisdiction = null;
        this.username = null;
        this.roles = null;
        this.cookieName = null;
    }

}

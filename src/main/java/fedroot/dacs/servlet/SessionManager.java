/*
 * SessionManager.java
 *
 * Created on December 17, 2010, 8:43 AM
 *
 * Copyright (c) 2010-2011 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.servlet;

import fedroot.dacs.DacsUtil;
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
import fedroot.dacs.http.DacsCookie;
import fedroot.dacs.http.DacsResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.params.HttpParams;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    private Credentials credentials;
    private Credential selectedCredential;
    private boolean allowMultipleCredentials = false;

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

    public SessionManager(String dacsBaseUri, HttpParams httpParams) {
        dacsEventNotifier = new DacsEventNotifier();

        try {
            dacsClientContext = new DacsClientContext(httpParams);
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

    public SessionManager(Federation federation) {
        dacsEventNotifier = new DacsEventNotifier();
        dacsClientContext = new DacsClientContext();
        this.federation = federation;
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
            if (credentials != null && credentials.hasCredentials() && !allowMultipleCredentials) {
                logger.log(Level.WARNING, "Multiple concurrent signon is not supported. Forcing signout.");
                signout();
            }
            this.jurisdiction = jurisdiction;

            // authenticate in given jurisdiction as username
            DacsAuthenticateRequest dacsAuthenticateRequest = new DacsAuthenticateRequest(jurisdiction, username, password);
            dacsClientContext.executePostRequest(dacsAuthenticateRequest);
            CredentialsLoader credentialsLoader = new CredentialsLoader(jurisdiction, dacsClientContext);
            credentials = credentialsLoader.getCredentials();

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

    /**
     * sets the <code>effective credential</code> from the DACS cookie(s) found
     * in an HTTP servlet request;
     * looks for cookies with a valid DACS cookie name in @param request, then
     * creates and attaches a corresponding HttpClient cookie in dacsClientContext.
     * The resulting DacsClientContext is used to execute a DacsCurrentCredentials
     * web service request.
     *
     * In some DACS deployments it is permissible for a user
     * session to carry multiple DACS credentials, but in those cases one such
     * credential must be the <i>selected</i> credential. A DacsException is thrown
     * if multiple DACS cookies are present without an an accompanying selection.
     * @return the credential carried the selected DACS cookie found in
     * @param request or null if none is found
     */
    public Credential resolveCredentials(HttpServletRequest request) throws DacsException {
        if (allowMultipleCredentials) {
            throw new NotImplementedException();
        } else {
            List<DacsCookie> dacsCookies = DacsUtil.getDacsCookies(federation, DacsUtil.getCookieHeaders(request));
            if (dacsCookies != null) {
                dacsClientContext.addDacsCookies(dacsCookies);
                // use the first authenticating jurisdiction to load credentials
                // bound in the DACS cookies
                for (Jurisdiction authenticatingJurisdiction : federation.getAuthenticatingJurisdictions()) {
                    CredentialsLoader credentialsLoader = new CredentialsLoader(authenticatingJurisdiction, dacsClientContext);
                    Credentials credentials = credentialsLoader.getCredentials();
                    setSelectedCredential(credentials != null ? credentials.getEffectiveCredentials() : null);
                    break;
                }
            } else {
                setSelectedCredential(null);
            }
            return getSelectedCredential();
        }
    }

    public synchronized DacsResponse getDacsResponse(DacsCheckRequest dacsCheckRequest) throws IOException, DacsException {
        try {
            DacsCheckLoader dacsCheckLoader = new DacsCheckLoader(dacsCheckRequest);
            return dacsCheckLoader.getDacsResponse(dacsClientContext);
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

    /**
     * @return the selectedCredential
     */
    public Credential getSelectedCredential() {
        return selectedCredential;
    }

    /**
     * @param selectedCredential the selectedCredential to set
     */
    public void setSelectedCredential(Credential selectedCredential) {
        this.selectedCredential = selectedCredential;
    }


}

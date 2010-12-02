/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fedroot.dacs.swingdemo;

import fedroot.dacs.client.DacsAuthenticateRequest;
import fedroot.dacs.entities.Credential;
import fedroot.dacs.entities.Credentials;
import fedroot.dacs.entities.CredentialsLoader;
import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.entities.Role;
import fedroot.dacs.events.DacsEventNotifier;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author rmorriso
 */

public class SessionController {

    private static final Logger logger = Logger.getLogger(SessionController.class.getName());
    private DacsEventNotifier dacsEventNotifier;
    private DacsClientContext dacsClientContext;
    private FederationLoader federationLoader;
    private Federation federation;
    private String name;
    private List<Role> roles;

    public SessionController(String dacsBaseUri) {
        dacsEventNotifier = new DacsEventNotifier();

        try {
            dacsClientContext = new DacsClientContext();
            federationLoader = new FederationLoader(dacsBaseUri, dacsClientContext);
            logger.log(Level.INFO, "loading federation from {0}", dacsBaseUri);
            federation = federationLoader.getFederation();
            logger.log(Level.INFO, "loaded federation {0}", federation.getFederationName());
            
            // TODO derive log level from Application runtime configuration
            logger.setLevel(Level.INFO);
        } catch (DacsException ex) {
            Logger.getLogger(SessionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return add object
     */
    public void addDacsEventListener(Object o) {
        dacsEventNotifier.addEventListener((DacsEventNotifier.Listener) o);

    }

    public void signon(String jname, String username, String password) {
        try {
            // authenticate as username
            Jurisdiction jurisdiction = federation.getJurisdictionByName(jname);
            DacsAuthenticateRequest dacsAuthenticateRequest = new DacsAuthenticateRequest(jurisdiction, username, password);
            dacsClientContext.executePostRequest(dacsAuthenticateRequest);
            name = username;
            CredentialsLoader credentialsLoader = new CredentialsLoader(jurisdiction, dacsClientContext);
            Credentials credentials = credentialsLoader.getCredentials();
            for (Credential credential : credentials.getCredentials()) {
                name = credential.getName();
                roles = credential.getRoles();
            }
        } catch (DacsException ex) {
            dacsEventNotifier.notify(ex);
            Logger.getLogger(SessionController.class.getName()).log(Level.INFO, ex.getMessage());
        }
    }

    public void signout() {
        name = null;
        logger.log(Level.INFO, "user signout: {0}", name);
    }

    private String shortName(String username) {
        return name.substring(name.indexOf(':'));
    }
}

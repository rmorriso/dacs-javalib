/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2;

import com.fedroot.dacs.Credentials;
import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.DacsUserAccount;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import com.fedroot.dacs.UserContext;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.app.Window;
import nextapp.echo2.webcontainer.ContainerContext;
import org.apache.log4j.Logger;

/**
 * DACS Ajax Demonstration Application
 */
public class DacsAjaxApp extends ApplicationInstance {

    public static final String DACS_BASEURI, FEDERATION_DOMAIN;
    public static final DacsContext APPCONTEXT = new DacsContext();
    public static final Federation FEDERATION; 
    public static final Jurisdiction JURISDICTION;
    static {
        // Static initializer to retrieve information from configuration properties file.
        ResourceBundle config = ResourceBundle.getBundle("/fedroot/echo2/demo/Configuration");
        FEDERATION_DOMAIN = config.getString("federation_domain");
        DACS_BASEURI = config.getString("dacs_baseuri");
        Federation federation;
        try {
            federation = Federation.getInstance(APPCONTEXT, DACS_BASEURI);
        } catch (Exception ex) {
            federation = null;
        }
        FEDERATION = federation;
        JURISDICTION = new Jurisdiction(DACS_BASEURI);
    }

    private Window mainWindow;
    private ConsoleWindowPane console;
    private UserContext userContext;
    private Logger logger;
    
    public DacsAjaxApp() {
        super();
        String username = getUsername();
        userContext = UserContext.getInstance(username);
        logger = Logger.getLogger("fedroot.echo2");
    }
    
    public ConsoleWindowPane getConsole() {
        return console;
    }
   
    /**
     * Convenience method to return the <code>ThreadLocal</code> instance 
     * precast to the appropriate type.
     * 
     * @return the active <code>DacsAjaxApp</code>
     * @see #getActive()
     */
    public static DacsAjaxApp getApp() {
        return (DacsAjaxApp) ApplicationInstance.getActive();
    }

    
    /**
     * Writes a message to a pop-up debugging console.
     * The console is used for displaying information such as
     * fired events.
     * 
     * @param message the message to write to the console
     *        (if null, console will be raised but not content
     *        will be written to it)
     */
    public void consoleWrite(String message) {
        if (console == null) {
            console = new ConsoleWindowPane();
            getDefaultWindow().getContent().add(console);
        } else if (console.getParent() == null) {
            getDefaultWindow().getContent().add(console);
        }
        if (message != null) {
            console.writeMessage(message);
        }
    }
    
    /**
     * Displays a <code>TestPane</code> from which the user may select an
     * interactive test to run.
     */
    public void displayTestPane() {
        mainWindow.setContent(new TestPane());
        console = null;
    }
    
    /**
     * Displays the <code>WelcomePane</code> which greets new users visiting
     * the application.
     */
    public void displayWelcomePane() {
        mainWindow.setContent(new WelcomePane());
        console = null;
    }

    /**
     * @see nextapp.echo2.app.ApplicationInstance#init()
     */
    public Window init() {
        setStyleSheet(Styles.FEDROOT_STYLE_SHEET);
        mainWindow = new Window();
        mainWindow.setTitle("DACS Ajax Demonstration Application");
        mainWindow.setContent(new WelcomePane());
        
        return mainWindow;
    }
    
    public boolean login(String jurisdiction, String username, String password) 
    throws Exception {
        Jurisdiction jur = FEDERATION.getJurisdictionByName(jurisdiction);
        DacsUserAccount acc = new DacsUserAccount(FEDERATION,jur,username);
        userContext = UserContext.getInstance(username);
        return userContext.authenticate(acc,password);
    }
    
    public String[] jurisdictionList() {
        String [] jurisdictions = null;
        if (FEDERATION != null) {
          jurisdictions = FEDERATION.getAuthenticatingJurisdictionNames().toArray(new String[0]);
        }
        return jurisdictions;
    }
    
    /**
     * extracts <code>username</code> from HTTP Basic Auth authorization header
     */
    protected String getUsername() {
        String username = null;
        ContainerContext cc = (ContainerContext) getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
        if (cc != null) {
            javax.servlet.http.Cookie[] jcookies = cc.getCookies();
            if (jcookies != null) {
                for (javax.servlet.http.Cookie jcookie : jcookies) {
                    APPCONTEXT.addDacsCookie(jcookie, FEDERATION_DOMAIN, "/");
                }
            }
            try {
                List<Credentials> credentials = JURISDICTION.currentCredentials(APPCONTEXT);
                if (credentials.size() < 1) {
                    throw new RuntimeException("application configuration error: no valid user found in request. Report to application administrator.");
                }
                username = credentials.get(0).getName();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (username == null) {
            return "dcaughra";
        } else {
            return username;
        }
    }
    
    public List<Credentials> getCredentials() {
        List <Credentials> credentials = new ArrayList<Credentials>();
        ContainerContext cc = (ContainerContext) getContextProperty(ContainerContext.CONTEXT_PROPERTY_NAME);
        if (cc != null) {
            javax.servlet.http.Cookie[] jcookies = cc.getCookies();
            if (jcookies != null) {
                for (javax.servlet.http.Cookie jcookie : jcookies) {
                    userContext.addDacsCookie(jcookie, "." + FEDERATION_DOMAIN, "/");
                    logger.debug("adding Java Cookie to userContext: " 
                            + jcookie.getName() + "=" + jcookie.getValue());
                }
            }
        }
        userContext.dumpDacsCookies(System.out);
        try {
//            credentials = JURISDICTION.currentCredentials(userContext);
            credentials = userContext.getDacsCredentials(FEDERATION);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return credentials;
    }
}

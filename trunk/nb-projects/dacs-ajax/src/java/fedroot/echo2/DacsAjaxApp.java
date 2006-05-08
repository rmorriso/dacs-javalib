/* 
 * This file is part of the Echo2 Extras Project.
 * Copyright (C) 2005-2006 NextApp, Inc.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 */

package fedroot.echo2;

import com.fedroot.dacs.DacsUserAccount;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import com.fedroot.dacs.UserContext;
import java.util.List;
import java.util.ResourceBundle;
import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.app.Window;

/**
 * DACS Ajax Demonstration Application
 */
public class DacsAjaxApp extends ApplicationInstance {

    public static final String DACS_BASEURI, FEDERATION_DOMAIN;
    public static final UserContext USERCONTEXT = UserContext.getInstance("foo");
    public static final Federation FEDERATION;
    static {
        // Static initializer to retrieve information from configuration properties file.
        ResourceBundle config = ResourceBundle.getBundle("/fedroot/echo2/demo/Configuration");
        FEDERATION_DOMAIN = config.getString("federation_domain");
        DACS_BASEURI = config.getString("dacs_baseuri");
        Federation federation;
        try {
            federation = Federation.getInstance(USERCONTEXT, DACS_BASEURI);
        } catch (Exception ex) {
            federation = null;
        }
        FEDERATION = federation;
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

    private Window mainWindow;
    private ConsoleWindowPane console;
    
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
        return USERCONTEXT.authenticate(acc,password);
    }
    
    public String[] jurisdictionList() {
        String [] jurisdictions = null;
        if (FEDERATION != null) {
          jurisdictions = FEDERATION.getAuthenticatingJurisdictionNames().toArray(new String[0]);
        }
        return jurisdictions;
    }
}

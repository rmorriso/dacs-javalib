/*
 * DacsAjaxServlet.java
 *
 * Created on September 22, 2005, 12:56 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2;
import java.util.List;
import javax.servlet.http.Cookie;
import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.webcontainer.WebContainerServlet;

/**
 * Interactive Test Application <code>WebContainerServlet</code> implementation.
 */
public class DacsAjaxServlet extends WebContainerServlet {

    /**
     * @see nextapp.echo2.webcontainer.WebContainerServlet#newApplicationInstance()
     */
    public ApplicationInstance newApplicationInstance() {
        return new DacsAjaxApp();
    }
    
    public String getDacsCookies() {
        Cookie[] cookie = super.getActiveConnection().getRequest().getCookies();
        return null;
    }
}

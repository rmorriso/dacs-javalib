/*
 * DacsAuthenticateRequest.java
 *
 * Created on February 23, 2010, 3:12 PM
 *
 * Copyright (c) 2010 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.client;

import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.DACS.ServiceName;
import fedroot.servlet.ServiceParameters;
import java.net.URISyntaxException;

/**
 *
 * @author rmorriso
 */
public class DacsAuthenticateRequest extends DacsWebServiceRequest {

    public enum args { DACS_BROWSER, DACS_JURISDICTION, DACS_VERSION, FORMAT, PASSWORD, USERNAME };

    private Jurisdiction jurisdiction;
    private String username;
    private String password;

    public DacsAuthenticateRequest(Jurisdiction jurisdiction, String username, String password) throws URISyntaxException {
        super(jurisdiction.getDacsUri() + "/" + ServiceName.dacs_authenticate);
        this.jurisdiction = jurisdiction;
        this.username = username;
        this.password = password;
    }

    @Override
    public ServiceParameters getServiceParameters() {
        ServiceParameters serviceParameters = super.getServiceParameters();
        serviceParameters.addParameter(args.USERNAME, username);
        serviceParameters.addParameter(args.PASSWORD, password);
        serviceParameters.addParameter(args.DACS_BROWSER, "1");
        serviceParameters.addParameter(args.DACS_JURISDICTION, jurisdiction.getJName());
        return serviceParameters;
    }
}

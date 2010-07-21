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
import fedroot.servlet.ParameterValidator;
import fedroot.servlet.ParameterValidators;
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

    /**
     * return ParameterValidators for use by clients that implement
     * the DacsAuthenticateRequest service
     * @return
     */
    @Override
    public ParameterValidators getParameterValidators() {
        ParameterValidators parameterValidators = new ParameterValidators();
        ParameterValidator parameterValidator = new ParameterValidator(ParameterValidator.ValidationType.ALL);
        parameterValidator.addParameter(args.USERNAME.toString());
        parameterValidator.addParameter(args.PASSWORD.toString());
        parameterValidator.addParameter(args.DACS_BROWSER.toString());
        parameterValidator.addParameter(args.DACS_JURISDICTION.toString());
        parameterValidators.addValidator(parameterValidator);
        return parameterValidators;
    }

}

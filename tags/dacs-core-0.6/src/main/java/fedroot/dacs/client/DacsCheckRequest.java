/*
 * DacsCheckRequest.java
 *
 * Created on February 23, 2010, 3:12 PM
 *
 * Copyright (c) 2010 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.client;

import fedroot.servlet.ParameterValidator;
import fedroot.servlet.ParameterValidators;
import fedroot.servlet.ServiceParameters;
import fedroot.servlet.WebServiceRequest;


/**
 *
 * @author rmorriso
 */
public class DacsCheckRequest extends WebServiceRequest {

    public enum args { DACS_ACS };

    private boolean checkOnly;

    public DacsCheckRequest(String uri) {
        this(uri,false);
    }

    public DacsCheckRequest(String uri, boolean checkOnly) {
        super(uri);
        this.checkOnly = checkOnly;
    }


    @Override
    public ServiceParameters getServiceParameters() {
       ServiceParameters serviceParameters = new ServiceParameters();
       serviceParameters.addParameter(args.DACS_ACS, getDacsAcs());
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
        parameterValidator.addParameter(args.DACS_ACS.toString());
        parameterValidators.addValidator(parameterValidator);
        return parameterValidators;
    }

    private String getDacsAcs() {
        switch (getHttpRequestType()) {
            case GET:
                return (checkOnly ? "-check_only%20-format+XMLSCHEMA" : "-check_fail%20-format+XMLSCHEMA");
            case PUT:
            default:
                return  (checkOnly ? "-check_only -format XMLSCHEMA" : "-check_fail -format XMLSCHEMA");
        }
    }
}

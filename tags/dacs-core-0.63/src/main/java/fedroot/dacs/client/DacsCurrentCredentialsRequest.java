/*
 * DacsCurrentCredentialsRequest.java
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

/**
 *
 * @author rmorriso
 */
public class DacsCurrentCredentialsRequest extends DacsWebServiceRequest {

    public enum args { DACS_JURISDICTION };

    public DacsCurrentCredentialsRequest(Jurisdiction jurisdiction) {
        super(jurisdiction.getDacsUri() + "/" + ServiceName.dacs_current_credentials);
    }

    /**
     * return ParameterValidators for use by clients that implement
     * the DacsCurentCredentialsRequest service
     * @return
     */
    @Override
    public ParameterValidators getParameterValidators() {
        ParameterValidators parameterValidators = new ParameterValidators();
        ParameterValidator parameterValidator = new ParameterValidator(ParameterValidator.ValidationType.ANY);
        parameterValidator.addParameter(args.DACS_JURISDICTION.toString());
        parameterValidators.addValidator(parameterValidator);
        return parameterValidators;
    }

}

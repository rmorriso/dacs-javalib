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
import fedroot.web.ServiceParameters;
import java.net.URISyntaxException;

/**
 *
 * @author rmorriso
 */
public class DacsCurrentCredentialsRequest extends DacsWebServiceRequest {

    public DacsCurrentCredentialsRequest(Jurisdiction jurisdiction) throws URISyntaxException {
        super(jurisdiction.getDacsUri() + "/" + ServiceName.dacs_current_credentials);
        ServiceParameters serviceParameters = new ServiceParameters();
//        serviceParameters.addParameter(null, null);
    }


}

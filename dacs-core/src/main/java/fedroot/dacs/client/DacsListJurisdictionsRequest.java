/*
 * DacsListJurisdictionsRequest.java
 *
 * Created on February 23, 2010, 3:12 PM
 *
 * Copyright (c) 2010 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.client;

import fedroot.dacs.DACS.ServiceName;
import fedroot.web.ServiceParameters;

/**
 *
 * @author rmorriso
 */
public class DacsListJurisdictionsRequest extends DacsWebServiceRequest {

    public DacsListJurisdictionsRequest(String dacsURI) {
        super(dacsURI + "/" + ServiceName.dacs_list_jurisdictions);
    }

    @Override
    public ServiceParameters getServiceParameters() {
        return super.getServiceParameters();
    }
}

/*
 * DacsWebServiceRequest.java
 *
 * Created on February 23, 2010, 12:12 PM
 *
 * Copyright (c) 2010 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.client;

import fedroot.dacs.DACS.CommonArgs;
import fedroot.dacs.DACS.ReplyFormat;
import fedroot.web.ServiceParameters;
import fedroot.web.WebServiceRequest;
import java.net.URISyntaxException;

/**
 * All DACS Web services extend this class
 * @author rmorriso
 */
abstract public class DacsWebServiceRequest extends WebServiceRequest {

    private ReplyFormat replyFormat;
    /**
     * constructor for DacsWebServiceRequest
     * @param serviceuri uri for DACS service
     */
    protected DacsWebServiceRequest(String serviceuri) throws URISyntaxException {
        super(serviceuri);
    }

    public ReplyFormat getReplyFormat() {
        return replyFormat;
    }

    public void setReplyFormat(ReplyFormat replyFormat) {
        this.replyFormat = replyFormat;
    }

    public void initParameters() {
        ServiceParameters dacsServiceParameters = getServiceParameters();
        // default reply format for DACS Web service requests is XML Schema
        dacsServiceParameters.addParameter(CommonArgs.FORMAT.toString(), replyFormat.toString());
    }
    
}

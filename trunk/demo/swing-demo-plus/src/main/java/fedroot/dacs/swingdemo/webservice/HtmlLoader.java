/*
 * HtmlLoader.java
 * Created on Jan 16, 2010 5:48:01 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.swingdemo.webservice;

import com.fedroot.dacs.AccessDenied;
import com.fedroot.dacs.DacsAcs;
import fedroot.dacs.client.DacsCheckRequest;
import fedroot.dacs.entities.WebServiceEntityLoader;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
public class HtmlLoader extends WebServiceEntityLoader {

    String htmlDocument;

    public HtmlLoader(DacsCheckRequest dacsCheckRequest, DacsClientContext dacsClientContext) throws DacsException {
        super(dacsCheckRequest);
        Object entity = load(dacsClientContext, "com.fedroot.dacs");
        if (entity instanceof DacsAcs) {
            DacsAcs dacsAcs = (DacsAcs) entity;
            AccessDenied accessDenied = dacsAcs.getAccessDenied();
            if (accessDenied.getEvent900() != null) {
                throw new DacsException(900, accessDenied.getEvent900().getMessage());
            } else if (accessDenied.getEvent901() != null) {
                throw new DacsException(901, accessDenied.getEvent901().getMessage());
            } else if (accessDenied.getEvent902() != null) {
                throw new DacsException(902, accessDenied.getEvent902().getMessage());
            } else if (accessDenied.getEvent903() != null) {
                throw new DacsException(903, accessDenied.getEvent903().getMessage());
            } else if (accessDenied.getEvent904() != null) {
                throw new DacsException(904, accessDenied.getEvent904().getMessage());
            } 
        } else {
            htmlDocument = entity.toString();
        }
    }

    public String getHtmlDocument() {
        return htmlDocument;
    }


}

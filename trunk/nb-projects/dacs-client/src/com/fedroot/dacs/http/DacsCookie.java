/*
 * DacsCookie.java
 *
 * Created on November 1, 2006, 8:43 AM
 *
 * Copyright 2006 Cisco Systems, Inc. All rights reserved. No portion of this
 * software may be reproduced in any form, or by any means, without prior written
 * permission from Cisco Systems, Inc.
 */

package com.fedroot.dacs.http;

import com.fedroot.dacs.exceptions.DacsRuntimeException;
import org.apache.commons.httpclient.Cookie;

/**
 *
 * @author ricmorri
 */
public class DacsCookie extends Cookie {
    
    /** Creates a new instance of DacsCookie */
    public DacsCookie(javax.servlet.http.Cookie jcookie) throws DacsRuntimeException {
        super(jcookie.getDomain(), jcookie.getName(),jcookie.getValue(),"/", jcookie.getMaxAge(),jcookie.getSecure());
        if(! isDacsCookie(jcookie)) {
            throw new DacsRuntimeException("invalid DACS cookie: " + jcookie.getName());
        }
    }
    
    public static boolean isDacsCookie(javax.servlet.http.Cookie jcookie) {
        return jcookie.getName().startsWith("DACS:");
    }
}

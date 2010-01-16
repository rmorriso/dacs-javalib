/*
 * DacsCookie.java
 *
 * Created on November 1, 2006, 8:43 AM
 *
 * Copyright 2006 Cisco Systems, Inc. All rights reserved. No portion of this
 * software may be reproduced in any form, or by any means, without prior written
 * permission from Cisco Systems, Inc.
 */

package fedroot.dacs.http;

import fedroot.dacs.exceptions.DacsRuntimeException;
import java.util.Date;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 *
 * @author ricmorri
 */
public class DacsCookie extends BasicClientCookie {
    
    /** Creates a new instance of DacsCookie from a javax.servlet.http.netCookie*/
    public DacsCookie(String federationDomain, javax.servlet.http.Cookie jcookie) throws DacsRuntimeException {
//        super(federationDomain, jcookie.getName(),jcookie.getValue(),"/", jcookie.getMaxAge(),jcookie.getSecure());
        super(jcookie.getName(), jcookie.getValue());
        if(! isDacsCookie(jcookie)) {
            throw new DacsRuntimeException("invalid DACS cookie: " + jcookie.getName());
        }
        Date expires = new Date();
        expires.setTime(expires.getTime() + jcookie.getMaxAge());

        setExpiryDate(expires);
        setSecure(jcookie.getSecure());
    }
    
    public static boolean isDacsCookie(javax.servlet.http.Cookie jcookie) {
        return jcookie.getName().startsWith("DACS:");
    }

}

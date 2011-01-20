/*
 * DacsRuntimeException.java
 *
 * Created on April 20, 2006, 9:01 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.exceptions;

/**
 *
 * @author rmorriso
 */
public class DacsRuntimeException extends RuntimeException {
    
    /** Creates a new instance of DacsRuntimeException */
    public DacsRuntimeException() {
        super();
    }
    
        /** Creates a new instance of DacsException */
    public DacsRuntimeException(String message) {
        super(message);
    }
    
}

/*
 * DacsException.java
 *
 * Created on December 9, 2005, 3:58 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.exceptions;

/**
 *
 * @author rmorriso
 */
public class DacsException extends Exception {

    private int statusCode;
    
    /** Creates a new instance of DacsException */
    public DacsException() {
        super();
    }
    
    /** Creates a new instance of DacsException */
    public DacsException(String dacsstatusline) {
        super(dacsstatusline);
    }

    /** Creates a new instance of DacsException */
    public DacsException(int statusCode, String dacsstatusline) {
        super(dacsstatusline);
        this.statusCode = statusCode;
    }


    public int getStatusCode() {
        return statusCode;
    }
}

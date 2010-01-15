/*
 * DacsCheck.java
 *
 * Created on November 24, 2005, 11:35 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;

import java.io.IOException;
import java.io.InputStream;

/**
 * facade Class for DACS dacs_check command
 * Note that dacs_check is a command line program - its 
 * commandpath must be specified
 * @author rmorriso
 */
public class DacsCheck {
    private static final String commandpath = "/local/dacs/bin/dacscheck";
    
    /**
     * test access by @param user to @param resource
     * @param username DACS user with respect to which test should be executed
     * @param resource test access to this resource for @param username
     * @throws java.io.IOException TODO
     * @throws java.lang.Exception TODO
     * @return true if access to @param resource would be allowed to @param username, else false
     */
    public static boolean test(String username, String resource)
    throws IOException, Exception {
        String[] command = new String[] {commandpath, "-q", "-fn", "FOO", "-app",  "test", "-i", username, resource};
        // Execute command
        Process child = Runtime.getRuntime().exec(command);
        // use waitFor() not exitValue() to avoid thread timing (see Java pitfall)
        // beware possibility of IO buffer filling and waitFor() not returning
        //  0 means access is granted
        //  1 means access is denied
        //  anything else means an error occurred
        int exitstatus = child.waitFor();
        switch (exitstatus) {
            case 0: return true;
            case 1: return false;
            default: throw new Exception("dacscheck error");
        }
    }
}
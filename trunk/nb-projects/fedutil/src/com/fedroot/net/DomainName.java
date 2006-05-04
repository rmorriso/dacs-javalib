/*
 * DomainTree.java
 *
 * Created on December 14, 2005, 1:18 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.net;


/**
 *
 * @author rmorriso
 */
public class DomainName {
    
    
    public static String head(String domainname) {
        int i = domainname.indexOf('.');
        if (i <= 0) {
            return null;
        } else {
            return domainname.substring(0,i);
        }
    }
    
    public static String tail(String domainname) {
        int i = domainname.indexOf('.');
        if (i < 0) {
            return null;
        } else {
            return domainname.substring(i+1);
        }
    }
 }

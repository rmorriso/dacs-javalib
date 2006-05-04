/*
 * DomainTree.java
 *
 * Created on December 14, 2005, 1:18 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.net;

import java.util.HashMap;

/**
 * an abstract data type for storing and retrieving properties to
 * be associated with an Internet domainname;
 * Example:
 *        com --> properties map
 *          |
 *           -->  fedroot --> properties map
 *                   | |
 *                   |  --> demo --> properties map
 *                    ----> test --> properties map
 *            ...
 * the properties associated with a given node in the DomainTree
 * is simply a mapping of property name (String) to value (Object)
 *
 * @author rmorriso
 */
public class DomainTree {
    protected HashMap<String,Object> properties;
    protected HashMap<String, DomainTree> subdomains;
    
    /**
     * no-arg constructor
     */
    public DomainTree() {
        this.properties = new HashMap<String,Object>();
        this.subdomains = new HashMap<String,DomainTree>();
    }
    
    /**
     * insert a <property, value> pair in the DomainTree 
     * at the node domainname
     */
    public void insert(String domainname, String property, Object value) {
        // store domainname in reverse order by domain components
        insert(reverse(domainname), property, value);
    }
    
    private void insert(String[] domaincomponents, String property, Object value) {
        if (domaincomponents == null) {
            properties.put(property, value);
        } else {
            DomainTree subdomain = getSubdomain(head(domaincomponents));
            // if subdomain has already been present return it
            // otherwise create and return a new subdomain
            if (subdomain == null) {
                subdomain = createSubdomain(head(domaincomponents));
            }
            // recursively insert property under subdomain
            subdomain.insert(tail(domaincomponents), property, value);
        }
    }
    
    /**
     * 
     * @param domainname 
     * @param property 
     * @return 
     */
    public Object get(String domainname, String property) {
        return get(reverse(domainname), property);
    }
    
    private Object get(String[] domaincomponents, String property) {
        if (domaincomponents == null) { // leaf node
            return properties.get(property);
        } else { // go deeper
            DomainTree subdomain = getSubdomain(head(domaincomponents));
            if (subdomain == null) {
                return null;
            } else {
                return subdomain.get(tail(domaincomponents), property);
            }
        }
    }
    
    /**
     * return the first occurence of property found searching "up" domainname
     */
    public Object search(String domainname, String property) {
        if (domainname == null) {
            return null;
        }
        Object value = get(domainname, property);
        if (value != null) {
            return value;
        } else {
            return search(DomainName.tail(domainname), property);
        }
    }
        
    private String head(String[] strings) {
        if (strings.length >= 1) {
            return strings[0];
        } else { 
            return null;
        }
    }
    
    private String[] tail(String[] strings) {
        if (strings.length <= 1) {
            return null;
        } else {
            String[] tail = new String[strings.length - 1];
            for (int i = 0; i < tail.length; i++) {
                tail[i] = strings[i+1];
            }
            return tail;
        }
    }
    
    /**
     * for domainname = "demo.fedroot.com", returns
     * an array {"com", "fedroot", "demo" }
     */
    private String[] reverse(String domainname) {
        String[] domaincomponents = domainname.split("\\.");
        int n = domaincomponents.length;
        String[] reverse = new String[n];
        for (int i = 0; i < n; i++) {
            reverse[i] = domaincomponents[n-1-i];
        }
        return reverse;
    }
    
    private DomainTree getSubdomain(String name) {
        return this.subdomains.get(name);
    }
    
    private DomainTree createSubdomain(String name) {
        DomainTree subdomain = new DomainTree();
        this.subdomains.put(name, subdomain);
        return subdomain;
    }
}

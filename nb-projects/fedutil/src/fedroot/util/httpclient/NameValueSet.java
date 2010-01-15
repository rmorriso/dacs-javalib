/*
 * NameValueSet.java
 *
 * Created on August 26, 2005, 11:31 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.util.httpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.httpclient.NameValuePair;

/**
 * Typed NameValueSet
 * a set of 1:M mappings from name keys to associated String values plus a
 * a set of name keys that must be present and assigned a value in the NameValueSet
 * If a mandatory name-value association is absent in NameValueSet the getNVPArray() 
 * method throws an exception.
 * @author rmorriso
 */
public class NameValueSet<T> {
    protected HashMap<T,String> nvhash;
    protected Set<T> mandatory;
    
    /**
     * no-arg constructor for NameValueSet;
     * initializes a typed HashMap to store  name-value associations;
     * there are no mandatory elements
     * 
     */
    public NameValueSet() {
       nvhash = new HashMap<T,String>();
       this.mandatory = (Set<T>) null;
    }
    
    /**
     * constructor for NameValueSet;
     * initializes a typed HashMap to store name-value associations
     * @param mandatory the set of name key elements of Type T that must be associated
     * with a value in in HashMap
     */
    public NameValueSet(Set<T> mandatory) {
        nvhash = new HashMap<T,String>();
        this.mandatory = mandatory;
    }
    
    
    /**
     * build an HttpClient NameValuePair array from the current state of
     * the NameValueSet. Throw a runtime exception if a mandatory name in the NameValueSet
     * is absent
     * @throws java.lang.RuntimeException if a mandatory name-value association is absent
     * @return the NameValuePair array generated from the NameValueSet
     */
   
    public NameValuePair[] getNVPArray()
    throws RuntimeException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        T name;
        String value;
        if (mandatory != null && ! nvhash.keySet().containsAll(mandatory)) {
            throw new RuntimeException("some mandatory names are missing");
        }
        NameValuePair[] nvparray = new NameValuePair[0];
        for (java.util.Map.Entry<T,String> entry : nvhash.entrySet()) {
            name = (T)entry.getKey();
            if (entry.getValue() == null) {
                if(isMandatory(name)) {
                    throw new RuntimeException("NVS getNVPArray(): " + name + " is mandatory but not set");
                }
            } else {
                value = entry.getValue().toString();
                nvps.add(new NameValuePair(name.toString(), value));
            }
        }
        return nvps.toArray(nvparray);
    }
        
    /**
     * get the value associated with a name key in the NameValueSet
     * @param name the name key
     * @throws java.lang.Exception TODO
     * @return the value associated with name in the NameValueSet
     */
    public String getValue(T name)
    throws Exception {
        if(nvhash.containsKey(name)) {
            return nvhash.get(name);
        } else {
            throw new Exception("NVS getValue(): " + name + "not defined in NameValueSet");
        }
    }
    
    /**
     * set the value associated with a name keuy in the NameValueSet
     * Note that Java type system enforces type constaint on allowed keys.
     * @param name the name key
     * @param value the value to associate with name
     */
    public void setValue(T name, String value) {
         nvhash.put(name, value);
    }
    
    /**
     * bulk pair-wise association of a typed array of name keys with a 
     * corresponding array of values
     * @param name the name key array
     * @param value the value array
     * @throws java.lang.Exception TODO
     */
    public void setValues(T[] name, String[] value) 
    throws Exception {
        if (name.length != value.length) {
            throw new Exception("NVS setValues(): name and value arrays different lengths");
        }
        for (int i =0; i < name.length; i++) {
            this.setValue(name[i],value[i]);
        }
    }
    
    /**
     * remove the association keyed on name from the NameValueSet
     * @param name the name to be removed
     * @throws java.lang.Exception TODO
     */
    public void remove(T name)
    throws Exception {
        if(isMandatory(name)) {
          throw new Exception("NVS remove(): " + name + "is mandatory in NameValueSet");
        } else {
          nvhash.remove(name);
        }
    }
    
    /**
     * test if given name is a mandatory key in NameValueSet
     * @param the name to test
     * @return true if mandatory, else false
     */
    private boolean isMandatory(T name) {
        if (mandatory != null) {
            return mandatory.contains(name);
        } else {
            return false;
        }
    }
    
    /**
     * dump the contents of the NameValueSet to System.out
     */
    public void dumpNameValueSet() {
        for (java.util.Map.Entry<T,String> entry : nvhash.entrySet()) {
            T name = (T)entry.getKey();
            String value = "";
            if (entry.getValue() != null) {
                value = entry.getValue().toString();
            }
            System.out.println("[" + name + ", " + value + ", required(" + isMandatory(name) + ")]");
        }
    }
}

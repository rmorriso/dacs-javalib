/*
 * XmlUtil.java
 *
 * Created on December 16, 2005, 11:05 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.xml;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

/**
 * helper class for XmlBeans
 * @author rmorriso
 */
public class XmlUtil {
    
    /** Creates a new instance of XmlUtil */
    public XmlUtil() {
    }
    
    /**
     * 
     * @param xmlfragment 
     * @return 
     */
    public static String extract(XmlObject xmlfragment) {
        String xmlstring = xmlfragment.toString();
        int i = xmlstring.indexOf(">");
        if (i > 0) {
            String tail = xmlstring.substring(i + 1);
            int j = tail.indexOf("<");
            return tail.substring(0,j);
        } else {
            return null;
        }
    }
}

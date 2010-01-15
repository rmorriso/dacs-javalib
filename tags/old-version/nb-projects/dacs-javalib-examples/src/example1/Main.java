/*
 * Example1.java
 *
 * Created on December 2, 2005, 8:28 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package example1;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.http.DacsGetMethod;
import java.io.IOException;
import org.apache.commons.httpclient.HttpStatus;

/**
 * main class for example 1 in DJL system documentation
 * @author rmorriso
 */
public class Main {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            DacsContext dacscontext = new DacsContext();
            String resource_uri = "https://demo.fedroot.com/test/dacs-wrapped/hello-dacs.shtml";
            DacsGetMethod dacsget = new DacsGetMethod(resource_uri);
            int httpstatus = dacscontext.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) {
                String s = dacsget.getResponseBodyAsString();
                System.out.println(s);
            } else {
                // handle error
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

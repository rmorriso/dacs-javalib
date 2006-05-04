/*
 * Example3.java
 *
 * Created on December 2, 2005, 8:28 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package example3;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.dacs.services.DacsAuthenticateService;
import com.fedroot.dacs.xmlbeans.DacsAuthReplyDocument;
import org.apache.xmlbeans.XmlObject;

/**
 * main class for example 3 in DJL system documentation
 * Direct Authentication
 * Output:
 * Present cookies:
 * DACS:DEMO:METALOGIC:smith(version = 0, domain = .demo.fedroot.com, path = /)
 * External Form: DACS:DEMO:METALOGIC:smith=jT1NsIl7zKf ... ket1wbL8/
 * @author rmorriso
 */
public class Main {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        String baseuri = "https://demo.fedroot.com/metalogic/dacs";
        String jurisdiction = "METALOGIC";
        String username = "smith";
        String password = "foozle";
        DacsContext dacscontext = new DacsContext();
        try {
            // prepare a DacsAuthenticateService for invocation in METALOGIC
            DacsAuthenticateService dacsservice =
                    new DacsAuthenticateService(baseuri, jurisdiction, username, password);
            // execute the service's DacsGetMethod wrt the dacscontext
            DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
            int dacsstatus = dacscontext.executeCheckFailMethod(dacsget);
            if (dacsstatus == DacsStatus.SC_DACS_ACCESS_GRANTED) {
                // parse the response stream as XML
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                // Check that reply is an instance of the DacsAuthReplyDocument
                System.out.println(expectedXmlObject.getClass().getName());
                if(expectedXmlObject instanceof DacsAuthReplyDocument) {
                    DacsAuthReplyDocument doc =
                            (DacsAuthReplyDocument)expectedXmlObject;
                    // get DacsAuthReply element
                    DacsAuthReplyDocument.DacsAuthReply authreply = doc.getDacsAuthReply();
                    // if authentication was successful credentials will be set
                    if (authreply.isSetDacsCurrentCredentials())
                        System.out.println("success: " + authreply.getDacsCurrentCredentials());
                } else {
                    System.out.println("failure: incorrect XML document type");
                }
            } else {
                System.out.println("failure: dacsstatus = " + DacsStatus.getStatusText(dacsstatus));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        dacscontext.dumpDacsCookies(System.out);
    }
}

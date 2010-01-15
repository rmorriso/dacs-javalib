/*
 * Example2.java
 *
 * Created on December 2, 2005, 8:28 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package example2;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;
import java.io.IOException;

/**
 * main class for example 2 in DJL system documentation
 * Checking a DACS Request
 * Output:
 * <?xml version="1.0" encoding="UTF-8"?>
 * <dacs_acs xmlns="http://fedroot.com/dacs/v1.4" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://fedroot.com/dacs/v1.4 /dtd-xsd/dacs_acs.xsd">
 *   <access_denied>
 *     <event902 message="Access denied, user not authenticated" handler="https://demo.fedroot.com/fedadmin/utils/login.php"/>
 *   </access_denied>
 * </dacs_acs>
 * @author rmorriso
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            DacsContext dacscontext = new DacsContext();
            DacsGetMethod dacsget = new DacsGetMethod("https://demo.fedroot.com/test/dacs-wrapped/hello-user.html");
//            int dacsstatus = dacscontext.executeMethod(dacsget, DACS.AcsCheck.check_only, DACS.ReplyFormat.XMLSCHEMA);
            int dacsstatus = dacscontext.executeCheckOnlyMethod(dacsget);
            switch (dacsstatus) {
                case DacsStatus.SC_DACS_ACCESS_GRANTED:
                    System.out.println("DacsGet returned status: (" + DacsStatus.SC_DACS_ACCESS_GRANTED + ") " + DacsStatus.getStatusText(dacsstatus));
                    System.out.println(dacsget.getResponseBodyAsString());                    
                    break;
                case DacsStatus.SC_DACS_ACCESS_DENIED:
                    System.out.println("DacsGet returned status: (" + DacsStatus.SC_DACS_ACCESS_DENIED + ") " + DacsStatus.getStatusText(dacsstatus));
                    System.out.println(dacsget.getResponseBodyAsString());
                    break;
                case DacsStatus.SC_DACS_ACCESS_ERROR:
                    System.out.println("DacsGet returned status: (" + DacsStatus.SC_DACS_ACCESS_ERROR + ") " + DacsStatus.getStatusText(dacsstatus));
                    break;
                default: // shouldn't happen - method is type-safe
                    System.out.println("DacsGet returned unknown status");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DacsException e) {
            System.out.println(e.getMessage());
        }
    }
}

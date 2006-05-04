/*
 * Example4_5_6.java
 *
 * Created on December 2, 2005, 8:28 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package example4_5_6;

import com.fedroot.dacs.DacsUserAccount;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import com.fedroot.dacs.UserContext;
import com.fedroot.dacs.exceptions.DacsException;
import org.apache.xmlbeans.XmlObject;

/**
 * main class for examples 4,5,6 in DJL system documentation
 * Authenticate with DacsUserAccount(s)
 * Output:
 * Credential: DACS:DEMO:METALOGIC:smith=RXFVwLWyb9x2igcLQuG8 ... 7CZOPB/MlMosTHsWiYwZU3G1LMy4UNE
 * Credential: DACS:DEMO:DSS:jones=jTyvCvU6YBUtiwcLROG8lZCoq9 ... P.YPggyr2HGuwUT6KtM78RustD6aQp9
 * @author rmorriso
 */
public class Main {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            String dacsbaseuri = "https://demo.fedroot.com/fedadmin/dacs";
            UserContext smith = UserContext.getInstance("john.smith");
            // instantiate federation and jurisdiction objects
            Federation demoFed = Federation.getInstance(smith, dacsbaseuri);
            Jurisdiction mlJur = demoFed.getJurisdictionByName("METALOGIC");
            Jurisdiction dssJur = demoFed.getJurisdictionByName("DSS");
            // useraccount in METALOGIC
            DacsUserAccount mlaccount = new DacsUserAccount(demoFed, mlJur, "smith", "yes");
            // useraccount in DSS
            DacsUserAccount dssaccount = new DacsUserAccount(demoFed, dssJur, "jones", "yes");
            // authenticate user smith with respect to mlaccount and dssaccount
            smith.authenticate(mlaccount, "foozle");
            smith.authenticate(dssaccount, "foozle");
            // UserContext smith should now contain credentials in METALOGIC/DSS
            for (org.apache.commons.httpclient.Cookie cookie : smith.getDacsCookies()) {
                System.out.println("Credential: " + cookie);
            }
        } catch (DacsException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

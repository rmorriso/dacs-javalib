/*
 * Example8.java
 *
 * Created on December 2, 2005, 8:28 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package example8;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.DacsUserAccount;
import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import com.fedroot.dacs.UserContext;
import java.io.IOException;

/**
 * main class for example 5 in DJL system documentation
 * @author rmorriso
 */
public class Main {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            String dacsbaseuri = "https://demo.fedroot.com/fedadmin/dacs";
            DacsContext dacscontext = new DacsContext();
            // instantiate federation and jurisdiction objects
            Federation demoFed = Federation.getInstance(dacscontext, dacsbaseuri);
            Jurisdiction mlJur = demoFed.getJurisdictionByName("METALOGIC");
            Jurisdiction dssJur = demoFed.getJurisdictionByName("DSS");
            UserContext smith = UserContext.getInstance("john.smith");
            DacsUserAccount mlaccount = new DacsUserAccount(demoFed, mlJur, "smith", "yes");
            DacsUserAccount dssaccount = new DacsUserAccount(demoFed, dssJur, "jones", "yes");
            // authenticate user smith with respect to account
            smith.authenticate(mlaccount, "foozle");
            smith.authenticate(dssaccount, "foozle");
            // replce with something else: smith.getContext().dumpDacsCookies(System.out);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

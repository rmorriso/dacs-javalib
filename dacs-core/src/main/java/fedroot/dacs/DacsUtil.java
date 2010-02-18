/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fedroot.dacs;

import fedroot.dacs.http.DacsCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ricmorri
 */
public class DacsUtil {

    private static Logger logger = Logger.getLogger(DacsUtil.class.getName());

    /**
     * extracts <code>username</code> from DACS credential cookie in HTTP request
     * looks for cookies with a valid DACS cookie name in @param request, then
     * creates and attaches a corresponding HttpClient cookie in a DacsClientContext.
     * The resulting DacsClientContext is used to execute a DacsCurrentCredentials
     * web service request. In some DACS deployments it is permissible for a user
     * session to carry mutliple DACS credentials, but in those cases one such
     * credential must be the <i>selected</i> credential. A DacsException is thrown if this
     * is not the case.
     * @return the username associated with the any DACS cookies found in @param request
     * or null if none is found
     */
    public static String resolveUser(HttpServletRequest request) {
        String username = null;
        if (request != null) {
            List<DacsCookie> dacsCookies = new ArrayList<DacsCookie>();
            javax.servlet.http.Cookie[] jcookies = request.getCookies();
            if (jcookies != null) {
                for (javax.servlet.http.Cookie jcookie : jcookies) {
                    if (DacsCookie.isDacsCookie(jcookie)) {
                        dacsCookies.add(new DacsCookie(jcookie));
//                        userName = fedHome.resolveUsername(jcookie);
//                        if (userName != null) { // found username
//                            logger.debug("resolved username " + userName);
//                            logger.debug("adding Java Cookie to userContext: " + jcookie.getName() + "=" + jcookie.getValue());
//                            return userName;
//                        }
                    }
                    // if there are multiple DACS cookies, exactly one must be
                    // indicated as the SELECTED DACS cookie
                    if (DacsCookie.isDacsSelectedCookie(jcookie)) {

                    }
                }
            }
        }
        return username;
    }
}

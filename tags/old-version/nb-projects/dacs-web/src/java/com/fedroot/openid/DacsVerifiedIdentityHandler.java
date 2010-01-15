package com.fedroot.openid;

import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;
import com.fedroot.dacs.UserContext;
import com.fedroot.dacs.exceptions.DacsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.net.URL;
import javax.servlet.http.HttpSession;
import org.idprism.openid.IdentityVerificationResponse;

public class DacsVerifiedIdentityHandler implements VerifiedIdentityHandler {
    public void handleVerifiedIdentity(IdentityVerificationResponse response, HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    throws IOException, ServletException {
        if (response.getMode().equals("id_res")) {
            try {
                HttpSession session = servletRequest.getSession();
                UserContext usercontext = (UserContext)session.getAttribute("user");
                Federation fed = Federation.getInstance(usercontext, "https://fedroot.com/dacs");
                Jurisdiction openid = fed.getJurisdictionByName("OPENID");
                String identityurl = response.getIdentityURL();
                URL u = new URL(identityurl);
                // TODO RNM - need better way to canonicallize identity URL
                identityurl= u.getProtocol() + "-" + u.getHost();
                //construct DACS credentials for identityurl, set DACS cookie in response
                String cookiename = usercontext.makeDacsCredentials(openid,identityurl);
                // now set the resulting creds in the response
                usercontext.setDacsCookies(servletResponse);
                // clear cookie from cache in usercontext
                // usercontext.removeCookie(cookiename); RNM - this isn't working
                usercontext.clearCookies();
            } catch (DacsException e) {
                throw new ServletException("failed to create DACS credentials from OpenID Identity");
            } catch (Exception e) {
                throw new ServletException("failed to instantiate Federation: " + e.getMessage());
            }
            // pass the IdentityResponse with request
            servletRequest.setAttribute("org.idprism.openid.response",response);
            servletRequest.getRequestDispatcher("/verificationResult.jsp").include(servletRequest, servletResponse);
        } else if (response.getMode().equals("cancel")) {
            servletRequest.getRequestDispatcher("/cancellationContent.html").include(servletRequest, servletResponse);
        } else {
            servletRequest.getRequestDispatcher("/errorContent.html").include(servletRequest, servletResponse);            
        }
    }
}

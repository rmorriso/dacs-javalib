package com.fedroot.openid;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import org.idprism.openid.IdentityVerificationResponse;

public interface VerifiedIdentityHandler {
    public void handleVerifiedIdentity(IdentityVerificationResponse response, HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    throws IOException, ServletException;
}

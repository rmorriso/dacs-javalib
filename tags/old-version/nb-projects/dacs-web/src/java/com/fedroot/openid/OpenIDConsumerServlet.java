package com.fedroot.openid;
/*
 * Copyright 2005 Nathan D. Bowen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fedroot.dacs.web.DacsServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.idprism.openid.util.OpenIDUtils;
import org.idprism.openid.association.Association;
import org.idprism.openid.IdentityVerificationRequest;
import org.idprism.openid.IdentityVerificationResponse;
import org.idprism.openid.OpenIDIdentity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;


/***
 * A classic-mode OpenID consumer as well as an AJAX-mode OpenID helper-consumer.
 */
public class OpenIDConsumerServlet extends DacsServlet {
    
    static Log log = LogFactory.getLog(OpenIDConsumerServlet.class);
    
    private OpenIDConsumer consumer;
    private VerifiedIdentityHandler verifiedIdentityHandler;
    
    public void init(ServletConfig servletConfig)
    throws ServletException {
        super.init(servletConfig);
        consumer = new OpenIDConsumer();
        
        String verifiedIdentityHandlerClassName = servletConfig.getInitParameter("verifiedIdentityHandler");
        if (verifiedIdentityHandlerClassName != null) {
            try {
                Object verifiedIdentityHandlerObj = Class.forName(verifiedIdentityHandlerClassName).newInstance();
                verifiedIdentityHandler = (VerifiedIdentityHandler)verifiedIdentityHandlerObj;
            } catch (ClassNotFoundException e) {
                log.error("Unable to find specified verified identity handler class '" + verifiedIdentityHandlerClassName + "'. OpenIDConsumerServlet not initialized.");
                throw new ServletException(e);
            } catch (Exception e) {
                log.error("Unable to initialize specified verified identity handler class '" + verifiedIdentityHandlerClassName + "'. OpenIDConsumerServlet not initialized.");
                throw new ServletException(e);
            }
        } else {
            log.error("No verifiedIdentityHandler found in servlet init parameters. OpenIDConsumerServlet not initialized.");
            throw new ServletException("No verifiedIdentityHandler found in OpenIDConsumerServlet init parameters.");
        }
    }
    
    /***
     * Does some simple checks on the request to choose between one of the 'do' methods.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        request.getRequestDispatcher("/include/header.html").include(request,response);
        if (request.getParameter("openid.mode") != null && request.getParameter("openid.mode").equalsIgnoreCase("id_res")) {
            doHandleIdentityVerificationResponse(request,response);
        } else if (request.getParameter("openid.mode") != null && request.getParameter("openid.mode").equalsIgnoreCase("cancel")) {
            doHandleIdentityVerificationResponse(request,response);
        } else if (request.getParameter("openid_url") != null) {
            boolean actAsHelper = request.getParameter("ajax") != null && request.getParameter("ajax").equalsIgnoreCase("true");
            if (actAsHelper) {
                doGenerateAjaxCleanedIdentity(request,response);
            } else {
                doGenerateOpenIDSigningRequest(request,response);
            }
        } else if (request.getPathInfo() != null && request.getPathInfo().startsWith("/admin")) {
            doAdmin(response);
        } else {
            request.getRequestDispatcher("/loginContent.jsp").include(request,response);
        }
        request.getRequestDispatcher("/include/footer.html").include(request,response);
    }
    
  
    /***
     * Respond (for AJAX mode) with a JSON object containing the cleaned
     * OpenID Identity URL and the OpenID Server URL.
     */
    private void doGenerateAjaxCleanedIdentity(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Generating cleaned identity and server URL for AJAX client.");
        }
        OpenIDIdentity identity = new OpenIDIdentity(request.getParameter("openid_url"));
        identity.prepare();
        if (identity.isPrepared()) {
            if (identity.isDelegated()) {
                request.getSession().setAttribute("org.idprism.openid.claimed_delegated_identity",identity);
            } else {
                request.getSession().setAttribute("org.idprism.openid.claimed_delegated_identity",null);
            }
            Association serverAssociation = consumer.getServerAssociation(identity.getServerURL());
            buf.append("{ id_server: \"" + identity.getServerURL() + "\", assoc_handle: \"" + serverAssociation.getHandle() + "\", clean_identity_url: \"" + identity.getIdentityToVerify() + "\" }");
        } else {
            buf.append("{ err_code: \"no_identity_servers\", err_text: \"Could not find your identity servers.\" }");
        }
    }
    
    /***
     * Redirect the UA (in classic mode) to the OpenID Server URL containing a
     * request for verification and signing of the cleaned Identity URL.
     */
    private void doGenerateOpenIDSigningRequest(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("Generating OpenID signing request for classic client.");
        }
        OpenIDIdentity identity = new OpenIDIdentity(request.getParameter("openid_url"));
        identity.prepare();
        if (identity.isPrepared()) {
            if (identity.isDelegated()) {
                request.getSession().setAttribute("org.idprism.openid.claimed_delegated_identity",identity);
            } else {
                request.getSession().setAttribute("org.idprism.openid.claimed_delegated_identity",null);
            }
            IdentityVerificationRequest idRequest = consumer.generateIdentityVerificationRequest(identity,getMyTrustRoot(request),OpenIDUtils.getServletPath(request));
            String signingRequestURL = OpenIDUtils.appendValueMapToURL("openid",identity.getServerURL(),idRequest.createValueMap(),response.getCharacterEncoding());
            // redirect to OpenID Server
            response.sendRedirect(signingRequestURL);
        } else {
            request.setAttribute("com.fedroot.error", "URL reference failed - please re-enter");
            request.getRequestDispatcher("/loginContent.jsp").include(request,response);
        }
    }
    
    /***
     * Respond with the result of verifying the server-provided signature,
     * in javascript for an AJAX client or HTML for a classic client.
     */
    private void doHandleIdentityVerificationResponse(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        boolean actAsHelper = request.getParameter("ajax") != null && request.getParameter("ajax").equalsIgnoreCase("true");
        String expectedReturnURL = OpenIDUtils.getServletPath(request) + (actAsHelper ? "?ajax=true" : "");
        
        IdentityVerificationResponse openIDResponse = new IdentityVerificationResponse();
        openIDResponse.processValueMap(OpenIDUtils.servletRequestToValueMap("openid",request));
        
        consumer.handleVerificationResponse(openIDResponse);
        
        if (openIDResponse.isError()) {
            if (actAsHelper) {
                buf.append("<script>");
                buf.append("if (parent.location.host == location.host) ");
                buf.append("parent.OpenID_general_error(");
                buf.append("{err_code: \"" + openIDResponse.getErrorCode() + "\",err_text: \"" + openIDResponse.getErrorText() + "\"}");
                buf.append(");");
                buf.append("</script>");
            } else {
                buf.append("Failed verification.<br>Error Code: " + openIDResponse.getErrorCode() + " (" + openIDResponse.getErrorText() + ")<br>");
                buf.append("Verification string used to test the provided signature was:<br>");
                buf.append(openIDResponse.getVerificationString());
            }
        } else if (openIDResponse.isCancelled()) {
            if (actAsHelper) {
                doClosePopup(request,response);
            } else{
                verifiedIdentityHandler.handleVerifiedIdentity(openIDResponse,request,response);
            }
        }  else {
            if (openIDResponse.isVerified()) {
                String verifiedIdentityURL = openIDResponse.getVerifiedIdentity().getCleanIDURL();
                if (request.getSession().getAttribute("org.idprism.openid.claimed_delegated_identity") != null) {
                    OpenIDIdentity originalIdentity = (OpenIDIdentity)request.getSession().getAttribute("org.idprism.openid.claimed_delegated_identity");
                    if (originalIdentity.getIdentityToVerify().equals(openIDResponse.getVerifiedIdentity().getCleanIDURL())) {
                        verifiedIdentityURL = originalIdentity.getCleanIDURL();
                    } else {
                        throw new ServletException("Received a response to the wrong request.");
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("Received identity verification from OpenID server '" + openIDResponse.getVerifiedIdentity().getServerURL() + "'. Identity verified: '" + openIDResponse.getVerifiedIdentity().getIdentityToVerify() + "', Timestamp: " + openIDResponse.getIssueTime() + ", Signature: " + openIDResponse.getSignature());
                }
                if (actAsHelper) {
                    String callbackPassCall = "";
                    callbackPassCall += ("OpenID_callback_pass(");
                    callbackPassCall += ("\"" + verifiedIdentityURL + "\"");
                    callbackPassCall += (",");
                    callbackPassCall += ("\"" + openIDResponse.getSignature() + "\"");
                    callbackPassCall += (",");
                    Pattern pattern = Pattern.compile("\n");
                    Matcher matcher = pattern.matcher(openIDResponse.getVerificationString());
                    callbackPassCall += ("\"" + matcher.replaceAll("\\\\n") + "\"");
                    callbackPassCall += (");\n");
                    
                    buf.append("<html><body><script>");
                    buf.append("if (window.opener) {");
                    buf.append("   var pwin = window.opener;");
                    buf.append("   pwin." + callbackPassCall);
                    buf.append("   window.close();");
                    buf.append("} else if (parent.location.host == location.host) {");
                    buf.append("   parent." + callbackPassCall);
                    buf.append("}");
                    buf.append("</script></body></html>");
                } else {
                    verifiedIdentityHandler.handleVerifiedIdentity(openIDResponse,request,response);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Received user_setup_url from OpenID server.");
                }
                if (actAsHelper) {
                    buf.append("<html><body><script>");
                    buf.append("if (parent.location.host == location.host) parent.OpenID_callback_fail(");
                    buf.append("\"" + request.getParameter("openid.user_setup_url") + "&openid.post_grant=close" + "\"");
                    buf.append(");");
                    buf.append("</script></body></html>");
                } else {
                    buf.append("It appears that you haven't confirmed that this site can be trusted with your remote identity. If you'd like to configure your trust settings, click ");
                    buf.append("<a href=\"" + request.getParameter("openid.user_setup_url") + "&openid.post_grant=return\">here</a>.");
                }
            }
        }
    }
    
    /***
     * Respond with the result of cancelling Identity verification,
     * in javascript for an AJAX client or HTML for a classic client.
     */
    private void doAdmin(HttpServletResponse response)
    throws IOException, ServletException {
        buf.append("<h1>IDPrism: OpenID Administration</h1>");
        buf.append("<h2>Associated Servers:</h2>");
        buf.append("<ul>");
        List serverAssociations = consumer.getServerAssociations();
        for (int i = 0; i < serverAssociations.size(); i++) {
            Association association = (Association) serverAssociations.get(i);
            buf.append("<li>");
            buf.append("<b>" + association.getRemoteURL() + "</b>");
            buf.append("<ul>");
            buf.append("<li>" + "<b>handle:</b> " + association.getHandle() + "</li>");
            buf.append("<li>" + "<b>type:</b> " + association.getClass() + "</li>");
            buf.append("<li>" + "<b>issued:</b> " + OpenIDUtils.formatTerseLocalDate(association.getIssueTime()) + "</li>");
            buf.append("<li>" + "<b>replace:</b> " + OpenIDUtils.formatTerseLocalDate(association.getReplacementTime()) + "</li>");
            buf.append("<li>" + "<b>expires:</b> " + OpenIDUtils.formatTerseLocalDate(association.getExpirationTime()) + "</li>");
            buf.append("</ul>");
            buf.append("</li>");
        }
        buf.append("</ul>");
        emitBuffer();
    }
    
    private void doClosePopup(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
//        PrintWriter out = response.getWriter();
//        response.setContentType("text/html");
        buf.append("<html><body><script>");
        buf.append("if (window.opener) {");
        buf.append("   window.close();");
        buf.append("}");
        buf.append("</script></body></html>");
        emitBuffer();
//        out.close();
    }
    
    /***
     * Construct an overly-simplistic trust root (with no wildcards) pointing
     * to the base URL of the provided request.
     */
    private String getMyTrustRoot(HttpServletRequest request) {
        String url = OpenIDUtils.getBaseURL(request);
        return url;
    }
    
}

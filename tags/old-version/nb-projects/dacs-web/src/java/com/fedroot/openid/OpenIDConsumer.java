package com.fedroot.openid;

import org.idprism.openid.association.Association;
import org.idprism.openid.association.AssociationSession;
import org.idprism.openid.association.ConsumerAssociationSession;
import org.idprism.openid.util.OpenIDUtils;
import org.idprism.openid.util.OpenIDNVFormat;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import org.idprism.openid.CheckAuthenticationResponse;
import org.idprism.openid.IdentityVerificationRequest;
import org.idprism.openid.IdentityVerificationResponse;
import org.idprism.openid.OpenIDIdentity;

public class OpenIDConsumer {
    static Log log = LogFactory.getLog(OpenIDConsumer.class);
    
    public IdentityVerificationRequest generateIdentityVerificationRequest(OpenIDIdentity identity, String trustRoot, String returnURL) {
        Association serverAssociation = getServerAssociation(identity.getServerURL());
        IdentityVerificationRequest idReq = new IdentityVerificationRequest();
        idReq.setSetupAllowed(true);
        idReq.setTrustRoot(trustRoot);
        idReq.setReturnURL(returnURL);
        idReq.setIdentityURL(identity.getIdentityToVerify());
        idReq.setAssociationHandle(serverAssociation.getHandle());
        return idReq;
    }
    
    //TODO: Need thread-safety here.
    public Association getServerAssociation(String serverURL) {
        Date now = new Date();
        Association association = (Association)serverAssociations.get(serverURL);
        if (association != null) {
            if (!association.getExpirationTime().after(now)) {
                // Expiration time has passed. Must replace.
                if (log.isDebugEnabled()) {
                    log.debug("Server association with '" + serverURL + "' has expired. Removing it from the cache.");
                }
                serverAssociations.remove(serverURL);
                association = null;
            } else if (!association.getReplacementTime().after(now)) {
                // Replacement time has passed, attempt to replace
                if (log.isDebugEnabled()) {
                    log.debug("Server association with '" + serverURL + "' has passed its replacement time. Attempting to replace.");
                }
                ConsumerAssociationSession assocSession = AssociationSession.getConsumerInstance("DH-SHA1",new String[] { "HMAC-SHA1" });
                try {
                    Association newAssociation = assocSession.initiateSession(serverURL);
                    association = newAssociation;
                    serverAssociations.put(serverURL,association);
                } catch (Exception e) {
                    log.warn("Exception while attempting to replace association with server '" + serverURL + "', will fall back to old association for now",e);
                }
            }
        }
        
        if (association == null) {
            ConsumerAssociationSession assocSession = AssociationSession.getConsumerInstance("DH-SHA1",new String[] { "HMAC-SHA1" });
            association = assocSession.initiateSession(serverURL);
            serverAssociations.put(serverURL,association);
        }
        association = (Association)serverAssociations.get(serverURL);
        if (log.isDebugEnabled()) {
            log.debug("Using association with '" + serverURL + "', handle '"
                    + association.getHandle()
                    + "'. Issued: " + OpenIDUtils.formatTerseLocalDate(association.getIssueTime())
                    + " Replace: " + OpenIDUtils.formatTerseLocalDate(association.getReplacementTime())
                    + " Expires: " + OpenIDUtils.formatTerseLocalDate(association.getExpirationTime()));
        }
        return association;
    }
    
    private HashMap serverAssociations = new HashMap();
    
    public List getServerAssociations() {
        return new ArrayList(serverAssociations.values());
    }
    
    private void removeServerAssociation(String serverURL, String handle) {
        synchronized(serverAssociations) {
            Association assoc = (Association)serverAssociations.get(serverURL);
            if (assoc != null && assoc.getHandle().equals(handle)) {
                serverAssociations.remove(serverURL);
            }
        }
    }
    
    public boolean handleVerificationResponse(IdentityVerificationResponse response) {
        if (response.isSetup()) {
            return false;
        } else if (response.isCancelled()) {
            return false;
        } else {
            OpenIDIdentity identity = new OpenIDIdentity(response.getIdentityURL());
            identity.prepare();
            Association assoc = null;
            if (response.getInvalidateHandle() == null) {
                assoc = getServerAssociation(identity.getServerURL());
            }
            if (assoc == null || !assoc.getHandle().equals(response.getAssociationHandle())) {
                log.warn("Couldn't find association with serverURL + '" +
                        identity.getServerURL() + "' and handle '" +
                        response.getAssociationHandle() + "' -- reverting to dumb consumer mode.");
                response.setMode("check_authentication");
                HttpClient httpClient = new HttpClient();
                PostMethod postMethod = new PostMethod(identity.getServerURL());
                try {
                    postMethod.setRequestBody(OpenIDUtils.valueMapToNameValuePairArray("openid",response.createValueMap()));
                    httpClient.executeMethod(postMethod);
                    CheckAuthenticationResponse checkAuthResponse = new CheckAuthenticationResponse();
                    String responseBody = postMethod.getResponseBodyAsString();
                    checkAuthResponse.processValueMap(OpenIDNVFormat.decodeString(responseBody));
                    response.setMode("id_res");
                    if (response.getInvalidateHandle() != null && response.getInvalidateHandle().length() > 0) {
                        if (checkAuthResponse.getInvalidateHandle() != null && checkAuthResponse.getInvalidateHandle().equals(response.getInvalidateHandle())) {
                            removeServerAssociation(identity.getServerURL(),response.getInvalidateHandle());
                        } else {
                            log.warn("id_res response from server '" + identity.getServerURL() 
                            + "' indicated assoc handle '" + response.getInvalidateHandle() 
                            + "' was invalid, but check_authentication did not confirm");
                        }
                    }
                    if (checkAuthResponse.getLifetime() <= 0) {
                        return false;
                    } else {
                        response.setVerifiedIdentity(identity);
                        return true;
                    }
                } catch (IOException e) {
                    log.error("Exception trying to contact server for dumb consumer check_authentication",e);
                }
                return false;
            } else {
                if (!assoc.verifySignature(response.getVerificationString(), response.getSignature())) {
                    response.setErrorCode("verify_failed");
                    response.setErrorText("signature verification failed");
                    return false;
                } else {
                    response.setVerifiedIdentity(identity);
                    return true;
                }
            }
        }
    }
    
}

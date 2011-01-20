/*
 * DacsStatusNew.java
 *
 * Created on December 8, 2005, 2:40 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.http;

/**
 * Constants enumerating the HTTP status codes (coppied from Jakarta Commons
 * HttpClient.HttpStatus.
 * All status codes defined in RFC1945 (HTTP/1.0, RFC2616 (HTTP/1.1), and
 * RFC2518 (WebDAV) are supported.
 * 
 * @see StatusLine
 * @author unascribed
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:jsdever@apache.org">Jeff Dever</a>
 * @author (DACS extension) <a href="mailto:rmorriso@fedroot.com">Rick Morrison</a>
 * 
 * TODO: Internationalization of reason phrases 
 * 
 * @version $Id: HttpStatus.java 155418 2005-02-26 13:01:52Z dirkv $
 */
public enum DacsStatusNew  {
    // --- 1xx Informational ---

    SC_CONTINUE(100, "Continue"),
    SC_SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    SC_PROCESSING(102, "WebDAV - Processing"),
    // --- 2xx Success ---
    SC_OK(200, "OK"),
    SC_CREATED(201, "Created"),
    SC_ACCEPTED(202, "Accepted"),
    SC_NON_AUTHORITATIVE_INFORMATION(203, "Non Authoritative Information"),
    SC_NO_CONTENT(204, "No Content"),
    SC_RESET_CONTENT(205, "Reset Content"),
    SC_PARTIAL_CONTEN(206, "Partial Content"),
    SC_MULTI_STATUS(207, "Multi-Status"),
    // --- 3xx Redirection ---
    SC_MULTIPLE_CHOICES(300, "Multiple Choices"),
    SC_MOVED_PERMANENTLY(301, "Moved Permanently"),
    SC_MOVED_TEMPORARILY(302, "Moved Temporarily"),
    SC_SEE_OTHER(303, "See Other"),
    SC_NOT_MODIFIED(304, "Not Modified"),
    SC_USE_PROXY(305, "Use Proxy"),
    SC_TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    // --- 4xx Client Error ---
    SC_BAD_REQUEST(400, "Bad Request"),
    SC_UNAUTHORIZED(401, "Unauthorized"),
    SC_PAYMENT_REQUIRED(402, "Payment Required"),
    SC_FORBIDDEN(403, "Forbidden"),
    SC_NOT_FOUND(404, "Not Found"),
    SC_METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    SC_NOT_ACCEPTABLE(406, "Not Acceptable"),
    SC_PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    SC_REQUEST_TIMEOUT(408, "Request Timeout"),
    SC_CONFLICT(409, "Conflict"),
    SC_GONE(410, "Gone"),
    SC_LENGTH_REQUIRED(411, "Length Required"),
    SC_PRECONDITION_FAILE(412, "Precondition Failed"),
    SC_REQUEST_TOO_LONG(413, "Request Entity to Large"),
    SC_REQUEST_URI_TOO_LONG(414, "Request URI Too Long"),
    SC_UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    SC_REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
    SC_EXPECTATION_FAILED(417, "Expectation Failed"),
    SC_INSUFFICIENT_SPACE_ON_RESOURCE(419, "WebDAV - Insufficient Space on Resource"),
    SC_METHOD_FAILURE(420, "WebDACS - Method Failure"),
    SC_UNPROCESSABLE_ENTITY(422, "WebDAV - Unprocessable Entity"),
    SC_LOCKED(423, "WebDAV - Locked"),
    SC_FAILED_DEPENDENCY(424, "WebDAV - Failed Dependency"),
    // --- 5xx Server Error ---
    SC_INTERNAL_SERVER_ERROR(500, "Server Error"),
    SC_NOT_IMPLEMENTED(501, "Not Implemented"),
    SC_BAD_GATEWAY(502, "Bad Gateway"),
    SC_SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    SC_GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    SC_HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
    SC_INSUFFICIENT_STORAGE(507,"Insufficient Storage"),
    // --- 9xx DACS event
    DACS_ACCESS_DENIED(797,
            "DACS has denied access"),
    DACS_ACCESS_GRANTED(798,
            "DACS has granted access"),
    DACS_ACCESS_ERROR(799,
            "DACS has generated an error during access control processing") ;
                
    public int code;
    private String message;
    DacsStatusNew(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
   public String getLocalizedMessage() {
       return this.message;
   }
}

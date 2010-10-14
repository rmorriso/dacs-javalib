/*
 * DacsClientContext.java
 * Created on Jan 15, 2010 8:02:49 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.demo.dacsweb;

import fedroot.dacs.DacsUtil;
import fedroot.dacs.entities.Credential;
import fedroot.dacs.entities.Federation;
import fedroot.dacs.entities.FederationLoader;
import fedroot.dacs.entities.Jurisdiction;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Norbert Trepke
 */
@WebFilter(filterName = "DacsFilter", urlPatterns = "/*", 
initParams = {
    @WebInitParam(name = "dacs_base_uri", value = "https://ca.nfis.org/cgi-bin/dacs"),
    @WebInitParam(name = "dacs_auth_jurisdiction", value = "CA"),
    @WebInitParam(name = "session_user_role", value = "dacsUserRole"),
    @WebInitParam(name = "auth_required", value = "false")})
public class DacsFilter implements Filter {

    private static final Logger logger = Logger.getLogger("com.fedroot");

    private static final boolean FINE = false;
    private static String DACS_BASE_URI;
    private static String DACS_AUTH_JURISDICTION;
    private static boolean DACS_AUTH_REQUIRED;
    private static String SESSION_DACS_CREDENTIAL = "session_dacs_credential";
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public DacsFilter() {
        logger.setLevel(Level.INFO);
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        // Create wrappers for the request and response objects.
        // Using these, you can extend the capabilities of the
        // request and response, for example, allow setting parameters
        // on the request before sending the request to the rest of the filter chain,
        // or keep track of the cookies that are set on the response.
        //
        // Caveat: some servers do not handle wrappers very well for forward or
        // include requests.

        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) request);
        ResponseWrapper wrappedResponse = new ResponseWrapper((HttpServletResponse) response);

        HttpSession session = wrappedRequest.getSession();

        logger.log(Level.FINE, "> Filtering: {0}", ((HttpServletRequest) request).getRequestURI());

        /*
         * use the ServletContext.log method to log filter messages
         */
        logger.log(Level.FINE, "doFilter called in: {0} on {1}", new Object[]{filterConfig.getFilterName(), new java.util.Date()});


        // log the session ID
        logger.log(Level.FINE, "session ID: {0}", session.getId());

        Enumeration<String> headers = wrappedRequest.getHeaders("cookie");
        while (headers.hasMoreElements()) {
            logger.log(Level.FINE, "header: {0}", headers.nextElement());
        }

        Credential credential = (Credential) session.getAttribute(SESSION_DACS_CREDENTIAL);
        if (credential == null) {
            try {
                DacsClientContext dacsClientContext = new DacsClientContext();
                FederationLoader federationLoader = new FederationLoader(DACS_BASE_URI, dacsClientContext);
                logger.log(Level.FINE, "loading federation from {0}", DACS_BASE_URI);
                Federation federation = federationLoader.getFederation();
                logger.log(Level.FINE, "loaded federation {0}", federation.getFederationName());
                Jurisdiction jurisdiction = federation.getJurisdictionByName(DACS_AUTH_JURISDICTION);
                logger.log(Level.FINE, "resolving user against jurisdiction {0} ({1})", new Object[]{jurisdiction.getJName(), jurisdiction.getDacsUri()});
                credential = DacsUtil.resolveUser(jurisdiction, wrappedRequest);
                if (credential == null) {
                    if (DACS_AUTH_REQUIRED) {
                        throw new DacsException("Failed to resolve DACS credentials found in request.");
                    }
                } else {
                    logger.log(Level.FINE, "resolved username as: {0}", credential);
                    session.setAttribute(SESSION_DACS_CREDENTIAL, credential);
                }
            } catch (DacsException ex) {
                sendProcessingError("Failed authenticating with DACS: " + ex.getMessage(), response);
            } catch (Exception ex) {
                sendProcessingError("Unknown error occured: " + ex.getMessage(), response);
            }
        } else {
            logger.log(Level.FINE, "found username in session as: {0}", credential);
        }

        Throwable problem = null;

        // chain the request/response
        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter 
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     * get the parameters to be used by DACS, get and store DACS Federation
     * information in ApplicationContext
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            DACS_BASE_URI = filterConfig.getInitParameter("dacs_base_uri");
            DACS_AUTH_JURISDICTION = filterConfig.getInitParameter("dacs_auth_jurisdiction");
            DACS_AUTH_REQUIRED = filterConfig.getInitParameter("dacs_auth_jurisdiction").equals("true");
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("DacsFilter()");
        }
        StringBuilder sb = new StringBuilder("DacsFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());

    }

    private void sendProcessingError(String error, ServletResponse response) {
        ServletOutputStream outputStream = null;
            PrintStream ps = null;
            PrintWriter pw = null;
        try {
            response.setContentType("text/plain");
            ps = new PrintStream(response.getOutputStream());
            pw = new PrintWriter(ps);
            pw.print(error);
            pw.close();
            ps.close();
            response.getOutputStream().close();

        } catch (Exception ex) {
            //TODO: catch this
        } finally {
            // TODO cleanup in here
        }


    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

    /**
     * This request wrapper class extends the support class HttpServletRequestWrapper,
     * which implements all the methods in the HttpServletRequest interface, as
     * delegations to the wrapped request. 
     * You only need to override the methods that you need to change.
     * You can get access to the wrapped request using the method getRequest()
     */
    class RequestWrapper extends HttpServletRequestWrapper {

        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }
        // You might, for example, wish to add a setParameter() method. To do this
        // you must also override the getParameter, getParameterValues, getParameterMap,
        // and getParameterNames methods.
        protected Hashtable localParams = null;

        public void setParameter(String name, String[] values) {
            logger.log(Level.FINE, "DacsFilter::setParameter({0}={1}" + ")" + " localParams = " + "{2}", new Object[]{name, values, localParams});

            if (localParams == null) {
                localParams = new Hashtable();
                // Copy the parameters from the underlying request.
                Map wrappedParams = getRequest().getParameterMap();
                Set keySet = wrappedParams.keySet();
                for (Iterator it = keySet.iterator(); it.hasNext();) {
                    Object key = it.next();
                    Object value = wrappedParams.get(key);
                    localParams.put(key, value);
                }
            }
            localParams.put(name, values);
        }

        @Override
        public String getParameter(String name) {
            logger.log(Level.FINE, "DacsFilter::getParameter({0}) localParams = {1}", new Object[]{name, localParams});
            if (localParams == null) {
                return getRequest().getParameter(name);
            }
            Object val = localParams.get(name);
            if (val instanceof String) {
                return (String) val;
            }
            if (val instanceof String[]) {
                String[] values = (String[]) val;
                return values[0];
            }
            return (val == null ? null : val.toString());
        }

        @Override
        public String[] getParameterValues(String name) {
            logger.log(Level.FINE, "DacsFilter::getParameterValues({0}) localParams = {1}", new Object[]{name, localParams});
            if (localParams == null) {
                return getRequest().getParameterValues(name);
            }
            return (String[]) localParams.get(name);
        }

        @Override
        public Enumeration getParameterNames() {
            logger.log(Level.FINE, "DacsFilter::getParameterNames() localParams = {0}", localParams);
            if (localParams == null) {
                return getRequest().getParameterNames();
            }
            return localParams.keys();
        }

        @Override
        public Map getParameterMap() {
            logger.log(Level.FINE, "DacsFilter::getParameterMap() localParams = {0}", localParams);
            if (localParams == null) {
                return getRequest().getParameterMap();
            }
            return localParams;
        }
    }

    /**
     * This response wrapper class extends the support class HttpServletResponseWrapper,
     * which implements all the methods in the HttpServletResponse interface, as
     * delegations to the wrapped response. 
     * You only need to override the methods that you need to change.
     * You can get access to the wrapped response using the method getResponse()
     */
    class ResponseWrapper extends HttpServletResponseWrapper {

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
        }
        // You might, for example, wish to know what cookies were set on the response
        // as it went throught the filter chain. Since HttpServletRequest doesn't
        // have a get cookies method, we will need to store them locally as they
        // are being set.
	/*
        protected Vector cookies = null;

        // Create a new method that doesn't exist in HttpServletResponse
        public Enumeration getCookies() {
        if (cookies == null)
        cookies = new Vector();
        return cookies.elements();
        }

        // Override this method from HttpServletResponse to keep track
        // of cookies locally as well as in the wrapped response.
        public void addCookie (Cookie cookie) {
        if (cookies == null)
        cookies = new Vector();
        cookies.add(cookie);
        ((HttpServletResponse)getResponse()).addCookie(cookie);
        }
         */
    }
}

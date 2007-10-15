/*
 * Jurisdiction.java
 *
 * Created on August 23, 2005, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs;
import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.http.DacsCookie;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.dacs.services.DacsAdminService;
import com.fedroot.dacs.services.DacsAdminService.ops;
import com.fedroot.dacs.services.DacsAuthAgentService;
import com.fedroot.dacs.services.DacsConfService;
import com.fedroot.dacs.services.DacsCurrentCredentialsService;
import com.fedroot.dacs.services.DacsSignoutService;
import com.fedroot.dacs.services.DacsVersionService;
import com.fedroot.dacs.xmlbeans.AclDocument;
import com.fedroot.dacs.xmlbeans.CommonStatusDocument;
import com.fedroot.dacs.xmlbeans.DacsAdminDocument;
import com.fedroot.dacs.xmlbeans.DacsAuthAgentDocument;
import com.fedroot.dacs.xmlbeans.DacsConfReplyDocument;
import com.fedroot.dacs.xmlbeans.DacsCurrentCredentialsDocument;
import com.fedroot.dacs.xmlbeans.DacsCurrentCredentialsDocument.DacsCurrentCredentials;
import com.fedroot.dacs.xmlbeans.DacsJurisdiction;
import com.fedroot.dacs.xmlbeans.RevocationDocument;
import com.fedroot.dacs.xmlbeans.UserAccountDocument;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

/**
 * data type for a DACS jurisdiction entity
 * @author rmorriso
 */
public class Jurisdiction {
    Federation federation;
    String name;
    String fullName;
    public String altname; // as defined in dacs_list_jurisdictions
    String dacsuri;
    public boolean authenticates;
    boolean prompts;
    List<Acl> acls;
    List<Revocation> revocations;
    List<DacsUserAccount> dacsusers;
    // Public constants for attributes supported by the XSLTC TransformerFactory.
    public final static String TRANSLET_NAME = "translet-name";
    public final static String DESTINATION_DIRECTORY = "destination-directory";
    public final static String PACKAGE_NAME = "package-name";
    public final static String JAR_NAME = "jar-name";
    public final static String GENERATE_TRANSLET = "generate-translet";
    public final static String AUTO_TRANSLET = "auto-translet";
    public final static String USE_CLASSPATH = "use-classpath";
    public final static String DEBUG = "debug";
    public final static String ENABLE_INLINING = "enable-inlining";
    public final static String INDENT_NUMBER = "indent-number";
    
    /**
     * constructor
     * @param dacsbaseuri points to base of DACS install at this jurisdiction
     */
    public Jurisdiction(String dacsbaseuri) {
        this.dacsuri = dacsbaseuri;
    }
    
    /**
     * create new Jurisdiction from DacsJurisdiction XmlBean
     * @param dacsjur the DacsJurisdiction XmlBean unmarshalled from the DACS dacs_list_jurisdictions service
     */
    public Jurisdiction(DacsJurisdiction dacsjur) {
        name = dacsjur.getJname();
        fullName = dacsjur.getName();
        setAltname(dacsjur.getAltName());
        dacsuri = dacsjur.getDacsUrl();
        setAuthenticates((dacsjur.getAuthenticates() == DacsJurisdiction.Authenticates.YES ? true : false));
        prompts = (dacsjur.getPrompts() == DacsJurisdiction.Prompts.YES ? true : false);
        acls = new ArrayList<Acl>();
        revocations = new ArrayList<Revocation>();
        dacsusers = new ArrayList<DacsUserAccount>();
    }
    
    /**
     * use this jurisdiction's dacs_current_credentials service to
     * retrieve current credentials present in @para dacscontext;
     * Note: this <em>does not</em> return just the credentials originating
     * with this jurisdiction.
     * @param dacscontext dacscontext to check
     * @return list of Credentials
     * @throws java.lang.Exception TODO
     */
    public List<Credentials> currentCredentials(DacsContext dacscontext)
    throws Exception {
        DacsCurrentCredentialsDocument dacsCurrentCredentialsDocument = null;
        int httpstatus;
        List<Credentials> credentials = new ArrayList<Credentials>();
        try {
            DacsCurrentCredentialsService dacsservice =
                    new DacsCurrentCredentialsService(this.dacsuri);
            DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
            httpstatus = dacscontext.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) {
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                dacsget.releaseConnection();
                // Check that it is an instance of the DacsCurrentCredentialsDocument
                if(expectedXmlObject instanceof DacsCurrentCredentialsDocument){
                    // DacsCurrentCredentialsDocument dacscredsdoc = DacsCurrentCredentialsDocument.Factory.parse(xmlstring);
                    dacsCurrentCredentialsDocument =
                            (DacsCurrentCredentialsDocument)expectedXmlObject;
                    List <DacsCurrentCredentialsDocument.DacsCurrentCredentials.Credentials> dacscredsList =
                            dacsCurrentCredentialsDocument.getDacsCurrentCredentials().getCredentialsList();
                    for(DacsCurrentCredentialsDocument.DacsCurrentCredentials.Credentials dacscreds : dacscredsList) {
                        Credentials creds =
                                new Credentials(
                                this.federation,
                                this.federation.getJurisdictionByName(dacscreds.getJurisdiction().toString()),
                                dacscreds);
                        credentials.add(creds);
                    }
                    return credentials;
                } else {
                    throw new Exception("Jurisdiction currentCredentials: invalid DacsCurrentCredentialsDocument");
                }
            }  else {
                throw new Exception("Jurisdiction currentCredentials: returned HTTP status " + httpstatus);
            }
        } catch (IOException e){
            System.out.println("IOException : " + e.getLocalizedMessage());
            throw e;
        } catch (XmlException e){
            System.out.println("XmlException: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * use this jurisdiction to resolve the Username in a given DACS cookie
     * @param jcookie a Sun javax.servlet.http.Cookie extracted from the HTTP request
     * @return DACS username defined in jcookie
     * @throws java.lang.Exception TODO
     */
    public String resolveUsername(javax.servlet.http.Cookie jcookie)
    throws DacsException, Exception {
        if (! DacsCookie.isDacsCookie(jcookie)) return null;
        DacsCurrentCredentialsDocument dacsCurrentCredentialsDocument = null;
        int httpstatus;
        List<Credentials> credentials = new ArrayList<Credentials>();
        // get an empty DacsContext
        DacsContext dacscontext = new DacsContext();
        dacscontext.addDacsCookie(this.getFederation(), jcookie);
        try {
            DacsCurrentCredentialsService dacsservice =
                    new DacsCurrentCredentialsService(this.dacsuri);
            DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
            httpstatus = dacscontext.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) {
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                dacsget.releaseConnection();
                // Check that it is an instance of the DacsCurrentCredentialsDocument
                if(expectedXmlObject instanceof DacsCurrentCredentialsDocument){
                    // DacsCurrentCredentialsDocument dacscredsdoc = DacsCurrentCredentialsDocument.Factory.parse(xmlstring);
                    dacsCurrentCredentialsDocument =
                            (DacsCurrentCredentialsDocument)expectedXmlObject;
                    List <DacsCurrentCredentialsDocument.DacsCurrentCredentials.Credentials> dacscredsList =
                            dacsCurrentCredentialsDocument.getDacsCurrentCredentials().getCredentialsList();
                    for(DacsCurrentCredentialsDocument.DacsCurrentCredentials.Credentials dacscreds : dacscredsList) {
                        Credentials creds =
                                new Credentials(
                                this.federation,
                                this.federation.getJurisdictionByName(dacscreds.getJurisdiction().toString()),
                                dacscreds);
                        credentials.add(creds);
                    }
                    if (credentials.size() == 1) {
                        Credentials cred = credentials.get(0);
                        return cred.getName();
                    } else {
                        return null;
                    }
                    
                } else {
                    throw new DacsException("Jurisdiction currentCredentials: invalid DacsCurrentCredentialsDocument");
                }
            }  else {
                throw new DacsException("Jurisdiction currentCredentials: returned HTTP status " + httpstatus);
            }
        } catch (IOException e){
            System.out.println("IOException : " + e.getLocalizedMessage());
            throw e;
        } catch (XmlException e){
            System.out.println("XmlException: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * signout (invalidate) all credentials in this jurisdiction;
     * calls DacsSignoutService under this jurisdiction
     * the DACS baseuri and jurisdiction name
     * @param dacscontext this DacsContext within which to execute the signout
     * @return the number of federation credentials remaining in dacscontext (other jurisdictions)
     * @throws java.lang.Exception TODO
     */
    public int signout(DacsContext dacscontext)
    throws Exception {
        int httpstatus;
        DacsSignoutService dacsservice =
                new DacsSignoutService(this.getDacsUri(), this.name);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        httpstatus = dacscontext.executeMethod(dacsget);
        if (httpstatus == HttpStatus.SC_OK) {
            // Parse xmlstring from httpget response
            XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
            dacsget.releaseConnection();
            // Check that it is an instance of the DacsCurrentCredentialsDocument
            if(expectedXmlObject instanceof DacsCurrentCredentialsDocument){
                DacsCurrentCredentialsDocument dacsdoc = (DacsCurrentCredentialsDocument)expectedXmlObject;
                DacsCurrentCredentialsDocument.DacsCurrentCredentials dacscurrentcreds =
                        dacsdoc.getDacsCurrentCredentials();
                List <DacsCurrentCredentialsDocument.DacsCurrentCredentials.Credentials> dacscreds = dacscurrentcreds.getCredentialsList();
                return dacscurrentcreds.sizeOfCredentialsArray();
            } else {
                throw new Exception("Jurisdiction signout: invalid DacsCurrentCredentialsDocument");
            }
        } else {
            throw new Exception("Jurisdiction signout: returned HTTP status " + httpstatus);
        }
    }
    
    /**
     * signout (invalidate) credentials in this jurisdiction's federation;
     * calls DacsSignoutService on this jurisdiction's dacsbaseuri
     * @param dacscontext the DacsContext within which to execute the signout
     * @return the number of federation credentials remaining after signout - should be 0
     * @throws java.lang.Exception TODO
     */
    public int signoutFederation(DacsContext dacscontext)
    throws Exception {
        int httpstatus;
        DacsSignoutService dacsservice =
                new DacsSignoutService(this.getDacsUri());
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        httpstatus = dacscontext.executeMethod(dacsget);
        if (httpstatus == HttpStatus.SC_OK) {
            // Parse xmlstring from httpget response
            XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
            dacsget.releaseConnection();
            // Check that it is an instance of the DacsAuthReplyDocument
            if(expectedXmlObject instanceof DacsCurrentCredentialsDocument){
                DacsCurrentCredentialsDocument dacsdoc = (DacsCurrentCredentialsDocument)expectedXmlObject;
                DacsCurrentCredentialsDocument.DacsCurrentCredentials dacscurrentcreds =
                        dacsdoc.getDacsCurrentCredentials();
                if (dacscurrentcreds.sizeOfCredentialsArray() > 0) {
                    throw new Exception("Jurisdiction sigoutFederation: returned non-empty current_credentials");
                }
                return 0;
            } else {
                throw new Exception("Jurisdiction signoutFederation: invalid DacsCurrentCredentialsDocument");
            }
        } else {
            throw new Exception("Jurisdiction signoutFederation: returned HTTP status " + httpstatus);
        }
    }
    
    /**
     * getter for acls instance variable
     * acls is initially empty and is only populated by an explicit call
     * to loadAcls
     * @return list of cached Acl objects previously populated from dacs_admin XML reply
     */
    public List<Acl> getAcls() {
        return this.acls;
    }
    
    /**
     * load jurisdictions ACLs;
     * calls DacsAdmin service to get jurisdiction's ACLs, inserting them in
     * list of ACLs
     * @param dacscontext invoke DacsAdminService/acls with respect to this DacsContext
     * Precondition: ADMIN_IDENTITY credentials are present in @param dacscontext
     * @throws java.lang.Exception TODO
     * @return list of Acl objects constructed from dacs_admin XML reply
     */
    public List<Acl> loadAcls(DacsContext dacscontext)
    throws Exception {
        try {
            DacsAdminService dacsservice =
                    new DacsAdminService(dacsuri, ops.acls);
            DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
            int httpstatus = dacscontext.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) {
                // Parse xmlstring from httpget response
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                dacsget.releaseConnection();
                // Check that it is an instance of the DacsAdminDocument
                if(expectedXmlObject instanceof DacsAdminDocument){
                    DacsAdminDocument dacsadmindoc = (DacsAdminDocument)expectedXmlObject;
                    //String federation = dacsadmindoc.getDacsCurrentCredentials().getFederationName().getStringValue();
                    List <com.fedroot.dacs.xmlbeans.AclHeaderDocument.AclHeader> aclheaders =
                            dacsadmindoc.getDacsAdmin().getAcls().getAclHeaderList();
                    for(com.fedroot.dacs.xmlbeans.AclHeaderDocument.AclHeader aclheader : aclheaders) {
                        Acl acl = new Acl(aclheader.getName().toString(),"yes");
                        acls.add(acl);
                    }
                    return this.acls;
                } else {
                    throw new Exception("invalid DacsAdminDocument");
                }
            } else {
                throw new Exception("Jurisdiction loadAcls: returned HTTP status " + httpstatus);
            }
        } catch (IOException e){
            System.out.println("IOException : " + e.getLocalizedMessage());
            throw e;
        } catch (XmlException e){
            System.out.println("XmlException: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * retrieve Acl by its name
     * @param dacscontext invoke DacsAdminService with respect to this DacsContext
     * @param name the ACL name
     * @return Acl object named by @param name
     * @throws java.lang.Exception TODO
     */
    public Acl getAclByName(DacsContext dacscontext, String name)
    throws Exception {
        try {
            DacsAdminService dacsservice =
                    new DacsAdminService(dacsuri, ops.acls);
            DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
            int httpstatus = dacscontext.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) {
                // Parse xmlstring from httpget response
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                dacsget.releaseConnection();
                // Check that it is an instance of the DacsAdminDocument
                if(expectedXmlObject instanceof DacsAdminDocument){
                    DacsAdminDocument dacsadmindoc = (DacsAdminDocument) expectedXmlObject;
                    AclDocument.Acl dacsacl =
                            dacsadmindoc.getDacsAdmin().getAcl();
                    return new Acl(dacsacl.getAclHeader().toString(), "yes");
                } else {
                    throw new Exception("Error in DacsCurrentCredentials");
                    // TODO: figure out the best way to blow up
                }
            } else {
                throw new Exception("dacscontext getAcl: returned HTTP status " + httpstatus);
            }
        } catch (IOException e){
            System.out.println("IOException : " + e.getLocalizedMessage());
            throw e;
        } catch (XmlException e){
            System.out.println("XmlException: " + e.getMessage());
            throw e;
        }
    }
    
    
    /**
     * lazy getter for jurisdiction configuration;
     * calls DacsConfService to load juridiction configuration if cache expired
     * PRECONDITION: credentials for ADMIN_IDENTITY present in dacscontext
     * @param dacscontext invoke DacsConfService with respect to this dacscontext
     * @throws java.lang.Exception TODO
     * @return JurisdictionConfiguation object constructed from dacs_conf XML reply
     */
    public JurisdictionConfiguration getConfiguration(DacsContext dacscontext)
    throws DacsException, Exception {
//        try {
        DacsConfService dacsservice =
                new DacsConfService(this.dacsuri);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        // execute in CHECK_FAIL mode
        int dacsstatus = dacscontext.executeCheckFailMethod(dacsget);
        if (dacsstatus == DacsStatus.SC_DACS_ACCESS_GRANTED) {
            // Parse httpget response
            XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
            dacsget.releaseConnection();
            // Check that it is an instance of the DacsConfDocument
            if(expectedXmlObject instanceof DacsConfReplyDocument) {
                return new JurisdictionConfiguration((DacsConfReplyDocument)expectedXmlObject);
            } else if (expectedXmlObject instanceof CommonStatusDocument) {
                CommonStatusDocument statdoc = (CommonStatusDocument) expectedXmlObject;
                CommonStatusDocument.CommonStatus status = statdoc.getCommonStatus();
                throw new DacsException("Jurisdiction loadConfiguration: " + status.getMessage());
            } else {
                throw new DacsException("Jurisdiction loadConfiguration: invalid DacsConfConfReply");
            }
        } else if (dacsstatus == DacsStatus.SC_DACS_ACCESS_DENIED) {
            dacsget.releaseConnection();
            throw new DacsException("loadConfiguration: access denied");
        } else {
            dacsget.releaseConnection();
            throw new DacsException("loadConfiguration: returned DACS status " + dacsstatus);
        }
    }
    
    /**
     * getter for jurisdiction configuration;
     * uses XSLT to generate HTML from XML reply of DacsConfService
     * PRECONDITION: credentials for ADMIN_IDENTITY present in dacscontext
     * @param dacscontext invoke DacsConfService with respect to this dacscontext
     * @throws com.fedroot.dacs.DacsException TODO
     * @return HTML representation translated from DacsConfService XML reply
     */
    public String getConfigurationHtml(DacsContext dacscontext)
    throws DacsException {
        
        DacsConfService dacsservice =
                new DacsConfService(this.dacsuri);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        // execute in CHECK_FAIL mode
        int dacsstatus = dacscontext.executeCheckFailMethod(dacsget);
        if (dacsstatus == DacsStatus.SC_DACS_ACCESS_GRANTED) {
            // generated translet classfile to use for transformation
            String translet = "DacsConfXslt";
            
            try {
                TransformerFactory transfact = TransformerFactory.newInstance();
                
                transfact.setAttribute(USE_CLASSPATH , true);
                // look in this package for the translet class
                transfact.setAttribute(PACKAGE_NAME, "com.fedroot.dacs.xsl");
                
                // set the translet in the transformer
                Transformer transformer = transfact.newTransformer(new StreamSource(translet));
                System.out.println(dacsget.getResponseBodyAsString());
                StreamSource xmlsource = new StreamSource(dacsget.getResponseBodyAsStream());
                
                ByteArrayOutputStream outstrm = new ByteArrayOutputStream();
                
                StreamResult outdoc = new StreamResult(outstrm);
                
                transformer.transform(xmlsource, outdoc);
                
                return outstrm.toString();
            } catch (Exception e) {
                throw new DacsException("exception applying XSL transformation: " + e.getMessage());
            } finally {
                dacsget.releaseConnection();
            }
        } else if (dacsstatus == DacsStatus.SC_DACS_ACCESS_DENIED) {
            throw new DacsException("DacsConfService: access denied");
        } else {
            throw new DacsException("DacsConfService: returned DACS status " + dacsstatus);
        }
    }
    
    /**
     * getter for jurisdiction version information;
     * uses XSLT to generate HTML from XML reply of DacsVersionService
     * PRECONDITION: credentials for ADMIN_IDENTITY present in dacscontext
     * @param dacscontext invoke DacsVersionService with respect to this dacscontext
     * @throws com.fedroot.dacs.DacsException TODO
     * @return HTML representation translated from DacsVersionService XML reply
     */
    public String getVersionHtml(DacsContext dacscontext)
    throws DacsException {
        DacsVersionService dacsservice =
                new DacsVersionService(this.dacsuri);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        // execute in CHECK_FAIL mode
        int dacsstatus = dacscontext.executeCheckFailMethod(dacsget);
        if (dacsstatus == DacsStatus.SC_DACS_ACCESS_GRANTED) {
            // generated translet classfile to use for transformation
            String translet = "DacsVersionXslt";
            try {
                TransformerFactory transfact = TransformerFactory.newInstance();
                
                transfact.setAttribute(USE_CLASSPATH , true);
                // look in this package for the translet class
                transfact.setAttribute(PACKAGE_NAME, "com.fedroot.dacs.xsl");
                
                // set the translet in the transformer
                Transformer transformer = transfact.newTransformer(new StreamSource(translet));
                System.out.println(dacsget.getResponseBodyAsString());
                StreamSource xmlsource = new StreamSource(dacsget.getResponseBodyAsStream());
                
                ByteArrayOutputStream outstrm = new ByteArrayOutputStream();
                
                StreamResult outdoc = new StreamResult(outstrm);
                
                transformer.transform(xmlsource, outdoc);
                
                return outstrm.toString();
            } catch (Exception e) {
                throw new DacsException("exception applying XSL transformation: " + e.getMessage());
            } finally {
                dacsget.releaseConnection();
            }
        } else if (dacsstatus == DacsStatus.SC_DACS_ACCESS_DENIED) {
            throw new DacsException("DacsVersionService: access denied");
        } else {
            throw new DacsException("DacsVersionService: returned DACS status " + dacsstatus);
        }
    }
    
    /**
     * getter for Jurisdiction.revocations
     * revocations is initially empty and is only populated by an explicit call
     * to loadRevocations()
     * @return list of Revocation objects constructed from DacsAdminService/revocations
     */
    
    public List<Revocation> getRevocations() {
        return this.revocations;
    }
    
    /**
     * load revocations in effect at this jurisdiction;
     * calls DacsAdminService/revocations to populate Revocations list
     * @param dacscontext invoke DacsAdminService with respect to this dacscontext
     * @throws java.lang.Exception TODO
     * @return list of Revocation objects constructed from dacs_admin/revocations XML reply
     */
    public List<Revocation> loadRevocations(DacsContext dacscontext)
    throws Exception {
        try {
            DacsAdminService dacsservice =
                    new DacsAdminService(this.dacsuri, ops.revocations);
            DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
            int httpstatus = dacscontext.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) {
                // Parse httpget response
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                dacsget.releaseConnection();
                // Check that it is an instance of the DacsAdminDocument
                if(expectedXmlObject instanceof DacsAdminDocument){
                    DacsAdminDocument dacsadmindoc = (DacsAdminDocument)expectedXmlObject;
                    List<RevocationDocument.Revocation> dacsrevocs =
                            dacsadmindoc.getDacsAdmin().getRevocations().getRevocationList();
                    for(RevocationDocument.Revocation dacsrevoc : dacsrevocs) {
                        Revocation revoc = new Revocation(dacsrevoc.getItem(), dacsrevoc.getType().toString());
                        this.revocations.add(revoc);
                    }
                    return this.revocations;
                } else {
                    throw new Exception("Error in Revocations");
                    // TODO: figure out the best way to blow up
                }
            } else {
                throw new Exception("dacscontext authenticate: returned HTTP status " + httpstatus);
            }
        } catch (IOException e){
            System.out.println("IOException : " + e.getLocalizedMessage());
            throw e;
        } catch (XmlException e){
            System.out.println("XmlException: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * getter for Jurisdiction.dacsusers
     * dacsusers is initially empty and is only populated by an explicit call
     * to getDacsUserAccount()
     * 
     * 
     * 
     * @return cached list of DacsUserAccount objects constructed from DacsAdminService/user
     */
    
    public DacsUserAccount getDacsUserAccount(DacsContext dacscontext, String username) {
        try {
        List<DacsUserAccount> useraccounts = getDacsUserAccounts(dacscontext);
        for (DacsUserAccount user : useraccounts) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
        } catch (Exception e) { // RNM - TODO fix this catch 
            return null;
        }
    }
    
    /**
     * load local DACS user account records (<em>not</em> 3rd party accounts like ADS or MySQL)
     * @param dacscontext invoke DacsAdminService with respect to this dacscontext
     * @throws java.lang.Exception TODO
     * @return list of DacsUserAccount objects contructed from dacs_admin/users XML reply
     */
    public List<DacsUserAccount> getDacsUserAccounts(DacsContext dacscontext)
    throws Exception {
        try {
            DacsAdminService dacsservice =
                    new DacsAdminService(dacsuri, ops.users);
            DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
            int httpstatus = dacscontext.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) {
                // Parse dacsget response
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                dacsget.releaseConnection();
                // Check that it is an instance of the DacsAdminDocument
                if(expectedXmlObject instanceof DacsAdminDocument){
                    DacsAdminDocument dacsadmindoc = (DacsAdminDocument) expectedXmlObject;
                    List <UserAccountDocument.UserAccount> dacsusers =
                            dacsadmindoc.getDacsAdmin().getUserAccounts().getUserAccountList();
                    for(UserAccountDocument.UserAccount dacsuser : dacsusers) {
                        DacsUserAccount user = new DacsUserAccount(this.federation, this, dacsuser.getName().toString(),"yes");
                        this.dacsusers.add(user);
                    }
                    return this.dacsusers;
                } else {
                    throw new Exception("invalid DacsAdminDocument");
                }
            } else {
                throw new Exception("DacsAdminService: returned HTTP status " + httpstatus);
            }
        } catch (IOException e){
            System.out.println("IOException : " + e.getLocalizedMessage());
            throw e;
        } catch (XmlException e){
            System.out.println("XmlException: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * get a DacsUserAccount without authentication (DANGEROUS! this is like /bin/su)
     * PRECONDITION: dacscontext must contain credentials that have been
     * granted access to dacs_auth_agent
     * @param dacscontext invoke DacsAuthAgentService with respect to this dacscontext
     * @param username name of account
     * @throws java.lang.Exception TODO
     * @return authenticated DacsUserAccount
     */
    public DacsUserAccount getAuthenticatedUser(DacsContext dacscontext, String username)
    throws Exception {
        DacsUserAccount authuser = null;
        DacsAuthAgentService dacsservice =
                new DacsAuthAgentService(this.dacsuri, this.name, username);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        int httpstatus = dacscontext.executeMethod(dacsget);
        if (httpstatus == HttpStatus.SC_OK) {
            
            // Parse dacsget response
            XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
            dacsget.releaseConnection();
            // Check that it is an instance of the DacsAuthAgentDocument
            if(expectedXmlObject instanceof DacsAuthAgentDocument){
                DacsAuthAgentDocument dacsauthagentdoc = (DacsAuthAgentDocument) expectedXmlObject;
                if (dacsauthagentdoc.getDacsAuthAgent().isSetCommonStatus()) {
                    throw new Exception("DacsAuthAgentService: " + dacsauthagentdoc.getDacsAuthAgent().getCommonStatus().getMessage());
                } else {
                    DacsCurrentCredentials dacscredentials =
                            dacsauthagentdoc.getDacsAuthAgent().getDacsCurrentCredentials();
                    if (dacscredentials.sizeOfCredentialsArray() > 0) {
                        authuser = new DacsUserAccount(this.federation, this, dacscredentials.getCredentialsArray(0).getName().toString(), "yes");
                    }
                    return authuser;
                }
            } else {
                throw new Exception("invalid DacsAuthAgentDocument");
            }
        } else {
            throw new Exception("DacsAuthAgentService: returned HTTP status " + httpstatus);
        }
    }
    
    
    /**
     * getter for Jurisdiction.federation
     * @return the federation for this jurisdiction
     */
    public Federation getFederation() {
        return federation;
    }
    
    /**
     * setter for Jurisdiction.federation
     * Note: this method should not exist. Jurisdiction should only
     * be set at the time of instantiation. It should not be possible to change a
     * jurisdiction's federation
     * @param federation federation to set
     */
    public void setFederation(Federation federation) {
        this.federation = federation;
    }
    
    /**
     * wrapper for dacs_admin/revocations service
     * TODO: this simply updates the cached list - needs
     * to push revocations through to DACS via call to DacsAdminService
     * @param revocations set revocations in this jurisdiction
     */
    public void setRevocations(List<Revocation> revocations) {
        this.revocations = revocations;
    }
    
    /**
     * get length of revocations list
     * @return size of revocations list
     */
    public int sizeOfRevocations() {
        return revocations.size();
    }
    
    /**
     * getter for Jurisdiction.name
     * @return jurisdiction name
     */
    public String getName() {
        return name;
    }
        
    /**
     * getter for Jurisdiction.dacsuri
     * @return uri for the base of the DACS install under this jurisdiction
     */
    public String getDacsUri() {
        return dacsuri;
    }
    
    /**
     * standard toString() operator
     * @return string representation of this jurisdiction
     */
    public String toString() {
        return "Jurisdiction: " + name + "(" + getAltname() + "), " + dacsuri;
    }
    
    public String getAltname() {
        return altname;
    }
    
    public void setAltname(String altname) {
        this.altname = altname;
    }
    
    public boolean isAuthenticates() {
        return authenticates;
    }
    
    public void setAuthenticates(boolean authenticates) {
        this.authenticates = authenticates;
    }
}

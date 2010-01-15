/*
 * JurisdictionConfiguration.java
 *
 * Created on October 24, 2005, 3:37 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;

import com.fedroot.dacs.xmlbeans.AuthDocument;
import com.fedroot.dacs.xmlbeans.DacsConfReplyDocument;
import com.fedroot.dacs.xmlbeans.DirectiveDocument;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * abstract data type for reply from dacs_conf web service
 * @author rmorriso
 */
public class JurisdictionConfiguration {
    String dacsuri;
    List<Directive> directives;
    List<AuthConfig> authconfigs;
    List<RolesConfig> rolesconfigs;
    
    /**
     * constructor
     * @param dacsconfdoc the XMLBean from DacsConfReply
     */
    public JurisdictionConfiguration(DacsConfReplyDocument dacsconfdoc) {
        DacsConfReplyDocument.DacsConfReply dacsconf = dacsconfdoc.getDacsConfReply();
        this.dacsuri = dacsconf.getUri();
        this.directives = new ArrayList<Directive>();
        this.authconfigs = new ArrayList<AuthConfig>();
        List <DirectiveDocument.Directive> directivelist = dacsconf.getDirectiveList();
        for(DirectiveDocument.Directive directive : directivelist) {
            directives.add(
                    new Directive(directive.getName().toString(),
                    directive.getValue().toString()));
        }
        List <AuthDocument.Auth> authlist = dacsconf.getAuthList();

        for(AuthDocument.Auth auth : authlist) {
            List<Directive> authdirectives = new ArrayList<Directive>();
            List <DirectiveDocument.Directive> authdirectivearray = auth.getDirectiveList();
            for (DirectiveDocument.Directive authdirective : authdirectivearray) {
                authdirectives.add(
                        new Directive(authdirective.getName().toString(),
                        authdirective.getValue().toString()));
            }
            authconfigs.add(
                    new AuthConfig(auth.getId().toString(), authdirectives));
        }
        // skip Roles configuration for now - we have a name clash in the XML
        // schema that's biting us ...
//                    List<RolesDocument.Roles> rolesarray = dacsconf.getRolesList();
        List<RolesConfig> rolesconfigs = new ArrayList<RolesConfig>();
//                    List<Directive> rolesdirectives = new ArrayList<Directive>();
//                    for(RolesDocument.Roles rolesdirective : rolesarray) {
//                        List <DirectiveDocument.Directive> rolesdirectivearray = rolesdirective.getDirectiveList();
//                        for (DirectiveDocument.Directive roledirective: rolesdirectivearray) {
//                            rolesdirectives.add(
//                               new Directive(roledirective.getName().toString(),
//                                    roledirective.getValue().toString()));
//                        }
//                        rolesconfigs.add(
//                           new RolesConfig(rolesdirective.getId().toString(), rolesdirectives));
//                    }
//                    this.configuration =
//                       new JurisdictionConfiguration(
//                            dacsconf.getUri().toString(),
//                            directives,
//                            authconfigs,
//                            rolesconfigs);
        
    }
    
    /**
     * constructor
     * @param dacsuri identifies the jurisdiction to which this configuration applies
     * @param directives list of generic directives
     * @param authconfigs list of auth configuration sections
     * @param rolesconfig list of role configuration sections
     */
    public JurisdictionConfiguration(String dacsuri, List<Directive> directives, List<AuthConfig> authconfigs, List<RolesConfig> rolesconfig) {
        this.dacsuri = dacsuri;
        this.directives = directives;
        this.authconfigs = authconfigs;
        this.rolesconfigs = rolesconfigs;
    }
    
    /**
     * standard toString() operator
     * @return string representation of JurisdictionConfiguration
     */
    public String toString() {
        return "Jurisdiction Configuration: " +
                "uri[" + this.dacsuri + "]" +
                "Directives" +
                this.directives.toString() +
                "AuthConfig" +
                this.authconfigs.toString() +
                "RolesConfig";
        // + this.rolesconfigs.toString();
    }
    /**
     * print JurisdictionConfiguration to out
     * @param out PrintStream to print to
     */
    public void dumpConfiguration(PrintStream out) {
        out.println("<pre>");
        // Display the jurisdiction configuration
        out.println("Jurisdiction Configuration: ");
        out.println("uri[" + this.dacsuri + "]");
        out.println("Directives");
        out.println(this.directives.toString());
        out.println("AuthConfig");
        out.println(this.authconfigs == null ? "empty" : this.authconfigs.toString());
        out.println("RolesConfig");
        out.println(this.rolesconfigs == null ? "empty" : this.rolesconfigs.toString());
        out.println("</pre>");
    }
    
    /**
     * print JurisdictionConfiguration to out
     * @param out PrintWriter to print to
     */
    public void dumpConfiguration(PrintWriter out) {
        out.println("<pre>");
        // Display the jurisdiction configuration
        out.println("Jurisdiction Configuration: ");
        out.println("uri[" + this.dacsuri + "]");
        out.println("AuthConfig");
        out.println(this.authconfigs == null ? "empty" : this.authconfigs.toString());
        out.println("RolesConfig");
        out.println(this.rolesconfigs == null ? "empty" : this.rolesconfigs.toString());
        out.println("</pre>");
    }
}

/*
 * DacsLoginForm.java
 *
 * Created on February 3, 2006, 9:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.fedroot.dacs.html;

import com.fedroot.dacs.Federation;
import com.fedroot.dacs.Jurisdiction;

/**
 *
 * @author rmorriso
 */
public class DacsLoginForm {
    private String dacsuri;
    private Federation federation;
    /** Creates a new instance of DacsLoginForm */
    public DacsLoginForm() {
        dacsuri = "https://demo.fedroot.com/fedadmin/dacs";
    }
    
    public String getXhtml() {
        String result;
        result = "<form name=\"jurisdiction_form\" METHOD=\"POST\" ACTION=\"\" onSubmit=\"jurisdiction_form_handler()\">";
        result += "<table><tr>";
        result += "<td width=\"30%\"><font size=\"+1\">";
        result += "<B>Select Home Jurisdiction</B></font></td>";
        result += "<td width=\"70%\">";
        result += "<select name=\"DACS_JURISDICTION\" size=\"1\">";
        result += "<option value=\"\" SELECTED>-&nbsp;-&nbsp;-&nbsp;-&nbsp;-&nbsp;-</option>";
        for (Jurisdiction jur : this.federation.getJurisdictions()) {
        result += "<option value=\"" + jur.getDacsUri() + "@" + jur.getName() + ">" + 
                jur.getName() + "</option>";
        result += "<option value=\"https://dss.demo.fedroot.com/cgi-bin/dacs@DSS\">DSS</option>";
        result += "<option value=\"https://demo.fedroot.com/metalogic/dacs@METALOGIC\">METALOGIC</option>";

        result += "</select></td></tr>";
        }
        result += "<tr><td width=\"30%\"><font size=\"+1\"><B>Username</B></font></td>";
        result += "<td width=\"70%\"><input type=\"TEXT\" size=20 name=\"USERNAME\"/></td></tr>";
        result += "<tr><td width=\"30%\"><font size=\"+1\"><B>Password</B></font></td>";
        result += "<td width=\"70%\"><input type=\"PASSWORD\" size=20 name=\"PASSWORD\"/></td>";
        result += "</tr>";
        result += "<tr><td width=\"30%\" ALIGN=\"LEFT\"><input type=\"submit\" value=\" Submit \"/></td>";
        result += "<td ALIGN=\"LEFT\"><input type=\"reset\" value=\" Reset all values \"/></td>";
        result += "</tr>";
        result += "<\table>";
        result += "</form>";
        return result;
    }
    
}

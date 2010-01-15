<xsl:stylesheet version = '1.0' 
  xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
  xmlns:dacs="http://fedroot.com/dacs/v1.4">

<xsl:output method="html"/>
<xsl:template match="dacs:dacs_conf_reply"> 
<table> 
<tr><td><h3>General Directives</h3></td></tr>
  <xsl:for-each select="//dacs:directive"> 
  	<tr><xsl:call-template name="print-directive"/></tr>
  </xsl:for-each> 
</table>
<table>
  <xsl:for-each select="//dacs:Auth">
    <tr><td><h3>Auth Directives: <i><xsl:value-of select="@id"/></i></h3> </td></tr>
        <xsl:for-each select="//dacs:Auth/dacs:directive">
           <tr><xsl:call-template name="print-directive"/></tr>
        </xsl:for-each>
  </xsl:for-each>
</table> 
  
  <table>
    <xsl:for-each select="//dacs:Roles">
      <tr><td><h3>Roles Directives: <i><xsl:value-of select="@id"/></i></h3></td></tr>
      <xsl:for-each select="//dacs:Roles/dacs:directive">
        <tr><xsl:call-template name="print-directive"/></tr>
      </xsl:for-each>
    </xsl:for-each>
  </table> 
  
</xsl:template>

<xsl:template name="print-directive">      
  <td><font size="-2"><b><xsl:value-of select="@name"/></b></font></td>
  <td><font size="-2"><xsl:value-of select="@value"/></font></td>
</xsl:template>
</xsl:stylesheet>




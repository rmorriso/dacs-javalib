<xsl:stylesheet version = '1.0' 
  xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
  xmlns:dacs="http://fedroot.com/dacs/v1.4">

<xsl:output method="html"/>
<xsl:template match="dacs:dacs_version"> 
<table> 
<tr><td><b><i>number:</i></b></td>
<td><xsl:value-of select="@number"/></td></tr>
<tr><td><b><i>release:</i></b></td>
<td><xsl:value-of select="@release"/></td></tr>
<tr><td><b><i>date:</i></b></td>
<td><xsl:value-of select="@date"/></td></tr>
<tr><td><b><i>revid:</i></b></td>
<td><xsl:value-of select="@revid"/></td></tr>
<tr><td><b><i>other:</i></b></td>
<td><xsl:value-of select="@other"/></td></tr>
</table>

<h3>File Versions</h3>
<table>
  <xsl:for-each select="//dacs:file"> 
  	<tr><xsl:call-template name="print-file"/></tr>
  </xsl:for-each> 
</table>
</xsl:template>

<xsl:template name="print-file">      
  <td><b>file:</b></td>
  <td><font size="-2"><b><xsl:value-of select="@revid"/></b></font></td>
</xsl:template>

</xsl:stylesheet>



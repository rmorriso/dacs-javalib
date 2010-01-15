<html xmlns="http://www.w3.org/1999/xhtml">

<%@ page import = "com.fedroot.jsptaglib.Constants"%>
<%@ page import = "java.util.Properties"%>

<%! Properties properties;%>
<%! String pageName;%>
<%! String noLangButton;%>

<%  pageName = request.getParameter(Constants.PAGE_NAME);
	noLangButton = request.getParameter(Constants.NO_LANG_BUTTON);
	
	properties = (Properties)session.getAttribute(Constants.TEXT);

	// set default values for image and alt tag
	String titleImage = properties.getProperty("title.admin.header");
	String alt_title  = properties.getProperty("alt.title.admin.header");

	String imageType = request.getParameter("image");

	// change defaults if needed
	if (imageType != null) {
		if (imageType.equals("login")) {
			titleImage = properties.getProperty("title.login.header");
			alt_title = properties.getProperty("alt.title.login.header");;
		} else if (imageType.equals("exception")) {
			titleImage = properties.getProperty("title.exception.header");
			alt_title = properties.getProperty("alt.title.exception.header");;
		} else if (imageType.equals("logout")){
			titleImage = properties.getProperty("title.logout.header");
			alt_title = properties.getProperty("alt.title.logout.header");;
			}
	}	
%>

  <head>
    <title><%= alt_title %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="css/nfis_access.css" rel="stylesheet" type="text/css" />
    <link href="/css/test/test.css" rel="stylesheet" type="text/css" />
  </head>

  <body bgcolor="#ffffff" leftmargin="0"
  topmargin="0" marginwidth="0" marginheight="0">
          <table width="600" border="0" cellpadding="0" cellspacing="0" 
          align="center" id="content" summary="page_column">
			<!-- Initial columns to define widths and subsequent span -->
            <tr>
              <td width="140" height="1">
                <img src="images/space.gif" width="140"
                height="1" />
              </td>
              <td width="25" height="1">
                <img src="images/space.gif" width="25"
                height="1" />
              </td>
              <td width="115" height="1">
                <img src="images/space.gif" width="115"
                height="1" />
              </td>
              <td width="260" height="1">
                <img src="images/space.gif" width="260"
                height="1" />
              </td>
              <td width="60" height="1">
                <img src="images/space.gif" width="60"
                height="1" />
              </td>
            </tr>
            <tr>
              <td width="280" height="53" colspan="3">
                <a href="http://nfis.org">
                	<img src="<%=properties.getProperty("logo.header")%>"
                		alt="<%=properties.getProperty("alt.logo.header")%>" width="280"
                height="53" border="0" /></a>
              </td>

              <td width="320" height="53" colspan="2">
                <img src="images/photo_01.jpg" 
                     alt="<%=properties.getProperty("alt.mountain.header")%>" 
                     width="320"
                height="53" border="0" />
              </td>
            </tr>

            <tr>
              <td width="600" height="10" colspan="5" bgcolor="#333333">
                <img alt="image" src="images/space.gif" width="600"
                height="10" />
              </td>
            </tr>

            <tr bgcolor="#ffffff">
              <td width="140" height="25" bgcolor="#333333"
              class="version">
                <div align="center">&#187; Beta Version</div>
              </td>

              <td width="25" height="25" valign="top">
                <img alt="image" src="images/tag_corner.gif" width="25"
                height="25" />
              </td>

              <td width="300" height="25" colspan="2">
<%
/* Add the appropriate image file and alt tag, based on the 
 * "image" parameter sent.
 */
%>
                <img src="<%= titleImage %>"
                alt="<%= alt_title %>" width="300"
                height="25" />
              </td>
       <% // if noLangButton is not set create the language button 
          if (noLangButton == null) {
       %>    
			<form name="<%=Constants.LANGUAGE_FORM%>" action="<%=pageName%>" method="POST" valign="middle">
              <td valign="middle">                                		                	
	          	<input type="image" name="<%=Constants.LANGUAGE%>" 
	          	       src="<%=properties.getProperty("langButtonImage.header")%>"
	          	       alt="<%=properties.getProperty("alt.langButtonImage.header")%>">
				<input type="hidden" name="<%=Constants.LANGUAGE%>" 
							 value="<%=properties.getProperty("otherLanguage")%>">		          	    		   
              </td>
             </form> 
        <% // else just print out the empty <td> tag
          } else {
        %>
        	<td></td>
        <%}
        %>
            </tr>
            <tr>
            	<!--td is written on one line to overcome nasty IE error -->
            	<td width="600" height="9" colspan="5" valign="top"><img alt="image" src="images/bg_undertitle.gif" /></td>
            </tr>
            <tr>

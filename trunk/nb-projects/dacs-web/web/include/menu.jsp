<%@ page import = "com.fedroot.jsptaglib.Constants"%>
<%@ page import = "java.util.Properties"%>

<%! Properties text;%>
<%
	text = (Properties)session.getAttribute(Constants.TEXT);
%>

<%
	String[] menuItems = new String[7];

	// determine the menu item being requested
	String menuItem = request.getParameter("menu");
	if (menuItem != null) {
		if (menuItem.equalsIgnoreCase("why_register")) {
			menuItems[0] = "<span class=\"menu\">&#187; </span><span class=\"menu_select\">" + text.getProperty("why.menu") + "</span>";
		} else {
			menuItems[0] = "<span class=\"menu\">&#187; <a href=\"" + Constants.WHY_REGISTER + "\">" + text.getProperty("why.menu") + "</a></span>";
		}
		
		if (menuItem.equalsIgnoreCase("registration")) {
			menuItems[1] = "<span class=\"menu\">&#187; </span><span class=\"menu_select\">" + text.getProperty("registration.menu") + "</span>";
		} else {
			menuItems[1] = "<span class=\"menu\">&#187; <a href=\"" + Constants.REGISTRATION + "\">" + text.getProperty("registration.menu") + "</a></span>";
		}
		
		if (menuItem.equalsIgnoreCase("login")) {
			menuItems[2] = "<span class=\"menu\">&#187; </span><span class=\"menu_select\">" + text.getProperty("login.menu") + "</span>";
		} else {
			menuItems[2] = "<span class=\"menu\">&#187; <a href=\"" + Constants.LOGIN_JSP + "\">" + text.getProperty("login.menu") + "</a></span>";
		}
		
		if (menuItem.equalsIgnoreCase("modify_profile")) {
			menuItems[3] = "<span class=\"menu\">&#187; </span><span class=\"menu_select\">" + text.getProperty("modify.menu") + "</span>";
		} else {
			menuItems[3] = "<span class=\"menu\">&#187; <a href=\"" + Constants.MODIFY_PROFILE_INTRO_JSP + "\">" + text.getProperty("modify.menu") + "</a></span>";
		}
		
		if (menuItem.equalsIgnoreCase("modify_password")) {
			menuItems[4] = "<span class=\"menu\">&#187; </span><span class=\"menu_select\">" + text.getProperty("modify.password.menu") + "</span>";
		} else {
			menuItems[4] = "<span class=\"menu\">&#187; <a href=\"/access/" +
				Constants.MODIFY_PASSWORD_JSP +  "\">" + text.getProperty("modify.password.menu") + "</a></span>";
		}
		
		if (menuItem.equalsIgnoreCase("forgot_password")) {
			menuItems[5] = "<span class=\"menu\">&#187; </span><span class=\"menu_select\">" + text.getProperty("forgot.menu") + "</span>";
		} else {
			menuItems[5] = "<span class=\"menu\">&#187; <a href=\"" + Constants.FORGOT_PASSWORD_JSP + "\">" + text.getProperty("forgot.menu") + "</a></span>";
		}
		
		if (menuItem.equalsIgnoreCase("logout")){
			menuItems[6] = "<span class=\"menu\">&#187; </span><span class=\"menu_select\">" + text.getProperty("logout.menu") + "</span>";
		} else {
			menuItems[6] = "<span class=\"menu\">&#187; <a href=\"" + Constants.SIGNOUT + "\">" + text.getProperty("logout.menu") + "</a></span>";
		}
    } else {
    	// default is login
		menuItems[0] = "<span class=\"menu\">&#187; <a href=\"" + Constants.WHY_REGISTER + "\">" + text.getProperty("why.menu") + "</a></span>";
		menuItems[1] = "<span class=\"menu\">&#187; <a href=\"" + Constants.REGISTRATION + "\">" + text.getProperty("registration.menu") + "</a></span>";
		menuItems[2] = "<span class=\"menu\">&#187; <a href=\"" + Constants.LOGIN_JSP + "\">" + text.getProperty("login.menu") + "</a></span>";
		menuItems[3] = "<span class=\"menu\">&#187; <a href=\"" + Constants.MODIFY_PROFILE_INTRO_JSP + "\">" + text.getProperty("modify.menu") + "</a></span>";
		menuItems[4] = "<span class=\"menu\">&#187; <a href=\"" + Constants.MODIFY_PASSWORD_JSP +  "\">" + text.getProperty("modify.password.menu") + "</a></span>";
		menuItems[5] = "<span class=\"menu\">&#187; <a href=\"" + Constants.FORGOT_PASSWORD_JSP + "\">" + text.getProperty("forgot.menu") + "</a></span>"; 
		menuItems[6] = "<span class=\"menu\">&#187; <a href=\"" + Constants.SIGNOUT + "\">" + text.getProperty("logout.menu") + "</a></span>"; 
	}
%>

              <td width="140" colspan="1" valign="top" bgcolor="#d1d6ee">
                <br />
                 <span class="menu"><b><%= text.getProperty("new.menu") %></b></span>
                <br />
                 <%=menuItems[0] %>
                <br />
                 <%=menuItems[1] %>
                <br /><br />
                 <span class="menu"><b><%= text.getProperty("current.menu") %></b></span>
                <br />
                 <%=menuItems[2] %>
                <br />
                 <%=menuItems[3] %>
                <br />
                 <%=menuItems[4] %>
                <br />
                 <%=menuItems[5] %>
                <br />
                 <%=menuItems[6] %>
              </td>

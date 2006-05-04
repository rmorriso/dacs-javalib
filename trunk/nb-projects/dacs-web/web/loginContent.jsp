<%
        String errorText = (String)request.getAttribute("com.fedroot.error");
%>
<div id="centercol">
<div id="centertext">
    <h1><a name="content">Login with OpenID</a></h1>
    <form method="post" action="<%= request.getContextPath() %>/openIDConsumer">
        <p>
        <b>Your OpenID Identity/URL</b> 
        <input name="openid_url" size="30"/> 
        <input type="submit" value="Verify"/>
        </p>
        <% if (errorText != null) { %>
        <p class="invalid"><%= errorText %> </p>
        <% } %>
    </form>
</div>    
</div>


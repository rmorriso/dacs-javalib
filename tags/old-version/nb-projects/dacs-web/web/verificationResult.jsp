<%@ page import = "java.util.Properties"%>
<%@ page import="org.idprism.openid.IdentityVerificationResponse"     %>

    <%
        IdentityVerificationResponse openIDResponse =
                (IdentityVerificationResponse)request.getAttribute("org.idprism.openid.response");
    %>
<div id="centercol">
   <div id="centertext">
       <h1 class="topheading"><a name="content">OpenID Verified!</a></h1>
       <p>The OpenID server at '<%= openIDResponse.getVerifiedIdentity().getServerURL() %>'
           indicated on '<%= openIDResponse.getIssueTime() %>' that you were logged in, 
           and that you were the owner of ' <%= openIDResponse.getVerifiedIdentity().getCleanIDURL() %>'
       </p>
       <p>The verification string used to test the provided signature was:</p>
       <xmp><%= openIDResponse.getVerificationString() %></xmp>
   </div>
</div>
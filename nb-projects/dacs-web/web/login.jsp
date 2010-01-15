<%@ page import = "java.util.Properties"%>
<%@ page import = "com.fedroot.jsptaglib.Constants" %>
<%@ page import = "com.fedroot.dacs.servlet.DacsError" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

    <head>
        <title>demo.fedroot.com :: login</title>
        <meta name="author" content="Metalogic Software Corporation"/>
        <meta name="keywords" content="metalogic,federated identity,identity management,dacs,authentication,access control,distributed,canadian,saml,dss"/>
        <meta name="dc.creator" content="Metalogic Software Corporation, Victoria, British Columbia"/>
        <meta name="dc.date.created" content="05-04-2006"/>
        <meta name="dc.date.modified" content="05-04-2006"/>
        <meta name="dc.language" scheme="ISO639-3" content="eng"/>
        <meta name="dc.subject" scheme="gccore" content=""/>
        <meta name="description" content="Working with DSS - Distributed Systems Software on the application of DACS"/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <meta name="robots" content="index,follow" />
        <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" href="/css/centered-layout.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="/css/screen.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
    </head>
    <body>
        <div id="header">
            <p class="tagline">
                <a href="https://fedroot.com">
                    infrastructure for the federated web
                </a>
            </p>
            <div id="metanav">
                <ul title="Meta Navigation Bar">
                    <li><a href="#content" title="Skip to content">skip to content</a></li>
                    <li><a href="/admin/contact_form.shtml">contact us</a></li>

                    <li><a href="/admin/search.shtml">search</a></li>
                    <li><a href="/admin/sitemap.shtml">sitemap</a></li>
                </ul>
            </div>

            <!-- #include virtual="/includes/notice.inc" -->
        </div> <!-- end header -->

        <div id="sorry">
            Unfortunately, as fedroot takes advantage of
            <a href="http://www.webstandards.org/">web standards</a>
            that your browser is unable to support, this site&#39;s layout will be simplified in your browser.
        </div> <!-- end sorry -->

        <div id="content">
            <div id="centertext">
                <h1><a name="content">Login with OpenID</a></h1>
                <form method="post" action="<%= request.getContextPath() %>/openIDConsumer">
                    <p>
                        <b>Your OpenID Identity/URL</b>
                        <input name="openid_url" size="30"/>
                        <input type="submit" value="Verify"/>
                    </p>
                </form>
            </div> <!-- end centertext -->


            <div id="footer">
                <p id="fedroot-details"><a href="http://fedroot.com/admin/copyright.shtml">copyright 2005-2006</a> | <a href="http://fedroot.com/">Metalogic Software Corp.</a> | <a href="/admin/privacy.shtml">privacy</a></p>

                <p id="standards"><a href="http://validator.w3.org/check/referer">[XHTML 1.0]</a> <a href="http://jigsaw.w3.org/css-validator/validator?uri=http://fedroot.com/css/screen.css">[CSS]</a></p>
            </div> <!-- end footer -->

        </div> <!-- end content -->
    </body>
</html>



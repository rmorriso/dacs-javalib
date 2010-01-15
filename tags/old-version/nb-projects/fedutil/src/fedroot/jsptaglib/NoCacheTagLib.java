package fedroot.jsptaglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This tag set no caching for a page.
 * 
 * Use this tag at the top of your page before committing the response to 
 * put no caching values into the Http header.
 * 
 * @author Eric Murphy
 *
 */
public class NoCacheTagLib extends TagSupport {


	/** 
	 * Code to set no caching parameters in the http header of the response
	 * 
	 * Here is the xml tag descriptor to use when making the entry in the .tld file:
	 * <p>
	 * <pre>
	 * 		&lt;tag&gt;
	 *		&lt;name&gt;PrintStackTrace&lt;/name&gt;
	 *		&lt;tagclass&gt;org.nfis.jsptaglib.PrintStackTraceTagLib&lt;/tagclass&gt;
	 *		&lt;info&gt;Prints out the stack trace from an Exception object&lt;/info&gt;
	 *		&lt;attribute&gt;
	 *			&lt;name&gt;errorName&lt;/name&gt;
	 *			&lt;required&gt;true&lt;/required&gt;
	 *			&lt;!-- allow dynamic assignment of this value --&gt;
	 *			&lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
	 *		&lt;/attribute&gt;
	 *	&lt;/tag&gt;
	 * </pre>
	 * </p>
	 * 
	 */
	
	public int doStartTag() {
					
		HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
		HttpServletRequest  request  = (HttpServletRequest)pageContext.getRequest();
			
		/* Set no caching on this page. First, set the expires header into 
		 * the past. Most browsers will not re-use expired pages.
		 */
		response.setDateHeader("Expires", 0);
		
		/* Next, set a "Pragma" header that tells HTTP 1.0 browsers not to 
		 * cache the page 
		 */
		response.setHeader("Pragma", "no-cache");
		
		/* Finally, use the HTTP 1.1 "Cache-Control" header to explicitly 
		 * tell HTTP 1.l browsers not to cache the page 
		 */
		if (request.getProtocol().equals("HTTP/1.1")) {
			response.setHeader("Cache-Control", "no-cache");
		}
		
		return SKIP_BODY;
	}	
}

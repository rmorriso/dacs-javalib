package fedroot.jsptaglib;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * To use this class there must be an {@link Exception} object set as an 
 * attribute in the request or session. The name of the Exception attribute 
 * must set as an attribute PrintStackTrace tag used in the jsp page.
 * 
 * It then prints out the stack trace for this Exception in an html text area.
 * 
 * @author Eric Murphy
 *
 */
public class PrintStackTraceTagLib extends TagSupport {

	private String errorName = null;
	
	public void setErrorName(String errorName) {
		
		this.errorName = errorName	;
	}
	
	public String getErrorName() {
		
		return errorName;
	}
	
	public int doStartTag() {
  	
		HttpSession session = pageContext.getSession(); 
		JspWriter out = pageContext.getOut();
		
		try {

	  	Exception error = getException(session);
	    
			if (error != null) {
	  			
				out.write( "<p>" + error.getMessage() + "</p>" +
						  "<form><textarea name='stack_trace' rows='20' cols='50' wrap='off'>" + 
						  "Error stack trace (used for debug purposes):");
		
				error.printStackTrace(new PrintWriter(out));
		    out.write("</textarea></form>");		      
			}
    } catch (IOException e) {
       				
    }
    return SKIP_BODY;
  }

  private Exception getException(HttpSession session) {
  	
		Exception error = (Exception) session.getAttribute(errorName);  		
		if (error == null) {
			
			ServletRequest request = pageContext.getRequest();
			error = (Exception) request.getAttribute(errorName);  		
		}	
		return error;
  }
  
  public void release() {
  	
  	super.release();
  	errorName = null;	
  }  
}


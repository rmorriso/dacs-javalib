package org.nfis.nfi.photoplot.httpclient;

import static org.nfis.nfi.photoplot.httpclient.HttpConstants.CONNECTION_TIMEOUT_KEY;
import static org.nfis.nfi.photoplot.httpclient.HttpConstants.HOST_KEY;
import static org.nfis.nfi.photoplot.httpclient.HttpConstants.HTTP;
import static org.nfis.nfi.photoplot.httpclient.HttpConstants.PLOT_NO_KEY;
import static org.nfis.nfi.photoplot.httpclient.HttpConstants.PORT_KEY;
import static org.nfis.nfi.photoplot.httpclient.HttpConstants.SERVLET_PATH;
import static org.nfis.nfi.photoplot.httpclient.HttpConstants.WEB_CONTEXT;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.nfis.nfi.photoplot.common.NFIResourceMessages;
import org.nfis.nfi.photoplot.rcp.util.PPAConstants;
import org.nfis.nfi.photoplot.rcp.util.QAReportUtil;
import org.nfis.nfi.photoplot.swt.dialog.ICancellable;
import org.nfis.nfi.photoplot.swt.dialog.ProgressDialog;

public class SubmissionPP {
	private static final String REQUEST_ACTION = "submission";
	private static final String CONFLICT_PLOT_ACTION = "conflictplot";
	private static final boolean HAND_SHAKE = false;
	private static String HOST = "localhost";
	private static int PORT = 8443;
	private static int CONNECTION_TIMEOUT = 5000;
//	private static int DOWNLOAD_STREAM_BUFFER_SIZE=8192;
	
//    private static String url = "https://localhost:8443/NFIWeb/pp/" + REQUEST_ACTION;
//    private static String url = "http://localhost:8080/NFIWeb/gp/" + REQUEST_ACTION;
    
	private final int M = 1024* 1024;
    // parameters passing into task.
    private Shell shell;
    private HttpClient client;
    private PostMethod post;
    private int status;
    private InputStream is;
    private Double fileLength;
    private String fileName;
    FilePartWithProgress part = null;
    
	private String srcFilePath; 
	public void setSrcFilePath(String src){
		srcFilePath = src;
	}
	public SubmissionPP(){
    	Properties prop = new Properties();
    	try {
    		prop.load(this.getClass().getResourceAsStream("httpclient.properties"));
        	CONNECTION_TIMEOUT = Integer.parseInt((String)prop.get(CONNECTION_TIMEOUT_KEY));
//        	DOWNLOAD_STREAM_BUFFER_SIZE = Integer.parseInt((String)prop.get(DOWNLOAD_STREAM_BUFFER_SIZE_KEY));
        	HOST = prop.getProperty(HOST_KEY);
        	PORT = Integer.parseInt((String)prop.get(PORT_KEY));
    	} catch (Exception e){ /* do nothing but using default. */  	}
	}
	
    public int request(Shell shell)  throws InvocationTargetException, InterruptedException {
    	int status = 0;
    	try {
    		this.shell = shell;
    		Protocol https = new Protocol("https", new EasySSLProtocolSocketFactory(), PORT);
    		Protocol.registerProtocol("https",https);
              client = new HttpClient();
              client.getHostConfiguration().setHost(HOST, PORT, https);
              
              client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
              
              String targetURL = HTTP+ "://"+ HOST+":"+PORT+"/" + WEB_CONTEXT +"/"+ SERVLET_PATH +"/" + REQUEST_ACTION;
              post = new PostMethod(targetURL);

              post.getParams().setBooleanParameter(
                      HttpMethodParams.USE_EXPECT_CONTINUE,
                      HAND_SHAKE);

              // When running from RCP.
              String appVer = QAReportUtil.getAppVersion();
              // for testing from main method.
//              String appVer = "v2.0.0.B";
              post.addRequestHeader(new Header(PPAConstants.ClientAppVersion, appVer));
              post.addRequestHeader(new Header(HttpConstants.USER_ID_KEY, getUserId()));

              File targetFile = new File (srcFilePath);
              part = new FilePartWithProgress(targetFile);

              submitWithProgress();
              
              Header updateClientApp = post.getResponseHeader(HttpConstants.CLIENT_APP_UPDATE_FLAG_KEY);
              if (updateClientApp!=null && Boolean.TRUE.toString().equalsIgnoreCase(updateClientApp.getValue())){
	  				MessageBox messageBox = new MessageBox(shell,
							SWT.ICON_WARNING | SWT.OK);
					messageBox.setMessage(NFIResourceMessages.getString("SWT.Dialog.Submission.Error.ClientNeedUpdate.Text"));
					messageBox.setText(NFIResourceMessages.getString("SWT.Dialog.Submission.Error.ClientNeedUpdate.Title"));
					messageBox.open();

              }
              if (status == HttpStatus.SC_OK) {
                  System.out.println("Upload complete, response=" + post.getResponseBodyAsString());
              } else {
                  System.out.println("Upload failed, response=" +HttpStatus.getStatusText(status));
              }
          } catch (InterruptedException ex) {
              throw ex;
          } catch (Exception ex) {
              throw new InvocationTargetException(ex);
          } finally {
              post.releaseConnection();
          }
          
          return status;
      }
    
    private String getUserId(){
    	return "Shawn";
    }
    
    public List<String> requestConflictPlot(List<String> plotList){
    	List<String> result = new ArrayList<String>();
    	int status = 0;
    	try {
    		Protocol https = new Protocol("https", new EasySSLProtocolSocketFactory(), PORT);
    		Protocol.registerProtocol("https",https);
              HttpClient client = new HttpClient();
              client.getHostConfiguration().setHost(HOST, PORT, https);
              
              client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
              
              String targetURL = HTTP+ "://"+ HOST+":"+PORT+"/" + WEB_CONTEXT +"/"+ SERVLET_PATH +"/" + CONFLICT_PLOT_ACTION;
              post = new PostMethod(targetURL);
              post.getParams().setBooleanParameter(
                      HttpMethodParams.USE_EXPECT_CONTINUE,
                      HAND_SHAKE);

              // When running from RCP.
              String appVer = QAReportUtil.getAppVersion();
              // for testing from main method.
//              String appVer = "v2.0.0.B";
              
              post.addRequestHeader(new Header(PPAConstants.ClientAppVersion, appVer));
              
              NameValuePair[] data = new NameValuePair[plotList.size()];
              // Add the form values
              for (int i=0; i<plotList.size();i++){
            	  String plotNoKey = PLOT_NO_KEY + "_" + i;
            	  String plotNoValue = plotList.get(i);
            	  
            	  data[i] = new NameValuePair(plotNoKey, plotNoValue);
              }
              
              post.setRequestBody(data);
              
              status = client.executeMethod(post);
              
              Header updateClientApp = post.getResponseHeader(HttpConstants.CLIENT_APP_UPDATE_FLAG_KEY);
              if (updateClientApp!=null && Boolean.TRUE.toString().equalsIgnoreCase(updateClientApp.getValue())){
	        	  	Shell shell = new Shell(Display.getCurrent());
	  				MessageBox messageBox = new MessageBox(shell,
							SWT.ICON_WARNING | SWT.OK);
					messageBox.setMessage(NFIResourceMessages.getString("SWT.Dialog.Submission.Error.ClientNeedUpdate.Text"));
					messageBox.setText(NFIResourceMessages.getString("SWT.Dialog.Submission.Error.ClientNeedUpdate.Title"));
					messageBox.open();
					status = HttpStatus.SC_CONFLICT;
					return null;
              }
              
              String resList = post.getResponseBodyAsString();
              if (status == HttpStatus.SC_OK && resList.trim().length()>0){
            	  String noBracketList = resList.substring(1, resList.length() -1);
            	  String[] list = noBracketList.split(",");
            	  for (int i=0;i<list.length;i++){
            		  list[i] = list[i].trim();
            	  }
            	  result = Arrays.asList(list);
              }
              
          } catch (Exception ex) {
              System.out.println("Error: " + ex.getMessage());
              ex.printStackTrace();
          } finally {
              post.releaseConnection();
          }
          
          return result;
      }
    
    private void submitWithProgress() throws InvocationTargetException, InterruptedException{
		IRunnableWithProgress task = new IRunnableWithProgress(){
			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
//				String progressText = NFIResourceMessages.getString("SWT.HttpClient.Storaging.Progress.Text");
				part.setMonitor(monitor);
				submitting(monitor);
				monitor.done();
			}
		};
		
    	ProgressDialog progressDialog = new ProgressDialog(shell, part);
		try {
			progressDialog.run(true,true, task);
		} finally {
			progressDialog.close();
		}
    }
    
    private void submitting(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException{
		try {
			Part[] parts = { part };
	        
	        post.setRequestEntity(
	                new MultipartRequestEntity(parts, 
	                post.getParams())
	                );
	            
	        status = client.executeMethod(post);
	        
	        if (part.isUserCancelled()){
	        	throw new InterruptedException(NFIResourceMessages.getString("SWT.HttpClient.UserCancel.Warning"));
	        }
		} catch (InterruptedException e){
			throw e;
		} catch (Exception e){ 
			throw new InvocationTargetException(e, NFIResourceMessages.getString("SWT.HttpClient.Uploading.Error"));
		}       
    }
}
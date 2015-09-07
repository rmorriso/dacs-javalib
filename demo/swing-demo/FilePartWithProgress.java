package org.nfis.nfi.photoplot.httpclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.nfis.nfi.photoplot.swt.dialog.ICancellable;

public class FilePartWithProgress extends FilePart implements ICancellable {
	private static final Log LOG = LogFactory.getLog(FilePartWithProgress.class);
	IProgressMonitor monitor;
	private volatile Boolean cancelControl = false;
	
    public FilePartWithProgress(File file) 
    throws FileNotFoundException {
        super(file.getName(), file);
    }
    
    public void setMonitor(IProgressMonitor monitor){
    	this.monitor = monitor;
    }

    protected void sendData(OutputStream out) throws IOException {
    	long length = lengthOfData();
        if (lengthOfData() == 0) {
            // this file contains no data, so there is nothing to send.
            // we don't want to create a zero length buffer as this will
            // cause an infinite loop when reading.
            LOG.debug("No data to send.");
            return;
        }
    	ProgressUpdater progressUpdater = new ProgressUpdater(monitor, length);
    	progressUpdater.start();
        LOG.trace("enter sendData(OutputStream out)");
//        IProgressMonitor monitor = progressUpdater.getMonitor();

        long sum = 0;
        
        byte[] tmp = new byte[4096];
        InputStream instream = getSource().createInputStream();
        try {
            int len;
            while ((len = instream.read(tmp)) >= 0) {
				if (cancelControl){
					break;
					// cannot throw InterruptedException because super class don't throw it.
				}
                out.write(tmp, 0, len);
                
				sum += len;
				progressUpdater.update(len);
            }
            monitor.setTaskName("Finished uploading, waiting server to save the submission.");
        } finally {
            // we're done with the stream, close it
            instream.close();
        }
    }

	@Override
	public void cancelProgress() {
		cancelControl = true;
		System.out.println("== progress cancelled.");
	}

	@Override
	public void startProgress() {
		System.out.println("== progress started.");
		cancelControl = false;
	}

	public Boolean isUserCancelled() {
		return cancelControl;
	}

	@Override
	public Boolean getFlag() {
		// TODO Auto-generated method stub
		return cancelControl;
	}   
}

/*
 * COPYRIGHT AND PERMISSION NOTICE
 *
 * Copyright (c) 2001-2005 The Queen in Right of Canada
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, provided that the above copyright notice(s) and this
 * permission notice appear in all copies of the Software and that both the
 * above copyright notice(s) and this permission notice appear in supporting
 * documentation.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS NOTICE
 * BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR CONSEQUENTIAL DAMAGES,
 * OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,
 * ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS
 * SOFTWARE.
 *
 * Except as contained in this notice, the name of a copyright holder shall not
 * be used in advertising or otherwise to promote the sale, use or other
 * dealings in this Software without prior written authorization of the
 * copyright holder.
 */

package fedroot.dacs.swingdemo.webservice;

import fedroot.dacs.client.DacsCheckRequest;
import fedroot.servlet.ParameterValidator;
import fedroot.servlet.ParameterValidators;
import fedroot.servlet.ServiceParameters;
import java.io.File;
import org.apache.http.entity.mime.content.FileBody;


/**
 *
 * @author rmorriso
 */
public class FileUploadRequest extends DacsCheckRequest {

    public enum args { username };

    private String username;
    private String filename1;
    private File file1;
    private String filename2;
    private File file2;
    
    public FileUploadRequest(String uri, String username, String filename1, File file1, String filename2, File file2) {
        super(uri);
        this.username = username;
        this.filename1 = filename1;
        this.file1 = file1;
        this.filename2 = filename2;
        this.file2 = file2;
    }

    @Override
    public ServiceParameters getServiceParameters() {
        ServiceParameters serviceParameters = super.getServiceParameters();
        serviceParameters.addParameter(args.username, username);
        serviceParameters.addFileBody(filename1, new FileBody(file1));
        serviceParameters.addFileBody(filename2, new FileBody(file2));
        return serviceParameters;
    }

    /**
     * return ParameterValidators for use by clients that implement
     * the DacsAuthenticateRequest service
     * @return
     */
    @Override
    public ParameterValidators getParameterValidators() {
        ParameterValidators parameterValidators = super.getParameterValidators();
        // username is mandatory
        ParameterValidator usernameValidator = new ParameterValidator(ParameterValidator.ValidationType.ALL);
        usernameValidator.addParameter(args.username.toString());
        parameterValidators.addValidator(usernameValidator);
        return parameterValidators;
    }
}

/*
 * DacsNotices.java
 *
 * Created on December 16, 2005, 10:40 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;

import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.dacs.services.DacsNoticeAckService;
import fedroot.xml.XmlUtil;
import com.fedroot.dacs.util.DacsAccess905Event;
import com.fedroot.dacs.xmlbeans.AckReplyDocument;
import com.fedroot.dacs.xmlbeans.DacsNoticesDocument;
import com.fedroot.dacs.xmlbeans.NoticeDocument;
import com.fedroot.dacs.xmlbeans.PresentationReplyDocument;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;

/**
 * wrapper class for DACS Notice Acceptance Tokens (NATs)
 * @author rmorriso
 */
public class DacsNotices {
    String notice_uris;
    String resource_uris;
    String time;
    String hmac;
    String presentationhandler;
    String ackhandler;
    DacsGetMethod acceptmethod, declinemethod;
    List<Notice> notices;
    
   /** Log object for this class. */
    private static final Log LOG = LogFactory.getLog(DacsNotices.class);
    
    
    /**
     * Creates a new instance of DacsNotices from DacsAccess905Event
     *
     * @param dacscontext
     * @param event
     */
    public DacsNotices(DacsContext dacscontext, DacsAccess905Event event) {
        DacsGetMethod dacsget = event.getDacsNoticePresentationMethod();
        this.notices = new ArrayList<Notice>();
        try {
            int dacsstatus = dacscontext.executeMethod(dacsget);
            if (dacsstatus == DacsStatus.SC_OK) {
                // parse XML and call event handlers if appropriate
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                // Check that it is an instance of the DacsAcsDocument
                LOG.info(expectedXmlObject.getClass().getName());
                if(expectedXmlObject instanceof DacsNoticesDocument) {
                    DacsNoticesDocument doc = (DacsNoticesDocument) expectedXmlObject;
                    DacsNoticesDocument.DacsNotices notices = doc.getDacsNotices();
                    PresentationReplyDocument.PresentationReply pres = notices.getPresentationReply();
                    // update time and hmac with new values returned in PresentationReply
                    this.time = pres.getTime().toString();
                    this.hmac = pres.getHmac();
                    for (NoticeDocument.Notice notice : pres.getNoticeList()) {
                        Notice dacsnotice = new Notice(notice.getUri().toString(), XmlUtil.extract(notice));
                        this.notices.add(dacsnotice);
                    }
                    this.acceptmethod = event.getDacsNoticeAckMethod(DacsNoticeAckService.ResponseValue.accepted, this.time, this.hmac);
                    this.declinemethod = event.getDacsNoticeAckMethod(DacsNoticeAckService.ResponseValue.declined, this.time, this.hmac);
                } else {
                    throw new RuntimeException("failed to load notices");
                }
            }
        } catch (Exception x) {
            throw new RuntimeException("failed to load notices");
        } finally {
            dacsget.releaseConnection();
        }
    }
    
    /**
     * return concatenation of Notice content
     */
    public String getContent() {
        StringBuffer buf = new StringBuffer();
        for (Notice dacsnotice : this.notices) {
            buf.append(dacsnotice.getContent());
        }
        return buf.toString();
    }
    
    public String getHtml() {
        StringBuffer buf = new StringBuffer();
        buf.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");
        buf.append("<head>");
        buf.append("</head>");
        buf.append("<body>");
        buf.append("<h1>Notices Must Be Acknowledged</h1>");
        for (Notice dacsnotice : this.notices) {
            buf.append(dacsnotice.getContent());
            buf.append("<hr/>");
            
        }
        buf.append("</body>");
        buf.append("</html>");
        return buf.toString();
    }
    
    public boolean acceptNotices(DacsContext dacscontext) {
        try {
            int dacsstatus = dacscontext.executeMethod(this.acceptmethod);
            if (dacsstatus == DacsStatus.SC_OK) {
                // parse XML and call event handlers if appropriate
                XmlObject expectedXmlObject = XmlObject.Factory.parse(this.acceptmethod.getResponseBodyAsStream());
                // Check that it is an instance of the DacsAcsDocument
                LOG.debug(expectedXmlObject.getClass().getName());
                if(expectedXmlObject instanceof DacsNoticesDocument) {
                    DacsNoticesDocument doc = (DacsNoticesDocument) expectedXmlObject;
                    DacsNoticesDocument.DacsNotices noticedoc = doc.getDacsNotices();
                    if (noticedoc.isSetAckReply()) {
                        AckReplyDocument.AckReply reply = noticedoc.getAckReply();
                        if (reply.getResponse() == AckReplyDocument.AckReply.Response.ACCEPTED) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (noticedoc.isSetCommonStatus()) {
                        throw new RuntimeException(noticedoc.getCommonStatus().getMessage());
                    } else {
                        throw new RuntimeException("shouldn't happen");
                    }
                } else {
                    throw new RuntimeException("invalid notice acknowledgment reply");
                }
            } else {
                throw new RuntimeException("HTTP Status " + dacsstatus);
            }
        } catch (Exception x) {
            throw new RuntimeException("notice acknowledgment failed");
        }
    }
}


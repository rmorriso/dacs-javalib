package com.fedroot.jsptaglib;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import com.fedroot.web.ProfileCookie;

/**
 * If the request includes a {@link Constants#LANGUAGE} parameter,
 * the requested {@link Properties} object will be set in the session.
 * If there is no language parameter specified and there is not already
 * a Properties file set in the session, the Properties object will be
 * set to English.
 * <p>
 * The Properties file referenced in the session by the {@link Constants#TEXT}
 * key contains the text for the page.
 * </p>
 */

/*
 * Pass:
 *  name of language change parameter
 *  name of attribute that points to the current text file in application
 *  Get the names of the files using Locale
 *
 */
public class SetTextPropertyTagLib extends TagSupport {
    
    public int doStartTag() {
        
        HttpServletRequest  request  = (HttpServletRequest)pageContext.getRequest();
        HttpServletResponse response =
                (HttpServletResponse)pageContext.getResponse();
        HttpSession session          = pageContext.getSession();
        ServletContext application   = pageContext.getServletContext();
        ProfileCookie profileCookie  = getProfileCookie(request);
        Properties textFile          = (Properties)session.getAttribute(Constants.TEXT);
        
        // this will be present if the user wants to change language
        String language = request.getParameter(Constants.LANGUAGE);
        
        // if there is a request to change language
        if (language != null) {
            
            profileCookie = new ProfileCookie(language);
            response.addCookie(profileCookie);
            
            // set the properties to the value of the Constants.LANGUAGE parameter
            setLanguage(language, session, application);
            
            // check to see if the cookie is set to a different language than textFile
        } else if ((textFile != null) && (profileCookie != null)) {
            
            String cookieLanguage = profileCookie.getLanguage();
            String textLanguage   = textFile.getProperty(Constants.LANGUAGE);
            
            if (!cookieLanguage.equals(textLanguage)) {
                
                setLanguage(cookieLanguage, session, application);
            }
            // if no properties are set in the session
        } else if (textFile == null) {
            
            // check for the presence of the profile cookie
            if (profileCookie != null) {
                
                String cookieLanguage = profileCookie.getLanguage();
                setLanguage(cookieLanguage, session, application);
                // no cookie, just set it to English
            } else {
                
                session.setAttribute(Constants.TEXT,
                        application.getAttribute(Constants.ENGLISH));
                profileCookie =
                        new ProfileCookie(ProfileCookie.EN);
                response.addCookie(profileCookie);
            }
        }
        return SKIP_BODY;
    }
    
    private void setLanguage
            (String language, HttpSession session, ServletContext application) {
        
        // set the properties to the value of the Constants.LANGUAGE parameter
        if(language.equals(Constants.FRENCH)) {
            
            session.setAttribute(Constants.TEXT,
                    application.getAttribute(Constants.FRENCH));
        } else {
            
            session.setAttribute(Constants.TEXT,
                    application.getAttribute(Constants.ENGLISH));
        }
    }
    
    private ProfileCookie getProfileCookie(HttpServletRequest request) {
        
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        
        Cookie foundCookie = ProfileCookie.getCookieByName
                (ProfileCookie.NFIS_PROFILE, cookies);
        
        ProfileCookie profileCookie = null;
        if (foundCookie != null) {
            
            try {
                profileCookie = new ProfileCookie(foundCookie);
            } catch (IllegalArgumentException iae) {
                // don't worry about bad profileCookie, just return null
            }
        }
        return profileCookie;
    }
    
}


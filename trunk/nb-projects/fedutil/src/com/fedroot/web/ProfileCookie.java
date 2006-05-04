/*
 * Created on Apr 8, 2004
 *
 * Created on December 14, 2005, 1:18 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package com.fedroot.web;


import javax.servlet.http.Cookie;

import com.fedroot.util.StringUtility;
import java.util.List;


/**
 * This Cookie encapsulates non secure information about a user the is useful to
 * pass along the NFIS network. Currently it holds the users current language
 * preference so this can be shared between applications
 *
 * @author rmorriso and emurphy
 */
public class ProfileCookie extends Cookie {
    
    /**
     * The name to use when retrieving the cookie
     */
    public static final String NFIS_PROFILE = "NFIS:profile";
    
    public static final String NFIS_DOMAIN = ".nfis.org";
    
    /** value for the key value pair in the cookie string 'lang=fr' */
    public static final String FR = "fr";
    
    /** value for the key value pair in the cookie string 'lang=en' */
    public static final String EN = "en";
    
    /** Key in the key value pair 'lang=fr or en' */
    public static final String LANG = "lang";
    
    private String language;
    
    /**
     * Cookie name will be set to {@link #NFIS_PROFILE}
     *
     * @param must be {@link #EN} or {@link #FR}
     */
    public ProfileCookie(String lang) throws IllegalArgumentException {
        
        super(NFIS_PROFILE, "");
        
        setLanguage(lang);
        addKeyValuePair(LANG, language);
        configureCookie();
    }
    
    /**
     * @param cookie this cookie must have the name {@link #NFIS_PROFILE} and
     *         contain the key value pair in the value 'lang= '.  The value
     *         must begin with en or fr.
     *
     * @throws IllegalArgumentException throw if the {@link Cookie#getName()}
     * does not return {@link #NFIS_PROFILE} or there is no key in the cookie
     * value equal to {@link #LANG} with a value beginning with {@link #EN} or
     * {@link #FR}.
     */
    public ProfileCookie(Cookie cookie) throws IllegalArgumentException {
        
        super(cookie.getName(), cookie.getValue());
        
        // get the passed in cookies name
        String name = this.getName();
        
        if (!name.equals(NFIS_PROFILE)) {
            throw new IllegalArgumentException
                    ("Input cookie name must be: " + NFIS_PROFILE);
        }
        
        String value = this.getValue();
        if (value == null) {
            throw new IllegalArgumentException("Input cookie must have a value.");
        }
        
        // get the tokens in the cookie string
        List<String> cookieTokens =
                StringUtility.getListFromDelimitedString(value, ";");
        
        // look for the key 'lang' and get its value
        boolean notFound = true;
        String currentValue = null;
        int size = cookieTokens.size();
        int index = 0;
        while ((index < size) && notFound) {
            
            currentValue = (String)cookieTokens.get(index);
            if (currentValue.startsWith(LANG)) {
                notFound = false;
            }
            index ++;
        }
        if (notFound) {
            
            throw new IllegalArgumentException("No " + LANG + " value");
        }
        String lang = currentValue.substring((currentValue.indexOf('=') + 1));
        setLanguage(lang);
        configureCookie();
    }
    
    private void configureCookie() {
        
        this.setDomain(NFIS_DOMAIN);
        this.setPath("/");
    }
    
    /**
     * Takes care of formatting this cookie for server sending to user agent.
     * This format will not work sending from user agent to server.  Check
     * cookie spec for more info.
     *
     * @param key a key to add to the value in the cookie string
     * @param value the value associated with the abouve key
     */
    private void addKeyValuePair(String key, String value) {
        
        String cookieValue = super.getValue();
        
        // if this not the first key value in this value string, add the semicolon
        if (!cookieValue.equals("")){
            key = ';' + key;
        }
        
        cookieValue += key + '=' + value;
        super.setValue(cookieValue);
    }
    
    public void setLanguage(String lang) {
        if (lang.startsWith(FR)) {
            language = FR;
        } else if (lang.startsWith(EN)) {
            language = EN;
        } else {
            throw new IllegalArgumentException("Language parameter must be " +
                    FR + " or " + EN);
        }
    }
    
    /**
     * A static helper method to be used on the server side to retrieve a
     * the NFIS.profile cookie, or any other cookie you know the name of.  This
     * should be included in a refactoring of EnhancedCookie to make
     * EnhancedCookie the generalized class.
     *
     * @param name name of the cookie you are looking for
     * @param cookies
     * @return returns null if not found
     */
    public static Cookie getCookieByName(String name, Cookie[] cookies) {
        
        Cookie foundCookie = null;
        String currentName = null;
        
        for(int i = 0; i < cookies.length; i ++) {
            
            currentName   = cookies[i].getName();
            if (currentName.equals(name)) {
                foundCookie = cookies[i];
                break;
            }
        }
        return foundCookie;
    }
    
    public String getLanguage() {
        return language;
    }
}

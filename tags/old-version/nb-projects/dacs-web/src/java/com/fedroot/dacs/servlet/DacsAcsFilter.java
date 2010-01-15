/*
 * DacsAcsFilter.java
 *
 * Created on November 29, 2005, 10:49 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.w3c.dom.css.Counter;

/**
 * DacsAcsFilter augments ServletRequests  QUERY_STRING with DACS_ACS=-check
 * @author rmorriso
 */
public final class DacsAcsFilter implements Filter {
    
   private FilterConfig filterConfig = null;
   public void init(FilterConfig filterConfig) 
      throws ServletException {
      this.filterConfig = filterConfig;
   }
   public void destroy() {
      this.filterConfig = null;
   }
   public void doFilter(ServletRequest request,
      ServletResponse response, FilterChain chain) 
      throws IOException, ServletException {
      if (filterConfig == null)
         return;
      StringWriter sw = new StringWriter();
      PrintWriter writer = new PrintWriter(sw);
      Counter counter = (Counter)filterConfig.
         getServletContext().
         getAttribute("hitCounter");
      writer.println();
      writer.println("===============");
      writer.println("The number of hits is: ");
      writer.println("===============");

   }
    
}

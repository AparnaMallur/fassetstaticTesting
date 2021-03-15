package com.fasset.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//@WebFilter("/FassetFilter")
public class CommonFilter implements Filter{

private ArrayList<String> urlList;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String url = req.getServletPath();
		
	    boolean allowedRequest = false;
	    if (urlList.contains(url) || url.contains(".css") || url.contains(".png") || url.contains(".jpg") || url.contains("resources")) {
	        allowedRequest = true;
	    }

	    if (!allowedRequest) {
	        HttpSession session = req.getSession(false);
	        if (null == session) {
	            res.sendRedirect("/fasset/login");
	        } else {
	        	if(session.getAttribute("user") != null) {
		        	res.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); 
					res.addHeader("Cache-Control", "post-check=0, pre-check=0");
					res.setHeader("Pragma","no-cache"); 
					res.setDateHeader ("Expires", 0); 
		            chain.doFilter(req, res);
	        	}else {
	        		 res.sendRedirect("/fasset/login");
	        	}
	        }
	    } else {
        	res.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); 
			res.addHeader("Cache-Control", "post-check=0, pre-check=0");
			res.setHeader("Pragma","no-cache"); 
			res.setDateHeader ("Expires", 0);
	        chain.doFilter(req, res);
	    }
		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String urls = config.getInitParameter("avoid-urls");
	    StringTokenizer token = new StringTokenizer(urls, ",");
	    urlList = new ArrayList<String>();
	    while (token.hasMoreTokens()) {
	        urlList.add(token.nextToken());

	    }
	}

}
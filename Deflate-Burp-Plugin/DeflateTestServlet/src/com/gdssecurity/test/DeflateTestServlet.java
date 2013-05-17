package com.gdssecurity.test;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

/**
 * Servlet implementation class for Servlet: DeflateServlet
 *
 */
 public class DeflateTestServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public DeflateTestServlet() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int intLevel = 9;
		boolean isNoWrap = Boolean.parseBoolean(request.getParameter("nowrap"));
		boolean isCompress = Boolean.parseBoolean(request.getParameter("compress"));
		boolean isDeflateHeader = true;
		
		try
		{
			 intLevel = Integer.parseInt(request.getParameter("level")) ;
		}
		catch (NumberFormatException nfex)
		{
			intLevel = 9;
		}
		
		if (request.getParameter("isdeflateheader") != null)
			isDeflateHeader = Boolean.parseBoolean(request.getParameter("isdeflateheader"));
		
		String str = "hello world";
		
		byte data[] = str.getBytes();
		
		response.setContentType("text/html");
		
		OutputStream outStream = response.getOutputStream();
		
		if (isCompress)
		{		
			
			if (isDeflateHeader)
				response.setHeader("Content-Encoding", "deflate");	
        
			outStream.write(com.gdssecurity.utils.Compression.deflate(data,intLevel,isNoWrap));
		}
		else
		{
			outStream.write(data);
		}
		
		outStream.close();
         
	}  	
	   
}
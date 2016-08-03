package com.finder.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.finder.executor.DatabaseHandler;
import com.finder.helper.RequestData;
import com.finder.iofile.ExcelFile;

/**
 * Servlet implementation class DownloadExcel
 */
public class DownloadExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadExcel() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		String email = (String) session.getAttribute("USER_EMAIL");
		if(email != null){
	        String reqId = request.getParameter("requestid");
	        Integer requestId = 0;
	        if(reqId != null){
	        	try{
	        		requestId = Integer.parseInt(reqId);
	        	}catch(NumberFormatException e){
	        		requestId = 0;
	        	}
	        }
	        RequestData data = (RequestData) DatabaseHandler.getRequest(requestId);
	        ExcelFile excelFile = new ExcelFile();
	        String otempFile = excelFile.Write(data);
	        try{  
		        File file = new File(otempFile);
			    OutputStream servletOutputStream = response.getOutputStream();
		        response.setContentType("application/vnd.ms-excel");
		        response.setHeader("Content-Disposition","attachment; filename=ResultExcel.xls");
		        InputStream in =new FileInputStream(otempFile);
		        long length = file.length();
		        byte[] bytes = new byte[(int) length];
		        int offset = 0;
		        int numRead = 0;
		        while (offset < bytes.length && (numRead=in.read(bytes, offset, bytes.length-offset)) >= 0)
		        {
		        offset += numRead;
		        }
		        servletOutputStream.write(bytes);
		        in.close();
		        servletOutputStream.flush();
		        servletOutputStream.close();
		        file.delete();
		    }catch(Exception ee){
		    	ee.printStackTrace();
		    	response.sendRedirect("/finder/pages/account/result.jsp?download=false");
		    }
		}else{
			response.sendRedirect("/finder/pages/timeout.html");
		}
	}

}

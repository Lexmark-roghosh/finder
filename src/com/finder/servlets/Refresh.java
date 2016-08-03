package com.finder.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.finder.executor.DatabaseHandler;
import com.finder.helper.DataPreparator;
import com.finder.helper.FinderConstants;
import com.finder.helper.RequestData;
import com.finder.helper.URLCollector;
import com.finder.helper.ValidationUtils;

/**
 * Servlet implementation class Refresh
 */
public class Refresh extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public Refresh(){
		super();
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String email = (String) session.getAttribute("USER_EMAIL");
		if(email != null){ // check if session timeout
		int reqID = 0;
		Integer requestID = (Integer) session.getAttribute("REQ_ID");
		String rID = request.getParameter("requestid");
		if(rID != null){
				//convert rid to reqID
			try{
				reqID = Integer.parseInt(rID);
			}catch(NumberFormatException e){
					
			}
		}else{
			if(requestID != null){
				reqID = requestID;
			}
		}
		
		boolean inProgressRequestPresent = false;
		RequestData requestData = (RequestData) DatabaseHandler.getRequest(reqID);
		int continiousBrokenCount = DatabaseHandler.getContinuousBrokenCount(email);
		int malformedCount=0;
		int totalClientError=0;
		int totalServerError=0;
		int checkedCount=0;
		int totalValid=0;
		int totalBrokenCount = 0;
		if(DatabaseHandler.requestStillInProgress(reqID)){ 
			inProgressRequestPresent = true;
			request.setAttribute("inProgress", inProgressRequestPresent);
			//handler.updateLatestRequestData(reqID,requestData);//to create
			response.setIntHeader("Refresh", 5);
		}
		if(requestData != null){
			List<URLCollector> uc = requestData.getUrlCollList();
			DataPreparator dp = new DataPreparator(uc);
			String interruptType = requestData.getInterruptionType();
			request.setAttribute("interruptType", interruptType);
			request.setAttribute("contBrokenCount", continiousBrokenCount);
			// ------ Section for All type (HYPERLINK, Doc, CSS, JS, Image) Broken links --------------
			
			checkedCount = requestData.getCheckedList().size();
	        request.setAttribute("checked", checkedCount);
	        totalValid = requestData.getValidList().size();
	        request.setAttribute("valid", totalValid);
	        totalBrokenCount = dp.getChildURLCount(HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("totalbroken", totalBrokenCount);
	        
	        List<String> allChild = dp.getChildURLlist(HttpURLConnection.HTTP_NOT_FOUND);
	        List<String> allParent = dp.getParentURLlist(HttpURLConnection.HTTP_NOT_FOUND);
	        List<String> allAnchor = dp.getAnchorTextList(HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("allBroken", dp.getChildURLgroup(allChild));
	        request.setAttribute("allAnchor", dp.getAnchorTextgroup(allChild, allParent, allAnchor));
	        request.setAttribute("allParent", dp.getParentURLgroup(allChild, allParent));
	     
	        malformedCount = dp.getChildURLCount(FinderConstants.MALFORMED_URL_RESPONSE_CODE);
			request.setAttribute("malformedCount", malformedCount);
			
			totalClientError = dp.getChildURLCount(HttpURLConnection.HTTP_BAD_REQUEST)
									+dp.getChildURLCount(HttpURLConnection.HTTP_UNAUTHORIZED)
									+dp.getChildURLCount(HttpURLConnection.HTTP_PAYMENT_REQUIRED)
									+dp.getChildURLCount(HttpURLConnection.HTTP_FORBIDDEN)
									+dp.getChildURLCount(HttpURLConnection.HTTP_BAD_METHOD)
									+dp.getChildURLCount(HttpURLConnection.HTTP_NOT_ACCEPTABLE)
									+dp.getChildURLCount(HttpURLConnection.HTTP_PROXY_AUTH)
									+dp.getChildURLCount(HttpURLConnection.HTTP_CLIENT_TIMEOUT)
									+dp.getChildURLCount(HttpURLConnection.HTTP_CONFLICT)
									+dp.getChildURLCount(HttpURLConnection.HTTP_GONE)
									+dp.getChildURLCount(HttpURLConnection.HTTP_LENGTH_REQUIRED)
									+dp.getChildURLCount(HttpURLConnection.HTTP_PRECON_FAILED)
									+dp.getChildURLCount(HttpURLConnection.HTTP_ENTITY_TOO_LARGE)
									+dp.getChildURLCount(HttpURLConnection.HTTP_REQ_TOO_LONG)
									+dp.getChildURLCount(HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
			request.setAttribute("totalClientError", totalClientError);
			
			totalServerError = dp.getChildURLCount(HttpURLConnection.HTTP_INTERNAL_ERROR)
									+dp.getChildURLCount(HttpURLConnection.HTTP_NOT_IMPLEMENTED)
									+dp.getChildURLCount(HttpURLConnection.HTTP_BAD_GATEWAY)
									+dp.getChildURLCount(HttpURLConnection.HTTP_UNAVAILABLE)
									+dp.getChildURLCount(HttpURLConnection.HTTP_GATEWAY_TIMEOUT)
									+dp.getChildURLCount(HttpURLConnection.HTTP_VERSION);
			request.setAttribute("totalServerError", totalServerError);
			
			int totalError = totalBrokenCount + malformedCount + totalClientError + totalServerError;
	        request.setAttribute("totalError", totalError);
	        
	        // ------ Section for HYPERLINK, CSS, JS, Image, Document Broken links --------------
	        
	        int totalHyperBrokenCount = dp.getChildURLCount(FinderConstants.HYPERLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("totalHyperBroken", totalHyperBrokenCount);
	        List<String> hyperBroken = dp.getChildURLlist(FinderConstants.HYPERLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        List<String> hyperParent = dp.getParentURLlist(FinderConstants.HYPERLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        List<String> hyperAnchor = dp.getAnchorTextList(FinderConstants.HYPERLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("brokenHyperList", dp.getChildURLgroup(hyperBroken));
	        request.setAttribute("anchorHyper", dp.getAnchorTextgroup(hyperBroken, hyperParent, hyperAnchor));
	        request.setAttribute("parentHyperList", dp.getParentURLgroup(hyperBroken, hyperParent));
	        
	        int totalImgBrokenCount = dp.getChildURLCount(FinderConstants.IMAGELINK, HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("totalImgBroken", totalImgBrokenCount);
	        List<String> imageBroken = dp.getChildURLlist(FinderConstants.IMAGELINK, HttpURLConnection.HTTP_NOT_FOUND);
	        List<String> imageParent = dp.getParentURLlist(FinderConstants.IMAGELINK, HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("brokenImageList", dp.getChildURLgroup(imageBroken));
	        request.setAttribute("parentImageList", dp.getParentURLgroup(imageBroken, imageParent));
	        
	        int totalCSSBrokenCount = dp.getChildURLCount(FinderConstants.CSSLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("totalCSSBroken", totalCSSBrokenCount);
	        List<String> cssBroken = dp.getChildURLlist(FinderConstants.CSSLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        List<String> cssParent = dp.getParentURLlist(FinderConstants.CSSLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("brokenCSSList", dp.getChildURLgroup(cssBroken));
	        request.setAttribute("parentCSSList", dp.getParentURLgroup(cssBroken, cssParent));
	        
	        int totalJSBrokenCount = dp.getChildURLCount(FinderConstants.JSLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("totalJSBroken", totalJSBrokenCount);
	        List<String> jsBroken = dp.getChildURLlist(FinderConstants.JSLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        List<String> jsParent = dp.getParentURLlist(FinderConstants.JSLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("brokenJSList", dp.getChildURLgroup(jsBroken));
	        request.setAttribute("parentJSList", dp.getParentURLgroup(jsBroken, jsParent));
	        
	        int totalDocBrokenCount = dp.getChildURLCount(FinderConstants.DOCLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("totalDocBroken", totalDocBrokenCount);
	        List<String> docBroken = dp.getChildURLlist(FinderConstants.DOCLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        List<String> docParent = dp.getParentURLlist(FinderConstants.DOCLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        List<String> docAnchor = dp.getAnchorTextList(FinderConstants.DOCLINK, HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("brokenDocList", dp.getChildURLgroup(docBroken));
	        request.setAttribute("anchorDoc", dp.getAnchorTextgroup(docBroken, docParent, docAnchor));
	        request.setAttribute("parentDocList", dp.getParentURLgroup(docBroken, docParent));
	        
	        request.setAttribute("inProgress", inProgressRequestPresent);
			getServletContext().getRequestDispatcher("/pages/account/result.jsp").forward(request, response);
		}else{
			response.sendRedirect("/finder/pages/account/myproject.jsp");
		}
	}else{
		response.sendRedirect("/finder/pages/timeout.html");
	}
}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	

}

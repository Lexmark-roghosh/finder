package com.finder.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
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
import com.finder.helper.UserRequestData;
import com.finder.helper.ValidationUtils;

/**
 * Servlet implementation class ProgressReportServlet
 */
public class ProgressReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProgressReportServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		RequestData requestData = null;
		boolean inProgressRequestPresent = false;
		if(session.getAttribute("USER_REQUEST_DATA") != null){
			UserRequestData userRequestData = (UserRequestData) session.getAttribute("USER_REQUEST_DATA");
			requestData = userRequestData.getInProgressRequest();
			if(requestData != null){
				inProgressRequestPresent = true;
			}else{
				requestData = userRequestData.getLatestCompletedRequest();
			}
		}
		
		if(requestData != null){
			List<URLCollector> uc = requestData.getUrlCollList();
			DataPreparator dp = new DataPreparator(uc);
			String startURL = requestData.getStartURL();
			request.setAttribute("startURL", startURL);
			int checkedCount = requestData.getCheckedList().size()-1;
	        request.setAttribute("checked", checkedCount);
	        int totalValid = requestData.getValidList().size();
	        request.setAttribute("valid", totalValid);
	        int totalBrokenCount = dp.getChildURLCount(HttpURLConnection.HTTP_NOT_FOUND);
	        request.setAttribute("totalbroken", totalBrokenCount);
	        int totalClientSideErrorCount = dp.getChildURLCount(HttpURLConnection.HTTP_BAD_REQUEST)
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
	        request.setAttribute("totalClientError", totalClientSideErrorCount);
	        int totalServerSideErrorCount = dp.getChildURLCount(HttpURLConnection.HTTP_INTERNAL_ERROR)
											+dp.getChildURLCount(HttpURLConnection.HTTP_NOT_IMPLEMENTED)
											+dp.getChildURLCount(HttpURLConnection.HTTP_BAD_GATEWAY)
											+dp.getChildURLCount(HttpURLConnection.HTTP_UNAVAILABLE)
											+dp.getChildURLCount(HttpURLConnection.HTTP_GATEWAY_TIMEOUT)
											+dp.getChildURLCount(HttpURLConnection.HTTP_VERSION);
	        request.setAttribute("totalServerError", totalServerSideErrorCount);
	        int malformedErrorCount = dp.getChildURLCount(FinderConstants.MALFORMED_URL_RESPONSE_CODE);
	        request.setAttribute("malformed", malformedErrorCount);
	        int totalError = totalBrokenCount + totalClientSideErrorCount + totalServerSideErrorCount + malformedErrorCount;
	        request.setAttribute("totalError", totalError);
	        
	        request.setAttribute("all404broken", dp.getChildURLlist(HttpURLConnection.HTTP_NOT_FOUND));
	        
	        request.setAttribute("all404parent", dp.getParentURLlist(HttpURLConnection.HTTP_NOT_FOUND));
	        
	        request.setAttribute("completed", inProgressRequestPresent);
		}
		
		if(inProgressRequestPresent){
			requestData.refreshCounter++;
			request.setAttribute("inProgress", inProgressRequestPresent);
			response.setIntHeader("Refresh", 5);
		}
		getServletContext().getRequestDispatcher("/pages/details.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

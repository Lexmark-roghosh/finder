package com.finder.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.finder.executor.DatabaseHandler;
import com.finder.executor.Execute;
import com.finder.helper.FinderConstants;
import com.finder.helper.RequestData;
import com.finder.helper.RequestDataMapper;
import com.finder.helper.ValidationUtils;

/**
 * Servlet implementation class RegLinkValidatorServlet
 */
public class RegLinkValidatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegLinkValidatorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		String userEmail = (String) session.getAttribute("USER_EMAIL");
	if(userEmail != null){
		ValidationUtils utils = new ValidationUtils();
		RequestData presentRequestData = new RequestData();
		String status;
		int reqID = 0;
		Execute execute = new Execute(presentRequestData);
		
		String startURL = request.getParameter(FinderConstants.START_PARAM_NAME);
		startURL = startURL.trim(); 
    	String[] proxy = DatabaseHandler.getProxy(userEmail);
    	String proxyHost = null;//get value from database for requested user,if value = "" change to null
	    String proxyPort = null;//get value from database for requested user,if value = "" change to null
    	if(proxy[2] != null){
    		proxyHost = proxy[0];
    		proxyPort = proxy[1];
    		if(proxyHost.equals("") || proxyPort.equals("")){
    			proxyHost = null;
    			proxyPort = null;
    		}
    	}
    	List<String> startURLs = new ArrayList<String>();
    	startURLs.add(startURL);
	    boolean isStartURLValid = execute.validateStartUrl(startURLs, proxyHost, proxyPort);
	    presentRequestData.setRequestType(FinderConstants.REQUEST_TYPE_FULL_SITE);
	    presentRequestData.setStartURL(startURL);
	    presentRequestData.setGroupName(startURL);
	    if(isStartURLValid){
	    	if(DatabaseHandler.getInProgressRequestID(userEmail) > 0){
	    		status = FinderConstants.REQUEST_PENDING;
	    		DatabaseHandler.storeUserRequestDataAtStartingOfRequest(userEmail, startURL, status, presentRequestData);
	    		getServletContext().getRequestDispatcher("/pages/account/myproject.jsp").forward(request, response);
	    	}else{
	    		status = FinderConstants.REQUEST_IN_PROGRESS;
	    		reqID = DatabaseHandler.storeUserRequestDataAtStartingOfRequest(userEmail, startURL, status, presentRequestData);
	    		utils.executeRequestAsynchronously(userEmail);
	    		session.setAttribute("REQ_ID", reqID);
	    		response.sendRedirect("/finder/Refresh?requestid="+reqID);
	    	}
	    }else{
	    	response.sendRedirect("/finder/pages/account/myproject.jsp?status="+isStartURLValid+"&reason="+Execute.invalidReason);
	    }
	}else{
		response.sendRedirect("/finder/pages/timeout.html");
	}
	    
		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

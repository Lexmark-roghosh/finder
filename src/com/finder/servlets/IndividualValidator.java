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
import com.finder.helper.ValidationUtils;

/**
 * Servlet implementation class IndividualValidator
 */
public class IndividualValidator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndividualValidator() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		String email = (String)session.getAttribute("USER_EMAIL");
		if(email != null){
			String groupName = request.getParameter(FinderConstants.INDIVIDUAL_GROUP_PARAM_NAME);
			groupName = groupName.trim();
			String groupLinks = request.getParameter(FinderConstants.INDIVIDUAL_GROUP_LINK_PARAM_NAME);
			RequestData requestData = null;
			Execute execute = null;
			ValidationUtils utils = new ValidationUtils();
			List<String> listOfGroupLinks = new ArrayList<String>(); 
		    int reqID = 0;
			if(groupLinks != null && groupName != null){
				requestData = new RequestData();
				execute = new Execute(requestData);
				String status = null;
				listOfGroupLinks = execute.convertGroupLinkStringToList(groupLinks);
				requestData.setRequestType(FinderConstants.REQUEST_TYPE_GROUP_LINK);
				requestData.setGroupName(FinderConstants.UNIQUE_PREFIX_TO_DISTINGUISH_GROUPLINKS+groupName);
				requestData.setGroupLinks(listOfGroupLinks);
				requestData.setStartURL(groupLinks);
				String[] proxy = DatabaseHandler.getProxy(email);
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
				boolean isStartURLValid = execute.validateStartUrl(listOfGroupLinks, proxyHost, proxyPort);
				if(isStartURLValid){
					if(DatabaseHandler.getInProgressRequestID(email) > 0){
			    		status = FinderConstants.REQUEST_PENDING;
			    		DatabaseHandler.storeUserRequestDataAtStartingOfRequest(email, groupName, status, requestData);
			    		getServletContext().getRequestDispatcher("/pages/account/myproject.jsp").forward(request, response);
			    	}else{
			    		status = FinderConstants.REQUEST_IN_PROGRESS;
			    		reqID = DatabaseHandler.storeUserRequestDataAtStartingOfRequest(email, groupName, status, requestData);
			    		utils.executeRequestAsynchronously(email);
			    		session.setAttribute("REQ_ID", reqID);
			    		response.sendRedirect("/finder/Refresh");
			    	}
				}else{
					response.sendRedirect("/finder/pages/account/myproject.jsp?valid="+isStartURLValid);
				}
			}else{
				response.sendRedirect("/finder/pages/account/myproject.jsp?status=false");
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

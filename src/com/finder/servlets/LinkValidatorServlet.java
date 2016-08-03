package com.finder.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.finder.executor.DatabaseHandler;
import com.finder.executor.Execute;
import com.finder.helper.FinderConstants;
import com.finder.helper.RequestData;
import com.finder.helper.UserRequestData;

/**
 * Servlet implementation class LinkValidatorServlet
 */
public class LinkValidatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LinkValidatorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		
		boolean inProgressRequestPresent = false;
		
		boolean isStartUrlValid = false;
		UserRequestData userRequestData = null;
		if(session.getAttribute("USER_REQUEST_DATA") != null){
			userRequestData = (UserRequestData) session.getAttribute("USER_REQUEST_DATA");
			inProgressRequestPresent = userRequestData.isAnyInProgressRequest();
		}else{
			//First time
			userRequestData = new UserRequestData();
			session.setAttribute("USER_REQUEST_DATA",userRequestData);
		}
		RequestData presentRequestData = null;
		
		ExecutorService executorService = Executors.newSingleThreadExecutor();
				
		final Execute execute;
		
		if(!inProgressRequestPresent){
			//have to create new request
			  final String startURL = request.getParameter(FinderConstants.START_PARAM_NAME);
		      final String proxyHost = null;
		      final String proxyPort = null;
		      presentRequestData = new RequestData();
		      presentRequestData.setRequestType(FinderConstants.REQUEST_TYPE_FULL_SITE);
		      presentRequestData.setStartURL(startURL);
		      userRequestData.addRequestData(presentRequestData);
		      execute = new Execute(presentRequestData);
		      List<String> allLinks = new ArrayList<String>();
		      allLinks.add(startURL);
		      isStartUrlValid = execute.validateStartUrl(allLinks, proxyHost, proxyPort);
		      if(isStartUrlValid){
		        	//TODO async
			     executorService.execute(new Runnable(){
						@Override
						public void run() {
							execute.executeRequest(startURL, proxyHost, proxyPort, 0, 0);
						}
			        });
			     executorService.shutdown();
			     response.sendRedirect("/finder/ProgressReportServlet?status=true");
		       }else{
					response.sendRedirect("/finder/pages/validator.jsp?status="+isStartUrlValid+"&reason="+Execute.invalidReason);
		       }
		}else{
			presentRequestData = userRequestData.getInProgressRequest();
			response.sendRedirect("/finder/ProgressReportServlet?status=true");
		}	
	       
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	

}

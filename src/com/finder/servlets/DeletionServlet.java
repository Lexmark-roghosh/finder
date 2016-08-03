package com.finder.servlets;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.finder.executor.DatabaseHandler;
import com.finder.helper.RequestData;
import com.finder.helper.RequestDataMapper;
import com.finder.helper.ValidationUtils;

/**
 * Servlet implementation class DeletionServlet
 */
public class DeletionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeletionServlet() {
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
			String deleteID = request.getParameter("deleteid");
			String interruptType = request.getParameter("interrupt");
			RequestData requestData = null;
			int reqID = 0;
			if(deleteID != null){
				//convert rid to reqID
				try{
					reqID = Integer.parseInt(deleteID);
				}catch(NumberFormatException e){
					reqID = 0;
				}
				if(interruptType != null && interruptType.equals("Stop")){
					requestData = (RequestData) DatabaseHandler.getRequest(reqID);
					requestData.setInterrupted(true);
					RequestDataMapper.interruptedMap.put(reqID, "INTERRUPTED");
					DatabaseHandler.updateInProgressRequestWithInterval(reqID, requestData);
					response.sendRedirect("/finder/pages/account/myproject.jsp?inerrupted=true");
				}else if(interruptType != null && interruptType.equals("Delete")){
					boolean deleted = DatabaseHandler.deleteRequest(email,reqID);
					if(deleted){
						response.sendRedirect("/finder/pages/account/myproject.jsp?del="+deleted);
					}else{
						response.sendRedirect("/finder/pages/account/myproject.jsp?del="+deleted);
					}
				}else{
					response.sendRedirect("/finder/pages/account/myproject.jsp?del=false");
				}
			}else{
				response.sendRedirect("/finder/pages/account/myproject.jsp?del=false");
			}
		}else{
			response.sendRedirect("/finder/pages/timeout.html");
		}
	}

}

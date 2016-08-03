package com.finder.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.finder.executor.DatabaseHandler;
import com.finder.helper.FinderConstants;

/**
 * Servlet implementation class AccountUpdationServlet
 */
public class AccountUpdationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountUpdationServlet() {
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
			String firstName = request.getParameter(FinderConstants.UPDATE_FIRST_NAME_FIELD_NAME);
			String lastName = request.getParameter(FinderConstants.UPDATE_LAST_NAME_FIELD_NAME);
			String ofcAddress = request.getParameter(FinderConstants.UPDATE_OFFICE_ADDRESS_FIELD_NAME);
			String pinCode = request.getParameter(FinderConstants.UPDATE_PIN_CODE_FIELD_NAME);
			String contactInfo = request.getParameter(FinderConstants.UPDATE_CONTACT_FIELD_NAME);
			String proxyHost = request.getParameter(FinderConstants.UPDATE_PROXY_HOST_FIELD_NAME);
			String proxyPort = request.getParameter(FinderConstants.UPDATE_PROXY_PORT_FIELD_NAME);
			String proxyStat = request.getParameter(FinderConstants.UPDATE_PROXY_STATUS_FIELD_NAME);
			String continuousBrokenCount = request.getParameter(FinderConstants.UPDATE_CONTINUOUS_BROKEN_FIELD_NAME);
			String continuousBrokenStatus = request.getParameter(FinderConstants.UPDATE_CONTINUOUS_BROKEN_STATUS_FIELD_NAME);
			
			Integer cBrokenCount = 50;
			if(continuousBrokenCount != null){
				try{
					cBrokenCount = Integer.parseInt(continuousBrokenCount);
				}catch(NumberFormatException e){
					cBrokenCount = 50;
				}
			}
			DatabaseHandler.updateAccountSettings(email,firstName,lastName,ofcAddress,pinCode,contactInfo,proxyHost,proxyPort,proxyStat,cBrokenCount,continuousBrokenStatus);
			String updatedfName = DatabaseHandler.getFirstName(email);
			if(updatedfName != null){
				session.setAttribute("FIRST_NAME", updatedfName);
			}
			response.sendRedirect("/finder/pages/account/myproject.jsp");
		}else{
			response.sendRedirect("/finder/pages/timeout.html");
		}
	}

}

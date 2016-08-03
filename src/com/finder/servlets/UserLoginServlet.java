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
 * Servlet implementation class UserLoginServlet
 */
public class UserLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserLoginServlet() {
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
		String email = request.getParameter(FinderConstants.LOGIN_EMAIL_FIELD_NAME);
		String password = request.getParameter(FinderConstants.LOGIN_PASSWORD_FIELD_NAME);
		String firstName;
		boolean correctEmailProvided = DatabaseHandler.userAlreadyRegistered(email);
		boolean valid = false;
		// if FALSE -> wrong email id provided
		if(correctEmailProvided){
			valid = DatabaseHandler.checkLoginCredential(email, password);
			firstName = DatabaseHandler.getFirstName(email);
			if(valid){
				if(firstName.equals(null) || firstName.equals("")){
					firstName = "USER";
				}
				session.setAttribute("USER_EMAIL", email);
				session.setAttribute("FIRST_NAME", firstName);
				response.sendRedirect("/finder/pages/account/myproject.jsp");
			}else{
				// return to login page with message = wrong password
				response.sendRedirect("/finder/pages/login.jsp?wrong=pass");
			}
		}else{
			// return to login page with message = wrong email id provided
			response.sendRedirect("/finder/pages/login.jsp?wrong=uname");
		}
	}
}

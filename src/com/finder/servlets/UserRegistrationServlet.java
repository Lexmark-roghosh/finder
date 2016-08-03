
package com.finder.servlets;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.finder.executor.DatabaseHandler;
import com.finder.helper.FinderConstants;

/**
 * Servlet implementation class UserRegistrationServlet
 */
public class UserRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserRegistrationServlet() {
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
		HttpSession session = request.getSession(true);
		String email = (String) session.getAttribute("REG_EMAIL");
		if(email != null){
			String otpString = request.getParameter("onetimepw");
			Integer actualOTP = 0;
			if(otpString != null){
				try{
					actualOTP = Integer.parseInt(otpString);
				}catch(NumberFormatException e){}
			}
			Integer expectedOTP = (Integer) session.getAttribute("REG_OTP");
			
			if(expectedOTP != null && expectedOTP.equals(actualOTP)){
				String firstName = (String) session.getAttribute("REG_FNAME");
				String lastName = (String) session.getAttribute("REG_LNAME");
				
				String password = (String) session.getAttribute("REG_PASS");
				String ofcAddress = (String) session.getAttribute("REG_OFCADD");
				String pinCode = (String) session.getAttribute("REG_PIN");
				String contactInfo = (String) session.getAttribute("REG_CONTACT");
				String country = null;
				Locale locale = request.getLocale();
				if(locale != null){
					country = locale.toString();
				}
				DatabaseHandler.registerNewUserToDatabase(email, password, firstName, lastName, ofcAddress, pinCode, contactInfo, country);
				// create session
				session.setAttribute("USER_EMAIL", email);
				session.setAttribute("FIRST_NAME", firstName);
				// redirect to user account page
				response.sendRedirect("/finder/pages/account/myproject.jsp");
			}else{
				// return back to verify email page
				response.sendRedirect("/finder/pages/verifyemail.jsp?otp=wrong");
			}
		}else{
			String fpwEmail = (String) session.getAttribute("FPW_EMAIL");
			if(fpwEmail != null){
				String fpwOTPString = request.getParameter("onetimepw");
				Integer fpwActualOTP = 0;
				if(fpwOTPString != null){
					try{
						fpwActualOTP = Integer.parseInt(fpwOTPString);
					}catch(NumberFormatException e){}
				}
				Integer fpwExpectedOTP = (Integer) session.getAttribute("FPW_OTP");
				if(fpwExpectedOTP != null && fpwExpectedOTP.equals(fpwActualOTP)){
					response.sendRedirect("/finder/pages/changepassword.jsp");
				}else{
					// return back to verify email page
					response.sendRedirect("/finder/pages/verifyemail.jsp?otp=wrong");
				}
			}else{
				response.sendRedirect("/finder/pages/timeout.html");
			}
		}
	}
}

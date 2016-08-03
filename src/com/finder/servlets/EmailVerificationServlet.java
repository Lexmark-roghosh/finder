package com.finder.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.finder.executor.DatabaseHandler;
import com.finder.helper.FinderConstants;
import com.finder.helper.ValidationUtils;
import com.finder.iofile.EmailSender;

/**
 * Servlet implementation class EmailVerificationServlet
 */
public class EmailVerificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmailVerificationServlet() {
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
		String email = request.getParameter(FinderConstants.REGISTRATION_EMAIL_FIELD_NAME);
		String hiddenFieldValue = request.getParameter(FinderConstants.HIDDEN_FIELD_NAME);
		if(hiddenFieldValue != null){
			boolean userAlreadyRegistered = false;
			userAlreadyRegistered = DatabaseHandler.userAlreadyRegistered(email);
			if(!userAlreadyRegistered && hiddenFieldValue.equals(FinderConstants.REG_HIDDEN_FIELD_VALUE)){
				String firstName = request.getParameter(FinderConstants.FIRST_NAME_FIELD_NAME);
				String lastName = request.getParameter(FinderConstants.LAST_NAME_FIELD_NAME);
				String password = request.getParameter(FinderConstants.REGISTRATION_PASSWORD_FIELD_NAME);
				String ofcAddress = request.getParameter(FinderConstants.REGISTRATION_OFFICE_ADDRESS_FIELD_NAME);
				String pinCode = request.getParameter(FinderConstants.REGISTRATION_PIN_CODE_FIELD_NAME);
				String contactInfo = request.getParameter(FinderConstants.REGISTRATION_CONTACT_FIELD_NAME);
					
				int OTP = ValidationUtils.generateOTP();
					
				EmailSender.sendEmailWithOTP(OTP, email, FinderConstants.REG_HIDDEN_FIELD_VALUE);
					
				session.setAttribute("REG_FNAME", firstName);
				session.setAttribute("REG_LNAME", lastName);
				session.setAttribute("REG_EMAIL", email);
				session.setAttribute("REG_PASS", password);
				session.setAttribute("REG_OFCADD", ofcAddress);
				session.setAttribute("REG_PIN", pinCode);
				session.setAttribute("REG_CONTACT", contactInfo);
				session.setAttribute("REG_OTP", OTP);
				response.sendRedirect("/finder/pages/verifyemail.jsp");
			}else if(userAlreadyRegistered && hiddenFieldValue.equals(FinderConstants.FORGOTPW_HIDDEN_FIELD_VALUE)){
				int OTP = ValidationUtils.generateOTP();
				EmailSender.sendEmailWithOTP(OTP, email, FinderConstants.FORGOTPW_HIDDEN_FIELD_VALUE);
				session.setAttribute("FPW_EMAIL", email);
				session.setAttribute("FPW_OTP", OTP);
				response.sendRedirect("/finder/pages/verifyemail.jsp");
			}else if(!userAlreadyRegistered && hiddenFieldValue.equals(FinderConstants.FORGOTPW_HIDDEN_FIELD_VALUE)){
				response.sendRedirect("/finder/pages/forgotpassword.jsp?registered="+userAlreadyRegistered);
			}
			else {
				response.sendRedirect("/finder/pages/signup.jsp?reg="+userAlreadyRegistered);
			}
		}else{
			response.sendRedirect("/finder/pages/signup.jsp");
		}
	}

}

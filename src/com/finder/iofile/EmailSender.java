package com.finder.iofile;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import sun.net.smtp.SmtpClient;

import com.finder.helper.FinderConstants;
import com.finder.helper.ValidationUtils;

public class EmailSender {

	private static final Logger logger =Logger.getLogger(EmailSender.class);
	
	public static void sendEmailWithOTP(int OTP, String emailAddress, String reqType){

		// TODO Auto-generated method stub
		
		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		
		Properties props = new Properties(); 
		props.setProperty("mail.transport.protocol", "smtp");  
		props.setProperty("mail.host", "mail.lexmark.com");  
		props.put("mail.smtp.auth", "false"); 
		
		/*//props.put("mail.smtp.port", "465");  
		props.put("mail.debug", "true"); 
		props.put("mail.smtp.starttls.enable", "true");
		//props.put("mail.smtp.socketFactory.port", "465");  
		//props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");*/  
		  
		/*Session session = Session.getInstance(props,new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {  
			return new PasswordAuthentication(FinderConstants.SENDER_EMAIL,	FinderConstants.SENDER_PASSWORD);  
			}  
		});*/ 
		
		Session session = Session.getInstance(props);  
		session.setDebug(true);
		 
		Transport transport = null;
		try {
			transport = session.getTransport();
		} catch (NoSuchProviderException e) {
			logger.error("ERROR : "+e.getMessage());
		}  
		InternetAddress addressFrom = null;
		try {
			addressFrom = new InternetAddress("noreply@lexmark.com", "Lexmark");
		} catch (UnsupportedEncodingException e) {
			logger.error("WRONG EMAIL ADDRESS : "+e.getMessage());
		}  
		   
		MimeMessage message = new MimeMessage(session);  
		try {
			message.setSender(addressFrom);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
			switch(reqType){
			case FinderConstants.REG_HIDDEN_FIELD_VALUE:
				
				message.setSubject(FinderConstants.VERIFICATION_EMAIL_SUBJECT);
				message.setContent(FinderConstants.VERIFICATION_EMAIL_BODY_BEFORE_OTP+
									OTP+
									FinderConstants.VERIFICATION_EMAIL_BODY_AFTER_OTP, "text/html");
				break;
			case FinderConstants.FORGOTPW_HIDDEN_FIELD_VALUE:
				
				message.setSubject(FinderConstants.FORGOTPW_EMAIL_SUBJECT);
				message.setContent(FinderConstants.FORGOTPW_EMAIL_BODY_BEFORE_OTP+
									OTP+
									FinderConstants.FORGOTPW_EMAIL_BODY_AFTER_OTP, "text/html");
				break;
			case FinderConstants.REOTP_HIDDEN_FIELD_VALUE:
				
				message.setSubject(FinderConstants.REOTP_EMAIL_SUBJECT);
				message.setContent(FinderConstants.REOTP_EMAIL_BODY_BEFORE_OTP+
									OTP+
									FinderConstants.REOTP_EMAIL_BODY_AFTER_OTP, "text/html");
				break;
			default:
				throw new RuntimeException("INVALID REQUEST TYPE PASSED !");
			}
			
			transport.connect();
		  	transport.send(message);  
		  	transport.close();
		  	logger.info("EMAIL SENT TO : "+emailAddress);
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error("MESSAGING ERROR : "+e.getMessage());
			logger.info("EMAIL COULD NOT BE SENT TO : "+emailAddress);
		}  
		
	}
}

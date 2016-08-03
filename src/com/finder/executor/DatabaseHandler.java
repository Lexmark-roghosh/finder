package com.finder.executor;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.finder.helper.ConnectionUnavailableException;
import com.finder.helper.FinderConstants;
import com.finder.helper.RequestData;
import com.finder.helper.ValidationUtils;

public class DatabaseHandler {
	
	private static final Logger logger =Logger.getLogger(DatabaseHandler.class);
	private static Properties databaseProp = new Properties();
	static{
		try {
			databaseProp.load(DatabaseHandler.class.getResourceAsStream("/database-config.properties"));
		} catch (FileNotFoundException e) {
			logger.error("Properties File Not Found");
		} catch (IOException e) {
			logger.error("IO Exception loading properties file");
		}
	}
	
	private static final Map<String,Integer> connectionTraceMap = new Hashtable<String, Integer>();
	
	private static Connection openDatabaseConnection(){
		String databaseName = databaseProp.getProperty("DATABASE.NAME");
		databaseName = databaseName.trim();
		String databaseUserName = databaseProp.getProperty("DATABASE.USERNAME");
		databaseUserName = databaseUserName.trim();
		String databasePassword = databaseProp.getProperty("DATABASE.PASSWORD");
		databasePassword = databasePassword.trim();
		String databaseConnectedIP = databaseProp.getProperty("DATABASE.CONNECTED_MACHINE_IP");
		databaseConnectedIP = databaseConnectedIP.trim();
		String DB_CONNECTION_URL = "jdbc:mysql://"+databaseConnectedIP+":3306/";
		Connection connection = null;
		if(databaseName != null && databaseUserName != null && databasePassword != null && databaseConnectedIP != null){
			try {
				Class.forName(FinderConstants.JDBC_DRIVER_CLASS).newInstance();
				connection = DriverManager.getConnection(DB_CONNECTION_URL+databaseName, 
						databaseUserName, databasePassword);
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException | SQLException e) {
				logger.error("Number of open connection: "+connectionTraceMap.get("CONN_COUNT"));
				logger.error("ERROR CONNECTING DATABASE : "+e.getMessage(),e);
				throw new ConnectionUnavailableException(e);
			}
			if(connectionTraceMap.containsKey("CONN_COUNT")){
				connectionTraceMap.put("CONN_COUNT", connectionTraceMap.get("CONN_COUNT")+1);
			}else{
				connectionTraceMap.put("CONN_COUNT",1);
			}
		}
		return connection;
	}
	
	public static Connection getConnection(){
		Connection connection = openDatabaseConnection();
		return connection;
	}
	
	
	public static boolean userAlreadyRegistered(String email){
		//Connection connection = connectToDatabase();
		Statement statement = null;
		String dbEmail = null;
		boolean exists = false;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String query = FinderConstants.QUERY_TO_VERIFY_USER_EXISTANCE;
			statement.executeQuery(query);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				dbEmail = resultSet.getString("email_id");
				if(dbEmail.equals(email)){
					exists = true;
					break;
				}
			}
		} catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
			exists = false;
		}finally{
			closeDatabaseConnection(connection);
		}
		}
		logger.info("USER EXISTS = "+exists);
		return exists;
	}
	
	public static void registerNewUserToDatabase(String email, String password, String fName, String lName, String ofcAddress,
											String pin, String contactNo, String country){
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
			try{
				
				String insert = "INSERT INTO user_account(email_id, password, first_name, last_name, office_address, pin_code, contact, country, account_creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				String creationDate = ValidationUtils.getCurrentTimeStamp();
				PreparedStatement pstmt = connection.prepareStatement(insert);
				pstmt.setString(1, email);
				pstmt.setString(2, password);
				pstmt.setString(3, fName);
				pstmt.setString(4, lName);
				pstmt.setString(5, ofcAddress);
				pstmt.setString(6, pin);
				pstmt.setString(7, contactNo);
				pstmt.setString(8, country);
				pstmt.setString(9, creationDate);
				pstmt.executeUpdate();
				pstmt.close();
				logger.info("SUCCESSFULLY UPDATED USER ACCOUNT TABLE for : "+email);
			} catch (SQLException e) {
				logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
			}
			finally{
				closeDatabaseConnection(connection);
			}
		}
	}
	
	public static boolean checkLoginCredential(String email, String password){
		boolean validUser = false;
		Statement statement = null;
		String dbPass = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String query = "SELECT password FROM user_account WHERE email_id = '"+email+"'";
			statement.executeQuery(query);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				dbPass = resultSet.getString("password");
				if(!dbPass.equals(null)){
					if(dbPass.equals(password)){
						validUser = true;
						break;
					}
				}else{
					logger.error("PASSWORD IS NULL");
				}
			}
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return validUser;
	}
	
	public static String[] getProxy(String email){
		String[] proxy = new String[3];
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String query = "SELECT * FROM user_account WHERE email_id = '"+email+"'";
			statement.executeQuery(query);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				proxy[0] = resultSet.getString("proxy_host");
				proxy[1] = resultSet.getString("proxy_port");
				proxy[2] = resultSet.getString("proxy_status");
				if((proxy[0] != null && proxy[1] != null && proxy[2] != null) && (proxy[0] == "" || proxy[1] == "" || proxy[2].equals("NO"))){
					proxy[0] = null;
					proxy[1] = null;
					proxy[2] = null;
				}
			}
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return proxy;
	}
	
	public static int getContinuousBrokenCount(String email){
		int count = 50;
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String query = "SELECT * FROM user_account WHERE email_id = '"+email+"'";
			statement.executeQuery(query);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				count = resultSet.getInt("continuous_broken");
				String ifApplyBroken = resultSet.getString("apply_broken");
				
				if(ifApplyBroken == null || !ifApplyBroken.equals("YES")){
					count = 0;
				}
			}
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return count;
	}
	public static String getFirstName(String email){
		String first = null;
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String query = "SELECT first_name FROM user_account WHERE email_id = '"+email+"'";
			statement.executeQuery(query);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				first = resultSet.getString("first_name");
			}
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return first;
	}
	public static int storeUserRequestDataAtStartingOfRequest(String email, String startURL, String request_status, Object requestData){
		int reqid = 0;
		String insert = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			insert = "INSERT INTO user_request(email_id, request, request_start, request_status, request_blob) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = connection.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, email);
			pstmt.setString(2, startURL);
			String startTime = ValidationUtils.getCurrentTimeStamp();
			pstmt.setString(3, startTime);
			pstmt.setString(4, request_status);
			pstmt.setObject(5, requestData);
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				reqid = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}catch (SQLException e) {
			logger.error("CAN'T STORE USER DATA AT REQUEST TABLE AT STARTING OF PROCESS"+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return reqid;
	}
	
	public static void updateAccountSettings(String email, String firstName, String lastName, String ofcAddress, String pinCode, String contactInfo, String proxyHost, String proxyPort, String proxyStat, int cBrokenCount, String cBrokenStat){
		String update = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			update = "UPDATE user_account SET first_name = ?, last_name = ?, office_address = ?, pin_code = ?, contact = ?, proxy_host = ?, proxy_port = ?, proxy_status = ?, continuous_broken = ?, apply_broken = ? WHERE email_id = ?";
			PreparedStatement pstmt = connection.prepareStatement(update);
			
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setString(3, ofcAddress);
			pstmt.setString(4, pinCode);
			pstmt.setString(5, contactInfo);
			pstmt.setString(6, proxyHost);
			pstmt.setString(7, proxyPort);
			pstmt.setString(8, proxyStat);
			pstmt.setInt(9, cBrokenCount);
			pstmt.setString(10, cBrokenStat);
			pstmt.setString(11, email);
			pstmt.executeUpdate();
			pstmt.close();
		}catch (SQLException e) {
			logger.error("CAN'T STORE UPDATED USER DATA AT USER ACCOUNT TABLE for email :"+email+". Error - "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
	}
	public static void resetPassword(String email, String password){
		String update = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
			try{
				
				update = "UPDATE user_account SET password = ? WHERE email_id = ?";
				PreparedStatement pstmt = connection.prepareStatement(update);
				pstmt.setString(1, password);
				pstmt.setString(2, email);
				pstmt.executeUpdate();
				pstmt.close();
			}catch(SQLException e) {
				logger.error("CAN'T RESET PASSWORD for email :"+email+". Error - "+e.getMessage());
			}
			finally{
				closeDatabaseConnection(connection);
			}
		}
	}
	public static List<String> getProjects(String email){
		List<String> projects = new ArrayList<String>();
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String query = "SELECT request FROM user_request WHERE email_id = '"+email+"'";
			statement.executeQuery(query);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				projects.add(resultSet.getString("request"));
			}
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return projects;
	}
	public static String getStartURL(int requestId){
		String startURL = null;
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String query = "SELECT request FROM user_request WHERE request_id = "+requestId;
			statement.executeQuery(query);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				startURL = resultSet.getNString("request");
				logger.info("FOUND START URL AS : "+startURL+"; FOR REQ ID = "+requestId);
			}
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return startURL;
	}
	public static int getInProgressRequestID(String email){
		int inProgressRequestID = 0;
		Statement statement = null;
		String status = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String query = "SELECT * FROM user_request WHERE email_id = '"+email+"'";
			statement.executeQuery(query);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				status = resultSet.getString("request_status");
				if(status.equals(FinderConstants.REQUEST_IN_PROGRESS)){
					inProgressRequestID = resultSet.getInt("request_id");
					logger.info("FOUND IN PROGRESS REQUEST ID : "+inProgressRequestID);
					break;
				}
			}
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return inProgressRequestID;
	}
	public static Object getRequest(int requestId){
		Object data = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		if(requestId != 0){
			try{
				String query = "SELECT request_blob FROM user_request WHERE request_id = ?";
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setLong(1, requestId);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()){
					byte[] buf = rs.getBytes(1);
					ObjectInputStream objectIn = null;
					if (buf != null)
						objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
	
					data = objectIn.readObject();
	
					rs.close();
					pstmt.close();
					
				}
			}catch (SQLException | IOException | ClassNotFoundException e) {
				logger.error("ERROR CONVERTING BLOB TO OBJECT : "+e.getMessage(),e);
			}
			finally{
				closeDatabaseConnection(connection);
			}
		}
		}
		return data;
	}
	public static void modifyRequestToInProgress(int requestId){
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			statement = connection.createStatement();
			String insert = "UPDATE user_request SET request_status='"
																+FinderConstants.REQUEST_IN_PROGRESS+
															"',request_start='"
																+ValidationUtils.getCurrentTimeStamp()+
															"' WHERE request_id = "
																+requestId;
			statement.executeUpdate(insert);
			logger.info("SUCCESSFULLY UPDATED REQUEST TABLE (Pending to InProgress) for : "+requestId);
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
	}
	public static void updateInProgressRequestWithInterval(int requestId, Object modifiedRequestData){
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			String update = "UPDATE user_request SET request_blob = ? WHERE request_id = ?";
			PreparedStatement pstmt = connection.prepareStatement(update);
			pstmt.setObject(1, modifiedRequestData);
			pstmt.setLong(2, requestId);
			pstmt.executeUpdate();
			pstmt.close();
			
		}catch (SQLException e) {
			logger.error("ERROR UPDATING REQUEST WITH INTERVAL FOR ID = "+requestId+". Error : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
	}
	public static void storeUserRequestDataAtEndOfRequest(int requestId,Object latestRequestData,Connection connection){
		RequestData data = (RequestData) latestRequestData;
		
		if(connection == null){
			connection = openDatabaseConnection();
		}
		
		if(connection != null){
		try{
			String update = "UPDATE user_request SET request_end = ?, request_blob = ?, request_status = ? WHERE request_id = ?";
			PreparedStatement pstmt = connection.prepareStatement(update);
			logger.info("IF PROCESS INTERRUPTED : "+data.isInterrupted());
			pstmt.setString(1, ValidationUtils.getCurrentTimeStamp());
			pstmt.setObject(2, latestRequestData);
			if(data.isInterrupted()){
				pstmt.setString(3, FinderConstants.REQUEST_INTERRUPTED);
			}else{
				pstmt.setString(3, FinderConstants.REQUEST_COMPLETED);
			}
			pstmt.setLong(4, requestId);
			pstmt.executeUpdate();
			pstmt.close();
			logger.info("SUCCESSFULLY UPDATED REQUEST TABLE (InProgress to Complete) for : "+requestId);
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
	}
	public static int getOldestPendingRequest(String email){
		int oldpendingid = 0;
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String query = "SELECT request_id FROM linkvalidator.user_request WHERE email_id='"+email+"' AND request_status='"+FinderConstants.REQUEST_PENDING+"' ORDER BY request_id ASC";
			statement.executeQuery(query);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				oldpendingid = resultSet.getInt("request_id");
				break;
			}
			logger.info("OLDEST PENDING REQUEST ID : "+oldpendingid);
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return oldpendingid;
	}
	public static boolean requestStillInProgress(int requestId){
		boolean inProgress = false;
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String query = "SELECT request_status FROM user_request WHERE request_id="+requestId;
			statement.executeQuery(query);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				String status = resultSet.getString("request_status");
				if(FinderConstants.REQUEST_IN_PROGRESS.equals(status)){
					inProgress = true;
				}
			}
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return inProgress;
	}
	public static boolean deleteRequest(String email, int requestId){
		boolean deleted = false;
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String delete = "DELETE FROM user_request WHERE request_id="+requestId+" and email_id='"+email+"'";
			statement.executeUpdate(delete);
			deleted = true;
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
			deleted = false;
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return deleted;
	}
	public static boolean checkIfrequestDeleted(String email, int requestId){
		boolean deleted = true;
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
		try{
			
			statement = connection.createStatement();
			String delete = "SELECT request_id FROM user_request WHERE email_id='"+email+"'";
			statement.executeQuery(delete);
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next()){
				if(requestId == resultSet.getInt("request_id")){
					deleted = false;
					break;
				}
			}
		}catch (SQLException e) {
			logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
			deleted = false;
		}
		finally{
			closeDatabaseConnection(connection);
		}
		}
		return deleted;
	}
	public static ResultSet getUserRequestResultSet(String email){
		ResultSet resultSet = null;
		Statement statement = null;
		Connection connection = null;
		connection = openDatabaseConnection();
		if(connection != null){
			try{
				statement = connection.createStatement();
				String query = "SELECT * FROM user_request WHERE email_id = '"+email+"' ORDER BY request_start DESC";
				statement.executeQuery(query);
				resultSet = statement.getResultSet();
			}catch (SQLException e) {
				logger.error("ERROR CREATING STATEMENT : "+e.getMessage(),e);
			}finally{
				closeDatabaseConnection(connection);
			}
		}
		return resultSet;
	}
	private static void closeDatabaseConnection(Connection connection){
		try {
			if(connection != null && !connection.isClosed()){
				connection.close();
				if(connectionTraceMap.containsKey("CONN_COUNT")){
					connectionTraceMap.put("CONN_COUNT", connectionTraceMap.get("CONN_COUNT")-1);
				}
			}
		} catch (SQLException e) {
			logger.error("CAN'T CLOSE CONNECTION");
		}
	}

}

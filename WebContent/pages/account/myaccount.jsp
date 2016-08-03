<%@ page language="java" import="java.util.*,java.lang.*,java.io.*,javax.servlet.http.*,javax.servlet.ServletException,com.finder.helper.FinderConstants,com.finder.executor.*,java.io.IOException,java.sql.*"contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Account Settings</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="/finder/css/style.css">
<link rel="icon" type="image/vnd.microsoft.icon" href="/finder/images/favicon.ico">
<link href="/finder/css/perfect-scrollbar.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    function disableBack() { window.history.forward() }

    window.onload = disableBack();
    window.onpageshow = function(evt) { if (evt.persisted) disableBack() }
});
$(document).ready(function(){
	$('input[id="fname"]').change(function(){
		var fName = $("#fname").val();
		if(fName == null || fName == "" || fName == " "){
			$("#fError").css("display", "block");
			$("#reg").attr('class', 'disable-button');
			$("#reg").attr('disabled', 'disabled');
		}else{
			$("#fError").css("display", "none");
			$("#reg").attr('class', 'button adjust-margin');
			$("#reg").removeAttr('disabled');
		}
	});
	$('#agree').change(function(){
		var hostVal= $('#host').val();
		var portVal = $('#port').val();
		if($('#agree').is(':checked')){
			if($.trim(hostVal) && $.trim(portVal)){
				$('#proxyError').hide();
				$('#agree').val('YES');
			}else{
				$('#proxyError').show();
				$('#agree').val('NO');
			}
		}else{
			$('#agree').val('NO');
		} 
	});
	$('#brokenAgree').change(function(){
		if($('#brokenAgree').is(':checked')){
			$('#brokenAgree').val("YES");
		}else{
			$('#brokenAgree').val("NO");
		} 
	});
	$('#reg').click(function(){
		$('#processing').show();
		$('#loadingover').show();
	});
});

function verifyBrokenCount(){
		var count = $('#BrokenCount').val();
		if($.trim(count)){
			if(count == '0' || count.match(/[^0-9]/g)){
				$("#brokenError").show();
				return false;
			}else{
				$("#brokenError").hide();
				$('#account-form').submit();
			}
		}else{
			$('#brokenError').show();
			return false;
		}
	}
</script>
</head>
<body class=" light-bg">
<!--  Header Start --->

<div class="container-fluid top-head">
  <div class="container">

      <!-- Static navbar -->
      <nav class="navbar navbar-default navbar-custom">
        <div class="container-fluid">
          <div class="navbar-header">
          <a href="#"><img src="/finder/images/logo.png" class="logo" alt="logo"></a>
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
          </div>
          
          <div id="navbar" class="navbar-collapse collapse">
             <ul class="nav navbar-nav navbar-right feedback-link">
             	<li><a href="mailto:roghosh@lexmark.com?Subject=Feedback%20For%20Broken%20Link%20Checker"><span class="glyphicon glyphicon-envelope"></span> Send Feedback</a></li>
             	<li><a href="/finder/pages/account/myproject.jsp"><span class="glyphicon glyphicon-briefcase"></span> My Project</a></li>
                <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><span class="glyphicon glyphicon-user"></span> Welcome <%= session.getAttribute("FIRST_NAME") %><span class="caret"></span></a>
                <ul class="dropdown-menu set-position" role="menu">
                  <li><a href="#">Account Settings</a></li>
                  <li><a href="/finder/help/UserManual.pdf" target="_blank">HELP</a></li>
                  <li><a href="javascript:document.logoutForm.submit()">Logout</a></li>
                 
                </ul>
              </li>
            </ul>
          
          </div><!--/.nav-collapse -->
        </div><!--/.container-fluid -->
      </nav>

     
    </div>
</div>
<!--  Header End ---> 
<!--  Body Content Start --->

<%	HttpSession httpSession = request.getSession(true);
	String email = (String) httpSession.getAttribute("USER_EMAIL");
	Connection connection = null;
	Statement statement = null;
	Properties databaseProp = new Properties();
	try {
		databaseProp.load(DatabaseHandler.class.getResourceAsStream("/database-config.properties"));
		String databaseName = databaseProp.getProperty("DATABASE.NAME");
		databaseName = databaseName.trim();
		String databaseUserName = databaseProp.getProperty("DATABASE.USERNAME");
		databaseUserName = databaseUserName.trim();
		String databasePassword = databaseProp.getProperty("DATABASE.PASSWORD");
		databasePassword = databasePassword.trim();
		String databaseConnectedIP = databaseProp.getProperty("DATABASE.CONNECTED_MACHINE_IP");
		databaseConnectedIP = databaseConnectedIP.trim();
		String DB_CONNECTION_URL = "jdbc:mysql://"+databaseConnectedIP+":3306/";
		Class.forName(FinderConstants.JDBC_DRIVER_CLASS).newInstance();
		connection = DriverManager.getConnection(DB_CONNECTION_URL+databaseName, 
				databaseUserName, databasePassword);
		statement = connection.createStatement();
		String query = "SELECT * FROM user_account WHERE email_id = '"+email+"'";
		statement.executeQuery(query);
		ResultSet resultSet = statement.getResultSet();%>

<div class="container">

    <div id="signup-form">
      <form id="account-form" class="form-horizontal" onsubmit="return verifyBrokenCount()" action="/finder/AccountUpdationServlet" method="post">
      		
        <h1>Update Personal Information</h1>
        <br/>
       			<div id="regError" class="message" style="display:none;">User Already Registered</div>
       	<label id="email">Your email address: <%= email %></label></br>
       		<%while(resultSet.next()){ %>
       	<label>Update your first name</label>
       	<%String first = resultSet.getString("first_name"); %>
        <input type="text" class="normal" name="updateFname" id="fname" value="<%= (first != null) ? first : ""%>">
       		 <div id="fError" class="message" style="display:none;">Enter valid first name</div>
       	<%String last = resultSet.getString("last_name"); %>
       	<label>Update your last name</label>	 
        <input type="text" class="normal" name="updateLname" id="lname" value="<%= (last != null) ? last : ""%>">
		<%String ofcAdd = resultSet.getString("office_address"); %>
        <label>Update office address</label>
        <textarea class="normal" name="updateofcaddress" id="address" rows="3"><%= (ofcAdd != null) ? ofcAdd : ""%></textarea>
      	<%String pin = resultSet.getString("pin_code"); %>
      	<label>Update PIN/ZIP</label>
      	<input type="text" class="normal" name="updatePincode" id="pin" value="<%= (pin != null) ? pin : ""%>">
      	<%String contact = resultSet.getString("contact"); %>
      	<label>Update contact no.</label>
      	<input type="text" class="normal" name="updateContact" id="contact" value="<%= (contact != null) ? contact : ""%>">
      	<%int bCount = resultSet.getInt("continuous_broken"); %>
      	<label>My task will be terminated if broken links are found continuously for the following numbers: </label>
        <input type="text" class=normal id="BrokenCount" name="continuousBroken" maxlength="3" value="<%= bCount%>">
      	<div id="brokenError" class="message" style="display:none;">Enter value between 1 to 999</div>
      	<div>
      		<%String applyBroken = resultSet.getString("apply_broken");
      		  if(applyBroken != null){
      			  if(applyBroken.equals("YES")){%>
      		<input id="brokenAgree" class="radio-full adjust-margin" type="checkbox" name="contBrokenStatus" value="" checked> <label class="control-label" for="message">Apply above settings while validate</label>  
      			<%}else{%>
      		<input id="brokenAgree" class="radio-full adjust-margin" type="checkbox" name="contBrokenStatus" value=""> <label class="control-label" for="message">Apply above settings while validate</label>
      			<%}
      		  }else{%>
      		<input id="brokenAgree" class="radio-full adjust-margin" type="checkbox" name="contBrokenStatus" value=""> <label class="control-label" for="message">Apply above settings while validate</label>
      			<%} %>
      	</div>
      	<label>Are your behind any proxy? If yes, please provide the following details:</label></br>
      	<%String host = resultSet.getString("proxy_host"); %>
      	<label>Proxy host</label>
      	<input type="text" class="normal" id="host" name="proxyHost" value="<%= (host != null) ? host : ""%>">
      	<%String port = resultSet.getString("proxy_port"); %>
      	<label>Port</label>
        <input type="text" class="normal inputtight" id="port" name="proxyPort" value="<%= (port != null) ? port : ""%>">
        
      <div>
      	<%String status = resultSet.getString("proxy_status");
      		if(status != null){
      			if(status.equals("YES")){%>
        <input id="agree" class="radio-full adjust-margin" type="checkbox" name="proxyStatus" value="<%= (status != null) ? status : "" %>" checked> <label class="control-label" for="message">Apply proxy settings for sites to validate</label>	
      			<%}else{ %>
      	<input id="agree" class="radio-full adjust-margin" type="checkbox" name="proxyStatus" value="<%= (status != null) ? status : "" %>"> <label class="control-label" for="message">Apply proxy settings for sites to validate</label>
      			<%}
      		}else{%>
      	<input id="agree" class="radio-full adjust-margin" type="checkbox" name="proxyStatus" value="<%= (status != null) ? status : "" %>"> <label class="control-label" for="message">Apply proxy settings for sites to validate</label>
      		<%} %>
      </div>
      <div id="proxyError" class="message" style="display:none;">Enter proxy host & port</div>
        <div class="form-actions">
          <button id="reg" type="submit" class="button adjust-margin">Update settings</button>
       
        </div>
      </form>
      
    </div>
  </div>
<%}
} catch (Exception e){%>
<div class="container">
 <div id="signup-form">
  <h1>Error !</h1>
  <span class="red">Database Connection Error. Please refresh the page after some time.</span>
 </div>
</div>		
<%	}finally{
		try {
			if(connection != null && !connection.isClosed()){
				connection.close();
			}
		} catch (SQLException e) {
		}
	}
%>
<div id="processing" style='display: none; text-align:center;'>
	<img src="/finder/images/loading-50.gif" />
	<p>Updating...</p>	
</div>
<div id='loadingover' style='display: none;'></div>
<!--  Body Content End ---> 
<!--  Footer Start --->

<div class="container-fluid footer">
  <div class="container">
    <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">© Broken Link Finder. 2015</div>
    <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 right">Powered by &nbsp;<img src="/finder/images/lexmark-logo.png" alt="lexmark logo"></div>
  </div>
</div>
<form name="logoutForm" action="/finder/UserLogoutServlet" method="post"></form>
<!--  Footer End ---> 
</body>
</html>
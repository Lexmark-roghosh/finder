<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.finder.helper.RequestData"%>
<%@ page language="java" import="java.util.*,java.lang.*,java.io.*,javax.servlet.http.*,javax.servlet.ServletException,com.finder.helper.FinderConstants,com.finder.executor.*,java.io.IOException,java.sql.*" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>My Project</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="/finder/css/style.css">
<link rel="stylesheet" type="text/css" media="screen" href="/finder/css/simptip.min.css">
<link rel="stylesheet" href="/finder/css/perfect-scrollbar.css">
<link rel="icon" type="image/vnd.microsoft.icon" href="/finder/images/favicon.ico">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script src="/finder/js/sorttable.js"></script>
<!-- <script src="/finder/js/Chart.js"></script> -->
<script type="text/javascript">
$(document).ready(function() {
    function disableBack() { window.history.forward() }

    window.onload = disableBack();
    window.onpageshow = function(evt) { if (evt.persisted) disableBack() }
});
// Verifying if Start URL is Broken 
$(document).ready(function(){
	var loc = $(location).attr('href');
	var reason = loc.split('&reason=');
	var arr = reason[0].split('?status=');
	var del = loc.split('?del=');
	var val = loc.split('?valid=');
	var itr = loc.split('?inerrupted=');
	
	if(val.length > 1){
		if(val[1] == 'false'){
			$('#groupLinkInvalid').show();
			$('#groupLinkError').hide();
		}
	}
	if(arr.length > 1){
		if(arr[1] == 'false'){
			$('#error1').html("Start URL is "+reason[1]);
			$("#error1").show();
			$("#error2").hide();
		}
	}
	if(del.length > 1){
		if(del[1] == 'true'){
			$('#deleted').show();
			setTimeout(function() {
				$('#deleted').hide();
			},5000);
		}else{
			$('#not-deleted').show();
			setTimeout(function() {
				$('#not-deleted').hide();
			},5000);
		}
	}
	if(itr.length > 1){
		if(itr[1] == 'true'){
			$('#stopping').show();
			setTimeout(function(){
				window.location = '/finder/pages/account/myproject.jsp';
			},5000);
		}
	}
});
$(document).ready(function(){
	$('#addSite').click(function(){
		var url = $('#homepageUrl').val();
		var reg = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
		if(url != null && url != "" && url != " "){
			if(reg.test(url)){
				$('#modalMessage').html(url);
			}else{
				 $('#homepageUrl').val("http://"+url);
				 $('#modalMessage').html("http://"+url);
			}
		}else{
			$("#error2").show();
			$("#error1").hide();
			return false;
		}
	});
});
$(document).ready(function(){
	
	$('#homepageUrl').keypress(function(ev){
		if(ev.keyCode==13){
			ev.preventDefault();
			$('#addSite').click();
		}
	});
});

$(document).ready(function(){
	$('#addLink').click(function(){
		var groupName = document.forms["groupLinks"]["groupname"].value;
		var links = document.forms["groupLinks"]["grouplink"].value;
		if($.trim(links)){
		//if(links != null && links != "" && links != '\n' && links != " "){
			var linkArr = links.split(/[ \r\n]+/);
			var generatedTextAreaContent = [];
			for(var i=0; i<linkArr.length; i++){
				if(linkArr[i] != null && linkArr[i] != " "){
					generatedTextAreaContent.push(linkArr[i]);
					$("#groupNameError").hide();
					$("#groupLinkError").hide();
				}else{
					$("#groupNameError").hide();
					$("#groupLinkError").show();
					return false;
				}
			}
		}else{
			$("#groupNameError").hide();
			$("#groupLinkError").show();
			return false;
		}
		if($.trim(groupName)){
			var grName = groupName.split('http');
			if(grName.length > 1){
				$('#gName').val(grName[1]); 
			}
			$("#groupNameError").hide();
			$("#groupLinkError").hide();
		}else{
			$("#groupNameError").show();
			$("#groupLinkError").hide();
			return false;
		}
	});
});
$(document).ready(function(){
	$('#gName').keypress(function(ev){
		if(ev.keyCode==13){
			ev.preventDefault();
			$('#addLink').click();
		}
	});
});
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
                <li><a href="#"><span class="glyphicon glyphicon-briefcase"></span> My Project</a></li>
                <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><span class="glyphicon glyphicon-user"></span> Welcome <%= session.getAttribute("FIRST_NAME") %><span class="caret"></span></a>
                <ul class="dropdown-menu set-position" role="menu">
                  <li><a href="/finder/pages/account/myaccount.jsp">Account Settings</a></li>
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
	RequestData requestData = null;
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
		String query = "SELECT * FROM user_request WHERE email_id = '"+email+"' ORDER BY request_start DESC";
		statement.executeQuery(query);
		ResultSet resultSet = statement.getResultSet();%>

		
  <div class="container">
    <div class="col-xs-12 col-sm-8 col-md-8 col-lg-8">
    <div id="deleted" class="loading" style="display:none;"><span class="green">Deleted Successfully</span></div>
    <div id="not-deleted" class="loading" style="display:none;"><span class="red">Can't be Deleted. Try again !</span></div>
    <div id="stopping" class="loading" style="display:none;"><span class="red">Trying to stop process. Refresh the page after some time to get current status.</span></div>
      <div class="list-box"> 
        <!--  List title --->
        <div class="list-title">
          <div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
            <p>Project Details (Sorted by Report date)</p>
          </div>
       
          <div style="clear:both;"></div>
        </div>
        <div id="account-page-description">
      <!--  List Category Starts --->
      
      <div id="table-list">
         <table id="rowspan3" cellspacing="0" class="sortable">
          <thead>
            <tr>
              <th class="th1">Date</th>
              <td class="th2">Project</td>
              <th class="th3">Status</th>
              <td class="th4"></td>
            </tr>
          </thead>
          <tbody>
          
          <%while(resultSet.next()){ 
        	  
        	  	SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
          		java.sql.Timestamp start_timestamp = resultSet.getTimestamp("request_start");
          		long milliseconds = start_timestamp.getTime() + (start_timestamp.getNanos() / 1000000);
          		java.util.Date start_date = new java.util.Date(milliseconds);
          		String startdate = format.format(start_date);
          		java.sql.Timestamp end_timestamp = resultSet.getTimestamp("request_end");
          		String enddate = "";
          		if(end_timestamp != null){
          			milliseconds = end_timestamp.getTime() + (end_timestamp.getNanos() / 1000000);
          			java.util.Date end_date = new java.util.Date(milliseconds);
          			enddate = format.format(end_date);
          		}
          %>
            <tr id="<%= resultSet.getInt("request_id") %>">
              <td class="td1"><%= startdate %></td>
              <%byte[] buf = resultSet.getBytes("request_blob");
				ObjectInputStream objectIn = null;
				if (buf != null){
					objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
				}
				Object data = objectIn.readObject();
				requestData = (RequestData)data;
              	String project = requestData.getGroupName(); %>
              	<%if(!project.startsWith(FinderConstants.UNIQUE_PREFIX_TO_DISTINGUISH_GROUPLINKS)){
					String dottedProject = "";
              		char projectChar[] = project.toCharArray();
					if(projectChar.length > 40){
						dottedProject = project.substring(0, 40).concat("...");
					}else{
						dottedProject = project;
					}
					%>
              		<td class="td2" title="<%= project%>"><%= dottedProject %></td>
              	<%}else{ 
              		String grpName[] = project.split(FinderConstants.UNIQUE_PREFIX_TO_DISTINGUISH_GROUPLINKS);
              		%>
              		<td class="td2"><a data-toggle="modal" data-target="#show-group-link" id="<%= resultSet.getInt("request_id") %>" class="showGroupLinks" href="#"><%= grpName[1] %></a>
              		<% 
              		List<String> alllinks = new ArrayList<String>();
					alllinks = requestData.getGroupLinks();
              			for(String eachLink : alllinks){%>
              		<p id="<%= resultSet.getInt("request_id") %>" style="display:none;"><%= eachLink%>@</p>
              			<%} %>
              	<%} %>
              	</td>
              <%if(FinderConstants.REQUEST_IN_PROGRESS.equals(resultSet.getString("request_status"))){ %>
              <td id="<%= resultSet.getInt("request_id") %>" class="td3"><a href="#" id="<%= resultSet.getInt("request_id") %>"><span class="yellow"><%= resultSet.getString("request_status") %></span></a>
              
              <%}else if(FinderConstants.REQUEST_COMPLETED.equals(resultSet.getString("request_status"))){ %>
              	<td id="<%= resultSet.getInt("request_id") %>" class="td3"><a href="#" id="<%= resultSet.getInt("request_id") %>"><span class="green"><%= resultSet.getString("request_status") %></span></a>
              	<p><%= enddate %></p>
              <%}else if(FinderConstants.REQUEST_INTERRUPTED.equals(resultSet.getString("request_status"))){ %>
              	<td id="<%= resultSet.getInt("request_id") %>" class="td3"><a href="#" id="<%= resultSet.getInt("request_id") %>"><span class="red"><%= resultSet.getString("request_status") %></span></a>
              	<p><%= enddate %></p>
              	<p><a href="#" class="simptip-position-left simptip-movable simptip-multiline simptip-warning simptip-smooth" data-tooltip="Interruption can happen due to 3 reasons: You've stopped or Continuous Broken Link count reached or Database Malfunction"><span class="glyphicon glyphicon-info-sign"></span></a></p>
              <%}else{ %>
              <td class="td3"><p class="blue"><%= resultSet.getString("request_status") %></p></td>
              <%} %>
              <%if(FinderConstants.REQUEST_IN_PROGRESS.equals(resultSet.getString("request_status"))){ %>
              <td id="<%= resultSet.getInt("request_id") %>" class="td4"><a id="<%= resultSet.getInt("request_id") %>" title="Stop" href="#" data-toggle="modal" data-target="#confirm-submit-delete"><span class="glyphicon glyphicon-stop red"></span></a></td>
              <%}else if(FinderConstants.REQUEST_PENDING.equals(resultSet.getString("request_status"))){%>
              <td id="<%= resultSet.getInt("request_id") %>" class="td4"><a id="<%= resultSet.getInt("request_id") %>" title="Delete" href="#" data-toggle="modal" data-target="#confirm-submit-delete"><span class="glyphicon glyphicon-trash red"></span></a></td>
              <%}else{ %>
              <td id="<%= resultSet.getInt("request_id") %>" class="td4"><a id="<%= resultSet.getInt("request_id") %>" title="Delete" href="#" data-toggle="modal" data-target="#confirm-submit-delete"><span class="glyphicon glyphicon-trash red"></span></a> | 
              	<%if(!project.startsWith(FinderConstants.UNIQUE_PREFIX_TO_DISTINGUISH_GROUPLINKS)){ %>
              		<a id="<%= resultSet.getInt("request_id") %>" title="Repeat" href="#" data-toggle="modal" data-target="#confirm-submit-site"><span class="glyphicon glyphicon-repeat green"></span></a></td>
              	<% }else{%>
              		<a id="<%= resultSet.getInt("request_id") %>" title="Repeat" href="#" data-toggle="modal" data-target="#confirm-submit-links"><span class="glyphicon glyphicon-repeat green"></span></a></td>
              	<%} %>
              <%} %>
            </tr>
        <%}%>
      
          </tbody>
        </table>
        </div>
        <!--  List Category Starts --->
               
        
        </div>
      </div>
    </div>
    <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 right-bar">
      <div class="white-box">
        <div class="white-box-title">Validate Website
        	<a href="#" class="shortHand simptip-position-top simptip-movable half-arrow simptip-multiline simptip-info simptip-smooth" data-tooltip="Traverse all the successive nodes of 'start url' along with the start url itself"><span class="glyphicon glyphicon-info-sign"></span></a>
        </div>
        <div id="add-website">
        <form id="siteForm" name="fullSite" action="/finder/RegLinkValidatorServlet" method="get">
            <div>
              <input type="text" class="normal" id="homepageUrl" name="starturl" placeholder="Enter Start URL">
            </div>
            <div id="error1" class="message" style="display:none;"></div>
            <div id="error2" class="message" style="display:none;">Enter valid URL</div>
            
            
             <!-- <div class="radio1 radio-inline">
                <input type="radio" id="inlineRadio1" value="option1" name="proxy1" checked>
                <label for="inlineRadio1"> With Proxy </label>
              </div>
              <div class="radio1 radio-inline">
                <input type="radio" id="inlineRadio2" value="option2" name="proxy1">
                <label for="inlineRadio2"> Without Proxy </label>
              </div>
            
            <div>
              <input type="text" class="normal" id="exampleInputEmail1" placeholder="Proxy Host Name">
            </div>
            <div class="">
              <input type="text" class="normal inputtight" id="exampleInputEmail1" placeholder="Port">
            </div>-->

              <!-- <div class="radio-full adjust-margin">
                <input type="radio" id="inlineRadio3" value="option3" name="userchoice1" checked>
                <label for="inlineRadio3"> I want to wait untill the process will end.</label>
              </div>
              <div class="radio-full">
                <input type="radio" id="inlineRadio4" value="option4" name="userchoice1">
                <label for="inlineRadio4">I want to save the result to my account.</label>
              </div> -->
      
            <div class="form-row clearfix">
              <div>
                <button id="addSite" type="button" class="button" data-toggle="modal" data-target="#confirm-submit-site">Add To Project</button>
              </div>
            </div>
          </form>
          </div>
      </div>
      <div class="white-box">
        <div class="white-box-title">Validate Multiple Pages
        	<a href="#" class="shortHand simptip-position-top simptip-movable half-arrow simptip-multiline simptip-info simptip-smooth" data-tooltip="Validate all the links present within provided Pages"><span class="glyphicon glyphicon-info-sign"></span></a>
        </div>
        <div id="add-website">
        <form id="groupForm" name="groupLinks" action="/finder/IndividualValidator" method="get">
            <div>
              <input type="text" class="normal" id="gName" name="groupname" placeholder="Create a group name for links">
              <div id="groupNameError" class="message" style="display:none;">Enter group name</div>
              <textarea class="normal medium" id="linkArea" name="grouplink" placeholder="Enter Individual Page Link separated by Enter key" rows="" cols=""></textarea>
              <div id="groupLinkError" class="message" style="display:none;">Enter valid URLs</div>
              <div id="groupLinkInvalid" class="message" style="display:none;">Invalid URL(s) present</div>
            </div>
            
             <!-- <div class="radio1 radio-inline">
                <input type="radio" id="inlineRadio1" value="option1" name="proxy1" checked>
                <label for="inlineRadio1"> With Proxy </label>
              </div>
              <div class="radio1 radio-inline">
                <input type="radio" id="inlineRadio2" value="option2" name="proxy1">
                <label for="inlineRadio2"> Without Proxy </label>
              </div>
            
            <div>
              <input type="text" class="normal" id="exampleInputEmail1" placeholder="Proxy Host Name">
            </div>
            <div class="">
              <input type="text" class="normal inputtight" id="exampleInputEmail1" placeholder="Port">
            </div>-->
            <div class="form-row clearfix">
              <div>
                <button id="addLink" type="button" class="button" data-toggle="modal" data-target="#confirm-submit-links">Add to project</button>
              </div>
            </div>
          </form>
          </div>
    </div>
  </div>
  <!-- Modal for whole site -->
<div class="modal fade" id="confirm-submit-site" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
      	<h1>Starting to hunt</h1> 
      	<span id="modalMessage" class="blue"></span>
		<p>* With/without proxy, as you set in your account settings.</p> 
		<p>The continuous broken link limit is by default 50 or what you have set in <a href="/finder/pages/account/myaccount.jsp">Account Settings</a> page.</p> 
      </div>
      <div class="modal-footer">
        <button id="cancel1" type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        <button id="submit1" type="button" class="btn btn-success success">Proceed</button></br>
        <span id="checking1" style="display:none;text-align:center;" class="green">Checking Start URL...</span>
      </div>
    </div>
  </div>
</div>
<!-- Modal for list of links  -->
<div class="modal fade" id="confirm-submit-links" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
      	<h1>Starting hunt down for broken links</h1>
      	<p>* With/without proxy, as you set in your account settings.</p>
      	<p>The continuous broken link limit is by default 50 or what you have set in <a href="/finder/pages/account/myaccount.jsp">Account Settings</a> page.</p> 
      </div>
      <div class="modal-footer">
        <button id="cancel2" type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        <button id="submit2" type="button" class="btn btn-success success">Proceed</button></br>
        <span id="checking2" style="display:none;text-align:center;" class="green">Checking URLs...</span>
      </div>
    </div>
  </div>
</div>
<!-- Modal for Delete  -->
<div class="modal fade" id="confirm-submit-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
      	<h1>Want to <span id="del" class="red"></span> ?</h1>
      </div>
      <div class="modal-footer">
        <button id="cancel3" type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        <button id="submit3" type="button" class="btn btn-success success">Confirm</button></br>
        <span id="checking3" style="display:none;text-align:center;" class="green">Please wait...</span>
      </div>
    </div>
  </div>
</div>
<!-- Modal for Showing group links  -->
<div class="modal fade" id="show-group-link" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
	      <h1>Group Links :</h1>
	      <p id="showGrpLnk"></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
      </div>
    </div>
  </div>
</div>

  </div>
<%} catch (Exception e){%>
 <div class="container">
  <div class="col-xs-12 col-sm-8 col-md-8 col-lg-8">
   <div class="list-box">	  
    	<!--  List title --->
      <div class="list-title">
          <div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
            <p>Error !</p>
          </div>
          <div style="clear:both;"></div>
       </div>
       <div id="account-page-description">
      	<!--  List Category Starts --->
      	<div id="table-list">
      		<span class="red">Database Connection Error. Please refresh the page after some time.</span>
      	</div>
      </div>
    </div>
   </div>
  </div>	  
      <%}
finally{
	try {
		if(connection != null && !connection.isClosed()){
			connection.close();
		}
	} catch (SQLException e) {
	}
}       

%> 
  <!--  Body Content End ---> 
  
  <!--  Footer Start --->
  
  <div class="container-fluid footer">
    <div class="container">
      <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">© Broken Link Finder. 2015</div>
      <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 right">Powered by &nbsp;<img src="/finder/images/lexmark-logo.png" alt="lexmark logo"></div>
    </div>
  </div>
  

				<!--  Footer End --->
<script type="text/javascript">
$(document).ready(function (){
	$(".td3 a").click(function(){
			var reqId = $(this).attr("id");
			$('input[name="requestid"]').val(reqId);
			$("#reqForm").submit();
		});
	//$('[data-toggle="tooltip"]').tooltip(); 
});
</script>
<script type="text/javascript">
$(document).ready(function (){
	$(".td4 a").click(function(){
			var reqId = $(this).attr("id");
			var intType = $(this).attr("title");
			if(intType != 'Repeat'){
				$('input[name="deleteid"]').val(reqId);
				$('input[name="interrupt"]').val(intType);
				$('#del').html(intType);
				$('#del-td').html();
			}else{
				// to do when repeat feature will be added
				var project = $('tr#'+reqId+" .td2").attr("title");
				if(project != null){
					//send to full site form
					$('#modalMessage').html(project);
					$('#homepageUrl').val(project);
				}else{
					//send to group link form
					var grpName = $("a#"+reqId+".showGroupLinks").text();
					alert("Groupname = "+grpName);
					var glinks = $('body').find('p#'+reqId).text();
					links = glinks.replace(/\@/g,'\n');
					alert("LINKS= "+links);
					$('#gName').val(grpName);
					$('#linkArea').html(links);
				}
			}
		});
});
</script>
<form name="logoutForm" action="/finder/UserLogoutServlet" method="post"></form>
<form id="reqForm" name="userRequestForm" action="/finder/Refresh" method="get">
<input type="hidden" name="requestid" value="">
</form>
<form id="delForm" name="deleteRequestForm" action="/finder/DeletionServlet" method="post">
<input type="hidden" name="deleteid" value="">
<input type="hidden" name="interrupt" value="">
</form>
<script type="text/javascript">

$(document).ready(function(){
	$('#submit1').click(function(){
		$(this).attr("disabled", "disabled");
		$('#cancel1').attr("disabled", "disabled");
		$('#checking1').show();
		$('#siteForm').submit();
	});
});
$(document).ready(function(){
	$('#submit2').click(function(){
		$(this).attr("disabled", "disabled");
		$('#cancel2').attr("disabled", "disabled");
		$('#checking2').show();
		$('#groupForm').submit();
	});
});
$(document).ready(function(){
	$('#submit3').click(function(){
		$(this).attr("disabled", "disabled");
		$('#cancel3').attr("disabled", "disabled");
		$('#checking3').show();
		$('#delForm').submit();
	});
	$('a.showGroupLinks').click(function(){
			var rqid = $(this).attr('id');
			var links = $('body').find('p#'+rqid).text();
			links = links.replace(/\@/g,'</br>');
			$('#showGrpLnk').html(links);
	});
	
});
</script>

</body>
</html>
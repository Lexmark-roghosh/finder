<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change Password</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="/finder/css/style.css">
<link rel="icon" type="image/vnd.microsoft.icon" href="/finder/images/favicon.ico">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body class=" light-bg">
<!--  Header Start --->

<div class="container-fluid top-head">
  <div class="container"> 
    
    <!-- Static navbar -->
    <nav class="navbar navbar-default navbar-custom">
      <div class="container-fluid">
        <div class="navbar-header"> <a href="/finder/pages/validator.jsp"><img src="/finder/images/logo.png" class="logo" alt="logo"></a>
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar"> <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right feedback-link">
            <li class="active"><a href="/finder/pages/signup.jsp">Signup</a></li>
            <li><a href="/finder/pages/login.jsp">Login</a></li>
            <li><a href="mailto:roghosh@lexmark.com?Subject=Feedback%20For%20Broken%20Link%20Checker"><span class="glyphicon glyphicon-envelope"></span> Send Feedback</a></li>
          </ul>
        </div>
        <!--/.nav-collapse --> 
      </div>
      <!--/.container-fluid --> 
    </nav>
  </div>
</div>
<!--  Header End --->

<!--  Body Content Start ---> 
<div class="container">
    <div id="signup-form">
      <form id="changepassword-form" class="form-horizontal" name="forgotForm" action="/finder/ResetPasswordServlet" method="post">
        <h1>Reset Account Password</h1>
        <br/>
              
        <input type="password" class="normal" name="fpwPassword" id="password" placeholder="New password">
     		<div id="pError" class="message" style="display:none;">Enter password</div>
        <input type="password" class="normal" name="confirmfpwpassword" id="confirm_password" placeholder="Confirm password">
        	<div id="pReError" class="message" style="display:none;">Password not matched</div>
        <div class="form-actions">
          <button id="verify" type="submit" class="button disable-button" disabled>Change password</button>
        </div>
      </form>
    </div>
  </div>
<div id="processing" style='display: none; text-align:center;'>
	<img src="/finder/images/loading-50.gif" />
	<p>Please wait...</p>	
</div>
<div id='loadingover' style='display: none;'></div>
<!--  Body Content End --->

<!--  Footer Start --->

<div class="container-fluid footer-home">
  <div class="container">
    <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">© Broken Link Finder. 2015</div>
    <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 right">Powered by &nbsp;<img src="/finder/images/lexmark-logo.png" alt="lexmark logo"></div>
  </div>
</div>
<script type="text/javascript">
$(document).ready(function() {
    function disableBack() { window.history.forward() }

    window.onload = disableBack();
    window.onpageshow = function(evt) { if (evt.persisted) disableBack() }
});
$(document).ready(function(){
	$('input[id="password"]').change(function(){
		pswrd = $("#password").val();
		rePassword = $("#confirm_password").val();
		if(!$.trim(pswrd)){
			$("#pError").show();
		}else{
			if(pswrd == rePassword){
				$("#pReError").hide();
			}else{
				$("#pReError").show();
			}
			$("#pError").hide();
		}
	});
	$('input[id="confirm_password"]').change(function(){
		rePassword = $("#confirm_password").val();
		if(pswrd == rePassword){
			$("#pReError").hide();
			$('#verify').attr('class','button adjust-margin');
			$("#verify").removeAttr('disabled');
		}else{
			$("#pReError").show();
			$("#verify").attr('class', 'disable-button');
			$("#verify").attr('disabled', 'disabled');
		}
	});
	$('#verify').click(function(){
		$('#processing').show();
		$('#loadingover').show();
	});
});
</script>
<!--  Footer End ---> 
</body>
</html>
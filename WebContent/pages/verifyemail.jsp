<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Email Verification</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="/finder/css/style.css">
<link rel="icon" type="image/vnd.microsoft.icon" href="/finder/images/favicon.ico">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    function disableBack() { window.history.forward() }

    window.onload = disableBack();
    window.onpageshow = function(evt) { if (evt.persisted) disableBack() }
    
    var loc = $(location).attr('href');
	var arr = loc.split('?otp=');
	if(arr.length > 1){
		$("#otpError").show();
	}else{
		$("#otpError").hide();
	}
});
/* $(document).ready(function(){
	var isOTPValid = false;
	var isOTPpresent = false;
	var opw = null;
	
	setInterval(function(){
		opw = $("#otp").val();
		$("#otp").keypress(function (e) {
		  if(opw.length < 5){
		    if (String.fromCharCode(e.keyCode).match(/[^0-9]/g)){
		    	$("#otpError").show();
		    	isOTPValid = false;
		    }else{
		    	$("#otpError").hide();
		    	isOTPValid = true;
		    }
		  }else{
			  
		  }
		});
		if($.trim(opw)){
			//$("#otpError").css("display", "block");
			isOTPpresent = true;
		}else{
			//$("#otpError").css("display", "none");
			isOTPpresent = false;
		}
		if(isOTPValid && isOTPpresent && opw.length == 5){
			$("#verify").attr('class', 'button adjust-margin');
			$("#verify").removeAttr('disabled');
		}else{
			$("#verify").attr('class', 'disable-button');
			$("#verify").attr('disabled', 'disabled');
		}
	},100);
	
}); */
function validateOTP()
{
	var opw = $("#otp").val();
	if($.trim(opw)){
		if(opw.length != 5){
			$("#otpError").show();
			return false;
		}else{
			document.emailVerification-form.submit();
			$('#processing').show();
			$('#loadingover').show();
		}	
	}else{
		$("#otpError").show();
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
        <div class="navbar-header"> <a href="/finder/pages/validator.jsp"><img src="/finder/images/logo.png" class="logo" alt="logo"></a>
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar"> <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right feedback-link">
            <li class="active"><a href="/finder/pages/signup.jsp">Signup</a></li>
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
      <form id="emailVerification-form" class="form-horizontal" name="otpForm" action="/finder/UserRegistrationServlet" onsubmit="return validateOTP()" method="post">
        <h1>Type OTP already send to provided email</h1>
        <br/>
              
        <input type="text" class="normal" name="onetimepw" id="otp" maxlength="5" placeholder="One Time Password">
		
        	<div id="otpError" class="message" style="display:none;">OTP not matched</div>
        <div class="form-actions">
          <button id="verify" type="submit" class="button adjust-margin">Get access</button>
       
        </div>
      </form>
      <!-- <p class="signup-text-link">Email Not Reached? <a href="#">Re-send OTP</a></p> -->
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

<!--  Footer End ---> 

</body>
</html>
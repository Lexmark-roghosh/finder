<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Sign Up</title>
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
});
$(document).ready(function(){
	var loc = $(location).attr('href');
	var arr = loc.split('?reg=');
	if(arr.length > 1){
		if(arr[1] == 'true'){
			$("#regError").show();
		}else{
			$("#regError").hide();
		}
	}else{
		$("#regError").hide();
	}
	var ifValidName = false;
	var ifValidPass = false;
	var ifValidEmail = false;
	//var ifAgreed = false;
	$('input[id="fname"]').change(function(){
		var fName = $("#fname").val();
		if(!$.trim(fName)){
			$("#fError").show();
			ifValidName = false;
		}else{
			$("#fError").hide();
			ifValidName = true;
		}
	});
	$('input[id="email"]').change(function(){
		var eMail = $("#email").val();
		var regex = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		if($.trim(eMail)){
			if(regex.test(eMail)){
				$("#eError").hide();
				ifValidEmail = true;
			}else{
				$("#eError").show();
				ifValidEmail = false;
			}
		}else{
			$("#eError").show();
			ifValidEmail = false;
		}
	});
	var pswrd = null;
	var rePassword = null;
	
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
			ifValidPass = true;
		}else{
			$("#pReError").show();
			ifValidPass = false;
		}
	});
	/* $('input[name="agree"]').change(function(){
		if($('input[id="agree"]').is(':checked') == false){
			$("#aError").show();
			ifAgreed = false;
		}else{
			$("#aError").hide();
			ifAgreed = true;
		}
	}); */
	
	setInterval(function(){
		if(ifValidName == true && ifValidPass == true && ifValidEmail == true){// && ifAgreed == true){
			$("#reg").attr('class', 'button adjust-margin');
			$("#reg").removeAttr('disabled');
		}else{
			$("#reg").attr('class', 'disable-button');
			$("#reg").attr('disabled', 'disabled');
		}
	}, 100);
	
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
        <div class="navbar-header"> <a href="/finder/pages/validator.jsp"><img src="/finder/images/logo.png" class="logo" alt="logo"></a>
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar"> <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right feedback-link">
            <li class="active"><a href="/finder/pages/login.jsp">LogIn</a></li>
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
      <form id="registration-form" class="form-horizontal" action="/finder/EmailVerificationServlet" method="post">
        <h1>Create a New Account <small>( * Fields are required )</small></h1>
        <br/>
       			<div id="regError" class="message" style="display:none;">User already registered</div>
        <input type="text" class="normal" name="regFname" id="fname" placeholder="* First Name">
       		 <div id="fError" class="message" style="display:none;">Enter valid first name</div>
        <input type="text" class="normal" name="regLname" id="lname" placeholder="Last Name">

        <input type="text" class="normal" name="regEmail" id="email" placeholder="* Email address">
        	<div id="eError" class="message" style="display:none;">Enter valid email ID</div>
        <input type="password" class="normal" name="regPassword" id="password" placeholder="* Password">
     		<div id="pError" class="message" style="display:none;">Enter password</div>
        <input type="password" class="normal" name="confirmregpassword" id="confirm_password" placeholder="* Confirm Password">
        	<div id="pReError" class="message" style="display:none;">Password not matched</div>
        <textarea class="normal" name="ofcaddress" id="address" rows="3" placeholder="Office Address"></textarea>
      
      	<input type="text" class="normal" name="regPincode" id="pin" placeholder="Pin/Zip Code">
      	
      	<input type="text" class="normal" name="regContact" id="contact" placeholder="Contact No.">
      	
      	<input type="hidden" name="requestType" id="sHidden" value="">
      	
      <!-- <div>
        
        <input id="agree" class="radio-full adjust-margin" type="checkbox" name="agree"> <label class="control-label" for="message"> * Please agree to our policy</label>
        	<div id="aError" class="message" style="display:none;">Please check our policy</div>
      </div> -->
        <div class="form-actions">
          <button id="reg" type="button" data-toggle="modal" data-target="#confirm-submit" class="disable-button" disabled>Register</button>
       
        </div>
      </form>
      <p class="signup-text-link">Already have an account? <a href="/finder/pages/login.jsp">Login here</a></p>
    </div>
<!-- Modal -->
<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
	      <p>An email containing One Time Password (OTP) will be sent to provided email ID (if not already registered). 
	      Provide OTP at the next page. OTP will be valid for next 30 minutes.</p>
      </div>
      <div class="modal-footer">
        <button id="cancel" type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        <button id="submit" type="button" class="btn btn-success success">I agree</button></br>
        <span id="checking" style="display:none;text-align:center;" class="green">Sending OTP...</span>
      </div>
    </div>
  </div>
</div>
<!-- Modal End -->
  </div>

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
//submitting the form
$(document).ready(function(){
	$('#submit').click(function(){
		$('#sHidden').val('registration-hidden');
		$(this).attr("disabled", "disabled");
		$('#cancel').attr("disabled", "disabled");
		$('#checking').show();
		$('#registration-form').submit();
	});
});
</script>
</body>
</html>
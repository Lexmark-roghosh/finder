<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<title>Broken Link Finder</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="/finder/css/style.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<link rel="icon" type="image/vnd.microsoft.icon" href="/finder/images/favicon.ico">
<script type="text/javascript">
$(document).ready(function() {
    function disableBack() { window.history.forward() }

    window.onload = disableBack();
    window.onpageshow = function(evt) { if (evt.persisted) disableBack() }
});
// Verifying if start URL contains protocol
$(document).ready(function(){
	$('#generate').click(function(){
		var url = $('#homepageUrl').val();
		var reg = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
		if($.trim(url)){
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
			$('#generate').click();
		}
	});
});
// enable / disable Generate button depending upon radio button selection 
$(document).ready(function(){
$('input[type="radio"]').click(function(){
	if("inlineRadio3" == $(this).attr("id"))
	{
		$("#generate").removeClass("disable-button");
		$("#generate").addClass("button");
		$("#generate").removeAttr('disabled');
	}
	if("inlineRadio4" == $(this).attr("id"))
	{
		$("#generate").removeClass("button");
		$("#generate").addClass("disable-button");
		$("#generate").attr("disabled", "disabled");
	}
});
});
// Showing error message if Start URL is Broken 
$(document).ready(function(){
	var loc = $(location).attr('href');
	var reason = loc.split('&reason=');
	var arr = reason[0].split('?status=');
	if(arr.length > 1){
		if(arr[1] == 'false'){
			$('#error1').html("Start URL is "+reason[1]);
			$("#error1").show();
			$("#error2").hide();
		}
	}
});
</script>
</head>
<body>
<!--  Header Start --->

<div class="container-fluid top-head">
  <div class="container">
    <!-- Static navbar -->
    <nav class="navbar navbar-default navbar-custom">
      <div class="container-fluid">
        <div class="navbar-header"> <a href="#"><img src="/finder/images/logo.png" class="logo" alt="logo"></a>
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

<div class="container-fluid body">
  <div class="container">
    <div class="col-xs-12 col-sm-5 col-md-5 col-lg-5 title">
      <h1>Hunt Down</h1>
      <h2>Website Broken Links</h2>
      <p>This tool helps you crawl through your website and picks up all broken links. 
         For the convenience of the webmasters to resolve those links, this tool points out the parent 
		 page of the broken links.<a href="#" data-toggle="modal" data-target="#detail-description">Details</a></p>
      <p><img src="/finder/images/arrow.png" alt="arrow"></p>
    </div>
    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 form-tab">
     <div class="tab-content">
       <h1>Validate Website</h1>
          <form id="fullSite" name="validateFull" action="/finder/LinkValidatorServlet" method="get">
            <div id="form1" class="form-row clearfix">
              <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <input type="text" class="normal" id="homepageUrl" name="starturl" placeholder="Enter Start URL. Example : http://www.example.com/index.html">
              </div>
            </div>
            <div id="error1" class="message" style="display:none;"></div>
            <div id="error2" class="message" style="display:none;">Enter valid URL</div>
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
              <div class="radio space">
                <input type="radio" id="inlineRadio3" value="fulloptionwait" name="fulluserchoice" checked>
                <label for="inlineRadio3"> I want to wait till the process ends.</label>
              </div>
              <div class="radio space">
                <input type="radio" id="inlineRadio4" value="fulloptionsave" name="fulluserchoice">
                <label for="inlineRadio4">I want to save the result to my account. Required to <a href="/finder/pages/signup.jsp"> SIGNUP </a>/<a href="/finder/pages/login.jsp"> LOGIN</a></label>
              </div>
         	</div>
            <div class="form-row clearfix">
              <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <!-- <button  type="submit" class="button">Generate Link Details</button> -->
                <button type="button" id="generate" name="btn" data-toggle="modal" data-target="#confirm-submit" class="button">Generate Link Details</button>
              </div>
            </div>
          </form>
        
      <!-- tab content --> 
    </div>
  </div>
  <!-- Modal -->
<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
      	<h1>Starting hunt down</h1>
      	<span id="modalMessage" class="blue"></span>
      	<p>You can check maximum 500 links within your site.</p> 
      </div>
      <div class="modal-footer">
        <button id="cancel" type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        <button id="submit" type="button" class="btn btn-success success">Proceed</button></br>
        <span id="checking" style="display:none;" class="green">Checking Start URL ... Please wait</span>
      </div>
    </div>
  </div>
</div>
<div class="modal fade" id="detail-description" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
      	<h1>Why do you need link checker?</h1>
      	<p>This tool not only fetches the Broken Links (Http Response Code: 404) 
      	but also lists out all other erroneous pages along with their parent pages 
      	and corresponding http response codes*. A consolidated excel sheet also can 
      	be downloaded* containing all the details. As the error is being pin pointed 
      	it is easier for the developer to fix the error easily. This tool can penetrate 
      	through all the pages within your site, except those which are accessible only 
      	after login. Example - any account pages (if exists within a site).</p>
			<p>* applicable only for the registered users.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
<!-- Modal End -->
</div>
</div>
<!--  Body Content End ---> 

<!--  Footer Start --->

<div class="container-fluid footer-home">
  <div class="container">
    <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">© Broken Link Finder. 2015</div>
    <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 right">Powered by &nbsp;<img src="/finder/images/lexmark-logo.png" alt="lexmark logo"></div>
  </div>
</div>
<script type="text/javascript">
// submitting the form
$(document).ready(function(){
	$('#submit').click(function(){
		$(this).attr("disabled", "disabled");
		$('#cancel').attr("disabled", "disabled");
		$('#checking').show();
		$('#fullSite').submit();
	});
});
</script>

<!--  Footer End --->
</body>
</html>
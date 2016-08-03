<%@ page language="java" import="java.util.List,java.lang.*" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Broken Link Finder</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="/finder/css/style.css">
<link rel="stylesheet" href="/finder/css/perfect-scrollbar.css">
<link rel="icon" type="image/vnd.microsoft.icon" href="/finder/images/favicon.ico">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script src="/finder/js/Chart.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	var isInProgress = <%= request.getAttribute("completed") %>;
	var totalCount = <%= request.getAttribute("totalError") %>;
	if(isInProgress == true){
		$("#gather").show();
		$("#done").hide();
		$("#chartDisplayBox").hide();
	}else{
		$("#gather").hide();
		$("#done").show();
		if(totalCount == 0){
			$("#chartDisplayBox").hide();
		}else{
			$("#chartDisplayBox").show();
		}
	}
});
</script>
</head>
<body class=" light-bg" onload="noBack();" onpageshow="if (event.persisted) noBack();" onunload="">
<!--  Header Start --->

<div class="container-fluid top-head">
  <div class="container">
    <div class="col-lg-6 logo"><a href="/finder/pages/validator.jsp"><img src="/finder/images/logo.png" class="logo" alt="logo"></a></div>
    <div class="col-lg-6 feedback-link"><a href="mailto:roghosh@lexmark.com?Subject=Feedback%20For%20Broken%20Link%20Checker" class="btn-sm"><span class="glyphicon glyphicon-envelope"></span> Send Feedback</a></div>
  </div>
</div>
<!--  Header End ---> 

<!--  Body Content Start --->

<div class="container-fluid">
  <div class="container">
    <div class="col-xs-12 col-sm-8 col-md-8 col-lg-8">
     <div id="gather" class="loading" style="display:none;">Gathering data for : <p><%= request.getAttribute("startURL") %></p><br><img src="/finder/images/loading.GIF"></div>
     <div id="done" class="loading" style="display:none;"><img src="/finder/images/complete-icon.png"><br>Hunting Completed</div>
      <div class="list-box"> 
        <!--  List title --->
        <div class="list-title">
          <div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
            <p>Broken List Details (Including Parent URL)</p>
          </div>
          <div style="clear:both;"></div>
        </div>
        <div id="description">
      <!--  List Category Starts --->
        
        <div class="sub-title"><span class="blue"><%= request.getAttribute("checked") %></span> links Checked<span class="red"> <%= request.getAttribute("totalbroken") %></span> broken links found</div>
        <% List<String> allBroken = (List<String>)request.getAttribute("all404broken");
			 List<String> allParent = (List<String>)request.getAttribute("all404parent");%>
		    <%if(allBroken.size() !=0){ %>
		    	<%for(int i=0;i<allBroken.size(); i++){%>
        <div class="list-dark">
          <ul class="no-list">
            <li class="broken-link"><a target="_blank" href="<%=allBroken.get(i) %>"><%=allBroken.get(i) %></a></li>
            <li class="parent-link"><a target="_blank" href="<%=allParent.get(i) %>"><%=allParent.get(i) %></a></li>
          </ul>
        </div>
        		<%} 
			}%>
        
        </div>
      </div>
    </div>
    <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 right-bar">
      <div id="counterDisplayBox" class="white-box">
        <div class="white-box-title">Link validation summary</div>
        <ul>
        <li>Total Links Checked <span class="mof-lebel"><%= request.getAttribute("checked") %></span></li>
        <li>Total Broken Links Found <span class="red-lebel"><%= request.getAttribute("totalbroken") %></span></li>
        </ul>
      </div>
      <div id="chartDisplayBox" class="white-box" style="display:block;">
        <div class="white-box-title">Error Type</div>
        <div id="canvas-holder" style="margin:auto; margin-top:30px; width:70%; text-align:center;">
          <canvas id="chart-area" width="100" height="100"/>
          </div>
          <p><span> Total Error: <%= request.getAttribute("totalError") %></span><br>Hover to check the Error type</p>
      </div>
    </div>
  </div>
  </div>
  <!--  Body Content End ---> 
  
  <!--  Footer Start --->
  
  <div class="container-fluid footer">
    <div class="container">
      <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">© Broken Link Finder. 2015</div>
      <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 right">Powered by &nbsp;<img src="images/lexmark-logo.png" alt="lexmark logo"></div>
    </div>
  </div>

<!--  Footer End ---> 
<!--  Pie Char Jquery for valu and color ---> 
<script>

		var doughnutData = [
				{
					value: <%= request.getAttribute("totalbroken") %>,
					color:"#F7464A",
					highlight: "#FF5A5E",
					label: "Broken"
				},
				{
					value: <%= request.getAttribute("malformed") %>,
					color: "#46BFBD",
					highlight: "#5AD3D1",
					label: "MalformedURL"
				},
				{
					value: <%= request.getAttribute("totalClientError") %>,
					color: "#FDB45C",
					highlight: "#FFC870",
					label: "ClientSideError"
				},
				{
					value: <%= request.getAttribute("totalServerError") %>,
					color: "#949FB1",
					highlight: "#A8B3C5",
					label: "ServerSideError"
				}
			];

			window.onload = function(){
				var ctx = document.getElementById("chart-area").getContext("2d");
				window.myDoughnut = new Chart(ctx).Doughnut(doughnutData, {responsive : true});
			};
	</script>
    <!--for Scroll Bar jquary--> 
<script src="/finder/js/jquery.mousewheel.js"></script>
<script src="/finder/js/perfect-scrollbar.js"></script>
<script type="text/javascript">
  $(document).ready(function ($) {
	$('#description').perfectScrollbar({
	  wheelSpeed: 20,
	  wheelPropagation: false
	});
  });
</script>
</body>
</html>
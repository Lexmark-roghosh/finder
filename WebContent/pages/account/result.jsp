<%@ page language="java" import="java.util.*,java.lang.*,java.io.*,javax.servlet.http.*,javax.servlet.ServletException,com.finder.helper.*,java.io.IOException,java.sql.*" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Result Page</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="/finder/css/style.css">
<link rel="stylesheet" type="text/css" media="screen" href="/finder/css/simptip.min.css">
<link rel="stylesheet" href="/finder/css/perfect-scrollbar.css">
<link rel="icon" type="image/vnd.microsoft.icon" href="/finder/images/favicon.ico">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="/finder/js/Chart.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	var totalCount = <%= request.getAttribute("totalError") %>;
	if(totalCount == 0){
		$("#chartDisplayBox").hide();
		$("#graphDisplayBox").hide();
	}
});
$(document).ready(function(){
	var isInProgress = <%= request.getAttribute("inProgress") %>;
	var totalChecked = <%= request.getAttribute("checked") %>;
	var intType = "<%= request.getAttribute("interruptType") %>";
	if(isInProgress == true){
		$("#gathering").show();
		$("#chartDisplayBox").hide();
		$("#graphDisplayBox").hide();
	}else if(totalChecked == 0){
		$("#notChecked").show();
	}else if(intType == "userInterruption"){
		$("#userInterrupt").show();
		$("#download").show();
	}else if(intType == "brokenLimitInterruption"){
		$("#brokenInterrupt").show();
		$("#download").show();
	}else if(intType == "databaseInterruption"){
		$("#databaseInterrupt").show();
		$("#download").show();
	}else{
		$("#done").show();
		$("#download").show();
	}
});
function Download(){
	var loc = $(location).attr('href');
	var arr = loc.split('?requestid=');
	if(arr.length > 1){
		$("#req").val(arr[1]);
		document.excelDownloadForm.submit();
	}else{
		alert("Error Downloading File");
	}
}
$(document).ready(function(){
	var loc = $(location).attr('href');
	var arr = loc.split('?download=');
	if(arr.length > 1){
		if(arr[1] == "false"){
			alert("Error Downloading File");
		}
	}
});
</script>
</head>
<body class="light-bg">
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

<div class="container-fluid">
  <div class="container">
    <div class="col-xs-12 col-sm-8 col-md-8 col-lg-8">
     <div id="gathering" class="loading" style="display:none;">Gathering Data <br><img src="/finder/images/loading.GIF"></div>
     <div id="notChecked" class="loading" style="display:none;"><span class="red">No such valid link/s found to check</span></div>
     <div id="done" class="loading" style="display:none;"><img src="/finder/images/complete-icon.png"><br>Completed Successfully</div>
     <div id="userInterrupt" class="loading" style="display:none;"><img width="60" height="60" src="/finder/images/exclamation.png"><br>Interrupted manually</div>
     <div id="brokenInterrupt" class="loading" style="display:none;"><img width="60" height="60" src="/finder/images/exclamation.png"><br>Maximum continious broken count reached. <a id="cont" class="details">show</a></div>
     <div id="databaseInterrupt" class="loading" style="display:none;"><img width="60" height="60" src="/finder/images/exclamation.png"><br>Interrupted due to database malfunction.</div>
      <div class="list-box"> 
        <!--  List title --->
        <div class="list-title">
          <div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
            <p>Broken List Details (Include Parent URL & Anchor text)</p>
          </div>
          <div id="download" class="col-xs-12 col-sm-5 col-md-5 col-lg-5" style="display:none;">
            <div id="dwnBtn" class="dropdown download-list"> <a id="dwn" onclick="return Download()" role="button" aria-expanded="false" class="shortHand simptip-position-top simptip-movable simptip-multiline simptip-warning simptip-smooth" data-tooltip="At report, Response Code = '0' will be showing the pages which are taking more than 10sec to respond">Download Report</a>
              
            </div>
            <div id="vwAllBtn" class="dropdown download-list" style="display:none;">
            	<a id="all" class="details" href="javascript:void();">Back To All Broken View</a>
            </div>
          </div>
          <div style="clear:both;"></div>
        </div>
       <div id="description">
      <!--  List Category Starts For all 404 broken --->
        
       <div id="all" class="sub-title" style="display:block;"><span class="blue"><%= request.getAttribute("checked") %></span> links Checked<span class="red"> <%= request.getAttribute("totalbroken") %></span> broken links found. (Showing latest 50 broken links)</div>
        <%  List<String> allBroken = new ArrayList<String>();
        	allBroken = (List<String>)request.getAttribute("allBroken");
        	List<List<String>> allParent = new ArrayList<List<String>>();
        	allParent = (List<List<String>>)request.getAttribute("allParent");
        	List<List<String>> allAnchor = new ArrayList<List<String>>();
        	allAnchor = (List<List<String>>)request.getAttribute("allAnchor"); %>
        	
			<% for(int k=(allBroken.size()-1); (k > (allBroken.size()-50) && (k >=0)); k--){ %>
	
        <div id="all" class="list-dark" style="display:block;">
          <ul class="no-list">
          <%try{ %>
            <li class="broken-link"><a href="<%=allBroken.get(k) %>" target="_blank"><%=allBroken.get(k) %></a></li>
            
            <%for(int j=0; j<allParent.get(k).size(); j++){ %>
            <li class="parent-link"><a href="<%=allParent.get(k).get(j) %>" target="_blank"><%=allParent.get(k).get(j) %></a></li>
            	<% if(!allAnchor.get(k).get(j).equals(FinderConstants.BLANK_ANCHOR_TEXT)){ %>
            <li class="anchor"><%=allAnchor.get(k).get(j) %></li>
            	<%} %>
            <%} 
          }catch(IndexOutOfBoundsException e){%>
        	  <li class="broken-link">Index Out of bound</li>
         <% } %>
          </ul>
        </div>
        		<%} %>
        <!--  List Category Starts --->
        <!--  List Category Starts For continious broken --->
        <% int count = (Integer)request.getAttribute("contBrokenCount");
        	if(count !=0){
        %>
        <div id="cont" class="sub-title" style="display:none;"><span class="blue">Continious <span class="red"> <%= count %></span> broken links found.<a class="details" id="cont" href="#">Show</a></div>
        <%  List<String> contBroken = new ArrayList<String>();
        	contBroken = (List<String>)request.getAttribute("allBroken");
        	List<List<String>> contParent = new ArrayList<List<String>>();
        	contParent = (List<List<String>>)request.getAttribute("allParent");
        	List<List<String>> contAnchor = new ArrayList<List<String>>();
        	contAnchor = (List<List<String>>)request.getAttribute("allAnchor"); 
        	%>
        	
			<% for(int k=(contBroken.size()-1); (k > (contBroken.size()-count) && (k >=0)); k--){%>
	
        <div id="cont" class="list-dark" style="display:none;">
          <ul class="no-list">
          
            <li class="broken-link"><a href="<%=contBroken.get(k) %>" target="_blank"><%=contBroken.get(k) %></a></li>
            
            <%for(int j=0; j<contParent.get(k).size(); j++){ %>
            <li class="parent-link"><a href="<%=contParent.get(k).get(j) %>" target="_blank"><%=contParent.get(k).get(j) %></a></li>
            	<% if(!contAnchor.get(k).get(j).equals(FinderConstants.BLANK_ANCHOR_TEXT)){%>
            <li class="anchor"><%=contAnchor.get(k).get(j) %></li>
            	<%} %>
            <%} %>
          </ul>
        </div>
        		<%} 
        		}%>
        <!--  List Category Starts For Hyperlink Broken --->
         <div id="hyper" class="sub-title" style="display:none;"><span class="blue">List of <span class="red"><%= request.getAttribute("totalHyperBroken") %></span> Broken Text-links</span></div>
        	<%  List<String> hyperBroken = (List<String>)request.getAttribute("brokenHyperList");
			 	List<List<String>> hyperParent = (List<List<String>>)request.getAttribute("parentHyperList");
			 	List<List<String>> hypAnchor = (List<List<String>>)request.getAttribute("anchorHyper");
			 	
				for(int k= 0; k<hyperBroken.size(); k++){%>
        <div id="hyper" class="list-dark" style="display:none;">
          <ul class="no-list">
          <%try{ %>
            <li class="broken-link"><a href="<%= hyperBroken.get(k) %>" target="_blank"><%= hyperBroken.get(k) %></a></li>
            	<%for(int j=0; j<hyperParent.get(k).size(); j++){ %>
            <li class="parent-link"><a href="<%=hyperParent.get(k).get(j) %>" target="_blank"><%=hyperParent.get(k).get(j) %></a></li>
           		<% if(!hypAnchor.get(k).get(j).equals(FinderConstants.BLANK_ANCHOR_TEXT)){%>
            <li class="anchor"><%=hypAnchor.get(k).get(j) %></li>
            	<%} %>
            <%}
          }catch(IndexOutOfBoundsException e){%>
    	  <li class="broken-link">Index Out of bound hyper</li>
     <% } %>
          </ul>
        </div>
        	<%} 
			%>
			<!--  List Category Starts For Document Broken --->
        <div id="doc" class="sub-title" style="display:none;"><span class="blue">List of <span class="red"><%= request.getAttribute("totalDocBroken") %></span> Broken Documents</span></div>
        	<%  List<String> docBroken = (List<String>)request.getAttribute("brokenDocList");
        		List<List<String>> docParent = (List<List<String>>)request.getAttribute("parentDocList");
        		List<List<String>> docAnchor = (List<List<String>>)request.getAttribute("anchorDoc");
        		
				for(int k= 0; k<docBroken.size(); k++){%>
        <div id="doc" class="list-dark" style="display:none;">
          <ul class="no-list">
          <%try{ %>
            <li class="broken-link"><a href="<%= docBroken.get(k) %>" target="_blank"><%= docBroken.get(k) %></a></li>
            	<%for(int j=0; j<docParent.get(k).size(); j++){ %>
            <li class="parent-link"><a href="<%=docParent.get(k).get(j) %>" target="_blank"><%=docParent.get(k).get(j) %></a></li>
            	<% if(!docAnchor.get(k).get(j).equals(FinderConstants.BLANK_ANCHOR_TEXT)){%>
            <li class="anchor"><%=docAnchor.get(k).get(j) %></li>
            	<%} %>
            	<%}
          }catch(IndexOutOfBoundsException e){%>
    	  <li class="broken-link">Index Out of bound doc</li>
     <% } %>
          </ul>
        </div>
        	<%} 
			%>
			<!--  List Category Starts For Image Broken --->
       <div id="image" class="sub-title" style="display:none;"><span class="blue">List of <span class="red"><%= request.getAttribute("totalImgBroken") %></span> Broken Images</span></div>
        	<%  List<String> imgBroken = (List<String>)request.getAttribute("brokenImageList");
        		List<List<String>> imgParent = (List<List<String>>)request.getAttribute("parentImageList");
			 	
				for(int k= 0; k<imgBroken.size(); k++){%>
        <div id="image" class="list-dark" style="display:none;">
          <ul class="no-list">
          <%try{ %>
            <li class="broken-link"><a href="<%= imgBroken.get(k) %>" target="_blank"><%= imgBroken.get(k) %></a></li>
            	<%for(String parent : imgParent.get(k)){ %>
            <li class="parent-link"><a href="<%= parent %>" target="_blank"><%= parent %></a></li>
            	<%}
          }catch(IndexOutOfBoundsException e){%>
    	  <li class="broken-link">Index Out of bound image</li>
     	<% } %>		
          </ul>
        </div>
        	<%} 
			%>
			<!--  List Category Starts For JS Broken --->
     <div id="js" class="sub-title" style="display:none;"><span class="blue">List of <span class="red"><%= request.getAttribute("totalJSBroken") %></span> Broken JS Files</span></div>
        	<%  List<String> jsBroken = (List<String>)request.getAttribute("brokenJSList");
        		List<List<String>> jsParent = (List<List<String>>)request.getAttribute("parentJSList");
			 	
				for(int k= 0; k<jsBroken.size(); k++){%>
        <div id="js" class="list-dark" style="display:none;">
          <ul class="no-list">
          <%try{ %>
            <li class="broken-link"><a href="<%= jsBroken.get(k) %>" target="_blank"><%= jsBroken.get(k) %></a></li>
            	<%for(String parent : jsParent.get(k)){ %>
            <li class="parent-link"><a href="<%= parent %>" target="_blank"><%= parent %></a></li>
            	<%}
          }catch(IndexOutOfBoundsException e){%>
    	  <li class="broken-link">Index Out of bound js</li>
     <% } %>
          </ul>
        </div>
        	<%} 
			%>
			<!--  List Category Starts For CSS Broken --->
       <div id="css" class="sub-title" style="display:none;"><span class="blue">List of <span class="red"><%= request.getAttribute("totalCSSBroken") %></span> Broken CSS Files</span></div>
        	<%  List<String> cssBroken = (List<String>)request.getAttribute("brokenCSSList");
        		List<List<String>> cssParent = (List<List<String>>)request.getAttribute("parentCSSList");
			 	
				for(int k= 0; k<cssBroken.size(); k++){%>
        <div id="css" class="list-dark" style="display:none;">
          <ul class="no-list">
          <%try{ %>
            <li class="broken-link"><a href="<%= cssBroken.get(k) %>" target="_blank"><%= cssBroken.get(k) %></a></li>
            	<%for(String parent : cssParent.get(k)){ %>
            <li class="parent-link"><a href="<%= parent %>" target="_blank"><%= parent %></a></li>
            	<%}
          }catch(IndexOutOfBoundsException e){%>
    	  <li class="broken-link">Index Out of bound</li>
     <% } %>
          </ul>
        </div>
        	<%} 
			%>
			
        </div>
      </div>
    </div>
    <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 right-bar">
    	<!-- Bar Graph -->
      <div id="graphDisplayBox" class="white-box" style="display:block;">
       <div class="white-box-title">Broken Link Type
       		<a href="#" class="shortHand simptip-position-left simptip-movable simptip-multiline simptip-info simptip-smooth" data-tooltip="The individual bar shows the number of 404 error of the respective link category.
       		 Click 'Details' link below to view."><span class="glyphicon glyphicon-info-sign"></span></a>
       </div>
        <div>
			<canvas id="canvas" height="450" width="600"></canvas>
		</div>
		<div class="bar-graph" >
         <ul>
	         <li><a class="details" id="hyper" href="javascript:void();">Details</a></li>
	         <li><a class="details" id="doc" href="javascript:void();">Details</a></li>
	         <li class="bar-position"><a class="details" id="image" href="javascript:void();">Details</a></li>
	         <li><a class="details" id="js" href="javascript:void();">Details</a></li>
	         <li><a class="details" id="css" href="javascript:void();">Details</a></li>
         </ul>
         
        </div>
			
      </div> 
      	<!-- Bar Graph End -->
      	<!-- Error Summery  -->
      <div id="counterDisplayBox" class="white-box">
        <div class="white-box-title">Link validation Summary
        	<a href="#" class="shortHand simptip-position-left simptip-movable simptip-multiline simptip-info simptip-smooth" data-tooltip="Total link checked : this count indicates total number of URLs the tool has traversed &#10;
        	 Total broken link found : this count indicates total 404 error pages within 'Total link checked'"><span class="glyphicon glyphicon-info-sign"></span></a>
        </div>
        <ul>
        <li>Total Link Checked<span class="green-lebel"><%= request.getAttribute("checked")%></span></li>
        <li>Total Broken link Found <span class="red-lebel"><%= request.getAttribute("totalbroken") %></span></li>
        
        </ul>
      </div>
      
       		<!--  Error Summery end ---> 
          	<!--  Error Type Graph Start ---> 
      <div id="chartDisplayBox" class="white-box" style="display:block;">
        <div class="white-box-title">Error Type</div>
        <div id="canvas-holder" style="margin:auto; margin-top:30px; width:70%; text-align:center;">
          <canvas id="chart-area" width="100" height="100"/>
        </div>
          <p><span> Total Error: <%= request.getAttribute("totalError") %></span><br>Hover To check the Error type</p>
      </div>
      		<!--  Error Type Graph end ---> 
         	 
    </div>
  </div>
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
<!--  Pie Char Jquery for valu and color ---> 
<script>

		var doughnutData = [
				{
					value: <%= (request.getAttribute("totalbroken") != null) ? request.getAttribute("totalbroken") : 0 %>,
					color:"#F7464A",
					highlight: "#FF5A5E",
					label: "404 Broken"
				},
				{
					value: <%= (request.getAttribute("malformedCount") != null) ? request.getAttribute("malformed") : 0 %>,
					color: "#46BFBD",
					highlight: "#5AD3D1",
					label: "MalformedURL"
				},
				{
					value: <%= (request.getAttribute("totalClientError") != null) ? request.getAttribute("totalClientError") : 0 %>,
					color: "#FDB45C",
					highlight: "#FFC870",
					label: "ClientSideError"
				},
				{
					value: <%= (request.getAttribute("totalServerError") != null) ?  request.getAttribute("totalServerError") : 0%>,
					color: "#949FB1",
					highlight: "#A8B3C5",
					label: "ServerSideError"
				}

			];

		var randomScalingFactor = function(){ return Math.round(Math.random()*100)};

		var barChartData = {
			labels : ["Page","Doc","Img","JS","CSS"],
			datasets : [
				
				{
					fillColor : "rgba(151,187,205,0.5)",
					strokeColor : "rgba(151,187,205,0.8)",
					highlightFill : "rgba(151,187,205,0.75)",
					highlightStroke : "rgba(151,187,205,1)",
					data : [<%= request.getAttribute("totalHyperBroken")%>,<%= request.getAttribute("totalDocBroken")%>,<%= request.getAttribute("totalImgBroken")%>,<%= request.getAttribute("totalJSBroken")%>,<%= request.getAttribute("totalCSSBroken")%>]
				}
			]

		}
		window.onload = function(){
			var ctx = document.getElementById("canvas").getContext("2d");
			window.myBar = new Chart(ctx).Bar(barChartData, {
				responsive : true
			});
			var ctx = document.getElementById("chart-area").getContext("2d");
					window.myDoughnut = new Chart(ctx).Doughnut(doughnutData, {responsive : true});
		}
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
  $(document).ready(function(){
	  $('a.details').click(function(){
		  var id = $(this).attr('id');
		  $('#description div').hide();
		  $('div#'+id).show();
		  if(id == 'all'){
			  $('#dwnBtn').show();
			  $('#vwAllBtn').hide();
		  }else{
			  $('#vwAllBtn').show();
			  $('#dwnBtn').hide();
		  }
	  });
  });
</script>
<form name="logoutForm" action="/finder/UserLogoutServlet" method="post"></form>
<form id="dwnldForm" name="excelDownloadForm" action="/finder/DownloadExcel" method="post">
<input type="hidden" id="req" name="requestid" value="">
</form>
</body>
</html>
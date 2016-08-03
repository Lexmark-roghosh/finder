<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>registration</title>
</head>
<body>
Register Here <br/>
<form action="/finder/UserRegistrationServlet" onsubmit="return fieldValidation()"method="POST">
<input type="text" name="regFname" placeholder="First Name">
<input type="text" name="regLname" placeholder="Last Name">
<input type="text" name="regEmail" placeholder="Email">
<input type="password" name="regPassword" placeholder="Password">
<input type="submit" value="Register">
</form>
Sign in Here <br/>
<form action="/finder/UserLoginServlet" onsubmit="return loginValidation()"method="POST">
<input type="text" name="loginEmail" placeholder="Email">
<input type="password" name="loginPassword" placeholder="Password">
<input type="submit" value="Sign In">
</form>
	wrong <h3><%= request.getAttribute("WRONG") %>
</body>
</html>
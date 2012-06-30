<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="me.benmordue.gaetest.PMF" %>
<%@ page import="me.benmordue.gaetest.ReadItUser" %>

<%@ taglib prefix="mytest" uri="http://www.tomcat-demo.com/testing"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>
</head>
<body>


<p>Test block</p>
<mytest:listattr />
<p>End of test block</p>

<%
    
    ReadItUser myUser = (ReadItUser) session.getAttribute("readItUser");
    
    if (myUser != null) {
%>
<p>Hello, <%= myUser.getTwitterScreenName() %>! (You can
<a href="/logout">sign out</a>.)</p>
<p>See your list of favourites <a href="lister.jsp">here</a>.</p>
<%
    } else {
%>
<p>Welcome!
<a href="/login">Sign in</a>
to use the service.</p>
<%
    }
%>

</body>
</html>
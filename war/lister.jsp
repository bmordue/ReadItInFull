<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
<title>Read It In Full</title>

<script type="text/javascript" src="https://www.google.com/jsapi?key=ABQIAAAA9l3qy-cONBbRT9UC7zeCSRRQGDklHMpeozCb8OrviK90TAIg_hS9P02k2kBczOAoOjD565m_bTaDmA"></script>
<script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.10.custom.min.js"></script>


<!-- jQuery stuff -->
<script type="text/javascript">
$(document).ready(function() {
	   // do stuff when DOM is ready
	   
	$('tbody#rows').load('/list');   

	$('#hidenames').click(function() {$('tr td:first-child').css('display','none'); });

	$('signout').click(function() {$.load('/logout');});

	$('.pageno').click(function() { $('tbody#rows').load('/list?page='+$(this).text());} );
	
	//checkbox to select all, from http://briancray.com/posts/check-all-jquery-javascript/
	$(function () {
	    $('.checkall').click(function () {
	        $(this).parents('fieldset:eq(0)').find(':checkbox').attr('checked', this.checked);
	    });
	});
	
	 });	//document ready

</script>


<link type="text/css" href="css/ui-lightness/jquery-ui-1.8.10.custom.css" rel="Stylesheet" />	

</head>
<body>
<%@ page import="twitter4j.Twitter" %>
<%@ page import="twitter4j.Status" %>
<%@ page import="twitter4j.User" %>
<%@ page import="twitter4j.AccountTotals" %>

<%@ page import="java.util.List" %>


<%
	Twitter twitter = (Twitter) session.getAttribute("twitter");
	
	User user = null;
	if (twitter != null) {
		try {
			user = twitter.verifyCredentials();
		} catch (Exception e) {
			// user will be null
		}
	}
    if (user != null) {
	AccountTotals totals = twitter.getAccountTotals();

%>
<h3><%= user.getName() %>, you have <%=totals.getFavorites() %> favourites</h3>


<ol>
<li>Select articles to send to Instapaper.</li>
<li>Enter your Instapaper login details.</li>
<li>Click 'Send'.</li>
</ol>
<form method="post" action="/send">
Instapaper username: <input type=text name="username" /> <br/>
Instapaper password: <input type=password name="password" /> <br />
<!--   <input type=checkbox name='remember'>Remember Instapaper password?</input><br />-->
<input type=checkbox name='removefavourites'>Remove Twitter favourites after
sending to Instapaper?</input><br />
<input type=submit value="Send"> <br />

<!--   <p><a href='#' id='hidenames'></p>Hide user names</p></a> -->
<fieldset>
<table class='pageme'>
<thead><tr>
<th>User</th><th><input type="checkbox" class="checkall" /> Select all</th><th>Tweet</th>
</tr></thead>
<tbody id='rows'>
<tr><td>Loading...</td></tr>
</tbody>
</table>
</form>
</fieldset>

<!-- Paging for table of Twitter favourites -->
 
<%
int maxPage = totals.getFavorites()/20;
for (int i=0;i<maxPage;i++) {
	%><a class="pageno" href="#"><%=i+1 %></a>&nbsp;   <% 
}
%>


<!-- If user is not correctly logged in: -->
<%} else {
	%>
	<p>You are not logged in.</p>
	<%
}%>




  </body>
</html>

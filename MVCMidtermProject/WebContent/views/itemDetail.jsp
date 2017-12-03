<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Item Detail</title>
<%@ include file="header.jsp"%>
</head>
<body>
	<%@ include file="navbar.jsp"%>
	<!-- this page should display an item's info 
		the item object is named "requestedItem"
		if the user has adequate permissions, a user object corresponding to the item's owner is added; it is named "itemOwner"
		if the user is the owner of the item, a boolean is added to the model to indicate that; it is named "authUserIsItemOwner" and it will be set to true
		
	 -->
	 <c:if test="${item != null }">
	<div>
		${item.title }
	</div>
	</c:if>
	
<!-- ==== This has more to do, but in a browser if you enter:
	http://localhost:8080/MVCMidtermProject/itemDetail.do?itemId=1
	In the console you can see the correct queries coming back from 
	the db in the console.==== -->
	
	
	 
	 <%@ include file="footer.jsp"%>
</body>
</html>
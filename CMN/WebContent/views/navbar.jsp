<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- The head and body isn't needed here b/c it is referenced in the other jsp
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Microsanctuary Equipment Exchange</title>
<%@ include file="header.jsp"%>

<link rel="stylesheet" type="text/css" href="css/Main.css">
</head>
<body> --%>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark"> 
		<a class="navbar-brand" href="index.do"> 
			<img src="images/chickenicon.jpeg" width="30" height="30" class="d-inline-block align-top" alt="microsanctuary logo">
			Microsanctuary Equipment Exchange
		</a>
		<a class="btn btn-social btn-facebook" href="https://www.facebook.com/Microsanctuary/" target="_blank" title="Colorado Microsanctuary Network Facebook"> 
				<span class="fa fa-facebook"></span> 
				Microsanctuary FB
			</a>
	</nav>
	
	<!-- 	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarNavAltMarkup"> -->
		
		<div class="navbar navbar-expand-md navbar-dark bg-dark">
			<ul class="nav navbar-nav">
				
				<li>
					<a class="text-white" href="showSearchPage.do">
						<span class="glyphicon glyphicon-user"></span>
						Borrow
					</a>
				</li>
				<li>
					<a class="text-white" href="showAllItems.do">Browse</a>
				</li>
				<c:if test="${sessionScope.loggedIn == true}">
					<li>
						<a class="text-white" href="showAddItem.do">Lend</a>
					</li>
					<li>
						<a class="text-white" href="getRequestsSentToUser.do"> Requests</a>
					</li>
					<li>
						<a class="text-white" href="showUserItems.do">My Items</a>
					</li>
					<li>
						<a class="text-white" href="showUserUpdateInfo.do">Account</a>
					</li>
					
				</c:if>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<c:if test="${sessionScope.loggedIn == false || sessionScope.loggedIn == null}">
					<li>
						<a class="text-white" href="getNewUser.do">
							<span class="glyphicon glyphicon-user"></span> 
							Sign Up
						</a>
					</li>
					<li>
						<a class="text-white" href="showLogin.do">
							<span class="glyphicon glyphicon-log-in"></span> 
							Login
						</a>
					</li>
				</c:if>
				<c:if test="${sessionScope.loggedIn == true}">
					<li>
						<a class="text-white" href="userLogout.do">
							<span class="glyphicon glyphicon-user"></span> 
							Logout
						</a>
					</li>
				</c:if>
				<c:if test="${sessionScope.authenticatedUser.permissionLevel == 2}">
					<li>
						<a class="text-white" href="showAdminPage.do">
							Admin
						</a>
					</li>
				</c:if>
			</ul>
			</div>
			


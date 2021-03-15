<!-- <h1>hello shilpa this is landing page</h1> -->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class='bg-login'>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<!-- <meta name="viewport" content="width=device-width, initial-scale=1" /> -->
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title><spring:message code="APP_TITLE" /></title>

<spring:url value="/resources/images/logo-top.png" var="logoTopIMG" />
<spring:url value="/resources/css/bootstrap.min.css" var="styleCSSBoostrap" />
<spring:url value="/resources/css/style.css" var="styleCSS" />
<spring:url value="/resources/css/icon.css" var="vimeoCSS" />
<spring:url value="/resources/css/font-awesome.min.css" var="chosenCSSfont" />
<spring:url value="/resources/images/favicon.ico" var="favicon" />

<spring:url value="/resources/js/jquery.min.js" var="jquerymin" />
<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapmin" />
<spring:url value="/resources/js/modernizr.min.js" var="modernizrmin" />
<spring:url value="/resources/js/detect.js" var="detect" />
<spring:url value="/resources/js/fastclick.js" var="fastclick" />
<spring:url value="/resources/js/jquery.slimscroll.js" var="slimscroll" />
<spring:url value="/resources/js/jquery.blockUI.js" var="blockUI" />
<spring:url value="/resources/js/waves.js" var="waves" />
<spring:url value="/resources/js/wow.min.js" var="wowmin" />
<spring:url value="/resources/js/jquery.nicescroll.js" var="nicescroll" />
<spring:url value="/resources/js/jquery.scrollTo.min.js" var="scrollTo" />
<spring:url value="/resources/js/app.js" var="app" />

<spring:url value="/resources/js/validations.js" var="valid" />
<script type="text/javascript" src="${jquerymin}"></script>
<c:url value="/j_spring_security_logout" var="logoutUrl" />
<link rel="shortcut icon" href="${favicon}" type="image/x-icon">
<link rel="icon" href="${favicon}" type="image/x-icon">
<link href="${styleCSSBoostrap}" rel="stylesheet" />
<link href="${styleCSS}" rel="stylesheet" />
<link href="${chosenCSSfont}" rel="stylesheet" />
<link rel='stylesheet' type='text/css' href='//fonts.googleapis.com/css?family=Open+Sans:400,300,600&amp;subset=cyrillic,latin'>
<link href='https://fonts.googleapis.com/css?family=Raleway:800,700,600,500,400,300' rel='stylesheet' type='text/css' />
<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>
<body class="index">
<header> 
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<c:set var = "uForm" value = '<%=request.getSession().getAttribute("userForms")%>' />
	<c:set var = "allForm" value = '<%=request.getSession().getAttribute("allforms")%>' />
	<c:set var = "role" value = '<%=request.getSession().getAttribute("iRole")%>' />
	<c:set var = "subscibed" value = '<%=request.getSession().getAttribute("fassetub")%>' />
</header>
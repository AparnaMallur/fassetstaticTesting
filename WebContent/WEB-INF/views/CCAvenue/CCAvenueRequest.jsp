<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form id="nonseamless" method="post" name="redirect" action="https://secure.ccavenue.com/transaction/transaction.do?command=initiateTransaction"> 
	<input type="hidden" id="encRequest" name="encRequest" value="${encRequest}"/>
	<input type="hidden" name="access_code" id="access_code" value="${accessCode}"/>
</form:form>
<script>
//alert("encRequest : ${encRequest}  access_code : ${accessCode}");
	document.redirect.submit();
</script> 

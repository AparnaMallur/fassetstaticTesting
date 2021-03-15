<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<form:form id="nonseamless" method="post" name="redirect" action="https://test.ccavenue.com/transaction/transaction.do?command=initiateTransaction"> 
	<input type="text" id="encRequest" name="encRequest" value="${encRequest}"/>
	<input type="text" name="access_code" id="access_code" value="${accessCode}"/>
</form:form>
<script>
	document.redirect.submit();
</script> 
	
<%@include file="/WEB-INF/includes/footerLogin.jsp"%>


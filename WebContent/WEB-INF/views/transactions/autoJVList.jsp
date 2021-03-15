<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Auto JV's</h3>
	<a href="homePage">Home</a> 
	<a href="payrollAutoJVList">Payroll JV</a>
	<a href="gstAutoJVList">GST JV</a>
	<a href="#">Depreciation JV</a>
</div>	
<%@include file="/WEB-INF/includes/footer.jsp" %>
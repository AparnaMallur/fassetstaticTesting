<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Accounting Year</h3>					
	<a href="homePage">Home</a> » <a href="accountingYearList">Accounting Year</a> » <a href="#">View</a>
</div>
	<div class="fassetForm">
		<form:form id="AccountYearForm"  commandName = "year">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Account Group Name:</strong></td>
						<td style="text-align: left;">${year.year_range}</td>			
					</tr>
					<tr>
						<td><strong>Start Date:</strong></td>
						<fmt:parseDate value="${year.start_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="start_date" type="date" pattern="dd-MM-yyyy" />
						<td style="text-align: left;">${start_date}</td>			
					</tr>
					<tr>
						<td><strong>End Date:</strong></td>
						<fmt:parseDate value="${year.end_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="end_date" type="date" pattern="dd-MM-yyyy" />
						<td style="text-align: left;">${end_date}</td>			
					</tr>
					<tr>
						<td><strong>Status:</strong></td>
						<td style="text-align: left;">${year.status==true ? "Enable" : "Disable"}</td>			
					</tr>
					
				</table>
			</div>
			<div class="col-md-12 text-center" >
				<button class="fassetBtn" type="button" onclick = "editAccountingYear(${year.year_id})">
					<spring:message code="btn_edit" />
				</button>
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
<script type="text/javascript">

		function editAccountingYear(id){
			window.location.assign('<c:url value="editAccountingYear"/>?id='+id);
		}
	
	function back(){
		window.location.assign('<c:url value = "accountingYearList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
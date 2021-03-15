<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Executive Timesheet</h3>					
	<a href="homePage">Home</a> » <a href="executiveTimesheetList">Executive Timesheet</a> » <a href="#">View</a>
</div>
	<div class="fassetForm">
		<form:form id="ExecutiveTimesheetForm"  commandName = "executiveTimesheet">
			<div class="row">
				<table class = "table">
					<tr>
						<td><strong>Date:</strong></td>
						<td style="text-align: left;">${executiveTimesheet.date}</td>			
					</tr>
				</table>
			</div>
			<div class='row'>
					<table class="table table-bordered table-striped" id="detailTable">
						<thead>
							<tr>
								<th>Company Name</th>
								<th>Category</th>
								<th>Total Time in Hrs</th>
								<th>Remark</th>
							</tr>
						</thead>
						<tbody>
						<c:set var="total_time" value="0"/>	
							<c:forEach var="detail" items="${executiveTimesheet.details}">
						<c:set var="total_time" value="${total_time + detail.total_time}" />
							<tr>
								<td>${detail.company.company_name}</td>
								<td>${detail.service.service_name}</td>
								<td>
								<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${detail.total_time}"/><br>
								</td>
								<td>${detail.remark}</td>
							</tr>
							</c:forEach>
							<tr>
							    <td></td>
								<td></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_time}"/><br></td>
							</tr>
						</tbody>
					</table>
				</div>
			<div class="row"  >
				<%-- <button class="fassetBtn" type="button" onclick = "edit(${executiveTimesheet.date})">
					<spring:message code="btn_edit" />
				</button> --%>
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
<script type="text/javascript">

	function edit(date){
		window.location.assign('<c:url value = "editExecutiveTimesheet"/>?date='+date);	
	}
	
	function back(){
		window.location.assign('<c:url value = "executiveTimesheetList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
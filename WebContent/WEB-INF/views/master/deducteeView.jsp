<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>TDS Master</h3>					
	<a href="homePage">Home</a> » <a href="deducteeList">TDS Master</a> » <a href="#">View</a>
</div>
	<div class="fassetForm">
		<form:form id="DeducteeForm"  commandName = "deductee">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>TDS Type:</strong></td>
						<td style="text-align: left;">${deductee.deductee_title}</td>			
					</tr>
				<%-- 	<tr>
						<td><strong>Industry Type:</strong></td>
						<td style="text-align: left;">${deductee.industryType.industry_name}</td>			
					</tr> --%>
					<tr>
						<td><strong>Rate:</strong></td>
						<td style="text-align: left;">${deductee.value}</td>			
					</tr>
					<tr>
						<td><strong>Effective Date:</strong></td>
						<td style="text-align: left;">${deductee.effective_date}</td>			
					</tr>
					<tr>
						<td><strong>Status:</strong></td>
						<td style="text-align: left;">${deductee.status==true ? "Enable" : "Disable"}</td>			
					</tr>
					
				</table>
			</div>
			<div class="col-md-12 text-center" >
				<button class="fassetBtn" type="button" onclick = "edit(${deductee.deductee_id})">
					<spring:message code="btn_edit" />
				</button>
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editdeductee"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "deducteeList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
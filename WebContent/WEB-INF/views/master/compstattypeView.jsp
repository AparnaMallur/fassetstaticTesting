<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Company Statutory Type</h3>					
	<a href="homePage">Home</a> » <a href="companystatutorytypeList">Company Statutory Type</a> » <a href="#">View</a>
</div>
	<div class="fassetForm">
		<form:form id="CompanyStatutoryTypeForm"  commandName = "type">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Company Statutory Type:</strong></td>
						<td style="text-align: left;">${type.company_statutory_name}</td>			
					</tr>
					<tr>
						<td><strong>Status:</strong></td>
					    <td style="text-align: left;">${type.status==true ? "Enable" : "Disable"}</td>
					</tr>
				</table>
			</div>
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "edit(${type.company_statutory_id})">
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
		window.location.assign('<c:url value = "editCompanyStatutoryType"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "companystatutorytypeList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
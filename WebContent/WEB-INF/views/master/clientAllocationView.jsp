<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Client Allocation View</h3>					
	<a href="homePage">Home</a> » <a href="clientAllocationToKpoExecutiveList">Client Allocation</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="clientAllocationViewForm"  commandName = "clientAllocationToKpoExecutive">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>User</strong></td>
					<td style="text-align: left;">${clientAllocationToKpoExecutive.user.first_name} ${clientAllocationToKpoExecutive.user.last_name}</td>			
				</tr>
				<tr>
					<td><strong>Company:</strong></td>
					<td style="text-align: left;">${clientAllocationToKpoExecutive.company.company_name}</td>			
				</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${clientAllocationToKpoExecutive.status==true ? "Enable" : "Disable"}</td>			
				</tr>
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "edit(${clientAllocationToKpoExecutive.allocation_Id})">
				<spring:message code="btn_edit"/>
			</button>
			<button class="fassetBtn" type="button" onclick = "back()">
				<spring:message code="btn_back"/>
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editClientAllocation"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "clientAllocationToKpoExecutiveList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
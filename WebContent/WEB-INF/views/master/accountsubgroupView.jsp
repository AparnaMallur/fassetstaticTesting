<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Sub Group</h3>					
	<a href="homePage">Home</a> » <a href="accsubgroupList">Sub Group</a> » <a href="#">View</a>
</div>
	<div class="fassetForm">
		<form:form id="AccountSubGroupForm"  commandName = "subgroup">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Account Sub Group Name:</strong></td>
						<td style="text-align: left;">${subgroup.subgroup_name}</td>			
					</tr>
					<tr>
						<td><strong>Account Group Name:</strong></td>
						<td style="text-align: left;">${group.group_name}</td>			
					</tr>
					<tr>
						<td><strong>Account Group type:</strong></td>
						<td style="text-align: left;">${type.account_group_name}</td>			
					</tr>
					<tr>
						<td><strong>Status</strong></td>
						<td style="text-align: left;">${subgroup.status==true ? "Enable" : "Disable"}</td>			
					</tr>
				</table>
			</div>
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "edit(${subgroup.subgroup_Id})">
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
		window.location.assign('<c:url value = "editAccountSubGroup"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "accsubgroupList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
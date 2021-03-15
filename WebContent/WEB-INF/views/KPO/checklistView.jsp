<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Checklist Master</h3>					
	<a href="homePage">Home</a> » <a href="checklist">Checklist Master</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="checklistForm"  commandName = "validation">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Checklist:</strong></td>
					<td style="text-align: left;">${validation.checklist_name}</td>			
				</tr>
				<tr>
					<td><strong>Is Checklist Mandatory:</strong></td>
					<td style="text-align: left;">${validation.is_mandatory==true ? "Yes" : "No"}</td>			
				</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${validation.status==true ? "Enable" : "Disable"}</td>			
				</tr>
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "edit(${validation.checklist_id})">
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
		window.location.assign('<c:url value = "editChecklist"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "checklist"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
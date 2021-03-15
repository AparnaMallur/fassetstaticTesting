<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>State</h3>					
	<a href="homePage">Home</a> » <a href="stateList">State</a> » <a href="#">View</a>
</div>	
	<div class="fassetForm">
		<form:form id="stateForm"  commandName = "state">
			<div class="col-md-12">
				<table class = "table">				
					<tr>
						<td><strong>Country Name:</strong></td>
						<td style="text-align: left;">${cntry.country_name}</td>			
					</tr>
					<tr>
						<td><strong>State Name:</strong></td>
						<td style="text-align: left;">${state.state_name}</td>			
					</tr>
					<tr>
						<td><strong>State Code:</strong></td>
						<td style="text-align: left;">${state.state_code}</td>			
					</tr>
					<tr>
					<td><strong>Status:</strong></td>					
					<td style="text-align: left;">${state.status==true ? "Enable" : "Disable"}</td>					
					</tr>
				</table>
			</div>
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "edit(${state.state_id})">
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
		window.location.assign('<c:url value = "editState"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "stateList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
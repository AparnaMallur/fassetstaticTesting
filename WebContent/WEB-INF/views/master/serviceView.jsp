<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Service</h3>					
	<a href="homePage">Home</a> » <a href="serviceList">Service</a> » <a href="#">View</a>
</div>
	<div class="fassetForm">
		<form:form id="ServiceForm"  commandName = "service">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Service Name:</strong></td>
						<td style="text-align: left;">${service.service_name}</td>			
					</tr>
				<!-- 	<tr>
						<td><strong>Service Charge:</strong></td>
						<td style="text-align: left;">${service.service_charge}</td>			
					</tr>
					<tr>
						<td><strong>Service Frequency:</strong></td>
						<td style="text-align: left;">${frequency.frequency_name}</td>			
					</tr>--> 	
					<tr>
						<td><strong>Status:</strong></td>
						<td style="text-align: left;">${service.status==true ? "Enable" : "Disable"}</td>			
					</tr>
				</table>
			</div>
			<div class="col-md-12 text-center"  >
				<button class="fassetBtn" type="button" onclick = "edit(${service.id})">
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
		window.location.assign('<c:url value = "editService"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "serviceList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>KPO Executive & Manager</h3>					
	<a href="homePage">Home</a> » <a href="KPOExecutiveList">KPO Executive & Manager</a> » <a href="#">View</a>
</div>
<div class="fassetForm">
		<form:form id="KPOExecutiveForm"  commandName ="user1">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Name:</strong></td>
						<td style="text-align: left;">${user1.first_name} ${user1.last_name}</td>
						<td><strong>Mail Id:</strong></td>
						<td style="text-align: left;">${user1.email}</td>
						<td><strong>Mobile:</strong></td>
						<td style="text-align: left;">${user1.mobile_no}</td>					
					</tr>
					<tr>
						<td><strong>Role:</strong></td>
						<td style="text-align: left;">${user1.role.role_name}</td>	
						<td><strong>Adhar Number:</strong></td>
						<td style="text-align: left;">${user1.adhaar_no}</td>
						<td><strong>PAN Number:</strong></td>
						<td style="text-align: left;">${user1.pan_no}</td>
						
					</tr>
					<tr>
						<td><strong>City:</strong></td>
						<td style="text-align: left;" >${user1.city.city_name}</td>
						<td><strong>State:</strong></td>
						<td style="text-align: left;" >${user1.state.state_name} -  ${user1.state.state_code}</td>
						<td><strong>Country:</strong></td>
						<td style="text-align: left;">${user1.country.country_name}</td>
					</tr>
					<tr>
						<td><strong>Status:</strong></td>
						<td style="text-align: left;">${user1.status== true? "Join" : "Left"}</td>	
					</tr>
				</table>
			</div>
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "edit(${user1.user_id})">
					<spring:message code="btn_edit" />
				</button>
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
<script type="text/javascript">
	
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	});
	
	function edit(id){
		window.location.assign('<c:url value = "editKPOExecutive"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "KPOExecutiveList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
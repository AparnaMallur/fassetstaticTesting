<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>Employee</h3>
	<a href="homePage">Home</a> » <a href="employeeList">Employee</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="employeeForm"  commandName = "employee">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Employee Name:</strong></td>
					<td style="text-align: left;">${employee.first_name} ${employee.middle_name} ${employee.last_name}</td>
				</tr>
				
				<tr>	
					<td><strong>Email Address:</strong></td>
					<td style="text-align: left;">${employee.email}</td>						
				</tr>
				
				<tr>
					<td><strong>Mobile Number:</strong></td>
					<td style="text-align: left;">${employee.mobile_no}</td>		
				</tr>
				
				<tr>
					<td><strong>Land-line Number:</strong></td>
					<td style="text-align: left;">${employee.landline_no}</td>		
				</tr>
				
				<tr>
					<td><strong>Date of Joining:</strong></td>
					<fmt:parseDate value="${employee.date_of_joining}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
					<fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy" />
					<td style="text-align: left;">${date}</td>	
				</tr>
				
				<tr>					
					<td><strong>Aadhar Number:</strong></td>
					<td style="text-align: left;">${employee.adhaar_no}</td>
				</tr>
				
				<tr>					
					<td><strong>PAN Number:</strong></td>
					<td style="text-align: left;">${employee.pan_no}</td>
				</tr>
				
				<tr>					
					<td><strong>Current Address:</strong></td>
					<td style="text-align: left;">${employee.current_address}</td>
				</tr>
				
				<tr>					
					<td><strong>Permanent Address:</strong></td>
					<td style="text-align: left;">${employee.permenant_address}</td>
				</tr>
				
				<tr>					
					<td><strong>Pin Code:</strong></td>
					<td style="text-align: left;">${employee.pin_code}</td>
				</tr>	
				
				<tr>					
					<td><strong>Country:</strong></td>
					<td style="text-align: left;">${employee.country.country_name}</td>
				</tr>	
				
				<tr>					
					<td><strong>State:</strong></td>
					<td style="text-align: left;">${employee.state.state_name} - ${employee.state.state_code}</td>
				</tr>	
				
				<tr>					
					<td><strong>City:</strong></td>
					<td style="text-align: left;">${employee.city.city_name}</td>
				</tr>
				
				<tr>
			        <td><strong>Status:</strong></td>
					<td style="text-align: left;">${employee.status==true ? "Join" : "Left"}</td>
		 		</tr>					
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "edit(${employee.user_id})">
				<spring:message code="btn_edit" />
			</button>
			<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editEmployee"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "employeeList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
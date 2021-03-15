<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Employee Master</h3>				
	<a href="homePage">Home</a> » <a href="allemployee">Employee Master</a>
</div>	
<div class="fassetForm">
	<form:form id="employeeForm"  commandName = "employee">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Code:</strong></td>
					<td style="text-align: left;">${employee.code}</td>			
				</tr>
				<tr>
					<td><strong>Name:</strong></td>
					<td style="text-align: left;">${employee.name}</td>			
				</tr>
				<tr>
					<td><strong>Date Of Joining:</strong></td>
					<fmt:parseDate value="${employee.doj}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
					<fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy" />
					<td style="text-align: left;">${date}</td>
					<%-- <td style="text-align: left;">${employee.doj}</td> --%>			
				</tr>
				<tr>
					<td><strong>Mobile:</strong></td>
					<td style="text-align: left;">${employee.mobile}</td>			
				</tr>
				<tr>
					<td><strong>PAN Number:</strong></td>
					<td style="text-align: left;">${employee.pan}</td>			
				</tr>
				<tr>
					<td><strong>Aadhaar No:</strong></td>
					<td style="text-align: left;">${employee.adharNo}</td>			
				</tr>
				<tr>
					<td><strong>Basic Salary:</strong></td>
					<td style="text-align: left;">${employee.basicSalary}</td>			
				</tr>
				<tr>
					<td><strong>DA:</strong></td> 
					<td style="text-align: left;">${employee.DA}</td>			
				</tr>
				<tr>
					<td><strong>Conveyance Allowance:</strong></td>
					<td style="text-align: left;">${employee.conveyanceAllowance}</td>			
				</tr>
				<tr>
					<td><strong>Other Allowances:</strong></td>
					<td style="text-align: left;">${employee.otherAllowances}</td>			
				</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${employee.status==true ? "Join" : "Left"}</td>
					
				</tr>
				
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "editemployeeMaster(${employee.employee_id})">
				<spring:message code="btn_edit"/>
			</button>
			<button class="fassetBtn" type="button" onclick = "back()">
				<spring:message code="btn_back"/>
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">
//update_${employeeMaster.employeeMaster_id}
	function editemployeeMaster(id)
    {
		 window.location.assign('<c:url value="editemployee"/>?id='+id);
	}
	
	function back(){
		window.location.assign('<c:url value = "allemployee"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
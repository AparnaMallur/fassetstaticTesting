<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<script type="text/javascript" src="${valid}"></script>



<div class="breadcrumb">
	<h3>Employee Master</h3>				
	<a href="homePage">Home</a> » <a href="allemployee">Employee Master</a>
</div>	

<div class="col-md-12 text-center">
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
		<button id="add" type="button" onclick = "add()">
			Add Employee
		</button>
	</div>
<div class="col-md-12">		
 	<div class = "borderForm">
		<table id="table" 
			 data-toggle="table"
			 data-search="false"
			 data-escape="false"			 
			 data-filter-control="true" 
			 data-show-export="false"
			 data-click-to-select="true"
			 data-pagination="true"
			 data-page-size="10"
			 data-toolbar="#toolbar" class = "table">
			<thead>
			<tr>
				<th class='test' >Action</th>
				<th  data-field="EmployeeCode" data-filter-control="input" data-sortable="true" >Employee Code</th>	
				<th  data-field="EmployeeName" data-filter-control="input" data-sortable="true" >Employee  Name</th>	
				<th  data-field="PanNo" data-filter-control="input" data-sortable="true" >PAN</th>
				<th  data-field="Aadhaar" data-filter-control="input" data-sortable="true" >Aadhaar No</th>
				<th  data-field="status" data-filter-control="select" data-sortable="true" >Status</th>						
				
			</tr>
			</thead>
			<tbody>
			
			<c:forEach var = "employee" items = "${employeeList}">
			  
			
			<tr>
			    <td style="text-align: left;">
					<i   id='view_${employee.employee_id}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"
					onclick = "viewemployeeMaster('${employee.employee_id}')"  class="acs-view fa fa-search" ></i>
					
					
					<i  id='update_${employee.employee_id}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;" 
					onclick = "editemployeeMaster('${employee.employee_id}')"    class="acs-update fa fa-pencil"></i>	
					       	
					<i  id='delete_${employee.employee_id}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;"
					onclick = "deleteemployeeMaster('${employee.employee_id}')"  class="acs-delete fa fa-times" ></i>
			  </td>
			
			
			<td style="text-align: left;">${employee.code}</td>
			<td style="text-align: left;">${employee.name}</td>
			<td style="text-align: left;">${employee.pan}</td>
			<td style="text-align: left;">${employee.adharNo}</td>
			<td style="text-align: left;">${employee.status==true ? "Join" : "Left"}</td>
			<%-- <td style="text-align: left;">${employee.status}</td> --%>		
			</tr>
				
			</c:forEach>
			</tbody>
		</table>
	</div>

</div>

<script type="text/javascript">
function add(){
	window.location.assign('<c:url value="employeeform"/>');
}
		$(function() {		
		    setTimeout(function() {
		        $("#successMsg").hide();
		    }, 3000);
		    $("#bulk_upload").hide();
		});
			
		   function editemployeeMaster(id)
		   {
			   window.location.assign('<c:url value="editemployee"/>?id='+id);
		   }
		   
		   function deleteemployeeMaster(id)
		   {
			   if (confirm("Are you sure you want to delete record?") == true) 
			   {
			   		window.location.assign('<c:url value="deleteemployee"/>?id='+id);
			   }
		   }
		   
		   function viewemployeeMaster(id)
		   {
			   window.location.assign('<c:url value="viewemployee"/>?id='+id);
		   }
		
		</script>

<%@include file="/WEB-INF/includes/footer.jsp" %>
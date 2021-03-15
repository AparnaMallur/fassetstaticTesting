<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Employee</h3>
	<a href="homePage">Home</a> » <a href="#">Employee</a>
</div>

<div class="col-md-12">
	<c:if test="${successMsg != null}">
		<div class="successMsg" id="successMsg">
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<c:if test="${errorMsg != null}">
		<div class="errorMsg" id = "errorMsg"> 
			<strong>${errorMsg}</strong>
		</div>
	</c:if>
	<div class="col-md-12 text-center">
		<button id="add" type="button" onclick="add()">
			Add New Employee
		</button>
	</div>
	<div class="borderForm">
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>
					<th class='test'>Action</th>
					<c:if test="${role == '2'}">
					<th data-field="companyName" data-filter-control="input"
						data-sortable="true">Company Name</th>
					</c:if>
					<th data-field="employeeName" data-filter-control="input"
						data-sortable="true">Employee Name</th>
					<th data-field="address" data-filter-control="input"
						data-sortable="true">Current Address</th>
					<th data-field="emailId" data-filter-control="input"
						data-sortable="true">Email Id</th>
					<th data-field="mobile" data-filter-control="input"
						data-sortable="true">Mobile Number</th>
					<th data-field="Role" data-filter-control="select"
						data-sortable="true">Role</th>
					<th data-field="Status" data-filter-control="select"
						data-sortable="true">Status</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="employee" items="${employeeList}">
						<c:if test="${employee[8] != 'Client Superuser'}">
					<tr>
					
						<td style="text-align: left;">
						<i   id='view_${employee[0]}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"
					onclick = "viewEmployee('${employee[0]}')"  class="acs-view fa fa-search" ></i>
								
								
								<i  id='update_${employee[0]}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;" 
					          onclick = "editEmployee('${employee[0]}')"  class="acs-update fa fa-pencil"></i>	
					          	
							   <i  id='delete_${employee[0]}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;"
							  onclick = "deleteEmployee('${employee[0]}')"   class="acs-delete fa fa-times" ></i>
								            
								
						</td>
						<c:if test="${role == '2'}">
					    <td style="text-align: left;">${employee[1]}</td>
					    </c:if>
						<td style="text-align: left;">${employee[2]} ${employee[3]} ${employee[4]}</td>
						<td style="text-align: left;">${employee[5]}</td>
						<td style="text-align: left;">${employee[6]}</td>
						<td style="text-align: left;">${employee[7]}</td>
						<td style="text-align: left;">${employee[8]}</td>						
						<td style="text-align: left;">${employee[9]==true ? "Join" : "Left"}</td>
					</tr>
					 </c:if>
				</c:forEach>
			</tbody>
		</table>
	</div>

</div>
<script type="text/javascript">
	$(function() {
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	    setTimeout(function() {
	        $("#errorMsg").hide()
	    }, 3000);
	});
	function viewEmployee(id){
		window.location.assign('<c:url value="viewEmployee"/>?id='+id);
	}
	
	function add(){
		var role='<c:out value= "${role}"/>';		
		if(role==7)
		{		
			alert("Please Subscribe with Fasset for use this feature!!!");
		}
		else
		{
		window.location.assign('<c:url value="employee"/>');
		}
	}
	
	function editEmployee(id){
		window.location.assign('<c:url value="editEmployee"/>?id='+id);
	}
	
	function deleteEmployee(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteEmployee"/>?id='+id);
		 } 
	}
	
	$(document).ready(function () {
		  var access_Insert;
		     var access_Update;
		     var access_View;
		     var access_Delete;
		     var menuid=localStorage.getItem("menu_aid");
		     
		     
		     <c:forEach var = "menu" items = "${menuList}">
				var id='${menu.menu_Id}';
				if(id==menuid){
					
					var access_Insert='${menu.access_Insert}';
					var access_Update='${menu.access_Update}';
					var access_View='${menu.access_View}';
					var access_Delete='${menu.access_Delete}';
				   
					
					 <c:forEach var = "employee" items = "${employeeList}">
					 
					 if(access_Insert=='false')
			      		{
							
							 
							var link = document.getElementById("add");
							link.style.display = 'none';
							
			      		}
					if(access_View=='false')
		      		{
						
						 var ID='view_${employee[0]}';
						var link = document.getElementById(ID);
						link.style.display = 'none';
						
		      		}
					if(access_Update=='false')
		      		{
						
						 var ID='update_${employee[0]}';
						var link = document.getElementById(ID);
						link.style.display = 'none';
						
		      		}
					if(access_Delete=='false')
		      		{
						
						 var ID='delete_${employee[0]}';
						var link = document.getElementById(ID);
						link.style.display = 'none';
		      		}
					 </c:forEach>
					
				}
		    </c:forEach>

	/* 	var menuid=localStorage.getItem("menu_aid");
	   $.ajax({
	        type: 'POST',
	        url: 'getActionAccess?menuid='+menuid,
	        async:false,
            contentType: 'application/json',
		      	success: function (data){  
		      	
		      		if(data["access_Insert"]==true)
		      		{
		      		    $("button").removeClass("acs-insert");
		      		}
		      		if(data["access_Update"]==true)
		      		{
		      		    $("i.update-ico").removeClass("acs-update");
		      		}
		      		if(data["access_View"]==true)
		      		{
		      		    $("i.view-ico").removeClass("acs-view");
		      		}
		      		if(data["access_Delete"]==true)
		      		{
		      		    $("i.delete-ico").removeClass("acs-delete");
		      		}
		      		
		      	},
		        error: function (e) {
		            console.log("ERROR: ", e);
		        },
		        done: function (e) {
		            console.log("DONE");
		        }        
	    });	     	 */
	});
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>
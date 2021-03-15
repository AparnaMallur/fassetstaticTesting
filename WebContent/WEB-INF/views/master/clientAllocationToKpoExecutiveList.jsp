<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>

<div class="breadcrumb">
	<h3>Client Allocation</h3>					
	<a href="homePage">Home</a> » <a href="#">Client Allocation</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<c:if test="${(role == '2')||(role == '4')}">
	<div class="col-md-12 text-center" >
		<button id="add"  type="button" onclick = "add()">
			Add Allocation
		</button>
	</div>
</c:if>
	<div class = "borderForm" >
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
			<c:if test="${(role == '2')||(role == '4')}">
			<th class='test' >Action</th>	
			</c:if>
				<th  data-field="User" data-filter-control="input" data-sortable="true" >User Name</th>
				<th  data-field="Manager" data-filter-control="input" data-sortable="true" >Manager Name</th>
				<th  data-field="Company" data-filter-control="input" data-sortable="true" >Company</th>	
			</tr> 
			</thead>
			<tbody>
			<c:forEach var = "allocation" items = "${clientAllocationToKpoExecutiveList}">
				<tr>
				<c:if test="${(role == '2')||(role == '4')}">
					<td style="text-align: left;">
								<i  id='view-ico' onclick = "viewAllocation('${allocation.allocation_Id}')" class="acs-view fa fa-search" ></i>
								<i  id='update-ico' onclick = "editAllocation('${allocation.allocation_Id}')" class="acs-update fa fa-pencil" ></i>
								<i  id='delete-ico' onclick = "deleteAllocation('${allocation.allocation_Id}')" class="acs-delete fa fa-times" ></i>	
					</td>
			    </c:if>
					<td style="text-align: left;">${allocation.user.first_name} ${allocation.user.last_name}</td>
					<td style="text-align: left;">${allocation.user.manager_id.first_name} ${allocation.user.manager_id.last_name}</td>
					<td style="text-align: left;">${allocation.company.company_name}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
<script type="text/javascript">
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});
	function viewAllocation(id){
		window.location.assign('<c:url value="viewClientAllocation"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="clientallocation"/>');
	}
	
	function editAllocation(id){
		window.location.assign('<c:url value="editClientAllocation"/>?id='+id);
	}
	
	function deleteAllocation(id){

		 if (confirm("Are you sure you want to delete record?") == true) {
		window.location.assign('<c:url value="deleteAllocation"/>?id='+id);
		 } 
	}
	$(document).ready(function () {
		var menuid=localStorage.getItem("menu_aid");
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
			   
				
				 <c:forEach var = "entry" items = "${receiptList}">
				 
				 if(access_Insert=='false')
		      		{
						
						 
						var link = document.getElementById("add");
						link.style.display = 'none';
						
		      		}
				if(access_View=='false')
	      		{
					
					 var ID='view_${entry.receipt_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${entry.receipt_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{

					var ID='delete_${entry.receipt_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
	      		}
				 </c:forEach>
				
			}
	    </c:forEach>
	    document.getElementById('progress-div').style.display="none";
		$("#file-div").show();	
		 var file='<c:out value="${successMsg}"/>';
		 if(file=="NA")
			{
				   $('#file-model').modal({
				    	show: true,
				   	});	
			}
	
	  /*  $.ajax({
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
<%@include file="/WEB-INF/includes/footer.jsp" %>

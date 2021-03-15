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
	<h3>Menu Master</h3>					
	<a href="homePage">Home</a> » <a href="#">Menu Master</a>
	</div>	
<div class="col-md-12">		
	<c:if test="${successMsg != null}">
		<div class="successMsg" id = "successMsg"> 
			<strong>${successMsg} </strong>
		</div>
	</c:if>
	<div class="col-md-12 text-center" >
		<button  type="button" onclick = "add()">
			Add New Menu
		</button>
	</div>
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
				<th class='test' >Action</th>	
				<th  data-field="menu" data-filter-control="input" data-sortable="true" >Menu Name</th>				
				<th  data-field="url" data-filter-control="input" data-sortable="true" >Menu URL</th>								
				<th  data-field="Parent" data-filter-control="select" data-sortable="true" >Parent</th>				
				<th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>				
				
				
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "MenuMaster" items = "${menuMasterList}">
				<tr>
					<td style="text-align: left;">
								<i  id='view-ico' onclick = "viewMenu('${MenuMaster.menu_id}')" class="acs-view fa fa-search" ></i>
								<i  id='update-ico' onclick = "editMenu('${MenuMaster.menu_id}')" class="acs-update fa fa-pencil" ></i>
								<i  id='delete-ico' onclick = "deleteMenu('${MenuMaster.menu_id}')" class="acs-delete fa fa-times" ></i>	
					</td>					
					<td style="text-align: left;">${MenuMaster.menu_name}</td>
					<td style="text-align: left;">${MenuMaster.menu_url}</td>
					<td style="text-align: left;">${MenuMaster.parent_id == Null ? "No Parent" : MenuMaster.parent_id.menu_name}</td>
					
					<td style="text-align: left;">${MenuMaster.status==true ? "Enable" : "Disable"}</td>
				</tr>
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
});
	function viewMenu(id){
		window.location.assign('<c:url value="viewMenu"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="menuMaster"/>');
	}
	
	function editMenu(id){
		window.location.assign('<c:url value="editMenu"/>?id='+id);
	}
	
	function deleteMenu(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteMenu"/>?id='+id);
		 } 
	}
	$(document).ready(function () {
		var menuid=localStorage.getItem("menu_aid");
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
	    });	     	
	});
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>

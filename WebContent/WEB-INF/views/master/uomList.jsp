<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Unit of Measurement</h3>
	<a href="homePage">Home</a> » <a href="#">Unit of Measurement</a>
</div>	
<div class="col-md-12">		
	<c:if test="${successMsg != null}">
		<div class="successMsg" id = "successMsg"> 
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<div class="col-md-12 text-center">
		<button id="add" type="button" onclick = "add()">
			Add New Unit of Measurement
		</button>
	</div>
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
				<th data-field="uom" data-filter-control="input" data-sortable="true" >Unit of Measurement</th>
				<th data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>				
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "uom" items = "${uomList}">
				<tr>
					<td style="text-align: left;">
					
					<i   id='view_${uom.uom_id}'  style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"
					 onclick = "viewUOM('${uom.uom_id}')"  class="acs-view fa fa-search" ></i>
								      
		        	<i  id='update_${uom.uom_id}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;" 
					onclick = "editUOM('${uom.uom_id}')" class="acs-update fa fa-pencil"></i>	
						
					<i  id='delete_${uom.uom_id}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;"
					onclick = "deleteUOM('${uom.uom_id}')" class="acs-delete fa fa-times" ></i>
								
	     			</td>					
					<td style="text-align: left;">${uom.unit}</td>
					<td style="text-align: left;">${uom.status==true ? "Enable" : "Disable"}</td>
					
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
	function viewUOM(id){
		window.location.assign('<c:url value="viewUOM"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="uom"/>');
	}
	
	function editUOM(id){
		window.location.assign('<c:url value="editUOM"/>?id='+id);
	}
	
	function deleteUOM(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
		window.location.assign('<c:url value="deleteUOM"/>?id='+id);
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
			   
				
				 <c:forEach var = "uom" items = "${uomList}">
				 
				 if(access_Insert=='false')
		      		{
						
						 
						var link = document.getElementById("add");
						link.style.display = 'none';
						
		      		}
				if(access_View=='false')
	      		{
					
					 var ID='view_${uom.uom_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${uom.uom_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{
					
					 var ID='delete_${uom.uom_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
	      		}
				 </c:forEach>
				
			}
	    </c:forEach>
		/* var menuid=localStorage.getItem("menu_aid");
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
<%@include file="/WEB-INF/includes/footer.jsp" %>
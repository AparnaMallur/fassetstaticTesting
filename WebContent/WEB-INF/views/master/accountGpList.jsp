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
	<h3>Group</h3>					
	<a href="homePage">Home</a> » <a href="#">Group</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
	<div class="col-md-12 text-center" >
		<button  id="add" type="button" onclick = "add()">
			Add New Account Group
		</button>
	</div>
	<div class="clearfix"></div>
	<div class = "borderForm" >
		<div class="table-responsive">
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
					 <th  data-field="Group" data-filter-control="input" data-sortable="true" >Account Group Name</th>
					<th  data-field="GroupType" data-filter-control="input" data-sortable="true" >Group Type</th>
					<th  data-field="Posting" data-filter-control="input" data-sortable="true" >Posting</th>
					<th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	
				</tr>
				</thead>
				<tbody>
				<c:forEach var = "Gplist" items = "${accountGpList}">
					<tr>
						<td style="text-align: left;">
											<i   id='view_${Gplist[0]}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;" 
										 onclick = "viewAccountGroup('${Gplist[0]}')"  class="acs-view fa fa-search" ></i>
														
								        	<i  id='update_${Gplist[0]}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;"  
									    onclick = "editAccountGroup('${Gplist[0]}')" class="acs-update fa fa-pencil" ></i>
									
									        <i  id='delete_${Gplist[0]}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;" 
									   onclick = "deleteAccountGroup('${Gplist[0]}')" class="acs-delete fa fa-times" ></i>	
										
						</td>					
						<td style="text-align: left;">${Gplist[1]}</td>				
						<td style="text-align: left;">${Gplist[2]}</td>	
						<td style="text-align: left;">${Gplist[3]}</td>					
						<td style="text-align: left;">${Gplist[4]==true ? "Enable" : "Disable"}</td>
						
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});
/* 	function PrevList()
	{
		window.location.assign('<c:url value="accountgroup"/>');
	}
	function NextList()
	{
		window.location.assign('<c:url value="accountgroup"/>');
	} */
	function viewAccountGroup(id){
		window.location.assign('<c:url value="viewAccountGroup"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="accountgroup"/>');
	}
	
	function editAccountGroup(id){
		window.location.assign('<c:url value="editAccountGroup"/>?id='+id);
	}
	function deleteAccountGroup(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
		window.location.assign('<c:url value="deleteAccountGroup"/>?id='+id);
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
			   
				
				 <c:forEach var = "gpList" items = "${accountGpList}">
				 
				 if(access_Insert=='false')
		      		{
						var link = document.getElementById("add");
						link.style.display = 'none';
						
		      		}
				if(access_View=='false')
	      		{
					
					 var ID='view_${gpList[0]}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${gpList[0]}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{
					
					 var ID='delete_${gpList[0]}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
	      		}
				 </c:forEach>
				
			}
	    </c:forEach>
/* 	   $.ajax({
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
	    });	 */     	
	});
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
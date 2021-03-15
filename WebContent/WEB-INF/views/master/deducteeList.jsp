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
	<h3>TDS Master</h3>					
	<a href="homePage">Home</a> » <a href="#">TDS Master</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
	<div class="col-md-12 text-center" >
		<button  id="add" type="button" onclick = "add()">
			Add New TDS 
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
				 <th  data-field="Deductee Name" data-filter-control="input" data-sortable="true" >TDS Type</th>
<!-- 				<th  data-field="Industry Name" data-filter-control="input" data-sortable="true" >Industry Name</th>
 -->				<th  data-field="Value" data-filter-control="input" data-sortable="true" >Rate</th>
				<th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "deductee" items = "${deducteeList}">
				<tr>
					<td style="text-align: left;">
					<i   id='view_${deductee[0]}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"
				onclick = "viewDeductee('${deductee[0]}')"   class="acs-view fa fa-search" ></i>
								
								
								<i  id='update_${deductee[0]}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;" 
					         onclick = "editDeductee('${deductee[0]}')"  class="acs-update fa fa-pencil"></i>		
							   <i  id='delete_${deductee[0]}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;"
							 onclick = "deleteDeductee('${deductee[0]}')"  class="acs-delete fa fa-times" ></i>
	     			</td>
					
					<td style="text-align: left;">${deductee[1]}</td>				
<%-- 					<td style="text-align: left;">${deductee.industryType.industry_name}</td>	
 --%>					<td style="text-align: left;">${deductee[2]}</td>					
					<td style="text-align: left;">${deductee[3]==true ? "Enable" : "Disable"}</td>
					
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
	function viewDeductee(id){
		window.location.assign('<c:url value="viewdeductee"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="deductee"/>');
	}
	
	function editDeductee(id){
		window.location.assign('<c:url value="editdeductee"/>?id='+id);
	}
	function deleteDeductee(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deletedeductee"/>?id='+id);
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
			   
				
				 <c:forEach var = "deductee" items = "${deducteeList}">
				 
				 if(access_Insert=='false')
		      		{
						
						 
						var link = document.getElementById("add");
						link.style.display = 'none';
						
		      		}
				if(access_View=='false')
	      		{
					
					 var ID='view_${deductee[0]}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${deductee[0]}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{
					
					 var ID='delete_${deductee[0]}';
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
	    });	  */    	
	});
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
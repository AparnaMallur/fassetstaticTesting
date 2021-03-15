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
	<h3>Country</h3>					
	<a href="homePage">Home</a> » <a href="#">Country</a>
</div>	
<div class="col-md-12">		
	<c:if test="${successMsg != null}">
		<div class="successMsg" id = "successMsg"> 
			<strong>${successMsg}</strong>
		</div>
	</c:if>

	<div class="col-md-12 text-center" >
		<button id="add" type="button" onclick = "add()">
			Add New Country
		</button>
	</div>
	<div class = "borderForm filtertable" >
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
				<th  data-field="Country" data-filter-control="input" data-sortable="true" >Country</th>
				<th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>				
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "country" items = "${countryList}">
				<tr>
					<td class='test' style="text-align: left;"  >
					<i   id='view_${country[0]}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"
					onclick = "viewCountry('${country[0]}')"   class="acs-view fa fa-search" ></i>
					<i  id='update_${country[0]}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;" 
					 onclick = "editCountry('${country[0]}')"  class="acs-update fa fa-pencil"></i>		
				   <i  id='delete_${country[0]}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;"
				 onclick = "deleteCountry('${country[0]}')"   class="acs-delete fa fa-times" ></i>
				 </td>
					           
								
					<td style="text-align: left;">${country[1]}</td>
					<td style="text-align: left;">${country[2]==true ? "Enable" : "Disable"}</td>					
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
	function viewCountry(id){
		window.location.assign('<c:url value="viewCountry"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="country"/>');
		
	}
	
	function editCountry(id){
		window.location.assign('<c:url value="editCountry"/>?id='+id);
	}
	
	function deleteCountry(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteCountry"/>?id='+id);
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
				   
					
					 <c:forEach var = "country" items = "${countryList}">
					 
					 if(access_Insert=='false')
			      		{
							
							 
							var link = document.getElementById("add");
							link.style.display = 'none';
							
			      		}
					if(access_View=='false')
		      		{
						
						 var ID='view_${country[0]}';
						var link = document.getElementById(ID);
						link.style.display = 'none';
						
		      		}
					if(access_Update=='false')
		      		{
						
						 var ID='update_${country[0]}';
						var link = document.getElementById(ID);
						link.style.display = 'none';
						
		      		}
					if(access_Delete=='false')
		      		{
						
						 var ID='delete_${country[0]}';
						var link = document.getElementById(ID);
						link.style.display = 'none';
		      		}
					 </c:forEach>
					
				}
		    </c:forEach>
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
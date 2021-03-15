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
	<h3>TaxMaster</h3>					
	<a href="homePage">Home</a> » <a href="#">TaxMaster</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
	<div class="col-md-12 text-center" >
		<button id="add" type="button" onclick = "add()">
			Add New TaxMaster
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
				<th  data-field="TaxName" data-filter-control="input" data-sortable="true" >Name</th>			
				<th  data-field="Vat" data-filter-control="input" data-sortable="true" >VAT</th>
				<th  data-field="Cst" data-filter-control="input" data-sortable="true" >CST</th>
				<th  data-field="Excise" data-filter-control="input" data-sortable="true" >EXCISE</th>
				<th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>				
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "taxMaster" items = "${taxMasterList}">
				<tr>
					<td style="text-align: left;">
					<i   id='view_${taxMaster[0]}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"
				onclick = "viewTaxMaster('${taxMaster[0]}')"  class="acs-view fa fa-search" ></i>
								
								
								<i  id='update_${taxMaster[0]}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;" 
					          onclick = "editTaxMaster('${taxMaster[0]}')"   class="acs-update fa fa-pencil"></i>		
							   <i  id='delete_${taxMaster[0]}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;"
							  onclick = "deleteTaxMaster('${taxMaster[0]}')"  class="acs-delete fa fa-times" ></i>
								            
								
					</td>				
					<td style="text-align: left;">${taxMaster[1]}</td>					
					<td style="text-align: left;">${taxMaster[2]}</td>
					<td style="text-align: left;">${taxMaster[3]}</td>
					<td style="text-align: left;">${taxMaster[4]}</td>
					<td style="text-align: left;">${taxMaster[5]==true ? "Enable" : "Disable"}</td>
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
	function viewTaxMaster(id){
		window.location.assign('<c:url value="viewTaxMaster"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="taxmaster"/>');
	}
	
	function editTaxMaster(id){
		window.location.assign('<c:url value="editTaxMaster"/>?id='+id);
	}
	
	function deleteTaxMaster(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
		window.location.assign('<c:url value="deleteTaxMaster"/>?id='+id);
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
			   
				
				 <c:forEach var = "taxMaster" items = "${taxMasterList}">
				 
				 if(access_Insert=='false')
		      		{
						
						 
						var link = document.getElementById("add");
						link.style.display = 'none';
						
		      		}
				if(access_View=='false')
	      		{
					
					 var ID='view_${taxMaster[0]}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${taxMaster[0]}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{
					
					 var ID='delete_${taxMaster[0]}';
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
	    });	   */
	 });	 
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
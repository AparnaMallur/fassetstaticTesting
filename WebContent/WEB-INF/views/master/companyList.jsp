<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />

<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Company</h3>
	<a href="homePage">Home</a> » <a href="#">Company</a>
</div>

<div class="col-md-12">
	<c:if test="${successMsg != null}">
		<div class="successMsg" id="successMsg">
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<div class="col-md-12 text-center">
		<button id="add" type="button" onclick="add()">
			Add New Company
		</button>
	</div>
	<div class="clearfix"></div>
	<div class="borderForm">
		<div class="table-responsive">
			<table id="table" data-toggle="table" data-search="false"
				data-escape="false" data-filter-control="true"
				data-show-export="false" data-click-to-select="true"
				data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
				class="table">
				<thead>
					<tr>
						<th class='test'>Action</th>
						<th data-field="companyName" data-filter-control="input"
							data-sortable="true">Company Name</th>
						<th data-field="address" data-filter-control="input"
							data-sortable="true">Address</th>
						<th data-field="emailId" data-filter-control="input"
							data-sortable="true">Email ID</th>
						<th data-field="mobile" data-filter-control="input"
							data-sortable="true">Mobile Number</th>					
						<th data-field="Status" data-filter-control="select"
							data-sortable="true">Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="company" items="${companyList}">
						<tr>
							<td style="text-align: left;">
						
										
								<c:choose>
										<c:when test="${ ((company[5]==2) || (company[5]==1))}">
											
																		
									<i  id='view_${company[0]}'  style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%"
								 onclick = "viewCompany('${company[0]}')"  class="acs-view fa fa-search" ></i>		
								 	<i  id='update_${company[0]}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;"  
									onclick = "editCompany('${company[0]}')" class="acs-update fa fa-pencil" ></i>
										
									</c:when>
									<c:otherwise>
									<i  id='view_${company[0]}'  style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%"
								 onclick = "viewCompany('${company[0]}')"  class="acs-view fa fa-search" ></i>		
																		
									<i  id='update_${company[0]}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;"  
									onclick = "editCompany('${company[0]}')" class="acs-update fa fa-pencil" ></i>	
									</c:otherwise>
								</c:choose>		
									
									
									
									
									
																
									<%-- <i  id='update_${company[0]}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;"  
									onclick = "editCompany('${company[0]}')" class="acs-update fa fa-pencil" ></i> --%>
									
									<%-- <i  id='delete_${company[0]}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;" 
									onclick = "deleteCompany('${company[0]}')" class="acs-delete fa fa-times" ></i> --%>	
							</td>
							<td style="text-align: left;">${company[1]}</td>
							<td style="text-align: left;">${company[2]}</td>
							<td style="text-align: left;">${company[3]}</td>
							<td style="text-align: left;">${company[4]}</td>
							<td style="text-align: left;">${company[5] == 0 ? "Disable" : company[5] == 1 ? "Pending For Approval" : company[5] == 2 ? "Trial Login" : company[5] == 3 ? "Subscribed User" :"Disable"}</td>
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
$( document ).ready(function() {	
	$("#bulk_upload").hide();
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
			   
				
				 <c:forEach var = "company" items = "${companyList}">
				 
				 if(access_Insert=='false')
		      		{
						var link = document.getElementById("add");
						link.style.display = 'none';
						
		      		}
				if(access_View=='false')
	      		{
					
					 var ID='view_${company[0]}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${company[0]}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{
					
					 var ID='delete_${company[0]}';
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
function uploaddiv(){
	$("#bulk_upload").show();
}
function hideuploaddiv(){
	$("#bulk_upload").hide();
}
	function viewCompany(id){
		window.location.assign('<c:url value="viewCompany"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="company"/>');	}
	
	function editCompany(id){
		window.location.assign('<c:url value="editCompany"/>?id='+id);
	}
	function editCompany1(id){
		alert("you cant edit the Trial User Company");
		return false;
	}
	function deleteCompany(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
		window.location.assign('<c:url value="deleteCompany"/>?id='+id);
		 } 
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>
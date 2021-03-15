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
	<h3>Executive Timesheet</h3>					
	<a href="homePage">Home</a> » <a href="#">Executive Timesheet</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 text-center" >
		<button id="add"  type="button" onclick = "add()">
			Add New Timesheet
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
			<th  data-field="date" data-filter-control="input" data-sortable="true" >Date</th>
			<th  data-field="Company" data-filter-control="input" data-sortable="true" >Company</th>	
			<th  data-field="Service" data-filter-control="input" data-sortable="true" >Category</th>
			<th  data-field="Total" data-filter-control="input" data-sortable="true" >Total Time in Hrs</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var = "executive" items = "${executiveTimesheetList}">
			<c:set var="total_time" value="0"/>	
			
			<c:forEach var = "detail" items = "${executive.details}">
						<c:set var="total_time" value="${ detail.total_time}" />
		   </c:forEach>
			<tr>
				<td style="text-align: left;">
					<i  id='view-ico' onclick = "viewExecutive('${executive.date}')" class="acs-view fa fa-search" ></i>
					<%--<a class="acs-view" href = "#" onclick = "viewExecutive('${executive.date}')"><img src='${viewImg}' style = "width: 20px;"/></a>
					 <a class="acs-update" href = "#" onclick = "editExecutive('${executive.date}')"><img src='${editImg}' style = "width: 20px;"/></a>
					<a class="acs-delete" href = "#" onclick = "deleteExecutive('${executive.date}')"><img src='${deleteImg}' style = "width: 20px;"/></a> --%>
				</td>
				<td style="text-align: left;">${executive.date}</td>
				<td style="text-align: left;">
					<c:forEach var = "detail" items = "${executive.details}">
						${detail.company.company_name} <br>
					</c:forEach>
				</td>
				<td style="text-align: left;">
					<c:forEach var = "detail" items = "${executive.details}">
						${detail.service.service_name} <br>
					</c:forEach>
				</td>
				<td style="text-align: left;">
					<c:forEach var = "detail" items = "${executive.details}">
					${detail.total_time} <br>
					</c:forEach>
				</td>
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

	
	$(document).ready(function () {
		var menuid=localStorage.getItem("menu_aid");
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
	
	});
	});
	
	function viewExecutive(date){
		window.location.assign('<c:url value="viewExecutiveTimesheet"/>?date='+date);
	}
	
	function add(){
		window.location.assign('<c:url value="executivetimesheet"/>');
	}
	
	function editExecutive(date){
		window.location.assign('<c:url value="editExecutiveTimesheet"/>?date='+date);
	}
	
	function deleteExecutive(date){
		 if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteExecutiveTimesheet"/>?date='+date);
		 } 
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>

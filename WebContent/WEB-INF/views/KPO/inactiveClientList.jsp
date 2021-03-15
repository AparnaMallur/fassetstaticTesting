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
	<h3>Inactive Client/Company List</h3>					
	<a href="homePage">Home</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
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
				<!-- <th class='test' >Action</th> -->	
				<th  data-field="company_name" data-filter-control="input" data-sortable="true" >Company Name</th>
				<th  data-field="User Name" data-filter-control="input" data-sortable="true" >Client Super User Name</th>
				<th  data-field="email" data-filter-control="input" data-sortable="true" >Email</th>
				<th  data-field="mobile_no" data-filter-control="select" data-sortable="true" >Mobile</th>
				<th  data-field="status" data-filter-control="select" data-sortable="true" >Status</th>	
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "comp" items = "${inactiveClientList}">
				<tr>
					<%-- <td style="text-align: left;">
								<i  id='update-ico' onclick = "activateCompany('${comp.company_id}')" class="acs-check fa fa-check-circle" ></i>			
					</td> --%>
					
					<td style="text-align: left;">${comp.company_name}</td>		
					<c:forEach var = "ClientSuperUser" items = "${comp.user}">
					<c:if test="${(ClientSuperUser.role.role_id == '5')}">
					<td style="text-align: left;"> 
				     ${ClientSuperUser.first_name}  ${ClientSuperUser.last_name}
				     </td>	
				     <td style="text-align: left;">
				      ${ClientSuperUser.email}
				      </td>
				       <td style="text-align: left;">
				        ${ClientSuperUser.mobile_no}
				      </td>
				   </c:if>
				   </c:forEach>
					<td style="text-align: left;">Inactive from more than 3 days</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="row" style="text-align: center; margin: 15px;">
		<button class="fassetBtn" type="button" onclick="back()">
			<spring:message code="btn_back" />
		</button>
	</div>
<script type="text/javascript">
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});
	
	function activateCompany(id){
		window.location.assign('<c:url value="editChecklistStatus"/>?id='+id);
	}
	
	function back(){
		window.location.assign('<c:url value = "homePage"/>');	
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

<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Company Approval</h3>					
	<a href="homePage">Home</a> » <a href="#">Company Approval</a>
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
				 <th class='test' >Action</th>	
				 <th  data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>
				  <th  data-field="Email Id" data-filter-control="input" data-sortable="true" >Email Id</th>
				   <th  data-field="Company Current Address" data-filter-control="input" data-sortable="true" >Company Current Address</th>
				    <th  data-field="Company Permanent Address" data-filter-control="input" data-sortable="true" >Company Permanent Address</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "company" items = "${compList}">
				<tr>
					<td style="text-align: left;">
					<i  id='update-ico' onclick = "editChecklist('${company.company_id}')" class="acs-update fa fa-pencil" ></i>
						<%--
						<a class="acs-update" href = "#" onclick = "editChecklist('${company.company_id}')"><img src='${editImg}' style = "width: 20px;"/></a>
					 <input class="logBt btn btn-primary" type="button" value="View" onclick = "viewSupplier('${supplier.supplier_id}')">
						<input class="logBt btn btn-primary" type="button" value="Edit" onclick = "editSuppliers('${supplier.supplier_id}')"> --%>
					</td>
					
					<td style="text-align: left;">${company.company_name}</td>	
					<td style="text-align: left;">${company.email_id}</td>
					<td style="text-align: left;">${company.current_address}</td>
				   <td style="text-align: left;">${company.permenant_address}</td>
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
function editChecklist(id){
	window.location.assign('<c:url value="editChecklistStatus"/>?id='+id);
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
	      		    $("td a").removeClass("acs-update");
	      		}
	      		if(data["access_View"]==true)
	      		{
	      		    $("td a").removeClass("acs-view");
	      		}
	      		if(data["access_Delete"]==true)
	      		{
	      		    $("td a").removeClass("acs-delete");
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
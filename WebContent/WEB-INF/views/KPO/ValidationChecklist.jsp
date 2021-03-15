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
	<h3>Checklist Master</h3>					
	<a href="homePage">Home</a> » <a href="#">Checklist Master</a>
</div>	
<div class="col-md-12">		
	<c:if test="${successMsg != null}">
		<div class="successMsg" id = "successMsg"> 
			<strong>${successMsg}</strong>
		</div>
	</c:if>

	<div class="col-md-12 text-center" >
		<button class="fassetBtn" type="button" onclick = "add()">
			Add New Checklist
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
				<th  data-field="Checklist Name" data-filter-control="input" data-sortable="true" >Checklist</th>
				<th  data-field="Mandatory" data-filter-control="input" data-sortable="true" >Is Checklist Mandatory</th>
				<th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>								
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "list" items = "${checklist}">
				<tr>
					<td class='test' style="text-align: left;"  >
								<i  id='view-ico' onclick = "viewChecklist('${list.checklist_id}')" class="acs-view fa fa-search" ></i>
								<i  id='update-ico' onclick = "editChecklist('${list.checklist_id}')" class="acs-update fa fa-pencil" ></i>
					</td>				
					<td style="text-align: left;">${list.checklist_name}</td>
					<td style="text-align: left;">${list.is_mandatory==true ? "Yes" : "No"}</td>					
					<td style="text-align: left;">${list.status==true ? "Enable" : "Disable"}</td>					
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
	function viewChecklist(id){
		window.location.assign('<c:url value="viewChecklist"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="clientValidation"/>');
	}
	
	function editChecklist(id){
		window.location.assign('<c:url value="editChecklist"/>?id='+id);
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
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
	<h3>Supplier</h3>					
	<a href="homePage">Home</a> » <a href="suppliersList">Supplier</a>
</div>	

 <c:choose>
	<c:when test="${isImport == true}">
				<!-- Small modal -->
			<div id="file-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	     		<div class="modal-dialog ">
	          		<div class="modal-content">
	              		<div class="modal-header">
	                   		<h4 class="modal-title" id="mySmallModalLabel">Import Status</h4>
	               		</div>
		   				<div class="modal-body">
		   				<c:if test="${successList.size()==0 && failureList.size()==0 && updateList.size()==0}">
		   				<label style="color:red;">${error}</label>
		   				</c:if>
		   				
		   				<c:if test="${successList.size()!=0}">
		   				<label style="color:green;">${successImportmsg}</label>
							<c:forEach var = "success" items = "${successList}">
							<ul><li>${success}</li></ul>
			                </c:forEach>
		   				</c:if>
		   				<c:if test="${updateList.size()!=0}">
		   				<label style="color:green;">${updatemsg}</label>
							<c:forEach var = "success" items = "${updateList}">
							<ul><li>${success}</li></ul>
			                </c:forEach>
		   				</c:if>
		   				<c:if test="${failureList.size()!=0}">
		   				<label style="color:red;">${failmsg}</label>
							<c:forEach var = "success" items = "${failureList}">
							<ul><li>${success}</li></ul>
			                </c:forEach>
		   				</c:if>
						</div>
						<div class="modal-footer">
 						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
 					   </div>
	       			</div><!-- /.modal-content -->
	      		</div>
			</div>
			<!-- /.modal -->  
	</c:when>
	<c:otherwise>
			<c:if test="${successMsg != null}">
			<div class="successMsg" id = "successMsg"> 
				<strong>${successMsg}</strong>		
			</div>
			</c:if>
	</c:otherwise>
</c:choose>
	<div class="col-md-12 text-center" >
		<button id="add" type="button" onclick = "add()">
			Add supplier
		</button>
			<c:if test="${(importflag==true)|| (role=='2') || (role=='3') || (role=='4')}">		
				<button class="fassetBtn" type="button" onclick = "uploaddiv()">
							Import Excel
				</button>
			</c:if>
			<!-- <button class="fassetBtn" type="button" onclick = "uploaddiv()">
							Import Excel
				</button> -->
		<c:if test="${importfail==true}">		
		<button class="fassetBtn" type="button" onclick = "supplierimportfailure()">		
					Failure List
		</button>
		</c:if>
	</div>
	<div id="bulk_upload" class='row' style="display:none;">
					<div id="progress-div" style="text-align:center">
					<img src="${processing}" style="height:80px"/>
					<h4>File uploading is in progress...</h4>
				</div>
			<div class="text-center" id="file-div">
						<form:form  action="importExcel" method="post" enctype="multipart/form-data" onsubmit = "return validate();">
			    	<c:choose>
						<c:when test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">
			    	<p>Please download template from here to import <a href="resources/templates/supplier-template.xlsx" style='font-weight:bold'>Download</a> </p>	
			    		</c:when>
			    		<c:otherwise>
			    	<p>Please download template from here to import <a href="resources/templates/supplier-template-company.xlsx" style='font-weight:bold'>Download</a> </p>	
			    		</c:otherwise>
			    	</c:choose>			    			
			    			<div style = "text-align: center;">			    				
								<label for="excelFile">Select Excel File:</label>	
								<input id = "excelFile" name="excelFile" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" style = "display: inline; margin: 10px 0px 10px 0px;">	
			    			</div>
			    			<div class="form-inline" style="margin-top:2%" >
								<div class="form-group">
									<input type ="submit" class="logBt btn btn-primary" value="<spring:message code="btn_upload" />">				
								</div>
								<div class="form-group" style="margin-left: 30px;">
									<input class="logBt btn btn-primary" type="button" onclick = "hideuploaddiv()" id = "btnCancel" value="<spring:message code="btn_cancel"/>">				
								</div>
							</div>
						</form:form>
			</div>
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
				<c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
				    <th data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>
				</c:if>	
					<th  data-field="Company" data-filter-control="input" data-sortable="true" >Supplier Name</th>	
					<th  data-field="Contact" data-filter-control="input" data-sortable="true" >Supplier's Company Name</th>	
					<th  data-field="Mobile" data-filter-control="input" data-sortable="true" >Mobile</th>
					<th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	
					<th  data-field="ApprovalStatus" data-filter-control="select" data-sortable="true" >Approval Status</th>			
								
				</tr>
				</thead>
				<tbody>
				<c:forEach var = "supplier" items = "${supplierList}">
					<tr>
						<td style="text-align: left;">
							
										 
							<a id='view_${supplier.supplier_id}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"  
							 href = "#" onclick = "viewSupplier('${supplier.supplier_id}')"><img src='${viewImg}' style = "width: 20px;"/></a>
							<c:choose>
						    <c:when test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">	
								<c:if test="${((supplier.supplier_approval==0) || (supplier.supplier_approval==1))}">
								<a id='update_${supplier.supplier_id}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;"
							      href = "#" onclick = "editSuppliers('${supplier.supplier_id}')">
							      <img src='${editImg}' style = "width: 20px;"/></a>
								<a id='delete_${supplier.supplier_id} ' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;" 
								href = "#" onclick = "deleteSuppliers('${supplier.supplier_id}')"><img src='${deleteImg}' style = "width: 20px;"/></a>
				
								</c:if>
							</c:when>
							<c:otherwise>
							
									   
							      <a id='update_${supplier.supplier_id}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;"
							      href = "#" onclick = "editSuppliers('${supplier.supplier_id}')">
							      <img src='${editImg}' style = "width: 20px;"/></a>
								<a id='delete_${supplier.supplier_id} ' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;" 
								href = "#" onclick = "deleteSuppliers('${supplier.supplier_id}')"><img src='${deleteImg}' style = "width: 20px;"/></a>
							</c:otherwise>
							</c:choose>
						</td>
						<c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
						<td style="text-align: left;">${supplier.company.company_name}</td>
						</c:if>	
						<td style="text-align: left;">${supplier.contact_name}</td>
						<td style="text-align: left;">${supplier.company_name}</td>
						<td style="text-align: left;"><fmt:formatNumber type="number" pattern="############0"
	                                             value="${supplier.mobile}" /></td>
						<td style="text-align: left;">${supplier.status==true ? "Enable" : "Disable"}</td>
						<td style="text-align: left;">${supplier.supplier_approval == 0 ? "Pending" : supplier.supplier_approval  == 1 ? "Rejected" : supplier.supplier_approval  == 2 ? "Primary Approval" : "Approved"}</td>
						
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

</div>

<script type="text/javascript">
$( document ).ready(function() {	
	$("#bulk_upload").hide();
});
function uploaddiv(){
	$("#bulk_upload").show();
}
function hideuploaddiv(){
	$("#bulk_upload").hide();
}
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});
	function viewSupplier(id){
		window.location.assign('<c:url value="viewSupplier"/>?id='+id+'&flag='+1);
	}
	
	function add(){
		window.location.assign('<c:url value="suppliers"/>');
	}
	
	function editSuppliers(id){
		window.location.assign('<c:url value="editSuppliers"/>?id='+id);
	}

	function deleteSuppliers(id){

		 if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteImportFailureSuppliers"/>?id='+id);
		 } 
	}
	
	function validate(){
		var validFileExtensions = [".xls", ".xlsx"];
		var filePath = document.getElementById("excelFile").value;
		if(filePath == null || filePath == ""){
			alert("Please select excel file");
			return false;
		}
		else{
			var blnValid = false;
		    for (var j = 0; j < validFileExtensions.length; j++) {
		        var sCurExtension = validFileExtensions[j];
		        if (filePath.substr(filePath.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
		            blnValid = true;
		            break;
		        }
		    }
		    
		    if (!blnValid) {
		        alert("File format should be " + validFileExtensions.join(","));
		        $("#excelFile").val('');
		        return false;
		    }
		}
		return true;
	}
	$(document).ready(function () {
		document.getElementById('progress-div').style.display="none";
		$("#file-div").show();
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
			   
				
				 <c:forEach var = "supplier" items = "${supplierList}">
				 
				 if(access_Insert=='false')
		      		{
						
						 
						var link = document.getElementById("add");
						link.style.display = 'none';
						
		      		}
				if(access_View=='false')
	      		{
					
					 var ID='view_${supplier.supplier_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${supplier.supplier_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{
					
					 var ID='delete_${supplier.supplier_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
	      		}
				 </c:forEach>
				
			}
	    </c:forEach>
	    
	   $('#file-model').modal({
	    	show: true,
	   	});	
	});
	function supplierimportfailure(){
		window.location.assign('<c:url value="supplierimportfailure"/>');
	}
	function validate(){		
		var validFileExtensions = [".xls", ".xlsx"];
		var filePath = document.getElementById("excelFile").value;
		if(filePath == null || filePath == ""){
			alert("Please select excel file");
			return false;
		}
		else{
			var blnValid = false;
		    for (var j = 0; j < validFileExtensions.length; j++) {
		        var sCurExtension = validFileExtensions[j];
		        if (filePath.substr(filePath.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
		            blnValid = true;
		            break;
		        }
		    }
		    
		    if (!blnValid) {
		        alert("File format should be " + validFileExtensions.join(","));
		        $("#excelFile").val('');
		        return false;
		    }
		}
		document.getElementById('progress-div').style.display="block";
		$("#file-div").hide();
		return true;
	}
	</script>
	<%@include file="/WEB-INF/includes/footer.jsp" %>
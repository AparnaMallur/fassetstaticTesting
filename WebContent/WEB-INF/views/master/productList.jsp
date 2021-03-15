<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />

<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Product</h3>
	<a href="homePage">Home</a>» <a href="productList">Product</a>
</div>	
<div class="col-md-12" >	
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

	<div class="col-md-12 text-center">
		<button id="add"  type="button" onclick = "add()">
			Add New Product/Service
		</button>
	<c:if test="${(importflag==true)|| (role=='2') || (role=='3') || (role=='4') || (role=='7')}">	
			<button class="fassetBtn" type="button" onclick = "uploaddiv()">
						Import Excel
			</button>
		</c:if>
		<!-- <button class="fassetBtn" type="button" onclick = "uploaddiv()">
						Import Excel
			</button> -->
		<c:if test="${importfail==true}">				
		<button class="fassetBtn" type="button" onclick = "productimportfailure()">
					Failure List
		</button>
		</c:if>
		<button class="fassetBtn" type="button" onclick = "download_table_as_csv('table');">
					Download Product
		</button>
	</div>
		<div id="bulk_upload" class='row' style="display:none;">
				<div id="progress-div" style="text-align:center;display:none" >
					<img src="${processing}" style="height:80px"/>
					<h4>File uploading is in progress...</h4>
				</div>
			<div class="text-center" id="file-div">
				<form:form  action="importExcelProduct" method="post" enctype="multipart/form-data" onsubmit = "return validate();">
			    	<c:choose>
						<c:when test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">
			    		<p>Please download template from here to import <a href="resources/templates/product-template.xlsx" style='font-weight:bold'> Download </a> </p>	
			    		</c:when>
			    		<c:otherwise>
			    		<p>Please download template from here to import <a href="resources/templates/product-template-company.xlsx" style='font-weight:bold'> Download </a> </p>	
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
	<div class = "borderForm">
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
				<th  data-field="Product" data-filter-control="input" data-sortable="true" >Product</th>
				<th  data-field="Hsn/San Number" data-filter-control="input" data-sortable="true" >HSN/SAC Number</th>
				<th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	
				<th  data-field="ApprovalStatus" data-filter-control="select" data-sortable="true" >Approval Status</th>			
			</tr>
			</thead>
			<tbody>			
			<c:forEach var = "product" items = "${productList}">
				<tr>
					<td style="text-align: left;">
					 <i   id='view_${product.product_id}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"
					onclick = "viewProduct('${product.product_id}')"   class="acs-view fa fa-search" ></i>
					
					
					<c:choose>
								<c:when
									test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">
									<c:if test="${product.product_approval!=3}">

										<i id='update_${ledger[0]}'
											style="cursor: pointer; color: green; font-size: 15px; margin: 0px 3%;"
											onclick="editLedger('${ledger[0]}')"
											class="acs-update fa fa-pencil"></i>

										<i id='delete_${ledger[0]}'
											style="cursor: pointer; color: #ed1c24; font-size: 15px; margin: 0px 3%;"
											onclick="deleteLedger('${ledger[0]}')"
											class="acs-delete fa fa-times"></i>
									</c:if>
								</c:when>
								<c:otherwise>
									<i id='update_${product.product_id}'
										style="cursor: pointer; color: green; font-size: 15px; margin: 0px 3%;"
										onclick="editProduct('${product.product_id}')"
										class="acs-update fa fa-pencil"></i>
									<i id='delete_${product.product_id}'
										style="cursor: pointer; color: #ed1c24; font-size: 15px; margin: 0px 3%;"
										onclick="deleteProduct('${product.product_id}')"
										class="acs-delete fa fa-times"></i>
								</c:otherwise>
							</c:choose>
							
					</td>
					<c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">	
					<td style="text-align: left;">${product.company.company_name}</td>	
					</c:if>			
					<td style="text-align: left;">${product.product_name}</td>
					<td style="text-align: left;">${product.hsn_san_no}</td>						
					<td style="text-align: left;">${product.status==true ? "Enable" : "Disable"}</td>	
					<td style="text-align: left;">${product.product_approval == 0 ? "Pending" : product.product_approval  == 1 ? "Rejected" : product.product_approval  == 2 ? "Primary Approval" : "Approved"}</td>
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
	function viewProduct(id){
		window.location.assign('<c:url value="viewProduct"/>?id='+id+'&flag='+1);
	}
	
	function add(){
		window.location.assign('<c:url value="product"/>');
	}
	
	function editProduct(id){
		window.location.assign('<c:url value="editProduct"/>?id='+id);
	}

	
	function deleteProduct(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteProduct"/>?id='+id);
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
			   
				
				 <c:forEach var = "product" items = "${productList}">
				 
				 if(access_Insert=='false')
		      		{
						
						 
						var link = document.getElementById("add");
						link.style.display = 'none';
						
		      		}
				if(access_View=='false')
	      		{
					
					 var ID='view_${product.product_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${product.product_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{
					
					 var ID='delete_${product.product_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
	      		}
				 </c:forEach>
				
			}
	    </c:forEach>

	
				
			    
			    document.getElementById('progress-div').style.display="none";
				$("#file-div").show();	
				$("#bulk_upload").hide();
	   
	   $('#file-model').modal({
	    	show: true,
	   	});	
	});
	function uploaddiv(){
		$("#bulk_upload").show();
	}
	function hideuploaddiv(){
		$("#bulk_upload").hide();
	}
	function productimportfailure(){
		window.location.assign('<c:url value="productimportfailure"/>');
	}
	function download_table_as_csv(table_id) {
		var csv = [];
		 $.ajax({
		        type: 'POST',
		        url: 'downloadProductMaster',
		        async:false,
	            contentType: 'application/json',
			      	success: function (data){  
			      		
			      		$.each(data, function (index, payment) {
			      			 var row = [];
			             	row.push(payment.productname);
			             	 csv.push(row.join(';'));
			             	});
				        	 var csv_string = csv.join('\n');
				     		var filename = 'ProductMaster' + '.csv';
				     	    var link = document.createElement('a');
				     	    link.style.display = 'none';
				     	    link.setAttribute('target', '_blank');
				     	    link.setAttribute('href', 'data:text/csv;charset=utf-8,' + encodeURIComponent(csv_string));
				     	    link.setAttribute('download', filename);
				     	    document.body.appendChild(link);
				     	    link.click();
				     	    document.body.removeChild(link);
			      		
			      	},
			        error: function (e) {
			            console.log("ERROR: ", e);
			        },
			        done: function (e) {
			            console.log("DONE");
			        }        
		    });	  
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
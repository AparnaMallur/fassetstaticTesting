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
	<h3>Customer</h3>					
	<a href="homePage">Home</a> » <a href="customersList">Customer</a>
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
 						<button type="button" class="btn btn-default" data-dismiss="modal" onclick="cancel1()">Close</button>
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
			Add New Customer
		</button>
		<%-- <c:if test="${(importflag==true)|| (role=='2') || (role=='3') || (role=='4')}">	
        <button class="fassetBtn" type="button" onclick = "uploaddiv()">
					Import Excel
		</button>
		</c:if> --%>
		<c:if test="${(importflag==true)|| (role=='2') || (role=='3') || (role=='4') || (role=='7')}">	
		<button class="fassetBtn" type="button" onclick = "uploaddiv()">
					Import Excel
		</button>
		</c:if>
		<c:if test="${importfail==true}">		
		<button class="fassetBtn" type="button" onclick = "customerimportfailure()">
					Failure List
		</button>
		</c:if>
		<button class="fassetBtn" type="button" onclick = "download_table_as_csv('table');">
					Download Customer
		</button>
	</div>
	<div id="bulk_upload" class='row' style="display:none;">
				<div id="progress-div" style="text-align:center">
					<img src="${processing}" style="height:80px"/>
					<h4>File uploading is in progress...</h4>
				</div>
			<div class="text-center" id="file-div">
				<form:form  action="importExcelCustomer" method="post" enctype="multipart/form-data" onsubmit = "return validate();">
			    	<c:choose>
						<c:when test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">
			    		<p>Please download template from here to import <a href="resources/templates/customer-template.xlsx" style='font-weight:bold'> Download </a> </p>	
			    		</c:when>
			    		<c:otherwise>
			    		<p>Please download template from here to import <a href="resources/templates/customer-template-company.xlsx" style='font-weight:bold'> Download </a> </p>	
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
			<c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
			  <th data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>
			</c:if>	
				<th  data-field="Customer Name" data-filter-control="input" data-sortable="true" >Customer Name</th>	
				<th  data-field="Customer's Company Name" data-filter-control="input" data-sortable="true" >Customer Company Name</th>	
				<th  data-field="Mobile" data-filter-control="input" data-sortable="true" >Mobile</th>
				<th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	
				<th  data-field="ApprovalStatus" data-filter-control="select" data-sortable="true" >Approval Status</th>			
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "customer" items = "${customerList}">
			
			
		
				<tr>
					<td style="text-align: left;">
					
								<i  id='view_${customer[0]}'  style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"
								 onclick = "viewCustomer('${customer[0]}')" class="acs-view fa fa-search" ></i>
					<c:choose>
						<c:when test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">
			    	
							<c:if test="${((customer[6]==0) || (customer[6]==1))}">	
							
					
								<i id='update_${customer[0]}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;"  
								 onclick = "editCustomers('${customer[0]}')" class="acs-update fa fa-pencil" ></i>
								 
								<i  id='delete_${customer[0]}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;" 
								 onclick = "deleteCustomers('${customer[0]}')" class="acs-delete fa fa-times" ></i>	
							</c:if>
						</c:when>
						<c:otherwise>
											
								<i id='update_${customer[0]}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;"  
								 onclick = "editCustomers('${customer[0]}')" class="acs-update fa fa-pencil" ></i>
								 
								<i  id='delete_${customer[0]}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;" 
								 onclick = "deleteCustomers('${customer[0]}')" class="acs-delete fa fa-times" ></i>	
								 
						</c:otherwise>
					</c:choose>
					</td>
					<c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
					<td style="text-align: left;">${customer[1]}</td>
					</c:if>	
					<td style="text-align: left;">${customer[2]}</td>
					<td style="text-align: left;">${customer[3]}</td>
					<td style="text-align: left;">${customer[4]}</td>
					<td style="text-align: left;">${customer[5]==true ? "Enable" : "Disable"}</td>
					<td style="text-align: left;">${customer[6] == 0 ? "Pending" : customer[6] == 1 ? "Rejected" : customer[6] == 2 ? "Primary Approval" : "Approved"}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<script type="text/javascript">
$( document ).ready(function() {	
	$("#bulk_upload").hide();
	
});

function uploaddiv(){
	$("#bulk_upload").show();
}


function cancel1(){
	var failure='<c:out value="${failureList.size()}"/>';
	
	if (failure>0){
    let link = document.createElement("a");
    link.download = "ErrorFile.txt";
    link.href ="resources/templates/ErrorFile.txt";
    link.click();
	}
}
function hideuploaddiv(){
	$("#bulk_upload").hide();
}
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});
	function viewCustomer(id){
		window.location.assign('<c:url value="viewCustomer"/>?id='+id+'&flag='+1);
	}
	
	function add(){
		window.location.assign('<c:url value="customers"/>');
	}
	
	function editCustomers(id){
		window.location.assign('<c:url value="editCustomers"/>?id='+id);
	}
	
	function deleteCustomers(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteCustomers"/>?id='+id);
		 } 
	}
	
	$(document).ready(function () {
		document.getElementById('progress-div').style.display="none";
		$("#file-div").show();	
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
	   $('#file-model').modal({
	    	show: true,
	   	});	
	});
	function customerimportfailure(){
		
		window.location.assign('<c:url value="customerimportfailure"/>');
	}
	function download_table_as_csv(table_id) {
		//downloadCustomerMaster
		var csv = [];
		$.ajax({
	        type: "POST",
	        url: "downloadCustomerMaster" , 	        
	        success: function (data) {
	        	$.each(data, function (index, payment) {
               		//alert(payment.firmname);	    
               	 var row = [];
             	
             	
             	row.push(payment.firmname);
             	 csv.push(row.join(';'));
             	});
	        	 var csv_string = csv.join('\n');
	     		var filename = 'customerMaster' + '.csv';
	     	    var link = document.createElement('a');
	     	    link.style.display = 'none';
	     	    link.setAttribute('target', '_blank');
	     	    link.setAttribute('href', 'data:text/csv;charset=utf-8,' + encodeURIComponent(csv_string));
	     	    link.setAttribute('download', filename);
	     	    document.body.appendChild(link);
	     	    link.click();
	     	    document.body.removeChild(link);
	        //alert("success");
	        },
	        error: function (e) {
	            console.log("ERROR: ", e);	            
	        },
	        done: function (e) {	            
	            console.log("DONE");
	        }
	    }); 
	//alert("done ajax");
	//var schoolList=${downloadCustomerList};
//alert(schoolList.length);
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
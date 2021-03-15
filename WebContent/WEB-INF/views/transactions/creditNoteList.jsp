<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<div class="breadcrumb">
	<h3>Credit Note</h3>
	<a href="homePage">Home</a> » <a href="creditNoteList">Credit Note</a>
</div>
<div class="col-md-12">
<c:choose>
	<c:when test="${(filemsg != null)}">
				<!-- Small modal -->
			<div id="file-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	     		<div class="modal-dialog ">
	          		<div class="modal-content">
	              		<div class="modal-header">
	                   		<h4 class="modal-title" id="mySmallModalLabel">Import Status</h4>
	               		</div>
		   				<div class="modal-body">
		   				<c:if test="${successVocharList.size()==0 && failureVocharList.size()==0}">
		   				<label style="color:red;">${filemsg}</label>
		   				</c:if>
		   				
		   				<c:if test="${successVocharList.size()!=0 && failureVocharList.size()!=0}">
		   				
		   				<label style="color:green;">${filemsg}</label>
							<c:forEach var = "success" items = "${successVocharList}">
							<ul><li>${success}</li></ul>
				            
			                </c:forEach>
			              
			                <label style="color:red;">${filemsg1}</label>
			                <c:forEach var = "fail" items = "${failureVocharList}">
				            <ul><li> ${fail}</li></ul>
			            </c:forEach>
		   				</c:if>
		   				
		   				<c:if test="${successVocharList.size()!=0 && failureVocharList.size()==0}">
		   				<label style="color:green;">${filemsg}</label>
							<c:forEach var = "success" items = "${successVocharList}">
							<ul><li>${success}</li></ul>
				            
			                </c:forEach>
		   				</c:if>
		   				
		   				<c:if test="${successVocharList.size()==0 && failureVocharList.size()!=0}">
			                <label style="color:red;">${filemsg1}</label>
			                <c:forEach var = "fail" items = "${failureVocharList}">
				            <ul><li> ${fail}</li></ul>
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
<c:if test="${flagFailureList==true}">	
	<div class="col-md-12 text-center">
	
	
	<c:if test="${importflag==false}">
		<button  id="add" type="button" onclick = "add()">
				Add New Credit Note
		</button>
		</c:if>	
		<c:if test="${importflag==true}">
		<button  id="add" type="button" onclick = "checkImportCredits()">
				Add New Credit Note
		</button>
		</c:if>	
		
		<c:if test="${importflag==true}">
			<button class="fassetBtn" type="button" onclick = "uploaddiv()">
					Import Excel
		</button>
		</c:if>
	
		<%-- <c:if test="${importfail==true}">		
		<button class="fassetBtn" type="button" onclick = "creditNoteFailure()">
					Import Failure
		</button>
		</c:if> --%>
	</div>
</c:if>
	<div id="bulk_upload" class='row' style="display:none">
				<div id="progress-div" style="text-align:center;display:none">
					<img src="${processing}" style="height:80px"/>
					<h4>File uploading is in progress...</h4>
				</div>
			<div class="text-center" id="file-div">
			<form:form  action="importExcelCredit" method="post" enctype="multipart/form-data" onsubmit = "return validate();">
			    		<p>Please download template from here to import <a href="resources/templates/Credit Note Voucher-Template.xlsx" style='font-weight:bold'>Download </a> </p>	
			    			
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
				<th  data-field="voucherNumber" data-filter-control="input" data-sortable="true" >Voucher Number</th>
				<th  data-field="date" data-filter-control="input" data-sortable="true" >Date</th>
				<th  data-field="billNumber" data-filter-control="input" data-sortable="true" >Sales Bill Number</th>			
				<th  data-field="customer" data-filter-control="input" data-sortable="true" >Customer Name</th>
				<th  data-field="round_off" data-filter-control="input" data-sortable="true" >Amount</th>			
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "creditNote" items = "${creditNoteList}">
				
					<c:choose>
						<c:when test="${creditNote.entry_status==3}">
						<tr class='sales-disabled'>
							<td style="text-align: left;">
							 <i  id='view_${creditNote.credit_no_id}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"
							  onclick = "viewCreditNote('${creditNote.credit_no_id}')" class="acs-view fa fa-search" ></i>	
						</c:when>
						<c:otherwise>
						<tr>
							<td style="text-align: left;">
							 <i  id='view_${creditNote.credit_no_id}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;"
							  onclick = "viewCreditNote('${creditNote.credit_no_id}')" class="acs-view fa fa-search" ></i>	
							  
								  <c:forEach var = "year" items = "${yearEndlist}">
						   <c:if test="${creditNote.accounting_year_id.year_id==year.accountingYear.year_id && year.yearEndingstatus==1}">						
								<i  id='update_${creditNote.credit_no_id}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;"
								 onclick = "editCreditNote('${creditNote.credit_no_id}')" class="acs-update fa fa-pencil" ></i>
								 
								<i  id='delete_${creditNote.credit_no_id}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;" 
								onclick = "deleteCreditNote('${creditNote.credit_no_id}')" class="acs-delete fa fa-times" ></i>
								</c:if>
								    </c:forEach>
						</c:otherwise>
					</c:choose>												
					</td>					
					<td style="text-align: left;">${creditNote.voucher_no}</td>
					<td style="text-align: left;">
					<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    <fmt:formatDate value="${parsedDate}" var="creditDate" type="date" pattern="dd-MM-yyyy" />
					${creditDate}</td>
					<td style="text-align: left;">${creditNote.sales_bill_id.voucher_no}</td>
					<td style="text-align: left;">${creditNote.customer.firm_name}</td>
					<td style="text-align: left;">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off+creditNote.tds_amount}" />
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<c:if test="${flagFailureList==false}">	
	<div class="row" style="text-align: center; margin: 15px;">
		<button class="fassetBtn" type="button" onclick="back()">
			<spring:message code="btn_back" />
		</button>
	</div>creditNoteList
	</c:if>
</div>
<script type="text/javascript">
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide();
    }, 3000);
    $("#bulk_upload").hide();
});
	function viewCreditNote(id){
		window.location.assign('<c:url value="viewCreditNote"/>?id='+id);
	}
	
	function add(){
		importflag=false;
	
		 var opening_flag='<c:out value="${opening_flag}"/>';
		 if(opening_flag==0)
			 {
				if (confirm("Please upload your opening balance first. Press OK to add Opening Balance") == true) {
					window.location.assign('<c:url value="subledgerOpeningList"/>');
					} 
				else
					{
					return false;
					}
			 return false;
			 }
		 else
			 {
		window.location.assign('<c:url value="creditNote"/>');
			 }
	}
	
	
	 function checkImportCredits(){
		 var opening_flag='<c:out value="${opening_flag}"/>';
		 if(opening_flag==0)
			 {
				if (confirm("Please upload your opening balance first. Press OK to add Opening Balance") == true) {
					window.location.assign('<c:url value="subledgerOpeningList"/>');
					} 
				else
					{
					return false;
					}
			 return false;
			 }
		 else
			 {
						
			 if (confirm("Are you sure that you have imported all transactions.") == true) {
					window.location.assign('<c:url value="creditNote"/>');
					} 
				else
					{
					return false;
					}
			 }
		 
	 }
	
	function editCreditNote(id){
		window.location.assign('<c:url value="editCreditNote"/>?id='+id);
	}
	
	function deleteCreditNote(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
		window.location.assign('<c:url value="deleteCreditNote"/>?id='+id);
		 } 

	}
	
	function creditNoteFailure(){
		window.location.assign('<c:url value="creditNoteFailure"/>');
	}
	
	function uploaddiv(){
		 var opening_flag='<c:out value="${opening_flag}"/>';
		 if(opening_flag==0)
			 {
				if (confirm("Please upload your opening balance first. Press OK to add Opening Balance") == true) {
					window.location.assign('<c:url value="subledgerOpeningList"/>');
					} 
				else
					{
					return false;
					}
			 return false;
			 }
		 else
			 {
		$("#bulk_upload").show();
			 }
	}
	function hideuploaddiv(){
		$("#bulk_upload").hide();
	}
	
	$(document).ready(function () {
		
		
	
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
		   
			
			 <c:forEach var = "creditNote" items = "${creditNoteList}">
			 
			 if(access_Insert=='false')
	      		{
					
					 
					var link = document.getElementById("add");
					link.style.display = 'none';
					
	      		}
			if(access_View=='false')
      		{
				
				 var ID='view_${creditNote.credit_no_id}';
				var link = document.getElementById(ID);
				link.style.display = 'none';
				
      		}
			if(access_Update=='false')
      		{
				
				 var ID='update_${creditNote.credit_no_id}';
				var link = document.getElementById(ID);
				link.style.display = 'none';
				
      		}
			if(access_Delete=='false')
      		{
				
				 var ID='delete_${creditNote.credit_no_id}';
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
function back(){
	window.location.assign('<c:url value = "creditNoteList"/>');	
}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>